/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.strategies;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;

import com.wholescripts.data.CommerceProtocolParameter;
import com.wholescripts.dto.WsCommerceCartParameter;


/**
 *
 */
public interface WholescriptsCommerceAddToCartStrategy
{
	public CommerceCartModification addToRecommend(final WsCommerceCartParameter parameter)
			throws CommerceCartModificationException;

   public CommerceCartModification addToProtocol(final CommerceProtocolParameter parameter) throws CommerceCartModificationException;
}
