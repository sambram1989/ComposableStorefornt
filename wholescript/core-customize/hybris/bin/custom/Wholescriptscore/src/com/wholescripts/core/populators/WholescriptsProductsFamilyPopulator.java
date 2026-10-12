package com.wholescripts.core.populators;

import com.wholescripts.core.model.WholescriptsBrandModel;
import com.wholescripts.core.utils.WholeScriptsPriceUtils;
import com.wholescripts.data.OtherAvailableProductsData;
import com.wholescripts.data.WholescriptsProductsFamilyData;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.cmsfacades.data.MediaData;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.util.Config;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;



public class WholescriptsProductsFamilyPopulator implements Populator<ProductModel, WholescriptsProductsFamilyData> {

    private static final String SAP_BASIC_MATERIAL="wholescripts.pdp.medpax.product.prefix";

    private static final String PRODUCT_NAME = "wholescripts.pdp.productname.classification.code";

    @Resource(name = "productService")
    private ProductService productService;

    @Resource(name = "b2bCustomerService")
    private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;

    @Resource(name = "mediaModelConverter")
    private Converter<MediaModel, MediaData> mediaModelConverter;

    @Resource(name="productModelUrlResolver")
    private UrlResolver<ProductModel> productModelUrlResolver;

    @Resource(name="classificationService")
    private ClassificationService classificationService;

    @Override
    public void populate(ProductModel productModel, WholescriptsProductsFamilyData wholescriptsProductsFamilyData) throws ConversionException {
        wholescriptsProductsFamilyData.setProductName(productModel.getName());
        wholescriptsProductsFamilyData.setSku(productModel.getCode());
        WholescriptsBrandModel wholeScriptBrand = productModel.getWholescriptsBrand();
        if (Objects.nonNull(wholeScriptBrand)) {
            wholescriptsProductsFamilyData.setBrandName(wholeScriptBrand.getName());
            wholescriptsProductsFamilyData.setBrandUrl(wholeScriptBrand.getBrandUrl());
        }

        wholescriptsProductsFamilyData.setDescription(productModel.getDescription());
        wholescriptsProductsFamilyData.setSummary(productModel.getSummary());

        if(productModel.getPicture()!=null)
        {
            wholescriptsProductsFamilyData.setImage(mediaModelConverter.convert(productModel.getPicture()));

        }
        wholescriptsProductsFamilyData.setProductUrl(productModelUrlResolver.resolve(productModel));

        B2BUnitModel b2bUnit = getB2BUnitForCustomer();
        if (WholeScriptsPriceUtils.isRetailCustomer(b2bUnit)) {
            double retailsPrice = WholeScriptsPriceUtils.getRetailPrice(productModel, b2bUnit);
            double yourPrice = WholeScriptsPriceUtils.getRetailPriceAfterDiscount(productModel, b2bUnit, retailsPrice);
            wholescriptsProductsFamilyData.setYourPrice(yourPrice);

        } else if (WholeScriptsPriceUtils.isWholeSaleCustomer(b2bUnit)) {
            double yourPrice = WholeScriptsPriceUtils.getWholesalePriceAfterDiscount(productModel, b2bUnit);
            wholescriptsProductsFamilyData.setYourPrice(yourPrice);
        }
        double mapPrice = WholeScriptsPriceUtils.getMAPPrice(productModel);
        wholescriptsProductsFamilyData.setMapPrice(mapPrice);

        //for MedPax Product:
        List<OtherAvailableProductsData> alsoAvailablProducts=new ArrayList<>();
        String sapBasicMaterial=productModel.getSapBasicMaterial();
        if(StringUtils.isNotBlank(sapBasicMaterial))
        {
            String productCode= Config.getParameter(SAP_BASIC_MATERIAL)+ sapBasicMaterial;
            OtherAvailableProductsData otherProducts=new OtherAvailableProductsData();
            try
            {
                ProductModel product=productService.getProductForCode(productCode);
                final FeatureList featureList = classificationService.getFeatures(product);
                if(Objects.nonNull(product) &&  !productCode.equals(productModel.getCode()))
                {
                    boolean isMedPax=product.getIsMedpax();
                    if(isMedPax)
                    {
                        otherProducts.setProductDisplyName("MedPax");
                        String productName = getProductName(featureList);
                        if(StringUtils.isNotBlank(productName)) {
                            otherProducts.setProductUrl("?q=\"" + productName + "\"");
                        }else{
                            otherProducts.setProductUrl("?q=\"" + product.getName() + "\"");
                        }
                        alsoAvailablProducts.add(otherProducts);
                    }
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
            if (CollectionUtils.isNotEmpty(alsoAvailablProducts)) {
                wholescriptsProductsFamilyData.setOtherAvailableProducts(alsoAvailablProducts);
            }
        }
    }

    B2BUnitModel getB2BUnitForCustomer() {
        final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
        if (Objects.nonNull(customer)) {
            return customer.getDefaultB2BUnit();
        }
        return null;
    }

    private String getProductName(FeatureList featureList){
        final Feature productName = featureList.getFeatureByCode(Config.getParameter(PRODUCT_NAME));
        String productNameData = "";
        if (Objects.nonNull(productName)) {
            final FeatureValue productNameValue = productName.getValue();
            if (Objects.nonNull(productNameValue)) {
                final Object attributeValue = productNameValue.getValue();
                productNameData = attributeValue.toString();
            }
        }
        return productNameData;
    }
}
