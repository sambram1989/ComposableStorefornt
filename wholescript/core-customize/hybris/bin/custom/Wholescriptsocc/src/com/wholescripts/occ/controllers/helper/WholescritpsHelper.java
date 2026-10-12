/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.occ.controllers.helper;

import com.wholescripts.core.constants.WholescriptscoreConstants;
import com.wholescripts.core.dao.WholescriptsCoreDao;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.ProductSearchFacade;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.enums.SearchQueryContext;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.FilterQueryOperator;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchFilterQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;
import de.hybris.platform.commercewebservicescommons.dto.search.facetdata.ProductCategorySearchPageWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.search.facetdata.ProductSearchPageWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Component;

import com.wholescripts.core.facade.WholescriptsCoreFacade;
import com.wholescripts.core.facade.WholescriptsSearchFacade;
import com.wholescripts.core.facade.WholescriptsShopifySearchFacade;
import com.wholescripts.core.utils.WholescriptsCoreUtils;
import com.wholescripts.data.WholescriptsContentData;
import com.wholescripts.data.WholescriptsSearchData;
import com.wholescripts.data.WholescriptsWebinarData;
import com.wholescripts.dto.WholescriptsContentSearchPageWsDTO;
import com.wholescripts.dto.WholescriptsWebinarSearchPageWsDTO;
import com.wholescripts.dto.WholescriptsWebinarWsDTO;
import com.wholescripts.search.facetdata.WholescriptsSearchPageData;
import com.wholescripts.search.facetdata.WholescriptsWebinarSearchPageData;

import java.util.Objects;
import org.apache.commons.collections.CollectionUtils;


@Component
public class WholescritpsHelper extends AbstractHelper
{
	@Resource(name = "productSearchFacade")
	private ProductSearchFacade<ProductData> productSearchFacade;
	@Resource(name = "wholescriptsSearchFacade")
	private WholescriptsSearchFacade<WholescriptsSearchData> wholescriptsSearchFacade;
	@Resource(name = "solrSearchStateConverter")
	private Converter<SolrSearchQueryData, SearchStateData> solrSearchStateConverter;
	@Resource(name = "wholescriptsCoreFacade")
	private WholescriptsCoreFacade wholescriptsCoreFacade;

	@Resource(name = "wholescriptsCoreDao")
	private WholescriptsCoreDao wholescriptsCoreDao;
	
	@Resource(name = "sessionService")
	private SessionService sessionService;
	
	protected static final int NEXT_TERM = 2;
	private static final int NEXT_FILTER = 2;
	private static final String IDENTIFIER_FIELD_KEY = "code";


	public ProductSearchPageWsDTO searchProducts(final String query, final int currentPage, final int pageSize, final String sort,
			final String fields, final String searchQueryContext)
	{
		final SearchQueryContext context = decodeContext(searchQueryContext);

		final ProductSearchPageData<SearchStateData, ProductData> sourceResult = searchProducts(query, currentPage, pageSize, sort,
				context);
		if (sourceResult instanceof ProductCategorySearchPageData)
		{
			return getDataMapper().map(sourceResult, ProductCategorySearchPageWsDTO.class, fields);
		}

		return getDataMapper().map(sourceResult, ProductSearchPageWsDTO.class, fields);
	}

	protected ProductSearchPageData<SearchStateData, ProductData> searchProducts(final String query, final int currentPage,
			final int pageSize, final String sort, final SearchQueryContext searchQueryContext)
	{
		final SolrSearchQueryData searchQueryData = decodeQuery(query);
		searchQueryData.setSearchQueryContext(searchQueryContext);

		final PageableData pageable = createPageableData(currentPage, pageSize, sort);

		return productSearchFacade.textSearch(solrSearchStateConverter.convert(searchQueryData), pageable);
	}

	protected WholescriptsWebinarSearchPageData<SearchStateData, WholescriptsWebinarData> searchWebinar(final String query,
			final int currentPage,
			final int pageSize, final String sort, final SearchQueryContext searchQueryContext)
	{
		final SolrSearchQueryData searchQueryData = decodeQuery(query);
		searchQueryData.setSearchQueryContext(searchQueryContext);

		final PageableData pageable = createPageableData(currentPage, pageSize, sort);

		return wholescriptsSearchFacade.searchWebinar(solrSearchStateConverter.convert(searchQueryData), pageable);
	}

	protected WholescriptsSearchPageData<SearchStateData, WholescriptsContentData> searchContent(final String query,
			final int currentPage, final int pageSize, final String sort, final SearchQueryContext searchQueryContext)
	{
		final SolrSearchQueryData searchQueryData = decodeQuery(query);
		searchQueryData.setSearchQueryContext(searchQueryContext);
		
		if(StringUtils.isEmpty(searchQueryData.getFreeTextSearch())) {
			for(SolrSearchQueryTermData termData  : searchQueryData.getFilterTerms()) {
				if (termData.getKey().equalsIgnoreCase("contenttype")) {
					
					if(termData.getValue().equalsIgnoreCase("Brand")) {
						searchQueryData.setSort("brandname-asc");
						
					} else if(termData.getValue().equalsIgnoreCase("Category")) {
						searchQueryData.setSort("categoryname-asc");
					}
				}
			}
		}

		final PageableData pageable = createPageableData(currentPage, pageSize, sort);

		return wholescriptsSearchFacade.searchContent(solrSearchStateConverter.convert(searchQueryData), pageable);
	}


	protected SearchQueryContext decodeContext(final String searchQueryContext)
	{
		if (StringUtils.isBlank(searchQueryContext))
		{
			return null;
		}

		try
		{
			return SearchQueryContext.valueOf(searchQueryContext);
		}
		catch (final IllegalArgumentException e)
		{
			throw new RequestParameterException(searchQueryContext + " context does not exist", RequestParameterException.INVALID,
					e);
		}
	}

	public SolrSearchQueryData decodeQuery(String queryString)
	{
		final SolrSearchQueryData searchQuery = new SolrSearchQueryData();
		final List<SolrSearchQueryTermData> filters = new ArrayList<SolrSearchQueryTermData>();

		if (queryString == null)
		{
			return searchQuery;
		}

		boolean isQueryHasQuotes= queryString.startsWith("\"") &&  queryString.endsWith("\"");
		if(isQueryHasQuotes)
		{
			searchQuery.setFreeTextSearch(queryString);
		}
		else {
			final String[] parts = queryString.split(":");
			if (parts.length > 0) {
				searchQuery.setFreeTextSearch(parts[0]);
				if (parts.length > 1) {
					searchQuery.setSort(parts[1]);
				}
			}

			for (int i = NEXT_TERM; i < parts.length; i = i + NEXT_TERM) {
				final SolrSearchQueryTermData term = new SolrSearchQueryTermData();
				term.setKey(parts[i]);
				term.setValue(parts[i + 1]);
				filters.add(term);
			}
		}


		searchQuery.setFilterTerms(filters);

		return searchQuery;
	}


	public WholescriptsWebinarSearchPageWsDTO searchWebinar(final String query, final int currentPage, final int pageSize, final String sort,
			final String fields, final String searchQueryContext)
	{
		final SearchQueryContext context = decodeContext(searchQueryContext);

		final WholescriptsWebinarSearchPageData<SearchStateData, WholescriptsWebinarData> sourceResult = searchWebinar(query,
				currentPage,
				pageSize,
				sort,
				context);

		return getDataMapper().map(sourceResult, WholescriptsWebinarSearchPageWsDTO.class, fields);
	}

	public WholescriptsContentSearchPageWsDTO searchContent(final String query, final int currentPage, final int pageSize,
			final String sort, final String fields, final String searchQueryContext)
	{
		final SearchQueryContext context = decodeContext(searchQueryContext);

		final WholescriptsSearchPageData<SearchStateData, WholescriptsContentData> sourceResult = searchContent(query,
				currentPage, pageSize, sort, context);

		return getDataMapper().map(sourceResult, WholescriptsContentSearchPageWsDTO.class, fields);
	}

	public WholescriptsWebinarWsDTO getFeaturedWebinar(final String fields)
	{
		final WholescriptsWebinarData WholescriptsWebinarData = wholescriptsCoreFacade.getfeaturedWebinar();
		return getDataMapper().map(WholescriptsWebinarData, WholescriptsWebinarWsDTO.class, fields);
	}
	
	public ProductSearchPageWsDTO searchPrivateLabelProducts(final String query, final int currentPage, final int pageSize, final String sort,
			final String fields, final String searchQueryContext)
	{
		final SearchQueryContext context = decodeContext(searchQueryContext);

		final ProductSearchPageData<SearchStateData, ProductData> sourceResult = searchPrivateLabelProducts(query, currentPage, pageSize, sort,
				context);
		if (sourceResult instanceof ProductCategorySearchPageData)
		{
			return getDataMapper().map(sourceResult, ProductCategorySearchPageWsDTO.class, fields);
		}

		return getDataMapper().map(sourceResult, ProductSearchPageWsDTO.class, fields);
	}
	
	protected ProductSearchPageData<SearchStateData, ProductData> searchPrivateLabelProducts(final String query, final int currentPage,
			final int pageSize, final String sort, final SearchQueryContext searchQueryContext)
	{
		final SolrSearchQueryData searchQueryData = decodeQuery(query);
		searchQueryData.setSearchQueryContext(searchQueryContext);

		final PageableData pageable = createPageableData(currentPage, pageSize, sort);

		return wholescriptsSearchFacade.SearchPrivateLabel(solrSearchStateConverter.convert(searchQueryData), pageable);
	}
	
	
	protected List<SolrSearchFilterQueryData> decodeFilters(String filters) {
		
		if (StringUtils.isEmpty(filters)) {
			return Lists.emptyList();
		}

		final String[] parts = filters.split(":");
		final List<SolrSearchFilterQueryData> filterQueries = new ArrayList<>();
		final Map<String, Set<String>> filterMap = new HashMap<>();
		for (int i = 0; i < parts.length; i = i + NEXT_FILTER) {
			final Set<String> values = filterMap.computeIfAbsent(parts[i], k -> new LinkedHashSet<>());
			if (parts.length <= i + 1) {
				throw new RequestParameterException("Invalid filters format. You have to provide filters as '<attributeKey1>:<attributeValue1>:...:<attributeKeyN>:<attributeValueN>'",
						RequestParameterException.INVALID);
			}
			values.addAll(Arrays.asList(parts[i + 1].split(",")));
		}
		filterMap.entrySet().stream().forEach(entry -> {
			final SolrSearchFilterQueryData filter = new SolrSearchFilterQueryData();
			filter.setKey(entry.getKey());
			filter.setValues(entry.getValue());
			filter.setOperator(FilterQueryOperator.OR);
			filterQueries.add(filter);
		});
		
		return filterQueries;
	}
	
	protected void adjustProductsOrderByInput(final List<SolrSearchFilterQueryData> filterList, final String query, final String sort, final ProductSearchPageData<SearchStateData, ProductData> data) {
		
		if (!StringUtils.isEmpty(query) || !StringUtils.isEmpty(sort) || data.getPagination().getNumberOfPages() > 1) {
			return;
		}
		var expectedOrder = getExpectedOrder(filterList);
		if (expectedOrder.isEmpty() ) {
			return;
		}
		final List<ProductData> orderedProducts = new ArrayList<>();

		for(var code : expectedOrder) {
			for(var product : data.getResults()) {
				if (code.equals(product.getCode())) {
					orderedProducts.add(product);
					break;
				}
			}
		}

		data.setResults(orderedProducts);
	}
	
	private List<String> getExpectedOrder(List<SolrSearchFilterQueryData> filterList) {
		List<String> codes = new ArrayList<>();

		for(var filter : filterList) {
			if ((IDENTIFIER_FIELD_KEY).equals(filter.getKey())) {
				codes.addAll(new ArrayList<>(filter.getValues()));
				break;
			}
		}
		return codes;
	}

	public ProductSearchPageWsDTO searchBlueLabelOrMedPaxProducts(String filters, String query, int currentPage, int pageSize, String sort, String fields, String searchQueryContext) {
		
		if (StringUtils.isEmpty(filters)) {
			return searchBlueLabelOrMedPaxProducts(query, currentPage, pageSize, sort, fields, searchQueryContext);
		}
		
		final SearchQueryContext context = decodeContext(searchQueryContext);
		final ProductSearchPageData<SearchStateData, ProductData> sourceResult = searchBlueLabelOrMedPaxProducts(filters, query, currentPage, pageSize, sort,
				context);
		
		if (sourceResult instanceof ProductCategorySearchPageData) {
			return getDataMapper().map(sourceResult, ProductCategorySearchPageWsDTO.class, fields);
		}
		
		if(sessionService.getAttribute(WholescriptscoreConstants.BESTSELLER_SESSION_FIELD) != null) {
			sessionService.removeAttribute(WholescriptscoreConstants.BESTSELLER_SESSION_FIELD);
		}

		return getDataMapper().map(sourceResult, ProductSearchPageWsDTO.class, fields);
	}
	protected ProductSearchPageData<SearchStateData, ProductData> searchBlueLabelOrMedPaxProducts(final String filters, final String query, final int currentPage,
																				 final int pageSize, final String sort, final SearchQueryContext searchQueryContext) {
		SolrSearchQueryData searchQueryData = decodeQuery(query);
		searchQueryData = decodeQueryForBestSellers(query, searchQueryData);
		var filterList = decodeFilters(filters);
		searchQueryData.setFilterQueries(filterList);
		searchQueryData.setSearchQueryContext(searchQueryContext);

		PageableData pageable = createPageableData(currentPage, pageSize, sort, searchQueryData);

		ProductSearchPageData<SearchStateData, ProductData> searchData =  productSearchFacade.textSearch(solrSearchStateConverter.convert(searchQueryData), pageable);
		
		adjustProductsOrderByInput(filterList, query, sort, searchData);
		return searchData;
	}
	protected ProductSearchPageWsDTO searchBlueLabelOrMedPaxProducts(final String query, final int currentPage,
																								final int pageSize, final String sort,final String fields, final String searchQueryContext) {
		final SearchQueryContext context = decodeContext(searchQueryContext);

		final ProductSearchPageData<SearchStateData, ProductData> sourceResult = searchBlueLabelOrMedPaxProducts(query, currentPage, pageSize, sort,
				context);
				
		if (sourceResult instanceof ProductCategorySearchPageData)
		{
			return getDataMapper().map(sourceResult, ProductCategorySearchPageWsDTO.class, fields);
		}

		return getDataMapper().map(sourceResult, ProductSearchPageWsDTO.class, fields);
	}

	protected ProductSearchPageData<SearchStateData, ProductData> searchBlueLabelOrMedPaxProducts(final String query, final int currentPage,
																				 final int pageSize, final String sort, final SearchQueryContext searchQueryContext)
	{
		SolrSearchQueryData searchQueryData = decodeQuery(query);
		searchQueryData = decodeQueryForBestSellers(query, searchQueryData);
		searchQueryData.setSearchQueryContext(searchQueryContext);

		final PageableData pageable = createPageableData(currentPage, pageSize, sort);

		ProductSearchPageData<SearchStateData, ProductData> searchData = productSearchFacade.textSearch(solrSearchStateConverter.convert(searchQueryData), pageable);
				
		return searchData;
	}
	
	public SolrSearchQueryData decodeQueryForBestSellers(final String query, final SolrSearchQueryData searchQuery) {
		
		if (StringUtils.isNotBlank(query) && Objects.nonNull(searchQuery.getSort())) {
			if (searchQuery.getSort().equalsIgnoreCase(WholescriptscoreConstants.BESTSELLER_SORT)) {
				if (CollectionUtils.isNotEmpty(searchQuery.getFilterTerms())) {
					for (SolrSearchQueryTermData filter : searchQuery.getFilterTerms()) {
						if (filter.getKey().equalsIgnoreCase("categoryUrl")) {
							CategoryModel category = wholescriptsCoreDao.getCategoryByCategoryUrl(filter.getValue());
							if (Objects.nonNull(category)) {
								searchQuery.setSort(searchQuery.getSort() + "_" + category.getCode());
								sessionService.setAttribute(WholescriptscoreConstants.BESTSELLER_SESSION_FIELD, category.getCode());
								break;

							}
						}
					}
				}

			}
		} else {
			searchQuery.setSort(WholescriptscoreConstants.BESTSELLER_SORT + "_" + "catalog");
			sessionService.setAttribute(WholescriptscoreConstants.BESTSELLER_SESSION_FIELD, "catalog");
		}
		return searchQuery;
	}
	
	
	protected PageableData createPageableData(final int currentPage, final int pageSize, final String sort, final SolrSearchQueryData searchQueryData) {
		final PageableData pageable = new PageableData();
		pageable.setCurrentPage(currentPage);
		pageable.setPageSize(pageSize);
		if(sort != null && sort.equalsIgnoreCase(WholescriptscoreConstants.BESTSELLER_SORT) 
				&& searchQueryData.getSort() != null 
				&& searchQueryData.getSort().contains(WholescriptscoreConstants.BESTSELLER_SORT)) {
			pageable.setSort(searchQueryData.getSort());
		} else {
			pageable.setSort(sort);
		}
		
		return pageable;
	}
	
	
}
