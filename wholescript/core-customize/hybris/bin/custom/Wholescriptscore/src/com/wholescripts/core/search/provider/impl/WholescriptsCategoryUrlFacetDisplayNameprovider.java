/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.search.provider.impl;

import de.hybris.platform.acceleratorservices.promotions.dao.PromotionsDao;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractFacetValueDisplayNameProvider;
import de.hybris.platform.solrfacetsearch.search.Breadcrumb;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import com.wholescripts.core.dao.WholescriptsCoreDao;


public class WholescriptsCategoryUrlFacetDisplayNameprovider extends AbstractFacetValueDisplayNameProvider {
	
	private final Map<String, Locale> localeCache = new HashMap<>();
	
	@Resource(name = "wholescriptsCoreDao")
	private WholescriptsCoreDao wholescriptsCoreDao;

	@Override
	public String getDisplayName(final SearchQuery query, final IndexedProperty property, final String facetValue) {
		
		final Locale locale = getLocale(query.getLanguage());
		String categoryName = null;
		
		CategoryModel category = getCategory(query);

		if (category != null) {
			categoryName = category.getName(locale);
		}
		return categoryName;
	}

	private CategoryModel getCategory(final SearchQuery query) {
		
		List<Breadcrumb> breadCrumbs = query.getBreadcrumbs();
		CategoryModel category = null;
		
		for (Breadcrumb breadCrumb : breadCrumbs) {
			
			String fieldName = breadCrumb.getFieldName();
			if("categoryUrl".equalsIgnoreCase(fieldName)) {
				String categoryURL = breadCrumb.getValue();
				if(StringUtils.isNotEmpty(categoryURL)) {
					category = wholescriptsCoreDao.getCategoryByCategoryUrl(categoryURL);
				}
				
			}
			
		}

		return category;
	}

	protected Locale getLocale(final String isoCode) {
		Locale result;
		result = localeCache.get(isoCode);
		if (result == null) {
			final String[] splitted_code = isoCode.split("_");
			if (splitted_code.length == 1) {
				result = new Locale(splitted_code[0]);
			}
			else {
				result = new Locale(splitted_code[0], splitted_code[1]);
			}

			localeCache.put(isoCode, result);
		}
		return result;
	}


}
