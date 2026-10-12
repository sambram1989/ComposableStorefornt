/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.dao.impl;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wholescripts.core.dao.WholescriptsPopularCategoriesDao;
import com.wholescripts.core.model.WsPopularCategoriesModel;
import com.wholescripts.core.model.WsPopularProductsModel;


public class WholescriptsPopularCategoriesDaoImpl implements WholescriptsPopularCategoriesDao
{
	private static final Logger LOG = LogManager.getLogger(WholescriptsPopularCategoriesDaoImpl.class);

	private static final String WHOLESCRIPTS_POPULAR_PRODUCTS_QUERY = "SELECT {p.PK} from {WsPopularProducts AS p} WHERE {p.categoryCode}=?categoryCode ORDER BY {p.weight} DESC";
	private static final String WHOLESCRIPTS_POPULAR_CATEGORIES_QUERY = "SELECT {cat.PK} from {WsPopularCategories AS cat} ORDER BY {cat.weight} DESC";
	private static final String WHOLESCRIPTS_TOP_BRANDS_QUERY = "SELECT {cat.PK} from {WsPopularBrands AS cat} ORDER BY {cat.weight} DESC";
	private static final String WHOLESCRIPTS_POPULAR_PRODUCTS_COUNT = "wholescripts.popular.products.for.each.category.count";
	private static final String WHOLESCRIPTS_POPULAR_CATEGORIES_COUNT = "wholescripts.popular.categories.count";
	private static final String WHOLESCRIPTS_POPULAR_PRODUCTS_QUERY_BY_CODE = "SELECT {p.PK} FROM {WsPopularProducts AS p  JOIN Product AS pr ON {p.productCode} = {pr.code} }  WHERE {p.categoryCode}=?categoryCode " + "AND {p.productCode}=?productCode " + "AND {pr.isMedPax} = '0'";
	private static final String WHOLESCRIPTS_POPULAR_CATEGORIES_QUERY_BY_CODE="SELECT {cat.PK} from {WsPopularCategories AS cat} WHERE {cat.categoryCode}=?categoryCode";
	@Resource(name = "flexibleSearchService")
	public FlexibleSearchService flexibleSearchService;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Override
	public List<WsPopularCategoriesModel> getPopularCategoriesDetails()
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(WHOLESCRIPTS_POPULAR_CATEGORIES_QUERY);
		query.setCount(configurationService.getConfiguration().getInt(WHOLESCRIPTS_POPULAR_CATEGORIES_COUNT, 6));
		final SearchResult<WsPopularCategoriesModel> result = flexibleSearchService.search(query);
		return result.getResult();
	}

	@Override
	public List<WsPopularProductsModel> getPopularProductsDetails(final String categoryCode)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(WHOLESCRIPTS_POPULAR_PRODUCTS_QUERY);
		query.setCount(configurationService.getConfiguration().getInt(WHOLESCRIPTS_POPULAR_PRODUCTS_COUNT, 12));
		query.addQueryParameter("categoryCode", categoryCode);
		final SearchResult<WsPopularProductsModel> result = flexibleSearchService.search(query);
		return result.getResult();
	}

	@Override
	public WsPopularProductsModel getPopularProductsDetails(String categoryCode, String productCode) {
		final FlexibleSearchQuery query = new FlexibleSearchQuery(WHOLESCRIPTS_POPULAR_PRODUCTS_QUERY_BY_CODE);
		query.addQueryParameter("categoryCode", categoryCode);
		query.addQueryParameter("productCode", productCode);
		final SearchResult<WsPopularProductsModel> result = flexibleSearchService.search(query);
		return result.getResult().isEmpty() ? null : result.getResult().get(0);
	}

	@Override
	public WsPopularCategoriesModel getPopularCategoriesDetails(String categoryCode) {
		final FlexibleSearchQuery query = new FlexibleSearchQuery(WHOLESCRIPTS_POPULAR_CATEGORIES_QUERY_BY_CODE);
		query.addQueryParameter("categoryCode", categoryCode);
		final SearchResult<WsPopularCategoriesModel> result = flexibleSearchService.search(query);
		return result.getResult().isEmpty() ? null : result.getResult().get(0);
	}

}
