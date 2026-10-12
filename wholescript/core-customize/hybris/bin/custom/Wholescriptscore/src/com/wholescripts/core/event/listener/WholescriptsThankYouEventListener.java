package com.wholescripts.core.event.listener;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

public class WholescriptsThankYouEventListener extends AbstractEventListener<WholescriptsThankYouEvent> {
    @Resource(name = "businessProcessService")
    private BusinessProcessService businessProcessService;
    @Resource(name = "modelService")
    private ModelService modelService;
    @Resource(name = "b2bCustomerService")
    private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;
    @Override
    protected void onEvent(WholescriptsThankYouEvent event) {
        StoreFrontCustomerProcessModel thankYouEmailProcess = businessProcessService
                .createProcess("sendThankYouEmailProcess-" + "-" + System.currentTimeMillis(), "sendThankYouEmailProcess");
        thankYouEmailProcess.setLanguage(event.getLanguage());
        thankYouEmailProcess.setSite(event.getSite());
        thankYouEmailProcess.setStore(event.getBaseStore());
        thankYouEmailProcess.setCurrency(event.getCurrency());
        thankYouEmailProcess.setCustomer(b2bCustomerService.getCurrentB2BCustomer());
        thankYouEmailProcess.setWsContactUs(event.getContactUsForm());
        modelService.save(thankYouEmailProcess);
        businessProcessService.startProcess(thankYouEmailProcess);
    }
    public void setBusinessProcessService(BusinessProcessService businessProcessService) {
        this.businessProcessService = businessProcessService;
    }
    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }
    public BusinessProcessService getBusinessProcessService() {
        return businessProcessService;
    }
    public B2BCustomerService<B2BCustomerModel, B2BUnitModel> getB2bCustomerService() {
        return b2bCustomerService;
    }
    public void setB2bCustomerService(B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService) {
        this.b2bCustomerService = b2bCustomerService;
    }
    public ModelService getModelService() {
        return modelService;
    }
}
