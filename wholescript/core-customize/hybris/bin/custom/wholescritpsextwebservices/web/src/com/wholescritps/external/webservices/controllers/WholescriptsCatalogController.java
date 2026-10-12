package com.wholescritps.external.webservices.controllers;

import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.wholescripts.core.facade.WholescriptsExternalAPIFacade;
import com.wholescripts.dto.CustomerSAPIdDTO;
import com.wholescripts.dto.ProductSkusDTO;
import com.wholescripts.dto.WholescriptsExternalAPIProductListWsDTO;
import com.wholescripts.dto.WholescriptsExternalAPIProductWsDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author Sadiq
 */
@RestController
@ApiVersion("v2")
@RequestMapping(value = "/{baseSiteId}/catalog")
@Tag(name = "Wholescripts Catalogs APIs ")
public class WholescriptsCatalogController {

    private static final Logger LOG = LoggerFactory.getLogger(WholescriptsCatalogController.class);

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
    @Cacheable(value = "nonPLExternalProductCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'getProductsData',#productSkusDTO)")
    @ApiBaseSiteIdParam
    @Secured({"ROLE_EXTERNAL_CLIENT"})
    @GetMapping("/ProductList")
    public String getProductsData(@Parameter(name = "productSkusWsDTO", description = "product SKU'S as part of Request body ") @RequestBody(required = false) final ProductSkusDTO productSkusDTO, @Parameter(schema = @Schema(allowableValues = {"BASIC", "DEFAULT", "FULL"})) @RequestParam(defaultValue = "DEFAULT") final String fields) {

        final List<String> returnMessage = new ArrayList<String>();
        final WholescriptsExternalAPIProductListWsDTO productListWsDTO = new WholescriptsExternalAPIProductListWsDTO();
        try {

            final List<WholescriptsExternalAPIProductWsDTO> allProductsDto;

            if (CollectionUtils.isNotEmpty(productSkusDTO.getProductSkus())) {
                allProductsDto = wholescriptsExternalAPIFacade.getSelectedNonPLProductsData(productSkusDTO.getProductSkus()).stream().map(productsData -> dataMapper.map(productsData, WholescriptsExternalAPIProductWsDTO.class, fields)).toList();
            } else {
                allProductsDto = wholescriptsExternalAPIFacade.getAllNonPLProducts().stream().map(productsData -> dataMapper.map(productsData, WholescriptsExternalAPIProductWsDTO.class, fields)).toList();

            }
            productListWsDTO.setWholescriptsExternalAPIProductList(allProductsDto);
            productListWsDTO.setSuccess(true);

        } catch (final Exception ex) {
            ex.printStackTrace();
            productListWsDTO.setSuccess(false);
            returnMessage.add(ex.getMessage());
            productListWsDTO.setReturnMessage(returnMessage);
        }
        return jsonProcess(productListWsDTO);

    }

    @Operation(operationId = "Get Products Data", summary = "This API will get list of Products Data for specific customer")
    @Cacheable(value = "plExternalProductCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'getPLProductsData',#customerSAPIdDTO)")
    @ApiBaseSiteIdParam
    @Secured({"ROLE_EXTERNAL_CLIENT"})
    @GetMapping("/PLProductList")
    public String getPLProductsData(@Parameter(name = "customerIdWsDTO", description = "Customer ID as part of Request body ") @RequestBody final CustomerSAPIdDTO customerSAPIdDTO, @Parameter(schema = @Schema(allowableValues = {"BASIC", "DEFAULT", "FULL"})) @RequestParam(defaultValue = "DEFAULT") final String fields) {

        final List<String> returnMessage = new ArrayList<String>();
        final WholescriptsExternalAPIProductListWsDTO productListWsDTO = new WholescriptsExternalAPIProductListWsDTO();

        if (StringUtils.isBlank(customerSAPIdDTO.getCustomerSAPID())) {
            productListWsDTO.setSuccess(false);
            returnMessage.add("Invalid customersapid");
            productListWsDTO.setReturnMessage(returnMessage);
            return jsonProcess(productListWsDTO);
        }

        try {
            List<WholescriptsExternalAPIProductWsDTO> productsDto = List.of();

            if (StringUtils.isNotEmpty(customerSAPIdDTO.getCustomerSAPID())) {

                productsDto = wholescriptsExternalAPIFacade.getPLProductsData(customerSAPIdDTO.getCustomerSAPID()).stream().map(productsData -> dataMapper.map(productsData, WholescriptsExternalAPIProductWsDTO.class, fields)).toList();
            }
            productListWsDTO.setWholescriptsExternalAPIProductList(productsDto);
            productListWsDTO.setSuccess(true);

        } catch (final Exception ex) {
            ex.printStackTrace();
            productListWsDTO.setSuccess(false);
            returnMessage.add(ex.getMessage());
            productListWsDTO.setReturnMessage(returnMessage);
        }
        return jsonProcess(productListWsDTO);
    }

    private String jsonProcess(final Object obj) {

        try {
            final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            final String json = ow.writeValueAsString(obj);
            return json;
        } catch (final JsonProcessingException e) {
            e.printStackTrace();
        }
        return StringUtils.EMPTY;
    }
}
