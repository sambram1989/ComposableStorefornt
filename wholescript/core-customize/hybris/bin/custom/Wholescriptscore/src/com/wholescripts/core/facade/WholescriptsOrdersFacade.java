package com.wholescripts.core.facade;

import com.wholescripts.data.*;
import de.hybris.platform.acceleratorfacades.payment.data.PaymentSubscriptionResultData;
import de.hybris.platform.acceleratorocc.dto.payment.SopPaymentDetailsWsDTO;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.PaymentAuthorizationException;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.store.BaseStoreModel;

import java.util.Map;

import com.wholescripts.dto.OrderChangeAutoShipRequestWsDTO;
import com.wholescripts.dto.WholescriptsValidateAutoshipWsDTO;

public interface WholescriptsOrdersFacade {

    public OrderHistoryListResponseData getOrderHistoryListForPatient(final B2BCustomerModel b2bCustomer, final String startDate, final String endDate) throws Exception;
    public OrderDetailsResponseData getOrderDetails(B2BCustomerModel b2bCustomer, final String orderNumber, final BaseStoreModel baseStore) throws Exception;
    public OrderHistoryPractionerResponseoneData getOrderHistoryListForPractitioner(final B2BCustomerModel b2bCustomer, final String startDate, final String endDate) throws Exception;
    OrderHistoryPractionerResponseoneData getOrderHistoryListByPatientID(final B2BCustomerModel b2bCustomer, final String startDate, final String endDate) throws Exception;

    void updateTermsInCart(final boolean termsChecked);

    void validateUser();

    void validateCart(final CartData cartData) throws InvalidCartException;

    WholescriptsGetCCResponseData getAddressFromSapByPayer(WholescriptsGetCCResponseData wholescriptsGetCCResponseData, B2BCustomerModel currentB2BCustomer) throws Exception;

    public PaymentSubscriptionResultData completeSopCreateSubscription(Map<String, String> parameterMap,
                                                                       SopPaymentDetailsWsDTO sopPaymentDetails, String cartId) throws Exception;
    AutoShipOrderResponseData getAutoShipOrders(String orderNumber) throws Exception;

    OrderUpdateAutoShipResponseData updateAutoShipOrder(OrderChangeAutoShipRequestWsDTO orderChangeAutoShipRequestWsDTO) throws Exception;

    void cancelOrderInSAP(String orderId, String orderEntryNumber) throws Exception;
    
    void authorizeExternalOrderPayment(final CartModel Cart, final PaymentInfoModel paymentInfo, final OrderCreationRequest orderCreationRequest) throws PaymentAuthorizationException;

    void updateProductRankingFromOrders(final OrderModel order);
    
    void validateAndAuthorizePayment(final CartData cartData, final OrderCreationRequest orderCreationRequest, final CustomerActivityData customerActivityData) throws PaymentAuthorizationException;

    OrderHistoryResponseData getOrderHistoryWebErrors() throws Exception;
    boolean hasAutoShip() throws Exception;
    
    void createACHPaymentInfo(OrderCreationRequest orderCreationRequest);
    void verifyAutoshipAddressForRestriction(WholescriptsValidateAutoshipWsDTO request) throws Exception;
}
