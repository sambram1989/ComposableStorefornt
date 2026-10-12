/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;

import com.wholescripts.dto.WsCommerceCartParameter;


/**
 *
 */
public interface WholescriptsRecommendCommerceCartService
{
	public CommerceCartModification addToRecommend(final WsCommerceCartParameter parameter)
			throws CommerceCartModificationException;

	/**
	 * @throws CommerceCartModificationException
	 *
	 */
	public CommerceCartModification updateQuantityForRecommendEntry(WsCommerceCartParameter parameter)
			throws CommerceCartModificationException;

}
