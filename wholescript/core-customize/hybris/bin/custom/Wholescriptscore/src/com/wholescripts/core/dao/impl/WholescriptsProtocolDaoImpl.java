/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.dao.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchParameter;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.wholescripts.core.dao.WholescriptsProtocolDao;
import com.wholescripts.core.enums.CustomerProfileType;
import com.wholescripts.core.enums.ProtocolSharedStatus;
import com.wholescripts.core.enums.ProtocolStatus;
import com.wholescripts.core.model.ProtocolModel;
import com.wholescripts.core.utils.WholescriptsCoreUtils;


/**
 *
 */
public class WholescriptsProtocolDaoImpl implements WholescriptsProtocolDao
{
	private static final String GET_PROTOCOLS_BY_NAME = "and LOWER({p.wsName}) LIKE LOWER(CONCAT('%',?wsName,'%'))";
	private static final String GET_PROTOCOLS_LIST = "SELECT {p:pk} FROM {Protocol as p join USER as u ON {p.user}={u.PK}} where {u.uid} =?uid and {p.isWSProtocol}=?isWSProtocol and {p.protocolStatus} IN (?protocolStatus)";
	private static final String GET_PROTOCOL_BY_ID = "SELECT {p:pk} FROM {Protocol as p join USER as u ON {p.user}={u.PK}} where {u.uid} =?uid and {p.code} =?code and {p.isWSProtocol}=?isWSProtocol and {p.protocolStatus} IN (?protocolStatus)";
	private static final String GET_PROTOCOL_NOT_IN_DRAFT = " AND {p.protocolStatus} NOT IN ({{ SELECT {pk} FROM {EnumerationValue} WHERE {code} = 'DRAFT' }})";
	//private static final String GET_PATIENT_PROTOCOLS_LIST = "SELECT {p:pk} FROM {Protocol as p join Protocol2Customer as rel ON {rel:source}={p.pk} join B2bCustomer as u ON {rel.target}={u.pk}} where {u.uid} =?uid and {p.protocolSharedStatus} IN(?protocolSharedStatus)";
	//private static final String GET_PATIENT_PROTOCOL_BY_ID = "SELECT {p:pk} FROM {Protocol as p join Protocol2Customer as rel ON {rel:source}={p.pk} join B2bCustomer as u ON {rel.target}={u.pk}} where {u.uid} =?uid and {p.code} =?code and {p.protocolSharedStatus} IN(?protocolSharedStatus)";
	private static final String GET_PATIENT_PROTOCOL_BY_ID = "SELECT {p:pk} FROM {Protocol as p join B2bCustomer as u ON {p.user}={u.pk}} where {p.user} = ?practitioner and {p.code} = ?code and {p.protocolSharedStatus} IN(?protocolSharedStatus)";
	private static final String GET_WS_PROTOCOLS_LIST = "SELECT {p:pk} FROM {Protocol as p} where {p.isWSProtocol}=?isWSProtocol and {p.protocolStatus} IN (?protocolStatus)";
	private static final String GET_WS_PROTOCOL_BY_CODE = "SELECT {p:pk} FROM {Protocol as p} where {p.code} =?code";
	private static final String GET_WS_PROTOCOL_BY_ID = "SELECT {p:pk} FROM {Protocol as p} where {p.isWSProtocol}=?isWSProtocol and {p.code} =?code and {p.protocolStatus} IN (?protocolStatus)";
	private final String GET_PATIENT_PROTOCOLS_LIST = "SELECT DISTINCT {prt:pk},{prt:wsName},{prt:modifiedtime},{prt.totalPrice},{prt.msrpSubTotalPrice} FROM {Protocol AS prt} WHERE {prt:PK} IN\r\n"
			+ "   (\r\n"
			+ "      {{\r\n"
			+ "        \r\n"
			+ "         SELECT {p:pk} FROM {Protocol as p join Protocol2Customer as rel ON {rel:source}={p.pk} join B2bCustomer as u ON {rel.target}={u.pk} join ProtocolEntry as pe on {pe.protocol}={p.pk}} where {u.uid} = ?patient and {p.protocolSharedStatus} IN (?sharedWithSelected)\r\n"
			+ "      }}\r\n" + "   )\r\n" + "   OR {prt:PK} IN\r\n" + "   (\r\n" + "      {{\r\n" + "         \r\n"
			+ "         SELECT {pr:pk} FROM {Protocol as pr join ProtocolEntry as pte on {pte.protocol}={pr.pk}} where {pr.user} = ?practitoner and {pr.protocolSharedStatus}= ?sharedWithAll }}\r\n"
			+ "   )\r\n" + "   OR {prt:PK} IN\r\n" + "   (\r\n" + "      {{\r\n" + "        \r\n"
			+ "         SELECT {p:pk} FROM {Protocol as p join Protocol2Customer as rel ON {rel:source}={p.pk} join B2bCustomer as u ON {rel.target}={u.pk} join MedPax as mp on {mp.medPaxOwner}={p.pk}} where {u.uid} = ?patient and {p.protocolSharedStatus} IN (?sharedWithSelected)\r\n"
			+ "      }}\r\n"
			+ "   )\r\n"
			+ "   OR {prt:PK} IN\r\n"
			+ "   (\r\n"
			+ "      {{\r\n"
			+ "         \r\n"
			+ "         SELECT {pr:pk} FROM {Protocol as pr join MedPax as mp on {mp.medPaxOwner}={pr.pk}} where {pr.user} = ?practitoner and {pr.protocolSharedStatus}= ?sharedWithAll }}\r\n"
			+ "   )";

	@Resource(name = "paginatedFlexibleSearchService")
	private PaginatedFlexibleSearchService paginatedFlexibleSearchService;
	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;

	@Resource(name = "searchRestrictionService")
	private SearchRestrictionService searchRestrictionService;

	@Resource(name = "b2bUnitService")
	private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;

	@Override
	public <T extends ProtocolModel> SearchPageData<T> getListProtocolsModel(final SearchPageData pageData, final B2BCustomerModel customer, final String protocolName, final boolean isWSPage, final boolean isNonDraft) {
		final boolean isWholescriptsDoctor = WholescriptsCoreUtils.isWholescriptsDoctor(customer);
		final PaginatedFlexibleSearchParameter parameter = new PaginatedFlexibleSearchParameter();
		final StringBuilder queryBuilder = new StringBuilder();
		final Map<String, Object> queryParameters = new HashMap<>();

		setISWSProtcolFlag(isWSPage, isWholescriptsDoctor, queryParameters);

		if (isWSPage) {
			queryBuilder.append(GET_WS_PROTOCOLS_LIST);
		} else {
			queryBuilder.append(GET_PROTOCOLS_LIST);
			queryParameters.put(CustomerModel.UID, customer.getUid());
		}

		if (StringUtils.isNotEmpty(protocolName)) {
			queryBuilder.append(GET_PROTOCOLS_BY_NAME);
			queryParameters.put(ProtocolModel.WSNAME, protocolName);
		}

		if (isNonDraft) {
			queryBuilder.append(GET_PROTOCOL_NOT_IN_DRAFT);
		}

		queryParameters.put(ProtocolModel.PROTOCOLSTATUS, getProtocolStatus(isWholescriptsDoctor, isWSPage));
		final FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(queryBuilder.toString(), queryParameters);
		parameter.setFlexibleSearchQuery(fsQuery);
		parameter.setSearchPageData(pageData);
		final Map<String, String> sortCodeToQueryAlias = new HashMap<>();
		sortCodeToQueryAlias.put(ProtocolModel.MODIFIEDTIME.toLowerCase(Locale.getDefault()), "p");
		sortCodeToQueryAlias.put(ProtocolModel.DATE.toLowerCase(Locale.getDefault()), "p");
		parameter.setSortCodeToQueryAlias(sortCodeToQueryAlias);

		return paginatedFlexibleSearchService.search(parameter);
	}

	@Override
	public Optional<ProtocolModel> getProtocolModelForID(final B2BCustomerModel user, final String protocolId,
			final boolean isWSPage)
	{
		final boolean isWholescriptsDoctor = WholescriptsCoreUtils.isWholescriptsDoctor(user);
		final StringBuilder queryBuilder = new StringBuilder();
		final Map<String, Object> queryParams = new HashMap<>();
		queryParams.put(ProtocolModel.CODE, protocolId);
		queryParams.put(CustomerModel.UID, user.getUid());
		queryParams.put(ProtocolModel.PROTOCOLSTATUS,getProtocolStatus(isWholescriptsDoctor, isWSPage));
		setISWSProtcolFlag(isWSPage, isWholescriptsDoctor, queryParams);
		if (isWSPage)
		{
			queryBuilder.append(GET_WS_PROTOCOL_BY_ID);
		}
		else if (user.getCustomerProfile().equals(CustomerProfileType.PATIENT))
		{
			final B2BCustomerModel practitioner = getPractitionerForPatient(user);
			queryBuilder.append(GET_PATIENT_PROTOCOL_BY_ID);
			queryParams.put("practitioner",practitioner);
			queryParams.put(ProtocolModel.PROTOCOLSHAREDSTATUS, getProtocolSharedStatus());
		}
		else
		{
			queryBuilder.append(GET_PROTOCOL_BY_ID);
		}
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryBuilder);
		query.addQueryParameters(queryParams);
		final SearchResult<ProtocolModel> result = flexibleSearchService.search(query);
		if (Objects.nonNull(result) && CollectionUtils.isNotEmpty(result.getResult()))
		{
			return Optional.of(result.getResult().get(0));
		}
		return Optional.empty();
	}

	@Override
	public SearchPageData<ProtocolModel> getPatientProtocolsModel(final SearchPageData pageData, final String userId, final B2BCustomerModel salesPersonPractitoner)
	{
		final PaginatedFlexibleSearchParameter parameter = new PaginatedFlexibleSearchParameter();
		final StringBuilder queryBuilder = new StringBuilder();
		final Map<String, Object> queryParameters = new HashMap();
		queryBuilder.append(GET_PATIENT_PROTOCOLS_LIST);
		queryParameters.put("patient", userId);
		queryParameters.put("practitoner", salesPersonPractitoner);
		queryParameters.put("sharedWithSelected", getProtocolSharedStatus());
		queryParameters.put("sharedWithAll", ProtocolSharedStatus.SHARED_WITH_ALL);
		final FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(queryBuilder.toString(), queryParameters);
		parameter.setFlexibleSearchQuery(fsQuery);
		parameter.setSearchPageData(pageData);
		final Map<String, String> sortCodeToQueryAlias = new HashMap();
		sortCodeToQueryAlias.put(ProtocolModel.MODIFIEDTIME.toLowerCase(Locale.getDefault()), "prt");
		sortCodeToQueryAlias.put(ProtocolModel.WSNAME.toLowerCase(Locale.getDefault()), "prt");
		sortCodeToQueryAlias.put(ProtocolModel.TOTALPRICE.toLowerCase(Locale.getDefault()), "prt");
		sortCodeToQueryAlias.put(ProtocolModel.MSRPSUBTOTALPRICE.toLowerCase(Locale.getDefault()), "prt");
		parameter.setSortCodeToQueryAlias(sortCodeToQueryAlias);
		return paginatedFlexibleSearchService.search(parameter);
	}

	private List<ProtocolSharedStatus> getProtocolSharedStatus()
	{
		final List<ProtocolSharedStatus> sharedStatus = new ArrayList<>();
		sharedStatus.add(ProtocolSharedStatus.SHARED_WITH_ALL);
		sharedStatus.add(ProtocolSharedStatus.SHARED_WITH_SELECTED);
		return sharedStatus;
	}

	private List<ProtocolStatus> getProtocolStatus(final boolean isDoctor, final boolean isWSPage)
	{
		final List<ProtocolStatus> protocolStatus = new ArrayList<>();
		if (isDoctor)
		{
			protocolStatus.add(ProtocolStatus.PUBLISHED);
			protocolStatus.add(ProtocolStatus.UNPUBLISHED);
			protocolStatus.add(ProtocolStatus.DRAFT);
			protocolStatus.add(ProtocolStatus.COMPLETED);
		}
		else if (isWSPage)
		{
			protocolStatus.add(ProtocolStatus.PUBLISHED);
		}
		else
		{
			protocolStatus.add(ProtocolStatus.COMPLETED);
			protocolStatus.add(ProtocolStatus.DRAFT);
		}
		return protocolStatus;
	}

	private void setISWSProtcolFlag(final boolean isWSPage, final boolean isWholescriptsDoctor,
			final Map<String, Object> queryParameters)
	{
		queryParameters.put(ProtocolModel.ISWSPROTOCOL, 0);
		if(isWholescriptsDoctor || isWSPage) {
			queryParameters.put(ProtocolModel.ISWSPROTOCOL, 1);
		}
	}

	private B2BCustomerModel getPractitionerForPatient(final B2BCustomerModel patient) {

		B2BCustomerModel salesPresonPractitioner = null;
		final String salesPerson = patient.getDefaultB2BUnit() != null ? patient.getDefaultB2BUnit().getSalesPerson() : null;
		final String b2bUnit = salesPerson != null ? WholescriptsCoreUtils.getAccountNumberWithSalesArea(salesPerson.replaceFirst("^0+", "")) : null ;
		searchRestrictionService.disableSearchRestrictions();
		final B2BUnitModel b2bUnitModel = b2bUnit != null ? b2bUnitService.getUnitForUid(b2bUnit) : null;
		final Set<B2BCustomerModel> customers = b2bUnitModel != null ? b2bUnitService.getB2BCustomers(b2bUnitModel) : null;
		searchRestrictionService.enableSearchRestrictions();
		if (CollectionUtils.isNotEmpty(customers)) {
			salesPresonPractitioner = customers.iterator().next();
		}
		return salesPresonPractitioner;

	}
	
	@Override
	public Optional<ProtocolModel> getProtocolModelForCode(final String protocolId)
	{
		final StringBuilder queryBuilder = new StringBuilder();
		final Map<String, Object> queryParams = new HashMap<>();
		queryParams.put(ProtocolModel.CODE, protocolId);
      queryBuilder.append(GET_WS_PROTOCOL_BY_CODE);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryBuilder);
		query.addQueryParameters(queryParams);
		final SearchResult<ProtocolModel> result = flexibleSearchService.search(query);
		if (Objects.nonNull(result) && CollectionUtils.isNotEmpty(result.getResult()))
		{
			return Optional.of(result.getResult().get(0));
		}
		return Optional.empty();
	}

}
