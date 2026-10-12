package com.wholescripts.core.utils;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commerceservices.price.CommercePriceService;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.util.Config;
import org.apache.commons.collections.CollectionUtils;
import org.junit.platform.commons.util.StringUtils;

import javax.annotation.Resource;
import java.util.Objects;

;

public class WholeScriptsPriceUtils {


    private static final String RETAIL_GROUP = "wholescripts.retail.group";
    private static final String WHOLESALE_GROUP = "wholescripts.wholesale.group";
    private static final String GUEST_GROUP = "wholescripts.guest.group";

    @Resource(name = "configurationService")
    private ConfigurationService configurationService;

    public static double getRetailPrice(final ProductModel product, B2BUnitModel currentB2bUnit) {
        double salesPrice = 0.0;
        if (Objects.nonNull(currentB2bUnit) && Objects.nonNull(currentB2bUnit.getUserPriceGroup())) {
            String customerPriceGroup = Config.getParameter(RETAIL_GROUP);
            if (currentB2bUnit.getUserPriceGroup().getCode().equals(customerPriceGroup)) {
                PriceRowModel priceRow = product.getEurope1Prices().stream().filter(pr ->pr.getUg()!=null && customerPriceGroup.equalsIgnoreCase(pr.getUg().getCode())).findAny().orElse(null);
                if (Objects.nonNull(priceRow)) {
                    salesPrice = priceRow.getPrice();
                }
            }
        }

        return salesPrice;
    }

    public static double getWholeSalePrice(final ProductModel product, B2BUnitModel currentB2bUnit) {
        double salesPrice = 0.0;
        if (Objects.nonNull(currentB2bUnit) && Objects.nonNull(currentB2bUnit.getUserPriceGroup())) {
            String customerPriceGroup = Config.getParameter(WHOLESALE_GROUP);
            if (currentB2bUnit.getUserPriceGroup().getCode().equals(customerPriceGroup)) {
                if(CollectionUtils.isNotEmpty(product.getEurope1Prices()) && StringUtils.isNotBlank(customerPriceGroup))
                {
                    PriceRowModel priceRow = product.getEurope1Prices().stream().filter(pr ->pr.getUg()!=null &&  customerPriceGroup.equalsIgnoreCase(pr.getUg().getCode())).findAny().orElse(null);
                    if (Objects.nonNull(priceRow)) {
                        salesPrice = priceRow.getPrice();
                    }
                }

            }
        }

        return salesPrice;
    }

    public static double getMAPPrice(final ProductModel Price) {

        double salesPrice = 0.0;
            String customerPriceGroup = Config.getParameter(GUEST_GROUP);
            if(CollectionUtils.isNotEmpty(Price.getEurope1Prices()) && StringUtils.isNotBlank(customerPriceGroup))
            {
                PriceRowModel priceRow = Price.getEurope1Prices().stream().filter(pr -> pr.getUg()!=null && customerPriceGroup.equalsIgnoreCase(pr.getUg().getCode())).findAny().orElse(null);
                if (Objects.nonNull(priceRow)) {
                    salesPrice = priceRow.getPrice();
                }

            }

        return salesPrice;
    }


    public static double getRetailPriceAfterDiscount(final ProductModel product, B2BUnitModel b2bUnit, double retailPrice) {
        try {
            if (Objects.nonNull(b2bUnit)) {
                if (StringUtils.isNotBlank(b2bUnit.getDiscount())) {
                    double percentValue = Double.parseDouble(b2bUnit.getDiscount());
                    double discountValue = (percentValue * retailPrice) / 100;
                    return retailPrice - discountValue;
                }
                else if (b2bUnit.getDiscount() == null && b2bUnit.getSapCustomerPriceGroup() != null) {
                    String customerGroup = String.valueOf(b2bUnit.getSapCustomerPriceGroup());
                    String discountProperty = Config.getParameter("sapCustomerPriceGroup." + customerGroup + ".discount");
                    if (StringUtils.isNotBlank(discountProperty)) {
                        double sapDiscount = Double.parseDouble(discountProperty);
                        double discountValue = (sapDiscount * retailPrice) / 100;
                        return retailPrice - discountValue;
                    }
                }
            }
        } catch (NumberFormatException e) {

        }
        return retailPrice;
    }


    public static double getYourPriceInPLP(final Double salePrice, B2BUnitModel b2bUnit) {

        if (b2bUnit != null) {
            if (StringUtils.isNotBlank(b2bUnit.getDiscount())) {
                double percentValue = Double.parseDouble(b2bUnit.getDiscount());
                double discountValue = (percentValue * salePrice) / 100;
                return salePrice - discountValue;
            }
            else if (b2bUnit.getDiscount() == null && b2bUnit.getSapCustomerPriceGroup() != null) {
                String customerGroup = String.valueOf(b2bUnit.getSapCustomerPriceGroup());
                String discountProperty = Config.getParameter("sapCustomerPriceGroup." + customerGroup + ".discount");

                if (StringUtils.isNotBlank(discountProperty)) {
                    double sapDiscount = Double.parseDouble(discountProperty);
                    double discountValue = (sapDiscount * salePrice) / 100;
                    return salePrice - discountValue;
                }
            }
        }
        return salePrice;

    }

    public static double getWholesalePriceAfterDiscount(final ProductModel product, B2BUnitModel b2bUnit){
            double wholeSalePrice = 0.0;
            try {
                wholeSalePrice = getWholeSalePrice(product, b2bUnit);
                    if (Objects.nonNull(b2bUnit)) {
                        if (StringUtils.isNotBlank(b2bUnit.getDiscount())) {
                            double percentValue = Double.parseDouble(b2bUnit.getDiscount());
                            double discountValue = (percentValue * wholeSalePrice) / 100;
                            return wholeSalePrice - discountValue;
                        } else if (b2bUnit.getDiscount() == null && b2bUnit.getSapCustomerPriceGroup() != null) {
                            String customerGroup = String.valueOf(b2bUnit.getSapCustomerPriceGroup());
                            String discountProperty = Config.getParameter("sapCustomerPriceGroup." + customerGroup + ".discount");

                            if (StringUtils.isNotBlank(discountProperty)) {
                                double sapDiscount = Double.parseDouble(discountProperty);
                                double discountValue = (sapDiscount * wholeSalePrice) / 100;
                                return wholeSalePrice - discountValue;
                            }
                        }
                    }
                } catch(Exception e){
                }

                return wholeSalePrice;
            }

    public static PriceInformation getProductPriceInformation(ProductModel product) {
        PriceInformation info = null;
        PriceDataType priceType = getProductPriceType(product);
        CommercePriceService commercePriceService = (CommercePriceService) Registry.getApplicationContext().getBean("commercePriceService");
        if (priceType.equals(PriceDataType.BUY)) {
            info = commercePriceService.getWebPriceForProduct(product);
        } else if (priceType.equals(PriceDataType.FROM)) {
            info = commercePriceService.getFromPriceForProduct(product);
        }
        return info;
    }

    public static PriceDataType getProductPriceType(ProductModel product) {
        final PriceDataType priceType;
        if (CollectionUtils.isEmpty(product.getVariants())) {
            priceType = PriceDataType.BUY;
        } else {
            priceType = PriceDataType.FROM;
        }
        return priceType;
    }

    public static boolean isRetailCustomer(B2BUnitModel currentB2bUnit) {
        if (Objects.nonNull(currentB2bUnit) && Objects.nonNull(currentB2bUnit.getUserPriceGroup())) {
            String customerPriceGroup = Config.getParameter(RETAIL_GROUP);
            return currentB2bUnit.getUserPriceGroup().getCode().equals(customerPriceGroup);
        }

        return false;
    }
    public static boolean isWholeSaleCustomer(B2BUnitModel currentB2bUnit) {
        if (Objects.nonNull(currentB2bUnit) && Objects.nonNull(currentB2bUnit.getUserPriceGroup())) {
            String customerPriceGroup = Config.getParameter(WHOLESALE_GROUP);
            return currentB2bUnit.getUserPriceGroup().getCode().equals(customerPriceGroup);
        }

        return false;
    }
    public static double getWholeSalePrice(final ProductModel product) {
        double salesPrice = 0.0;
            String customerPriceGroup = Config.getParameter(WHOLESALE_GROUP);
                if(CollectionUtils.isNotEmpty(product.getEurope1Prices()) && StringUtils.isNotBlank(customerPriceGroup))
                {
                    PriceRowModel priceRow = product.getEurope1Prices().stream().filter(pr ->pr.getUg()!=null &&  customerPriceGroup.equalsIgnoreCase(pr.getUg().getCode())).findAny().orElse(null);
                    if (Objects.nonNull(priceRow)) {
                        salesPrice = priceRow.getPrice();
                    }
                }

        return salesPrice;
    }
    public static double getRetailPrice(final ProductModel product) {
        double salesPrice = 0.0;
        String customerPriceGroup = Config.getParameter(RETAIL_GROUP);
        if(CollectionUtils.isNotEmpty(product.getEurope1Prices()) && StringUtils.isNotBlank(customerPriceGroup))
        {
            PriceRowModel priceRow = product.getEurope1Prices().stream().filter(pr ->pr.getUg()!=null &&  customerPriceGroup.equalsIgnoreCase(pr.getUg().getCode())).findAny().orElse(null);
            if (Objects.nonNull(priceRow)) {
                salesPrice = priceRow.getPrice();
            }
        }

        return salesPrice;
    }
}
