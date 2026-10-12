/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.facade.impl;

import com.wholescripts.core.facade.WholescriptsPatientDirectHubFacade;
import com.wholescripts.core.service.WholescriptsPatientDirectHubService;
import com.wholescripts.dto.referralCodes.ReferralCodeWsDTO;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import org.springframework.web.multipart.MultipartFile;
import de.hybris.platform.cmsfacades.data.MediaData;


import javax.annotation.Resource;

import com.wholescripts.core.model.ReferralCodesModel;
import com.wholescripts.data.ReferralCodeData;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

/**
 *
 */
public class WholescriptsPatientDirectHubFacadeImpl implements WholescriptsPatientDirectHubFacade {
	@Resource(name = "wholescriptsPatientDirectHubService")
	private WholescriptsPatientDirectHubService wholescriptsPatientDirectHubService;
	@Resource(name = "wsPatientDirectHubConverter")
	private Converter<ReferralCodesModel, ReferralCodeData> wsPatientDirectHubConverter;

	@Resource(name = "b2bCustomerService")
	private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;
	@Resource(name = "addressConverter")
	private Converter<AddressModel, AddressData> addressConverter;

	@Override
	public ReferralCodeData createReferralCodeAndPatientDirectHub(ReferralCodeWsDTO referralCodeRequest) throws Exception {
		ReferralCodesModel referralCode = wholescriptsPatientDirectHubService.createReferralCodeAndPatientDirectHub(referralCodeRequest);
		return wsPatientDirectHubConverter.convert(referralCode);
	}

	@Override
	public SearchPageData<ReferralCodeData> getReferralCodesData(final SearchPageData searchPageData) {
		final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
		final SearchPageData<ReferralCodesModel> searchData = wholescriptsPatientDirectHubService.getReferralCodesData(searchPageData, customer.getUid());
		final SearchPageData<ReferralCodeData> result = new SearchPageData();
		final List<ReferralCodeData> referralCodeDataList = searchData.getResults().stream()
				.map(this::getreferralCodeData).filter(Optional::isPresent).map(Optional::get)
				.collect(toList());
		result.setResults(referralCodeDataList);
		result.setPagination(searchData.getPagination());
		result.setSorts(searchData.getSorts());
		return result;
	}

	@Override
	public ReferralCodeData updateReferralCodeData(final String referralCode, final String referralLink, final int discount) throws Exception {
		ReferralCodesModel referralCodesData = wholescriptsPatientDirectHubService.updateReferralCodeData(referralCode, referralLink, discount);
		return wsPatientDirectHubConverter.convert(referralCodesData);
	}


	public Optional<ReferralCodeData> getreferralCodeData(final ReferralCodesModel referralCodesModel) {
		return Optional.ofNullable(wsPatientDirectHubConverter.convert(referralCodesModel));
	}

	@Override
	public List<ReferralCodeData> getReferralCodesCSV() {
		List<ReferralCodesModel> referralCodesModel = wholescriptsPatientDirectHubService.getReferralCodesCSV();
		return wsPatientDirectHubConverter.convertAll(referralCodesModel);
	}

	@Override
	public MediaData uploadProfileImage(String referralCode, String lastName, MultipartFile multipart) {
		final B2BCustomerModel customerForProfileImage = b2bCustomerService.getCurrentB2BCustomer();
		ReferralCodesModel mediaForReferralCodes = wholescriptsPatientDirectHubService.uploadProfileImage(referralCode, lastName, customerForProfileImage, multipart);
		MediaData mediaData = new MediaData();
		if (mediaForReferralCodes != null && mediaForReferralCodes.getProfileImage() != null) {
			MediaModel media = mediaForReferralCodes.getProfileImage();

			mediaData.setAltText(media.getAltText());
			mediaData.setCode(media.getCode());
			mediaData.setDescription(media.getDescription());
			mediaData.setDownloadUrl(media.getDownloadURL());
			mediaData.setMime(media.getMime());
			mediaData.setUrl(media.getURL());
		} else {
			throw new IllegalStateException("Profile image upload failed or no image associated with the referral code");
		}

		return mediaData;
	}

	@Override
	public AddressData addingAddressToReferralCodes(String referralCodeId, AddressData addressData) {
		AddressModel addressModel = wholescriptsPatientDirectHubService.addingAddressToReferralCodes(referralCodeId, addressData);
		return addressConverter.convert(addressModel);
	}

	@Override
	public void removeProfileImage(String referralCode){
        wholescriptsPatientDirectHubService.removeProfileImage(referralCode);
    }

	@Override
	public void updateAccountUpdateForPatient(ReferralCodeWsDTO referralCodeRequest, String patientId) throws Exception {
		wholescriptsPatientDirectHubService.updateAccountUpdateForPatient(referralCodeRequest, patientId);
	}

	@Override
	public ReferralCodeData getReferralCodeDetails(String referralCode) {
		return wsPatientDirectHubConverter.convert(wholescriptsPatientDirectHubService.getReferralCodeDetails(referralCode));
	}

	@Override
	public ReferralCodeData getReferralLinkDetails(String referralLink) {
		return wsPatientDirectHubConverter.convert(wholescriptsPatientDirectHubService.getReferralLinkDetails(referralLink));
	}

}
