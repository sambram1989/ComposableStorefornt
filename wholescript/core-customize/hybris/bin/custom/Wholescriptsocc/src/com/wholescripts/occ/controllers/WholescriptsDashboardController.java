/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.occ.controllers;

import com.wholescripts.core.facade.WholescriptsUserFacade;
import com.wholescripts.data.DashboardResponseData;
import com.wholescripts.dto.DashboardResponseWsDTO;
import com.wholescripts.occ.constants.WholescriptsSecuredAccessConstants;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.b2bocc.v2.controllers.BaseController;

import javax.annotation.Resource;


/**
 * Dashboard Controller
 */
@Controller
@ApiVersion("v2")
@RequestMapping(value = "/{baseSiteId}/dashboard/{userId}")
public class WholescriptsDashboardController extends BaseController
{
	private static final Logger LOG = LogManager.getLogger(WholescriptsDashboardController.class);

	@Resource(name = "dataMapper")
	protected DataMapper dataMapper;


	@Resource(name = "wholescriptsUserFacade")
	private WholescriptsUserFacade wholescriptsUserFacade;

	@Resource(name = "b2bCustomerService")
	private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;

	@Secured(
			{ WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,
					WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/getDashboardDetails", method = RequestMethod.GET)
	@ResponseBody
	@Operation(operationId = "getDashboardDetails", summary = "return DashBoard Details")
	@ApiBaseSiteIdAndUserIdParam
	@ApiResponse(responseCode = "200", description = "DashboardResponseWsDTO", content = @Content(schema = @Schema(implementation = DashboardResponseWsDTO.class)))
	public DashboardResponseWsDTO getDashboardDetails(
			@ApiFieldsParam @RequestParam(defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields) {
		try {
			DashboardResponseData dashboardResponseData = wholescriptsUserFacade.getDashBoardDetailsFromSAP(b2bCustomerService.getCurrentB2BCustomer());

			return dataMapper.map(dashboardResponseData, DashboardResponseWsDTO.class);
		} catch (final Exception e) {
			throw new RequestParameterException(String.format(sanitize(e.getMessage())), RequestParameterException.INVALID);
		}
	}
}



