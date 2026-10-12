package com.wholescripts.core.event.listener;


import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;

public class WholescriptsContactUsEventListener extends AbstractEventListener<WholescriptsContactUsEvent>{
    private BusinessProcessService businessProcessService;
    private ModelService modelService;
    @Override
    protected void onEvent(WholescriptsContactUsEvent event) {
        StoreFrontCustomerProcessModel processModel = businessProcessService
                .createProcess("sendContactUsEmailProcess-" + "-" + System.currentTimeMillis(), "sendContactUsEmailProcess");
        processModel.setLanguage(event.getLanguage());
        processModel.setSite(event.getSite());
        processModel.setStore(event.getBaseStore());
        processModel.setCurrency(event.getCurrency());
        processModel.setWsContactUs(event.getContactUsForm());
        processModel.setCustomer(event.getCustomer());
        modelService.save(processModel);
        businessProcessService.startProcess(processModel);
    }
    public void setBusinessProcessService(BusinessProcessService businessProcessService) {
        this.businessProcessService = businessProcessService;
    }
    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }
}
