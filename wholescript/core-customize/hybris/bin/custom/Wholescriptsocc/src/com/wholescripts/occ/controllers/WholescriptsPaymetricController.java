package com.wholescripts.occ.controllers;

import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wholescripts.core.service.WholescriptsIntegrationsService;
import com.wholescripts.core.service.WholescriptsTokenizationService;
import com.wholescripts.data.WholescriptsPaymetricTokenizationData;
import com.wholescripts.dto.PaymetricResponseDTO;
import com.wholescripts.dto.WholescriptsPaymetricTokenizationWsDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;



@Controller
@ApiVersion("v2")
@RequestMapping(value = "/{baseSiteId}/XiIntercept3")
@Tag(name = "WholeScripts  paymetric")
public class WholescriptsPaymetricController
{

	@Resource(name = "wholescriptsTokenizationService")
	WholescriptsTokenizationService tokenizationService;
	@Resource(name = "dataMapper")
	protected DataMapper dataMapper;
	@Resource(name = "wholescriptsIntegrationsService")
	private WholescriptsIntegrationsService wholescriptsIntegrationsService;


	@RequestMapping(value = "/getAccessToken", method = RequestMethod.GET)
	@ResponseBody
	@Operation(operationId = "paymetric access token", description = "paymetric access token", summary = "paymetric access token")
	public WholescriptsPaymetricTokenizationWsDTO getXiInterceptAccessToken(final HttpServletRequest request)
	{

		final WholescriptsPaymetricTokenizationData paymetricTokenizationData = tokenizationService.getAccessToken(request);
		return dataMapper.map(paymetricTokenizationData, WholescriptsPaymetricTokenizationWsDTO.class,
				FieldSetLevelHelper.DEFAULT_LEVEL);

	}

   @RequestMapping(value = "/card-details",method = RequestMethod.GET)
   @ResponseBody
   @Operation(operationId = "card details", description = "card details", summary = "it will return card details")
   public PaymetricResponseDTO getCardDetails(@Parameter(description = "accessToken") @RequestParam(required = true) String accessToken) throws Exception
   {
     return  dataMapper.map(wholescriptsIntegrationsService.getCardDetails(accessToken), PaymetricResponseDTO.class);
   }
   
}

