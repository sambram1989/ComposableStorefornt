package com.wholescripts.core.facade.impl;

import com.wholescripts.core.facade.WholescriptsCoreFacade;
import com.wholescripts.core.model.DosingModel;
import com.wholescripts.core.model.WebinarModel;
import com.wholescripts.core.model.ItemTimeModel;
import com.wholescripts.core.model.PresenterModel;
import com.wholescripts.core.model.WholescriptsBrandModel;
import com.wholescripts.core.model.WholescriptsTopicModel;
import com.wholescripts.core.model.WholescriptsFaqListModel;
import com.wholescripts.core.model.WSContactUsFormModel;
import com.wholescripts.core.populators.WholescriptsBrandPopulator;
import com.wholescripts.core.service.WholescriptsCoreService;
import com.wholescripts.data.*;
import com.wholescripts.dto.DosingData;
import com.wholescripts.dto.ItemTimeData;
import com.wholescripts.dto.WholescriptsBrandData;
import com.wholescripts.dto.dosing.DosingRequestWsDto;
import com.wholescripts.dto.WholescriptsTopicData;
import com.wholescripts.dto.WSContactUsFormWsDTO;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class WholescriptsCoreFacadeImpl implements WholescriptsCoreFacade {
	@Resource(name = "wScriptDosingConverter")
	private Converter<DosingModel, DosingData> wScriptDosingConverter;
	@Resource(name = "wholescriptsCoreService")
	private WholescriptsCoreService wholescriptsCoreService;
	@Resource(name = "productFacade")
	private ProductFacade productFacade;
	@Resource(name = "wScriptsItemTimeConverter")
	private Converter<ItemTimeModel, ItemTimeData> wScriptsItemTimeConverter;
	@Resource(name = "wScriptsTopicConverter")
	private Converter<WholescriptsTopicModel, WholescriptsTopicData> wScriptsTopicConverter;
	@Resource(name = "wsFaqConverter")
	private Converter<WholescriptsFaqListModel, FaqListData> wsFaqConverter;
	@Resource(name = "wsWebinarConverter")
	private Converter<WebinarModel, WholescriptsWebinarData> wsWebinarConverter;

	@Resource(name = "wholescriptsPresenterConverter")
	private Converter<PresenterModel, WholescriptsPresenterData> wholescriptsPresenterConverter;

	@Resource(name = "brandPopulator")
	private WholescriptsBrandPopulator brandPopulator;
    @Resource(name="categoryService")
	private CategoryService categoryService;
	@Resource(name = "categoryConverter")
	private Converter<CategoryModel, CategoryData> categoryConverter;

	@Resource(name = "catalogVersionService")
	private CatalogVersionService catalogVersionService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "klaviyoCatalogFeedConverter")
	private Converter<ProductModel, KlaviyoCatalogFeedData> klaviyoCatalogFeedConverter;

    @Override
    public List<DosingData> saveDosingEntries(final DosingRequestWsDto dosingRequest) {
        return wScriptDosingConverter.convertAll(wholescriptsCoreService.saveDosingEntries(dosingRequest));
    }

    @Override
    public List<DosingData> getDosingEntries(final String productId){
   	 return wScriptDosingConverter.convertAll(wholescriptsCoreService.getDosingEntries(productId));
    }

    @Override
    public List<ItemTimeData> getAllActiveItemTimes() {
        final List<ItemTimeModel> itemTimeModelList = wholescriptsCoreService.getAllActiveItemTimes();
        return wScriptsItemTimeConverter.convertAll(itemTimeModelList);
    }

	 @Override
	 public List<WholescriptsTopicData> getTopicDetails(final String page, final String childTopic)
	 {
		 final List<WholescriptsTopicModel> topicModelList = wholescriptsCoreService.getAllTopicDetails(page, childTopic);
		 return wScriptsTopicConverter.convertAll(topicModelList);
	 }

	 @Override
    public WSContactUsFormModel save(final WSContactUsFormWsDTO input ) {
		 final WSContactUsFormModel formModel = wholescriptsCoreService.saveForm(input);
   	 return formModel;
    }

	 @Override
	 public List<FaqListData> getFaqQusAnsDetails()
	 {
		 final List<WholescriptsFaqListModel> faqList = wholescriptsCoreService.getFaqQusAnsDetails();
		 return wsFaqConverter.convertAll(faqList);
	 }

	 @Override
	 public FaqListData getFaqListByUid(final String uid) {
		final WholescriptsFaqListModel faqListModel = wholescriptsCoreService.getFaqListByUid(uid);
		return wsFaqConverter.convert(faqListModel);
	 }

	@Override
	public void updateConsent(String userId, boolean isSmsEnabled, boolean isEmailEnabled) {
		wholescriptsCoreService.updateConsent(userId, isSmsEnabled, isEmailEnabled);
	}

	public SearchPageData<ProductData> getDiscontinuedProducts(final SearchPageData searchPageData) {
		final SearchPageData<ProductModel> searchData = wholescriptsCoreService.getDiscontinuedProducts(searchPageData);
		return convertToProductDataSearchPage(searchData);
	}

	public SearchPageData<ProductData> getAvailableWhileSuppliesLastProducts(final SearchPageData searchPageData) {
		final SearchPageData<ProductModel> searchData = wholescriptsCoreService.getAvailableWhileSuppliesLastProducts(searchPageData);
		return convertToProductDataSearchPage(searchData);
	}

	private SearchPageData<ProductData> convertToProductDataSearchPage(SearchPageData<ProductModel> searchData) {
		final SearchPageData<ProductData> result = new SearchPageData<>();
		final List<ProductData> productDataList = searchData.getResults().stream()
				.map(productModel -> getProductData(productModel))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(toList());
		result.setResults(productDataList);
		result.setPagination(searchData.getPagination());
		result.setSorts(searchData.getSorts());
		return result;
	}

	 public Optional<ProductData> getProductData(final ProductModel productModel)
	 {
		 return Optional
				 .of(productFacade.getProductForOptions(productModel,
						 Arrays.asList(ProductOption.BASIC, ProductOption.SUMMARY, ProductOption.PRODUCT_UPDATES)));
	 }

	 @Override
	 public WholescriptsWebinarData getfeaturedWebinar()
	 {
		 final WebinarModel featuredWebinar = wholescriptsCoreService.getfeaturedWebinar();
		 WholescriptsWebinarData wholescriptsWebinarData = new WholescriptsWebinarData();
		 if (null != featuredWebinar)
		 {
			 wholescriptsWebinarData = wsWebinarConverter.convert(featuredWebinar);
		 }
		 return wholescriptsWebinarData;
	 }

	@Override
	public WholescriptsWebinarData getWebinarDetails(final String  webinarCode) {
		final WebinarModel webinar=wholescriptsCoreService.getWebinarDetails(webinarCode);
		WholescriptsWebinarData wholescriptsWebinarData = new WholescriptsWebinarData();
		if (null != webinar)
		{
			wholescriptsWebinarData = wsWebinarConverter.convert(webinar);
		}
		return wholescriptsWebinarData;
	}

	@Override
	public WholescriptsPresenterData getPresenterDetails(String presenterCode) {
		final PresenterModel presenter=wholescriptsCoreService.getPresenterDetails(presenterCode);
		WholescriptsPresenterData wholescriptsWebinarData = new WholescriptsPresenterData();
		if (null != presenter)
		{
			wholescriptsWebinarData = wholescriptsPresenterConverter.convert(presenter);
		}
		return wholescriptsWebinarData;
	}

	@Override
	public List<WholescriptsPresenterData> getAllPresenterDetails() {
		final List<PresenterModel > presenter=wholescriptsCoreService.getAllPresenterDetails();
		if (null != presenter)
		{
			return  wholescriptsPresenterConverter.convertAll(presenter);
		}
		return Collections.EMPTY_LIST;
	}

	@Override
	public WholescriptsCategoryOrBrandData getCategoryOrBrandDetails(String categoryCode, String brandCode) {

		WholescriptsCategoryOrBrandData wsCategoryOrBrandData=new WholescriptsCategoryOrBrandData();
		if (StringUtils.isNotEmpty(categoryCode)) {
			CategoryModel category = categoryService.getCategoryForCode(categoryCode);
			CategoryData categoryData = categoryConverter.convert(category);

			if(Objects.nonNull(categoryData) && StringUtils.isNotEmpty(categoryData.getRetailName())){
				categoryData.setRetailName(category.getRetailName());
			}
			wsCategoryOrBrandData.setCategoryDetails(categoryData);

		} else if (StringUtils.isNotEmpty(brandCode)) {
			final WholescriptsBrandData brandData = new WholescriptsBrandData();
			WholescriptsBrandModel brandModel=	wholescriptsCoreService.getBrandForCode(brandCode);
			brandPopulator.populate(brandModel, brandData);
			wsCategoryOrBrandData.setBrandDetails(brandData);

		}
		return wsCategoryOrBrandData;
	}

	@Override
	public DashboardProductsData getBestSellerProducts()
	{
		Integer limit = 10 ;
		DashboardProductsData dashboardProductsData = new DashboardProductsData();
		List<ProductModel> buyAgainProducts = wholescriptsCoreService.getProductsFromPractitionerOrders(limit);
		final List<ProductOption> options = getProductOptionsForDisplay();

		List<ProductData> buyAgainProductsData = buyAgainProducts.stream()
				.map(productModel -> productFacade.getProductForOptions(productModel,options))
				.filter(Objects::nonNull).collect(toList());
		dashboardProductsData.setBuyAgainProducts(buyAgainProductsData);

		Set<String> buyAgainProductCodes = buyAgainProducts.stream().map(ProductModel::getCode).collect(Collectors.toSet());

		int remainingLimit = limit - buyAgainProducts.size();
		if (remainingLimit > 0) {
			List<ProductModel> bestSellerProducts = wholescriptsCoreService.getBestSellerProducts(remainingLimit)
					.stream().filter(product -> !buyAgainProductCodes.contains(product.getCode())).collect(toList());

			List<ProductData> bestSellerProductsData = bestSellerProducts.stream()
					.map(productModel -> productFacade.getProductForOptions(productModel, options))
					.filter(Objects::nonNull).collect(toList());
			dashboardProductsData.setBestSellerProducts(bestSellerProductsData);
		}
		return dashboardProductsData;
	}

	public List<KlaviyoCatalogFeedData> getSelectedProductsDataForKlaviyo(final List<String> productSkus) {
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(
				configurationService.getConfiguration().getString("wholescripts.product.catalog"),
				configurationService.getConfiguration().getString("wholescripts.product.catalog.version"));
		List<ProductModel> productModelList = wholescriptsCoreService.getSelectedProductsDataForKlaviyo(productSkus, catalogVersion);
		if (productModelList == null || productModelList.isEmpty()) {
			return Collections.emptyList();
		}
		return klaviyoCatalogFeedConverter.convertAll(productModelList);
	}

	public List<KlaviyoCatalogFeedData> getAllProductsForKlaviyo() {
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(
				configurationService.getConfiguration().getString("wholescripts.product.catalog"),
				configurationService.getConfiguration().getString("wholescripts.product.catalog.version"));
		List<ProductModel> productModelList = wholescriptsCoreService.getAllProductsForKlaviyo(catalogVersion);
		if (productModelList == null || productModelList.isEmpty()) {
			return Collections.emptyList();
		}
		return klaviyoCatalogFeedConverter.convertAll(productModelList);
	}

	protected List<ProductOption> getProductOptionsForDisplay() {
		return new ArrayList<>(Arrays.asList(ProductOption.VARIANT_FIRST_VARIANT, ProductOption.BASIC,
				ProductOption.URL, ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.GALLERY,
				ProductOption.CATEGORIES, ProductOption.REVIEW, ProductOption.PROMOTIONS, ProductOption.CLASSIFICATION,
				ProductOption.VARIANT_FULL, ProductOption.STOCK, ProductOption.VOLUME_PRICES, ProductOption.PRICE_RANGE,
				ProductOption.DELIVERY_MODE_AVAILABILITY, ProductOption.IMAGES));
	}

	@Override
	public void updateSMSNotification(String status, String phoneNumber) {
		if ("X".equalsIgnoreCase(status)) {
			wholescriptsCoreService.updateSMSNotification(phoneNumber);
		}
	}

	@Override
	public void changeUid(String newUid) throws DuplicateUidException {
		wholescriptsCoreService.changeUid(newUid);
	}

	@Override
	public void updateVideoWatchStatus(String userId, String webinarCode) {
		wholescriptsCoreService.updateVideoWatchStatus(userId,webinarCode);
	}
}
