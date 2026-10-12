/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.dao.impl;

import com.wholescripts.core.model.RegionProductSwapRestrictionModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.wholescripts.core.dao.WholescriptsCartValidationDao;
import com.wholescripts.core.model.RegionProductAgeRestrictionModel;
import com.wholescripts.core.model.RegionProductRestrictionModel;

import java.util.Collections;
import java.util.List;

/**
 *
 */ 
public class WholescriptsCartValidationDaoImpl implements WholescriptsCartValidationDao
{
	private static final String GET_PRODUCT_REGION_RESTRICTION ="SELECT {pr.PK} FROM {RegionProductRestriction as pr} where {pr.productCode}=?productCode and {pr.regionCode} =?regionCode and {validFrom} <= (CURRENT_DATE) and {validTo} >= (CURRENT_DATE)";
	private static final String GET_PRODUCT_REGION_AGE_RESTRICTION ="SELECT {pr.PK} FROM {RegionProductAgeRestriction as pr} where {pr.productCode}=?productCode and {pr.regionCode} =?regionCode and {validFrom} <= (CURRENT_DATE) and {validTo} >= (CURRENT_DATE)";
	private static final String GET_PRODUCT_SWAP_REGION_RESTRICTION ="SELECT {sr.PK} FROM {RegionProductSwapRestriction as sr} where {sr.productCode}=?productCode and {sr.regionCode} =?regionCode";
	private static final String GET_DISTINCT_REGIONS_SWAP_REGION_RESTRICTION = "SELECT {rpsr.pk} FROM {RegionProductSwapRestriction AS rpsr} WHERE {rpsr.productCode}=?product";

	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;
	
	@Override
	public boolean validateIfProductInCAExclusion(String productCode, String regionCode)
	{
		if(StringUtils.isNotEmpty(productCode) && StringUtils.isNotEmpty(regionCode)) {
			final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_PRODUCT_REGION_RESTRICTION);
			query.addQueryParameter(RegionProductRestrictionModel.PRODUCTCODE, productCode);
			query.addQueryParameter(RegionProductRestrictionModel.REGIONCODE, regionCode);
			final SearchResult<RegionProductRestrictionModel> result = flexibleSearchService.search(query);
			return result.getResult().isEmpty() ? false : true;
		}
		return false;
	}

	@Override
	public boolean validateIfProductInNYExclusion(String productCode, String regionCode)
	{
		if(StringUtils.isNotEmpty(productCode) && StringUtils.isNotEmpty(regionCode)) {
			final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_PRODUCT_REGION_AGE_RESTRICTION);
			query.addQueryParameter(RegionProductAgeRestrictionModel.PRODUCTCODE, productCode);
			query.addQueryParameter(RegionProductAgeRestrictionModel.REGIONCODE, regionCode);
			final SearchResult<RegionProductAgeRestrictionModel> result = flexibleSearchService.search(query);
			return result.getResult().isEmpty() ? false : true;
		}
		return false;
	}

	@Override
	public RegionProductSwapRestrictionModel updateProductFromSwapRestriction(String productCode, String regionCode) {
		if(StringUtils.isNotEmpty(productCode) && StringUtils.isNotEmpty(regionCode)) {
			final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_PRODUCT_SWAP_REGION_RESTRICTION);
			query.addQueryParameter(RegionProductSwapRestrictionModel.PRODUCTCODE, productCode);
			query.addQueryParameter(RegionProductSwapRestrictionModel.REGIONCODE, regionCode);
			final SearchResult<RegionProductSwapRestrictionModel> result = flexibleSearchService.search(query);
			return result.getResult().isEmpty() ? null : result.getResult().get(0);
		}
        return null;
    }

	@Override
	public List<RegionProductSwapRestrictionModel> getRestrictedRegionsForProduct(String productCode) {
		if (StringUtils.isNotEmpty(productCode)) {
			final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_DISTINCT_REGIONS_SWAP_REGION_RESTRICTION);
			query.addQueryParameter("product", productCode);
			final SearchResult<RegionProductSwapRestrictionModel> result = flexibleSearchService.search(query);
			return result.getResult();
		}
		return Collections.emptyList();
	}

}
