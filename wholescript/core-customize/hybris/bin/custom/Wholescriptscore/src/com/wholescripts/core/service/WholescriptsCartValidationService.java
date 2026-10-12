/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service;

import com.wholescripts.core.model.RegionProductSwapRestrictionModel;
import com.wholescripts.dto.WholescriptsValidateCartsWsDTO;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;


/**
 *
 */
public interface WholescriptsCartValidationService
{
	public void validateCart(final WholescriptsValidateCartsWsDTO requestData) throws Exception;

	public RegionProductSwapRestrictionModel updateProductFromSwapRestriction(final AbstractOrderEntryModel source);

	public void validateBrandAgreement()throws Exception;

}
