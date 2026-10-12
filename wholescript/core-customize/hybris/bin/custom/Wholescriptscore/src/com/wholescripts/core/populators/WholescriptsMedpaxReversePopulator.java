/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;


import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wholescripts.core.enums.ItemDuration;
import com.wholescripts.core.model.MedPaxModel;
import com.wholescripts.core.model.MedpaxEntryModel;
import com.wholescripts.core.model.ProtocolModel;
import com.wholescripts.core.service.WholescriptsProtocolService;
import com.wholescripts.core.service.WholescriptsRecommendService;
import com.wholescripts.dto.MedPaxWsDTO;
import com.wholescripts.dto.MedpaxEntryWsDTO;


/**
 *
 */
public class WholescriptsMedpaxReversePopulator implements Populator<MedPaxWsDTO, MedPaxModel>
{

	private static final Logger LOG = LoggerFactory.getLogger(WholescriptsMedpaxReversePopulator.class);
	@Resource(name = "wholescriptsMedpaxEntryReverseConverter")
	private Converter<MedpaxEntryWsDTO, MedpaxEntryModel> wholescriptsMedpaxEntryReverseConverter;


	@Resource(name = "b2bCustomerService")
	private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;

	@Resource(name = "wholescriptsProtocolService")
	private WholescriptsProtocolService wholescriptsProtocolService;

	@Resource(name = "wholescriptsRecommendService")
	private WholescriptsRecommendService wholescriptsRecommendService;

	@Resource(name = "wsMedPaxCodeGenerator")
	private KeyGenerator wsMedPaxCodeGenerator;
	@Resource(name = "productService")
	private ProductService productService;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	
	@Resource(name = "enumerationService")
	protected EnumerationService enumerationService;

	@Override
	public void populate(final MedPaxWsDTO source, final MedPaxModel target) throws ConversionException
	{

		target.setDescription(source.getDescription());
		target.setName(source.getName());
		target.setTotalPrice(source.getTotalPrice());
		target.setMsrpTotalPrice(source.getMsrpTotalPrice());
		target.setCode(wsMedPaxCodeGenerator.generate().toString());

		if (CollectionUtils.isNotEmpty(source.getMedpaxEntry()))
		{
			target.setMedpaxEntry(wholescriptsMedpaxEntryReverseConverter.convertAll(source.getMedpaxEntry()));
		}
		LOG.info("OWNER ID :::::::::::::" + source.getProtocolId() != null ? source.getProtocolId() : source.getRecommendationId());
		if(StringUtils.isNotEmpty(source.getProtocolId()) ) {
			final Optional<ProtocolModel> protocol = wholescriptsProtocolService.getProtocolModelForID(source.getProtocolId(), false);
			if(protocol.isPresent()) {
				target.setMedPaxOwner(protocol.get());
			}
		}
		else if (StringUtils.isNotEmpty(source.getRecommendationId()))
		{
			target.setMedPaxOwner(wholescriptsRecommendService.getRecommendation(source.getRecommendationId()));
		}
		else
		{
			target.setMedPaxOwner(b2bCustomerService.getCurrentB2BCustomer());
		}
		//added default carton for medpax
		
		ProductModel carton = productService.getProductForCode(configurationService.getConfiguration().getString("medpax.product.carton"));
		if(Objects.nonNull(carton)) {
			target.setCarton(carton);
			target.setCartonDescription(carton.getName());
		}
		
		if (source.getExternalCartonCode() != null) {
			ProductModel medPaxCarton = productService.getProductForCode(source.getExternalCartonCode());
			if(Objects.nonNull(medPaxCarton)) {
				target.setCarton(medPaxCarton);
			}
		}
		
		if(Objects.nonNull(source.getAbstractOrderQty())) {
			target.setAbstractOrderQty(source.getAbstractOrderQty());
		}
		
		if(source.getItemDuration() != null) {
			target.setItemDuration(enumerationService.getEnumerationValue(ItemDuration.class, source.getItemDuration()));
		}
		
		
	}
}
