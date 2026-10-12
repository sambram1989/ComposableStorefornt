package com.wholescripts.core.actions;

import com.wholescripts.core.model.RecommendationModel;
import com.wholescripts.core.service.WholescriptsIntegrationsService;
import com.wholescripts.data.WsSendRecommendationTextMessageResponse;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.task.RetryLaterException;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.Objects;

public class SendTextMessageAction extends AbstractSimpleDecisionAction<StoreFrontCustomerProcessModel> {

    @Resource(name = "wholescriptsIntegrationsService")
    private WholescriptsIntegrationsService wholescriptsIntegrationsService;
    @Override
    public Transition executeAction(StoreFrontCustomerProcessModel storeFrontCustomerProcessModel) throws RetryLaterException, Exception {
        WsSendRecommendationTextMessageResponse response=wholescriptsIntegrationsService.sendMessageUsingSinch(storeFrontCustomerProcessModel);
        if(Objects.nonNull(response)&& StringUtils.isNotBlank(response.getId()))
        {
            return Transition.OK;
        }
        return Transition.NOK;
    }
}
