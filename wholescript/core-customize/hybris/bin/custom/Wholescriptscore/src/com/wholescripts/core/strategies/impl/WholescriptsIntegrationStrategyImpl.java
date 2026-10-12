/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.strategies.impl;

import de.hybris.platform.acceleratorocc.dto.payment.SopPaymentDetailsWsDTO;
import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.apiregistryservices.model.BasicCredentialModel;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.basecommerce.enums.PointOfServiceTypeEnum;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.ACHCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.gigya.gigyaservices.model.GigyaConfigModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.outboundservices.service.DestinationSearchService;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.promotions.model.PromotionOrderEntryConsumedModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.sap.core.configuration.http.impl.HTTPDestinationServiceImpl;
import de.hybris.platform.sap.core.configuration.model.SAPConfigurationModel;
import de.hybris.platform.sap.core.configuration.model.SAPHTTPDestinationModel;
import de.hybris.platform.sap.sapmodel.model.SAPDeliveryModeModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.util.Config;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Collections;


import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.google.common.collect.Lists;
import com.wholescripts.core.constants.WholescriptscoreConstants;
import com.wholescripts.core.dao.ReferralCodesDao;
import com.wholescripts.core.dao.WholescriptsCartValidationDao;
import com.wholescripts.core.enums.CustomerProfileType;
import com.wholescripts.core.exception.WholescriptsPageNotFoundException;
import com.wholescripts.core.model.BrandAgreementModel;
import com.wholescripts.core.model.RecommendationModel;
import com.wholescripts.core.model.WholescriptsIntegrationsModel;
import com.wholescripts.core.service.WholescriptsProductsService;
import com.wholescripts.core.service.WholescriptsUserService;
import com.wholescripts.core.strategies.WholescriptsIntegrationStrategy;
import com.wholescripts.core.utils.WholescriptsCoreUtils;
import com.wholescripts.data.*;
import com.wholescripts.dto.OrderChangeAutoShipCreditCardRequestWsDTO;
import com.wholescripts.dto.OrderChangeAutoShipLineItemListWsDTO;
import com.wholescripts.dto.OrderChangeAutoShipLineItemWsDTO;
import com.wholescripts.dto.OrderChangeAutoShipRequestWsDTO;
import com.wholescripts.dto.referralCodes.ReferralCodeWsDTO;





/**
 *
 */
public class WholescriptsIntegrationStrategyImpl implements WholescriptsIntegrationStrategy {
    private static final Logger LOG = Logger.getLogger(WholescriptsIntegrationStrategyImpl.class);
    private static final String SCPI_OAUTH_DESTINATION = "scpiTokenConsumedDestination";
    private static final String UPS_BASIC_DESTINATION = "upsConsumedDestination";
    private static final String ACCERTIFY_LOGIN_BASIC_DESTINATION = "accertifyLoginConsumedDestination";
    private static final String ENCODE_UTF8 = StandardCharsets.UTF_8.name().intern();
    private static final String PERSONA_BASIC_DESTINATION = "personaAgeConsumedDestination";
    private static final String ACCERTIFY_CREATE_ACCOUNT_BASIC_DESTINATION = "accertifyCreateAccountConsumedDestination";
    private static final String ACCERTIFY_UPDATE_ACCOUNT_BASIC_DESTINATION = "accertifyUpdateAccountConsumedDestination";
    private static final String STATUS_INTEGRATION_SUCCESS = "SUCCESS";
    private static final String STATUS_INTEGRATION_FAIL = "FAIL";

    @Resource(name = "modelService")
    private ModelService modelService;
    @Resource(name = "destinationSearchService")
    private DestinationSearchService destinationSearchService;
    @Resource(name = "userService")
    private UserService userService;
    @Resource(name = "sapCoreHTTPDestinationService")
    private HTTPDestinationServiceImpl httpDestinationService;
    @Resource(name = "baseStoreService")
    private BaseStoreService baseStoreService;
    @Resource(name = "referralCodesDao")
    private ReferralCodesDao referralCodesDao;
    @Resource(name = "cartService")
    private CartService cartService;
    @Resource(name = "b2bCustomerService")
    private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;

    @Resource(name = "sessionService")
    private SessionService sessionService;

    @Resource(name = "commonI18NService")
    private CommonI18NService commonI18NService;
    @Resource(name = "wholescriptsCartValidationDao")
    private WholescriptsCartValidationDao wholescriptsCartValidationDao;
    @Resource(name = "configurationService")
    private ConfigurationService configurationService;

    @Resource(name = "wholescriptsUserService")
    private WholescriptsUserService wholescriptsUserService;
    @Resource(name = "cmsSiteService")
    private CMSSiteService cmsSiteService;

    @Resource(name = "wholescriptsProductsService")
    WholescriptsProductsService wholescriptsProductsService;

    @Resource(name = "siteBaseUrlResolutionService")
    private SiteBaseUrlResolutionService siteBaseUrlResolutionService;
    


    private static ReferralCodesListRequestData createReferralCodesRequestData(final B2BCustomerModel b2bCustomer,
                                                                               final ReferralCodeWsDTO referralCodeRequest) {
        final ReferralCodesListRequestData requestBody = new ReferralCodesListRequestData();
        final ReferralCodesItemData referralCodesItemData = new ReferralCodesItemData();
        final List<ReferralCodesRequestData> referralCodesListRequestData = new ArrayList<>();

        final ReferralCodesRequestData referralCodesRequestData = new ReferralCodesRequestData();
        referralCodesRequestData.setRefCode(StringUtils.isNotEmpty(referralCodeRequest.getReferralCode()) ? referralCodeRequest.getReferralCode().toUpperCase() : StringUtils.EMPTY);
        referralCodesRequestData.setRefName(StringUtils.isNotEmpty(referralCodeRequest.getLastName()) ? referralCodeRequest.getLastName().toUpperCase() : StringUtils.EMPTY);
        referralCodesRequestData.setDiscount(referralCodeRequest.getDiscount());
        referralCodesRequestData.setAccessLevel("1");
		  /*
		   * if (Objects.nonNull(b2bCustomer) && Objects.nonNull(b2bCustomer.getDefaultB2BUnit()) &&
		   * Objects.nonNull(b2bCustomer.getDefaultB2BUnit().getSapCustomerGroup())) { if
		   * ("01".equals(b2bCustomer.getDefaultB2BUnit().getSapCustomerGroup().getCode())) {
		   * referralCodesRequestData.setRefCodeType("ZREFCD"); } else if
		   * ("02".equals(b2bCustomer.getDefaultB2BUnit().getSapCustomerGroup().getCode())) {
		   * referralCodesRequestData.setRefCodeType("ZREFC1"); } }
		   */
        referralCodesListRequestData.add(referralCodesRequestData);
        referralCodesItemData.setItems(referralCodesListRequestData);
        requestBody
                .setAccountNumber(WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(b2bCustomer.getDefaultB2BUnit().getUid()));
        requestBody.setReferralCodes(referralCodesItemData);
        return requestBody;
    }

    @Override
    public AccuralsPDFResponseData getPDFforAccurals(final B2BCustomerModel b2bCustomer, final String billingDoc) throws Exception {
        AccuralsPDFResponseData responseData = new AccuralsPDFResponseData();
        if (Objects.nonNull(b2bCustomer.getDefaultB2BUnit())) {
            final AccuralsPDFRequestData requestBody = new AccuralsPDFRequestData();
            requestBody.setBilling_doc(billingDoc);
            final ObjectMapper objectMapper = new ObjectMapper();
            final HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(WholescriptsCoreUtils.getSCPIOauthAccessToken(getConsumedDestinationModel()));
            final RestTemplate restTemplate = new RestTemplate();
            final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
            final String pdfDestName = Config.getParameter("accurals.pdf.destination.name");
            try {
                final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(pdfDestName);
                final ResponseEntity<String> response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST,
                        requestEntity, String.class);
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
                responseData = objectMapper.readValue(response.getBody(), AccuralsPDFResponseData.class);
                saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, pdfDestName);
            } catch (Exception ex) {
                saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, pdfDestName);
                throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
            }

        }
        return responseData;
    }

    private void saveIntegrationModel(final String requestBody, final ObjectMapper objectMapper,
                                      final String responseBody, final UserModel customer,
                                      final String status, final String destName) throws JsonProcessingException {
        try {
            final WholescriptsIntegrationsModel integration = modelService.create(WholescriptsIntegrationsModel.class);
            integration.setRequest(objectMapper.writeValueAsString(requestBody) + "-" + LocalDateTime.now().toString());
            // getting DataIntegrityViolationException while saving response beacause of too long data so need to fix
            integration.setResponse(responseBody);
            integration.setUserId(customer);
            integration.setDestinationName(destName);
            integration.setStatus(status);
            modelService.save(integration);
        } catch (final Exception e) {
            LOG.info("EXCEPtion while saving integration table:::" + e.getMessage());
        }
    }

    @Override
    public AccuralsInvoiceResponseListData getInvoivcesForAccurals(final B2BCustomerModel b2bCustomer, final boolean isPatient,
                                                                   final String startDate, final String endDate) throws Exception {
        AccuralsInvoiceResponseListData responseData = new AccuralsInvoiceResponseListData();
        if (Objects.nonNull(b2bCustomer.getDefaultB2BUnit())) {
            final AccuralsInvoiceRequestData requestBody = new AccuralsInvoiceRequestData();
            createAccuralInvoiceRequest(b2bCustomer, isPatient, startDate, endDate, requestBody);
            final ObjectMapper objectMapper = new ObjectMapper();
            final HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(WholescriptsCoreUtils.getSCPIOauthAccessToken(getConsumedDestinationModel()));
            final RestTemplate restTemplate = new RestTemplate();
            final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
            final String invoiceDestName = Config.getParameter("accurals.invoice.list.destination.name");
            try {
                final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(invoiceDestName);
                final ResponseEntity<String> response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST,
                        requestEntity, String.class);
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
                objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
                objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
                objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
                final ReturnResponseData returnData = objectMapper.readValue(response.getBody(), ReturnResponseData.class);
                saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, invoiceDestName);
                if ("0".equals(returnData.getRETURN()) && "0".equals(returnData.getInv_count())) {
                    return new AccuralsInvoiceResponseListData();
                } else {
                    if ("1".equals(returnData.getRETURN())) {
                        final ReturnErrorMessageData errorMessage = objectMapper.readValue(response.getBody(),
                                ReturnErrorMessageData.class);
                       throw new RequestParameterException(String.format(errorMessage.getReturnMessage().getItems().stream().filter(i -> i.getType().equals("E")).map(i -> i.getMessage()).collect(Collectors.toList()).toString()),
              RequestParameterException.UNKNOWN_IDENTIFIER);
                    } else {
                        responseData = objectMapper.readValue(response.getBody(), AccuralsInvoiceResponseListData.class);
                    }
                }

            } catch (Exception ex) {
                saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, invoiceDestName);
                throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
            }

        }
        return responseData;
    }

    private void createAccuralInvoiceRequest(final B2BCustomerModel b2bCustomer, final boolean isPatient, final String startDate,
                                             final String endDate, final AccuralsInvoiceRequestData requestBody) {
        // to do here added some hard coded value will be change later
        final Map<String, String> dateMap = new HashMap<>();
        requestBody
                .setAccount_number(WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(b2bCustomer.getDefaultB2BUnit().getUid()));
        requestBody.setPatient(StringUtils.EMPTY);
        if (isPatient) {
            requestBody.setPatient("X");
        }
        requestBody.setSales_org("1710");
        requestBody.setDist_channel("10");
        requestBody.setDivision("10");
        requestBody.setCompany("1710");
        dateMap.put("START_DATE", startDate);
        dateMap.put("END_DATE", endDate);
        requestBody.setDate_range(dateMap);
        requestBody.setCount(StringUtils.EMPTY);
        requestBody.setEnd_rec("0");
        requestBody.setStart_rec("0");
    }

    @Override
    public void createReferralCodeData(final B2BCustomerModel b2bCustomer, final ReferralCodeWsDTO referralCodeRequest)
            throws Exception {
        ReturnResponseData referralCodesResponseData = new ReturnResponseData();
        if (Objects.nonNull(b2bCustomer.getDefaultB2BUnit())) {
            final ReferralCodesListRequestData requestBody = createReferralCodesRequestData(b2bCustomer, referralCodeRequest);
            final ObjectMapper objectMapper = new ObjectMapper();
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(WholescriptsCoreUtils.getSCPIOauthAccessToken(getConsumedDestinationModel()));
            final RestTemplate restTemplate = new RestTemplate();
            final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
            final String referralCodesAddDestName = Config.getParameter("referralCodes.sendRequest.destination.name");
            try {
                final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(referralCodesAddDestName);
                final ResponseEntity<String> response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST,
                        requestEntity, String.class);
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
                referralCodesResponseData = objectMapper.readValue(response.getBody(), ReturnResponseData.class);
                saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, referralCodesAddDestName);
                validateReferralCodes(referralCodesResponseData, objectMapper, response);

            } catch (Exception ex) {
                saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, referralCodesAddDestName);
                throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
            }

        }
    }

    @Override
    public void updateReferralCodeData(final B2BCustomerModel b2bCustomer, String referralCode, int discount)
            throws Exception {
        ReturnResponseData referralCodesResponseData = new ReturnResponseData();

        if (Objects.nonNull(b2bCustomer.getDefaultB2BUnit())) {
            UpdateReferralCodesRequestData referralCodesRequestData = new UpdateReferralCodesRequestData();
            ReferralCodesUpdateRequestData requestData = new ReferralCodesUpdateRequestData();
            requestData.setRefCode(referralCode.split("\\|")[0]);
            requestData.setRefName(referralCode.split("\\|")[1]);
            requestData.setDiscount(discount);
            requestData.setAccessLevel("1");
            referralCodesRequestData.setAccountNumber(WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(b2bCustomer.getDefaultB2BUnit().getUid()));
            referralCodesRequestData.setReferralCodes(requestData);

            final ObjectMapper objectMapper = new ObjectMapper();
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(WholescriptsCoreUtils.getSCPIOauthAccessToken(getConsumedDestinationModel()));
            final RestTemplate restTemplate = new RestTemplate();
            final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(referralCodesRequestData), headers);
            final String referralCodesDestNamel = Config.getParameter("referralCodes.updateRequest.destination.name");
            try {
                final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(referralCodesDestNamel);
                final ResponseEntity<String> response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST,
                        requestEntity, String.class);
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
                objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
                referralCodesResponseData = objectMapper.readValue(response.getBody(), ReturnResponseData.class);
                saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, referralCodesDestNamel);
                validateReferralCodes(referralCodesResponseData, objectMapper, response);
            } catch (Exception ex) {
                saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, referralCodesDestNamel);
                throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
            }

        }
    }

    private void validateReferralCodes(final ReturnResponseData referralCodesResponseData,
                                       final ObjectMapper objectMapper,
                                       final ResponseEntity<String> response) throws JsonProcessingException {
        if ("1".equals(referralCodesResponseData.getRETURN())) {
            final ReturnReferralErrorMessageData errorMessage =
                    objectMapper.readValue(response.getBody(), ReturnReferralErrorMessageData.class);

            ReturnMessageData item = Optional.ofNullable(errorMessage)
                    .map(ReturnReferralErrorMessageData::getReturnMessage)
                    .map(ReturnReferralMessageItemData::getItem)
                    .orElse(null);

            if (item != null && "E".equalsIgnoreCase(item.getType())) {
                throw new RequestParameterException(
                        item.getMessage(),
                        RequestParameterException.UNKNOWN_IDENTIFIER
                );
            }
        }
    }





    private ConsumedDestinationModel getConsumedDestinationModel() {
        // here we are using  ConsumedDestinationModel to get only oauth creadentials  . not using destination
        return destinationSearchService.findDestination(SCPI_OAUTH_DESTINATION);
    }

    @Override
    public Map<String,String> referralValidationFromSCPI(final Map<String, String> requestBody) throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(WholescriptsCoreUtils.getSCPIOauthAccessToken(getConsumedDestinationModel()));
        final RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
        final UserModel b2bCustomer = userService.getCurrentUser();
        final String referralDestName = Config.getParameter("referral.verification.destination.name");
        try {
            final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(referralDestName);
            final ResponseEntity<String> response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST,
                    requestEntity, String.class);
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            final ReturnResponseData responseData = objectMapper.readValue(response.getBody(), ReturnResponseData.class);
            saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, referralDestName);
            Map<String,String> discountMap = new HashMap<>();
            if ("0".equals(responseData.getRETURN())) {
            	JSONObject jsonObject = new JSONObject(response.getBody());
               String discount = jsonObject.getString("DISCOUNT");
               discountMap.put("isValid", "true");
               discountMap.put("discount", "0");
               if(StringUtils.isNotEmpty(discount)) {
               	discountMap.put("discount", discount);
               }
                return discountMap;
            }
            discountMap.put("isValid", "false");
            discountMap.put("discount", "0");
            return discountMap;
        } catch (Exception ex) {
            saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, referralDestName);
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
        }
    }

    @Override
    public boolean emailValidationFromSCPI(final String email) throws Exception {
        final SAPConfigurationModel configModel = getSapConfiguration();
        if (Objects.nonNull(configModel)) {
            final EmailVerificationRequestData requestBody = new EmailVerificationRequestData();
            requestBody.setAccount(StringUtils.EMPTY);
            requestBody.setEmail(email);
            requestBody.setCompany(configModel.getSapcommon_salesOrganization());
            requestBody.setSalesOrg(configModel.getSapcommon_salesOrganization());
            requestBody.setDistChannel(configModel.getSapcommon_distributionChannel());
            requestBody.setDivision(configModel.getSapcommon_division());
            final ObjectMapper objectMapper = new ObjectMapper();
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(WholescriptsCoreUtils.getSCPIOauthAccessToken(getConsumedDestinationModel()));
            final RestTemplate restTemplate = new RestTemplate();
            final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
            final UserModel b2bCustomer = userService.getCurrentUser();
            final String emailVerificationDestName = Config.getParameter("email.verification.destination.name");
            try {
                final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(emailVerificationDestName);
                final ResponseEntity<String> response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST,
                        requestEntity, String.class);
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
                objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
                final ReturnResponseData responseData = objectMapper.readValue(response.getBody(), ReturnResponseData.class);
                saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, emailVerificationDestName);
                if ("1".equals(responseData.getRETURN())) {
                    return true;
                }
                return false;
            } catch (Exception ex) {
                saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, emailVerificationDestName);
                throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
            }

        }
        throw new RequestParameterException(String.format("Sap configuartion model is not found"),
                RequestParameterException.MISSING);
    }

    private SAPConfigurationModel getSapConfiguration() {
        final BaseStoreModel baseStore = baseStoreService.getCurrentBaseStore();
        return baseStore.getSAPConfiguration();
    }

    private SAPHTTPDestinationModel getHttpDestinationModel(final String httpDestName) {
        return httpDestinationService.getHttpDestinationsByName(httpDestName);
    }

    @Override
    public RegistraionResponseData createAccount(final RegistraionRequestData requestBody) throws Exception {
        final SAPConfigurationModel configModel = getSapConfiguration();
        if (Objects.nonNull(configModel)) {
            createRequestForAccountCreation(requestBody, configModel);
            RegistraionResponseData registrationResponse = new RegistraionResponseData();
            final ObjectMapper objectMapper = new ObjectMapper();
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(WholescriptsCoreUtils.getSCPIOauthAccessToken(getConsumedDestinationModel()));
            final RestTemplate restTemplate = new RestTemplate();
            final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
            final String patientDestName = Config.getParameter("create.patient.destination.name");
            final UserModel b2bCustomer = userService.getCurrentUser();
            ResponseEntity<String> response = null;
            try {
                final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(patientDestName);
                response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST, requestEntity, String.class);
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
                objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
                final ReturnResponseData responseData = objectMapper.readValue(response.getBody(), ReturnResponseData.class);
                saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, patientDestName);
                if ("0".equals(responseData.getRETURN())) {
                    registrationResponse = objectMapper.readValue(response.getBody(), RegistraionResponseData.class);
                    registrationResponse.setReferralCodeData(requestBody.getReferralCode());
                    registrationResponse.setAccountType(requestBody.getAccountType());
                    registrationResponse.setSapDeliveryBlock(requestBody.getDeliveryBlock());
                    registrationResponse.setSapPaymentTerms(requestBody.getPaymentTerms());
                    registrationResponse.setSapCustomerPriceGroup(requestBody.getPriceGroup());
                    registrationResponse.setSapShippingCond(requestBody.getShipMethod());
                    registrationResponse.setSapCustomerClassfication(requestBody.getCustomerClass());
                    registrationResponse.setIncoTerms(requestBody.getSalesData().getIncoTerms());
                    return registrationResponse;
                }
            } catch (Exception ex) {
                saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, patientDestName);
                throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
            }

            final ReturnErrorMessageData errorMessage = objectMapper.readValue(response.getBody(), ReturnErrorMessageData.class);
             throw new RequestParameterException(String.format(errorMessage.getReturnMessage().getItems().stream().filter(i -> i.getType().equals("E")).map(i -> i.getMessage()).collect(Collectors.toList()).toString()),
              RequestParameterException.UNKNOWN_IDENTIFIER);
        }
        throw new RequestParameterException(String.format("Sap configuartion model is not found"),
                RequestParameterException.MISSING);
    }


    private void createRequestForAccountCreation(final RegistraionRequestData requestBody, final SAPConfigurationModel configModel) {
        B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
        requestBody.setCompany(configModel.getSapcommon_salesOrganization());
        requestBody.setSalesOrg(configModel.getSapcommon_salesOrganization());
        requestBody.setDistChannel(configModel.getSapcommon_distributionChannel());
        requestBody.setDivision(configModel.getSapcommon_division());
        if (Objects.nonNull(requestBody.getReferralCode()) && StringUtils.isNotEmpty(requestBody.getReferralCode().getRefCode())) {
      	  final ReferralCodesRequestData referralRequest = requestBody.getReferralCode();
      	  if ("01".equals(requestBody.getAccountType())) {
      		  referralRequest.setRefCodeType("ZREFCD");
      	  } else if ("02".equals(requestBody.getAccountType())) {
      		  referralRequest.setRefCodeType("ZREFC1");
      	  }
      	  referralRequest.setAccessLevel("1");
        } else {
      	  //if refferal code is null then setting blank to avoid error from sap
      	  final ReferralCodesRequestData referralRequest = new ReferralCodesRequestData();
      	  referralRequest.setRefCodeType("");
      	  referralRequest.setAccessLevel("");
      	  referralRequest.setRefCode("");
      	  referralRequest.setRefName("");
      	  requestBody.setReferralCode(referralRequest);
        }
        // setting blank to avoid error from sap
        if (Objects.nonNull(requestBody.getReferralCode()) && "02".equals(requestBody.getAccountType()) && Objects.nonNull(requestBody.getReferralCode().getDiscount())) {
      	  requestBody.setDiscount(String.valueOf(requestBody.getReferralCode().getDiscount()));
        }else {
      	  requestBody.setDiscount("");        
        }
        //setting blank to avoid error from sap
        if (Objects.isNull(requestBody.getPersonData())) {
            final RegistraionPersonData persondata = new RegistraionPersonData();
            persondata.setFirstName("");
            persondata.setLastName("");
            requestBody.setPersonData(persondata);
        }
        requestBody.setPriceGroup(configModel.getSapCustomerPriceGroup());
        requestBody.setPaymentTerms(configModel.getCreditMemoPytTerm());
        requestBody.setOrigin("WEB");
  
        final SAPDeliveryModeModel deliveryMode = configModel.getSapDeliveryModes().stream().findFirst().orElse(null);
        if (Objects.nonNull(deliveryMode)) {
            requestBody.setShipMethod(deliveryMode.getDeliveryValue());
        }else {
      	  requestBody.setShipMethod("01");
        }
        requestBody.setAccessLevel("1");
        requestBody.setBpType("9001");
        final RegistrationCompanyData companyData = new RegistrationCompanyData();
        companyData.setBurks(configModel.getSapcommon_salesOrganization());
        companyData.setPterms(configModel.getCreditMemoPytTerm());
        companyData.setReconAct(configModel.getReconciliationAcct());
        companyData.setSortKey(configModel.getSortkey());
        companyData.setCreditmPterms(configModel.getCreditMemoPytTerm());
        companyData.setBankStatement(configModel.getBankStatement());
        requestBody.setCompanyData(companyData);
        final RegistrationSalesData salesData = requestBody.getSalesData();
        salesData.setVkorg(configModel.getSapcommon_salesOrganization());
        salesData.setVtweg(configModel.getSapcommon_distributionChannel());
        salesData.setSpart(configModel.getSapcommon_division());
        salesData.setPriceProc("5");
        if (requestBody.getAccountType().equals("01")) {
            salesData.setPriceProc("1");
        }
        salesData.setPriceList("");
        salesData.setDeliveryPri(configModel.getDeliveryPriority());
        salesData.setIncoTerms(configModel.getIncoterms());
        salesData.setIncoTermsLoc(requestBody.getAddressData().getCity() + "," + requestBody.getAddressData().getRegion());
        salesData.setActAsGrp(configModel.getActAsGrp());
        salesData.setMaxPrtlDlv(configModel.getMaxPartDeliveries());
        salesData.setTaxClass(configModel.getTaxClass());
        requestBody.setSalesData(salesData);
        //setting blank to avoid error from sap
        if (Objects.isNull(requestBody.getOrganization())) {
            final OrganizationData organizationData = new OrganizationData();
            organizationData.setName1("");
            organizationData.setName2("");
            requestBody.setOrganization(organizationData);
        }
    }

    @Override
    public AddressVerificationResponseData verifyAddressFromUPS(final AddressVerificationRequestData requestBody) throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        final HttpHeaders headers = new HttpHeaders();
        final ConsumedDestinationModel consumed = destinationSearchService.findDestination(UPS_BASIC_DESTINATION);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(WholescriptsCoreUtils.getUPSAccessToken(consumed));
        final RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
        final String addressVerificationDestName = Config.getParameter("ups.address.verification.destination.name");
        final UserModel b2bCustomer = userService.getCurrentUser();
        try {
            final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(addressVerificationDestName);
            final ResponseEntity<String> response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST,
                    requestEntity, String.class);
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            final AddressVerificationResponseData responseData = objectMapper.readValue(response.getBody(),
                    AddressVerificationResponseData.class);
            saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, addressVerificationDestName);
            return responseData;
        } catch (Exception ex) {
            saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, addressVerificationDestName);
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
        }
    }

    @Override
    public USPSAddressValidateResponseData verifyAddressFromUSPS(final String requestData) throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_XML));
        final RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        final String addressVerificationDestName = Config.getParameter("usps.address.verification.destination.name");
        final UserModel b2bCustomer = userService.getCurrentUser();
        try {
            final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(addressVerificationDestName);
            final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(httpDestination.getTargetURL());
            builder.queryParam("API", URLEncoder.encode("Verify", ENCODE_UTF8));
            builder.queryParam("XML", URLEncoder.encode(requestData));
            final ResponseEntity<String> response = restTemplate.exchange(builder.build(true).encode().toUri(), HttpMethod.POST,
                    requestEntity, String.class);
            String[] tagsToWrap = { "Zip5", "Zip4", "DeliveryPoint" };
            String safeXml =WholescriptsCoreUtils.wrapWithCDataForTags(response.getBody(), tagsToWrap);
            final JSONObject json = XML.toJSONObject(safeXml);
            final String convertINJson = json.toString(4);
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            final USPSAddressValidateResponseData responseData = objectMapper.readValue(convertINJson,
                    USPSAddressValidateResponseData.class);
            saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, addressVerificationDestName);
            return responseData;
        } catch (Exception ex) {
            saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, addressVerificationDestName);
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
        }
    }

    @Override
    public AccertifyVerificationResponseData accertifyLoginVerificaton(final AccertifyVerificationRequestData requestBody)
            throws Exception {
        final ConsumedDestinationModel consumed = destinationSearchService.findDestination(ACCERTIFY_LOGIN_BASIC_DESTINATION);
        final BasicCredentialModel basic = (BasicCredentialModel) consumed.getCredential();
        final String auth = basic.getUsername() + ":" + basic.getPassword();
        final String accertifyLoginURL = consumed.getUrl();
        final String basicAuth = "Basic" + " " + Base64.getEncoder().encodeToString(auth.getBytes());
        final RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", basicAuth);
        headers.setContentType(MediaType.APPLICATION_JSON);
        final ObjectMapper objectMapper = new ObjectMapper();
        final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
        final UserModel b2bCustomer = userService.getCurrentUser();
        try {
            final ResponseEntity<String> response = restTemplate.exchange(accertifyLoginURL, HttpMethod.POST, requestEntity,
                    String.class);
            saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, consumed.getId());
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            return objectMapper.readValue(response.getBody(), AccertifyVerificationResponseData.class);
        } catch (Exception ex) {
            saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, consumed.getId());
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
        }

    }

    @Override
    public OrderHistoryListResponseData getOrderHistoryListForPatient(final B2BCustomerModel b2bCustomer, final String startDate,
                                                                      final String endDate) throws Exception {
        OrderHistoryListResponseData responseData = new OrderHistoryListResponseData();

        final OrderHistoryListRequestData requestBody = new OrderHistoryListRequestData();
        createOrderHistoryPatientsRequest(b2bCustomer, startDate, endDate, requestBody);
        final ObjectMapper objectMapper = new ObjectMapper();
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(WholescriptsCoreUtils.getSCPIOauthAccessToken(getConsumedDestinationModel()));
        final RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
        final String orderHistoryDestName = Config.getParameter("patient.order.history.destination.name");
        try {
            final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(orderHistoryDestName);
            final ResponseEntity<String> response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST,
                    requestEntity, String.class);
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
            objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
            objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            final ReturnResponseData returnData = objectMapper.readValue(response.getBody(), ReturnResponseData.class);
            final OrderHistoryListResponseData returnResponseData = objectMapper.readValue(response.getBody(),
                    OrderHistoryListResponseData.class);
            saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, orderHistoryDestName);
            if ("0".equals(returnData.getRETURN()) && "0".equals(returnResponseData.getOrderCount())) {
                return new OrderHistoryListResponseData();
            }
            if ("1".equals(returnData.getRETURN())) {
                final ReturnErrorMessageData errorMessage = objectMapper.readValue(response.getBody(), ReturnErrorMessageData.class);
                throw new RequestParameterException(String.format(errorMessage.getReturnMessage().getItems().stream().filter(i -> i.getType().equals("E")).map(i -> i.getMessage()).collect(Collectors.toList()).toString()),
              RequestParameterException.UNKNOWN_IDENTIFIER);

            }
            responseData = objectMapper.readValue(response.getBody(), OrderHistoryListResponseData.class);
            return responseData;

        } catch (Exception ex) {
            saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, orderHistoryDestName);
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
        }

    }


    private void createOrderHistoryPatientsRequest(final B2BCustomerModel b2bCustomer, final String startDate,
                                                   final String endDate, final OrderHistoryListRequestData requestBody) {

        final SAPConfigurationModel configModel = getSapConfiguration();
        if (Objects.nonNull(configModel)) {
            final Map<String, String> dateMap = new HashMap<>();
            dateMap.put("START_DATE", startDate);
            dateMap.put("END_DATE", endDate);
            requestBody.setDateRange(dateMap);
            requestBody.setAccountNumber(
                    WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(b2bCustomer.getDefaultB2BUnit().getUid()));
            requestBody.setSalesOrg(configModel.getSapcommon_salesOrganization());
            requestBody.setDistChannel(configModel.getSapcommon_distributionChannel());
            requestBody.setDivision(configModel.getSapcommon_division());
            requestBody.setMaxCount("0");
            requestBody.setOrHdr("X");
            requestBody.setOrCcard("X");
            requestBody.setOrItems("X");
            requestBody.setOrAddr("X");
            requestBody.setDlHdr("X");
            requestBody.setDlItems("X");
            requestBody.setInHdr("X");
            requestBody.setInItems("X");
            requestBody.setCount("");
            requestBody.setStartRec("0");
            requestBody.setEndRec("0");
            requestBody.setActype("PA");
            requestBody.setOpenOrders("");
            requestBody.setOrderNumber("");
        }
    }


    @Override
    public OrderHistoryPractionerResponseoneData getOrderHistoryListByPatientID(final B2BCustomerModel b2bCustomer,
                                                                                    final String startDate, final String endDate) throws Exception {
        OrderHistoryPractionerResponseoneData responseData = new OrderHistoryPractionerResponseoneData();

        if (Objects.nonNull(b2bCustomer.getDefaultB2BUnit())) {
            final OrderHistoryPractionerRequestData requestBody = new OrderHistoryPractionerRequestData();
            createOrderHistoryPatientsByIdRequest(b2bCustomer, startDate, endDate, requestBody);
            final ObjectMapper objectMapper = new ObjectMapper();
            final HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(WholescriptsCoreUtils.getSCPIOauthAccessToken(getConsumedDestinationModel()));
            final RestTemplate restTemplate = new RestTemplate();
            final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
            final String orderHistoryByPatientIDDestName = Config.getParameter("order.history.destination.name.ByPatientID");
            try {
                final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(orderHistoryByPatientIDDestName);
                final ResponseEntity<String> response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST,
                        requestEntity, String.class);
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
                objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
                objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
                objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
                final ReturnResponseData returnData = objectMapper.readValue(response.getBody(), ReturnResponseData.class);
                final OrderHistoryPractionerResponseoneData returnResponseData = objectMapper.readValue(response.getBody(),
                        OrderHistoryPractionerResponseoneData.class);
                saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, orderHistoryByPatientIDDestName);
                if ("0".equals(returnData.getRETURN()) && "0".equals(returnResponseData.getOrder_count())) {
                    return new OrderHistoryPractionerResponseoneData();
                }
                if ("1".equals(returnData.getRETURN())) {
                    final ReturnErrorMessageData errorMessage = objectMapper.readValue(response.getBody(), ReturnErrorMessageData.class);
      throw new RequestParameterException(String.format(errorMessage.getReturnMessage().getItems().stream().filter(i -> i.getType().equals("E")).map(i -> i.getMessage()).collect(Collectors.toList()).toString()),
              RequestParameterException.UNKNOWN_IDENTIFIER);
                }
                responseData = objectMapper.readValue(response.getBody(), OrderHistoryPractionerResponseoneData.class);

            } catch (Exception ex) {
                saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, orderHistoryByPatientIDDestName);
                throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
            }

        }
        return responseData;
    }

    private void createOrderHistoryPatientsByIdRequest(final B2BCustomerModel b2bCustomer, final String startDate,
                                                   final String endDate, final OrderHistoryPractionerRequestData requestBody) {

        final SAPConfigurationModel configModel = getSapConfiguration();
        final Map<String, String> dateMap = new HashMap<>();
        dateMap.put("START_DATE", startDate);
        dateMap.put("END_DATE", endDate);
        requestBody.setDateRange(dateMap);
        requestBody.setAccountNumber(WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(b2bCustomer.getDefaultB2BUnit().getUid()));
        requestBody.setSalesOrg(configModel.getSapcommon_salesOrganization());
        requestBody.setDistChannel(configModel.getSapcommon_distributionChannel());
        requestBody.setDivision(configModel.getSapcommon_division());
        requestBody.setSearchType("");
        requestBody.setSearchString("");
        requestBody.setOpenOrders("");
        requestBody.setMaxCount("0");
        requestBody.setInitialSearch("X");
        requestBody.setStartRec("0");
        requestBody.setEndRec("0");
        requestBody.setActype("PA");
    }



    @Override
    public OrderDetailsResponseData getOrderDetails(final B2BCustomerModel b2bCustomer, final String orderNumber, final BaseStoreModel baseStore) throws Exception {
        OrderDetailsResponseData responseData = new OrderDetailsResponseData();

        if (Objects.nonNull(b2bCustomer.getDefaultB2BUnit())) {
            final OrderDetailsRequestData requestBody = new OrderDetailsRequestData();
            createOrderDetailsRequest(b2bCustomer, null, null, requestBody, orderNumber, baseStore);
            final ObjectMapper objectMapper = new ObjectMapper();
            final HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(WholescriptsCoreUtils.getSCPIOauthAccessToken(getConsumedDestinationModel()));
            final RestTemplate restTemplate = new RestTemplate();
            final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
            final String orderHistoryDestName = Config.getParameter("order.details.eco.destination.name");

            try {
                final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(orderHistoryDestName);
                final ResponseEntity<String> response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST,
                        requestEntity, String.class);
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
                objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
                objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
                objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
                final ReturnResponseData returnData = objectMapper.readValue(response.getBody(), ReturnResponseData.class);
                final OrderDetailsResponseData returnResponseData = objectMapper.readValue(response.getBody(),
                        OrderDetailsResponseData.class);
                saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, orderHistoryDestName);
                if ("0".equals(returnData.getRETURN()) && "0".equals(returnResponseData.getOrderCount())) {
                    return new OrderDetailsResponseData();
                }
                if ("1".equals(returnData.getRETURN())) {
                    final ReturnErrorMessageData errorMessage = objectMapper.readValue(response.getBody(), ReturnErrorMessageData.class);
                     throw new RequestParameterException(String.format(errorMessage.getReturnMessage().getItems().stream().filter(i -> i.getType().equals("E")).map(i -> i.getMessage()).collect(Collectors.toList()).toString()),
              RequestParameterException.UNKNOWN_IDENTIFIER);
                }
                responseData = objectMapper.readValue(response.getBody(), OrderDetailsResponseData.class);
            } catch (Exception ex) {
                saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, orderHistoryDestName);
                throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
            }

        }
        return responseData;
    }


    private void createOrderDetailsRequest(final B2BCustomerModel b2bCustomer, final String startDate, final String endDate,
                                           final OrderDetailsRequestData requestBody, final String orderNumber,
                                           final BaseStoreModel baseStore) {

        final SAPConfigurationModel configModel = baseStore.getSAPConfiguration();
        if (Objects.nonNull(configModel)) {
            final Map<String, String> dateMap = new HashMap<>();
            dateMap.put("START_DATE", startDate);
            dateMap.put("END_DATE", endDate);
            requestBody.setDateRange(dateMap);

            requestBody.setAccountNumber(
                    WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(b2bCustomer.getDefaultB2BUnit().getUid()));
            requestBody.setSalesOrg(configModel.getSapcommon_salesOrganization());
            requestBody.setDistChannel(configModel.getSapcommon_distributionChannel());
            requestBody.setDivision(configModel.getSapcommon_division());
            requestBody.setOpenOrders("X");
            requestBody.setOrderNumber(orderNumber);
            requestBody.setMaxCount("");
            requestBody.setCount("");
            requestBody.setStartRec("0");
            requestBody.setEndRec("0");
            requestBody.setActype("PA");
            requestBody.setOrHdr("X");
            requestBody.setOrCcard("X");
            requestBody.setOrItems("X");
            requestBody.setOrAddr("X");
            requestBody.setDlHdr("X");
            requestBody.setDlItems("X");
            requestBody.setInHdr("X");
            requestBody.setInItems("X");
            requestBody.setOrDisc("X");
        }
    }


    @Override
    public OrderHistoryPractionerResponseoneData getOrderHistoryListForPractitioner(final B2BCustomerModel b2bCustomer,
                                                                                    final String startDate, final String endDate) throws Exception {
        OrderHistoryPractionerResponseoneData responseData = new OrderHistoryPractionerResponseoneData();

        if (Objects.nonNull(b2bCustomer.getDefaultB2BUnit())) {
            final OrderHistoryPractionerRequestData requestBody = new OrderHistoryPractionerRequestData();
            createOrderHistoryListPractitionerRequest(b2bCustomer, startDate, endDate, requestBody);
            final ObjectMapper objectMapper = new ObjectMapper();
            final HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(WholescriptsCoreUtils.getSCPIOauthAccessToken(getConsumedDestinationModel()));
            final RestTemplate restTemplate = new RestTemplate();
            final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
            final String orderHistoryDestName = Config.getParameter("practitioner.order.history.destination.name");
            try {
                final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(orderHistoryDestName);
                final ResponseEntity<String> response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST,
                        requestEntity, String.class);
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
                objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
                objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
                objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
                final ReturnResponseData returnData = objectMapper.readValue(response.getBody(), ReturnResponseData.class);
                final OrderHistoryPractionerResponseoneData returnResponseData = objectMapper.readValue(response.getBody(),
                        OrderHistoryPractionerResponseoneData.class);
                saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, orderHistoryDestName);
                if ("0".equals(returnData.getRETURN()) && "0".equals(returnResponseData.getOrder_count())) {
                    return new OrderHistoryPractionerResponseoneData();
                }
                if ("1".equals(returnData.getRETURN())) {
                    final ReturnErrorMessageData errorMessage = objectMapper.readValue(response.getBody(), ReturnErrorMessageData.class);
                    throw new RequestParameterException(String.format(errorMessage.getReturnMessage().getItems().stream().filter(i -> i.getType().equals("E")).map(i -> i.getMessage()).collect(Collectors.toList()).toString()),
              RequestParameterException.UNKNOWN_IDENTIFIER);
                }
                responseData = objectMapper.readValue(response.getBody(), OrderHistoryPractionerResponseoneData.class);

            } catch (Exception ex) {
                saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, orderHistoryDestName);
                throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
            }

        }
        return responseData;
    }


    private void createOrderHistoryListPractitionerRequest(final B2BCustomerModel b2bCustomer, final String startDate,
                                                           final String endDate, final OrderHistoryPractionerRequestData requestBody) {
        final SAPConfigurationModel configModel = getSapConfiguration();
        if (Objects.nonNull(configModel)) {
            final Map<String, String> dateMap = new HashMap<>();
            dateMap.put("START_DATE", startDate);
            dateMap.put("END_DATE", endDate);
            requestBody.setDateRange(dateMap);
            requestBody.setAccountNumber(
                    WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(b2bCustomer.getDefaultB2BUnit().getUid()));
            requestBody.setSalesOrg(configModel.getSapcommon_salesOrganization());
            requestBody.setDistChannel(configModel.getSapcommon_distributionChannel());
            requestBody.setDivision(configModel.getSapcommon_division());
            requestBody.setSearchType("");
            requestBody.setSearchString("");
            requestBody.setOpenOrders("");
            requestBody.setMaxCount("");
            requestBody.setInitialSearch("X");
            requestBody.setCount("");
            requestBody.setStartRec("");
            requestBody.setEndRec("");
            requestBody.setActype("PR");
        }
    }


    @Override
    public SapAddressResponseData createAddressinSAP(final AddressData address, final B2BCustomerModel b2bCustomer)
            throws Exception {
        final SAPConfigurationModel configModel = getSapConfiguration();
        if (Objects.nonNull(configModel)) {
            final CreateAddressInSAPData requestBody = createRequestForAddressCreation(address, configModel, b2bCustomer);
            SapAddressResponseData addressResponse = new SapAddressResponseData();
            final ObjectMapper objectMapper = new ObjectMapper();
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(WholescriptsCoreUtils.getSCPIOauthAccessToken(getConsumedDestinationModel()));
            final RestTemplate restTemplate = new RestTemplate();
            final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
            ResponseEntity<String> response = null;
            final String addressDestName = Config.getParameter("wholescripts.create.address.destination.name");
            try {
                final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(addressDestName);
                response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST,
                        requestEntity, String.class);
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
                objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
                final SapAddressResponseData responseData = objectMapper.readValue(response.getBody(), SapAddressResponseData.class);
                saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, addressDestName);
                if ("0".equals(responseData.getRETURN())) {
                    addressResponse = objectMapper.readValue(response.getBody(), SapAddressResponseData.class);
                    return addressResponse;
                }
            } catch (Exception ex) {
                saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, addressDestName);
                throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
            }

            final ReturnErrorMessageData errorMessage = objectMapper.readValue(response.getBody(), ReturnErrorMessageData.class);
              throw new RequestParameterException(String.format(errorMessage.getReturnMessage().getItems().stream().filter(i -> i.getType().equals("E")).map(i -> i.getMessage()).collect(Collectors.toList()).toString()),
              RequestParameterException.UNKNOWN_IDENTIFIER);
        }
        throw new RequestParameterException(String.format("Sap configuartion model is not found"),
                RequestParameterException.MISSING);
    }

    @Override
    public SapAddressResponseData createAddressinSAP(final WholescriptsExternalAPIAddressData wholescriptsExternalAPIAddressData)
            throws Exception {
        final SAPConfigurationModel configModel = getSapConfiguration();
        if (Objects.nonNull(configModel)) {
            final CreateAddressInSAPData requestBody = createRequestForAddressCreation(wholescriptsExternalAPIAddressData, configModel);
            SapAddressResponseData addressResponse = new SapAddressResponseData();
            final ObjectMapper objectMapper = new ObjectMapper();
            final HttpHeaders headers = new HttpHeaders();
            ResponseEntity<String> response = null;
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(WholescriptsCoreUtils.getSCPIOauthAccessToken(getConsumedDestinationModel()));
            final RestTemplate restTemplate = new RestTemplate();
            final UserModel b2bCustomer = userService.getCurrentUser();
            final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
            final String addressDestName = Config.getParameter("wholescripts.create.address.destination.name");
            try {
                final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(addressDestName);
                response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST,
                        requestEntity, String.class);
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
                objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
                final SapAddressResponseData responseData = objectMapper.readValue(response.getBody(), SapAddressResponseData.class);
                saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), userService.getCurrentUser(), STATUS_INTEGRATION_SUCCESS, addressDestName);
                if ("0".equals(responseData.getRETURN())) {
                    addressResponse = objectMapper.readValue(response.getBody(), SapAddressResponseData.class);
                    return addressResponse;
                }
            } catch (Exception ex) {
                saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, addressDestName);
                throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
            }
            final ReturnErrorMessageData errorMessage = objectMapper.readValue(response.getBody(), ReturnErrorMessageData.class);
            throw new RequestParameterException(String.format(errorMessage.getReturnMessage().getItems().stream().filter(i -> i.getType().equals("E")).map(i -> i.getMessage()).collect(Collectors.toList()).toString()),
              RequestParameterException.UNKNOWN_IDENTIFIER);
        }
        throw new RequestParameterException(String.format("Sap configuartion model is not found"),
                RequestParameterException.MISSING);
    }

    private CreateAddressInSAPData createRequestForAddressCreation(final AddressData address,
                                                                   final SAPConfigurationModel configModel, final B2BCustomerModel b2bCustomer) {
        final CreateAddressInSAPData requestBody = new CreateAddressInSAPData();
        requestBody
                .setAccountNumber(WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(b2bCustomer.getDefaultB2BUnit().getUid()));
        requestBody.setCompany(configModel.getSapcommon_salesOrganization());
        requestBody.setSalesOrg(configModel.getSapcommon_salesOrganization());
        requestBody.setDisChannel(configModel.getSapcommon_distributionChannel());
        requestBody.setDivision(configModel.getSapcommon_division());
        requestBody.setOrigin("WEB");
        final SapPersonData personData = new SapPersonData();
        personData.setFirstName(address.getFirstName());
        personData.setLastName(address.getLastName());
        requestBody.setPersonData(personData);
        final SapAddressData addressData = new SapAddressData();
        addressData.setStreet(address.getLine1());
        addressData.setCity(address.getTown());
        addressData.setHouseNumber("");
        addressData.setPoBox("");
        addressData.setPostalCode(address.getPostalCode());
        addressData.setStrSuppl1(address.getLine2());
        addressData.setStrSuppl2("");
        addressData.setStrSuppl3("");
        addressData.setLangu("EN");
        if (Objects.nonNull(address.getCountry())) {
            addressData.setCountry(address.getCountry().getIsocode());
        }
        if (Objects.nonNull(address.getRegion()) && Objects.nonNull(address.getRegion().getIsocode())) {
            final String region;
            if (address.getRegion().getIsocode().contains("-")) {
                final String[] unit = address.getRegion().getIsocode().split("-");
                addressData.setRegion(unit[1]);
            } else {
                addressData.setRegion(address.getRegion().getIsocode());
            }

        }

        addressData.setEmail(address.getEmail());
        addressData.setTelePhone(address.getCellphone());
        addressData.setMobile(address.getCellphone());
        addressData.setCoName(address.getRemarks());
        requestBody.setAddress(addressData);
        return requestBody;
    }

    private CreateAddressInSAPData createRequestForAddressCreation(final WholescriptsExternalAPIAddressData wholescriptsExternalAPIAddressData,
                                                                   final SAPConfigurationModel configModel) {
        final CreateAddressInSAPData requestBody = new CreateAddressInSAPData();
        requestBody
                .setAccountNumber(WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(wholescriptsExternalAPIAddressData.getCustomerSAPId()));
        requestBody.setCompany(configModel.getSapcommon_salesOrganization());
        requestBody.setSalesOrg(configModel.getSapcommon_salesOrganization());
        requestBody.setDisChannel(configModel.getSapcommon_distributionChannel());
        requestBody.setDivision(configModel.getSapcommon_division());
        requestBody.setOrigin("WEB");

        final SapPersonData personData = new SapPersonData();
        personData.setFirstName(wholescriptsExternalAPIAddressData.getFirstName());
        personData.setLastName(wholescriptsExternalAPIAddressData.getLastName());

        requestBody.setPersonData(personData);
        final SapAddressData addressData = new SapAddressData();

        addressData.setEmail(wholescriptsExternalAPIAddressData.getEmail());
        addressData.setStreet(wholescriptsExternalAPIAddressData.getStreet1());
        addressData.setCity(wholescriptsExternalAPIAddressData.getCity());
        addressData.setPostalCode(wholescriptsExternalAPIAddressData.getZip());
        addressData.setLangu(wholescriptsExternalAPIAddressData.getLanguage());
        addressData.setCountry(wholescriptsExternalAPIAddressData.getCountry());
        addressData.setRegion(wholescriptsExternalAPIAddressData.getRegion());
        addressData.setTelePhone(wholescriptsExternalAPIAddressData.getPhone());
        addressData.setMobile(wholescriptsExternalAPIAddressData.getPhone());
        requestBody.setAddress(addressData);
        return requestBody;
    }


    @Override
    public CreateOrderInSAPResponseData createOrderInSap(final B2BCustomerModel b2bCustomer, final CartModel cartmodel)
            throws Exception {
        SAPConfigurationModel configModel = null;
        if (cartmodel.getExternalOrder()) {
            configModel = cartmodel.getStore().getSAPConfiguration();
        } else {
            configModel = getSapConfiguration();
        }
        if (Objects.nonNull(configModel)) {
            final CreateOrderInSAPRequestData requestBody = createRequestForOrderCreation(b2bCustomer, cartmodel, configModel,false);
            final ObjectMapper objectMapper = new ObjectMapper();
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(WholescriptsCoreUtils.getSCPIOauthAccessToken(getConsumedDestinationModel()));
            final RestTemplate restTemplate = new RestTemplate();
            final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
            final String orderDestName = Config.getParameter("wholescripts.create.order.destination.name");
            ResponseEntity<String> response = null;
            try {
                final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(orderDestName);
                LOG.info("Order create request for cart:" + cartmodel.getCode() + objectMapper.writeValueAsString(requestBody));
                response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST,
                        requestEntity, String.class);
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
                objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
                CreateOrderInSAPResponseData responseData = objectMapper.readValue(response.getBody(),
                        CreateOrderInSAPResponseData.class);
                LOG.info("Order create response for cart: " + cartmodel.getCode() + objectMapper.writeValueAsString(responseData));
                saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, orderDestName);
                if ("0".equals(responseData.getRETURN())) {
                    responseData = objectMapper.readValue(response.getBody(), CreateOrderInSAPResponseData.class);
                    return responseData;
                }
            } catch (Exception ex) {
                saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, orderDestName);
                throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
            }

            final ReturnErrorMessageData errorMessage = objectMapper.readValue(response.getBody(), ReturnErrorMessageData.class);
           throw new RequestParameterException(String.format(errorMessage.getReturnMessage().getItems().stream().filter(i -> i.getType().equals("E")).map(i -> i.getMessage()).collect(Collectors.toList()).toString()),
              RequestParameterException.UNKNOWN_IDENTIFIER);
        }
        throw new RequestParameterException(String.format("Sap configuartion model is not found"),
                RequestParameterException.MISSING);
    }

    private CreateOrderInSAPRequestData createRequestForOrderCreation(final B2BCustomerModel b2bCustomer,
                                                                      final CartModel cartmodel, final SAPConfigurationModel configModel,final boolean orderSimulate) throws Exception {

        final CreateOrderInSAPRequestData requestBody = new CreateOrderInSAPRequestData();
        requestBody.setShipTo(WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(b2bCustomer.getDefaultB2BUnit().getUid()));
        if (Objects.nonNull(cartmodel.getDeliveryAddress())) {
            if (Objects.nonNull(cartmodel.getDeliveryAddress().getSapPublicKey())) {
                final String sapPulicKey = cartmodel.getDeliveryAddress().getSapPublicKey();
                if (StringUtils.isNotEmpty(sapPulicKey) && sapPulicKey.contains("|")) {
                    final String[] addressNumber = sapPulicKey.split("\\|");
                    requestBody.setShipTo(String.format("%010d", Integer.valueOf(addressNumber[1])));
                }
            }
            if (Objects.nonNull(cartmodel.getDeliveryAddress().getRegion())) {
                final String regionCode = cartmodel.getDeliveryAddress().getRegion().getIsocode();
                requestBody.setCarrier(getCarrierCodeForOrder(cartmodel, regionCode));
            }

        }
        requestBody.setCustRefer(cartmodel.getPurchaseOrderNumber());
        requestBody.setSoldTo(WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(b2bCustomer.getDefaultB2BUnit().getUid()));
        requestBody.setSalesOrg(configModel.getSapcommon_salesOrganization());
        requestBody.setDistChannel(configModel.getSapcommon_distributionChannel());
        requestBody.setDivision(configModel.getSapcommon_division());
        requestBody.setOrigin("WEB");
        
        if (sessionService.getAttribute(WholescriptscoreConstants.ACTING_USER_ROLE) != null) {
      	  final List userRole = new ArrayList<>(sessionService.getAttribute(WholescriptscoreConstants.ACTING_USER_ROLE));
           Optional<String> isAsagent=userRole.stream().filter(r -> WholescriptscoreConstants.ASAGENT_GROUP.equalsIgnoreCase(r.toString())).findAny();
           if (isAsagent.isPresent()) {
         	  requestBody.setOrigin("CSW");
           }
           
        }
        
        if (cartmodel.getExternalOrder()) {
      	  requestBody.setOrigin(cartmodel.getSourceApplication().getCode());
        }  
        
        final Date date = new Date();
        final String todayDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        requestBody.setOrderDate(todayDate);
        requestBody.setOrderType(configModel.getSapcommon_transactionType());
        requestBody.setWebOrderNumber(cartmodel.getWebOrderNumber());
        requestBody.setSPECPROCID(cartmodel.getSpecProcId());
        requestBody.setInstructions(cartmodel.getInstructions());
        if (Objects.nonNull(cartmodel.getPaymentType())) {
            requestBody.setPaymentTerms(cartmodel.getPaymentType().getCode());
        }
        if (Objects.nonNull(cartmodel.getDeliveryMode())) {
            requestBody.setShipMethod(cartmodel.getDeliveryMode().getCode());

        }
        if (Objects.nonNull(cartmodel.getDeliveryCost())) {
            requestBody.setShippingCharge(cartmodel.getDeliveryCost().toString());
        }

        CreditCardRequestData creditCardRequest = null;
        
        if ("ZACH".equalsIgnoreCase(cartmodel.getPaymentType().getCode()) ||
					 "ZWIR".equalsIgnoreCase(cartmodel.getPaymentType().getCode())) {
      	  creditCardRequest = createACHPaymentdetails(cartmodel); 
      	  
        } else {
      	  creditCardRequest = createPaymentdetails(cartmodel);  
        }
        
        requestBody.setCreditCardDetails(creditCardRequest);

        final CreateOrderItemsListRequestData itemsList = new CreateOrderItemsListRequestData();
        final List<CreateOrderItemsRequestData> itemsListData = new ArrayList<CreateOrderItemsRequestData>();
        double totalMedpaxQty = 0.0;
        for (final AbstractOrderEntryModel entry : cartmodel.getEntries()) {

            final CreateOrderItemsRequestData itemRequest = new CreateOrderItemsRequestData();
            itemRequest.setEntryNumber(String.format("%06d", entry.getEntryNumber()));
            if (CollectionUtils.isNotEmpty(entry.getEntryGroupNumbers())) {
                itemRequest.setEntryGroupNumber(entry.getEntryGroupNumbers().iterator().next().toString());
            } else {
                itemRequest.setEntryGroupNumber("000000");
            }
            itemRequest.setPoNumber("1");
            itemRequest.setAction("I");
            itemRequest.setMaterial(entry.getProduct().getCode());
            if (Objects.nonNull(entry.getUnit())) {
                itemRequest.setSalesUnit(entry.getUnit().getCode());

            }
            itemRequest.setDescription(entry.getProductDescription());
            if (cartmodel.getExternalOrder()) {
                itemRequest.setDescription(entry.getDescription());

            } else {
                final EntryGroup entryGroup = cartmodel.getEntryGroups().stream()
                        .filter(eg -> eg.getGroupNumber().equals(entry.getEntryNumber())).findFirst().orElse(null);
                if (Objects.nonNull(entryGroup)) {
                    itemRequest.setDescription(entryGroup.getLabel());
                }
            }
            if(!orderSimulate) {
            	itemRequest.setDoseNote(entry.getDoseNote());
            	if(StringUtils.isNotEmpty(entry.getSapTemplateId()) && entry.getSapTemplateId().split("-")[0].equalsIgnoreCase("recommendation")) {
            		itemRequest.setRecommeId(entry.getSapTemplateId().split("-")[1]);  
            	}           	         
            }
            itemRequest.setPrice(entry.getBasePrice().toString());
            // abstract order qty in case of medpax Qty updating in cart page
            int abstractOrderQty = getAbstractOrderQty(entry);
            final List<String> excludedProducts = Arrays.asList(Config.getParameter("medpax.product.exclude").split(","));
            if (!excludedProducts.contains(entry.getProduct().getCode()) && CollectionUtils.isNotEmpty(entry.getEntryGroupNumbers())) {
                if(cartmodel.getExternalOrder()) {
               	 itemRequest.setQuantity(String.format("%013d", entry.getQuantityByCase()));
                } else {
               	 itemRequest.setQuantity(String.format("%013d", entry.getQuantityByCase() * 30 * abstractOrderQty));
                }
                totalMedpaxQty= abstractOrderQty;
            } else {
                itemRequest.setQuantity(String.format("%013d", entry.getQuantityByCase() * abstractOrderQty));
            }
            if (Objects.nonNull(entry.getSelectedAutoShip())) {
                if (StringUtils.isNotBlank(entry.getSelectedAutoShip().getCode())) {
                    if (!entry.getSelectedAutoShip().getCode().toLowerCase().contains("one-time")) {
                        itemRequest.setFrequency(entry.getSelectedAutoShip().getCode());
                        itemRequest.setFrequencyType("1");
                        itemRequest.setStartDate(todayDate);
                    }

                }

            }
            itemsListData.add(itemRequest);

        }
        itemsList.setItem(itemsListData);
        requestBody.setItems(itemsList);

        if(totalMedpaxQty > 150) {
      	  requestBody.setDeliveryBlock("Z3");
        }
        else if ( Objects.nonNull(cartmodel.getDeliveryBlock()) &&  cartmodel.getDeliveryBlock()) {
            requestBody.setDeliveryBlock("Z6");
        }
        
        requestBody.setSimulate(""); 
        if(orderSimulate) {
      	  requestBody.setSimulate("X"); 
        }
        Set<PromotionResultModel> promotionResults= cartmodel.getAllPromotionResults();
        Set<PromotionResultModel> headerPromotionResults =promotionResults.stream().filter(p  -> CollectionUtils.isEmpty( p.getConsumedEntries())).collect(Collectors.toSet());
        if(CollectionUtils.isNotEmpty(headerPromotionResults)) {
           HeaderDiscountItemsListData headerDiscount = new HeaderDiscountItemsListData();
           List<HeaderDiscountItemsData>  headerDiscountList= new ArrayList<>(); 
           setHeaderDiscount(headerDiscountList, headerPromotionResults);
           headerDiscount.setItem(headerDiscountList);
           requestBody.setHeaderDiscount(headerDiscount);
        }
        Set<PromotionResultModel> itemPromotionResults =promotionResults.stream().filter(p  -> CollectionUtils.isNotEmpty( p.getConsumedEntries())).collect(Collectors.toSet());
        if(CollectionUtils.isNotEmpty(itemPromotionResults)) {
        ItemsDiscountItemsListData itemsDiscount = new ItemsDiscountItemsListData();
        List<ItemsDiscountItemsData> itemDiscountList = new ArrayList<>();
        setItemDiscount(itemPromotionResults, itemDiscountList);
        itemsDiscount.setItem(itemDiscountList);
        requestBody.setItemsDiscount(itemsDiscount);
        }
        return requestBody;
    }

	private void setItemDiscount(Set<PromotionResultModel> itemPromotionResults, List<ItemsDiscountItemsData> itemDiscountList)
			throws JsonProcessingException, JsonMappingException
	{
		final ObjectMapper objectMapper = new ObjectMapper();
        for(PromotionResultModel itemPromo:itemPromotionResults) {
      	  for(PromotionOrderEntryConsumedModel entry:itemPromo.getConsumedEntries()) {
      		  ItemsDiscountItemsData itemData = new ItemsDiscountItemsData();
      			RuleBasedPromotionModel rulePromo = (RuleBasedPromotionModel) itemPromo.getPromotion();
      			itemData.setItemNumber(String.valueOf(entry.getOrderEntryNumber()));
      			itemData.setPromoCode(rulePromo.getCode());   
      			PromotionSourceRuleModel sourceRule=(PromotionSourceRuleModel) rulePromo.getRule().getSourceRule();
      			JsonNode data = objectMapper.readTree(sourceRule.getActions()) ;
      			data.forEach(d -> {
      				if(sourceRule.getDiscountCash()) {
      					itemData.setDiscountCash(d.get("parameters").get("value").get("value").get(commonI18NService.getCurrentCurrency().getIsocode()).toString());
               		}
      				else {
               			itemData.setDiscount(d.get("parameters").get("value").get("value").toString());
                    }
           			;});
      			itemData.setStackable("");
           		if(Objects.nonNull(sourceRule.getAllowedStackable()) && sourceRule.getAllowedStackable()) {
           			itemData.setStackable("X");
           		}
           		if(sourceRule.getNonDiscountable()) {
           			itemData.setNonDiscount("X");
           		}
      			itemDiscountList.add(itemData);
      	  }
        }
	}


	private void setHeaderDiscount(List<HeaderDiscountItemsData> headerDiscountList,
			Set<PromotionResultModel> headerPromotionResults) throws JsonProcessingException, JsonMappingException
	{
		final ObjectMapper objectMapper = new ObjectMapper();
        for(PromotionResultModel headerPromo:headerPromotionResults) {
        	HeaderDiscountItemsData headerPromotion =new  HeaderDiscountItemsData();
        	if(headerPromo.getPromotion() instanceof RuleBasedPromotionModel) {
        		RuleBasedPromotionModel rulePromo = (RuleBasedPromotionModel) headerPromo.getPromotion();
        		headerPromotion.setPromoCode(rulePromo.getCode());
        		PromotionSourceRuleModel sourceRule=(PromotionSourceRuleModel) rulePromo.getRule().getSourceRule();
        		JsonNode data = objectMapper.readTree(sourceRule.getActions()) ;
        		data.forEach(d -> {
        			if(sourceRule.getDiscountCash()) {
             		 headerPromotion.setDiscountCash(d.get("parameters").get("value").get("value").get(commonI18NService.getCurrentCurrency().getIsocode()).toString());
             		}
        			else {
             			headerPromotion.setDiscount(d.get("parameters").get("value").get("value").toString());
                  }
        			;});
        		headerPromotion.setStackable("");
        		if(Objects.nonNull(sourceRule.getAllowedStackable()) && sourceRule.getAllowedStackable()) {
        			headerPromotion.setStackable("X");
        		}
        		headerPromotion.setNonDiscount("");
        		if(sourceRule.getNonDiscountable()) {
        			headerPromotion.setNonDiscount("X");
        		}
        	
        		headerDiscountList.add(headerPromotion);
        	}
        }
	}

	private int getAbstractOrderQty(final AbstractOrderEntryModel entry)
	{
		int abstractOrderQty= 1;
		if(Objects.nonNull(entry.getAbstractOrderQty())) {
		  abstractOrderQty = entry.getAbstractOrderQty().intValue();
		}
		return abstractOrderQty;
	}

    public OrderSimulateResponseData orderSimulate(final CartModel cart, final BaseStoreModel baseStore) throws Exception {
        OrderSimulateResponseData responseData = new OrderSimulateResponseData();
        SAPConfigurationModel configModel = null;
        if (baseStore != null) {
            configModel = baseStore.getSAPConfiguration();
        } else {
            configModel = getSapConfiguration();
        }
        if (Objects.nonNull(configModel) && Objects.nonNull(cart)) {
            final B2BCustomerModel customer = (B2BCustomerModel) cart.getUser();
            final OrderSimulateRequestData requestBody = new OrderSimulateRequestData();
            createOrderSimulateRequest(requestBody, configModel, cart, customer);
            final ObjectMapper objectMapper = new ObjectMapper();
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(WholescriptsCoreUtils.getSCPIOauthAccessToken(getConsumedDestinationModel()));
            final RestTemplate restTemplate = new RestTemplate();
            final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
            final String orderSimulateDestName = Config.getParameter("order.simulate.destination.name");
            try {
                final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(orderSimulateDestName);
                final ResponseEntity<String> response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST,
                        requestEntity, String.class);
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
                objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
                responseData = objectMapper.readValue(response.getBody(), OrderSimulateResponseData.class);
                saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), customer, STATUS_INTEGRATION_SUCCESS, orderSimulateDestName);
                return responseData;
            } catch (Exception ex) {
                saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), customer, STATUS_INTEGRATION_FAIL, orderSimulateDestName);
                throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
            }

        }
        throw new RequestParameterException("session cart not found", RequestParameterException.MISSING);
    }

    private void createOrderSimulateRequest(final OrderSimulateRequestData requestBody, final SAPConfigurationModel configModel,
                                            final CartModel cart, final B2BCustomerModel customer) {
        requestBody.setInstructions("");
        requestBody.setSalesOrg(configModel.getSapcommon_salesOrganization());
        requestBody.setDistChannel(configModel.getSapcommon_distributionChannel());
        requestBody.setDivision(configModel.getSapcommon_division());
        requestBody.setNoTax("");
        requestBody.setOrderDate("");
        if (Objects.nonNull(cart.getPaymentType())) {
            requestBody.setPaymentTerms(cart.getPaymentType().getCode());
        }
        requestBody.setOrderType(configModel.getSapcommon_transactionType());
        requestBody.setPromoCode("");
        requestBody.setShipCharge(String.valueOf(cart.getDeliveryCost()));
        requestBody.setShipCost("0.00");
        if (Objects.nonNull(customer.getDefaultB2BUnit())) {
            requestBody.setShipTo(WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(customer.getDefaultB2BUnit().getUid()));
            requestBody.setSoldTo(WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(customer.getDefaultB2BUnit().getUid()));
        }
        if (Objects.nonNull(cart.getDeliveryAddress())) {
            if (Objects.nonNull(cart.getDeliveryAddress().getSapPublicKey())) {
                final String sapPulicKey = cart.getDeliveryAddress().getSapPublicKey();
                if (StringUtils.isNotEmpty(sapPulicKey) && sapPulicKey.contains("|")) {
                    final String[] addressNumber = sapPulicKey.split("\\|");
                    requestBody.setShipTo(String.format("%010d", Integer.valueOf(addressNumber[1])));
                }
            }
            if (Objects.nonNull(cart.getDeliveryAddress().getRegion())) {
                final String regionCode = cart.getDeliveryAddress().getRegion().getIsocode();
                requestBody.setCarrier(getCarrierCodeForOrder(cart, regionCode));
            }

        }
        if (Objects.nonNull(cart.getDeliveryMode())) {
      	  requestBody.setShipMethod(cart.getDeliveryMode().getCode());
      	  if(cart.getDeliveryMode() instanceof ZoneDeliveryModeModel) {
      		  ZoneDeliveryModeModel zoneDeliveryMode=  (ZoneDeliveryModeModel) cart.getDeliveryMode();
      		  requestBody.setShipMethod(zoneDeliveryMode.getSapShippingCond());
      	  }  
        }
        requestBody.setSimulateFull("X");
        final OrderSimulateItemListData itemListData = new OrderSimulateItemListData();
        final List<OrderSimulateItemData> itemDataList = new ArrayList<>();
        final List<AbstractOrderEntryModel> entries = cart.getEntries();
        entries.forEach(e -> {
            createItemRequest(e, itemDataList, cart);
        });
        itemListData.setItem(itemDataList);
        requestBody.setItems(itemListData);
    }

    private String getCarrierCodeForOrder(final CartModel cart, String regionCode) {
        if (regionCode.contains("-")) {
            regionCode = regionCode.split("-")[1];
        }
        if (Objects.nonNull(cart.getDeliveryMode()) && cart.getDeliveryMode() instanceof ZoneDeliveryModeModel) {
            final ZoneDeliveryModeModel deliveryMode = (ZoneDeliveryModeModel) cart.getDeliveryMode();
            for (final AbstractOrderEntryModel entry : cart.getEntries()) {
                final boolean isExcluded = wholescriptsCartValidationDao.validateIfProductInNYExclusion(entry.getProduct().getCode(), regionCode);

                if (isExcluded && !StringUtils.containsAnyIgnoreCase(cart.getDeliveryAddress().getLine1(), configurationService.getConfiguration().getString("wholescripts.address.po.box"))) {
                    return deliveryMode.getSapCarrierShippingOption();
                }
            }
            return deliveryMode.getSapCarrier();
        }

        return StringUtils.EMPTY;
    }

    private void createItemRequest(final AbstractOrderEntryModel entry, final List<OrderSimulateItemData> itemDataList,
                                   final CartModel cart) {
        final OrderSimulateItemData itemData = new OrderSimulateItemData();
        itemData.setItemNumber(String.format("%06d", entry.getEntryNumber()));
        itemData.setHglvItem("000000");
        final Integer entryGroupNumbers = entry.getEntryGroupNumbers().stream().findFirst().orElse(null);
        if (Objects.nonNull(entryGroupNumbers)) {
            itemData.setHglvItem(String.format("%06d", entryGroupNumbers));
        }
        itemData.setPoItemNo(cart.getPurchaseOrderNumber());
        itemData.setMaterial(entry.getProduct().getCode());
        itemData.setDescription("");
        final EntryGroup entryGroup = cart.getEntryGroups().stream()
                .filter(eg -> eg.getGroupNumber().equals(entry.getEntryNumber())).findFirst().orElse(null);
        if (Objects.nonNull(entryGroup)) {
            itemData.setDescription(entryGroup.getLabel());
        }
        final List<String> excludedProducts = Arrays.asList(Config.getParameter("medpax.product.exclude").split(","));
       // abstract order qty in case of medpax Qty updating in cart page
        int abstractOrderQty= 1;
        if(Objects.nonNull(entry.getAbstractOrderQty())) {
      	  abstractOrderQty = entry.getAbstractOrderQty().intValue();
        }
        boolean isPrivateLabel=wholescriptsProductsService.isPrivateLabel(entry.getProduct());
        if(isPrivateLabel)
        {
            itemData.setReqQty(String.format("%013d", entry.getQuantityByCase() * abstractOrderQty));
        }
        else if (!excludedProducts.contains(entry.getProduct().getCode()) && CollectionUtils.isNotEmpty(entry.getEntryGroupNumbers())) {
            itemData.setReqQty(String.format("%013d", entry.getQuantity() * 30 * abstractOrderQty));
        } else {
            itemData.setReqQty(String.format("%013d", entry.getQuantity() * abstractOrderQty));

        }
        if (Objects.nonNull(entry.getProduct().getUnit())) {
            itemData.setSalesUnit(entry.getProduct().getUnit().getCode());
        }
        if (Objects.nonNull(entry.getSelectedAutoShip())) {
            if (StringUtils.isNotBlank(entry.getSelectedAutoShip().getCode()) && !entry.getSelectedAutoShip().getCode().toLowerCase().contains("one-time")) {
                itemData.setFrequency(entry.getSelectedAutoShip().getCode());
                itemData.setFrequencyType("1");
            }

        }
        itemData.setPrice(String.valueOf(entry.getBasePrice()));
        itemDataList.add(itemData);
    }


    public WholescriptsADDCCInSapResponseData createCreditCardInSap(final WholescriptsAuthResponseData preAuthResponse, String addressNumber) throws Exception {
        final SAPConfigurationModel configModel = getSapConfiguration();
        if (Objects.nonNull(configModel)) {
            final B2BCustomerModel b2bCustomer = b2bCustomerService.getCurrentB2BCustomer();
            final WholescriptsAddCreditCardInSapRequestData requestBody = createRequestForCreditCardCreationInSAP(b2bCustomer,
                    preAuthResponse,addressNumber);
            final ObjectMapper objectMapper = new ObjectMapper();
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(WholescriptsCoreUtils.getSCPIOauthAccessToken(getConsumedDestinationModel()));
            final RestTemplate restTemplate = new RestTemplate();
            final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
            final String creditCardDestName = Config.getParameter("wholescripts.create.creditcard.destination.name");
            ResponseEntity<String> response = null;
            try {
                final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(creditCardDestName);
                response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST, requestEntity, String.class);
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
                objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
                WholescriptsADDCCInSapResponseData responseData = objectMapper.readValue(response.getBody(),
                        WholescriptsADDCCInSapResponseData.class);
                saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, creditCardDestName);
                if ("0".equals(responseData.getReturnCode())) {
                    responseData = objectMapper.readValue(response.getBody(), WholescriptsADDCCInSapResponseData.class);
                    return responseData;
                }

            } catch (Exception ex) {
                saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, creditCardDestName);
                throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
            }

            final ReturnErrorMessageData errorMessage = objectMapper.readValue(response.getBody(), ReturnErrorMessageData.class);
    throw new RequestParameterException(String.format(errorMessage.getReturnMessage().getItems().stream().filter(i -> i.getType().equals("E")).map(i -> i.getMessage()).collect(Collectors.toList()).toString()),
              RequestParameterException.UNKNOWN_IDENTIFIER);
        }
        throw new RequestParameterException(String.format("Sap configuartion model is not found"),
                RequestParameterException.MISSING);
    }

    private WholescriptsAddCreditCardInSapRequestData createRequestForCreditCardCreationInSAP(final B2BCustomerModel b2bCustomer,
                                                                                              final WholescriptsAuthResponseData paymentDetails, String addressNumber) throws Exception {
        final WholescriptsAddCreditCardInSapRequestData requestData = new WholescriptsAddCreditCardInSapRequestData();
        requestData
                .setAccountNumber(WholescriptsCoreUtils.getDefaultB2bUnit(addressNumber));

        requestData.setValidTo(paymentDetails.getCardExpirationDate());
        final WholescriptsGetCCResponseData getCCResponseData = getCreditCardInfoFromSap(b2bCustomer);
        requestData.setCardId("000001");
        if (Objects.nonNull(getCCResponseData) && Objects.nonNull(getCCResponseData.getCartData()) && Objects.nonNull(getCCResponseData.getCartData().getItem())) {
            final List<WholescriptsGetCCCartItemResponseData> getCardsList = getCCResponseData.getCartData().getItem();

            if (CollectionUtils.isNotEmpty(getCardsList)) {
                final List<String> cardList = new ArrayList<>();
                for (final WholescriptsGetCCCartItemResponseData card : getCardsList) {
                    cardList.add(card.getCardId());
                }
                if (CollectionUtils.isNotEmpty(cardList)) {
                    String cardId = cardList.stream()
                            .max((c1, c2) -> Integer.compare(Integer.parseInt(c1), Integer.parseInt(c2)))
                            .orElse(null);
                    if(StringUtils.isNotEmpty(cardId))
                    {
                        requestData.setCardId(String.format("%06d", Integer.parseInt(cardId) + 1));
                    }
                }

            }

        }

        final WholescriptsCreditCardRequestData creditCardRequestData = new WholescriptsCreditCardRequestData();
        creditCardRequestData.setCardType(paymentDetails.getCardType());
        creditCardRequestData.setCardNumber(paymentDetails.getCardNumber());
        creditCardRequestData.setCreditCardName(paymentDetails.getCardHolderName());
        creditCardRequestData.setCartDefault("");
        requestData.setCreditCartData(creditCardRequestData);
        return requestData;
    }


    @Override
    public WholescriptsGetCCResponseData getCreditCardInfoFromSap(final B2BCustomerModel b2bCustomer) throws Exception {
        final WholescriptsGetCCRequestData requestBody = new WholescriptsGetCCRequestData();
        if (Objects.nonNull(b2bCustomer.getDefaultB2BUnit())) {
            requestBody
                    .setAccountNumber(WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(b2bCustomer.getDefaultB2BUnit().getUid()));
        }

        final ObjectMapper objectMapper = new ObjectMapper();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(WholescriptsCoreUtils.getSCPIOauthAccessToken(getConsumedDestinationModel()));
        final RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
        final String creditCardDestName = Config.getParameter("wholescripts.get.creditcard.destination.name");
        ResponseEntity<String> response = null;
        try {

            final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(creditCardDestName);
            response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST, requestEntity, String.class);
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
            objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
            WholescriptsGetCCResponseData responseData = objectMapper.readValue(response.getBody(),
                    WholescriptsGetCCResponseData.class);
            saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), userService.getCurrentUser(), STATUS_INTEGRATION_SUCCESS, creditCardDestName);
            if ("0".equals(responseData.getReturnCode())) {
                responseData = objectMapper.readValue(response.getBody(), WholescriptsGetCCResponseData.class);
                return responseData;
            }

        } catch (Exception ex) {
            saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, creditCardDestName);
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
        }

        final ReturnErrorMessageData errorMessage = objectMapper.readValue(response.getBody(), ReturnErrorMessageData.class);
        throw new RequestParameterException(String.format(errorMessage.getReturnMessage().getItems().stream().filter(i -> i.getType().equals("E")).map(i -> i.getMessage()).collect(Collectors.toList()).toString()),
              RequestParameterException.UNKNOWN_IDENTIFIER);

    }

    @Override
    public WholescriptsGetAddressFromSapResponseData getAddressFromSap(final B2BCustomerModel b2bCustomer) throws Exception {
        final SAPConfigurationModel configModel = getSapConfiguration();
        if (Objects.nonNull(configModel)) {
            final WholescriptsGetAddressFromSapRequestData requestBody = createRequestForGetAddressFromSap(b2bCustomer, configModel);
            final ObjectMapper objectMapper = new ObjectMapper();
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(WholescriptsCoreUtils.getSCPIOauthAccessToken(getConsumedDestinationModel()));
            final RestTemplate restTemplate = new RestTemplate();
            final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
            final String addressDestName = Config.getParameter("wholescripts.get.address.destination.name");
            ResponseEntity<String> response = null;
            try {
                final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(addressDestName);
                response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST, requestEntity, String.class);
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
                objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
                objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
                objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);

                WholescriptsGetAddressFromSapResponseData responseData = objectMapper.readValue(response.getBody(),
                        WholescriptsGetAddressFromSapResponseData.class);
                saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, addressDestName);
                if ("0".equals(responseData.getReturnCode())) {
                    responseData = objectMapper.readValue(response.getBody(), WholescriptsGetAddressFromSapResponseData.class);
                    return responseData;
                }

            } catch (Exception ex) {
                saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, addressDestName);
                throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
            }

            final ReturnErrorMessageData errorMessage = objectMapper.readValue(response.getBody(), ReturnErrorMessageData.class);
              throw new RequestParameterException(String.format(errorMessage.getReturnMessage().getItems().stream().filter(i -> i.getType().equals("E")).map(i -> i.getMessage()).collect(Collectors.toList()).toString()),
              RequestParameterException.UNKNOWN_IDENTIFIER);
        }
        throw new RequestParameterException(String.format("Sap configuartion model is not found"),
                RequestParameterException.MISSING);
    }

    private WholescriptsGetAddressFromSapRequestData createRequestForGetAddressFromSap(final B2BCustomerModel b2bCustomer,
                                                                                       final SAPConfigurationModel configModel) {
        final WholescriptsGetAddressFromSapRequestData requestBody = new WholescriptsGetAddressFromSapRequestData();
        if (Objects.nonNull(b2bCustomer.getDefaultB2BUnit())) {
            requestBody
                    .setAccountNumber(WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(b2bCustomer.getDefaultB2BUnit().getUid()));
        }
        requestBody.setSalesOrg(configModel.getSapcommon_salesOrganization());
        requestBody.setDisChannel(configModel.getSapcommon_distributionChannel());
        requestBody.setDivision(configModel.getSapcommon_division());
        return requestBody;
    }

    @Override
    public UPSShipingResponseData getUPSShipingCharges() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        final HttpHeaders headers = new HttpHeaders();
        final ConsumedDestinationModel consumed = destinationSearchService.findDestination(UPS_BASIC_DESTINATION);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(WholescriptsCoreUtils.getUPSAccessToken(consumed));
        final RestTemplate restTemplate = new RestTemplate();
        final UPSShipingRequestData requestBody = createRequestForUPSShipingCost();
        final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
        final String shipingCostDestName = Config.getParameter("ups.shiping.cost.destination.name");
        final UserModel b2bCustomer = userService.getCurrentUser();
        try {
            final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(shipingCostDestName);
            final ResponseEntity<String> response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST,
                    requestEntity, String.class);
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            final UPSShipingResponseData responseData = objectMapper.readValue(response.getBody(), UPSShipingResponseData.class);
            saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), userService.getCurrentUser(), STATUS_INTEGRATION_SUCCESS, shipingCostDestName);
            return responseData;
        } catch (Exception ex) {
            saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, shipingCostDestName);
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
        }

    }

    private UPSShipingRequestData createRequestForUPSShipingCost() {
        final PointOfServiceModel pos = getPOSForAddress();
        final CartModel cart = cartService.getSessionCart();
        final UPSShipingRequestData requestBody = new UPSShipingRequestData();
        final UPSShipingRateRequestData rateRequestData = new UPSShipingRateRequestData();
        final ShipingRequestData shipingRequestData = new ShipingRequestData();
        final TransactionReferenceData transactionReferenceData = new TransactionReferenceData();
        transactionReferenceData.setCustomerContext("Bare Bones Rate Request");
        shipingRequestData.setTransactionReference(transactionReferenceData);
        shipingRequestData.setRequestOption("Shop");
        rateRequestData.setRequest(shipingRequestData);
        final PickupTypeData pickupTypeData = new PickupTypeData();
        pickupTypeData.setCode("01");
        pickupTypeData.setDescription("Daily Pickup");
        rateRequestData.setPickupType(pickupTypeData);
        final CustomerClassificationData customerClassificationData = new CustomerClassificationData();
        customerClassificationData.setCode("04");
        customerClassificationData.setDescription("Retail Rates");
        rateRequestData.setCustomerClassification(customerClassificationData);
        final ShipmentData shipmentData = new ShipmentData();
        final ShipperData shipperData = new ShipperData();
        final AddressRequestData shipperAddressRequestData = new AddressRequestData();
        if (Objects.nonNull(pos)) {
            final AddressModel address = pos.getAddress();
            if (Objects.nonNull(address)) {
                shipperAddressRequestData.setCity(address.getTown());
                shipperAddressRequestData.setStateProvinceCode(address.getRegion().getIsocodeShort());
                shipperAddressRequestData.setPostalCode(address.getPostalcode());
                shipperAddressRequestData.setCountryCode(address.getCountry().getIsocode());
            }
        }
        shipperData.setAddress(shipperAddressRequestData);
        shipmentData.setShipper(shipperData);
        final ShipToData shipToData = new ShipToData();
        final AddressRequestData shipToAddressRequestData = new AddressRequestData();
        if (Objects.nonNull(cart)) {
            final AddressModel address = cart.getDeliveryAddress();
            if (Objects.nonNull(address)) {
                shipToAddressRequestData.setCity(address.getTown());
                shipToAddressRequestData.setStateProvinceCode(address.getRegion().getIsocodeShort());
                shipToAddressRequestData.setPostalCode(address.getPostalcode());
                shipToAddressRequestData.setCountryCode(address.getCountry().getIsocode());
                shipToAddressRequestData.setResidentialAddressIndicator("True");
            }
        }
        shipToData.setAddress(shipToAddressRequestData);
        shipmentData.setShipTo(shipToData);
        final ShipFromData shipFromData = new ShipFromData();
        final AddressRequestData shipFromAddressRequestData = new AddressRequestData();
        if (Objects.nonNull(pos)) {
            final AddressModel address = pos.getAddress();
            if (Objects.nonNull(address)) {
                shipFromAddressRequestData.setCity(address.getTown());
                shipFromAddressRequestData.setStateProvinceCode(address.getRegion().getIsocodeShort());
                shipFromAddressRequestData.setPostalCode(address.getPostalcode());
                shipFromAddressRequestData.setCountryCode(address.getCountry().getIsocode());
            }
        }
        shipFromData.setAddress(shipFromAddressRequestData);
        shipmentData.setShipFrom(shipFromData);
        final UPSServiceData upsServiceData = new UPSServiceData();
        upsServiceData.setCode("03");
        shipmentData.setService(upsServiceData);
        final List<UPSPackageData> packages = new ArrayList<>();
        final double totalWeight = getTotalWeight(cart);
        final double totalWeightInLB = totalWeight / 16;
        final int roundtotalWeightInLB = (int) Math.ceil(totalWeightInLB);
        if (roundtotalWeightInLB > 60) {
            final List<Integer> totalPackages = splitIntoEqualPackages(roundtotalWeightInLB, 60);
            for (final int weight : totalPackages) {
                createRequestForPackets(rateRequestData, packages, weight);
            }
        } else {
            createRequestForPackets(rateRequestData, packages, roundtotalWeightInLB);
        }
        shipmentData.setShipPackage(packages);
        rateRequestData.setShipment(shipmentData);
        requestBody.setRateRequest(rateRequestData);
        return requestBody;
    }

    private double getTotalWeight(final CartModel cart) {
        double totalWeight = 0.0d;
        final List<String> excludedProducts = Arrays.asList(Config.getParameter("medpax.product.exclude").split(","));
        final List<AbstractOrderEntryModel> originalEntries = new ArrayList<>(cart.getEntries());
        if (CollectionUtils.isNotEmpty(cart.getEntryGroups())) {
            final List<AbstractOrderEntryModel> filteredEntries = new ArrayList<>();
            final List<EntryGroup> parentEntryGroups = cart.getEntryGroups().stream()
                    .filter(eg -> StringUtils.isNotEmpty(eg.getExternalReferenceId())).collect(Collectors.toList());
            for (final EntryGroup eg : parentEntryGroups) {
                final List<AbstractOrderEntryModel> medpaxEntries = cart.getEntries().stream()
                        .filter(e -> StringUtils.isNotEmpty(e.getTemplateId())
                                && e.getTemplateId().split("-")[1].equals(eg.getExternalReferenceId()))
                        .collect(Collectors.toList());
                for (final AbstractOrderEntryModel entry : medpaxEntries) {
                    if (excludedProducts.contains(entry.getProduct().getCode())) {
                        totalWeight += entry.getQuantity() * Double
                                .valueOf(StringUtils.isNotEmpty(entry.getProduct().getWeight()) ? entry.getProduct().getWeight() : "0");
                    } else {
                        totalWeight += entry.getQuantity() * 30 * Double.valueOf(entry.getProduct().getWeight());
                    }
                    filteredEntries.add(entry);
                }
            }
            originalEntries.removeAll(filteredEntries);
        }
        totalWeight += getTotalWeightForBluelabelEntries(originalEntries, totalWeight);
        return totalWeight;
    }

    private double getTotalProductVolume(final CartModel cart) {
        double totalProductVolume = 0.0d;
        final List<String> excludedProducts = Arrays.asList(Config.getParameter("medpax.product.exclude").split(","));
        final List<AbstractOrderEntryModel> originalEntries = new ArrayList<>(cart.getEntries());
        if (CollectionUtils.isNotEmpty(cart.getEntryGroups())) {
            final List<AbstractOrderEntryModel> filteredEntries = new ArrayList<>();
            final List<EntryGroup> parentEntryGroups = cart.getEntryGroups().stream()
                    .filter(eg -> StringUtils.isNotEmpty(eg.getExternalReferenceId())).collect(Collectors.toList());
            for (final EntryGroup eg : parentEntryGroups) {
                final List<AbstractOrderEntryModel> medpaxEntries = cart.getEntries().stream()
                        .filter(e -> StringUtils.isNotEmpty(e.getTemplateId())
                                && e.getTemplateId().split("-")[1].equals(eg.getExternalReferenceId()))
                        .collect(Collectors.toList());
                for (final AbstractOrderEntryModel entry : medpaxEntries) {
                    filteredEntries.add(entry);
                }
            }
            originalEntries.removeAll(filteredEntries);
        }
        totalProductVolume += getTotalVolumeForBluelabelEntries(originalEntries, totalProductVolume);
        return totalProductVolume;
    }

    private PointOfServiceModel getPOSForAddress() {
        final BaseStoreModel baseStore = baseStoreService.getCurrentBaseStore();
        final WarehouseModel warehouse = baseStore.getWarehouses().stream().findFirst().orElse(null);
        PointOfServiceModel pos = null;
        if (Objects.nonNull(warehouse)) {
            pos = warehouse.getPointsOfService().stream().filter(p -> p.getType().equals(PointOfServiceTypeEnum.WAREHOUSE))
                    .findFirst().orElse(null);
        }
        return pos;
    }

    private double getTotalWeightForBluelabelEntries(final List<AbstractOrderEntryModel> entries, double totalWeight) {
        for (final AbstractOrderEntryModel entry : entries) {
            totalWeight += entry.getQuantity()
                    * Double.valueOf(StringUtils.isNotEmpty(entry.getProduct().getWeight()) ? entry.getProduct().getWeight() : "0");
        }
        return totalWeight;
    }

    private double getTotalVolumeForBluelabelEntries(final List<AbstractOrderEntryModel> entries, double totalProductVolume) {
        for (final AbstractOrderEntryModel entry : entries) {
            totalProductVolume += entry.getQuantity() * Double.valueOf(StringUtils.isNotEmpty(entry.getProduct().getWidth()) ? entry.getProduct().getWidth() : "0") * Double.valueOf(StringUtils.isNotEmpty(entry.getProduct().getHeight()) ? entry.getProduct().getHeight() : "0") * Double.valueOf(StringUtils.isNotEmpty(entry.getProduct().getLength()) ? entry.getProduct().getLength() : "0");
        }
        return totalProductVolume;
    }

    private void createRequestForPackets(final UPSShipingRateRequestData rateRequestData, final List<UPSPackageData> packages,
                                         final int roundtotalWeightInLB) {
        final UPSPackageData upsPackageData = new UPSPackageData();
        final PackagingTypeData packagingTypeData = new PackagingTypeData();
        packagingTypeData.setCode("02");
        packagingTypeData.setDescription("Packaging");
        upsPackageData.setPackagingType(packagingTypeData);
        final DimensionsData dimensionsData = new DimensionsData();
        final UnitOfMeasurementData unitOfMeasurementData = new UnitOfMeasurementData();
        unitOfMeasurementData.setCode("IN");
        unitOfMeasurementData.setDescription("Inches");
        dimensionsData.setUnitOfMeasurement(unitOfMeasurementData);
        dimensionsData.setLength("0");
        dimensionsData.setWidth("0");
        dimensionsData.setHeight("0");
        upsPackageData.setDimensions(dimensionsData);
        final PackageWeightData packageWeightData = new PackageWeightData();
        final UnitOfMeasurementData weightUnitOfMeasurementData = new UnitOfMeasurementData();
        weightUnitOfMeasurementData.setCode("LBS");
        weightUnitOfMeasurementData.setDescription("Pounds");
        packageWeightData.setUnitOfMeasurement(weightUnitOfMeasurementData);
        packageWeightData.setWeight(String.valueOf(roundtotalWeightInLB));
        upsPackageData.setPackageWeight(packageWeightData);
        packages.add(upsPackageData);
    }

    private List<Integer> splitIntoEqualPackages(final int totalWeight, final int maxPackageWeight) {
        final List<Integer> packages = new ArrayList<>();
        // Determine the number of packages
        final int numberOfPackages = (int) Math.ceil((double) totalWeight / maxPackageWeight);
        // Calculate the weight for each package
        final int weightPerPackage = totalWeight / numberOfPackages;
        // Handle remainder to make weights equal
        int remainingWeight = totalWeight % numberOfPackages;
        for (int i = 0; i < numberOfPackages; i++) {
            if (remainingWeight > 0) {
                packages.add(weightPerPackage + 1); // Distribute remaining weight
                remainingWeight--;
            } else {
                packages.add(weightPerPackage);
            }
        }
        return packages;
    }

    @Override
    public SharedRewardsResponseData getSharedRewards(final B2BCustomerModel b2bCustomer, final String date) throws Exception {
        SharedRewardsResponseData responseData = new SharedRewardsResponseData();
        if (Objects.nonNull(b2bCustomer.getDefaultB2BUnit())) {
            final String sharedRewardsDestName = Config.getParameter("shared.rewards.destination.name");
            final ObjectMapper objectMapper = new ObjectMapper();
            HttpEntity<String> requestEntity = null;
            try {
                final SharedRewardsRequestData requestBody = new SharedRewardsRequestData();
                final SAPConfigurationModel configModel = getSapConfiguration();
                requestBody.setAccountNumber(WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(b2bCustomer.getDefaultB2BUnit().getUid()));
                if (Objects.nonNull(configModel)) {
                    requestBody.setSalesOrg(configModel.getSapcommon_salesOrganization());
                    requestBody.setDistChannel(configModel.getSapcommon_distributionChannel());
                    requestBody.setDivision(configModel.getSapcommon_division());
                    requestBody.setDate(date);
                }

                final HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setBearerAuth(WholescriptsCoreUtils.getSCPIOauthAccessToken(getConsumedDestinationModel()));
                final RestTemplate restTemplate = new RestTemplate();
                requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
                final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(sharedRewardsDestName);
                final ResponseEntity<String> response = restTemplate.exchange(httpDestination.getTargetURL(),
                        HttpMethod.POST, requestEntity, String.class);
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
                objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
                objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
                objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
                final ReturnResponseData returnData = objectMapper.readValue(response.getBody(), ReturnResponseData.class);
                if ("1".equals(returnData.getRETURN())) {
                    final ReturnErrorMessageData errorMessage = objectMapper.readValue(response.getBody(),
                            ReturnErrorMessageData.class);
                      throw new RequestParameterException(String.format(errorMessage.getReturnMessage().getItems().stream().filter(i -> i.getType().equals("E")).map(i -> i.getMessage()).collect(Collectors.toList()).toString()),
              RequestParameterException.UNKNOWN_IDENTIFIER);
                }
                responseData = objectMapper.readValue(response.getBody(), SharedRewardsResponseData.class);
                saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, sharedRewardsDestName);
            } catch (Exception ex) {
                saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, sharedRewardsDestName);
                throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
            }
        }
        return responseData;
    }

    @Override
    public AgingResponseData getAging(final B2BCustomerModel b2bCustomer, final String keyDate) throws Exception {
        AgingResponseData responseData = new AgingResponseData();
        if (Objects.nonNull(b2bCustomer.getDefaultB2BUnit())) {
            final String agingDestName = Config.getParameter("aging.destination.name");
            final ObjectMapper objectMapper = new ObjectMapper();
            HttpEntity<String> requestEntity = null;
            try {
                final AgingRequestData requestBody = new AgingRequestData();
                final SAPConfigurationModel configModel = getSapConfiguration();
                requestBody.setAccount(WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(b2bCustomer.getDefaultB2BUnit().getUid()));
                if (Objects.nonNull(configModel)) {
                    requestBody.setCompany(configModel.getSapcommon_salesOrganization());
                }
                requestBody.setKeydate(keyDate);
                final HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setBearerAuth(WholescriptsCoreUtils.getSCPIOauthAccessToken(getConsumedDestinationModel()));
                final RestTemplate restTemplate = new RestTemplate();
                requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
                final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(agingDestName);
                final ResponseEntity<String> response = restTemplate.exchange(httpDestination.getTargetURL(),
                        HttpMethod.POST, requestEntity, String.class);
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
                objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
                objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
                objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
                final ReturnResponseData returnData = objectMapper.readValue(response.getBody(), ReturnResponseData.class);
                if ("1".equals(returnData.getRETURN())) {
                    final ReturnErrorMessageData errorMessage = objectMapper.readValue(response.getBody(),
                            ReturnErrorMessageData.class);
                      throw new RequestParameterException(String.format(errorMessage.getReturnMessage().getItems().stream().filter(i -> i.getType().equals("E")).map(i -> i.getMessage()).collect(Collectors.toList()).toString()),
              RequestParameterException.UNKNOWN_IDENTIFIER);
                }
                responseData = objectMapper.readValue(response.getBody(), AgingResponseData.class);
                saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, agingDestName);
            } catch (Exception ex) {
                saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, agingDestName);
                throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
            }
        }
        return responseData;
    }

    @Override
    public USPSShippingResponseData getUSPSShipingCharges() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_XML));
        final RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        final String shippingCostDestName = Config.getParameter("usps.shipping.cost.destination.name");
        final UserModel b2bCustomer = userService.getCurrentUser();
        final String requestData = createUSPSRequest();
        try {
            final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(shippingCostDestName);
            final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(httpDestination.getTargetURL());
            builder.queryParam("API", URLEncoder.encode("RateV4", ENCODE_UTF8));
            builder.queryParam("XML", URLEncoder.encode(requestData));
            final ResponseEntity<String> response = restTemplate.exchange(builder.build(true).encode().toUri(), HttpMethod.POST,
                    requestEntity, String.class);
            final JSONObject json = XML.toJSONObject(response.getBody());
            final String convertINJson = json.toString(4);
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            final USPSShippingResponseData responseData = objectMapper.readValue(convertINJson, USPSShippingResponseData.class);
            saveIntegrationModel(requestData, objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, shippingCostDestName);
            return responseData;
        } catch (Exception ex) {
            saveIntegrationModel(requestData, objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, shippingCostDestName);
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
        }

    }

    private String createUSPSRequest() {
        final PointOfServiceModel pos = getPOSForAddress();
        final CartModel cart = cartService.getSessionCart();
        final double totalWeight = getTotalWeight(cart);
        final double totalWeightInLB = totalWeight / 16;
        final int roundtotalWeightInLB = (int) totalWeightInLB;
        final double totalWeightInOunces = totalWeight % 16;
        final int roundtotalWeightInOunces = (int) totalWeightInOunces;

        final List<String> widthList = cart.getEntries().stream().filter(e -> StringUtils.isNotEmpty(e.getProduct().getWidth()))
                .map(e -> e.getProduct().getWidth()).collect(Collectors.toList());
        final List<Double> widthListDouble = widthList.stream().map(Double::valueOf).collect(Collectors.toList());
        double maxWidth = widthListDouble.stream().filter(w -> 0.0 != w).max(Double::compareTo).orElse(1d);


        final List<String> lengthList = cart.getEntries().stream().filter(e -> StringUtils.isNotEmpty(e.getProduct().getLength()))
                .map(e -> e.getProduct().getLength()).collect(Collectors.toList());
        final List<Double> lengthListDouble = lengthList.stream().map(Double::valueOf).collect(Collectors.toList());
        double maxLength = lengthListDouble.stream().filter(l -> 0.0 != l).max(Double::compareTo).orElse(1d);

        final List<String> heightList = cart.getEntries().stream().filter(e -> StringUtils.isNotEmpty(e.getProduct().getHeight()))
                .map(e -> e.getProduct().getHeight()).collect(Collectors.toList());
        final List<Double> heightListDouble = heightList.stream().map(Double::valueOf).collect(Collectors.toList());
        double maxHeight = heightListDouble.stream().filter(h -> 0.0 != h).max(Double::compareTo).orElse(1d);

        final double totalVolume = getTotalProductVolume(cart);
        final DecimalFormat fomatter = new DecimalFormat("0.00");
        double cubeRootOfTotalVolume = Math.cbrt(totalVolume);
        cubeRootOfTotalVolume = Double.valueOf(fomatter.format(cubeRootOfTotalVolume));
        if (cubeRootOfTotalVolume > maxWidth) {
            maxWidth = cubeRootOfTotalVolume;
        }

        if (cubeRootOfTotalVolume > maxLength) {
            maxLength = cubeRootOfTotalVolume;
        }

        if (cubeRootOfTotalVolume > maxHeight) {
            maxHeight = cubeRootOfTotalVolume;
        }

        String size = "Regular";

        if (maxLength > 12 || maxWidth > 12 || maxLength > maxWidth) {
            size = "Large";
        }

        double girth = 2 * (maxHeight + maxWidth);
        girth = Double.valueOf(fomatter.format(girth));
        StringBuilder xml = new StringBuilder();
        xml.append("<RateV4Request USERID='591XYMOG4891' PASSWORD=''>");
        xml.append("<Revision>2</Revision>");
        if (roundtotalWeightInLB > 70) {
            final List<Integer> totalPackages = splitIntoEqualPackages(roundtotalWeightInLB, 70);
            for (int i = 0; totalPackages.size() > i; i++) {
                xml.append("<Package ID='" + i + "'>");
                xml = createUSPSXMLRequest(pos, cart, totalPackages.get(i), roundtotalWeightInOunces, maxWidth,
                        maxLength, maxHeight, size, girth, xml);
            }
        } else {
            xml.append("<Package ID='0'>");
            xml = createUSPSXMLRequest(pos, cart, roundtotalWeightInLB, roundtotalWeightInOunces, maxWidth,
                    maxLength, maxHeight, size, girth, xml);
        }
        xml.append("</RateV4Request>");
        return xml.toString();
    }

    private StringBuilder createUSPSXMLRequest(final PointOfServiceModel pos, final CartModel cart, final int roundtotalWeightInLB,
                                               final int roundtotalWeightInOunces, final double maxWidth, final double maxLength, final double maxHeight, final String size, final double girth, final StringBuilder xml) {
        xml.append("<Service>ALL</Service>");
        xml.append("<ZipOrigination>");
        if (Objects.nonNull(pos.getAddress())) {
            xml.append(pos.getAddress().getPostalcode());
        }
        xml.append("</ZipOrigination>");
        xml.append("<ZipDestination>");
        if (Objects.nonNull(cart.getDeliveryAddress()) && StringUtils.isNotEmpty(cart.getDeliveryAddress().getPostalcode())) {
            if (cart.getDeliveryAddress().getPostalcode().contains("-")) {
                final String[] zipDest = cart.getDeliveryAddress().getPostalcode().split("-");
                xml.append(zipDest[0]);
            } else {
                xml.append(cart.getDeliveryAddress().getPostalcode());
            }
        }
        xml.append("</ZipDestination>");
        xml.append("<Pounds>");
        xml.append(roundtotalWeightInLB);
        xml.append("</Pounds>");
        xml.append("<Ounces>");
        xml.append(roundtotalWeightInOunces);
        xml.append("</Ounces>");
        xml.append("<Container/>");
        xml.append("<Size>");
        xml.append(size);
        xml.append("</Size>");
        xml.append("<Width>");
        xml.append(maxWidth);
        xml.append("</Width>");
        xml.append("<Length>");
        xml.append(maxLength);
        xml.append("</Length>");
        xml.append("<Height>");
        xml.append(maxHeight);
        xml.append("</Height>");
        xml.append("<Girth>");
        xml.append(girth);
        xml.append("</Girth>");
        xml.append("<Machinable>");
        xml.append("FALSE");
        xml.append("</Machinable>");
        xml.append("</Package>");
        return xml;
    }


    @Override
    public WholescriptsAuthResponseData preAuthorizationForTheCart(final Map<String, String> parameters, final SopPaymentDetailsWsDTO sopPaymentDetails) throws Exception {
        final CartModel cart = cartService.getSessionCart();
        final WholescriptsPreAuthRequestData requestBody = createRequestForCreditCardPreAuth(cart, parameters, sopPaymentDetails);
        return paymentricAuthorizationForTheCart(requestBody);
    }

    private WholescriptsPreAuthRequestData createRequestForCreditCardPreAuth(final CartModel cart, final Map<String, String> parameters, final SopPaymentDetailsWsDTO sopPaymentDetails) {

        final WholescriptsPreAuthRequestData requestBody = new WholescriptsPreAuthRequestData();
        final WholescriptsPreAuthCardAddressRequestData addressRequest = new WholescriptsPreAuthCardAddressRequestData();
        final WholescriptsPreAuthBillingHeaderRequestData billingHeaderRequestData = new WholescriptsPreAuthBillingHeaderRequestData();
        final WholescriptsPreAuthCreditCardRequestData creditCardRequestData = new WholescriptsPreAuthCreditCardRequestData();
        final B2BCustomerModel b2bCustomer = b2bCustomerService.getCurrentB2BCustomer();

        addressRequest.setAddressLine1(sopPaymentDetails.getBillTo_street1());
        addressRequest.setCity(sopPaymentDetails.getBillTo_city());
        addressRequest.setCountry(sopPaymentDetails.getBillTo_country());
        if(StringUtils.isNotBlank(sopPaymentDetails.getBillTo_email()))
        {
            addressRequest.setEmail(sopPaymentDetails.getBillTo_email());

        }
        if (Objects.nonNull(b2bCustomer.getDefaultB2BUnit())) {
            addressRequest.setId(WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(b2bCustomer.getDefaultB2BUnit().getUid()));
        }
        String firstName = sopPaymentDetails.getBillTo_firstName();
        String lastName = sopPaymentDetails.getBillTo_lastName();
        if (StringUtils.isNotBlank(firstName) || StringUtils.isNotBlank(lastName)) {
            StringBuilder nameBuilder = new StringBuilder();

            if (StringUtils.isNotBlank(firstName)) {
                nameBuilder.append(firstName);
            }

            if (StringUtils.isNotBlank(lastName)) {
                if (!nameBuilder.isEmpty()) {
                    nameBuilder.append(" ");
                }
                nameBuilder.append(lastName);
            }

            addressRequest.setName(nameBuilder.toString());
        }

        addressRequest.setPostalCode(sopPaymentDetails.getBillTo_postalCode());
        addressRequest.setRegion(sopPaymentDetails.getBillTo_region());
        if (StringUtils.isNotBlank(sopPaymentDetails.getBillTo_phoneNumber())) {
            addressRequest.setPhone(sopPaymentDetails.getBillTo_phoneNumber());
        }

        creditCardRequestData.setCardAddress(addressRequest);
        billingHeaderRequestData.setCurrencyKey("USD");
        billingHeaderRequestData.setTransactionMessageType("CSTO");
        if(sopPaymentDetails.isCheckoutPage())
        {
            billingHeaderRequestData.setTransactionMessageType("CGEN");
        }

        billingHeaderRequestData.setCreditCard(creditCardRequestData);
        billingHeaderRequestData.setNetworkTransactionId("");
        requestBody.setBillingHeader(billingHeaderRequestData);
        requestBody.setMessage("WholeScripts.com Pre-authorization");
        requestBody.setPaymetricAccertify(null);
        final BaseStoreModel baseStore = baseStoreService.getCurrentBaseStore();
        requestBody.setStoreId(baseStore.getPaymentricStoreId());
        requestBody.setIsOrderSubmit(false);
        if (Objects.nonNull(parameters)) {
            creditCardRequestData.setCardType(parameters.get("card_cardType"));
            creditCardRequestData.setCardDataSource("E");
            creditCardRequestData.setCardNumber(parameters.get("card_accountNumber"));
            creditCardRequestData.setCardExpirationDate(parameters.get("card_expirationMonth") + "/" + parameters.get("card_expirationYear"));
            creditCardRequestData.setCardHolderName(parameters.get("card_nameOnCard"));
            creditCardRequestData.setCardCVV2(parameters.get("cvv"));

        }

        if (Objects.nonNull(parameters)) {
            final String cardType = parameters.get("card_cardType");
            if ((cardType.equals("ax") || cardType.equals("di"))) {

                billingHeaderRequestData.setAmount("1.0");
            } else {
                billingHeaderRequestData.setAmount("0.0");
            }
        } else {
            billingHeaderRequestData.setAmount("0.0");

        }

        if (Objects.nonNull(b2bCustomer.getDefaultB2BUnit())) {
            requestBody.setSapCustomerId(WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(b2bCustomer.getDefaultB2BUnit().getUid()));
        }

        return requestBody;
    }


    private WholescriptsPreAuthRequestData createRequestForCreditCardAuth(final CartModel cart, final OrderCreationRequest orderCreationRequest) {
        final B2BCustomerModel b2bCustomer = (B2BCustomerModel) cart.getUser();
        final WholescriptsPreAuthRequestData requestBody = new WholescriptsPreAuthRequestData();
        final WholescriptsAuthPaymetricAccertifyRequestData accertifyData = new WholescriptsAuthPaymetricAccertifyRequestData();
        final WholescriptsAuthShippingHeaderRequestData shippingData = new WholescriptsAuthShippingHeaderRequestData();
        final WholescriptsAuthAccertifyCollectorJsRequestData paymetricAccertifyData = new WholescriptsAuthAccertifyCollectorJsRequestData();
        final WholescriptsPreAuthBillingHeaderRequestData billingHeaderRequestData = new WholescriptsPreAuthBillingHeaderRequestData();
        final WholescriptsPreAuthCreditCardRequestData creditCardRequestData = new WholescriptsPreAuthCreditCardRequestData();
        final WholescriptsPreAuthCardAddressRequestData billingAddressRequest = new WholescriptsPreAuthCardAddressRequestData();
        final WholescriptsPreAuthCardAddressRequestData shippingAddressRequest = new WholescriptsPreAuthCardAddressRequestData();


        billingHeaderRequestData.setAmount(String.valueOf(calculateAuthAmount(cart)));
        billingHeaderRequestData.setCurrencyKey(cart.getCurrency().getIsocode());
        if (Objects.nonNull(cart.getPaymentInfo()) && cart.getPaymentInfo() instanceof final CreditCardPaymentInfoModel paymentInfoModel) {
            creditCardRequestData.setCardDataSource("E");
            creditCardRequestData.setCardType(WholescriptsCoreUtils.getPaymetricCardTypeForAuth(paymentInfoModel.getType()));
            creditCardRequestData.setCardNumber(paymentInfoModel.getNumber());
            creditCardRequestData.setCardHolderName(paymentInfoModel.getCcOwner());
            creditCardRequestData.setCardExpirationDate(paymentInfoModel.getValidToMonth() + "/" + paymentInfoModel.getValidToYear());
            if(StringUtils.isNotEmpty(paymentInfoModel.getCvvNumber()))
            {
                creditCardRequestData.setCardCVV2(paymentInfoModel.getCvvNumber());
            }
        }
        final AddressModel billingAddress = cart.getPaymentAddress();
        if (Objects.nonNull(billingAddress)) {
            billingAddressRequest.setAddressLine1(billingAddress.getLine1());
            billingAddressRequest.setCity(billingAddress.getTown());
            if (Objects.nonNull(billingAddress.getCountry())) {
                billingAddressRequest.setCountry(billingAddress.getCountry().getIsocode());

            }
            if(StringUtils.isNotBlank(billingAddress.getEmail()))
            {
                billingAddressRequest.setEmail(billingAddress.getEmail());

            }
            if (Objects.nonNull(b2bCustomer.getDefaultB2BUnit())) {
                billingAddressRequest.setId(WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(b2bCustomer.getDefaultB2BUnit().getUid()));

            }
            String firstName = billingAddress.getFirstname();
            String lastName = billingAddress.getLastname();
            if (StringUtils.isNotBlank(firstName) || StringUtils.isNotBlank(lastName)) {
                StringBuilder nameBuilder = new StringBuilder();

                if (StringUtils.isNotBlank(firstName)) {
                    nameBuilder.append(firstName);
                }

                if (StringUtils.isNotBlank(lastName)) {
                    if (!nameBuilder.isEmpty()) {
                        nameBuilder.append(" ");
                    }
                    nameBuilder.append(lastName);
                }

                billingAddressRequest.setName(nameBuilder.toString());
            }
            billingAddressRequest.setPostalCode(billingAddress.getPostalcode());
            if (Objects.nonNull(billingAddress.getRegion())) {
                final String regionCode = billingAddress.getRegion().getIsocode();
                billingAddressRequest.setRegion(regionCode.split("-")[1]);
            }
            if(StringUtils.isNotBlank(billingAddress.getPhone1()))
            {
                billingAddressRequest.setPhone(billingAddress.getPhone1());

            }
        }
        creditCardRequestData.setCardAddress(billingAddressRequest);
        billingHeaderRequestData.setCreditCard(creditCardRequestData);
        if (Objects.nonNull(cart.getPaymentInfo()) && cart.getPaymentInfo() instanceof final CreditCardPaymentInfoModel paymentInfoModel) {
            billingHeaderRequestData.setTransactionMessageType("CUSE");
            if(StringUtils.isNotEmpty(paymentInfoModel.getCvvNumber()))
            {
                billingHeaderRequestData.setTransactionMessageType("CGEN");
            }
        }


        billingHeaderRequestData.setNetworkTransactionId("");
        requestBody.setBillingHeader(billingHeaderRequestData);
        if (orderCreationRequest != null) {
            paymetricAccertifyData.setPageID(orderCreationRequest.getPageId());
            paymetricAccertifyData.setDeviceTransactionID(orderCreationRequest.getDeviceTransactionId());
            paymetricAccertifyData.setSessionID(orderCreationRequest.getSessionId());
            paymetricAccertifyData.setUbaEvents(orderCreationRequest.getUbaEvents());
            paymetricAccertifyData.setUbaID(orderCreationRequest.getUbaId());
            accertifyData.setUserAgent(orderCreationRequest.getUserAgent());
            accertifyData.setIpAddress(orderCreationRequest.getIpAddress());
        }
        accertifyData.setAccertifyCollectorJsData(paymetricAccertifyData);
        StringBuilder promoCode= new StringBuilder();
        if(CollectionUtils.isNotEmpty(cart.getAllPromotionResults())) {
      	  promoCode= new StringBuilder("| ");
           for(PromotionResultModel promo:cart.getAllPromotionResults())
           {
         		RuleBasedPromotionModel rulePromo = (RuleBasedPromotionModel) promo.getPromotion();
         		promoCode.append(rulePromo.getCode());	
           }
        }
        accertifyData.setPromoCode(promoCode.toString());
        accertifyData.setEventSource("web");
        if (b2bCustomer.getCustomerProfile().equals(CustomerProfileType.PRACTITIONER)) {
            accertifyData.setAccountType("Business");
            accertifyData.setCustomerGroup("Practitioner");

        } else if (b2bCustomer.getCustomerProfile().equals(CustomerProfileType.PATIENT)) {
            accertifyData.setAccountType("Individual");
            accertifyData.setCustomerGroup("Patient");

        }
        accertifyData.setBrand("WholeScripts");
        if (Objects.nonNull(b2bCustomer.getDefaultB2BUnit())) {
            accertifyData.setAccountID(WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(b2bCustomer.getDefaultB2BUnit().getUid()));

        }
        accertifyData.setSuccess(Boolean.TRUE);
        accertifyData.setFraudServiceFlag("3");
        accertifyData.setTaxAmount(String.valueOf(cart.getTotalTax()));
        final AddressModel address = cart.getDeliveryAddress();
        if (Objects.nonNull(address)) {
            shippingAddressRequest.setAddressLine1(address.getLine1());
            shippingAddressRequest.setCity(address.getTown());
            if (Objects.nonNull(address.getCountry())) {
                shippingAddressRequest.setCountry(address.getCountry().getIsocode());

            }
            if(StringUtils.isNotBlank(address.getEmail()))
            {
                shippingAddressRequest.setEmail(address.getEmail());

            }
            if (Objects.nonNull(b2bCustomer.getDefaultB2BUnit())) {
                shippingAddressRequest.setId(WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(b2bCustomer.getDefaultB2BUnit().getUid()));

            }
            String firstName = address.getFirstname();
            String lastName = address.getLastname();
            if (StringUtils.isNotBlank(firstName) || StringUtils.isNotBlank(lastName)) {
                StringBuilder nameBuilder = new StringBuilder();

                if (StringUtils.isNotBlank(firstName)) {
                    nameBuilder.append(firstName);
                }

                if (StringUtils.isNotBlank(lastName)) {
                    if (!nameBuilder.isEmpty()) {
                        nameBuilder.append(" ");
                    }
                    nameBuilder.append(lastName);
                }

                shippingAddressRequest.setName(nameBuilder.toString());
            }
            shippingAddressRequest.setPostalCode(address.getPostalcode());
            if (Objects.nonNull(address.getRegion())) {
                final String regionCode = address.getRegion().getIsocode();
                shippingAddressRequest.setRegion(regionCode.split("-")[1]);

            }
            if(StringUtils.isNotBlank(address.getPhone1()))
            {
                shippingAddressRequest.setPhone(address.getPhone1());
            }

            shippingData.setShippingAddress(shippingAddressRequest);
        }

        if (Objects.nonNull(cart.getDeliveryMode())) {
            shippingData.setShippingMethod(cart.getDeliveryMode().getCode());
        }
        shippingData.setTotalShippingCharge(String.valueOf(cart.getDeliveryCost()));
        accertifyData.setShippingHeader(shippingData);

        final List<WholescriptsLineItemAuthRequestData> lineItemsList = new ArrayList<>();
        for (final AbstractOrderEntryModel entry : cart.getEntries()) {

            final WholescriptsLineItemAuthRequestData itemRequest = new WholescriptsLineItemAuthRequestData();
            final List<String> excludedProducts = Arrays.asList(Config.getParameter("medpax.product.exclude").split(","));

            if (!excludedProducts.contains(entry.getProduct().getCode()) && CollectionUtils.isNotEmpty(entry.getEntryGroupNumbers())) {
                itemRequest.setQuantity(String.valueOf(entry.getQuantity() * 30));
            } else {
                itemRequest.setQuantity(String.valueOf(entry.getQuantity()));
            }

            String productName = entry.getProduct().getName();

            if (entry.getProduct().getSapMaterialGroup() != null) {
                productName = entry.getProduct().getSapMaterialGroup().getCode().concat("|").concat(productName);
            }

            itemRequest.setProductName(productName);
            itemRequest.setSku(entry.getProduct().getCode());
            itemRequest.setPrice(String.valueOf(entry.getTotalPrice()));

            lineItemsList.add(itemRequest);

        }
        accertifyData.setLineItems(lineItemsList);
        requestBody.setPaymetricAccertify(accertifyData);
        final BaseStoreModel baseStore = cart.getStore();
        requestBody.setStoreId(baseStore.getPaymentricStoreId());
        requestBody.setIsOrderSubmit(true);
        requestBody.setMessage("WholeScripts.com Order");
        if (Objects.nonNull(b2bCustomer.getDefaultB2BUnit())) {
            requestBody.setSapCustomerId(WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(b2bCustomer.getDefaultB2BUnit().getUid()));
        }
        return requestBody;
    }

    private BigDecimal calculateAuthAmount(final CartModel cart) {
        final Double totalPrice = cart.getTotalPrice();
        final Double totalTax = (cart.getNet().booleanValue() && cart.getStore() != null
                && cart.getStore().getExternalTaxEnabled().booleanValue()) ? cart.getTotalTax() : Double.valueOf(0d);
        final BigDecimal totalPriceWithoutTaxBD = BigDecimal.valueOf(totalPrice == null ? 0d : totalPrice.doubleValue()).setScale(2,
                RoundingMode.HALF_EVEN);
        final BigDecimal totalPriceBD = BigDecimal.valueOf(totalTax == null ? 0d : totalTax.doubleValue())
                .setScale(2, RoundingMode.HALF_EVEN).add(totalPriceWithoutTaxBD);
        return totalPriceBD;
    }

    private WholescriptsAuthResponseData paymentricAuthorizationForTheCart(final WholescriptsPreAuthRequestData requestBody) throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        final RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
        final String authDestName = Config.getParameter("wholescripts.creditcard.auth.destination.name");
        final UserModel b2bCustomer = userService.getCurrentUser();
        try {
            final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(authDestName);
            LOG.info("Payment auth request" + objectMapper.writeValueAsString(requestBody));
            final ResponseEntity<String> response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST,
                    requestEntity, String.class);
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            final WholescriptsAuthResponseData responseData = objectMapper.readValue(response.getBody(), WholescriptsAuthResponseData.class);
            LOG.info("Payment auth response" + objectMapper.writeValueAsString(responseData));

            saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, authDestName);
            return responseData;
        } catch (Exception ex) {
            saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, authDestName);
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
        }


    }

    @Override
    public WholescriptsAuthResponseData authorizationForTheCart(final CartModel cart, final OrderCreationRequest orderCreationRequest) throws JsonProcessingException {
        final WholescriptsPreAuthRequestData requestBody = createRequestForCreditCardAuth(cart, orderCreationRequest);
        return paymentricAuthorizationForTheCart(requestBody);
    }

    @Override
    public WholescriptsAccountChangeResponseData accountChangeForPatient(final B2BCustomerModel b2bCustomer, final ReferralCodeWsDTO referralCodeRequest) throws Exception {
        final SAPConfigurationModel configModel = getSapConfiguration();
        if (Objects.nonNull(configModel)) {
            final WholescriptsAccountChangeRequestData requestBody = createRequestForAccountUpdate(configModel, b2bCustomer, referralCodeRequest);
            WholescriptsAccountChangeResponseData accountResponse = new WholescriptsAccountChangeResponseData();
            final ObjectMapper objectMapper = new ObjectMapper();
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(WholescriptsCoreUtils.getSCPIOauthAccessToken(getConsumedDestinationModel()));
            final RestTemplate restTemplate = new RestTemplate();
            final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
            final String accountUpdateDestName = Config.getParameter("wholescripts.update.patient.account.destination.name");
            ResponseEntity<String> response = null;
            try {
                final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(accountUpdateDestName);
                response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST, requestEntity, String.class);
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
                objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
                objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
                objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
                final WholescriptsAccountChangeResponseData responseData = objectMapper.readValue(response.getBody(), WholescriptsAccountChangeResponseData.class);
                saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, accountUpdateDestName);
                if ("0".equals(responseData.getReturnCode())) {
                    accountResponse = objectMapper.readValue(response.getBody(), WholescriptsAccountChangeResponseData.class);
                    return accountResponse;
                }

            } catch (Exception ex) {
                saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, accountUpdateDestName);
                throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
            }

            final ReturnErrorMessageData errorMessage = objectMapper.readValue(response.getBody(), ReturnErrorMessageData.class);
              throw new RequestParameterException(String.format(errorMessage.getReturnMessage().getItems().stream().filter(i -> i.getType().equals("E")).map(i -> i.getMessage()).collect(Collectors.toList()).toString()),
              RequestParameterException.UNKNOWN_IDENTIFIER);
        }
        throw new RequestParameterException(String.format("Sap configuartion model is not found"),
                RequestParameterException.MISSING);
    }

    private WholescriptsAccountChangeRequestData createRequestForAccountUpdate(final SAPConfigurationModel configModel, final B2BCustomerModel b2bCustomer, final ReferralCodeWsDTO referralCodeRequest) {
        final WholescriptsAccountChangeRequestData requestBody = new WholescriptsAccountChangeRequestData();
        if (Objects.nonNull(b2bCustomer.getDefaultB2BUnit())) {
            B2BUnitModel defaultUnit=b2bCustomer.getDefaultB2BUnit();
         if(StringUtils.isNotBlank(defaultUnit.getSapBlocked()))
            {
                requestBody.setIsBlocked(defaultUnit.getSapBlocked());
            }

            if (Objects.nonNull(defaultUnit.getSapCustomerGroup()))
            {
                requestBody.setAccountType(defaultUnit.getSapCustomerGroup().getCode());
            }
            if (Objects.nonNull(defaultUnit.getSapCustomerPriceGroup()))
            {
                requestBody.setPriceGroup(defaultUnit.getSapCustomerPriceGroup().getCode());
            }
            if (Objects.nonNull(defaultUnit.getSapPaymentTerms()))
            {
                requestBody.setPaymentTerms(defaultUnit.getSapPaymentTerms().getCode());
            }
            requestBody.setAccountNumber(WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(defaultUnit.getUid()));

        }
        requestBody.setCompany(configModel.getSapcommon_salesOrganization());
        requestBody.setDivision(configModel.getSapcommon_division());
        final ReferralCodesRequestData referralCodesRequestData = new ReferralCodesRequestData();
        referralCodesRequestData.setRefCode(referralCodeRequest.getReferralCode());
        referralCodesRequestData.setRefName(referralCodeRequest.getLastName());
        referralCodesRequestData.setAccessLevel("1");
        final SapAddressData addressData = new SapAddressData();
        if (Objects.nonNull(b2bCustomer.getDefaultB2BUnit())) {
            final String accountNumber = WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnitWithoutFormat(b2bCustomer.getDefaultB2BUnit().getUid());
            final Optional<AddressModel> defaultAddress = b2bCustomer.getDefaultB2BUnit().getAddresses().stream().filter(a -> a.getSapPublicKey() != null && a.getSapPublicKey().contains(accountNumber)).findFirst();
            AddressModel address;
            if (defaultAddress.isPresent()) {
                address = defaultAddress.get();
                addressData.setStreet(address.getLine1());
                addressData.setCity(address.getTown());
                addressData.setHouseNumber("");
                addressData.setPoBox("");
                addressData.setPostalCode(address.getPostalcode());
                addressData.setStrSuppl1(address.getLine2());
                addressData.setStrSuppl2("");
                addressData.setStrSuppl3("");
                addressData.setLangu("EN");
                if (Objects.nonNull(address.getCountry())) {
                    addressData.setCountry(address.getCountry().getIsocode());
                }
                if (Objects.nonNull(address.getRegion()) && Objects.nonNull(address.getRegion().getIsocode())) {
                    final String region;
                    if (address.getRegion().getIsocode().contains("-")) {
                        final String[] unit = address.getRegion().getIsocode().split("-");
                        addressData.setRegion(unit[1]);
                    } else {
                        addressData.setRegion(address.getRegion().getIsocode());
                    }
                }
               if(StringUtils.isNotBlank(b2bCustomer.getUid())){
                    addressData.setEmail(b2bCustomer.getUid());
                }
                addressData.setTelePhone(address.getPhone1());
                addressData.setMobile(address.getCellphone());
                addressData.setCoName(address.getCompany());
            }
        }
        requestBody.setAddress(addressData);
        requestBody.setReferralCode(referralCodesRequestData);
        requestBody.setSalesOrg(configModel.getSapcommon_salesOrganization());
        requestBody.setDistChannel(configModel.getSapcommon_distributionChannel());
        if (Objects.nonNull(referralCodeRequest.getDiscount())) {
            requestBody.setDiscount(String.format("%02d", referralCodeRequest.getDiscount()));
        }
        requestBody.setAccessLevel("1");
        return requestBody;
    }


    @Override
    public AutoShipOrderResponseData getAutoShipOrders(final String orderNumber) throws Exception {
        AutoShipOrderResponseData responseData = new AutoShipOrderResponseData();
        final SAPConfigurationModel configModel = getSapConfiguration();
        final B2BCustomerModel b2bCustomer = b2bCustomerService.getCurrentB2BCustomer();
        if (Objects.nonNull(b2bCustomer.getDefaultB2BUnit())) {
            final AutoShipRequestData requestBody = createAutoShipRequestData(b2bCustomer, configModel, orderNumber);
            final ObjectMapper objectMapper = new ObjectMapper();
            final HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(WholescriptsCoreUtils.getSCPIOauthAccessToken(getConsumedDestinationModel()));
            final RestTemplate restTemplate = new RestTemplate();
            final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
            final String autoShipDestName = Config.getParameter("autoship.order.history.destination.name");
            ResponseEntity<String> response = null;
            try {
                final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(autoShipDestName);
                response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST, requestEntity, String.class);
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
                objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
                objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
                objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
                final ReturnResponseData returnData = objectMapper.readValue(response.getBody(), ReturnResponseData.class);
                saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, autoShipDestName);
                if ("0".equals(returnData.getRETURN())) {
                    responseData = objectMapper.readValue(response.getBody(), AutoShipOrderResponseData.class);
                    if (responseData.getAutoShipsHdr() != null && CollectionUtils.isNotEmpty(responseData.getAutoShipsHdr().getItem())) {
                        List<String> soldToList = responseData.getAutoShipsHdr().getItem().stream().map(a -> a.getSoldTo()).collect(Collectors.toList());

                        if (CollectionUtils.isNotEmpty(soldToList) && !soldToList.contains(WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(b2bCustomer.getDefaultB2BUnit().getUid()))) {
                            throw new WholescriptsPageNotFoundException("sold To doesn't match");
                        }
                    }

                    return responseData;
                }
            } catch (Exception ex) {
            	if(ex instanceof WholescriptsPageNotFoundException) {
            		 throw new WholescriptsPageNotFoundException("sold To doesn't match");
            	}
            	saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, autoShipDestName);
               throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
            }
            final ReturnErrorMessageData errorMessage = objectMapper.readValue(response.getBody(), ReturnErrorMessageData.class);
       throw new RequestParameterException(String.format(errorMessage.getReturnMessage().getItems().stream().filter(i -> i.getType().equals("E")).map(i -> i.getMessage()).collect(Collectors.toList()).toString()),
              RequestParameterException.UNKNOWN_IDENTIFIER);

        }
        return responseData;
    }

    @Override
    public AutoShipOrderResponseData getAutoShipOrdersForCurrentUser() throws Exception {
        final AutoShipOrderResponseData responseData = new AutoShipOrderResponseData();
        final SAPConfigurationModel configModel = getSapConfiguration();
        final B2BCustomerModel b2bCustomer = b2bCustomerService.getCurrentB2BCustomer();

        if (Objects.nonNull(b2bCustomer.getDefaultB2BUnit())) {
            final AutoShipRequestData requestBody = createAutoShipRequestData(b2bCustomer, configModel, ""); // No order number needed
            final ObjectMapper objectMapper = new ObjectMapper();
            final HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(WholescriptsCoreUtils.getSCPIOauthAccessToken(getConsumedDestinationModel()));

            final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
            final String autoShipDestName = Config.getParameter("autoship.order.history.destination.name");
            ResponseEntity<String> response = null;
            try {
                final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(autoShipDestName);
                final RestTemplate restTemplate = new RestTemplate();
                response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST, requestEntity, String.class);

                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
                objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
                objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
                objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));

                final ReturnResponseData returnData = objectMapper.readValue(response.getBody(), ReturnResponseData.class);
                if ("0".equals(returnData.getRETURN())) {
                    return objectMapper.readValue(response.getBody(), AutoShipOrderResponseData.class);
                }
            } catch (Exception ex) {
                throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
            }

            final ReturnErrorMessageData errorMessage = objectMapper.readValue(response.getBody(), ReturnErrorMessageData.class);
            throw new RequestParameterException(String.format(errorMessage.getReturnMessage().getItems().stream().filter(i -> i.getType().equals("E")).map(i -> i.getMessage()).collect(Collectors.toList()).toString()),
                  RequestParameterException.UNKNOWN_IDENTIFIER);
        }
        return responseData;
    }


    private AutoShipRequestData createAutoShipRequestData(final B2BCustomerModel b2bCustomer, final SAPConfigurationModel
            configModel, final String orderNumber) {
        final AutoShipRequestData requestBody = new AutoShipRequestData();
        if (orderNumber.isEmpty()) {
            requestBody.setAccountNumber(
                    WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(b2bCustomer.getDefaultB2BUnit().getUid()));
            requestBody.setOrderNumber("");
        } else {
            requestBody.setAccountNumber("");
            requestBody.setOrderNumber(orderNumber);
        }
        requestBody.setSalesOrg(configModel.getSapcommon_salesOrganization());
        requestBody.setDistChannel(configModel.getSapcommon_distributionChannel());
        requestBody.setDivision(configModel.getSapcommon_division());
        requestBody.setAsType("PR");
        return requestBody;
    }


    @Override
    public AddressChangeResponseData updateAddressInSAP(final AddressWsDTO addressRequestData) throws Exception {
        final SAPConfigurationModel configModel = getSapConfiguration();
        if (Objects.nonNull(configModel)) {
            final ChangeAddressInSAPRequestData requestBody = createRequestForAddressUpdate(configModel, addressRequestData);
            AddressChangeResponseData addressUpdateResponse = new AddressChangeResponseData();
            final ObjectMapper objectMapper = new ObjectMapper();
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(WholescriptsCoreUtils.getSCPIOauthAccessToken(getConsumedDestinationModel()));
            final RestTemplate restTemplate = new RestTemplate();
            final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
            final String addressUpdateDestName = Config.getParameter("update.address.destination.name");
            ResponseEntity<String> response = null;
            final UserModel b2bCustomer = userService.getCurrentUser();
            try {
                final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(addressUpdateDestName);
                response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST, requestEntity, String.class);
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
                final ReturnResponseData responseData = objectMapper.readValue(response.getBody(), ReturnResponseData.class);
                saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), userService.getCurrentUser(), STATUS_INTEGRATION_SUCCESS, addressUpdateDestName);
                if ("0".equals(responseData.getRETURN())) {
                    addressUpdateResponse = objectMapper.readValue(response.getBody(), AddressChangeResponseData.class);
                    return addressUpdateResponse;
                }
            } catch (Exception ex) {
                saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, addressUpdateDestName);
                throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
            }

            final ReturnErrorMessageData errorMessage = objectMapper.readValue(response.getBody(), ReturnErrorMessageData.class);
             throw new RequestParameterException(String.format(errorMessage.getReturnMessage().getItems().stream().filter(i -> i.getType().equals("E")).map(i -> i.getMessage()).collect(Collectors.toList()).toString()),
              RequestParameterException.UNKNOWN_IDENTIFIER);
        }
        throw new RequestParameterException(String.format("SAP configuration model is not found"),
                RequestParameterException.MISSING);
    }

    private ChangeAddressInSAPRequestData createRequestForAddressUpdate(final SAPConfigurationModel
                                                                                configModel, final AddressWsDTO addressRequestData) {

        final ChangeAddressInSAPRequestData changeAddressInSAPRequestData = new ChangeAddressInSAPRequestData();
        changeAddressInSAPRequestData.setAccountNumber(addressRequestData.getAddressNumber());
        changeAddressInSAPRequestData.setSalesOrg(configModel.getSapcommon_salesOrganization());
        changeAddressInSAPRequestData.setDisChannel(configModel.getSapcommon_distributionChannel());
        changeAddressInSAPRequestData.setDivision(configModel.getSapcommon_division());
        changeAddressInSAPRequestData.setOrigin("");

        final AddressChangePersonData personData = new AddressChangePersonData();
        personData.setFirstName(addressRequestData.getFirstName());
        personData.setLastName(addressRequestData.getLastName());
        changeAddressInSAPRequestData.setPersonData(personData);

        final RegistrationAddressData addressData = new RegistrationAddressData();
        addressData.setStreet(addressRequestData.getLine1());
        addressData.setCity(addressRequestData.getTown());
        addressData.setHouseNo("");
        addressData.setPoBox("");
        addressData.setPostlCode(addressRequestData.getPostalCode());
        addressData.setStrSuppl1(addressRequestData.getLine2());
        addressData.setStrSuppl2("");
        addressData.setStrSuppl3("");
        addressData.setLang("E");
        if (Objects.nonNull(addressRequestData.getCountry())) {
            addressData.setCountry(addressRequestData.getCountry().getIsocode());
        }
        if (Objects.nonNull(addressRequestData.getRegion()) && Objects.nonNull(addressRequestData.getRegion().getIsocode())) {
            final String region;
            if (addressRequestData.getRegion().getIsocode().contains("-")) {
                final String[] unit = addressRequestData.getRegion().getIsocode().split("-");
                addressData.setRegion(unit[1]);
            } else {
                addressData.setRegion(addressRequestData.getRegion().getIsocode());
            }
        }

        addressData.setOrdEmail(addressRequestData.getEmail());
        addressData.setTelephone(addressRequestData.getCellphone());
        addressData.setMobile(addressRequestData.getCellphone());
        addressData.setCoName(addressRequestData.getRemarks());
        changeAddressInSAPRequestData.setAddressData(addressData);
        return changeAddressInSAPRequestData;

    }

    @Override
    public void removeAddress(final B2BCustomerModel b2bCustomer, final String addressId) throws Exception {

        final AddressRemoveInSAPRequestData requestBody = new AddressRemoveInSAPRequestData();
        final SAPConfigurationModel configurationModel = getSapConfiguration();
        requestBody.setAccountNumber(addressId);
        if (Objects.nonNull(configurationModel)) {
            requestBody.setSalesOrg(configurationModel.getSapcommon_salesOrganization());
            requestBody.setDisChannel(configurationModel.getSapcommon_distributionChannel());
            requestBody.setDivision(configurationModel.getSapcommon_division());
        }
        ReturnResponseData removeAddressResponse = null;
        ResponseEntity<String> response = null;
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(WholescriptsCoreUtils.getSCPIOauthAccessToken(getConsumedDestinationModel()));
        final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
        final String removeAddressDestName = Config.getParameter("remove.address.destination.name");
        try {
            final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(removeAddressDestName);
            final RestTemplate restTemplate = new RestTemplate();
            response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST, requestEntity, String.class);
            removeAddressResponse = objectMapper.readValue(response.getBody(), ReturnResponseData.class);
            saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, removeAddressDestName);
        } catch (Exception ex) {
            saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, removeAddressDestName);
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
        }

        if (null != removeAddressResponse && "1".equals(removeAddressResponse.getRETURN())) {
            final ReturnErrorMessageData errorMessage = objectMapper.readValue(response.getBody(), ReturnErrorMessageData.class);
  throw new RequestParameterException(String.format(errorMessage.getReturnMessage().getItems().stream().filter(i -> i.getType().equals("E")).map(i -> i.getMessage()).collect(Collectors.toList()).toString()),
              RequestParameterException.UNKNOWN_IDENTIFIER);
        }
    }

    @Override
    public PersonaInquiriesResponseData verifyAgeAndUpdateInSAP(final String inquiryId) throws Exception {
        final ConsumedDestinationModel consumed = destinationSearchService.findDestination(PERSONA_BASIC_DESTINATION);
        final ObjectMapper objectMapper = new ObjectMapper();
        HttpEntity<String> requestEntity = null;
        final UserModel b2bCustomer = userService.getCurrentUser();
        try {
            final BasicCredentialModel basic = (BasicCredentialModel) consumed.getCredential();
            final String auth = basic.getPassword();
            final String peosonainquiryURL = consumed.getUrl() + inquiryId;
            final String basicAuth = "Bearer" + " " + auth;
            final RestTemplate restTemplate = new RestTemplate();
            final HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", basicAuth);
            headers.setContentType(MediaType.APPLICATION_JSON);
            requestEntity = new HttpEntity<>(headers);
            // Send the request and get the response
            final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(peosonainquiryURL);
            final ResponseEntity<String> response = restTemplate.exchange(builder.build(true).encode().toUri(), HttpMethod.GET,
                    requestEntity, String.class);
            saveIntegrationModel(peosonainquiryURL, objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, consumed.getId());
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            return objectMapper.readValue(response.getBody(), PersonaInquiriesResponseData.class);
        } catch (Exception ex) {
            saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, consumed.getId());
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
        }
    }

    @Override
    public boolean UpdateAgeVerificationInSAP(final AgeValidationRequestData requestBody) throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(WholescriptsCoreUtils.getSCPIOauthAccessToken(getConsumedDestinationModel()));
        final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
        final String ageValidationDestName = Config.getParameter("age.validation.destination.name");
        final UserModel b2bCustomer = userService.getCurrentUser();
        try {

            final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(ageValidationDestName);
            final RestTemplate restTemplate = new RestTemplate();
            final ResponseEntity<String> response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST,
                    requestEntity, String.class);

            final ReturnErrorMessageData messageData = objectMapper.readValue(response.getBody(), ReturnErrorMessageData.class);
            saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, ageValidationDestName);
            final Optional<ReturnMessageData> itemData = messageData.getReturnMessage().getItems().stream().filter(i -> i.getType().equals("S")).findAny();
            if (itemData.isPresent()) {
                return true;
            }
            return false;
        } catch (Exception ex) {
            saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, ageValidationDestName);
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
        }

    }

    @Override
    public ExternalSystemResponseData sendPractitionerAmendment(final PractitionerAmendmentRequestData requestBody)
            throws Exception {

        final ObjectMapper objectMapper = new ObjectMapper();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        final RestTemplate restTemplate = new RestTemplate();
        LOG.debug("Submit amendment request is " + objectMapper.writeValueAsString(requestBody) + " for email id "
                + requestBody.getEmail());
        final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
        final String destinationUrlName = Config.getParameter("ws.submit.practitioner.amendment.destination.name");
        final UserModel b2bCustomer = userService.getCurrentUser();
        try {

            final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(destinationUrlName);
            final ResponseEntity<String> response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST,
                    requestEntity, String.class);
            LOG.debug("Submit amendment response is " + response.getBody() + " for email id " + requestBody.getEmail());
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            final ExternalSystemResponseData responseData = objectMapper.readValue(response.getBody(),
                    ExternalSystemResponseData.class);
            saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, destinationUrlName);
            return responseData;

        } catch (Exception ex) {
            saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, destinationUrlName);
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
        }

    }

    @Override
    public CustomerExternalDetailsData getExternalDetails(final String accountNumber) throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        final RestTemplate restTemplate = new RestTemplate();
        final String destinationUrlName = Config.getParameter("ws.estore.external.details.destination.name");
        final UserModel b2bCustomer = userService.getCurrentUser();
        final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(destinationUrlName);
        final String tagetUrl = httpDestination.getTargetURL();
        final String destinationUrl = tagetUrl.concat(accountNumber);
        final String request = "Get external details destinationUrl is " + destinationUrl + " for b2bunit id " + accountNumber;
        LOG.debug(request);
        try {
            final ResponseEntity<String> response = restTemplate.getForEntity(destinationUrl, String.class);
            LOG.debug("External details response is " + response.getBody() + " for b2bunit id " + accountNumber);
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            final CustomerExternalDetailsData responseData = objectMapper.readValue(response.getBody(),
                    CustomerExternalDetailsData.class);
            saveIntegrationModel(request, objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, destinationUrlName);
            return responseData;
        } catch (Exception ex) {
            saveIntegrationModel(request, objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, destinationUrlName);
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
        }

    }

    @Override
    public ReturnResponseData removeCreditCard(final String cardId, final B2BCustomerModel b2bCustomer, String payerId) throws Exception {
        final WholescriptsRemoveCardRequestData requestBody = new WholescriptsRemoveCardRequestData();
        if(StringUtils.isNotEmpty(payerId))
        {
            requestBody.setAccountNumber(WholescriptsCoreUtils.getDefaultB2bUnit(payerId));
        }
        requestBody.setCartId(cardId);

        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(WholescriptsCoreUtils.getSCPIOauthAccessToken(getConsumedDestinationModel()));
        final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
        final String removeCardDestName = Config.getParameter("wholescripts.delete.creditcard.destination.name");
        ResponseEntity<String> response = null;
        try {
            final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(removeCardDestName);

            final RestTemplate restTemplate = new RestTemplate();
            response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST, requestEntity, String.class);
            final ReturnResponseData removeCardResponse = objectMapper.readValue(response.getBody(), ReturnResponseData.class);
            saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, removeCardDestName);
            if ("0".equals(removeCardResponse.getRETURN())) {
                return removeCardResponse;
            }
        } catch (Exception ex) {
            saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, removeCardDestName);
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
        }

        final ReturnErrorMessageData errorMessage = objectMapper.readValue(response.getBody(), ReturnErrorMessageData.class);
          throw new RequestParameterException(String.format(errorMessage.getReturnMessage().getItems().stream().filter(i -> i.getType().equals("E")).map(i -> i.getMessage()).collect(Collectors.toList()).toString()),
              RequestParameterException.UNKNOWN_IDENTIFIER);

    }

    @Override
    public PractitionerAmendmentDetailData getPractitionerAmendment(final String type, final String accountNumber)
            throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        final RestTemplate restTemplate = new RestTemplate();
        final String destinationUrlName = Config.getParameter("ws.practitioner.amendment.details.destination.name");
        final UserModel b2bCustomer = userService.getCurrentUser();
        final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(destinationUrlName);
        final String targetUrl = httpDestination.getTargetURL();
        final String destinationUrl = targetUrl.concat(type).concat("/").concat(accountNumber);
        final String request = "Get amendment details destinationUrl is " + destinationUrl + " for b2bunit id " + accountNumber;
        LOG.debug(request);
        try {
            final ResponseEntity<String> response = restTemplate.getForEntity(destinationUrl, String.class);
            LOG.debug("External amendment details response is " + response.getBody() + " for b2bunit id " + accountNumber);
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            final PractitionerAmendmentDetailData responseData = objectMapper.readValue(response.getBody(),
                    PractitionerAmendmentDetailData.class);
            saveIntegrationModel(request, objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, destinationUrlName);
            return responseData;
        } catch (Exception ex) {
            saveIntegrationModel(request, objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, destinationUrlName);
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
        }


    }

    @Override
    public EstoreCredentialResponseData addEstoreCredentials(final EstoreCredentialRequestData requestBody) throws Exception {

        final ObjectMapper objectMapper = new ObjectMapper();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        final RestTemplate restTemplate = new RestTemplate();
        LOG.debug("Add estore credentials request is " + objectMapper.writeValueAsString(requestBody) + " for sap csutomer id "
                + requestBody.getSapCustomerId());
        final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
        final String destinationUrlName = Config.getParameter("ws.add.estore.credential.destination.name");
        final UserModel b2bCustomer = userService.getCurrentUser();
        try {

            final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(destinationUrlName);
            final ResponseEntity<String> response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST,
                    requestEntity, String.class);
            LOG.debug("Add estore credentials response is " + response.getBody() + " for sap customer id "
                    + requestBody.getSapCustomerId());
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            final EstoreCredentialResponseData responseData = objectMapper.readValue(response.getBody(),
                    EstoreCredentialResponseData.class);
            saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, destinationUrlName);
            return responseData;
        } catch (Exception ex) {
            saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, destinationUrlName);
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
        }


    }

    @Override
    public EstoreCredentialResponseData deleteEstoreCredentials(final EstoreCredentialRequestData requestBody) throws Exception {

        final ObjectMapper objectMapper = new ObjectMapper();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        final RestTemplate restTemplate = new RestTemplate();
        LOG.debug("Delete estore credentials response is " + objectMapper.writeValueAsString(requestBody) + " for sap csutomer id "
                + requestBody.getSapCustomerId());
        final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
        final String destinationUrlName = Config.getParameter("ws.delete.estore.credential.destination.name");
        final UserModel b2bCustomer = userService.getCurrentUser();
        try {

            final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(destinationUrlName);
            final ResponseEntity<String> response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST,
                    requestEntity, String.class);
            LOG.debug("Delete estore credentials response is " + response.getBody() + " for sap customer id "
                    + requestBody.getSapCustomerId());
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            final EstoreCredentialResponseData responseData = objectMapper.readValue(response.getBody(),
                    EstoreCredentialResponseData.class);
            saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, destinationUrlName);
            return responseData;
        } catch (Exception ex) {
            saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, destinationUrlName);
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
        }


    }

    @Override
    public EstoreCredentialResponseData changeEstoreCredentials(final EstoreCredentialRequestData requestBody) throws Exception {

        final ObjectMapper objectMapper = new ObjectMapper();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        final RestTemplate restTemplate = new RestTemplate();
        LOG.debug("Change estore credentials request is " + objectMapper.writeValueAsString(requestBody) + " for sap csutomer id "
                + requestBody.getSapCustomerId());
        final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
        final String destinationUrlName = Config.getParameter("ws.change.estore.credential.destination.name");
        final UserModel b2bCustomer = userService.getCurrentUser();
        try {

            final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(destinationUrlName);
            final ResponseEntity<String> response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST,
                    requestEntity, String.class);
            LOG.debug("Change estore credentials response is " + response.getBody() + " for sap customer id "
                    + requestBody.getSapCustomerId());
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            final EstoreCredentialResponseData responseData = objectMapper.readValue(response.getBody(),
                    EstoreCredentialResponseData.class);
            saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, destinationUrlName);
            return responseData;
        } catch (Exception ex) {
            saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, destinationUrlName);
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
        }


    }

    @Override
    public EstoreOrderHistory getEstorePendingOrders(final String accountNumber) throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        final RestTemplate restTemplate = new RestTemplate();
        final String destinationUrlName = Config.getParameter("ws.pending.estore.orders.destination.name");
        final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(destinationUrlName);
        final String targetUrl = httpDestination.getTargetURL();
        final String destinationUrl = targetUrl.concat(accountNumber);
        final String request = "Estore pending orders destinationUrl is " + destinationUrl + " for b2bunit id " + accountNumber;
        LOG.debug(request);
        final UserModel b2bCustomer = userService.getCurrentUser();
        try {
            final ResponseEntity<String> response = restTemplate.getForEntity(destinationUrl, String.class);
            LOG.debug("External amendment details response is " + response.getBody() + " for b2bunit id " + accountNumber);
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            final EstoreOrderHistory responseData = objectMapper.readValue(response.getBody(),
                    EstoreOrderHistory.class);
            saveIntegrationModel(request, objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, destinationUrlName);
            return responseData;

        } catch (Exception ex) {
            saveIntegrationModel(request, objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, destinationUrlName);
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
        }
    }

    @Override
    public ShopifyStatusResponseData getShopifyStatus(final String accountNumber) throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        final RestTemplate restTemplate = new RestTemplate();
        final String destinationUrlName = Config.getParameter("ws.shopify.status.destination.name");
        final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(destinationUrlName);
        final String targetUrl = httpDestination.getTargetURL();
        final String destinationUrl = targetUrl.concat(accountNumber);
        final String request = "Shopify status destinationUrl is " + destinationUrl + " for b2bunit id " + accountNumber;
        LOG.debug(request);
        final UserModel b2bCustomer = userService.getCurrentUser();
        try {
            final ResponseEntity<String> response = restTemplate.getForEntity(destinationUrl, String.class);
            LOG.debug("Shopify status response is " + response.getBody() + " for b2bunit id " + accountNumber);
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            final ShopifyStatusResponseData responseData = objectMapper.readValue(response.getBody(),
                    ShopifyStatusResponseData.class);
            saveIntegrationModel(request, objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, destinationUrlName);
            return responseData;
        } catch (Exception ex) {
            saveIntegrationModel(request, objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, destinationUrlName);
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
        }

    }

    @Override
    public ShopifyProductDetailsData getShopifyProducts(final String accountNumber) throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        final RestTemplate restTemplate = new RestTemplate();
        final String destinationUrlName = Config.getParameter("ws.shopify.products.destination.name");
        final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(destinationUrlName);
        final String targetUrl = httpDestination.getTargetURL();
        final String destinationUrl = targetUrl.concat(accountNumber);
        final String request = "Get shopify products destinationUrl is " + destinationUrl + " for b2bunit id " + accountNumber;
        LOG.debug(request);
        final UserModel b2bCustomer = userService.getCurrentUser();
        try {
            final ResponseEntity<String> response = restTemplate.getForEntity(destinationUrl, String.class);
            LOG.debug("Shopify products response is " + response.getBody() + " for b2bunit id " + accountNumber);
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            final ShopifyProductDetailsData responseData = objectMapper.readValue(response.getBody(),
                    ShopifyProductDetailsData.class);
            saveIntegrationModel(request, objectMapper, response.getBody(), userService.getCurrentUser(), STATUS_INTEGRATION_SUCCESS, destinationUrlName);
            return responseData;
        } catch (Exception ex) {
            saveIntegrationModel(request, objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, destinationUrlName);
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
        }


    }

    @Override
    public OrderUpdateAutoShipResponseData updateAutoShipOrder(final OrderChangeAutoShipRequestWsDTO autoShipRequestWsDTO) throws Exception {


        final SAPConfigurationModel configModel = getSapConfiguration();
        if (Objects.nonNull(configModel)) {
            final OrderChangeAutoShipRequestData requestBody = new OrderChangeAutoShipRequestData();
            requestBody.setSalesOrg(configModel.getSapcommon_salesOrganization());
            requestBody.setDistChannel(configModel.getSapcommon_distributionChannel());
            requestBody.setDivision(configModel.getSapcommon_division());
            createRequestForAutoShipUpdate(autoShipRequestWsDTO, requestBody);
            final ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            final UserModel b2bCustomer = userService.getCurrentUser();
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(WholescriptsCoreUtils.getSCPIOauthAccessToken(getConsumedDestinationModel()));

            final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
            final String updateAutoShipDestName = Config.getParameter("wholescripts.update.autoship.orders.destination.name");
            ResponseEntity<String> response = null;
            try {
                final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(updateAutoShipDestName);

                final RestTemplate restTemplate = new RestTemplate();
                response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST, requestEntity, String.class);
                objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
                objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
                final OrderUpdateAutoShipResponseData responseData = objectMapper.readValue(response.getBody(), OrderUpdateAutoShipResponseData.class);
                saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, updateAutoShipDestName);
                if ("0".equals(responseData.getReturnCode())) {
                    return responseData;
                }
            } catch (Exception ex) {
                saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, updateAutoShipDestName);
                throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
            }

            final ReturnErrorMessageData errorMessage = objectMapper.readValue(response.getBody(), ReturnErrorMessageData.class);
             throw new RequestParameterException(String.format(errorMessage.getReturnMessage().getItems().stream().filter(i -> i.getType().equals("E")).map(i -> i.getMessage()).collect(Collectors.toList()).toString()),
              RequestParameterException.UNKNOWN_IDENTIFIER);

        }
        throw new RequestParameterException(String.format("Sap configuartion model is not found"),
                RequestParameterException.MISSING);

    }

    private void createRequestForAutoShipUpdate(final OrderChangeAutoShipRequestWsDTO autoShipRequestWsDTO, final OrderChangeAutoShipRequestData requestData) {
        requestData.setOrderNumber(autoShipRequestWsDTO.getOrderNumber());
        requestData.setInstructions(autoShipRequestWsDTO.getInstructions());
        requestData.setDoseNote(autoShipRequestWsDTO.getDoseNote());
        requestData.setShipMethod(autoShipRequestWsDTO.getShipMethod());
        requestData.setCarrier(autoShipRequestWsDTO.getCarrier());
        requestData.setShipTo(autoShipRequestWsDTO.getShipTo());
        requestData.setEndDate(autoShipRequestWsDTO.getEndDate());
        final OrderChangeAutoShipCreditCardRequestData creditCardRequestData = new OrderChangeAutoShipCreditCardRequestData();
        final OrderChangeAutoShipCreditCardRequestWsDTO creditRequest = autoShipRequestWsDTO.getCreditCardDetails();
        creditCardRequestData.setCardNumber(creditRequest.getCardNumber());
        creditCardRequestData.setCardHolderName(creditRequest.getCardHolderName());
        creditCardRequestData.setValidTo(creditRequest.getValidTo());
        creditCardRequestData.setCardType(creditRequest.getCardType());
        requestData.setCreditCardDetails(creditCardRequestData);
        final OrderChangeAutoShipLineItemListWsDTO items = autoShipRequestWsDTO.getItems();
        final List<OrderChangeAutoShipLineItemData> itemResponse = new ArrayList<>();
        final OrderChangeAutoShipLineItemListData lineItemListData = new OrderChangeAutoShipLineItemListData();
        for (final OrderChangeAutoShipLineItemWsDTO item : items.getItem()) {
            final OrderChangeAutoShipLineItemData lineItemData = new OrderChangeAutoShipLineItemData();
            lineItemData.setEntryNumber(item.getEntryNumber());
            lineItemData.setEntryGroupNumber(item.getEntryGroupNumber());
            lineItemData.setPoItemNumber(item.getPoItemNumber());
            lineItemData.setMaterial(item.getMaterial());
            lineItemData.setDescription(item.getDescription());
            lineItemData.setRequestedQty(item.getRequestedQty());
            lineItemData.setSalesUnit(item.getSalesUnit());
            final String price = String.valueOf(commonI18NService.roundCurrency(Double.parseDouble(item.getPrice()), 2));
            lineItemData.setPrice(price);
            lineItemData.setStartDate(item.getStartDate());
            lineItemData.setEndDate(item.getEndDate());
            lineItemData.setFrequency(item.getFrequency());
            lineItemData.setFrequency_type("1");
            lineItemData.setText(item.getText());
            lineItemData.setAction(item.getAction());
            lineItemData.setDoseNote(item.getDoseNote());
            lineItemData.setSapPrice(item.getSapPrice());
            lineItemData.setConfirmedQuantity(item.getConfirmedQuantity());
            lineItemData.setNextDate(item.getNextDate());
            lineItemData.setDateChangeType(item.getDateChangeType());
            lineItemData.setSapDiscount(item.getSapDiscount());
            lineItemData.setSapSubtotal(item.getSapSubtotal());
            lineItemData.setSapTax(item.getSapTax());
            lineItemData.setSapTotal(item.getSapTotal());
            lineItemData.setMatEntered(item.getMatEntered());
            lineItemData.setCaExclusion(item.getCaExclusion());
            lineItemData.setPriority(item.getPriority());
            lineItemData.setRecommendationId(item.getRecommendationId());
            lineItemData.setDiscount(item.getDiscount());
            lineItemData.setTemplateId(item.getTemplateId());
            lineItemData.setDiscountCash(item.getDiscountCash());
            itemResponse.add(lineItemData);
        }
        lineItemListData.setItem(itemResponse);
        requestData.setItems(lineItemListData);

    }

    @Override
    public ExternalSystemResponseData addShopifyProducts(final String accountNumber, final List<String> products) throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        final RestTemplate restTemplate = new RestTemplate();
        final String destinationUrlName = Config.getParameter("ws.add.shopify.products.destination.name");
        final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(destinationUrlName);
        final String targetUrl = httpDestination.getTargetURL();
        final String destinationUrl = targetUrl.concat(accountNumber);
        final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(products), headers);
        LOG.debug("Add shopify products request is " + requestEntity.getBody() + " for b2bunit id " + accountNumber);
        final UserModel b2bCustomer = userService.getCurrentUser();
        try {
            final ResponseEntity<String> response = restTemplate.exchange(destinationUrl, HttpMethod.POST,
                    requestEntity, String.class);
            LOG.debug("Add shopify products response is " + response.getBody() + " for b2bunit id " + accountNumber);
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            final ExternalSystemResponseData responseData = objectMapper.readValue(response.getBody(),
                    ExternalSystemResponseData.class);
            saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, destinationUrlName);
            return responseData;
        } catch (Exception ex) {
            saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, destinationUrlName);
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
        }
    }

    @Override
    public ExternalSystemResponseData deleteShopifyProducts(final String accountNumber, final List<String> products) throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        final RestTemplate restTemplate = new RestTemplate();
        final String destinationUrlName = Config.getParameter("ws.delete.shopify.products.destination.name");
        final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(destinationUrlName);
        final String targetUrl = httpDestination.getTargetURL();
        final String destinationUrl = targetUrl.concat(accountNumber);
        final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(products), headers);
        LOG.debug("DELETE shopify products request is " + requestEntity.getBody() + " for b2bunit id " + accountNumber);
        final UserModel b2bCustomer = userService.getCurrentUser();
        try {
            final ResponseEntity<String> response = restTemplate.exchange(destinationUrl, HttpMethod.DELETE,
                    requestEntity, String.class);
            LOG.debug("DELETE shopify products response is " + response.getBody() + " for b2bunit id " + accountNumber);
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            final ExternalSystemResponseData responseData = objectMapper.readValue(response.getBody(),
                    ExternalSystemResponseData.class);
            saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), userService.getCurrentUser(), STATUS_INTEGRATION_SUCCESS, destinationUrlName);
            return responseData;
        } catch (Exception ex) {
            saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, destinationUrlName);
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
        }


    }

    @Override
    public ExternalSystemResponseData estoreShippingConfirmation(final EstoreShippingConfirmationRequest requestBody) throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        final RestTemplate restTemplate = new RestTemplate();
        final String destinationUrlName = Config.getParameter("ws.estore.shipping.confirmation.destination.name");
        final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(destinationUrlName);
        final String destinationUrl = httpDestination.getTargetURL();
        final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
        LOG.debug("Estore shipping confirmation request is " + requestEntity.getBody() + " for sap customer id " + requestBody.getSapCustomerId());
        final UserModel b2bCustomer = userService.getCurrentUser();
        try {
            final ResponseEntity<String> response = restTemplate.exchange(destinationUrl, HttpMethod.POST,
                    requestEntity, String.class);
            LOG.debug("Estore shipping confirmation response is " + response.getBody() + " for sap customer id " + requestBody.getSapCustomerId());
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            final ExternalSystemResponseData responseData = objectMapper.readValue(response.getBody(),
                    ExternalSystemResponseData.class);
            saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, destinationUrlName);
            return responseData;
        } catch (Exception ex) {
            saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, destinationUrlName);
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
        }

    }

    @Override
    public AccertifyVerificationCreateAccountResponseData accertifyCreateAccountVerification(final AccertifyVerificationCreateAccountRequestData requestData) throws Exception {
        final ConsumedDestinationModel consumedDestination = destinationSearchService.findDestination(ACCERTIFY_CREATE_ACCOUNT_BASIC_DESTINATION);
        final BasicCredentialModel credentialModel = (BasicCredentialModel) consumedDestination.getCredential();
        final String authorization = credentialModel.getUsername() + ":" + credentialModel.getPassword();
        final String accertifyCreateAccountURL = consumedDestination.getUrl();
        final String basicAuthorization = "Basic" + " " + Base64.getEncoder().encodeToString(authorization.getBytes());
        final RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", basicAuthorization);
        headers.setContentType(MediaType.APPLICATION_JSON);
        final ObjectMapper objectMapper = new ObjectMapper();
        final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestData), headers);
        final UserModel b2bCustomer = userService.getCurrentUser();
        try {
            final ResponseEntity<String> response = restTemplate.exchange(accertifyCreateAccountURL, HttpMethod.POST, requestEntity, String.class);
            saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, accertifyCreateAccountURL);
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
            objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
            objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            return objectMapper.readValue(response.getBody(), AccertifyVerificationCreateAccountResponseData.class);
        } catch (Exception ex) {
            saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, accertifyCreateAccountURL);
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
        }

    }


    @Override
    public AccertifyVerificationUpdateAccountResponseData accertifyUpdateAccountVerification(final AccertifyVerificationUpdateAccountRequestData requestData) throws Exception {
        final ConsumedDestinationModel consumedDestination = destinationSearchService.findDestination(ACCERTIFY_UPDATE_ACCOUNT_BASIC_DESTINATION);
        final BasicCredentialModel credentialModel = (BasicCredentialModel) consumedDestination.getCredential();
        final String authorization = credentialModel.getUsername() + ":" + credentialModel.getPassword();
        final String accertifyUpdateAccountURL = consumedDestination.getUrl();
        final String basicAuthorization = "Basic" + " " + Base64.getEncoder().encodeToString(authorization.getBytes());
        final RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", basicAuthorization);
        headers.setContentType(MediaType.APPLICATION_JSON);
        final ObjectMapper objectMapper = new ObjectMapper();
        final UserModel b2bCustomer = userService.getCurrentUser();
        final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestData), headers);
        try {
            final ResponseEntity<String> response = restTemplate.exchange(accertifyUpdateAccountURL, HttpMethod.POST, requestEntity, String.class);
            saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, accertifyUpdateAccountURL);
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
            objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
            objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            return objectMapper.readValue(response.getBody(), AccertifyVerificationUpdateAccountResponseData.class);
        } catch (Exception ex) {
            saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, accertifyUpdateAccountURL);
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
        }

    }

    @Override
    public void cancelOrderInSAP(final String orderId, final String orderEntryNumber) throws Exception {
        
   	  final OrderRemoveRequestData requestBody = new OrderRemoveRequestData();
        requestBody.setOrderNumber(orderId);
        requestBody.setReason("70");

        if (StringUtils.isNotEmpty(orderEntryNumber)) {
            // Handling line item cancellation
            OrderCancelItemData itemData = new OrderCancelItemData();
            itemData.setItem(orderEntryNumber);
            requestBody.setItems(itemData);
        }

        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(WholescriptsCoreUtils.getSCPIOauthAccessToken(getConsumedDestinationModel()));
        final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
        final String removeOrderDestName = Config.getParameter("remove.order.destination.name");
        ReturnResponseData removeOrderResponse = null;
        ResponseEntity<String> response = null;
        final UserModel b2bCustomer = userService.getCurrentUser();
        try {
            final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(removeOrderDestName);
            final RestTemplate restTemplate = new RestTemplate();
            response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST,
                    requestEntity, String.class);
            removeOrderResponse = objectMapper.readValue(response.getBody(), ReturnResponseData.class);
            saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, removeOrderDestName);

        } catch (Exception ex) {
            saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, removeOrderDestName);
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
        }

        if ("1".equals(removeOrderResponse.getRETURN())) {
            final ReturnErrorMessageData errorMessage = objectMapper.readValue(response.getBody(), ReturnErrorMessageData.class);
            throw new RequestParameterException(String.format(errorMessage.getReturnMessage().getItems().stream().filter(i -> i.getType().equals("E")).map(i -> i.getMessage()).collect(Collectors.toList()).toString()),
              RequestParameterException.UNKNOWN_IDENTIFIER);
        }
    }


    @Override
    public EstoreOrderHistory estoreOrderHistory(final EstoreOrderHistoryRequestData requestBody) throws Exception {

        final ObjectMapper objectMapper = new ObjectMapper();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        final RestTemplate restTemplate = new RestTemplate();
        LOG.debug("Estore order history request is " + objectMapper.writeValueAsString(requestBody) + " for sap csutomer id "
                + requestBody.getSapCustomerId());
        final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
        final String destinationUrlName = Config.getParameter("ws.estore.order.history.destination.name");
        final UserModel b2bCustomer = userService.getCurrentUser();
        try {
            final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(destinationUrlName);
            final ResponseEntity<String> response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST,
                    requestEntity, String.class);
            LOG.debug("Estore order history request is " + response.getBody() + " for sap customer id "
                    + requestBody.getSapCustomerId());
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            final EstoreOrderHistory responseData = objectMapper.readValue(response.getBody(), EstoreOrderHistory.class);
            saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, destinationUrlName);
            return responseData;
        } catch (Exception ex) {
            saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, destinationUrlName);
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
        }


    }

    @Override
    public WsSendRecommendationTextMessageResponse sendMessageUsingSinch(final StoreFrontCustomerProcessModel processModel) throws JsonProcessingException {
        final WsSendRecommendationTextMessageRequest request = new WsSendRecommendationTextMessageRequest();
        request.setReceiveFrom(Config.getParameter("sinch.from.phoneNumber"));
        final String toPhoneNumber = Config.getParameter("sinch.to.phoneNumber");
        RecommendationModel rModel=processModel.getRecommendation();
        List<String> toList = new ArrayList<>();
        if (StringUtils.isNotBlank(toPhoneNumber)) {
            toList.add(toPhoneNumber);
            request.setSendMessageTo(toList);
        } else {
            final String phoneNumber = getUserPhoneNumberByAddress(rModel);
            toList.add(phoneNumber);
            request.setSendMessageTo(toList);
        }

        final StringBuilder data = new StringBuilder();

        String practitionerName=rModel.getUser().getName();
        if(StringUtils.isNotEmpty(practitionerName))
        {
            String[] name = practitionerName.split("\\s+");
            practitionerName=name[0];
        }

       String baseUrl= siteBaseUrlResolutionService.getWebsiteUrlForSite(processModel.getSite(),true,"");
        data.append(practitionerName).append(" ").append(Config.getParameter("sinch.send.text.message.description1"));
        data.append(baseUrl).append(Config.getParameter("sinch.send.text.message.description2"));
        data.append(rModel.getCode());
        data.append(System.lineSeparator());
        data.append(Config.getParameter("sinch.send.text.message.description3"));
        request.setTextMessage(String.valueOf(data));
        request.setType("mt_text");
        final ObjectMapper objectMapper = new ObjectMapper();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + Config.getParameter("sinch.accessToken"));
        final RestTemplate restTemplate = new RestTemplate();
        final String destinationUrlName = Config.getParameter("ws.sinch.send.message.destination.name");
        final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(request), headers);
        final B2BCustomerModel b2bCustomer = rModel.getPatient();
        try {
            final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(destinationUrlName);
            final String targetUrl = httpDestination.getTargetURL();
            final String destinationUrl = targetUrl.concat(Config.getParameter("sinch.service.plan.id") + "/batches");
            final ResponseEntity<String> response = restTemplate.exchange(destinationUrl, HttpMethod.POST,
                    requestEntity, String.class);
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            final WsSendRecommendationTextMessageResponse responseData = objectMapper.readValue(response.getBody(),
                    WsSendRecommendationTextMessageResponse.class);
            saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, destinationUrlName);
            return responseData;
        } catch (Exception ex) {
            saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, destinationUrlName);
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
        }


    }

    private String getUserPhoneNumberByAddress(final RecommendationModel rModel) {
        final B2BCustomerModel customer = wholescriptsUserService.getUserByUid(rModel.getPatient().getUid());
        if (Objects.nonNull(customer) && Objects.nonNull(customer.getDefaultB2BUnit())) {
            final Optional<AddressModel> address = customer.getDefaultB2BUnit().getAddresses().stream().findFirst();
            if (address.isPresent()) {
                return address.get().getPhone1();
            }

        }
        return "";
    }

    private CreditCardRequestData createPaymentdetails(final CartModel cartmodel) {

        final CreditCardRequestData creditCardRequest = new CreditCardRequestData();
        if (Objects.nonNull(cartmodel.getPaymentInfo())
                && cartmodel.getPaymentInfo() instanceof final CreditCardPaymentInfoModel paymentInfoModel) {
            creditCardRequest.setCcType(WholescriptsCoreUtils.getCardTypeToSAP(paymentInfoModel.getType().getCode().toUpperCase()));
            creditCardRequest.setCcNumber(paymentInfoModel.getNumber());
            final YearMonth yearMonth = YearMonth.of(Integer.parseInt(paymentInfoModel.getValidToYear()),
                    Integer.parseInt(paymentInfoModel.getValidToMonth()));
            final LocalDate validToDate = yearMonth.atEndOfMonth();
            creditCardRequest.setCcValidTo(String.valueOf(validToDate));
            creditCardRequest.setCcName(paymentInfoModel.getCcOwner());
            if(Objects.nonNull(sessionService.getAttribute("networkTransactionID"))){
            	creditCardRequest.setZzNetworkId(sessionService.getAttribute("networkTransactionID"));
            	sessionService.removeAttribute("networkTransactionID");
            }
            
        }

        PaymentTransactionModel authrizedTransaction = null;
        PaymentTransactionEntryModel authrizedTransactionEntry = null;
        if (CollectionUtils.isNotEmpty(cartmodel.getPaymentTransactions())) {
            final List<PaymentTransactionModel> transactions = cartmodel.getPaymentTransactions();
            for (PaymentTransactionModel transaction : transactions) {
                for (PaymentTransactionEntryModel transactionEntry : transaction.getEntries()) {
                    if (transactionEntry.getType() != null
                            && PaymentTransactionType.AUTHORIZATION.getCode().equalsIgnoreCase(transactionEntry.getType().getCode())) {
                        authrizedTransaction = transaction;
                        break;
                    }
                }
                if (authrizedTransaction != null) {
                    break;
                }
            }
        }

        if (authrizedTransaction != null) {

            Optional<PaymentTransactionEntryModel> pteOptional = authrizedTransaction.getEntries().stream()
                    .filter(entry -> entry.getType() != null
                            && entry.getType().getCode().equalsIgnoreCase(PaymentTransactionType.AUTHORIZATION.getCode()))
                    .findAny();

            if (pteOptional.isPresent()) {
                authrizedTransactionEntry = pteOptional.get();
            }

            if (authrizedTransactionEntry != null) {

                if (authrizedTransactionEntry.getCurrency() != null) {
                    creditCardRequest.setCurrency(authrizedTransactionEntry.getCurrency().getIsocode());
                }

                creditCardRequest.setAuthFlag("X");
                if (authrizedTransactionEntry.getAmount() != null) {
                    creditCardRequest.setAuthAmount(authrizedTransactionEntry.getAmount().toString());
                    creditCardRequest.setCcReAmount(authrizedTransactionEntry.getAmount().toString());
                }
                final Date time = authrizedTransactionEntry.getTime();
                if (time != null) {
                    final DateFormat formatter = new SimpleDateFormat(WholescriptscoreConstants.DATE_FORMAT);
                    final String authDateTime = formatter.format(time);
                    creditCardRequest.setAuthDate(authDateTime.split(StringUtils.SPACE)[0]);
                    creditCardRequest.setAuthTime(authDateTime.split(StringUtils.SPACE)[1]);
                }

                final String transactionDetails = authrizedTransactionEntry.getTransactionStatusDetails();
                if (transactionDetails != null) {
                    final String[] transactionDetailsArr = transactionDetails.split(":");
                    if (transactionDetailsArr.length >= 2) {
                        creditCardRequest.setAuthCcNumber(transactionDetailsArr[1]);
                    }
                    if (transactionDetailsArr.length >= 4) {
                        creditCardRequest.setAuthRefNumber(transactionDetailsArr[3]);
                    }
                    if (transactionDetailsArr.length == 6) {
                        creditCardRequest.setMERCHIDCL(transactionDetailsArr[5]);
                    }

                }
                creditCardRequest.setCC_REACT("A");
                if (authrizedTransactionEntry.getTransactionStatus().equalsIgnoreCase("ACCEPTED")) {
                    creditCardRequest.setCC_REACT_T("APPROVAL");
                }
                creditCardRequest.setCcStateEx("C");

            }
        }
        return creditCardRequest;

    }

    @Override
    public ExternalOrderDetailsResponseData getExternalOrderDetails(final B2BCustomerModel b2bCustomer, final String orderNumber, final BaseStoreModel baseStore) throws Exception {

        ExternalOrderDetailsResponseData responseData = new ExternalOrderDetailsResponseData();

        if (Objects.nonNull(b2bCustomer.getDefaultB2BUnit())) {
            final OrderDetailsRequestData requestBody = new OrderDetailsRequestData();
            createOrderDetailsRequest(b2bCustomer, null, null, requestBody, orderNumber, baseStore);
            final ObjectMapper objectMapper = new ObjectMapper();
            final HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(WholescriptsCoreUtils.getSCPIOauthAccessToken(getConsumedDestinationModel()));
            final RestTemplate restTemplate = new RestTemplate();
            final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
            final String orderHistoryDestName = Config.getParameter("order.details.destination.name");
            ReturnResponseData returnData = null;
            ResponseEntity<String> response = null;
            try {
                final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(orderHistoryDestName);
                response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST, requestEntity, String.class);
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
                objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
                objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
                objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));

                returnData = objectMapper.readValue(response.getBody(), ReturnResponseData.class);
                saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2bCustomer, STATUS_INTEGRATION_SUCCESS, orderHistoryDestName);
                responseData = objectMapper.readValue(response.getBody(), ExternalOrderDetailsResponseData.class);

            } catch (Exception ex) {
                saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, orderHistoryDestName);
                throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
            }

            if ("1".equals(returnData.getRETURN())) {
                final ReturnErrorMessageData errorMessage = objectMapper.readValue(response.getBody(), ReturnErrorMessageData.class);
                  throw new RequestParameterException(String.format(errorMessage.getReturnMessage().getItems().stream().filter(i -> i.getType().equals("E")).map(i -> i.getMessage()).collect(Collectors.toList()).toString()),
              RequestParameterException.UNKNOWN_IDENTIFIER);
            }

        }
        return responseData;
    }

    @Override
    public void updateAccertifyFlagInCDC(final B2BCustomerModel customer) throws Exception {
        CMSSiteModel cmsSite = cmsSiteService.getCurrentSite();
        if (Objects.isNull(cmsSite)) {
            cmsSite = (CMSSiteModel) customer.getSite();
        }
        final GigyaConfigModel gigyaConfig = cmsSite.getGigyaConfig();
        if (Objects.nonNull(gigyaConfig)) {
            final ObjectMapper objectMapper = new ObjectMapper();
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_FORM_URLENCODED));
            final RestTemplate restTemplate = new RestTemplate();
            final String accertifyflagDestName = Config.getParameter("cdc.accertify.disabled.flag.destination.name");
            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
            try {
                final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(accertifyflagDestName);
                final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(httpDestination.getTargetURL());
                final StringBuilder data = new StringBuilder();
                data.append("{");
                if (Objects.nonNull(customer.getAccertifyEmailVerified())) {
                    final Date date = customer.getAccertifyEmailVerified();
                    // Convert Date to Instant
                    final Instant instant = date.toInstant();

                    // Format Instant to ISO 8601 string
                    final String isoDate = DateTimeFormatter.ISO_INSTANT.withZone(ZoneOffset.UTC).format(instant);
                    data.append("'AccertifyEmailVerified':");
                    data.append("'" + isoDate + "',");
                }
                data.append("'FailedAccertifyCheck':");
                data.append("'" + customer.getFailedAccertifyCheck().toString() + "'}");
                requestBody.add("userKey", gigyaConfig.getGigyaUserKey());
                requestBody.add("secret", gigyaConfig.getGigyaUserSecret());
                requestBody.add("apiKey", gigyaConfig.getGigyaApiKey());
                requestBody.add("UID", customer.getGyUID());
                requestBody.add("data", data.toString());
                boolean isActive = Boolean.TRUE;
                if (customer.isLoginDisabled()) {
                    isActive = Boolean.FALSE;
                }
                requestBody.add("isActive", String.valueOf(isActive));
                final HttpEntity requestEntity = new HttpEntity<>(requestBody, headers);
                final ResponseEntity<String> response = restTemplate.exchange(builder.build(true).encode().toUri(), HttpMethod.POST,
                        requestEntity, String.class);
                saveIntegrationModel(requestBody.toString(), objectMapper, response.getBody(), customer, STATUS_INTEGRATION_SUCCESS, accertifyflagDestName);
            } catch (Exception ex) {
                saveIntegrationModel(requestBody.toString(), objectMapper, ex.getMessage(), customer, STATUS_INTEGRATION_FAIL, accertifyflagDestName);
                throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
            }

        }
    }  
    
    @Override
 	public OrderSimulateResponseData orderSimulateORCreate(final CartModel cartmodel, final boolean orderSimulate) throws Exception
 	{
 		SAPConfigurationModel configModel = configModel = cartmodel.getStore().getSAPConfiguration();
 		OrderSimulateResponseData responseData = new OrderSimulateResponseData();
 		if (Objects.nonNull(configModel))
 		{
 			final CreateOrderInSAPRequestData requestBody = createRequestForOrderCreation((B2BCustomerModel) cartmodel.getUser(),
 					cartmodel, configModel,orderSimulate);
 			final ObjectMapper objectMapper = new ObjectMapper();
 			final HttpHeaders headers = new HttpHeaders();
 			headers.setContentType(MediaType.APPLICATION_JSON);
 			headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
 			headers.setBearerAuth(WholescriptsCoreUtils.getSCPIOauthAccessToken(getConsumedDestinationModel()));
 			final RestTemplate restTemplate = new RestTemplate();
 			final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
 			final String orderDestName = Config.getParameter("wholescripts.simulate.create.order.destination.name");
 			ResponseEntity<String> response = null;
 			try
 			{
 				final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(orderDestName);
 				LOG.info("Order create request for cart:" + cartmodel.getCode() + requestEntity.getBody());
 				response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST, requestEntity, String.class);
 				objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
 				objectMapper
 						.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
 				objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
 				 objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
 				 responseData = objectMapper.readValue(response.getBody(), OrderSimulateResponseData.class);
 				LOG.info("Order create response for cart: " + cartmodel.getCode() + objectMapper.writeValueAsString(responseData));
 				saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), cartmodel.getUser(),
 						STATUS_INTEGRATION_SUCCESS, orderDestName);
 				if ("0".equals(responseData.getReturns()))
 				{
 					responseData = objectMapper.readValue(response.getBody(), OrderSimulateResponseData.class);
 					responseData.setSuccess(true);
 					return responseData;
 				}
 			}
 			catch (final Exception ex)
 			{
 				saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), (B2BCustomerModel) cartmodel.getUser(), STATUS_INTEGRATION_FAIL,
 						orderDestName);
 				responseData.setSuccess(false);
 			}
 			if(Objects.nonNull(response)) {
 				final ReturnErrorMessageData errorMessage = objectMapper.readValue(response.getBody(), ReturnErrorMessageData.class);
 				List<String> numberList = errorMessage.getReturnMessage().getItems().stream().filter(i -> i.getType().equals("E")).map(i -> i.getNumber()).collect(Collectors.toList());
 				if(numberList.contains("034")) {
 					responseData.setDisplayZero(true);         
 				}
 				saveIntegrationModel(requestEntity.getBody(), objectMapper, String.format(errorMessage.getReturnMessage().getItems().stream().filter(i -> i.getType().equals("E")).map(i -> i.getMessage()).collect(Collectors.toList()).toString()), (B2BCustomerModel) cartmodel.getUser(), STATUS_INTEGRATION_FAIL,
 						orderDestName); 
 				responseData.setSuccess(false);	
 			}
 		
 		}
		return responseData;
 	}

    @Override
    public OrderHistoryResponseData getOrderHistoryWebErrors() throws Exception {
        OrderHistoryRequestData requestBody = new OrderHistoryRequestData();
        final SAPConfigurationModel configModel = getSapConfiguration();
        requestBody.setDIST_CHANNEL(configModel.getSapcommon_distributionChannel());
        requestBody.setSALES_ORG(configModel.getSapcommon_salesOrganization());
        requestBody.setDIVISION(configModel.getSapcommon_division());
        requestBody.setORDER_DATE("");
        B2BCustomerModel b2bCustomer = b2bCustomerService.getCurrentB2BCustomer();
        if(Objects.nonNull(b2bCustomer) && Objects.nonNull(b2bCustomer.getDefaultB2BUnit())){
            String formattedb2b = WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(b2bCustomer.getDefaultB2BUnit().getUid());
            requestBody.setSOLD_TO(formattedb2b);
        }else{
            requestBody.setSOLD_TO("");
        }

        requestBody.setERRORS("");
        requestBody.setORDER_NUMBER("");
        OrderItem orderItem = new OrderItem();
        orderItem.setORDER_DATE("");
        orderItem.setVBELN("");
        requestBody.setT_ORDERS(orderItem);
        final ObjectMapper objectMapper = new ObjectMapper();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(WholescriptsCoreUtils.getSCPIOauthAccessToken(getConsumedDestinationModel()));
        final RestTemplate restTemplate = new RestTemplate();

        final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
        final String destinationUrlName = Config.getParameter("order.history.web.error");

        try {
            final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(destinationUrlName);
            final ResponseEntity<String> response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST,
                    requestEntity, String.class);
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
            objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            final OrderHistoryResponseData responseData = objectMapper.readValue(response.getBody(), OrderHistoryResponseData.class);
            saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(),b2bCustomer ,STATUS_INTEGRATION_SUCCESS, destinationUrlName);
            return responseData;
        } catch (Exception ex) {
            saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2bCustomer, STATUS_INTEGRATION_FAIL, destinationUrlName);
            throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
        }
    }

    @Override
    public DashboardResponseData getDashBoardDetailsFromSAP(final B2BCustomerModel b2BCustomerModel) throws Exception{


        final SAPConfigurationModel configModel = getSapConfiguration();
        if (Objects.nonNull(configModel)) {
            final DashboardCustomerConfigRequestData requestBody = createRequestForGetDashBoardDetailsFromSAP(b2BCustomerModel,configModel);

            final ObjectMapper objectMapper = new ObjectMapper();
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(WholescriptsCoreUtils.getSCPIOauthAccessToken(getConsumedDestinationModel()));
            final RestTemplate restTemplate = new RestTemplate();
            final HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(requestBody), headers);
            final String addressDestName = Config.getParameter("wholescripts.get.dashboard.destination.name");
            ResponseEntity<String> response = null;
            try {
                final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(addressDestName);
                response = restTemplate.exchange(httpDestination.getTargetURL(), HttpMethod.POST, requestEntity, String.class);
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
                objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
                objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
                objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);

                DashboardResponseData responseData = objectMapper.readValue(response.getBody(),
                        DashboardResponseData.class);
                saveIntegrationModel(requestEntity.getBody(), objectMapper, response.getBody(), b2BCustomerModel, STATUS_INTEGRATION_SUCCESS, addressDestName);
                return responseData;
            } catch (Exception ex) {
                saveIntegrationModel(requestEntity.getBody(), objectMapper, ex.getMessage(), b2BCustomerModel, STATUS_INTEGRATION_FAIL, addressDestName);
                throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
            }
        }
        throw new RequestParameterException(String.format("Sap configuartion model is not found"),
                RequestParameterException.MISSING);

    }

    @Override
    public ResponseEntity<String> updateBrandAgreementConsentInCDC(B2BCustomerModel customer) throws Exception {
        CMSSiteModel cmsSite = cmsSiteService.getCurrentSite();
        if (Objects.isNull(cmsSite)) {
            cmsSite = (CMSSiteModel) customer.getSite();
        }
        final GigyaConfigModel gigyaConfig = cmsSite.getGigyaConfig();
        if (Objects.nonNull(gigyaConfig)) {
            final ObjectMapper objectMapper = new ObjectMapper();
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_FORM_URLENCODED));
            final RestTemplate restTemplate = new RestTemplate();
            final String updateBrandAgreementDestName = Config.getParameter("cdc.accertify.disabled.flag.destination.name");
            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
            try {
                final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(updateBrandAgreementDestName);
                final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(httpDestination.getTargetURL());
                List<String> brandNames = new ArrayList<>();
                JSONArray jsonArray = null;
                if (Objects.nonNull(customer) && Objects.nonNull(customer.getDefaultB2BUnit())) {
                    B2BUnitModel b2bUnit = customer.getDefaultB2BUnit();
                    if (CollectionUtils.isNotEmpty(b2bUnit.getBrandAgreements())) {
                        brandNames =  b2bUnit.getBrandAgreements().stream()
                                .map(BrandAgreementModel::getBrandName)
                                .collect(Collectors.toSet())
                                .stream()
                                .toList();
                        if (CollectionUtils.isNotEmpty(brandNames)) {
                            jsonArray = new JSONArray(brandNames);

                        }

                    }

                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("BrandAgreementsSigned", jsonArray);
                requestBody.add("userKey", gigyaConfig.getGigyaUserKey());
                requestBody.add("secret", gigyaConfig.getGigyaUserSecret());
                requestBody.add("apiKey", gigyaConfig.getGigyaApiKey());
                requestBody.add("UID", customer.getGyUID());
                requestBody.add("data", jsonObject.toString());
                final HttpEntity requestEntity = new HttpEntity<>(requestBody, headers);
                final ResponseEntity<String> response = restTemplate.exchange(builder.build(true).encode().toUri(), HttpMethod.POST,
                        requestEntity, String.class);
                saveIntegrationModel(requestBody.toString(), objectMapper, response.getBody(), customer, STATUS_INTEGRATION_SUCCESS, updateBrandAgreementDestName);
                return response;
            } catch (Exception ex) {
                saveIntegrationModel(requestBody.toString(), objectMapper, ex.getMessage(), customer, STATUS_INTEGRATION_FAIL, updateBrandAgreementDestName);
                throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
            }

        }
        throw new RequestParameterException(RequestParameterException.UNKNOWN_IDENTIFIER);
    }

    private DashboardCustomerConfigRequestData createRequestForGetDashBoardDetailsFromSAP(final B2BCustomerModel b2bCustomer, final SAPConfigurationModel configModel) {
        final DashboardCustomerConfigRequestData requestBody = new DashboardCustomerConfigRequestData();

        if (Objects.nonNull(b2bCustomer.getDefaultB2BUnit())) {
            requestBody
                    .setKUNNR(WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(b2bCustomer.getDefaultB2BUnit().getUid()));
        }

        requestBody.setVKORG(configModel.getSapcommon_salesOrganization());
        requestBody.setVTWEG(configModel.getSapcommon_distributionChannel());
        requestBody.setSPART(configModel.getSapcommon_division());
        requestBody.setENTRIES("10");
        requestBody.setPRA_MOST_ORDERED("X");
        requestBody.setPRA_MO_DAYS("9999");
        requestBody.setPRA_LATEST_ORDERS("X");
        requestBody.setPRA_LO_DAYS("9999");
        requestBody.setNEW_PATIENTS("X");
        requestBody.setNP_DAYS("9999");
        requestBody.setTOP_PATIENTS("X");
        requestBody.setTP_DAYS("9999");
        requestBody.setPA_MOST_ORDERED("X");
        requestBody.setPA_MO_DAYS("9999");
        requestBody.setPA_LATEST_ORDERS("X");
        requestBody.setPA_LO_DAYS("9999");

        return requestBody;
    }
    
    private CreditCardRequestData createACHPaymentdetails(final CartModel cartmodel) {

       final CreditCardRequestData creditCardRequest = new CreditCardRequestData();
       final ACHCardPaymentInfoModel  achPaymentInfoModel = cartmodel.getAchCardPaymentInfo();
       if (Objects.nonNull(achPaymentInfoModel)) {
      	  
           creditCardRequest.setCcType(achPaymentInfoModel.getCcType());
           creditCardRequest.setCcNumber(achPaymentInfoModel.getCcNumber());
           creditCardRequest.setCcValidTo(achPaymentInfoModel.getCcValidity());
           creditCardRequest.setCcName(achPaymentInfoModel.getCcName());
           creditCardRequest.setBillAmount("0.0");
           creditCardRequest.setAuthFlag("X");
           creditCardRequest.setAuthAmount(achPaymentInfoModel.getAuthAmount().toString());
           creditCardRequest.setCurrency(achPaymentInfoModel.getCurrency());
           creditCardRequest.setAuthDate(achPaymentInfoModel.getAuthDate());
           creditCardRequest.setAuthTime(achPaymentInfoModel.getAuthTime());
           creditCardRequest.setAuthCcNumber(achPaymentInfoModel.getAuthCCNumber());
           creditCardRequest.setAuthRefNumber(achPaymentInfoModel.getAuthRefNumber());
           creditCardRequest.setCC_REACT("A");
           creditCardRequest.setCcReAmount(achPaymentInfoModel.getCcReAuthAmount().toString());
           creditCardRequest.setGlAccount(StringUtils.EMPTY);
           creditCardRequest.setCcStateEx("C");
           creditCardRequest.setCC_REACT_T(achPaymentInfoModel.getCcAuthResponse());
           creditCardRequest.setMERCHIDCL("WSCR_ZACH_USD");
           creditCardRequest.setPreAuth(StringUtils.EMPTY);
           creditCardRequest.setAuthType(StringUtils.EMPTY);
           creditCardRequest.setDataOrigin("E");
           creditCardRequest.setRADRCHECK1(StringUtils.EMPTY);;
       }
      
       return creditCardRequest;

   }

   @Override
   public void updateAccountInfo(final B2BCustomerModel customer) throws Exception {
       CMSSiteModel cmsSite = cmsSiteService.getCurrentSite();
       if (Objects.isNull(cmsSite)) {
           cmsSite = (CMSSiteModel) customer.getSite();
       }
       final GigyaConfigModel gigyaConfig = cmsSite.getGigyaConfig();
       if (Objects.nonNull(gigyaConfig)) {
           final ObjectMapper objectMapper = new ObjectMapper();
           final HttpHeaders headers = new HttpHeaders();
           headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
           headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_FORM_URLENCODED));
           final RestTemplate restTemplate = new RestTemplate();
           final String accertifyflagDestName = Config.getParameter("cdc.accertify.disabled.flag.destination.name");
           MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
           try {
               final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(accertifyflagDestName);
               final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(httpDestination.getTargetURL());
               requestBody.add("userKey", gigyaConfig.getGigyaUserKey());
               requestBody.add("secret", gigyaConfig.getGigyaUserSecret());
               requestBody.add("apiKey", gigyaConfig.getGigyaApiKey());
               requestBody.add("UID", customer.getGyUID());
               JSONObject preferences = new JSONObject();
               JSONObject textMessage = new JSONObject();
               textMessage.put("isConsentGranted", false);
               preferences.put("TextMessage", textMessage);
               requestBody.add("preferences", preferences.toString());

               final HttpEntity requestEntity = new HttpEntity<>(requestBody, headers);
               final ResponseEntity<String> response = restTemplate.exchange(builder.build(true).encode().toUri(), HttpMethod.POST,
                       requestEntity, String.class);
               saveIntegrationModel(requestBody.toString(), objectMapper, response.getBody(), customer, STATUS_INTEGRATION_SUCCESS, accertifyflagDestName);
           } catch (Exception ex) {
               saveIntegrationModel(requestBody.toString(), objectMapper, ex.getMessage(), customer, STATUS_INTEGRATION_FAIL, accertifyflagDestName);
               throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
           }
       }
    }
   
   @Override
	public boolean validateLoginDetailsInCDC(String password, String loginId,AccertifyVerificationRequestData request) throws Exception
	{
		final CMSSiteModel cmsSite = cmsSiteService.getCurrentSite();
		final GigyaConfigModel gigyaConfig = cmsSite.getGigyaConfig();
		if (Objects.nonNull(gigyaConfig))
		{
			final ObjectMapper objectMapper = new ObjectMapper();
			final HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_FORM_URLENCODED));
			final RestTemplate restTemplate = new RestTemplate();
			final String cdcLoginDestName = Config.getParameter("cdc.login.destination.name");
			final MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
			try
			{
				final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(cdcLoginDestName);
				final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(httpDestination.getTargetURL());
				requestBody.add("userKey", gigyaConfig.getGigyaUserKey());
				requestBody.add("secret", gigyaConfig.getGigyaUserSecret());
				requestBody.add("apiKey", gigyaConfig.getGigyaApiKey());
				requestBody.add("loginID", loginId);
				requestBody.add("password", password);
				requestBody.add("include", configurationService.getConfiguration().getString("cdc.include.login.details"));
				final HttpEntity requestEntity = new HttpEntity<>(requestBody, headers);
				final ResponseEntity<String> response = restTemplate.exchange(builder.build(true).encode().toUri(), HttpMethod.POST,
						requestEntity, String.class);
				saveIntegrationModel(requestBody.toString(), objectMapper, response.getBody(), null, STATUS_INTEGRATION_SUCCESS,
						cdcLoginDestName);
				 JSONObject obj = new JSONObject(response.getBody());
		        String statusReason = obj.getString("statusReason");
		        if(statusReason.equalsIgnoreCase("OK")) {
		      	  try {
		      		  ObjectMapper mapper = new ObjectMapper();
			           JsonNode root = mapper.readTree(response.getBody());
			           String businessPartnerId = root.path("data").path("BusinessPartnerID").asText();
			           request.setAccountID(businessPartnerId);	
			           String firstName = root.path("profile").path("firstName").asText();
			           request.setFirstName(firstName);
			           String lastName = root.path("profile").path("lastName").asText();
			           request.setLastName(lastName);
		      	  }catch (Exception e) {
						LOG.error(e);
					}
		      	  return true;
		        }
			}
			catch (final Exception ex)
			{
				saveIntegrationModel(requestBody.toString(), objectMapper, ex.getMessage(), null, STATUS_INTEGRATION_FAIL,
						cdcLoginDestName);
				throw new RequestParameterException(String.format(ex.getMessage()), RequestParameterException.UNKNOWN_IDENTIFIER);
			}

		}
		return false;
	}

	@Override
	public PaymetricResponseData getCardDetails(String accessToken) throws Exception
	{
		try {
			// Generate HMAC signature
			final ObjectMapper objectMapper = new ObjectMapper();
			Mac hmac = Mac.getInstance("HmacSHA256");
			SecretKeySpec secretKeySpec = new SecretKeySpec(configurationService.getConfiguration().getString("shared.key").getBytes(), "HmacSHA256");
			hmac.init(secretKeySpec);
			byte[] hmacBytes = hmac.doFinal(accessToken.getBytes());
			String signature = Base64.getEncoder().encodeToString(hmacBytes);
			// Construct the URL
			String query = String.format("MerchantGUID=%s&Signature=%s&AccessToken=%s",
					configurationService.getConfiguration().getString("merchant.guid"), signature, accessToken);
			final String cvvCardDetails = Config.getParameter("cvv.card.details.destination.name");
			final SAPHTTPDestinationModel httpDestination = getHttpDestinationModel(cvvCardDetails);
			String fullUrl = httpDestination.getTargetURL() + query;
			// Make the HTTPS request
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			HttpEntity<String> entity = new HttpEntity<>(headers);
			ResponseEntity<String> response = restTemplate.exchange(fullUrl, HttpMethod.GET, entity, String.class);
			// Parse the XML response
			String xmlResponse = response.getBody();
			JSONObject jsonResponse = XML.toJSONObject(xmlResponse);
			 saveIntegrationModel(fullUrl, objectMapper, jsonResponse.toString(), null, STATUS_INTEGRATION_SUCCESS, cvvCardDetails);
			 objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			 objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
			 objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
			 objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
			 return objectMapper.readValue(jsonResponse.toString(), PaymetricResponseData.class);
		} catch (Exception e) {
			throw new RequestParameterException(String.format(e.getMessage()), RequestParameterException.INVALID);
		}
	}
}


