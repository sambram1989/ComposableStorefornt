package com.wholescripts.core.populators;

import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.StockData;
import de.hybris.platform.commerceservices.product.CommerceProductService;
import de.hybris.platform.commerceservices.stock.strategies.WarehouseSelectionStrategy;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.Config;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.wholescripts.core.constants.WholescriptscoreConstants;
import com.wholescripts.core.model.WholescriptsBrandModel;
import com.wholescripts.core.model.WsPopularProductsModel;
import com.wholescripts.core.service.WholescriptsPopularCategoriesService;
import com.wholescripts.core.service.WholescriptsWishlist2Service;
import com.wholescripts.core.utils.WholescriptsCoreUtils;
import com.wholescripts.data.ShopifyProductsData;


public class WholescriptsShopifyProductPopulator implements Populator<ProductModel, ShopifyProductsData> {

    private static final String WHOLESALE_GROUP = "wholescripts.wholesale.group";
    private static final String MAP_GROUP = "wholescripts.guest.group";
    private static final String SHORT_DESCRIPTION = "wholescripts.pdp.shortdescription.classification.code";
    private static final String PRODUCT_FORM = "wholescripts.pdp.productform.classification.code";
    private static final String BEST_SELLAR_COUNT = "wholescripts.bestSeller.products.count";
    private static final String IS_NEW = "New";
    private static final String IS_FEATURED = "Featured";

    @Resource(name = "classificationService")
    private ClassificationService classificationService;

    @Resource(name = "configurationService")
    private ConfigurationService configurationService;

    @Resource(name = "priceDataFactory")
    private PriceDataFactory priceDataFactory;

    @Autowired
    private CommonI18NService commonI18NService;

    @Resource(name = "commerceProductService")
    private CommerceProductService commerceProductService;

    @Resource(name = "wholescriptsPopularCategoriesService")
    private WholescriptsPopularCategoriesService wholescriptsPopularCategoriesService;
    
    @Resource(name = "enumerationService")
    protected EnumerationService enumerationService;
    
    @Resource(name = "baseStoreService")
    private BaseStoreService baseStoreService;

    @Resource(name = "stockService")
    private StockService stockService;
    
    @Autowired
    private Converter<ProductModel, StockData> stockConverter;
    
    @Autowired
    private Converter<StockLevelStatus, StockData> stockLevelStatusConverter;
    
    @Autowired
    private ProductService productService;

    private String categoriesQualifier;
    
    private UrlResolver<ProductModel> urlResolver;
    
    @Resource(name = "wholescriptsWishlist2Service")
    private WholescriptsWishlist2Service wholescriptsWishlist2Service;
    
    @Resource(name = "userService")
    private UserService userService;

    @Override
    public void populate(final ProductModel source, final ShopifyProductsData productData) throws ConversionException {

        if (StringUtils.isNotEmpty(source.getCode())) {
            productData.setCode(source.getCode());
        }

        if (source.getPicture() != null) {
            productData.setImage(source.getPicture().getURL());
        }
        final FeatureList featureList = classificationService.getFeatures(source);

        populateName(featureList, source, productData);

        populatePrices(source, productData);

        final WholescriptsBrandModel brandModel = source.getWholescriptsBrand();
        if (brandModel != null) {
      	   final Map<String, String> brnadMap = new HashMap<String, String>();
      	   brnadMap.put(brandModel.getCode(), brandModel.getName());
            productData.setBrandValues(brnadMap);
            productData.setBrandName(brandModel.getName());
            productData.setBrandUrl(brandModel.getBrandUrl());
        }

        populateShortDescription(featureList, source, productData);

        setBestSellerProduct(source, productData);

        populateCategories(source, productData);

        if (source.getSapMaterialGroup() != null) {
      	   final Map<String, String> materialGroupMap = new HashMap<String, String>();
      	   final String groupName = enumerationService.getEnumerationName(source.getSapMaterialGroup());
      	   materialGroupMap.put(source.getSapMaterialGroup().getCode(), groupName);
            productData.setProductGroup(materialGroupMap);
        }

        populateProductForm(featureList, source, productData);

        if (source.getIsFeatured()) {
            productData.setIsFeatured(IS_FEATURED);
        }

        if (source.getIsNew()) {
            productData.setIsNew(IS_NEW);
        }
        
        productData.setProductUrl(getUrlResolver().resolve(source));
        
        populateStockDetails(source, productData);
        
        setFavoriteProduct(source, productData);

    }

    private void populateCategories(final ProductModel source, final ShopifyProductsData productData) {
        final Collection<CategoryModel> productCategories = commerceProductService
                .getSuperCategoriesExceptClassificationClassesForProduct(source);
        if (productCategories != null && !productCategories.isEmpty()) {
            final Map<String, String> categories = new HashMap<String, String>();
            for (final CategoryModel directCategory : productCategories) {
                categories.put(directCategory.getCode(), directCategory.getName());
            }
            productData.setCategories(categories);
        }
    }


    private void populateName(final FeatureList featureList, final ProductModel source, final ShopifyProductsData productData) {

        final Feature pimProductName = featureList.getFeatureByCode(
                configurationService.getConfiguration().getString(WholescriptscoreConstants.CLASSIFICATION_ATTRIBUTE_PRODUCT_NAME));
        String productName = null;
        if (Objects.nonNull(pimProductName)) {
            final FeatureValue productNameValue = pimProductName.getValue();
            if (Objects.nonNull(productNameValue)) {
                productName = productNameValue.getValue().toString();
            } else {
                productName = source.getName();
            }
        } else {
            productName = source.getName();
        }

        if (StringUtils.isNotEmpty(productName)) {
            productData.setName(productName);
        }
    }

    private void populateShortDescription(final FeatureList featureList, final ProductModel source,
                                          final ShopifyProductsData productData) {
        final Feature shortDescriptionFeature = featureList
                .getFeatureByCode(configurationService.getConfiguration().getString(SHORT_DESCRIPTION));
        if (Objects.nonNull(shortDescriptionFeature)) {
            final FeatureValue shortDescriptionValue = shortDescriptionFeature.getValue();
            if (Objects.nonNull(shortDescriptionValue)) {
                final Object attributeValue = shortDescriptionValue.getValue();
                final String shortDescription = attributeValue.toString();
                productData.setShortDescription(shortDescription);
            }
        }
    }

    private void populateProductForm(final FeatureList featureList, final ProductModel source,
                                     final ShopifyProductsData productData) {
        final Feature productFormFeature = featureList
                .getFeatureByCode(configurationService.getConfiguration().getString(PRODUCT_FORM));
        
        
        if(Objects.nonNull(productFormFeature)) {
           final List<FeatureValue> productFormFeatureValues = productFormFeature.getValues();
           if (CollectionUtils.isNotEmpty(productFormFeatureValues)) {
               final Object attributeValue = productFormFeatureValues.get(0).getValue();
               final String productFormCode = ((ClassificationAttributeValueModel) attributeValue).getCode();
               final String productFormName = ((ClassificationAttributeValueModel) attributeValue).getName();
               if (StringUtils.isNotBlank(productFormCode)) {
               	final Map<String, String> productFormMap = new HashMap<String, String>();
               	productFormMap.put(productFormCode, productFormName);
               	productData.setDeliveryForm(productFormMap);
               }
           }
       }
    }

    private void populatePrices(final ProductModel source, final ShopifyProductsData productData) {

        final Collection<PriceRowModel> priceRows = source.getEurope1Prices();
        final String wholeSalePriceGroup = Config.getParameter(WHOLESALE_GROUP);
        final PriceRowModel wholesalePriceRow = priceRows.stream()
                .filter(pr -> pr.getUg() != null && wholeSalePriceGroup.equalsIgnoreCase(pr.getUg().getCode())).findAny()
                .orElse(null);
        if (wholesalePriceRow != null && wholesalePriceRow.getPrice() != null) {
            final Double wholesalePrice = wholesalePriceRow.getPrice();
            final PriceData wholesalePriceData = priceDataFactory.create(PriceDataType.BUY,
                    BigDecimal.valueOf(wholesalePrice.doubleValue()), commonI18NService.getCurrentCurrency());
            productData.setWholesalePrice(wholesalePriceData);
        }

        final String mapPriceGroup = Config.getParameter(MAP_GROUP);
        final PriceRowModel mapPriceRow = priceRows.stream()
                .filter(pr -> pr.getUg() != null && mapPriceGroup.equalsIgnoreCase(pr.getUg().getCode())).findAny().orElse(null);

        if (mapPriceRow != null && mapPriceRow.getPrice() != null) {
            final Double mapPrice = mapPriceRow.getPrice();
            final PriceData mapPriceData = priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(mapPrice.doubleValue()),
                    commonI18NService.getCurrentCurrency());
            productData.setMapPrice(mapPriceData);

        }

    }

    private void setBestSellerProduct(final ProductModel productModel, final ShopifyProductsData productData) {

        final Collection<CategoryModel> categories = commerceProductService
                .getSuperCategoriesExceptClassificationClassesForProduct(productModel);
        final String count = Config.getParameter(BEST_SELLAR_COUNT);
        long productCount = 0;
        if (StringUtils.isNotBlank(count)) {
            productCount = Long.parseLong(count);
        }
        if (CollectionUtils.isNotEmpty(categories)) {
            boolean isBestSeller = false;
            for (final CategoryModel category : categories) {
                final List<WsPopularProductsModel> bestSellerProducts = new ArrayList<WsPopularProductsModel>();

                if (category != null && !isBestSeller) {
                    final List<WsPopularProductsModel> popularProducts = wholescriptsPopularCategoriesService
                            .getPopularProductsDetails(category.getCode());

                    bestSellerProducts
                            .addAll(popularProducts.stream().sorted(Comparator.comparing(WsPopularProductsModel::getWeight).reversed())
                                    .limit(productCount).collect(Collectors.toSet()));
                    if (CollectionUtils.isNotEmpty(bestSellerProducts)) {
                        for (final WsPopularProductsModel product : bestSellerProducts) {
                            if (product != null && product.getProductCode().equals(productModel.getCode())) {
                                isBestSeller = true;
                                productData.setBestSeller(true);
                                break;
                            }
                        }
                    }
                }
            }
        }

    }
    
	 private void populateStockDetails(final ProductModel source, final ShopifyProductsData productData) {

		 final BaseStoreModel baseStore = baseStoreService.getCurrentBaseStore();
		 StockLevelModel stockLevel = stockService.getStockLevel(source, baseStore.getWarehouses().get(0));
		 if(Objects.nonNull(stockLevel)) {
			 StockLevelStatus stockLevelStatusEnum = WholescriptsCoreUtils.checkStatus(stockLevel);
			 if (StockLevelStatus.LOWSTOCK.equals(stockLevelStatusEnum)) {
				 try{
					 final StockData stockData = stockConverter.convert(source);
					 productData.setStock(stockData.getStockLevelStatus().getCode());

				 }
				 catch (final UnknownIdentifierException ex) {
					 final StockData stockData = stockLevelStatusConverter.convert(StockLevelStatus.OUTOFSTOCK);
					 productData.setStock(stockData.getStockLevelStatus().getCode());
				 }
			 }
			 else {
				 final StockData stockData = stockLevelStatusConverter.convert(stockLevelStatusEnum);
				 productData.setStock(stockData.getStockLevelStatus().getCode());
			 }
			 
		 }
	 }
	 
	 private void setFavoriteProduct(final ProductModel source, final ShopifyProductsData productData) {
		 final UserModel currentUser = userService.getCurrentUser();
		 final List<Wishlist2Model> userWishLists = wholescriptsWishlist2Service.findWishlistsByUser(currentUser);
		 final List<ProductModel> wishListProducts = new ArrayList<ProductModel>();
		 userWishLists.forEach(userWishList -> userWishList.getEntries()
				 .forEach(wishlistEntry -> wishListProducts.add(wishlistEntry.getProduct())));
		 
		 wishListProducts.forEach(wishListProduct -> {
	        if(wishListProduct.getCode().equalsIgnoreCase(source.getCode())) {
	      	  productData.setFavorite(true);
	        }
	    });
   }


    public String getCategoriesQualifier() {
        return categoriesQualifier;
    }

    public void setCategoriesQualifier(final String categoriesQualifier) {
        this.categoriesQualifier = categoriesQualifier;
    }
    
	 public UrlResolver<ProductModel> getUrlResolver() {
		 return urlResolver;
	 }


	 public void setUrlResolver(UrlResolver<ProductModel> urlResolver) {
		 this.urlResolver = urlResolver;
	 }
    
    


}
