/**
 * 
 */
package com.wholescripts.occ.controllers;

import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wholescripts.core.facade.WholescriptsRegistraionFacade;
import com.wholescripts.data.RegistraionRequestData;
import com.wholescripts.data.RegistraionResponseData;
import com.wholescripts.dto.RegistraionResponseWsDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@ApiVersion("v2")
@RequestMapping(value = "/{baseSiteId}/register")
@Tag(name = "Wholescripts Registration")
public class WholescriptsRegistrationController
{
	protected static final String DEFAULT_FIELD_SET = FieldSetLevelHelper.DEFAULT_LEVEL;
	private static final String ADDRESS_MAPPING = "line1,line2,town,postalCode,region(isocode),country(isocode)";
	private static final String OBJECT_NAME_ADDRESS_DATA = "addressData";
	@Resource(name = "wholescriptsRegistraionFacade")
	private WholescriptsRegistraionFacade wholescriptsRegistraionFacade;
	@Resource(name = "dataMapper")
   protected DataMapper dataMapper;

	@Secured({ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@PostMapping(value = "/email/verification")
	@ResponseBody
	@ApiBaseSiteIdParam
	@Operation(operationId = "emailverification", summary = "validate email in SCPI")
	public Map<String, Boolean> emailValidationFromSCPI(@Parameter(description = "Email for validation. Example:test@domin.com.")
	 @RequestParam(required = true) final  String email) throws Exception
	{
		final Map<String, Boolean> responseMap = new HashMap<>();
		responseMap.put("isValid", wholescriptsRegistraionFacade.emailValidationFromSCPI(email));
		return responseMap;
	}
	
	@Secured({ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@PostMapping(value = "/referral/verification")
	@ResponseBody
	@ApiBaseSiteIdParam
	@Operation(operationId = "referralverification", summary = "validate referral code in SCPI")
	public Map<String, String> referralValidationFromSCPI(@Parameter(description = "Sample Request for Referral Code validation. Example:{'REF_CODE': 'test','REF_NAME': 'test'}") @RequestBody
	final Map<String,String> request) throws Exception
	{
		return wholescriptsRegistraionFacade.referralValidationFromSCPI(request);
	}
	
	@Secured({ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@PostMapping(value = "/create/account")
	@ResponseBody
	@ApiBaseSiteIdParam
	@Operation(operationId = "createAccount", summary = "it will create Account for patient/practitioner.")
	public RegistraionResponseWsDTO createAccount(@RequestBody
	final RegistraionRequestData request) throws Exception
	{
		try {
			RegistraionResponseData responseData=wholescriptsRegistraionFacade.createAccount(request);
			return dataMapper.map(responseData, RegistraionResponseWsDTO.class);
		}catch (Exception ex) {
			throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.MISSING);
		}		
	}
}
