package com.wholescripts.core.populators;

import com.wholescripts.core.model.*;
import com.wholescripts.core.utils.WholescriptsCoreUtils;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.product.CommerceProductService;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.Config;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.wholescripts.core.dao.WholescriptsCartValidationDao;
import com.wholescripts.core.dao.WholescriptsProductsDao;
import com.wholescripts.core.service.WholescriptsPopularCategoriesService;
import com.wholescripts.core.service.WholescriptsProductsService;
import com.wholescripts.core.service.WholescriptsUserService;
import com.wholescripts.core.utils.WholeScriptsPriceUtils;
import com.wholescripts.data.OtherAvailableProductsData;
import com.wholescripts.data.SupportedAutoShipData;

public class WholeScriptsProductPopulator implements Populator<ProductModel, ProductData> {

    private final static Logger LOG = Logger.getLogger(WholeScriptsProductPopulator.class);
    private static final String AM_DOSING = "wholescripts.dosage.am.classification.code";
    private static final String PM_DOSING = "wholescripts.dosage.pm.classification.code";
    private static final String DAILY_DOSING = "wholescripts.dosage.daily.classification.code";
    private static final String BEDTIME_DOSING = "wholescripts.dosage.bedtime.classification.code";
    private static final List<String> PRIVATE_LABEL_CODES = Arrays.asList("1000014", "1001024");
    private static final String MEDIA_URL="media.wholescripts.https";
    private static final String IMAGE_TYPE_PRODUCT = "product";

    @Resource(name = "priceDataFactory")
    private PriceDataFactory priceDataFactory;

    @Resource(name = "b2bCustomerService")
    private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;

    @Resource(name = "userService")
    private UserService userService;

    @Resource
    private BaseStoreService baseStoreService;

    @Resource(name = "wholescriptsPopularCategoriesService")
    private WholescriptsPopularCategoriesService wholescriptsPopularCategoriesService;
    
    @Resource(name="commerceProductService")
    private CommerceProductService commerceProductService;

    @Resource(name = "productService")
    private ProductService productService;

    @Resource(name="classificationService")
    private ClassificationService classificationService;

    @Resource(name="productModelUrlResolver")
    private UrlResolver<ProductModel> productModelUrlResolver;

    @Resource(name="imageConverter")
    private Converter<MediaModel, ImageData> imageConverter;
    
    @Resource(name="wsAutoShipFrequnceyConverter")
    private Converter<SupportedAutoShipModel, SupportedAutoShipData> frequencyConveter;

    @Resource(name = "wholescriptsCartValidationDao")
    private WholescriptsCartValidationDao wholescriptsCartValidationDao;

    @Resource(name = "configurationService")
    private ConfigurationService configurationService;

    @Resource(name = "commonI18NService")
    private CommonI18NService commonI18NService;

    @Resource(name = "wholescriptsProductsService")
    private WholescriptsProductsService wholescriptsProductsService;
    
    @Resource(name = "wholescriptsProductsDao")
    private WholescriptsProductsDao wholescriptsProductsDao;
    
    @Resource(name = "wholescriptsUserService")
    private WholescriptsUserService wholescriptsUserService;

    private static final String BLUE_LABEL_CODE = "wholescripts.blue.label.code";
    private static final String BEST_SELLAR_COUNT = "wholescripts.bestSeller.products.count";

    private static final String SAP_BASIC_MATERIAL="wholescripts.pdp.medpax.product.prefix";

    private static final String MASTER_VARIANT="wholescripts.pdp.mastervariant.classification.code";
    private static final String PRODUCT_FORM="wholescripts.pdp.productform.classification.code";

    private static final String PRODUCT_COUNT="wholescripts.pdp.productcount.classification.code";

    private static final String PRODUCT_MATERIAL_VARIANT_FLAVOR="wholescripts.pdp.flavor.classification.code";

    private static final String DOCTOR_REFERENCE_SHEET="wholescripts.pdp.doctorreferencesheet.classification.code";

    private static final String SUPPLEMENT_FACTS="wholescripts.pdp.supplementfacts.classification.code";
    private static final String SHORT_DESCRIPTION="wholescripts.pdp.shortdescription.classification.code";
    private static final String PRODUCT_NAME = "wholescripts.pdp.productname.classification.code";



    @Override
    public void populate(ProductModel productModel, ProductData productData) throws ConversionException {
        final PriceDataType priceType = WholeScriptsPriceUtils.getProductPriceType(productModel);
        final PriceInformation info = WholeScriptsPriceUtils.getProductPriceInformation(productModel);
        final BaseStoreModel baseStore = baseStoreService.getCurrentBaseStore();
        B2BUnitModel b2bUnit = getB2BUnitForCustomer();
        if (WholeScriptsPriceUtils.isRetailCustomer(b2bUnit)) {
            double retailsPrice = WholeScriptsPriceUtils.getRetailPrice(productModel, b2bUnit);
                calculateRetailPrice(retailsPrice, productData, baseStore);
                double yourPrice = WholeScriptsPriceUtils.getRetailPriceAfterDiscount(productModel, b2bUnit, retailsPrice);
                    final PriceData yourPriceData = priceDataFactory.create(priceType, BigDecimal.valueOf(yourPrice),
                            baseStore.getDefaultCurrency());
                    productData.setYourPrice(yourPriceData);
            double mapPrice = WholeScriptsPriceUtils.getMAPPrice(productModel);
                calculateMapPrice(mapPrice, productData, baseStore);


        } else if (WholeScriptsPriceUtils.isWholeSaleCustomer(b2bUnit)) {
            double wholeSalePrice = WholeScriptsPriceUtils.getWholeSalePrice(productModel, b2bUnit);
                calculateWholeSalePrice(wholeSalePrice, productData, baseStore);
                double yourPrice = WholeScriptsPriceUtils.getWholesalePriceAfterDiscount(productModel, b2bUnit);
                    final PriceData yourPriceData = priceDataFactory.create(priceType, BigDecimal.valueOf(yourPrice),
                            baseStore.getDefaultCurrency());
                    productData.setYourPrice(yourPriceData);

            double retailsPrice = WholeScriptsPriceUtils.getRetailPrice(productModel);
                calculateRetailPrice(retailsPrice, productData, baseStore);
            double mapPrice = WholeScriptsPriceUtils.getMAPPrice(productModel);
                calculateMapPrice(mapPrice, productData, baseStore);


        } else {
            double mapPrice = WholeScriptsPriceUtils.getMAPPrice(productModel);
                final PriceData mapPriceData = priceDataFactory.create(priceType, BigDecimal.valueOf(mapPrice),
                        baseStore.getDefaultCurrency());
                productData.setMapPrice(mapPriceData);

        }

        try {
            String countryIso = "US";
            String regionIso = "CA";
            String fullRegionCode = countryIso + "-" + regionIso;
            RegionModel regionModel = commonI18NService.getRegion(commonI18NService.getCountry(countryIso), fullRegionCode);

            if (regionModel != null) {
                boolean isRestricted = wholescriptsCartValidationDao.validateIfProductInCAExclusion(productModel.getCode(), regionIso);
                if (isRestricted) {
                    String restrictionMessage = configurationService.getConfiguration().getString("wholescripts.region.pdp.warning.description");
                    productData.setRegionSwapDescription(restrictionMessage + " " + regionModel.getName());
                }
            }
        } catch (Exception e) {
            LOG.warn("Error while checking region restriction for PDP", e);
        }


        WholescriptsBrandModel wholeScriptBrand = productModel.getWholescriptsBrand();
        if (Objects.nonNull(wholeScriptBrand)) {
            productData.setBrandName(wholeScriptBrand.getName());
            productData.setBrandUrl(wholeScriptBrand.getBrandUrl());
            productData.setBrandCode(wholeScriptBrand.getCode());
        }

        if (productModel.getPicture() != null) {
            LOG.info("Product has a picture: {}" + productModel.getPicture().getURL());

            try {
                ImageData mainImage = imageConverter.convert(productModel.getPicture());
                if (mainImage != null) {
                    LOG.info("Converted Image URL: {}" + mainImage.getUrl());

                    List<ImageData> images = new ArrayList<>();
                    images.add(mainImage);

                    if (productData.getImages() == null) {
                        productData.setImages(new ArrayList<>());
                    }

                    images.addAll(productData.getImages());
                    productData.setImages(images);
                } else {
                    LOG.warn("Image conversion failed, mainImage is null for product: {}" + productModel.getCode());
                }
            } catch (Exception e) {
                LOG.error("Exception while converting image for product: {}" + productModel.getCode(), e);
            }
        } else {
            LOG.warn("ProductModel.getPicture() is null for product: {}" + productModel.getCode());
        }


        setBestSellerProduct(productModel, productData);
        productData.setIsNew(productModel.getIsNew());
        productData.setIsFeatured(productModel.getIsFeatured());
        productData.setIsMedpax(productModel.getIsMedpax());
        
        final String genericPLName = wholescriptsProductsService.getGenericPLNameFromCMI(productModel.getCode());
        
        if (StringUtils.isNotBlank(genericPLName)) {
      	  productData.setGenericPLName(genericPLName);
        }
        else {
      	  productData.setGenericPLName(productModel.getGenericPLName());
        }
        
        
        setFavoriteProduct(productModel, productData);

        List<OtherAvailableProductsData>  alsoAvailablProducts=new ArrayList<>();

        productData.setSapEAN(productModel.getSapEAN());
        String sapBasicMaterial=productModel.getSapBasicMaterial();
        if(StringUtils.isNotBlank(sapBasicMaterial))
        {
            String productCode=Config.getParameter(SAP_BASIC_MATERIAL)+ sapBasicMaterial;
            OtherAvailableProductsData  otherProducts=new OtherAvailableProductsData();
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
                        if(StringUtils.isNotBlank(productName)){
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


        }
        final FeatureList featureList = classificationService.getFeatures(productModel);

        if (Objects.nonNull(featureList)) {
            final Feature masterVariantFeature = featureList.getFeatureByCode(Config.getParameter(MASTER_VARIANT));
            if (Objects.nonNull(masterVariantFeature)) {
                final FeatureValue masterVariantValue = masterVariantFeature.getValue();

                if (Objects.nonNull(masterVariantValue)) {
                    String masterVariant = (String) masterVariantValue.getValue();

                    if (StringUtils.isNotBlank(masterVariant)) {
                        try {
                            ProductModel masterVariantProduct = productService.getProductForCode(masterVariant);
                            if (Objects.nonNull(masterVariantProduct)) {

                                List<ProductModel> relatedProducts = wholescriptsProductsService.getProductsByMasterVariant(
                                        Config.getParameter(MASTER_VARIANT), masterVariant, productModel.getCatalogVersion().getPk().toString()
                                );
                                relatedProducts = relatedProducts.stream().filter(product -> !product.getCode().equals(productModel.getCode())).collect(Collectors.toList());

                                for (ProductModel relatedProduct : relatedProducts) {
                                    if (!relatedProduct.getCode().equals(productModel.getCode())) {
                                        OtherAvailableProductsData otherProducts = new OtherAvailableProductsData();
                                        otherProducts.setProductUrl(productModelUrlResolver.resolve(relatedProduct));
                                        final FeatureList masterFeatureList = classificationService.getFeatures(relatedProduct);

                                        if (Objects.nonNull(masterFeatureList)) {
                                            String productForm = extractFeatureValue(masterFeatureList, Config.getParameter(PRODUCT_FORM));
                                            String productCount = extractFeatureValue(masterFeatureList, Config.getParameter(PRODUCT_COUNT));
                                            String productMaterialVariantflavor = extractFeatureValue(masterFeatureList, Config.getParameter(PRODUCT_MATERIAL_VARIANT_FLAVOR));

                                            if (StringUtils.isNotBlank(productMaterialVariantflavor) || StringUtils.isNotBlank(productForm) || StringUtils.isNotBlank(productCount)) {
                                                otherProducts.setProductDisplyName(
                                                        String.join(" ", productMaterialVariantflavor,productCount, productForm).trim()
                                                );
                                            }

                                            alsoAvailablProducts.add(otherProducts);
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {

                        }
                    }
                }
                final Feature shortDescription = featureList.getFeatureByCode(Config.getParameter(SHORT_DESCRIPTION));
                if (Objects.nonNull(shortDescription)) {
                    final FeatureValue shortDescriptionValue = shortDescription.getValue();
                    if (Objects.nonNull(shortDescriptionValue)) {
                        final Object attributeValue = shortDescriptionValue.getValue();
                        String shortDescriptionData = attributeValue.toString();
                        productData.setShortDescription(shortDescriptionData);
                    }
                }

                final Feature productName = featureList.getFeatureByCode(Config.getParameter(PRODUCT_NAME));
                if (Objects.nonNull(productName)) {
                    final FeatureValue productNameValue = productName.getValue();
                    if (Objects.nonNull(productNameValue)) {
                        final Object attributeValue = productNameValue.getValue();
                        String productNameData = attributeValue.toString();
                        productData.setProductName(productNameData);
                        productData.setName(productNameData);
                    }
                }
            }

        final Feature featureDctrSheet = featureList.getFeatureByCode(Config.getParameter(DOCTOR_REFERENCE_SHEET));
            if(Objects.nonNull(featureDctrSheet))
            {
                final FeatureValue dctrSheetValue = featureDctrSheet.getValue();
                if (Objects.nonNull(dctrSheetValue)) {
                    MediaModel dctrReferenceMedia = (MediaModel) dctrSheetValue.getValue();
                    if (Objects.nonNull(dctrReferenceMedia))
                    {
                        productData.setDoctorReferenceSheet(imageConverter.convert(dctrReferenceMedia));
                    }
                }
            }
             final Feature featureSupplements = featureList.getFeatureByCode(Config.getParameter(SUPPLEMENT_FACTS));

            if(Objects.nonNull(featureSupplements))
            {
                final FeatureValue supplementsValue = featureSupplements.getValue();
                if (Objects.nonNull(supplementsValue)) {
                    MediaModel supplementsMedia = (MediaModel) supplementsValue.getValue();
                    if (Objects.nonNull(supplementsMedia))
                    {
                        productData.setSupplementFacts(imageConverter.convert(supplementsMedia));
                    }
                }
            }

        }

        if (productModel.getSapMaterialGroup() != null && PRIVATE_LABEL_CODES.contains(productModel.getSapMaterialGroup().getCode())) {
            final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
            final B2BUnitModel unit = customer.getDefaultB2BUnit();
            String accountNumber = WholescriptsCoreUtils.getFormattedDefaultB2bUnit(unit.getUid());
            CustomerMaterialInformationModel cmInfoModel = wholescriptsProductsService.getCustomerMaterialInfo(
                    productModel.getCode().replaceFirst("^0+", ""), accountNumber);

            if (cmInfoModel != null && cmInfoModel.getProductDescription() != null) {
                productData.setName(cmInfoModel.getProductDescription());
            }
        }

        if (CollectionUtils.isNotEmpty(alsoAvailablProducts)) {
            productData.setOtherAvailableProducts(alsoAvailablProducts);
        }
        if(CollectionUtils.isNotEmpty(productModel.getSupportedAutoShip()))
        {
            productData.setAutoShipFrequency(frequencyConveter.convertAll(productModel.getSupportedAutoShip()));
        }
        productData.setIsPrivateLabel(wholescriptsProductsService.isPrivateLabel(productModel));
        
        if(Objects.nonNull(featureList)) {
      	  setDosingData(featureList,productData);
        }
        setDeliveryForm(productModel,productData);

        setMedPaxImage(productModel,productData);
    }

	private void setDeliveryForm(ProductModel productModel,ProductData productData)
	{
		boolean isMedpax = Boolean.TRUE.equals(productModel.getIsMedpax());

        if (isMedpax) {
            // checking for blue label product associated with Medpax
            List<ProductModel> blueLabelProducts = wholescriptsProductsDao.getProductsByMedpaxCriteria(configurationService.getConfiguration().getString(BLUE_LABEL_CODE),
            		productModel.getSapBasicMaterial(),
            		productModel.getCatalogVersion());

            if (CollectionUtils.isNotEmpty(blueLabelProducts)) {
                for (ProductModel blueLabelProduct : blueLabelProducts) {
                    String materialProductForm = extractMaterialProductForm(blueLabelProduct);
                    if (StringUtils.isNotBlank(materialProductForm)) {
                  	  productData.setDeliveryForm(materialProductForm);
                        break; // Stop after first valid form
                    }
                }
            }
        }
	}

	//Extracts the material product form from the product's classification features.
   private String extractMaterialProductForm(ProductModel product) {
       FeatureList features = classificationService.getFeatures(product);
       return extractFeatureValueForMedpax(features, configurationService.getConfiguration().getString(PRODUCT_FORM));
   }
   
   // Extracts a feature value based on the feature code.
   private String extractFeatureValueForMedpax(FeatureList featureList, String featureCode) {
       if (featureList == null || StringUtils.isBlank(featureCode)) {
           return null;
       }
       Feature feature = featureList.getFeatureByCode(featureCode);
       if (feature != null && CollectionUtils.isNotEmpty(feature.getValues())) {
           FeatureValue value = feature.getValues().get(0); // Get the first value
           if (value.getValue() instanceof ClassificationAttributeValueModel) {
               return ((ClassificationAttributeValueModel) value.getValue()).getName();
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
                                break;
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
                            break;
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


    private void setDosingData(final FeatureList featureList,ProductData productData)
	{
		final Feature amDosing = featureList.getFeatureByCode(configurationService.getConfiguration().getString(AM_DOSING));
        if (Objects.nonNull(amDosing)) {
            final FeatureValue amDosingValue = amDosing.getValue();
            productData.setInAM("0");
            if (Objects.nonNull(amDosingValue)) {
                final Object attributeValue = amDosingValue.getValue();
                String am = ((ClassificationAttributeValueModel) attributeValue).getName();
                productData.setInAM(am);
            }
        }

        final Feature pmDosing = featureList.getFeatureByCode(configurationService.getConfiguration().getString(PM_DOSING));
        if (Objects.nonNull(pmDosing)) {
            final FeatureValue pmDosingValue = pmDosing.getValue();
            productData.setInPM("0");
            if (Objects.nonNull(pmDosingValue)) {
                final Object attributeValue = pmDosingValue.getValue();
                String pm =((ClassificationAttributeValueModel) attributeValue).getName();
                productData.setInPM(pm);
            }
        }

        final Feature dailyDosing = featureList.getFeatureByCode(configurationService.getConfiguration().getString(DAILY_DOSING));
        if (Objects.nonNull(dailyDosing)) {
            final FeatureValue dailyDosingValue = dailyDosing.getValue();
            productData.setInDaily("0");
            if (Objects.nonNull(dailyDosingValue)) {
                final Object attributeValue = dailyDosingValue.getValue();
                String daily = ((ClassificationAttributeValueModel) attributeValue).getName();
                productData.setInDaily(daily);
            }
        }

        final Feature bedtimeDosing = featureList.getFeatureByCode(configurationService.getConfiguration().getString(BEDTIME_DOSING));
        if (Objects.nonNull(bedtimeDosing)) {
            final FeatureValue bedtimeDosingValue = bedtimeDosing.getValue();
            productData.setInBedtime("0");
            if (Objects.nonNull(bedtimeDosingValue)) {
                final Object attributeValue = bedtimeDosingValue.getValue();
                String bedtime =((ClassificationAttributeValueModel) attributeValue).getName();
                productData.setInBedtime(bedtime);
            }
        }
	}

    private String extractFeatureValue(FeatureList featureList, String featureCode) {
        final Feature feature = featureList.getFeatureByCode(featureCode);
        if (Objects.nonNull(feature) && CollectionUtils.isNotEmpty(feature.getValues())) {
            Object attributeValue = feature.getValues().get(0).getValue();
            if (attributeValue instanceof ClassificationAttributeValueModel) {
                return ((ClassificationAttributeValueModel) attributeValue).getName();
            }
        }
        return "";
    }

    private void setBestSellerProduct(ProductModel productModel, ProductData productData) {
        Collection<CategoryModel> categories=commerceProductService.getSuperCategoriesExceptClassificationClassesForProduct(productModel);
        String count = Config.getParameter(BEST_SELLAR_COUNT);
        long productCount = 0;
        if(StringUtils.isNotBlank(count))
        {
            productCount = Long.parseLong(count);
        }
        if(CollectionUtils.isNotEmpty(categories))
        {
            boolean isBestSeller=false;
            for (final CategoryModel category : categories)
            {
                final List<WsPopularProductsModel> bestSellerProducts = new ArrayList<WsPopularProductsModel>();

                if(category != null && !isBestSeller)
                {
                    List<WsPopularProductsModel> popularProducts= wholescriptsPopularCategoriesService.getPopularProductsDetails(category.getCode());

                    bestSellerProducts.addAll(popularProducts.stream()
                            .sorted(Comparator.comparing(WsPopularProductsModel::getWeight).reversed())
                            .limit(productCount).collect(Collectors.toSet()));
                    if(CollectionUtils.isNotEmpty(bestSellerProducts))
                    {
                        for(final WsPopularProductsModel product : bestSellerProducts)
                        {
                            if(product!=null && product.getProductCode().equals(productModel.getCode()))
                            {
                                isBestSeller=true;
                                productData.setIsBestSeller(true);
                                break;
                            }
                        }
                    }
                }
            }
        }

    }


    B2BUnitModel getB2BUnitForCustomer()
    {
        final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
        if (Objects.nonNull(customer)) {
            return customer.getDefaultB2BUnit();
        }
        return null;
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
    private void calculateWholeSalePrice(Double wholeSalePrice, ProductData target, BaseStoreModel baseStore) {
        final PriceData priceData = priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(wholeSalePrice),
                baseStore.getDefaultCurrency());
        target.setWholeSalePrice(priceData);
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
    
    private void setFavoriteProduct(ProductModel productModel, ProductData productData) {
   	 
       final List<String> productCodes = wholescriptsUserService.getFavouriteProductCodes();
       productData.setIsFavorite(false);
       if (productCodes.contains(productData.getCode())) {
      	 productData.setIsFavorite(true);
       }
   }
    
}
