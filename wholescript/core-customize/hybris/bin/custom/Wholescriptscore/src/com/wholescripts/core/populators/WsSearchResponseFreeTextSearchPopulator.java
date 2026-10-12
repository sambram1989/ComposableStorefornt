/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;


import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchResponse;
import de.hybris.platform.converters.Populator;

import com.wholescripts.search.facetdata.WholescriptsSearchPageData;

/**
 */
public class WsSearchResponseFreeTextSearchPopulator<STATE, ITEM>
		implements Populator<SolrSearchResponse, WholescriptsSearchPageData<STATE, ITEM>>
{
	@Override
	public void populate(final SolrSearchResponse source, final WholescriptsSearchPageData<STATE, ITEM> target)
	{
		target.setFreeTextSearch(source.getRequest().getSearchText());
	}
}
