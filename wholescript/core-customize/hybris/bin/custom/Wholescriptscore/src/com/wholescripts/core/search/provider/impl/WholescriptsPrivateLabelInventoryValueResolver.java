/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.search.provider.impl;

import com.wholescripts.core.dao.WholescriptsCoreDao;
import com.wholescripts.core.model.WholeScriptsBrightStockProductsModel;
import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commerceservices.stock.strategies.WarehouseSelectionStrategy;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.Config;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.wholescripts.core.model.CustomerMaterialInformationModel;
import com.wholescripts.core.model.SapUnitOfMeasureModel;


public class WholescriptsPrivateLabelInventoryValueResolver
		extends AbstractValueResolver<CustomerMaterialInformationModel, Object, Object>
{
	private static final String CASES = "wholescripts.prelabeled.inventory.cases";
	private static final String UNIT = "wholescripts.prelabeled.inventory.unit";
	private static final String BASE_STORE = "wholescripts.base.store";
	private static final String STOCK = "wholescripts.prelabeled.inventory.stock";
	private static final String FORCEOUTOFSTOCK = "wholescripts.inventory.force.outofstock";

	@Resource(name = "productService")
	ProductService productService;
	@Resource(name = "catalogVersionService")
	private CatalogVersionService catalogVersionService;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;
	@Resource(name = "warehouseSelectionStrategy")
	private WarehouseSelectionStrategy warehouseSelectionStrategy;
	@Resource(name = "stockService")
	private StockService stockService;
	@Resource(name = "searchRestrictionService")
	private SearchRestrictionService searchRestrictionService;
	@Resource(name = "sessionService")
	private SessionService sessionService;

	@Resource(name = "wholescriptsCoreDao")
	private WholescriptsCoreDao wholescriptsCoreDao;

	@Override
	protected void addFieldValues(final InputDocument inputDocument, final IndexerBatchContext indexerBatchContext,
			final IndexedProperty indexedProperty, final CustomerMaterialInformationModel custMatInfoModel,
			final ValueResolverContext<Object, Object> valueResolverContext) throws FieldValueProviderException
	{
		if (StringUtils.isNotEmpty(custMatInfoModel.getProductCode()))
		{
			try
			{
				final ProductModel product = sessionService.executeInLocalView(new SessionExecutionBody()
				{
					@Override
					public ProductModel execute()
					{
						searchRestrictionService.disableSearchRestrictions();
						final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(
								configurationService.getConfiguration().getString("wholescripts.product.catalog"),
								configurationService.getConfiguration().getString("wholescripts.product.catalog.version"));
						return  productService.getProductForCode(catalogVersion,
								String.format("%018d",Integer.valueOf(custMatInfoModel.getProductCode())));
					}
				});
				if (Objects.nonNull(product))
				{
					final BaseStoreModel baseStore = baseStoreService.getBaseStoreForUid(configurationService.getConfiguration().getString(BASE_STORE));
					StockLevelModel stockLevelModel =stockService.getStockLevel(product, baseStore.getWarehouses().get(0));
					StockLevelStatus stockLevelStatus = checkPrivateLabelStockStatus(stockLevelModel, custMatInfoModel);
					if (indexedProperty.getValueProviderParameter().equals(Config.getParameter(STOCK))){
						inputDocument.addField(indexedProperty, stockLevelStatus.toString(), valueResolverContext.getFieldQualifier());
					}
					if(Objects.nonNull(stockLevelModel)) {

						if(indexedProperty.getValueProviderParameter().equals(Config.getParameter(FORCEOUTOFSTOCK))&& Objects.nonNull(stockLevelModel.getInStockStatus()))
						{
								boolean isForceOutOfStockLevel =  InStockStatus.FORCEOUTOFSTOCK.equals(stockLevelModel.getInStockStatus());

								if (isForceOutOfStockLevel) {
									inputDocument.addField(indexedProperty, true, valueResolverContext.getFieldQualifier());

								}
								else {
									inputDocument.addField(indexedProperty, false, valueResolverContext.getFieldQualifier());
								}



						}
						if(StockLevelStatus.INSTOCK.equals(stockLevelStatus)
								&& CollectionUtils.isNotEmpty(product.getSapUnitOfMeasure())) {
							if (stockLevelModel.getAvailable() > stockLevelModel.getReserved()) {
								final SapUnitOfMeasureModel sapUnit = product.getSapUnitOfMeasure().stream().filter(s -> "CS".equals(s.getUnitType()) || "EA".equals(s.getUnitType()))
										.findFirst().orElse(null);
								final int cases = Objects.nonNull(sapUnit) ? (int) Math.ceil(stockLevelModel.getAvailable() / sapUnit.getConversion()) : 0;
								if (indexedProperty.getValueProviderParameter().equals(Config.getParameter(CASES))) {
									inputDocument.addField(indexedProperty, cases, valueResolverContext.getFieldQualifier());
								}
								if (indexedProperty.getValueProviderParameter().equals(Config.getParameter(UNIT))) {
									inputDocument.addField(indexedProperty, stockLevelModel.getAvailable(), valueResolverContext.getFieldQualifier());
								}
							}
							else
							{
								addDefaultUnitAndCases(inputDocument,indexedProperty,valueResolverContext);
							}
						}
						else {
							addDefaultUnitAndCases(inputDocument,indexedProperty,valueResolverContext);
						}
						}
					else {
						addDefaultUnitAndCases(inputDocument,indexedProperty,valueResolverContext);
					}
				}
			}
			catch (final Exception e)
			{
				// XXX: handle exception
			}
		}
	}

	private void addDefaultUnitAndCases(InputDocument inputDocument, IndexedProperty indexedProperty, ValueResolverContext<Object, Object> valueResolverContext) throws FieldValueProviderException {
		if (indexedProperty.getValueProviderParameter().equals(Config.getParameter(UNIT))) {
			inputDocument.addField(indexedProperty, 0, valueResolverContext.getFieldQualifier());
		}
		if (indexedProperty.getValueProviderParameter().equals(Config.getParameter(CASES))) {
			inputDocument.addField(indexedProperty, 0, valueResolverContext.getFieldQualifier());
		}
	}

	private Long totalUnit(final Collection<StockLevelModel> stockLevels)
	{
		long totalActualAmount = 0;
		for (final StockLevelModel stockLevel : stockLevels)
		{
			// If any stock level is flagged as FORCEINSTOCK then return null to indicate in stock
			if (InStockStatus.FORCEINSTOCK.equals(stockLevel.getInStockStatus()))
			{
				final long availableToSellQuantity = getAvailableToSellQuantity(stockLevel);
				if (availableToSellQuantity > 0 || !stockLevel.isTreatNegativeAsZero())
				{
					totalActualAmount += availableToSellQuantity;
				}
			}
		}
		return Long.valueOf(totalActualAmount);
	}

	protected long getAvailableToSellQuantity(final StockLevelModel stockLevel)
	{
		return stockLevel.getAvailable() - stockLevel.getReserved() + (long) stockLevel.getOverSelling();
	}
	protected  StockLevelStatus checkPrivateLabelStockStatus(StockLevelModel stockLevel,CustomerMaterialInformationModel customerMaterial) {
		StockLevelStatus resultStatus = StockLevelStatus.OUTOFSTOCK;
		if(Objects.isNull(stockLevel)) {
			if(Objects.nonNull(customerMaterial.getBrightStock())) {
				String brightStockCode = String.format("%018d", Integer.valueOf(customerMaterial.getBrightStock()));
				WholeScriptsBrightStockProductsModel brightStockModel = wholescriptsCoreDao.getBrightStockForPLProduct(brightStockCode);
				if (Objects.nonNull(brightStockModel)) {

					if(brightStockModel.getOutOfStock())
					{
						//already out of stock by default
					}
					else {
						resultStatus = StockLevelStatus.INSTOCK;
					}
				}
			}
			return resultStatus;
		} else {
			if(Objects.nonNull(stockLevel.getInStockStatus()) && !InStockStatus.NOTSPECIFIED.equals(stockLevel.getInStockStatus()))
			{
				boolean isInStockStatusLevel =  InStockStatus.FORCEINSTOCK.equals(stockLevel.getInStockStatus());
				if (isInStockStatusLevel) {
					resultStatus = StockLevelStatus.INSTOCK;
				}
			}

			else {
				if(stockLevel.getAvailable() < stockLevel.getReserved())
				{
					if(Objects.nonNull(customerMaterial.getBrightStock()))
					{
						String brightStockCode=String.format("%018d",Integer.valueOf(customerMaterial.getBrightStock()));
						WholeScriptsBrightStockProductsModel brightStockModel=wholescriptsCoreDao.getBrightStockForPLProduct(brightStockCode);
						if(Objects.nonNull(brightStockModel))
						{
							if(brightStockModel.getOutOfStock())
							{
								//already out of stock by default
							}
							else {
								resultStatus = StockLevelStatus.INSTOCK;
							}

						}

					}

				}
				else {
					resultStatus = StockLevelStatus.INSTOCK;
				}

			}

			return resultStatus;
		}

    }
}
