/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.facade;

import com.wholescripts.dto.WholescriptsValidateCartsWsDTO;

/**
 *
 */
public interface WholescriptsCartValidationFacade
{
 void validateCart(WholescriptsValidateCartsWsDTO requestData) throws Exception;
}
