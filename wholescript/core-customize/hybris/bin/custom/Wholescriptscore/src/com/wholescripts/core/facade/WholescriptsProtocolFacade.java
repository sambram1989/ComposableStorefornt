package com.wholescripts.core.facade;

import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.servicelayer.data.SearchPageData;

import java.util.Optional;

import com.wholescripts.core.model.ProtocolModel;
import  com.wholescripts.data.AddtoProtocolParam;
import com.wholescripts.data.CommerceProtocolParameter;
import com.wholescripts.data.protocol.ProtocolData;
import com.wholescripts.dto.protocol.ProtocolEntryWsDto;


public interface WholescriptsProtocolFacade
{
	SearchPageData<ProtocolData> getProtocolListData(SearchPageData searchPageData, String protocolName,final boolean isWSPage, final boolean isNonDraft);
	Optional<ProtocolData> getProtocolDetailsData(String protocolId,final boolean isWSPage);
	Optional<ProtocolData> addNewProtocol(CommerceProtocolParameter parameter);
	Optional<ProtocolData> saveProtocol(CommerceProtocolParameter parameter);
	Optional<ProtocolData> shareProtocol(CommerceProtocolParameter parameter);
	CartModificationData createProtocolEntryForStore(final String code, final long quantity,	final String protocolId, final ProtocolEntryWsDto entry, final String storeId)
			throws CommerceCartModificationException;
	CartModificationData createProtocolEntry(final String code, final long quantity,	final String protocolId, final ProtocolEntryWsDto entry)
			throws CommerceCartModificationException;
	CartModificationData addToProtocol(final AddtoProtocolParam addToCartParams) throws CommerceCartModificationException;
	CartModificationData updateProtocolEntry(long entryNumber, long quantity,final String protocolId, final ProtocolEntryWsDto entry) throws CommerceCartModificationException;
	CartModificationData updateProtocolEntry(long entryNumber, String storeId) throws CommerceCartModificationException;
	Optional<ProtocolData> deleteProtocol(final String protocolId);
	SearchPageData<ProtocolData> getPatientProtocolListData(SearchPageData searchPageData);
	Optional<ProtocolModel> stopSharingProtocol(String protocolId);
	public Optional<ProtocolData> addDuplicateProtocol(final CommerceProtocolParameter parameter)
			throws CommerceCartModificationException;
	public CartModificationData deleteProtocolEntry(final long entryNumber, final long quantity, String protcolId)
			throws CommerceCartModificationException;

	/**
	 * @throws CommerceCartModificationException
	 *
	 */
	Optional<ProtocolData> clone(String recommendationId, String name) throws CommerceCartModificationException;
}
