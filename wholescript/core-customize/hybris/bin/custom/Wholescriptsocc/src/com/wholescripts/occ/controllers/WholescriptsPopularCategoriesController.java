/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.occ.controllers;

import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wholescripts.core.facade.WholescriptsPopularCategoriesFacade;
import com.wholescripts.dto.WsPopularCategoriesData;
import com.wholescripts.dto.WsPopularCategoriesWsDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;


/**
 * Main Controller for Popular categories, products, brands content on Shop page. This can be deprecated in future if
 * any 3rd party API integrated
 */
@Controller
@ApiVersion("v2")
@RequestMapping(value = "/{baseSiteId}/popular")
@Tag(name = "Wholescripts Popular Categories")
public class WholescriptsPopularCategoriesController
{
	private static final Logger LOG = LogManager.getLogger(WholescriptsPopularCategoriesController.class);
	@Resource(name = "dataMapper")
	protected DataMapper dataMapper;
	@Resource(name = "wholescriptsPopularCategoriesFacade")
	private WholescriptsPopularCategoriesFacade wholescriptsPopularCategoriesFacade;

	@GetMapping(value = "/{type}")
	@ResponseBody
	@ApiBaseSiteIdParam
	@Operation(operationId = "getShopPage", summary = "Returns the populor category, brand and products data in GuestShopPage")
	public WsPopularCategoriesWsDTO getShopPage(
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body.", schema = @Schema(allowableValues =
			{ "BASIC", "DEFAULT", "FULL" }))
			@RequestParam(defaultValue = "BASIC")
			final String fields, @Parameter(description = "CATEGORY or BRAND", required = true)
			@PathVariable(value = "type", required = true)
			final String type)
	{
		final WsPopularCategoriesData popularCategoriesData = wholescriptsPopularCategoriesFacade
				.getPopularCategoriesDetails(fields, type);

		return dataMapper.map(popularCategoriesData, WsPopularCategoriesWsDTO.class);
	}

}
