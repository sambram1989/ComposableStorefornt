/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;

import de.hybris.platform.commercefacades.order.converters.populator.ZoneDeliveryModePopulator;
import de.hybris.platform.commercefacades.order.data.ZoneDeliveryModeData;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;


/**
 */
public class WsZoneDeliveryModePopulator extends ZoneDeliveryModePopulator
{
	@Override
	public void populate(final ZoneDeliveryModeModel source, final ZoneDeliveryModeData target)
	{
		super.populate(source, target);
		target.setSapCarrier(source.getSapCarrier());
		target.setSapCarrierShippingOption(source.getSapCarrierShippingOption());
		target.setApiCode(source.getApiCode());

	}
}
