package com.wholescripts.core.dao;

import com.wholescripts.core.model.ProtocolModel;
import com.wholescripts.core.model.ReferralCodesModel;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;

import java.util.List;
import java.util.Optional;

public interface ReferralCodesDao {

   public List<ReferralCodesModel> findReferralCodesByUser(String uId);
   <T extends ReferralCodesModel> SearchPageData<T>  getReferralCodesListByUser(SearchPageData pageData,String uId);

   Optional<ReferralCodesModel> getReferralCodeModelForID(String referralCode,String lastName);

   List<ReferralCodesModel> getReferralCodesCSV(String uId);

   Optional<ReferralCodesModel> getReferralLinkForID(String referralLink);
}
