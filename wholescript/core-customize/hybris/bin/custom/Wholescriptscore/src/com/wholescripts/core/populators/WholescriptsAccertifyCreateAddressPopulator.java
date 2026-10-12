/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.wholescripts.core.model.ReferralCodesModel;
import com.wholescripts.core.utils.WholescriptsDateUtils;
import com.wholescripts.data.AccertifyVerificationAddressRequestData;
import com.wholescripts.data.AccertifyVerificationCreateAccountRequestData;
import com.wholescripts.data.AccertifyVerificationRequestAccountData;



public class WholescriptsAccertifyCreateAddressPopulator implements Populator<AddressData, AccertifyVerificationCreateAccountRequestData> {
	
	@Resource(name = "b2bCustomerService")
   private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;

	@Override
	public void populate(final AddressData addressData, final AccertifyVerificationCreateAccountRequestData accertifyRequestData)
			throws ConversionException {
		
		final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
		
		accertifyRequestData.setSuccess("true");
		accertifyRequestData.setFailureCode(null);
		accertifyRequestData.setFirstName(addressData.getFirstName());
		
		if (customer != null) {
			accertifyRequestData.setLastName(addressData.getLastName());
		}
		
		if(isPractitionerUser(customer)) {
			accertifyRequestData.setLastName(null);
		} else {
			accertifyRequestData.setLastName(addressData.getLastName());
		}
		
		final List<AccertifyVerificationAddressRequestData> accertifyAddresses = new ArrayList<AccertifyVerificationAddressRequestData>();
		
		final AccertifyVerificationAddressRequestData accertifyAddress = new AccertifyVerificationAddressRequestData();
		
		accertifyAddress.setAddressID(addressData.getAddressNumber());
		accertifyAddress.setAddressType(addressData.getAddressType());
		accertifyAddress.setPrimaryFlag(false);
		accertifyAddress.setFirstName(addressData.getFirstName());
		accertifyAddress.setLastName(addressData.getLastName());
		if(StringUtils.isNotEmpty(addressData.getEmail())) {
		accertifyAddress.setEmailAddress(addressData.getEmail());
		}
		accertifyAddress.setPhone(addressData.getPhone());
		accertifyAddress.setAddress1(addressData.getLine1());
		accertifyAddress.setAddress2(addressData.getLine2());
		accertifyAddress.setCity(addressData.getTown());
		if (addressData.getRegion() != null) {
			String regionCode = addressData.getRegion().getIsocode();
			accertifyAddress.setRegion(regionCode.split("-")[1]);
		}
		accertifyAddress.setPostCode(addressData.getPostalCode());
		if(addressData.getCountry() != null) {
			accertifyAddress.setCountry(addressData.getCountry().getIsocode());
		}
		accertifyAddress.setAddressCreatedDateTime(WholescriptsDateUtils.convertDateInUTCFormat(new Date()));
		accertifyAddress.setAddressUpdatedDateTime(WholescriptsDateUtils.convertDateInUTCFormat(new Date()));
		if(isPractitionerUser(customer)) {
			accertifyAddress.setCompanyName(addressData.getFirstName());
		} else {
			accertifyAddress.setCompanyName(null);
		}
		
		accertifyAddresses.add(accertifyAddress);
		
		accertifyRequestData.setAddresses(accertifyAddresses);
		
		final AccertifyVerificationRequestAccountData accountData = new AccertifyVerificationRequestAccountData();

		accountData.setAccountLocale("en-US");
		accountData.setAccountLevel("Standard");
		if(isPractitionerUser(customer)) {
			accountData.setAccountType("Business");
			accountData.setAccountReferralCode(null);
		} else {
			accountData.setAccountType("Individual");
			if (customer.getDefaultB2BUnit() != null 
					&& CollectionUtils.isNotEmpty(customer.getDefaultB2BUnit().getReferralCodes())) {
				ReferralCodesModel referralCodeModel = customer.getDefaultB2BUnit().getReferralCodes().iterator().next();
				if (referralCodeModel != null) {
					accountData.setAccountReferralCode(referralCodeModel.getReferralCode() + "|" + referralCodeModel.getLastName());
				}
			}
			
		}
		
		accountData.setAccountRegistrationDateTime(WholescriptsDateUtils.convertDateInUTCFormat(new Date()));
		accertifyRequestData.setAccount(accountData);
		
		accertifyRequestData.setEventSource(addressData.getAccertifyEventSource());
		if(StringUtils.isNotEmpty(addressData.getEmail())) {
			accertifyRequestData.setEmailAddress(addressData.getEmail());
		}
		accertifyRequestData.setHashedPassword(addressData.getAccertifyHashedPassword());
		accertifyRequestData.setUsername(customer.getUid());
		accertifyRequestData.setBrand("WholeScripts");
		accertifyRequestData.setDeviceTransactionID(addressData.getAccertifyDeviceTransactionId());
		accertifyRequestData.setUbaID(addressData.getAccertifyUbaID());
		accertifyRequestData.setUbaEvents(addressData.getAccertifyUbaEvents());
		accertifyRequestData.setSessionID(addressData.getAccertifySessionID());
		accertifyRequestData.setIpAddress(addressData.getIpAddress());
		accertifyRequestData.setUserAgent(addressData.getUserAgent());
	}
	
	private boolean isPractitionerUser(final B2BCustomerModel customer) {
		
		if (customer != null && customer.getDefaultB2BUnit() != null && 
				customer.getDefaultB2BUnit().getSapCustomerGroup() != null && 
				customer.getDefaultB2BUnit().getSapCustomerGroup().getCode().equalsIgnoreCase("01")) {
			return true;
		} else {
			return false;
		}
	}

}
