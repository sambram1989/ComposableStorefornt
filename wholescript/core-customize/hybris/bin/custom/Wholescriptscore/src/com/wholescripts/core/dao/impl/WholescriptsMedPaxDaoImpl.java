/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.dao.impl;

import com.wholescripts.core.model.RecommendationModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.wholescripts.core.dao.WholescriptsMedPaxDao;
import com.wholescripts.core.model.CustomerMaterialInformationModel;
import com.wholescripts.core.model.MedPaxModel;
import com.wholescripts.core.model.MedpaxEntryModel;


/**
 *
 */
public class WholescriptsMedPaxDaoImpl implements WholescriptsMedPaxDao
{

	private final static String FIND_MEDPAX_BY_USER = "SELECT {" + MedPaxModel.PK + "} FROM {" + MedPaxModel._TYPECODE
			+ "} WHERE {" + MedPaxModel.MEDPAXOWNER + "}=?owner";

	private final static String FIND_MEDPAX_ID = "SELECT {" + MedPaxModel.PK + "} FROM {" + MedPaxModel._TYPECODE + "} WHERE {"
			+ MedPaxModel.CODE + "}=?code";
	private final static String FIND_MEDPAX_ENTRY_NUMBER = "SELECT {" + MedpaxEntryModel.PK + "} FROM {"
			+ MedpaxEntryModel._TYPECODE + "} WHERE {" + MedpaxEntryModel.ENTRYNUMBER + "}=?entryNumber";

	private final static String FIND_CMIR_FOR_ACCOUNT = "SELECT {" + CustomerMaterialInformationModel.PK + "} FROM {" + CustomerMaterialInformationModel._TYPECODE
			+ "} WHERE {" + CustomerMaterialInformationModel.ACCOUNT + "}=?account";
	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;

	@Override
	public MedPaxModel getMedPaxForOwner(final ItemModel owner)
	{
		final StringBuilder queryBuilder = new StringBuilder();
		final Map<String, Object> queryParams = new HashMap<String, Object>();
		queryBuilder.append(FIND_MEDPAX_BY_USER);
		queryParams.put("owner", owner);
		final SearchResult<MedPaxModel> result = flexibleSearchService.search(queryBuilder.toString(), queryParams);
		return result.getResult().isEmpty() ? null : result.getResult().get(0);
	}


	@Override
	public MedPaxModel getMedPaxByCode(final String code)
	{
		final StringBuilder queryBuilder = new StringBuilder();
		final Map<String, Object> queryParams = new HashMap<String, Object>();
		queryBuilder.append(FIND_MEDPAX_ID);
		queryParams.put("code", code);
		final SearchResult<MedPaxModel> result = flexibleSearchService.search(queryBuilder.toString(), queryParams);
		return result.getResult().isEmpty() ? null : result.getResult().get(0);
	}

	@Override
	public MedpaxEntryModel getMedPaxEntryByNumber(final String entryNumber)
	{
		final StringBuilder queryBuilder = new StringBuilder();
		final Map<String, Object> queryParams = new HashMap<String, Object>();
		queryBuilder.append(FIND_MEDPAX_ENTRY_NUMBER);
		queryParams.put("entryNumber", entryNumber);
		final SearchResult<MedpaxEntryModel> result = flexibleSearchService.search(queryBuilder.toString(), queryParams);
		return result.getResult().isEmpty() ? null : result.getResult().get(0);
	}


	@Override
	public List<CustomerMaterialInformationModel> getCMIRList(final String accountNumber)
	{
		final StringBuilder queryBuilder = new StringBuilder();
		final Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put(CustomerMaterialInformationModel.ACCOUNT, accountNumber);
		queryBuilder.append(FIND_CMIR_FOR_ACCOUNT);
		final SearchResult<CustomerMaterialInformationModel> result = flexibleSearchService.search(queryBuilder.toString(),queryParams);
		return result.getResult().isEmpty() ? Collections.EMPTY_LIST : result.getResult() ;
	}

}
