/*
 * Copyright (c) 2025 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.dao.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.wholescripts.core.dao.WholescriptsPromotionCategoryDao;
import com.wholescripts.core.model.PromotionCategoryModel;

/**
 *
 */
public class WholescriptsPromotionCategoryDaoImpl implements WholescriptsPromotionCategoryDao
{
	private static final String GET_PROMOTION_CATEGORY_BY_CODE = "SELECT {pc.pk} FROM {PromotionCategory AS pc} WHERE {pc.code} = ?code";
	private static final String GET_PROMOTION_BRAND_BY_CODE = "SELECT {pb.pk} FROM {PromotionBrand AS pb} WHERE {pb.code} = ?code";
	@Resource(name = "flexibleSearchService")
   private FlexibleSearchService flexibleSearchService;
	
	@Override
	public PromotionCategoryModel getPromotionCategoryByCode(String code)
	{
		 final StringBuilder queryBuilder = new StringBuilder();
       final Map<String, Object> queryParams = new HashMap<>();
       final FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(queryBuilder.toString());
       queryParams.put(PromotionCategoryModel.CODE, code);
       queryBuilder.append(GET_PROMOTION_CATEGORY_BY_CODE);
       final FlexibleSearchQuery query = new FlexibleSearchQuery(queryBuilder);
       query.addQueryParameters(queryParams);
       final SearchResult<PromotionCategoryModel> result = flexibleSearchService.search(query);
       if (Objects.nonNull(result) && CollectionUtils.isNotEmpty(result.getResult()))
       {
           return result.getResult().get(0);
       }
       return null;
	}
}
