/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service;

import com.wholescripts.core.model.DosingModel;
import com.wholescripts.core.model.ItemTimeModel;
import com.wholescripts.core.model.WSContactUsFormModel;
import com.wholescripts.core.model.WholescriptsFaqListModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import com.wholescripts.core.model.CustomerMaterialInformationModel;
import com.wholescripts.core.model.DosingModel;
import com.wholescripts.core.model.PresenterModel;
import com.wholescripts.core.model.ItemTimeModel;
import com.wholescripts.core.model.WSContactUsFormModel;
import com.wholescripts.core.model.WebinarModel;
import com.wholescripts.core.model.WholescriptsBrandModel;
import com.wholescripts.core.model.WholescriptsFaqListModel;
import com.wholescripts.core.model.WholescriptsTopicModel;

import com.wholescripts.data.WholescriptsWebinarData;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import com.wholescripts.core.model.WatchTrackerModel;
import java.util.Collection;
import java.util.List;

import com.wholescripts.dto.WSContactUsFormWsDTO;
import com.wholescripts.dto.dosing.DosingRequestWsDto;

public interface WholescriptsCoreService {
    Collection<DosingModel> saveDosingEntries(final DosingRequestWsDto dosingRequest);
    List<DosingModel> getDosingEntries(final String productId);
    public List<ItemTimeModel> getAllActiveItemTimes();
	 public WSContactUsFormModel saveForm(WSContactUsFormWsDTO input);
	 List<WholescriptsFaqListModel> getFaqQusAnsDetails();
	 WholescriptsFaqListModel getFaqListByUid(final String uid);
	void updateConsent(String userId, boolean isSmsNotificationEnabled, boolean isEmailNotificationEnabled);
	 /**
	  *
	  */
	 List<WholescriptsTopicModel> getAllTopicDetails(final String page, final String childTopic);
	 SearchPageData<ProductModel> getDiscontinuedProducts(SearchPageData searchPageData);
	SearchPageData<ProductModel> getAvailableWhileSuppliesLastProducts(SearchPageData searchPageData);
	public WholescriptsBrandModel getBrandForCode(final String brandCode);

	List<WholescriptsBrandModel> getTopBrands();

	WebinarModel getfeaturedWebinar();

	WebinarModel getWebinarDetails(final String  webinarCode);

	PresenterModel getPresenterDetails(String presenterCode);

	List<PresenterModel> getAllPresenterDetails();

	/**
	 * Finds all {@link WholescriptsBrandModel}s with the specific <url>url</url>.
	 *
	 * @param brandurl the WholescriptsBrand url
	 * @return all found {@link WholescriptsBrandModel}, or an empty WholescriptsBrand can be found is returned
	 */
	WholescriptsBrandModel getBrandCodeByBrandUrl(String brandurl);
	CustomerMaterialInformationModel getCustomerInfoModelForAccountAndBlueLabel(String productCode);

	List<ProductModel> getBestSellerProducts(Integer limit);

	List<ProductModel> getProductsFromPractitionerOrders(Integer limit);

	public WholescriptsBrandModel getBrandByName(final String brandName);

	List<ProductModel> getSelectedProductsDataForKlaviyo(final List<String> productSkusList, CatalogVersionModel catalogVersion);

	List<ProductModel> getAllProductsForKlaviyo(final CatalogVersionModel catalogVersion);

	void updateSMSNotification(String phoneNumber);

	void changeUid(String newUid) throws DuplicateUidException;

	void updateVideoWatchStatus(String userId, String webinarCode);

	WatchTrackerModel getWatchTrackerModel(String userId, String webinarCode);

	List<WatchTrackerModel> getWebinarsBasedOnStatus(String userId, boolean b);

	List<OrderModel> getOrdersForKlaviyo();
}
