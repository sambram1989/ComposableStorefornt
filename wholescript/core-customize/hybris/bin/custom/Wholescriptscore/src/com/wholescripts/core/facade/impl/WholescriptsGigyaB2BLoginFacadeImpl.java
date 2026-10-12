/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.facade.impl;

import de.hybris.platform.b2b.company.B2BCommerceB2BUserGroupService;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.gigya.gigyab2bfacades.login.impl.DefaultGigyaB2BLoginFacade;
import de.hybris.platform.gigya.gigyab2bservices.data.GigyaGroupsData;
import de.hybris.platform.gigya.gigyab2bservices.data.GigyaOrganisationData;
import de.hybris.platform.gigya.gigyaservices.api.exception.GigyaApiException;
import de.hybris.platform.gigya.gigyaservices.data.GigyaJsOnLoginInfo;
import de.hybris.platform.gigya.gigyaservices.data.GigyaUserObject;
import de.hybris.platform.gigya.gigyaservices.login.GigyaLoginService;
import de.hybris.platform.gigya.gigyaservices.model.GigyaConfigModel;
import de.hybris.platform.sap.core.configuration.model.SAPConfigurationModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gigya.socialize.GSKeyNotFoundException;
import com.gigya.socialize.GSResponse;
import com.wholescripts.core.enums.CustomerProfileType;


/**
 *
 */
public class WholescriptsGigyaB2BLoginFacadeImpl extends DefaultGigyaB2BLoginFacade
{
	private static final Logger LOG = LoggerFactory.getLogger(WholescriptsGigyaB2BLoginFacadeImpl.class);
	private static final String ERROR = "Error: ";
	@Resource(name = "b2bUnitService")
	private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;
	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;
	@Resource(name = "b2bCommerceB2BUserGroupService")
	private B2BCommerceB2BUserGroupService b2bCommerceB2BUserGroupService;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = "gigyaLoginService")
	private GigyaLoginService gigyaLoginService;
	@Resource(name = "enumerationService")
	private EnumerationService enumerationService;

	@Override
	public boolean processGigyaLogin(final GigyaJsOnLoginInfo jsInfo, final GigyaConfigModel gigyaConfig)
	{
		try
		{
			final ObjectMapper objectMapper = new ObjectMapper();
			final ObjectMapper objectMapper1 = new ObjectMapper();
			try
			{
				LOG.info("GigyaJsOnLoginInfo::::::::::::" + objectMapper.writeValueAsString(jsInfo));
				LOG.info("GigyaConfigModel::::::::::::" + objectMapper1.writeValueAsString(gigyaConfig));
			}
			catch (final JsonProcessingException e)
			{
			}
			final Boolean isSiteGlobal = gigyaConfig.getIsSiteGlobal();
			final String idToken = jsInfo.getIdToken();

			Boolean isCallVerified = false;

			if (BooleanUtils.isTrue(isSiteGlobal) && StringUtils.isBlank(idToken))
			{
				LOG.error("Global Access ID Token is mandatory for a global site login verification.");
				return false;
			}
			else if (StringUtils.isNotBlank(idToken))
			{
				isCallVerified = gigyaLoginService.verifyGigyaCallIdTokenExpiryAndSignature(gigyaConfig, idToken)
						&& gigyaLoginService.verifyGigyaIdTokenContainsUID(idToken, jsInfo.getUID());
			}
			else
			{
				isCallVerified = gigyaLoginService.verifyGigyaCall(gigyaConfig, jsInfo.getUID(), jsInfo.getUIDSignature(),
						jsInfo.getSignatureTimestamp());
			}

			if (Boolean.TRUE.equals(isCallVerified))
			{
				final UserModel gigyaUser = gigyaLoginService.findCustomerByGigyaUid(jsInfo.getUID());
				if (gigyaUser != null)
				{
					updateUser(gigyaConfig, gigyaUser);
				}
				else
				{
					// On creation all customer data from gigya is synched
					createNewCustomer(gigyaConfig, jsInfo.getUID());
				}
			}
			return isCallVerified;

		}
		catch (final DuplicateUidException e)
		{
			try
			{
				LOG.error("Error duplicate ID found for gigyaUid: {}", jsInfo.getUID(), e);
				gigyaLoginService.notifyGigyaOfLogout(gigyaConfig, jsInfo.getUID());
			}
			catch (final GigyaApiException e1)
			{
				LOG.error(ERROR, e1);
			}
			return false;
		}
		catch (final Exception e)
		{
			try
			{
				LOG.error(ERROR, e);
				gigyaLoginService.notifyGigyaOfLogout(gigyaConfig, jsInfo.getUID());
			}
			catch (final GigyaApiException e1)
			{
				LOG.error(ERROR, e1);
			}
			return false;
		}
	}

	@Override
	public void updateUser(final GigyaConfigModel gigyaConfig, final UserModel user) throws GSKeyNotFoundException
	{
		// Update mandatory info i.e. UID, name and then update based on mappings
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

		if (user instanceof B2BCustomerModel)
		{
			final B2BCustomerModel gigyaUser = (B2BCustomerModel) user;
			if (StringUtils.isEmpty(gigyaUser.getCustomerID()))
			{
				gigyaUser.setCustomerID(UUID.randomUUID().toString());
			}
			
			final GSResponse accountInfo = getAccountInfo(gigyaConfig, mapper, gigyaUser);
			final ObjectMapper objectMapper = new ObjectMapper();
			final ObjectMapper objectMapper1 = new ObjectMapper();
			final ObjectMapper objectMapper2 = new ObjectMapper();
			try
			{
				LOG.info("accountInfo::::::::::::" + objectMapper.writeValueAsString(accountInfo));
				LOG.info("GigyaConfigModel::::::::::::updateUser" + objectMapper1.writeValueAsString(gigyaConfig));
				LOG.info("gigyaUser::::::::::::updateUser" + objectMapper2.writeValueAsString(gigyaUser));
			}
			catch (final JsonProcessingException e)
			{
			}
			updateBasicInformation(gigyaUser, accountInfo, gigyaConfig);
			setLastLoginForUser(gigyaUser);
			//updateB2BInfo(gigyaConfig, gigyaUser, accountInfo);

			getModelService().save(gigyaUser);

			getGigyaConsentFacade().synchronizeConsents(getPreferenceData(accountInfo), gigyaUser);
			scheduleDataSyncFromCDCToCommerce(gigyaUser);
		}
	}

	private void setLastLoginForUser(final CustomerModel gigyaUser) {
		LocalDate locDate = LocalDate.now();
		Calendar calendar = new GregorianCalendar();
		calendar.set(locDate.getYear(), locDate.getMonthValue() - 1, locDate.getDayOfMonth());
		Date lastLogin = calendar.getTime();
		gigyaUser.setGyLastLoginTimestamp(calendar.getTimeInMillis());
		gigyaUser.setLastLogin(lastLogin);
	}
	
	private void updateB2BInfo(final GigyaConfigModel gigyaConfig, final CustomerModel gigyaUser, final GSResponse accountInfo)
	{
		if (gigyaUser instanceof B2BCustomerModel)
		{
			final GigyaGroupsData groupsData = getGroupsData(accountInfo);
			if (groupsData != null && CollectionUtils.isNotEmpty(groupsData.getOrganizations()))
			{
				assignB2BUnits(groupsData, (B2BCustomerModel) gigyaUser, gigyaConfig);
			}

			getGigyaAuthServiceForGigyaConfig(gigyaConfig).removeAuthorisationsOfCustomer(gigyaUser);
			getGigyaAuthServiceForGigyaConfig(gigyaConfig).assignAuthorisationsToCustomer(gigyaUser);
		}
	}

	private void assignB2BUnits(final GigyaGroupsData groupsData, final B2BCustomerModel b2bCustomer,
			final GigyaConfigModel gigyaConfig)
	{
		if (groupsData != null && CollectionUtils.isNotEmpty(groupsData.getOrganizations()))
		{
			final Set<PrincipalGroupModel> organizations = new HashSet<>();
			groupsData.getOrganizations().forEach(item -> {

				if (gigyaConfig.isCreateOrganizationOnLogin())
				{

					final B2BUnitModel b2bUnit = createOrFetchB2BUnit(gigyaConfig, item);
					b2bUnitService.addMember(b2bUnit, b2bCustomer);
					organizations.add(b2bUnit);
				}
				else
				{
					final B2BUnitModel b2bUnit = b2bUnitService.getUnitForUid(item.getBpid());
					setGigyaAttributesInB2BUnit(gigyaConfig, item, b2bUnit);

					b2bUnitService.addMember(b2bUnit, b2bCustomer);
					organizations.add(b2bUnit);
				}
			});
			b2bCustomer.setDefaultB2BUnit((B2BUnitModel) organizations.iterator().next());
		}
	}

	private B2BUnitModel createOrFetchB2BUnit(final GigyaConfigModel gigyaConfig, final GigyaOrganisationData item)
	{
		B2BUnitModel b2bUnit = b2bUnitService.getUnitForUid(item.getBpid());
		if (b2bUnit == null)
		{
			b2bUnit = getModelService().create(B2BUnitModel.class);
			b2bUnit.setUid(item.getBpid());
			b2bUnit.setName(item.getOrgName());
			b2bUnit.setGyUID(item.getOrgId());
			b2bUnit.setGyApiKey(gigyaConfig.getGigyaApiKey());
			getModelService().save(b2bUnit);
		}
		return b2bUnit;
	}

	private void setGigyaAttributesInB2BUnit(final GigyaConfigModel gigyaConfig, final GigyaOrganisationData item,
			final B2BUnitModel b2bUnit)
	{
		b2bUnit.setGyUID(item.getOrgId());
		b2bUnit.setGyApiKey(gigyaConfig.getGigyaApiKey());
		getModelService().save(b2bUnit);
	}

	@Override
	public UserModel createNewCustomer(final GigyaConfigModel gigyaConfig, final String uid) throws DuplicateUidException
	{
		LOG.info("Strat Customer creation..............WholescriptsGigyaB2BLoginFacadeImpl");
		LOG.info("creating gigyaUserObject..............WholescriptsGigyaB2BLoginFacadeImpl");
		final GigyaUserObject gigyaUserObject = getGigyaLoginService().fetchGigyaInfo(gigyaConfig, uid);
		final ObjectMapper objectMapper = new ObjectMapper();
		try
		{
			LOG.info("gigyaUserObject::::::::::::" + objectMapper.writeValueAsString(gigyaUserObject));
		}
		catch (final JsonProcessingException e)
		{
		}
		LOG.info("getBusinessPartnerID::::::::::::" + gigyaUserObject.getBusinessPartnerID());
		if (StringUtils.isEmpty(gigyaUserObject.getEmail()))
		{
			throw new GigyaApiException("Gigya User does not have an email address");
		}

		if (StringUtils.isNotEmpty(gigyaUserObject.getBusinessPartnerID()))
		{
			return createB2BCustomer(gigyaConfig, uid, gigyaUserObject);
		}
		else
		{
			LOG.info("INSIDE createCustomer:::::::::::");
			return createCustomer(gigyaConfig, uid, gigyaUserObject);
		}
	}

	private B2BCustomerModel createB2BCustomer(final GigyaConfigModel gigyaConfig, final String uid,
			final GigyaUserObject gigyaUserObject)
	{
		try
		{
			LOG.info("INSIDE createB2BCustomer..........::::");
			// Update B2B Customer if possible as it can be replicated from S/4
			LOG.info("EMAIL" + gigyaUserObject.getEmail().toLowerCase());
			final UserModel user = getUserService().getUserForUID(gigyaUserObject.getEmail().toLowerCase());
			if (user instanceof B2BCustomerModel)
			{
				final B2BCustomerModel b2bCustomer = (B2BCustomerModel) user;
				b2bCustomer.setGyUID(gigyaUserObject.getUID());
				b2bCustomer.setGyApiKey(gigyaConfig.getGigyaApiKey());
				b2bCustomer.setName(getCustomerNameStrategy().getName(gigyaUserObject.getFirstName(), gigyaUserObject.getLastName()));
				if (StringUtils.isEmpty(b2bCustomer.getCustomerID()))
				{
					b2bCustomer.setCustomerID(UUID.randomUUID().toString());
				}
				assignB2BUnits(gigyaUserObject.getBusinessPartnerID(), b2bCustomer, gigyaConfig);
				getGigyaAuthServiceForGigyaConfig(gigyaConfig).assignAuthorisationsToCustomer(b2bCustomer);
				setUserGroups(gigyaUserObject, b2bCustomer);
				getModelService().save(b2bCustomer);
				getGigyaConsentFacade().synchronizeConsents(getPreferencesObject(gigyaUserObject), b2bCustomer);
				scheduleDataSyncFromCDCToCommerce(b2bCustomer);

				return b2bCustomer;
			}
		}
		catch (final UnknownIdentifierException e)
		{
			LOG.debug("User with email already exists.");
		}

		final B2BCustomerModel gigyaUser = getModelService().create(B2BCustomerModel.class);
		gigyaUser.setGyIsOriginGigya(true);
		gigyaUser.setName(getCustomerNameStrategy().getName(gigyaUserObject.getFirstName(), gigyaUserObject.getLastName()));
		gigyaUser.setUid(gigyaUserObject.getEmail().toLowerCase());
		gigyaUser.setOriginalUid(gigyaUserObject.getEmail().toLowerCase());
		gigyaUser.setGyUID(uid);
		gigyaUser.setCustomerID(UUID.randomUUID().toString());
		gigyaUser.setGyApiKey(gigyaConfig.getGigyaApiKey());
		gigyaUser.setSessionLanguage(getCommonI18NService().getCurrentLanguage());
		gigyaUser.setSessionCurrency(getCommonI18NService().getCurrentCurrency());
		gigyaUser.setEmail(gigyaUserObject.getEmail());
		LOG.info("Assigning b2b units:::::::::");
		LOG.info(
				"first gigyaUserObject getCustomerGroup :::::::::::::" + StringUtils.isNotEmpty(gigyaUserObject.getCustomerGroup()));
		LOG.info("secong gigyaUserObject getCustomerGroup :::::::::::::"
				+ StringUtils.equalsIgnoreCase(String.valueOf("01"), gigyaUserObject.getCustomerGroup()));
		LOG.info("third  gigyaUserObject getCustomerGroup :::::::::::::" + "02".equals(gigyaUserObject.getCustomerGroup()));
		LOG.info("four gigyaUserObject getCustomerGroup :::::::::::::"
				+ StringUtils.equalsIgnoreCase(String.valueOf("02"), gigyaUserObject.getCustomerGroup()));
		LOG.info("first gigyaUserObject getCustomerGroup :::::::::::::" + gigyaUserObject.getCustomerGroup());
		setUserGroups(gigyaUserObject, gigyaUser);
		assignB2BUnits(gigyaUserObject.getBusinessPartnerID(), gigyaUser, gigyaConfig);
		getGigyaAuthServiceForGigyaConfig(gigyaConfig).assignAuthorisationsToCustomer(gigyaUser);
		getSapB2BModelService().save(gigyaUser);

		getGigyaConsentFacade().synchronizeConsents(getPreferencesObject(gigyaUserObject), gigyaUser);
		scheduleDataSyncFromCDCToCommerce(gigyaUser);
		LOG.info("END customer creations:::::::::");
		return gigyaUser;
	}

	/**
	 * 
	 */
	private void setUserGroups(final GigyaUserObject gigyaUserObject, final B2BCustomerModel gigyaUser)
	{
		if (StringUtils.isNotEmpty(gigyaUserObject.getCustomerGroup()))
		{
			final Set<PrincipalGroupModel> groups = new HashSet<>(gigyaUser.getGroups());
			if (StringUtils.isNotEmpty(gigyaUserObject.getCustomerGroup()))
			{
				groups.add(b2bCommerceB2BUserGroupService.getUserGroupForUID(
						configurationService.getConfiguration().getString("customergroup."+gigyaUserObject.getCustomerGroup()+".userRegisrationRole"), UserGroupModel.class));
				gigyaUser.setCustomerProfile(enumerationService.getEnumerationValue(CustomerProfileType.class, configurationService.getConfiguration().getString("customergroup."+gigyaUserObject.getCustomerGroup()+".customerprofiletype")));
			}
			gigyaUser.setGroups(groups);
		}
	}

	private void assignB2BUnits(final String businessPartner, final B2BCustomerModel b2bCustomer,
			final GigyaConfigModel gigyaConfig)
	{
		final BaseStoreModel baseStore = baseStoreService.getCurrentBaseStore();
		final SAPConfigurationModel config = baseStore.getSAPConfiguration();
		if (StringUtils.isNotEmpty(businessPartner) && Objects.nonNull(config))
		{
			final StringBuilder bpId = new StringBuilder();
			bpId.append(businessPartner).append("_").append(config.getSapcommon_salesOrganization()).append("_")
					.append(config.getSapcommon_distributionChannel()).append("_").append(config.getSapcommon_division());
			LOG.info("Default b2b unit ::::::::" + bpId);
			final B2BUnitModel b2bUnit = b2bUnitService.getUnitForUid(bpId.toString());
			setGigyaAttributesInB2BUnit(gigyaConfig, businessPartner, b2bUnit);
			//	b2bUnitService.addMember(b2bUnit, b2bCustomer);
			b2bCustomer.setDefaultB2BUnit(b2bUnit);
		}
	}

	private void setGigyaAttributesInB2BUnit(final GigyaConfigModel gigyaConfig, final String businessPartner,
			final B2BUnitModel b2bUnit)
	{
		b2bUnit.setGyUID(businessPartner);
		b2bUnit.setGyApiKey(gigyaConfig.getGigyaApiKey());
		getModelService().save(b2bUnit);
	}
}
