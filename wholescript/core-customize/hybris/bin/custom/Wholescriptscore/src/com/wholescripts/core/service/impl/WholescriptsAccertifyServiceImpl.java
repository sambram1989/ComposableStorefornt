/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;
import de.hybris.platform.commerceservices.security.SecureToken;
import de.hybris.platform.commerceservices.security.SecureTokenService;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.wholescripts.core.event.listener.WholescriptsAccertifyFraudCheckEvent;
import com.wholescripts.core.event.listener.WholescriptsShareRecommendationEvent;
import com.wholescripts.core.service.WholescriptsAccertifyService;
import com.wholescripts.core.service.WholescriptsIntegrationsService;
import com.wholescripts.data.*;


public class WholescriptsAccertifyServiceImpl implements WholescriptsAccertifyService
{
	protected static final String EMAIL_VERIFIED = "EmailVerified";
	@Resource(name = "wholescriptsIntegrationsService")
	private WholescriptsIntegrationsService wholescriptsIntegrationsService;
	@Resource(name = "b2bCustomerService")
	private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;
	@Resource(name = "modelService")
	private ModelService modelService;
	@Resource(name = "eventService")
	private EventService eventService;
	 @Resource(name = "baseStoreService")
    private BaseStoreService baseStoreService;
	 @Resource(name = "commonI18NService")
    private CommonI18NService commonI18NService;
	 @Resource(name = "baseSiteService")
    private BaseSiteService baseSiteService;
	 @Resource(name = "secureTokenService")
	 private SecureTokenService secureTokenService;
	 @Resource(name = "configurationService")
	 private ConfigurationService configurationService;
	 @Resource(name = "businessProcessService")
	 private BusinessProcessService businessProcessService;

	@Override
	public boolean accertifyLoginVerificaton(AccertifyVerificationRequestData request,final String password, final String accountExist) throws Exception
	{
		String email = request.getEmailAddress();
		if(accountExist.equalsIgnoreCase("Yes")) {
			if(!wholescriptsIntegrationsService.validateLoginDetailsInCDC(password, email,request)) {
				request.setSuccess("false");
				request.setFailureCode("passwordIncorrect");
			}
		}else {
			request.setSuccess("false");
			request.setFailureCode("accountDoesntExist");
			 wholescriptsIntegrationsService.accertifyLoginVerificaton(request);
			return false;
		}
		
		B2BCustomerModel customer = b2bCustomerService.getUserForUID(email);
		if(Objects.isNull(customer)) {
			throw new ModelNotFoundException(email);
		}
	
		if(request.getFailAccertifyCheck()) {
			sendFraudEmailToCustomer(customer);
			return false;
		}
		else if (!isVerifiedDateMoreThan24Hours(customer.getAccertifyEmailVerified())) {
			AccertifyVerificationResponseData response= wholescriptsIntegrationsService.accertifyLoginVerificaton(request);
			AccertifyResultData result = response.getResults();
			AccertifyRiskScoreData riskScore= result.getRiskScore();
			if(!"approve".equalsIgnoreCase(riskScore.getRecommendationCode())) {
				// send email to customer
				sendFraudEmailToCustomer(customer);

				return false;
			}
		}
		return true;
	}
	
	private void sendFraudEmailToCustomer(B2BCustomerModel customer) throws Exception
	{
		final long timeStamp = Integer.valueOf(configurationService.getConfiguration().getString("accertify.tokenValiditySeconds"))  > 0L ? new Date().getTime() : 0L;
		final SecureToken data = new SecureToken(customer.getUid(), timeStamp);
		final String token = secureTokenService.encryptData(data);
		customer.setToken(token);
		customer.setLoginDisabled(Boolean.TRUE);
		customer.setFailedAccertifyCheck(Boolean.TRUE);
		modelService.save(customer);
		WholescriptsAccertifyFraudCheckEvent event=new WholescriptsAccertifyFraudCheckEvent(customer);
		eventService.publishEvent(initializeEvent(event,customer));

		// update flag in CDC
		wholescriptsIntegrationsService.updateAccertifyFlagInCDC(customer);
	}

	private boolean isVerifiedDateMoreThan24Hours(Date verifiedDate)
	{
		if(Objects.nonNull(verifiedDate)) {
			Instant instant = verifiedDate.toInstant();
			LocalDateTime verifiedDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
			LocalDateTime currentDateTime = LocalDateTime.now();
			Duration duration = Duration.between(verifiedDateTime, currentDateTime);
			return  duration.toHours() > 24;
		}
		return false;
	}

	@Override
	public AccertifyVerificationCreateAccountResponseData accertifyCreateAccountVerification(AccertifyVerificationCreateAccountRequestData request) throws Exception {
		return wholescriptsIntegrationsService.accertifyCreateAccountVerification(request);
	}

	@Override
	public AccertifyVerificationUpdateAccountResponseData accertifyUpdateAccountVerification(AccertifyVerificationUpdateAccountRequestData requestData) throws Exception {
		return wholescriptsIntegrationsService.accertifyUpdateAccountVerification(requestData);
	}
	
   private AbstractEvent initializeEvent(WholescriptsAccertifyFraudCheckEvent event,B2BCustomerModel customer) {
      event.setBaseStore(baseStoreService.getCurrentBaseStore());
      event.setSite(baseSiteService.getCurrentBaseSite());
      event.setB2bCustomer(customer);
      event.setLanguage(commonI18NService.getCurrentLanguage());
      event.setCurrency(commonI18NService.getCurrentCurrency());

      return event;
  }

	@Override
	public void verifyEmailAndUpdateCDCAccertifyFlag(String token) throws Exception
	{
		final SecureToken data = secureTokenService.decryptData(token);
	   B2BCustomerModel customer = b2bCustomerService.getUserForUID(data.getData());
		if (Integer.valueOf(configurationService.getConfiguration().getString("accertify.tokenValiditySeconds")) > 0L)
		{
			final long delta = new Date().getTime() - data.getTimeStamp();
			if (delta / 1000 > Integer.valueOf(configurationService.getConfiguration().getString("accertify.tokenValiditySeconds")))
			{
				throw new IllegalArgumentException("token expired");
			}
		}
		if (customer == null)
		{
			throw new IllegalArgumentException("user for token not found");
		}
		if (!token.equals(customer.getToken()))
		{
			throw new TokenInvalidatedException();
		}
		businessProcessService.triggerEvent(customer.getUid() +"_"+ EMAIL_VERIFIED);		
	}

}
