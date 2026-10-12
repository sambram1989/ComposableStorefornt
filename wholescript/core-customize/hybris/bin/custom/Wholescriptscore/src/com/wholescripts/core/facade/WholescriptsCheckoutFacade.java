/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.facade;

import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.order.InvalidCartException;

import com.wholescripts.data.CustomerActivityData;
import com.wholescripts.data.OrderCreationRequest;

/**
 *
 */
public interface WholescriptsCheckoutFacade
{
	public boolean setDeliveryMode(final String deliveryModeCode,final String deliveryCost);
	
	OrderModel placeOrder(final CartModel cartModel, final SalesApplication salesChannel) throws InvalidCartException;
	
	boolean authorizeExternalOrderPayment(final CartModel cart, final PaymentInfoModel paymentInfo, final OrderCreationRequest orderCreationRequest);
	
	boolean authorizePayment(final OrderCreationRequest orderCreationRequest, final CustomerActivityData customerActivityData);
}
