/*
 * Copyright (c) 2025 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service;

import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;

/**
 *
 */
public interface WholescriptsCommerceCartService
{
	void applyPromotionOnCart(CommerceCartParameter parameter) throws Exception;
}
