/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.strategies.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.outboundservices.service.DestinationSearchService;
import de.hybris.platform.sap.core.configuration.http.impl.HTTPDestinationServiceImpl;
import de.hybris.platform.sap.core.configuration.model.SAPHTTPDestinationModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wholescripts.core.model.WholescriptsIntegrationsModel;
import com.wholescripts.core.strategies.XymonetIntegrationStrategy;
import com.wholescripts.core.utils.WholescriptsCoreUtils;
import com.wholescripts.data.XymonetW9ResponseData;


/**
 *
 */
public class XymonetIntegrationStrategyImpl implements XymonetIntegrationStrategy
{
	private static final Logger LOG = Logger.getLogger(XymonetIntegrationStrategyImpl.class.getName());
	@Resource(name = "modelService")
	private ModelService modelService;
	@Resource(name = "destinationSearchService")
   private DestinationSearchService destinationSearchService;
   @Resource(name = "sapCoreHTTPDestinationService")
   private HTTPDestinationServiceImpl httpDestinationService;
   @Resource(name = "configurationService")
   private ConfigurationService configurationService;

	@Override
	public Map<String, String> showW9Link(final B2BCustomerModel customer) throws JsonProcessingException
	{
		Map<String, String> response = new HashMap<>();
		if (Objects.nonNull(customer.getDefaultB2BUnit()))
		{
			final HttpHeaders headers = new HttpHeaders();
			final RestTemplate restTemplate = new RestTemplate();
			final HttpEntity<String> requestEntity = new HttpEntity<>(headers);
			final String xymonetShowW9URL = configurationService.getConfiguration().getString("xymonet.show.w9form.destination.name");
	      final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(xymonetShowW9URL);
			final String xymonetURL = httpDestination.getTargetURL() + WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(customer.getDefaultB2BUnit().getUid());
			final ResponseEntity<Map> entity = restTemplate.exchange(xymonetURL, HttpMethod.GET, requestEntity, Map.class);
			response = entity.getBody();
			saveRequestAndResponseInIntegrationTable(response, xymonetURL, customer);
		}
		return response;
	}

	private void saveRequestAndResponseInIntegrationTable(final Map<String, String> response, final String request,
			final B2BCustomerModel customer) throws JsonProcessingException
	{
		final WholescriptsIntegrationsModel integration = modelService.create(WholescriptsIntegrationsModel.class);
		integration.setRequest(request + "-" + LocalDateTime.now().toString());
		integration.setResponse(new ObjectMapper().writeValueAsString(response));
		integration.setUserId(customer);
		modelService.save(integration);
	}

	@Override
	public XymonetW9ResponseData downloadW9Form(final B2BCustomerModel customer) throws JsonProcessingException
	{
		final XymonetW9ResponseData responseData = new XymonetW9ResponseData();
		try
		{
			if (Objects.nonNull(customer.getDefaultB2BUnit()))
			{
				final HttpHeaders headers = new HttpHeaders();
				final RestTemplate restTemplate = new RestTemplate();
				final HttpEntity<String> requestEntity = new HttpEntity<>(headers);
				final String xymonetURL = configurationService.getConfiguration().getString("xymonet.download.w9form.destination.name");
		      final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(xymonetURL);
				final String xymonetDownloadURL = httpDestination.getTargetURL()
						+ WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(customer.getDefaultB2BUnit().getUid());
				final ResponseEntity<Map> entity = restTemplate.exchange(xymonetDownloadURL, HttpMethod.GET, requestEntity,
						Map.class);
				final Map<String, String> responseMap = entity.getBody();
				responseData.setFileName(Config.getParameter("xymonet.download.file.name"));
				responseData.setMsg(responseMap.get("mimeType"));
				responseData.setSignedBy(responseMap.get("signedBy"));
				responseData.setSignedDate(responseMap.get("signedDate"));
				responseData.setSuccess(String.valueOf(responseMap.get("success")));
				responseData.setMsg(responseMap.get("msg"));
				final StringBuilder pdfUrl = new StringBuilder();
				pdfUrl.append(responseMap.get("fileData"));
				responseData.setFileData(pdfUrl.toString());
				saveRequestAndResponseInIntegrationTable(responseMap, xymonetDownloadURL, customer);
				return responseData;
			}
		}
		catch (final Exception e)
		{
		 LOG.debug(e.getMessage());
		}
		return responseData;
	}
	
	 private SAPHTTPDestinationModel getHttpDestinationModel(final String httpDestName) {
       return httpDestinationService.getHttpDestinationsByName(httpDestName);
   }
}