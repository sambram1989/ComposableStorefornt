package com.wholescripts.core.service;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.servicelayer.data.SearchPageData;

import java.util.Optional;

import com.wholescripts.core.model.ProtocolModel;
import com.wholescripts.core.model.RecommendationModel;
import com.wholescripts.data.CommerceProtocolParameter;

public interface WholescriptsProtocolService {

	SearchPageData<ProtocolModel> getProtocolsList(SearchPageData searchPageData,String protocolName,final boolean isWSPage,final boolean isNonDraft);
	Optional<ProtocolModel> getProtocolModelForID(String protocolId,final boolean isWSPage);
	Optional<ProtocolModel> createProtocol(final CommerceProtocolParameter parameter);
	Optional<ProtocolModel> saveProtocol(final CommerceProtocolParameter parameter);
	Optional<ProtocolModel> shareProtocol(final CommerceProtocolParameter parameter);
	CommerceCartModification addToProtocol(CommerceProtocolParameter parameter) throws CommerceCartModificationException;
	CommerceCartModification updateQuantityForProtocolEntry(final CommerceProtocolParameter parameter)
			throws CommerceCartModificationException;
	CommerceCartModification updateToShippingModeForProtocolEntry(final CommerceProtocolParameter parameters)
			throws CommerceCartModificationException;
	Optional<ProtocolModel> deleteProtocol(final String protocolId);
	SearchPageData<ProtocolModel> getPatientProtocolList(SearchPageData searchPageData);
	Optional<ProtocolModel> stopSharingProtocol( final String protocolId);
	public Optional<ProtocolModel> duplicateProtocol(final CommerceProtocolParameter parameter)
			throws CommerceCartModificationException;

	/**
	 * @throws CommerceCartModificationException
	 *
	 */
	Optional<ProtocolModel> clone(RecommendationModel recommendationModel, String name) throws CommerceCartModificationException;
	Optional<ProtocolModel> getProtocolModelForCode(String protocolId);
}
