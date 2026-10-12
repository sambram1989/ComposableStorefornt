package com.wholescripts.core.populators;


import com.wholescripts.core.constants.WholescriptscoreConstants;
import com.wholescripts.core.helper.WholescriptsHelper;
import com.wholescripts.core.model.CustomerMaterialInformationModel;
import com.wholescripts.core.model.RegionProductAgeRestrictionModel;
import com.wholescripts.core.model.SapUnitOfMeasureModel;
import com.wholescripts.core.service.WholescriptsProudctsService;
import com.wholescripts.core.utils.WholescriptsDateUtils;
import com.wholescripts.data.MedPaxDetailsData;
import com.wholescripts.data.WholescriptsExternalAPIProductData;
import com.wholescripts.data.WholescriptsExternalAPIProductImagesData;
import com.wholescripts.data.WholescriptsExternalDosingData;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.Config;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import de.hybris.platform.commerceservices.stock.strategies.WarehouseSelectionStrategy;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author Sadiq
 */

public class WholescriptsExternalAPIPLProductPopulator implements Populator<ProductModel, WholescriptsExternalAPIProductData> {

    private static final Logger LOG = Logger.getLogger(WholescriptsExternalAPIPLProductPopulator.class);

    private static final String SAP_BASIC_MATERIAL = "wholescripts.pdp.medpax.product.prefix";
    private static final String RETAIL_GROUP = "wholescripts.retail.group";
    private static final String WHOLESALE_GROUP = "wholescripts.wholesale.group";
    private static final String SUPPLEMENT_FACTS = "wholescripts.pdp.supplementfacts.classification.code";
    private static final String DOCTOR_REFERENCE_SHEET = "wholescripts.pdp.doctorreferencesheet.classification.code";
    private static final String SHORT_DESCRIPTION = "wholescripts.pdp.shortdescription.classification.code";
    private static final String LONG_DESCRIPTION = "wholescripts.pdp.longdescription.classification.code";
    //private static final String WARNING = "wholescripts.pdp.warning.classification.code";
    private static final String COUNT = "wholescripts.pdp.productcount.classification.code";
    private static final String COUNT_UNIT = "wholescripts.pdp.productform.classification.code";
    private static final String AM_DOSING = "wholescripts.dosage.am.classification.code";
    private static final String PM_DOSING = "wholescripts.dosage.pm.classification.code";
    private static final String DAILY_DOSING = "wholescripts.dosage.daily.classification.code";
    private static final String BEDTIME_DOSING = "wholescripts.dosage.bedtime.classification.code";
    private static final String GUEST_GROUP = "wholescripts.guest.group";
    private static final String FLAVOR = "wholescripts.pdp.flavor.classification.code";
    private static final String retailPriceGroup = Config.getParameter(RETAIL_GROUP);
    private static final String wholeSalePriceGroup = Config.getParameter(WHOLESALE_GROUP);
    private static final String onlinePriceGroup = Config.getParameter(GUEST_GROUP);
    private static final String IMAGE_TYPE_SUPPLEMENT_FACTS = "supplement_facts";
    private static final String IMAGE_TYPE_PRODUCT = "product";
    private static final String IMAGE_TYPE_OTHERS = "other";
    private static final String EA_UNIT_TYPE = "EA";
    private static final String APPROVAL_STATUS_APPROVED = "approved";
    private static final String PRODUCT_SWAP_RESTRICTION_CODE = "product.swap.restriction.code";
    private static final String MEDIA_URL="media.wholescripts.https";
    private static final String MATERIAL_PRODUCT_GROUP = "wholescripts.material.product.group.classification.code";
    private static final String MEDIA_FORMAT="515x515";
    private static final String PL_PRODUCT_IMAGE_NAME="wholescripts.pl.product.image";
    
    @Resource(name = "productService")
    private ProductService productService;
    @Resource(name = "classificationService")
    private ClassificationService classificationService;
    @Resource(name = "commerceStockService")
    private CommerceStockService commerceStockService;
    @Resource(name = "baseStoreService")
    private BaseStoreService baseStoreService;
    @Resource(name = "configurationService")
    private ConfigurationService configurationService;

    @Resource(name = "wholescriptsProudctsService")
    private WholescriptsProudctsService wholescriptsProudctsService;

    @Resource(name = "catalogVersionService")
    private CatalogVersionService catalogVersionService;

    @Resource(name = "stockService")
    private StockService stockService;

    @Resource(name = "warehouseSelectionStrategy")
    private WarehouseSelectionStrategy warehouseSelectionStrategy;
    
    @Resource(name = "mediaService")
    private MediaService mediaService;

    @Override
    public void populate(ProductModel source, WholescriptsExternalAPIProductData target) throws ConversionException {

        final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(
                configurationService.getConfiguration().getString("wholescripts.product.catalog"),
                configurationService.getConfiguration().getString("wholescripts.product.catalog.version"));

            populateBasicAttributes(source, target);
            final BaseStoreModel baseStore = baseStoreService.getCurrentBaseStore();
            populateStock(source, target, baseStore);
            populateRetailAndWholeSalePrices(source, target);

            List<WholescriptsExternalAPIProductImagesData> imagesData = new ArrayList<>();
            
            final String plDefaultImageName = configurationService.getConfiguration().getString(PL_PRODUCT_IMAGE_NAME);
            
            final MediaModel plDefaultImage = mediaService.getMedia(catalogVersion, plDefaultImageName);
            
				if (plDefaultImage != null) {

					WholescriptsExternalAPIProductImagesData wholescriptsExternalAPIProductImagesData = new WholescriptsExternalAPIProductImagesData();
					wholescriptsExternalAPIProductImagesData.setImageType(IMAGE_TYPE_PRODUCT);
					wholescriptsExternalAPIProductImagesData.setMimeType(plDefaultImage.getMime());
					wholescriptsExternalAPIProductImagesData.setOrder(1);
					wholescriptsExternalAPIProductImagesData.setUrl(configurationService.getConfiguration().getString(MEDIA_URL) + plDefaultImage.getURL());
					imagesData.add(wholescriptsExternalAPIProductImagesData);


				}

            List<MediaContainerModel> gallaryImagesContainer = source.getGalleryImages();
            if (CollectionUtils.isNotEmpty(gallaryImagesContainer)) {
                for (MediaContainerModel mediaContainerModel : gallaryImagesContainer) {
                    Collection<MediaModel> gallaryImages = mediaContainerModel.getMedias();
                    if (CollectionUtils.isNotEmpty(gallaryImages)) {
                  	   int order = 2;
                        for (MediaModel gallaryImage : gallaryImages) {
                        	
                        	if(gallaryImage.getMediaFormat().getQualifier().equalsIgnoreCase(MEDIA_FORMAT)) {
                        		
                        		WholescriptsExternalAPIProductImagesData wholescriptsExternalAPIProductImagesData = new WholescriptsExternalAPIProductImagesData();
                              if(null != source.getSapMaterialGroup() && StringUtils.equals(source.getSapMaterialGroup().getCode(),"1000009")){
                                  wholescriptsExternalAPIProductImagesData.setImageType("carton");
                              } else{
                                  wholescriptsExternalAPIProductImagesData.setImageType(IMAGE_TYPE_PRODUCT);
                              }
                              wholescriptsExternalAPIProductImagesData.setUrl(configurationService.getConfiguration().getString(MEDIA_URL)+gallaryImage.getURL());
                              wholescriptsExternalAPIProductImagesData.setMimeType(gallaryImage.getMime());
                              wholescriptsExternalAPIProductImagesData.setOrder(order);
                              imagesData.add(wholescriptsExternalAPIProductImagesData);
                              order++;
                        	}
                        }
                    }
                }
            }

            Collection<MediaModel> otherImagesCollection = source.getOthers();
            if (CollectionUtils.isNotEmpty(otherImagesCollection)) {
            	 int order = 1;
                for (MediaModel otherMedia : otherImagesCollection) {
               	 if(otherMedia.getMediaFormat().getQualifier().equalsIgnoreCase(MEDIA_FORMAT)) {
               		 WholescriptsExternalAPIProductImagesData wholescriptsExternalAPIProductImagesData = new WholescriptsExternalAPIProductImagesData();
                      wholescriptsExternalAPIProductImagesData.setImageType(IMAGE_TYPE_OTHERS);
                      wholescriptsExternalAPIProductImagesData.setUrl(configurationService.getConfiguration().getString(MEDIA_URL)+otherMedia.getURL());
                      wholescriptsExternalAPIProductImagesData.setMimeType(otherMedia.getMime());
                      wholescriptsExternalAPIProductImagesData.setOrder(order);
                      imagesData.add(wholescriptsExternalAPIProductImagesData);
                      order++;
               	 }

                }
            }


            final FeatureList featureList = classificationService.getFeatures(source);
            
				final Feature pimProductName = featureList.getFeatureByCode(configurationService.getConfiguration()
						.getString(WholescriptscoreConstants.CLASSIFICATION_ATTRIBUTE_PRODUCT_NAME));
				String productName = null;
				if (Objects.nonNull(pimProductName)) {
					final FeatureValue productNameValue = pimProductName.getValue();
					if (Objects.nonNull(productNameValue)) {
						productName = productNameValue.getValue().toString();
					}
					else {
						productName = source.getName();
					}
				}
				else {
					productName = source.getName();
				}

				if (StringUtils.isNotEmpty(productName)) {
					target.setProductName(productName);
				}
            
            final Feature featureSupplements = featureList.getFeatureByCode(configurationService.getConfiguration().getString(SUPPLEMENT_FACTS));

            if (Objects.nonNull(featureSupplements)) {
                final FeatureValue supplementsValue = featureSupplements.getValue();
                if (Objects.nonNull(supplementsValue)) {
                    MediaModel supplementsMedia = (MediaModel) supplementsValue.getValue();
                    if (Objects.nonNull(supplementsMedia)) {
                  		  WholescriptsExternalAPIProductImagesData wholescriptsExternalAPIProductImagesData = new WholescriptsExternalAPIProductImagesData();
                          wholescriptsExternalAPIProductImagesData.setImageType(IMAGE_TYPE_SUPPLEMENT_FACTS);
                          wholescriptsExternalAPIProductImagesData.setUrl(configurationService.getConfiguration().getString(MEDIA_URL)+supplementsMedia.getURL());
                          wholescriptsExternalAPIProductImagesData.setMimeType(supplementsMedia.getMime());
                          wholescriptsExternalAPIProductImagesData.setOrder(1);
                          
                          imagesData.add(wholescriptsExternalAPIProductImagesData);
                    }
                }
            }

            target.setImages(imagesData);

            final Feature shortDescription = featureList.getFeatureByCode(configurationService.getConfiguration().getString(SHORT_DESCRIPTION));
            if (Objects.nonNull(shortDescription)) {
                final FeatureValue shortDescriptionValue = shortDescription.getValue();
                if (Objects.nonNull(shortDescriptionValue)) {
                    final Object attributeValue = shortDescriptionValue.getValue();
                    String shortDescriptionData = attributeValue.toString();
                    target.setDescriptionShort(shortDescriptionData);
                }
            }
            final Feature longDescription = featureList.getFeatureByCode(configurationService.getConfiguration().getString(LONG_DESCRIPTION));
            if (Objects.nonNull(longDescription)) {
                final FeatureValue longDescriptionValue = longDescription.getValue();
                if (Objects.nonNull(longDescriptionValue)) {
                    final Object attributeValue = longDescriptionValue.getValue();
                    String longDescriptionData = attributeValue.toString();
                    target.setDescriptionFull(longDescriptionData);
                }
            }

            final Feature count = featureList.getFeatureByCode(configurationService.getConfiguration().getString(COUNT));
            if (Objects.nonNull(count)) {
                final FeatureValue countValue = count.getValue();
                if (Objects.nonNull(countValue)) {
                    final Object attributeValue = countValue.getValue();
                    String countData = ((ClassificationAttributeValueModel) attributeValue).getName();
                    target.setCount(countData);
                }
            }
            final Feature countUnit = featureList.getFeatureByCode(configurationService.getConfiguration().getString(COUNT_UNIT));
            if (Objects.nonNull(countUnit)) {
                final FeatureValue countUnitValue = countUnit.getValue();
                if (Objects.nonNull(countUnitValue)) {
                    final Object attributeValue = countUnitValue.getValue();
                    String countUnitData = ((ClassificationAttributeValueModel) attributeValue).getName();
                    target.setCountUnit(countUnitData);
                }
            }
            final Feature drs = featureList.getFeatureByCode(configurationService.getConfiguration().getString(DOCTOR_REFERENCE_SHEET));
            if (Objects.nonNull(drs)) {
                final FeatureValue drsValue = drs.getValue();
                if (Objects.nonNull(drsValue)) {
                    MediaModel drsData = (MediaModel) drsValue.getValue();
                    if (Objects.nonNull(drsData)) {
                        target.setDrs(configurationService.getConfiguration().getString(MEDIA_URL)+drsData.getURL());
                    }
                }
            }

            final Feature flavor = featureList.getFeatureByCode(configurationService.getConfiguration().getString(FLAVOR));
            if (Objects.nonNull(flavor)) {
                final FeatureValue flavorValue = flavor.getValue();
                if (Objects.nonNull(flavorValue)) {
                    final Object attributeValue = flavorValue.getValue();
                    String flavorData = ((ClassificationAttributeValueModel) attributeValue).getName();
                    target.setFlavor(flavorData);
                }
            }

            List<WholescriptsExternalDosingData> dosingList = new ArrayList<>();

            final Feature amDosing = featureList.getFeatureByCode(configurationService.getConfiguration().getString(AM_DOSING));
            if (Objects.nonNull(amDosing)) {
                final FeatureValue amDosingValue = amDosing.getValue();
                if (Objects.nonNull(amDosingValue)) {
                    final Object attributeValue = amDosingValue.getValue();
                    int amDosingData = Integer.valueOf(((ClassificationAttributeValueModel) attributeValue).getName());
                    WholescriptsExternalDosingData wsAmDosingData = new WholescriptsExternalDosingData();
                    if (amDosingData > 0) {
                        wsAmDosingData.setTime("AM");
                        wsAmDosingData.setQty(String.valueOf(amDosingData));
                        dosingList.add(wsAmDosingData);
                    }
                }
            }

            final Feature pmDosing = featureList.getFeatureByCode(configurationService.getConfiguration().getString(PM_DOSING));
            if (Objects.nonNull(pmDosing)) {
                final FeatureValue pmDosingValue = pmDosing.getValue();
                if (Objects.nonNull(pmDosingValue)) {
                    final Object attributeValue = pmDosingValue.getValue();
                    int pmDosingData = Integer.valueOf(((ClassificationAttributeValueModel) attributeValue).getName());
                    WholescriptsExternalDosingData wsPmDosingData = new WholescriptsExternalDosingData();
                    if (pmDosingData > 0) {
                        wsPmDosingData.setTime("PM");
                        wsPmDosingData.setQty(String.valueOf(pmDosingData));
                        dosingList.add(wsPmDosingData);
                    }
                }
            }

            final Feature dailyDosing = featureList.getFeatureByCode(configurationService.getConfiguration().getString(DAILY_DOSING));
            if (Objects.nonNull(dailyDosing)) {
                final FeatureValue dailyDosingValue = dailyDosing.getValue();
                if (Objects.nonNull(dailyDosingValue)) {
                    final Object attributeValue = dailyDosingValue.getValue();
                    int dailyDosingData = Integer.valueOf(((ClassificationAttributeValueModel) attributeValue).getName());
                    WholescriptsExternalDosingData wsDailyDosingData = new WholescriptsExternalDosingData();
                    if (dailyDosingData > 0) {
                        wsDailyDosingData.setTime("DAILY");
                        wsDailyDosingData.setQty(String.valueOf(dailyDosingData));
                        dosingList.add(wsDailyDosingData);
                    }
                }
            }

            final Feature bedtimeDosing = featureList.getFeatureByCode(configurationService.getConfiguration().getString(BEDTIME_DOSING));
            if (Objects.nonNull(bedtimeDosing)) {
                final FeatureValue bedtimeDosingValue = bedtimeDosing.getValue();
                if (Objects.nonNull(bedtimeDosingValue)) {
                    final Object attributeValue = bedtimeDosingValue.getValue();
                    int bedtimeDosingData = Integer.valueOf(((ClassificationAttributeValueModel) attributeValue).getName());
                    WholescriptsExternalDosingData wsBedTimeDosingData = new WholescriptsExternalDosingData();
                    if (bedtimeDosingData > 0) {
                        wsBedTimeDosingData.setTime("BEDTIME");
                        wsBedTimeDosingData.setQty(String.valueOf(bedtimeDosingData));
                        dosingList.add(wsBedTimeDosingData);
                    }
                }
            }
            target.setDefaultDosing(dosingList);

            final Feature materialProductGroups = featureList.getFeatureByCode(configurationService.getConfiguration().getString(MATERIAL_PRODUCT_GROUP));
            List<FeatureValue> categories = materialProductGroups.getValues();
            List<String> listOfCategories = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(categories)) {
                for (FeatureValue featureValue : categories) {
                    String value = ((ClassificationAttributeValueModel) featureValue.getValue())
                            .getName();
                    listOfCategories.add(value);
                }
            }
            target.setCategories(listOfCategories);
        
    }

    private void populateBasicAttributes(ProductModel source, WholescriptsExternalAPIProductData target) {

        if (StringUtils.isNotEmpty(source.getSapBasicMaterial()) && source.getIsMedpax()) {
            target.setMedPaxSku(WholescriptsHelper.addLeadingZeros(source.getSapBasicMaterial()));
        }

        if (StringUtils.isNotEmpty(source.getGenericPLName())) {
            target.setGenericName(source.getGenericPLName());
        }
        if (StringUtils.isNotEmpty(source.getCode())) {
            target.setSku(source.getCode());
        }
        if (StringUtils.isNotEmpty(source.getSapEAN())) {
            target.setUpc(source.getSapEAN());
        }
        /**if (Objects.nonNull(source.getPicture())) {
            target.setProductImage(configurationService.getConfiguration().getString(MEDIA_URL)+ source.getPicture().getURL());
        }**/
        if (Objects.nonNull(source.getWholescriptsBrand())) {
            target.setBrand(source.getWholescriptsBrand().getName());
        }
        target.setReleaseDate(WholescriptsDateUtils.convertDateInUTCFormat(source.getCreationtime()));

        if(source.getSapMaterialGroup() != null){
            target.setMaterialGroup(source.getSapMaterialGroup().getCode());
        }

        if(StringUtils.equalsIgnoreCase(source.getApprovalStatus().getCode(),APPROVAL_STATUS_APPROVED)){
            target.setIsAvailableInCatalog("true");
        } else {
            target.setIsAvailableInCatalog("false");
        }

        target.setIsOnBackOrder(Boolean.FALSE.toString());

        String swapProductCode = wholescriptsProudctsService.getProductSwapRestriction(source.getCode());
        if(StringUtils.isNotBlank(swapProductCode)){

            List<String> swapRestrictionvalues = new ArrayList<>();
            swapRestrictionvalues.add(configurationService.getConfiguration().getString(PRODUCT_SWAP_RESTRICTION_CODE));
            if (CollectionUtils.isNotEmpty(swapRestrictionvalues)) {
            	target.setWarnings(swapRestrictionvalues);
            }
            
            if (StringUtils.isNotEmpty(swapProductCode)) {
            	target.setCaliProductSwapSKU(swapProductCode);
            }
            
        }

        List<RegionProductAgeRestrictionModel> ageRestrictionModels = wholescriptsProudctsService.getProductAgeRestriction(source.getCode());
        if(CollectionUtils.isNotEmpty(ageRestrictionModels)){
            List<String> ageRestrictionvalues = new ArrayList<>();
            for(RegionProductAgeRestrictionModel ageRestrictionModel : ageRestrictionModels){
                ageRestrictionvalues.add(ageRestrictionModel.getRegionCode());
            }
            if (CollectionUtils.isNotEmpty(ageRestrictionModels)) {
            	target.setAgeRestrictedRegions(ageRestrictionvalues);
            }
        }

        List<SapUnitOfMeasureModel> sapUnitOfMeasures = source.getSapUnitOfMeasure();
        if(CollectionUtils.isNotEmpty(sapUnitOfMeasures)){
            for(SapUnitOfMeasureModel sapUnitofMeasure : sapUnitOfMeasures){
                if(StringUtils.equalsIgnoreCase(sapUnitofMeasure.getUnitType(),EA_UNIT_TYPE)){

                    if(!StringUtils.isBlank(sapUnitofMeasure.getWeight())){
                        target.setWeight(Double.valueOf(sapUnitofMeasure.getWeight()));
                    }
                    if(!StringUtils.isBlank(sapUnitofMeasure.getLength())){
                        target.setLength(Double.valueOf(sapUnitofMeasure.getLength()));
                    }

                    if(!StringUtils.isBlank(sapUnitofMeasure.getWidth())){
                        target.setWidth(Double.valueOf(sapUnitofMeasure.getWidth()));
                    }

                    if(!StringUtils.isBlank(sapUnitofMeasure.getHeight())){
                        target.setHeight(Double.valueOf(sapUnitofMeasure.getHeight()));
                    }
                    break;
                }
            }
        }
    }

    private void populateStock(ProductModel source, WholescriptsExternalAPIProductData target, BaseStoreModel baseStore){
        Long stockQty = WholescriptsHelper.getTotalUnit(
                stockService.getStockLevels(source, warehouseSelectionStrategy.getWarehousesForBaseStore(baseStore)));
        if (stockQty != null) {
            target.setQuantity(stockQty.intValue());
        }
    }

    private void populateMedPaxDetails(ProductModel source, WholescriptsExternalAPIProductData target, BaseStoreModel baseStore) {
        String sapBasicMaterial = source.getSapBasicMaterial();
        if (StringUtils.isNotBlank(sapBasicMaterial)) {
            String productCode = Config.getParameter(SAP_BASIC_MATERIAL) + sapBasicMaterial;
            try {
                ProductModel product = productService.getProductForCode(productCode);
                MedPaxDetailsData medPaxDetailsData = new MedPaxDetailsData();
                if (Objects.nonNull(product)) {
                    boolean isMedPax = product.getIsMedpax();
                    if (isMedPax) {
                        Long stockMedPaxQty = commerceStockService.getStockLevelForProductAndBaseStore(source, baseStore);
                        if (stockMedPaxQty != null) {
                            medPaxDetailsData.setQuantity(stockMedPaxQty.intValue());
                        }
                        }
                        if (StringUtils.isNotEmpty(source.getName())) {
                            medPaxDetailsData.setName(product.getName());
                            medPaxDetailsData.setGenericName(product.getName());
                        }

                        PriceRowModel medPaxRetailPriceRow = product.getEurope1Prices().stream().filter(pr -> pr.getUg() != null && retailPriceGroup.equalsIgnoreCase(pr.getUg().getCode())).findAny().orElse(null);
                        if (Objects.nonNull(medPaxRetailPriceRow)) {
                            medPaxDetailsData.setRetailPrice(medPaxRetailPriceRow.getPrice());
                        }
                        PriceRowModel medPaxWholeSalePriceRow = product.getEurope1Prices().stream().filter(pr -> pr.getUg() != null && wholeSalePriceGroup.equalsIgnoreCase(pr.getUg().getCode())).findAny().orElse(null);
                        if (Objects.nonNull(medPaxWholeSalePriceRow)) {
                            medPaxDetailsData.setWholesalePrice(medPaxWholeSalePriceRow.getPrice());
                        }
                    target.setMedPaxDetails(medPaxDetailsData);
                    }
                } catch (Exception e) {
                LOG.error(e.getMessage());
            }

            }
        }


    private void populateRetailAndWholeSalePrices(ProductModel source, WholescriptsExternalAPIProductData target){
        PriceRowModel retailPriceRow = source.getEurope1Prices().stream().filter(pr -> pr.getUg() != null && retailPriceGroup.equalsIgnoreCase(pr.getUg().getCode())).findAny().orElse(null);
        if (Objects.nonNull(retailPriceRow)) {
            target.setRetailPrice(retailPriceRow.getPrice());
        }
        PriceRowModel wholeSalePriceRow = source.getEurope1Prices().stream().filter(pr -> pr.getUg() != null && wholeSalePriceGroup.equalsIgnoreCase(pr.getUg().getCode())).findAny().orElse(null);
        if (Objects.nonNull(wholeSalePriceRow)) {
            target.setWholesalePrice(wholeSalePriceRow.getPrice());
        }

        PriceRowModel onlineMapPrice = source.getEurope1Prices().stream().filter(pr -> pr.getUg()!=null && onlinePriceGroup.equalsIgnoreCase(pr.getUg().getCode())).findAny().orElse(null);
        if (Objects.nonNull(onlineMapPrice)) {
            target.setOnlineMAP(onlineMapPrice.getPrice());
        }
    }

    private static String addLeadingZeros(String productCode) {
        if (productCode == null) {
            return "000000000000000000"; // Handle null input
        }
        int paddingLength = 18 - productCode.length();
        if (paddingLength > 0) {
            return "0".repeat(paddingLength) + productCode;
        } else {
            return productCode; // String is already 18 or more chars
        }
    }
}
