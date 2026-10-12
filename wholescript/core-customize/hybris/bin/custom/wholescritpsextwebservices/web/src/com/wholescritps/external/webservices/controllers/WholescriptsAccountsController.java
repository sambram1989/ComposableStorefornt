package com.wholescritps.external.webservices.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.wholescripts.core.facade.WholescriptsExternalAPIFacade;
import com.wholescripts.core.facade.WholescriptsUserFacade;
import com.wholescripts.core.utils.WholescriptsCoreUtils;
import com.wholescripts.data.*;
import com.wholescripts.dto.*;
import com.wholescritps.external.webservices.constants.WholescritpsextwebservicesConstants;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * @author Sadiq
 */

@RestController
@ApiVersion("v2")
@RequestMapping(value = "/{baseSiteId}/accounts")
@Tag(name = "Wholescripts Accounts APIs ")
public class WholescriptsAccountsController {

    private static final Logger LOG = LoggerFactory.getLogger(WholescriptsAccountsController.class);
    @Resource(name = "dataMapper")
    protected DataMapper dataMapper;
    @Resource(name = "wholescriptsUserFacade")
    private WholescriptsUserFacade wholescriptsUserFacade;
    @Resource(name = "wholescriptsExternalAPIFacade")
    private WholescriptsExternalAPIFacade wholescriptsExternalAPIFacade;
    
    @Resource(name = "b2bUnitService")
    private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;

    @PostMapping("/addAddresses")
    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(operationId = "createAddress", summary = "Creates a new address.", description = "Creates a new address with detailed information provided.")
    @ApiBaseSiteIdParam
    public WholescriptsExternalAPIAddressCreationWsDTO createAddress(
            @Parameter(description = "Address object.", required = true) @RequestBody final WholescriptsExternalAPIAddressWsDTO wholescriptsExternalAPIAddressWsDTO,
            @ApiFieldsParam @RequestParam(defaultValue = WholescritpsextwebservicesConstants.DEFAULT_FIELD_SET) final String fields) {

   	  final WholescriptsExternalAPIAddressCreationWsDTO response = new WholescriptsExternalAPIAddressCreationWsDTO();
        final List<String> returnMessage = new ArrayList<>();
        
        if (Objects.isNull(wholescriptsExternalAPIAddressWsDTO) || StringUtils.isBlank(wholescriptsExternalAPIAddressWsDTO.getCustomerSAPId())) {
            response.setSuccess(false);
            returnMessage.add("Request or customerSAPID can not be blank");
            response.setReturnMessage(returnMessage);
            return response;
        }

        final WholescriptsExternalAPIAddressData addressData = dataMapper.map(wholescriptsExternalAPIAddressWsDTO, WholescriptsExternalAPIAddressData.class);
        String salesAreaAcccountNo = WholescriptsCoreUtils.getAccountNumberWithSalesArea(addressData.getCustomerSAPId().replaceFirst("^0+", ""));
        final B2BUnitModel b2bUnitModel = b2bUnitService.getUnitForUid(salesAreaAcccountNo);
        try {
      	  if (b2bUnitModel != null) {
      		  SapAddressResponseData sapAddressResponseData = wholescriptsExternalAPIFacade.createExternalAPIAddress(addressData,b2bUnitModel);
              if (sapAddressResponseData.getRETURN().equalsIgnoreCase("0")) {
                  response.setAddressId(sapAddressResponseData.getAddressNumber());
                  response.setSuccess(true);
              } else {
                 response.setSuccess(false);
                 returnMessage.add("Address creation failed");
                 response.setReturnMessage(returnMessage);
             }
      	  }
      	   else {
                response.setSuccess(false);
                returnMessage.add("Invalid customerSAPID");
                response.setReturnMessage(returnMessage);
            }
        } catch (final Exception e) {
            e.printStackTrace();
            response.setSuccess(false);
            returnMessage.add(e.getMessage());
            response.setReturnMessage(returnMessage);
        }
        return response;
    }

    @Operation(operationId = "Get Customer Data", summary = "This API will ge Customer Data")
    @Cacheable(value = "createExternalAddressCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'getCustomer',#requestDto)")
    @ApiBaseSiteIdParam
    @Secured({"ROLE_EXTERNAL_CLIENT"})
    @GetMapping("/getCustomer")
    public String getCustomer(
            @Parameter(name = "customerIdWsDTO", description = "Customer ID as part of Request body ")
            @RequestBody final ExternalCustomerRequestDTO requestDto, @Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues = {"BASIC", "DEFAULT", "FULL"})) @RequestParam(defaultValue = "DEFAULT") final String fields) {

        WholescriptsExternalAPICustomerWsDTO response = new WholescriptsExternalAPICustomerWsDTO();
        final List<String> returnMessage = new ArrayList<String>();
        if (StringUtils.isBlank(requestDto.getCustomerSAPID()) && StringUtils.isBlank(requestDto.getEmail())) {
            response.setSuccess(false);
            returnMessage.add("Customer SAP Id and Email both can not be empty");
            response.setReturnMessage(returnMessage);
            return jsonProcess(response);
        }

        try {
            final WholescriptsExternalAPICustomerData customer = wholescriptsExternalAPIFacade.getCustomerData(requestDto.getCustomerSAPID(), requestDto.getEmail());
            response = dataMapper.map(customer, WholescriptsExternalAPICustomerWsDTO.class, fields);
            response.setSuccess(true);

        } catch (final Exception ex) {
            ex.printStackTrace();
            response.setSuccess(false);
            returnMessage.add(ex.getMessage());
            response.setReturnMessage(returnMessage);
        }
        return jsonProcess(response);
    }

    @Operation(operationId = "Get Addresses Data", summary = "This API will get Addresses Data")
    @Cacheable(value = "getExternalAddressCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'getCustomer',#customerSAPIdDTO)")
    @ApiBaseSiteIdParam
    @Secured({"ROLE_EXTERNAL_CLIENT"})
    @GetMapping("/getAddresses")
    public String getAddresses(
            @Parameter(name = "customerIdWsDTO", description = "Customer ID as part of Request body ")
            @RequestBody final CustomerSAPIdDTO customerSAPIdDTO, @Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues = {"BASIC", "DEFAULT", "FULL"})) @RequestParam(defaultValue = "DEFAULT") final String fields) {

   	  WholescriptsExternalAPIAddressListWsDTO externalAPICustomerWsDTO = new WholescriptsExternalAPIAddressListWsDTO();
   	  final List<String> errorMsg = new ArrayList<>();
   	  try {
            final WholescriptsExternalAPIAddressListData wholescriptsExternalAPIAddressListData = wholescriptsExternalAPIFacade.getAddressData(customerSAPIdDTO.getCustomerSAPID());
            externalAPICustomerWsDTO = dataMapper.map(wholescriptsExternalAPIAddressListData, WholescriptsExternalAPIAddressListWsDTO.class);
            externalAPICustomerWsDTO.setSuccess(Boolean.TRUE);
        } catch (final Exception e) {
            e.printStackTrace();
            errorMsg.add(e.getMessage());
            externalAPICustomerWsDTO.setSuccess(Boolean.FALSE);
            externalAPICustomerWsDTO.setReturnMessage(errorMsg);
        }
   	  return jsonProcess(externalAPICustomerWsDTO);
    }

    @Operation(operationId = "Get Patients Data", summary = "This API will get Patients Data")
    @Cacheable(value = "getExternalPatientCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'getPatients',#customerSAPIdDTO)")
    @ApiBaseSiteIdParam
    @Secured({"ROLE_EXTERNAL_CLIENT"})
    @GetMapping("/getPatients")
    public String getPatients(
            @Parameter(name = "customerIdWsDTO", description = "Customer ID as part of Request body ")
            @RequestBody final CustomerSAPIdDTO customerSAPIdDTO, @Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues = {"BASIC", "DEFAULT", "FULL"})) @RequestParam(defaultValue = "DEFAULT") final String fields) {
   	 
   	 WholescriptsExternalAPIPatientListWsDTO patientListWsDTO = new WholescriptsExternalAPIPatientListWsDTO(); 
   	 final List<String> errorMsg = new ArrayList<>();
   	 try {
            final WholescriptsExternalAPIPatientListData wholescriptsExternalAPIPatientListData = wholescriptsExternalAPIFacade.getPatientsData(customerSAPIdDTO.getCustomerSAPID());
            patientListWsDTO = dataMapper.map(wholescriptsExternalAPIPatientListData, WholescriptsExternalAPIPatientListWsDTO.class);
            
            patientListWsDTO.setSuccess(Boolean.TRUE);

        } catch (final Exception e) {
            e.printStackTrace();
            errorMsg.add(e.getMessage());
            patientListWsDTO.setSuccess(Boolean.FALSE);
            patientListWsDTO.setReturnMessage(errorMsg);
            
        }
   	 return jsonProcess(patientListWsDTO);
    }
    
    private String jsonProcess(final Object obj) {
   	 
   		try {
   			  ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
   	        String json = ow.writeValueAsString(obj);
   			  return json;
   		}
   		catch (JsonProcessingException e){
   			e.printStackTrace();
   		}
   		return StringUtils.EMPTY;
      }
}
