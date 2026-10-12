/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;

import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SearchQueryPageableData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;
import de.hybris.platform.commerceservices.search.solrfacetsearch.querybuilder.FreeTextQueryBuilder;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.SearchConfig;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import com.wholescripts.core.service.WholescriptsUserService;


public class WholescriptsSearchTextPopulator<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, INDEXED_TYPE_SORT_TYPE>
		implements
		Populator<SearchQueryPageableData<SolrSearchQueryData>, SolrSearchRequest<FacetSearchConfig, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE>>
{
	private static final String FAVOURITE_PAGE = "FavouritesPage";

	@Resource(name = "wholescriptsUserService")
	WholescriptsUserService wholescriptsUserService;
	private List<FreeTextQueryBuilder> freeTextQueryBuilders;
	private List<FreeTextQueryBuilder> favouritesPageQueryBuilders;

	/**
	 * @return the favouritesPageQueryBuilders
	 */
	public List<FreeTextQueryBuilder> getFavouritesPageQueryBuilders()
	{
		return favouritesPageQueryBuilders;
	}

	/**
	 * @param favouritesPageQueryBuilders
	 *           the favouritesPageQueryBuilders to set
	 */
	public void setFavouritesPageQueryBuilders(final List<FreeTextQueryBuilder> favouritesPageQueryBuilders)
	{
		this.favouritesPageQueryBuilders = favouritesPageQueryBuilders;
	}

	public List<FreeTextQueryBuilder> getFreeTextQueryBuilders()
	{
		return freeTextQueryBuilders;
	}

	@Required
	public void setFreeTextQueryBuilders(final List<FreeTextQueryBuilder> freeTextQueryBuilders)
	{
		this.freeTextQueryBuilders = freeTextQueryBuilders;
	}

	@Override
	public void populate(final SearchQueryPageableData<SolrSearchQueryData> source,
			final SolrSearchRequest<FacetSearchConfig, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SearchQuery, INDEXED_TYPE_SORT_TYPE> target)
	{
		// cleanup the search text
		String cleanedFreeTextSearch = cleanupSolrSearchText(target.getSearchQueryData().getFreeTextSearch());
		target.setSearchText(cleanedFreeTextSearch);

		if (StringUtils.isBlank(cleanedFreeTextSearch) && wholescriptsUserService.isWishlistAvailableForUser())
		{
			cleanedFreeTextSearch = "*";
		}

		final SearchConfig searchConfig = target.getFacetSearchConfig().getSearchConfig();
		if (searchConfig != null && StringUtils.isNotBlank(cleanedFreeTextSearch))
		{
			addFreeTextQuery(target.getSearchQuery(), cleanedFreeTextSearch);
		}
	}

	protected String cleanupSolrSearchText(final String text)
	{
		String cleanedText = text;
		if (text == null)
		{
			cleanedText = "";
		}
		else
		{
			// Replace ':' special character in solr text with whitespace
			cleanedText = cleanedText.replace(':', ' ').trim();

			// Replace SOLR keywords with lowercased
			cleanedText = cleanedText.replace("AND", "and");
			cleanedText = cleanedText.replace("OR", "or");
		}

		return cleanedText;
	}

	protected void addFreeTextQuery(final SearchQuery searchQuery, final String cleanedFreeTextSearch)
	{
		// Split the full text string to separate words
		final String[] words = cleanedFreeTextSearch.split("\\s+");

		if (FAVOURITE_PAGE.equalsIgnoreCase(cleanedFreeTextSearch))
		{
			favouritePageQueryBuilder(searchQuery, cleanedFreeTextSearch, words);
		}
		else
		{
			freeTextQueryBuilder(searchQuery, cleanedFreeTextSearch, words);
		}

	}

	/**
	 *
	 */
	private void freeTextQueryBuilder(final SearchQuery searchQuery, final String cleanedFreeTextSearch, final String[] words)
	{
		for (final FreeTextQueryBuilder freeTextQueryBuilder : getFreeTextQueryBuilders())
		{
			freeTextQueryBuilder.addFreeTextQuery(searchQuery, cleanedFreeTextSearch, words);
		}
	}

	/**
	 *
	 */
	private void favouritePageQueryBuilder(final SearchQuery searchQuery, final String cleanedFreeTextSearch,
			final String[] words)
	{
		for (final FreeTextQueryBuilder freeTextQueryBuilder : getFavouritesPageQueryBuilders())
		{
			freeTextQueryBuilder.addFreeTextQuery(searchQuery, cleanedFreeTextSearch, words);
		}
	}
}
