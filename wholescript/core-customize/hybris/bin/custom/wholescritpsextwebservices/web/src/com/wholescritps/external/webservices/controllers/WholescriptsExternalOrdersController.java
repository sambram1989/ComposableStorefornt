package com.wholescritps.external.webservices.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wholescripts.core.facade.WholescriptsExternalAPIFacade;
import com.wholescripts.core.page.validators.WholescriptsExternalOrderRequestValidator;
import com.wholescripts.data.*;
import com.wholescritps.external.webservices.constants.WholescritpsextwebservicesConstants;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@RestController
@ApiVersion("v2")
@RequestMapping(value = "/{baseSiteId}")
@Tag(name = "Wholescripts External Order APIs ")
public class WholescriptsExternalOrdersController {

    private static final Logger LOG = LoggerFactory.getLogger(WholescriptsExternalOrdersController.class);

    @Resource(name = "dataMapper")
    protected DataMapper dataMapper;

    @Resource(name = "wholescriptsExternalAPIFacade")
    private WholescriptsExternalAPIFacade wholescriptsExternalAPIFacade;

    @Resource(name = "wsExternalOrderRequestValidator")
    private WholescriptsExternalOrderRequestValidator wsExternalOrderRequestValidator;

    @Secured({"ROLE_EXTERNAL_CLIENT"})
    @RequestMapping(value = "/place/order", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    @ApiBaseSiteIdParam
    public ExternalOrderResponseData createExternalOrder(
            @Parameter(name = "ExternalRequestData", description = "External Order Request") @RequestBody final ExternalOrderRequestData externalOrderRequest,
            @Parameter(hidden = true) final HttpServletRequest request,
            @RequestParam(defaultValue = WholescritpsextwebservicesConstants.DEFAULT_FIELD_SET) final String fields) {

        final Errors errors = validate(externalOrderRequest, "External order request data", wsExternalOrderRequestValidator);
        ExternalOrderResponseData response = new ExternalOrderResponseData();
        final List<String> returnMessages = new ArrayList<String>();
        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(e -> returnMessages.add(e.getDefaultMessage()));
            response.setSuccess(false);
            response.setReturnMessage(returnMessages);
            return response;
        }
        try {
            final OrderCreationRequest orderCreationRequest = new OrderCreationRequest();
            orderCreationRequest.setUserAgent(getBrowserString(request));
            orderCreationRequest.setIpAddress(getIpAddress(request));
            response = wholescriptsExternalAPIFacade.createExternalOrder(externalOrderRequest, orderCreationRequest);
        } catch (final Exception ex) {
            ex.printStackTrace();
            LOG.error("Error in external order creation ", ex.getMessage());
            response.setSuccess(false);
            returnMessages.add(ex.getMessage());
            response.setReturnMessage(returnMessages);
        }
        return response;
    }

    protected Errors validate(final Object object, final String objectName, final Validator validator) {
        final Errors errors = new BeanPropertyBindingResult(object, objectName);
        validator.validate(object, errors);
        return errors;
    }

    @Secured({"ROLE_EXTERNAL_CLIENT"})
    @DeleteMapping(value = "/cancel/order")
    @ResponseBody
    @Operation(operationId = "cancel orderfrom external system", summary = "cancel orderfrom external system")
    @ApiBaseSiteIdAndUserIdParam
    @ResponseStatus(HttpStatus.OK)
    public ExternalOrderResponseData cancelExternalOrder(@RequestBody final ExternalOrderDetailRequestData requestData) {

        ExternalOrderResponseData response = new ExternalOrderResponseData();
        final List<String> returnMessages = new ArrayList<String>();
        try {
            response = wholescriptsExternalAPIFacade.cancelExternalOrder(requestData);
        } catch (final Exception ex) {
            LOG.error("Error in external order cancellation ", ex.getMessage());
            response.setSuccess(false);
            returnMessages.add(ex.getMessage());
            response.setReturnMessage(returnMessages);
        }
        return response;
    }

    @Secured({"ROLE_EXTERNAL_CLIENT"})
    @RequestMapping(value = "/order/detail", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(operationId = "getOrderDetail", summary = "get the details of order from external system")
    @ApiBaseSiteIdAndUserIdParam
    @ResponseStatus(HttpStatus.OK)
    public String orderDetail(@RequestBody final ExternalOrderDetailRequestData request) {

        ExternalOrderDetailsResponseWSDTO response = new ExternalOrderDetailsResponseWSDTO();
        final List<String> returnMessage = new ArrayList<String>();
        if (StringUtils.isEmpty(request.getCustomerSAPId())
                || StringUtils.isEmpty(request.getOrderNumber())) {
            response.setSuccess(false);
            returnMessage.add("Invalid customersapid or ordernumber");
            response.setReturnMessage(returnMessage);
            return jsonProcess(response);
        }
        try {
            response = wholescriptsExternalAPIFacade.getExternalOrderDetail(request);
        } catch (final Exception ex) {
            LOG.error("Error in external order cancellation ", ex.getMessage());
            response.setSuccess(false);
            returnMessage.add(ex.getMessage());
            response.setReturnMessage(returnMessage);
        }
        return jsonProcess(response);
    }

    private String getIpAddress(final HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    private String getBrowserString(final HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }
    
    private String jsonProcess(final Object obj) {
   	 final Gson gson = new GsonBuilder().serializeNulls().create();
       return gson.toJson(obj);
    }


}
