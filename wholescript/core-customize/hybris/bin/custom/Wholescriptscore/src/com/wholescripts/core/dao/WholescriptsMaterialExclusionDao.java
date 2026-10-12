/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.dao;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.List;

import com.wholescripts.core.model.BrandCustomerClassRestrictionModel;
import com.wholescripts.core.model.CustomerClassProductRestrictionModel;
import com.wholescripts.core.model.CustomerProductGroupRestrictionModel;
import com.wholescripts.core.model.CustomerProductRestrictionModel;
import com.wholescripts.core.model.RegionProductRestrictionModel;

public interface WholescriptsMaterialExclusionDao
{
	public boolean validateBrandCustomerClassRestriction(final ProductModel product, final B2BCustomerModel customer);
	public boolean validateCustomerClassProductRestriction(final ProductModel product, final B2BCustomerModel customer);
	public boolean validateCustomerProductRestriction(final ProductModel product, final B2BCustomerModel customer);
	public boolean validateCustomerProductGroupRestriction(final ProductModel product, final B2BCustomerModel customer);
	public List<CustomerProductRestrictionModel> getCustomerProductRestrictions(final String productCode);
	public List<CustomerProductGroupRestrictionModel> getCustomerProductGroupRestrictions(final String productGroup);
	public List<CustomerClassProductRestrictionModel> getCustomerClassProductRestrictions(final String productCode);
	public List<BrandCustomerClassRestrictionModel> getBrandCustomerClassRestriction(final String brandCode);

	boolean isMedpaxExcluedForCustomer(String accountNumber);
}
