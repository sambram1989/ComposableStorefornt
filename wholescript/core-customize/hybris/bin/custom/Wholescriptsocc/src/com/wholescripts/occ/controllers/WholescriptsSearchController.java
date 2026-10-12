/**
 *
 */
package com.wholescripts.occ.controllers;

import de.hybris.platform.b2bocc.v2.controllers.BaseController;
import de.hybris.platform.commercewebservicescommons.dto.search.facetdata.ProductSearchPageWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.PaginationWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;

import java.util.List;
import java.util.NoSuchElementException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import com.wholescripts.core.facade.WholescriptsCoreFacade;
import com.wholescripts.data.WholescriptsPresenterData;
import com.wholescripts.data.WholescriptsWebinarData;
import com.wholescripts.dto.WholescriptsContentSearchPageWsDTO;
import com.wholescripts.dto.WholescriptsPresenterWsDTO;
import com.wholescripts.dto.WholescriptsPresentersListDTO;
import com.wholescripts.dto.WholescriptsWebinarSearchPageWsDTO;
import com.wholescripts.dto.WholescriptsWebinarWsDTO;
import com.wholescripts.occ.constants.WholescriptsSecuredAccessConstants;
import com.wholescripts.occ.controllers.helper.WholescritpsHelper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;


/**
 *
 */
@RestController
@Tag(name = "WholescriptsSearch")
@RequestMapping(value = "/{baseSiteId}/wsSearch")
public class WholescriptsSearchController extends BaseController {

	@Resource(name = "wholescritpsHelper")
	private WholescritpsHelper wholescritpsHelper;

	@Resource(name = "wholescriptsCoreFacade")
	protected WholescriptsCoreFacade wholescriptsCoreFacade;

	protected static final String DEFAULT_PAGE_SIZE = "20";
	protected static final String DEFAULT_CURRENT_PAGE = "0";
	protected static final String BASIC_FIELD_SET = FieldSetLevelHelper.BASIC_LEVEL;
	protected static final String DEFAULT_FIELD_SET = FieldSetLevelHelper.DEFAULT_LEVEL;
	protected static final String HEADER_TOTAL_COUNT = "X-Total-Count";
	protected static final String INVALID_REQUEST_BODY_ERROR_MESSAGE = "Request body is invalid or missing";

	@Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
	@RequestMapping(value = "/webinar", method = RequestMethod.GET)
	@ResponseBody
	@Operation(operationId = "getwebinars", summary = "Retrieves a list of webinars.", description = "Retrieves a list of webinars and related webinar search data, such as available facets, available sorting, and spelling suggestions."
			+ " To enable spelling suggestions, you need to have indexed properties configured to be used for spell checking.")
	@ApiBaseSiteIdParam
	public WholescriptsWebinarSearchPageWsDTO getWebinars(
			@Parameter(description = "Formatted query string. It contains query criteria like free text search, facet. The format is <freeTextSearch>:<sort>:<facetKey1>:<facetValue1>:...:<facetKeyN>:<facetValueN>.")
			@RequestParam(required = false) final String query, @Parameter(description = "Current result page. Default value is 0.")
			@RequestParam(defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage, @Parameter(description = "Number of results returned per page.")
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) final int pageSize, @Parameter(description = "Sorting method applied to the return results.")
			@RequestParam(required = false) final String sort,
			@Parameter(description = "Name of the search query template to be used in the search query. Examples: DEFAULT, SUGGESTIONS.")
			@RequestParam(required = false) final String searchQueryContext, @ApiFieldsParam
			@RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields, final HttpServletResponse response) {
		final WholescriptsWebinarSearchPageWsDTO result = wholescritpsHelper.searchWebinar(query, currentPage, pageSize, sort,
				addPaginationField(fields), searchQueryContext);
		setTotalCountHeader(response, result.getPagination());
		return result;
	}

	@RequestMapping(value = "/content", method = RequestMethod.GET)
	@ResponseBody
	@Operation(operationId = "getcontents", summary = "Retrieves a list of content.", description = "Retrieves a list of content and related content search data, such as available facets, available sorting, and spelling suggestions."
			+ " To enable spelling suggestions, you need to have indexed properties configured to be used for spell checking.")
	@ApiBaseSiteIdParam
	public WholescriptsContentSearchPageWsDTO getContents(
			@Parameter(description = "Formatted query string. It contains query criteria like free text search, facet. The format is <freeTextSearch>:<sort>:<facetKey1>:<facetValue1>:...:<facetKeyN>:<facetValueN>.")
			@RequestParam(required = false)
			final String query, @Parameter(description = "Current result page. Default value is 0.")
			@RequestParam(defaultValue = DEFAULT_CURRENT_PAGE)
			final int currentPage, @Parameter(description = "Number of results returned per page.")
			@RequestParam(defaultValue = DEFAULT_PAGE_SIZE)
			final int pageSize, @Parameter(description = "Sorting method applied to the return results.")
			@RequestParam(required = false)
			final String sort,
			@Parameter(description = "Name of the search query template to be used in the search query. Examples: DEFAULT, SUGGESTIONS.")
			@RequestParam(required = false)
			final String searchQueryContext, @ApiFieldsParam
			@RequestParam(defaultValue = DEFAULT_FIELD_SET)
			final String fields, final HttpServletResponse response)
	{
		final WholescriptsContentSearchPageWsDTO result = wholescritpsHelper.searchContent(query, currentPage, pageSize, sort,
				addPaginationField(fields), searchQueryContext);
		setTotalCountHeader(response, result.getPagination());
		return result;
	}

	@Operation(operationId = "getFeatured", summary = "Allows get Featured in Webinar List page", description = " This API request is get advertising and API resource")
	@GetMapping(value = "/getFeatured")
	@ResponseBody
	@ApiBaseSiteIdParam
	public WholescriptsWebinarWsDTO getFeatured(@RequestParam(defaultValue = FieldSetLevelHelper.DEFAULT_LEVEL) final String fields) {
		return wholescritpsHelper.getFeaturedWebinar(fields);
	}


	/**
	 * Adds pagination field to the 'fields' parameter
	 *
	 * @param fields
	 * @return fields with pagination
	 */
	@Override
	protected String addPaginationField(final String fields) {
		String fieldsWithPagination = fields;

		if (StringUtils.isNotBlank(fieldsWithPagination)) {
			fieldsWithPagination += ",";
		}
		fieldsWithPagination += "pagination";

		return fieldsWithPagination;
	}


	@Override
	protected void setTotalCountHeader(final HttpServletResponse response, final PaginationWsDTO paginationDto) {
		if (paginationDto != null && paginationDto.getTotalResults() != null) {
			response.setHeader(HEADER_TOTAL_COUNT, String.valueOf(paginationDto.getTotalResults()));
		}
	}

	@Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
	@RequestMapping(value = "/webinars/{webinarCode}", method = RequestMethod.GET)
	@ResponseBody
	@Operation(operationId = "getWebinarDetails", summary = "It will return the details of Webinar Based on Webinar Code")
	@ApiBaseSiteIdAndUserIdParam
	@ApiResponse(responseCode = "200", description = "WebinarWsDTO", content = @Content(schema = @Schema(implementation = WholescriptsWebinarWsDTO.class)))
	public WholescriptsWebinarWsDTO getWebinarDetails(
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
					{"BASIC", "DEFAULT", "FULL"}))
			@RequestParam(defaultValue = "DEFAULT") final String fields, @PathVariable(required = true) final String webinarCode) {
		try {
			final WholescriptsWebinarData webinarData= wholescriptsCoreFacade.getWebinarDetails(webinarCode);
			return getDataMapper().map(webinarData,WholescriptsWebinarWsDTO.class,fields);

		} catch (final NoSuchElementException e) {
			throw new RequestParameterException(String.format(e.getMessage(), RequestParameterException.INVALID));
		}

    }
	@Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
	@RequestMapping(value = "/presenters/{presenterCode}", method = RequestMethod.GET)
	@ResponseBody
	@Operation(operationId = "getPresenterDetails", summary = "It will return the details of Presenter Based on Presenter Code")
	@ApiBaseSiteIdAndUserIdParam
	@ApiResponse(responseCode = "200", description = "PresenterWsDTO", content = @Content(schema = @Schema(implementation = WholescriptsPresenterWsDTO.class)))
	public WholescriptsPresenterWsDTO getPresenterDetails(
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
					{"BASIC", "DEFAULT", "FULL"}))
			@RequestParam(defaultValue = "DEFAULT") final String fields, @PathVariable(required = true) final String presenterCode) {
		try {

			final WholescriptsPresenterData webinarData= wholescriptsCoreFacade.getPresenterDetails(presenterCode);
			return getDataMapper().map(webinarData, WholescriptsPresenterWsDTO.class,fields);

		} catch (final NoSuchElementException e) {
			throw new RequestParameterException(String.format(e.getMessage(), RequestParameterException.INVALID));
		}

    }
	@Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
	@RequestMapping(value = "/presenters", method = RequestMethod.GET)
	@ResponseBody
	@Operation(operationId = "getAllPresenterDetails", summary = "It will return the details of 8  Presenters ")
	@ApiBaseSiteIdAndUserIdParam
	@ApiResponse(responseCode = "200", description = "PresentersListWsDTO", content = @Content(schema = @Schema(implementation = WholescriptsPresentersListDTO.class)))
	public WholescriptsPresentersListDTO getAllPresenterDetails(
			@Parameter(description = "Response configuration. This is the list of fields that should be returned in the response body. Examples: BASIC, DEFAULT, FULL.", schema = @Schema(allowableValues =
					{"BASIC", "DEFAULT", "FULL"}))
			@RequestParam(defaultValue = "DEFAULT") final String fields) {
		final WholescriptsPresentersListDTO listData = new WholescriptsPresentersListDTO();
		try {
			final List<WholescriptsPresenterData> webinarListData= wholescriptsCoreFacade.getAllPresenterDetails();
			if(CollectionUtils.isNotEmpty(webinarListData))
			{
				final List<WholescriptsPresenterWsDTO> presenterWsDTO =webinarListData.stream().map(webinarData -> getDataMapper().map(webinarData, WholescriptsPresenterWsDTO.class, fields)).toList();
				listData.setPresentersList(presenterWsDTO);
			}

		} catch (final NoSuchElementException e) {
			throw new RequestParameterException(String.format(e.getMessage(), RequestParameterException.INVALID));
		}

		return listData;
	}

	@Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
	@RequestMapping(value = "/search/private-label", method = RequestMethod.GET)
	@ResponseBody
	@Operation(operationId = "getPrivateLabelProducts", summary = "Retrieves a list of private label products.", description =
			"Retrieves a list of private label products and related product search data, such as available facets, available sorting, and spelling suggestions."
			+ " To enable spelling suggestions, you need to have indexed properties configured to be used for spell checking.")
	@ApiBaseSiteIdParam
	public ProductSearchPageWsDTO getPrivateLabelProducts(
			@Parameter(description = "Formatted query string. It contains query criteria like free text search, facet. The format is <freeTextSearch>:<sort>:<facetKey1>:<facetValue1>:...:<facetKeyN>:<facetValueN>.") @RequestParam(required = false) final String query,
			@Parameter(description = "Current result page. Default value is 0.") @RequestParam(defaultValue = DEFAULT_CURRENT_PAGE) final int currentPage,
			@Parameter(description = "Number of results returned per page.") @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) final int pageSize,
			@Parameter(description = "Sorting method applied to the return results.") @RequestParam(required = false) final String sort,
			@Parameter(description = "Name of the search query template to be used in the search query. Examples: DEFAULT, SUGGESTIONS.") @RequestParam(required = false) final String searchQueryContext,
			@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields, final HttpServletResponse response)
	{
		final ProductSearchPageWsDTO result = wholescritpsHelper.searchPrivateLabelProducts(query, currentPage, pageSize, sort,
				addPaginationField(fields), searchQueryContext);
		setTotalCountHeader(response, result.getPagination());
		return result;
	}
	@Secured({WholescriptsSecuredAccessConstants.ROLE_CUSTOMERGROUP, WholescriptsSecuredAccessConstants.ROLE_GUEST, WholescriptsSecuredAccessConstants.ROLE_CUSTOMERMANAGERGROUP, WholescriptsSecuredAccessConstants.ROLE_TRUSTED_CLIENT})
		@PostMapping(value="/updateWatchStatus")
	@Operation(operationId = "updateVideoWatchStatus", summary = "It will update whether video watched or not for  Webinar  Code")
	@ApiBaseSiteIdParam
	public String updateVideoWatchedStatus(
			 @Parameter(required = true) final String userId, @Parameter(required = true) final String webinarCode) {
		try {
			wholescriptsCoreFacade.updateVideoWatchStatus(userId,webinarCode);
			return "Status Updated Successfully";

		} catch (final NoSuchElementException e) {
			throw new RequestParameterException(String.format(e.getMessage(), RequestParameterException.INVALID));
		}

	}

}
