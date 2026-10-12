/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.dao.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchParameter;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.net.URLDecoder;


import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wholescripts.core.dao.WholescriptsUserDao;
import com.wholescripts.core.enums.CustomerProfileType;
import com.wholescripts.core.enums.ProtocolSharedStatus;
import com.wholescripts.core.model.CustomerMaterialInformationModel;
import com.wholescripts.core.model.ProtocolModel;
import com.wholescripts.core.model.RecommendationModel;
import com.wholescripts.core.utils.WholescriptsCoreUtils;


/**
 *
 */
public class WholescriptsUserDaoImpl implements WholescriptsUserDao
{
    private static final Logger LOG = LogManager.getLogger(WholescriptsUserDaoImpl.class);

    private final static String ADDRESS_GROUP_CODE = "address.api.sapcustomer.group.codes";

	protected static final String USER_QUERY = "SELECT {user:" + B2BCustomerModel.PK + "} FROM {" + B2BCustomerModel._TYPECODE
			+ " as user}";
	protected static final String WHERE_CODE_CLAUSE = " WHERE {user:" + B2BCustomerModel.UID + "} =?uid";

	//	private static final String FIND_ALL_PATIENTS_QUERY = "SELECT {b2bPat.pk} FROM {B2BCustomer as b2bPat"
	//			+ " JOIN CustomerProfileType as cpt ON {b2bPat.customerProfile}= {cpt.pk}} WHERE {b2bPat.referralCode} IN ({{"
	//			+ " SELECT {referralCode} from {ReferralCodes as rc JOIN Dispensary as d ON {rc.dispensary}={d.pk}} where {d.id}=?dispensaryId"
	//			+ " }}) AND {cpt.code} =?customerProfile ORDER BY {b2bPat.name}";


    private static final String SEARCH_PATIENTS_QUERY = "SELECT {b2bPat.pk} FROM {B2BCustomer AS b2bPat " +
            "JOIN CustomerProfileType AS cpt ON {b2bPat.customerProfile} = {cpt.pk} " +
            "JOIN B2BUnit AS b2b ON {b2bPat.defaultB2BUnit} = {b2b.pk} ";
    private static final String SEARCH_REFERRAL_JOIN_TO_PATIENTS_QUERY= " JOIN ReferralCodes AS rc ON {b2b.pk} = {rc.b2bUnit}";
    private static final String SEARCH_SALES_PERSON =" WHERE {b2b.salesPerson} = ?salesPersonId";
    private static final String SEARCH_PATIENTS_CUSTOMER_PROFILE = " AND {cpt.code} = ?customerProfile";
    public static final String UNASSIGNED_REFERRAL_QUERY = " AND NOT EXISTS ({{ SELECT {rc.pk} FROM {ReferralCodes AS rc} WHERE {rc.b2bUnit} = {b2b.pk} }})";
    private static final String STRING_REFERRAL_CODE_QUERY = " AND {rc.referralCode} IN (?codes) AND {rc.lastName} IN (?lastNames)";
    private static final String STRING_DISCOUNT_QUERY = " {b2b.discount} = '";
    private static final String STRING_OR_QUERY = "' OR ";
    private static final String STRING_NAME_LIKE_QUERY = " AND (LOWER({b2bPat.name}) LIKE LOWER('%";
    private static final String STRING_UID_LIKE_QUERY = " OR LOWER({b2bPat.uid}) LIKE LOWER('%";
    private static final String STRING_NAME_LIKE_QUERY_CLOSING = "%') ";
    private static final String STRING_CLOSING_BRACKET = "%'))";
    private static final String ORDERBY_NAME_PATIENTS_QUERY = " ORDER BY {b2bPat.name}";
    private static final String USER_QUERY_BY_CUSTOMER_PROFILE = "SELECT {b2bc.pk} FROM {B2BCustomer as b2bc JOIN CustomerProfileType as cpt ON {b2bc.customerProfile}= {cpt.pk}} WHERE {cpt.code} = ?profileType";
    private static final String GET_ALL_ADDRESS_FOR_CUSTOMER="SELECT {a.pk} FROM { Address as a	join B2BUnit as customer on {a.owner}={customer.pk} join sapcustomergroupenum as customerGroup on {customer.sapcustomergroup}={customerGroup.pk}} where {customer.id}=?customerId and {customerGroup.code} in (?sapCustomerGroups) and  {customer.uid} like ?customerUid and {customer.active} = 1";
    private static final String SEARCH_QUERY_FIRSTNAME="   AND ( LOWER({a.firstname}) LIKE LOWER(CONCAT('%',?firstname,'%'))";
    private static final String SEARCH_QUERY_LASTNAME="  OR LOWER({a.lastname}) LIKE LOWER(CONCAT('%',?lastname,'%'))";
    private static final String SEARCH_QUERY_STREET="  OR LOWER({a.streetname}) LIKE LOWER(CONCAT('%',?streetname,'%'))";
    private static final String SEARCH_QUERY_TOWN="  OR LOWER({a.town}) LIKE LOWER(CONCAT('%',?town,'%'))";
    private static final String SEARCH_QUERY_POSTALCODE="  OR LOWER({a.postalcode}) LIKE LOWER(CONCAT('%',?postalcode,'%')) )";

    private static final String GET_PATIENT_RECOMMENDATIONS = "SELECT {r.pk} FROM {Recommendation as r join B2BCustomer as c ON {r.patient}={c.PK}} where {r. user}=?user";
    private static final String GET_PATIENT_PROTOCOLS ="SELECT {p:pk} FROM {Protocol as p join Protocol2Customer as rel ON {rel:source}={p. pk} join B2bCustomer as u ON {rel. target}={u. pk}} where {p. protocolSharedStatus} IN(?protocolSharedStatus) and  {p.user} =?user and {u.pk}=?pk";


    private static final String SEARCH_PATIENTS_FOR_PRACTITONER_QUERY = "SELECT {b2bPat.pk} FROM {B2BCustomer AS b2bPat JOIN CustomerProfileType AS cpt ON {b2bPat.customerProfile} = {cpt.pk} JOIN B2BUnit AS b2b ON {b2bPat.defaultB2BUnit} = {b2b.pk}} WHERE {b2b.salesPerson} = ?salesPersonId  AND {cpt.code} = ?customerProfile";
    private static final String SHARED_ALL_PROTOCOLS = "Select {p.pk} from {Protocol as p JOIN ProtocolSharedStatus as pss on {p.protocolSharedStatus}={pss.pk}} WHERE {pss.code} = ?sharedStatus";	 
    private static final String GET_PATIENT_DETAILS="SELECT {cust.pk} FROM {B2BCustomer as cust JOIN b2bunit as unit ON {cust.defaultb2bunit}= {unit.pk}} WHERE {cust.uid} = ?uid and {unit.salesPerson}=?salesPerson";
    
    @Resource(name = "searchRestrictionService")
	 private SearchRestrictionService searchRestrictionService;
    
	 @Resource(name = "flexibleSearchService")
	 public FlexibleSearchService flexibleSearchService;

	 @Resource(name = "paginatedFlexibleSearchService")
	 private PaginatedFlexibleSearchService paginatedFlexibleSearchService;

    @Resource (name = "configurationService")
    private ConfigurationService configurationService;

    @Override
    public <T extends B2BCustomerModel> SearchPageData<T> getAllPatientsForPractitioner(
            final SearchPageData<CustomerData> searchPageData, final String salesPersonId, final String referralCode,
            final String discount, final String searchText) {

        final StringBuilder queryBuilder = new StringBuilder(SEARCH_PATIENTS_QUERY);

        final boolean joinReferral = (!"UNASSIGNED".equalsIgnoreCase(referralCode)) &&
                (StringUtils.isNotEmpty(referralCode) || StringUtils.isNotEmpty(discount));

        if (joinReferral) {
            queryBuilder.append(SEARCH_REFERRAL_JOIN_TO_PATIENTS_QUERY).append(" ");
        }
        queryBuilder.append("}").append(SEARCH_SALES_PERSON).append(SEARCH_PATIENTS_CUSTOMER_PROFILE);

        final Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("salesPersonId", salesPersonId);
        queryParams.put("customerProfile", CustomerProfileType.PATIENT.getCode());

        if ("UNASSIGNED".equalsIgnoreCase(referralCode)) {
            queryBuilder.append(UNASSIGNED_REFERRAL_QUERY);
        } else if (joinReferral) {

            if (StringUtils.isNotEmpty(referralCode)) {
                List<String> codes = new ArrayList<>();
                List<String> names = new ArrayList<>();

                String[] referralPairs = URLDecoder.decode(referralCode, StandardCharsets.UTF_8).split(",");
                for (String pair : referralPairs) {
                    String[] parts = pair.trim().split("\\|");
                    if (parts.length == 2) {
                        codes.add(parts[0].trim());
                        names.add(parts[1].trim());
                    }
                }

                if (!codes.isEmpty() && !names.isEmpty()) {
                    queryBuilder.append(STRING_REFERRAL_CODE_QUERY);
                    queryParams.put("codes", codes);
                    queryParams.put("lastNames", names);
                }
            }

            if (StringUtils.isNotEmpty(discount)) {
                List<String> discounts = Arrays.asList(discount.split(","));
                List<String> discountConditions = discounts.stream().map(d -> STRING_DISCOUNT_QUERY + d.trim() + "'").collect(Collectors.toList());
                if (!discountConditions.isEmpty()) {
                    queryBuilder.append(" AND (").append(String.join(" OR ", discountConditions)).append(")");
                }
            }
        }
        if (StringUtils.isNotEmpty(searchText)) {
            queryBuilder.append(STRING_NAME_LIKE_QUERY).append(searchText).append(STRING_NAME_LIKE_QUERY_CLOSING).append(STRING_UID_LIKE_QUERY).append(searchText).append(STRING_CLOSING_BRACKET);
        }

        queryBuilder.append(ORDERBY_NAME_PATIENTS_QUERY);
        final FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(queryBuilder.toString(),queryParams);
        final PaginatedFlexibleSearchParameter parameter = new PaginatedFlexibleSearchParameter();
        parameter.setFlexibleSearchQuery(fsQuery);
        if (searchPageData != null) {
      	  parameter.setSearchPageData(searchPageData);
        }
        
        final Map<String, String> sortMap = new HashMap<>();
        parameter.setSortCodeToQueryAlias(sortMap);

        searchRestrictionService.disableSearchRestrictions();
        final SearchPageData result = paginatedFlexibleSearchService.search(parameter);
        searchRestrictionService.enableSearchRestrictions();

        return result;
    }


    public B2BCustomerModel getUserByUid(final String user)
	 {
		 final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(USER_QUERY + WHERE_CODE_CLAUSE);
		 searchQuery.addQueryParameter("uid", user);
		 searchQuery.setCount(1);
		 searchRestrictionService.disableSearchRestrictions();
		 final B2BCustomerModel result = flexibleSearchService.searchUnique(searchQuery);
		 searchRestrictionService.enableSearchRestrictions();
		 return result;
	 }

    public SearchResult<B2BCustomerModel> getUserByCustomerProfile(final String customerProfile) {

        final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(USER_QUERY_BY_CUSTOMER_PROFILE);
        searchQuery.addQueryParameter("profileType", customerProfile);
        searchRestrictionService.disableSearchRestrictions();
		  final SearchResult<B2BCustomerModel> result = flexibleSearchService.search(searchQuery);
		  searchRestrictionService.enableSearchRestrictions();
		  return result;

    }

    @Override
    public List<AddressModel> getAllAddressForCustomers(B2BUnitModel customer, String addressSearch) {
        String sapCustomerGroupCodes= configurationService.getConfiguration().getString(ADDRESS_GROUP_CODE);
        List<String> sapCustomerGroupList = new ArrayList<>(Arrays.asList(sapCustomerGroupCodes.split(",")));

        if(customer.getSapCustomerGroup() !=null){
            sapCustomerGroupList.add(customer.getSapCustomerGroup().getCode());
        }

        final PaginatedFlexibleSearchParameter parameter = new PaginatedFlexibleSearchParameter();
        final StringBuilder queryBuilder = new StringBuilder();
        final Map<String, Object> queryParameters = new HashMap();
        queryBuilder.append(GET_ALL_ADDRESS_FOR_CUSTOMER);

        queryParameters.put("customerId", WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(customer.getUid()).replaceFirst("^0+", ""));
        queryParameters.put("sapCustomerGroups", sapCustomerGroupList);
        queryParameters.put("customerUid","%1710_10_10");

        if (StringUtils.isNotEmpty(addressSearch))
        {
            queryBuilder.append(SEARCH_QUERY_FIRSTNAME);
            queryParameters.put("firstname",addressSearch);
            queryBuilder.append(SEARCH_QUERY_LASTNAME);
            queryParameters.put("lastname",addressSearch);
            queryBuilder.append(SEARCH_QUERY_STREET);
            queryParameters.put("streetname",addressSearch);
            queryBuilder.append(SEARCH_QUERY_TOWN);
            queryParameters.put("town",addressSearch);
            queryBuilder.append(SEARCH_QUERY_POSTALCODE);
            queryParameters.put("postalcode",addressSearch);

        }
        final FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(queryBuilder.toString(), queryParameters);
        searchRestrictionService.disableSearchRestrictions();
        SearchResult<AddressModel> searchResult = flexibleSearchService.search(fsQuery);
        searchRestrictionService.enableSearchRestrictions();
        return searchResult.getResult().isEmpty() ? Collections.emptyList() : searchResult.getResult();
    }

    @Override
    public List<RecommendationModel> getRecommendationForPatient(B2BCustomerModel practitioner) {
        final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_PATIENT_RECOMMENDATIONS);
        query.addQueryParameter("user", practitioner);
        final SearchResult<RecommendationModel> result = flexibleSearchService.search(query);
        return result.getResult();
    }

    @Override
    public List<ProtocolModel> getProtocolsForPatient(B2BCustomerModel patient, B2BCustomerModel practitioner) {
        final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_PATIENT_PROTOCOLS);
        query.addQueryParameter(ProtocolModel.PROTOCOLSHAREDSTATUS, getProtocolSharedStatus());
        query.addQueryParameter("user", practitioner);
        query.addQueryParameter("pk", patient);
        final SearchResult<ProtocolModel> result = flexibleSearchService.search(query);
        return result.getResult();
    }
    private List<ProtocolSharedStatus> getProtocolSharedStatus()
    {
        final List<ProtocolSharedStatus> sharedStatus = new ArrayList<>();
        sharedStatus.add(ProtocolSharedStatus.SHARED_WITH_ALL);
        sharedStatus.add(ProtocolSharedStatus.SHARED_WITH_SELECTED);
        return sharedStatus;
    }

    @Override
    public List<B2BUnitModel> getAllB2bUnits() {
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT DISTINCT {b2bunit.pk} FROM {B2BUnit AS b2bunit}");
        SearchResult<B2BUnitModel> result = flexibleSearchService.search(fQuery);
        if (Objects.nonNull(result.getResult()) && result.getCount() > 0) {
            return result.getResult();
        } else {
            return null;
        }
    }

    @Override
    public List<CustomerMaterialInformationModel> getDistinctAccountNumberFromCustomerMaterial() {
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT DISTINCT {cmi.pk} FROM {CustomerMaterialInformation AS cmi}");
        SearchResult<CustomerMaterialInformationModel> result = flexibleSearchService.search(fQuery);
        if (Objects.nonNull(result.getResult()) && result.getCount() > 0) {
            return result.getResult();
        } else {
            return null;
        }
    }
 
    @Override
    public List<B2BCustomerModel> getAllPatientsForPractitioner(final String salesPersonId) {
   	 
   	 FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(SEARCH_PATIENTS_FOR_PRACTITONER_QUERY);
       searchQuery.addQueryParameter("salesPersonId", salesPersonId);
       searchQuery.addQueryParameter("customerProfile", CustomerProfileType.PATIENT.getCode());
       searchRestrictionService.disableSearchRestrictions();
       SearchResult<B2BCustomerModel> searchResult = flexibleSearchService.search(searchQuery);
       searchRestrictionService.enableSearchRestrictions();
       return searchResult.getResult().isEmpty() ? Collections.emptyList() : searchResult.getResult();
        
    }
    
    @Override
    public List<ProtocolModel> getSharedAllProtocals() {
   	 
   	 FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(SHARED_ALL_PROTOCOLS);
       searchQuery.addQueryParameter("sharedStatus", ProtocolSharedStatus.SHARED_WITH_ALL.getCode());
       SearchResult<ProtocolModel> searchResult = flexibleSearchService.search(searchQuery);
       return searchResult.getResult().isEmpty() ? Collections.emptyList() : searchResult.getResult();
        
    }

	@Override
	public B2BCustomerModel getPatientModelByID(String patientID, String salesPersonID)
	{
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(GET_PATIENT_DETAILS);
		 searchQuery.addQueryParameter(B2BCustomerModel.UID, patientID);
		 searchQuery.addQueryParameter(B2BUnitModel.SALESPERSON, salesPersonID);
		 searchQuery.setCount(1);
		 searchRestrictionService.disableSearchRestrictions();
		 final B2BCustomerModel result = flexibleSearchService.searchUnique(searchQuery);
		 searchRestrictionService.enableSearchRestrictions();
		 return result;
	}
}
