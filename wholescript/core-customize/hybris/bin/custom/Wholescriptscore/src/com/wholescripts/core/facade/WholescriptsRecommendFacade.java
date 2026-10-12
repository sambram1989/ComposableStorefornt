/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.facade;

import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.servicelayer.data.SearchPageData;

import java.util.Optional;

import com.wholescripts.dto.WholescriptsRecommendSearchParams;
import com.wholescripts.dto.WsAddtoCartParam;
import com.wholescripts.dto.WsRecommendationData;
import com.wholescripts.dto.WsRecommendationEntryInputWsDTO;


/**
 *
 */
public interface WholescriptsRecommendFacade
{

	public WsRecommendationData createRecommendation(final String patientId);

	public CartModificationData createEntryRecommendation(final String code, final long qty, final String recommendationId,
			final WsRecommendationEntryInputWsDTO entry) throws CommerceCartModificationException;

	public CartModificationData addToCart(final WsAddtoCartParam addToCartParams) throws CommerceCartModificationException;

	/**
	 * @throws CommerceCartModificationException
	 *
	 */
	public CartModificationData createEntryRecommendationForStore(String code, long quantity, String recommendationId,
			WsRecommendationEntryInputWsDTO entry, String pickupStore) throws CommerceCartModificationException;

	/**
	 *
	 */
	public WsRecommendationData getRecommendation(final String recommendationID);

	/**
	 * @param recommendId
	 * @throws CommerceCartModificationException
	 *
	 */
	public CartModificationData updateRecommendEntry(long entryNumber, String recommendId, WsRecommendationEntryInputWsDTO entry,
			Long qty) throws CommerceCartModificationException;

	/**
	 *
	 */
	public SearchPageData<WsRecommendationData> getRecommendationListData(SearchPageData searchPageDataInput,
			String userId, final WholescriptsRecommendSearchParams searchParam);
	/**
	 *
	 */
	public Optional<WsRecommendationData> getRecommendationDetailsData(String userId, String recommendationID);

	/**
	 * @param notes
	 * @param isSmsNotificationEnabled
	 * @param isEmailNotificationEnabled
	 */
	public WsRecommendationData shareRecommendation(String userId, String patientId, String recommendationId, String notes,
													String termsAgreed, boolean isSmsNotificationEnabled, boolean isEmailNotificationEnabled);

	/**
	 *
	 */
	public WsRecommendationData getNewRecommendationListData(
			SearchPageData<WsRecommendationData> searchPageDataInput, String userId);

	/**
	 *
	 */

	public WsRecommendationData updateRecommendationStatus(String userId, String recommendationId, final String status);


	/**
	 *
	 */
	public Optional<WsRecommendationData> deleteRecommendation(String userId, String recommendationId);

	public CartModificationData updateRecommendationEntry(final long entryNumber, final long quantity, String recommendationId)
			throws CommerceCartModificationException;

	/**
	 *
	 */
	public Optional<WsRecommendationData> clone(String protocolId, String patientId,final boolean isWSPage) throws CommerceCartModificationException;

	public WsRecommendationData swapProtocol(String templateId, String recommendationId,final boolean isWSPage);

	/**
	 *
	 */

	/**
	 *
	 */
	SearchPageData<WsRecommendationData> getPatientRecommendationListData(SearchPageData searchPageDataInput,
																   String userId,final WholescriptsRecommendSearchParams searchParam);

}
