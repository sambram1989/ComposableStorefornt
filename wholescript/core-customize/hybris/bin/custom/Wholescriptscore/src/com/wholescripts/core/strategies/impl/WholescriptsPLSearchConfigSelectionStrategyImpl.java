/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.strategies.impl;

import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.SolrFacetSearchConfigSelectionStrategy;
import de.hybris.platform.commerceservices.search.solrfacetsearch.strategies.exceptions.NoValidSolrConfigException;
import de.hybris.platform.solrfacetsearch.daos.SolrFacetSearchConfigDao;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;

import java.util.Optional;

/**
 *
 */
public class WholescriptsPLSearchConfigSelectionStrategyImpl implements SolrFacetSearchConfigSelectionStrategy
{

	private SolrFacetSearchConfigDao solrFacetSearchConfigDao;


	/**
	 * @return the solrFacetSearchConfigDao
	 */
	public SolrFacetSearchConfigDao getSolrFacetSearchConfigDao()
	{
		return solrFacetSearchConfigDao;
	}


	/**
	 * @param solrFacetSearchConfigDao
	 *           the solrFacetSearchConfigDao to set
	 */
	public void setSolrFacetSearchConfigDao(final SolrFacetSearchConfigDao solrFacetSearchConfigDao)
	{
		this.solrFacetSearchConfigDao = solrFacetSearchConfigDao;
	}


	@Override
	public SolrFacetSearchConfigModel getCurrentSolrFacetSearchConfig() throws NoValidSolrConfigException
	{
		final Optional<SolrFacetSearchConfigModel> result = solrFacetSearchConfigDao.findAllFacetSearchConfigs().stream()
				.filter(config -> config.getName().contains("wholescriptsPrivateLabelProductIndex")).findFirst();
		if (result.isEmpty())
		{
			throw new NoValidSolrConfigException(
					"No Valid SolrFacetSearchConfig configured neither for base site/base store/session product catalog versions.");
		}
		return result.get();
	}
}


