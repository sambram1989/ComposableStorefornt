/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;

import com.wholescripts.core.model.ReferralCodesModel;
import com.wholescripts.core.service.WholescriptsIntegrationsService;
import com.wholescripts.core.strategies.WholescriptsMaterialExclusionStrategy;
import com.wholescripts.core.utils.WholescriptsCoreUtils;
import com.wholescripts.data.AutoShipOrderResponseData;
import com.wholescripts.data.PaymentTermsData;
import com.wholescripts.data.SapCentralDeliveryBlockData;
import com.wholescripts.data.SapOnCreditHoldData;
import com.wholescripts.data.SapCustomerClassificationData;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.model.B2BUserGroupModel;
import de.hybris.platform.b2bcommercefacades.company.converters.populators.B2BCustomerPopulator;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.util.Config;

import java.util.*;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.lang3.StringUtils;

public class WholescriptsB2BCustomerPopulator extends B2BCustomerPopulator
{
 private static String USER_GROUPS="wholescripts.user.groups";
 @Resource(name = "configurationService")
 private ConfigurationService configurationService;

	@Resource(name = "wholescriptsIntegrationsService")
	private WholescriptsIntegrationsService wholescriptsIntegrationsService;

	@Resource(name = "addressConverter")
	private Converter<AddressModel, AddressData> addressConverter;

    @Resource(name = "enumerationService")
    protected EnumerationService enumerationService;

	@Resource(name = "wsMaterialExclusionStrategy")
	private WholescriptsMaterialExclusionStrategy wsMaterialExclusionStrategy;
	@Override
	public void populate(CustomerModel source, CustomerData target) throws ConversionException
	{
		if(source instanceof B2BCustomerModel customerModel) {
            populateWSRoles(customerModel,target);
			if(target.getRoles().contains(configurationService.getConfiguration().getString("wholescripts.practitioner.group"))) {
				target.setRootUrl(configurationService.getConfiguration().getString("wholescripts.practitioner.login.url"));
			}else {
				target.setRootUrl(configurationService.getConfiguration().getString("wholescripts.patient.login.url"));
			}
			if (Objects.nonNull(target.getUnit()))
			{
				final B2BUnitData b2BUnitData=target.getUnit();
				b2BUnitData.setUid(WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(target.getUnit().getUid()));
				target.setUnit(b2BUnitData);

			}
			Set<ReferralCodesModel> b2bUnitReferralCodes = customerModel.getDefaultB2BUnit().getReferralCodes();
			if (b2bUnitReferralCodes != null && !b2bUnitReferralCodes.isEmpty()) {
				List<ReferralCodesModel> referralCodesList = new ArrayList<>(b2bUnitReferralCodes);
				String firstReferralCode = referralCodesList.get(0).getReferralCode();
				String lastName = referralCodesList.get(0).getLastName();
				target.setReferralCode(firstReferralCode.concat("|").concat(lastName));
			}
			final B2BUnitModel defaultB2BUnit = customerModel.getDefaultB2BUnit();
			if (Objects.nonNull(defaultB2BUnit)) {
				target.setReceiveConsultationFee(defaultB2BUnit.getReceiveConsultationFee());
				target.setPerosnaExpirationDate(defaultB2BUnit.getAgeExpirationDate());
				target.setPersonaBlockedDate(defaultB2BUnit.getAgeBlockedDate());
				target.setDiscount(defaultB2BUnit.getDiscount());

				if(Objects.nonNull(defaultB2BUnit.getCreditLimit()))
				{
					target.setCreditAmount(String.valueOf(defaultB2BUnit.getCreditLimit().getAmount()));
					target.setCreditUsed(defaultB2BUnit.getCreditLimit().getCreditUsed());
				}

                if (Objects.nonNull(defaultB2BUnit.getSapPaymentTerms()))
                {
					PaymentTermsData paymentTerms=new PaymentTermsData();
					String paymentTermsName=enumerationService.getEnumerationName(defaultB2BUnit.getSapPaymentTerms());
					paymentTerms.setCode(defaultB2BUnit.getSapPaymentTerms().getCode());
					paymentTerms.setName(paymentTermsName);
					target.setPaymentTerms(paymentTerms);
				}
				if (Objects.nonNull(target.getUnit()))
				{
					String accountNumber =WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnitWithoutFormat(defaultB2BUnit.getUid());
					AddressModel addressModel;
					Optional<AddressModel> defaultAddress= defaultB2BUnit.getAddresses().stream().filter(a -> a.getSapPublicKey()!=null && a.getSapPublicKey().contains(accountNumber)).findFirst();
					if (defaultAddress.isPresent())
					{
						addressModel=defaultAddress.get();
						target.setDefaultShippingAddress(addressConverter.convert(addressModel));
						target.setDefaultAddress(addressConverter.convert(addressModel));
						target.setContactNumber(addressModel.getPhone1());

					}
				}
				populateCentralDeliveryBlock(customerModel.getDefaultB2BUnit(),target);
				populateOnCreditHold(customerModel.getDefaultB2BUnit(),target);
				populateCustomerClassification(customerModel.getDefaultB2BUnit(),target);
				String accountNumber=WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(defaultB2BUnit.getUid());
				if(StringUtils.isNotBlank(accountNumber))
				{
					target.setHideMedpaxPage(wsMaterialExclusionStrategy.isMedpaxExcluedForCustomer(accountNumber));

				}

			}
			if(Objects.nonNull(customerModel.getIsEmailNotificationEnabled()))
			{
				target.setIsEmailNotificationEnabled(customerModel.getIsEmailNotificationEnabled());

			}
			if(Objects.nonNull(customerModel.getIsSmsNotificationEnabled()))
			{
				target.setIsSmsNotificationEnabled(customerModel.getIsSmsNotificationEnabled());

			}

			/* Removing for integration logs issue
			try {
				AutoShipOrderResponseData autoShipResponse = wholescriptsIntegrationsService.getAutoShipOrders("");
				if (Objects.nonNull(autoShipResponse) && Objects.nonNull(autoShipResponse.getAutoShipsHdr()) && Objects.nonNull(autoShipResponse.getAutoShipsHdr().getItem())
						&& !autoShipResponse.getAutoShipsHdr().getItem().isEmpty()) {
					target.setAutoShips(true);
				}
			} catch (Exception ex) {
				target.setAutoShips(false); // Default to false if the logic fails
			}*/
			target.setAutoShips(Boolean.TRUE);
		}
	}

	private void populateCustomerClassification(B2BUnitModel source, CustomerData target) {

		if (Objects.nonNull(source.getSapCustomerClassification()))
		{
			SapCustomerClassificationData data = new SapCustomerClassificationData();
			String onCreditHoldName = enumerationService.getEnumerationName(source.getSapCustomerClassification());
			data.setName(onCreditHoldName);
			if(null != source.getSapOnCreditHold()) {
				data.setCode(source.getSapOnCreditHold().getCode());
			}
			target.setSapCustomerClassification(data);
		}
	}

	protected void populateWSRoles(final B2BCustomerModel source, final CustomerData target)
	{
		final Set<String> roles = new HashSet<String>();
		final Set<PrincipalGroupModel> roleModels = new HashSet<>(source.getGroups());
		CollectionUtils.filter(roleModels, PredicateUtils.notPredicate(PredicateUtils.instanceofPredicate(B2BUnitModel.class)));
		CollectionUtils.filter(roleModels,
				PredicateUtils.notPredicate(PredicateUtils.instanceofPredicate(B2BUserGroupModel.class)));
	    String wsGroups=Config.getParameter(USER_GROUPS);
	    List<String> allowedGroups = new ArrayList<>(getB2BUserGroupsLookUpStrategy().getUserGroups());
	    if(StringUtils.isNotEmpty(wsGroups) && wsGroups.contains(",")) {
	   	 allowedGroups.addAll( Arrays.asList(wsGroups.split(","))); 
	    }else {
	   	 allowedGroups.add(wsGroups);
	    }
	   
		for (final PrincipalGroupModel role : roleModels)
		{
			
			// only display allowed usergroups
			if (allowedGroups.contains(role.getUid()))
			{
				roles.add(role.getUid());
				for(final PrincipalGroupModel parentRole : role.getGroups()) {
					roles.add(parentRole.getUid());
				}
			}
		}
		target.setRoles(roles);
	}

	protected void populateCentralDeliveryBlock(B2BUnitModel source, CustomerData target)
	{
		if (Objects.nonNull(source.getSapCentralDeliveryBlock()))
		{
			SapCentralDeliveryBlockData data = new SapCentralDeliveryBlockData();
			String centralDeliveryBlockName = enumerationService.getEnumerationName(source.getSapCentralDeliveryBlock());
			data.setName(centralDeliveryBlockName);
			data.setCode(source.getSapCentralDeliveryBlock().getCode());
			target.setSapCentralDeliveryBlock(data);
		}
	}

	protected void populateOnCreditHold(B2BUnitModel source, CustomerData target)
	{
		if (Objects.nonNull(source.getSapOnCreditHold()))
		{
			SapOnCreditHoldData data = new SapOnCreditHoldData();
			String onCreditHoldName = enumerationService.getEnumerationName(source.getSapOnCreditHold());
			data.setName(onCreditHoldName);
			data.setCode(source.getSapOnCreditHold().getCode());
			target.setSapOnCreditHold(data);
		}
	}
}
