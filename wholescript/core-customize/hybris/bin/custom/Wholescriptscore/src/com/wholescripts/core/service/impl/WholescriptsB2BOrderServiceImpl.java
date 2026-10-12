package com.wholescripts.core.service.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.services.impl.DefaultB2BOrderService;
import de.hybris.platform.core.model.order.AbstractOrderModel;

public class WholescriptsB2BOrderServiceImpl extends DefaultB2BOrderService {
	
	
	@Override
	protected boolean isB2BContext(final AbstractOrderModel order) {
		if (order.getExternalOrder()) {
			final B2BCustomerModel customer = (order.getUser() instanceof B2BCustomerModel) ? (B2BCustomerModel) order.getUser()
					: null;
			return order != null && customer != null && order.getStatus() != null;
		}
		return order != null && getB2bCustomerService().getCurrentB2BCustomer() != null && order.getStatus() != null;

	}

}
