/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service;

import java.util.List;
import java.util.Map;

import com.wholescripts.core.model.MedPaxModel;
import com.wholescripts.core.model.MedpaxEntryModel;
import com.wholescripts.data.MedpaxWebKitData;
import com.wholescripts.dto.MedPaxWsDTO;
import com.wholescripts.dto.MedpaxEntryWsDTO;
import com.wholescripts.dto.UpdateMedPaxPacketsInfoWsDTO;
import com.wholescripts.dto.UpdateMedPaxPacketsWsDTO;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;


/**
 *
 */
public interface WholescriptsMedPaxService
{
	MedPaxModel createMedPax(MedPaxModel medPaxModel);

	MedPaxModel updateMedPaxName(MedPaxWsDTO medPaxWsDTO);

	MedPaxModel updateMedPaxNotes(MedPaxWsDTO medPaxWsDTO);

	MedPaxModel getMedPaxForCurrentUser();

	MedPaxModel addEntryToMedpax(MedpaxEntryModel MedPaxEntryModel, final String code);

	MedpaxEntryModel updateMedpax(final MedpaxEntryWsDTO medpaxEntryWsDTO, final String entryNumber);

	MedPaxModel getMedPaxByCode(final String code);

	/**
	 *
	 */
	MedPaxModel updateMedpaxPackets(UpdateMedPaxPacketsWsDTO updateMedPaxPacketsWsDTO);
	MedPaxModel getMedPaxForOwner(String protocolId, String recommendationId, boolean isWSProtocol);

	void updateMedpaxHeader(final MedPaxModel medpax, final Integer box, final Integer packets, final Integer formulas);

	void removeEntryFromMedpax(String medPaxEntryNumber, String code);

	MedPaxModel cloneMedpax(MedPaxModel medPaxModel, CartModel cartModel) throws CommerceCartModificationException;
	Map<ProductModel,String> getCartonsFromCMIR();
	void updateMedpaxForCarton(String productCode, String medpaxCode, String cartonName);
	List<MedpaxWebKitData> getWebKitsFromCMIR();

	MedPaxModel updateMedpaxPacketsInfo(final UpdateMedPaxPacketsInfoWsDTO updateMedPaxPacketsInfoWsDTO);
}
