/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.paymetric.controller;

import de.hybris.platform.paymetric.service.TokenizationService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@SuppressWarnings("unused")
@RequestMapping(value = "/XiIntercept3")
public class PaymetricTokenizationController
{
	private static Logger LOG = LoggerFactory.getLogger(PaymetricTokenizationController.class);


	@Resource(name = "tokenizationService")
	TokenizationService tokenizationService;


	@RequestMapping(value = "/getAccessToken", method = RequestMethod.GET)
	@ResponseBody
	public String getXiInterceptAccessToken(final HttpServletRequest request)
	{

		return tokenizationService.getAccessToken(request);

	}


	@RequestMapping(value = "/getResponse", method = RequestMethod.GET)
	@ResponseBody
	public String getResponsePacket(final HttpServletRequest request)
	{

		return tokenizationService.getResponsePacket(request);

	}


}
