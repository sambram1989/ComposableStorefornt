/**
 *
 */
package com.wholescripts.occ.controllers;

import de.hybris.platform.b2bocc.v2.controllers.BaseController;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wholescripts.core.facade.WholescriptsUserFacade;
import com.wholescripts.data.CustomerActivityData;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;



@RestController
@Tag(name = "WholescriptsCustomerActivity")
@RequestMapping(value = "/{baseSiteId}/users/{userId}/activity")
public class WholescriptsActivityController extends BaseController {
	
	@Resource(name = "wholescriptsUserFacade")
   private WholescriptsUserFacade wholescriptsUserFacade;

	@Secured({"ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP"})
   @PostMapping
   @ResponseBody
   @ApiBaseSiteIdAndUserIdParam
   @Operation(operationId = "saveCustomerActivity", summary = "Save Customer Activity")
   public Boolean saveCustomerActivity(@Parameter(description = "Request of save customer activity") @RequestBody final CustomerActivityData customerActivityData,
   		@Parameter(hidden = true) final HttpServletRequest request) {

		if (customerActivityData == null) {
			throw new ValidationException("Request can not be null");
		}
		else {
			customerActivityData.setIpAddress(getIpAddress(request));
		}

		return wholescriptsUserFacade.saveCustomerActivity(customerActivityData);
   }
	
	private String getIpAddress (final HttpServletRequest request) {
		 String ipAddress = request.getHeader("X-FORWARDED-FOR");  
		 if (ipAddress == null) {  
		     ipAddress = request.getRemoteAddr();  
		 }
		 return ipAddress;
	 }
}
