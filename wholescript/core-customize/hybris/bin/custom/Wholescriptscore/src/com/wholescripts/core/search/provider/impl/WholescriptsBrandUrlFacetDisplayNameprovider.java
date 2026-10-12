/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.search.provider.impl;

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractFacetValueDisplayNameProvider;
import de.hybris.platform.solrfacetsearch.search.Breadcrumb;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.wholescripts.core.dao.WholescriptsCoreDao;
import com.wholescripts.core.model.WholescriptsBrandModel;


public class WholescriptsBrandUrlFacetDisplayNameprovider extends AbstractFacetValueDisplayNameProvider {
	
	@Resource(name = "wholescriptsCoreDao")
   private WholescriptsCoreDao wholescriptsCoreDao;

	@Override
	public String getDisplayName(final SearchQuery query, final IndexedProperty property, final String facetValue) {
		
		String brandName = null;
		
		WholescriptsBrandModel brand = getBrand(query);

		if (brand != null) {
			brandName = brand.getName();
		}
		return brandName;
	}

	private WholescriptsBrandModel getBrand(final SearchQuery query) {
		
		List<Breadcrumb> breadCrumbs = query.getBreadcrumbs();
		WholescriptsBrandModel brand = null;
		
		for (Breadcrumb breadCrumb : breadCrumbs) {
			String fieldName = breadCrumb.getFieldName();
			if("brandURL".equalsIgnoreCase(fieldName)) {
				String brandURL = breadCrumb.getValue();
				if(StringUtils.isNotEmpty(brandURL)) {
					brand = wholescriptsCoreDao.getBrandCodeByBrandUrl(brandURL);
				}
				
			}
			
		}

		return brand;
	}


}
