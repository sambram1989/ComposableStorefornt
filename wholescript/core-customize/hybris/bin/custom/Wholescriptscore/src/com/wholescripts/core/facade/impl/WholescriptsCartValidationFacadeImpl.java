/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.facade.impl;

import javax.annotation.Resource;

import com.wholescripts.core.facade.WholescriptsCartValidationFacade;
import com.wholescripts.core.service.WholescriptsCartValidationService;
import com.wholescripts.dto.WholescriptsValidateCartsWsDTO;

/**
 *
 */
public class WholescriptsCartValidationFacadeImpl implements WholescriptsCartValidationFacade
{

	@Resource(name = "wholescriptsCartValidationService")
	private WholescriptsCartValidationService wholescriptsCartValidationService;
	
	@Override
	public void validateCart(WholescriptsValidateCartsWsDTO requestData) throws Exception
	{
		wholescriptsCartValidationService.validateCart(requestData);
	}

}
