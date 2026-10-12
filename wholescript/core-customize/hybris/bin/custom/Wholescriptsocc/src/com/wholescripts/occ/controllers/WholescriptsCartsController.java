/**
 *
 */
package com.wholescripts.occ.controllers;

import static de.hybris.platform.commercefacades.order.constants.OrderOccControllerRequestFromConstants.CART_ENTRIES_CONTROLLER;

import de.hybris.platform.b2bacceleratorfacades.api.cart.CartFacade;
import de.hybris.platform.b2bocc.v2.controllers.BaseController;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.DeliveryModesData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.commerceservices.request.mapping.annotation.RequestMappingOverride;
import de.hybris.platform.commercewebservices.core.requestfrom.RequestFromValueSetter;
import de.hybris.platform.commercewebservices.core.skipfield.SkipCartFieldValueSetter;
import de.hybris.platform.commercewebservicescommons.annotation.SiteChannelRestriction;
import de.hybris.platform.commercewebservicescommons.dto.order.CartModificationWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.CartWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.DeliveryModeListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.CartEntryException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.webservicescommons.dto.PaginationWsDTO;
import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;
import de.hybris.platform.webservicescommons.pagination.WebPaginationUtils;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdUserIdAndCartIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;
import de.hybris.platform.webservicescommons.util.YSanitizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.wholescripts.core.exception.AgeRestrictionException;
import com.wholescripts.core.exception.BrandAgreementException;
import com.wholescripts.core.exception.POBoxAgeRestrictionException;
import com.wholescripts.core.exception.PromotionErrorMessageException;
import com.wholescripts.core.exception.PromotionInvalidException;
import com.wholescripts.core.exception.RegionExclusionException;
import com.wholescripts.core.facade.WholescriptsCartFacade;
import com.wholescripts.core.facade.WholescriptsCartValidationFacade;
import com.wholescripts.core.facade.WholescriptsCommercePromotionRestrictionFacade;
import com.wholescripts.core.facade.WholescriptsUserFacade;
import com.wholescripts.core.facade.impl.WholescriptsDefaultB2BCheckoutFacade;
import com.wholescripts.core.strategies.WholescriptsCommerceCartCalculationStrategy;
import com.wholescripts.data.AddtocartRequestData;
import com.wholescripts.data.OrderSimulateResponseData;
import com.wholescripts.dto.CartErrorWsDTO;
import com.wholescripts.dto.ConsentUpdateBrandListWsDTO;
import com.wholescripts.dto.WholescriptsBrandData;
import com.wholescripts.dto.WholescriptsBrandWsDTO;
import com.wholescripts.dto.WholescriptsValidateCartsWsDTO;
import com.wholescripts.occ.constants.WholescriptsSecuredAccessConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;



@Controller
@ApiVersion("v2")
@RequestMapping(value = "/{baseSiteId}")
@Tag(name = "Wholescripts Carts")
public class WholescriptsCartsController extends BaseController
{
	protected static final String DEFAULT_FIELD_SET = FieldSetLevelHelper.DEFAULT_LEVEL;
	protected static final String ENTRY = "entry";

	private static final String  BASE_SITE_USER_PATH="/users/{userId}/carts";

	private static final String BASE_SITE_ORGANIZATION_PATH="/orgUsers/{userId}";

	private static final String ADDRESS_MAPPING = "firstName,lastName,titleCode,phone,cellphone,line1,line2,town,postalCode,country(isocode),region(isocode),defaultAddress,companyName,addressDescription";
	private static final String CART_VALIDATION_REQUEST = "cartValidationRequest";
	private static final String OBJECT_NAME_ADDRESS="address";
	protected static final String API_COMPATIBILITY_B2B_CHANNELS = "api.compatibility.b2b.channels";
	public static final Double ZERO = 0.0;
	@Resource(name = "dataMapper")
	private DataMapper dataMapper;
	@Resource(name = "wholescriptsCartFacade")
	private WholescriptsCartFacade wholescriptsCartFacade;

	@Resource(name = "wholescriptsUserFacade")
	private WholescriptsUserFacade wholescriptsUserFacade;

	@Resource(name = "addressDTOValidator")
	private Validator addressDTOValidator;

	@Resource(name = "webPaginationUtils")
	private WebPaginationUtils webPaginationUtils;
	@Resource(name = "skipCartFieldValueSetter")
	private SkipCartFieldValueSetter skipCartFieldValueSetter;
	@Resource(name = "b2bCartFacade")
	private CartFacade cartFacade;

	@Resource(name = "b2BPlaceOrderCartValidator")
	private Validator placeOrderCartValidator;
	@Resource(name = "b2bCheckoutFacade")
   private WholescriptsDefaultB2BCheckoutFacade b2bCheckoutFacade;
	@Resource(name = "wholeScriptsCartValidator")
	private Validator wholeScriptsCartValidator;
	@Resource(name = "wholescriptsCartValidationFacade")
	private WholescriptsCartValidationFacade wholescriptsCartValidationFacade;

	@Resource(name = "orderEntryUpdateValidator")
	private Validator orderEntryUpdateValidator;
	
	@Resource(name = "cartService")
	private CartService cartService;
	
	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;
	@Resource(name = "calculationService")
	private CalculationService calculationService;
	@Resource(name = "wsCommercePromotionRestrictionFacade")
	private WholescriptsCommercePromotionRestrictionFacade wsCommercePromotionRestrictionFacade;
	@Resource(name = "wholescriptsCommerceCartCalculationStrategy")
	WholescriptsCommerceCartCalculationStrategy wholescriptsCommerceCartCalculationStrategy;
	@Resource(name = "modelService")
   private ModelService modelService;
	@Resource(name = "requestFromValueSetter")
	private RequestFromValueSetter requestFromValueSetter;

	

	@PostMapping(value =BASE_SITE_USER_PATH + "/{cartId}/addtocart")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	@Operation(operationId = "AddToCart", summary = "adding entries in cart.", description = "It will create cart entries for custom medpax ,protcol and recommendation and adding in session cart")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public CartWsDTO createCart(@Parameter(description = "ID for Custom MedPax")
	@RequestParam(required = false)
	final String medpaxId, @Parameter(description = "ID for protocol")
	@RequestParam(required = false)
	final String protocolId, @Parameter(description = "ID for recommendation")
	@RequestParam(required = false)
	final String recommendationId, @ApiFieldsParam
	@RequestParam(defaultValue = DEFAULT_FIELD_SET)
	final String fields, @PathVariable
	final String cartId, @RequestParam(required = false, defaultValue = "false") final boolean isWSPage) throws CommerceCartModificationException
	{
		try
		{
			final CartData cartData = wholescriptsCartFacade.addToCart(medpaxId, protocolId, recommendationId,isWSPage);
			return dataMapper.map(cartData, CartWsDTO.class, fields);
		}
		catch (final Exception e)
		{
			throw new RequestParameterException(String.format(e.getMessage(), sanitize(cartId)),
					RequestParameterException.INVALID, cartId);
		}
	}
	@Secured({ "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_GUEST", "ROLE_TRUSTED_CLIENT" })
	@PostMapping(value =BASE_SITE_USER_PATH +"/{cartId}/addresses/delivery", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	@RequestMappingOverride(priorityProperty = "occ.wholescripts.address.priority")
	@Operation(operationId = "createCartDeliveryAddress", summary = "Creates a delivery address for the cart.", description = "")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public AddressWsDTO createCartDeliveryAddress(@Parameter(description =
			"Request body parameter that contains details such as the customer's first name (firstName), the customer's last name (lastName), the customer's title (titleCode), the customer's phone (phone), "
					+ "the country (country.isocode), the first part of the address (line1), the second part of the address (line2), the town (town), the postal code (postalCode), and the region (region.isocode).\n\nThe DTO is in XML or .json format.", required = true) @RequestBody final AddressWsDTO address,
												  @ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields,@PathVariable
													  final String cartId)
	{
		validate(address, OBJECT_NAME_ADDRESS, addressDTOValidator);
		AddressData addressData = getDataMapper().map(address, AddressData.class, ADDRESS_MAPPING);
		addressData.setShippingAddress(true);
		addressData.setVisibleInAddressBook(true);
        try {
            addressData =wholescriptsUserFacade.addAddress(addressData);
			wholescriptsCartFacade.setCartDeliveryAddressInternal(addressData.getId());
        } catch (Exception e) {
			throw new RequestParameterException(String.format(e.getMessage(), sanitize(cartId)),
					RequestParameterException.INVALID, cartId);
        }

		return dataMapper.map(addressData, AddressWsDTO.class, fields);
	}

	@Secured({ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value =BASE_SITE_USER_PATH +"/{cartId}/addresses",method = RequestMethod.GET)
	@ResponseBody
	@Operation(operationId = "getAddresses", summary = "Retrieves the addresses of a customer.", description = "Retrieves a list of all the addresses associated with a customer.")
	@ApiBaseSiteIdAndUserIdParam
	@ApiResponse(responseCode = "200", description = "List of customer's addresses")
	public AddressListWsDTO getAddresses(@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
			{ "BASIC", "DEFAULT", "FULL" }))
											 @RequestParam(defaultValue = "DEFAULT")
											 final String fields, @Parameter(description = "Current result page. Default value is 0.")
											 @RequestParam(required = false, defaultValue = DEFAULT_CURRENT_PAGE)
											 final int currentPage, @Parameter(description = "Number of results returned per page. Default value: 10.")
											 @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE)
											 final int pageSize,@PathVariable
	final String cartId, @RequestParam(required = false)
	final String addressSearch)
	{
		try
		{
			List<AddressData> addressDataSearchPageDataList=wholescriptsUserFacade.getAllAddressForCustomers(addressSearch);
			final AddressListWsDTO addressList = new AddressListWsDTO();
			final List<AddressWsDTO> addressWsDTOList = addressDataSearchPageDataList.stream()
					.map(addressData -> dataMapper.map(addressData, AddressWsDTO.class, fields)).toList();

			addressList.setAddresses(addressWsDTOList);
			return addressList;
		} catch (Exception e) {
			throw new RequestParameterException(String.format(e.getMessage(), sanitize(cartId)),
					RequestParameterException.INVALID, cartId);
		}

	}

	private AddressListWsDTO formatSearchPageDataResult(String fields, SearchPageData<AddressData> searchPageDataResult) {

		final AddressListWsDTO addressList = new AddressListWsDTO();
		final List<AddressWsDTO> addressWsDTOList = searchPageDataResult.getResults().stream()
				.map(addressData -> dataMapper.map(addressData, AddressWsDTO.class, fields)).toList();
		addressList.setAddresses(addressWsDTOList);

		final PaginationWsDTO paginationWsDTO = webPaginationUtils.buildPaginationWsDto(searchPageDataResult.getPagination());
		addressList.setPagination(paginationWsDTO);
		return addressList;
	}
	
	@RequestMappingOverride(priorityProperty = "occ.wholescripts.cart.priority")
	@GetMapping(value =BASE_SITE_USER_PATH + "/{cartId}")
	@ResponseBody
	@Operation(operationId = "getCart", summary = "Retrieves a cart.", description = "Retrieves a cart using the cart identifier. To get entryGroup information, set fields value as follows: fields=entryGroups(BASIC), fields=entryGroups(DEFAULT), or fields=entryGroups(FULL).")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public CartWsDTO getCart(@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields,@PathVariable final String userId,
			@RequestParam(value = "orderSimulate", required = false, defaultValue = "true") final boolean orderSimulate) throws Exception
	{

		CartModel cart= cartService.getSessionCart();
		skipCartFieldValueSetter.setValue(fields);
		// CartMatchingFilter sets current cart based on cartId, so we can return cart from the session
		wholescriptsCommerceCartCalculationStrategy.applyPromotionOnCart(cart);
		OrderSimulateResponseData responseData= null;
		if(!userId.equalsIgnoreCase("anonymous") && orderSimulate) {
			responseData =wholescriptsCartFacade.orderSimulate(cart, baseStoreService.getCurrentBaseStore());
		}
		if(Objects.isNull(responseData) || (Objects.nonNull(responseData) && !responseData.getSuccess()) || userId.equalsIgnoreCase("anonymous")) {
			cart.setCalculated(false);
			calculationService.calculate(cart);
		}
		if(Objects.nonNull(responseData) && Objects.nonNull(responseData.getDisplayZero()) && responseData.getDisplayZero()) {
			cart.setTotalPrice(ZERO);
			modelService.save(cart);
		}
		CartData cartData = cartFacade.getCurrentCart();
		if(Objects.isNull(responseData) || (Objects.nonNull(responseData) && !responseData.getSuccess())) {
			cartData.getEntries().forEach(e -> {e.setBasePrice(e.getTotalPrice());});
		}
		cartData.setSuccess(false);
		if(Objects.nonNull(responseData)) {
			cartData.setHeaderInvalidsPromoCodes(CollectionUtils.isNotEmpty(responseData.getHeaderInvalidsPromoCodes()) ? responseData.getHeaderInvalidsPromoCodes() : Collections.EMPTY_LIST);
			cartData.setItemInvalidsPromoCodes(CollectionUtils.isNotEmpty(responseData.getItemInvalidsPromoCodes()) ? responseData.getItemInvalidsPromoCodes() : Collections.EMPTY_LIST);
			cartData.setSuccess(responseData.getSuccess());
		}
		cartData.getEntries().forEach(e -> {
			e.getProduct().setName(e.getProductDescription());
		});
		return getDataMapper().map(cartData, CartWsDTO.class, fields);
	}

	@Secured({ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@PutMapping(value =BASE_SITE_USER_PATH + "/{cartId}/addresses/delivery")
	@ResponseStatus(HttpStatus.OK)
	@SiteChannelRestriction(allowedSiteChannelsProperty = API_COMPATIBILITY_B2B_CHANNELS)
	@RequestMappingOverride(priorityProperty = "occ.wholescripts.address.priority")
	@Operation(operationId = "replaceCartDeliveryAddress", summary = "Updates the delivery address for a cart.", description = "The updated address country must be a valid delivery country for the current base store.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public void replaceCartDeliveryAddress(
			@Parameter(description = "Address identifier", required = true) @RequestParam final String addressId)
	{
		wholescriptsCartFacade.setCartDeliveryAddressInternal(addressId);
	}
	
	protected static String sanitize(final String input)
	{
		return YSanitizer.sanitize(input);
	}
	
	@RequestMappingOverride(priorityProperty = "occ.wholescripts.cart.priority")
	@Secured({ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@GetMapping(value =BASE_SITE_USER_PATH + "/{cartId}/deliverymodes")
	@ResponseBody
	@Operation(operationId = "getCartDeliveryModes", summary = "Retrieves the delivery modes for the current store and delivery address.", description =
			"Retrieves the delivery modes defined for the current base store and the cart delivery address. If the cart does not have a delivery address, an empty list will be returned.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public DeliveryModeListWsDTO getCartDeliveryModes(
			@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final DeliveryModesData deliveryModesData = new DeliveryModesData();
		deliveryModesData.setDeliveryModes(b2bCheckoutFacade.getSupportedDeliveryModes());
		return getDataMapper().map(deliveryModesData, DeliveryModeListWsDTO.class, fields);
	}
	
	@Secured({ "ROLE_CUSTOMERGROUP", "ROLE_GUEST", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMappingOverride(priorityProperty = "occ.wholescripts.cart.priority")
	@PutMapping(value =BASE_SITE_USER_PATH + "/{cartId}/deliverymode")
	@ResponseStatus(HttpStatus.OK)
	@Operation(operationId = "replaceCartDeliveryMode", summary = "Updates the delivery mode for the cart.", description = "Updates the delivery mode details for the cart based on the specified delivery mode identifier.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public void replaceCartDeliveryMode(
			@Parameter(description = "Delivery mode identifier (code)", required = true) @RequestParam(required = true) final String deliveryModeId, @Parameter(description = "Delivery Cost", required = false) @RequestParam(required = false) final String deliveryCost)
	{
		setCartDeliveryModeInternal(deliveryModeId,deliveryCost);
	}


	@Secured({ "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_GUEST", "ROLE_TRUSTED_CLIENT" })
	@PostMapping(value =BASE_SITE_USER_PATH + "/{cartId}/addresses/create-delivery-address", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	@RequestMappingOverride(priorityProperty = "occ.wholescripts.address.priority")
	@Operation(operationId = "createCartDeliveryAddress", summary = "Creates a delivery address for the cart.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public AddressWsDTO createCartDeliveryAddress(@Parameter(description = "Request body parameter that contains details such as the customer's first name (firstName), the customer's last name (lastName), the customer's title (titleCode), the customer's phone (phone), "
							+ "the country (country.isocode), the first part of the address (line1), the second part of the address (line2), the town (town), the postal code (postalCode), and the region (region.isocode).\n\nThe DTO is in XML or .json format.", required = true)
			@RequestBody final AddressWsDTO address,
			@Parameter(description = "The address number provided by the user.", required = true)
			@RequestParam final String addressNumber,
			@ApiFieldsParam
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields, @PathVariable final String cartId)
	{

		//validate(address, OBJECT_NAME_ADDRESS, addressDTOValidator);
		AddressData addressData = getDataMapper().map(address, AddressData.class, ADDRESS_MAPPING);
		addressData.setShippingAddress(true);
		addressData.setVisibleInAddressBook(true);

		try {
			addressData = wholescriptsUserFacade.addAddressWithoutSAP(addressData,addressNumber);
		} catch (Exception e) {
			throw new RequestParameterException(String.format(e.getMessage(), sanitize(cartId)), RequestParameterException.INVALID, cartId
			);
		}
		return dataMapper.map(addressData, AddressWsDTO.class, fields);
	}
	
	@Secured({ "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_GUEST", "ROLE_TRUSTED_CLIENT" })
	@PostMapping(value =BASE_SITE_USER_PATH + "/{cartId}/verification", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	@Operation(operationId = "verifyCart", summary = "it will validate cart entries against exclusion table and persona verify ID.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public ResponseEntity<String> validateCarts(@Parameter(description = "Request Body", required = true)
			@RequestBody final WholescriptsValidateCartsWsDTO request) throws Exception
	{
		if(!request.isBrandValidation())
		{
			validateCartRequestData(request, CART_VALIDATION_REQUEST, addressDTOValidator);
		}

      wholescriptsCartValidationFacade.validateCart(request);
		return ResponseEntity.ok("verified successfully.");
	}

	@Operation(operationId = "addOrgCartEntry", hidden = true, summary = "Adds more quantity to the cart for a specific entry",
			description = "Adds more quantity to the cart for a specific entry based on it's product code, if the product is already in the cart the amount will be added to the existing quantity.")
	@RequestMappingOverride(priorityProperty = "occ.addCartEntry.priority")
	@SiteChannelRestriction(allowedSiteChannelsProperty = API_COMPATIBILITY_B2B_CHANNELS)
	@PostMapping(value =BASE_SITE_ORGANIZATION_PATH + "/carts/{cartId}/entries")
	@ResponseBody
	@ApiBaseSiteIdUserIdAndCartIdParam
	public CartModificationWsDTO addCartEntry(
			@Parameter(description = "Base site identifier.", required = true) @PathVariable final String baseSiteId,
			@Parameter(description = "Code of the product to be added to the cart.", required = true) @RequestParam(required = true) final String code,
			@Parameter(description = "Amount to be added.", required = false) @RequestParam(required = false, defaultValue = "1") final long quantity,
			@ApiFieldsParam @RequestParam(required = false, defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields,@Parameter(description = "Code of the auto ship Frequency to be added to the cart.", required = false) @RequestParam(required = false) final String autoShipCode)
	{
		final OrderEntryData orderEntry = getOrderEntryData(quantity, code,autoShipCode);
		return dataMapper.map(wholescriptsCartFacade.addOrderEntry(orderEntry), CartModificationWsDTO.class, fields);
    }
	@PatchMapping(value = BASE_SITE_USER_PATH + "/{cartId}/entries/{entryNumber}", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@RequestMappingOverride(priorityProperty = "occ.wholescripts.cart.priority")
	@ResponseBody
	@Operation(operationId = "updateCartEntry", summary = "Updates the details of a cart entry.", description = "Updates the quantity of a single cart entry and the details of the pickup store.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public CartModificationWsDTO updateCartEntry(@PathVariable final String baseSiteId,
												 @Parameter(description = "Each entry in a cart has an entry number. Cart entries are numbered in ascending order, starting with zero.", required = true) @PathVariable final long entryNumber,
												 @Parameter(description = "Request body parameter that contains details such as the quantity of product (quantity), and the pickup store name (deliveryPointOfService.name)\n\nThe DTO is in XML or .json format.", required = true) @RequestBody final OrderEntryWsDTO entry,
												 @ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields)
			throws CommerceCartModificationException
	{
		final CartData cart = cartFacade.getCurrentCart();
		final OrderEntryData orderEntry = getCartEntryForNumber(cart, entryNumber);

		validateProductCode(orderEntry, entry);

		if (entry.getQuantity() == null)
		{
			entry.setQuantity(orderEntry.getQuantity());
		}else if(entry.getQuantity() == 0) {
			entry.setQuantity(1L);
		}

		validate(entry, ENTRY, orderEntryUpdateValidator);

		final String pickupStore = entry.getDeliveryPointOfService() == null ? null : entry.getDeliveryPointOfService().getName();
		return updateCartEntryInternal(baseSiteId, cart, orderEntry, entry.getQuantity(), pickupStore, fields, false,entry.getAutoShipCode());
	}

	@DeleteMapping(value = BASE_SITE_USER_PATH + "/{cartId}/entries")
	@ResponseStatus(HttpStatus.OK)
	@Operation(operationId = "removeCartEntries", summary = "Deletes the cart entries.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public void removeCartEntries(
			@Parameter(description = "Each entry in a cart has an entry number. Cart entries are numbered in ascending order, starting with zero.", required = true) @RequestParam(required = true) final String entryNumbers)
			throws CommerceCartModificationException
	{
		wholescriptsCartFacade.updateCartEntry(entryNumbers);
	}
	protected CartModificationWsDTO updateCartEntryInternal(final String baseSiteId, final CartData cart,
															final OrderEntryData orderEntry, final Long qty, final String pickupStore, final String fields, final boolean putMode, String autoShipCode)
			throws CommerceCartModificationException
	{
		final long entryNumber = orderEntry.getEntryNumber().longValue();
		final String productCode = orderEntry.getProduct().getCode();
		final PointOfServiceData currentPointOfService = orderEntry.getDeliveryPointOfService();

		CartModificationData cartModificationData1 = null;
		CartModificationData cartModificationData2 = null;

		if (!StringUtils.isEmpty(pickupStore))
		{
			if (currentPointOfService == null || !currentPointOfService.getName().equals(pickupStore))
			{
				//was 'shipping mode' or store is changed
				validateForAmbiguousPositions(cart, orderEntry, pickupStore);
				//stockPOSValidator.validate(baseSiteId, productCode, pickupStore, entryNumber);
				cartModificationData1 = wholescriptsCartFacade.updateCartEntry(entryNumber, pickupStore);
			}
		}
		else if (putMode && currentPointOfService != null)
		{
			//was 'pickup in store', now switch to 'shipping mode'
			validateForAmbiguousPositions(cart, orderEntry, pickupStore);
			//stockValidator.validate(baseSiteId, productCode, entryNumber);
			cartModificationData1 = wholescriptsCartFacade.updateCartEntry(entryNumber, pickupStore);
		}

		if (qty != null)
		{
			cartModificationData2 = wholescriptsCartFacade.updateCartEntry(entryNumber, qty,autoShipCode);
		}

		return getDataMapper().map(mergeCartModificationData(cartModificationData1, cartModificationData2),
				CartModificationWsDTO.class, fields);
	}
	protected static OrderEntryData getCartEntryForNumber(final CartData cart, final long number)
	{
		final Integer requestedEntryNumber = (int) number;
		return CollectionUtils.emptyIfNull(cart.getEntries()).stream()
				.filter(entry -> entry != null && requestedEntryNumber.equals(entry.getEntryNumber())).findFirst()
				.orElseThrow(() -> new CartEntryException("Entry not found", CartEntryException.NOT_FOUND, String.valueOf(number)));
	}
	protected static void validateForAmbiguousPositions(final CartData currentCart, final OrderEntryData currentEntry,
														final String newPickupStore)
	{
		final OrderEntryData entryToBeModified = getCartEntry(currentCart, currentEntry.getProduct().getCode(), newPickupStore);
		if (entryToBeModified != null && !entryToBeModified.getEntryNumber().equals(currentEntry.getEntryNumber()))
		{
			throw new CartEntryException("Ambiguous cart entries! Entry number " + currentEntry.getEntryNumber()
					+ " after change would be the same as entry " + entryToBeModified.getEntryNumber(),
					CartEntryException.AMBIGIOUS_ENTRY, entryToBeModified.getEntryNumber().toString());
		}
	}
	protected static OrderEntryData getCartEntry(final CartData cart, final String productCode, final String pickupStore)
	{
		final Predicate<OrderEntryData> productsEqualFilter = orderEntryData -> orderEntryData != null
				&& orderEntryData.getProduct() != null && orderEntryData.getProduct().getCode() != null //
				&& orderEntryData.getProduct().getCode().equals(productCode);

		final Predicate<OrderEntryData> noStoresFilter = orderEntryData -> pickupStore == null
				&& orderEntryData.getDeliveryPointOfService() == null;

		final Predicate<OrderEntryData> storesEqualFilter = orderEntryData -> pickupStore != null
				&& orderEntryData.getDeliveryPointOfService() != null && pickupStore.equals(
				orderEntryData.getDeliveryPointOfService().getName());

		return cart.getEntries().stream() //
				.filter(productsEqualFilter.and(noStoresFilter.or(storesEqualFilter))).findFirst() //
				.orElse(null);
	}
	protected static void validateProductCode(final OrderEntryData originalEntry, final OrderEntryWsDTO entry)
	{
		final String productCode = originalEntry.getProduct().getCode();
		final Errors errors = new BeanPropertyBindingResult(entry, ENTRY);
		if (entry.getProduct() != null && entry.getProduct().getCode() != null && !entry.getProduct().getCode().equals(productCode))
		{
			errors.reject("cartEntry.productCodeNotMatch");
			throw new WebserviceValidationException(errors);
		}
	}
	protected OrderEntryData getOrderEntryData(long quantity, String productCode, String autoShipCode) {
		final OrderEntryData orderEntry = new OrderEntryData();
		orderEntry.setQuantity(quantity);
		orderEntry.setProduct(new ProductData());
		orderEntry.getProduct().setCode(productCode);
		orderEntry.setEntryNumber(null);
		orderEntry.setAutoShipCode(autoShipCode);
		if(Objects.isNull(autoShipCode))
		{
			orderEntry.setAutoShipCode("One-Time");

		}

		return orderEntry;
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

	protected CartData setCartDeliveryModeInternal(final String deliveryModeId, final String deliveryCost)
	{
		if (b2bCheckoutFacade.setDeliveryMode(deliveryModeId, deliveryCost))
		{
			return cartFacade.getCurrentCart();
		}
		throw new RequestParameterException(sanitize(deliveryModeId),RequestParameterException.INVALID, deliveryModeId);	
	}
	
	protected void validateCartRequestData(final Object object, final String objectName, final Validator validator)
	{
		final Errors errors = new BeanPropertyBindingResult(object, objectName);
		wholeScriptsCartValidator.validate(object, errors);
		if (errors.hasErrors())
		{
			throw new WebserviceValidationException(errors);
		}
	}
	
	@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
	@ResponseBody
	@ExceptionHandler({ RegionExclusionException.class })
	public CartErrorWsDTO handleRegionExclusionException(final RegionExclusionException ex)
	{
		return handleErrorInternal("RegionExclusionException", ex.getEntries());
	}
	
	@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
	@ResponseBody
	@ExceptionHandler({ POBoxAgeRestrictionException.class })
	public CartErrorWsDTO handlePOBoxAgeRestrictionException(final POBoxAgeRestrictionException ex)
	{
		return handleErrorInternal("POBoxAgeRestrictionException", ex.getEntries());
	}
	
	@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
	@ResponseBody
	@ExceptionHandler({ AgeRestrictionException.class })
	public CartErrorWsDTO handleAgeRestrictionException(final AgeRestrictionException ex)
	{
		return handleErrorInternal("AgeRestrictionException", ex.getEntries());
	}

	@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
	@ResponseBody
	@ExceptionHandler({ BrandAgreementException.class })
	public CartErrorWsDTO handleBrandAgreementException(final BrandAgreementException ex)
	{
		return handleBrandsErrorInternal("BrandAgreementException", ex.getBrandsAgreementList());
	}
	
	protected CartErrorWsDTO handleErrorInternal(final String type, final List<OrderEntryData> orderEntries)
	{
		CartErrorWsDTO error= new CartErrorWsDTO();
		List<OrderEntryWsDTO> list = new ArrayList<>();
		orderEntries.forEach( e -> {list.add(dataMapper.map(e, OrderEntryWsDTO.class));});
		error.setEntries(list);
		error.setType(type);
		return error;
	}
	protected CartErrorWsDTO handleBrandsErrorInternal(final String type, final List<WholescriptsBrandData> orderEntries)
	{
		CartErrorWsDTO error= new CartErrorWsDTO();
		List<WholescriptsBrandWsDTO> list = new ArrayList<>();
		orderEntries.forEach( e -> {list.add(dataMapper.map(e, WholescriptsBrandWsDTO.class));});
		error.setBrandEntries(list);
		error.setType(type);
		return error;
	}
	
	@Secured(
	{ WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,
			WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@PatchMapping(value = BASE_SITE_USER_PATH + "/{cartId}/medpax/{entryNumber}")
	@ResponseBody
	@Operation(operationId = "updateMedpaxCartEntry", summary = "Updates the details of a medpax cart entry.", description = "Updates the quantity of Medpax entries.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public CartModificationWsDTO updateMedpaxCartEntry(@Parameter(description = "medpaxId for update") @RequestParam(required = true) final String medpaxId, @Parameter(description = "quantity for update") @RequestParam(required = true) final String qty, 
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields, @PathVariable final String entryNumber) throws CommerceCartModificationException
	{
		final CartModificationData cartModificationData = wholescriptsCartFacade.updateMedpaxCartEntry(medpaxId,entryNumber, qty);
		
		return dataMapper.map(cartModificationData, CartModificationWsDTO.class,fields);
	}
	
	@Secured(
	{ WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,
			WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@PostMapping(value = BASE_SITE_USER_PATH + "/{cartId}/medpax/delete/{entryNumber}")
	@Operation(operationId = "deleteMedPaxEntryFromCart", summary = "deleting a MedPax entry from cart", description = "deleting a MedPax entry from cart")
	@ApiBaseSiteIdUserIdAndCartIdParam
	@ApiResponse(responseCode = "200", description = "entry removed successfully.")
	public ResponseEntity<String> deleteMedPaxEntryFromCart(
			@Parameter(description = "Medpax Id For delete entry From Cart")
			@RequestParam(required = true)
			final String medpaxId,@Parameter(description = "Item Time code for deleting Dosing")
			@RequestParam(required = true)
			final String itemTimeCode,@PathVariable final String entryNumber)
	{
		try {
			wholescriptsCartFacade.deleteMedPaxEntryFromCart(medpaxId, entryNumber, itemTimeCode);
			return ResponseEntity.ok("entry removed successfully.");
		}
		 catch (Exception e) {
			throw new RequestParameterException( e.getMessage(),RequestParameterException.INVALID, e);
		}
	}
	
	@Secured(
	{ WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST,
			WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT })
	@PostMapping(value = BASE_SITE_USER_PATH + "/{cartId}/orderhistory/addtocart")
	@ResponseBody
	@Operation(operationId = "AddToCartFromOrderHistory", summary = "Add order entries into cart", description = "Add order entries into cart")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public CartWsDTO addToCartFromOrderHistory(@Parameter(description = "Add to cart Request Data",required = true)
			@RequestBody AddtocartRequestData requestData)
	{
		try {
	
			CartData cartData = wholescriptsCartFacade.addToCart(requestData);
			return dataMapper.map(cartData, CartWsDTO.class);
		}
		 catch (Exception e) {
			throw new RequestParameterException( e.getMessage(),RequestParameterException.INVALID, e);
		}
	}
	
	@Secured({ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_GUEST", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMappingOverride(priorityProperty = "occ.addCartEntry.priority")
	@PostMapping(value =  BASE_SITE_USER_PATH + "/{cartId}/promotions")
	@ResponseStatus(HttpStatus.OK)
	@Operation(operationId = "doApplyCartPromotion", summary = "Assigns a promotion to the cart.", description =
			"Applies a promotion to the cart using the promotion identifier. "
					+ "Requests pertaining to promotions have been developed for the previous version of promotions and vouchers, and as a result, some of them are currently not compatible "
					+ "with the new promotions engine.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	@ApiResponse(responseCode = "200", description = "Promotion is applied successfully.")
	public ResponseEntity<String> doApplyCartPromotion(
			@Parameter(description = "Promotion identifier.", required = true,example = "percentage_discount_on_camera") @RequestParam(required = true) final String promotionId)
			throws Exception
	{
		wsCommercePromotionRestrictionFacade.enablePromotionForCart(promotionId);
		return ResponseEntity.ok("Promotion is applied successfully.");
	}
	
	@Secured({ "ROLE_CUSTOMERGROUP", "ROLE_CLIENT", "ROLE_GUEST", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_TRUSTED_CLIENT" })
	@RequestMappingOverride(priorityProperty = "occ.addCartEntry.priority")
	@DeleteMapping(value = BASE_SITE_USER_PATH +  "/{cartId}/promotions/{promotionId}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(operationId = "removeCartPromotion", summary = "Deletes the promotion.", description =
			"Deletes the promotion of the order using the promotion identifier defined for the cart. "
					+ "Requests pertaining to promotions have been developed for the previous version of promotions and vouchers, and as a result, some of them are currently not compatible with "
					+ "the new promotions engine.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	@ApiResponse(responseCode = "200", description = "Promotion is removed successfully.")
	public ResponseEntity<String>  removeCartPromotion(
			@Parameter(description = "Promotion identifier.", required = true, example = "percentage_discount_on_camera") @PathVariable final String promotionId)
			throws Exception
	{
		wsCommercePromotionRestrictionFacade.disablePromotionForCart(promotionId);
		return ResponseEntity.ok("Promotion is removed successfully.");
	}
	
	
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody
	@ExceptionHandler({ PromotionInvalidException.class })
	public ErrorListWsDTO handlePromotionInvalidException(final PromotionInvalidException ex)
	{
		return handleErrorInternal("PromotionInvalidException", ex.getMessage());
	}
	
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ResponseBody
	@ExceptionHandler({ PromotionErrorMessageException.class })
	public ErrorListWsDTO haPromotionErrorMessagesageException(PromotionErrorMessageException ex)
	{
		return handleErrorInternal("PromotionErrorMessageException", ex.getMessage());
	}
	
	
	@Secured({ "ROLE_CUSTOMERGROUP", "ROLE_CUSTOMERMANAGERGROUP", "ROLE_GUEST", "ROLE_TRUSTED_CLIENT" })
	@PostMapping(value =BASE_SITE_USER_PATH + "/{cartId}/brandConsentUpdate",consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@Operation(operationId = "brandConsentUpdate", summary = "it will update the Brand Consent for the Customer.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public ResponseEntity<String> updateBrandsAgreement(@Parameter(description = "Request Body", required = true)
															@RequestBody final ConsentUpdateBrandListWsDTO request) throws Exception
	{
		try {
			wholescriptsCartFacade.updateBrandConsentForCustomer(request);
			return ResponseEntity.ok("Consent Updated Successfully.");
		}
		catch (Exception e) {
			throw new RequestParameterException( e.getMessage(),RequestParameterException.INVALID, e);
		}

	}
	
	@RequestMappingOverride(priorityProperty = "occ.deleteCartEntry.priority")
	@DeleteMapping(value =BASE_SITE_USER_PATH + "/{cartId}/entries/{entryNumber}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(operationId = "removeCartEntry", summary = "Deletes the cart entry.")
	@ApiBaseSiteIdUserIdAndCartIdParam
	public void removeCartEntry(
			@Parameter(description = "Each entry in a cart has an entry number. Cart entries are numbered in ascending order, starting with zero.", required = true) @PathVariable final long entryNumber)
			throws CommerceCartModificationException
	{
		requestFromValueSetter.setRequestFrom(CART_ENTRIES_CONTROLLER);
		final CartData cart = cartFacade.getCurrentCart();
		getCartEntryForNumber(cart, entryNumber);
		wholescriptsCartFacade.updateCartEntry(String.valueOf(entryNumber));
		
	}
}
