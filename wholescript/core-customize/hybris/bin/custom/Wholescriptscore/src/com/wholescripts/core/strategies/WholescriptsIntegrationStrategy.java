/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.strategies;

import de.hybris.platform.acceleratorocc.dto.payment.SopPaymentDetailsWsDTO;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.store.BaseStoreModel;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wholescripts.data.*;
import com.wholescripts.dto.OrderChangeAutoShipRequestWsDTO;
import com.wholescripts.dto.referralCodes.ReferralCodeWsDTO;

/**
 *
 */
public interface WholescriptsIntegrationStrategy
{
	public AccuralsPDFResponseData getPDFforAccurals(final B2BCustomerModel b2bCustomer, final String billingDoc) throws Exception;

	public AccuralsInvoiceResponseListData getInvoivcesForAccurals(final B2BCustomerModel b2bCustomer, final boolean isPatient,
			final String startDate, final String endDate) throws Exception;

	public void createReferralCodeData(final B2BCustomerModel b2bCustomer, ReferralCodeWsDTO referralCodeRequest) throws Exception;

	public void updateReferralCodeData(final B2BCustomerModel b2bCustomer,String referralCode,int discount) throws Exception;

	public boolean emailValidationFromSCPI(final String email) throws Exception;

	public Map<String,String> referralValidationFromSCPI(final Map<String, String> request) throws Exception;

	public RegistraionResponseData createAccount(RegistraionRequestData request) throws Exception;

	public AddressVerificationResponseData verifyAddressFromUPS(final AddressVerificationRequestData requestData) throws Exception;

	public USPSAddressValidateResponseData verifyAddressFromUSPS(final String requestData) throws Exception;

	public AccertifyVerificationResponseData accertifyLoginVerificaton(final AccertifyVerificationRequestData request)
			throws Exception;

	public SapAddressResponseData createAddressinSAP(AddressData request, B2BCustomerModel b2bCustomer) throws Exception;;
	public OrderHistoryListResponseData getOrderHistoryListForPatient(B2BCustomerModel b2bCustomer, String startDate, String endDate) throws Exception;
	public OrderHistoryPractionerResponseoneData getOrderHistoryListForPractitioner(B2BCustomerModel b2bCustomer, String startDate, String endDate) throws Exception;

	OrderHistoryPractionerResponseoneData getOrderHistoryListByPatientID(final B2BCustomerModel b2bCustomer, final String startDate, final String endDate) throws Exception;
	public OrderSimulateResponseData orderSimulate(final CartModel cart, final BaseStoreModel baseStore) throws Exception;
    CreateOrderInSAPResponseData createOrderInSap(B2BCustomerModel b2bCustomer, CartModel cartmodel) throws Exception;

    WholescriptsADDCCInSapResponseData createCreditCardInSap(WholescriptsAuthResponseData preAuthResponse, String addressNumber) throws Exception;

    WholescriptsGetCCResponseData getCreditCardInfoFromSap(B2BCustomerModel b2bCustomer) throws Exception;

	WholescriptsGetAddressFromSapResponseData getAddressFromSap(B2BCustomerModel b2bCustomer) throws Exception;
	public UPSShipingResponseData getUPSShipingCharges() throws Exception;

	public SharedRewardsResponseData getSharedRewards(B2BCustomerModel b2bCustomer,String date) throws Exception;
	public AgingResponseData getAging(B2BCustomerModel b2bCustomer,final String keyDate) throws Exception;
	public USPSShippingResponseData getUSPSShipingCharges() throws Exception;
	WholescriptsAuthResponseData preAuthorizationForTheCart(Map<String, String> parameters, SopPaymentDetailsWsDTO sopPaymentDetails) throws Exception;

	WholescriptsAuthResponseData authorizationForTheCart(final CartModel cart, final OrderCreationRequest orderCreationRequest) throws Exception;

	public WholescriptsAccountChangeResponseData accountChangeForPatient(B2BCustomerModel b2bCustomer, ReferralCodeWsDTO referralCodeRequest) throws Exception;
	AutoShipOrderResponseData getAutoShipOrders(String orderNumber) throws Exception;
	AddressChangeResponseData updateAddressInSAP(AddressWsDTO addressRequestData)  throws Exception;


	void removeAddress(final B2BCustomerModel b2bCustomer, final String addressId) throws Exception;
	PersonaInquiriesResponseData verifyAgeAndUpdateInSAP(String inquiryId) throws Exception;
	boolean UpdateAgeVerificationInSAP(final AgeValidationRequestData request) throws Exception;
	CustomerExternalDetailsData getExternalDetails(final String accountNumber) throws Exception;
	ExternalSystemResponseData sendPractitionerAmendment(final PractitionerAmendmentRequestData requestBody) throws Exception;

	ReturnResponseData removeCreditCard(String cardId, B2BCustomerModel customer, String payerId) throws Exception;

	PractitionerAmendmentDetailData getPractitionerAmendment(final String type, final String accountNumber) throws Exception;

	EstoreCredentialResponseData addEstoreCredentials(final EstoreCredentialRequestData requestData) throws Exception;
	EstoreCredentialResponseData changeEstoreCredentials(final EstoreCredentialRequestData requestData) throws Exception;
	EstoreCredentialResponseData deleteEstoreCredentials(final EstoreCredentialRequestData requestData) throws Exception;
	EstoreOrderHistory getEstorePendingOrders(final String accountNumber) throws Exception;

	ShopifyStatusResponseData getShopifyStatus(final String accountNumber) throws Exception;
	
	ShopifyProductDetailsData getShopifyProducts(final String accountNumber) throws Exception;
   OrderUpdateAutoShipResponseData updateAutoShipOrder(OrderChangeAutoShipRequestWsDTO orderChangeAutoShipRequestWsDTO) throws Exception;
   
   ExternalSystemResponseData addShopifyProducts(final String accountNumber, final List<String> products) throws Exception;
   ExternalSystemResponseData deleteShopifyProducts(final String accountNumber, final List<String> products) throws Exception;
   ExternalSystemResponseData estoreShippingConfirmation(final EstoreShippingConfirmationRequest requestBody) throws Exception;

	AccertifyVerificationCreateAccountResponseData accertifyCreateAccountVerification(final AccertifyVerificationCreateAccountRequestData request) throws Exception;
	AccertifyVerificationUpdateAccountResponseData accertifyUpdateAccountVerification(final AccertifyVerificationUpdateAccountRequestData requestData) throws Exception;

	void cancelOrderInSAP(String orderId, String orderEntryNumber) throws Exception;
	
	EstoreOrderHistory estoreOrderHistory(final EstoreOrderHistoryRequestData requestBody) throws Exception;

    WsSendRecommendationTextMessageResponse sendMessageUsingSinch(StoreFrontCustomerProcessModel rModel) throws JsonProcessingException;

	OrderDetailsResponseData getOrderDetails(B2BCustomerModel b2bCustomer, String orderNumber, BaseStoreModel baseStore)
			throws Exception;


	SapAddressResponseData createAddressinSAP(final WholescriptsExternalAPIAddressData wholescriptsExternalAPIAddressData) throws Exception;

	ExternalOrderDetailsResponseData getExternalOrderDetails(final B2BCustomerModel b2bCustomer, final String orderNumber,
			final BaseStoreModel baseStore) throws Exception;

	void updateAccertifyFlagInCDC(B2BCustomerModel customer) throws Exception;
	public OrderSimulateResponseData orderSimulateORCreate(final CartModel cart,final boolean orderSimulate) throws Exception;

	OrderHistoryResponseData getOrderHistoryWebErrors() throws Exception;

	DashboardResponseData getDashBoardDetailsFromSAP(final B2BCustomerModel b2BCustomerModel) throws Exception;

	ResponseEntity<String> updateBrandAgreementConsentInCDC(B2BCustomerModel customer) throws Exception;
	AutoShipOrderResponseData getAutoShipOrdersForCurrentUser() throws Exception;

	void updateAccountInfo(final B2BCustomerModel customer) throws Exception;
	boolean validateLoginDetailsInCDC(final String password,final String loginId,AccertifyVerificationRequestData request) throws Exception;
	PaymetricResponseData getCardDetails(String accessToken) throws Exception;
}
