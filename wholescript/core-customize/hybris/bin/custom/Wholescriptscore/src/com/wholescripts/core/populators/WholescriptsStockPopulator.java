package com.wholescripts.core.populators;

import com.wholescripts.core.dao.WholescriptsCoreDao;
import com.wholescripts.core.model.CustomerMaterialInformationModel;
import com.wholescripts.core.model.WholeScriptsBrightStockProductsModel;
import com.wholescripts.core.service.WholescriptsProductsService;
import com.wholescripts.core.utils.WholescriptsCoreUtils;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commercefacades.product.converters.populator.StockPopulator;
import de.hybris.platform.commercefacades.product.data.StockData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.store.BaseStoreModel;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.Objects;

public class WholescriptsStockPopulator extends StockPopulator<ProductModel, StockData> {

    @Resource(name = "stockService")
    private StockService stockService;

    @Resource(name = "wholescriptsProductsService")
    WholescriptsProductsService wholescriptsProductsService;

    @Resource(name = "b2bCustomerService")
    private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;

    @Resource(name = "wholescriptsCoreDao")
    private WholescriptsCoreDao wholescriptsCoreDao;

    @Override
    public void populate(final ProductModel productModel, final StockData stockData) throws ConversionException
    {
        final BaseStoreModel baseStore = getBaseStoreService().getCurrentBaseStore();
        if (baseStore != null && isStockSystemEnabled(baseStore)){
            StockLevelModel stockLevel =stockService.getStockLevel(productModel, baseStore.getWarehouses().get(0));
            boolean isPrivateLabel=wholescriptsProductsService.isPrivateLabel(productModel);
            if(isPrivateLabel)
            {
                final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
                if (Objects.nonNull(customer) && Objects.nonNull(customer.getDefaultB2BUnit())) {
                    final B2BUnitModel unit = customer.getDefaultB2BUnit();
                    String accountNumber =WholescriptsCoreUtils.getFormattedDefaultB2bUnit(unit.getUid());
                    if (StringUtils.isNotEmpty(accountNumber))
                    {
                        CustomerMaterialInformationModel cmInfoModel=wholescriptsProductsService.getCustomerMaterialInfo( productModel.getCode().replaceFirst("^0+", ""),accountNumber);
                        if(Objects.nonNull(cmInfoModel))
                        {
                            StockLevelStatus stockLevelStatus= checkPrivateLabelStockStatus(stockLevel,cmInfoModel);
                            stockData.setStockLevelStatus(stockLevelStatus);
                        }
                    }

                }

            }
            else
            {
                StockLevelStatus status= WholescriptsCoreUtils.checkStatus(stockLevel);
                stockData.setStockLevelStatus(status);
            }

            if(Objects.nonNull(stockLevel) && Objects.nonNull(stockLevel.getInStockStatus()))
            {
                boolean isForceOutOfStockLevel =  InStockStatus.FORCEOUTOFSTOCK.equals(stockLevel.getInStockStatus());

                stockData.setForceOutOfStock(isForceOutOfStockLevel);
            }

        }

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
