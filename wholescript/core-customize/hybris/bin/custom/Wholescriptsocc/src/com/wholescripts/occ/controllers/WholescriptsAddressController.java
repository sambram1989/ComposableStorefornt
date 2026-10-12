package com.wholescripts.occ.controllers;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.b2bocc.v2.controllers.BaseController;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.commerceservices.request.mapping.annotation.RequestMappingOverride;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.webservicescommons.cache.CacheControl;
import de.hybris.platform.webservicescommons.cache.CacheControlDirective;
import de.hybris.platform.webservicescommons.dto.PaginationWsDTO;
import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;
import de.hybris.platform.webservicescommons.pagination.WebPaginationUtils;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.wholescripts.core.exception.AccertifyValidationException;
import com.wholescripts.core.exception.AgeRestrictionException;
import com.wholescripts.core.exception.POBoxAgeRestrictionException;
import com.wholescripts.core.facade.WholescriptsUserFacade;
import com.wholescripts.data.AddressChangeResponseData;
import com.wholescripts.data.WholescriptsGetAddressFromSapResponseData;
import com.wholescripts.dto.WholescriptsGetAddressFromSapResponseWSDTO;
import com.wholescripts.occ.constants.WholescriptsSecuredAccessConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;


@Controller
@ApiVersion("v2")
@Tag(name = "Wholescripts Address Controller ")
@RequestMapping(value = "/{baseSiteId}/users/{userId}/addresses")
@CacheControl(directive = CacheControlDirective.PRIVATE)
public class WholescriptsAddressController extends BaseController
{

    public static final String ADDRESS_DOES_NOT_EXIST = "Address with given id: '%s' doesn't exist or belong to another user";
    private static final Logger LOG = LoggerFactory.getLogger(WholescriptsAddressController.class);
    protected static final String DEFAULT_FIELD_SET = FieldSetLevelHelper.DEFAULT_LEVEL;

    private static final String ADDRESS_MAPPING = "firstName,lastName,titleCode,email,phone,cellphone,line1,line2,town,postalCode,region(isocode),district,country(isocode),defaultAddress,remarks,accertifyEventSource,accertifyHashedPassword,accertifyDeviceTransactionId,accertifyUbaID,accertifyPageId,accertifyUbaEvents,accertifySessionID,addressNumber";
    private static final String OBJECT_NAME_ADDRESS = "address";
    private static final String OBJECT_NAME_ADDRESS_ID = "addressId";
    private static final String OBJECT_NAME_ADDRESS_DATA = "addressData";

    @Resource(name = "addressDTOValidator")
    private Validator addressDTOValidator;

    @Resource(name = "wholescriptsUserFacade")
    private WholescriptsUserFacade wholescriptsUserFacade;

    @Resource(name = "webPaginationUtils")
    private WebPaginationUtils webPaginationUtils;

    @Resource(name = "dataMapper")
    private DataMapper dataMapper;

    @Resource(name = "b2bCustomerService")
    private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;

    @Secured({ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
    @RequestMapping(method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMappingOverride(priorityProperty = "occ.wholescripts.address.priority")
    @Operation(operationId = "createAddress", summary = "Creates a new address.", description = "Creates a new address with detailed information provided.")
    @ApiBaseSiteIdAndUserIdParam
    public AddressWsDTO createAddress(
            @Parameter(description = "Address object.", required = true) @RequestBody final AddressWsDTO address,
            @Parameter(hidden = true) final HttpServletRequest request,
            @ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
    {
        validate(address, OBJECT_NAME_ADDRESS, addressDTOValidator);
        AddressData addressData = getDataMapper().map(address, AddressData.class, ADDRESS_MAPPING);
        addressData.setShippingAddress(true);
        addressData.setVisibleInAddressBook(true);
        addressData.setIpAddress(getIpAddress(request));
        addressData.setUserAgent(getBrowserString(request));
        try {
            addressData = wholescriptsUserFacade.addAddress(addressData);
        } catch (Exception e) {
            throw new RequestParameterException(String.format(e.getMessage(), sanitize(addressData.getId())),
                    RequestParameterException.INVALID, addressData.getId());
        }

        return getDataMapper().map(addressData, AddressWsDTO.class, fields);
    }
    @Secured({ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
    @GetMapping
    @ResponseBody
    @RequestMappingOverride(priorityProperty = "occ.wholescripts.address.priority")
    @Operation(operationId = "getAddresses", summary = "Retrieves the addresses of a customer.", description = "Retrieves a list of all the addresses associated with a customer.")
    @ApiBaseSiteIdAndUserIdParam
    @ApiResponse(responseCode = "200", description = "List of customer's addresses")
    public AddressListWsDTO getAddresses(@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
            { "BASIC", "DEFAULT", "FULL" }))
                                         @RequestParam(defaultValue = "DEFAULT")
                                         final String fields, @Parameter(description = "Current result page. Default value is 0.")
                                         @RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE)
                                         final int currentPage, @Parameter(description = "Number of results returned per page. Default value: 10.")
                                         @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE)
                                         final int pageSize, @RequestParam(required = false)
                                         final String addressSearch)
    {
        try
        {
            List<AddressData> addressDataSearchPageDataList=wholescriptsUserFacade.getAllAddressForCustomers(addressSearch);
            final AddressListWsDTO addressList = new AddressListWsDTO();
            final List<AddressWsDTO> addressWsDTOList = addressDataSearchPageDataList.stream()
                    .map(addressData -> dataMapper.map(addressData, AddressWsDTO.class, fields)).toList();

            addressList.setAddresses(addressWsDTOList);
            return addressList;
        } catch (Exception e) {
            throw new RequestParameterException(String.format(e.getMessage()),
                    RequestParameterException.INVALID);
        }

    }


    @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
    @PostMapping("/get-all-addresses")
    @ResponseBody
    @Operation(operationId = "getAllAddresses", summary = "Fetches all addresses associated with the current customer.", description = "Returns a list of all addresses linked to the current customer account.")
    @ApiBaseSiteIdAndUserIdParam
    @ApiResponse(responseCode = "200", description = "WholescriptsGetAddressFromSapResponseWSDTO", content = @Content(schema = @Schema(implementation = WholescriptsGetAddressFromSapResponseWSDTO.class))
    )
    public WholescriptsGetAddressFromSapResponseWSDTO getAllAddresses(@Parameter(description = "Response configuration. Options: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues = {"BASIC", "DEFAULT", "FULL"})) @RequestParam(defaultValue = "DEFAULT") final String fields)
    {
        try {
            final WholescriptsGetAddressFromSapResponseData addressDataList = wholescriptsUserFacade.getAddressFromSap(b2bCustomerService.getCurrentB2BCustomer());
            return dataMapper.map(addressDataList, WholescriptsGetAddressFromSapResponseWSDTO.class, fields);

        } catch (final Exception ex) {
            throw new RequestParameterException("Error fetching addresses: " + ex.getMessage(), RequestParameterException.INVALID
            );
        }
    }

    @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
    @PatchMapping("/update-address")
    @ResponseBody
    @Operation(operationId = "updateAddress", summary = "update the  addresses associated with the current customer.", description = "to update addresses linked to the current customer account.")
    @ApiBaseSiteIdAndUserIdParam
    @ApiResponse(responseCode = "200", description = "AddressWsDTO", content = @Content(schema = @Schema(implementation = AddressWsDTO.class)))
    public Map<String, Boolean> updateAddress(@Parameter(description = "Response configuration. Options: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues = {"BASIC", "DEFAULT", "FULL"})) @RequestParam(defaultValue = "DEFAULT") final String fields, 
   		 @RequestBody final AddressWsDTO addressWsDTO, @Parameter(hidden = true) final HttpServletRequest request) throws Exception {
        try {
            validate(addressWsDTO, OBJECT_NAME_ADDRESS, addressDTOValidator);
            final AddressData addressData = getDataMapper().map(addressWsDTO, AddressData.class, ADDRESS_MAPPING);
            addressData.setShippingAddress(true);
            addressData.setVisibleInAddressBook(true);
            addressData.setIpAddress(getIpAddress(request));
            addressData.setUserAgent(getBrowserString(request));
            final AddressChangeResponseData updatedAddressData = wholescriptsUserFacade.updateAddress(addressWsDTO, addressData);
            return Map.ofEntries(Map.entry("SUCCESS", Boolean.TRUE));
        } catch (ModelSavingException  | WebserviceException e ) {
            throw new RequestParameterException(String.format(e.getMessage(), sanitize(addressWsDTO.getId())),
                    RequestParameterException.INVALID, addressWsDTO.getId());
        }
    }

    @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
    @DeleteMapping(value = "/remove/{addressId}")
    @ResponseBody
    @Operation(operationId = "deleteCustomerAddress", summary = "Removes the address of a customer from SAP and the local system.", description = "This API deletes a customer's address by removing it from both SAP.")
    @ApiBaseSiteIdAndUserIdParam
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Boolean> deleteAddress(@Parameter(description = "Address identifier.", example = "00000001", required = true) @PathVariable final String addressId, @RequestBody final AddressWsDTO addressWsDTO, final HttpServletRequest request)
    {
        try {
            final B2BCustomerModel currentCustomer = b2bCustomerService.getCurrentB2BCustomer();
            final AddressData addressData = getDataMapper().map(addressWsDTO, AddressData.class, ADDRESS_MAPPING);
            addressData.setIpAddress(getIpAddress(request));
            addressData.setUserAgent(getBrowserString(request));
            wholescriptsUserFacade.removeAddressInSAP(currentCustomer, addressId, addressData);
            return Map.ofEntries(Map.entry("SUCCESS", Boolean.TRUE));
        } catch (Exception e) {
            throw new RequestParameterException(String.format(e.getMessage(), sanitize(addressId)),
                    RequestParameterException.INVALID, addressId);
        }
    }

    //Using this For Deleting address with Credit cards with it.
    @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
    @RequestMapping(value = "/remove-address/{addressId}", method = RequestMethod.DELETE)
    @Operation(operationId = "removeAddress", summary = "Deletes the address of a customer.", description = "Deletes the address of a customer, ensuring any linked credit cards are removed first.")
    @ApiBaseSiteIdAndUserIdParam
    @ResponseStatus(HttpStatus.OK)
    public void removeAddress(@Parameter(description = "Address identifier.", example = "00000001", required = true) @PathVariable final String addressId, @RequestParam(required = false) List<String> creditCardIds, @RequestBody final AddressWsDTO addressWsDTO, final HttpServletRequest request) {
        try {
            final AddressData addressData = getDataMapper().map(addressWsDTO, AddressData.class, ADDRESS_MAPPING);
            addressData.setIpAddress(getIpAddress(request));
            addressData.setUserAgent(getBrowserString(request));
            wholescriptsUserFacade.removeAddressInCommerce(addressId, creditCardIds,addressData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private AddressListWsDTO formatSearchPageDataResult(String fields, SearchPageData<AddressData> searchPageDataResult) {

        final AddressListWsDTO addressList = new AddressListWsDTO();
        final List<AddressWsDTO> addressWsDTOList = searchPageDataResult.getResults().stream()
                .map(addressData -> getDataMapper().map(addressData, AddressWsDTO.class, fields)).toList();
        addressList.setAddresses(addressWsDTOList);

        final PaginationWsDTO paginationWsDTO = webPaginationUtils.buildPaginationWsDto(searchPageDataResult.getPagination());
        addressList.setPagination(paginationWsDTO);
        return addressList;

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
 	@ExceptionHandler({ AccertifyValidationException.class })
 	public ErrorListWsDTO handleAccertifyValidationException(final AccertifyValidationException ex)
 	{
 		return handleErrorInternal("AccertifyValidationException", ex.getMessage());
 	}
	
 	
}

