/*
	 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.facade;

import java.util.Map;

import com.wholescripts.data.RegistraionRequestData;
import com.wholescripts.data.RegistraionResponseData;


public interface WholescriptsRegistraionFacade
{
   boolean emailValidationFromSCPI(final  String email) throws Exception;
   Map<String,String> referralValidationFromSCPI(final Map<String,String> request) throws Exception;
   RegistraionResponseData createAccount(final RegistraionRequestData request) throws Exception;
}
