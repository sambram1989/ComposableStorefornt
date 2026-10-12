/**
 *
 */
package com.wholescripts.occ.controllers;

import de.hybris.platform.b2bocc.v2.controllers.BaseController;
import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.product.data.StockData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commercewebservices.core.stock.CommerceStockFacade;
import de.hybris.platform.commercewebservicescommons.dto.order.CartModificationWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.UserWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.CartEntryException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.LowStockException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.ProductLowStockException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.StockSystemException;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.webservicescommons.dto.PaginationWsDTO;
import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.pagination.WebPaginationUtils;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;
import de.hybris.platform.webservicescommons.util.YSanitizer;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.GetMapping;

import com.wholescripts.core.exception.WholescriptsPageNotFoundException;
import com.wholescripts.core.facade.WholescriptsRecommendFacade;
import com.wholescripts.core.helper.WholescriptsHelper;
import com.wholescripts.dto.RecommendationListWsDTO;
import com.wholescripts.dto.WholescriptsRecommendSearchParams;
import com.wholescripts.dto.WsRecommendationData;
import com.wholescripts.dto.WsRecommendationEntryData;
import com.wholescripts.dto.WsRecommendationEntryInputWsDTO;
import com.wholescripts.dto.WsRecommendationWsDTO;
import com.wholescripts.occ.constants.WholescriptsoccConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
/**
 * @author Aadesh.jhanwar
 *
 */
@Controller
@RequestMapping(value = "/{baseSiteId}/users/{userId}")
@Tag(name = "Recommendation")
public class WholescriptsRecommendationController extends BaseController
{
	private static final String PRODUCT = "Product";
	private static final String OBJECT_NAME_RECOMMENDATION_ID = "recommendationId";
	private static final String PROTOCOL_DOES_NOT_EXIST = "Recommendation with given id: '%s' doesn't exist or belong to another user";
	private static final Logger LOG = LogManager.getLogger(WholescriptsRecommendationController.class);
	public static final String DEFAULT_CURRENT_PAGE = "0";
	public static final String DEFAULT_PAGE_SIZE = "10";

	@Resource(name = "wholescriptsRecommendFacade")
	private WholescriptsRecommendFacade wholescriptsRecommendFacade;

	@Resource(name = "dataMapper")
	protected DataMapper dataMapper;

	@Resource(name = "wholescriptsHelper")
	protected WholescriptsHelper wholescriptsHelper;

	@Resource(name = "commerceStockFacade")
	protected CommerceStockFacade commerceStockFacade;

	@Resource(name = "orderEntryCreateValidator")
	private Validator orderEntryCreateValidator;

	@Resource(name = "orderEntryUpdateValidator")
	private Validator orderEntryUpdateValidator;

	@Resource(name = "webPaginationUtils")
	private WebPaginationUtils webPaginationUtils;

	private static final long DEFAULT_PRODUCT_QUANTITY = 1;
	protected static final String ENTRY = "entry";

	@Secured(
			{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/recommendation", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	@ApiBaseSiteIdParam
	public WsRecommendationWsDTO createRecommendation(
	 @Parameter(name = "inputWsDTO", description = "Request body parameter")
	@RequestBody
	final WsRecommendationWsDTO recommendInput, @RequestParam(defaultValue = WholescriptsoccConstants.DEFAULT_FIELD_SET)
	final String fields)

	{
		try
		{
		final WsRecommendationData recommendData = wholescriptsRecommendFacade.createRecommendation(
				recommendInput.getPatient());
		return dataMapper.map(recommendData, WsRecommendationWsDTO.class, fields);
	}
	catch (final ModelSavingException e)
	{
		throw new RequestParameterException(String.format(e.getMessage(), sanitize(recommendInput.getPatient())), RequestParameterException.INVALID,
				recommendInput.getPatient());
	}
	}

	@Secured(
			{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/recommendation/{recommendationId}/share", method = RequestMethod.PATCH)
	@ResponseBody
	@ApiBaseSiteIdParam
	public WsRecommendationWsDTO shareRecommendation(@Parameter(description = "Share a Recommendation.")
	@PathVariable
	final String userId, @PathVariable
	final String recommendationId, @RequestParam(defaultValue = WholescriptsoccConstants.DEFAULT_FIELD_SET)
	final String fields, @Parameter(name = "inputWsDTO", description = "Request body parameter")
	@RequestBody
	final WsRecommendationWsDTO recommendInput,@RequestParam(required = false) boolean isSmsNotificationEnabled,
													 @RequestParam(required = false) boolean isEmailNotificationEnabled)

	{
		try
		{
		final WsRecommendationData recommendData = wholescriptsRecommendFacade.shareRecommendation(userId,
				recommendInput.getPatient(),
				recommendationId, recommendInput.getNotes(), recommendInput.getTermsAgreed(),isSmsNotificationEnabled,isEmailNotificationEnabled);
		return dataMapper.map(recommendData, WsRecommendationWsDTO.class, fields);
	}
	catch (final ModelSavingException e)
	{
		throw new RequestParameterException(String.format(e.getMessage(), sanitize(userId)), RequestParameterException.INVALID,
				userId);
	}
	}


	@Secured(
			{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/recommendation/{recommendationId}/updateStatus", method = RequestMethod.PATCH)
	@ResponseBody
	@ApiBaseSiteIdParam
	public WsRecommendationWsDTO updateRecommendationStatus(@Parameter(description = "Update a Recommendation status.")
	@PathVariable
	final String userId, @PathVariable
	final String recommendationId, @RequestParam
	final String status, @RequestParam(defaultValue = WholescriptsoccConstants.DEFAULT_FIELD_SET)
	final String fields)

	{
		try
		{
			final WsRecommendationData recommendData = wholescriptsRecommendFacade.updateRecommendationStatus(userId,
					recommendationId, status);
		return dataMapper.map(recommendData, WsRecommendationWsDTO.class, fields);
	}
	catch (final ModelSavingException e)
	{
		throw new RequestParameterException(String.format(e.getMessage(), sanitize(userId)), RequestParameterException.INVALID,
				userId);
	}
	}


	@Secured(
			{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/recommendation", method = RequestMethod.GET)
	@ResponseBody
	@Operation(operationId = "getRecommendation", summary = "it will return the paginated list of recommendation for a login user")
	@ApiBaseSiteIdAndUserIdParam
	@ApiResponse(responseCode = "200", description = "RecommendationListWsDTO", content = @Content(schema = @Schema(implementation = RecommendationListWsDTO.class)))
	public RecommendationListWsDTO getMyRecommendationList(
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
			{ "BASIC", "DEFAULT", "FULL" }))
			@RequestParam(defaultValue = "DEFAULT")
			final String fields, @Parameter(description = "Current result page. Default value is 0.")
			@RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE)
			final int currentPage, @Parameter(description = "Number of results returned per page. Default value: 10.")
			@RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE)
			final int pageSize, @Parameter(description = "Sorting method applied to the return results.", example = "uid:asc")
			@RequestParam(required = false, defaultValue = "date:desc")
			final String sort, final WholescriptsRecommendSearchParams searchParam, @PathVariable
			final String userId)
	{
		try
		{
			// Creates a SearchPageData object contains requested pagination and sorting information
			final SearchPageData<WsRecommendationData> searchPageDataInput = webPaginationUtils.buildSearchPageData(sort,
					currentPage, pageSize, true);
			// Get search result in a SearchPageData object contains search results with applied pagination and sorting information
			final SearchPageData<WsRecommendationData> recommendationListData = wholescriptsRecommendFacade
					.getRecommendationListData(searchPageDataInput, userId, searchParam);
			return formatSearchPageDataResult(fields, recommendationListData);
		}
		catch (final ModelNotFoundException e)
		{
			throw new RequestParameterException(String.format(e.getMessage(), sanitize(userId)), RequestParameterException.INVALID,
					userId);
		}
	}

	@Secured(
			{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/newRecommendation", method = RequestMethod.GET)
	@ResponseBody
	@Operation(operationId = "getNewRecommendation", summary = "it will return the paginated list of new recommendation popup for a login user")
	@ApiBaseSiteIdAndUserIdParam
	@ApiResponse(responseCode = "200", description = "RecommendationListWsDTO", content = @Content(schema = @Schema(implementation = RecommendationListWsDTO.class)))
	public WsRecommendationWsDTO getNewRecommendationList(
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
			{ "BASIC", "DEFAULT", "FULL" }))
			@RequestParam(defaultValue = "DEFAULT")
			final String fields, @Parameter(description = "Current result page. Default value is 0.")
			@RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE)
			final int currentPage, @Parameter(description = "Number of results returned per page. Default value: 10.")
			@RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE)
			final int pageSize, @Parameter(description = "Sorting method applied to the return results.", example = "uid:asc")
			@RequestParam(required = false, defaultValue = "date:desc")
			final String sort, @PathVariable
			final String userId)
	{
		try
		{
		// Creates a SearchPageData object contains requested pagination and sorting information
		final SearchPageData<WsRecommendationData> searchPageDataInput = webPaginationUtils.buildSearchPageData(sort, currentPage,
				pageSize, true);
		// Get search result in a SearchPageData object contains search results with applied pagination and sorting information
		final WsRecommendationData recommendationData = wholescriptsRecommendFacade
				.getNewRecommendationListData(searchPageDataInput, userId);
		return dataMapper.map(recommendationData, WsRecommendationWsDTO.class, fields);
	}
	catch (final ModelNotFoundException e)
	{
		throw new RequestParameterException(String.format(e.getMessage(), sanitize(userId)), RequestParameterException.INVALID,
				userId);
	}
	}

	protected RecommendationListWsDTO formatSearchPageDataResult(final String fields,
			final SearchPageData<WsRecommendationData> searchPageDataResult)
	{
		// Map the results into a ProtocolListWsDTO which is an intermediate WsDTO
		final RecommendationListWsDTO listData = new RecommendationListWsDTO();
		final List<WsRecommendationWsDTO> recommendationWsDTOList = searchPageDataResult.getResults().stream()
				.map(recommendData -> dataMapper.map(recommendData, WsRecommendationWsDTO.class, fields)).toList();
		listData.setRecommendations(recommendationWsDTOList);

		// Convert pagination and sorting data into PaginationWsDTO'
		final PaginationWsDTO paginationWsDTO = webPaginationUtils.buildPaginationWsDto(searchPageDataResult.getPagination());
		listData.setPagination(paginationWsDTO);
		return listData;
	}

	@Secured(
			{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/recommendation/{recommendationId}", method = RequestMethod.GET)
	@ResponseBody
	@Operation(operationId = "getMyRecommendationlDetails", summary = "It will return the details of recommendations based on recommendation id")
	@ApiBaseSiteIdAndUserIdParam
	@ApiResponse(responseCode = "200", description = "WsRecommendationWsDTO", content = @Content(schema = @Schema(implementation = WsRecommendationWsDTO.class)))
	public WsRecommendationWsDTO getMyRecommendationDetails(
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
			{ "BASIC", "DEFAULT", "FULL" }))
			@RequestParam(defaultValue = "DEFAULT")
			final String fields, @PathVariable
			final String recommendationId, @PathVariable
			final String userId) throws WholescriptsPageNotFoundException
	{
		try
		{
			final Optional<WsRecommendationData> recommendationData = wholescriptsRecommendFacade
					.getRecommendationDetailsData(userId, recommendationId);
			return dataMapper.map(recommendationData.get(), WsRecommendationWsDTO.class, fields);
		}
		catch (final ModelNotFoundException e)
		{
			throw new WholescriptsPageNotFoundException(String.format(e.getMessage(), sanitize(recommendationId)));
		}
	}



	@Secured(
			{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/recommendation/{recommendationId}/entries", method = RequestMethod.POST)
	@ResponseBody
	public CartModificationWsDTO createRecommendEntry(
			@Parameter(description = "Request body parameter that contains details such as the product code (product.code), the quantity of product (quantity), and the pickup store name (deliveryPointOfService.name).\n\nThe DTO is in XML or .json format.", required = true)
			@RequestBody
			final WsRecommendationEntryInputWsDTO entry, @PathVariable
			final String userId, @PathVariable
			final String recommendationId, @PathVariable
			final String baseSiteId,
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body.", schema = @Schema(allowableValues =
			{ "BASIC", "DEFAULT", "FULL" }))
			@RequestParam(defaultValue = WholescriptsoccConstants.DEFAULT_FIELD_SET)
			final String fields) throws CommerceCartModificationException
	{
		try
		{
		if (entry.getQuantity() == null)
		{
			entry.setQuantity(DEFAULT_PRODUCT_QUANTITY);
		}
		validate(entry, ENTRY, orderEntryCreateValidator);
		final String pickupStore = entry.getDeliveryPointOfService() == null ? null : entry.getDeliveryPointOfService().getName();
		return addRecommendationEntryInternal(baseSiteId, recommendationId, entry.getProduct().getCode(), entry.getQuantity(),
				pickupStore,
				fields, entry);
	}
	catch (final ModelNotFoundException e)
	{
		throw new RequestParameterException(String.format(e.getMessage(), sanitize(recommendationId)),
				RequestParameterException.INVALID, recommendationId);
	}

	}

	@Secured(
			{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@PatchMapping(value = "/recommendation/{recommendationId}/entries/{entryNumber}", consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	@Operation(operationId = "updateRecommendationEntry", summary = "Updates the details of a recommendation entry.", description = "Updates the quantity of a recommendation entry and the details of the pickup store.")
	public CartModificationWsDTO updateRecommendationEntry(@PathVariable
	final String baseSiteId, @PathVariable
	final String recommendationId,
			@Parameter(description = "Each entry in a cart has an entry number. Cart entries are numbered in ascending order, starting with zero.", required = true)
			@PathVariable
			final long entryNumber,
			@Parameter(description = "Request body parameter that contains details such as the quantity of product (quantity), and the pickup store name (deliveryPointOfService.name)\n\nThe DTO is in XML or .json format.", required = true)
			@RequestBody
			final WsRecommendationEntryInputWsDTO entry, @ApiFieldsParam
			@RequestParam(defaultValue = WholescriptsoccConstants.DEFAULT_FIELD_SET)
			final String fields) throws CommerceCartModificationException
	{
		try
		{
		final WsRecommendationData recommendData = wholescriptsRecommendFacade.getRecommendation(recommendationId);
		final WsRecommendationEntryData orderEntry = wholescriptsHelper.getRecommendationEntryForNumber(recommendData, entryNumber);

		validateProductCode(orderEntry, entry);

		if (entry.getQuantity() == null)
		{
			entry.setQuantity(orderEntry.getQuantity());
		}

		validate(entry, ENTRY, orderEntryUpdateValidator);

		final String pickupStore = entry.getDeliveryPointOfService() == null ? null : entry.getDeliveryPointOfService().getName();
		return updateRecommendEntryInternal(baseSiteId, recommendData, orderEntry, entry, entry.getQuantity(), pickupStore, fields,
				false);
	}
	catch (final CommerceCartModificationException e)
	{
		throw new RequestParameterException(String.format(e.getMessage(), sanitize(recommendationId)),
				RequestParameterException.INVALID, recommendationId);
	}
	}

	@Secured(
			{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@DeleteMapping(value = "/recommendation/{recommendationId}/entries/{entryNumber}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@Operation(operationId = "deleteRecommendationEntry", summary = "deletes a recommendation entry.", description = "deletes the recommendatio entry.")
	public void deleteRecommendationEntry(@PathVariable
	final String baseSiteId, @PathVariable
	final String recommendationId,
			@Parameter(description = "Each entry in a cart has an entry number. Cart entries are numbered in ascending order, starting with zero.", required = true)
			@PathVariable
			final long entryNumber) throws CommerceCartModificationException
	{
		try
		{
			final WsRecommendationData recommendData = wholescriptsRecommendFacade.getRecommendation(recommendationId);
			wholescriptsRecommendFacade.updateRecommendationEntry(entryNumber, 0, recommendationId);
		}
		catch (final CommerceCartModificationException e)
		{
			throw new RequestParameterException(String.format(e.getMessage(), sanitize(recommendationId)),
					RequestParameterException.INVALID, recommendationId);
		}
	}

	@Secured(
			{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/recommendation/{recommendationId}/delete", method = RequestMethod.DELETE)
	@ResponseBody
	@Operation(operationId = "deleteRecommendation", summary = "It will update the recommendation status as deleted.")
	@ApiBaseSiteIdAndUserIdParam
	public WsRecommendationWsDTO deleteRecommendation(
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
			{ "BASIC", "DEFAULT", "FULL" }))
			@RequestParam(defaultValue = "DEFAULT")
			final String fields, @PathVariable
			final String userId, @PathVariable
			final String recommendationId)
	{
		try
		{
			final Optional<WsRecommendationData> recommendData = wholescriptsRecommendFacade.deleteRecommendation(userId,
					recommendationId);
			return dataMapper.map(recommendData.get(), WsRecommendationWsDTO.class, fields);
		}
		catch (final ModelSavingException e)
		{
			throw new RequestParameterException(String.format(e.getMessage(), sanitize(userId)), RequestParameterException.INVALID,
					userId);
		}
	}


	protected static void validateProductCode(final WsRecommendationEntryData originalEntry,
			final WsRecommendationEntryInputWsDTO entry)
	{
		final String productCode = originalEntry.getProduct().getCode();
		final Errors errors = new BeanPropertyBindingResult(entry, ENTRY);
		if (entry.getProduct() != null && entry.getProduct().getCode() != null && !entry.getProduct().getCode().equals(productCode))
		{
			errors.reject("cartEntry.productCodeNotMatch");
			throw new WebserviceValidationException(errors);
		}
	}


	protected CartModificationWsDTO addRecommendationEntryInternal(final String baseSiteId, final String recommendationId,
			final String code,
			final long qty, final String pickupStore, final String fields, final WsRecommendationEntryInputWsDTO entry)
			throws CommerceCartModificationException
	{
		final CartModificationData cartModificationData;
		if (StringUtils.isNotEmpty(pickupStore))
		{
			//validateIfProductIsInStockInPOS(baseSiteId, code, pickupStore, null);
			cartModificationData = wholescriptsRecommendFacade.createEntryRecommendationForStore(code, qty, recommendationId, entry,
					pickupStore);
		}
		else
		{
			//validateIfProductIsInStockOnline(baseSiteId, code, null);
			cartModificationData = wholescriptsRecommendFacade.createEntryRecommendation(code, qty, recommendationId, entry);
		}
		return dataMapper.map(cartModificationData, CartModificationWsDTO.class, fields);
	}

	protected CartModificationWsDTO updateRecommendEntryInternal(final String baseSiteId, final WsRecommendationData cart,
			final WsRecommendationEntryData orderEntry, final WsRecommendationEntryInputWsDTO entry, final Long qty,
			final String pickupStore, final String fields, final boolean putMode) throws CommerceCartModificationException
	{
		final long entryNumber = orderEntry.getEntryNumber().longValue();
		final String productCode = orderEntry.getProduct().getCode();
		final PointOfServiceData currentPointOfService = orderEntry.getDeliveryPointOfService();

		final CartModificationData cartModificationData1 = null;
		CartModificationData cartModificationData2 = null;


		if (qty != null)
		{
			cartModificationData2 = wholescriptsRecommendFacade.updateRecommendEntry(entryNumber, cart.getId(), entry, qty);
		}

		return dataMapper.map(mergeCartModificationData(cartModificationData1, cartModificationData2), CartModificationWsDTO.class,
				fields);
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

	protected static void validateForAmbiguousPositions(final WsRecommendationData currentCart,
			final WsRecommendationEntryData currentEntry, final String newPickupStore)
	{
		final WsRecommendationEntryData entryToBeModified = getRecommendationEntry(currentCart, currentEntry.getProduct().getCode(),
				newPickupStore);
		if (entryToBeModified != null && !entryToBeModified.getEntryNumber().equals(currentEntry.getEntryNumber()))
		{
			throw new CartEntryException(
					"Ambiguous cart entries! Entry number " + currentEntry.getEntryNumber()
							+ " after change would be the same as entry " + entryToBeModified.getEntryNumber(),
					CartEntryException.AMBIGIOUS_ENTRY, entryToBeModified.getEntryNumber().toString());
		}
	}

	protected static WsRecommendationEntryData getRecommendationEntry(final WsRecommendationData cart, final String productCode,
			final String pickupStore)
	{
		final Predicate<WsRecommendationEntryData> productsEqualFilter = orderEntryData -> orderEntryData != null
				&& orderEntryData.getProduct() != null && orderEntryData.getProduct().getCode() != null //
				&& orderEntryData.getProduct().getCode().equals(productCode);

		final Predicate<WsRecommendationEntryData> noStoresFilter = orderEntryData -> pickupStore == null
				&& orderEntryData.getDeliveryPointOfService() == null;

		final Predicate<WsRecommendationEntryData> storesEqualFilter = orderEntryData -> pickupStore != null
				&& orderEntryData.getDeliveryPointOfService() != null
				&& pickupStore.equals(orderEntryData.getDeliveryPointOfService().getName());

		return cart.getRecommendationEntries().stream() //
				.filter(productsEqualFilter.and(noStoresFilter.or(storesEqualFilter))).findFirst() //
				.orElse(null);
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

	@Secured(
			{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/recommendation/{recommendationId}/swap-protocol", method = RequestMethod.PATCH)
	@ResponseBody
	@Operation(operationId = "swapProtocol", summary = "Swaps the current protocol with a new one.")
	@ApiBaseSiteIdAndUserIdParam
	@ApiResponse(responseCode = "200", description = "Protocol swapped successfully", content = @Content(schema = @Schema(implementation = WsRecommendationWsDTO.class)))
	public WsRecommendationWsDTO swapProtocol(@Parameter(description = "Swaps the current protocol with a new one.")
											  @RequestParam(required = true) final String templateId, @RequestParam(required = true) final String recommendationId, @RequestParam(defaultValue = WholescriptsoccConstants.DEFAULT_FIELD_SET) final String fields
											  , @RequestParam(required = false, defaultValue = "false") final boolean isWSPage) {
		try {
			final WsRecommendationData recommendationData = wholescriptsRecommendFacade.swapProtocol(templateId, recommendationId,isWSPage);
			return dataMapper.map(recommendationData, WsRecommendationWsDTO.class, fields);
		} catch (final ModelSavingException e) {
			throw new RequestParameterException(String.format(e.getMessage(), sanitize(templateId)), RequestParameterException.INVALID,
					templateId);
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

	protected static String sanitize(final String input)
	{
		return YSanitizer.sanitize(input);
	}
	@Secured(
			{ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@GetMapping(value = "/getPatientRecommendations")
	@ResponseBody
	@Operation(operationId = "getPatientRecommendations", summary = "it will return the paginated list of recommendation for a patient")
	@ApiBaseSiteIdAndUserIdParam
	@ApiResponse(responseCode = "200", description = "RecommendationListWsDTO", content = @Content(schema = @Schema(implementation = RecommendationListWsDTO.class)))
	public RecommendationListWsDTO getPatientRecommendations(
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
					{ "BASIC", "DEFAULT", "FULL" }))
			@RequestParam(defaultValue = "DEFAULT")
			final String fields, @Parameter(description = "Current result page. Default value is 0.")
			@RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE)
			final int currentPage, @Parameter(description = "Number of results returned per page. Default value: 10.")
			@RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE)
			final int pageSize,@RequestParam
			final String patientId,final WholescriptsRecommendSearchParams searchParam)
	{
		try
		{
			final SearchPageData<WsRecommendationData> searchPageData = webPaginationUtils.buildSearchPageData("",
					currentPage, pageSize, true);
			final SearchPageData<WsRecommendationData> recommendationListData = wholescriptsRecommendFacade.getPatientRecommendationListData(searchPageData,patientId,searchParam);

			return formatSearchPageDataResult(fields, recommendationListData);
		}
		catch (final ModelNotFoundException e)
		{
			throw new RequestParameterException(String.format(e.getMessage(), sanitize(patientId)), RequestParameterException.INVALID,
					patientId);
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
