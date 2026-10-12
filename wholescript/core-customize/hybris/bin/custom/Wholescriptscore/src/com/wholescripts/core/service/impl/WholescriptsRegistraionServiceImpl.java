/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service.impl;

import de.hybris.platform.b2b.company.B2BCommerceB2BUserGroupService;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commerceservices.user.UserMatchingService;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.sap.core.configuration.model.SAPConfigurationModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.Objects;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.wholescripts.core.enums.SapCustomerClassificationEnum;
import com.wholescripts.core.enums.SapCustomerGroupEnum;
import com.wholescripts.core.enums.SapCustomerPriceGroupEnum;
import com.wholescripts.core.enums.SapDeliveryBlockEnum;
import com.wholescripts.core.enums.SapIncotermsEnum;
import com.wholescripts.core.enums.SapPaymentTermsEnum;
import com.wholescripts.core.enums.SapShippingCondEnum;
import com.wholescripts.core.service.WholescriptsRegistraionService;
import com.wholescripts.data.ReferralCodesRequestData;
import com.wholescripts.data.RegistraionResponseData;
import com.wholescripts.data.RegistrationAddressData;


/**
 *
 */
public class WholescriptsRegistraionServiceImpl implements WholescriptsRegistraionService
{
	@Resource(name = "modelService")
	private ModelService modelService;
	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18NService;
	@Resource(name = "b2bCommerceB2BUserGroupService")
	private B2BCommerceB2BUserGroupService b2bCommerceB2BUserGroupService;
	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;
	@Resource(name="userMatchingService")
	private UserMatchingService userMatchingService;
	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "sessionService")
	private SessionService sessionService;

	@Override
	public void createAccount(final RegistraionResponseData response)
	{
		final SAPConfigurationModel configModel = getSapConfiguration();
		if (Objects.nonNull(configModel))
		{
			setCurrentUser("cdcadmin");
			final B2BUnitModel parentUnit = modelService.create(B2BUnitModel.class);
			String accountNumber= response.getAccountNumber().replaceFirst("^0+", "");
			parentUnit.setUid(accountNumber);
			modelService.save(parentUnit);
			final B2BUnitModel childUnit = modelService.create(B2BUnitModel.class);
			final StringBuilder salesUnit = new StringBuilder();
			salesUnit.append(accountNumber).append("_").append(configModel.getSapcommon_salesOrganization())
					.append("_").append(configModel.getSapcommon_division()).append("_")
					.append(configModel.getSapcommon_distributionChannel());
			childUnit.setUid(salesUnit.toString());
			childUnit.setGroups(Set.of(parentUnit));
			final ReferralCodesRequestData referral = response.getReferralCodeData();
			if (Objects.nonNull(referral) && Objects.nonNull(referral.getDiscount()))
			{
				childUnit.setDiscount(String.valueOf(referral.getDiscount()));
			
			}
			if(response.getAccountType().equals("02")) {
				childUnit.setSapCustomerGroup(SapCustomerGroupEnum.valueOf("02"));
				childUnit.setUserPriceGroup(UserPriceGroup.valueOf("02"));
			}
			else
			{
				childUnit.setSapCustomerGroup(SapCustomerGroupEnum.valueOf("01"));
				childUnit.setUserPriceGroup(UserPriceGroup.valueOf("01"));
			}
			if(StringUtils.isNotEmpty(response.getSapDeliveryBlock())) {
				childUnit.setSapDeliveryBlock(SapDeliveryBlockEnum.valueOf(response.getSapDeliveryBlock()));
			}
			if(StringUtils.isNotEmpty(response.getSapPaymentTerms())) {
				childUnit.setSapPaymentTerms(SapPaymentTermsEnum.valueOf(response.getSapPaymentTerms()));
			}
			if(StringUtils.isNotEmpty(response.getSapCustomerPriceGroup())) {
				childUnit.setSapCustomerPriceGroup(SapCustomerPriceGroupEnum.valueOf(response.getSapCustomerPriceGroup()));
			}
			if(StringUtils.isNotEmpty(response.getSapShippingCond())) {
				childUnit.setSapShippingCond(SapShippingCondEnum.valueOf(response.getSapShippingCond()));
			}
			if(StringUtils.isNotEmpty(response.getSapCustomerClassfication())) {
				childUnit.setSapCustomerClassification(SapCustomerClassificationEnum.valueOf(response.getSapCustomerClassfication()));
			}
			if(StringUtils.isNotEmpty(response.getIncoTerms())) {
				childUnit.setSapIncoterms(SapIncotermsEnum.valueOf(response.getIncoTerms()));
			}
			modelService.save(childUnit);
			sessionService.removeAttribute("user");
			final AddressModel address = modelService.create(AddressModel.class);
			final RegistrationAddressData addressData = response.getAddressData();
			createAddressModel(childUnit, address, addressData);
		}

	}

	private void createAddressModel(final B2BUnitModel unit, final AddressModel address, final RegistrationAddressData addressData)
	{
		address.setOwner(unit);
		address.setLine1(addressData.getStreet());
		address.setTown(addressData.getCity());
		if (StringUtils.isNotEmpty(addressData.getCountry()) && StringUtils.isNotEmpty(addressData.getRegion()))
		{
			final CountryModel country = commonI18NService.getCountry(addressData.getCountry());
			address.setCountry(country);
			address.setRegion(commonI18NService.getRegion(country, country.getIsocode()+"-"+addressData.getRegion()));
		}
		address.setPhone1(addressData.getTelephone());
		address.setEmail(addressData.getOrdEmail());
		address.setPostalcode(addressData.getPostlCode());
		modelService.save(address);
	}

	private SAPConfigurationModel getSapConfiguration()
	{
		final BaseStoreModel baseStore = baseStoreService.getCurrentBaseStore();
		return baseStore.getSAPConfiguration();
	}
	
	protected void setCurrentUser(final String id)
	{
		try
		{
			final UserModel user = userMatchingService.getUserByProperty(id, UserModel.class);
			setCurrentUser(user);
		}
		catch (final UnknownIdentifierException ex)
		{
			throw ex;
		}
	}

	protected void setCurrentUser(final UserModel user)
	{
		userService.setCurrentUser(user);
	}

}
