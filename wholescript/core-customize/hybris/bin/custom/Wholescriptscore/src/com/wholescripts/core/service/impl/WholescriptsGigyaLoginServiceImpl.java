/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service.impl;

import de.hybris.platform.gigya.gigyaservices.api.exception.GigyaApiException;
import de.hybris.platform.gigya.gigyaservices.constants.GigyaservicesConstants;
import de.hybris.platform.gigya.gigyaservices.data.GigyaAccount;
import de.hybris.platform.gigya.gigyaservices.data.GigyaUserObject;
import de.hybris.platform.gigya.gigyaservices.enums.GigyaUserManagementMode;
import de.hybris.platform.gigya.gigyaservices.login.impl.DefaultGigyaLoginService;
import de.hybris.platform.gigya.gigyaservices.model.GigyaConfigModel;
import de.hybris.platform.gigya.gigyaservices.service.GigyaService;
import de.hybris.platform.gigya.gigyaservices.utils.GigyaUtils;
import de.hybris.platform.util.Config;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gigya.socialize.GSObject;
import com.gigya.socialize.GSResponse;

/**
 *
 */
public class WholescriptsGigyaLoginServiceImpl extends DefaultGigyaLoginService	
{
	private static final Logger LOG = LoggerFactory.getLogger(WholescriptsGigyaLoginServiceImpl.class);
	@Resource(name = "gigyaService")
	private GigyaService gigyaService;
	
	@Override
	public GigyaUserObject fetchGigyaInfo(final GigyaConfigModel gigyaConfig, final String uid)
	{
		try
		{
			if (gigyaConfig != null && gigyaConfig.getMode() != null
			                && gigyaConfig.getMode() == GigyaUserManagementMode.RAAS)
			{
				return fetchRaasAccount(gigyaConfig, uid);
			}
			else
			{
				return null;
			}
		}
		catch (final IOException e)
		{
			throw new GigyaApiException(e.getMessage(), e);
		}
	}

	protected GigyaUserObject fetchRaasAccount(final GigyaConfigModel gigyaConfig, final String guid)
	                throws IOException
	{
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

		final Map<String, Object> params = new LinkedHashMap<>();
		params.put("UID", guid);
		params.put("extraProfileFields", Config.getString("gigya.extra.fields",
		                "languages,address,phones,education,honors,publications,patents,certifications,professionalHeadline,bio,industry,specialties,work,skills,religion,politicalView,interestedIn,relationshipStatus,hometown,favorites,followersCount,followingCount,username,locale,verified,timezone,likes"));
		params.put("include",
		                Config.getString("gigya.basic.include", "loginIDs,emails,profile,data,preferences,groups"));

		// Add environment details to getAccountInfo call
		params.put("environment", GigyaUtils.getEnvironmentDetails());
		final GSObject gigyaParams = convertMapToGsObject(mapper, params);
		final GSResponse res = gigyaService.callRawGigyaApiWithConfigAndObject("accounts.getAccountInfo", gigyaParams,
		                gigyaConfig, GigyaservicesConstants.MAX_RETRIES, GigyaservicesConstants.TRY_NUM);

		return prepareAndGetUserObject(mapper, res);
	}

	protected GigyaUserObject prepareAndGetUserObject(final ObjectMapper mapper, final GSResponse res)
	                throws IOException
	{
		final ObjectMapper objectMapper = new ObjectMapper();
		try
		{
			LOG.info("Response from CDC::::::::::::"+objectMapper.writeValueAsString(res.getData()));
		}
		catch (JsonProcessingException e)
		{
		}
		final GigyaAccount accObj = mapper.readValue(res.getData().toJsonString(), GigyaAccount.class);
		Map<String, Object> data = accObj.getData();
		final GigyaUserObject usrObj = accObj.getProfile();
		usrObj.setLoginIDs(accObj.getLoginIDs());
		usrObj.setUID(accObj.getUID());
		usrObj.setPreferences(accObj.getPreferences());
		usrObj.setDataCenter(accObj.getDataCenter());
		if(Objects.nonNull(data.get("BusinessPartnerID"))) {
			usrObj.setBusinessPartnerID(String.valueOf(data.get("BusinessPartnerID")));
		}
		LOG.info("CustomerGroup::::::::::::"+data.get("CustomerGroup"));
		if(Objects.nonNull(data.get("CustomerGroup"))) {
			usrObj.setCustomerGroup(String.valueOf(data.get("CustomerGroup")));
		}
		return usrObj;
	}

	private GSObject convertMapToGsObject(final ObjectMapper mapper, final Map<String, Object> parms)
	{
		GSObject gigyaParams = new GSObject();
		try
		{
			gigyaParams = new GSObject(mapper.writeValueAsString(parms));
		}
		catch (final Exception e)
		{
			final String msg = "Error creating gigya request parmeters";
			LOG.error(msg, e);
			throw new GigyaApiException(msg);
		}
		return gigyaParams;
	}
}
