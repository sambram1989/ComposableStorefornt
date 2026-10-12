/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service.impl;

import com.wholescripts.core.dao.WholescriptsCoreDao;
import com.wholescripts.core.enums.ContactSelectedCategoryEnum;
import com.wholescripts.core.enums.CustomerProfileType;
import com.wholescripts.core.event.listener.WholescriptsContactUsEvent;
import com.wholescripts.core.event.listener.WholescriptsThankYouEvent;
import com.wholescripts.core.model.*;
import com.wholescripts.core.service.WholescriptsCoreService;
import com.wholescripts.core.service.WholescriptsIntegrationsService;
import com.wholescripts.core.utils.WholescriptsCoreUtils;
import com.wholescripts.dto.WSContactUsFormWsDTO;
import com.wholescripts.dto.dosing.DosingRequestWsDto;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.interceptor.impl.UniqueAttributesInterceptor;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

public class WholescriptsCoreServiceImpl implements WholescriptsCoreService {
    private static final Logger LOG = LoggerFactory.getLogger(WholescriptsCoreServiceImpl.class);
    @Resource(name = "modelService")
    private ModelService modelService;
    @Resource(name = "userService")
    private UserService userService;
    @Resource(name = "productService")
    private ProductService productService;
    @Resource(name = "wholescriptsCoreDao")
    private WholescriptsCoreDao wholescriptsCoreDao;
    @Resource(name = "wsDosingCodeGenerator")
    private KeyGenerator wsDosingCodeGenerator;
	 @Resource(name = "enumerationService")
	 protected EnumerationService enumerationService;
	 @Resource(name = "b2bCustomerService")
    private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;
	@Resource(name = "eventService")
	private EventService eventService;
	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18NService;
	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;
	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;
	@Resource(name = "catalogVersionService")
	private CatalogVersionService catalogVersionService;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = "wholescriptsIntegrationsService")
	private WholescriptsIntegrationsService wholescriptsIntegrationsService;

    @Override
    public List<DosingModel> saveDosingEntries(final DosingRequestWsDto dosingRequest) {
   	List<DosingModel> dosingList = getDosingEntries(dosingRequest.getProductId());
   	if(CollectionUtils.isNotEmpty(dosingList)) {
   		modelService.removeAll(dosingList);
   	}
   	 if (MapUtils.isNotEmpty(dosingRequest.getItemTimesCodes())) {
   		 dosingRequest.getItemTimesCodes().entrySet().stream().forEach(it -> {
   			 final DosingModel dosingModel = modelService.create(DosingModel.class);
   			 dosingModel.setId(wsDosingCodeGenerator.generate().toString());
   			 dosingModel.setB2bCustomer((B2BCustomerModel) userService.getCurrentUser());
   			 dosingModel.setProduct(productService.getProductForCode(dosingRequest.getProductId()));
   			 dosingModel.setIsSavedAsCustom(dosingRequest.isIsSavedAsCustom());
   			 dosingModel.setItemTimes(wholescriptsCoreDao.getItemTimeByCode(it.getKey()));
   			 dosingModel.setQuantity(Integer.valueOf(it.getValue()));
   			 modelService.save(dosingModel);
   			 modelService.refresh(dosingModel);
   		 });
   		 return wholescriptsCoreDao.getAllDosingByProductnUser(dosingRequest.getProductId(), userService.getCurrentUser().getUid());
   	 } else {
   		 return Collections.emptyList();
   	 }
    }

	@Override
	public List<DosingModel> getDosingEntries(final String productId)
	{
		return wholescriptsCoreDao.getAllDosingByProductnUser(productId, userService.getCurrentUser().getUid());
	}

	@Override
	public List<ItemTimeModel> getAllActiveItemTimes() {
		return wholescriptsCoreDao.getAllActiveItemTimes();
	}

	@Override
	public List<WholescriptsTopicModel> getAllTopicDetails(final String page, final String childTopic)
	{
		return wholescriptsCoreDao.getAllTopicDetails(page, childTopic);
	}

	@Override
	public WSContactUsFormModel saveForm(final WSContactUsFormWsDTO input)
	{
		final WSContactUsFormModel model = modelService.create(WSContactUsFormModel.class);
		model.setContactFirstName(input.getFirstName());
		model.setContactLastName(input.getLastName());
		model.setContactPhone(input.getPhone());
		model.setContactEmail(input.getEmail());
		model.setContactAccountNumber(input.getAccountNumber());
		model.setContactSelectCat(enumerationService.getEnumerationValue(ContactSelectedCategoryEnum.class, input.getCategory()));
		model.setContactMesssage(input.getMessage());

		modelService.save(model);
		//Contact us Event
		WholescriptsContactUsEvent event=new WholescriptsContactUsEvent(model);
		eventService.publishEvent(initializeEvent(event, model));
		//Thank you Event
		WholescriptsThankYouEvent thankYouEmailEvent = new WholescriptsThankYouEvent(model);
		eventService.publishEvent(initThankyouEvent(thankYouEmailEvent,model));

		return model;
	}

	private AbstractEvent initializeEvent(WholescriptsContactUsEvent event, WSContactUsFormModel contactUsForm) {
		event.setBaseStore(baseStoreService.getCurrentBaseStore());
		event.setSite(baseSiteService.getCurrentBaseSite());
		event.setContactUsForm(contactUsForm);
		event.setCustomer(b2bCustomerService.getCurrentB2BCustomer());
		event.setLanguage(commonI18NService.getCurrentLanguage());
		event.setCurrency(commonI18NService.getCurrentCurrency());
		return event;
	}
	private AbstractEvent initThankyouEvent(WholescriptsThankYouEvent event,WSContactUsFormModel contactUsForm) {
		event.setBaseStore(baseStoreService.getCurrentBaseStore());
		event.setSite(baseSiteService.getCurrentBaseSite());
		event.setContactUsForm(contactUsForm);
		event.setCustomer(b2bCustomerService.getCurrentB2BCustomer());
		event.setLanguage(commonI18NService.getCurrentLanguage());
		event.setCurrency(commonI18NService.getCurrentCurrency());
		return event;
	}

	@Override
	public List<WholescriptsFaqListModel> getFaqQusAnsDetails()
	{
		return wholescriptsCoreDao.getFaqQusAnsDetails();
	}

	@Override
	public WholescriptsFaqListModel getFaqListByUid(final String uid) {
		return wholescriptsCoreDao.getFaqListByUid(uid);
	}

	@Override
	public void updateConsent(String userId, boolean isSmsNotificationEnabled, boolean isEmailNotificationEnabled) {
		final B2BCustomerModel customer = (B2BCustomerModel) userService.getUserForUID(userId);
		if (customer == null) {
			throw new IllegalArgumentException("User not found for the provided userId: " + userId);
		}
		customer.setIsSmsNotificationEnabled(isSmsNotificationEnabled);
		customer.setIsEmailNotificationEnabled(isEmailNotificationEnabled);
		modelService.save(customer);
	}

	public SearchPageData<ProductModel> getDiscontinuedProducts(final SearchPageData searchPageData) {
		return wholescriptsCoreDao.getDiscontinuedProducts(searchPageData);
	}
	@Override
	public SearchPageData<ProductModel> getAvailableWhileSuppliesLastProducts(final SearchPageData searchPageData) {
		return wholescriptsCoreDao.getAvailableWhileSuppliesLastProducts(searchPageData);
	}

	@Override
	public WholescriptsBrandModel getBrandForCode(final String brandCode)
	{
		return wholescriptsCoreDao.getBrandForCode(brandCode);
	}

	@Override
	public List<WholescriptsBrandModel> getTopBrands()
	{
		return wholescriptsCoreDao.getTopBrands();
	}

	@Override
	public WebinarModel getfeaturedWebinar()
	{
		return wholescriptsCoreDao.getfeaturedWebinar();
	}

	@Override
	public WebinarModel getWebinarDetails(final String webinarCode) {
		return wholescriptsCoreDao.getWebinarDetails(webinarCode);
	}

	@Override
	public PresenterModel getPresenterDetails(String presenterCode) {
		return wholescriptsCoreDao.getPresenterDetails(presenterCode);
	}

	@Override
	public List<PresenterModel> getAllPresenterDetails() {
		return wholescriptsCoreDao.getPresenterDetails();
	}

	/**
	 * Finds all {@link WholescriptsBrandModel}s with the specific <url>url</url>.
	 *
	 * @param brandurl the WholescriptsBrand url
	 * @return all found {@link WholescriptsBrandModel}, or an empty WholescriptsBrand can be found is returned
	 */
	@Override
	public WholescriptsBrandModel getBrandCodeByBrandUrl(String brandurl) {
		return wholescriptsCoreDao.getBrandCodeByBrandUrl(brandurl);
	}

	@Override
	public CustomerMaterialInformationModel getCustomerInfoModelForAccountAndBlueLabel(String productCode)
	{
		B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
		if(Objects.nonNull(customer)  && Objects.nonNull(customer.getDefaultB2BUnit()) ) {
			String accountNumber = WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(customer.getDefaultB2BUnit().getUid());
			return wholescriptsCoreDao.getCustomerInfoModelForAccountAndBlueLabel(productCode, accountNumber);
		}
		return null;
	}

	@Override
	public WholescriptsBrandModel getBrandByName(String brandName) {
		return wholescriptsCoreDao.getBrandByName(brandName);
	}
	
	@Override
	public List<ProductModel> getBestSellerProducts(Integer limit){
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(
				configurationService.getConfiguration().getString("wholescripts.product.catalog"),
				configurationService.getConfiguration().getString("wholescripts.product.catalog.version"));

		List<WsPopularProductsModel> popularProducts =  Optional.ofNullable(
				wholescriptsCoreDao.getPopularProducts(limit,catalogVersion)
		).orElse(Collections.emptyList());

		return popularProducts.stream()
				.map(p -> {
					try {
						return productService.getProductForCode(p.getProductCode());
					} catch (Exception e) {
						return null;
					}
				})
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	@Override
	public List<ProductModel> getProductsFromPractitionerOrders(Integer limit) {
		return Optional.ofNullable(
				wholescriptsCoreDao.getProductsFromPractitionerOrders(limit)
		).orElseGet(ArrayList::new);
	}

	@Override
	public List<ProductModel> getSelectedProductsDataForKlaviyo(final List<String> productSkusList, CatalogVersionModel catalogVersion) {
		return wholescriptsCoreDao.getSelectedProductsDataForKlaviyo(productSkusList, catalogVersion);
	}

	@Override
	public List<ProductModel> getAllProductsForKlaviyo(final CatalogVersionModel catalogVersion){
		return wholescriptsCoreDao.getAllProductsForKlaviyo(catalogVersion);
	}

	@Override
	public void updateSMSNotification(String phoneNumber) {
		B2BUnitModel b2bunit = wholescriptsCoreDao.getB2BUnitByPhoneNumber(phoneNumber);
		if (b2bunit == null) {
			LOG.warn("No B2BUnit found for phone number: " + phoneNumber);
			return;
		}
		B2BCustomerModel customer =  b2bunit.getMembers().stream()
				.filter(member -> member instanceof B2BCustomerModel)
				.map(member -> (B2BCustomerModel)member)
				.filter(member -> member.getCustomerProfile().equals(CustomerProfileType.PATIENT))
				.findFirst().orElse(null);
		if(customer != null){
			try {
				wholescriptsIntegrationsService.updateAccountInfo(customer);
			} catch (Exception e) {
				LOG.error("Error updating Accertify flag in CDC for customer: " + customer.getUid(), e);
				return;
			}
			customer.setIsSmsNotificationEnabled(false);
			modelService.save(customer);
		}
	}

	@Override
	public void changeUid(String newUid) throws DuplicateUidException {
		Assert.hasText(newUid, "The field [newUid] cannot be empty");

		try
		{
			B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
			customer.setOriginalUid(newUid);
			customer.setUid(newUid);
			customer.setEmail(newUid);
			customer.setCustomerID(newUid);
			modelService.save(customer);
		}catch (ModelSavingException e)
		{
			Throwable cause = e.getCause();
			if (cause  instanceof UniqueAttributesInterceptor.AmbiguousUniqueKeysException)
			{
				throw new DuplicateUidException("User with email " + newUid + " already exists.", e);
			}
			throw new IllegalStateException("Failed to update user UID due to an unexpected error.", e);
		}
	}

	@Override
	public List<OrderModel> getOrdersForKlaviyo() {
		return wholescriptsCoreDao.getOrdersForKlaviyo();
	}

	@Override
	public void updateVideoWatchStatus(String userId, String webinarCode) {

		WatchTrackerModel existingModel=getWatchTrackerModel(userId,webinarCode);
		if(Objects.nonNull(existingModel))
		{
			Integer rank=existingModel.getWeight();
			existingModel.setWeight(rank+1);
			modelService.save(existingModel);
		}
		else {
			final WatchTrackerModel model = modelService.create(WatchTrackerModel.class);
			model.setUserId(userId);
			model.setWebinarCode(webinarCode);
			model.setIsVideoWatched(Boolean.TRUE);
			modelService.save(model);
		}


	}

	@Override
	public WatchTrackerModel getWatchTrackerModel(String userId, String webinarCode) {
		return wholescriptsCoreDao.getWatchTrackerModel(userId,webinarCode);
	}

	@Override
	public List<WatchTrackerModel> getWebinarsBasedOnStatus(String userId, boolean status) {
		return wholescriptsCoreDao.getWebinarsBasedOnStatus(userId,status);
	}
}
