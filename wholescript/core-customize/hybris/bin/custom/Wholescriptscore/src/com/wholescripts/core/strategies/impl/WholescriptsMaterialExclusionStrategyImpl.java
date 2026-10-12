/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.strategies.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.core.model.product.ProductModel;

import javax.annotation.Resource;

import com.wholescripts.core.dao.WholescriptsMaterialExclusionDao;
import com.wholescripts.core.strategies.WholescriptsMaterialExclusionStrategy;


public  class WholescriptsMaterialExclusionStrategyImpl implements WholescriptsMaterialExclusionStrategy
{
	@Resource(name = "wholescriptsMaterialExclusionDao")
	WholescriptsMaterialExclusionDao wholescriptsMaterialExclusionDao;
	@Resource(name = "b2bCustomerService")
	private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;
  
	@Override
	public boolean checkIfProductInExclusionTable(ProductModel product)
	{
		final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
		if (customer == null) {
			return false;
		}

		if(wholescriptsMaterialExclusionDao.validateBrandCustomerClassRestriction(product, b2bCustomerService.getCurrentB2BCustomer())) 
		{
			return true;
		}else if(wholescriptsMaterialExclusionDao.validateCustomerClassProductRestriction(product, b2bCustomerService.getCurrentB2BCustomer()))
		{
			return true;
		}else if(wholescriptsMaterialExclusionDao.validateCustomerProductGroupRestriction(product, b2bCustomerService.getCurrentB2BCustomer())) 
		{
			return true;
		}else if(wholescriptsMaterialExclusionDao.validateCustomerProductRestriction(product, b2bCustomerService.getCurrentB2BCustomer()))
		{
			return true;
		}else {
			return false;
		}
	}

	@Override
	public boolean isMedpaxExcluedForCustomer(String accountNumber) {
        return wholescriptsMaterialExclusionDao.isMedpaxExcluedForCustomer(accountNumber);
    }
}
