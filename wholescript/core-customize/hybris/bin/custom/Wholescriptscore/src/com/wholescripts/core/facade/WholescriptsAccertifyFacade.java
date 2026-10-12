/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.facade;

import com.wholescripts.data.*;


public interface WholescriptsAccertifyFacade
{
	public boolean accertifyLoginVerificaton(final AccertifyVerificationRequestData request,final String password, final String accountExist) throws Exception;
	AccertifyVerificationCreateAccountResponseData accertifyCreateAccountVerification(final AccertifyVerificationCreateAccountRequestData request) throws Exception;
	AccertifyVerificationUpdateAccountResponseData accertifyUpdateAccountVerification(final AccertifyVerificationUpdateAccountRequestData requestData) throws Exception;
   void verifyEmailAndUpdateCDCAccertifyFlag(String token) throws Exception;
}
