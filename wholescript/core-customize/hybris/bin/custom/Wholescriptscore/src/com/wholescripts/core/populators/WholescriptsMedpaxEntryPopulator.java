/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;


import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import javax.annotation.Resource;

import com.wholescripts.core.model.DosingModel;
import com.wholescripts.core.model.MedpaxEntryModel;
import com.wholescripts.data.MedpaxEntryData;
import com.wholescripts.dto.DosingData;

import java.util.Objects;


/**
 *
 */
public class WholescriptsMedpaxEntryPopulator implements Populator<MedpaxEntryModel, MedpaxEntryData>
{
	@Resource(name = "productConverter")
	private Converter<ProductModel, ProductData> productConverter;

	@Resource(name = "wScriptDosingConverter")
	private Converter<DosingModel, DosingData> wScriptDosingConverter;

	@Override
	public void populate(final MedpaxEntryModel source, final MedpaxEntryData target) throws ConversionException
	{
		target.setPrice(source.getPrice());
		target.setEntryNumber(source.getEntryNumber());
		target.setProduct(productConverter.convert(source.getProduct()));
		if(Objects.nonNull(source.getMsrpPrice())) {
			target.setMsrpPrice(source.getMsrpPrice());
		}
		if(Objects.nonNull(source.getQuantity())) {
			target.setQuantity(source.getQuantity());
		}
		if (source.getDosing() != null)
		{
			target.setDosing(wScriptDosingConverter.convertAll(source.getDosing()));
		}
	}
}
