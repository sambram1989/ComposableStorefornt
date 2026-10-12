/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;

import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.RecommendationEntryModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import javax.annotation.Resource;

import com.wholescripts.core.model.DosingModel;
import com.wholescripts.dto.DosingData;
import com.wholescripts.dto.WsRecommendationEntryData;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;


/**
 *
 */
public class WholescriptsRecommendEntryPopulator
		implements Populator<RecommendationEntryModel, WsRecommendationEntryData>
{
	private Converter<AbstractOrderEntryModel, OrderEntryData> orderEntryConverter;

	@Resource(name = "enumerationService")
	protected EnumerationService enumerationService;

	@Resource(name = "wScriptDosingConverter")
	private Converter<DosingModel, DosingData> wScriptDosingConverter;

	@Resource(name = "priceDataFactory")
	private PriceDataFactory priceDataFactory;

	@Override
	public void populate(final RecommendationEntryModel source, final WsRecommendationEntryData target)
			throws ConversionException
	{
		final OrderEntryData orderEntryData = getOrderEntryConverter().convert(source);
		if (source.getEntryNumber() != null)
		{
		target.setEntryNumber(source.getEntryNumber());
		}
		if (orderEntryData.getProduct() != null)
		{
		target.setProduct(orderEntryData.getProduct());
		}
		if (source.getQuantity() != null)
		{
		target.setQuantity(source.getQuantity());
		}
		if (orderEntryData.getBasePrice() != null)
		{
		target.setBasePrice(orderEntryData.getBasePrice());
		}
		if (orderEntryData.getTotalPrice() != null)
		{
		target.setTotalPrice(orderEntryData.getTotalPrice());
		}
		if (source.getItemDuration() != null)
		{
		target.setItemDuration(source.getItemDuration().toString());
		}
		if(source.getDosing()!=null)
		{
			target.setDosing(wScriptDosingConverter.convertAll(source.getDosing()));
		}
		if (source.getProductItemType() != null)
		{
			target.setProductItemType(source.getProductItemType().getCode());
		}
		if (source.getTemplateId() != null)
		{
		target.setTemplateId(source.getTemplateId());
		}
		if(source.getMsrpBasePrice() != null){
			final PriceData priceData = priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(source.getMsrpBasePrice()), "USD");
			target.setMsrpBasePrice(priceData);
		}
		if(source.getMsrpTotalPrice() != null){
			final PriceData priceData = priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(source.getMsrpTotalPrice()), "USD");
			target.setMsrpTotalPrice(priceData);
		}
		if(CollectionUtils.isNotEmpty(source.getComments())) {
			CommentModel comment=	source.getComments().get(source.getComments().size()-1);
			target.setNotes(comment.getText());
		}
	}

	public Converter<AbstractOrderEntryModel, OrderEntryData> getOrderEntryConverter()
	{
		return orderEntryConverter;
	}

	/**
	 * @param orderEntryConverter
	 *           the orderEntryConverter to set
	 */
	public void setOrderEntryConverter(final Converter<AbstractOrderEntryModel, OrderEntryData> orderEntryConverter)
	{
		this.orderEntryConverter = orderEntryConverter;
	}

}
