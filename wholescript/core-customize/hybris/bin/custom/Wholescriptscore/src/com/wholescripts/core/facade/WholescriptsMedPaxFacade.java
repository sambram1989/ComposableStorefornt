/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.facade;

import java.util.List;

import com.wholescripts.data.*;
import com.wholescripts.dto.MedPaxWsDTO;
import com.wholescripts.dto.MedpaxEntryWsDTO;
import com.wholescripts.dto.UpdateMedPaxPacketsInfoWsDTO;
import com.wholescripts.dto.UpdateMedPaxPacketsWsDTO;


/**
 *
 */
public interface WholescriptsMedPaxFacade
{

	MedPaxData createMedPax(MedPaxWsDTO medPaxWsDTO);

	MedPaxData updateMedPaxName(MedPaxWsDTO medPaxWsDTO);

	MedPaxData updateMedPaxNotes(MedPaxWsDTO medPaxWsDTO);

	MedPaxData getMedPaxForCurrentUser();

	MedPaxData addEntryToMedpax(MedpaxEntryWsDTO medpaxEntryWsDTO, final String code);

	MedpaxEntryData updateMedpax(final MedpaxEntryWsDTO medpaxEntryWsDTO, final String entryNumber);

	ReviewMedPaxData getPacketsByMexpaxCode(final String code);

	ReviewMedPaxData updateMedpaxPackets(UpdateMedPaxPacketsWsDTO updateMedPaxPacketsWsDTO);
	MedPaxData getMedPaxForOwner(final String protocolId, final String recommendationId, boolean isWSProtocol);
   MedPaxData getMedPaxByCode(final String medpaxId);
	void removeEntryFromMedpax(final String medPaxEntryNumber, final String code);
	void removeMedpax(final String code);
	public List<MedpaxCartonData> getCartonsData();
	public void updateMedpaxForCarton(final String productCode,final String medpaxCode, final String cartonName);
	List<MedpaxWebKitData> getPrivateLabelWebKits();

	ReviewMedPaxData updateMedpaxPacketsInfo(final UpdateMedPaxPacketsInfoWsDTO updateMedPaxPacketsInfoWsDTO);

	OrderedMedpaxProductsData getOrderedMedpaxProductsForUser(final String uId,final String productSearch);

}
