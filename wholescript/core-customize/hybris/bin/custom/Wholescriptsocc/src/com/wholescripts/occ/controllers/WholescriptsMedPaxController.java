/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.occ.controllers;

import com.wholescripts.data.OrderedMedpaxProductsData;
import com.wholescripts.dto.*;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;
import de.hybris.platform.webservicescommons.util.YSanitizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.wholescripts.core.facade.WholescriptsMedPaxFacade;
import com.wholescripts.data.MedPaxData;
import com.wholescripts.data.MedpaxCartonData;
import com.wholescripts.data.MedpaxWebKitData;
import com.wholescripts.occ.constants.WholescriptsSecuredAccessConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;


/**
 * MedPax Controller
 */
@Controller
@ApiVersion("v2")
@RequestMapping(value = "/{baseSiteId}/users/{userId}")
@Tag(name = "Wholescripts MedPax")
public class WholescriptsMedPaxController
{
	public static final String DEFAULT_CURRENT_PAGE = "0";
	public static final String DEFAULT_PAGE_SIZE = "30";
	private static final String MEDPAX_DOES_NOT_EXIST = "Medpax for given current user doesn't exist";
	private static final String OWNER_MEDPAX_DOES_NOT_EXIST = "MEDPAX with given id: '%s' doesn't exist";
	private static final String CARTON_NOT_FOUND = "Carton with given id: '%s' doesn't exist";

	private static final Logger LOG = LogManager.getLogger(WholescriptsMedPaxController.class);
	@Resource(name = "dataMapper")
	protected DataMapper dataMapper;
	@Resource(name = "enumerationService")
	protected EnumerationService enumerationService;
	@Resource(name = "wholescriptsMedPaxFacade")
	protected WholescriptsMedPaxFacade wholescriptsMedPaxFacade;


	@Secured(
	{ WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,
			WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@PostMapping(value = "/medPax/create", consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@Operation(operationId = "createMedPax", summary = "creating a MedPax", description = "creating a MedPax along with dosing and item time")
	@ApiBaseSiteIdAndUserIdParam
	public MedPaxWsDTO createMedPax(
			@Parameter(description = "Request body parameter that contains details such as the product code (product.code), the quantity of product (quantity),"
					+ "\"product\": {\n \"code\": \"1997551\"\n},\n\"qty\": \"1\"\n}"
					+ "\nRequest body are the same for all storefronts." + "\n\nThe DTO is in XML or .json format.", required = true)
			@RequestBody
			final MedPaxWsDTO medPaxWsDTO,
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
			{ "BASIC", "DEFAULT", "FULL" }))
			@RequestParam(defaultValue = "DEFAULT")
			final String fields)
	{
		return dataMapper.map(wholescriptsMedPaxFacade.createMedPax(medPaxWsDTO), MedPaxWsDTO.class, fields);
	}

	@Secured(
	{ WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,
			WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@PostMapping(value = "/updateMedPaxName", consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@Operation(operationId = "updateMedPaxName", summary = "Update a MedPax", description = "creating a MedPax along with dosing and item time")
	@ApiBaseSiteIdAndUserIdParam
	public MedPaxWsDTO updateMedPaxName(
			@Parameter(description = "update MedPax Name", required = true)
			@RequestBody
			final MedPaxWsDTO medPaxWsDTO,
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
			{ "BASIC", "DEFAULT", "FULL" }))
			@RequestParam(defaultValue = "DEFAULT")
			final String fields)
	{
		return dataMapper.map(wholescriptsMedPaxFacade.updateMedPaxName(medPaxWsDTO), MedPaxWsDTO.class, fields);
	}

	@Secured(
	{ WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,
			WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@PostMapping(value = "/updateMedPaxNotes", consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@Operation(operationId = "processMedPaxNotes", summary = "Process MedPax Notes", description = "process a MedPax notes")
	@ApiBaseSiteIdAndUserIdParam
	public MedPaxWsDTO updateMedPaxNotes(@Parameter(description = "update MedPax Notes", required = true)
	@RequestBody
	final MedPaxWsDTO medPaxWsDTO,
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
			{ "BASIC", "DEFAULT", "FULL" }))
			@RequestParam(defaultValue = "DEFAULT")
			final String fields)
	{
		return dataMapper.map(wholescriptsMedPaxFacade.updateMedPaxNotes(medPaxWsDTO), MedPaxWsDTO.class, fields);
	}

	@Secured(
	{ WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,
			WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/getMyMedPax", method = RequestMethod.GET)
	@ResponseBody
	@Operation(operationId = "getMyMedPaxDetails", summary = "It will return the details of medpax based on customer")
	@ApiBaseSiteIdAndUserIdParam
	@ApiResponse(responseCode = "200", description = "MedPaxWsDTO", content = @Content(schema = @Schema(implementation = MedPaxWsDTO.class)))
	public MedPaxWsDTO getMyMedPaxDetails(
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
			{ "BASIC", "DEFAULT", "FULL" }))
			@RequestParam(defaultValue = "DEFAULT")
			final String fields)
	{
		try
		{
			return dataMapper.map(wholescriptsMedPaxFacade.getMedPaxForCurrentUser(), MedPaxWsDTO.class, fields);
		}
		catch (final Exception e)
		{
			throw new RequestParameterException(String.format(e.getMessage()), RequestParameterException.INVALID);
		}
	}
	@Secured(
	{ WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,
			WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/getMedPaxForOwner", method = RequestMethod.GET)
	@ResponseBody
	@Operation(operationId = "getMedPaxDetailsForOwner", summary = "It will return the details of medpax based on owner")
	@ApiBaseSiteIdAndUserIdParam
	@ApiResponse(responseCode = "200", description = "MedPaxWsDTO", content = @Content(schema = @Schema(implementation = MedPaxWsDTO.class)))
	public MedPaxWsDTO getMedPaxForOwner(
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
			{ "BASIC", "DEFAULT", "FULL" }))
			@RequestParam(defaultValue = "FULL")
			final String fields, @Parameter(description = "Protocol ID parameter to get medpax for protocol") @RequestParam(required = false) final String protocolId, @Parameter(description = "Recommendation ID parameter to get medpax for recommendation") @RequestParam(required = false) final String recommendationId,@RequestParam(required = false, defaultValue = "false")
			final boolean isWSProtocol)
	{
		try
		{
			MedPaxData medpaxData = wholescriptsMedPaxFacade.getMedPaxForOwner(protocolId, recommendationId,isWSProtocol);
			if(Objects.nonNull(medpaxData)) {
				return dataMapper.map(medpaxData, MedPaxWsDTO.class, fields);
			}
			return null;
		}
		catch (final Exception e)
		{
			throw new RequestParameterException(String.format(e.getMessage(), sanitize(StringUtils.isNotEmpty(protocolId) ? protocolId :recommendationId)),
					RequestParameterException.MISSING);
		}
	}

	@Secured(
			{ WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,
				WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/getMedPaxPackets/{medPaxId}", method = RequestMethod.GET)
	@ResponseBody
	@Operation(operationId = "getMyMedPaxDetails", summary = "It will return the details of medpax packets based on code")
	@ApiBaseSiteIdAndUserIdParam
	@ApiResponse(responseCode = "200", description = "MedPaxPacketsWsDTO", content = @Content(schema = @Schema(implementation = MedPaxPacketsWsDTO.class)))
	public ReviewMedPaxWsDTO getPacketsByMexpaxCode(
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
		{ "BASIC", "DEFAULT", "FULL" }))
			@RequestParam(defaultValue = "DEFAULT")
			final String fields, @PathVariable("medPaxId") final String medPaxId)
	{
		try
		{
			return dataMapper.map(wholescriptsMedPaxFacade.getPacketsByMexpaxCode(medPaxId), ReviewMedPaxWsDTO.class, fields);
		}
		catch (final Exception e)
		{
			throw new RequestParameterException(String.format(e.getMessage()), RequestParameterException.INVALID);
		}
	}

	@Secured(
	{ WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,
			WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/medPax/{medPaxId}/addEntry", method = RequestMethod.PUT)
	@Operation(operationId = "addEntryToMedpax", summary = "It will create entry for the medpax")
	@ResponseBody
	@ApiBaseSiteIdAndUserIdParam
	public MedPaxWsDTO addEntryToMedpax(
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
			{ "BASIC", "DEFAULT", "FULL" }))
			@RequestParam(defaultValue = "DEFAULT")
			final String fields, @PathVariable("medPaxId")
			final String medPaxId, @RequestBody(required = true)
			final MedpaxEntryWsDTO medpaxEntryWsDTO)
	{
		try
		{
			return dataMapper.map(wholescriptsMedPaxFacade.addEntryToMedpax(medpaxEntryWsDTO, medPaxId), MedPaxWsDTO.class, fields);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			throw new RequestParameterException(String.format(e.getMessage(), RequestParameterException.INVALID));
		}
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/medPaxEntry/{medPaxEntryNumber}", method = RequestMethod.PATCH)
	@ResponseBody
	@Operation(operationId = "updateMedpaxEntry", summary = "Updates the Medpax entry data")
	@ApiBaseSiteIdAndUserIdParam
	@ApiResponse(responseCode = "200", description = "Medpax entry updated successfully", content = @Content(schema = @Schema(implementation = MedpaxEntryWsDTO.class)))
	public MedpaxEntryWsDTO updateMedpaxEntry(
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
			{ "BASIC", "DEFAULT", "FULL" }))
			@RequestParam(defaultValue = "DEFAULT")
			final String fields, @PathVariable("medPaxEntryNumber")
			final String medPaxEntryNumber, @RequestBody(required = true)
			final MedpaxEntryWsDTO medpaxEntryWsDTO)
	{

		try
		{
			return dataMapper.map(wholescriptsMedPaxFacade.updateMedpax(medpaxEntryWsDTO, medPaxEntryNumber), MedpaxEntryWsDTO.class,
					fields);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			throw new RequestParameterException(String.format(e.getMessage(), RequestParameterException.INVALID));
		}
	}


	@Secured(
	{ WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,
			WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@PostMapping(value = "/update/medPaxPackets", consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@Operation(operationId = "Update a MedPax packets", summary = "Update a MedPax packets", description = "Update a MedPax packets")
	@ApiBaseSiteIdAndUserIdParam
	public ReviewMedPaxWsDTO updateMedpaxPackets(
			@Parameter(description = "", required = true)
			@RequestBody
			final UpdateMedPaxPacketsWsDTO updateMedPaxPacketsWsDTO,
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
			{ "BASIC", "DEFAULT", "FULL" }))
			@RequestParam(defaultValue = "DEFAULT")
			final String fields)
	{
		return dataMapper.map(wholescriptsMedPaxFacade.updateMedpaxPackets(updateMedPaxPacketsWsDTO), ReviewMedPaxWsDTO.class,
				fields);
	}

	@Secured(
			{ WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,
					WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@PatchMapping(value = "/update/medPaxPacketsInfo", consumes =
			{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@Operation(operationId = "Update a MedPax packets Info", summary = "Update a MedPax packets Info", description = "Update a MedPax packets Info")
	@ApiBaseSiteIdAndUserIdParam
	public ReviewMedPaxWsDTO updateMedpaxPacketsInfo(
			@Parameter(description = "", required = true)
			@RequestBody
			final UpdateMedPaxPacketsInfoWsDTO updateMedPaxPacketsInfoWsDTO,
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
					{ "BASIC", "DEFAULT", "FULL" }))
			@RequestParam(defaultValue = "DEFAULT")
			final String fields)
	{
		return dataMapper.map(wholescriptsMedPaxFacade.updateMedpaxPacketsInfo(updateMedPaxPacketsInfoWsDTO), ReviewMedPaxWsDTO.class,
				fields);
	}

	protected static String sanitize(final String input)
	{
		return YSanitizer.sanitize(input);
	}

	@Secured(
			{ WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,
				WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/getMedPaxByCode", method = RequestMethod.GET)
	@ResponseBody
	@Operation(operationId = "getMedPaxDetailsForCode", summary = "It will return the details of medpax based on code")
	@ApiBaseSiteIdAndUserIdParam
	@ApiResponse(responseCode = "200", description = "MedPaxWsDTO", content = @Content(schema = @Schema(implementation = MedPaxWsDTO.class)))
	public MedPaxWsDTO getMedPaxByCode(
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
		{ "BASIC", "DEFAULT", "FULL" }))
			@RequestParam(defaultValue = "DEFAULT")
			final String fields, @Parameter(description = "Medpax ID parameter to get medpax for protocol") @RequestParam(required = true) final String medpaxId)
	{
		try
		{
			return dataMapper.map(wholescriptsMedPaxFacade.getMedPaxByCode(medpaxId), MedPaxWsDTO.class, fields);
		}
		catch (final Exception e)
		{
			throw new RequestParameterException(String.format(OWNER_MEDPAX_DOES_NOT_EXIST, sanitize(medpaxId)),
					RequestParameterException.MISSING);
		}
	}

	@Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
	@DeleteMapping(value = "/medPaxEntry/{medPaxId}/{medPaxEntryNumber}")
	@Operation(operationId = "removeMedpaxEntry", summary = "Removes a Medpax entry by its ID.")
	@ApiBaseSiteIdAndUserIdParam
	@ApiResponse(responseCode = "200", description = "Medpax entry removed successfully.")
	public ResponseEntity<String> removeMedpaxEntry(
			@PathVariable("medPaxEntryNumber") final String medPaxEntryNumber,
			@PathVariable("medPaxId") final String medPaxId) {

		try {
			wholescriptsMedPaxFacade.removeEntryFromMedpax(medPaxEntryNumber, medPaxId);
			return ResponseEntity.ok("Medpax entry removed successfully.");
		}
		 catch (Exception e) {
			throw new RequestParameterException("Unexpected error occurred: " + e.getMessage(),
					RequestParameterException.INVALID, e);
		}
	}
	
	@Secured(
	{ WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,
			WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@DeleteMapping(value = "/medPax/delete")
	@Operation(operationId = "deleteMedPax", summary = "deleting a MedPax", description = "deleting a MedPax")
	@ApiBaseSiteIdAndUserIdParam
	@ApiResponse(responseCode = "200", description = "Medpax removed successfully.")
	public ResponseEntity<String> deleteMedPax(
			@Parameter(description = "Medpax Id For delete whole medpax")
			@RequestParam(required = true)
			final String medpaxId)
	{
		try {
			wholescriptsMedPaxFacade.removeMedpax(medpaxId);
			return ResponseEntity.ok("Medpax removed successfully.");
		}
		 catch (Exception e) {
			throw new RequestParameterException("Unexpected error occurred: " + e.getMessage(),
					RequestParameterException.INVALID, e);
		}
	}
	
	@Secured(
			{ WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,
				WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/getPrivateLabelCartons", method = RequestMethod.GET)
	@ResponseBody
	@Operation(operationId = "getCartonsForPrivateLabelUser", summary = "It will return the details of cartons")
	@ApiBaseSiteIdAndUserIdParam
	@ApiResponse(responseCode = "200", description = "MedpaxCartonListWsDTO", content = @Content(schema = @Schema(implementation = MedpaxCartonListWsDTO.class)))
	public MedpaxCartonListWsDTO getPrivateLabelCartons()
	{
		try
		{
			MedpaxCartonListWsDTO cartonList = new MedpaxCartonListWsDTO();
			List<MedpaxCartonWsDTO> cartonListDto = new ArrayList<>();
			List<MedpaxCartonData> cartonDataList = wholescriptsMedPaxFacade.getCartonsData();
			cartonDataList.forEach( c -> { cartonListDto.add(dataMapper.map(c, MedpaxCartonWsDTO.class));});
			cartonList.setMedpaxCartonList(cartonListDto);
			return cartonList;
		}
		catch (final Exception e)
		{
			throw new RequestParameterException(String.format(sanitize(e.getMessage())),
					RequestParameterException.INVALID);
		}
	}
	
	@Secured(
			{ WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,
				WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/setPrivateLabelCarton", method = RequestMethod.POST)
	@ResponseBody
	@Operation(operationId = "setPrivateLabelCarton", summary = "It will update the custom carton in medpax")
	@ApiBaseSiteIdAndUserIdParam
	@ApiResponse(responseCode = "200")
	public void setPrivateLabelCarton(@Parameter(description = "Product Code")
	@RequestParam(required = true)
	final String productCode,@Parameter(description = "Carton Name")
	@RequestParam(required = true)
	final String cartonName,@Parameter(description = "Medpax Id For Update carton")
	@RequestParam(required = true)
	final String medpaxId)
	{
		try
		{
			 wholescriptsMedPaxFacade.updateMedpaxForCarton(productCode, medpaxId, cartonName);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new RequestParameterException(String.format(CARTON_NOT_FOUND, sanitize(productCode)),
					RequestParameterException.MISSING);
		}
	}
	
	@Secured(
			{ WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,
				WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/getPrivateLabelWebKit", method = RequestMethod.GET)
	@ResponseBody
	@Operation(operationId = "getPrivateLabelWebKit", summary = "It will return the details of private label web kit")
	@ApiBaseSiteIdAndUserIdParam
	@ApiResponse(responseCode = "200", description = "MedpaxWebKitListWsDTO", content = @Content(schema = @Schema(implementation = MedpaxWebKitListWsDTO.class)))
	public MedpaxWebKitListWsDTO getPrivateLabelWebKit()
	{
		try
		{
			MedpaxWebKitListWsDTO kitList = new MedpaxWebKitListWsDTO();
			List<MedpaxWebKitWsDTO> kitListDto = new ArrayList<>();
			List<MedpaxWebKitData> kitDataList = wholescriptsMedPaxFacade.getPrivateLabelWebKits();
			kitDataList.forEach( c -> { kitListDto.add(dataMapper.map(c, MedpaxWebKitWsDTO.class));});
			kitList.setMedpaxKitList(kitListDto);
			return kitList;
		}
		catch (final Exception e)
		{
			throw new RequestParameterException(String.format(sanitize(e.getMessage())),
					RequestParameterException.INVALID);
		}
	}

	@Secured(
			{ WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,
					WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@RequestMapping(value = "/getOrderedMedpaxProductsForUser", method = RequestMethod.GET)
	@ResponseBody
	@Operation(operationId = "getOrderedMedpaxProductsForUser", summary = "return Ordered Medpax Products")
	@ApiBaseSiteIdAndUserIdParam
	@ApiResponse(responseCode = "200", description = "OrderedMedpaxProductsWsDTO", content = @Content(schema = @Schema(implementation = OrderedMedpaxProductsWsDTO.class)))
	public OrderedMedpaxProductsWsDTO getOrderedMedpaxProductsForUser(
			@Parameter(description = "logged in user uid", required = true) @PathVariable("userId") final String uID,
			@RequestParam(required = false) final String productSearch,
			@ApiFieldsParam @RequestParam(defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields) {
		try {
				OrderedMedpaxProductsData orderedMedpaxProductsData = wholescriptsMedPaxFacade.getOrderedMedpaxProductsForUser(uID ,productSearch);

			return dataMapper.map(orderedMedpaxProductsData, OrderedMedpaxProductsWsDTO.class, fields);
		} catch (final Exception e) {
			throw new RequestParameterException(String.format(sanitize(e.getMessage())), RequestParameterException.INVALID);
		}
	}
}



