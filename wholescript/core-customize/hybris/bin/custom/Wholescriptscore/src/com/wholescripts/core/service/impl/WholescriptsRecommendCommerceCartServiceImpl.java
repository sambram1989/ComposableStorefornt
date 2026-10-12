/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service.impl;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;

import javax.annotation.Resource;

import com.wholescripts.core.service.WholescriptsRecommendCommerceCartService;
import com.wholescripts.core.strategies.WholescriptsCommerceAddToCartStrategy;
import com.wholescripts.core.strategies.WholescriptsCommerceUpdateCartStrategy;
import com.wholescripts.dto.WsCommerceCartParameter;


/**
 *
 */
public class WholescriptsRecommendCommerceCartServiceImpl implements WholescriptsRecommendCommerceCartService
{
	@Resource(name = "wsCommerceAddToCartStrategy")
	private WholescriptsCommerceAddToCartStrategy wsCommerceAddToCartStrategy;

	@Resource(name = "wsCommerceUpdateCartStrategy")
	private WholescriptsCommerceUpdateCartStrategy wsCommerceUpdateCartStrategy;

	@Override
	public CommerceCartModification addToRecommend(final WsCommerceCartParameter parameter)
			throws CommerceCartModificationException
	{
		return wsCommerceAddToCartStrategy.addToRecommend(parameter);
	}

	public CommerceCartModification updateQuantityForRecommendEntry(final WsCommerceCartParameter parameter)
			throws CommerceCartModificationException
	{
		return wsCommerceUpdateCartStrategy.updateQuantityForRecommendEntry(parameter);
	}
}
