/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.dao;

import de.hybris.platform.core.servicelayer.data.SearchPageData;

import java.util.List;
import java.util.Optional;

import com.wholescripts.core.model.RecommendationModel;
import com.wholescripts.dto.WholescriptsRecommendSearchParams;


/**
 *
 */
public interface WholescriptsRecommendationDao
{
	public RecommendationModel getRecommendationByCode(final String recommendationId);

	/**
	 *
	 */
	public SearchPageData<RecommendationModel> getListRecommendationsModel(final SearchPageData searchPageData,
			final String userId, final WholescriptsRecommendSearchParams searchParam);

	/**
	 *
	 */
	public Optional<RecommendationModel> getRecommendationDetails(String userId, String recommendationId);

	/**
	 *
	 */
	public SearchPageData<RecommendationModel> getNewListRecommendationsModel(SearchPageData searchPageData, String userId);

    public SearchPageData<RecommendationModel> getRecommendationsForPatient(final SearchPageData searchPageData,final String patientId,final WholescriptsRecommendSearchParams searchParam);

	List<RecommendationModel> getLatestRecommendationsByPractionerId(final String userId);
}
