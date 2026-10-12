/**
 * 
 */
package com.wholescripts.occ.controllers;

import com.wholescripts.data.*;
import com.wholescripts.dto.AccertifyVerificationCreateAccountResponseWsDTO;
import com.wholescripts.dto.AccertifyVerificationUpdateAccountResponseWsDTO;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
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

import com.wholescripts.core.facade.WholescriptsAccertifyFacade;
import com.wholescripts.dto.AccertifyVerificationResponseWsDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@ApiVersion("v2")
@RequestMapping(value = "/{baseSiteId}/accertify")
@Tag(name = "Accertify")
public class WholescriptsAccertifyController
{
	@Resource(name = "dataMapper")
	private DataMapper dataMapper;
	@Resource(name = "wholescriptsAccertifyFacade")
	private WholescriptsAccertifyFacade wholescriptsAccertifyFacade;
	
	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@PostMapping(value = "/verification/login")
	@ResponseBody
	@ApiBaseSiteIdParam
	@Operation(operationId = "loginVerification", summary = "login validation from accertify")
	public Map<String, Boolean> loginVerificationFromAccertify(@RequestBody
	final AccertifyVerificationRequestData requestData, @Parameter(description = "password")
	@RequestParam(required = false)
	final String password, @Parameter(description = "accountExist")
	@RequestParam(required = false)
	final String accountExist) throws Exception
	{
		try
		{
			final Map<String, Boolean> map = new HashMap<>();
			final boolean isFraud = wholescriptsAccertifyFacade.accertifyLoginVerificaton(requestData,password,accountExist);
			map.put("Approve", isFraud);
			return map;
		}
		catch (final Exception ex)
		{
			throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.INVALID);
		}
	}


	@Secured({ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@PostMapping(value = "/verification/create-account")
	@ResponseBody
	@ApiBaseSiteIdParam
	@Operation(operationId = "createAccountVerification", summary = "Create account validation from accertify")
	public AccertifyVerificationCreateAccountResponseWsDTO createAccountVerification(@RequestBody final AccertifyVerificationCreateAccountRequestData requestData) throws Exception
	{
		try {
			AccertifyVerificationCreateAccountResponseData responseData = wholescriptsAccertifyFacade.accertifyCreateAccountVerification(requestData);
			return dataMapper.map(responseData, AccertifyVerificationCreateAccountResponseWsDTO.class);
		}
		catch (Exception ex) {
			throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.INVALID);
		}
	}

	@Secured({ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@PostMapping(value = "/verification/update-account")
	@ResponseBody
	@ApiBaseSiteIdParam
	@Operation(operationId = "updateAccountVerification", summary = "Update account validation from accertify")
	public AccertifyVerificationUpdateAccountResponseWsDTO updateAccountVerification(@RequestBody final AccertifyVerificationUpdateAccountRequestData requestData) throws Exception
	{
		try {
			AccertifyVerificationUpdateAccountResponseData responseData = wholescriptsAccertifyFacade.accertifyUpdateAccountVerification(requestData);
			return dataMapper.map(responseData, AccertifyVerificationUpdateAccountResponseWsDTO.class);
		}
		catch (Exception ex) {
			throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.INVALID);
		}
	}
	
	@PostMapping(value = "/email/verified")
	@ResponseBody
	@ApiBaseSiteIdParam
	@Operation(operationId = "verifyEmailAndUpdateCDCAccertifyFlag", summary = "verify Email And Update CDC Accertify Flag")
	public void verifyEmailAndUpdateCDCAccertifyFlag( @Parameter(description = "token to verify customer email") @RequestParam final String token) throws Exception
	{
		try {
			wholescriptsAccertifyFacade.verifyEmailAndUpdateCDCAccertifyFlag(token);
		}
		catch (Exception ex) {
			throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.INVALID);
		}
	}

}
