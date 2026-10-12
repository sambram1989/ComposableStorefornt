/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.search.provider.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
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

import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.wholescripts.core.model.CustomerMaterialInformationModel;
import com.wholescripts.core.model.SapUnitOfMeasureModel;

/**
 *
 */
public class WholescriptsPrivateLabelCaseQtyValueResolver extends AbstractValueResolver<CustomerMaterialInformationModel, Object, Object>
{
	@Resource(name = "productService")
	ProductService productService;
	@Resource(name = "catalogVersionService")
	private CatalogVersionService catalogVersionService;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = "searchRestrictionService")
	private SearchRestrictionService searchRestrictionService;
	@Resource(name = "sessionService")
	private SessionService sessionService;

	@Override
	protected void addFieldValues(InputDocument inputDocument, IndexerBatchContext indexerBatchContext, IndexedProperty indexedProperty, CustomerMaterialInformationModel custMatInfoModel,
			ValueResolverContext<Object, Object> valueResolverContext) throws FieldValueProviderException
	{
		if(StringUtils.isNotEmpty(custMatInfoModel.getProductCode())) {
			try {
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
				if(Objects.nonNull(product) && Objects.nonNull(product.getUnit()) && CollectionUtils.isNotEmpty(product.getSapUnitOfMeasure())) {
					SapUnitOfMeasureModel sapUnit = product.getSapUnitOfMeasure().stream().filter(s -> "CS".equals(s.getUnitType()) || "EA".equals(s.getUnitType())).findFirst().orElse(null);
					if(Objects.nonNull(sapUnit)) {
						inputDocument.addField(indexedProperty, sapUnit.getConversion(), valueResolverContext.getFieldQualifier());
					}
				}
			}
			catch (Exception e) {
				// XXX: handle exception
			}
		}
	}
}
