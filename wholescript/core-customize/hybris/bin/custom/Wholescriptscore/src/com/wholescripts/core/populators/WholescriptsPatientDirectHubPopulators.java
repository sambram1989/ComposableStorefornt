/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;

import com.wholescripts.core.enums.SapCustomerClassificationEnum;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Comparator;
import java.util.Objects;

import com.wholescripts.core.model.ReferralCodesModel;
import com.wholescripts.data.ReferralCodeData;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import javax.annotation.Resource;

/**
 *
 */
public class WholescriptsPatientDirectHubPopulators implements Populator<ReferralCodesModel, ReferralCodeData>
{
	private static final Logger LOG = Logger.getLogger(WholescriptsPatientDirectHubPopulators.class);
	@Resource(name="imageConverter")
	private Converter<MediaModel, ImageData> imageConverter;

	@Resource(name = "addressConverter")
	private Converter<AddressModel, AddressData> addressConverter;

	@Resource(name = "b2bCustomerService")
	private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;

	@Resource(name = "enumerationService")
	private EnumerationService enumerationService;

	@Override
	public void populate(ReferralCodesModel source, ReferralCodeData target) throws ConversionException
	{
		if(Objects.nonNull(source.getReferralCode()))
		{
			String fullReferralCode = source.getReferralCode();
			String referralCodeBeforePipe = fullReferralCode.split("\\|")[0];
			target.setReferralCode(referralCodeBeforePipe);
		}
		target.setDiscount(source.getDiscount());
		target.setLastName(source.getLastName());
		target.setDescription(source.getDescription());
		target.setReferralLink(source.getReferralLink());
		target.setWebsite(source.getWebsite());
        try {
            if(source.getProfileImage()!=null)
            {
                target.setProfileImage(imageConverter.convert(source.getProfileImage()));
            }
        } catch (ConversionException e) {

        }
		if (CollectionUtils.isNotEmpty(source.getAddress())) {
			// Find the address with the latest modified time
			AddressModel latestAddress = source.getAddress().stream()
					.max(Comparator.comparing(AddressModel::getModifiedtime))
					.orElse(null);

			if (latestAddress != null) {
				target.setAddress(addressConverter.convert(latestAddress));
			}
		}


		if(Objects.nonNull(source.getPatientDirectHub())) {
			target.setPatientDirectHubId(source.getPatientDirectHub().getId());
		}

		// Set practitioner name and credential from the referral code B2BUnit
		try {
			// Initialize default values
			target.setCredential("");
			target.setPractitionerName("");
			// Early return if source or defaultB2BUnit is null
			if (Objects.isNull(source.getB2bUnit())) {
				return;
			}
			B2BUnitModel defaultB2BUnit = source.getB2bUnit();
			// Set practitioner name
			if (Objects.nonNull(defaultB2BUnit.getName())) {
				target.setPractitionerName(defaultB2BUnit.getName());
			}
			// Set credential if sapCustomerClassification is available
			if (Objects.nonNull(defaultB2BUnit.getSapCustomerClassification())) {
				SapCustomerClassificationEnum sapCustomerClassification = defaultB2BUnit.getSapCustomerClassification();
				target.setCredential(enumerationService.getEnumerationName(sapCustomerClassification));
			}
		} catch (Exception e) {
			// Log the exception for debugging
			LOG.error("Failed to fetch or process B2B customer data", e);
			throw new ConversionException("Failed to fetch or process B2B customer data", e);
		}
	}
}