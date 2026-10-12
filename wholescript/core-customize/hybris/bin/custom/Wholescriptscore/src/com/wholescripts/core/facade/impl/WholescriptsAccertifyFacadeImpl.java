/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.facade.impl;

import javax.annotation.Resource;

import com.wholescripts.core.facade.WholescriptsAccertifyFacade;
import com.wholescripts.core.service.WholescriptsAccertifyService;
import com.wholescripts.data.*;


public class WholescriptsAccertifyFacadeImpl implements WholescriptsAccertifyFacade
{
	@Resource(name = "wholescriptsAccertifyService")
	private WholescriptsAccertifyService wholescriptsAccertifyService;

	@Override
	public boolean accertifyLoginVerificaton(AccertifyVerificationRequestData request,final String password,final String accountExist) throws Exception
	{
		return wholescriptsAccertifyService.accertifyLoginVerificaton(request,password,accountExist);
	}

	@Override
	public AccertifyVerificationCreateAccountResponseData accertifyCreateAccountVerification(AccertifyVerificationCreateAccountRequestData request) throws Exception {
		return wholescriptsAccertifyService.accertifyCreateAccountVerification(request);
	}
	@Override
	public AccertifyVerificationUpdateAccountResponseData accertifyUpdateAccountVerification(AccertifyVerificationUpdateAccountRequestData requestData) throws Exception {
		return wholescriptsAccertifyService.accertifyUpdateAccountVerification(requestData);
	}

	@Override
	public void verifyEmailAndUpdateCDCAccertifyFlag(String token) throws Exception
	{
		wholescriptsAccertifyService.verifyEmailAndUpdateCDCAccertifyFlag(token);		
	}
}
