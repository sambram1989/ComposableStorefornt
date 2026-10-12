/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.dao;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;

import java.util.Optional;

import com.wholescripts.core.model.ProtocolModel;

/**
 *
 */
public interface WholescriptsProtocolDao
{
	<T extends ProtocolModel> SearchPageData<T> getListProtocolsModel(SearchPageData pageData,B2BCustomerModel customer,String protocolName,final boolean isWSPage,final boolean isNonDraft);
	Optional<ProtocolModel> getProtocolModelForID(final B2BCustomerModel customer, String protocolId,final boolean isWSPage);
	<T extends ProtocolModel> SearchPageData<T> getPatientProtocolsModel(final SearchPageData pageData, final String userId, final B2BCustomerModel salesPersonPractitoner);
	Optional<ProtocolModel> getProtocolModelForCode(String protocolId);
}
