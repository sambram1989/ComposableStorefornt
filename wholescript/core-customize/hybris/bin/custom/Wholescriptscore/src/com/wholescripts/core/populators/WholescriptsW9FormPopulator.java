/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.text.SimpleDateFormat;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wholescripts.core.constants.WholescriptscoreConstants;
import com.wholescripts.core.model.W9FormModel;
import com.wholescripts.dto.W9FormData;


/**
 *
 */
public class WholescriptsW9FormPopulator implements Populator<W9FormModel, W9FormData>
{
	private static final Logger LOG = LoggerFactory.getLogger(WholescriptsW9FormPopulator.class);

	@Resource(name = "b2bCustomerService")
	private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;


	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "modelService")
	ModelService modelService;

	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18NService;

	@Resource(name = "enumerationService")
	protected EnumerationService enumerationService;

	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;

	@Override
	public void populate(final W9FormModel source, final W9FormData target) throws ConversionException
	{
		// XXX Auto-generated method stub
		final SimpleDateFormat dateFormat = new SimpleDateFormat(getDateFormat());
		target.setB2bCustomer(source.getB2bCustomer().getName());
		target.setName(source.getName());
		target.setBusinessName(source.getBusinessName());
		target.setFedTaxClass(source.getFedTaxClass().getCode());
		target.setExemptPayee(source.getExemptPayee());
		target.setExemptFatca(source.getExemptFatca());

		final AddressData addressData = new AddressData();
		final RegionData regionData = new RegionData();
		final AddressModel adrressModel = source.getAddress();
		if (adrressModel.getRegion() != null)
		{
			final String isocode = adrressModel.getRegion().getIsocode();
			try
			{
				final RegionModel regionModel = commonI18NService.getRegion(commonI18NService.getCountry("US"), isocode);
				regionData.setIsocode(regionModel.getIsocode());
				regionData.setName(regionModel.getName());
			}
			catch (final UnknownIdentifierException e)
			{
				throw new ConversionException("No region with the code " + isocode + " found.", e);
			}
			catch (final AmbiguousIdentifierException e)
			{
				throw new ConversionException("More than one region with the code " + isocode + " found.", e);
			}
		}
		addressData.setLine1(adrressModel.getLine1());
		addressData.setTown(adrressModel.getTown());
		addressData.setPostalCode(adrressModel.getPostalcode());
		addressData.setRegion(regionData);
		target.setAddress(addressData);

		target.setAccountNumbers(source.getAccountNumbers());
		target.setRequestersInfo(source.getRequestersInfo());
		target.setSsn(source.getSsn());
		target.setEin(source.getEin());
		if (source.getTaxClassLimitedText() != null)
		{
			target.setTaxClassLimitedText(source.getTaxClassLimitedText());
		}
		if (source.getTaxClassOtherText() != null)
		{
			target.setTaxClassOtherText(source.getTaxClassOtherText());
		}
		target.setSignature(source.getSignature());
		if (source.getSignatureDate() != null)
		{
			target.setSignatureDate(dateFormat.format(source.getSignatureDate()));
		}
		target.setBrowserString(source.getBrowserString());
		target.setIp(source.getIp());

	}

	public String getDateFormat()
	{
		return configurationService.getConfiguration().getString(WholescriptscoreConstants.DATE_FORMAT_CONFIG
				+ (commerceCommonI18NService.getCurrentLocale() != null && commerceCommonI18NService.getCurrentCurrency() != null
						? commerceCommonI18NService.getCurrentLocale().getLanguage()
								+ commerceCommonI18NService.getCurrentCurrency().getIsocode()
						: WholescriptscoreConstants.DATE_FORMAT_CONFIG_DEFAULT));
	}

}
