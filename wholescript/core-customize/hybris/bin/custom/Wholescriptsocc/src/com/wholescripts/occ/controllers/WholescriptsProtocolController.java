package com.wholescripts.occ.controllers;


import de.hybris.platform.b2bocc.v2.controllers.BaseController;
import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.product.data.StockData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.commercewebservices.core.stock.CommerceStockFacade;
import de.hybris.platform.commercewebservicescommons.dto.order.CartModificationWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.CartEntryException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.LowStockException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.ProductLowStockException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.StockSystemException;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.webservicescommons.dto.PaginationWsDTO;
import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;
import de.hybris.platform.webservicescommons.pagination.WebPaginationUtils;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;
import de.hybris.platform.webservicescommons.util.YSanitizer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.wholescripts.core.exception.WholescriptsPageNotFoundException;
import com.wholescripts.core.facade.WholescriptsProtocolFacade;
import com.wholescripts.core.facade.WholescriptsRecommendFacade;
import com.wholescripts.core.model.ProtocolModel;
import com.wholescripts.data.CommerceProtocolParameter;
import com.wholescripts.data.protocol.ProtocolData;
import com.wholescripts.data.protocol.ProtocolEntryData;
import com.wholescripts.dto.WsRecommendationData;
import com.wholescripts.dto.protocol.ProtocolEntryWsDto;
import com.wholescripts.dto.protocol.ProtocolListWsDTO;
import com.wholescripts.dto.protocol.ProtocolWsDTO;
import com.wholescripts.occ.constants.WholescriptsSecuredAccessConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;


@Controller
@ApiVersion("v2")
@RequestMapping(value = "/{baseSiteId}/users/{userId}")
@Tag(name = "Protocols")
public class WholescriptsProtocolController extends BaseController
{
	private static final String OBJECT_NAME_PROTOCOL_ID = "protocolId";
	private static final String PROTOCOL_DOES_NOT_EXIST = "Protocol with given id: '%s' doesn't exist or belong to another user";
	private static final Logger LOG = LogManager.getLogger(WholescriptsProtocolController.class);
	public static final String DEFAULT_CURRENT_PAGE = "0";
	public static final String DEFAULT_PAGE_SIZE = "10";
	protected static final String MODEL_SAVING_ERROR_MESSAGE = "Model Saving error";
	protected static final String DEFAULT_FIELD_SET = FieldSetLevelHelper.DEFAULT_LEVEL;
	private static final long DEFAULT_PRODUCT_QUANTITY = 1;
	protected static final String ENTRY = "entry";
	private static final String PRODUCT = "Product";

	@Resource(name = "dataMapper")
	private DataMapper dataMapper;
	@Resource(name = "webPaginationUtils")
	private WebPaginationUtils webPaginationUtils;
	@Resource(name = "wholescriptsProtocolFacade")
	private WholescriptsProtocolFacade wholescriptsProtocolFacade;
	@Resource(name = "orderEntryCreateValidator")
	private Validator orderEntryCreateValidator;
	@Resource(name = "commerceStockFacade")
	protected CommerceStockFacade commerceStockFacade;
	@Resource(name = "orderEntryUpdateValidator")
	private Validator orderEntryUpdateValidator;
	@Resource(name = "wholescriptsRecommendFacade")
	private WholescriptsRecommendFacade wholescriptsRecommendFacade;

	@Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
	@RequestMapping(value = "/protocols", method = RequestMethod.GET)
	@ResponseBody
	@Operation(operationId = "getProtocols", summary = "it will return the paginated list of protocols for Practitioner")
	@ApiBaseSiteIdAndUserIdParam
	@ApiResponse(responseCode = "200", description = "ProtocolListWsDTO", content = @Content(schema = @Schema(implementation = ProtocolListWsDTO.class)))
	public ProtocolListWsDTO getMyProtocolList(
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
		{ "BASIC", "DEFAULT", "FULL" }))
			@RequestParam(defaultValue = "DEFAULT")
			final String fields, @Parameter(description = "Current result page. Default value is 0.")
			@RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE)
			final int currentPage, @Parameter(description = "Number of results returned per page. Default value: 10.")
			@RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE)
			final int pageSize, @Parameter(description = "Sorting method applied to the return results.", example = "uid:asc")
			@RequestParam(required = false, defaultValue = "date:desc")
			final String sort, @RequestParam(required = false)
			final String protocolName, @RequestParam(required = false, defaultValue = "false")
			final boolean isWSPage, @RequestParam(required = false, defaultValue = "false") final boolean isNonDraft)
	{
		// Creates a SearchPageData object contains requested pagination and sorting information
		final SearchPageData<ProtocolData> searchPageDataInput = webPaginationUtils.buildSearchPageData(sort, currentPage, pageSize,
				true);
		// Get search result in a SearchPageData object contains search results with applied pagination and sorting information
		final SearchPageData<ProtocolData> protocolsListData = wholescriptsProtocolFacade.getProtocolListData(searchPageDataInput, protocolName, isWSPage,isNonDraft);
		return formatSearchPageDataResult(fields, protocolsListData);
	}

	@Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
	@RequestMapping(value = "/protocols/{protocolId}", method = RequestMethod.GET)
	@ResponseBody
	@Operation(operationId = "getMyProtocolDetails", summary = "It will return the details of protocol based on protocol id")
	@ApiBaseSiteIdAndUserIdParam
	@ApiResponse(responseCode = "200", description = "ProtocolWsDTO", content = @Content(schema = @Schema(implementation = ProtocolWsDTO.class)))
	public ProtocolWsDTO getMyProtocolDetails(
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
		{ "BASIC", "DEFAULT", "FULL" }))
			@RequestParam(defaultValue = "DEFAULT")
			final String fields, @PathVariable(required = true)
			final String protocolId, @RequestParam(required = false, defaultValue = "false")
			final boolean isWSPage) throws WholescriptsPageNotFoundException
	{
		try
		{
			final Optional<ProtocolData> protocolData = wholescriptsProtocolFacade.getProtocolDetailsData(protocolId,isWSPage);
			return dataMapper.map(protocolData.get(), ProtocolWsDTO.class, fields);
		}
		catch (final NoSuchElementException e)
		{
			throw new WholescriptsPageNotFoundException(String.format(PROTOCOL_DOES_NOT_EXIST, sanitize(protocolId)));
		}
		catch (final Exception e)
		{
			throw new RequestParameterException(String.format(PROTOCOL_DOES_NOT_EXIST, sanitize(protocolId)),
					RequestParameterException.MISSING, OBJECT_NAME_PROTOCOL_ID);
		}
	}

	protected ProtocolListWsDTO formatSearchPageDataResult(final String fields,
			final SearchPageData<ProtocolData> searchPageDataResult)
	{
		// Map the results into a ProtocolListWsDTO which is an intermediate WsDTO
		final ProtocolListWsDTO listData = new ProtocolListWsDTO();
		final List<ProtocolWsDTO> protocolWsDTOList = searchPageDataResult.getResults().stream()
				.map(protocolData -> dataMapper.map(protocolData, ProtocolWsDTO.class, fields)).toList();
		listData.setProtocolsListWsDto(protocolWsDTOList);
		// Convert pagination and sorting data into PaginationWsDTO'
		final PaginationWsDTO paginationWsDTO = webPaginationUtils.buildPaginationWsDto(searchPageDataResult.getPagination());
		listData.setPagination(paginationWsDTO);
		return listData;
	}

	protected static String sanitize(final String input)
	{
		return YSanitizer.sanitize(input);
	}


	@Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
	@RequestMapping(value = "/protocols/{protocolId}/duplicate", method = RequestMethod.POST)
	@ResponseBody
	@Operation(operationId = "addDuplicateProtocol", summary = "It will duplicate a new protocol")
	@ApiBaseSiteIdAndUserIdParam
	public ProtocolWsDTO addDuplicateProtocol(
			@Parameter(description = "Request body parameter that contains details such as the protocol name, description.", required = true)
			@RequestBody
			final ProtocolWsDTO protocol,
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
		{ "BASIC", "DEFAULT", "FULL" }))
			@RequestParam(defaultValue = "DEFAULT")
			final String fields, @PathVariable
			final String protocolId, @RequestParam(required = false, defaultValue = "false")
			final boolean isWSPage) throws CommerceCartModificationException
	{
		try
		{
			final CommerceProtocolParameter param = convertCommerceProtocolParam(protocol, protocolId);
			param.setIsWSPage(isWSPage);
			final Optional<ProtocolData> protocolData = wholescriptsProtocolFacade.addDuplicateProtocol(param);
			return dataMapper.map(protocolData.get(), ProtocolWsDTO.class, fields);
		}
		catch (final ModelSavingException e)
		{
			throw new RequestParameterException(String.format(e.getMessage(), sanitize(protocolId)),
					RequestParameterException.INVALID, protocolId);
		}
	}

	@Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
	@RequestMapping(value = "/protocols", method = RequestMethod.POST)
	@ResponseBody
	@Operation(operationId = "addNewProtocol", summary = "It will create a new protocol")
	@ApiBaseSiteIdAndUserIdParam
	public ProtocolWsDTO addNewProtocol(
			@Parameter(description = "Request body parameter that contains details such as the protocol name, description.", required = true)
			@RequestBody
			final ProtocolWsDTO protocol,
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
		{ "BASIC", "DEFAULT", "FULL" }))
			@RequestParam(defaultValue = "DEFAULT")
			final String fields)
	{
		try
		{
			final CommerceProtocolParameter param = convertCommerceProtocolParam(protocol, StringUtils.EMPTY);
			final Optional<ProtocolData> protocolData = wholescriptsProtocolFacade.addNewProtocol(param);
			return dataMapper.map(protocolData.get(), ProtocolWsDTO.class, fields);
		}
		catch (final ModelSavingException e)
		{
			throw new RequestParameterException(String.format(e.getMessage(), sanitize(protocol.getName())), RequestParameterException.INVALID,
					protocol.getName());
		}
	}

	@Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
	@RequestMapping(value = "/protocols/{protocolId}", method = RequestMethod.PATCH)
	@ResponseBody
	@Operation(operationId = "saveProtocol", summary = "It will Update a protocol")
	@ApiBaseSiteIdAndUserIdParam
	public ProtocolWsDTO saveProtocol(
			@Parameter(description = "Request body parameter that contains details such as the protocol name, description.", required = true)
			@RequestBody
			final ProtocolWsDTO protocol,
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
		{ "BASIC", "DEFAULT", "FULL" }))
			@RequestParam(defaultValue = "DEFAULT")
			final String fields, @PathVariable(required = true)
			final String protocolId)
	{
		try
		{
			final CommerceProtocolParameter param = convertCommerceProtocolParam(protocol, protocolId);
			final Optional<ProtocolData> protocolData = wholescriptsProtocolFacade.saveProtocol(param);
			return dataMapper.map(protocolData.get(), ProtocolWsDTO.class, fields);
		}
		catch (final ModelSavingException e)
		{
			throw new RequestParameterException(String.format(e.getMessage(), sanitize(protocolId)), RequestParameterException.INVALID,
					protocolId);
		}
	}

	@Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
	@RequestMapping(value = "/protocols/share/{protocolId}", method = RequestMethod.POST)
	@ResponseBody
	@Operation(operationId = "shareProtocol", summary = "It will share protocol with patients")
	@ApiBaseSiteIdAndUserIdParam
	public ProtocolWsDTO shareProtocol(
			@Parameter(description = "Request body parameter that contains details such as the protocol name, description.", required = true)
			@RequestBody
			final ProtocolWsDTO protocol,
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
		{ "BASIC", "DEFAULT", "FULL" }))
			@RequestParam(defaultValue = "DEFAULT")
			final String fields, @PathVariable(required = true)
			final String protocolId)
	{
		try
		{
			final CommerceProtocolParameter param = convertCommerceProtocolParam(protocol, protocolId);
			final Optional<ProtocolData> protocolData = wholescriptsProtocolFacade.shareProtocol(param);
			return dataMapper.map(protocolData.get(), ProtocolWsDTO.class, fields);
		}
		catch (final ModelSavingException e)
		{
			throw new RequestParameterException(String.format(e.getMessage(), sanitize(protocolId)), RequestParameterException.INVALID,
					protocolId);
		}
	}

	@Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
	@RequestMapping(value = "/prReference/clone", method = RequestMethod.POST)
	@ResponseBody
	@Operation(operationId = "clonePRReference", summary = "It will clone protocol to Recommendation and vice versa")
	@ApiBaseSiteIdAndUserIdParam
	public Map<String, Object> clone(@RequestParam(required = false)
	final String recommendationId, @RequestParam(required = false)
	final String protocolId, @RequestParam(required = false)
	final String patientId, @RequestParam(required = false)
	final String name, @RequestParam(required = false, defaultValue = "false")
	final boolean isWSPage) throws CommerceCartModificationException
	{
		final Map<String, Object> map = new HashMap<>();
		if (StringUtils.isNotEmpty(protocolId) && StringUtils.isNotEmpty(patientId))
		{
		try
			{
				final Optional<WsRecommendationData> wsData = wholescriptsRecommendFacade.clone(protocolId, patientId,isWSPage);
				map.put("Key", "recommendationId");
				map.put("Text", wsData.get().getId());
				return map;
		}
		catch (final ModelSavingException e)
		{
			throw new RequestParameterException(String.format(e.getMessage(), sanitize(protocolId)), RequestParameterException.INVALID,
					protocolId);
		}
	}
	else if (StringUtils.isNotEmpty(recommendationId) && StringUtils.isNotEmpty(name))
	{
		try
		{
			final Optional<ProtocolData> protocolData = wholescriptsProtocolFacade.clone(recommendationId, name);
			map.put("Key", "protocolId");
			map.put("Text", protocolData.get().getId());
			return map;
		}
		catch (final ModelSavingException e)
		{
			throw new RequestParameterException(String.format(e.getMessage(), sanitize(protocolId)),
					RequestParameterException.INVALID, recommendationId);
		}
	}
	return map;


	}

	/**
	 * @param protocol
	 * @param protocolId
	 * @return
	 */
	private CommerceProtocolParameter convertCommerceProtocolParam(final ProtocolWsDTO protocol, final String protocolId)
	{
		final CommerceProtocolParameter param = new CommerceProtocolParameter();
		param.setName(protocol.getName());
		param.setDescription(protocol.getDescription());
		param.setNotes(protocol.getNotes());
		param.setProtocolId(protocolId);
		param.setPatients(protocol.getPatients());
		param.setSharedWithAll(protocol.isSharedWithAll());
		param.setTermsAgreed(protocol.getTermsAgreed());
		param.setProtocolStatus(protocol.getProtocolStatus());
		return param;
	}

	@Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
	@PostMapping(value = "/protocols/{protocolId}/entries", consumes ={ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@Operation(operationId = "createProtocolEntry", summary = "creating a protocol entry along with dosing and item time", description = "creating a protocol entry along with dosing and item time")
	@ApiBaseSiteIdAndUserIdParam
	public CartModificationWsDTO createProtocolEntry(@PathVariable
			final String baseSiteId,
			@Parameter(description = "Request body parameter that contains details such as the product code (product.code), the quantity of product (quantity),"
					+ "\"product\": {\n \"code\": \"1997551\"\n},\n\"qty\": \"1\"\n}"
					+ "\nRequest body are the same for all storefronts." + "\n\nThe DTO is in XML or .json format.", required = true)
	@RequestBody
	final ProtocolEntryWsDto entry,
	@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
{ "BASIC", "DEFAULT", "FULL" }))
	@RequestParam(defaultValue = "DEFAULT")
	final String fields, @PathVariable
	final String protocolId) throws CommerceCartModificationException
	{
		if (entry.getQuantity() == null)
		{
			entry.setQuantity(DEFAULT_PRODUCT_QUANTITY);
		}
		validate(entry, ENTRY, orderEntryCreateValidator);
		final String pickupStore = entry.getDeliveryPointOfService() == null ? null : entry.getDeliveryPointOfService().getName();
		return addProtocolEntryInternal(baseSiteId, protocolId, entry.getProduct().getCode(), entry.getQuantity(), pickupStore,
				fields, entry);
	}

	@Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
	@PatchMapping(value = "/protocols/{protocolId}/{entryNumber}", consumes ={ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@Operation(operationId = "updateProtocolEntry", summary = "Updates the details of a protocol entry.", description = "Updates the quantity of a single protocol entry and the details of the pickup store.")
	@ApiBaseSiteIdAndUserIdParam
	public CartModificationWsDTO updateProtocolEntry(@PathVariable
			final String baseSiteId,
			@Parameter(description = "Each entry in a protocol has an entry number. Protocol entries are numbered in ascending order, starting with zero.", required = true)
	@PathVariable
	final long entryNumber,
	@Parameter(description = "Request body parameter that contains details such as the quantity of product (quantity), and the pickup store name (deliveryPointOfService.name)\n\nThe DTO is in XML or .json format.", required = true)
	@RequestBody
	final ProtocolEntryWsDto entry, @ApiFieldsParam
	@RequestParam(defaultValue = DEFAULT_FIELD_SET)
	final String fields, @PathVariable
	final String protocolId) throws CommerceCartModificationException
	{
		final Optional<ProtocolData> protocolData = wholescriptsProtocolFacade.getProtocolDetailsData(protocolId,false);
		final ProtocolEntryData protocolEntry = getProtocolEntryForNumber(protocolData.get(), entryNumber);
		validateProductCode(protocolEntry, entry);
		if (entry.getQuantity() == null)
		{
			entry.setQuantity(protocolEntry.getQuantity());
		}
		validate(entry, ENTRY, orderEntryUpdateValidator);
		final String pickupStore = entry.getDeliveryPointOfService() == null ? null : entry.getDeliveryPointOfService().getName();
		return updateProtocolEntryInternal(baseSiteId, protocolData.get(), protocolEntry, entry.getQuantity(), pickupStore, fields,
				false, protocolId, entry);
	}

	@Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
	@RequestMapping(value = "/protocols/{protocolId}/delete", method = RequestMethod.DELETE)
	@ResponseBody
	@Operation(operationId = "deleteProtocol", summary = "It will update the protocol status as deleted.")
	@ApiBaseSiteIdAndUserIdParam
	public ProtocolWsDTO deleteProtocol(
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
		{ "BASIC", "DEFAULT", "FULL" }))
			@RequestParam(defaultValue = "DEFAULT")
			final String fields, @PathVariable
			final String protocolId)
	{
		try
		{
			final Optional<ProtocolData> protocolData = wholescriptsProtocolFacade.deleteProtocol(protocolId);
			return dataMapper.map(protocolData.get(), ProtocolWsDTO.class, fields);
		}
		catch (final ModelSavingException e)
		{
			throw new RequestParameterException(String.format(e.getMessage(), sanitize(protocolId)), RequestParameterException.INVALID,
					protocolId);
		}
	}

	@Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
	@RequestMapping(value = "/patient/protocols", method = RequestMethod.GET)
	@ResponseBody
	@Operation(operationId = "getPatientProtocols", summary = "it will return the paginated list of protocols for Patient")
	@ApiBaseSiteIdAndUserIdParam
	@ApiResponse(responseCode = "200", description = "ProtocolListWsDTO", content = @Content(schema = @Schema(implementation = ProtocolListWsDTO.class)))
	public ProtocolListWsDTO getPatientProtocols(
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
		{ "BASIC", "DEFAULT", "FULL" }))
			@RequestParam(defaultValue = "DEFAULT")
			final String fields, @Parameter(description = "Current result page. Default value is 0.")
			@RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE)
			final int currentPage, @Parameter(description = "Number of results returned per page. Default value: 10.")
			@RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE)
			final int pageSize, @Parameter(description = "Sorting method applied to the return results.", example = "uid:asc")
			@RequestParam(required = false, defaultValue = "modifiedtime:desc")
			final String sort)
	{
		// Creates a SearchPageData object contains requested pagination and sorting information
		final SearchPageData<ProtocolData> searchPageDataInput = webPaginationUtils.buildSearchPageData(sort, currentPage, pageSize,
				true);
		// Get search result in a SearchPageData object contains search results with applied pagination and sorting information
		final SearchPageData<ProtocolData> protocolsListData = wholescriptsProtocolFacade.getPatientProtocolListData(searchPageDataInput);
		return formatSearchPageDataResult(fields, protocolsListData);
	}

	protected static void validateProductCode(final ProtocolEntryData protocolEntry, final ProtocolEntryWsDto entry)
	{
		final String productCode = protocolEntry.getProduct().getCode();
		final Errors errors = new BeanPropertyBindingResult(entry, ENTRY);
		if (entry.getProduct() != null && entry.getProduct().getCode() != null && !entry.getProduct().getCode().equals(productCode))
		{
			errors.reject("cartEntry.productCodeNotMatch");
			throw new WebserviceValidationException(errors);
		}
	}

	protected CartModificationWsDTO updateProtocolEntryInternal(final String baseSiteId, final ProtocolData protocol,
			final ProtocolEntryData protocolEntry, final Long qty, final String pickupStore, final String fields,
			final boolean putMode, final String protocolId,final ProtocolEntryWsDto entry)
					throws CommerceCartModificationException
	{
		final long entryNumber = protocolEntry.getEntryNumber().longValue();
		final String productCode = protocolEntry.getProduct().getCode();
		final PointOfServiceData currentPointOfService = protocolEntry.getDeliveryPointOfService();
		CartModificationData cartModificationData1 = null;
		CartModificationData cartModificationData2 = null;
		if (!StringUtils.isEmpty(pickupStore))
		{
			if (currentPointOfService == null || !currentPointOfService.getName().equals(pickupStore))
			{
				//was 'shipping mode' or store is changed
				validateForAmbiguousPositions(protocol, protocolEntry, pickupStore);
				cartModificationData1 = wholescriptsProtocolFacade.updateProtocolEntry(entryNumber, pickupStore);
			}
		}
		else if (putMode && currentPointOfService != null)
		{
			//was 'pickup in store', now switch to 'shipping mode'
			validateForAmbiguousPositions(protocol, protocolEntry, pickupStore);
			cartModificationData1 = wholescriptsProtocolFacade.updateProtocolEntry(entryNumber, pickupStore);
		}

		if (qty != null)
		{
			cartModificationData2 = wholescriptsProtocolFacade.updateProtocolEntry(entryNumber, qty, protocolId, entry);
		}
		return dataMapper.map(mergeCartModificationData(cartModificationData1, cartModificationData2), CartModificationWsDTO.class,
				fields);
	}

	protected static void validateForAmbiguousPositions(final ProtocolData protocol, final ProtocolEntryData currentEntry,
			final String newPickupStore)
	{
		final ProtocolEntryData entryToBeModified = getCartEntry(protocol, currentEntry.getProduct().getCode(), newPickupStore);
		if (entryToBeModified != null && !entryToBeModified.getEntryNumber().equals(currentEntry.getEntryNumber()))
		{
			throw new CartEntryException(
					"Ambiguous cart entries! Entry number " + currentEntry.getEntryNumber()
					+ " after change would be the same as entry " + entryToBeModified.getEntryNumber(),
					CartEntryException.AMBIGIOUS_ENTRY, entryToBeModified.getEntryNumber().toString());
		}
	}

	protected static CartModificationData mergeCartModificationData(final CartModificationData cmd1,
			final CartModificationData cmd2)
	{
		if ((cmd1 == null) && (cmd2 == null))
		{
			return new CartModificationData();
		}
		if (cmd1 == null)
		{
			return cmd2;
		}
		if (cmd2 == null)
		{
			return cmd1;
		}
		final CartModificationData cmd = new CartModificationData();
		cmd.setDeliveryModeChanged(
				Boolean.TRUE.equals(cmd1.getDeliveryModeChanged()) || Boolean.TRUE.equals(cmd2.getDeliveryModeChanged()));
		cmd.setEntry(cmd2.getEntry());
		cmd.setQuantity(cmd2.getQuantity());
		cmd.setQuantityAdded(cmd1.getQuantityAdded() + cmd2.getQuantityAdded());
		cmd.setStatusCode(cmd2.getStatusCode());
		return cmd;
	}

	protected static ProtocolEntryData getCartEntry(final ProtocolData protocol, final String productCode,
			final String pickupStore)
	{
		final Predicate<ProtocolEntryData> productsEqualFilter = protocolEntryData -> protocolEntryData != null
				&& protocolEntryData.getProduct() != null && protocolEntryData.getProduct().getCode() != null
				&& protocolEntryData.getProduct().getCode().equals(productCode);
		final Predicate<ProtocolEntryData> noStoresFilter = protocolEntryData -> pickupStore == null
				&& protocolEntryData.getDeliveryPointOfService() == null;
		final Predicate<ProtocolEntryData> storesEqualFilter = protocolEntryData -> pickupStore != null
				&& protocolEntryData.getDeliveryPointOfService() != null
				&& pickupStore.equals(protocolEntryData.getDeliveryPointOfService().getName());
		return protocol.getProtocolEntries().stream().filter(productsEqualFilter.and(noStoresFilter.or(storesEqualFilter)))
				.findFirst().orElse(null);
	}

	protected static ProtocolEntryData getProtocolEntryForNumber(final ProtocolData protocol, final long number)
	{
		final Integer requestedEntryNumber = (int) number;
		return CollectionUtils.emptyIfNull(protocol.getProtocolEntries()).stream()
				.filter(entry -> entry != null && requestedEntryNumber.equals(entry.getEntryNumber())).findFirst()
				.orElseThrow(() -> new CartEntryException("Entry not found", CartEntryException.NOT_FOUND, String.valueOf(number)));
	}

	protected CartModificationWsDTO addProtocolEntryInternal(final String baseSiteId, final String protocolId,
			final String productCode, final long qty, final String pickupStore, final String fields, final ProtocolEntryWsDto entry) throws CommerceCartModificationException
	{
		final CartModificationData cartModificationData;
		if (StringUtils.isNotEmpty(pickupStore))
		{
			//validateIfProductIsInStockInPOS(baseSiteId, productCode, pickupStore, null);
			cartModificationData = wholescriptsProtocolFacade.createProtocolEntryForStore(productCode, qty, protocolId, entry,
					pickupStore);
		}
		else
		{
			//validateIfProductIsInStockOnline(baseSiteId, productCode, null);
			cartModificationData = wholescriptsProtocolFacade.createProtocolEntry(productCode, qty, protocolId, entry);
		}
		return dataMapper.map(cartModificationData, CartModificationWsDTO.class, fields);
	}

	protected void validateIfProductIsInStockInPOS(final String baseSiteId, final String productCode, final String storeName,
			final Long entryNumber)
	{
		if (!commerceStockFacade.isStockSystemEnabled(baseSiteId))
		{
			throw new StockSystemException("Stock system is not enabled on this site", StockSystemException.NOT_ENABLED, baseSiteId);
		}
		final StockData stock = commerceStockFacade.getStockDataForProductAndPointOfService(productCode, storeName);
		if (stock != null && stock.getStockLevelStatus().equals(StockLevelStatus.OUTOFSTOCK))
		{
			if (entryNumber != null)
			{
				throw new LowStockException(PRODUCT + "[" + sanitize(productCode) + "] is currently out of stock",
						LowStockException.NO_STOCK, String.valueOf(entryNumber));
			}
			else
			{
				throw new ProductLowStockException(PRODUCT + " [" + sanitize(productCode) + "] is currently out of stock",
						LowStockException.NO_STOCK, productCode);
			}
		}
		else if (stock != null && stock.getStockLevelStatus().equals(StockLevelStatus.LOWSTOCK))
		{
			if (entryNumber != null)
			{
				throw new LowStockException("Not enough product in stock", LowStockException.LOW_STOCK, String.valueOf(entryNumber));
			}
			else
			{
				throw new ProductLowStockException("Not enough product in stock", LowStockException.LOW_STOCK, productCode);
			}
		}
	}

	protected void validateIfProductIsInStockOnline(final String baseSiteId, final String productCode, final Long entryNumber)
	{
		if (!commerceStockFacade.isStockSystemEnabled(baseSiteId))
		{
			throw new StockSystemException("Stock system is not enabled on this site", StockSystemException.NOT_ENABLED, baseSiteId);
		}
		final StockData stock = commerceStockFacade.getStockDataForProductAndBaseSite(productCode, baseSiteId);
		if (stock != null && stock.getStockLevelStatus().equals(StockLevelStatus.OUTOFSTOCK))
		{
			if (entryNumber != null)
			{
				throw new LowStockException("Product [" + sanitize(productCode) + "] cannot be shipped - out of stock online",
						LowStockException.NO_STOCK, String.valueOf(entryNumber));
			}
			else
			{
				throw new ProductLowStockException("Product [" + sanitize(productCode) + "] cannot be shipped - out of stock online",
						LowStockException.NO_STOCK, productCode);
			}
		}
	}

	@Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
	@RequestMapping(value = "/protocols/{protocolId}/stop-sharing", method = RequestMethod.PATCH)
	@ResponseBody
	@Operation(operationId = "stopSharingProtocol", summary = "Stops sharing a protocol with patients for a specific user.")
	@ApiBaseSiteIdAndUserIdParam
	public ProtocolWsDTO stopSharingProtocol(
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
					{ "BASIC", "DEFAULT", "FULL" }))
			@RequestParam(defaultValue = "DEFAULT")
			final String fields,@PathVariable(required = true)
			final String protocolId)
	{
		try
		{
			final Optional<ProtocolModel> protocolData = wholescriptsProtocolFacade.stopSharingProtocol(protocolId);
			return dataMapper.map(protocolData.get(), ProtocolWsDTO.class, fields);
		}
		catch (final ModelSavingException e)
		{
			throw new RequestParameterException(String.format(e.getMessage(), sanitize(protocolId)), RequestParameterException.INVALID,
					protocolId);
		}
	}

	@Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
	@DeleteMapping(value = "/protocols/{protocolId}/entries/{entryNumber}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@Operation(operationId = "deleteProtoocolEntry", summary = "deletes a Protocol entry.", description = "deletes the Protocol entry.")
	@ApiBaseSiteIdAndUserIdParam
	public void deleteProtocolEntry(@PathVariable
	final String protocolId,@Parameter(description = "Each entry in a protoocl has an entry number. Protocol entries are numbered in ascending order, starting with zero.", required = true)
			@PathVariable final long entryNumber) throws CommerceCartModificationException
	{
		try
		{
			wholescriptsProtocolFacade.deleteProtocolEntry(entryNumber, 0, protocolId);
		}
		catch (final CommerceCartModificationException e)
		{
			throw new RequestParameterException(String.format(e.getMessage(), sanitize(protocolId)),
					RequestParameterException.INVALID, protocolId);
		}
	}

	public void validate(final Object object, final String objectName, final Validator validator)
	{
		final Errors errors = new BeanPropertyBindingResult(object, objectName);
		validator.validate(object, errors);
		if (errors.hasErrors())
		{
			throw new WebserviceValidationException(errors);
		}
	}
	
	 @ResponseStatus(value = HttpStatus.BAD_REQUEST)
	 @ResponseBody
	 @ExceptionHandler({ WholescriptsPageNotFoundException.class })
	 public ErrorListWsDTO handleModelNotFoundnException(final WholescriptsPageNotFoundException ex)
	 {
		 return handleErrorInternal("WholescriptsPageNotFoundException", ex.getMessage());
	 }
}
