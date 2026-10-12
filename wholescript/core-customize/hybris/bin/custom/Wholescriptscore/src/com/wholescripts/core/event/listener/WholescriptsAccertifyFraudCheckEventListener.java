/*
 * Copyright (c) 2025 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.event.listener;

import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;

/**
 *
 */
public class WholescriptsAccertifyFraudCheckEventListener  extends AbstractEventListener<WholescriptsAccertifyFraudCheckEvent>
{

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
	protected void onEvent(WholescriptsAccertifyFraudCheckEvent event)
	{
	    StoreFrontCustomerProcessModel processModel = ( StoreFrontCustomerProcessModel) getBusinessProcessService()
             .createProcess("sendAccetifyFraudCheckEmailProcess-" +event.getB2bCustomer().getUid()+ "-"
                     + System.currentTimeMillis(), "sendAccetifyFraudCheckEmailProcess");
     processModel.setCurrency(event.getCurrency());
     processModel.setLanguage(event.getLanguage());
     processModel.setStore(event.getBaseStore());
     processModel.setSite(event.getSite());
     processModel.setUser(event.getB2bCustomer());
     getModelService().save(processModel);
     getBusinessProcessService().startProcess(processModel);
		
	}

}
