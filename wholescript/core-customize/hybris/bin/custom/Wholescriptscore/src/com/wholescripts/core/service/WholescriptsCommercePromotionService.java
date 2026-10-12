/*
 * Copyright (c) 2025 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service;

import de.hybris.platform.promotions.model.AbstractPromotionModel;

/**
 *
 */
public interface WholescriptsCommercePromotionService
{
	public AbstractPromotionModel getPromotionForCode(final String code) throws Exception;
	AbstractPromotionModel getPromotionModel(final String code);
}
