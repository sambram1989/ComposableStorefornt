/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.strategies;

import de.hybris.platform.commerceservices.enums.SearchQueryContext;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.AutocompleteSuggestion;

import java.util.List;
import java.util.Set;

import com.wholescripts.search.facetdata.WholescriptsSearchPageData;


/**
 *
 */
public interface WholescriptsSearchStrategy<STATE, ITEM, SEARCH_RESULT extends WholescriptsSearchPageData<STATE, ITEM>, AUTOCOMPLETE_RESULT extends AutocompleteSuggestion>
{
	/**
	 * Initiate a new search using simple free text query.
	 *
	 * @param text
	 *           the search text
	 * @param pageableData
	 *           the page to return, can be null to use defaults
	 * @return the search results
	 */
	SEARCH_RESULT textSearch(String text, PageableData pageableData);

	/**
	 * Initiate a new search using simple free text query in a search query context.
	 *
	 * @param text
	 *           the search text
	 * @param searchQueryContext
	 *           search query context
	 * @param pageableData
	 *           the page to return, can be null to use defaults
	 * @return the search results
	 */
	SEARCH_RESULT textSearch(String text, SearchQueryContext searchQueryContext, PageableData pageableData);

	/**
	 * Refine an exiting search. The query object allows more complex queries using facet selection. The SearchQueryData
	 * must have been obtained from the results of a call to either {@link #textSearch(String, PageableData)} or
	 * {@link #categorySearch(String, PageableData)}.
	 *
	 * @param searchQueryData
	 *           the search query object
	 * @param pageableData
	 *           the page to return
	 * @return the search results
	 */
	SEARCH_RESULT searchAgain(STATE searchQueryData, PageableData pageableData);

	/**
	 * Get the auto complete suggestions for the input provided.
	 *
	 * @param input
	 *           the user's input on which the autocomplete is based
	 * @return a list of suggested search terms
	 */
	List<AUTOCOMPLETE_RESULT> getAutocompleteSuggestions(String input);

	/**
	 * Get the index types for baseSiteId, catalogId and catalogversion
	 *
	 * @param baseSiteId
	 *           the base site identifier
	 * @param catalogId
	 *           the catalog identifier
	 * @param catalogVersion
	 *           the catalog version
	 * @return a list of suggested search terms
	 */
	Set<String> getIndexTypes(String baseSiteId, String catalogId, String catalogVersion);
}
