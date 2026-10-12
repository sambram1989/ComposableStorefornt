/*
 * Copyright (c) 2025 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.strategies;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.promotions.result.PromotionOrderResults;

public interface WholescriptsCommerceCartCalculationStrategy
{
	PromotionOrderResults applyPromotionOnCart(final CartModel cart);
}
