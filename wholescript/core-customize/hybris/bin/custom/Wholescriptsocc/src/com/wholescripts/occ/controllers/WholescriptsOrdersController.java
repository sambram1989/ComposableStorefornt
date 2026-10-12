package com.wholescripts.occ.controllers;


import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.b2bacceleratorfacades.api.cart.CartFacade;
import de.hybris.platform.b2bacceleratorfacades.checkout.data.PlaceOrderData;
import de.hybris.platform.b2bocc.security.SecuredAccessConstants;
import de.hybris.platform.b2bocc.v2.controllers.BaseController;
import de.hybris.platform.b2bocc.v2.skipfield.SkipOrderFieldValueSetter;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.commerceservices.request.mapping.annotation.RequestMappingOverride;
import de.hybris.platform.commercewebservicescommons.annotation.SiteChannelRestriction;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.PaymentAuthorizationException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.commercewebservicescommons.strategies.CartLoaderStrategy;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;
import de.hybris.platform.webservicescommons.pagination.WebPaginationUtils;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;

import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.wholescripts.core.exception.AgeRestrictionException;
import com.wholescripts.core.exception.POBoxAgeRestrictionException;
import com.wholescripts.core.exception.RegionExclusionException;
import com.wholescripts.core.exception.WholescriptsPageNotFoundException;
import com.wholescripts.core.facade.WholescriptsOrdersFacade;
import com.wholescripts.core.facade.WholescriptsUserFacade;
import com.wholescripts.core.facade.impl.WholescriptsDefaultB2BCheckoutFacade;
import com.wholescripts.data.AutoShipOrderResponseData;
import com.wholescripts.data.CustomerActivityData;
import com.wholescripts.data.OrderCreationRequest;
import com.wholescripts.data.OrderDetailsResponseData;
import com.wholescripts.data.OrderHistoryListResponseData;
import com.wholescripts.data.OrderHistoryPractionerResponseoneData;
import com.wholescripts.data.OrderHistoryResponseData;
import com.wholescripts.data.OrderUpdateAutoShipResponseData;
import com.wholescripts.dto.AutoShipOrderResponseWsDTO;
import com.wholescripts.dto.OrderChangeAutoShipRequestWsDTO;
import com.wholescripts.dto.OrderDetailsResponseWsDTO;
import com.wholescripts.dto.OrderHistoryListPractionerResponseWsDTO;
import com.wholescripts.dto.OrderHistoryListResponseWsDTO;
import com.wholescripts.dto.OrderHistoryResponseWsDTO;
import com.wholescripts.dto.OrderUpdateAutoShipResponseWsDTO;
import com.wholescripts.dto.WholescriptsValidateAutoshipWsDTO;
import com.wholescripts.occ.constants.WholescriptsSecuredAccessConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Orders")
@ApiVersion("v2")
@RequestMapping(value = "/{baseSiteId}/users/{userId}")
public class WholescriptsOrdersController  extends BaseController{

    protected static final String API_COMPATIBILITY_B2B_CHANNELS = "api.compatibility.b2b.channels";

    @Resource(name = "webPaginationUtils")
    private WebPaginationUtils webPaginationUtils;

    @Resource(name = "b2bCustomerService")
    private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;

    @Resource(name = "wholescriptsOrdersFacade")
    private WholescriptsOrdersFacade wholescriptsOrdersFacade;

    @Resource(name = "dataMapper")
    private DataMapper dataMapper;

    @Resource(name = "orderFieldValueSetter")
    private SkipOrderFieldValueSetter skipOrderFieldValueSetter;

    @Resource(name = "cartLoaderStrategy")
    private CartLoaderStrategy cartLoaderStrategy;

    @Resource(name = "b2bCartFacade")
    private CartFacade cartFacade;

    @Resource(name = "b2bCheckoutFacade")
    private WholescriptsDefaultB2BCheckoutFacade b2bCheckoutFacade;
    
    @Resource(name = "baseStoreService")
    private BaseStoreService baseStoreService;
    
    @Resource(name = "wholescriptsUserFacade")
    private WholescriptsUserFacade wholescriptsUserFacade;

    @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
    @PostMapping("/patient/order-list")
    @ResponseBody
    @Operation(operationId = "getOrderHistoryData", summary = "Returns a paginated list of order history data for a patient.")
    @ApiBaseSiteIdAndUserIdParam
    @ApiResponse(responseCode = "200", description = "OrderHistoryListWsDTO",
            content = @Content(schema = @Schema(implementation = OrderHistoryListResponseWsDTO.class)))
    public OrderHistoryListResponseWsDTO getOrderHistoryData(
            @Parameter(description = "Response configuration. Options: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues = {"BASIC", "DEFAULT", "FULL"}))
            @RequestParam(defaultValue = "DEFAULT") final String fields,
            @RequestParam("startDate") final String startDate, @RequestParam("endDate") final String endDate) {

        try {
            final OrderHistoryListResponseData orderHistoryListResponseData = wholescriptsOrdersFacade.getOrderHistoryListForPatient(b2bCustomerService.getCurrentB2BCustomer(), startDate, endDate);
            return dataMapper.map(orderHistoryListResponseData, OrderHistoryListResponseWsDTO.class, fields);
        } catch (final Exception ex) {
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.MISSING);
        }

    }


    @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
    @PostMapping("/order-details")
    @ResponseBody
    @Operation(operationId = "getOrderDetails", summary = "Returns a order details data for a patient and practitioner both.")
    @ApiBaseSiteIdAndUserIdParam
    @ApiResponse(responseCode = "200", description = "OrderHistoryListWsDTO",
            content = @Content(schema = @Schema(implementation = OrderDetailsResponseWsDTO.class)))
    public OrderDetailsResponseWsDTO getOrderDetails(
            @Parameter(description = "Response configuration. Options: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues = {"BASIC", "DEFAULT", "FULL"}))
            @RequestParam(defaultValue = "DEFAULT") final String fields,
            @RequestParam("orderNumber") final String orderNumber) throws WholescriptsPageNotFoundException {

        try {
            final OrderDetailsResponseData orderDetailsPatientResponseData = wholescriptsOrdersFacade.getOrderDetails(b2bCustomerService.getCurrentB2BCustomer(),orderNumber, baseStoreService.getCurrentBaseStore());
            return dataMapper.map(orderDetailsPatientResponseData, OrderDetailsResponseWsDTO.class);
        } 
        catch (final WholescriptsPageNotFoundException ex) {
           throw new WholescriptsPageNotFoundException(String.format(ex.getMessage()));
       }
        catch (final Exception ex) {
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.MISSING);
        }
    }

    @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
    @PostMapping("/practitioner/order-list")
    @ResponseBody
    @Operation(operationId = "getPractitionerOrderHistoryData", summary = "Returns a paginated list of order history data for a practitioner.")
    @ApiBaseSiteIdAndUserIdParam
    @ApiResponse(responseCode = "200", description = "OrderHistoryListWsDTO",
            content = @Content(schema = @Schema(implementation = OrderHistoryListPractionerResponseWsDTO.class)))
    public OrderHistoryListPractionerResponseWsDTO getOrderHistoryDataForPractitioner(
            @Parameter(description = "Response configuration. Options: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues = {"BASIC", "DEFAULT", "FULL"}))
            @RequestParam(defaultValue = "DEFAULT") final String fields,
            @RequestParam("startDate") final String startDate, @RequestParam("endDate") final String endDate) {

        try {
            final OrderHistoryPractionerResponseoneData orderHistoryPractionerResponseoneData = wholescriptsOrdersFacade.getOrderHistoryListForPractitioner(b2bCustomerService.getCurrentB2BCustomer(), startDate, endDate);
            return dataMapper.map(orderHistoryPractionerResponseoneData, OrderHistoryListPractionerResponseWsDTO.class, fields);
        } catch (final Exception ex) {
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.MISSING);
        }

    }

    @Secured({ SecuredAccessConstants.ROLE_CUSTOMERGROUP, SecuredAccessConstants.ROLE_GUEST,
            SecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
    @PostMapping(value = "/orders")
    @RequestMappingOverride(priorityProperty = "occ.wholescripts.order.priority")
    @SiteChannelRestriction(allowedSiteChannelsProperty = API_COMPATIBILITY_B2B_CHANNELS)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ApiBaseSiteIdAndUserIdParam
    @Operation(operationId = "placeOrgOrder", summary = "Creates a B2B order.", description = "Creates a B2B order. By default the payment type is ACCOUNT. Set payment type to CARD if placing an order using credit card.")
    public OrderWsDTO placeOrgOrder(
            @Parameter(description = "Cart identifier: cart code for logged-in user, cart GUID for anonymous user, or 'current' for the last modified cart.", example = "00000110", required = true) @RequestParam(required = true) final String cartId,
            @Parameter(description = "Whether terms were accepted or not.", required = true) @RequestParam(required = true) final boolean termsChecked,
            @Parameter(description = "Order Creation Request Data") final @RequestBody(required = false) OrderCreationRequest orderCreationRequest,
            @Parameter(hidden = true) final HttpServletRequest request,
            @ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields,@Parameter(description = "Purchase Order Number", required = false) @RequestParam(required = false) final String poNum)
            throws InvalidCartException, PaymentAuthorizationException {
   	 
   	 final CustomerActivityData customerActivityData = new CustomerActivityData();
   	 
		 final String ipAddress = getIpAddress(request);
		 final String userAgent = getBrowserString(request);
		 customerActivityData.setIpAddress(ipAddress);

		 try {
			 wholescriptsOrdersFacade.validateUser();
			 skipOrderFieldValueSetter.setValue(fields);
			 cartLoaderStrategy.loadCart(cartId);
			 wholescriptsOrdersFacade.updateTermsInCart(termsChecked);
			 final CartData cartData = cartFacade.getCurrentCart();
			 wholescriptsOrdersFacade.validateCart(cartData);
			 if (orderCreationRequest != null) {
				 orderCreationRequest.setIpAddress(ipAddress);
				 orderCreationRequest.setUserAgent(userAgent);
			 }
			 if("ZACH".equalsIgnoreCase(cartData.getPaymentType().getCode()) ||
					 "ZWIR".equalsIgnoreCase(cartData.getPaymentType().getCode())) {
				 wholescriptsOrdersFacade.createACHPaymentInfo(orderCreationRequest);
			 } else {
				 wholescriptsOrdersFacade.validateAndAuthorizePayment(cartData, orderCreationRequest, customerActivityData);
			 }
			 
			 final PlaceOrderData  placeOrderData = new PlaceOrderData();
			 placeOrderData.setPoNumber(poNum);
			 final AbstractOrderData abstractOrderData = b2bCheckoutFacade.placeOrder(placeOrderData);
			 final OrderData orderData = (OrderData) abstractOrderData;
			 customerActivityData.setMessage("Order created: " + orderData.getCode());
			 customerActivityData.setEvent("order_created");
			 wholescriptsUserFacade.saveCustomerActivity(customerActivityData);
			 orderData.getEntries().forEach(e ->{
				e.getProduct().setName(e.getProductDescription());
			 });
			 return dataMapper.map(orderData, OrderWsDTO.class, fields);

		 }
		 catch (final Exception ex) {
			 customerActivityData.setIpAddress(ipAddress);
			 customerActivityData.setMessage("Order failed due to Error : " + String.format(ex.getMessage()));
			 wholescriptsUserFacade.saveCustomerActivity(customerActivityData);
			 throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.MISSING);
		 }


    }

    @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
    @PostMapping("/orderListById")
    @ResponseBody
    @Operation(operationId = "getOrderHistoryData", summary = "Returns a paginated list of order history data for a patient from Patients profile.")
    @ApiBaseSiteIdAndUserIdParam
    @ApiResponse(responseCode = "200", description = "OrderHistoryListWsDTO",
            content = @Content(schema = @Schema(implementation = OrderHistoryListPractionerResponseWsDTO.class)))
    public OrderHistoryListPractionerResponseWsDTO getOrderHistoryDataForPatient(
            @Parameter(description = "Response configuration. Options: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues = {"BASIC", "DEFAULT", "FULL"}))
            @RequestParam(defaultValue = "DEFAULT") final String fields,
            @RequestParam("startDate") final String startDate, @RequestParam("endDate") final String endDate,@RequestParam final String patientId) throws WholescriptsPageNotFoundException{

        try {
      	   
      	   final B2BCustomerModel patientCustomer =  b2bCustomerService.getUserForUID(patientId);
      	   if (Objects.isNull(patientCustomer)) {
      	   	throw new WholescriptsPageNotFoundException("Patient id is not valid");
      	   }
            final OrderHistoryPractionerResponseoneData orderHistoryListResponseData = wholescriptsOrdersFacade.getOrderHistoryListByPatientID(patientCustomer, startDate, endDate);
            if (Objects.isNull(orderHistoryListResponseData)) {
                throw new RequestParameterException("No order history data found for the given patientId and date range");
            }
            return dataMapper.map(orderHistoryListResponseData, OrderHistoryListPractionerResponseWsDTO.class, fields);
        } catch (final WholescriptsPageNotFoundException ex) {
           throw new WholescriptsPageNotFoundException(String.format(ex.getMessage()));
       } catch (final Exception ex) {
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.MISSING);
        }

    }

    @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
    @GetMapping("/get-auto-ship")
    @Operation(operationId = "getAutoShip", summary = "Returns a auto ship details data for a patient and practitioner both.")
    @ApiBaseSiteIdAndUserIdParam
    @ApiResponse(responseCode = "200", description = "AutoShipOrderResponseWsDTO",
            content = @Content(schema = @Schema(implementation = AutoShipOrderResponseWsDTO.class)))
    public AutoShipOrderResponseWsDTO getAutoShipOrders() {
        try {
            final AutoShipOrderResponseData autoShipResponseData = wholescriptsOrdersFacade.
                    getAutoShipOrders("");
            return dataMapper.map(autoShipResponseData, AutoShipOrderResponseWsDTO.class);
        } catch (final Exception ex) {
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.MISSING);
        }
    }

    @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
    @GetMapping("/get-auto-ship/{orderNumber}")
    @Operation(operationId = "getAutoShipByOrderId", summary = "Returns a auto ship details data for a patient and practitioner both based on particular order number.")
    @ApiBaseSiteIdAndUserIdParam
    @ApiResponse(responseCode = "200", description = "AutoShipOrderResponseWsDTO",
    content = @Content(schema = @Schema(implementation = AutoShipOrderResponseWsDTO.class)))
    public AutoShipOrderResponseWsDTO getAutoShipOrders(@PathVariable final String orderNumber) throws WholescriptsPageNotFoundException {
   	 try {
   		 final AutoShipOrderResponseData autoShipResponseData = wholescriptsOrdersFacade.
   				 getAutoShipOrders(orderNumber);
   		 return dataMapper.map(autoShipResponseData, AutoShipOrderResponseWsDTO.class);
   	 } catch(WholescriptsPageNotFoundException ex) {
   		 throw new WholescriptsPageNotFoundException("Autoship does'nt exist:-"+orderNumber);
   	 }catch (final Exception ex) {
   		 throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.MISSING);
   	 }
    }

    @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
    @GetMapping("/has-auto-ship")
    @Operation(operationId = "hasAutoShip", summary = "Checks if the current user has any auto-ship data available.")
    @ApiBaseSiteIdAndUserIdParam
    @ApiResponse(responseCode = "200", description = "Returns true if auto-ship data exists, otherwise false.",
            content = @Content(schema = @Schema(implementation = Boolean.class)))
    public boolean hasAutoShip() {
        try {
            return wholescriptsOrdersFacade.hasAutoShip();
        } catch (final Exception ex) {
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.MISSING);
        }
    }

    @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
    @PostMapping("/updateAutoShipOrder")
    @Operation(operationId = "updateAutoShipByOrderId", summary = "Updates  a auto ship details data for a patient and practitioner both based on particular order number.")
    @ApiBaseSiteIdAndUserIdParam
    @ApiResponse(responseCode = "200", description = "OrderUpdateAutoShipResponseWsDTO",
            content = @Content(schema = @Schema(implementation = OrderUpdateAutoShipResponseWsDTO.class)))
    public OrderUpdateAutoShipResponseWsDTO updateAutoShipOrder(@Parameter(description="Request body parameter that contains details such as the orderNumber ,shipto,credit card info  ") @RequestBody final OrderChangeAutoShipRequestWsDTO orderChangeAutoShipRequestWsDTO ) {
        try {

            final OrderUpdateAutoShipResponseData updateAutoShipResponseData =wholescriptsOrdersFacade.updateAutoShipOrder(orderChangeAutoShipRequestWsDTO);

            return dataMapper.map(updateAutoShipResponseData, OrderUpdateAutoShipResponseWsDTO.class);
        } catch (final Exception ex) {
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.MISSING);
        }
    }

    @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
    @DeleteMapping(value = "/cancel-order/{orderId}")
    @ResponseBody
    @Operation(operationId = "cancelOrderAndEntries", summary = "Cancels the order and its entries in SAP and the local system.", description = "This API cancels a customer's order and all related order entries by removing them from both SAP.")
    @ApiBaseSiteIdAndUserIdParam
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Boolean> cancelOrderAndEntries(@Parameter(description = "Order identifier.", example = "00000001", required = true) @PathVariable final String orderId,
                                                      @Parameter final String orderEntryNumber)
    {
        try {
            wholescriptsOrdersFacade.cancelOrderInSAP(orderId,orderEntryNumber);
            return Map.ofEntries(Map.entry("SUCCESS", Boolean.TRUE));
        } catch (final Exception e) {
            throw new RequestParameterException(String.format(e.getMessage()), RequestParameterException.INVALID, orderId);
        }
    }

    @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
    @GetMapping("/orderWebError")
    @Operation(operationId = "orderWebError", summary = "Gets the details of orderWebError.")
    @ApiBaseSiteIdAndUserIdParam
    @ApiResponse(responseCode = "200", description = "OrderHistoryResponseWsDTO",
            content = @Content(schema = @Schema(implementation = OrderHistoryResponseWsDTO.class)))
    public OrderHistoryResponseWsDTO orderHistoryWebErrors(){
        try {
            final OrderHistoryResponseData orderHistoryResponseData =wholescriptsOrdersFacade.getOrderHistoryWebErrors();
            return dataMapper.map(orderHistoryResponseData, OrderHistoryResponseWsDTO.class);
        } catch (final Exception ex) {
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.MISSING);
        }
    }

    
    private String getIpAddress (final HttpServletRequest request) {
		 String ipAddress = request.getHeader("X-FORWARDED-FOR");  
		 if (ipAddress == null) {  
		     ipAddress = request.getRemoteAddr();  
		 }
		 return ipAddress;
	 }
	 
	 private String getBrowserString (final HttpServletRequest request) {
		 return request.getHeader("User-Agent");
	 }

	 @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
	 @PostMapping(value ="/autoship/verification", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	 @ResponseStatus(HttpStatus.CREATED)
	 @ResponseBody
	 @Operation(operationId = "verifyAutoshipOrderForAddressChange", summary = "It will verify autoship order for address Change.")
	 @ApiBaseSiteIdAndUserIdParam
	 public ResponseEntity<String> verifyAutoshipAddressAndProducts(@Parameter(description="Request body parameter that contains details such as Address and product codes ") @RequestBody final WholescriptsValidateAutoshipWsDTO request ) throws Exception {
		 wholescriptsOrdersFacade.verifyAutoshipAddressForRestriction(request);
		 return ResponseEntity.ok("verified successfully.");
	 }
	 
	 	@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
	 	@ResponseBody
	 	@ExceptionHandler({ AgeRestrictionException.class })
	 	public ErrorListWsDTO handleAgeRestrictionException(final AgeRestrictionException ex)
	 	{
	 		return handleErrorInternal("AgeRestrictionException", ex.getMessage());
	 	}

		@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
		@ResponseBody
		@ExceptionHandler({ POBoxAgeRestrictionException.class })
		public ErrorListWsDTO handlePOBoxAgeRestrictionException(final POBoxAgeRestrictionException ex)
		{
			return handleErrorInternal("POBoxAgeRestrictionException", ex.getMessage());
		}
		
		@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
		@ResponseBody
		@ExceptionHandler({ RegionExclusionException.class })
		public ErrorListWsDTO handleRegionExclusionException(final RegionExclusionException ex)
		{
			return handleErrorInternal("RegionExclusionException", ex.getMessage());
		}
		
		 @ResponseStatus(value = HttpStatus.BAD_REQUEST)
		 @ResponseBody
		 @ExceptionHandler({ WholescriptsPageNotFoundException.class })
		 public ErrorListWsDTO handleModelNotFoundnException(final WholescriptsPageNotFoundException ex)
		 {
			 return handleErrorInternal("WholescriptsPageNotFoundException", ex.getMessage());
		 }
		
}
