/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.dao;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeValueModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;

import java.util.Collection;


/**
 * DAO to find zone delivery modes.
 */
public interface WholescriptsDeliveryModeDao extends Dao
{

	/**
	 * Find the delivery modes for the delivery country, currency, net flag and store.
	 *
	 * @param order
	 *           the order with search parameters.
	 * @return the matching delivery modes
	 */
	Collection<DeliveryModeModel> findDeliveryModes(AbstractOrderModel order, String sapCustomerCode);
	ZoneDeliveryModeValueModel findZoneDeliveryModesValue(AbstractOrderModel order, String sapCustomerCode,DeliveryModeModel deliveryMode);

}
