/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.dao.impl;

import com.wholescripts.core.dao.WholescriptsItemTimeDao;
import com.wholescripts.core.model.ItemTimeModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class WholescriptsItemTimeDaoImpl implements WholescriptsItemTimeDao
{
	protected static final String ITEMTIME_QUERY = "SELECT {time:" + ItemTimeModel.PK + "} FROM {"
			+ ItemTimeModel._TYPECODE + " as time}";
	protected static final String WHERE_CODE_CLAUSE = " WHERE {time:" + ItemTimeModel.CODE + "} =?code";

	protected static final String GET_ITEM_TIME_BY_NAME="SELECT {time:pk} FROM {ItemTime as time} where LOWER({time.name})=LOWER(?name)";

	@Resource(name = "flexibleSearchService")
	public FlexibleSearchService flexibleSearchService;


	@Override
	public ItemTimeModel getItemTimeByCode(final String code)
	{
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(ITEMTIME_QUERY + WHERE_CODE_CLAUSE);
		searchQuery.addQueryParameter("code", code);
		searchQuery.setCount(1);
		return flexibleSearchService.searchUnique(searchQuery);
	}

	@Override
	public ItemTimeModel getItemTimeByName(final String name)
	{
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(GET_ITEM_TIME_BY_NAME);
		searchQuery.addQueryParameter("name", name);
		searchQuery.setCount(1);
		return flexibleSearchService.searchUnique(searchQuery);
	}
}
