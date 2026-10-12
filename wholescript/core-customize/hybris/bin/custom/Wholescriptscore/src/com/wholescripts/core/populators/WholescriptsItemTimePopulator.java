/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;

import de.hybris.platform.cmsfacades.data.MediaData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Objects;

import javax.annotation.Resource;

import com.wholescripts.core.model.ItemTimeModel;
import com.wholescripts.dto.ItemTimeData;


/**
 *
 */
public class WholescriptsItemTimePopulator implements Populator<ItemTimeModel, ItemTimeData>
{
	@Resource(name="mediaModelConverter")
	private Converter<MediaModel, MediaData> mediaModelConverter;
	@Resource(name = "enumerationService")
	private EnumerationService enumerationService;

	@Override
	public void populate(final ItemTimeModel source, final ItemTimeData target) throws ConversionException
	{
		target.setCode(source.getCode());
		if(Objects.nonNull(source.getTimeOfTheDay())) {
			target.setTimeOfTheDay(enumerationService.getEnumerationName(source.getTimeOfTheDay()));
		}
		target.setDisplayOrder(source.getDisplayOrder());
		target.setIsActive(source.getIsActive());
		target.setIsDisplayable(source.getIsDisplayable());
		target.setSapValue(source.getSapValue());
		target.setParentItemTimeCode(source.getParentItemTimeCode());
		if(Objects.nonNull(source.getIconImage()) && Objects.nonNull(source.getIconImage().getCatalogVersion()) ) {
			target.setIconImage(mediaModelConverter.convert(source.getIconImage()));
		}
	}
}
