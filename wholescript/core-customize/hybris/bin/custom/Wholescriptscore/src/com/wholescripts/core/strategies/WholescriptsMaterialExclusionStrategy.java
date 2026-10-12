/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.strategies;

import de.hybris.platform.core.model.product.ProductModel;

public interface WholescriptsMaterialExclusionStrategy
{
	public boolean checkIfProductInExclusionTable(final ProductModel productCode);

	public boolean isMedpaxExcluedForCustomer(String accountNumber);
}
