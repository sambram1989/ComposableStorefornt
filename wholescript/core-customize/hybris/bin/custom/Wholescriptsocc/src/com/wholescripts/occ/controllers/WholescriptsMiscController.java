package com.wholescripts.occ.controllers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.wholescripts.core.enums.ContactSelectedCategoryEnum;
import com.wholescripts.core.enums.ItemDuration;
import com.wholescripts.core.facade.WholescriptsCoreFacade;
import com.wholescripts.core.model.WSContactUsFormModel;
import com.wholescripts.data.DashboardProductsData;
import com.wholescripts.data.FaqListData;
import com.wholescripts.data.ProductUpdate.TableType;
import com.wholescripts.data.WholescriptsCategoryOrBrandData;
import com.wholescripts.dto.*;
import com.wholescripts.dto.dosing.DosingListWsDTO;
import com.wholescripts.dto.dosing.DosingRequestWsDto;
import com.wholescripts.occ.constants.WholescriptsSecuredAccessConstants;
import com.wholescripts.occ.constants.WholescriptsoccConstants;
import com.wholescripts.occ.controllers.helper.WholescritpsHelper;
import com.wholescripts.sinch.dto.SinchInboundMessageDTO;
import de.hybris.platform.commercefacades.product.data.*;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductListWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.facetdata.ProductSearchPageWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.webservicescommons.dto.PaginationWsDTO;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;
import de.hybris.platform.webservicescommons.pagination.WebPaginationUtils;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;


@RestController
@ApiVersion("v2")
@RequestMapping(value = "/{baseSiteId}")
@Tag(name = "Wholescripts Misc")
public class WholescriptsMiscController
{

    private static final Logger LOG = LogManager.getLogger(WholescriptsMiscController.class);
	private static final String CONSIDER_FIELDS_KEY = "toggle.occ.retrieving.product.performance.improvement.enabled";

    @Resource(name = "dataMapper")
    protected DataMapper dataMapper;
    @Resource(name = "wholescriptsCoreFacade")
    protected WholescriptsCoreFacade wholescriptsCoreFacade;
	 @Resource(name = "enumerationService")
	 protected EnumerationService enumerationService;
	 @Resource(name = "wholescritpsHelper")
	 private WholescritpsHelper wholescritpsHelper;
	 @Resource(name = "webPaginationUtils")
	 private WebPaginationUtils webPaginationUtils;
	 @Resource(name = "configurationService")
	 private ConfigurationService configurationService;

	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "modelService")
	private ModelService modelService;

    @Operation(operationId = "saveCustomDosingEntries", summary = "Allows saving dosing entries as custom.", description = " This API request is save Custom dosing entries for future references.")
    @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
	 @PostMapping(value = "/users/{userId}/saveCustomDosingEntries", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ApiBaseSiteIdAndUserIdParam
    public DosingListWsDTO saveDosingEntries(@Parameter(description = "The list of Custom dosing to be queried", required = true) @RequestBody @Nonnull final Map<String,String> itemTimesCodes, @RequestParam(required = true)
    final String productId, @RequestParam(required = true)
    final boolean isSavedAsCustom,@ApiFieldsParam
    @RequestParam(defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields) {
   	 final DosingRequestWsDto dosingRequest = convertInDosingRequest(itemTimesCodes, productId, isSavedAsCustom);
   	 final List<DosingData> listDosingData = wholescriptsCoreFacade.saveDosingEntries(dosingRequest);
   	 return  convertDosingDataInWsDTO(fields, listDosingData);
    }

	/**
	 * @param fields
	 * @param listDosingData
	 * @return
	 */
	private DosingListWsDTO convertDosingDataInWsDTO(final String fields, final List<DosingData> listDosingData)
	{
		final DosingListWsDTO listData = new DosingListWsDTO();
   	 final List<DosingWsDTO> dosingWsDTOList = listDosingData.stream()
   			 .map(doisingData -> dataMapper.map(doisingData, DosingWsDTO.class,fields)).toList();
   	 listData.setDosingList(dosingWsDTOList);
		return listData;
	}

	@Operation(operationId = "getCustomDosingEntries", summary = "Allows get custom dosing entries.", description = " This API request is get Custom dosing entries for future references.")
	@Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
	@GetMapping(value = "/users/{userId}/getCustomDosingEntries")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiBaseSiteIdAndUserIdParam
	public DosingListWsDTO getCustomDosingEntries( @RequestParam(required = false)
	final String productId,@ApiFieldsParam
	@RequestParam(defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields) {
		final List<DosingData> listDosingData = wholescriptsCoreFacade.getDosingEntries(productId);
		return  convertDosingDataInWsDTO(fields, listDosingData);
	}

	/**
	 * @param itemTimesCodes
	 * @param productId
	 * @param isSavedAsCustom
	 * @return
	 */
    private DosingRequestWsDto convertInDosingRequest(final Map<String, String> itemTimesCodes, final String productId,
   		 final boolean isSavedAsCustom)
    {
   	 final DosingRequestWsDto dosingRequest = new DosingRequestWsDto();
   	 dosingRequest.setIsSavedAsCustom(isSavedAsCustom);
   	 dosingRequest.setProductId(productId);
   	 dosingRequest.setItemTimesCodes(itemTimesCodes);
   	 return dosingRequest;
    }

  @Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
  @GetMapping(value = "/users/{userId}/itemtimes")
  @ResponseBody
  @ApiBaseSiteIdAndUserIdParam
  @ApiResponse(responseCode = "200", description = "ItemTimeListWsDTO", content = @Content(schema = @Schema(implementation = ItemTimeListWsDTO.class)))
  @Operation(operationId = "getItemTimeList", summary = "It will return the All Active ItemTimes ")
  public ItemTimeListWsDTO getItemTimeDetails(@PathVariable("baseSiteId") final String baseSiteId, @ApiFieldsParam
         @RequestParam(defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields) {

      final ItemTimeListWsDTO itemTimeListWsDTO = new ItemTimeListWsDTO();
      final List<ItemTimeWsDTO> listWsDto = wholescriptsCoreFacade.getAllActiveItemTimes().stream()
               .map(itemTimeData -> dataMapper.map(itemTimeData, ItemTimeWsDTO.class, fields)).toList();
      itemTimeListWsDTO.setItemTimeListWsDTO(listWsDto);
      return itemTimeListWsDTO;
  }

  @PostMapping(value = "/contactUs")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  @ApiBaseSiteIdParam
  public ResponseEntity<String> saveContactusForm(@Parameter(description = "Save the contact us form detail")
  @RequestBody
  final WSContactUsFormWsDTO input, @RequestParam(defaultValue = WholescriptsoccConstants.DEFAULT_FIELD_SET)
  final String fields)

  {
	  try
	  {
		  final WSContactUsFormModel contactUsForm = wholescriptsCoreFacade.save(input);
		  return new ResponseEntity<String>(HttpStatus.OK);
	  }
	  catch (final ModelSavingException e)
	  {
		  throw new RequestParameterException(String.format(e.getMessage()), RequestParameterException.INVALID);
	  }
  }

  @RequestMapping(value = "/Category", method = RequestMethod.GET)
  @ResponseBody
  @Operation(operationId = "getTicketCategory", summary = "Get the key value pair of a Ticket Category", description = "Get the key value pair of a Ticket Category.")
  public List<Map<String, Object>> getTicketCategory()
  {
	  final List<ContactSelectedCategoryEnum> categoryList = enumerationService
			  .getEnumerationValues(ContactSelectedCategoryEnum._TYPECODE);
	  final List<Map<String, Object>> result = new ArrayList<>();
	  for (final ContactSelectedCategoryEnum category : categoryList)
	  {
		  final Map<String, Object> map = new HashMap<>();
		  final String text = enumerationService.getEnumerationName(category);
		  if (StringUtils.isNotEmpty(text))
		  {
			  map.put("Key", category.getCode());
			  map.put("Text", text);
			  result.add(map);
		  }
	  }
	  return result;
  }

	@Operation(operationId = "getFaqQusAnsData", summary = "Allows get faq question answer details.", description = " This API request is get faq question answer details.")
	@GetMapping(value = "/getFaqQusAnsDetails")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@ApiBaseSiteIdParam
	public FaqWsResponseDTO getFaqQusAnsDetails() {
		final List<FaqListData> faqListData = wholescriptsCoreFacade.getFaqQusAnsDetails();
		final FaqWsResponseDTO responsedata = new FaqWsResponseDTO();
		final List<FaqWsListDTO> faqWsDtoList = faqListData.stream()
				.map(faq -> dataMapper.map(faq, FaqWsListDTO.class)).toList();
		responsedata.setData(faqWsDtoList);
		return responsedata;
	}

	@Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_CLIENT, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
	@Operation(operationId = "getFaqQusAnsDataByID", summary = "Allows get faq question answer details by ID.", description = " This API request is get faq question answer details.")
	@GetMapping(value = "/getFaqQusAnsById/{uid}")
	@ApiBaseSiteIdParam
	public ResponseEntity<FaqWsListDTO> getFaqById(@PathVariable final String uid) {
		final FaqListData faqListData = wholescriptsCoreFacade.getFaqListByUid(uid);
		if (Objects.isNull(faqListData)) {
			return ResponseEntity.ok().build();
		}
		FaqWsListDTO faqWsListDTO = dataMapper.map(faqListData, FaqWsListDTO.class);
		return ResponseEntity.ok(faqWsListDTO);
	}

	@Operation(operationId = "getTopic", summary = "Allows get topic api and advertising", description = " This API request is get advertising and API resource")
	@GetMapping(value = "/topicPages/{page}")
	@ResponseBody
	@ApiBaseSiteIdParam
	public WholescriptsTopicListWsDTO getTopicDetails(@PathVariable
	final String page, @RequestParam(required = false)
	final String childTopic, @RequestParam(defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL)
	final String fields)
	{
		final WholescriptsTopicListWsDTO topicListWsDTO = new WholescriptsTopicListWsDTO();
		final List<WholescriptsTopicWsDTO> topicWsDto = wholescriptsCoreFacade.getTopicDetails(page, childTopic).stream()
				.map(topicData -> dataMapper.map(topicData, WholescriptsTopicWsDTO.class, fields)).toList();
		topicListWsDTO.setWholescriptsTopicWsDTO(topicWsDto);
		return topicListWsDTO;

	}

	@RequestMapping(value = "/productUpdates", method = RequestMethod.GET)
	@ResponseBody
	@Operation(operationId = "getProductUpdate", summary = "it will return the discontinued products")
	@ApiBaseSiteIdParam
	@ApiResponse(responseCode = "200", description = "ProductListWsDTO", content = @Content(schema = @Schema(implementation = ProductListWsDTO.class)))
	public ProductListWsDTO getProductUpdates(
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
			{ "BASIC", "DEFAULT", "FULL" }))
			@RequestParam(defaultValue = "DEFAULT")
			final String fields, @Parameter(description = "Current result page. Default value is 0.")
			@RequestParam(required = false, defaultValue = WholescriptsoccConstants.DEFAULT_CURRENT_PAGE)
			final int currentPage, @Parameter(description = "Number of results returned per page. Default value: 10.")
			@RequestParam(required = false, defaultValue = WholescriptsoccConstants.DEFAULT_PAGE_SIZE)
			final int pageSize, @Parameter(description = "Sorting method applied to the return results.", example = "uid:asc")
			@RequestParam(required = false, defaultValue = "name")
			final String sort, @RequestParam(required = true)
			final String productTableRequested)
	{
		final SearchPageData<ProductData> searchPageDataInput = webPaginationUtils.buildSearchPageData(sort, currentPage,
				pageSize, true);

		if (productTableRequested.equalsIgnoreCase(TableType.DISCONTINUED_PRODUCTS.toString()))
		{
			// Creates a SearchPageData object contains requested pagination and sorting information

			// Get search result in a SearchPageData object contains search results with applied pagination and sorting information
			final SearchPageData<ProductData> productListData = wholescriptsCoreFacade.getDiscontinuedProducts(searchPageDataInput);
			return formatSearchPageDataResult(fields, productListData, null);
		}
		else if (productTableRequested.equalsIgnoreCase(TableType.AVAILABLE_WHILE_SUPPLIES_LAST.toString()))
		{
			/*final String query = configurationService.getConfiguration().getString("wholescripts.availableWhileSupplyList.query");
			final String searchQueryContext = configurationService.getConfiguration().getString("wholescripts.searchQueryContext");
			final ProductSearchPageWsDTO result = wholescritpsHelper.searchProducts(query, currentPage, pageSize, sort,
					addPaginationField(fields), searchQueryContext);
			return formatSearchPageDataResult(fields, null, result);*/
			final SearchPageData<ProductData> productListData = wholescriptsCoreFacade.getAvailableWhileSuppliesLastProducts(searchPageDataInput);
			return formatSearchPageDataResult(fields, productListData, null);
		}

		throw new RequestParameterException("ProductTableRequested param must be either "
				+ TableType.AVAILABLE_WHILE_SUPPLIES_LAST.toString() + " or " + TableType.DISCONTINUED_PRODUCTS.toString(),
				RequestParameterException.INVALID);

	}

	@RequestMapping(value = "/itemsDuration", method = RequestMethod.GET)
	@ResponseBody
	@ApiBaseSiteIdParam
	@Operation(operationId = "getAllItemDurations", summary = "Get the key value pair of a Item Duration", description = "Get the key value pair of a Item Duration")
	public List<Map<String, Object>> getAllItemDurations() {
		final List<ItemDuration> itemDurationList = enumerationService
				.getEnumerationValues(ItemDuration._TYPECODE);
		final List<Map<String, Object>> result = new ArrayList<>();
		for (final ItemDuration itemDuration : itemDurationList) {
			final Map<String, Object> map = new HashMap<>();
			final String name = enumerationService.getEnumerationName(itemDuration);
			if (StringUtils.isNotEmpty(name)) {
				map.put("Code", itemDuration.getCode());
				map.put("Name", name);
				result.add(map);
			}
		}
		return result;
	}
	@RequestMapping(value = "/categoryOrBrand", method = RequestMethod.GET)
	@ResponseBody
	@Operation(operationId = "getCategoryOrBrandDetails", summary = "It will return the details of Category or Brand  Based on Code Provided")
	@ApiBaseSiteIdAndUserIdParam
	@ApiResponse(responseCode = "200", description = "CategoryOrBrandWsDTO", content = @Content(schema = @Schema(implementation = CategoryOrBrandWsDTO.class)))
	public CategoryOrBrandWsDTO getCategoryOrBrandDetails(
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
					{"BASIC", "DEFAULT", "FULL"}))
			@RequestParam(defaultValue = "DEFAULT") final String fields, @Parameter(description = "Category Code Used to get the Details") @RequestParam(required = false) final String categoryCode,@Parameter(description = "Brand Code used to get Brand Details") @RequestParam(required = false) final String brandCode) {
		try {

			WholescriptsCategoryOrBrandData wsCategoryOrBranData=wholescriptsCoreFacade.getCategoryOrBrandDetails(categoryCode,brandCode);
			return dataMapper.map(wsCategoryOrBranData, CategoryOrBrandWsDTO.class,fields);
		} catch (final NoSuchElementException e) {
			throw new RequestParameterException(String.format(e.getMessage(), RequestParameterException.INVALID));
		}
	}

	@Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_CLIENT, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
	@Operation(operationId = "consentUpdate", summary = "Allows to use the consent update.", description = " This API request is used to update consent.")
	@PostMapping(value = "/consentUpdate")
	@ApiBaseSiteIdParam
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<String> saveConsentUpdate(@RequestParam String userId,
																 @RequestParam boolean isSmsNotificationEnabled,
																 @RequestParam boolean isEmailNotificationEnabled) {
		try {
			wholescriptsCoreFacade.updateConsent(userId, isSmsNotificationEnabled, isEmailNotificationEnabled);
			return ResponseEntity.ok("Successfully updated consent");
		} catch (IllegalArgumentException e) {
			// Handle specific case where the user is not found
			LOG.error("User not found: {}", e.getMessage());
			return ResponseEntity.ok(e.getMessage());
		}
		catch (Exception e) {
			LOG.error("Error while processing consent update: {}", e.getMessage(), e);
			return ResponseEntity.ok("Consent update not successful");
		}
	}

	@RequestMapping(value = "/users/{userId}/getBestSellerProducts", method = RequestMethod.GET)
	@ResponseBody
	@Operation(operationId = "getBestSellerProducts", summary = "It will return the Best Seller Products")
	@ApiBaseSiteIdAndUserIdParam
	@ApiResponse(responseCode = "200", description = "DashboardProductsWSDTO", content = @Content(schema = @Schema(implementation = DashboardProductsWSDTO.class)))
	public DashboardProductsWSDTO getBestSellerProducts(
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
					{"BASIC", "DEFAULT", "FULL"}))
			@RequestParam(defaultValue = "DEFAULT") final String fields) {
		try {

			DashboardProductsWSDTO dashboardProducts = new DashboardProductsWSDTO();
			DashboardProductsData dashboardProductsData= wholescriptsCoreFacade.getBestSellerProducts();

			List<ProductWsDTO> buyAgainProducts = Optional.ofNullable(dashboardProductsData.getBuyAgainProducts())
					.orElse(Collections.emptyList())
					.stream()
					.map(productData -> dataMapper.map(productData, ProductWsDTO.class, fields))
					.toList();

			List<ProductWsDTO> bestSellerProducts = Optional.ofNullable(dashboardProductsData.getBestSellerProducts())
					.orElse(Collections.emptyList())
					.stream()
					.map(productData -> dataMapper.map(productData, ProductWsDTO.class, fields))
					.toList();

			dashboardProducts.setBuyAgainProducts(buyAgainProducts);
			dashboardProducts.setBestSellerProducts(bestSellerProducts);

			return dashboardProducts;

		} catch (final NoSuchElementException e) {
			throw new RequestParameterException(String.format(e.getMessage(), RequestParameterException.INVALID));
		}
	}

	@Operation(operationId = "Get All Products Data", summary = "This API will get list of all the Products Data")
	@ApiBaseSiteIdParam
	@GetMapping("/KlaviyocatalogFeed")
	public void getProductsData(
			@Parameter(name = "productSkusWsDTO", description = "product SKU'S as part of Request body ") @RequestBody(required = false) final ProductSkusDTO productSkusDTO,
			@Parameter(schema = @Schema(allowableValues = {"BASIC", "DEFAULT", "FULL"})) @RequestParam(defaultValue = "DEFAULT") final String fields, HttpServletResponse response) {

		Map<String, Object> responseData = new HashMap<>();
		List<KlaviyoCatalogFeedWsDTO> allProductsDto = new ArrayList<>();
		try {

			if (productSkusDTO !=null && CollectionUtils.isNotEmpty(productSkusDTO.getProductSkus())) {
				allProductsDto = wholescriptsCoreFacade.getSelectedProductsDataForKlaviyo(productSkusDTO.getProductSkus()).stream()
						.map(productsData -> dataMapper.map(productsData, KlaviyoCatalogFeedWsDTO.class, fields)).toList();
			} else {
				allProductsDto = wholescriptsCoreFacade.getAllProductsForKlaviyo().stream()
						.map(productsData -> dataMapper.map(productsData, KlaviyoCatalogFeedWsDTO.class, fields)).toList();

			}

			String jsonResponse = jsonProcess(allProductsDto);
			response.setContentType("application/json;charset=utf-8");
			response.getOutputStream().write(jsonResponse.getBytes(StandardCharsets.UTF_8));
			response.getOutputStream().close();

		} catch (final Exception ex) {
			responseData.put("success", false);
			responseData.put("error", "Failed to retrieve product data");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Operation(operationId = "Sinch Inbound Response", summary = "This API will be invloked by Sinch webhook")
	@ApiBaseSiteIdParam
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@PostMapping("/sinch/inbound")
	public String getSinchInboundResponse(@Parameter(description = "Sinch request body") @RequestBody String payload) {
		LOG.info("Received Sinch Response");
		LOG.info("Printing Sinch Response:\n{}", payload);

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;

		try {
			payload = payload.replace("\\", "");
			jsonNode = objectMapper.readTree(payload);
		} catch (JsonProcessingException e) {
			LOG.error("Error parsing JSON payload", e);
			return "Invalid JSON Format";
		}

		if (jsonNode != null && jsonNode.has("body") && jsonNode.has("from")) {
			String body = jsonNode.get("body").asText();
			String from = jsonNode.get("from").asText();

			wholescriptsCoreFacade.updateSMSNotification(body, from);
		} else {
			LOG.error("Missing required fields in Sinch response");
			return "Invalid JSON Payload - Missing fields";
		}
		return payload;
	}

	@Secured({"ROLE_TRUSTED_CLIENT"})
	@PostMapping(value = "/users/{userId}/changeUID")
	@ResponseBody
	@Operation(operationId = "changeUID", summary = "It will update the UID of the User")
	@ApiBaseSiteIdAndUserIdParam
	public void changeUID(
			@Parameter(description = "New UID of a customer.", required = true) @RequestParam final String newUID)
			throws DuplicateUidException
	{
		if (!EmailValidator.getInstance().isValid(newUID))
		{
			throw new RequestParameterException("newUID [" + newUID + "] is not a valid e-mail address!",
					RequestParameterException.INVALID, "newUID");
		}
		wholescriptsCoreFacade.changeUid(newUID);
	}

	private String jsonProcess(final Object obj) {

		try {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(obj);
			return json;
		}
		catch (JsonProcessingException e){
			e.printStackTrace();
		}
		return StringUtils.EMPTY;
	}

	protected ProductListWsDTO formatSearchPageDataResult(final String fields,
			final SearchPageData<ProductData> searchPageDataResult, final ProductSearchPageWsDTO result)
	{
		// Map the results into a ProductListWsDTO which is an intermediate WsDTO
		final ProductListWsDTO listData = new ProductListWsDTO();
		List<ProductWsDTO> productListWsDTOList;
		PaginationWsDTO paginationWsDTO;
		if(null != searchPageDataResult) {
			productListWsDTOList = searchPageDataResult.getResults().stream()
					.map(productData -> dataMapper.map(productData, ProductWsDTO.class, fields)).toList();
			listData.setProducts(productListWsDTOList);
			paginationWsDTO = webPaginationUtils.buildPaginationWsDto(searchPageDataResult.getPagination());
			listData.setCurrentPage(paginationWsDTO.getPage());
			listData.setTotalPageCount(paginationWsDTO.getTotalPages());
			listData.setTotalProductCount(paginationWsDTO.getTotalCount().intValue());

			}else {
				productListWsDTOList = result.getProducts();
				listData.setProducts(productListWsDTOList);
				listData.setCurrentPage(result.getPagination().getCurrentPage());
				listData.setTotalPageCount(result.getPagination().getTotalPages());
				listData.setTotalProductCount(result.getPagination().getTotalResults().intValue());
			}
		// Convert pagination and sorting data into PaginationWsDTO'

		return listData;
	}

	/**
	 * Adds pagination field to the 'fields' parameter
	 *
	 * @param fields
	 * @return fields with pagination
	 */
	protected String addPaginationField(final String fields)
	{
		String fieldsWithPagination = fields;

		if (StringUtils.isNotBlank(fieldsWithPagination))
		{
			fieldsWithPagination += ",";
		}
		fieldsWithPagination += "pagination";

		return fieldsWithPagination;
	}
}
