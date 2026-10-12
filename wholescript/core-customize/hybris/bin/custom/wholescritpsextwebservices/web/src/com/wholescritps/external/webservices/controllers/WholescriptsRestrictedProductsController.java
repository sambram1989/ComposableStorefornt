package com.wholescritps.external.webservices.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.wholescripts.core.facade.WholescriptsExternalAPIFacade;
import com.wholescripts.dto.WholescriptsExternalRestrictedProductsData;
import com.wholescripts.dto.WholescriptsExternalRestrictedProductsWsDTO;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vinay
 */
@RestController
@ApiVersion("v2")
@RequestMapping(value = "/{baseSiteId}/catalog")
@Tag(name = "Wholescripts Product Exclusions Inclusions APIs ")
public class WholescriptsRestrictedProductsController {

    @Resource(name = "wholescriptsExternalAPIFacade")
    private WholescriptsExternalAPIFacade wholescriptsExternalAPIFacade;

    @Resource(name = "dataMapper")
    protected DataMapper dataMapper;


    @Operation(operationId = "Get Product Exclustions Data", summary = "This API will get list of Products Exclusions")
    @Cacheable(value = "restrictedExternalProductCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'getProductExclusionsData')")
    @ApiBaseSiteIdParam
    @Secured({"ROLE_EXTERNAL_CLIENT"})
    @GetMapping("/productexclusions")
    public String getProductExclusionsData() {

        final List<String> returnMessage = new ArrayList<String>();
        WholescriptsExternalRestrictedProductsWsDTO response = new WholescriptsExternalRestrictedProductsWsDTO();
        try {
            final WholescriptsExternalRestrictedProductsData resultData = wholescriptsExternalAPIFacade.getRestrictedProductsData();
            response = dataMapper.map(resultData, WholescriptsExternalRestrictedProductsWsDTO.class);
            response.setSuccess(true);
            return jsonProcess(response);
        } catch (final Exception e) {
            e.printStackTrace();
            returnMessage.add(e.getMessage());
            response.setSuccess(false);
            response.setReturnMessage(returnMessage);
        }
        return jsonProcess(response);

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
}
