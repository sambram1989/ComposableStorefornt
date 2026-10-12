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



public class WholescriptsAccertifyOldAddressPopulator implements Populator<AddressModel, AccertifyVerificationUpdateAccountRequestData> {
	
	@Resource(name = "b2bCustomerService")
   private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;

	@Override
	public void populate(AddressModel addressModel, final AccertifyVerificationUpdateAccountRequestData accertifyRequestData)
			throws ConversionException {
		
		final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
		
		final List<AccertifyVerificationAddressRequestData> previousAddresses = new ArrayList<AccertifyVerificationAddressRequestData>();
		final AccertifyVerificationAddressRequestData previousAddress = new AccertifyVerificationAddressRequestData();
		final AccertifyVerificationPreviousValuesData previousValues = new AccertifyVerificationPreviousValuesData();
		
		previousValues.setEmailAddress(customer.getUid());
		previousValues.setPhone(addressModel.getPhone1());
		previousValues.setFirstName(customer.getName());
		previousValues.setLastName("");
		previousValues.setHashedPassword(null);
		previousValues.setUsername(customer.getUid());
		
		if (addressModel.getSapPublicKey() != null && addressModel.getSapPublicKey().contains("|")) {
			previousAddress.setAddressID(addressModel.getSapPublicKey().split("\\|")[1]);
		}
		
		if (addressModel.getOwner() instanceof B2BUnitModel) {
          B2BUnitModel b2BUnitModel = (B2BUnitModel) addressModel.getOwner();
          if (null != b2BUnitModel.getSapCustomerGroup()) {
         	 previousAddress.setAddressType("shipping");
          }
      }
		
		String sapCustomerId = addressModel.getSapCustomerID();
		
		if(customer.getDefaultB2BUnit() != null) {
			
			String accountNumber = WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnitWithoutFormat(customer.getDefaultB2BUnit().getUid());
			
			if(sapCustomerId.equalsIgnoreCase(accountNumber)) {
				previousAddress.setPrimaryFlag(false);
			}
		}
		
		previousAddress.setFirstName(addressModel.getFirstname());
		previousAddress.setLastName(addressModel.getLastname());
		previousAddress.setEmailAddress(addressModel.getEmail());
		previousAddress.setPhone(addressModel.getPhone1());
		previousAddress.setAddress1(addressModel.getLine1());
		previousAddress.setAddress2(addressModel.getLine2());
		previousAddress.setCity(addressModel.getTown());
		if(addressModel.getRegion() != null) {
			String regionCode = addressModel.getRegion().getIsocode();
			previousAddress.setRegion(regionCode.split("-")[1]);
			
		}
		previousAddress.setPostCode(addressModel.getPostalcode());
		if(addressModel.getCountry() != null) {
			previousAddress.setCountry(addressModel.getCountry().getIsocode());
			
		}
		
		previousAddress.setAddressUpdatedDateTime(WholescriptsDateUtils.convertDateInUTCFormat(new Date()));
		previousAddress.setAddressCreatedDateTime(WholescriptsDateUtils.convertDateInUTCFormat(addressModel.getCreationtime()));
		
		if(isPractitionerUser(customer)) {
			previousAddress.setCompanyName(addressModel.getFirstname());
		} else {
			previousAddress.setCompanyName(null);
		}
		
		previousAddresses.add(previousAddress);
		previousValues.setAddresses(previousAddresses);
		
		accertifyRequestData.setPreviousValues(previousValues);
		
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
