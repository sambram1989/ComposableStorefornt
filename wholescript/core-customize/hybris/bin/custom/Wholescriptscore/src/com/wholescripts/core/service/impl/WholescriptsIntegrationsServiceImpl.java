package com.wholescripts.core.service.impl;

import de.hybris.platform.acceleratorocc.dto.payment.SopPaymentDetailsWsDTO;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wholescripts.core.constants.WholescriptsIntegrationConstants;
import com.wholescripts.core.facade.WholescriptsCommercePromotionRestrictionFacade;
import com.wholescripts.core.service.WholescriptsIntegrationsService;
import com.wholescripts.core.strategies.AvalaraIntegrationStrategy;
import com.wholescripts.core.strategies.WholescriptsIntegrationStrategy;
import com.wholescripts.core.strategies.XymonetIntegrationStrategy;
import com.wholescripts.core.utils.WholescriptsCoreUtils;
import com.wholescripts.data.*;
import com.wholescripts.dto.OrderChangeAutoShipRequestWsDTO;
import com.wholescripts.dto.referralCodes.ReferralCodeWsDTO;

/**
 * @author ViralD
 * A common class for handling all Wholescripts Integrations
 */
public class WholescriptsIntegrationsServiceImpl implements WholescriptsIntegrationsService {

    private static final Logger LOG = Logger.getLogger(WholescriptsIntegrationsService.class.getName());

    @Resource
    private AvalaraIntegrationStrategy avalaraIntegrationStrategy;
    @Resource(name = "xymonetIntegrationStrategy")
    private XymonetIntegrationStrategy xymonetIntegrationStrategy;
    @Resource(name = "wholescriptsIntegrationStrategy")
    private WholescriptsIntegrationStrategy wholescriptsIntegrationStrategy;
    @Resource(name = "baseStoreService")
    private BaseStoreService baseStoreService;
    @Resource(name = "b2bUnitService")
    private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;

    @Resource(name = "searchRestrictionService")
    private SearchRestrictionService searchRestrictionService;
    @Resource(name = "wsCommercePromotionRestrictionFacade")
 	private WholescriptsCommercePromotionRestrictionFacade wsCommercePromotionRestrictionFacade;


    public void integrateWith(final Map<String, String> configParams, final B2BCustomerModel customer) {
        if (configParams.get("destination").equals(WholescriptsIntegrationConstants.AVALARA)) {
            avalaraIntegrationStrategy.integrate(configParams, customer);
        }
    }

    public void setAvalaraIntegrationStrategy(final AvalaraIntegrationStrategy avalaraIntegrationStrategy) {
        this.avalaraIntegrationStrategy = avalaraIntegrationStrategy;
    }

    @Override
    public Map<String, String> integrateWithXymonetShowW9(final B2BCustomerModel b2bCustomer) throws JsonProcessingException {
        return xymonetIntegrationStrategy.showW9Link(b2bCustomer);
    }

    @Override
    public XymonetW9ResponseData integrateWithXymonetDownloadW9(final B2BCustomerModel b2bCustomer) throws JsonProcessingException {
        return xymonetIntegrationStrategy.downloadW9Form(b2bCustomer);
    }

    @Override
    public AccuralsPDFResponseData getPDFforAccurals(final B2BCustomerModel b2bCustomer, final String billingDoc) throws Exception {
        return wholescriptsIntegrationStrategy.getPDFforAccurals(b2bCustomer, billingDoc);
    }

    @Override
    public AccuralsInvoiceResponseListData getInvoivcesForAccurals(final B2BCustomerModel b2bCustomer, final boolean isPatient, final String startDate, final String endDate) throws Exception {

        AccuralsInvoiceResponseListData responseData = wholescriptsIntegrationStrategy.getInvoivcesForAccurals(b2bCustomer, isPatient, startDate, endDate);
        if (responseData != null && responseData.getInvoiceList() != null) {
            // Remove invoices where the invoice number starts with "HB"
            responseData.getInvoiceList().getItems().removeIf(invoice ->
                    invoice.getInvoiceNumber() != null && invoice.getInvoiceNumber().startsWith("HB")
            );

            // Process remaining invoices
            responseData.getInvoiceList().getItems().forEach(invoice -> {
                if (invoice.getAddress() != null && invoice.getAddress().getItems() != null && invoice.getAddress().getItems().get(0) != null) {
                    String partnerId = invoice.getAddress().getItems().get(0).getPartner();
                    invoice.setProfileId(getUserIdFromPartner(partnerId));
                    setReferralInfoFromPartner(invoice, partnerId);
                }
            });
        }


        return responseData;
    }

    private String getUserIdFromPartner(String partnerId) {
        searchRestrictionService.disableSearchRestrictions();
        String formattedPartnerId = WholescriptsCoreUtils.getAccountNumberWithSalesArea(removeLeadingZeros(partnerId));
        B2BUnitModel b2bUnit = b2bUnitService.getUnitForUid(formattedPartnerId);
        searchRestrictionService.enableSearchRestrictions();
        if (b2bUnit != null && b2bUnit.getMembers() != null) {
            return b2bUnit.getMembers().stream().filter(member -> member instanceof B2BCustomerModel).map(member -> ((B2BCustomerModel) member).getUid()).findFirst().orElse(null);
        }

        return null;
    }

    private void setReferralInfoFromPartner(AccuralsInvoiceResponseData invoice, String partnerId) {
        searchRestrictionService.disableSearchRestrictions();
        String formattedPartnerId = WholescriptsCoreUtils.getAccountNumberWithSalesArea(removeLeadingZeros(partnerId));
        B2BUnitModel b2bUnit = b2bUnitService.getUnitForUid(formattedPartnerId);
        searchRestrictionService.enableSearchRestrictions();

        if (b2bUnit != null && b2bUnit.getReferralCodes() != null) {
            // Extract the first referral code and last name using streams
            b2bUnit.getReferralCodes().stream().findFirst().ifPresent(referral -> {
                invoice.setPatientReferralCode(referral.getReferralCode());
                invoice.setPatientLastName(referral.getLastName());
            });
        }
    }

    private String removeLeadingZeros(String partnerId) {
        return partnerId.replaceFirst("^0+", ""); // Removes leading zeros
    }



    @Override
    public void createReferralCodeData(final B2BCustomerModel b2bCustomer, final ReferralCodeWsDTO referralCodeRequest) throws Exception {
        wholescriptsIntegrationStrategy.createReferralCodeData(b2bCustomer, referralCodeRequest);
    }

    @Override
    public void updateReferralCodeData(final B2BCustomerModel b2bCustomer, String referralCode, int discount) throws Exception {
        wholescriptsIntegrationStrategy.updateReferralCodeData(b2bCustomer, referralCode, discount);
    }

    @Override
    public boolean emailValidationFromSCPI(final String email) throws Exception {
        return wholescriptsIntegrationStrategy.emailValidationFromSCPI(email);
    }

    @Override
    public Map<String,String> referralValidationFromSCPI(final Map<String, String> request) throws Exception {
        return wholescriptsIntegrationStrategy.referralValidationFromSCPI(request);
    }

    @Override
    public RegistraionResponseData createAccount(final RegistraionRequestData request) throws Exception {
        return wholescriptsIntegrationStrategy.createAccount(request);
    }

    @Override
    public AddressVerificationResponseData verifyAddressFromUPS(final AddressVerificationRequestData requestData) throws Exception {
        return wholescriptsIntegrationStrategy.verifyAddressFromUPS(requestData);
    }

    @Override
    public USPSAddressValidateResponseData verifyAddressFromUSPS(final String requestData) throws Exception {
        return wholescriptsIntegrationStrategy.verifyAddressFromUSPS(requestData);
    }

    @Override
    public AccertifyVerificationResponseData accertifyLoginVerificaton(final AccertifyVerificationRequestData request) throws Exception {
        return wholescriptsIntegrationStrategy.accertifyLoginVerificaton(request);
    }

    @Override
    public SapAddressResponseData createAddressinSAP(final AddressData address, final B2BCustomerModel b2bCustomer) throws Exception {
        return wholescriptsIntegrationStrategy.createAddressinSAP(address, b2bCustomer);
    }

    @Override
    public SapAddressResponseData createAddressinSAP(final WholescriptsExternalAPIAddressData address) throws Exception {
        return wholescriptsIntegrationStrategy.createAddressinSAP(address);
    }

    @Override
    public OrderHistoryListResponseData getOrderHistoryListForPatient(final B2BCustomerModel b2bCustomer, final String startDate, final String endDate) throws Exception {
        return wholescriptsIntegrationStrategy.getOrderHistoryListForPatient(b2bCustomer, startDate, endDate);
    }

    public OrderDetailsResponseData getOrderDetails(final B2BCustomerModel b2bCustomer, final String orderNumber, final BaseStoreModel baseStore) throws Exception {
        return wholescriptsIntegrationStrategy.getOrderDetails(b2bCustomer, orderNumber, baseStore);
    }

    @Override
    public OrderHistoryPractionerResponseoneData getOrderHistoryListForPractitioner(final B2BCustomerModel b2bCustomer, final String startDate, final String endDate) throws Exception {
        return wholescriptsIntegrationStrategy.getOrderHistoryListForPractitioner(b2bCustomer, startDate, endDate);
    }

    @Override
    public OrderHistoryPractionerResponseoneData getOrderHistoryListByPatientID(final B2BCustomerModel b2bCustomer, final String startDate, final String endDate) throws Exception{
        return  wholescriptsIntegrationStrategy.getOrderHistoryListByPatientID(b2bCustomer,startDate,endDate);
    }


    public OrderSimulateResponseData orderSimulate(final CartModel cart, final BaseStoreModel baseStore) {
        try {
            return wholescriptsIntegrationStrategy.orderSimulate(cart, baseStore);
        } catch (final Exception e) {
            throw new RequestParameterException(e.getMessage());
        }
    }

    @Override
    public CreateOrderInSAPResponseData createOrderInSap(final B2BCustomerModel b2bCustomer, final CartModel cartmodel) throws Exception {
        return wholescriptsIntegrationStrategy.createOrderInSap(b2bCustomer, cartmodel);
    }

    @Override
    public WholescriptsADDCCInSapResponseData createCreditCardInSap(final WholescriptsAuthResponseData preAuthResponse, String addressNumber) throws Exception {
        return wholescriptsIntegrationStrategy.createCreditCardInSap(preAuthResponse,addressNumber);
    }

    @Override
    public WholescriptsGetCCResponseData getCreditCardInfoFromSap(final B2BCustomerModel b2bCustomer) throws Exception {
        return wholescriptsIntegrationStrategy.getCreditCardInfoFromSap(b2bCustomer);
    }

    @Override
    public WholescriptsGetAddressFromSapResponseData getAddressFromSap(final B2BCustomerModel b2bCustomer) throws Exception {
        return wholescriptsIntegrationStrategy.getAddressFromSap(b2bCustomer);
    }

    @Override
    public UPSShipingResponseData getUPSShipingCharges() throws Exception {
        return wholescriptsIntegrationStrategy.getUPSShipingCharges();
    }


    @Override
    public WholescriptsAuthResponseData authorizationForTheCart(final CartModel cart, final OrderCreationRequest orderCreationRequest) throws Exception {
        return wholescriptsIntegrationStrategy.authorizationForTheCart(cart, orderCreationRequest);
    }


    public SharedRewardsResponseData getSharedRewards(final B2BCustomerModel b2bCustomer, final String date) throws Exception {
        return wholescriptsIntegrationStrategy.getSharedRewards(b2bCustomer, date);
    }

    public AgingResponseData getAging(final B2BCustomerModel b2bCustomer, final String keyDate) throws Exception {
        return wholescriptsIntegrationStrategy.getAging(b2bCustomer, keyDate);
    }

    @Override
    public USPSShippingResponseData getUSPSShipingCharges() throws Exception {
        return wholescriptsIntegrationStrategy.getUSPSShipingCharges();
    }

    @Override
    public WholescriptsAuthResponseData preAuthorizationForTheCart(final Map<String, String> parameters, final SopPaymentDetailsWsDTO sopPaymentDetails) throws Exception {
        return wholescriptsIntegrationStrategy.preAuthorizationForTheCart(parameters, sopPaymentDetails);
    }

    @Override
    public WholescriptsAccountChangeResponseData accountChangeForPatient(final B2BCustomerModel b2bCustomer, final ReferralCodeWsDTO referralCodeRequest) throws Exception {
        return wholescriptsIntegrationStrategy.accountChangeForPatient(b2bCustomer, referralCodeRequest);
    }

    @Override
    public AutoShipOrderResponseData getAutoShipOrders(final String orderNumber) throws Exception {
        return wholescriptsIntegrationStrategy.getAutoShipOrders(orderNumber);
    }

    @Override
    public AddressChangeResponseData updateAddressInSAP(final AddressWsDTO addressRequestData) throws Exception {
        return wholescriptsIntegrationStrategy.updateAddressInSAP(addressRequestData);
    }

    @Override
    public void removeAddress(final B2BCustomerModel b2bCustomer, final String addressId) throws Exception {
        wholescriptsIntegrationStrategy.removeAddress(b2bCustomer, addressId);
    }

    @Override
    public PersonaInquiriesResponseData verifyAgeAndUpdateInSAP(final String inquiryId) throws Exception {
        return wholescriptsIntegrationStrategy.verifyAgeAndUpdateInSAP(inquiryId);
    }

    @Override
    public boolean UpdateAgeVerificationInSAP(final AgeValidationRequestData request) throws Exception {
        return wholescriptsIntegrationStrategy.UpdateAgeVerificationInSAP(request);
    }

    @Override
    public ExternalSystemResponseData sendPractitionerAmendment(final PractitionerAmendmentRequestData request) throws Exception {
        return wholescriptsIntegrationStrategy.sendPractitionerAmendment(request);
    }


    @Override
    public CustomerExternalDetailsData getExternalDetails(final String unitId) throws Exception {
        return wholescriptsIntegrationStrategy.getExternalDetails(unitId);
    }


    @Override
    public ReturnResponseData removeCreditCard(final String cardId, final B2BCustomerModel customer, String payerId) throws Exception {
        return wholescriptsIntegrationStrategy.removeCreditCard(cardId, customer,payerId);
    }

    @Override
    public PractitionerAmendmentDetailData getPractitionerAmendment(final String type, final String accountNumber) throws Exception {
        return wholescriptsIntegrationStrategy.getPractitionerAmendment(type, accountNumber);
    }

    @Override
    public EstoreCredentialResponseData addEstoreCredentials(final EstoreCredentialRequestData requestBody) throws Exception {
        return wholescriptsIntegrationStrategy.addEstoreCredentials(requestBody);
    }

    @Override
    public EstoreCredentialResponseData deleteEstoreCredentials(final EstoreCredentialRequestData requestBody) throws Exception {
        return wholescriptsIntegrationStrategy.deleteEstoreCredentials(requestBody);
    }

    @Override
    public EstoreCredentialResponseData changeEstoreCredentials(final EstoreCredentialRequestData requestBody) throws Exception {
        return wholescriptsIntegrationStrategy.changeEstoreCredentials(requestBody);
    }

    @Override
    public EstoreOrderHistory getEstorePendingOrders(final String accountNumber) throws Exception {
        return wholescriptsIntegrationStrategy.getEstorePendingOrders(accountNumber);
    }

    @Override
    public ShopifyStatusResponseData getShopifyStatus(final String accountNumber) throws Exception {
        return wholescriptsIntegrationStrategy.getShopifyStatus(accountNumber);
    }

    @Override
    public ShopifyProductDetailsData getShopifyProducts(final String accountNumber) throws Exception {
        return wholescriptsIntegrationStrategy.getShopifyProducts(accountNumber);
    }


    @Override
    public OrderUpdateAutoShipResponseData updateAutoShipOrder(final OrderChangeAutoShipRequestWsDTO orderChangeAutoShipRequestWsDTO) throws Exception {
        return wholescriptsIntegrationStrategy.updateAutoShipOrder(orderChangeAutoShipRequestWsDTO);
    }

    @Override
    public ExternalSystemResponseData addShopifyProducts(final String accountNumber, final List<String> products) throws Exception {
        return wholescriptsIntegrationStrategy.addShopifyProducts(accountNumber, products);
    }

    @Override
    public ExternalSystemResponseData deleteShopifyProducts(final String accountNumber, final List<String> products)
            throws Exception {
        return wholescriptsIntegrationStrategy.deleteShopifyProducts(accountNumber, products);
    }

    @Override
    public ExternalSystemResponseData estoreShippingConfirmation(final EstoreShippingConfirmationRequest requestBody)
            throws Exception {
        return wholescriptsIntegrationStrategy.estoreShippingConfirmation(requestBody);
    }


    @Override
    public AccertifyVerificationCreateAccountResponseData accertifyCreateAccountVerification(AccertifyVerificationCreateAccountRequestData request) throws Exception {
        return wholescriptsIntegrationStrategy.accertifyCreateAccountVerification(request);
    }

    @Override
    public AccertifyVerificationUpdateAccountResponseData accertifyUpdateAccountVerification(AccertifyVerificationUpdateAccountRequestData requestData) throws Exception {
        return wholescriptsIntegrationStrategy.accertifyUpdateAccountVerification(requestData);
    }

    @Override
    public void cancelOrderInSAP(String orderId, String orderEntryNumber) throws Exception {
        wholescriptsIntegrationStrategy.cancelOrderInSAP(orderId, orderEntryNumber);
    }

    @Override
    public EstoreOrderHistory estoreOrderHistory(final EstoreOrderHistoryRequestData requestBody) throws Exception {
        return wholescriptsIntegrationStrategy.estoreOrderHistory(requestBody);
    }

    @Override
    public WsSendRecommendationTextMessageResponse sendMessageUsingSinch(StoreFrontCustomerProcessModel recommendation) throws JsonProcessingException {
        return wholescriptsIntegrationStrategy.sendMessageUsingSinch(recommendation);
    }

    @Override
    public ExternalOrderDetailsResponseData getExternalOrderDetails(final B2BCustomerModel b2bCustomer, final String orderNumber, final BaseStoreModel baseStore) throws Exception {
        return wholescriptsIntegrationStrategy.getExternalOrderDetails(b2bCustomer, orderNumber, baseStore);
    }

    @Override
    public void updateAccertifyFlagInCDC(B2BCustomerModel customer) throws Exception {
        wholescriptsIntegrationStrategy.updateAccertifyFlagInCDC(customer);
    }

    @Override
    public OrderSimulateResponseData orderSimulateORCreate(CartModel cart,final boolean orderSimulate) throws Exception
    {
   	 OrderSimulateResponseData response = wholescriptsIntegrationStrategy.orderSimulateORCreate(cart,orderSimulate);
   	 Set<PromotionResultModel> promotionResults= cart.getAllPromotionResults();
   	 if(CollectionUtils.isNotEmpty(promotionResults) && Objects.nonNull(response)) {
   		 checkIFHeaderDiscountsIsValid(response, promotionResults);
      	 checkIfItemDiscountsIsValid(response, promotionResults);
   	 }
   	 return response;
    }
    
	private void checkIfItemDiscountsIsValid(OrderSimulateResponseData response, Set<PromotionResultModel> promotionResults)
			throws Exception
	{
		Set<PromotionResultModel> itemPromotionResults =promotionResults.stream().filter(p  -> CollectionUtils.isNotEmpty( p.getConsumedEntries())).collect(Collectors.toSet());
   	 List<String> itemDiscountCodes=  itemPromotionResults.stream().map(hd -> hd.getPromotion().getCode()).collect(Collectors.toList());
   	 ItemsDiscountItemsListData itemDiscountList= response.getItemsDiscount();

   	 if(Objects.nonNull(itemDiscountList)) {
   		 List<String> invalidsPromoCodes= new ArrayList<>();
   		 List<ItemsDiscountItemsData> items = itemDiscountList.getItem();
   		 List<String> responseItemsDiscountCodes = items.stream().map(h -> h.getPromoCode()).collect(Collectors.toList());
   		 for(String promoCode : itemDiscountCodes) {
             if(!responseItemsDiscountCodes.contains(promoCode)) {
            	 wsCommercePromotionRestrictionFacade.disablePromotionForCart(promoCode);
            	 invalidsPromoCodes.add(promoCode);
            	 response.setHeaderInvalidsPromoCodes(invalidsPromoCodes);
             }
   		 }
   	 }else {
   		 if(CollectionUtils.isNotEmpty(itemDiscountCodes)) {
   			 for(String promoCode :itemDiscountCodes) {
      			 wsCommercePromotionRestrictionFacade.disablePromotionForCart(promoCode);
      		 }
   			 response.setHeaderInvalidsPromoCodes(itemDiscountCodes);
   		 }
   	 }
	}

	private void checkIFHeaderDiscountsIsValid(OrderSimulateResponseData response, Set<PromotionResultModel> promotionResults)
			throws Exception
	{
		Set<PromotionResultModel> headerPromotionResults =promotionResults.stream().filter(p  -> CollectionUtils.isEmpty( p.getConsumedEntries())).collect(Collectors.toSet());
   	 List<String> headerDiscountCodes=  headerPromotionResults.stream().map(hd -> hd.getPromotion().getCode()).collect(Collectors.toList());

   	 HeaderDiscountItemsListData headerDiscountList = response.getHeaderDiscount();
   	 if(Objects.nonNull(headerDiscountList)) {
   		 List<String> invalidsPromoCodes= new ArrayList<>();
   		 List<HeaderDiscountItemsData> headerItems = headerDiscountList.getItem();
   		 List<String> responseHeaderDiscountCodes = headerItems.stream().map(h -> h.getPromoCode()).collect(Collectors.toList());
   		 for(String promoCode : headerDiscountCodes) {
             if(!responseHeaderDiscountCodes.contains(promoCode)) {
            	 wsCommercePromotionRestrictionFacade.disablePromotionForCart(promoCode);
            	 invalidsPromoCodes.add(promoCode);
            	 response.setHeaderInvalidsPromoCodes(invalidsPromoCodes);
             }
   		 }
   	 }else {
   		 if(CollectionUtils.isNotEmpty(headerDiscountCodes)) {
   			 for(String promoCode :headerDiscountCodes) {
      			 wsCommercePromotionRestrictionFacade.disablePromotionForCart(promoCode);
      		 }
   			 response.setHeaderInvalidsPromoCodes(headerDiscountCodes);
   		 }
   	 }
	}

    @Override
    public OrderHistoryResponseData getOrderHistoryWebErrors() throws Exception{
        return wholescriptsIntegrationStrategy.getOrderHistoryWebErrors();
    }

    @Override
    public DashboardResponseData getDashBoardDetailsFromSAP(final B2BCustomerModel b2BCustomerModel) throws Exception {
        return wholescriptsIntegrationStrategy.getDashBoardDetailsFromSAP(b2BCustomerModel);
    }

    @Override
    public ResponseEntity<String> updateBrandAgreementConsentInCDC(B2BCustomerModel customer) throws Exception {
        return wholescriptsIntegrationStrategy.updateBrandAgreementConsentInCDC(customer);
    }

    @Override
    public AutoShipOrderResponseData getAutoShipOrdersForCurrentUser() throws Exception {
        return wholescriptsIntegrationStrategy.getAutoShipOrdersForCurrentUser();
    }

    @Override
    public void updateAccountInfo(B2BCustomerModel customer) throws Exception {
        wholescriptsIntegrationStrategy.updateAccountInfo(customer);
    }

	@Override
	public boolean validateLoginDetailsInCDC(final String password,final String loginId,AccertifyVerificationRequestData request) throws Exception
	{
		return wholescriptsIntegrationStrategy.validateLoginDetailsInCDC(password, loginId,request);
	}

	@Override
	public PaymetricResponseData getCardDetails(String accessToken) throws Exception
	{
		return wholescriptsIntegrationStrategy.getCardDetails(accessToken);
	}
}