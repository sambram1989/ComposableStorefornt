package com.wholescripts.core.event.listener;

import com.wholescripts.core.model.RecommendationModel;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;

public class WholescriptsSendRecommendationByTextEvent extends AbstractCommerceUserEvent {


    private RecommendationModel recommendation;

    public RecommendationModel getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(RecommendationModel recommendation) {
        this.recommendation = recommendation;
    }

    public WholescriptsSendRecommendationByTextEvent(final RecommendationModel recommendation) {
        super();
        this.recommendation = recommendation;

    }
}
