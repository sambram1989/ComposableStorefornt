/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.wholescripts.core.model.ReferralCodesModel;
import com.wholescripts.core.utils.WholescriptsCoreUtils;
import com.wholescripts.core.utils.WholescriptsDateUtils;
import com.wholescripts.data.AccertifyVerificationAddressRequestData;
import com.wholescripts.data.AccertifyVerificationCreateAccountRequestData;
import com.wholescripts.data.AccertifyVerificationPreviousValuesData;
import com.wholescripts.data.AccertifyVerificationRequestAccountData;
import com.wholescripts.data.AccertifyVerificationUpdateAccountRequestData;
import com.wholescripts.data.AccertifyVerificationUpdatedValuesData;



public class WholescriptsAccertifyUpdateAddressPopulator implements Populator<AddressData, AccertifyVerificationUpdateAccountRequestData> {
	
	@Resource(name = "b2bCustomerService")
   private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;

	@Override
	public void populate(AddressData addressData, final AccertifyVerificationUpdateAccountRequestData accertifyRequestData)
			throws ConversionException {
		
		final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
		
		final List<AccertifyVerificationAddressRequestData> updatedAddresses = new ArrayList<AccertifyVerificationAddressRequestData>();
		final AccertifyVerificationAddressRequestData updatedAddress = new AccertifyVerificationAddressRequestData();
		final AccertifyVerificationUpdatedValuesData updatedValues = new AccertifyVerificationUpdatedValuesData();
		
		updatedValues.setEmailAddress(customer.getUid());
		updatedValues.setPhone(addressData.getPhone());
		updatedValues.setFirstName(customer.getName());
		updatedValues.setLastName("");
		updatedValues.setHashedPassword(addressData.getAccertifyHashedPassword());
		updatedValues.setUsername(customer.getUid());
		updatedValues.setHashedPassword(addressData.getAccertifyHashedPassword());
		
		updatedAddress.setAddressID(addressData.getAddressNumber());
		updatedAddress.setAddressType("shipping");
		
		if(addressData.getAddressNumber() != null && customer.getDefaultB2BUnit() != null) {
			String accountNumber = WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnitWithoutFormat(customer.getDefaultB2BUnit().getUid());
			if(addressData.getAddressNumber().equalsIgnoreCase(accountNumber)) {
				updatedAddress.setPrimaryFlag(true);
			}
		}
		updatedAddress.setFirstName(addressData.getFirstName());
		updatedAddress.setLastName(addressData.getLastName());
		updatedAddress.setEmailAddress(addressData.getEmail());
		updatedAddress.setPhone(addressData.getPhone());
		updatedAddress.setAddress1(addressData.getLine1());
		updatedAddress.setAddress2(addressData.getLine2());
		updatedAddress.setCity(addressData.getTown());
		if(addressData.getRegion() != null) {
			String regionCode = addressData.getRegion().getIsocode();
			updatedAddress.setRegion(regionCode.split("-")[1]);
		}
		
		updatedAddress.setPostCode(addressData.getPostalCode());
		
		if(addressData.getCountry() != null) {
			updatedAddress.setCountry(addressData.getCountry().getIsocode());
		}
		
		updatedAddress.setAddressUpdatedDateTime(WholescriptsDateUtils.convertDateInUTCFormat(new Date()));
		updatedAddress.setAddressCreatedDateTime(WholescriptsDateUtils.convertDateInUTCFormat(new Date()));
		if(isPractitionerUser(customer)) {
			updatedAddress.setCompanyName(addressData.getFirstName());
		} else {
			updatedAddress.setCompanyName(null);
		}
		
		updatedAddresses.add(updatedAddress);
		
		updatedValues.setAddresses(updatedAddresses);
		
		accertifyRequestData.setUpdateTrigger("customerInitiated");
		accertifyRequestData.setAccountID(WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(customer.getDefaultB2BUnit().getUid()));
		accertifyRequestData.setUpdatedValues(updatedValues);
		
		accertifyRequestData.setEventSource(addressData.getAccertifyEventSource());
		accertifyRequestData.setEmailAddress(customer.getUid());
		accertifyRequestData.setHashedPassword(addressData.getAccertifyHashedPassword());
		accertifyRequestData.setUsername(customer.getUid());
		accertifyRequestData.setBrand("WholeScripts");
		accertifyRequestData.setDeviceTransactionID(addressData.getAccertifyDeviceTransactionId());
		accertifyRequestData.setUbaID(addressData.getAccertifyUbaID());
		accertifyRequestData.setUbaEvents(addressData.getAccertifyUbaEvents());
		accertifyRequestData.setSessionID(addressData.getAccertifySessionID());
		accertifyRequestData.setIpAddress(addressData.getIpAddress());
		accertifyRequestData.setUserAgent(addressData.getUserAgent());
		accertifyRequestData.setPageID(addressData.getAccertifyPageId());
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
