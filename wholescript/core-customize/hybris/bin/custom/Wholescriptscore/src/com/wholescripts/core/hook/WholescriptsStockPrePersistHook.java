package com.wholescripts.core.hook;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commerceservices.stock.strategies.WarehouseSelectionStrategy;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.inboundservices.persistence.PersistenceContext;
import de.hybris.platform.inboundservices.persistence.hook.PrePersistHook;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;

public class WholescriptsStockPrePersistHook implements PrePersistHook {

    private static final Logger LOG = LoggerFactory.getLogger(WholescriptsStockPrePersistHook.class);


    private static final String BASE_STORE = "wholescripts.base.store";

    @Resource(name = "productService")
    private ProductService productService;

    @Resource(name = "configurationService")
    private ConfigurationService configurationService;
    @Resource(name = "baseStoreService")
    private BaseStoreService baseStoreService;
    @Resource(name = "warehouseSelectionStrategy")
    private WarehouseSelectionStrategy warehouseSelectionStrategy;
    @Resource(name = "stockService")
    private StockService stockService;

    @Resource(name = "catalogVersionService")
    private CatalogVersionService catalogVersionService;

    @Resource(name = "sessionService")
    private SessionService sessionService;
    @Resource(name = "searchRestrictionService")
    private SearchRestrictionService searchRestrictionService;

    @Override
    public Optional<ItemModel> execute(ItemModel item, PersistenceContext context) {

        LOG.info("Started executing  WholescriptsStockPrePersistHook");
        if (item instanceof StockLevelModel stockLevelModel)
        {
            try{
                LOG.info("Started executing Try Block of   WholescriptsStockPrePersistHook");

                final ProductModel product = sessionService.executeInLocalView(new SessionExecutionBody()
                {
                    @Override
                    public ProductModel execute()
                    {
                        searchRestrictionService.disableSearchRestrictions();
                        final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(
                                configurationService.getConfiguration().getString("wholescripts.product.catalog"),
                                configurationService.getConfiguration().getString("wholescripts.product.catalog.version"));
                        return  productService.getProductForCode(catalogVersion,stockLevelModel.getProductCode());
                    }
                });
                final BaseStoreModel baseStore = baseStoreService.getBaseStoreForUid(configurationService.getConfiguration().getString(BASE_STORE));
                final StockLevelModel stockLevel =
                        stockService.getStockLevel(product, baseStore.getWarehouses().get(0));
                LOG.info("Started executing  Product and stock check block");
                if(Objects.nonNull(product))
                {
                    if(Objects.nonNull(stockLevel))
                    {
                        LOG.info("Stock Level is available for the Product" + product.getCode());
                        if(stockLevel.getReserved()<=0)
                        {
                            LOG.info("Updating the Reserved Quantity for the Product" + product.getCode());
                            if(product.getIsMedpax())
                            {
                                stockLevelModel.setReserved(200);

                            }
                            else
                            {
                                stockLevelModel.setReserved(1);
                            }
                        }

                    }
                    else
                    {
                        LOG.info("Stock Level is not available for the Product" + product.getCode());
                        LOG.info("Updating the Reserved Quantity for the Product" + product.getCode());
                        if(product.getIsMedpax())
                        {
                            stockLevelModel.setReserved(200);

                        }
                        else
                        {
                            stockLevelModel.setReserved(1);
                        }
                    }


                }
            } catch (Exception e) {
                LOG.info("Exception Occurred While executing WholescriptsStockPrePersistHook"+ e.getMessage());

            }
        }
        return Optional.of(item);
    }
}
