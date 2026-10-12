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

import org.apache.commons.collections4.CollectionUtils;

import com.wholescripts.core.model.DosingModel;
import com.wholescripts.core.model.ItemTimeModel;
import com.wholescripts.dto.DosingData;
import com.wholescripts.dto.ItemTimeData;


/**
 *
 */
public class WholescriptsDosingPopulator implements Populator<DosingModel, DosingData>
{
	@Resource(name = "productConverter")
	private Converter<ProductModel, ProductData> productConverter;

	@Resource(name = "wScriptsItemTimeConverter")
	private Converter<ItemTimeModel, ItemTimeData> wScriptsItemTimeConverter;

	@Override
	public void populate(final DosingModel source, final DosingData target) throws ConversionException
	{
		target.setId(source.getId());
		target.setIsSavedAsCustom(source.getIsSavedAsCustom());
		target.setQuantity(source.getQuantity());
		target.setProduct(productConverter.convert(source.getProduct()));
		
		if(CollectionUtils.isNotEmpty(source.getItemTimes())) {
			target.setItemTimes(wScriptsItemTimeConverter.convert(source.getItemTimes().get(0)));
		}
	}
}
