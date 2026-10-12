/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;

import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.wholescripts.data.WholescriptsExternalAPIAddressData;


/**
 */
public class ExternalAddressReversePopulator implements Populator<WholescriptsExternalAPIAddressData, AddressModel>
{
	private CommonI18NService commonI18NService;
	private FlexibleSearchService flexibleSearchService;

	@Override
	public void populate(final WholescriptsExternalAPIAddressData addressData, final AddressModel addressModel) throws ConversionException
	{
		Assert.notNull(addressData, "Parameter addressData cannot be null.");
		Assert.notNull(addressModel, "Parameter addressModel cannot be null.");

		addressModel.setFirstname(addressData.getFirstName());
		addressModel.setLastname(addressData.getLastName());
		addressModel.setEmail(addressData.getEmail());
		addressModel.setRemarks(addressData.getAddressName());
		addressModel.setLine1(addressData.getStreet1());
		addressModel.setLine2(addressData.getStreet2());
		addressModel.setTown(addressData.getCity());
		addressModel.setPostalcode(addressData.getZip());
		addressModel.setPhone1(addressData.getPhone());
		//addressModel.setCellphone(addressData.get);
		//addressModel.setDistrict(addressData.getDistrict());

		if (addressData.getCountry() != null)
		{
			//final String isocode = addressData.getCountry().getIsocode();
			try
			{
				final CountryModel countryModel = getCommonI18NService().getCountry(addressData.getCountry());
				addressModel.setCountry(countryModel);
			}
			catch (final UnknownIdentifierException e)
			{
				throw new ConversionException("No country with the code " + addressData.getCountry() + " found.", e);
			}
			catch (final AmbiguousIdentifierException e)
			{
				throw new ConversionException("More than one country with the code " + addressData.getCountry() + " found.", e);
			}
		}

		if (addressData.getRegion() != null)
		{
			final String isocode = addressData.getCountry()+ "-" +addressData.getRegion();
			try
			{
				final RegionModel regionModel = getCommonI18NService().getRegion(
						getCommonI18NService().getCountry(addressData.getCountry()), isocode);
				addressModel.setRegion(regionModel);
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

		
		addressModel.setShippingAddress(Boolean.valueOf(true));
		
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	protected FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}
}
