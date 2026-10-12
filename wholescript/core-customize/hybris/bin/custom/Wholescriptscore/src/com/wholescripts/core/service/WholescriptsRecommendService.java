/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.servicelayer.data.SearchPageData;

import java.util.List;
import java.util.Optional;

import com.wholescripts.core.model.ProtocolModel;
import com.wholescripts.core.model.RecommendationModel;
import com.wholescripts.dto.WholescriptsRecommendSearchParams;


/**
 *
 */
public interface WholescriptsRecommendService
{

	public RecommendationModel createRecommendation(final String userId, final String patientId);

	/**
	 *
	 */
	public RecommendationModel getRecommendation(String recommendationId);

	/**
	 *
	 */
	public SearchPageData<RecommendationModel> getRecommendationList(SearchPageData searchPageData, String userId,
			final WholescriptsRecommendSearchParams searchParam);


	/**
	 *
	 */
	public Optional<RecommendationModel> getRecommendationDetails(String userId, String recommendationId);

	/**
	 *
	 */
	public SearchPageData<RecommendationModel> getNewRecommendationList(final SearchPageData searchPageData, final String userId);

	public Optional<RecommendationModel> deleteRecommendation(String userId, String recommendationId);

	/**
	 * @throws CommerceCartModificationException
	 *
	 */
	public RecommendationModel clone(B2BCustomerModel user, ProtocolModel protcol, String patientId)
			throws CommerceCartModificationException;

	public RecommendationModel swapProtocol(String templateId, String recommendationId,final boolean isWSPage);

	/**
	 *
	 */

	public SearchPageData<RecommendationModel> getPatientRecommendationList(final SearchPageData searchPageData, final String userId,final WholescriptsRecommendSearchParams searchParam);

	void triggerRecommendationNotificationsForUser(RecommendationModel wsModel, boolean isSmsNotificationEnabled, boolean isEmailNotificationEnabled);

	List<RecommendationModel> getLatestRecommendationsByPractionerId(final String uId);
}
