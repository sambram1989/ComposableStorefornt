/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;

import de.hybris.platform.commerceservices.search.facetdata.BreadcrumbData;
import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import de.hybris.platform.commerceservices.search.facetdata.SpellingSuggestionData;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.wholescripts.data.WholescriptsWebinarData;
import com.wholescripts.search.facetdata.WholescriptsSearchPageData;
import com.wholescripts.search.facetdata.WholescriptsWebinarSearchPageData;


/**
 */
public class WholscriptsSearchPagePopulator<QUERY, STATE, RESULT, ITEM extends WholescriptsWebinarData, SCAT, CATEGORY>
		implements Populator<WholescriptsSearchPageData<QUERY, RESULT>, WholescriptsWebinarSearchPageData<STATE, ITEM>>
{
	private Converter<QUERY, STATE> searchStateConverter;
	private Converter<BreadcrumbData<QUERY>, BreadcrumbData<STATE>> breadcrumbConverter;
	private Converter<FacetData<QUERY>, FacetData<STATE>> facetConverter;
	private Converter<SpellingSuggestionData<QUERY>, SpellingSuggestionData<STATE>> spellingSuggestionConverter;
	private Converter<RESULT, ITEM> wsSearchResultWebinarConverter;


	protected Converter<QUERY, STATE> getSearchStateConverter()
	{
		return searchStateConverter;
	}

	@Required
	public void setSearchStateConverter(final Converter<QUERY, STATE> searchStateConverter)
	{
		this.searchStateConverter = searchStateConverter;
	}

	protected Converter<BreadcrumbData<QUERY>, BreadcrumbData<STATE>> getBreadcrumbConverter()
	{
		return breadcrumbConverter;
	}

	@Required
	public void setBreadcrumbConverter(final Converter<BreadcrumbData<QUERY>, BreadcrumbData<STATE>> breadcrumbConverter)
	{
		this.breadcrumbConverter = breadcrumbConverter;
	}

	protected Converter<FacetData<QUERY>, FacetData<STATE>> getFacetConverter()
	{
		return facetConverter;
	}

	@Required
	public void setFacetConverter(final Converter<FacetData<QUERY>, FacetData<STATE>> facetConverter)
	{
		this.facetConverter = facetConverter;
	}

	/**
	 * @return the wsSearchResultWebinarConverter
	 */
	public Converter<RESULT, ITEM> getWsSearchResultWebinarConverter()
	{
		return wsSearchResultWebinarConverter;
	}

	/**
	 * @param wsSearchResultWebinarConverter
	 *           the wsSearchResultWebinarConverter to set
	 */
	public void setWsSearchResultWebinarConverter(final Converter<RESULT, ITEM> wsSearchResultWebinarConverter)
	{
		this.wsSearchResultWebinarConverter = wsSearchResultWebinarConverter;
	}

	protected Converter<SpellingSuggestionData<QUERY>, SpellingSuggestionData<STATE>> getSpellingSuggestionConverter()
	{
		return spellingSuggestionConverter;
	}

	@Required
	public void setSpellingSuggestionConverter(final Converter<SpellingSuggestionData<QUERY>, SpellingSuggestionData<STATE>> spellingSuggestionConverter)
	{
		this.spellingSuggestionConverter = spellingSuggestionConverter;
	}

	@Override
	public void populate(final WholescriptsSearchPageData<QUERY, RESULT> source,
			final WholescriptsWebinarSearchPageData<STATE, ITEM> target)
	{
		target.setFreeTextSearch(source.getFreeTextSearch());

		if (source.getBreadcrumbs() != null)
		{
			target.setBreadcrumbs(Converters.convertAll(source.getBreadcrumbs(), getBreadcrumbConverter()));
		}

		target.setCurrentQuery(getSearchStateConverter().convert(source.getCurrentQuery()));

		if (source.getFacets() != null)
		{
			target.setFacets(Converters.convertAll(source.getFacets(), getFacetConverter()));
		}

		target.setPagination(source.getPagination());

		if (source.getResults() != null)
		{
			target.setResults(Converters.convertAll(source.getResults(), getWsSearchResultWebinarConverter()));
		}

		target.setSorts(source.getSorts());

		if (source.getSpellingSuggestion() != null)
		{
			target.setSpellingSuggestion(getSpellingSuggestionConverter().convert(source.getSpellingSuggestion()));
		}

		target.setMeetPresenter(getPresenter(target.getResults()));

		target.setKeywordRedirectUrl(source.getKeywordRedirectUrl());
	}

	private List<String> getPresenter(final List<ITEM> list)
	{
		final List<WholescriptsWebinarData> webinar = (List<WholescriptsWebinarData>) list;
		final List<String> meetPresenter = new ArrayList<String>();

		for (final WholescriptsWebinarData wholescriptsWebinarData : webinar)
		{
			if (null != wholescriptsWebinarData.getPresenter() && CollectionUtils.isNotEmpty(wholescriptsWebinarData.getPresenter()))
			{
				meetPresenter.addAll(wholescriptsWebinarData.getPresenter());
			}
		}
		return meetPresenter;
	}
}
