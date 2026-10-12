/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service;

import com.wholescripts.core.model.CustomerMaterialInformationModel;

import com.wholescripts.core.model.RegionProductAgeRestrictionModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.List;

/**
 *
 */
public interface WholescriptsProudctsService
{
	List<ProductModel>  getNonPLProductsBySKUsList(final List<String> productSkusList, final CatalogVersionModel catalogVersion);

	String getProductSwapRestriction(final String productCode);

	List<RegionProductAgeRestrictionModel> getProductAgeRestriction(final String productCode);

	List<CustomerMaterialInformationModel> getCustomerMaterialInformationByCustomerId(String customerId);

	List<ProductModel> getAllNonPLProducts(final CatalogVersionModel catalogVersion);

	ProductModel getPLProductBySKU(final String productCode, CatalogVersionModel catalogVersion);
}
