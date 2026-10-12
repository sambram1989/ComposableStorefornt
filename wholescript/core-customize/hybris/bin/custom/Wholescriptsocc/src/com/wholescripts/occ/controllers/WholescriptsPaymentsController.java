package com.wholescripts.occ.controllers;

import com.wholescripts.core.facade.WholescriptsOrdersFacade;
import com.wholescripts.core.facade.WholescriptsUserFacade;
import com.wholescripts.core.page.validators.WholeScriptsSopPaymentDetailsValidator;
import com.wholescripts.core.service.WholescriptsIntegrationsService;
import com.wholescripts.core.service.WholescriptsTokenizationService;
import com.wholescripts.data.ExternalSystemResponseData;
import com.wholescripts.data.ReturnResponseData;
import com.wholescripts.data.WholescriptsGetCCResponseData;
import com.wholescripts.dto.WholescriptsGetCCResponseWsDTO;

import com.wholescripts.occ.constants.WholescriptsSecuredAccessConstants;
import de.hybris.platform.acceleratorfacades.payment.data.PaymentSubscriptionResultData;

import de.hybris.platform.acceleratorocc.dto.payment.SopPaymentDetailsWsDTO;
import de.hybris.platform.acceleratorocc.exceptions.PaymentProviderException;


import de.hybris.platform.acceleratorservices.payment.data.PaymentErrorField;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoDatas;


import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.commerceservices.request.mapping.annotation.RequestMappingOverride;
import de.hybris.platform.commercewebservicescommons.dto.order.PaymentDetailsListWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.commercewebservicescommons.dto.order.PaymentDetailsWsDTO;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdUserIdAndCartIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.beanutils.PropertyUtils;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.HttpStatus;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper.DEFAULT_LEVEL;


@Controller
@ApiVersion("v2")
@RequestMapping(value = "/{baseSiteId}/users/{userId}")
@Tag(name = "WholeScripts  payments Controller")
public class WholescriptsPaymentsController {
    private final static Logger LOG = Logger.getLogger(WholescriptsPaymentsController.class);


    @Resource(name = "wholescriptsIntegrationsService")
    private WholescriptsIntegrationsService wholescriptsIntegrationsService;

    @Resource(name = "b2bCustomerService")
    private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;

    @Resource(name = "dataMapper")
    protected DataMapper dataMapper;

    @Resource(name = "wholescriptsOrdersFacade")
    private WholescriptsOrdersFacade wholescriptsOrdersFacade;


    @Resource(name = "checkoutFacade")
    private CheckoutFacade checkoutFacade;

    @Resource(name = "wholescriptsTokenizationService")
    WholescriptsTokenizationService tokenizationService;

    @Resource(name = "wholescriptsCartFacade")
    private CartFacade cartFacade;


    @Resource(name = "wsSopPaymentDetailsValidator")
    private WholeScriptsSopPaymentDetailsValidator sopPaymentDetailsValidator;

    @Resource(name = "wholescriptsUserFacade")
    private WholescriptsUserFacade wholescriptsUserFacade;

    @Secured({ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
    @GetMapping(value="/paymentdetails")
    @ResponseBody
    @RequestMappingOverride(priorityProperty = "occ.wholescripts.order.priority")
    @Operation(operationId = "getPaymentDetailsList", summary = "Retrieves all credit card payment details of the customer from Sap.", description = "Retrieves all credit card payment details made by the customer.")
    @ApiBaseSiteIdAndUserIdParam
    public WholescriptsGetCCResponseWsDTO getPaymentDetailsList(
            @ApiFieldsParam @RequestParam(defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields) throws Exception {
        try {
            WholescriptsGetCCResponseData wholescriptsGetCCResponseData= wholescriptsIntegrationsService.getCreditCardInfoFromSap(b2bCustomerService.getCurrentB2BCustomer());
            wholescriptsGetCCResponseData=wholescriptsOrdersFacade.getAddressFromSapByPayer(wholescriptsGetCCResponseData,b2bCustomerService.getCurrentB2BCustomer());
            wholescriptsGetCCResponseData= wholescriptsUserFacade.maskCardNumber(wholescriptsGetCCResponseData);
            return dataMapper.map(wholescriptsGetCCResponseData, WholescriptsGetCCResponseWsDTO.class, fields);
        }
        catch (Exception e) {
            throw new RequestParameterException(String.format(e.getMessage()),
                    RequestParameterException.INVALID);

        }


    }


    @PostMapping(value = "/payment/sop/response")
    @ResponseBody
    @RequestMappingOverride(priorityProperty = "occ.wholescripts.order.priority")
    @Secured({ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
    @Operation(operationId = "doHandleSopPaymentResponse", summary = "Creates the SOP payment details.", description =
            "If the SOP payment subscription was successfully created, the payment details are defined and returned."
                    +"\n\nNote: The execution of the “Try it out” button always returns an error because "
                    + "the extended carts controller handles parameters differently, depending on which payment provider is used.")
    @ApiBaseSiteIdUserIdAndCartIdParam
    public PaymentDetailsWsDTO doHandleSopPaymentResponse(@Parameter(hidden = true) final HttpServletRequest request,
                                                          @Parameter(hidden = true) final SopPaymentDetailsWsDTO sopPaymentDetails,
                                                          @ApiFieldsParam @RequestParam(defaultValue = DEFAULT_LEVEL) final String fields, @RequestParam(required = false) final String cartId)
    {
      final Errors errors = validate(sopPaymentDetails, "SOP data", sopPaymentDetailsValidator);

        final PaymentSubscriptionResultData paymentSubscriptionResultData;
        try {
            paymentSubscriptionResultData = wholescriptsOrdersFacade.completeSopCreateSubscription(getParameterMap(request,sopPaymentDetails),
                    sopPaymentDetails,cartId);
            final CCPaymentInfoData paymentInfoData = handlePaymentSubscriptionResultData(paymentSubscriptionResultData, errors);
            return dataMapper.map(paymentInfoData, PaymentDetailsWsDTO.class, fields);
        } catch (Exception e) {
            throw new RequestParameterException(String.format(e.getMessage()), RequestParameterException.MISSING);

        }

    }

    private Map<String, String> getParameterMap(final HttpServletRequest request, SopPaymentDetailsWsDTO sopPaymentDetails) throws Exception {
        final Map<String, String> map = new HashMap<>();
        final Enumeration myEnum = request.getParameterNames();
        while (myEnum.hasMoreElements())
        {
            final String paramName = (String) myEnum.nextElement();
            final String paramValue = request.getParameter(paramName);
            map.put(paramName, paramValue);
        }

        final String xml = tokenizationService.getResponsePacket(request);
        LOG.info("Response Packet XML:" + xml);
        final Map<String, Object> cardDetails = new HashMap<>();
        try
        {
            final JSONObject obj = XML.toJSONObject(xml);

            final JSONObject field = obj.getJSONObject("PaymetricResponse").getJSONObject("Fields");

            final JSONArray arr = field.getJSONArray("FormField");

            for (int i = 0; i < arr.length(); i++)
            {
                final String key = arr.getJSONObject(i).getString("Name");
                cardDetails.put(key, arr.getJSONObject(i).get("Value"));
            }


        }
        catch (final Exception e)
        {
            e.printStackTrace();
            throw new Exception("Unable to Receive Response Packet from Paymetric" +  e.getMessage());
        }

            map.put("card_cardType", cardDetails.get("Card Type").toString());

        map.put("card_accountNumber", cardDetails.get("Card Number").toString());
        map.put("card_nameOnCard", cardDetails.get("Card Holder Name").toString());

        map.put("card_expirationMonth", cardDetails.get("Expiration Month").toString());
        map.put("card_expirationYear", cardDetails.get("Expiration Year").toString());
        map.put("cvv", cardDetails.get("Card Security Code").toString());
        return map;
    }

    protected Errors validate(final Object object, final String objectName, final Validator validator)
    {
        final Errors errors = new BeanPropertyBindingResult(object, objectName);
        validator.validate(object, errors);
        if (errors.hasErrors())
        {
            throw new WebserviceValidationException(errors);
        }
        return errors;
    }
    protected CCPaymentInfoData handlePaymentSubscriptionResultData(
            final PaymentSubscriptionResultData paymentSubscriptionResultData, Errors errors)
    {
        if (paymentSubscriptionResultData!=null && paymentSubscriptionResultData.isSuccess() && paymentSubscriptionResultData.getStoredCard() != null)
        {
            return paymentSubscriptionResultData.getStoredCard();
        }
        else if (paymentSubscriptionResultData.getErrors() != null && !paymentSubscriptionResultData.getErrors().isEmpty())
        {
            SopPaymentDetailsWsDTO sopPaymentDetailsWsDTO = null;
            if (errors == null)
            {
                sopPaymentDetailsWsDTO = new SopPaymentDetailsWsDTO();
                errors = new BeanPropertyBindingResult(sopPaymentDetailsWsDTO, "SOP data");
            }

            for (final PaymentErrorField paymentErrorField : paymentSubscriptionResultData.getErrors().values())
            {
                if (paymentErrorField.isMissing())
                {
                    LOG.error("Missing: " + paymentErrorField.getName());
                    errors.rejectValue(paymentErrorField.getName(), "field.required", "Please enter a value for this field");
                }
                if (paymentErrorField.isInvalid())
                {
                    try
                    {
                        if (sopPaymentDetailsWsDTO != null)
                        {
                            PropertyUtils.setProperty(sopPaymentDetailsWsDTO, paymentErrorField.getName(), "invalid");
                        }
                    }
                    catch (final Exception e)
                    {
                        LOG.error(e.getMessage(), e);
                    }
                    LOG.error("Invalid: " + paymentErrorField.getName());
                    errors.rejectValue(paymentErrorField.getName(), "field.invalid", new Object[] { paymentErrorField.getName() },
                            "This value is invalid for this field");
                }
            }
            throw new WebserviceValidationException(errors);
        }
        else if (paymentSubscriptionResultData.getDecision() != null && "error"
                .equalsIgnoreCase(paymentSubscriptionResultData.getDecision()))
        {
            LOG.error("Failed to create pre authorization. Error occurred while contacting external payment services.");
            throw new PaymentProviderException(
                    "Failed to create pre authorization. Decision :" + paymentSubscriptionResultData.getDecision(),
                    paymentSubscriptionResultData.getResultCode());
        }
        throw new PaymentProviderException(
                "Failed to create payment details. Decision :" + paymentSubscriptionResultData.getDecision(),
                paymentSubscriptionResultData.getResultCode());
    }
    @Secured({ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
    @GetMapping(value = "/carts/{cartId}/getPaymentInfoDetails")
    @ResponseBody
    @Operation(operationId = "getPaymentInfoDetails", summary = "Retrieves all credit card payment info details of the customer.", description = "Retrieves all credit card payment details made by the customer.")
    @ApiBaseSiteIdAndUserIdParam
    public PaymentDetailsListWsDTO getPaymentInfoDetails(
            @ApiFieldsParam @RequestParam(defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields,@PathVariable
    final String cartId) throws Exception {

        try{
            final CCPaymentInfoDatas paymentInfoDataList = new CCPaymentInfoDatas();
            CCPaymentInfoData cartPaymentInfo=wholescriptsUserFacade.getCartPaymentInfo();
            List<CCPaymentInfoData> paymentInfos= wholescriptsUserFacade.getCCPaymentInfos(true,cartPaymentInfo);
            paymentInfoDataList.setPaymentInfos(paymentInfos);
            paymentInfoDataList.setCartPaymentInfo(cartPaymentInfo);

            return dataMapper.map(paymentInfoDataList, PaymentDetailsListWsDTO.class, fields);
        }
        catch (Exception e)
        {
            throw new RequestParameterException(String.format(e.getMessage()), RequestParameterException.MISSING);
        }

    }

    @Secured({ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
    @PutMapping(value = "/carts/{cartId}/{paymentDetailsId}")
    @Operation(operationId = "updatePaymentDetails", summary = "Updates the selected payment info to the Cart. ", description =
            "Updates existing credit card payment details made by the customer using the paymentDetailsId.")
    @ApiBaseSiteIdAndUserIdParam
    @RequestMappingOverride(priorityProperty = "occ.wholescripts.order.priority")
    @ResponseStatus(HttpStatus.OK)
    public void updatePaymentDetails(
            @Parameter(description = "Payment details identifier.", required = true) @PathVariable final String paymentDetailsId,@PathVariable
    final String cartId)
    {

        try{
            wholescriptsUserFacade.updateCCPaymentInfo(paymentDetailsId);
        }

        catch (Exception e) {
        throw new RequestParameterException(String.format(e.getMessage()), RequestParameterException.MISSING);

    }

    }
    @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
    @DeleteMapping(value = "/remove/{cardId}")
    @ResponseBody
    @Operation(operationId = "removePaymentDetails", summary = "Deletes detailed information for a specific credit card payment.", description = "Deletes specific credit card payment details made by the customer using the paymentDetailsId.")
    @ApiBaseSiteIdAndUserIdParam
    @ResponseStatus(HttpStatus.OK)
    public String deleteCreditCard(@Parameter(description = "cardId.", required = true) @PathVariable final String cardId,@Parameter(description = "cardNumber.") @RequestParam final String cardNumber,@Parameter(description = "payerId",required = true) @RequestParam final String payerId)
    {
        try {
            B2BCustomerModel customer=b2bCustomerService.getCurrentB2BCustomer();
            ReturnResponseData response=wholescriptsIntegrationsService.removeCreditCard(cardId,customer,payerId);
            if("0".equals(response.getRETURN()))
            {
                wholescriptsUserFacade.removeCreditCard(cardId,cardNumber,customer);
                return "SUCCESS";

            }
        } catch (Exception e) {
            throw new RequestParameterException(String.format(e.getMessage()),
                    RequestParameterException.INVALID, cardId);
        }
        return "Failed To Delete the Card ";
    }
    
	 @Secured(
	 { "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	 @PostMapping(value = "/estore/card")
	 @ResponseBody
	 @ApiBaseSiteIdAndUserIdParam
	 @Operation(operationId = "Add estore card", summary = "Add estore card")
	 public ExternalSystemResponseData addEstoreCard(@Parameter(hidden = true) final HttpServletRequest request, 
			 @Parameter(hidden = true) @RequestBody final SopPaymentDetailsWsDTO sopPaymentDetails, @ApiFieldsParam @RequestParam(defaultValue = DEFAULT_LEVEL) final String fields) {
		 
		 final Errors errors = validate(sopPaymentDetails, "SOP data", sopPaymentDetailsValidator);
		 try {
			 final WholescriptsGetCCResponseData wholescriptsGetCCResponseData = wholescriptsIntegrationsService.getCreditCardInfoFromSap(b2bCustomerService.getCurrentB2BCustomer());
			 return wholescriptsUserFacade.addEstoreCard(sopPaymentDetails, wholescriptsGetCCResponseData);
		 }
		 catch (Exception e) {
			 throw new RequestParameterException(String.format(e.getMessage()), RequestParameterException.MISSING);
		 }

	 }
	 
	 @Secured(
	 { "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	 @GetMapping(value = "/estore/card")
	 @ResponseBody
	 @ApiBaseSiteIdAndUserIdParam
	 @Operation(operationId = "Get estore card", summary = "Get estore card")
	 public SopPaymentDetailsWsDTO getEstoreCard(@Parameter(hidden = true) final HttpServletRequest request, 
			 @Parameter(hidden = true) @ApiFieldsParam @RequestParam(defaultValue = DEFAULT_LEVEL) final String fields) {
		 try {
			 
			 WholescriptsGetCCResponseData wholescriptsGetCCResponseData = wholescriptsIntegrationsService.getCreditCardInfoFromSap(b2bCustomerService.getCurrentB2BCustomer());
			 wholescriptsGetCCResponseData = wholescriptsOrdersFacade.getAddressFromSapByPayer(wholescriptsGetCCResponseData, b2bCustomerService.getCurrentB2BCustomer());
			 return wholescriptsUserFacade.getEstoreCard(wholescriptsGetCCResponseData);
		 }
		 catch (Exception e) {
			 throw new RequestParameterException(String.format(e.getMessage()), RequestParameterException.MISSING);
		 }
	 }

}
