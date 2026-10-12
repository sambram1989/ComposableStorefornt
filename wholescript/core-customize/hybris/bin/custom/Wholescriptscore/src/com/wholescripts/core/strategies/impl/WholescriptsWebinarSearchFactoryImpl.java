/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.strategies.impl;

import de.hybris.platform.commerceservices.search.solrfacetsearch.data.AutocompleteSuggestion;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;

import com.wholescripts.core.strategies.WholescriptsSearchFactory;
import com.wholescripts.core.strategies.WholescriptsSearchStrategy;
import com.wholescripts.search.facetdata.WholescriptsSearchPageData;


/**
 * Default implementation for {@link WholescriptsWebinarSearchFactoryImpl}.
 */
public class WholescriptsWebinarSearchFactoryImpl<ITEM> implements WholescriptsSearchFactory<ITEM>
{

	private WholescriptsSearchStrategy searchStrategy;

	@Override
	public WholescriptsSearchStrategy<SolrSearchQueryData, ITEM, WholescriptsSearchPageData<SolrSearchQueryData, ITEM>, AutocompleteSuggestion> getSearchStrategy()
	{
		return searchStrategy;
	}

	/**
	 * @param searchStrategy
	 *           the searchStrategy to set
	 */
	public void setSearchStrategy(final WholescriptsSearchStrategy searchStrategy)
	{
		this.searchStrategy = searchStrategy;
	}


}
