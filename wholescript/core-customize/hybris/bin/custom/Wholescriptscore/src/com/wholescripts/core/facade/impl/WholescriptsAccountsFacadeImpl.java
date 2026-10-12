package com.wholescripts.core.facade.impl;
import com.wholescripts.core.facade.WholescriptsAccountsFacade;
import com.wholescripts.core.service.WholescriptsIntegrationsService;
import com.wholescripts.data.AgingResponseData;
import com.wholescripts.data.SharedRewardsResponseData;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Resource;
import java.util.Objects;

public class WholescriptsAccountsFacadeImpl implements WholescriptsAccountsFacade {
    @Resource(name = "wholescriptsIntegrationsService")
    private WholescriptsIntegrationsService wholescriptsIntegrationsService;
    @Resource(name = "configurationService")
    private ConfigurationService configurationService;


    public SharedRewardsResponseData getSharedRewards(B2BCustomerModel b2bCustomer,String date) throws Exception {
        SharedRewardsResponseData responseData=wholescriptsIntegrationsService.getSharedRewards(b2bCustomer,date);
        if(Objects.nonNull(responseData)&& Objects.nonNull(responseData.getShareReward()))
        {
            String curSrewards=responseData.getShareReward().getCurSrewards();
            if(StringUtils.isNotBlank(curSrewards))
            {
                String discount=configurationService.getConfiguration().getString("sapCustomerPriceGroup."+ curSrewards + ".discount");
                responseData.setDiscountPercentage(discount);
            }

        }
        return responseData;
    }
    public AgingResponseData getAging(B2BCustomerModel b2bCustomer, String keyDate) throws Exception {
        return wholescriptsIntegrationsService.getAging(b2bCustomer,keyDate);
    }
}