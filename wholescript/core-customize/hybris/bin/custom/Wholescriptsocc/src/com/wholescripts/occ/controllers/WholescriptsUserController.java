/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.occ.controllers;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.b2bocc.v2.controllers.BaseController;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.commercewebservicescommons.dto.user.UserWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.webservicescommons.dto.PaginationWsDTO;
import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;
import de.hybris.platform.webservicescommons.pagination.WebPaginationUtils;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.wholescripts.core.enums.FedTaxClass;
import com.wholescripts.core.exception.WholescriptsPageNotFoundException;
import com.wholescripts.core.facade.ReferralCodesFacade;
import com.wholescripts.core.facade.WholescriptsAccountsFacade;
import com.wholescripts.core.facade.WholescriptsUserFacade;
import com.wholescripts.core.service.WholescriptsIntegrationsService;
import com.wholescripts.data.AgingResponseData;
import com.wholescripts.data.AvalaraTaxLinkData;
import com.wholescripts.data.CustomerExternalDetailsData;
import com.wholescripts.data.EstoreCredentialRequestData;
import com.wholescripts.data.EstoreCredentialResponseData;
import com.wholescripts.data.EstoreOrderHistory;
import com.wholescripts.data.EstoreOrderHistoryRequestData;
import com.wholescripts.data.EstoreShippingConfirmationRequest;
import com.wholescripts.data.ExternalSystemResponseData;
import com.wholescripts.data.PractitionerAmendmentDetailData;
import com.wholescripts.data.SharedRewardsResponseData;
import com.wholescripts.data.ShopifyProductDetailsData;
import com.wholescripts.data.ShopifyProductsRequestData;
import com.wholescripts.data.ShopifySearchResultData;
import com.wholescripts.data.ShopifyStatusResponseData;
import com.wholescripts.data.WishlistData;
import com.wholescripts.data.XymonetW9ResponseData;
import com.wholescripts.dto.AgingResponseWsDTO;
import com.wholescripts.dto.ApiFilterListWsDTO;
import com.wholescripts.dto.ApiFilterWsDTO;
import com.wholescripts.dto.AvalaraTaxLinkWsDTO;
import com.wholescripts.dto.PatientWsDTO;
import com.wholescripts.dto.PatientsListWsDTO;
import com.wholescripts.dto.PractitionerAmendmentRequestWsDTO;
import com.wholescripts.dto.SharedRewardsResponseWsDTO;
import com.wholescripts.dto.W9FormData;
import com.wholescripts.dto.W9FormWsDTO;
import com.wholescripts.dto.WishlistListsWsDTO;
import com.wholescripts.dto.WishlistWsDTO;
import com.wholescripts.dto.XymonetW9ResponseDTO;
import com.wholescripts.dto.referralCodes.ReferralCodeWsDTO;
import com.wholescripts.occ.constants.WholescriptsSecuredAccessConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;


/**
 * Main Controller for Users
 */
@Controller
@ApiVersion("v2")
@RequestMapping(value = "/{baseSiteId}/users/{userId}")
@Tag(name = "Wholescripts Users")
public class WholescriptsUserController extends BaseController {
    public static final String DEFAULT_CURRENT_PAGE = "0";
    public static final String DEFAULT_PAGE_SIZE = "30";
    private static final Logger LOG = LogManager.getLogger(WholescriptsUserController.class);
    @Resource(name = "dataMapper")
    protected DataMapper dataMapper;
    @Resource(name = "enumerationService")
    protected EnumerationService enumerationService;
    @Resource(name = "wholescriptsUserFacade")
    private WholescriptsUserFacade wholescriptsUserFacade;
    @Resource(name = "webPaginationUtils")
    private WebPaginationUtils webPaginationUtils;
    @Resource(name = "referralCodesFacade")
    private ReferralCodesFacade referralCodesFacade;
    @Resource(name = "userService")
    private UserService userService;
    @Resource(name = "wholescriptsIntegrationsService")
    private WholescriptsIntegrationsService wholescriptsIntegrationsService;
    @Resource(name = "b2bCustomerService")
    private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;

    @Resource(name = "wholescriptsAccountsFacade")
    private WholescriptsAccountsFacade wholescriptsAccountsFacade;

    @Secured({"ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP"})
    @GetMapping(value = "/patients")
    @ResponseBody
    @ApiBaseSiteIdAndUserIdParam
    @Operation(operationId = "getAllPatients", summary = "Returns the paginated list of patients for practionersId")
    public PatientsListWsDTO getAllPatients(@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body.", schema = @Schema(allowableValues = {"BASIC", "DEFAULT", "FULL"})) @RequestParam(defaultValue = "BASIC") final String fields, @Parameter(description = "Current result page. Default value is 0.") @RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage, @Parameter(description = "Number of results returned per page. Default value: 10.") @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize, @Parameter(description = "Sorting method applied to the return results.", example = "name:asc") @RequestParam(required = false, defaultValue = "name:asc") final String sort, @Parameter(description = "Referral Codes selected from filters", required = false) @RequestParam(required = false) final String referralCode, @Parameter(description = "Discounts selected from filters", required = false) @RequestParam(required = false) final String discount, @Parameter(description = "Search Text entered in search box", required = false) @RequestParam(required = false) final String searchText) {
        final SearchPageData<CustomerData> searchPageData = webPaginationUtils.buildSearchPageData(sort, currentPage, pageSize, true);
        final SearchPageData<CustomerData> patientsList = wholescriptsUserFacade.getAllPatientsForPractitioner(searchPageData, referralCode, discount, searchText);

        final PatientsListWsDTO listData = new PatientsListWsDTO();
        final List<PatientWsDTO> patientWsDTOList = patientsList.getResults().stream().map(patientData -> dataMapper.map(patientData, PatientWsDTO.class, fields)).toList();
        listData.setPatients(patientWsDTOList);

        final PaginationWsDTO paginationWsDTO = webPaginationUtils.buildPaginationWsDto(patientsList.getPagination());
        listData.setPagination(paginationWsDTO);
        return listData;
    }


    @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
    @GetMapping(value = "/avaTaxLink")
    @ResponseBody
    @ApiBaseSiteIdAndUserIdParam
    @Operation(operationId = "getAvalaraTaxLink", summary = "Returns the Avalara tax URL for current B2BCustomer")
    public AvalaraTaxLinkWsDTO getAvalaraTaxLink() throws WebserviceException {
        try {
            final AvalaraTaxLinkData avalaraTaxLinkData = wholescriptsUserFacade.getAvalaraTaxLinkForCurrentUser();
            return dataMapper.map(avalaraTaxLinkData, AvalaraTaxLinkWsDTO.class);
        } catch (final WebserviceException ex) {
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.INVALID);
        }
    }

    @Secured({"ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP"})
    @GetMapping(value = "/apiFilters")
    @ResponseBody
    @ApiBaseSiteIdAndUserIdParam
    @Operation(operationId = "getAllPatients", summary = "Returns the Referral codes list for specific practitioner")
    public ApiFilterListWsDTO getReferralCodes(@PathVariable("baseSiteId") final String baseSiteId, @ApiFieldsParam @RequestParam(defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields) {
		final ApiFilterListWsDTO apiFilters = new ApiFilterListWsDTO();
		final List<ApiFilterWsDTO> filterListWsDTO = referralCodesFacade.getApiFilters()
				.stream()
				.map(apiWsDto -> dataMapper.map(apiWsDto, ApiFilterWsDTO.class, fields))
				.toList();
		apiFilters.setApiFilters(filterListWsDTO);
		return apiFilters;
    }

    @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
    @PatchMapping(value = "/setPatientDirectStatus")
    @ResponseBody
    @ApiBaseSiteIdAndUserIdParam
    @Operation(operationId = "updatePatientDirectnConsultFeeStatus", summary = "It will update the Patient Direct and Consult fee Status")
    public ResponseEntity updatePatientDirectnConsultFee(@RequestParam final boolean isPatientDirect, @RequestParam final boolean receiveConsultationFee) throws Exception {
        try {
            wholescriptsUserFacade.updatePatientDirectnConsultFee(isPatientDirect, receiveConsultationFee);
            return new ResponseEntity(HttpStatus.OK);
        } catch (final ModelSavingException ex) {
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.MISSING);
        }
    }

    @Secured({"ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP"})
    @PostMapping(value = "/sendW9Info")
    @ResponseBody
    @ApiBaseSiteIdAndUserIdParam
    @Operation(operationId = "sendW9Info", summary = "Saves the W9Form data in db")
    public W9FormWsDTO sendW9Info(@Parameter(description = "Data object that contains information necessary for W9Form request creation", required = true) @RequestBody(required = true) final W9FormWsDTO w9FormWsDTO) {

        final W9FormData w9FormData = dataMapper.map(w9FormWsDTO, W9FormData.class);

        final W9FormData result = wholescriptsUserFacade.saveW9FormData(w9FormData);
        return dataMapper.map(result, W9FormWsDTO.class);
    }

    @Secured({"ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP"})
    @GetMapping(value = "/w9formStatus")
    @ResponseBody
    @Operation(operationId = "getW9FormStatus", summary = "Allows get w9 form status.", description = " This API request is get status for w9 form.")
    @ApiBaseSiteIdAndUserIdParam
    public List<Map<String, Object>> getStatus() {
        final List<FedTaxClass> fedTaxList = enumerationService.getEnumerationValues(FedTaxClass._TYPECODE);
        final List<Map<String, Object>> result = new ArrayList<>();
        for (final FedTaxClass fedTax : fedTaxList) {
            final Map<String, Object> map = new HashMap<>();
            final String text = enumerationService.getEnumerationName(fedTax);
            if (StringUtils.isNotEmpty(text)) {
                map.put("Key", fedTax.getCode());
                map.put("Text", text);
                result.add(map);
            }
        }
        return result;
    }

    @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
    @GetMapping(value = "/showW9Link")
    @ResponseBody
    @ApiBaseSiteIdAndUserIdParam
    @Operation(operationId = "showW9Link", summary = "It will Displays a W9 form link to download the form")
    public Map<String, String> showW9Link() throws Exception {
        try {
            final Map<String, String> configParams = new HashMap<>();
            final Map<String, String> response = new HashMap<>();
            final Map<String, String> map = wholescriptsIntegrationsService.integrateWithXymonetShowW9(b2bCustomerService.getCurrentB2BCustomer());
            response.put("success", String.valueOf(map.get("success")));
            response.put("msg", map.get("msg"));
            return response;
        } catch (final WebserviceException ex) {
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.MISSING);
        }
    }

    @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
    @GetMapping(value = "/downloadW9Form")
    @ResponseBody
    @ApiBaseSiteIdAndUserIdParam
    @Operation(operationId = "downloadW9Form", summary = "The W9 for gets downloaded")
    public XymonetW9ResponseDTO downloadW9Form() throws Exception {
        try {
            final XymonetW9ResponseData response = wholescriptsIntegrationsService.integrateWithXymonetDownloadW9(b2bCustomerService.getCurrentB2BCustomer());
            return dataMapper.map(response, XymonetW9ResponseDTO.class);
        } catch (final WebserviceException ex) {
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.MISSING);
        }
    }

	@Secured({"ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP"})
	@PostMapping(value = "/favorites")
	@ResponseBody
	@ApiBaseSiteIdAndUserIdParam
	@Operation(operationId = "favorites", summary = "Saves the favorites data")
	public WishlistWsDTO saveWishList(
			@Parameter(description = "Data object that contains information necessary for Wishlist request creation", required = true)
			@RequestParam(required = true) final String productCode) {
		try {
			final WishlistData wishListData = wholescriptsUserFacade.saveWishList(productCode);
			return dataMapper.map(wishListData, WishlistWsDTO.class);
		} catch (Exception e) {
			LOG.error("Error while saving wishlist entry for product: {}", productCode, e);
			return new WishlistWsDTO();
		}
	}

	@Secured({"ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP"})
	@DeleteMapping(value = "/removeFavoriteEntry")
	@ResponseBody
	@ApiBaseSiteIdAndUserIdParam
	@Operation(operationId = "removeFavoriteEntry", summary = "Removes an entry from the favorites list")
	public WishlistWsDTO removeWishListEntry(
			@Parameter(description = "Data object that contains information necessary for Wishlist request creation", required = true)
			@RequestParam(required = true) final String productCode) {
		try {
			final WishlistData wishListData = wholescriptsUserFacade.removeWishListEntry(productCode);
			return dataMapper.map(wishListData, WishlistWsDTO.class);
		} catch (Exception e) {
			LOG.error("Error while removing wishlist entry for product: {}", productCode, e);
			return new WishlistWsDTO();
		}
	}

    @Secured({"ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP"})
    @PostMapping(value = "/getAllWishList")
    @ResponseBody
    @ApiBaseSiteIdAndUserIdParam
    @Operation(operationId = "favorites", summary = "Saves the favorites data")
    public WishlistListsWsDTO getAllWishList() {
        final List<WishlistData> wishlistDataList = wholescriptsUserFacade.getAllWishList();
        WishlistListsWsDTO list = new WishlistListsWsDTO();
        list.setWishLists(dataMapper.mapAsList(wishlistDataList, WishlistWsDTO.class,FieldSetLevelHelper.FULL_LEVEL));
        return list;
    }

    @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
    @PostMapping("/get-shared-rewards")
    @ResponseBody
    @Operation(operationId = "getSharedRewards", summary = "Returns shared rewards data for a patient.")
    @ApiBaseSiteIdAndUserIdParam
    @ApiResponse(responseCode = "200", description = "SharedRewardsResponseWsDTO", content = @Content(schema = @Schema(implementation = SharedRewardsResponseWsDTO.class)))
    public SharedRewardsResponseWsDTO getSharedRewards(@Parameter(description = "Response configuration. Options: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues = {"BASIC", "DEFAULT", "FULL"}))
                                                       @RequestParam(defaultValue = "DEFAULT") final String fields,@RequestParam("date") final String date) {
        try {
            SharedRewardsResponseData sharedRewardsResponseData = wholescriptsAccountsFacade.getSharedRewards(b2bCustomerService.getCurrentB2BCustomer(),date);
            return dataMapper.map(sharedRewardsResponseData, SharedRewardsResponseWsDTO.class, fields);
        } catch (final Exception ex) {
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.MISSING);
        }
    }
    @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
    @PostMapping("/get-aging")
    @ResponseBody
    @Operation(operationId = "getAgingData", summary = "Returns aging data for a patient.")
    @ApiBaseSiteIdAndUserIdParam
    @ApiResponse(responseCode = "200", description = "AgingResponseWsDTO", content = @Content(schema = @Schema(implementation = AgingResponseWsDTO.class)))
    public AgingResponseWsDTO getAgingData(@Parameter(description = "Response configuration. Options: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues = {"BASIC", "DEFAULT", "FULL"})) @RequestParam(defaultValue = "DEFAULT") final String fields,
                                           @RequestParam("keyDate") final String keyDate) throws WholescriptsPageNotFoundException {
        try {
			B2BCustomerModel customer=b2bCustomerService.getCurrentB2BCustomer();
			validatePaymentTerms(customer);
            AgingResponseData agingResponseData = wholescriptsAccountsFacade.getAging(customer, keyDate);
            return dataMapper.map(agingResponseData, AgingResponseWsDTO.class, fields);
        }
		catch(ModelNotFoundException ex) {
			throw new WholescriptsPageNotFoundException("Aging tab should not be visible to Customer");
		}catch (final Exception ex) {
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.MISSING);
        }
    }

	private void validatePaymentTerms(B2BCustomerModel customer) {
		if(Objects.nonNull(customer.getDefaultB2BUnit())&& Objects.nonNull(customer.getDefaultB2BUnit().getSapPaymentTerms())&& Objects.nonNull(customer.getDefaultB2BUnit().getSapPaymentTerms().getCode())) {
			if(!customer.getDefaultB2BUnit().getSapPaymentTerms().getCode().startsWith("NT"))
			{
				throw new ModelNotFoundException("Aging tab should not be visible to Customer");

			}

		}
	}

	@Secured(
   		 { "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
    @GetMapping(value = "/getPatientDetails")
    @ResponseBody
    @ApiBaseSiteIdAndUserIdParam
    @Operation(operationId = "getPatientDetails", summary = "Returns the patientDetails")
    public UserWsDTO getPatientDetails(
   		 @Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body.")
   		 @RequestParam(defaultValue = "DEFAULT")
   		 final String fields, final String email) throws WholescriptsPageNotFoundException
    {
   	 try {
   		 final CustomerData patient = wholescriptsUserFacade.getPatientDetails(email);
   		 return dataMapper.map(patient, UserWsDTO.class, fields);
   	 }
   	 catch(ModelNotFoundException ex) {
   		 throw new WholescriptsPageNotFoundException("Customer not found.");
   	 }

    }

	 @Secured(
	 { "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	 @PostMapping(value = "/age/verification")
	 @ResponseStatus(HttpStatus.OK)
	 @ResponseBody
	 @ApiBaseSiteIdAndUserIdParam
	 @Operation(operationId = "verifyAge", summary = "verify the age from persona and update in SAP")
	 public void verifyAgeAndUpdateInSAP(@Parameter(description = "Inquiry Id to get age data")
	 @RequestParam(required = true)
	 final String inquiryId)
	 {
		 try
		 {
			 wholescriptsUserFacade.verifyAgeAndUpdateInSAP(inquiryId);
		 }
		 catch (Exception e)
		 {
			 throw new RequestParameterException(String.format("Age verification is failed for InquiryId:-" + inquiryId),
					 RequestParameterException.INVALID);
		 }

	 }

	 @Secured(
	 { "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	 @PostMapping(value = "/sendAmendment")
	 @ResponseBody
	 @ApiBaseSiteIdAndUserIdParam
	 @Operation(operationId = "Send Practitioner Amendment", summary = "it will submit the practitioner amendment")
	 public ExternalSystemResponseData sendPractitionerAmendment(@RequestBody
	 final PractitionerAmendmentRequestWsDTO requestDTO, @Parameter(hidden = true) final HttpServletRequest request) throws Exception {
		 try {
			 requestDTO.setIp(getIpAddress(request));
			 requestDTO.setBrowserString(getBrowserString(request));
			 return wholescriptsUserFacade.sendPractitionerAmendment(requestDTO);
		 }
		 catch (final Exception ex) {
			 throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.MISSING);
		 }
	 }

	 @Secured(
	 { "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	 @GetMapping(value = "/externalDetails")
	 @ResponseBody
	 @ApiBaseSiteIdAndUserIdParam
	 @Operation(operationId = "Get External Details", summary = "Returns Customer External Details")
	 public CustomerExternalDetailsData getExternalDetails(@RequestParam(defaultValue = "DEFAULT")
	 final String fields) {
		 try {
			 return wholescriptsUserFacade.getExternalDetails();
		 }
		 catch (final Exception ex) {
			 throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.MISSING);
		 }
	 }
	 
	 @Secured(
	 { "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	 @GetMapping(value = "/amendment")
	 @ResponseBody
	 @ApiBaseSiteIdAndUserIdParam
	 @Operation(operationId = "Get Practitioner Amendment Details", summary = "Returns Practitioner Amendment Details")
	 public PractitionerAmendmentDetailData getPractitionerAmendment(@RequestParam(required = true)
	 final String type) {
		 try {
			 return wholescriptsUserFacade.getPractitionerAmendment(type);
		 }
		 catch (final Exception ex) {
			 throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.MISSING);
		 }
	 }
	 
	 @Secured(
	 { "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	 @PostMapping(value = "/estore/addCredential")
	 @ResponseBody
	 @ApiBaseSiteIdAndUserIdParam
	 @Operation(operationId = "Add Estore Credentials", summary = "Add Estore Credentials")
	 public EstoreCredentialResponseData addEstoreCredentials(@RequestBody final EstoreCredentialRequestData request)
	 {
		 try {
			 return wholescriptsUserFacade.addEstoreCredentials(request);
		 }
		 catch (final Exception ex) {
			 throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.MISSING);
		 }
	 }
	 
	 @Secured(
	 { "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	 @PostMapping(value = "/estore/deleteCredential")
	 @ResponseBody
	 @ApiBaseSiteIdAndUserIdParam
	 @Operation(operationId = "Delete Estore Credentials", summary = "Delete Estore Credentials")
	 public EstoreCredentialResponseData deleteEstoreCredentials(@RequestBody final EstoreCredentialRequestData request)
	 {
		 try {
			 return wholescriptsUserFacade.deleteEstoreCredentials(request);
		 }
		 catch (final Exception ex) {
			 throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.MISSING);
		 }
	 }
	 
	 @Secured(
	 { "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	 @PostMapping(value = "/estore/changeCredential")
	 @ResponseBody
	 @ApiBaseSiteIdAndUserIdParam
	 @Operation(operationId = "Change Estore Credentials", summary = "Change Estore Credentials")
	 public EstoreCredentialResponseData changeEstoreCredentials(@RequestBody final EstoreCredentialRequestData request)
	 {
		 try {
			 return  wholescriptsUserFacade.changeEstoreCredentials(request);
		 }
		 catch (final Exception ex) {
			 throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.MISSING);
		 }
	 }
	 
	 @Secured(
	 { "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	 @GetMapping(value = "/estore/pendingOrders")
	 @ResponseBody
	 @ApiBaseSiteIdAndUserIdParam
	 @Operation(operationId = "Get Estore Pending Orders", summary = "Get Estore Pending Orders")
	 public EstoreOrderHistory getEstorePendingOrders(@Parameter(hidden = true) final HttpServletRequest request) {
		 try {
			 return wholescriptsUserFacade.getEstorePendingOrders();
		 }
		 catch (final Exception ex) {
			 throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.MISSING);
		 }
	 }
	 
	 @Secured(
	 { "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	 @GetMapping(value = "/shopify/status")
	 @ResponseBody
	 @ApiBaseSiteIdAndUserIdParam
	 @Operation(operationId = "Get Shopify Status", summary = "Get Shopify Status")
	 public ShopifyStatusResponseData getShopifyStatus(@Parameter(hidden = true) final HttpServletRequest request) {
		 try {
			 return wholescriptsUserFacade.getShopifyStatus();
		 }
		 catch (final Exception ex) {
			 throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.MISSING);
		 }
	 }
	 
	 @Secured(
	 { "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	 @GetMapping(value = "/shopify/products")
	 @ResponseBody
	 @ApiBaseSiteIdAndUserIdParam
	 @Operation(operationId = "Get Shopify Products", summary = "Get Shopify Products")
	 public ShopifyProductDetailsData getShopifyProducts(@Parameter(hidden = true) final HttpServletRequest request) {
		 try {
			 return wholescriptsUserFacade.getShopifyProducts();
		 }
		 catch (final Exception ex) {
			 throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.MISSING);
		 }
	 }
	 
	 @Secured(
	 { "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	 @PostMapping(value = "/shopify/products")
	 @ResponseBody
	 @ApiBaseSiteIdAndUserIdParam
	 @Operation(operationId = "Add Shopify Products", summary = "Add Shopify Products")
	 public ExternalSystemResponseData addShopifyProducts(@RequestBody final ShopifyProductsRequestData request, 
			 @Parameter(hidden = true) final HttpServletRequest httpRequest) {
		 try {
			 return wholescriptsUserFacade.addShopifyProducts(request);
		 }
		 catch (final Exception ex) {
			 throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.MISSING);
		 }
	 }
	 
	 @Secured(
	 { "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	 @DeleteMapping(value = "/shopify/products")
	 @ResponseBody
	 @ApiBaseSiteIdAndUserIdParam
	 @Operation(operationId = "Delete Shopify Products", summary = "Delete Shopify Products")
	 public ExternalSystemResponseData deleteShopifyProducts(@RequestBody final ShopifyProductsRequestData request, 
			 @Parameter(hidden = true) final HttpServletRequest httpRequest) {
		 try {
			 return wholescriptsUserFacade.deleteShopifyProducts(request);
		 }
		 catch (final Exception ex) {
			 throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.MISSING);
		 }
	 }
	 
	 @Secured(
	 { "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	 @PostMapping(value = "/estore/shippingConfirmation")
	 @ResponseBody
	 @ApiBaseSiteIdAndUserIdParam
	 @Operation(operationId = "Estore Shipping Confirmation", summary = "Estore Shipping Confirmation")
	 public ExternalSystemResponseData estoreShippingConfirmation(@RequestBody final EstoreShippingConfirmationRequest request, 
			 @Parameter(hidden = true) final HttpServletRequest httpRequest) {
		 try {
			 return wholescriptsUserFacade.estoreShippingConfirmation(request);
		 }
		 catch (final Exception ex) {
			 throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.MISSING);
		 }
	 }
	 
	 @Secured(
	 { "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	 @PostMapping(value = "/estore/order/history")
	 @ResponseBody
	 @ApiBaseSiteIdAndUserIdParam
	 @Operation(operationId = "Estore Order History", summary = "Estore Order History")
	 public EstoreOrderHistory estoreOrderHistory(@RequestBody final EstoreOrderHistoryRequestData request, 
			 @Parameter(hidden = true) final HttpServletRequest httpRequest) {
		 try {
			 return wholescriptsUserFacade.estoreOrderHistory(request);
		 }
		 catch (final Exception ex) {
			 throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.MISSING);
		 }
	 }

	@Secured(
			{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@GetMapping(value = "/getRecommendationNotificationSettings")
	@ResponseBody
	@ApiBaseSiteIdAndUserIdParam
	@Operation(operationId = "getRecommendationNotificationSettingsForUser", summary = "Returns the Notification Details(SMS or Email preferences values)")
	public UserWsDTO getRecommendationNotificationSettingsForUser(
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body.")
			@RequestParam(defaultValue = "DEFAULT")
			final String fields, final String customerId)
	{
		final CustomerData customerData = wholescriptsUserFacade.getNotificationSettingsforUser(customerId);
		return dataMapper.map(customerData, UserWsDTO.class, fields);
	}

	@Secured({"ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP"})
	@PostMapping(value = "/changePractitioner")
	@ResponseBody
	@Operation(operationId = "changePractitionerForPatient", summary = "It will changes the  practitioner  for  patient ")
	@ApiBaseSiteIdAndUserIdParam
	public String changePractitioner(@RequestBody(required = true) final ReferralCodeWsDTO referralCodeRequest) {
		try {
			return wholescriptsUserFacade.changePractitionerForPatient(referralCodeRequest);
		} catch (final Exception ex) {
			throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.MISSING);
		}
	}

	@Secured(
			{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@GetMapping(value = "/getPractitionerDetails")
	@ResponseBody
	@ApiBaseSiteIdAndUserIdParam
	@Operation(operationId = "getPractitionerDetails", summary = "Returns the practitioner Details of Patient")
	public UserWsDTO getPractitionerDetails(
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body.")
			@RequestParam(defaultValue = "DEFAULT")
			final String fields)
	{
		final CustomerData practitionerDetails = wholescriptsUserFacade.getPractitionerDetails();
		return dataMapper.map(practitionerDetails, UserWsDTO.class, fields);
	}
	
	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@GetMapping(value = "/shopify/products/search")
	@ResponseBody
	@ApiBaseSiteIdAndUserIdParam
	@Operation(operationId = "Search Shopify Products", summary = "Search Shopify Products") 
	public ShopifySearchResultData searchShopifyProducts(@RequestParam(defaultValue = "DEFAULT") final String fields, @Parameter(hidden = true) final HttpServletRequest httpRequest) {
		try {
			 return wholescriptsUserFacade.searchShopifyProducts(fields);
		}
		catch (final Exception ex) {
			ex.printStackTrace();
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
	 
	 
	 @ResponseStatus(value = HttpStatus.BAD_REQUEST)
	 @ResponseBody
	 @ExceptionHandler({ WholescriptsPageNotFoundException.class })
	 public ErrorListWsDTO handleModelNotFoundnException(final WholescriptsPageNotFoundException ex)
	 {
		 return handleErrorInternal("WholescriptsPageNotFoundException", ex.getMessage());
	 }

}



