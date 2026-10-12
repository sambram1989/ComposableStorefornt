/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;

import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.commercefacades.order.converters.populator.OrderEntryPopulator;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.ProtocolEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;

import com.wholescripts.core.model.DosingModel;
import com.wholescripts.data.protocol.ProtocolEntryData;
import com.wholescripts.dto.DosingData;


/**
 *
 */
public class WholescriptsProtocolEntryPopulator implements Populator<ProtocolEntryModel, ProtocolEntryData> 
{
	@Resource(name = "wScriptDosingConverter")
	private Converter<DosingModel, DosingData> wScriptDosingConverter;
	@Resource(name = "productConverter")
	private Converter<ProductModel, ProductData> productConverter;
	@Resource(name = "priceDataFactory")
	private PriceDataFactory priceDataFactory;
	@Resource(name = "orderEntryPopulator")
	private OrderEntryPopulator orderEntryPopulator;
	@Resource(name="enumerationService")
	private EnumerationService enumerationService;

	public void populate(final ProtocolEntryModel source, final ProtocolEntryData target) throws ConversionException
	{
		orderEntryPopulator.populate(source, target);
		target.setDosing(wScriptDosingConverter.convertAll(source.getDosing()));
		if (Objects.nonNull(source.getItemDuration()))
		{
			target.setItemDuration(source.getItemDuration().getCode());
		}
		if (Objects.nonNull(source.getProductItemType()))
		{
			target.setProductItemType(enumerationService.getEnumerationName(source.getProductItemType()));
		}
		if(CollectionUtils.isNotEmpty(source.getComments())) {
			CommentModel comment=	source.getComments().get(source.getComments().size()-1);
			target.setNotes(comment.getText());
		}

		if(source.getMsrpBasePrice() != null){
			final PriceData msrpBasePrice = priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(source.getMsrpBasePrice()), "USD");
			target.setMsrpBasePrice(msrpBasePrice);
		}
		if(source.getMsrpTotalPrice() != null){
			final PriceData msrpTotalPrice = priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(source.getMsrpTotalPrice()), "USD");
			target.setMsrpTotalPrice(msrpTotalPrice);
		}

		if(source.getTotalPrice()!=null) {
			final PriceData priceData = priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(source.getTotalPrice()), "USD");
			target.setTotalPrice(priceData);
		}

	}
}
