package com.wholescripts.core.process.email.context;


import com.wholescripts.core.model.RecommendationModel;
import com.wholescripts.core.model.ReferralCodesModel;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import org.apache.commons.collections.CollectionUtils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public  class WsShareRecommendationEmailContext extends AbstractEmailContext<StoreFrontCustomerProcessModel> {
    public static final String RECOMMENDATION_ID = "recommendation";

    @Override
    protected BaseSiteModel getSite(StoreFrontCustomerProcessModel businessProcessModel) {
        return businessProcessModel.getSite();
    }

    @Override
    protected CustomerModel getCustomer(StoreFrontCustomerProcessModel businessProcessModel) {
        return businessProcessModel.getCustomer();
    }

    @Override
    protected LanguageModel getEmailLanguage(StoreFrontCustomerProcessModel businessProcessModel) {
        return businessProcessModel.getLanguage();
    }

    @Override
    public void init(StoreFrontCustomerProcessModel businessProcessModel, EmailPageModel emailPageModel) {
        super.init(businessProcessModel, emailPageModel);
       RecommendationModel  recommendation=businessProcessModel.getRecommendation();
        put(RECOMMENDATION_ID, recommendation.getCode());
        put("email", recommendation.getPatient().getUid());
        put("fromDisplayName", recommendation.getUser().getName());
        put("displayName", recommendation.getPatient().getName());
        B2BCustomerModel practitioner= (B2BCustomerModel) recommendation.getUser();
        final B2BUnitModel defaultB2BUnit=practitioner.getDefaultB2BUnit();
        Set<ReferralCodesModel> practitionerReferralCodes=new HashSet<>();
        Set<ReferralCodesModel> patientReferralCodes=new HashSet<>();

       if(Objects.nonNull(defaultB2BUnit)) {
            practitionerReferralCodes =defaultB2BUnit.getReferralCodes();

       }
        final B2BUnitModel patientB2BUnit=recommendation.getPatient().getDefaultB2BUnit();
        if(Objects.nonNull(patientB2BUnit)) {
             patientReferralCodes =patientB2BUnit.getReferralCodes();
        }
        if(CollectionUtils.isNotEmpty(practitionerReferralCodes)&& CollectionUtils.isNotEmpty(patientReferralCodes))
        {
            Set<ReferralCodesModel> finalPatientReferralCodes = patientReferralCodes;
            Optional<ReferralCodesModel> referralCodes = practitionerReferralCodes.stream()
                    .filter(ref1 -> finalPatientReferralCodes.stream()
                            .anyMatch(ref2 -> ref1.getReferralCode().equals(ref2.getReferralCode()) && ref1.getLastName().equals(ref2.getLastName()))).findFirst();
            if(referralCodes.isPresent())
            {
                ReferralCodesModel referralCodesModel= referralCodes.get();
                put("referralCode", referralCodesModel.getReferralCode());
                put("referralLastName", referralCodesModel.getLastName());
            }

        }

        put(BASE_URL, getSiteBaseUrlResolutionService().getWebsiteUrlForSite(getSite(businessProcessModel), "", true, ""));
    }
}
