/**
 *
 */
package com.wholescripts.occ.controllers;


import com.wholescripts.core.model.ReferralCodesModel;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.cmsfacades.data.MediaData;
import de.hybris.platform.cmssmarteditwebservices.dto.MediaWsDTO;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.webservicescommons.dto.PaginationWsDTO;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.pagination.WebPaginationUtils;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.webservicescommons.util.YSanitizer;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.wholescripts.core.facade.WebBannersAndScriptsFacade;
import com.wholescripts.core.facade.WholescriptsPatientDirectHubFacade;
import com.wholescripts.core.service.WholescriptsIntegrationsService;
import com.wholescripts.data.AccuralsInvoiceResponseListData;
import com.wholescripts.data.AccuralsPDFResponseData;
import com.wholescripts.data.ReferralCodeData;
import com.wholescripts.dto.AccuralsInvoiceResponseListWSDTO;
import com.wholescripts.dto.AccuralsPDFResponseWsDTO;
import com.wholescripts.dto.WebBannersAndScriptsData;
import com.wholescripts.dto.WebBannersAndScriptsWsDTO;
import com.wholescripts.dto.referralCodes.ReferralCodeListWsDTO;
import com.wholescripts.dto.referralCodes.ReferralCodeWsDTO;
import com.wholescripts.occ.constants.WholescriptsSecuredAccessConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.server.ResponseStatusException;


@Controller
@ApiVersion("v2")
@RequestMapping(value = "/{baseSiteId}/users/{userId}")
@Tag(name = "PatientDirectHub")
public class WholescriptsPatientDirectHubController {
    public static final String DEFAULT_CURRENT_PAGE = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";

    @Resource(name = "wholescriptsPatientDirectHubFacade")
    private WholescriptsPatientDirectHubFacade wholescriptsPatientDirectHubFacade;
    @Resource(name = "dataMapper")
    private DataMapper dataMapper;

    @Resource(name = "webPaginationUtils")
    private WebPaginationUtils webPaginationUtils;

    @Resource(name = "webBannersAndScriptsFacade")
    private WebBannersAndScriptsFacade webBannersAndScriptsFacade;

    @Resource(name = "sessionService")
    private SessionService sessionService;
    @Resource(name = "wholescriptsIntegrationsService")
 	 private WholescriptsIntegrationsService wholescriptsIntegrationsService;
    @Resource(name = "b2bCustomerService")
    private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;

    @Secured({"ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP"})
    @PostMapping(value = "/referral-code")
    @ResponseBody
    @Operation(operationId = "createReferralCodes", summary = "It will create referral codes and patient direct hub")
    @ApiBaseSiteIdAndUserIdParam
    public ReferralCodeWsDTO createReferralCodes(@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
            {"BASIC", "DEFAULT", "FULL"}))
                                                 @RequestParam(defaultValue = "DEFAULT") final String fields, @RequestBody(required = true) final ReferralCodeWsDTO referralCodeRequest) {
        try {
            ReferralCodeData referralData = wholescriptsPatientDirectHubFacade.createReferralCodeAndPatientDirectHub(referralCodeRequest);
            return dataMapper.map(referralData, ReferralCodeWsDTO.class, fields);
        } catch (final Exception e) {
            throw new RequestParameterException(String.format(e.getMessage(), sanitize(referralCodeRequest.getReferralCode())), RequestParameterException.INVALID,
                    referralCodeRequest.getReferralCode());
        }
    }

    @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
    @RequestMapping(value = "/referral-codes", method = RequestMethod.GET)
    @ResponseBody
    @Operation(operationId = "getReferralCodesData", summary = "it will return the paginated list of referral data for Practitioner")
    @ApiBaseSiteIdAndUserIdParam
    @ApiResponse(responseCode = "200", description = "ProtocolListWsDTO", content = @Content(schema = @Schema(implementation = ReferralCodeListWsDTO.class)))
    public ReferralCodeListWsDTO getReferralCodesData(
            @Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
                    {"BASIC", "DEFAULT", "FULL"}))
            @RequestParam(defaultValue = "DEFAULT") final String fields, @Parameter(description = "Current result page. Default value is 0.")
            @RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage, @Parameter(description = "Number of results returned per page. Default value: 10.")
            @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE) final int pageSize, @Parameter(description = "Sorting method applied to the return results.", example = "uid:asc")
            @RequestParam(required = false, defaultValue = "modifiedtime:desc") final String sort) {
        // Creates a SearchPageData object contains requested pagination and sorting information
        final SearchPageData<ReferralCodeData> searchPageDataInput = webPaginationUtils.buildSearchPageData(sort, currentPage, pageSize,
                true);
        // Get search result in a SearchPageData object contains search results with applied pagination and sorting information
        final SearchPageData<ReferralCodeData> referralCodeListData = wholescriptsPatientDirectHubFacade.getReferralCodesData(searchPageDataInput);
        return formatSearchPageDataResult(fields, referralCodeListData);
    }

    @Secured(
            {"ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP"})
    @RequestMapping(value = "/referral-code/{referralCode}", method = RequestMethod.PATCH)
    @ResponseBody
    @Operation(operationId = "updateReferralCodeData", summary = "Updates the discount and referral link for a given referral code.")
    @ApiBaseSiteIdAndUserIdParam
    @ApiResponse(responseCode = "200", description = "Referral code updated successfully", content = @Content(schema = @Schema(implementation = ReferralCodeWsDTO.class)))
    public ReferralCodeWsDTO updateReferralCodeData(
            @PathVariable("referralCode") String referralCode,
            @RequestParam(required = true) final int discount,
            @RequestParam(required = true) final String referralLink) {

        try {
            ReferralCodeData updatedReferralCode = wholescriptsPatientDirectHubFacade.updateReferralCodeData(referralCode, referralLink, discount);
            return dataMapper.map(updatedReferralCode, ReferralCodeWsDTO.class);

        } catch (Exception e) {
            throw new RequestParameterException(String.format(e.getMessage(), sanitize(referralCode)), RequestParameterException.INVALID, referralCode);
        }
    }

    @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
    @RequestMapping(value = "/all-referral-codes", method = RequestMethod.GET)
    @ResponseBody
    @Operation(operationId = "exportReferralCodesCSV", summary = "It will return a list of referral data for Practitioner")
    @ApiBaseSiteIdAndUserIdParam
    public ReferralCodeListWsDTO getAllReferralCodesData(
            @Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.",
                    schema = @Schema(allowableValues = {"BASIC", "DEFAULT", "FULL"}))
            @RequestParam(defaultValue = "DEFAULT") final String fields)
    {

        List<ReferralCodeData> referralCodes = wholescriptsPatientDirectHubFacade.getReferralCodesCSV();
        final ReferralCodeListWsDTO listData = new ReferralCodeListWsDTO();
        final List<ReferralCodeWsDTO> referralCodeWsDTO = referralCodes.stream()
                .map(referralCodeData -> dataMapper.map(referralCodeData, ReferralCodeWsDTO.class, fields))
                .collect(Collectors.toList());
        listData.setReferralCodeListWsDto(referralCodeWsDTO);
        return listData;
    }


    @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
    @RequestMapping(value = "/referral-code/{referralCode}/uploadProfileImage", method = RequestMethod.POST)
    @Operation(operationId = "uploadProfileImage", summary = "It will Updates the Profile Image for the ReferralCodes")
    @ResponseBody
    @ApiBaseSiteIdAndUserIdParam
    public MediaWsDTO UploadProfileImageforReferralCodes(@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues = {"BASIC", "DEFAULT", "FULL"}))
                                                         @RequestParam(defaultValue = "DEFAULT") final String fields, @Parameter(description = "referral code to Update the Image", required = true)
                                                         final String referralCode, @Parameter(description = "last name  to Update the Image", required = true)
                                                         @RequestParam(required = true) final String lastName, @Parameter(description = "The file representing the actual binary contents of the media to be created.", required = true)
                                                         @RequestPart("file") final MultipartFile multiPart) {
        try {
            MediaData MediaData = wholescriptsPatientDirectHubFacade.uploadProfileImage(referralCode, lastName, multiPart);
            return getDataMapper().map(MediaData, MediaWsDTO.class, fields);
        } catch (final ModelSavingException e) {
            e.printStackTrace();
            throw new RequestParameterException(String.format(e.getMessage(), RequestParameterException.INVALID));
        }
    }

    @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
    @RequestMapping(value = "/referral-code/{referralCode}/removeProfileImage", method = RequestMethod.DELETE)
    @Operation(operationId = "removeProfileImage", summary = "It will remove the Profile Image for the ReferralCodes")
    @ResponseBody
    @ApiBaseSiteIdAndUserIdParam
    public void removeProfileImageForReferralCodes(
            @Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.",
                    schema = @Schema(allowableValues = {"BASIC", "DEFAULT", "FULL"}))
            @RequestParam(defaultValue = "DEFAULT") final String fields,
            @Parameter(description = "referral code to remove the image", required = true) @PathVariable final String referralCode){

        try {
            wholescriptsPatientDirectHubFacade.removeProfileImage(referralCode);
        } catch (final ModelSavingException e) {
            e.printStackTrace();
            throw new RequestParameterException(String.format(e.getMessage(), RequestParameterException.INVALID));
        }
    }



    protected ReferralCodeListWsDTO formatSearchPageDataResult(final String fields,
                                                               final SearchPageData<ReferralCodeData> searchPageDataResult) {
        final ReferralCodeListWsDTO listData = new ReferralCodeListWsDTO();
        final List<ReferralCodeWsDTO> referralCodeWsDTO = searchPageDataResult.getResults().stream()
                .map(referralCodeData -> dataMapper.map(referralCodeData, ReferralCodeWsDTO.class, fields)).toList();
        listData.setReferralCodeListWsDto(referralCodeWsDTO);
        final PaginationWsDTO paginationWsDTO = webPaginationUtils.buildPaginationWsDto(searchPageDataResult.getPagination());
        listData.setPagination(paginationWsDTO);
        return listData;
    }

    @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
    @RequestMapping(value = "/referral-code/{referralCode}/addAddress", method = RequestMethod.PUT)
    @Operation(operationId = "addingAddressToReferralCodes", summary = "It will create address for the ReferralCodes")
    @ResponseBody
    @ApiBaseSiteIdAndUserIdParam
    public AddressWsDTO addingAddressToReferralCodes(@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues = {"BASIC", "DEFAULT", "FULL"}))
                                                         @RequestParam(defaultValue = "DEFAULT") final String fields, @PathVariable("referralCode") String referralCodeId,
                                                         @RequestBody(required = true)  final AddressWsDTO addressWsDTO  ) {
        try {
            AddressData addressData = dataMapper.map(addressWsDTO, AddressData.class);
            AddressData addedAddress = wholescriptsPatientDirectHubFacade.addingAddressToReferralCodes(referralCodeId,addressData);
            return getDataMapper().map(addedAddress, AddressWsDTO.class, fields);
        } catch (final ModelSavingException e) {
            e.printStackTrace();
            throw new RequestParameterException(String.format(e.getMessage(), RequestParameterException.INVALID));
        }
    }


    @Secured({"ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP"})
    @GetMapping(value = "/referral-code/{referralCode}/webBannersScripts")
    @ResponseBody
    @Operation(operationId = "getWebBannerScripts", summary = "Get WebBanner and Scripts")
    @ApiBaseSiteIdAndUserIdParam
    @ApiResponse(responseCode = "200", description = "Get WebBanner and Scripts", content = @Content(schema = @Schema(implementation = WebBannersAndScriptsWsDTO.class)))
    public WebBannersAndScriptsWsDTO getWebBannerScripts(@PathVariable("referralCode") String referralCode) {
        sessionService.setAttribute("referralCode", referralCode.split("\\|")[0]);
        sessionService.setAttribute("lastName",referralCode.split("\\|")[1]);
        WebBannersAndScriptsData data = webBannersAndScriptsFacade.getAllWebBannersAndScripts();
        sessionService.removeAttribute("referralCode");
        sessionService.removeAttribute("lastName");
        return dataMapper.map(data, WebBannersAndScriptsWsDTO.class);
    }


    public DataMapper getDataMapper() {
        return dataMapper;
    }

    protected static String sanitize(final String input) {
        return YSanitizer.sanitize(input);
    }
    

@Secured(
 			{ WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,
 				WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT })
 	@PostMapping(value = "/getAccuralsPDF")
 	@ResponseBody
 	@ApiBaseSiteIdAndUserIdParam
 	@Operation(operationId = "getAccuralsPDF", summary = "It will return base 64 url for accurals PDF")
 	public AccuralsPDFResponseWsDTO getAccuralsPDF(@RequestParam(required =true)
		final String billingDoc) throws Exception
 	{
 		try
 		{
 			AccuralsPDFResponseData responseData= wholescriptsIntegrationsService.getPDFforAccurals(b2bCustomerService.getCurrentB2BCustomer(),billingDoc);
 			return dataMapper.map(responseData, AccuralsPDFResponseWsDTO.class);
 		}
 		catch (final Exception ex)
 		{
 			throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.MISSING);
 		}
 	}
    
    @Secured(
  			{ WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,
  				WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT })
  	@PostMapping(value = "/getAccuralsInvoices")
  	@ResponseBody
  	@ApiBaseSiteIdAndUserIdParam
  	@Operation(operationId = "getAccuralsInvoices", summary = "It will return the of invoicesoices")
    public AccuralsInvoiceResponseListWSDTO getAccuralsInvoices(
 			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
 			{ "BASIC", "DEFAULT", "FULL" }))
 			@RequestParam(defaultValue = "DEFAULT")
 			final String fields, @RequestParam(defaultValue = "false")
 			final boolean isPatient, @RequestParam(required = true)
 			final String startDate, @RequestParam(required =true)
 			final String endDate) throws Exception
  	{
  		try
  		{
  			AccuralsInvoiceResponseListData invoiceListData= wholescriptsIntegrationsService.getInvoivcesForAccurals(b2bCustomerService.getCurrentB2BCustomer(),isPatient,startDate,endDate);
  			return dataMapper.map(invoiceListData, AccuralsInvoiceResponseListWSDTO.class,fields);
  		}
  		catch (final Exception ex)
  		{
  			throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.MISSING);
  		}
  	}

    @Secured({"ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP"})
    @PostMapping(value = "/accountUpdate")
    @ResponseBody
    @Operation(operationId = "accountUpdateForPatient", summary = "It will updates the  referral code and Discount for  patient ")
    @ApiBaseSiteIdAndUserIdParam
    public String accountUpdateForPatient(@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
            {"BASIC", "DEFAULT", "FULL"}))
                                                 @RequestParam(defaultValue = "DEFAULT") final String fields, @RequestBody(required = true) final ReferralCodeWsDTO referralCodeRequest,@RequestParam
    final String patientId) {
        try {
            wholescriptsPatientDirectHubFacade.updateAccountUpdateForPatient(referralCodeRequest,patientId);
            return "Referral Codes Updated Successfully";
        } catch (final Exception e) {
            throw new RequestParameterException(String.format(e.getMessage(), sanitize(referralCodeRequest.getReferralCode())), RequestParameterException.INVALID,
                    referralCodeRequest.getReferralCode());
        }
    }

    @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
    @RequestMapping(value = "/referral-code-details", method = RequestMethod.GET)
    @ResponseBody
    @Operation(operationId = "getReferralCodeDetails", summary = "Fetches details of a specific referral code")
    @ApiBaseSiteIdAndUserIdParam
    public ReferralCodeWsDTO getReferralCodeDetails(
            @Parameter(description = "Referral code for which details are requested", required = true)
            @RequestParam String referralCode,
            @Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.",
                    schema = @Schema(allowableValues = {"BASIC", "DEFAULT", "FULL"}))
            @RequestParam(defaultValue = "DEFAULT") final String fields) {

        ReferralCodeData referralCodeData = wholescriptsPatientDirectHubFacade.getReferralCodeDetails(referralCode);
        if (Objects.isNull(referralCodeData)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Referral code not found");
        }
        return dataMapper.map(referralCodeData, ReferralCodeWsDTO.class, fields);
    }

    @Secured({ WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
    @GetMapping(value = "/referralLinkDetails")
    @ResponseBody
    @Operation(operationId = "getReferralLinkDetails", summary = "Fetches details of a specific referral link")
    @ApiBaseSiteIdAndUserIdParam
    public ReferralCodeWsDTO getReferralLinkDetails(
            @Parameter(description = "Referral link for which details are requested", required = true)
            @RequestParam String referralLink,
            @Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.",
                    schema = @Schema(allowableValues = {"BASIC", "DEFAULT", "FULL"}))
            @RequestParam(defaultValue = "DEFAULT") final String fields) {

        if (StringUtils.isBlank(referralLink)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Referral link cannot be empty");
        }
        ReferralCodeData referralCodeData = wholescriptsPatientDirectHubFacade.getReferralLinkDetails(referralLink);
        if (Objects.isNull(referralCodeData)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Referral Link not found");
        }
        return dataMapper.map(referralCodeData, ReferralCodeWsDTO.class, fields);
    }


}
