package com.wholescripts.core.facade.impl;

import com.wholescripts.core.model.IngredientReferenceModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.impl.DefaultProductFacade;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.commercefacades.product.data.ImageData;


import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.wholescripts.core.constants.WholescriptscoreConstants;
import com.wholescripts.core.facade.WholescriptsProductsFacade;
import com.wholescripts.core.facade.WholescriptsShopifyFacatesFacade;
import com.wholescripts.core.facade.WholescriptsUserFacade;
import com.wholescripts.core.model.IngredientModel;
import com.wholescripts.core.service.WholescriptsProductsService;
import com.wholescripts.data.IngredientData;
import com.wholescripts.data.ShopifyProductData;
import com.wholescripts.data.ShopifyProductDetailsData;
import com.wholescripts.data.ShopifyProductsData;
import com.wholescripts.data.ShopifySearchResultData;
import com.wholescripts.data.WholescriptsProductsFamilyData;

public class WholescriptsProductsFacadeImpl extends DefaultProductFacade<ProductModel> implements WholescriptsProductsFacade {

    private static final Logger LOG = LoggerFactory.getLogger(WholescriptsProductsFacadeImpl.class);

    private static final String SHOPIFY_NOT_IMPORTED = "Not Imported";
    private static final String PL_PRODUCT_IMAGE_NAME="wholescripts.pl.product.image";
    private static final String MEDIA_URL="media.wholescripts.https";
    private static final String IMAGE_TYPE_PRODUCT = "product";
    private static final String VALID_PDP_MATERIAL_CODES = "wholescripts.valid.pdp.product.material.codes";

    @Resource(name = "wholescriptsProductsService")
    WholescriptsProductsService wholescriptsProductsService;

    @Resource(name = "wholescriptsProductsFamilyConverter")
    private Converter<ProductModel, WholescriptsProductsFamilyData> wholescriptsProductsFamilyConverter;

    @Resource(name = "productFacade")
    private ProductFacade productFacade;

    @Resource(name = "wholescriptsSupplementIngredientsConverter")
    private Converter<IngredientModel, IngredientData> wholescriptsSupplementIngredientsConverter;

    @Resource(name = "sessionService")
    private SessionService sessionService;

    @Resource(name = "wsShopifyProductConverter")
    private Converter<ProductModel, ShopifyProductsData> wsShopifyProductConverter;

    @Resource(name = "wholescriptsUserFacade")
    private WholescriptsUserFacade wholescriptsUserFacade;
    
    @Autowired
    private WholescriptsShopifyFacatesFacade wholescriptsShopifyFacatesFacade;

    @Resource(name = "mediaService")
    private MediaService mediaService;

    @Resource(name = "catalogVersionService")
    private CatalogVersionService catalogVersionService;

    @Resource(name = "configurationService")
    private ConfigurationService configurationService;

    @Resource(name = "productService")
    private ProductService productService;
    @Resource(name = "modelService")
    ModelService modelService;


    @Override
    public List<WholescriptsProductsFamilyData> getAllProductFamilies(final String productCode) {
        return wholescriptsProductsFamilyConverter.convertAll(wholescriptsProductsService.getAllProductFamiliesData(productCode));
    }

    @Override
    public ProductData getProductDetailsByCode(final String productId) {
        final List<ProductOption> options = getProductOptionsForDisplay();
        ProductModel productModel = wholescriptsProductsService.getProductDetailsByCode(productId);
        ProductData productData = productFacade.getProductForOptions(productModel, options);

       final ProductModel privateLabelProduct = productService.getProductForCode(productId);
        if (privateLabelProduct.getSapMaterialGroup() != null && "1000014".equalsIgnoreCase(privateLabelProduct.getSapMaterialGroup().getCode())) {
            final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(
                    configurationService.getConfiguration().getString("wholescripts.product.catalog"),
                    configurationService.getConfiguration().getString("wholescripts.product.catalog.version"));

            final String plDefaultImageName = configurationService.getConfiguration().getString(PL_PRODUCT_IMAGE_NAME);
            final MediaModel plDefaultImage = mediaService.getMedia(catalogVersion, plDefaultImageName);

            if (plDefaultImage != null) {
                ImageData privateLabelImageData = new ImageData();
                privateLabelImageData.setAltText(privateLabelProduct.getName());
                privateLabelImageData.setFormat(IMAGE_TYPE_PRODUCT);
                privateLabelImageData.setUrl(configurationService.getConfiguration().getString(MEDIA_URL) + plDefaultImage.getURL());
                productData.setPrivateLabelImage(privateLabelImageData);
            }
        }
        return productData;
    }


    @Override
    public List<IngredientData> getIngredientsByProductCode(List<String> productCodes) {
        Map<String, IngredientData> mergedIngredients = new HashMap<>();

        for (String code : productCodes) {
            ProductModel product = productService.getProductForCode(code);
            if (product != null && product.getIngredientReferences() != null) {
                Map<String, List<IngredientReferenceModel>> grouped = product.getIngredientReferences().stream()
                        .filter(ref -> ref.getIngredient() != null)
                        .collect(Collectors.groupingBy(ref -> ref.getIngredient().getCode()));

                for (Map.Entry<String, List<IngredientReferenceModel>> entry : grouped.entrySet()) {
                    IngredientModel model = entry.getValue().get(0).getIngredient();
                    double totalUnits = entry.getValue().stream().filter(r -> r.getUnits() != null)
                            .mapToDouble(IngredientReferenceModel::getUnits).sum();

                    IngredientData data = mergedIngredients.getOrDefault(model.getCode(), wholescriptsSupplementIngredientsConverter.convert(model));
                    double newTotal = (data.getIngredientReferenceUnit() != null ? data.getIngredientReferenceUnit() : 0.0) + totalUnits;
                    data.setIngredientReferenceUnit(newTotal);

                    Double rda = model.getRda();
                    if (rda != null && rda > 0 && newTotal > 0) {
                        int percentageDV = (int) Math.round((newTotal / rda) * 100);
                        data.setPercentageDV(percentageDV);
                    }

                    mergedIngredients.put(model.getCode(), data);
                }
            }
        }

        return new ArrayList<>(mergedIngredients.values());
    }



    private List<ProductOption> getProductOptionsForDisplay() {
        return new ArrayList<>(Arrays.asList(ProductOption.VARIANT_FIRST_VARIANT, ProductOption.BASIC,
                ProductOption.URL, ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.GALLERY,
                ProductOption.CATEGORIES, ProductOption.REVIEW, ProductOption.PROMOTIONS, ProductOption.CLASSIFICATION,
                ProductOption.VARIANT_FULL, ProductOption.STOCK, ProductOption.VOLUME_PRICES, ProductOption.PRICE_RANGE,
                ProductOption.DELIVERY_MODE_AVAILABILITY, ProductOption.IMAGES));
    }

    @Override
    public ShopifySearchResultData searchShopifyProducts(final String accountNumber) {
        final ShopifySearchResultData shopifySearchResult = new ShopifySearchResultData();
        final List<ProductModel> productModels = wholescriptsProductsService.getProductsForShopify(accountNumber);
        final List<ShopifyProductsData> shopifyProductDetails = wsShopifyProductConverter.convertAll(productModels);
        populateShopifyStatus(shopifyProductDetails);
        shopifySearchResult.setShopifyResults(shopifyProductDetails);
        wholescriptsShopifyFacatesFacade.buildShopifyFacate(productModels, shopifySearchResult);
        return shopifySearchResult;
    }

    private void populateShopifyStatus(final List<ShopifyProductsData> shopifyProductDetails) {

        try {

            final ShopifyProductDetailsData shopifyResponse = wholescriptsUserFacade.getShopifyProducts();
            final List<ShopifyProductData> shopifyProducts = shopifyResponse.getProducts();
            for (final ShopifyProductsData result : shopifyProductDetails) {
                result.setImportStatus(SHOPIFY_NOT_IMPORTED);
                for (final ShopifyProductData shopifyProduct : shopifyProducts) {
                    if (result.getCode().equalsIgnoreCase(shopifyProduct.getSku())) {
                        LOG.debug("Search product code is " + result.getCode() + " and shopify Product code is "
                                + shopifyProduct.getSku());

                        final String shopifyId = shopifyProduct.getShopifyProductId();
                        LOG.debug("shopifyId is " + shopifyId + " and shopify Product code is " + shopifyProduct.getSku());
                        result.setImportStatus(shopifyProduct.getStatus());
                        break;
                    }

                }
            }

        } catch (final Exception e) {
            LOG.error("Error during populating shopify status", e.getMessage());
        }

    }
    
	 @Override
	 public ProductData getProductForCodeAndOptions(final String code, final Collection<ProductOption> options) {

		 final ProductModel productModel = getProductService().getProductForCode(code);

		 final String validPDPMaterialGroups = configurationService.getConfiguration().getString(VALID_PDP_MATERIAL_CODES);
		 final String[] validMaterialGroups = validPDPMaterialGroups.split(WholescriptscoreConstants.COMMA);
		 final List<String> validMaterialGroupsList = Arrays.asList(validMaterialGroups);

		 if (!validMaterialGroupsList.contains(productModel.getSapMaterialGroup().getCode())) {
			 throw new IllegalArgumentException("Product code is not valid for detail page");
		 }

		 final ProductData productData = super.getProductConverter().convert(productModel);

		 if (options != null) {
			 getProductConfiguredPopulator().populate(productModel, productData, options);
		 }

		 return productData;
	 }

}
