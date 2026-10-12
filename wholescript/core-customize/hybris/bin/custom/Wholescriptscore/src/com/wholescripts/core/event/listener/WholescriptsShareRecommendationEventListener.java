package com.wholescripts.core.event.listener;

import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;

public class WholescriptsShareRecommendationEventListener extends AbstractEventListener<WholescriptsShareRecommendationEvent> {

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
    protected void onEvent(WholescriptsShareRecommendationEvent event) {
        // Create a new instance of the process
        StoreFrontCustomerProcessModel processModel = ( StoreFrontCustomerProcessModel) getBusinessProcessService()
                .createProcess("wsShareRecommendationEmailProcess-" +event.getRecommendation().getCode()+ "-"
                        + System.currentTimeMillis(), "wsShareRecommendationEmailProcess");
        processModel.setCurrency(event.getCurrency());
        processModel.setLanguage(event.getLanguage());
        processModel.setStore(event.getBaseStore());
        processModel.setSite(event.getSite());
        processModel.setRecommendation(event.getRecommendation());
        getModelService().save(processModel);
        getBusinessProcessService().startProcess(processModel);


    }
}
