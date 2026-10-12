package com.wholescripts.occ.controllers;

import com.wholescripts.core.facade.WholescriptsExternalAPIFacade;
import com.wholescripts.dto.ExternalAPIEMRRecommendationItemsWsDTO;
import com.wholescripts.dto.ExternalAPIEMRRecommendationResponseWsDTO;
import com.wholescripts.dto.ExternalAPIEMRRecommendationWsDTO;
import com.wholescripts.occ.constants.WholescriptsoccConstants;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import com.wholescripts.dto.WholescriptsExternalAPIProductListWsDTO;
import com.wholescripts.dto.WholescriptsExternalAPIProductWsDTO;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Sadiq
 */
@RestController
@ApiVersion("v2")
@RequestMapping( value = "/{baseSiteId}")
@Tag(name = "Wholescripts External APIs ")
public class WholescriptsExternalAPIController {

    @Resource(name = "wholescriptsExternalAPIFacade")
    private WholescriptsExternalAPIFacade wholescriptsExternalAPIFacade;

    @Resource(name = "dataMapper")
    protected DataMapper dataMapper;

    @Operation(operationId = "Get All Product Brand Names", summary = "It will get all the Product Brand Names")
    @ApiBaseSiteIdParam
    @GetMapping("/BrandList")
    public List<String> getAllProductsBrandNames() {
        return wholescriptsExternalAPIFacade.getAllProductsBrandNames();
    }

    @Operation(operationId = "Get All Product Categories", summary = "It will get all the Product Categories")
    @ApiBaseSiteIdParam
    @GetMapping("/CategoryList")
    public List<String> getAllProductsCategories() {
        return wholescriptsExternalAPIFacade.getAllProductsCategories();
    }

    @Operation(operationId = "Get All Products Data", summary = "This API will get list of all the Products Data")
    @ApiBaseSiteIdParam
    @GetMapping("/ProductList")
    public WholescriptsExternalAPIProductListWsDTO getProductsData(@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
            { "BASIC", "DEFAULT", "FULL" })) @RequestParam(defaultValue = "DEFAULT") final String fields){
        WholescriptsExternalAPIProductListWsDTO productListWsDTO= new WholescriptsExternalAPIProductListWsDTO();
        List<WholescriptsExternalAPIProductWsDTO> allProductsDto = wholescriptsExternalAPIFacade.getAllProductsData().stream()
                .map(productsData -> dataMapper.map(productsData, WholescriptsExternalAPIProductWsDTO.class, fields)).toList();
        productListWsDTO.setWholescriptsExternalAPIProductList(allProductsDto);
        return productListWsDTO;
    }


    @RequestMapping(value = "/recommendations", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    @ApiBaseSiteIdParam
    public ExternalAPIEMRRecommendationResponseWsDTO createRecommendation(
            @Parameter(name = "inputWsDTO", description = "Request body parameter")
            @RequestBody final ExternalAPIEMRRecommendationWsDTO recommendationWsDTO, @RequestParam(defaultValue = WholescriptsoccConstants.DEFAULT_FIELD_SET) final String fields) {

        ExternalAPIEMRRecommendationResponseWsDTO response = new ExternalAPIEMRRecommendationResponseWsDTO();
        try {
            response=wholescriptsExternalAPIFacade.createRecommenDation(recommendationWsDTO);


        } catch (final Exception e) {

        }
        return response;
    }
}