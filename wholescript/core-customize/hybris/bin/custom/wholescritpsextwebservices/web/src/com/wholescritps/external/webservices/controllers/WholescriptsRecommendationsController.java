package com.wholescritps.external.webservices.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.wholescripts.core.facade.WholescriptsExternalAPIFacade;
import com.wholescripts.data.ExternalRecommendationRequestData;
import com.wholescripts.data.WsExternalRecommendationResponse;
import com.wholescripts.dto.ExternalAPIEMRRecommendationResponseWsDTO;
import com.wholescripts.dto.ExternalAPIEMRRecommendationWsDTO;
import com.wholescripts.dto.OrderHistoryListPractionerResponseWsDTO;
import com.wholescritps.external.webservices.constants.WholescritpsextwebservicesConstants;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sadiq
 */
@RestController
@ApiVersion("v2")
@RequestMapping(value = "/{baseSiteId}")
@Tag(name = "Wholescripts Recommendations APIs ")
public class WholescriptsRecommendationsController {

    @Resource(name = "dataMapper")
    protected DataMapper dataMapper;
    @Resource(name = "wholescriptsExternalAPIFacade")
    private WholescriptsExternalAPIFacade wholescriptsExternalAPIFacade;

    @Secured({"ROLE_EXTERNAL_CLIENT"})
    @RequestMapping(value = "/recommendations", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    @ApiBaseSiteIdParam
    public ExternalAPIEMRRecommendationResponseWsDTO createRecommendation(
            @Parameter(name = "inputWsDTO", description = "Request body parameter")
            @RequestBody final ExternalAPIEMRRecommendationWsDTO recommendationWsDTO, @RequestParam(defaultValue = WholescritpsextwebservicesConstants.DEFAULT_FIELD_SET) final String fields) {

        ExternalAPIEMRRecommendationResponseWsDTO response = new ExternalAPIEMRRecommendationResponseWsDTO();
        final List<String> returnMessage = new ArrayList<String>();
        try {
            response = wholescriptsExternalAPIFacade.createRecommenDation(recommendationWsDTO);
        } catch (final Exception e) {
            e.printStackTrace();
            returnMessage.add(e.getMessage());
            response.setSuccess(false);
            response.setReturnMessage(returnMessage);
        }
        return response;
    }

    @Secured({"ROLE_EXTERNAL_CLIENT"})
    @Operation(operationId = "Get All Recommendations External", summary = "This API will get all external api recommendations.")
    @Cacheable(value = "getExternalRecommendationCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'getExternalRecommendations',#request)")
    @RequestMapping(value = "/recommendations/details", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiBaseSiteIdParam
    public String getExternalRecommendations(@RequestBody final ExternalRecommendationRequestData request,
   		 @RequestParam(defaultValue = WholescritpsextwebservicesConstants.DEFAULT_FIELD_SET) final String fields) {

        WsExternalRecommendationResponse response = new WsExternalRecommendationResponse();
        final List<String> returnMessage = new ArrayList<String>();
        try {
            response = wholescriptsExternalAPIFacade.getRecommendationsForExternal(request);
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