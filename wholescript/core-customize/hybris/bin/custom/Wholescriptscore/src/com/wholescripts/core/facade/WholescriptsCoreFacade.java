package com.wholescripts.core.facade;

import com.wholescripts.core.model.WSContactUsFormModel;
import com.wholescripts.data.*;
import com.wholescripts.dto.DosingData;
import com.wholescripts.dto.ItemTimeData;
import com.wholescripts.dto.WSContactUsFormWsDTO;
import com.wholescripts.dto.WholescriptsTopicData;
import com.wholescripts.dto.dosing.DosingRequestWsDto;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.core.servicelayer.data.SearchPageData;

import java.util.Collection;
import java.util.List;

public interface WholescriptsCoreFacade {
    List<DosingData> saveDosingEntries(final DosingRequestWsDto dosingRequest);
    List<DosingData> getDosingEntries(final String productId);
    public List<ItemTimeData> getAllActiveItemTimes();
	 public WSContactUsFormModel save(WSContactUsFormWsDTO input);
	 List<FaqListData> getFaqQusAnsDetails();
	FaqListData getFaqListByUid(final String uid);
	void updateConsent(String userId, boolean isSmsEnabled, boolean isEmailEnabled);
	 /**
	  *
	  */
	 List<WholescriptsTopicData> getTopicDetails(String page, String chilTopic);
	 SearchPageData<ProductData> getDiscontinuedProducts(
			 SearchPageData searchPageData);
	SearchPageData<ProductData> getAvailableWhileSuppliesLastProducts(
			SearchPageData searchPageData);

	 WholescriptsWebinarData getfeaturedWebinar();

	WholescriptsWebinarData getWebinarDetails(final String webinarCode);

	WholescriptsPresenterData getPresenterDetails(String presenterCode);

	List<WholescriptsPresenterData> getAllPresenterDetails();

	WholescriptsCategoryOrBrandData getCategoryOrBrandDetails(String categoryCode, String brandCode);

	public DashboardProductsData getBestSellerProducts();

	List<KlaviyoCatalogFeedData> getSelectedProductsDataForKlaviyo(final List<String> productSkus);

	List<KlaviyoCatalogFeedData> getAllProductsForKlaviyo();

	void updateSMSNotification(String status, String phoneNumber);

	void changeUid(String newUid) throws DuplicateUidException;

    void updateVideoWatchStatus(String userId, String webinarCode);
}
