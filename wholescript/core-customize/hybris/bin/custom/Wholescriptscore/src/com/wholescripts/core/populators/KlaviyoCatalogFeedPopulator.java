package com.wholescripts.core.populators;

import com.wholescripts.core.helper.WholescriptsHelper;
import com.wholescripts.data.KlaviyoCatalogFeedData;
import com.wholescripts.data.WholescriptsExternalAPIProductImagesData;
import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.commerceservices.stock.strategies.WarehouseSelectionStrategy;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.Config;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.*;

public class KlaviyoCatalogFeedPopulator implements Populator<ProductModel, KlaviyoCatalogFeedData> {

    private static final String PRODUCT_NAME = "wholescripts.pdp.productname.classification.code";
    private static final String SHORT_DESCRIPTION = "wholescripts.pdp.shortdescription.classification.code";
    private static final String MATERIAL_PRODUCT_GROUP = "wholescripts.material.product.group.classification.code";
    private static final String RETAIL_GROUP = "wholescripts.retail.group";
    private static final String retailPriceGroup = Config.getParameter(RETAIL_GROUP);
    private static final String MEDIA_FORMAT="515x515";
    private static final String MEDIA_URL="media.wholescripts.https";

    @Resource(name="classificationService")
    private ClassificationService classificationService;

    @Resource(name = "configurationService")
    private ConfigurationService configurationService;

    @Resource(name = "baseStoreService")
    private BaseStoreService baseStoreService;

    @Resource(name = "warehouseSelectionStrategy")
    private WarehouseSelectionStrategy warehouseSelectionStrategy;

    @Resource(name = "stockService")
    private StockService stockService;

    @Resource(name = "baseSiteService")
    private BaseSiteService baseSiteService;

    @Resource(name="productModelUrlResolver")
    private UrlResolver<ProductModel> productModelUrlResolver;

    @Resource(name="siteBaseUrlResolutionService")
    private SiteBaseUrlResolutionService siteBaseUrlResolutionService;

    @Override
    public void populate(ProductModel source, KlaviyoCatalogFeedData target) throws ConversionException {

        target.setId(source.getCode());
        target.setSku(source.getCode());
        target.setTitle(getProductName(source));

        PriceRowModel priceRow = source.getEurope1Prices().stream().filter(pr -> pr.getUg() != null && retailPriceGroup.equalsIgnoreCase(pr.getUg().getCode())).findAny().orElse(null);
        if (Objects.nonNull(priceRow)) {
            target.setPrice(priceRow.getPrice());
        }

        target.setInventory_quantity(populateStock(source));
        target.setInventory_policy(0);

        target.setImage_link(getImageURL(source));

        final String baseUrl = siteBaseUrlResolutionService.getWebsiteUrlForSite(baseSiteService.getCurrentBaseSite(), true,
                StringUtils.EMPTY);
        String productURL = baseUrl.replaceAll("/+$", "") + "/" + productModelUrlResolver.resolve(source).replaceAll("^/+", "");
        target.setLink(productURL);

        final FeatureList featureList = classificationService.getFeatures(source);

        final Feature shortDescription = featureList.getFeatureByCode(configurationService.getConfiguration().getString(SHORT_DESCRIPTION));
        if (Objects.nonNull(shortDescription)) {
            final FeatureValue shortDescriptionValue = shortDescription.getValue();
            if (Objects.nonNull(shortDescriptionValue)) {
                final Object attributeValue = shortDescriptionValue.getValue();
                String shortDescriptionData = attributeValue.toString();
                target.setDescription(shortDescriptionData);
            }
        }

        final Feature materialProductGroups = featureList.getFeatureByCode(configurationService.getConfiguration().getString(MATERIAL_PRODUCT_GROUP));
        if (materialProductGroups != null) {
            List<FeatureValue> categories = materialProductGroups.getValues();
            Set<String> listOfCategories = new HashSet<>();
            if (CollectionUtils.isNotEmpty(categories)) {
                for (FeatureValue featureValue : categories) {
                    String value = ((ClassificationAttributeValueModel) featureValue.getValue())
                            .getName();
                    listOfCategories.add(value);
                }
            }
            target.setCategories(new ArrayList<>(listOfCategories));
        }
    }

    private Integer populateStock(ProductModel source){
        final BaseStoreModel baseStore = baseStoreService.getCurrentBaseStore();
        Long stockQty = WholescriptsHelper.getTotalUnit(
                stockService.getStockLevels(source, warehouseSelectionStrategy.getWarehousesForBaseStore(baseStore)));
        return stockQty.intValue();
    }

    private String getProductName(ProductModel productModel){
        String productName = "";
        final FeatureList featureList = classificationService.getFeatures(productModel);
        final Feature feature = featureList.getFeatureByCode(Config.getParameter(PRODUCT_NAME));
        if (Objects.nonNull(feature)) {
            final FeatureValue productNameValue = feature.getValue();
            if (Objects.nonNull(productNameValue)) {
                final Object attributeValue = productNameValue.getValue();
                productName = attributeValue.toString();
            }
        }
        if(productName.isEmpty()){
            productName = productModel.getName();
        }
        return productName;
    }

    private String getImageURL(ProductModel source){
        String imageURL = "";
        List<MediaContainerModel> gallaryImagesContainer = source.getGalleryImages();
        if (CollectionUtils.isNotEmpty(gallaryImagesContainer)) {
            for (MediaContainerModel mediaContainerModel : gallaryImagesContainer) {
                Collection<MediaModel> gallaryImages = mediaContainerModel.getMedias();
                if (CollectionUtils.isNotEmpty(gallaryImages)) {
                    for (MediaModel gallaryImage : gallaryImages) {
                        if(gallaryImage.getMediaFormat().getQualifier().equalsIgnoreCase(MEDIA_FORMAT)) {
                            imageURL = configurationService.getConfiguration().getString(MEDIA_URL)+gallaryImage.getURL();
                        }
                    }
                }
            }
        }
        return imageURL;
    }
}
