/*
 * Copyright (c) 2025 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;

import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;


/**
 *
 */
public class WholescriptsRegionPopulator implements Populator<RegionModel, RegionData>
{

	@Override
	public void populate(final RegionModel source, final RegionData target) throws ConversionException
	{
		target.setIsShippingTAxWarningEnabled(source.getIsShippingTAxWarningEnabled());
	}
}
