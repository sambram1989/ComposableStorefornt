/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service.impl;

import com.wholescripts.core.dao.WholescriptsMedPaxDao;
import com.wholescripts.core.event.listener.WholescriptsSendRecommendationByTextEvent;
import com.wholescripts.core.event.listener.WholescriptsShareRecommendationEvent;
import com.wholescripts.core.model.MedPaxModel;
import com.wholescripts.core.service.WholescriptsMedPaxService;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.ProtocolEntryModel;
import de.hybris.platform.core.model.order.RecommendationEntryModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.impl.DefaultAbstractOrderService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import de.hybris.platform.store.services.BaseStoreService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wholescripts.core.dao.WholescriptsRecommendationDao;
import com.wholescripts.core.enums.CustomerProfileType;
import com.wholescripts.core.enums.RecommendationStatus;
import com.wholescripts.core.model.ProtocolModel;
import com.wholescripts.core.model.RecommendationModel;
import com.wholescripts.core.service.WholescriptsProtocolService;
import com.wholescripts.core.service.WholescriptsRecommendService;
import com.wholescripts.core.service.WholescriptsUserService;
import com.wholescripts.dto.WholescriptsRecommendSearchParams;

/**
 *
 */
public class WholescriptsRecommendServiceImpl implements WholescriptsRecommendService {
    private static final Logger LOG = Logger.getLogger(WholescriptsRecommendServiceImpl.class);
    protected static final int APPEND_AS_LAST = -1;
    @Resource(name = "wsCodeGenerator")
    private KeyGenerator keyGenerator;

    @Resource(name = "modelService")
    private ModelService modelService;

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "commonI18NService")
    private CommonI18NService commonI18NService;

    @Resource(name = "timeService")
    private TimeService timeService;

    @Resource(name = "productService")
    private ProductService productService;

    @Resource(name = "wholescriptsRecommendationDao")
    private WholescriptsRecommendationDao wholescriptsRecommendationDao;

    @Resource(name = "baseSiteService")
    private BaseSiteService baseSiteService;

    @Resource(name = "wholescriptsUserService")
    private WholescriptsUserService wholescriptsUserService;

    @Resource(name = "wsRecommendOrderService")
    DefaultAbstractOrderService<RecommendationModel, RecommendationEntryModel> wsRecommendOrderService;

    @Resource(name = "wholescriptsProtocolService")
    private WholescriptsProtocolService wholescriptsProtocolService;

	 @Resource(name = "b2bCustomerService")
	 private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;

    @Resource(name = "wholescriptsMedPaxDao")
    private WholescriptsMedPaxDao wholescriptsMedPaxDao;

    @Resource(name = "wholescriptsMedPaxService")
    private WholescriptsMedPaxService wholescriptsMedPaxService;


    @Resource(name = "eventService")
    private EventService eventService;

    @Resource(name = "baseStoreService")
    private BaseStoreService baseStoreService;
    @Resource(name = "calculationService")
 	private CalculationService calculationService;


    @Override
    public RecommendationModel createRecommendation(final String userId, final String patientID) {
        final RecommendationModel model = modelService.create(RecommendationModel.class);
        B2BCustomerModel customer = null;
        model.setCode(keyGenerator.generate().toString());
        if(StringUtils.isNotBlank(userId))
        {
            model.setUser(userService.getUserForUID(userId));
        }
        else {
            model.setUser(b2bCustomerService.getCurrentB2BCustomer());
        }

        model.setRecommendationStatus(RecommendationStatus.DRAFT);
        model.setCurrency(commonI18NService.getCurrentCurrency());
        model.setSite(baseSiteService.getCurrentBaseSite());
        if (StringUtils.isNoneEmpty(patientID)) {
            customer = wholescriptsUserService.getUserByUid(patientID);
            model.setPatient(customer);
        }
        model.setDate(timeService.getCurrentTime());
        modelService.save(model);

        return model;
    }

    @Override
	 public RecommendationModel clone(final B2BCustomerModel user, final ProtocolModel protcol, final String patientId)
            throws CommerceCartModificationException {
        final List<AbstractOrderEntryModel> entries = new ArrayList<>();
		  B2BCustomerModel customer = null;
        final RecommendationModel model = modelService.create(RecommendationModel.class);
        model.setCode(keyGenerator.generate().toString());
        model.setUser(userService.getCurrentUser());
        model.setRecommendationStatus(RecommendationStatus.DRAFT);
        model.setCurrency(commonI18NService.getCurrentCurrency());
        model.setSite(baseSiteService.getCurrentBaseSite());
        model.setDate(timeService.getCurrentTime());
		  if (StringUtils.isNoneEmpty(patientId))
		  {
			  customer = wholescriptsUserService.getUserByUid(patientId);
			  model.setPatient(customer);
		  }
        model.setEntries(entries);
        enteriesProcess(model, protcol);
        model.setTotalPrice(protcol.getTotalPrice());
        model.setMsrpSubTotalPrice(protcol.getMsrpSubTotalPrice());
        modelService.save(model);
        modelService.refresh(model);

        final MedPaxModel medpax =wholescriptsMedPaxDao.getMedPaxForOwner(protcol);
        if(Objects.nonNull(medpax)) {
            wholescriptsMedPaxService.cloneMedpax(medpax,model);
        }
            return model;
    }

    protected void enteriesProcess(final RecommendationModel recommend, final ProtocolModel protocol)
            throws CommerceCartModificationException {
        final List<ProtocolEntryModel> entryList = protocol.getProtocolEntry();
        for (final ProtocolEntryModel entry : entryList) {
            final RecommendationEntryModel recommendEntryModel = wsRecommendOrderService.addNewEntry(recommend, entry.getProduct(),
                    entry.getQuantity(), entry.getUnit(), APPEND_AS_LAST, false);
            recommendEntryModel.setDosing(entry.getDosing());
            recommendEntryModel.setItemDuration(entry.getItemDuration());
            recommendEntryModel.setProductItemType(entry.getProductItemType());
            recommendEntryModel.setBasePrice(entry.getBasePrice());
            recommendEntryModel.setTotalPrice(entry.getTotalPrice());
            recommendEntryModel.setMsrpBasePrice(entry.getMsrpBasePrice());
            recommendEntryModel.setMsrpTotalPrice(entry.getMsrpTotalPrice());
				recommendEntryModel.setTemplateId(protocol.getCode());

        }
    }


    @Override
    public Optional<RecommendationModel> deleteRecommendation(final String userId, final String recommendationId) {
        final Optional<RecommendationModel> recommendationModel = wholescriptsRecommendationDao.getRecommendationDetails(userId,
                recommendationId);
        final B2BCustomerModel customer = (B2BCustomerModel) userService.getCurrentUser();
        if (recommendationModel.isPresent()) {
            if (customer.getCustomerProfile().getCode().equals(CustomerProfileType.PATIENT.getCode())) {
                final RecommendationModel recommend = recommendationModel.get();
                recommend.setRecommendationStatus(RecommendationStatus.PATIENT_DELETED);
                modelService.save(recommend);
            } else if (customer.getCustomerProfile().getCode().equals(CustomerProfileType.PRACTITIONER.getCode())) {
                final RecommendationModel recommend = recommendationModel.get();
                recommend.setRecommendationStatus(RecommendationStatus.DISABLED);
                modelService.save(recommend);
            }
        }
        return recommendationModel;
    }

    @Override
    public RecommendationModel getRecommendation(final String recommendationId) {
   	 RecommendationModel recommendation= wholescriptsRecommendationDao.getRecommendationByCode(recommendationId);
       if(Objects.nonNull(recommendation)) {
      	 try
			{
      		 recommendation.setCalculated(false);
				calculationService.calculate(recommendation);
			}
			catch (CalculationException e)
			{
				LOG.error("calculation exception"+e.getMessage());
			}
       }
   	 return recommendation;
    }

    @Override
    public SearchPageData<RecommendationModel> getRecommendationList(final SearchPageData searchPageData, final String userId,
   		 final WholescriptsRecommendSearchParams searchParam) {
   	 SearchPageData<RecommendationModel>  recomendations= wholescriptsRecommendationDao.getListRecommendationsModel(searchPageData, userId, searchParam); 
   	 recomendations.getResults().forEach( r  ->{
   		 try
   		 {
   			 r.setCalculated(false);
   			 calculationService.calculate(r);
   		 }
   		 catch (CalculationException e)
   		 {
   			 LOG.error("calculation exception"+e.getMessage());
   		 }
   	 });
   	 return recomendations;
    }

    @Override
    public SearchPageData<RecommendationModel> getNewRecommendationList(final SearchPageData searchPageData, final String userId) {
   	 SearchPageData<RecommendationModel>  recomendations= wholescriptsRecommendationDao.getNewListRecommendationsModel(searchPageData, userId);
   	 recomendations.getResults().forEach( r  ->{
   		 try
   		 {
   			 r.setCalculated(false);
   			 calculationService.calculate(r);
   		 }
   		 catch (CalculationException e)
   		 {
   			 LOG.error("calculation exception"+e.getMessage());
   		 }
   	 });
   	 return recomendations;
    }

    @Override
    public Optional<RecommendationModel> getRecommendationDetails(final String userId, final String protocolId) {
   	 Optional<RecommendationModel>  recomendation= wholescriptsRecommendationDao.getRecommendationDetails(userId, protocolId);
   	 if(recomendation.isPresent()) {
   		 try
   		 {
   			 recomendation.get().setCalculated(false);
   			 calculationService.calculate(recomendation.get());
   		 }
   		 catch (CalculationException e)
   		 {
   			 LOG.error("calculation exception"+e.getMessage());
   		 }
   	 }
   	 return recomendation;
    }

    public RecommendationModel swapProtocol(final String templateId, final String recommendationId,final boolean isWSPage) {
   	 final RecommendationModel recommendationModel = wholescriptsRecommendationDao.getRecommendationByCode(recommendationId);
   	 final Optional<ProtocolModel> optionalProtocolModel = wholescriptsProtocolService.getProtocolModelForID(templateId,isWSPage);
   	 final ProtocolModel newProtocolModel = optionalProtocolModel.get();
   	 final List<AbstractOrderEntryModel> existingEntries = recommendationModel.getEntries();
   	 // final List<AbstractOrderEntryModel> filteredEntries = existingEntries.stream().filter(e -> StringUtils.isNotEmpty(e.getTemplateId()) && !e.getTemplateId().equals(templateId)).collect(Collectors.toList());
   	 if (CollectionUtils.isNotEmpty(existingEntries)) {
   		 modelService.removeAll(existingEntries);
   		 modelService.refresh(recommendationModel);
   	 }
   	 MedPaxModel existingMedpax= wholescriptsMedPaxDao.getMedPaxForOwner(recommendationModel);
   	 if(Objects.nonNull(existingMedpax)) {
   		 modelService.remove(existingMedpax);
   	 }
   	 MedPaxModel newMedpax= wholescriptsMedPaxDao.getMedPaxForOwner(newProtocolModel);
   	 if(Objects.nonNull(newMedpax)) {
   		 try
			{
				wholescriptsMedPaxService.cloneMedpax(newMedpax, recommendationModel);
			}
			catch (CommerceCartModificationException e)
			{
		    LOG.error(e);
			}
   	 }
   	 final List<ProtocolEntryModel> newEntries = new ArrayList<>(newProtocolModel.getProtocolEntry());
   	 newEntries.forEach(d -> createEntry(d, recommendationModel, templateId));
   	 return recommendationModel;
    }

    @Override
    public SearchPageData<RecommendationModel> getPatientRecommendationList(SearchPageData searchPageData, String userId,final WholescriptsRecommendSearchParams searchParam) {
   	 SearchPageData<RecommendationModel>  recomendations=wholescriptsRecommendationDao.getRecommendationsForPatient(searchPageData,userId,searchParam);
   	 recomendations.getResults().forEach( r  ->{
   		 try
   		 {
   			 r.setCalculated(false);
   			 calculationService.calculate(r);
   		 }
   		 catch (CalculationException e)
   		 {
   			 LOG.error("calculation exception"+e.getMessage());
   		 }
   	 });
   	 return recomendations;
    }

    private void createEntry(final ProtocolEntryModel originalEntry, final RecommendationModel recommendationModel, final String templateId) {
        final RecommendationEntryModel entryModel = wsRecommendOrderService.addNewEntry(
                recommendationModel, originalEntry.getProduct(), originalEntry.getQuantity(), originalEntry.getUnit(),
                APPEND_AS_LAST, false);
        entryModel.setDeliveryPointOfService(originalEntry.getDeliveryPointOfService());
        entryModel.setDosing(originalEntry.getDosing());
        entryModel.setComments(originalEntry.getComments());
        entryModel.setItemDuration(originalEntry.getItemDuration());
        entryModel.setProductItemType(originalEntry.getProductItemType());
        entryModel.setTemplateId(templateId);
        modelService.save(entryModel);
        modelService.save(recommendationModel);

    }
    @Override
    public void triggerRecommendationNotificationsForUser(RecommendationModel wsModel, boolean isSmsNotificationEnabled, boolean isEmailNotificationEnabled) {
        wsModel.setIsEmailNotificationEnabled(isEmailNotificationEnabled);
        wsModel.setIsSmsNotificationEnabled(isSmsNotificationEnabled);
        if(isEmailNotificationEnabled)
        {
            try{
                WholescriptsShareRecommendationEvent event=new WholescriptsShareRecommendationEvent(wsModel);
                eventService.publishEvent(initializeEvent(event, wsModel.getPatient()));
            } catch (Exception e) {
               e.printStackTrace();
            }

        }
        if(isSmsNotificationEnabled)
        {
            if(Objects.nonNull(wsModel.getPatient()) && StringUtils.isNotBlank(wsModel.getPatient().getUid()))
            {
                B2BCustomerModel  customer = wholescriptsUserService.getUserByUid(wsModel.getPatient().getUid());
                if(Objects.nonNull(customer) && Objects.nonNull(customer.getDefaultB2BUnit()))
                {
                    Optional<AddressModel> address=customer.getDefaultB2BUnit().getAddresses().stream().findFirst();
                    if(address.isPresent())
                    {
                        try{
                            WholescriptsSendRecommendationByTextEvent event=new WholescriptsSendRecommendationByTextEvent(wsModel);
                            eventService.publishEvent(initializeEventForTextMessage(event, wsModel.getPatient()));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }


            }



        }

    }

    public List<RecommendationModel> getLatestRecommendationsByPractionerId(final String uId){
        return wholescriptsRecommendationDao.getLatestRecommendationsByPractionerId(uId);
    }

    private AbstractEvent initializeEventForTextMessage(WholescriptsSendRecommendationByTextEvent event, B2BCustomerModel customerModel) {
        event.setBaseStore(baseStoreService.getCurrentBaseStore());
        event.setSite(baseSiteService.getCurrentBaseSite());
        event.setCustomer(customerModel);
        event.setLanguage(commonI18NService.getCurrentLanguage());
        event.setCurrency(commonI18NService.getCurrentCurrency());
        return event;
    }

    private AbstractEvent initializeEvent(WholescriptsShareRecommendationEvent event, CustomerModel customerModel) {
        event.setBaseStore(baseStoreService.getCurrentBaseStore());
        event.setSite(baseSiteService.getCurrentBaseSite());
        event.setCustomer(customerModel);
        event.setLanguage(commonI18NService.getCurrentLanguage());
        event.setCurrency(commonI18NService.getCurrentCurrency());
        return event;
    }
}

