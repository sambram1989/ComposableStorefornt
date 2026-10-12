package com.wholescripts.core.dao;


import com.wholescripts.core.model.*;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;

import java.util.List;

public interface WholescriptsCoreDao {
	List<ItemTimeModel> getItemTimeByCode(String itemTimeCode);
	List<DosingModel> getAllDosingByProductnUser(String productID, String userID);
	public List<ItemTimeModel> getAllActiveItemTimes();
	List<WholescriptsFaqListModel> getFaqQusAnsDetails();
	WholescriptsFaqListModel getFaqListByUid(final String uid);
	/**
	 *
	 */
	List<WholescriptsTopicModel> getAllTopicDetails(String page, String childTopic);
	public SearchPageData<ProductModel> getDiscontinuedProducts(final SearchPageData pageData);
	public WsDiscontinuedProductsModel getDiscontinuedProductBySku(final String sku);
	public SearchPageData<ProductModel> getAvailableWhileSuppliesLastProducts(final SearchPageData pageData);
	WholescriptsBrandModel getBrandForCode(String brandCode);

	List<WholescriptsBrandModel> getTopBrands();

	WebinarModel getfeaturedWebinar();

	WebinarModel getWebinarDetails(final String webinarCode);

	PresenterModel getPresenterDetails(String presenterCode);

	List<WebinarModel>  getRelatedWebinarsByWebCategory(final List<WebinarCategoryTagModel> webCategory, String code);

	List<WebinarModel>  getRelatedWebinarsByPresenter(String presenterCode);

	List<PresenterModel> getPresenterDetails();

	CategoryModel getCategoryByCategoryUrl(String categoryUrl);

	WholescriptsBrandModel getBrandCodeByBrandUrl(String brandurl);
	CustomerMaterialInformationModel getCustomerInfoModelForAccountAndBlueLabel(final String productCode,final String account);

	SupportedAutoShipModel getAutoShipByCode(String code);

	WholeScriptsBrightStockProductsModel getBrightStockForPLProduct(String productCode);

	WholescriptsBrandModel getBrandByName(String brandName);
	List<WsPopularProductsModel> getPopularProducts(Integer limit,CatalogVersionModel catalogVersion);

	List<ProductModel> getProductsFromPractitionerOrders(Integer limit);

	List<ProductModel> getSelectedProductsDataForKlaviyo(final List<String> productSkusList, CatalogVersionModel catalogVersion);

	public List<ProductModel> getAllProductsForKlaviyo(final CatalogVersionModel catalogVersion);

	B2BUnitModel getB2BUnitByPhoneNumber(String phoneNumber);

	List<OrderModel> getOrdersForKlaviyo();

	List<WatchTrackerModel> getWatchTrackerDetails(String webinarCode);

	WatchTrackerModel getWatchTrackerModel(String userId, String webinarCode);

	List<WatchTrackerModel> getWebinarsBasedOnStatus(String userId, boolean status);

}
