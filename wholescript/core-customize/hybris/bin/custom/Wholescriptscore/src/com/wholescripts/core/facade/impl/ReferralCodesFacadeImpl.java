package com.wholescripts.core.facade.impl;

import com.wholescripts.core.facade.ReferralCodesFacade;
import com.wholescripts.core.service.ReferralCodesService;
import com.wholescripts.data.ApiFilterData;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Sadiq
 */
public class ReferralCodesFacadeImpl implements ReferralCodesFacade {

    @Resource(name = "referralCodesService")
    private ReferralCodesService referralCodesService;

    @Override
    public List<ApiFilterData> getApiFilters() {
        List<ApiFilterData> apiFilters = referralCodesService.getApiFilters();
        return apiFilters;
    }
}
