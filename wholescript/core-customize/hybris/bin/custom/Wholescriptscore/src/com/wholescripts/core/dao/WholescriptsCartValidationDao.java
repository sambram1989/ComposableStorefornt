/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.dao;

import com.wholescripts.core.model.RegionProductSwapRestrictionModel;

import java.util.List;

/**
 *
 */
public interface WholescriptsCartValidationDao
{
	public boolean validateIfProductInCAExclusion(String productCode, String regionCode);
	public boolean validateIfProductInNYExclusion(String productCode, String regionCode);

	public RegionProductSwapRestrictionModel updateProductFromSwapRestriction(String productCode, String regionCode);
	List<RegionProductSwapRestrictionModel> getRestrictedRegionsForProduct(String productCode);
}
