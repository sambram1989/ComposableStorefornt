package com.wholescripts.occ.controllers;

import com.wholescripts.data.IngredientData;
import de.hybris.platform.acceleratorfacades.order.data.PriceRangeData;
import de.hybris.platform.b2bocc.v2.controllers.BaseController;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.PromotionData;
import de.hybris.platform.commercefacades.product.data.ReviewData;
import de.hybris.platform.commercefacades.product.data.StockData;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.commerceservices.request.mapping.annotation.RequestMappingOverride;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.facetdata.ProductSearchPageWsDTO;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.webservicescommons.cache.CacheControl;
import de.hybris.platform.webservicescommons.cache.CacheControlDirective;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.wholescripts.core.exception.WholescriptsPageNotFoundException;
import com.wholescripts.core.facade.WholescriptsProductsFacade;
import com.wholescripts.dto.IngredientListWsDTO;
import com.wholescripts.dto.IngredientWsDTO;
import com.wholescripts.dto.WholescriptsProductFamilyListWsDTO;
import com.wholescripts.dto.WholescriptsProductFamilyWsDTO;
import com.wholescripts.occ.controllers.helper.WholescritpsHelper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@Tag(name = "Wholescripts Products Controller")
@ApiVersion("v2")
@RequestMapping(value = "/{baseSiteId}/products")
public class WholescritpsProductsController extends BaseController {

   @Resource(name = "dataMapper")
   protected DataMapper dataMapper;

   @Resource(name = "wholescriptsProductsFacade")
   protected WholescriptsProductsFacade wholescriptsProductsFacade;

   @Resource(name = "wholescritpsHelper")
   private WholescritpsHelper wholescritpsHelper;
    
   @Resource(name = "configurationService")
   private ConfigurationService configurationService;
    
   private static final Logger LOG = LoggerFactory.getLogger(WholescritpsProductsController.class);
   private static final String CONSIDER_FIELDS_KEY = "toggle.occ.retrieving.product.performance.improvement.enabled";
   private static final String INVALID_CODE_FOR_PDP = "product code: '%s' is not valid for detail page";
   
   @RequestMapping(value = "/{productCode}", method = RequestMethod.GET)
 	@CacheControl(directive = CacheControlDirective.PRIVATE, maxAge = 120)
 	@Cacheable(value = "productCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.ProductCacheKeyGenerator).generateKey(true,true,#productCode,#fields)")
 	@RequestMappingOverride
   @ResponseBody
 	@Operation(operationId = "getProduct", summary = "Retrieves product details.", description = "Retrieves the details of a single product using the product identifier.")
 	@ApiBaseSiteIdParam
 	public ProductWsDTO getProduct(
 			@Parameter(description = "Product identifier.", required = true) @PathVariable final String productCode,
 			@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields) throws WholescriptsPageNotFoundException {
   	try {
   		boolean isConsiderFields = configurationService.getConfiguration().getBoolean(CONSIDER_FIELDS_KEY, true);
    		Collection<ProductOption> options = isConsiderFields ? extractProductOptions(fields):EnumSet.allOf(ProductOption.class);
    		if(LOG.isDebugEnabled()) {
    			LOG.debug("getProduct: code={} | options={}", sanitize(productCode), options);
    		}

    		final ProductData product = wholescriptsProductsFacade.getProductForCodeAndOptions(productCode, options);
    		return getDataMapper().map(product, ProductWsDTO.class, fields);
   		
   	} catch (final Exception e) {
			throw new WholescriptsPageNotFoundException(String.format(INVALID_CODE_FOR_PDP, productCode));
		}
 		
 	}


    @Operation(operationId = "Get All Product Families Data", summary = "This API will get the list of all Product Families Data")
    @ApiBaseSiteIdParam
    @GetMapping("/product-family")
    public WholescriptsProductFamilyListWsDTO getProductFamiliesData(
            @Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.",
                    schema = @Schema(allowableValues = { "BASIC", "DEFAULT", "FULL" })) @RequestParam(defaultValue = "DEFAULT") final String fields,final String productCode) {

        WholescriptsProductFamilyListWsDTO productFamilyListWsDTO = new WholescriptsProductFamilyListWsDTO();
        List<WholescriptsProductFamilyWsDTO> productFamiliesDto = wholescriptsProductsFacade.getAllProductFamilies(productCode).stream().map(productFamilyData -> dataMapper.map(productFamilyData, WholescriptsProductFamilyWsDTO.class, fields))
                .toList();

        productFamilyListWsDTO.setWholescriptsProductFamilyList(productFamiliesDto);
        return productFamilyListWsDTO;
    }

    @GetMapping("/{productId}/product-details")
    @Operation(operationId = "getProductByCode", summary = "Get product by code (uuid)", description = "Endpoint to retrieve a product that matches the given product code uuid.")
    @ApiResponse(responseCode = "200", description = "Product data")
    @ApiBaseSiteIdParam
    public ProductWsDTO getProductDetailsByCode(
            @Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.",
                    schema = @Schema(allowableValues = {"BASIC", "DEFAULT", "FULL"}))
            @RequestParam(defaultValue = "DEFAULT") final String fields,
            @PathVariable final String productId) {

        final ProductData productData = wholescriptsProductsFacade.getProductDetailsByCode(productId);
        return dataMapper.map(productData, ProductWsDTO.class, fields);
    }

    @GetMapping("/supplement-facts")
    @Operation(operationId = "getSupplementFactsByProductCodes", summary = "Get supplement facts by product codes", description = "Endpoint to retrieve supplement facts for multiple products by their codes")
    @ApiResponse(responseCode = "200", description = "Supplement facts data")
    public List<IngredientListWsDTO> getSupplementFactsByProductCodes(
            @Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.",
                    schema = @Schema(allowableValues = {"BASIC", "DEFAULT", "FULL"}))
            @RequestParam(defaultValue = "DEFAULT") final String fields,
            @RequestParam List<String> productIds) {

		List<IngredientData> aggregatedList = wholescriptsProductsFacade.getIngredientsByProductCode(productIds);

		IngredientListWsDTO ingredientListWsDTO = new IngredientListWsDTO();
		ingredientListWsDTO.setIngredientList(aggregatedList.stream().map(data -> dataMapper.map(data, IngredientWsDTO.class, fields))
						.collect(Collectors.toList())
		);

		return List.of(ingredientListWsDTO);
        }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    @RequestMappingOverride(priorityProperty = "occ.wholescripts.products.priority")
    @Operation(operationId = "getProducts", summary = "Retrieves a list of products.", description =
            "Retrieves a list of products and related product search data, such as available facets, available sorting, and spelling suggestions."
                    + " To enable spelling suggestions, you need to have indexed properties configured to be used for spell checking.")
    @ApiBaseSiteIdParam
    public ProductSearchPageWsDTO getProducts(@Parameter(description = "Filter conditions that combine attribute keys and attribute values. The attributes are the fields or properties for the Product type configured in the search index type."
            + " The format is <attributeKey1>:<attributeValue1>:...:<attributeKeyN>:<attributeValueN>.") @RequestParam(required = false) final String filters,
            @Parameter(description = "Formatted query string. It contains query criteria like free text search, facet. The format is <freeTextSearch>:<sort>:<facetKey1>:<facetValue1>:...:<facetKeyN>:<facetValueN>.") @RequestParam(required = false) final String query,
            @Parameter(description = "Current result page. Default value is 0.") @RequestParam(defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
            @Parameter(description = "Number of results returned per page.") @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
            @Parameter(description = "Sorting method applied to the return results.") @RequestParam(required = false) final String sort,
            @Parameter(description = "Name of the search query template to be used in the search query. Examples: DEFAULT, SUGGESTIONS.") @RequestParam(required = false) final String searchQueryContext,
            @ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields, final HttpServletResponse response)
    {
        final ProductSearchPageWsDTO result = wholescritpsHelper.searchBlueLabelOrMedPaxProducts(filters,query, currentPage, pageSize, sort,
                addPaginationField(fields), searchQueryContext);
        setTotalCountHeader(response, result.getPagination());
        return result;
    }
    
    protected Collection<ProductOption> extractProductOptions(final String fields) {
 		final ProductData tempProductData = new ProductData();
 		tempProductData.setImages(Lists.newArrayList(new ImageData()));
 		tempProductData.setReviews(Lists.newArrayList(new ReviewData()));
 		tempProductData.setNumberOfReviews(Integer.valueOf(0));
 		tempProductData.setPotentialPromotions(Lists.newArrayList(new PromotionData()));
 		tempProductData.setPrice(new PriceData());
 		tempProductData.setPurchasable(Boolean.FALSE);
 		tempProductData.setPriceRange(new PriceRangeData());
 		tempProductData.setStock(new StockData());

 		final ProductWsDTO productWsDTO = getDataMapper().map(tempProductData, ProductWsDTO.class, fields);
 		final boolean skipImages = CollectionUtils.isEmpty(productWsDTO.getImages());
 		final EnumSet<ProductOption> options = EnumSet.allOf(ProductOption.class);
 		if (skipImages)
 		{
 			options.remove(ProductOption.IMAGES);
 			options.remove(ProductOption.GALLERY);
 		}
 		final boolean skipReviews = CollectionUtils.isEmpty(productWsDTO.getReviews()) && productWsDTO.getNumberOfReviews() == null;
 		if (skipReviews)
 		{
 			options.remove(ProductOption.REVIEW);
 		}
 		final boolean skipPromotions = CollectionUtils.isEmpty(productWsDTO.getPotentialPromotions());
 		if (skipPromotions)
 		{
 			options.remove(ProductOption.PROMOTIONS);
 		}
 		final boolean skipPrice = productWsDTO.getPrice() == null && productWsDTO.getPurchasable() == null;
 		if (skipPrice)
 		{
 			options.remove(ProductOption.PRICE);
 		}
 		final boolean skipPriceRange = productWsDTO.getPriceRange() == null;
 		if (skipPriceRange)
 		{
 			options.remove(ProductOption.PRICE_RANGE);
 		}
 		final boolean skipStock = productWsDTO.getStock() == null;
 		if (skipStock)
 		{
 			options.remove(ProductOption.STOCK);
 		}
 		return options;
 	}
    
}




