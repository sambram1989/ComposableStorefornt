/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.facade.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.wholescripts.core.facade.WholescriptsRegistraionFacade;
import com.wholescripts.core.service.WholescriptsIntegrationsService;
import com.wholescripts.core.service.WholescriptsRegistraionService;
import com.wholescripts.data.EmailVerificationRequestData;
import com.wholescripts.data.RegistraionRequestData;
import com.wholescripts.data.RegistraionResponseData;

public class WholescriptsRegistraionFacadeImpl implements WholescriptsRegistraionFacade
{
	@Resource(name = "wholescriptsIntegrationsService")
	private WholescriptsIntegrationsService wholescriptsIntegrationsService;
	@Resource(name = "wholescriptsRegistraionService")
	private WholescriptsRegistraionService wholescriptsRegistraionService;
	
	@Override
	public boolean emailValidationFromSCPI(final String email) throws Exception
	{
		return wholescriptsIntegrationsService.emailValidationFromSCPI(email);
	}

	@Override
	public Map<String,String> referralValidationFromSCPI(final Map<String, String> request) throws Exception
	{
		return wholescriptsIntegrationsService.referralValidationFromSCPI(request);
	}

	@Override
	public RegistraionResponseData createAccount(RegistraionRequestData request) throws Exception
	{
		RegistraionResponseData responseData = wholescriptsIntegrationsService.createAccount(request);
		if(StringUtils.isNotEmpty(responseData.getAccountNumber())) {
		wholescriptsRegistraionService.createAccount(responseData);
		}
		return responseData;
	}
}
