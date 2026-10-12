package com.wholescripts.core.dao.impl;

import com.wholescripts.core.utils.WholescriptsCoreUtils;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.wholescripts.core.dao.WholescriptsExternalAPIDao;
import com.wholescripts.core.model.BrandCustomerClassRestrictionModel;
import com.wholescripts.core.model.CustomerClassProductRestrictionModel;
import com.wholescripts.core.model.CustomerProductGroupRestrictionModel;
import com.wholescripts.core.model.CustomerProductRestrictionModel;
import com.wholescripts.core.model.RecommendationModel;
import com.wholescripts.core.model.RegionProductRestrictionModel;

/**
 * @author Sadiq
 */
public class WholescriptsExternalAPIDaoImpl implements WholescriptsExternalAPIDao {

    @Resource (name = "configurationService")
    private ConfigurationService configurationService;

    private final static String ADDRESS_GROUP_CODE = "address.api.sapcustomer.group.codes";

    private static final String BRAND_CUSTOMER_CLASS_RESTRICTION_QUERY = "SELECT {pk} FROM {BrandCustomerClassRestriction}";
    private static final String CUSTOMER_CLASS_PRODUCT_RESTRICTION_QUERY = "SELECT {pk} FROM {CustomerClassProductRestriction}";
    private static final String CUSTOMER_PRODUCT_RESTRICTION_QUERY = "SELECT {pk} FROM {CustomerProductRestriction}";
    private static final String CUSTOMER_PRODUCT_RESTRICTION_GROUP_QUERY = "SELECT {pk} FROM {CustomerProductGroupRestriction}";
    private static final String REGION_PRODUCT_RESTRICTION_QUERY = "SELECT {pk} FROM {RegionProductRestriction}";

    private static final String EXTERNAL_RECOMMENDATION_QUERY = "SELECT {r:pk} FROM {Recommendation AS r} WHERE {r.user} = ?practitioner";
    private static final String EXTERNAL_RECOMMENDATION_QUERY_WITH_PATIENT = " AND {r.patient} = ?patient ";
    private static final String EXTERNAL_RECOMMENDATION_QUERY_ID_CONDITION = " And {r.code} = ?code ";
    
    private static final String GET_ALL_ADDRESS_FOR_CUSTOMER = "SELECT {customer.pk} FROM {B2BUnit as customer join sapcustomergroupenum as customerGroup on {customer.sapcustomergroup}={customerGroup.pk} } where {customer.id}=?customerId and {customerGroup.code} in (?sapCustomerGroups) and  {customer.uid} like ?customerUid and {customer.active} = 1";

    private static final String GET_ALL_PATIENTS_FOR_CUSTOMER = "SELECT {b2bUnit.pk} FROM {B2BUnit as b2bUnit  join sapCustomerGroupEnum as customer on {b2bUnit.sapCustomerGroup}={customer.pk} } where {customer.code}=?customerGroup and {b2bUnit.salesPerson}=?salesPerson";

    private final String CATEGORY_QUERY = "SELECT {c.pk} FROM {Category AS c} where {c.catalogVersion}=?catalogVersion AND {c.code} LIKE ?sapCodeLike ";
    
    private final String BRAND_QUERY = "SELECT {b.name} FROM {WholescriptsBrand AS b} where {b.code} LIKE ?sapCodeLike ";
    
    @Resource(name = "flexibleSearchService")
    private FlexibleSearchService flexibleSearchService;

    

    @Override
    public List<String> getAllProductsBrandNames() {
        
        FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(BRAND_QUERY);
        fsQuery.addQueryParameter("sapCodeLike", "Z_MM_BRANDS%");
        fsQuery.setResultClassList(List.of(String.class));
        SearchResult<String> searchResult = flexibleSearchService.search(fsQuery);
        return searchResult.getResult();
    }

    @Override
    public List<String> getAllProductsCategories(final CatalogVersionModel catalogVersion) {

        FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(CATEGORY_QUERY);
        fsQuery.addQueryParameter("catalogVersion", catalogVersion);
        fsQuery.addQueryParameter("sapCodeLike", "Z_PRODUCT_CATEGORIES%");

        SearchResult<CategoryModel> searchResult = flexibleSearchService.search(fsQuery);
        List<CategoryModel> categoryModelList = searchResult.getResult();

        return categoryModelList.stream()
                .map(CategoryModel::getName)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecommendationModel> findRecommendationsByPatient(B2BCustomerModel patient) {
        String query = "SELECT {r:pk} FROM {Recommendation AS r} WHERE {r.patient} = ?patient";
        FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(query);
        fsQuery.addQueryParameter("patient",patient);
        SearchResult<RecommendationModel> searchResult = flexibleSearchService.search(fsQuery);
        return searchResult.getResult();
    }

    @Override
    public List<RecommendationModel> findRecommendationsByPractitioner(B2BCustomerModel practitioner) {
        String query = "SELECT {p:pk} FROM {Recommendation AS p} WHERE {p.user} = ?practitioner";
        FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(query);
        fsQuery.addQueryParameter("practitioner",practitioner);
        SearchResult<RecommendationModel> searchResult = flexibleSearchService.search(fsQuery);
        return searchResult.getResult();
    }

    @Override
    public List<BrandCustomerClassRestrictionModel> getBrandCustomerClassRestrictions() {

        FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(BRAND_CUSTOMER_CLASS_RESTRICTION_QUERY);
        SearchResult<BrandCustomerClassRestrictionModel> searchResult = flexibleSearchService.search(fsQuery);
        return searchResult.getResult().isEmpty() ? Collections.emptyList() : searchResult.getResult();

    }

    @Override
    public List<CustomerClassProductRestrictionModel> getCustomerClassProductRestrictions() {

        FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(CUSTOMER_CLASS_PRODUCT_RESTRICTION_QUERY);
        SearchResult<CustomerClassProductRestrictionModel> searchResult = flexibleSearchService.search(fsQuery);
        return searchResult.getResult().isEmpty() ? Collections.emptyList() : searchResult.getResult();

    }

    @Override
    public List<CustomerProductRestrictionModel> getCustomerProductRestrictions() {

        FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(CUSTOMER_PRODUCT_RESTRICTION_QUERY);
        SearchResult<CustomerProductRestrictionModel> searchResult = flexibleSearchService.search(fsQuery);
        return searchResult.getResult().isEmpty() ? Collections.emptyList() : searchResult.getResult();

    }

    @Override
    public List<CustomerProductGroupRestrictionModel> getCustomerProductGroupRestrictions() {

        FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(CUSTOMER_PRODUCT_RESTRICTION_GROUP_QUERY);
        SearchResult<CustomerProductGroupRestrictionModel> searchResult = flexibleSearchService.search(fsQuery);
        return searchResult.getResult().isEmpty() ? Collections.emptyList() : searchResult.getResult();

    }

    @Override
    public List<RegionProductRestrictionModel> getRegionProductRestrictions() {

        FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(REGION_PRODUCT_RESTRICTION_QUERY);
        SearchResult<RegionProductRestrictionModel> searchResult = flexibleSearchService.search(fsQuery);
        return searchResult.getResult().isEmpty() ? Collections.emptyList() : searchResult.getResult();

    }
    
    @Override
	 public List<RecommendationModel> getRecommendationsForExternal(final B2BCustomerModel practitioner, final B2BCustomerModel patient, final String recommendationId) {
   	 
   	  String query = EXTERNAL_RECOMMENDATION_QUERY;
   	  final Map<String, Object> queryParameters = new HashMap<String, Object>();
   	  
   	  queryParameters.put("practitioner", practitioner);
        
        if (Objects.nonNull(patient)) {
      	  query = query.concat(EXTERNAL_RECOMMENDATION_QUERY_WITH_PATIENT);
      	  queryParameters.put("patient", patient);
        }
        
        if (StringUtils.isNotEmpty(recommendationId)) {
      	  query = query.concat(EXTERNAL_RECOMMENDATION_QUERY_ID_CONDITION);
      	  queryParameters.put("code", recommendationId);
      	  
        }
        
        FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(query);
        fsQuery.addQueryParameters(queryParameters);
        
        SearchResult<RecommendationModel> searchResult = flexibleSearchService.search(fsQuery);
        return searchResult.getResult();
    }
 

    @Override
    public List<B2BUnitModel> getShippingAddressB2BUnits(final B2BUnitModel b2BUnitModel) {

        String sapCustomerGroupCodes= configurationService.getConfiguration().getString(ADDRESS_GROUP_CODE);
        List<String> sapCustomerGroupList = new ArrayList<>(Arrays.asList(sapCustomerGroupCodes.split(",")));
        if(b2BUnitModel.getSapCustomerGroup() !=null){
            sapCustomerGroupList.add(b2BUnitModel.getSapCustomerGroup().getCode());
        }
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(GET_ALL_ADDRESS_FOR_CUSTOMER);
        searchQuery.addQueryParameter("customerId", WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(b2BUnitModel.getUid()).replaceFirst("^0+", ""));
        searchQuery.addQueryParameter("sapCustomerGroups", sapCustomerGroupList);
        searchQuery.addQueryParameter("customerUid","%1710_10_10");
        SearchResult<B2BUnitModel> searchResult = flexibleSearchService.search(searchQuery);
        return searchResult.getResult().isEmpty() ? Collections.emptyList() : searchResult.getResult();
    }

    @Override
    public List<B2BUnitModel> getPatients(final String customerId){
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(GET_ALL_PATIENTS_FOR_CUSTOMER);
        searchQuery.addQueryParameter("customerGroup", "02");
        searchQuery.addQueryParameter("salesPerson", customerId);
        SearchResult<B2BUnitModel> searchResult = flexibleSearchService.search(searchQuery);
        return searchResult.getResult().isEmpty() ? Collections.emptyList() : searchResult.getResult();
    }
}
