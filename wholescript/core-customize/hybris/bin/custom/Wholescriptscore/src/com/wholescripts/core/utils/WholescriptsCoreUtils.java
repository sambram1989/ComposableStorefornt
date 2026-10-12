/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.utils;

import de.hybris.platform.apiregistryservices.model.BasicCredentialModel;
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel;
import de.hybris.platform.apiregistryservices.model.ConsumedOAuthCredentialModel;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.core.enums.CreditCardType;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.util.Config;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.google.common.collect.Lists;
import com.wholescripts.core.enums.SapCustomerGroupEnum;
import com.wholescripts.data.OauthToken;

/**
 *
 */
public class WholescriptsCoreUtils
{
	 private static String DOCTOR_GROUP = "wholescripts.doctor";
	 private static final String ENCODE_UTF8 = StandardCharsets.UTF_8.name().intern();
	 private static final String SAP_CUSTOMER_GROUP = "external.api.customer.groups";
	 
	 @Resource(name = "configurationService")
    private ConfigurationService configurationService;
	
    public static boolean isWholescriptsDoctor(B2BCustomerModel customer)
    {
   	    String docGroup =Config.getParameter(DOCTOR_GROUP);
          Optional<PrincipalGroupModel> group = customer.getGroups().stream().filter(g -> docGroup.equals(g.getUid())).findAny();
          return group.isPresent();
    }
    
    public static String getSCPIOauthAccessToken(ConsumedDestinationModel destination) throws Exception
    {
   	 ConsumedOAuthCredentialModel oauth = (ConsumedOAuthCredentialModel) destination.getCredential();
   	 final HttpHeaders headers = new HttpHeaders();
   	 headers.setAccept(Lists.newArrayList(MediaType.ALL));
   	 headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
   	 final HttpEntity httpEntity = new HttpEntity<>(headers);
   	 final ObjectMapper mapper = new ObjectMapper();
   	 final RestTemplate restTemplate = new RestTemplate();
   	 final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(oauth.getOAuthUrl());
   	 builder.queryParam("grant_type", URLEncoder.encode("client_credentials", ENCODE_UTF8));
   	 builder.queryParam("client_id",URLEncoder.encode(oauth.getClientId()), "UTF-8");
   	 builder.queryParam("client_secret",URLEncoder.encode(oauth.getClientSecret()), "UTF-8");
   	 builder.queryParam("refresh_token",URLEncoder.encode(oauth.getOAuthUrl()), ENCODE_UTF8);
   	 restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName(ENCODE_UTF8)));
   	 final ResponseEntity<String> response = restTemplate.exchange(builder.build(true).encode().toUri(), HttpMethod.POST,httpEntity, String.class);
   	 mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
   	 mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
   	 final OauthToken token = mapper.readValue(response.getBody(), OauthToken.class);
   	 return token.getAccessToken();
    }
    
    public static String getUPSAccessToken(ConsumedDestinationModel destination) throws Exception
    {  	 
   	 BasicCredentialModel basic = (BasicCredentialModel) destination.getCredential();
   	 String auth = basic.getUsername() + ":" + basic.getPassword();
   	 String basicAuth = "Basic" + " " + Base64.getEncoder().encodeToString(auth.getBytes());
   	 final HttpHeaders headers = new HttpHeaders();
   	 headers.setAccept(Lists.newArrayList(MediaType.ALL));
   	 headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
   	 headers.set("Authorization", basicAuth);
   	 headers.set("x-merchant-id", "31657X");
   	 MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
   	 requestBody.add("grant_type", "client_credentials");
   	 final HttpEntity httpEntity = new HttpEntity<>(requestBody,headers);
   	 final ObjectMapper mapper = new ObjectMapper();
   	 final RestTemplate restTemplate = new RestTemplate();
   	 final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(destination.getUrl());
   	 restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName(ENCODE_UTF8)));
   	 final ResponseEntity<String> response = restTemplate.exchange(builder.build(true).encode().toUri(), HttpMethod.POST,httpEntity, String.class);
   	 mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
   	 mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
   	 final OauthToken token = mapper.readValue(response.getBody(), OauthToken.class);
   	 return token.getAccessToken();
    }
    
    public static String getAccountNumberForDefaultB2bUnit(String b2bUnit) {
   	 if(b2bUnit.contains("_")) {
   		 String unit[] = b2bUnit.split("_");
   		 return String.format("%010d",Integer.valueOf(unit[0]));
   	 }
   	 return b2bUnit;
    }

	public static String getAccountNumberWithSalesArea(String b2bUnit) {
		return b2bUnit.concat("_1710_10_10");
	}

	public static String getAccountNumberForDefaultB2bUnitWithoutFormat(String b2bUnit) {
		if(b2bUnit.contains("_")) {
			String unit[] = b2bUnit.split("_");
			return unit[0];
		}
		return b2bUnit;
	}
	public static String getPaymetricCardTypeForAuth(CreditCardType type)
	{
        return switch (type.getCode().toLowerCase()) {
            case "visa" -> "vi";
            case "amex" -> "ax";
            case "discover" -> "di";
            case "master" -> "mc";
            default -> "";
        };
    }
	public static CreditCardType getCardTypeForSAP(String type) {
		if (type.equals("VISA")) {
			return CreditCardType.valueOf("VISA");
		} else if (type.equals("AMEX")) {
			return CreditCardType.valueOf("AMEX");
		} else if (type.equals("DISC")) {
			return CreditCardType.valueOf("DISCOVER");
		} else if (type.equals("MC")) {
			return CreditCardType.valueOf("MASTER");
		}
        return null;
    }
	public static String getCardTypeToSAP(String type) {
		if (type.equals("VISA")) {
			return "VISA";
		} else if (type.equals("AMEX")) {
			return "AMEX";
		} else if (type.equals("DISCOVER")) {
			return "DISC";
		} else if (type.equals("MASTER")) {
			return "MC";
		}
		return null;
	}
	public static String getShipToOrPayerForDefaultB2bUnit(String number) {

		if(StringUtil.isNotBlank(number))
		{
			return String.format("%010d",Integer.valueOf(number));

		}
        return StringUtils.EMPTY;
    }
	public static String getMaskedCardNumber(String cardNumber) {
		String[] cCardNumber = cardNumber.split("-");

		return "************" + cCardNumber[2];
	}

	public static String getDefaultB2bUnit(String salesPerson) {
			return String.format("%010d",Integer.valueOf(salesPerson));

	}
	public static StockLevelStatus checkStatus(StockLevelModel stockLevel) {
		StockLevelStatus resultStatus = StockLevelStatus.OUTOFSTOCK;
		if(Objects.isNull(stockLevel)) {
			return resultStatus;
		} else {
			if(Objects.nonNull(stockLevel.getInStockStatus()) && !InStockStatus.NOTSPECIFIED.equals(stockLevel.getInStockStatus()))
			{
				boolean isInStockStatusLevel =  InStockStatus.FORCEINSTOCK.equals(stockLevel.getInStockStatus());
				if (isInStockStatusLevel) {
					resultStatus = StockLevelStatus.INSTOCK;
				}

			}

			else {
				if(stockLevel.getAvailable() > stockLevel.getReserved())
				{
					resultStatus = StockLevelStatus.INSTOCK;
				}

			}

			return resultStatus;
		}
	}

	public static String getFormattedDefaultB2bUnit(String unitId) {
		if(unitId.contains("_")) {
			String unit[] = unitId.split("_");
			return String.format("%010d",Integer.valueOf(unit[0]));
		}
		return String.format("%010d",Integer.valueOf(unitId));


	}
	
	public static boolean isValidSAPCustomerGroup(final B2BUnitModel b2bUnitModel) {
		
		final String customerGroups = Config.getParameter(SAP_CUSTOMER_GROUP);
		final String[] customerGroupsArray = StringUtils.split(customerGroups, ",");
      final List<String> customerGroupsList = Arrays.stream(customerGroupsArray).toList();
      final SapCustomerGroupEnum customerGroup = b2bUnitModel.getSapCustomerGroup();
      boolean isValidSAPCustomerGroup = false;
      if (customerGroup != null && customerGroupsList.contains(customerGroup.getCode())) {
      	isValidSAPCustomerGroup =true;
      }
      return isValidSAPCustomerGroup;
	}
	
	public static String wrapWithCDataForTags(String xml, String... tagsToProtect) {
	    for (String tag : tagsToProtect) {
	        // Matches: <Zip5>08618</Zip5> → <Zip5><![CDATA[08618]]></Zip5>
	        xml = xml.replaceAll(
	            "<" + tag + ">(\\d+)</" + tag + ">",
	            "<" + tag + "><![CDATA[$1]]></" + tag + ">"
	        );
	    }
	    return xml;
	}

}
