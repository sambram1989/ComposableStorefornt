package com.wholescripts.core.dao.impl;

import com.wholescripts.core.dao.ReferralCodesDao;
import com.wholescripts.core.model.ProtocolModel;
import com.wholescripts.core.model.ReferralCodesModel;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchParameter;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchService;

import java.util.*;

import de.hybris.platform.util.Config;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.collections.CollectionUtils;



import javax.annotation.Resource;

public class ReferralCodesDaoImpl implements ReferralCodesDao {
    @Resource(name = "flexibleSearchService")
    private FlexibleSearchService flexibleSearchService;

    @Resource(name = "paginatedFlexibleSearchService")
    private PaginatedFlexibleSearchService paginatedFlexibleSearchService;
    private static final String REFERRAL_CODES = "SELECT {r.pk} FROM {ReferralCodes AS r JOIN B2BUnit AS b2b ON {r.b2bUnit} = {b2b.pk} JOIN B2BCustomer AS c ON {c.defaultB2BUnit} = {b2b.pk}} WHERE {c.uid} = ?uId";

    private static final String GET_REFERRAL_CODES_LIST = "SELECT {r.pk} FROM {ReferralCodes AS r JOIN B2BUnit AS b2b ON {r.b2bUnit} = {b2b.pk} JOIN B2BCustomer AS c ON {c.defaultB2BUnit} = {b2b.pk}} WHERE {c.uid} = ?uId";

    private static final String GET_REFERRAL_CODE_BY_ID = "SELECT {r.pk} FROM {ReferralCodes AS r JOIN B2BUnit AS b2b ON {r.b2bUnit} = {b2b.pk} JOIN SapCustomerGroupEnum AS scg ON {b2b.sapCustomerGroup} = {scg.pk} JOIN B2BCustomer AS c ON {c.defaultB2BUnit} = {b2b.pk}} WHERE {r.referralCode} = ?referralCode AND {r.lastName} = ?lastName AND {scg.code} = ?sapCustomerGroup";

    private static final String GET_REFERRAL_CODE_BY_LINK = "SELECT {r.pk} FROM {ReferralCodes AS r} " + "WHERE LOWER({r.referralLink}) = LOWER(?referralLink)";

    private static final String SAP_PRACTITIONER_CUSTOMER_GROUP = "wholescripts.sapCustomerGroup..practitioner.code";

    @Override
    public List<ReferralCodesModel> findReferralCodesByUser(String uId) {
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(REFERRAL_CODES);
        searchQuery.addQueryParameter("uId", uId);
        SearchResult<ReferralCodesModel> search = flexibleSearchService.search(searchQuery);
        return search.getResult();
    }


    @Override
    public <T extends ReferralCodesModel> SearchPageData<T> getReferralCodesListByUser(final SearchPageData pageData, String uId) {
        final PaginatedFlexibleSearchParameter parameter = new PaginatedFlexibleSearchParameter();
        final StringBuilder queryBuilder = new StringBuilder();
        final Map<String, Object> queryParameters = new HashMap<>();
        if (StringUtils.isNotEmpty(uId)) {
            queryBuilder.append(GET_REFERRAL_CODES_LIST);
        } else {
            throw new IllegalArgumentException("User ID cannot be null or empty.");
        }
        queryParameters.put(CustomerModel.UID, uId);
        final FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(queryBuilder.toString(), queryParameters);
        parameter.setFlexibleSearchQuery(fsQuery);
        parameter.setSearchPageData(pageData);
        final Map<String, String> sortCodeToQueryAlias = new HashMap<>();
        sortCodeToQueryAlias.put(ReferralCodesModel.MODIFIEDTIME.toLowerCase(Locale.getDefault()), "r");
        parameter.setSortCodeToQueryAlias(sortCodeToQueryAlias);
        return paginatedFlexibleSearchService.search(parameter);
    }


    @Override
    public Optional<ReferralCodesModel> getReferralCodeModelForID(final String referralCode,String lastName)
    {
        final StringBuilder queryBuilder = new StringBuilder();
        final Map<String, Object> queryParams = new HashMap<>();
        final FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(queryBuilder.toString());
        queryParams.put(ReferralCodesModel.REFERRALCODE, referralCode);
        queryParams.put(ReferralCodesModel.LASTNAME, lastName);
        queryParams.put("sapCustomerGroup", Config.getParameter(SAP_PRACTITIONER_CUSTOMER_GROUP));
        queryBuilder.append(GET_REFERRAL_CODE_BY_ID);
        final FlexibleSearchQuery query = new FlexibleSearchQuery(queryBuilder);
        query.addQueryParameters(queryParams);
        final SearchResult<ReferralCodesModel> result = flexibleSearchService.search(query);
        if (Objects.nonNull(result) && CollectionUtils.isNotEmpty(result.getResult()))
        {
            return Optional.of(result.getResult().get(0));
        }
        return Optional.empty();

    }
    @Override
    public List<ReferralCodesModel> getReferralCodesCSV(String uId){
        try {
            final StringBuilder queryBuilder = new StringBuilder();
            final Map<String, Object> queryParameters = new HashMap<>();
            if (StringUtils.isNotEmpty(uId)) {
                queryBuilder.append(GET_REFERRAL_CODES_LIST);
            } else {
                throw new IllegalArgumentException("User ID cannot be null or empty.");
            }
            queryParameters.put(CustomerModel.UID, uId);

            final FlexibleSearchQuery query = new FlexibleSearchQuery(queryBuilder.toString());
            query.addQueryParameters(queryParameters);
            final SearchResult<ReferralCodesModel> result = flexibleSearchService.search(query);
            return result.getResult();
        }catch (final Exception e) {
            return Collections.emptyList();

        }


    }

    @Override
    public Optional<ReferralCodesModel> getReferralLinkForID(String referralLink) {
        final Map<String, Object> queryParams = new HashMap<>();
        queryParams.put(ReferralCodesModel.REFERRALLINK, referralLink);
        final FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(GET_REFERRAL_CODE_BY_LINK);
        fsQuery.addQueryParameters(queryParams);
        final SearchResult<ReferralCodesModel> result = flexibleSearchService.search(fsQuery);
        if (Objects.nonNull(result) && CollectionUtils.isNotEmpty(result.getResult()))
        {
            return Optional.of(result.getResult().get(0));
        }
        return Optional.empty();
    }
}







