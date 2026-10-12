/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wholescripts.core.enums.FedTaxClass;
import com.wholescripts.core.model.W9FormModel;
import com.wholescripts.dto.W9FormData;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


public class W9FormDataReversePopulator implements Populator<W9FormData, W9FormModel>
{
	private static final Logger LOG = LoggerFactory.getLogger(W9FormDataReversePopulator.class);

	@Resource(name = "b2bCustomerService")
	private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "modelService")
	ModelService modelService;

	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18NService;

	@Resource(name = "enumerationService")
	protected EnumerationService enumerationService;

	@Override
	public void populate(final W9FormData source, final W9FormModel target) throws ConversionException
	{
		final AddressModel address = modelService.create(AddressModel.class);
		final RegionModel region = modelService.create(RegionModel.class);
		final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
		target.setB2bCustomer(customer);
		target.setName(source.getName());
		target.setBusinessName(source.getBusinessName());
		target.setFedTaxClass(enumerationService.getEnumerationValue(FedTaxClass.class, source.getFedTaxClass()));
		target.setExemptPayee(source.getExemptPayee());
		target.setExemptFatca(source.getExemptFatca());

		final AddressData addressData = source.getAddress();
		if (addressData.getRegion() != null)
		{
			final String isocode = addressData.getRegion().getIsocode();
			try
			{
				final RegionModel regionModel = commonI18NService.getRegion(commonI18NService.getCountry("US"), isocode);
				address.setRegion(regionModel);
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
		address.setOwner(target);
		address.setLine1(addressData.getLine1());
		address.setTown(addressData.getTown());
		address.setPostalcode(addressData.getPostalCode());
		target.setAddress(address);
		if (source.getTaxClassLimitedText() != null)
		{
		target.setTaxClassLimitedText(source.getTaxClassLimitedText());
		}
		if (source.getTaxClassOtherText() != null)
		{
			target.setTaxClassOtherText(source.getTaxClassOtherText());
		}
		target.setAccountNumbers(source.getAccountNumbers());
		target.setRequestersInfo(source.getRequestersInfo());
		target.setSsn(source.getSsn());
		target.setEin(source.getEin());
		target.setSignature(source.getSignature());
		try
		{
				target.setSignatureDate(convertDateInEST());
		}
		catch (final ParseException e)
		{
			LOG.error(String.format("Error while converting the signatureDate due to %s", e.getLocalizedMessage()));
		}
		target.setBrowserString(source.getBrowserString());
		target.setIp(source.getIp());
	}

	private Date convertDateInEST() throws ParseException {
		Date currentDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		String estFormattedDate = formatter.format(currentDate);
		return formatter.parse(estFormattedDate);
	}
}
