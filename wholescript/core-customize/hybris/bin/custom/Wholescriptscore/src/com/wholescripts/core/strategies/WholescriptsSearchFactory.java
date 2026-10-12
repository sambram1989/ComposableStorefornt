/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.strategies;

import de.hybris.platform.commerceservices.search.ProductSearchStrategy;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.AutocompleteSuggestion;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;

import com.wholescripts.search.facetdata.WholescriptsSearchPageData;


/**
 * ProductSearchStrategyFactory interface. Implementations of this interface are responsible for getting the applicable instance of {@link ProductSearchStrategy}.
 *
 * @param <ITEM> The type of items returned as part of the search results of specific
 *               {@link de.hybris.platform.commerceservices.search.ProductSearchStrategy}. For example
 *               {@link de.hybris.platform.commerceservices.search.solrfacetsearch.impl.DefaultSolrFacetSearchProductSearchStrategy}
 */
public interface WholescriptsSearchFactory<ITEM>
{

	/**
	 * Returns applicable instance of {@link ProductSearchStrategy}.
	 *
	 * @return product search strategy
	 */
	WholescriptsSearchStrategy<SolrSearchQueryData, ITEM, WholescriptsSearchPageData<SolrSearchQueryData, ITEM>, AutocompleteSuggestion> getSearchStrategy();
}
