/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.strategies;

import de.hybris.platform.b2b.model.B2BCustomerModel;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wholescripts.data.XymonetW9ResponseData;

/**
 *
 */
public interface XymonetIntegrationStrategy
{
	public Map<String,String> showW9Link( B2BCustomerModel customer) throws JsonProcessingException;
	public XymonetW9ResponseData downloadW9Form( B2BCustomerModel customer) throws JsonProcessingException;
}
