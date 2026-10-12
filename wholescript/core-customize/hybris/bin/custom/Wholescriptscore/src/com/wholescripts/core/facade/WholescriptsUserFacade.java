/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.facade;

import de.hybris.platform.acceleratorocc.dto.payment.SopPaymentDetailsWsDTO;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import de.hybris.platform.core.servicelayer.data.SearchPageData;

import java.util.List;

import com.wholescripts.data.AddressChangeResponseData;
import com.wholescripts.data.AvalaraTaxLinkData;
import com.wholescripts.data.CustomerActivityData;
import com.wholescripts.data.CustomerExternalDetailsData;
import com.wholescripts.data.DashboardResponseData;
import com.wholescripts.data.EstoreCredentialRequestData;
import com.wholescripts.data.EstoreCredentialResponseData;
import com.wholescripts.data.EstoreOrderHistory;
import com.wholescripts.data.EstoreOrderHistoryRequestData;
import com.wholescripts.data.EstoreShippingConfirmationRequest;
import com.wholescripts.data.ExternalSystemResponseData;
import com.wholescripts.data.PractitionerAmendmentDetailData;
import com.wholescripts.data.ShopifyProductDetailsData;
import com.wholescripts.data.ShopifyProductsRequestData;
import com.wholescripts.data.ShopifySearchResultData;
import com.wholescripts.data.ShopifyStatusResponseData;
import com.wholescripts.data.WholescriptsGetAddressFromSapResponseData;
import com.wholescripts.data.WholescriptsGetCCResponseData;
import com.wholescripts.data.WishlistData;
import com.wholescripts.dto.PractitionerAmendmentRequestWsDTO;
import com.wholescripts.dto.W9FormData;
import com.wholescripts.dto.referralCodes.ReferralCodeWsDTO;


public interface WholescriptsUserFacade {

    SearchPageData<CustomerData> getAllPatientsForPractitioner(SearchPageData<CustomerData> searchPageData, String referralCode,
                                                               String discount, String searchText);

    AvalaraTaxLinkData getAvalaraTaxLinkForCurrentUser();

    void updatePatientDirectnConsultFee(final boolean isPatientDirect, final boolean receiveConsultFee);

    public W9FormData saveW9FormData(W9FormData w9FormData);

    WishlistData saveWishList(String productCode);

    WishlistData removeWishListEntry(String productCode);

    List<WishlistData> getAllWishList();

    AddressData addAddress(AddressData addressData) throws Exception;

    List<AddressData> getAllAddressForCustomers(String addressSearch) throws Exception;

    WholescriptsGetAddressFromSapResponseData getAddressFromSap(B2BCustomerModel b2bCustomer) throws Exception;

    CustomerData getPatientDetails(String email);
    AddressData addAddressWithoutSAP(AddressData addressData,String addressNumber);

    void removeAddressInSAP(B2BCustomerModel currentCustomer, String addressId, AddressData addressData) throws Exception;

    void updateCCPaymentInfo(String paymentInfo);

    List<CCPaymentInfoData>  getCCPaymentInfos(boolean saved, CCPaymentInfoData cartPaymentInfo) throws Exception;

    CCPaymentInfoData getCartPaymentInfo();
    void verifyAgeAndUpdateInSAP(final String inquiryId) throws Exception;
    ExternalSystemResponseData sendPractitionerAmendment(PractitionerAmendmentRequestWsDTO request) throws Exception;
    CustomerExternalDetailsData getExternalDetails() throws Exception;

    void removeCreditCard(String cartId, String cardNumber, B2BCustomerModel customer);
    PractitionerAmendmentDetailData getPractitionerAmendment(final String type) throws Exception;


    EstoreCredentialResponseData addEstoreCredentials(final EstoreCredentialRequestData requestBody) throws Exception;

    EstoreCredentialResponseData deleteEstoreCredentials(final EstoreCredentialRequestData requestBody) throws Exception;

    EstoreCredentialResponseData changeEstoreCredentials(final EstoreCredentialRequestData requestBody) throws Exception;

    EstoreOrderHistory getEstorePendingOrders() throws Exception;


    String getMaskedCardNumber(final String cardNumber);

    WholescriptsGetCCResponseData maskCardNumber(WholescriptsGetCCResponseData wholescriptsGetCCResponseData);
    
    ShopifyStatusResponseData getShopifyStatus() throws Exception;
    
    ShopifyProductDetailsData getShopifyProducts() throws Exception;
    ExternalSystemResponseData addShopifyProducts(final ShopifyProductsRequestData shopifyProducts) throws Exception;
    ExternalSystemResponseData deleteShopifyProducts(final ShopifyProductsRequestData shopifyProducts) throws Exception;
    ExternalSystemResponseData estoreShippingConfirmation(final EstoreShippingConfirmationRequest requestBody) throws Exception;
    SopPaymentDetailsWsDTO getEstoreCard(final WholescriptsGetCCResponseData wholescriptsGetCCResponseData) throws Exception;
    
    EstoreOrderHistory estoreOrderHistory(final EstoreOrderHistoryRequestData requestBody) throws Exception;

    CustomerData getNotificationSettingsforUser(String email);
    
    ExternalSystemResponseData addEstoreCard(final SopPaymentDetailsWsDTO sopPaymentDetails, final WholescriptsGetCCResponseData wholescriptsGetCCResponseData) throws Exception;

    String  changePractitionerForPatient(ReferralCodeWsDTO referralCodeRequest) throws Exception;

    CustomerData getPractitionerDetails();
    
    boolean saveCustomerActivity(final CustomerActivityData customerActivityData);
    
    AddressChangeResponseData updateAddress(final AddressWsDTO addressRequestData, final AddressData addressData) throws Exception;
    
    ShopifySearchResultData searchShopifyProducts(final String fields) throws Exception;

    DashboardResponseData getDashBoardDetailsFromSAP(final B2BCustomerModel b2BCustomerModel) throws Exception ;

    void removeAddressInCommerce(String addressId, List<String> creditCardIds, AddressData addressData) throws Exception;

}
