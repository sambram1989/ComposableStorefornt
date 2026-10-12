/*
 * Copyright (c) 2025 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.facade;

import de.hybris.platform.commercefacades.product.data.PromotionData;
import de.hybris.platform.commercefacades.promotion.PromotionOption;

import java.util.Collection;

/**
 *
 */
public interface WholescriptsCommercePromotionRestrictionFacade
{
	public void enablePromotionForCart(final String promotionCode) throws Exception;
	public void disablePromotionForCart(final String promotionCode) throws Exception;
	public PromotionData getPromotion(final String code, final Collection<PromotionOption> options);
}
