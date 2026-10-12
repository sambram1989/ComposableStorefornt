/*
 * Copyright (c) 2025 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.actions;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction.Transition;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.RetryLaterException;

import java.util.Date;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.wholescripts.core.model.RecommendationModel;
import com.wholescripts.core.service.WholescriptsIntegrationsService;
import com.wholescripts.data.WsSendRecommendationTextMessageResponse;


public class UpdateAccertifyFlagInCDCAction extends AbstractSimpleDecisionAction<StoreFrontCustomerProcessModel> {

	@Resource(name = "wholescriptsIntegrationsService")
	private WholescriptsIntegrationsService wholescriptsIntegrationsService;
	@Resource(name = "modelService")
	private ModelService modelService;
	@Override
	public Transition executeAction(StoreFrontCustomerProcessModel storeFrontCustomerProcessModel) throws RetryLaterException, Exception {
		B2BCustomerModel customer=(B2BCustomerModel) storeFrontCustomerProcessModel.getUser();
		try {
			customer.setAccertifyEmailVerified(new Date());
			customer.setLoginDisabled(false);
			customer.setFailedAccertifyCheck(false);
			modelService.save(customer);
			customer.setSite(storeFrontCustomerProcessModel.getSite());
			wholescriptsIntegrationsService.updateAccertifyFlagInCDC(customer);
			return Transition.OK;
		}catch (Exception ex) {
			customer.setAccertifyEmailVerified(null);
			customer.setLoginDisabled(true);
			customer.setFailedAccertifyCheck(true);
			modelService.save(customer);
			return Transition.NOK;
		}   
	}
}
