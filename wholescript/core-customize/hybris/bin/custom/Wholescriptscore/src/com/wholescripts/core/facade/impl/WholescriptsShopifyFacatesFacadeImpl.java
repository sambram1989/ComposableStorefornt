package com.wholescripts.core.facade.impl;

import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wholescripts.core.facade.WholescriptsShopifyFacatesFacade;
import com.wholescripts.core.facade.WholescriptsUserFacade;
import com.wholescripts.core.model.IngredientModel;
import com.wholescripts.core.service.WholescriptsProductsService;
import com.wholescripts.data.IngredientData;
import com.wholescripts.data.ShopifyFacetData;
import com.wholescripts.data.ShopifyFacetValueData;
import com.wholescripts.data.ShopifyProductsData;
import com.wholescripts.data.ShopifySearchResultData;
import com.wholescripts.data.WholescriptsProductsFamilyData;


public class WholescriptsShopifyFacatesFacadeImpl implements WholescriptsShopifyFacatesFacade {

    private static final Logger LOG = LoggerFactory.getLogger(WholescriptsShopifyFacatesFacadeImpl.class);

    private static final String NOT_IMPORTED = "Not Imported";
    private static final String IMPORTED_PRODUCTS = "Imported Products";
    private static final String FAILED_IMPORT = "Failed to Import";
    private static final String PENDING_IMPORT = "Pending Import";
    private static final String NEW = "New";
    private static final String FEATURED = "Featured";

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

    @Override
    public void buildShopifyFacate(final List<ProductModel> productModels, final ShopifySearchResultData shopifySearchResult) {

        final List<ShopifyProductsData> shopifyProducts = shopifySearchResult.getShopifyResults();
        final ShopifyFacetData shopifyFacets = new ShopifyFacetData();

        buildImportStatusFacet(shopifyProducts, shopifyFacets);
        buildStatusFacet(shopifyProducts, shopifyFacets);
        buildCategoriesFacet(shopifyProducts, shopifyFacets);
        buildBrandFacet(shopifyProducts, shopifyFacets);
        buildProductGroupFacet(shopifyProducts, shopifyFacets);
        buildDeliveryFormFacet(shopifyProducts, shopifyFacets);

        shopifySearchResult.setShopifyFacets(shopifyFacets);


    }

    private void buildBrandFacet(final List<ShopifyProductsData> shopifyProducts, final ShopifyFacetData shopifyFacets) {

        final Set<ShopifyFacetValueData> brandFacetValues = new HashSet<ShopifyFacetValueData>();
        final Map<String, String> brandValues = new HashMap<String, String>();

        for (final ShopifyProductsData shopifyProduct : shopifyProducts) {
            final Map<String, String> brandMap = shopifyProduct.getBrandValues();
            if (MapUtils.isNotEmpty(brandMap)) {
                for (final var entry : brandMap.entrySet()) {
                    brandValues.put(entry.getKey(), entry.getValue());
                }
            }
        }

        if (MapUtils.isNotEmpty(brandValues)) {
            for (final var entry : brandValues.entrySet()) {
                final ShopifyFacetValueData brandFacetValue = new ShopifyFacetValueData();
                brandFacetValue.setCode(entry.getKey());
                brandFacetValue.setName(entry.getValue());

                brandFacetValues.add(brandFacetValue);
            }
        }

        if (CollectionUtils.isNotEmpty(brandFacetValues)) {
            shopifyFacets.setBrand(brandFacetValues.stream().collect(Collectors.toList()));
        }

    }

    private void buildProductGroupFacet(final List<ShopifyProductsData> shopifyProducts, final ShopifyFacetData shopifyFacets) {

        final Set<ShopifyFacetValueData> productGroupFacetValues = new HashSet<ShopifyFacetValueData>();
        final Map<String, String> productGroupValues = new HashMap<String, String>();

        for (final ShopifyProductsData shopifyProduct : shopifyProducts) {
            final Map<String, String> productGroupMap = shopifyProduct.getProductGroup();
            if (MapUtils.isNotEmpty(productGroupMap)) {
                for (final var entry : productGroupMap.entrySet()) {
                    productGroupValues.put(entry.getKey(), entry.getValue());
                }
            }
        }

        if (MapUtils.isNotEmpty(productGroupValues)) {
            for (final var entry : productGroupValues.entrySet()) {
                final ShopifyFacetValueData productgroupFacetValue = new ShopifyFacetValueData();
                productgroupFacetValue.setCode(entry.getKey());
                productgroupFacetValue.setName(entry.getValue());

                productGroupFacetValues.add(productgroupFacetValue);
            }
        }

        if (CollectionUtils.isNotEmpty(productGroupFacetValues)) {
            shopifyFacets.setProductGroup(productGroupFacetValues.stream().collect(Collectors.toList()));
        }

    }


    private void buildDeliveryFormFacet(final List<ShopifyProductsData> shopifyProducts, final ShopifyFacetData shopifyFacets) {

        final Set<ShopifyFacetValueData> deliveryFormFacetValues = new HashSet<ShopifyFacetValueData>();
        final Map<String, String> deliveryFormValues = new HashMap<String, String>();

        for (final ShopifyProductsData shopifyProduct : shopifyProducts) {
            final Map<String, String> deliveryForm = shopifyProduct.getDeliveryForm();
            if (MapUtils.isNotEmpty(deliveryForm)) {
                for (final var entry : deliveryForm.entrySet()) {
                    deliveryFormValues.put(entry.getKey(), entry.getValue());
                }
            }
        }

        if (MapUtils.isNotEmpty(deliveryFormValues)) {
            for (final var entry : deliveryFormValues.entrySet()) {
                final ShopifyFacetValueData deliveryFormFacetValue = new ShopifyFacetValueData();
                deliveryFormFacetValue.setCode(entry.getKey());
                deliveryFormFacetValue.setName(entry.getValue());

                deliveryFormFacetValues.add(deliveryFormFacetValue);
            }
        }

        if (CollectionUtils.isNotEmpty(deliveryFormFacetValues)) {
            shopifyFacets.setDeliveryForm(deliveryFormFacetValues.stream().collect(Collectors.toList()));
        }

    }

    private void buildCategoriesFacet(final List<ShopifyProductsData> shopifyProducts, final ShopifyFacetData shopifyFacets) {

        final Set<ShopifyFacetValueData> categoryFacetValues = new HashSet<ShopifyFacetValueData>();
        final Map<String, String> totalCategories = new HashMap<String, String>();

        for (final ShopifyProductsData shopifyProduct : shopifyProducts) {
            final Map<String, String> categories = shopifyProduct.getCategories();
            if (MapUtils.isNotEmpty(categories)) {
                for (final var entry : categories.entrySet()) {
                    totalCategories.put(entry.getKey(), entry.getValue());
                }
            }
        }

        if (MapUtils.isNotEmpty(totalCategories)) {
            for (final var entry : totalCategories.entrySet()) {
                final ShopifyFacetValueData categoryFacetValue = new ShopifyFacetValueData();
                categoryFacetValue.setCode(entry.getKey());
                categoryFacetValue.setName(entry.getValue());

                categoryFacetValues.add(categoryFacetValue);
            }
        }

        if (CollectionUtils.isNotEmpty(categoryFacetValues)) {
            shopifyFacets.setCategories(categoryFacetValues.stream().collect(Collectors.toList()));
        }

    }

    private void buildImportStatusFacet(final List<ShopifyProductsData> shopifyProducts, final ShopifyFacetData shopifyFacets) {

        final Set<ShopifyFacetValueData> shopifyFacetValues = new HashSet<ShopifyFacetValueData>();

        final Optional<ShopifyProductsData> notImportOptional = shopifyProducts.stream()
                .filter(shopifyProduct -> shopifyProduct.getImportStatus().equalsIgnoreCase(NOT_IMPORTED)).findAny();

        if (notImportOptional.isPresent()) {

            final ShopifyFacetValueData facetValue1 = new ShopifyFacetValueData();
            facetValue1.setCode(NOT_IMPORTED);
            facetValue1.setName(NOT_IMPORTED);
            shopifyFacetValues.add(facetValue1);
        }

        final Optional<ShopifyProductsData> importeOptional = shopifyProducts.stream()
                .filter(shopifyProduct -> shopifyProduct.getImportStatus().equalsIgnoreCase(IMPORTED_PRODUCTS)).findAny();

        if (importeOptional.isPresent()) {
            final ShopifyFacetValueData facetValue2 = new ShopifyFacetValueData();
            facetValue2.setCode(IMPORTED_PRODUCTS);
            facetValue2.setName(IMPORTED_PRODUCTS);
            shopifyFacetValues.add(facetValue2);
        }

        final Optional<ShopifyProductsData> failedImportOptional = shopifyProducts.stream()
                .filter(shopifyProduct -> shopifyProduct.getImportStatus().equalsIgnoreCase(FAILED_IMPORT)).findAny();

        if (failedImportOptional.isPresent()) {
            final ShopifyFacetValueData facetValue3 = new ShopifyFacetValueData();
            facetValue3.setCode(FAILED_IMPORT);
            facetValue3.setName(FAILED_IMPORT);
            shopifyFacetValues.add(facetValue3);
        }

        final Optional<ShopifyProductsData> pendingImportOptional = shopifyProducts.stream()
                .filter(shopifyProduct -> shopifyProduct.getImportStatus().equalsIgnoreCase(PENDING_IMPORT)).findAny();

        if (pendingImportOptional.isPresent()) {
            final ShopifyFacetValueData facetValue4 = new ShopifyFacetValueData();
            facetValue4.setCode(PENDING_IMPORT);
            facetValue4.setName(PENDING_IMPORT);
            shopifyFacetValues.add(facetValue4);
        }

        if (CollectionUtils.isNotEmpty(shopifyFacetValues)) {
            shopifyFacets.setImportStatus(shopifyFacetValues.stream().collect(Collectors.toList()));
        }

    }

    private void buildStatusFacet(final List<ShopifyProductsData> shopifyProducts, final ShopifyFacetData shopifyFacets) {

        final Set<ShopifyFacetValueData> shopifyFacetValues = new HashSet<ShopifyFacetValueData>();

        final Optional<ShopifyProductsData> isNewOptional = shopifyProducts.stream()
                .filter(shopifyProduct -> NEW.equalsIgnoreCase(shopifyProduct.getIsNew())).findAny();

        if (isNewOptional.isPresent()) {

            final ShopifyFacetValueData facetValue1 = new ShopifyFacetValueData();
            facetValue1.setCode(NEW);
            facetValue1.setName(NEW);
            shopifyFacetValues.add(facetValue1);
        }

        final Optional<ShopifyProductsData> isFeaturedOptional = shopifyProducts.stream()
                .filter(shopifyProduct -> FEATURED.equalsIgnoreCase(shopifyProduct.getIsFeatured())).findAny();

        if (isFeaturedOptional.isPresent()) {
            final ShopifyFacetValueData facetValue2 = new ShopifyFacetValueData();
            facetValue2.setCode(FEATURED);
            facetValue2.setName(FEATURED);
            shopifyFacetValues.add(facetValue2);
        }

        if (CollectionUtils.isNotEmpty(shopifyFacetValues)) {
            shopifyFacets.setStatus(shopifyFacetValues.stream().collect(Collectors.toList()));
        }

    }


}
