/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.facade.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.AutocompleteSuggestionData;
import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.ProductSearchAutocompleteService;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.AutocompleteSuggestion;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.threadcontext.ThreadContextService;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.wholescripts.core.facade.WholescriptsSearchFacade;
import com.wholescripts.core.service.WholescriptsSearchService;
import com.wholescripts.data.WholescriptsContentData;
import com.wholescripts.data.WholescriptsSearchData;
import com.wholescripts.data.WholescriptsWebinarData;
import com.wholescripts.search.facetdata.WholescriptsSearchPageData;
import com.wholescripts.search.facetdata.WholescriptsWebinarSearchPageData;


/**
 *
 */
public class WholescriptsSearchFacadeImpl<ITEM extends WholescriptsSearchData> implements WholescriptsSearchFacade<ITEM>
{
	private WholescriptsSearchService<SolrSearchQueryData, SearchResultValueData, WholescriptsSearchPageData<SolrSearchQueryData, SearchResultValueData>> wholescriptsSearchService;
	private Converter<WholescriptsSearchPageData<SolrSearchQueryData, SearchResultValueData>, WholescriptsWebinarSearchPageData<SearchStateData, WholescriptsWebinarData>> wholscriptsSearchPageConverter;
	private Converter<SearchQueryData, SolrSearchQueryData> searchQueryDecoder;
	private Converter<AutocompleteSuggestion, AutocompleteSuggestionData> autocompleteSuggestionConverter;
	private ProductSearchAutocompleteService<AutocompleteSuggestion> autocompleteService;
	private ThreadContextService threadContextService;
	private Converter<WholescriptsSearchPageData<SolrSearchQueryData, SearchResultValueData>, WholescriptsSearchPageData<SearchStateData, WholescriptsContentData>> wholscriptsContentSearchPageConverter;
	@Resource(name = "wsPLProductCategorySearchPageConverter")
	private Converter<ProductCategorySearchPageData<SolrSearchQueryData, SearchResultValueData, CategoryModel>, ProductCategorySearchPageData<SearchStateData, ITEM, CategoryData>> productCategorySearchPageConverter;
	/**
	 * @return the wholescriptsSearchService
	 */
	public WholescriptsSearchService<SolrSearchQueryData, SearchResultValueData, WholescriptsSearchPageData<SolrSearchQueryData, SearchResultValueData>> getWholescriptsSearchService()
	{
		return wholescriptsSearchService;
	}

	/**
	 * @param wholescriptsSearchService
	 *           the wholescriptsSearchService to set
	 */
	public void setWholescriptsSearchService(
			final WholescriptsSearchService<SolrSearchQueryData, SearchResultValueData, WholescriptsSearchPageData<SolrSearchQueryData, SearchResultValueData>> wholescriptsSearchService)
	{
		this.wholescriptsSearchService = wholescriptsSearchService;
	}

	protected Converter<SearchQueryData, SolrSearchQueryData> getSearchQueryDecoder()
	{
		return searchQueryDecoder;
	}

	@Required
	public void setSearchQueryDecoder(final Converter<SearchQueryData, SolrSearchQueryData> searchQueryDecoder)
	{
		this.searchQueryDecoder = searchQueryDecoder;
	}

	protected Converter<AutocompleteSuggestion, AutocompleteSuggestionData> getAutocompleteSuggestionConverter()
	{
		return autocompleteSuggestionConverter;
	}

	@Required
	public void setAutocompleteSuggestionConverter(
			final Converter<AutocompleteSuggestion, AutocompleteSuggestionData> autocompleteSuggestionConverter)
	{
		this.autocompleteSuggestionConverter = autocompleteSuggestionConverter;
	}

	protected ProductSearchAutocompleteService<AutocompleteSuggestion> getAutocompleteService()
	{
		return autocompleteService;
	}

	@Required
	public void setAutocompleteService(final ProductSearchAutocompleteService<AutocompleteSuggestion> autocompleteService)
	{
		this.autocompleteService = autocompleteService;
	}

	protected ThreadContextService getThreadContextService()
	{
		return threadContextService;
	}

	@Required
	public void setThreadContextService(final ThreadContextService threadContextService)
	{
		this.threadContextService = threadContextService;
	}

	/**
	 * @return the wholscriptsSearchPageConverter
	 */
	public Converter<WholescriptsSearchPageData<SolrSearchQueryData, SearchResultValueData>, WholescriptsWebinarSearchPageData<SearchStateData, WholescriptsWebinarData>> getWholscriptsSearchPageConverter()
	{
		return wholscriptsSearchPageConverter;
	}

	/**
	 * @param wholscriptsSearchPageConverter
	 *           the wholscriptsSearchPageConverter to set
	 */
	public void setWholscriptsSearchPageConverter(
			final Converter<WholescriptsSearchPageData<SolrSearchQueryData, SearchResultValueData>, WholescriptsWebinarSearchPageData<SearchStateData, WholescriptsWebinarData>> wholscriptsSearchPageConverter)
	{
		this.wholscriptsSearchPageConverter = wholscriptsSearchPageConverter;
	}

	/**
	 * @return the wholscriptsContentSearchPageConverter
	 */
	public Converter<WholescriptsSearchPageData<SolrSearchQueryData, SearchResultValueData>, WholescriptsSearchPageData<SearchStateData, WholescriptsContentData>> getWholscriptsContentSearchPageConverter()
	{
		return wholscriptsContentSearchPageConverter;
	}

	/**
	 * @param wholscriptsContentSearchPageConverter
	 *           the wholscriptsContentSearchPageConverter to set
	 */
	public void setWholscriptsContentSearchPageConverter(
			final Converter<WholescriptsSearchPageData<SolrSearchQueryData, SearchResultValueData>, WholescriptsSearchPageData<SearchStateData, WholescriptsContentData>> wholscriptsContentSearchPageConverter)
	{
		this.wholscriptsContentSearchPageConverter = wholscriptsContentSearchPageConverter;
	}

	@Override
	public WholescriptsWebinarSearchPageData<SearchStateData, WholescriptsWebinarData> searchWebinar(
			final SearchStateData searchState,
			final PageableData pageableData)
	{
		Assert.notNull(searchState, "SearchStateData must not be null.");

		return getThreadContextService().executeInContext(
				new ThreadContextService.Executor<WholescriptsWebinarSearchPageData<SearchStateData, WholescriptsWebinarData>, ThreadContextService.Nothing>()
				{
					@Override
					public WholescriptsWebinarSearchPageData<SearchStateData, WholescriptsWebinarData> execute()
					{
						return getWholscriptsSearchPageConverter()
								.convert(getWholescriptsSearchService().searchWebinar(decodeState(searchState, null), pageableData));
					}
				});
	}

	@Override
	public WholescriptsSearchPageData<SearchStateData, WholescriptsContentData> searchContent(final SearchStateData searchState,
			final PageableData pageableData)
	{
		Assert.notNull(searchState, "SearchStateData must not be null.");

		return getThreadContextService().executeInContext(
				new ThreadContextService.Executor<WholescriptsSearchPageData<SearchStateData, WholescriptsContentData>, ThreadContextService.Nothing>()
				{
					@Override
					public WholescriptsSearchPageData<SearchStateData, WholescriptsContentData> execute()
					{
						return getWholscriptsContentSearchPageConverter().convert(
								getWholescriptsSearchService().searchContent(decodeState(searchState, null), pageableData));
					}
				});
	}

	@Override
	public List<AutocompleteSuggestionData> getAutocompleteSuggestions(final String input)
	{
		return getThreadContextService()
				.executeInContext(new ThreadContextService.Executor<List<AutocompleteSuggestionData>, ThreadContextService.Nothing>()
				{
					@Override
					public List<AutocompleteSuggestionData> execute()
					{
						return Converters.convertAll(getAutocompleteService().getAutocompleteSuggestions(input),
								getAutocompleteSuggestionConverter());
					}
				});

	}

	protected SolrSearchQueryData decodeState(final SearchStateData searchState, final String categoryCode)
	{
		final SolrSearchQueryData searchQueryData = getSearchQueryDecoder().convert(searchState.getQuery());
		if (categoryCode != null && searchQueryData != null)
		{
			searchQueryData.setCategoryCode(categoryCode);
		}

		return searchQueryData;
	}
	
	@Override
	public ProductSearchPageData<SearchStateData, ProductData> SearchPrivateLabel(final SearchStateData searchState,
			final PageableData pageableData)
	{
		Assert.notNull(searchState, "SearchStateData must not be null.");

		return getThreadContextService().executeInContext(
				new ThreadContextService.Executor<ProductSearchPageData<SearchStateData, ProductData>, ThreadContextService.Nothing>()
				{
					@Override
					public ProductSearchPageData<SearchStateData, ProductData> execute()
					{
						return productCategorySearchPageConverter
						.convert(getWholescriptsSearchService().searchPrivateLabel(decodeState(searchState, null), pageableData));
					}
				});
	}
}
