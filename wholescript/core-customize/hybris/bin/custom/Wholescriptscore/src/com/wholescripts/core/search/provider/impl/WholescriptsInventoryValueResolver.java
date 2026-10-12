package com.wholescripts.core.search.provider.impl;

import com.wholescripts.core.model.CustomerMaterialInformationModel;
import com.wholescripts.core.utils.WholescriptsCoreUtils;
import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.commerceservices.stock.strategies.CommerceAvailabilityCalculationStrategy;
import de.hybris.platform.commerceservices.stock.strategies.WarehouseSelectionStrategy;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.Config;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Objects;

public class WholescriptsInventoryValueResolver extends AbstractValueResolver<ProductModel, Object, Object> {

    private static final String BASE_STORE = "wholescripts.base.store";
    private static final String FORCEOUTOFSTOCK = "wholescripts.inventory.force.outofstock";
    private static final String STOCK = "wholescripts.prelabeled.inventory.stock";

    @Resource(name = "configurationService")
    private ConfigurationService configurationService;
    @Resource(name = "baseStoreService")
    private BaseStoreService baseStoreService;

    @Resource(name = "warehouseSelectionStrategy")
    private WarehouseSelectionStrategy warehouseSelectionStrategy;
    @Resource(name = "stockService")
    private StockService stockService;

    @Resource(name = "commerceStockService")
    private CommerceStockService commerceStockService;

    @Resource(name="defaultCommerceStockLevelCalculationStrategy")
    private CommerceAvailabilityCalculationStrategy commerceStockLevelCalculationStrategy;


    @Override
    protected void addFieldValues(InputDocument inputDocument, IndexerBatchContext indexerBatchContext, IndexedProperty indexedProperty, ProductModel productModel, ValueResolverContext<Object, Object> valueResolverContext) throws FieldValueProviderException {
        if(Objects.nonNull(productModel))
        {
            final BaseStoreModel baseStore = baseStoreService.getBaseStoreForUid(configurationService.getConfiguration().getString(BASE_STORE));
            if (baseStore != null && commerceStockService.isStockSystemEnabled(baseStore)){
                StockLevelModel stockLevel =stockService.getStockLevel(productModel, baseStore.getWarehouses().get(0));
                StockLevelStatus status=WholescriptsCoreUtils.checkStatus(stockLevel);
                if(indexedProperty.getValueProviderParameter().equals(Config.getParameter(STOCK)))
                {
                    inputDocument.addField(indexedProperty, status.toString(), valueResolverContext.getFieldQualifier());

                }
                if(Objects.nonNull(stockLevel)) {
                    if(indexedProperty.getValueProviderParameter().equals(Config.getParameter(FORCEOUTOFSTOCK)) && Objects.nonNull(stockLevel.getInStockStatus()))
                    {
                        boolean isForceOutOfStockLevel =  InStockStatus.FORCEOUTOFSTOCK.equals(stockLevel.getInStockStatus());

                        if (isForceOutOfStockLevel) {
                            inputDocument.addField(indexedProperty, true, valueResolverContext.getFieldQualifier());

                        }
                        else {
                            inputDocument.addField(indexedProperty, false, valueResolverContext.getFieldQualifier());
                        }

                    }
                }


            }

        }

    }
}
