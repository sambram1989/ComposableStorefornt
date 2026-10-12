/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service.impl;

import de.hybris.platform.b2b.model.B2BCommentModel;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCommentService;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.impl.DefaultCommerceCartCalculationStrategy;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.ProtocolEntryModel;
import de.hybris.platform.core.model.order.RecommendationEntryModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.impl.DefaultAbstractOrderService;
import de.hybris.platform.order.strategies.ordercloning.impl.DefaultCloneAbstractOrderStrategy;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.servicelayer.user.UserService;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wholescripts.core.dao.WholescriptsMedPaxDao;
import com.wholescripts.core.dao.WholescriptsProtocolDao;
import com.wholescripts.core.enums.CustomerProfileType;
import com.wholescripts.core.enums.ProtocolSharedStatus;
import com.wholescripts.core.enums.ProtocolStatus;
import com.wholescripts.core.model.MedPaxModel;
import com.wholescripts.core.model.ProtocolModel;
import com.wholescripts.core.model.RecommendationModel;
import com.wholescripts.core.service.WholescriptsMedPaxService;
import com.wholescripts.core.service.WholescriptsProtocolService;
import com.wholescripts.core.service.WholescriptsUserService;
import com.wholescripts.core.strategies.WholescriptsCommerceAddToCartStrategy;
import com.wholescripts.core.strategies.WholescriptsCommerceUpdateCartStrategy;
import com.wholescripts.core.utils.WholescriptsCoreUtils;
import com.wholescripts.data.CommerceProtocolParameter;


/**
 *
 */

public class WholescriptsProtocolServiceImpl implements WholescriptsProtocolService
{
	private static final Logger LOG = LoggerFactory.getLogger(WholescriptsProtocolServiceImpl.class);
	protected static final int APPEND_AS_LAST = -1;
	@Resource(name = "wholescriptsProtocolDao")
	private WholescriptsProtocolDao wholescriptsProtocolDao;
	@Resource(name = "modelService")
	private ModelService modelService;
	@Resource(name = "wsProtocolCodeGenerator")
	private KeyGenerator wsProtocolCodeGenerator;
	@Resource(name = "enumerationService")
	private EnumerationService enumerationService;
	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18NService;
	@Resource(name = "timeService")
	private TimeService timeService;
	@Resource(name = "cloneAbstractOrderStrategy")
	private DefaultCloneAbstractOrderStrategy cloneAbstractOrderStrategy;
	@Resource(name = "wsProtocolOrderService")
	private DefaultAbstractOrderService<ProtocolModel, ProtocolEntryModel> wsProtocolOrderService;
	@Resource(name = "wsCommerceAddToCartStrategy")
	private WholescriptsCommerceAddToCartStrategy wsCommerceAddToCartStrategy;
	@Resource(name = "b2bCommentService")
	private B2BCommentService<AbstractOrderModel> b2bCommentService;
	@Resource(name = "wsCommerceUpdateCartStrategy")
	private WholescriptsCommerceUpdateCartStrategy wsCommerceUpdateCartStrategy;
	@Resource(name = "commerceCartCalculationStrategy")
	private DefaultCommerceCartCalculationStrategy commerceCartCalculationStrategy;
	@Resource(name = "b2bCustomerService")
	private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;
	@Resource(name = "wholescriptsMedPaxDao")
	private WholescriptsMedPaxDao wholescriptsMedPaxDao;
	@Resource(name = "wholescriptsMedPaxService")
	private WholescriptsMedPaxService wholescriptsMedPaxService;
	
	@Resource(name = "wholescriptsUserService")
   WholescriptsUserService wholescriptsUserService;
	
	@Resource(name = "b2bUnitService")
   private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;
	
	@Resource(name = "searchRestrictionService")
	private SearchRestrictionService searchRestrictionService;
	@Resource(name = "calculationService")
	private CalculationService calculationService;
	

	@Override
	public SearchPageData<ProtocolModel> getProtocolsList(final SearchPageData searchPageData, final String protocolName,
			final boolean isWSPage,final boolean isNonDraft)
	{
		final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
		SearchPageData<ProtocolModel> protcols= wholescriptsProtocolDao.getListProtocolsModel(searchPageData, customer, protocolName, isWSPage,isNonDraft);
		protcols.getResults().forEach( p  ->{
			try
			{
				p.setCalculated(false);
				calculationService.calculate(p);
			}
			catch (CalculationException e)
			{
				LOG.error("calculation exception"+e.getMessage());
			}
		});
		return protcols;
	}

	@Override
	public Optional<ProtocolModel> getProtocolModelForID(final String protocolId, final boolean isWSPage)
	{
		final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
		if (Objects.nonNull(customer))
		{
			Optional<ProtocolModel>  protcol=wholescriptsProtocolDao.getProtocolModelForID(customer, protocolId, isWSPage);
			if(protcol.isPresent()) {
				try
				{
					protcol.get().setCalculated(false);
					calculationService.calculate(protcol.get());
				}
				catch (CalculationException e)
				{
					LOG.error("calculation exception"+e.getMessage());
				}
			}
			return protcol;
		}
		return Optional.empty();
	}
	
	@Override
	public Optional<ProtocolModel> getProtocolModelForCode(final String protocolId)
	{
			Optional<ProtocolModel>  protcol=wholescriptsProtocolDao.getProtocolModelForCode(protocolId);
			if(protcol.isPresent()) {
				try
				{
					protcol.get().setCalculated(false);
					calculationService.calculate(protcol.get());
					return protcol;
				}
				catch (CalculationException e)
				{
					LOG.error("calculation exception"+e.getMessage());
				}
			}
		return Optional.empty();
	}
	@Override
	public Optional<ProtocolModel> createProtocol(final CommerceProtocolParameter parameter)
	{
		final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
		final ProtocolModel protocolModel = modelService.create(ProtocolModel.class);
		protocolModel.setCode(wsProtocolCodeGenerator.generate().toString());
		protocolModel.setProtocolStatus(ProtocolStatus.DRAFT);
		protocolModel.setIsWSProtocol(Boolean.FALSE);
		if (WholescriptsCoreUtils.isWholescriptsDoctor(customer))
		{
			protocolModel.setIsWSProtocol(Boolean.TRUE);
		}
		protocolModel.setProtocolSharedStatus(ProtocolSharedStatus.NOT_SHARED);
		protocolModel.setWsName(parameter.getName());
		protocolModel.setWsDescription(parameter.getDescription());
		protocolModel.setUser(customer);
		protocolModel.setCurrency(commonI18NService.getCurrentCurrency());
		protocolModel.setDate(timeService.getCurrentTime());
		if (StringUtils.isNotEmpty(parameter.getTermsAgreed()) && parameter.getTermsAgreed().equalsIgnoreCase("true"))
		{
			protocolModel.setTermsAgreed(Boolean.TRUE);
		}
		modelService.save(protocolModel);
		return Optional.of(protocolModel);
	}

	@Override
	public Optional<ProtocolModel> duplicateProtocol(final CommerceProtocolParameter parameter)
			throws CommerceCartModificationException
	{
		final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
		final ProtocolModel duplicateProtocolModel = modelService.create(ProtocolModel.class);
		try
		{
			final Optional<ProtocolModel> protocolModel = this.getProtocolModelForID(parameter.getProtocolId(),
					parameter.isIsWSPage());
			final List<AbstractOrderEntryModel> entries = new ArrayList<>();
			duplicateProtocolModel.setEntries(entries);
			enteriesProcess(protocolModel.get(), duplicateProtocolModel);
			BeanUtils.copyProperties(duplicateProtocolModel, protocolModel);
			if (customer.getCustomerProfile().getCode().equals(CustomerProfileType.PRACTITIONER.getCode()))
			{
				duplicateProtocolModel.setCode(wsProtocolCodeGenerator.generate().toString());
				duplicateProtocolModel.setProtocolStatus(ProtocolStatus.DRAFT);
				duplicateProtocolModel.setIsWSProtocol(Boolean.FALSE);
				if (WholescriptsCoreUtils.isWholescriptsDoctor(customer))
				{
					duplicateProtocolModel.setIsWSProtocol(Boolean.TRUE);
				}
				duplicateProtocolModel.setProtocolSharedStatus(ProtocolSharedStatus.NOT_SHARED);
				duplicateProtocolModel.setWsName(parameter.getName());
				duplicateProtocolModel.setWsDescription(parameter.getDescription());
				duplicateProtocolModel.setUser(customer);
				duplicateProtocolModel.setCurrency(commonI18NService.getCurrentCurrency());
				duplicateProtocolModel.setDate(timeService.getCurrentTime());
				duplicateProtocolModel.setTotalPrice(protocolModel.get().getTotalPrice());
				duplicateProtocolModel.setMsrpSubTotalPrice(protocolModel.get().getMsrpSubTotalPrice());
				modelService.save(duplicateProtocolModel);
				final ProtocolModel protocolModelOpt = protocolModel.get();
				final MedPaxModel medpax = wholescriptsMedPaxDao.getMedPaxForOwner(protocolModelOpt);

				if (Objects.nonNull(medpax)) {
					wholescriptsMedPaxService.cloneMedpax(medpax, duplicateProtocolModel);
				}

			}
		}
		catch (IllegalAccessException | InvocationTargetException e)
		{
			e.printStackTrace();
		}
		return Optional.of(duplicateProtocolModel);
	}

	protected void enteriesProcess(final ProtocolModel protocolModel, final ProtocolModel duplicateProtocolModel)
	{
		final List<ProtocolEntryModel> entryList = protocolModel.getProtocolEntry();
		for (final ProtocolEntryModel entry : entryList)
		{
			final ProtocolEntryModel protocolEntryModel = wsProtocolOrderService.addNewEntry(duplicateProtocolModel,
					entry.getProduct(), entry.getQuantity(), entry.getUnit(), APPEND_AS_LAST, false);
			protocolEntryModel.setBasePrice(entry.getBasePrice());
			protocolEntryModel.setTotalPrice(entry.getTotalPrice());
			protocolEntryModel.setDosing(entry.getDosing());
			protocolEntryModel.setItemDuration(entry.getItemDuration());
			protocolEntryModel.setProductItemType(entry.getProductItemType());
			protocolEntryModel.setProtocol(duplicateProtocolModel);
			protocolEntryModel.setTemplateId(protocolModel.getCode());
		}
	}

	@Override
	public Optional<ProtocolModel> clone(final RecommendationModel recommendationModel, final String name)
			throws CommerceCartModificationException
	{
		final List<AbstractOrderEntryModel> entries = new ArrayList<>();
		final ProtocolModel model = modelService.create(ProtocolModel.class);
		model.setCode(wsProtocolCodeGenerator.generate().toString());
		model.setUser(userService.getCurrentUser());
		model.setProtocolStatus(ProtocolStatus.DRAFT);
		model.setProtocolSharedStatus(ProtocolSharedStatus.NOT_SHARED);
		model.setWsName(name);
		model.setCurrency(commonI18NService.getCurrentCurrency());
		model.setDate(timeService.getCurrentTime());
		model.setEntries(entries);
		recommendationEnteriesProcess(model, recommendationModel);
		model.setTotalPrice(recommendationModel.getTotalPrice());
		model.setMsrpSubTotalPrice(recommendationModel.getMsrpSubTotalPrice());
		modelService.save(model);
		modelService.refresh(model);

		final MedPaxModel medpax =wholescriptsMedPaxDao.getMedPaxForOwner(recommendationModel);
		if(Objects.nonNull(medpax)) {
			wholescriptsMedPaxService.cloneMedpax(medpax,model);
		}
		return Optional.of(model);
	}

	protected void recommendationEnteriesProcess(final ProtocolModel protocolModel, final RecommendationModel recommendationModel)
	{
		final List<AbstractOrderEntryModel> entryList = recommendationModel.getEntries();
		for (final AbstractOrderEntryModel entry : entryList)
		{
			if (entry instanceof RecommendationEntryModel)
			{
				final RecommendationEntryModel entryRecommend = (RecommendationEntryModel) entry;

				final ProtocolEntryModel protocolEntryModel = wsProtocolOrderService.addNewEntry(protocolModel, entry.getProduct(),
						entry.getQuantity(), entry.getUnit(), APPEND_AS_LAST, false);
				protocolEntryModel.setBasePrice(entry.getBasePrice());
				protocolEntryModel.setTotalPrice(entry.getTotalPrice());
				protocolEntryModel.setMsrpBasePrice(entry.getMsrpBasePrice());
				protocolEntryModel.setMsrpTotalPrice(entry.getMsrpTotalPrice());
				protocolEntryModel.setDosing((entryRecommend.getDosing()));
				protocolEntryModel.setItemDuration(entryRecommend.getItemDuration());
				protocolEntryModel.setProductItemType(entryRecommend.getProductItemType());
				protocolEntryModel.setProtocol(protocolModel);
				protocolEntryModel.setTemplateId(recommendationModel.getCode());
			}
		}
	}

	@Override
	public CommerceCartModification addToProtocol(final CommerceProtocolParameter parameter)
			throws CommerceCartModificationException
	{
		return wsCommerceAddToCartStrategy.addToProtocol(parameter);
	}

	@Override
	public CommerceCartModification updateQuantityForProtocolEntry(final CommerceProtocolParameter parameter)
			throws CommerceCartModificationException
	{
		return wsCommerceUpdateCartStrategy.updateQuantityForProtocolEntry(parameter);
	}

	@Override
	public CommerceCartModification updateToShippingModeForProtocolEntry(final CommerceProtocolParameter parameters)
			throws CommerceCartModificationException
	{
		return wsCommerceUpdateCartStrategy.updateToShippingModeForProtocolEntry(parameters);
	}

	@Override
	public Optional<ProtocolModel> deleteProtocol(final String protocolId)
	{
		final Optional<ProtocolModel> protocolModel = this.getProtocolModelForID(protocolId, false);
		if (protocolModel.isPresent())
		{
			final ProtocolModel protocol = protocolModel.get();
			protocol.setProtocolStatus(ProtocolStatus.DELETED);
			modelService.save(protocol);
		}
		return protocolModel;
	}

	@Override
	public Optional<ProtocolModel> saveProtocol(final CommerceProtocolParameter parameter)
	{
		final Optional<ProtocolModel> protocolModel = this.getProtocolModelForID(parameter.getProtocolId(), false);
		if (protocolModel.isPresent())
		{
			final B2BCommentModel b2bComment = modelService.create(B2BCommentModel.class);
			b2bComment.setWsNotes(parameter.getNotes());
			b2bCommentService.addComment(protocolModel.get(), b2bComment);
			if (StringUtils.isNotEmpty(parameter.getName()))
			{
				protocolModel.get().setWsName(parameter.getName());
			}
			if (StringUtils.isNotEmpty(parameter.getDescription()))
			{
				protocolModel.get().setWsDescription(parameter.getDescription());
			}
			if (StringUtils.isNotEmpty(parameter.getTermsAgreed()) && parameter.getTermsAgreed().equalsIgnoreCase("true"))
			{
				protocolModel.get().setTermsAgreed(Boolean.TRUE);
			}
			if (StringUtils.isNotEmpty(parameter.getProtocolStatus()))
			{
				protocolModel.get()
						.setProtocolStatus(enumerationService.getEnumerationValue(ProtocolStatus.class, parameter.getProtocolStatus()));
			}
			protocolModel.get().setDate(timeService.getCurrentTime());
			modelService.save(protocolModel.get());
		}
		return protocolModel;
	}

	@Override
	public Optional<ProtocolModel> shareProtocol(final CommerceProtocolParameter parameter)
	{
		final Optional<ProtocolModel> protocolModel = this.getProtocolModelForID(parameter.getProtocolId(), false);
		if (protocolModel.isPresent())
		{
			ProtocolModel protocol = protocolModel.get();
			final List<B2BCustomerModel> patientList = new ArrayList<>();
			parameter.getPatients().stream().forEach(d -> {
				getPatientModelForId(d, patientList);
			});
			if (CollectionUtils.isNotEmpty(protocol.getPatients()))
			{
				final Set<B2BCustomerModel> newPatientList = new HashSet<>(protocol.getPatients());
				newPatientList.addAll(patientList);
				protocol.setPatients(new ArrayList<>(newPatientList));
			}
			else
			{
				protocol.setPatients(patientList);
			}
			protocol.setProtocolSharedStatus(ProtocolSharedStatus.SHARED_WITH_SELECTED);
			if (parameter.isSharedWithAll())
			{
				protocolModel.get().setProtocolSharedStatus(ProtocolSharedStatus.SHARED_WITH_ALL);
				
			}
			if (StringUtils.isNotEmpty(parameter.getTermsAgreed()) && parameter.getTermsAgreed().equalsIgnoreCase("true"))
			{
				protocol.setTermsAgreed(Boolean.TRUE);
			}
			modelService.save(protocol);
		}
		return protocolModel;
	}

	private void getPatientModelForId(final String patientId, final List<B2BCustomerModel> patientList)
	{
		final UserModel patient = userService.getUserForUID(patientId);
		if (Objects.nonNull(patient) && patient instanceof B2BCustomerModel)
		{
			patientList.add((B2BCustomerModel) patient);
		}
	}

	@Override
	public SearchPageData<ProtocolModel> getPatientProtocolList(final SearchPageData searchPageData)
	{
		final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
		B2BCustomerModel salesPresonPractitioner = null;
		SearchPageData<ProtocolModel> protocols = null;
		final String salesPerson = customer.getDefaultB2BUnit() != null ? customer.getDefaultB2BUnit().getSalesPerson() : null;
		final String b2bUnit = salesPerson != null ? WholescriptsCoreUtils.getAccountNumberWithSalesArea(salesPerson.replaceFirst("^0+", "")) : null ;
		searchRestrictionService.disableSearchRestrictions();
		final B2BUnitModel b2bUnitModel = b2bUnit != null ? b2bUnitService.getUnitForUid(b2bUnit) : null;
		final Set<B2BCustomerModel> customers = b2bUnitModel != null ? b2bUnitService.getB2BCustomers(b2bUnitModel) : null;
		searchRestrictionService.enableSearchRestrictions();
		if (CollectionUtils.isNotEmpty(customers)) {
			salesPresonPractitioner = customers.iterator().next();
		}
		if (salesPresonPractitioner != null) {
			protocols = wholescriptsProtocolDao.getPatientProtocolsModel(searchPageData, customer.getUid(), salesPresonPractitioner);
		}
		return protocols;
	}

	@Override
	public Optional<ProtocolModel> stopSharingProtocol(final String protocolId)
	{
		final Optional<ProtocolModel> protocol = this.getProtocolModelForID(protocolId, false);
		if (protocol.isPresent())
		{
			final ProtocolModel protocolModel = protocol.get();
			protocolModel.setProtocolSharedStatus(ProtocolSharedStatus.NOT_SHARED);
			modelService.save(protocolModel);
		}
		return protocol;
	}
}
