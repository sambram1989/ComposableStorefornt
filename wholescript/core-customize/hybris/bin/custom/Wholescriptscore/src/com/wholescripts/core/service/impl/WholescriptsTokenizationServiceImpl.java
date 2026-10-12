package com.wholescripts.core.service.impl;

import com.paymetric.sdk.AccessTokenUtility;
import com.paymetric.sdk.XIConfig;
import com.paymetric.sdk.models.AccessTokenResponse.AccessTokenResponsePacket;
import com.paymetric.sdk.models.MerchantHtmlPacket.IFrameMerchantHtmlPacket;
import com.wholescripts.core.service.WholescriptsIntegrationsService;
import com.wholescripts.core.service.WholescriptsTokenizationService;
import com.wholescripts.core.strategies.WholescriptsCreditCardPaymentInfoCreateStrategy;
import com.wholescripts.data.WholescriptsADDCCInSapResponseData;
import com.wholescripts.data.WholescriptsAuthResponseData;
import com.wholescripts.data.WholescriptsPaymetricTokenizationData;
import de.hybris.platform.acceleratorocc.dto.payment.SopPaymentDetailsWsDTO;
import de.hybris.platform.acceleratorservices.payment.data.*;
import de.hybris.platform.acceleratorservices.payment.enums.DecisionsEnum;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class WholescriptsTokenizationServiceImpl  implements WholescriptsTokenizationService {

    private static final Logger LOG = LoggerFactory.getLogger(WholescriptsTokenizationServiceImpl.class);

    public static final String GUID = XIConfig.CC_GUID.get();

    IFrameMerchantHtmlPacket packetRQ = null;
    AccessTokenResponsePacket packetRS = null;
    XIConfig xiConfig = new XIConfig();
    String IFrameURL = "";

    @Resource(name="mediaService")
    private MediaService mediaService;

    @Resource(name = "cartService")
    private CartService cartService;

    @Resource(name = "modelService")
    private ModelService modelService;

    @Resource(name = "wholescriptsIntegrationsService")
    private WholescriptsIntegrationsService wholescriptsIntegrationsService;

    @Resource(name = "wsCreditCardPaymentInfoCreateStrategy")
    WholescriptsCreditCardPaymentInfoCreateStrategy wsCreditCardPaymentInfoCreateStrategy;

    @Override
    public WholescriptsPaymetricTokenizationData getAccessToken(HttpServletRequest request) {
        WholescriptsPaymetricTokenizationData paymetricTokenizatioData = null;
        try
        {
            String hostURI = request.getParameter("hostUri");
            if(StringUtils.isEmpty(hostURI))
            {
                hostURI=XIConfig.URL.get();
            }

            packetRQ = xiConfig.getIFramePacket(request, hostURI);

            packetRS = AccessTokenUtility.GetIFrameAccessToken(XIConfig.CC_GUID.get(), XIConfig.CC_PSK.get(), XIConfig.URL.get(),
                    packetRQ);

            if (!(packetRS.getRequestError() + "").equals("null"))
            {
                final String strError = packetRS.getRequestError().getStatusCode() + "," + packetRS.getRequestError().getMessage();
                request.setAttribute("errormessage", strError);
                request.setAttribute("errorstyle", "class='errorMsg'");
            }
            else
            {
                final String accessToken = packetRS.getResponsePacket().getAccessToken();
                IFrameURL = Config.getParameter("paymetric.xi.diecommURL") + "/view/iframe/" + XIConfig.CC_GUID.get() + "/" + accessToken + "/true";
                paymetricTokenizatioData = new WholescriptsPaymetricTokenizationData();
                paymetricTokenizatioData.setPaymetricsIframeAccessTokenUrl(IFrameURL);
                paymetricTokenizatioData.setPaymetricsIframeStaticToken(XIConfig.CC_GUID.get());
                paymetricTokenizatioData.setPaymetricsIframeToken(accessToken);
                paymetricTokenizatioData.setSignature(packetRS.getResponsePacket().getSignature());

            }

        }
        catch (final Exception e)
        {

            e.printStackTrace();

        }

        return paymetricTokenizatioData;
    }

    @Override
    public String getResponsePacket(HttpServletRequest request) {
        String response = "";

        try
        {
            final String strAccessToken = request.getParameter("accessToken");

            response = AccessTokenUtility.GetResponsePacket(XIConfig.CC_GUID.get(), XIConfig.CC_PSK.get(), Config.getParameter("paymetric.xi.responsePacket"),
                    strAccessToken);

        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    public PaymentSubscriptionResultItem completeSopCreatePaymentSubscription(B2BCustomerModel customerModel, SopPaymentDetailsWsDTO sopPaymentDetails, Map<String, String> parameters) throws Exception {
        final CartModel cart = cartService.getSessionCart();
        final Map<String, PaymentErrorField> errors = new HashMap<>();
        PaymentSubscriptionResultItem subscriptionResult = new PaymentSubscriptionResultItem();

        try {
            if (parameters.getOrDefault("card_cardType", "").isEmpty()) {
                addErrorField("card_cardType", "UNABLE to determine Card Type", errors);
            }
            if (parameters.getOrDefault("billTo_country", "").isEmpty()) {
                addErrorField("billTo_country", "UNABLE to determine Country Code", errors);
            }
            if (!errors.isEmpty()) {
                subscriptionResult.setErrors(errors);
                return subscriptionResult;
            }

            WholescriptsAuthResponseData preAuthResult = wholescriptsIntegrationsService.preAuthorizationForTheCart(parameters,sopPaymentDetails);
            if (preAuthResult.getStatusCode().equals("100") && preAuthResult.isAuthorized()) {
                LOG.info("#####Pre Auth is Success ##### ");
                if (sopPaymentDetails.isSavePaymentInfo()) {
                    WholescriptsADDCCInSapResponseData sapPaymentResponse = wholescriptsIntegrationsService.createCreditCardInSap(preAuthResult,sopPaymentDetails.getAddressNumber());
                    if (sapPaymentResponse != null && "0".equals(sapPaymentResponse.getReturnCode())) {

                            LOG.info("######ADD Credit Card in SAP is Success ##### ");
                          subscriptionResult=createCreditCardInfo(parameters, customerModel, sopPaymentDetails, sapPaymentResponse);


                    }
                    else {
                        throw new Exception("Card Creation Failed in  SAP:");

                    }

                }
                else {
                    subscriptionResult=createCreditCardInfo(parameters, customerModel, sopPaymentDetails, null);

                }

            }
                else
                {

                    throw new Exception("Card Validation (pre-authorization) FAILED:");
                }

        }
        catch (final Exception ex)
        {
            ex.printStackTrace();
            throw new Exception(ex);
        }

        return subscriptionResult;
    }

    private PaymentSubscriptionResultItem createCreditCardInfo(Map<String, String> parameters, B2BCustomerModel customerModel, SopPaymentDetailsWsDTO sopPaymentDetails, WholescriptsADDCCInSapResponseData sapPaymentResponse) {
        PaymentSubscriptionResultItem subscriptionResult = new PaymentSubscriptionResultItem();
        subscriptionResult.setSuccess(true);
        subscriptionResult.setDecision(DecisionsEnum.ACCEPT.name());
        subscriptionResult.setResultCode("100");

        CreditCardPaymentInfoModel cardPaymentInfoModel = wsCreditCardPaymentInfoCreateStrategy.saveSubscription(
                parameters, customerModel, sopPaymentDetails, sapPaymentResponse);
        LOG.info("######Payment Info Creation is Success ##### ");

        cardPaymentInfoModel.setSubscriptionValidated(true);
        subscriptionResult.setStoredCard(cardPaymentInfoModel);
        modelService.save(cardPaymentInfoModel);
        return subscriptionResult;
    }

    private void addErrorField(final String strName, final String strMsg, final Map<String, PaymentErrorField> errors)
{
    final PaymentErrorField errorField = new PaymentErrorField();

    errorField.setName(strName);
    errorField.setMissing(true);
    errors.put(strMsg, errorField);
}
}
