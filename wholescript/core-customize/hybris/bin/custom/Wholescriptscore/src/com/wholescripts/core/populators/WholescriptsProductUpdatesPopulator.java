/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;

import com.wholescripts.core.dao.WholescriptsCoreDao;
import com.wholescripts.core.model.WsDiscontinuedProductsModel;
import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Populate the product data with the product's discontinued date
 */
public class WholescriptsProductUpdatesPopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends AbstractProductPopulator<SOURCE, TARGET>
{

	@Resource(name = "wholescriptsCoreDao")
	private WholescriptsCoreDao wholescriptsCoreDao;

	@Override
	public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException
	{
		final WsDiscontinuedProductsModel discontinuedProduct = wholescriptsCoreDao.getDiscontinuedProductBySku(productModel.getCode());
		if (discontinuedProduct != null && discontinuedProduct.getDiscontinuedDate() != null)
		{
			final Date discontinuedDate = discontinuedProduct.getDiscontinuedDate();
			productData.setDiscontinuedDate(formatDate(discontinuedDate));
		}
	}

	protected String formatDate(final Date date)
	{
		if (date == null)
		{
			return null;
		}
		final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		return sdf.format(date);
	}

}
