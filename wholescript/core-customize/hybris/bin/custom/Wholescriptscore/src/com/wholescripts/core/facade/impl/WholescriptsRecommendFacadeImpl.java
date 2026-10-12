/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.facade.impl;

import static java.util.stream.Collectors.toList;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.comments.model.DomainModel;
import de.hybris.platform.comments.services.CommentService;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.impl.DefaultCartFacade;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.wholescripts.core.constants.GeneratedWholescriptscoreConstants.Enumerations.CustomerProfileType;
import com.wholescripts.core.enums.RecommendationStatus;
import com.wholescripts.core.facade.WholescriptsRecommendFacade;
import com.wholescripts.core.model.ProtocolModel;
import com.wholescripts.core.model.RecommendationModel;
import com.wholescripts.core.service.WholescriptsProtocolService;
import com.wholescripts.core.service.WholescriptsRecommendCommerceCartService;
import com.wholescripts.core.service.WholescriptsRecommendService;
import com.wholescripts.core.service.WholescriptsUserService;
import com.wholescripts.dto.WholescriptsRecommendSearchParams;
import com.wholescripts.dto.WsAddtoCartParam;
import com.wholescripts.dto.WsCartModificationData;
import com.wholescripts.dto.WsCommerceCartParameter;
import com.wholescripts.dto.WsRecommendationData;
import com.wholescripts.dto.WsRecommendationEntryInputWsDTO;


/**
 *
 */
public class WholescriptsRecommendFacadeImpl extends DefaultCartFacade implements WholescriptsRecommendFacade
{

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "wholescriptsRecommendConverter")
	private Converter<RecommendationModel, WsRecommendationData> wholescriptsRecommendConverter;

	@Resource(name = "wsCommerceCartParameterConverter")
	private Converter<WsAddtoCartParam, WsCommerceCartParameter> wsCommerceCartParameterConverter;

	@Resource(name = "wsCartModificationConverter")
	private Converter<CommerceCartModification, WsCartModificationData> wsCartModificationConverter;

	@Resource(name = "wholescriptsRecommendService")
	private WholescriptsRecommendService wholescriptsRecommendService;

	@Resource(name = "wsRecommendCommerceCartService")
	private WholescriptsRecommendCommerceCartService wsRecommendCommerceCartService;

	@Resource(name = "commentService")
	private CommentService commentService;

	@Resource(name = "wsCommentCodeGenerator")
	private KeyGenerator wsCommentCodeGenerator;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "wholescriptsUserService")
	private WholescriptsUserService wholescriptsUserService;

	@Resource(name = "wholescriptsProtocolService")
	private WholescriptsProtocolService wholescriptsProtocolService;

	@Resource(name = "b2bCustomerService")
	private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;
	@Resource(name = "timeService")
	private TimeService timeService;


	@Override
	public WsRecommendationData createRecommendation(final String patientId)
	{
		// XXX Auto-generated method stub
		final RecommendationModel wsModel = wholescriptsRecommendService.createRecommendation(StringUtils.EMPTY, patientId);
		return wholescriptsRecommendConverter.convert(wsModel);
	}

	@Override
	public Optional<WsRecommendationData> clone(final String protocolId, final String patientId, final boolean isWSPage)
			throws CommerceCartModificationException
	{
		final B2BCustomerModel customer = (B2BCustomerModel) userService.getCurrentUser();
		if (customer.getCustomerProfile().getCode().equals(CustomerProfileType.PRACTITIONER))
		{
			final Optional<ProtocolModel> protocolModel = wholescriptsProtocolService.getProtocolModelForID(protocolId, isWSPage);
			final RecommendationModel wsModel = wholescriptsRecommendService.clone(customer, protocolModel.get(), patientId);
			return Optional.of(wholescriptsRecommendConverter.convert(wsModel));
		}
		return Optional.empty();
	}

	@Override
	public WsRecommendationData shareRecommendation(final String userId, final String patientId, final String recommendationId,
			final String notes, final String termsAgreed, final boolean isSmsNotificationEnabled,
			final boolean isEmailNotificationEnabled)
	{
		// XXX Auto-generated method stub
		final RecommendationModel wsModel = wholescriptsRecommendService.getRecommendation(recommendationId);
		wsModel.setRecommendationStatus(RecommendationStatus.SENT);
		if (notes != null)
		{
			final DomainModel domainModel = commentService.getDomainForCode(Config.getParameter("comment.domain.code"));
			final ComponentModel componentModel = commentService.getComponentForCode(domainModel,
					Config.getParameter("comment.component.code"));
			final CommentTypeModel commentTypeModel = commentService.getCommentTypeForCode(componentModel,
					Config.getParameter("comment.component.type.code"));

			final CommentModel commentModel = modelService.create(CommentModel.class);
			commentModel.setCode(wsCommentCodeGenerator.generate().toString());
			commentModel.setText(notes);
			commentModel.setAuthor(userService.getCurrentUser());
			commentModel.setComponent(componentModel);
			commentModel.setCommentType(commentTypeModel);



			final List<CommentModel> commentList = new ArrayList<>();
			commentList.add(commentModel);
			wsModel.setComments((commentList));
		}
		if (StringUtils.isNotEmpty(termsAgreed) && termsAgreed.equalsIgnoreCase("true"))
		{
			wsModel.setTermsAgreed(Boolean.TRUE);
		}
		wholescriptsRecommendService.triggerRecommendationNotificationsForUser(wsModel, isSmsNotificationEnabled,
				isEmailNotificationEnabled);
		modelService.save(wsModel);
		return wholescriptsRecommendConverter.convert(wsModel);
	}

	@Override
	public CartModificationData createEntryRecommendation(final String code, final long quantity, final String recommendationId,
			final WsRecommendationEntryInputWsDTO entry) throws CommerceCartModificationException
	{
		final WsAddtoCartParam params = new WsAddtoCartParam();
		params.setProductCode(code);
		params.setQuantity(quantity);
		params.setRecommendationId(recommendationId);
		params.setEntryInput(entry);

		return addToCart(params);
	}

	@Override
	public CartModificationData createEntryRecommendationForStore(final String code, final long quantity,
			final String recommendationId, final WsRecommendationEntryInputWsDTO entry, final String storeId)
			throws CommerceCartModificationException
	{
		final WsAddtoCartParam params = new WsAddtoCartParam();
		params.setProductCode(code);
		params.setQuantity(quantity);
		params.setRecommendationId(recommendationId);
		params.setStoreId(storeId);
		params.setEntryInput(entry);
		return addToCart(params);
	}

	@Override
	public CartModificationData addToCart(final WsAddtoCartParam addToCartParams) throws CommerceCartModificationException
	{

		final WsCommerceCartParameter parameter = wsCommerceCartParameterConverter.convert(addToCartParams);
		final CommerceCartModification modification = wsRecommendCommerceCartService.addToRecommend(parameter);

		return wsCartModificationConverter.convert(modification);
	}

	@Override
	public WsRecommendationData getRecommendation(final String recommendationID)
	{
		// XXX Auto-generated method stub
		final WsRecommendationData recommendationData;
		final RecommendationModel wsModel = wholescriptsRecommendService.getRecommendation(recommendationID);
		recommendationData = wholescriptsRecommendConverter.convert(wsModel);
		return recommendationData;
	}


	@Override
	public CartModificationData updateRecommendEntry(final long entryNumber, final String recommendationId,
			final WsRecommendationEntryInputWsDTO entry, final Long qty) throws CommerceCartModificationException
	{
		final WsAddtoCartParam dto = new WsAddtoCartParam();
		dto.setQuantity(qty);
		dto.setProductCode(entry.getProduct().getCode());
		dto.setEntryInput(entry);
		dto.setRecommendationId(recommendationId);
		final WsCommerceCartParameter parameter = wsCommerceCartParameterConverter.convert(dto);
		if (parameter != null)
		{
			parameter.setEnableHooks(true);
			parameter.setEnableHooks(true);
			parameter.setEntryNumber(entryNumber);
		}
		final CommerceCartModification modification = wsRecommendCommerceCartService.updateQuantityForRecommendEntry(parameter);
		return wsCartModificationConverter.convert(modification);
	}

	@Override
	public SearchPageData<WsRecommendationData> getRecommendationListData(final SearchPageData searchPageData, final String userId,
			final WholescriptsRecommendSearchParams searchParam)
	{
		final SearchPageData<RecommendationModel> searchData = wholescriptsRecommendService.getRecommendationList(searchPageData,
				userId, searchParam);
		final SearchPageData<WsRecommendationData> result = new SearchPageData();
		final List<WsRecommendationData> recommendDataList = searchData.getResults().stream()
				.map(recommendModel -> getRecommendData(recommendModel)).filter(Optional::isPresent).map(Optional::get)
				.collect(toList());
		result.setResults(recommendDataList);
		result.setPagination(searchData.getPagination());
		result.setSorts(searchData.getSorts());
		return result;
	}

	@Override
	public WsRecommendationData getNewRecommendationListData(final SearchPageData searchPageData, final String userId)
	{

		WsRecommendationData recommednData = new WsRecommendationData();
		final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
		if (customer.getCustomerProfile().getCode().equals(CustomerProfileType.PATIENT))
		{
			final SearchPageData<RecommendationModel> searchData = wholescriptsRecommendService
					.getNewRecommendationList(searchPageData, userId);

			final Comparator<RecommendationModel> recommendationComparator = Comparator
					.comparing(RecommendationModel::getModifiedtime);
			final RecommendationModel latestRecommendations = searchData.getResults().stream().max(recommendationComparator).get();
			if (customer.getLastLogin() != null && customer.getLastLogin().after(latestRecommendations.getCreationtime()))
			{
				recommednData = getNewRecommendData(latestRecommendations);
			}
		}
		return recommednData;
	}


	@Override
	public WsRecommendationData updateRecommendationStatus(final String userId, final String recommendationId, final String status)
	{
		final RecommendationModel wsModel = wholescriptsRecommendService.getRecommendation(recommendationId);
		final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();

		if (customer.getCustomerProfile().getCode().equals(CustomerProfileType.PATIENT))
		{
			final String statusList = Config.getParameter("allowed." + customer.getCustomerProfile().getCode() + ".status");
			final List<String> list = new ArrayList<String>(Arrays.asList(statusList.split(" , ")));
			if (list.stream().anyMatch(statuses -> statuses.contains(status)))
			{
				wsModel.setRecommendationStatus(RecommendationStatus.PATIENT_VIEWED);
				modelService.save(wsModel);
				return wholescriptsRecommendConverter.convert(wsModel);
			}

		}
		else
		{
			if (customer.getCustomerProfile().getCode().equals(CustomerProfileType.PRACTITIONER))
			{
				final String statusList = Config.getParameter("allowed." + customer.getCustomerProfile().getCode() + ".status");
				final List<String> list = new ArrayList<String>(Arrays.asList(statusList.split(" , ")));
				if (list.stream().anyMatch(statuses -> statuses.contains(status)))
				{
					wsModel.setRecommendationStatus(RecommendationStatus.PRACTITIONER_CANCELLED);
					wsModel.setDate(timeService.getCurrentTime());
					modelService.save(wsModel);
					return wholescriptsRecommendConverter.convert(wsModel);
				}

			}

		}
		return wholescriptsRecommendConverter.convert(wsModel);
	}


	@Override
	public Optional<WsRecommendationData> deleteRecommendation(final String userId, final String recommendationId)
	{
		final Optional<RecommendationModel> recommendationModel = wholescriptsRecommendService.deleteRecommendation(userId,
				recommendationId);

		if (recommendationModel.isPresent())
		{
			return Optional.of(wholescriptsRecommendConverter.convert(recommendationModel.get()));
		}
		return Optional.empty();
	}

	public Optional<WsRecommendationData> getRecommendData(final RecommendationModel model)
	{
		return Optional.of(wholescriptsRecommendConverter.convert(model));
	}

	public WsRecommendationData getNewRecommendData(final RecommendationModel model)
	{
		return wholescriptsRecommendConverter.convert(model);
	}

	@Override
	public Optional<WsRecommendationData> getRecommendationDetailsData(final String userId, final String recommendationId)
	{
		final Optional<RecommendationModel> recommendationModel = wholescriptsRecommendService.getRecommendationDetails(userId,
				recommendationId);
		if (recommendationModel.isPresent())
		{
			return Optional.of(getRecommendData(recommendationModel.get()).get());
		}
		throw new ModelNotFoundException("Does not exist:-" + recommendationId);
	}

	@Override
	public CartModificationData updateRecommendationEntry(final long entryNumber, final long quantity,
			final String recommendationId) throws CommerceCartModificationException
	{
		final WsAddtoCartParam params = new WsAddtoCartParam();
		params.setQuantity(quantity);
		params.setRecommendationId(recommendationId);
		final WsCommerceCartParameter parameter = wsCommerceCartParameterConverter.convert(params);
		if (parameter != null)
		{
			parameter.setEnableHooks(true);
			parameter.setEnableHooks(true);
			parameter.setEntryNumber(entryNumber);
		}
		final CommerceCartModification modification = wsRecommendCommerceCartService.updateQuantityForRecommendEntry(parameter);
		return wsCartModificationConverter.convert(modification);
	}

	public WsRecommendationData swapProtocol(final String templateId, final String recommendationId, final boolean isWSPage)
	{
		final RecommendationModel recommendationModel = wholescriptsRecommendService.swapProtocol(templateId, recommendationId,
				isWSPage);
		return wholescriptsRecommendConverter.convert(recommendationModel);
	}

	@Override
	public SearchPageData<WsRecommendationData> getPatientRecommendationListData(final SearchPageData searchData,
			final String userId, final WholescriptsRecommendSearchParams searchParam)
	{
		final SearchPageData<RecommendationModel> searchResultData = wholescriptsRecommendService
				.getPatientRecommendationList(searchData, userId, searchParam);
		final SearchPageData<WsRecommendationData> result = new SearchPageData();
		final List<WsRecommendationData> recommendDataList = searchResultData.getResults().stream().map(this::getRecommendData)
				.filter(Optional::isPresent).map(Optional::get).collect(toList());
		result.setResults(recommendDataList);
		result.setPagination(searchData.getPagination());
		return result;
	}


}
