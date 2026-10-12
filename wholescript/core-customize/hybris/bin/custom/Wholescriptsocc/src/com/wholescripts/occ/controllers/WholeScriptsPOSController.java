package com.wholescripts.occ.controllers;

import com.wholescripts.core.facade.WholeScriptsStoreFinderFacade;
import com.wholescripts.data.protocol.ProtocolData;
import com.wholescripts.occ.constants.WholescriptsSecuredAccessConstants;
import de.hybris.platform.b2bocc.v2.controllers.BaseController;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.commerceservices.storefinder.data.StoreFinderSearchPageData;
import de.hybris.platform.commercewebservicescommons.dto.store.StoreFinderSearchPageWsDTO;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.webservicescommons.cache.CacheControl;
import de.hybris.platform.webservicescommons.cache.CacheControlDirective;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.pagination.WebPaginationUtils;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.log4j.Logger;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@CacheControl(directive = CacheControlDirective.PUBLIC, maxAge = 1800)
@RestController
@ApiVersion("v2")
@RequestMapping(value = "/{baseSiteId}/stores")
@Tag(name = "Wholescripts Find  Practitioners ")
public class WholeScriptsPOSController extends BaseController {

    private static final Logger LOG = Logger.getLogger(WholeScriptsPOSController.class);

    private static final String DEFAULT_SEARCH_RADIUS_METRES = "100000.0";
    private static final String DEFAULT_ACCURACY = "0.0";
    protected static final String DEFAULT_PAGE_SIZE = "10";


    @Resource(name = "wholeScriptsStoreFinderFacade")
    private WholeScriptsStoreFinderFacade wholeScriptsStoreFinderFacade;

    @Resource(name = "configurationService")
    private ConfigurationService configurationService;

    @Resource(name = "webPaginationUtils")
    private WebPaginationUtils webPaginationUtils;


    @RequestMapping(value = "/pointOfServiceList",method = RequestMethod.GET)
    @ResponseBody
    @Operation(operationId = "getPractitionerLocations", summary = "Retrieves the store locations for the Practitioner.", description = "Retrieves the store locations that are near the location specified in a query or the latitude and longitude parameters.")
    @ApiBaseSiteIdParam
    public StoreFinderSearchPageWsDTO getStoreLocations(
            @Parameter(description = "Location in plain text. Example: USA or New York City") @RequestParam(required = false) final String address,
            @Parameter(description = "Coordinate that specifies the north-south position of a point on the Earth's surface.") @RequestParam(required = false) final Double latitude,
            @Parameter(description = "Coordinate that specifies the east-west position of a point on the Earth's surface.") @RequestParam(required = false) final Double longitude,
            @Parameter(description = "Current result page. Default value is 0.") @RequestParam(defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
            @Parameter(description = "Number of results returned per page.") @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
            @Parameter(description = "Sorting method applied to the return results.") @RequestParam(defaultValue = "asc") final String sort,
            @Parameter(description = "Radius in meters. Max value: 40075000.0 (Earth's perimeter).") @RequestParam(defaultValue = DEFAULT_SEARCH_RADIUS_METRES) final double radius,
            @Parameter(description = "Accuracy in meters.") @RequestParam(defaultValue = DEFAULT_ACCURACY) final double accuracy,
            @ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields, final HttpServletResponse response)
    {
        StoreFinderSearchPageWsDTO wsDtoResult=new StoreFinderSearchPageWsDTO();
        try{
            final StoreFinderSearchPageData<PointOfServiceData> result= wholeScriptsStoreFinderFacade.locationSearch(address, latitude, longitude, currentPage, pageSize,
                    sort, radius, accuracy, addPaginationField(fields));
            // X-Total-Count header
            if(Objects.nonNull(result))
            {
                wsDtoResult= getDataMapper().map(result, StoreFinderSearchPageWsDTO.class, fields);
                setTotalCountHeader(response, wsDtoResult.getPagination());
            }

            return wsDtoResult;

        } catch (Exception e) {
            LOG.error("Exception occurred While getting Practitioners" + e.getMessage());
            return wsDtoResult;
        }

    }
}
