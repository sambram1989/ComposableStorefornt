/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service.impl;

import de.hybris.platform.commerceservices.address.AddressErrorCode;
import de.hybris.platform.commerceservices.address.AddressFieldType;
import de.hybris.platform.commerceservices.address.AddressVerificationDecision;
import de.hybris.platform.commerceservices.address.data.AddressFieldErrorData;
import de.hybris.platform.commerceservices.address.data.AddressVerificationResultData;
import de.hybris.platform.commerceservices.address.impl.DefaultAddressVerificationService;
import de.hybris.platform.commerceservices.address.util.AddressVerificationResultUtils;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.wholescripts.core.service.WholescriptsIntegrationsService;
import com.wholescripts.data.AddressKeyFormatData;
import com.wholescripts.data.AddressVerificationRequestData;
import com.wholescripts.data.AddressVerificationResponseData;
import com.wholescripts.data.CandidateResponseData;
import com.wholescripts.data.USPSAddressData;
import com.wholescripts.data.USPSAddressValidateResponseData;
import com.wholescripts.data.requestOptionData;
import com.wholescripts.data.xavRequestData;
import com.wholescripts.data.xavResponseData;

/**
 *
 */
public class WholescriptsAddressVerificationService extends DefaultAddressVerificationService
{
	public static final String UNKNOWN= "unknown";
	@Resource(name = "wholescriptsIntegrationsService")
	private WholescriptsIntegrationsService wholescriptsIntegrationsService;
	@Resource(name = "modelService")
	private ModelService modelService;
	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18NService;
	boolean isSameAddress = false;
	
	@Override
	public AddressVerificationResultData<AddressVerificationDecision, AddressFieldErrorData<AddressFieldType, AddressErrorCode>> verifyAddress(
			AddressModel addressModel)
	{
		final AddressVerificationResultData<AddressVerificationDecision, AddressFieldErrorData<AddressFieldType, AddressErrorCode>> acceptedResult = createVerificationResult();
		validateAddressFields(acceptedResult, addressModel);
		if (AddressVerificationResultUtils.requiresErrorHandling(acceptedResult))
		{
			acceptedResult.setDecision(AddressVerificationDecision.lookup(REJECT));
		}
		else
		{
			try
			{  
				AddressVerificationRequestData verificationData = createRequest(addressModel);
				//USP address veification
				AddressVerificationResponseData response =wholescriptsIntegrationsService.verifyAddressFromUPS(verificationData);
				final List<AddressModel> suggestedAddresses = new ArrayList<AddressModel>();
				xavResponseData rs= response.getXavResponse();
				if(Objects.nonNull(rs.getCandidate())) {
				for(CandidateResponseData responseAddress : rs.getCandidate()) {
					final AddressModel address = modelService.create(AddressModel.class);
					AddressKeyFormatData formatData = responseAddress.getAddressKeyFormat();
					StringBuilder line1 = new StringBuilder();
					if(StringUtils.isNotEmpty(addressModel.getLine2())) {
						address.setLine1(formatData.getAddressLine().get(0));
					}else {
						for(String line : formatData.getAddressLine()) {
							line1.append(line);
							line1.append(" ");
						}
						address.setLine1(line1.substring(0,line1.length() - 1).toString());
					}
					address.setTown(formatData.getPoliticalDivision2());
					if(StringUtils.isNotEmpty(formatData.getCountryCode()) && StringUtils.isNotEmpty(formatData.getPoliticalDivision1())) {
						CountryModel country = commonI18NService.getCountry(formatData.getCountryCode());
						address.setCountry(country);
						address.setRegion(commonI18NService.getRegion(country, country.getIsocode()+"-"+formatData.getPoliticalDivision1()));
						address.setPostalcode(formatData.getPostcodePrimaryLow()+"-"+formatData.getPostcodeExtendedLow());
						suggestedAddresses.add(address);
					}
				}
				}
				String uspsRequest = createXMLRequest(addressModel);
				//USPS address verification
				USPSAddressValidateResponseData responseData =	wholescriptsIntegrationsService.verifyAddressFromUSPS(uspsRequest);
				USPSAddressData uspsAddress = responseData.getAddressValidateResponse().getAddress();
				//compare USP and USPA addresses
				addUSPSAddressINSuggestedAddressList(addressModel, suggestedAddresses, rs, uspsAddress);
				if(CollectionUtils.isNotEmpty(suggestedAddresses) && StringUtils.isNotEmpty(addressModel.getLine2())) {
					suggestedAddresses.forEach(a -> {
						a.setLine2(addressModel.getLine2());
					});
				}
				acceptedResult.setSuggestedAddresses(suggestedAddresses);
				acceptedResult.setDecision(AddressVerificationDecision.lookup(ACCEPT));
				if(CollectionUtils.isEmpty(suggestedAddresses)) {
					acceptedResult.setDecision(AddressVerificationDecision.lookup(UNKNOWN));
				}
				return acceptedResult;
			}
			catch (Exception e)
			{
				throw new RequestParameterException(String.format(e.getMessage()), RequestParameterException.INVALID);
			}
		}
		return acceptedResult;
	}

	private void addUSPSAddressINSuggestedAddressList(AddressModel addressModel, final List<AddressModel> suggestedAddresses,
			xavResponseData rs, USPSAddressData uspsAddress)
	{
		if(Objects.nonNull(uspsAddress) && StringUtils.isNotEmpty(uspsAddress.getAddress2())) {
			if(Objects.nonNull(rs.getCandidate())) {
			rs.getCandidate().stream().filter(c -> ! isSameAddress).forEach(a -> {
				isSameAddress = compareAddresses(a,uspsAddress);
			});
			}
			if(!isSameAddress) {
				final AddressModel uspsAddressModel = modelService.create(AddressModel.class);
				uspsAddressModel.setLine1(uspsAddress.getAddress2());
				uspsAddressModel.setTown(uspsAddress.getCity());
				if(Objects.nonNull(addressModel.getCountry()) && StringUtils.isNotEmpty(uspsAddress.getState())) {
					CountryModel country = addressModel.getCountry();
					uspsAddressModel.setCountry(country);
					uspsAddressModel.setRegion(commonI18NService.getRegion(country, country.getIsocode()+"-"+uspsAddress.getState()));
				}
				uspsAddressModel.setPostalcode(uspsAddress.getZip5()+"-"+uspsAddress.getZip4());
				suggestedAddresses.add(uspsAddressModel);
			}
		} 
		isSameAddress= false;
	}
	
	private AddressVerificationRequestData createRequest(AddressModel addressModel)
	{
		AddressVerificationRequestData verificationData = new AddressVerificationRequestData();
		xavRequestData request = new xavRequestData();
		requestOptionData rq = 	new requestOptionData();
		rq.setRequestOption("1");
		request.setRequest(rq);
		AddressKeyFormatData format = new AddressKeyFormatData();
		format.setAddressLine(Arrays.asList(addressModel.getLine1()));
		format.setConsigneeName(StringUtils.EMPTY);
		format.setBuildingName(StringUtils.EMPTY);
		format.setPoliticalDivision1(addressModel.getRegion().getIsocode());
		format.setPoliticalDivision2(addressModel.getTown());
		format.setPostcodePrimaryLow(addressModel.getPostalcode());
		format.setCountryCode(addressModel.getCountry().getIsocode());
		request.setAddressKeyFormat(format);
		verificationData.setXavRequest(request);
		return verificationData;
	}

	protected void validateAddressFields(
			final AddressVerificationResultData<AddressVerificationDecision, AddressFieldErrorData<AddressFieldType, AddressErrorCode>> result,
			final AddressModel address)
	{
		final List<AddressFieldErrorData<AddressFieldType, AddressErrorCode>> errorList = new ArrayList<AddressFieldErrorData<AddressFieldType, AddressErrorCode>>();
		if (StringUtils.isEmpty(address.getLine1()))
		{
			addErrorToVerificationResult(ADDRESS_LINE_1, MISSING, errorList);
		}
		else if (StringUtils.length(address.getLine1()) > 255)
		{
			addErrorToVerificationResult(ADDRESS_LINE_1, INVALID, errorList);
		}
		if (StringUtils.isEmpty(address.getTown()))
		{
			addErrorToVerificationResult(CITY, MISSING, errorList);
		}
		else if (StringUtils.length(address.getTown()) > 255)
		{
			addErrorToVerificationResult(CITY, INVALID, errorList);
		}
		if (address.getRegion() != null && address.getRegion().getIsocode() == null)
		{
			addErrorToVerificationResult(REGION, MISSING, errorList);
		}
		else if (address.getRegion() != null && StringUtils.length(address.getRegion().getIsocode()) > 255)
		{
			addErrorToVerificationResult(REGION, INVALID, errorList);
		}
		if (StringUtils.isEmpty(address.getPostalcode()))
		{
			addErrorToVerificationResult(ZIP_CODE, MISSING, errorList);
		}
		else if (StringUtils.length(address.getPostalcode()) > 10)
		{
			addErrorToVerificationResult(ZIP_CODE, INVALID, errorList);
		}
		if (address.getCountry() == null || (address.getCountry() != null && address.getCountry().getIsocode() == null))
		{
			addErrorToVerificationResult(COUNTRY, MISSING, errorList);
		}
		else if (StringUtils.length(address.getCountry().getIsocode()) > 255)
		{
			addErrorToVerificationResult(COUNTRY, INVALID, errorList);
		}
		result.setFieldErrors(errorList);
	}
	
	private String createXMLRequest(final AddressModel model)
	{
		StringBuilder xml = new StringBuilder();
		String regionIsoCode = model.getRegion().getIsocode();
		if (regionIsoCode.contains("-")) {
			regionIsoCode = regionIsoCode.split("-")[1];
		}
		
		xml.append("<AddressValidateRequest USERID='591XYMOG4891'>");
		xml.append("<Revision>1</Revision>");
		xml.append("<Address ID='0'>");
		xml.append("<Address1></Address1>");
		xml.append("<Address2>");
		xml.append(model.getLine1());
		xml.append("</Address2>");
		xml.append("<City>");
		xml.append(model.getTown());
		xml.append("</City>");
		xml.append("<State>");
		xml.append(regionIsoCode);
		xml.append("</State>");
		xml.append("<Zip5>");
		xml.append(model.getPostalcode());
		xml.append("</Zip5>");
		xml.append("<Zip4>");
		xml.append("");
		xml.append("</Zip4>");
		xml.append("</Address>");
		xml.append("</AddressValidateRequest>");
		return xml.toString();
	}
	
	private boolean compareAddresses(CandidateResponseData upsaddress, USPSAddressData usps) {	
		AddressKeyFormatData ups = upsaddress.getAddressKeyFormat();
		if ((CollectionUtils.isNotEmpty(ups.getAddressLine())&& ups.getAddressLine().get(0).equalsIgnoreCase(usps.getAddress2()) && ups.getPoliticalDivision2().equalsIgnoreCase(usps.getCity())
				&& ups.getPoliticalDivision1().equalsIgnoreCase(usps.getState()) && ups.getPostcodePrimaryLow().equalsIgnoreCase(usps.getZip5())
				&& ups.getPostcodeExtendedLow().equalsIgnoreCase(usps.getZip4())))
		{
         return true;
		}
    return false;
	}

}
