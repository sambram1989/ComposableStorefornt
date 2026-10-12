/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;

import javax.annotation.Resource;

import com.wholescripts.core.service.WholescriptsSearchService;
import com.wholescripts.core.strategies.WholescriptsPrivateLabelSearchStrategy;
import com.wholescripts.core.strategies.WholescriptsSearchFactory;
import com.wholescripts.search.facetdata.WholescriptsSearchPageData;


/**
 *
 */
public class WholescriptsSearchServiceImpl<ITEM> implements
		WholescriptsSearchService<SolrSearchQueryData, ITEM, WholescriptsSearchPageData<SolrSearchQueryData, ITEM>>
{

	private WholescriptsSearchFactory<ITEM> wholescriptsWebinarSearchFactory;
	private WholescriptsSearchFactory<ITEM> wholescriptsContentSearchFactory;
	@Resource(name = "privateLabelSearchStrategy")
	private WholescriptsPrivateLabelSearchStrategy<ITEM> wholescriptsPrivateLabelSearchStrategy;

	/**
	 * @return the wholescriptsWebinarSearchFactory
	 */
	public WholescriptsSearchFactory<ITEM> getWholescriptsWebinarSearchFactory()
	{
		return wholescriptsWebinarSearchFactory;
	}

	/**
	 * @param wholescriptsWebinarSearchFactory
	 *           the wholescriptsWebinarSearchFactory to set
	 */
	public void setWholescriptsWebinarSearchFactory(final WholescriptsSearchFactory<ITEM> wholescriptsWebinarSearchFactory)
	{
		this.wholescriptsWebinarSearchFactory = wholescriptsWebinarSearchFactory;
	}

	@Override
	public WholescriptsSearchPageData<SolrSearchQueryData, ITEM> searchWebinar(final SolrSearchQueryData searchQueryData,
			final PageableData pageableData)
	{
		return wholescriptsWebinarSearchFactory.getSearchStrategy().searchAgain(searchQueryData, pageableData);
	}

	/**
	 * @return the wholescriptsContentSearchFactory
	 */
	public WholescriptsSearchFactory<ITEM> getWholescriptsContentSearchFactory()
	{
		return wholescriptsContentSearchFactory;
	}

	/**
	 * @param wholescriptsContentSearchFactory
	 *           the wholescriptsContentSearchFactory to set
	 */
	public void setWholescriptsContentSearchFactory(final WholescriptsSearchFactory<ITEM> wholescriptsContentSearchFactory)
	{
		this.wholescriptsContentSearchFactory = wholescriptsContentSearchFactory;
	}

	@Override
	public WholescriptsSearchPageData<SolrSearchQueryData, ITEM> searchContent(final SolrSearchQueryData searchQueryData,
			final PageableData pageableData)
	{
		return wholescriptsContentSearchFactory.getSearchStrategy().searchAgain(searchQueryData, pageableData);
	}

	@Override
	public ProductCategorySearchPageData<SolrSearchQueryData, ITEM, CategoryModel> searchPrivateLabel(
			final SolrSearchQueryData searchQueryData, final PageableData pageableData)
	{
		return wholescriptsPrivateLabelSearchStrategy.searchAgain(searchQueryData, pageableData);
	}


}
