package com.wholescripts.core.populators;

import com.wholescripts.core.constants.WholescriptscoreConstants;
import com.wholescripts.core.dao.WholescriptsProductsDao;
import com.wholescripts.core.model.CustomerMaterialInformationModel;
import com.wholescripts.core.service.WholescriptsCoreService;
import com.wholescripts.core.service.WholescriptsProductsService;
import com.wholescripts.core.utils.WholeScriptsPriceUtils;
import com.wholescripts.core.utils.WholescriptsCoreUtils;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.converters.populator.SearchResultProductPopulator;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import de.hybris.platform.util.Config;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class WholeScriptsSearchResultProductPopulator extends SearchResultProductPopulator {

    private final static Logger LOG = Logger.getLogger(WholeScriptsSearchResultProductPopulator.class);
    private static final String MEDIA_URL="media.wholescripts.https";
    private static final String IMAGE_TYPE_PRODUCT = "product";
    private static final String BLUE_LABEL_CODE = "wholescripts.blue.label.code";
    private static final String MASTER_VARIANT="wholescripts.pdp.mastervariant.classification.code";
    private static final String SAP_BASIC_MATERIAL="wholescripts.pdp.medpax.product.prefix";
    private static final String PRODUCT_NAME = "wholescripts.pdp.productname.classification.code";

    @Resource(name = "b2bCustomerService")
    private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;

    @Resource(name = "baseStoreService")
    private BaseStoreService baseStoreService;

    @Resource(name = "priceDataFactory")
    private PriceDataFactory priceDataFactory;
    @Resource(name = "productService")
    private ProductService productService;
    @Resource(name = "wholescriptsCoreService")
    private WholescriptsCoreService wholescriptsCoreService;
    @Resource(name = "wholescriptsProductsDao")
    private WholescriptsProductsDao wholescriptsProductsDao;
    @Resource(name = "configurationService")
    private ConfigurationService configurationService;
    @Resource(name="classificationService")
    private ClassificationService classificationService;

    @Resource(name = "sessionService")
    private SessionService sessionService;
    
    @Resource(name = "wholescriptsProductsService")
    private WholescriptsProductsService wholescriptsProductsService;
    
    @Override
    public void populate(SearchResultValueData source, ProductData target) {

        final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
        final Double wholeSalePrice =  getValue(source, "wholeSalePrice");
        final Double retailsPrice = getValue(source, "retailPrice");
        final Double mapPrice =  getValue(source, "mapPrice");
        final BaseStoreModel baseStore = baseStoreService.getCurrentBaseStore();

        if (Objects.nonNull(customer)) {
         B2BUnitModel   defaultUnit=  customer.getDefaultB2BUnit();
            if(WholeScriptsPriceUtils.isWholeSaleCustomer(defaultUnit)) {
                if (wholeSalePrice != null) {
                    final PriceData priceData = priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(wholeSalePrice),
                            baseStore.getDefaultCurrency());
                    target.setWholeSalePrice(priceData);
                double yourPrice = WholeScriptsPriceUtils.getYourPriceInPLP(wholeSalePrice, defaultUnit);
                final PriceData yourPriceData = priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(yourPrice),
                        baseStore.getDefaultCurrency());
                target.setYourPrice(yourPriceData);
            }
                if (retailsPrice != null)
                {
                    final PriceData priceData = priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(retailsPrice),
                            baseStore.getDefaultCurrency());
                   target.setRetailPrice(priceData);
                }
                if(mapPrice!=null)
                {
                    calculateMapPrice(mapPrice,target,baseStore);

                }

            }
            else if(WholeScriptsPriceUtils.isRetailCustomer(defaultUnit))
            {
                if (retailsPrice != null) {
                    final PriceData priceData = priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(retailsPrice),
                            baseStore.getDefaultCurrency());
                    target.setRetailPrice(priceData);

                    double yourPrice = WholeScriptsPriceUtils.getYourPriceInPLP(retailsPrice, defaultUnit);
                    final PriceData yourPriceData = priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(yourPrice),
                            baseStore.getDefaultCurrency());
                    target.setYourPrice(yourPriceData);
                }
                if (mapPrice != null)
                {
                    calculateMapPrice(mapPrice,target,baseStore);
                }

            }
            else {
                if(mapPrice!=null)
                {
                    calculateMapPrice(mapPrice,target,baseStore);

                }
            }
        }
        else {
            if(mapPrice!=null)
            {
                calculateMapPrice(mapPrice,target,baseStore);

            }
        }
        target.setIsFeatured(this.<Boolean> getValue(source, "isFeatured"));
        target.setIsNew(this.<Boolean> getValue(source, "isNew"));
        target.setMaterialProductForm(this.<String> getValue(source, "materialProductForm"));
        target.setInAM(this.<String> getValue(source, "inAM"));
        target.setInPM(this.<String> getValue(source, "inPM"));
        target.setInDaily(this.<String> getValue(source, "inDaily"));
        target.setInBedtime(this.<String> getValue(source, "inBedtime"));
        target.setProductName(this.<String> getValue(source, "productName"));
        
        final String bestSellersCategory = sessionService.getAttribute(WholescriptscoreConstants.BESTSELLER_SESSION_FIELD);
        
        if (StringUtils.isNotBlank(bestSellersCategory)) {
      	  
      	  final Boolean isBestSeller = this.<Boolean> getValue(source, WholescriptscoreConstants.BESTSELLER_SORT_FIELD
      			  .concat(WholescriptscoreConstants.UNDERSCORE).concat(bestSellersCategory)
      			  .concat(WholescriptscoreConstants.UNDERSCORE).concat(WholescriptscoreConstants.BESTSELLER_SORT_FIELD_TYPE));
           if(isBestSeller != null && isBestSeller) {
         	  target.setIsBestSeller(isBestSeller);
           }
        }
        
        target.setBrandName(this.<String> getBrandName(source, "brandName"));
        target.setIsMedpax(this.<Boolean> getValue(source, "isMedpax"));
        target.setBrandUrl(this.<String> getValue(source, "brandUrl"));
        target.setBrandCode(this.<String> getValue(source, "brandCode"));
        
        target.setShortDescription(this.<String> getValue(source, "shortDescription"));
        String productCode =this.<String> getValue(source, "code");
        if(StringUtils.isNotEmpty(productCode)) {
      	CustomerMaterialInformationModel customerInfo =  wholescriptsCoreService.getCustomerInfoModelForAccountAndBlueLabel(productCode);
          if(Objects.nonNull(customerInfo)) {
         	 target.setPrivateLabelDescription(customerInfo.getProductDescription());
          }
        }

        String sapBasicMaterial = "";
        if (StringUtils.isNotEmpty(productCode)) {
            ProductModel productModel = productService.getProductForCode(productCode);
            sapBasicMaterial = productModel.getSapBasicMaterial();
            if (Boolean.TRUE.equals(productModel.getIsMedpax())) {
                setMedPaxImage(productModel, target);
            }
        }

        String pCode = Config.getParameter(SAP_BASIC_MATERIAL)+ sapBasicMaterial;
        try{
            ProductModel product = productService.getProductForCode(pCode);
            final FeatureList featureList = classificationService.getFeatures(product);
            if(Objects.nonNull(product) &&  !productCode.equals(product.getCode())){
                boolean isMedPax = product.getIsMedpax();
                if(isMedPax)
                {
                    String productName = getProductName(featureList);
                    if(StringUtils.isNotBlank(productName)) {
                        target.setMedPaxUrl("?q=\"" + productName + "\"");
                    }else{
                        target.setMedPaxUrl("?q=\"" + product.getName() + "\"");
                    }
                }else {
                    target.setMedPaxUrl(StringUtils.EMPTY);
                }
            }

        }catch (Exception e){
            LOG.error(e.getMessage());
        }

        target.setForceOutOfStock(this.<Boolean> getValue(source, "forceOutOfStock"));
        populateGenericPLName(source, target);
    }


    private void calculateMapPrice(Double mapPrice, ProductData target, BaseStoreModel baseStore) {
        final PriceData priceData = priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(mapPrice),
                baseStore.getDefaultCurrency());
        target.setMapPrice(priceData);
    }
    private void calculateRetailPrice(Double retailsPrice, ProductData target, BaseStoreModel baseStore) {
        final PriceData priceData = priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(retailsPrice),
                baseStore.getDefaultCurrency());
       target.setRetailPrice(priceData);
    }
    protected String getBrandName(final SearchResultValueData source, final String propertyName)
    {
        if (source.getValues() == null)
        {
            return null;
        }
        if(Objects.nonNull(source.getValues().get(propertyName)))
        {
            List<String> brands= (List<String>) source.getValues().get(propertyName);
            if (!brands.isEmpty()) {
                return brands.get(0);
            }
        }

        return null;
    }

    private void setMedPaxImage(ProductModel productModel, ProductData productData) {
        boolean isMedpax = Boolean.TRUE.equals(productModel.getIsMedpax());

        if (isMedpax) {
            List<ProductModel> blueLabelProducts = wholescriptsProductsDao.getProductsByMedpaxCriteria(
                    configurationService.getConfiguration().getString(BLUE_LABEL_CODE),
                    productModel.getSapBasicMaterial(),
                    productModel.getCatalogVersion());
            if (CollectionUtils.isNotEmpty(blueLabelProducts)) {
                for (ProductModel blueLabelProduct : blueLabelProducts) {

                    if (!blueLabelProduct.getCode().equals(productModel.getCode())) {

                        ProductModel masterVariantProduct = getMasterVariantProduct(blueLabelProduct);
                        if (masterVariantProduct != null) {
                            MediaModel productImage = masterVariantProduct.getPicture();
                            if (productImage != null) {
                                ImageData medPaxImage = createImageData(productImage, masterVariantProduct.getName());
                                productData.setMedPaxImage(medPaxImage);
                                return;
                            }
                        }
                    }
                }

                // If no Master Variant Image, use the first available Blue Label product image
                for (ProductModel blueLabelProduct : blueLabelProducts) {
                    if (!blueLabelProduct.getCode().equals(productModel.getCode())) {
                        MediaModel productImage = blueLabelProduct.getPicture();
                        if (productImage != null) {
                            ImageData medPaxImage = createImageData(productImage, blueLabelProduct.getName());
                            productData.setMedPaxImage(medPaxImage);
                            return;
                        }
                    }
                }
            }
        }
    }

    private ImageData createImageData(MediaModel mediaModel, String altText) {
        ImageData imageData = new ImageData();
        String mediaUrl = configurationService.getConfiguration().getString(MEDIA_URL) + mediaModel.getURL();
        imageData.setUrl(mediaUrl);
        imageData.setAltText(altText);
        imageData.setFormat(IMAGE_TYPE_PRODUCT);
        return imageData;
    }

    private ProductModel getMasterVariantProduct(ProductModel blueLabelProduct) {
        FeatureList featureList = classificationService.getFeatures(blueLabelProduct);
        if (featureList != null) {
            Feature feature = featureList.getFeatureByCode(Config.getParameter(MASTER_VARIANT));
            if (feature != null && feature.getValue() != null) {
                String masterVariant = (String) feature.getValue().getValue();
                if (StringUtils.isNotBlank(masterVariant)) {
                    return productService.getProductForCode(masterVariant);
                }
            }
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
    
    protected void populateGenericPLName(final SearchResultValueData source, final ProductData target) {
   	 
   	 final String genericPLName = wholescriptsProductsService.getGenericPLNameFromCMI(target.getCode());
   	 
   	 if (StringUtils.isNotBlank(genericPLName)) {
   		 target.setGenericPLName(genericPLName);
   	 } else {
   		 target.setGenericPLName(this.<String> getValue(source, "genericPLName"));
   	 }
 	}
    
    
}
