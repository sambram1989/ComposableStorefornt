/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.facade;

import com.wholescripts.data.ReferralCodeData;
import com.wholescripts.dto.referralCodes.ReferralCodeWsDTO;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import org.springframework.web.multipart.MultipartFile;
import de.hybris.platform.cmsfacades.data.MediaData;


import java.util.List;

/**
 *
 */
public interface WholescriptsPatientDirectHubFacade
{
   ReferralCodeData createReferralCodeAndPatientDirectHub(final ReferralCodeWsDTO referralCodeRequest) throws Exception;

   ReferralCodeData updateReferralCodeData(String referralCode,String referralLink, int discount) throws Exception;

   SearchPageData<ReferralCodeData> getReferralCodesData(SearchPageData searchPageData);

   List<ReferralCodeData> getReferralCodesCSV();

   MediaData uploadProfileImage(String referralCode,String lastName,MultipartFile multipart);

   AddressData addingAddressToReferralCodes(String referralCodeId, AddressData addressData);

   void removeProfileImage(String referralCode);

    void  updateAccountUpdateForPatient(ReferralCodeWsDTO referralCodeRequest, String patientId) throws Exception;

   ReferralCodeData getReferralCodeDetails(String referralCode);

   ReferralCodeData getReferralLinkDetails(String referralLink);
}
