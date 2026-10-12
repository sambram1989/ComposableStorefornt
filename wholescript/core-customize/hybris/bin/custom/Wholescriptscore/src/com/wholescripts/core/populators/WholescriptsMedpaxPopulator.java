/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;


import de.hybris.platform.cmsfacades.data.MediaData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;

import com.wholescripts.core.model.MedPaxModel;
import com.wholescripts.core.model.MedpaxEntryModel;
import com.wholescripts.data.MedPaxData;
import com.wholescripts.data.MedpaxCartonData;
import com.wholescripts.data.MedpaxEntryData;

import java.util.Objects;


/**
 *
 */
public class WholescriptsMedpaxPopulator implements Populator<MedPaxModel, MedPaxData>
{
	@Resource(name = "productConverter")
	private Converter<ProductModel, ProductData> productConverter;

	@Resource(name = "wholescriptsMedpaxEntryConverter")
	private Converter<MedpaxEntryModel, MedpaxEntryData> wholescriptsMedpaxEntryConverter;
	@Resource(name = "mediaModelConverter")
   private Converter<MediaModel, MediaData> mediaModelConverter;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Override
	public void populate(final MedPaxModel source, final MedPaxData target) throws ConversionException
	{

		if(Objects.nonNull(source)) {
			target.setDescription(source.getDescription());
			target.setName(source.getName());
			target.setNotes(source.getNotes());
			target.setCode(source.getCode());
		}
		if(Objects.nonNull(source.getMsrpTotalPrice())) {
			target.setMsrpTotalPrice(source.getMsrpTotalPrice());}
		if(Objects.nonNull(source.getTotalPrice())) {
			target.setTotalPrice(source.getTotalPrice());
		}
		if (CollectionUtils.isNotEmpty(source.getMedpaxEntry()))
		{
			target.setMedpaxEntry(wholescriptsMedpaxEntryConverter.convertAll(source.getMedpaxEntry()));
		}
		target.setCarton(getCartonData(source));
	}
	
	private MedpaxCartonData getCartonData(final MedPaxModel source)
	{
		MedpaxCartonData cartonData = new MedpaxCartonData();
		cartonData.setCartonName(source.getCartonDescription());
		if(Objects.nonNull(source.getCarton())) {
			cartonData.setProductCode(source.getCarton().getCode());
         if(Objects.nonNull(source.getCarton().getPicture())) {
         	cartonData.setImage(mediaModelConverter.convert(source.getCarton().getPicture()));
         }
		}
		if(source.getCarton() != null && configurationService.getConfiguration().getString("medpax.product.carton").equals(source.getCarton().getCode())) {
			cartonData.setDefaultCarton(Boolean.TRUE);
		}
		return cartonData;
	}
}
