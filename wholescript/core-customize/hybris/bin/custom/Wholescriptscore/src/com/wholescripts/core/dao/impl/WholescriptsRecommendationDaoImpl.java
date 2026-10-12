/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.dao.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchParameter;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.wholescripts.core.constants.GeneratedWholescriptscoreConstants.Enumerations.CustomerProfileType;
import com.wholescripts.core.constants.WholescriptscoreConstants;
import com.wholescripts.core.dao.WholescriptsRecommendationDao;
import com.wholescripts.core.enums.RecommendationStatus;
import com.wholescripts.core.model.RecommendationModel;
import com.wholescripts.core.service.WholescriptsUserService;
import com.wholescripts.dto.WholescriptsRecommendSearchParams;


/**
 *
 */
public class WholescriptsRecommendationDaoImpl implements WholescriptsRecommendationDao
{
	private static final String GET_RECOMMENDATION_LIST_FOR_PRACTITIONER_NEW = "SELECT {" + RecommendationModel.PK + "}  FROM {"
			+ RecommendationModel._TYPECODE + " as r  JOIN " + UserModel._TYPECODE + " as u ON {r:" + RecommendationModel.USER
			+ "}={u:pk}  JOIN " + RecommendationStatus._TYPECODE + " as s ON {r:" + RecommendationModel.RECOMMENDATIONSTATUS
			+ "}={s:pk}  JOIN " + B2BCustomerModel._TYPECODE + " as bc ON {r:" + RecommendationModel.PATIENT + "}={bc:"
			+ B2BCustomerModel.PK + "}}";

	private static final Logger LOG = Logger.getLogger(WholescriptsRecommendationDaoImpl.class);
	private static final String GET_RECOMMENDATIONS_BY_NAME = "SELECT {r:pk} FROM {Recommendation as r join USER as u ON {r.user}={u.PK}} where {u.uid} =?uid and {r.name} LIKE CONCAT('%',?name,'%')";
	private static final String GET_RECOMMENDATION_LIST_FOR_PRACTITIONER = "SELECT Distinct {r:pk}, {r.date} FROM {Recommendation as r JOIN User as u ON {r.user}={u.PK} JOIN B2BCustomer as bc ON {r.patient}={bc.PK} JOIN B2BUnit AS bu ON {bc.defaultB2bunit}={bu.PK} JOIN RecommendationStatus AS s ON {r.recommendationStatus}={s.pk} JOIN ReferralCodes AS rc ON {rc.b2bUnit}={bu.PK}}";
	private static final String GET_RECOMMENDATION_LIST_FOR_PATIENT = "SELECT Distinct {r:pk}, {r.date} FROM {Recommendation as r join B2BCustomer as u ON {r.patient}={u.PK} JOIN RecommendationStatus as s ON {r.recommendationStatus}={s.pk}} where {u.uid} =?uid";
	private static final String GET_RECOMMENDATION_BY_ID = "SELECT {r:pk} FROM {Recommendation as r join USER as u ON {r.user}={u.PK}} where {u.uid} =?uid and {r.code} =?code ";
	private static final String GET_NEW_RECOMMENDATION_LIST_FOR_PATIENT = "SELECT {r:pk} FROM {Recommendation as r join B2BCustomer as u ON {r.patient}={u.PK} JOIN RecommendationStatus as s ON {r.recommendationStatus}={s.pk}} where {u.uid} =?uid AND {s.code}='SENT' ";
	private static final String GET_RECOMMENDATION_BY_ID_FOR_PATIENT = "SELECT {r:pk} FROM {Recommendation as r join B2BCustomer as u ON {r.patient}={u.PK}} where {u.uid} =?uid and {r.code} =?code ";

	private static final String FIND_RECOMMENDATION_LIST_FOR_STATUS = " AND {s.code} IN (?status)";
	private static final String SEARCH_RECOMMENDATION_BY_DATES = " AND {r.creationTime} >= ?startDate AND {r.creationTime} < ?endDate";
	private static final String FIND_RECOMMENDATION_LIST_FOR_REFERRAL = " AND {rc.referralCode} IN (?codes) AND {rc.lastName} IN (?names)";
	private static final String FIND_RECOMMENDATION_LIST_FOR_TEXT = " AND (LOWER({bc.name}) LIKE LOWER(CONCAT('%',?text,'%')) OR LOWER({bc.uid}) LIKE LOWER(CONCAT('%',?text,'%')))";
	protected static final String RECOMMENDATION_QUERY = "SELECT {recommend:" + RecommendationModel.PK + "} FROM {"
			+ RecommendationModel._TYPECODE + " as recommend}";
	protected static final String WHERE_CODE_CLAUSE = " WHERE {recommend:" + RecommendationModel.CODE + "} =?code";
	protected static final String WHERE_USER_CODE_CLAUSE = " WHERE {u:" + UserModel.UID + "} =?uid";
	private static final String USER_CLAUSE = " WHERE {u.uid}=?uid";
	private static final String PRACTITIONER_DDEFAULT_RECOMMENDATION_LIST = "  AND {s.code} NOT IN ('DISABLED')";
	private static final String GET_LATEST_RECOMMENDATION_LIST_FOR_PRACTITIONER = "SELECT {r:pk} FROM {	Recommendation as r JOIN User as u ON {r.user}={u.PK} JOIN B2BCustomer as bc ON {r.patient}={bc.PK} JOIN RecommendationStatus as s ON {r.recommendationStatus}={s.pk}} where {u.uid}=?uId  order by {r.date} desc";
	private static final String GROUP_BY_RECOMMENDATION = " GROUP BY {r.pk}, {r.date} ";
	
	@Resource(name = "flexibleSearchService")
	public FlexibleSearchService flexibleSearchService;

	@Resource(name = "wholescriptsUserService")
	private WholescriptsUserService wholescriptsUserService;

	@Resource(name = "paginatedFlexibleSearchService")
	private PaginatedFlexibleSearchService paginatedFlexibleSearchService;

	@Resource(name = "searchRestrictionService")
	private SearchRestrictionService searchRestrictionService;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "b2bCustomerService")
	private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;

	@Override
	public RecommendationModel getRecommendationByCode(final String recommendationId)
	{
		// XXX Auto-generated method stub
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(RECOMMENDATION_QUERY + WHERE_CODE_CLAUSE);
		searchQuery.addQueryParameter(RecommendationModel.CODE, recommendationId);
		searchQuery.setCount(1);
		return flexibleSearchService.searchUnique(searchQuery);
	}

	@Override
	public SearchPageData<RecommendationModel> getListRecommendationsModel(final SearchPageData searchPageData,
			final String userId, final WholescriptsRecommendSearchParams searchParam)
	{
		final PaginatedFlexibleSearchParameter parameter = new PaginatedFlexibleSearchParameter();
		final StringBuilder queryBuilder = new StringBuilder();
		final Map<String, Object> queryParameters = new HashMap<>();

		final B2BCustomerModel user = b2bCustomerService.getCurrentB2BCustomer();
		if (user.getCustomerProfile().getCode().equals(CustomerProfileType.PATIENT))
		{
			queryBuilder.append(GET_RECOMMENDATION_LIST_FOR_PATIENT);
			queryParameters.put(CustomerModel.UID, user.getUid());

			// Status Filter
			final Set<String> status = Arrays.stream(Config.getParameter("allowed.PATIENT.status").split(","))
					.collect(Collectors.toSet());
			queryParameters.put("status", status);
			queryBuilder.append(FIND_RECOMMENDATION_LIST_FOR_STATUS);
			queryBuilder.append(GROUP_BY_RECOMMENDATION);

			final FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(queryBuilder.toString(), queryParameters);
			parameter.setFlexibleSearchQuery(fsQuery);
			parameter.setSearchPageData(searchPageData);
			final Map<String, String> sortCodeToQueryAlias = new HashMap<>();
			sortCodeToQueryAlias.put("date".toLowerCase(Locale.getDefault()), "r");
			parameter.setSortCodeToQueryAlias(sortCodeToQueryAlias);
			return paginatedFlexibleSearchService.search(parameter);
		}
		else
		{
			queryBuilder.append(GET_RECOMMENDATION_LIST_FOR_PRACTITIONER);
			queryBuilder.append(USER_CLAUSE);
			queryParameters.put(CustomerModel.UID, user.getUid());

			if (Objects.nonNull(searchParam.getStatus()))
			{
				final Set<String> status = Arrays.stream(searchParam.getStatus().split(","))
						.collect(Collectors.toSet());
				queryParameters.put("status", status);
				queryBuilder.append(FIND_RECOMMENDATION_LIST_FOR_STATUS);
			}
			else
			{
				queryParameters.put("status", RecommendationStatus.DISABLED);
				queryBuilder.append(PRACTITIONER_DDEFAULT_RECOMMENDATION_LIST);
			}

			if (Objects.nonNull(searchParam.getReferral())) {
				List<String> referralPairs = Arrays.asList(URLDecoder.decode(searchParam.getReferral(), StandardCharsets.UTF_8).split(","));

				List<String> codes = new ArrayList<>();
				List<String> names = new ArrayList<>();

				for (String pair : referralPairs) {
					String[] parts = pair.split("\\|");
					if (parts.length == 2) {
						codes.add(parts[0]);  // Extract referral code
						names.add(parts[1]);  // Extract last name
					}
				}

				if (!codes.isEmpty() && !names.isEmpty()) {
					queryParameters.put("codes", codes); // List of referral codes
					queryParameters.put("names", names); // List of last names
					queryBuilder.append(FIND_RECOMMENDATION_LIST_FOR_REFERRAL);
				}
			}


			if (Objects.nonNull(searchParam.getSearchText()))
			{
				queryParameters.put("text", searchParam.getSearchText());
				queryBuilder.append(FIND_RECOMMENDATION_LIST_FOR_TEXT);
			}

			if (StringUtils.isNotEmpty(searchParam.getStartDate()) && StringUtils.isNotEmpty(searchParam.getEndDate()))
			{
				String endDate = getEndDate(searchParam);
				queryParameters.put("startDate", searchParam.getStartDate());
				queryParameters.put("endDate", endDate);
				queryBuilder.append(SEARCH_RECOMMENDATION_BY_DATES);
			}

			queryBuilder.append(GROUP_BY_RECOMMENDATION);
			final FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(queryBuilder.toString(), queryParameters);
			parameter.setFlexibleSearchQuery(fsQuery);
			parameter.setSearchPageData(searchPageData);
			final Map<String, String> sortCodeToQueryAlias = new HashMap<>();
			sortCodeToQueryAlias.put(RecommendationModel.DATE.toLowerCase(Locale.getDefault()), "r");
			parameter.setSortCodeToQueryAlias(sortCodeToQueryAlias);
			searchRestrictionService.disableSearchRestrictions();
			return paginatedFlexibleSearchService.search(parameter);
		}
	}


	private String getEndDate(final WholescriptsRecommendSearchParams searchParam)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try
		{
			date = sdf.parse(searchParam.getEndDate());
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE, 1);
			Date nextDay = cal.getTime();
			return sdf.format(nextDay);
		}
		catch (ParseException e)
		{
			LOG.info(e.getMessage());
		}
		return searchParam.getEndDate();
	}


	public SearchPageData<RecommendationModel> getNewListRecommendationsModel(final SearchPageData searchPageData,
			final String userId)
	{
		final PaginatedFlexibleSearchParameter parameter = new PaginatedFlexibleSearchParameter();
		final StringBuilder queryBuilder = new StringBuilder();
		final Map<String, Object> queryParameters = new HashMap();

		final B2BCustomerModel user = b2bCustomerService.getCurrentB2BCustomer();
			queryBuilder.append(GET_NEW_RECOMMENDATION_LIST_FOR_PATIENT);
		queryParameters.put(CustomerModel.UID, user.getUid());
			final FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(queryBuilder.toString(), queryParameters);
			parameter.setFlexibleSearchQuery(fsQuery);
			parameter.setSearchPageData(searchPageData);
			final Map<String, String> sortCodeToQueryAlias = new HashMap();
			sortCodeToQueryAlias.put(RecommendationModel.DATE.toLowerCase(Locale.getDefault()), "r");
			parameter.setSortCodeToQueryAlias(sortCodeToQueryAlias);
			return paginatedFlexibleSearchService.search(parameter);
	}


	@Override
	public Optional<RecommendationModel> getRecommendationDetails(final String userId, final String recommendationId)
	{
		final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
		final StringBuilder queryBuilder = new StringBuilder();
		final Map<String, Object> queryParams = new HashMap<>();

		if ((customer.getCustomerProfile().getCode().equals(CustomerProfileType.PATIENT)))
		{
			queryBuilder.append(GET_RECOMMENDATION_BY_ID_FOR_PATIENT);
			queryParams.put(RecommendationModel.CODE, recommendationId);
			queryParams.put(CustomerModel.UID, customer.getUid());
		}else {
			queryBuilder.append(GET_RECOMMENDATION_BY_ID);
			queryParams.put(RecommendationModel.CODE, recommendationId);
			queryParams.put(CustomerModel.UID, customer.getUid());
		}
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryBuilder);
		query.addQueryParameters(queryParams);
		final SearchResult<RecommendationModel> result = flexibleSearchService.search(query);

		if (Objects.nonNull(result) && CollectionUtils.isNotEmpty(result.getResult()))
		{
			return Optional.of(result.getResult().get(0));
		}
		return Optional.empty();
	}

	private Date dateConvert(final String convertDate)
	{
		try
		{
			final SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
			final SimpleDateFormat sdf1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
			final Date dt = new SimpleDateFormat(WholescriptscoreConstants.DATE_FORMAT_IN_REQUEST).parse(convertDate);
			final String str1 = sdf.format(dt);
			return sdf1.parse(str1);
		}
		catch (final Exception e)
		{
			LOG.info(e.getMessage());
		}
		return Calendar.getInstance().getTime();
	}

	@Override
	public SearchPageData<RecommendationModel> getRecommendationsForPatient(final SearchPageData searchPageData, String patientId, final WholescriptsRecommendSearchParams searchParam) {
		final PaginatedFlexibleSearchParameter parameter = new PaginatedFlexibleSearchParameter();
		final StringBuilder queryBuilder = new StringBuilder();
		final Map<String, Object> queryParameters = new HashMap();
		queryBuilder.append(GET_RECOMMENDATION_LIST_FOR_PATIENT);
		queryParameters.put(CustomerModel.UID, patientId);

		if (Objects.nonNull(searchParam.getReferral())) {
			List<String> referralPairs = Arrays.asList(searchParam.getReferral().split(","));
			List<String> codes = new ArrayList<>();
			List<String> names = new ArrayList<>();

			for (String pair : referralPairs) {
				String[] parts = pair.split("\\|");
				if (parts.length == 2) {
					codes.add(parts[0]);
					names.add(parts[1]);
				}
			}

			if (!codes.isEmpty() && !names.isEmpty()) {
				queryParameters.put("codes", codes);
				queryParameters.put("names", names);
				queryBuilder.append(FIND_RECOMMENDATION_LIST_FOR_REFERRAL);
			}
		}

		if (StringUtils.isNotEmpty(searchParam.getSearchText())) {
			queryParameters.put("text", searchParam.getSearchText());
			queryBuilder.append(FIND_RECOMMENDATION_LIST_FOR_TEXT);
		}


		if (StringUtils.isNotEmpty(searchParam.getStartDate()) && StringUtils.isNotEmpty(searchParam.getEndDate())) {
			Date startDate = dateConvert(searchParam.getStartDate());
			queryParameters.put("startDate", startDate);
			queryParameters.put("endDate", getEndDate(searchParam));
			queryBuilder.append(SEARCH_RECOMMENDATION_BY_DATES);
		}
		
		queryBuilder.append(GROUP_BY_RECOMMENDATION);
		final FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(queryBuilder.toString(), queryParameters);
		parameter.setFlexibleSearchQuery(fsQuery);
		parameter.setSearchPageData(searchPageData);
		final Map<String, String> sortCodeToQueryAlias = new HashMap<>();
		sortCodeToQueryAlias.put(RecommendationModel.DATE.toLowerCase(Locale.getDefault()), "r");
		parameter.setSortCodeToQueryAlias(sortCodeToQueryAlias);
		searchRestrictionService.disableSearchRestrictions();
		SearchPageData<RecommendationModel> result = paginatedFlexibleSearchService.search(parameter);
		searchRestrictionService.enableSearchRestrictions();
		return result;
	}

	public List<RecommendationModel> getLatestRecommendationsByPractionerId(final String uId){
		final StringBuilder queryBuilder = new StringBuilder();
		final Map<String, Object> queryParams = new HashMap<String, Object>();
		queryBuilder.append(GET_LATEST_RECOMMENDATION_LIST_FOR_PRACTITIONER);
		queryParams.put("uId", uId);
		final SearchResult<RecommendationModel> result = flexibleSearchService.search(queryBuilder.toString(), queryParams);
		return result.getResult().isEmpty() ? null : result.getResult();
	}
}
