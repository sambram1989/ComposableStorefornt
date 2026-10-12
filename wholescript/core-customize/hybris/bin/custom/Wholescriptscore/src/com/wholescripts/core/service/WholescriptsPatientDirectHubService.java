/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service;

import com.wholescripts.core.model.ReferralCodesModel;
import com.wholescripts.dto.referralCodes.ReferralCodeWsDTO;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;


/**
 *
 */
public interface WholescriptsPatientDirectHubService
{
    ReferralCodesModel createReferralCodeAndPatientDirectHub(ReferralCodeWsDTO referralCodeRequest) throws Exception;

    SearchPageData<ReferralCodesModel> getReferralCodesData(SearchPageData searchPageData,String uId);

    ReferralCodesModel updateReferralCodeData(String referralCode,String referralLink, int discount) throws Exception;

    List<ReferralCodesModel> getReferralCodesCSV();

    ReferralCodesModel uploadProfileImage(String referralCode, String lastName, B2BCustomerModel customerForProfileImage, MultipartFile imagefile);

     AddressModel addingAddressToReferralCodes(String referralCodeId, AddressData addressData);

    void removeProfileImage(String referralCode);

    void updateAccountUpdateForPatient(ReferralCodeWsDTO referralCodeRequest, String patientId) throws Exception;

    ReferralCodesModel getReferralCodeDetails(String referralCode);

    ReferralCodesModel getReferralLinkDetails(String referralLink);
}
