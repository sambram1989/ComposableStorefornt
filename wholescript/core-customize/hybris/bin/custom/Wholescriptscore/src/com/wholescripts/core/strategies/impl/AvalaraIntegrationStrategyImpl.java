package com.wholescripts.core.strategies.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wholescripts.core.model.WholescriptsIntegrationsModel;
import com.wholescripts.core.strategies.AvalaraIntegrationStrategy;
import com.wholescripts.core.utils.WholescriptsCoreUtils;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.wholescripts.core.constants.WholescriptsIntegrationConstants.CERTEXPRESSINVITES;

/**
 * Integration strategy class for Avalara.
 */
public class AvalaraIntegrationStrategyImpl implements AvalaraIntegrationStrategy {
    private static final Logger LOG = Logger.getLogger(AvalaraIntegrationStrategyImpl.class.getName());

    @Resource
    private ModelService modelService;

    public void integrate(Map<String, String> configParams, B2BCustomerModel customer) {

        try {
            String userName = configParams.get("username");
            String password = configParams.get("password");
            String auth = userName + ":" + password;
            String sapCustomerAccountID = customer.getDefaultB2BUnit() != null ? WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnitWithoutFormat(customer.getDefaultB2BUnit().getUid()) : configParams.get("sapCustomerNumber");
            String avalaraURL = configParams.get("avalaraURL") + sapCustomerAccountID + "/" + CERTEXPRESSINVITES;

            // Encode the basic auth credentials
            String basicAuth = configParams.get("authType") + " " + Base64.getEncoder().encodeToString(auth.getBytes());

            // Create an instance of RestTemplate
            RestTemplate restTemplate = new RestTemplate();

            // Prepare request headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", basicAuth);
            headers.setContentType(MediaType.APPLICATION_JSON);

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> requestBody = new HashMap<>();

            requestBody.put("recipient", null);
            requestBody.put("coverLetterTitle", null);
            requestBody.put("exposureZones", null);
            requestBody.put("exemptReasons", null);
            requestBody.put("deliveryMethod", "Download");
            if (StringUtils.isNotEmpty(customer.getSapConsumerID())) {
                requestBody.put("SapCustomer", customer.getSapConsumerID());
            } else if(StringUtils.isNotEmpty(sapCustomerAccountID)){
                requestBody.put("SapCustomer", sapCustomerAccountID);
            }

            LOG.info("REQUEST BODY:" + requestBody);
            // Create the request entity with the headers and body
            HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
            LOG.info("REQUEST ENTITY:" + requestEntity);
            // Send the request and get the response
            ResponseEntity<String> response = restTemplate.exchange(avalaraURL, HttpMethod.POST, requestEntity, String.class);
            //Record the Request and Response

            List<Map<String, Object>> responseMap = objectMapper.readValue(response.getBody(), new TypeReference<List<Map<String, Object>>>() {
            });

            Map<String, Object> invitation = (Map<String, Object>) responseMap.get(0).get("invitation");
            String avalaraTaxLink = (String) invitation.get("requestLink");

            LOG.info("avalaraTaxLink from AVALARA:" + avalaraTaxLink);

            if (StringUtils.isEmpty(avalaraTaxLink)) {
                avalaraTaxLink = configParams.get("defaultAvalaraLink");
            }

            customer.setAvalaraTaxLink(avalaraTaxLink);
            calculateAndSetLinkExpiryDate(customer, configParams.get("expiryPeriod"));

            modelService.save(customer);

            // Output the response
            LOG.info("Response Code: " + response.getStatusCodeValue());
            LOG.info("Response Body: " + response.getBody());
            recordRequestAndResponse(objectMapper.writeValueAsString(requestBody), response.toString(), customer);
        } catch (Exception e) {
            customer.setAvalaraTaxLink(configParams.get("defaultAvalaraLink"));
            calculateAndSetLinkExpiryDate(customer, configParams.get("expiryPeriod"));
            modelService.save(customer);
            LOG.error(e);
        }

    }

    private void calculateAndSetLinkExpiryDate(B2BCustomerModel customer, String expiryPeriod) {

        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            now = now.plusDays(Long.parseLong(expiryPeriod));
            Instant instant = now.atZone(ZoneId.systemDefault()).toInstant();
            Date expiryDate = Date.from(instant);
            LOG.info("Date = " + expiryDate);
            customer.setAvalaraTaxLinkExpiryDate(expiryDate);
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    public void recordRequestAndResponse(String request, String response, B2BCustomerModel customer) {

        WholescriptsIntegrationsModel wholescriptsIntegrations = modelService.create(WholescriptsIntegrationsModel.class);
        wholescriptsIntegrations.setRequest(request);
        wholescriptsIntegrations.setResponse(response);
        wholescriptsIntegrations.setUserId(customer);
        modelService.save(wholescriptsIntegrations);

    }
}
