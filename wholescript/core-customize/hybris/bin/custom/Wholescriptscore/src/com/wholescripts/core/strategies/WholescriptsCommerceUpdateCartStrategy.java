/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.strategies;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.model.order.CartModel;

import com.wholescripts.data.CommerceProtocolParameter;
import com.wholescripts.dto.WsCommerceCartParameter;


/**
 *
 */
public interface WholescriptsCommerceUpdateCartStrategy
{

	public CommerceCartModification updateQuantityForRecommendEntry(WsCommerceCartParameter parameter)
			throws CommerceCartModificationException;
	
	CommerceCartModification updateQuantityForProtocolEntry(final CommerceProtocolParameter parameters)
			throws CommerceCartModificationException;
	
	CommerceCartModification updateToShippingModeForProtocolEntry(final CommerceProtocolParameter parameters)
			throws CommerceCartModificationException;
	public void normalizeEntryNumbers(final CartModel cartModel);
}
