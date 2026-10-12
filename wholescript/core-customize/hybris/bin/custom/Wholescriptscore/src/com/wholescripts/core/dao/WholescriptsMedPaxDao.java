/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.dao;

import com.wholescripts.core.model.RecommendationModel;
import de.hybris.platform.core.model.ItemModel;

import java.util.List;

import com.wholescripts.core.model.CustomerMaterialInformationModel;
import com.wholescripts.core.model.MedPaxModel;
import com.wholescripts.core.model.MedpaxEntryModel;


/**
 *
 */
public interface WholescriptsMedPaxDao
{
	MedPaxModel getMedPaxForOwner(final ItemModel owner);

	MedPaxModel getMedPaxByCode(final String code);

	MedpaxEntryModel getMedPaxEntryByNumber(final String entryNumber);
   List<CustomerMaterialInformationModel> getCMIRList(final String accountNumber);
}
