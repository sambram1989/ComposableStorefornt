/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.converters.Populator;

import com.wholescripts.dto.WsCartModificationData;


/**
 *
 */
public class WholescriptCartModificationPopulator implements Populator<CommerceCartModification, WsCartModificationData>
{


	@Override
	public void populate(final CommerceCartModification source, final WsCartModificationData target)
	{

		target.setQuantity(source.getQuantity());
		target.setQuantityAdded(source.getQuantityAdded());
		target.setDeliveryModeChanged(source.getDeliveryModeChanged());
		target.setEntryGroupNumbers(source.getEntryGroupNumbers());
	}
}
