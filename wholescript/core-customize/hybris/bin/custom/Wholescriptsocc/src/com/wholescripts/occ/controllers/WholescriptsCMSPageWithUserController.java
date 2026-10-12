package com.wholescripts.occ.controllers;

import de.hybris.platform.cms2.constants.Cms2Constants;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.exceptions.InvalidPageQualifierException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cmsfacades.data.AbstractPageData;
import de.hybris.platform.cmsfacades.exception.ValidationException;
import de.hybris.platform.cmsfacades.pages.PageFacade;
import de.hybris.platform.cmsocc.data.CMSPageWsDTO;
import de.hybris.platform.cmsocc.jaxb.adapters.pages.PageAdapterUtil;
import de.hybris.platform.cmsocc.jaxb.adapters.pages.PageWsDTOAdapter;
import de.hybris.platform.cmsocc.mapping.CMSDataMapper;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.commerceservices.request.mapping.annotation.RequestMappingOverride;
import de.hybris.platform.commercewebservicescommons.annotation.SecurePortalUnauthenticatedAccess;
import de.hybris.platform.webservicescommons.cache.CacheControl;
import de.hybris.platform.webservicescommons.cache.CacheControlDirective;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.pagination.WebPaginationUtils;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.wholescripts.core.facade.WholescriptsPageFacade;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.Map;

@Controller
@CacheControl(directive = CacheControlDirective.PRIVATE)
@ApiVersion("v2")
@Tag(name = "Wholescripts Pages")
@RequestMapping(value = "/{baseSiteId}/users/{userId}/cms")
public class WholescriptsCMSPageWithUserController {

    public static final String DEFAULT_CURRENT_PAGE = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    @Resource(name = "cmsDataMapper")
    protected CMSDataMapper dataMapper;
    @Resource(name = "cmsPageFacade")
    private PageFacade pageFacade;
    @Resource(name = "webPaginationUtils")
    private WebPaginationUtils webPaginationUtils;
    @Resource(name = "wholescriptsPageFacade")
    private WholescriptsPageFacade wholescriptsPageFacade;


    @SecurePortalUnauthenticatedAccess
    @GetMapping(value ="/pages")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @Operation(summary = "Retrieves the page data and the CMS content slots.", //
            description = "Retrieves the page data and CMS content slots using the pageLabel/pageId or the code parameter. "
                    + "If you don't provide a value for one of these parameters, the homepage CMS content slots are retrieved. "
                    + "\nContent pages can be filtered using pageLabel/pageId while the other page types can be filtered using the specified code.",//
            operationId = "getPage")
    @ApiBaseSiteIdParam
    @Parameter(name = "pageLabel", description =
            "Page label. If no page has a label that matches the page label exactly, try to find pages whose label starts with a section of the page label.\n"
                    + "Note: URL encoding on the page label should be applied when the label contains special characters. When pageLabel is provided, pageLabelOrId will be ignored.", example = "/cart", schema = @Schema(type = "string"), in = ParameterIn.QUERY)
    @Parameter(name = "pageId", description = "Page id. When pageId is provided, pageLabelOrId will be ignored.", example = "cartPage", schema = @Schema(type = "string"), in = ParameterIn.QUERY)
    @ApiResponse(responseCode = "400", description = "Should not pass both pageLabel and pageId simultaneously. (InvalidPageQualifierException)")
    @ApiResponse(responseCode = "200", description = "CMSPageWsDTO", content = @Content(schema = @Schema(implementation = CMSPageWsDTO.class)))
    @RequestMappingOverride(priorityProperty = "cmsocc.wholescripts.Page.Pages.priority")
    public PageAdapterUtil.PageAdaptedData getPage(
            @Parameter(description = "Page type", schema = @Schema(allowableValues = {"ContentPage", "ProductPage", "CategoryPage", "CatalogPage"}))
            //
            @RequestParam(required = true, defaultValue = ContentPageModel._TYPECODE) final String pageType,
            @Parameter(description = "Page Label or Id. If no page has a label that matches the page label exactly, try to find pages whose label starts with a section of the page label. Otherwise, try to find the page by id.\n"
                    + "When label and ID values can match, passing in the pageId parameter may result in obtaining variation pages with the same pageLabel but different pageId. Therefore, it is advisable to use both the pageId and pageLabel parameters to ensure accuracy."
                    + "Note: URL encoding on the page label should be applied when the label contains special characters.", example = "/cart", schema = @Schema(deprecated = true))
            //
            @RequestParam(required = false) final String pageLabelOrId,
            @Parameter(description = "Page code. Examples: homepage, electronics, cameras, 585.") //
            @RequestParam(required = false) final String code,
            @RequestParam(required = false) final String url,
            @Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues = {"BASIC", "DEFAULT", "FULL"}))
            //
            @RequestParam(defaultValue = "DEFAULT") final String fields) throws CMSItemNotFoundException {
        try
        {
            final String pageLabel;
            final String pageId;
            AbstractPageData pageData;
            final ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (servletRequestAttributes != null) {
                HttpServletRequest request = servletRequestAttributes.getRequest();
                pageLabel = request.getParameter("pageLabel");
                pageId = request.getParameter("pageId");
            } else {
                pageLabel = null;
                pageId = null;
            }
            final String finalUsedPageLabelOrId = getFinalUsedPageLabelOrId(pageLabelOrId, pageLabel, pageId);
            if(StringUtils.isNotEmpty(url)){
                pageData = wholescriptsPageFacade.getPageforUrlData(pageType, finalUsedPageLabelOrId, url);
            } else {
                pageData = pageFacade.getPageData(pageType, finalUsedPageLabelOrId, code);
            }
            final CMSPageWsDTO page = (CMSPageWsDTO) dataMapper.map(pageData, fields);
            Map<String, Object> keyWordsMap=new HashMap<>();
            if(StringUtils.isNotEmpty(page.getKeywords()))
            {
                keyWordsMap.put("keywords",page.getKeywords());
                page.setOtherProperties(keyWordsMap);
            }
            Map<String, Object> propertiesWordsMap=new HashMap<>();
            if(StringUtils.isNotEmpty(page.getProperties()))
            {
                propertiesWordsMap.put("properties",page.getProperties());
                page.setOtherProperties(propertiesWordsMap);
            }
            final PageWsDTOAdapter adapter = new PageWsDTOAdapter();
            return adapter.marshal(page);
        }
        catch (final ValidationException e)
        {
            throw new WebserviceValidationException(e.getValidationObject());
        }
    }

    private String getFinalUsedPageLabelOrId(final String pageLabelOrId, final String pageLabel,
                                             final String pageId)
            throws UnsupportedOperationException {
        String finalUsedPageLabelOrId = pageLabelOrId;
        if (pageLabel != null && pageId != null) {
            throw new InvalidPageQualifierException("Should not pass both pageLabel and pageId simultaneously.");
        } else if (pageId != null) {
            finalUsedPageLabelOrId = Cms2Constants.PAGE_ID_PREFIX + pageId;
        } else if (pageLabel != null) {
            finalUsedPageLabelOrId = Cms2Constants.PAGE_LABEL_PREFIX + pageLabel;
        }
        return finalUsedPageLabelOrId;
    }
}


