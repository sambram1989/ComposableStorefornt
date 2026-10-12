/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;

import de.hybris.platform.commercefacades.product.converters.populator.ImagePopulator;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wholescripts.core.model.WholescriptsBrandModel;
import com.wholescripts.dto.WholescriptsBrandData;


public class WholescriptsBrandPopulator implements Populator<WholescriptsBrandModel, WholescriptsBrandData>
{
	private static final Logger LOG = LoggerFactory.getLogger(WholescriptsBrandPopulator.class);

	@Resource(name = "imagePopulator")
	ImagePopulator imagePopulator;

	@Override
	public void populate(final WholescriptsBrandModel source, final WholescriptsBrandData target) throws ConversionException
	{
		if (StringUtils.isNotEmpty(source.getCode()))
		{
			target.setCode(source.getCode());
		}
		if (StringUtils.isNotEmpty(source.getName()))
		{
			target.setName(source.getName());
		}
		if (StringUtils.isNotEmpty(source.getDescription()))
		{
			target.setDescription(source.getDescription());
		}
		if (Objects.nonNull(source.getBrandLogo()))
		{
			final ImageData brandLogo = new ImageData();
			imagePopulator.populate(source.getBrandLogo(), brandLogo);
			target.setBrandLogo(brandLogo);
		}
		if (Objects.nonNull(source.getBrandImage()))
		{
			final ImageData brandImage = new ImageData();
			imagePopulator.populate(source.getBrandImage(), brandImage);
			target.setBrandImage(brandImage);
		}
		if (StringUtils.isNotEmpty(source.getBrandUrl()))
		{
			target.setBrandUrl(source.getBrandUrl());
		}
		if (Objects.nonNull(source.getIsTopBrand()))
		{
			target.setIsTopBrand(source.getIsTopBrand());
		}
		if (Objects.nonNull(source.getBrandPriority()))
		{
			target.setBrandPriority(source.getBrandPriority());
		}
		if(Objects.nonNull(source.getBrandAgreementPdf()))
		{
			final ImageData brandImage = new ImageData();
			imagePopulator.populate(source.getBrandAgreementPdf(), brandImage);
			target.setBrandAgreementPdf(brandImage);
		}
		else if (StringUtils.isNotEmpty(source.getBrandAgreementPdfUrl()))
		{
			target.setBrandAgreementPdfUrl(source.getBrandAgreementPdfUrl());
		}
		if (Objects.nonNull(source.getAgreementRequired()))
		{
			target.setAgreementRequired(source.getAgreementRequired());
		}
		if (StringUtils.isNotEmpty(source.getShortDescription()))
		{
			target.setShortDescription(source.getShortDescription());
		}
	}


}
