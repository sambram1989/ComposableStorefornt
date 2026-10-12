package com.wholescripts.core.service.impl;

import com.wholescripts.core.dao.ReferralCodesDao;
import com.wholescripts.core.dao.WholescriptsExternalAPIDao;
import com.wholescripts.core.enums.RecommendationStatus;
import com.wholescripts.core.model.ReferralCodesModel;
import com.wholescripts.core.service.ReferralCodesService;
import com.wholescripts.core.utils.WholescriptsCoreUtils;
import com.wholescripts.data.ApiFilterData;
import com.wholescripts.data.ConfigData;
import com.wholescripts.data.FilterData;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.config.impl.DefaultConfigurationService;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Sadiq
 */
public class ReferralCodesServiceImpl implements ReferralCodesService {
    @Resource(name = "referralCodesDao")
    private ReferralCodesDao referralCodesDao;
    @Resource(name = "configurationService")
    private DefaultConfigurationService configurationService;
    @Resource(name = "enumerationService")
    private EnumerationService enumerationService;
    @Resource(name = "wholescriptsExternalAPIDao")
    private WholescriptsExternalAPIDao wholescriptsExternalAPIDao;
    @Resource(name = "b2bCustomerService")
    private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;
    @Resource(name = "searchRestrictionService")
    private SearchRestrictionService searchRestrictionService;

    @Override
    public List<ApiFilterData> getApiFilters() {
        List<ApiFilterData> apiFilters = new ArrayList<>();

        B2BCustomerModel b2BCustomer = b2bCustomerService.getCurrentB2BCustomer();

        List<B2BUnitModel> patientUnits = new ArrayList<>();

        if (b2BCustomer != null && b2BCustomer.getDefaultB2BUnit() != null) {
            final String currentB2BUnitId = b2BCustomer.getDefaultB2BUnit().getUid();
            if (Objects.nonNull(currentB2BUnitId)) {
                final String salesPersonId = WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(currentB2BUnitId);
                try {
                    searchRestrictionService.disableSearchRestrictions();
                    List<B2BUnitModel> fetchedUnits = wholescriptsExternalAPIDao.getPatients(salesPersonId);
                    if (fetchedUnits != null) {
                        patientUnits = fetchedUnits.stream().filter(Objects::nonNull).distinct().toList();
                    }
                } finally {
                    searchRestrictionService.enableSearchRestrictions();
                }
            }
        }
        
        // Add referral filters
        String referralTitle = configurationService.getConfiguration().getString("apifilter.referralcode.title");
        String referralIsOpen = configurationService.getConfiguration().getString("apifilter.referralcode.isopen", "false");
        String referralViewMore = configurationService.getConfiguration().getString("apifilter.referralcode.viewMore", "false");
        apiFilters.add(createReferralFilterSection(patientUnits, referralTitle, referralIsOpen, referralViewMore));
        // Add discount filters
        String discountTitle = configurationService.getConfiguration().getString("apifilter.discountcode.title");
        String discountIsOpen = configurationService.getConfiguration().getString("apifilter.discountcode.isopen", "false");
        String discountViewMore = configurationService.getConfiguration().getString("apifilter.discountcode.viewMore", "false");
        apiFilters.add(createDiscountFilterSection(patientUnits, discountTitle, discountIsOpen, discountViewMore));
        // Add recommendation status filters
        String recommendationTitle = configurationService.getConfiguration().getString("apifilter.recommendationstatus.title");
        String recommendationIsOpen = configurationService.getConfiguration().getString("apifilter.recommendationstatus.isopen", "false");
        String recommendationViewMore = configurationService.getConfiguration().getString("apifilter.recommendationstatus.viewMore", "false");
        apiFilters.add(createRecommendationStatusFilterSection(recommendationTitle, recommendationIsOpen, recommendationViewMore));

        return apiFilters;
    }

    // Method to create referral filter section
    private ApiFilterData createReferralFilterSection(List<B2BUnitModel> patientUnits, String title, String isOpen, String viewMore) {
        Set<String> uniqueReferrals = new HashSet<>();
        List<FilterData> referralFilters = new ArrayList<>();

        for (B2BUnitModel unit : patientUnits) {
            Set<ReferralCodesModel> referralCodes = unit.getReferralCodes();

            if (referralCodes == null || referralCodes.isEmpty()) {
                // No referral codes at all — mark as Unassigned
                FilterData unassigned = new FilterData();
                unassigned.setCode("Unassigned");
                unassigned.setValue("Unassigned");
                if (uniqueReferrals.add("Unassigned")) {
                    referralFilters.add(unassigned);
                }
            } else {
                // Process each referral code
                for (ReferralCodesModel ref : referralCodes) {
                    String referralCode = ref.getReferralCode();
                    String lastName = ref.getLastName();

                    FilterData data = new FilterData();
                    data.setCode(StringUtils.defaultIfBlank(referralCode, "Unassigned"));
                    data.setValue(StringUtils.isNotBlank(referralCode) ? referralCode + " | " + lastName : "Unassigned");

                    if (uniqueReferrals.add(data.getCode())) {
                        referralFilters.add(data);
                    }
                }
            }
        }

        referralFilters.sort((f1, f2) -> {
            if ("Unassigned".equalsIgnoreCase(f1.getCode())) {
                return -1;
            } else if ("Unassigned".equalsIgnoreCase(f2.getCode())) {
                return 1;
            } else {
                return f1.getValue().compareToIgnoreCase(f2.getValue());
            }
        });

        return createApiFilterData(title, referralFilters, isOpen, viewMore);
    }




    private ApiFilterData createDiscountFilterSection(List<B2BUnitModel> patientUnits, String title, String isOpen, String viewMore) {
        Set<String> uniqueDiscounts = new HashSet<>();
        List<FilterData> discountFilters = patientUnits.stream()
                .map(b2bUnit -> {
                    String discount = StringUtils.defaultIfBlank(b2bUnit.getDiscount(), "0"); // handles null, "", "  "
                    FilterData data = new FilterData();
                    data.setCode(discount);
                    data.setValue(discount + "%");
                    return data;
                })
                .filter(data -> uniqueDiscounts.add(data.getCode()))
                .collect(Collectors.toList());

        return createApiFilterData(title, discountFilters, isOpen, viewMore);
    }



    // Method to create recommendation status filter section
    private ApiFilterData createRecommendationStatusFilterSection(String title, String isOpen, String viewMore) {
        List<RecommendationStatus> enumValues = enumerationService.getEnumerationValues(RecommendationStatus.class);
        List<FilterData> recommendationStatusFilters = enumValues.stream()
                .map(enumValue -> {
                    FilterData data = new FilterData();
                    data.setCode(enumValue.getCode());
                    data.setValue(enumValue.getCode().replace("_", " "));
                    return data;
                })
                .collect(Collectors.toList());

        return createApiFilterData(title, recommendationStatusFilters, isOpen, viewMore);
    }

    // Helper method to create ApiFilterData
    private ApiFilterData createApiFilterData(String title, List<FilterData> filters, String isOpen, String viewMore) {
        ConfigData config = new ConfigData();
        config.setIsopen(isOpen);
        config.setViewMore(viewMore);

        ApiFilterData apiFilterData = new ApiFilterData();
        apiFilterData.setTitle(title);
        apiFilterData.setFilters(filters);
        apiFilterData.setConfig(config);
        return apiFilterData;
    }



}
