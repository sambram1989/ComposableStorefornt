/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.actions;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.outboundservices.service.DestinationSearchService;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.sap.core.configuration.http.impl.HTTPDestinationServiceImpl;
import de.hybris.platform.sap.core.configuration.model.SAPHTTPDestinationModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.util.Config;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wholescripts.core.enums.FedTaxClass;
import com.wholescripts.core.model.W9FormModel;
import com.wholescripts.core.model.WholescriptsIntegrationsModel;
import com.wholescripts.core.utils.WholescriptsCoreUtils;
import com.wholescripts.data.FedTaxClassData;
import com.wholescripts.data.XymonetW9RequestData;


/**
 *
 */
public class SendW9FormAction extends AbstractSimpleDecisionAction<StoreFrontCustomerProcessModel>
{
   @Resource(name = "modelService")
	private ModelService modelService;
   @Resource(name = "destinationSearchService")
   private DestinationSearchService destinationSearchService;
   @Resource(name = "sapCoreHTTPDestinationService")
   private HTTPDestinationServiceImpl httpDestinationService;
   @Resource(name = "configurationService")
   private ConfigurationService configurationService;
   
	@Override
	public Transition executeAction(final StoreFrontCustomerProcessModel process) throws RetryLaterException, Exception
	{
		final XymonetW9RequestData requestBody = new XymonetW9RequestData();
		final W9FormModel form = process.getW9Form();
		createRequest(process, requestBody, form);
		final ObjectMapper objectMapper = new ObjectMapper();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		final RestTemplate restTemplate = new RestTemplate();
		final String xymonetURL = configurationService.getConfiguration().getString("xymonet.send.w9form.destination.name");
      final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(xymonetURL);
		final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
		final ResponseEntity<Map> response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST, requestEntity, Map.class);
		saveRequestAndResponseInIntegrationTable(requestBody, objectMapper, response, process);
		if (Objects.nonNull(response) && response.getBody().containsValue(true))
		{
			return Transition.OK;
		}
		return Transition.NOK;
	}

	private void createRequest(final StoreFrontCustomerProcessModel process, final XymonetW9RequestData requestBody,
			final W9FormModel form)
	{
		final FedTaxClassData fedtaxData = new FedTaxClassData();
		createFedTaxClassData(fedtaxData, form.getFedTaxClass());
		requestBody.setName(form.getName());
		requestBody.setBusinessName(form.getBusinessName());
		requestBody.setFedTaxClass(fedtaxData);
		requestBody.setExemptPayeeCode(form.getExemptPayee());
		requestBody.setExemptFatcaCode(form.getExemptFatca());
		if (Objects.nonNull(form.getAddress()))
		{
			requestBody.setAddress(form.getAddress().getLine1());
			requestBody.setCity(form.getAddress().getTown());
			if (Objects.nonNull(form.getAddress().getRegion()))
			{
				requestBody.setState(form.getAddress().getRegion().getIsocode());
			}
			requestBody.setZipCode(form.getAddress().getPostalcode());
		}
		requestBody.setAccountNumbers(form.getAccountNumbers());
		requestBody.setRequestersNameAndAddress(form.getRequestersInfo());
		requestBody.setSSN(form.getSsn());
		requestBody.setEIN(form.getEin());
		requestBody.setSignature(form.getSignature());
		
		if (Objects.nonNull(form.getSignatureDate()))
		{
			final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			final String signatureDate = sdf.format(form.getSignatureDate());
			requestBody.setSignatureDate(signatureDate);
		}
		requestBody.setBrowserString(form.getBrowserString());
		requestBody.setIP(form.getIp());
		if (Objects.nonNull(form.getB2bCustomer()))
		{
			B2BUnitModel b2bUnit = form.getB2bCustomer().getDefaultB2BUnit();
			if(Objects.nonNull(b2bUnit)) {
				requestBody.setSAPUserId(WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(b2bUnit.getUid()));
			}	
		}
	}

	private void createFedTaxClassData(final FedTaxClassData fedtaxData, final FedTaxClass taxClass)
	{
		if (FedTaxClass.TAXCLASSINDIVIDUAL.equals(taxClass))
		{
			fedtaxData.setIndividual(Boolean.TRUE);
		}
		else if (FedTaxClass.TAXCLASSCCORPORATION.equals(taxClass))
		{
			fedtaxData.setCCorporation(Boolean.TRUE);
		}
		else if (FedTaxClass.TAXCLASSSCORPORATION.equals(taxClass))
		{
			fedtaxData.setSCorporation(Boolean.TRUE);
		}
		else if (FedTaxClass.TAXCLASSPARTNERSHIP.equals(taxClass))
		{
			fedtaxData.setPartner(Boolean.TRUE);
		}
		else if (FedTaxClass.TAXCLASSTRUST.equals(taxClass))
		{
			fedtaxData.setTrust(Boolean.TRUE);
		}
		else if (FedTaxClass.TAXCLASSLIMITED.equals(taxClass))
		{
			fedtaxData.setLLC(Boolean.TRUE);
		}
		else if (FedTaxClass.TAXCLASSOTHER.equals(taxClass))
		{
			fedtaxData.setOther(Boolean.TRUE);
		}
	}

	private void saveRequestAndResponseInIntegrationTable(final XymonetW9RequestData requestBody, final ObjectMapper objectMapper,
			final ResponseEntity<Map> response, final StoreFrontCustomerProcessModel process) throws JsonProcessingException
	{
		final WholescriptsIntegrationsModel integration = modelService.create(WholescriptsIntegrationsModel.class);
		integration.setRequest(objectMapper.writeValueAsString(requestBody)+"-"+process.getModifiedtime().toString());
		integration.setResponse(objectMapper.writeValueAsString(response.getBody()));
		integration.setUserId(process.getCustomer());
		modelService.save(integration);
	}
	
	 private SAPHTTPDestinationModel getHttpDestinationModel(final String httpDestName) {
       return httpDestinationService.getHttpDestinationsByName(httpDestName);
   }
}
