/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.facade;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.AutocompleteSuggestionData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;

import java.util.List;

import com.wholescripts.data.WholescriptsContentData;
import com.wholescripts.data.WholescriptsSearchData;
import com.wholescripts.data.WholescriptsWebinarData;
import com.wholescripts.search.facetdata.WholescriptsSearchPageData;
import com.wholescripts.search.facetdata.WholescriptsWebinarSearchPageData;


/**
 *
 */
public interface WholescriptsSearchFacade<ITEM extends WholescriptsSearchData>
{
	/**
	 * Refine an exiting search. The query object allows more complex queries using facet selection. The SearchStateData
	 * must have been obtained from the results of a call to {@link #textSearch(String)}.
	 *
	 * @param searchState
	 *           the search query object
	 * @param pageableData
	 *           the page to return
	 * @return the search results
	 */
	WholescriptsWebinarSearchPageData<SearchStateData, WholescriptsWebinarData> searchWebinar(SearchStateData searchState,
			PageableData pageableData);

	WholescriptsSearchPageData<SearchStateData, WholescriptsContentData> searchContent(SearchStateData searchState,
			PageableData pageableData);

	/**
	 * Get the auto complete suggestions for the provided input.
	 *
	 * @param input
	 *           the user's input
	 * @return a list of suggested search terms
	 */
	List<AutocompleteSuggestionData> getAutocompleteSuggestions(String input);
	public ProductSearchPageData<SearchStateData, ProductData> SearchPrivateLabel(final SearchStateData searchState,
			final PageableData pageableData);
}