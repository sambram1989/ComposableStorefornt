/*
 * Copyright (c) 2025 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;

import com.wholescripts.core.model.RecommendationModel;

/**
 *
 */
public class AccertifyFraudCheckEmailContext extends AbstractEmailContext<StoreFrontCustomerProcessModel> {
 
	public static final String TOKEN = "token";
   @Override
   protected BaseSiteModel getSite(StoreFrontCustomerProcessModel businessProcessModel) {
       return businessProcessModel.getSite();
   }

   @Override
   protected CustomerModel getCustomer(StoreFrontCustomerProcessModel businessProcessModel) {
       return businessProcessModel.getCustomer();
   }

   @Override
   protected LanguageModel getEmailLanguage(StoreFrontCustomerProcessModel businessProcessModel) {
       return businessProcessModel.getLanguage();
   }

   @Override
   public void init(StoreFrontCustomerProcessModel businessProcessModel, EmailPageModel emailPageModel) {
       super.init(businessProcessModel, emailPageModel);
       B2BCustomerModel  customer=(B2BCustomerModel) businessProcessModel.getUser();
       put(EMAIL, customer.getUid());
       put(FROM_DISPLAY_NAME, customer.getName());
       put(DISPLAY_NAME, customer.getName());
       put(TOKEN, customer.getToken());
       put(BASE_URL, getSiteBaseUrlResolutionService().getWebsiteUrlForSite(getSite(businessProcessModel), "", true, ""));
   }

}
