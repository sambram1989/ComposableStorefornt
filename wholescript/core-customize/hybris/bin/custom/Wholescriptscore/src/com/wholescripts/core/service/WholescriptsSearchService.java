/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service;

import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;

import com.wholescripts.search.facetdata.WholescriptsSearchPageData;


/**
 *
 */
public interface WholescriptsSearchService<STATE, ITEM, RESULT extends WholescriptsSearchPageData<STATE, ITEM>>
{
	/**
	 * Refine an exiting search. The query object allows more complex queries using facet selection. The SearchQueryData
	 * must have been obtained from the results of a call to either {@link #textSearch(String,PageableData)} or
	 * {@link #categorySearch(String,PageableData)}.
	 *
	 * @param searchQueryData
	 *           the search query object
	 * @param pageableData
	 *           the page to return
	 * @return the search results
	 */
	RESULT searchWebinar(STATE searchQueryData, PageableData pageableData);

	RESULT searchContent(STATE searchQueryData, PageableData pageableData);
	ProductCategorySearchPageData searchPrivateLabel(STATE searchQueryData, PageableData pageableData);
}
