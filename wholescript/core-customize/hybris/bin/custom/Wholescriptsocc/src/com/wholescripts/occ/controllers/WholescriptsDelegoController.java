package com.wholescripts.occ.controllers;


import de.hybris.platform.b2bocc.security.SecuredAccessConstants;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.wholescripts.core.facade.WholescriptsDelegoFacade;
import com.wholescripts.data.ACHDelegoTokenResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@Tag(name = "Delego")
@ApiVersion("v2")
@RequestMapping(value = "/{baseSiteId}/users/{userId}")
public class WholescriptsDelegoController {
	
	private static final Logger LOG = Logger.getLogger(WholescriptsDelegoController.class);

	@Resource(name = "wholescriptsDelegoFacade")
	private WholescriptsDelegoFacade wholescriptsDelegoFacade;



	@Secured(
	{ SecuredAccessConstants.ROLE_CUSTOMERGROUP, SecuredAccessConstants.ROLE_GUEST,
			SecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, SecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@GetMapping(value = "/delego/token")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiBaseSiteIdAndUserIdParam
	@Operation(operationId = "delegoToken", summary = "Creates Delego Token", description = "Creates Delego Token")
	public ACHDelegoTokenResponse createToken() {

		ACHDelegoTokenResponse response = new ACHDelegoTokenResponse();
		try {
			String token = wholescriptsDelegoFacade.getJWTToken();
			response.setToken(token);
			response.setSorg("1710");
		}
		catch (final Exception e) {
			e.printStackTrace();
			LOG.error("Error in creating delego token ", e);
		}
		return response;

	}




}
