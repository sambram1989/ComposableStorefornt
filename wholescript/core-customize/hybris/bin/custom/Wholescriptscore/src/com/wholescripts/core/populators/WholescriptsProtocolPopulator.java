/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;

import com.wholescripts.core.dao.WholescriptsMedPaxDao;
import com.wholescripts.core.model.MedPaxModel;
import com.wholescripts.core.model.RecommendationModel;
import de.hybris.platform.b2b.model.B2BCommentModel;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.ProtocolEntryModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;

import com.wholescripts.core.model.ProtocolModel;
import com.wholescripts.data.protocol.ProtocolData;
import com.wholescripts.data.protocol.ProtocolEntryData;


/**
 *
 */
public class WholescriptsProtocolPopulator implements Populator<ProtocolModel, ProtocolData>
{
	@Resource(name = "wsProtocolsEntryConverter")
	private Converter<ProtocolEntryModel, ProtocolEntryData> wsProtocolsEntryConverter;
	@Resource(name = "priceDataFactory")
	private PriceDataFactory priceDataFactory;
	@Resource(name = "wholescriptsUserConverter")
	Converter wholescriptsUserConverter;

	@Resource(name = "wholescriptsMedPaxDao")
	private WholescriptsMedPaxDao wholescriptsMedPaxDao;
	
	@Override
	public void populate(final ProtocolModel source, final ProtocolData target) throws ConversionException
	{
		target.setId(source.getCode());
		target.setDescription(source.getWsDescription());
		target.setName(source.getWsName());
		target.setIsWSProtocol(source.getIsWSProtocol());
		if (Objects.nonNull(source.getDate()))
		{
			target.setModifiedTime(source.getDate().toString());
		}
		if (Objects.nonNull(source.getProtocolSharedStatus()))
		{
			target.setProtocolSharedStatus(source.getProtocolSharedStatus().getCode());
		}
		if (Objects.nonNull(source.getProtocolStatus()))
		{
			target.setProtocolStatus(source.getProtocolStatus().getCode());
		}
		final PriceData priceData = priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(source.getTotalPrice()), "USD");
		target.setTotalPrice(priceData);
		target.setProtocolEntries(wsProtocolsEntryConverter.convertAll(source.getProtocolEntry()));
		target.setTotalItems(source.getProtocolEntry().size());
		for(B2BCommentModel comment :source.getB2bcomments()) {
			target.setNotes(comment.getWsNotes());
		}
		target.setTotalUnitCount(calcTotalUnitCount(source));
		target.setPatientList(wholescriptsUserConverter.convertAll(source.getPatients())); 
		target.setIsWSProtocol(source.getIsWSProtocol());
		if(source.getMsrpSubTotalPrice() != null){
			final PriceData msrpSubTotalPrice = priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(source.getMsrpSubTotalPrice()), "USD");
			target.setMsrpSubTotalPrice(msrpSubTotalPrice);
		}
	}

	protected Integer calcTotalUnitCount(final ProtocolModel source) {
		int totalUnitCount = 0;
		final MedPaxModel medpax = wholescriptsMedPaxDao.getMedPaxForOwner(source);
		for (final AbstractOrderEntryModel orderEntryModel : source.getEntries()) {
			totalUnitCount += orderEntryModel.getQuantity().intValue();
		}
		if (Objects.nonNull(medpax)) {
			totalUnitCount += 1;
		}
		return Integer.valueOf(totalUnitCount);
	}

}
