/*
 * Copyright (c) 2025 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.event.listener;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.core.model.user.CustomerModel;


public class WholescriptsAccertifyFraudCheckEvent extends AbstractCommerceUserEvent
{
	private B2BCustomerModel b2bCustomer;
	
	public WholescriptsAccertifyFraudCheckEvent(B2BCustomerModel b2bCustomer) {
		super();
		this.b2bCustomer=b2bCustomer;
	}


	public B2BCustomerModel getB2bCustomer()
	{
		return b2bCustomer;
	}


	public void setB2bCustomer(B2BCustomerModel b2bCustomer)
	{
		this.b2bCustomer = b2bCustomer;
	}
}
