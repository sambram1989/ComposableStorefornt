package com.wholescripts.core.event.listener;

import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;

public class WholescriptsSendRecommendationByTextListener extends AbstractEventListener<WholescriptsSendRecommendationByTextEvent> {

    BusinessProcessService businessProcessService;
    ModelService modelService;


    public BusinessProcessService getBusinessProcessService() {
        return businessProcessService;
    }

    public void setBusinessProcessService(BusinessProcessService businessProcessService) {
        this.businessProcessService = businessProcessService;
    }

    public ModelService getModelService() {
        return modelService;
    }

    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    @Override
    protected void onEvent(WholescriptsSendRecommendationByTextEvent event) {
        // Create a new instance of the process
        StoreFrontCustomerProcessModel processModel = ( StoreFrontCustomerProcessModel) getBusinessProcessService()
                .createProcess("sendRecommendationProcess-" +event.getRecommendation().getCode()+ "-"
                        + System.currentTimeMillis(), "sendRecommendationProcess");
        processModel.setCurrency(event.getCurrency());
        processModel.setLanguage(event.getLanguage());
        processModel.setStore(event.getBaseStore());
        processModel.setSite(event.getSite());
        processModel.setRecommendation(event.getRecommendation());
        getModelService().save(processModel);
        getBusinessProcessService().startProcess(processModel);


    }
}
