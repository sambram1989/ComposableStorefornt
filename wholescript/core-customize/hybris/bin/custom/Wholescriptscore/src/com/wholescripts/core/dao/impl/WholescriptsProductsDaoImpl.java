package com.wholescripts.core.dao.impl;


import com.wholescripts.core.dao.WholescriptsProductsDao;
import com.wholescripts.core.model.CustomerMaterialInformationModel;
import com.wholescripts.core.model.RegionProductAgeRestrictionModel;
import com.wholescripts.core.model.RegionProductSwapRestrictionModel;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import javax.annotation.Resource;
import java.util.*;

public class WholescriptsProductsDaoImpl implements WholescriptsProductsDao {

    private static final Logger LOG = Logger.getLogger(WholescriptsProductsDaoImpl.class.getName());

    private static final String PRODUCT_FAMILY_PRODUCTS = "SELECT {p.pk} FROM {Product AS p JOIN ProductFeature AS pf ON {p.pk} = {pf.product} JOIN CatalogVersion AS cv ON {p.catalogVersion} = {cv.pk}} WHERE {pf.qualifier} = ?featureQualifier   AND {pf.stringValue} LIKE CONCAT('%',?featureQualifierValue,'%')   AND {cv.pk} = ?catalogVersion   AND {p.sapMaterialGroup} NOT IN ({{SELECT {pk} FROM {sapmaterialgroupenum} WHERE {code} IN ('1000014','1000018','1001024','1000025')}})";

    private final static String FIND_NON_PL_PRODUCTS_BY_SKUS = "SELECT {p.pk} FROM {Product AS p JOIN CatalogVersion AS cv ON {p.catalogVersion} = {cv.pk}} WHERE {cv.pk} = ?catalogVersion AND {p.code} in (?skus)   AND {p.sapMaterialGroup} NOT IN ({{SELECT {pk} FROM {sapmaterialgroupenum} WHERE {code} IN (?nonPLMaterialCodes)}})";

    private final static String FIND_PL_PRODUCT_BY_SKU = "SELECT {p.pk} FROM {Product AS p JOIN CatalogVersion AS cv ON {p.catalogVersion} = {cv.pk}} WHERE {cv.pk} = ?catalogVersion AND {p.code}=?productCode";

    private final static String FIND_ALL_NON_PL_PRODUCTS = "SELECT {p.pk} FROM {Product AS p JOIN CatalogVersion AS cv ON {p.catalogVersion} = {cv.pk}} WHERE {cv.pk} = ?catalogVersion  AND {p.sapMaterialGroup} NOT IN ({{SELECT {pk} FROM {sapmaterialgroupenum} WHERE {code} IN (?nonPLMaterialCodes)}})";

    private final static String FIND_ORDERED_MED_PAX_PRODUCTS = "SELECT distinct {p.pk} from {Product as p join OrderEntry as oe on {oe.product}={p.pk} join Order as o on {oe.order}={o.pk} } where {o.user}=?user and {p.isMedpax}=1 ";

    private static final String SEARCH_QUERY_PRODUCTNAME = "   AND ( LOWER({p.name}) LIKE LOWER(CONCAT('%',?name,'%'))";

    private static final String SEARCH_QUERY_PRODUCT_GENERIC_PL_NAME = "  OR LOWER({p.genericPLName}) LIKE LOWER(CONCAT('%',?genericPLName,'%')))";

    private final static String SWAP_RESTRICTIONS_BY_PRODUCT_CODE = "select {pk} from {RegionProductSwapRestriction} where {productcode}=?productCode";

    private final static String AGE_RESTRICTIONS_BY_PRODUCT_CODE = "select {pk} from {RegionProductAgeRestriction} where {productcode}=?productCode";

    private final static String FIND_CUSTOMER_MATERIAL_INFORMATION = "select {pk} from {CustomerMaterialInformation} where {account}=?account";

    private final static String FIND_SHOPIFY_PRODUCTS = "SELECT {p.pk} FROM {Product AS p JOIN CatalogVersion AS cv ON {p.catalogVersion} = {cv.pk}} WHERE {cv.pk} = ?catalogVersion  AND {p.sapMaterialGroup} IN ({{SELECT {pk} FROM {sapmaterialgroupenum} WHERE {code} IN (?materialCodes)}})";

    private final static String NON_PL_PRODUCT_MATERIAL_CODES = "non.pl.material.group.codes";

    private final static String SHOPIFY_PRODUCT_MATERIAL_CODES = "shopify.product.material.groups";

    private final static String COMMA = ",";

    private static final String PRODUCT_BY_MASTER_VARIANT_QUERY = "SELECT {p.pk} FROM {Product AS p JOIN ProductFeature AS pf ON {p.pk} = {pf.product} JOIN CatalogVersion AS cv ON {p.catalogVersion} = {cv.pk}} WHERE {pf.qualifier} = ?featureQualifier AND {pf.stringValue} = ?featureQualifierValue AND {cv.pk} = ?catalogVersion AND {p.sapMaterialGroup} NOT IN ({{SELECT {pk} FROM {sapmaterialgroupenum} WHERE {code} IN ('1000014','1000018','1001024','1000025')}}) ";

    private static final String CUSTOMER_MATERIAL_BY_PRODUCT_ODE = "select {cm.pk} from {CustomerMaterialInformation as cm } where {cm.productCode}=?productCode and {cm.account}=?account";

    private static final String GET_BLUE_LABEL_PRODUCTS_FOR_MEDPAX = "SELECT {p.pk} FROM {Product AS p JOIN SapMaterialGroupEnum AS g ON {p:sapMaterialGroup} = {g:pk} JOIN CatalogVersion AS cv ON {p.catalogVersion} = {cv.pk}} WHERE {g.code} IN (?sapMaterialGroup) AND {p.sapBasicMaterial} = ?sapBasicMaterial AND {cv.pk} = ?catalogVersion";

    private static final String CUSTOMER_MATERIAL_BY_BLUELABEL_ODE = "select {cm.pk} from {CustomerMaterialInformation as cm } where {cm.blueLabel}=?blueLabel and {cm.account}=?account";


    @Resource(name = "configurationService")
    private ConfigurationService configurationService;

    @Resource(name = "flexibleSearchService")
    private FlexibleSearchService flexibleSearchService;

    @Resource(name = "b2bCustomerService")
    private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;


    @Override
    public List<ProductModel> getAllProductFamiliesData(String featureQualifier, String featureQualifierValue, String catalogVersionPk) {
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(PRODUCT_FAMILY_PRODUCTS);
        searchQuery.addQueryParameter("featureQualifier", featureQualifier);
        searchQuery.addQueryParameter("featureQualifierValue", featureQualifierValue);
        searchQuery.addQueryParameter("catalogVersion", catalogVersionPk);

        SearchResult<ProductModel> searchResult = flexibleSearchService.search(searchQuery);
        return searchResult.getResult().isEmpty() ? Collections.emptyList() : searchResult.getResult();
    }

    public ProductModel getPLProductBySKU(final String productCode, CatalogVersionModel catalogVersion) {
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(FIND_PL_PRODUCT_BY_SKU);
        searchQuery.addQueryParameter("catalogVersion", catalogVersion.getPk());
        searchQuery.addQueryParameter("productCode", productCode);

        SearchResult<ProductModel> result = flexibleSearchService.search(searchQuery);
        return result.getResult().isEmpty() ? null : result.getResult().get(0);
    }

    public List<ProductModel> getNonPLProductsBySKUsList(final List<String> productSkusList, CatalogVersionModel catalogVersion) {
        String nonPLMaterialCodes = configurationService.getConfiguration().getString(NON_PL_PRODUCT_MATERIAL_CODES);
        String[] nonPLMaterialCodesArray = StringUtils.split(nonPLMaterialCodes, COMMA);
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(FIND_NON_PL_PRODUCTS_BY_SKUS);
        searchQuery.addQueryParameter("catalogVersion", catalogVersion.getPk());
        searchQuery.addQueryParameter("skus", productSkusList);
        searchQuery.addQueryParameter("nonPLMaterialCodes", Arrays.stream(nonPLMaterialCodesArray).toList());

        SearchResult<ProductModel> searchResult = flexibleSearchService.search(searchQuery);
        return searchResult.getResult().isEmpty() ? Collections.emptyList() : searchResult.getResult();
    }

    @Override
    public List<ProductModel> getAllNonPLProducts(final CatalogVersionModel catalogVersion) {
        String nonPLMaterialCodes = configurationService.getConfiguration().getString(NON_PL_PRODUCT_MATERIAL_CODES);
        String[] nonPLMaterialCodesArray = StringUtils.split(nonPLMaterialCodes, COMMA);
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(FIND_ALL_NON_PL_PRODUCTS);
        searchQuery.addQueryParameter("catalogVersion", catalogVersion.getPk());
        searchQuery.addQueryParameter("nonPLMaterialCodes", Arrays.stream(nonPLMaterialCodesArray).toList());

        SearchResult<ProductModel> searchResult = flexibleSearchService.search(searchQuery);
        return searchResult.getResult().isEmpty() ? Collections.emptyList() : searchResult.getResult();
    }

    @Override
    public Collection<RegionProductSwapRestrictionModel> getProductSwapRestriction(String productCode) {
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(SWAP_RESTRICTIONS_BY_PRODUCT_CODE);
        searchQuery.addQueryParameter("productCode", productCode);

        SearchResult<RegionProductSwapRestrictionModel> searchResult = flexibleSearchService.search(searchQuery);
        return searchResult.getResult().isEmpty() ? Collections.emptyList() : searchResult.getResult();
    }

    @Override
    public List<RegionProductAgeRestrictionModel> getProductAgeRestriction(String productCode) {
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(AGE_RESTRICTIONS_BY_PRODUCT_CODE);
        searchQuery.addQueryParameter("productCode", productCode);

        SearchResult<RegionProductAgeRestrictionModel> searchResult = flexibleSearchService.search(searchQuery);
        return searchResult.getResult().isEmpty() ? Collections.emptyList() : searchResult.getResult();
    }

    @Override
    public List<CustomerMaterialInformationModel> getCustomerMaterialInformationByCustomerId(String customerId) {

        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(FIND_CUSTOMER_MATERIAL_INFORMATION);
        searchQuery.addQueryParameter("account", customerId);

        SearchResult<CustomerMaterialInformationModel> searchResult = flexibleSearchService.search(searchQuery);
        return searchResult.getResult().isEmpty() ? Collections.emptyList() : searchResult.getResult();

    }

    @Override
    public List<ProductModel> getOrderedMedpaxProductsForUser(final String uId, final String productSearch) {
        try {
            final StringBuilder queryBuilder = new StringBuilder();
            final Map<String, Object> queryParameters = new HashMap();
            queryBuilder.append(FIND_ORDERED_MED_PAX_PRODUCTS);
            B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
            if (null != customer) {
                queryParameters.put("user", customer);
            } else {
                throw new InputMismatchException(uId + ": UID is not found in the system");
            }
            if (StringUtils.isNotEmpty(productSearch)) {
                queryBuilder.append(SEARCH_QUERY_PRODUCTNAME);
                queryParameters.put("name", productSearch);
                queryBuilder.append(SEARCH_QUERY_PRODUCT_GENERIC_PL_NAME);
                queryParameters.put("genericPLName", productSearch);
            }
            FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(queryBuilder.toString(), queryParameters);
            SearchResult<ProductModel> searchResult = flexibleSearchService.search(searchQuery);
            return searchResult.getResult().isEmpty() ? Collections.emptyList() : searchResult.getResult();
        } catch (InputMismatchException e) {
            LOG.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public List<ProductModel> getProductsForShopify(final CatalogVersionModel catalogVersion) {
        final String materialCodes = configurationService.getConfiguration().getString(SHOPIFY_PRODUCT_MATERIAL_CODES);
        final String[] materialCodesArray = StringUtils.split(materialCodes, COMMA);
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(FIND_SHOPIFY_PRODUCTS);
        searchQuery.addQueryParameter("catalogVersion", catalogVersion.getPk());
        searchQuery.addQueryParameter("materialCodes", Arrays.stream(materialCodesArray).toList());

        SearchResult<ProductModel> searchResult = flexibleSearchService.search(searchQuery);
        if(!searchResult.getResult().isEmpty()) {
      	  LOG.debug("WholescriptsProductsDaoImpl getProductsForShopify, Size of the result is " + searchResult.getResult().size()
      			  + " and query is " + searchQuery.toString());
        }
        return searchResult.getResult().isEmpty() ? Collections.emptyList() : searchResult.getResult();
    }

    @Override
    public List<ProductModel> getProductsByMasterVariant(String featureQualifier, String featureQualifierValue, String catalogVersionPk) {
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(PRODUCT_BY_MASTER_VARIANT_QUERY);
        searchQuery.addQueryParameter("featureQualifier", featureQualifier);
        searchQuery.addQueryParameter("featureQualifierValue", featureQualifierValue);
        searchQuery.addQueryParameter("catalogVersion", catalogVersionPk);

        SearchResult<ProductModel> searchResult = flexibleSearchService.search(searchQuery);
        return searchResult.getResult().isEmpty() ? Collections.emptyList() : searchResult.getResult();
    }

    @Override
    public CustomerMaterialInformationModel getCustomerMaterialInfo(String productCode, String account) {
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(CUSTOMER_MATERIAL_BY_PRODUCT_ODE);
        searchQuery.addQueryParameter("productCode", productCode);
        searchQuery.addQueryParameter("account", account);
        SearchResult<CustomerMaterialInformationModel> searchResult = flexibleSearchService.search(searchQuery);
        return searchResult.getResult().isEmpty() ? null : searchResult.getResult().get(0);
    }

    @Override
    public List<ProductModel> getProductsByMedpaxCriteria(String sapMaterialGroups, String sapBasicMaterial, CatalogVersionModel catalogVersion) {
        try {
      	  	final List<String> blueLabelMaterialGroups =  Arrays.asList( sapMaterialGroups.split(","));
            FlexibleSearchQuery fQuery = new FlexibleSearchQuery(GET_BLUE_LABEL_PRODUCTS_FOR_MEDPAX);
            fQuery.addQueryParameter("sapMaterialGroup", blueLabelMaterialGroups);
            fQuery.addQueryParameter("sapBasicMaterial", sapBasicMaterial);
            fQuery.addQueryParameter("catalogVersion", catalogVersion.getPk());

            SearchResult<ProductModel> result = flexibleSearchService.search(fQuery);

            return (result != null && result.getCount() > 0) ? result.getResult() : Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public CustomerMaterialInformationModel getCustomerMaterialInfoByBlueLabel(String blueLabel, String account) {
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(CUSTOMER_MATERIAL_BY_BLUELABEL_ODE);
        searchQuery.addQueryParameter("blueLabel", blueLabel);
        searchQuery.addQueryParameter("account", account);
        SearchResult<CustomerMaterialInformationModel> searchResult = flexibleSearchService.search(searchQuery);
        return searchResult.getResult().isEmpty() ? null : searchResult.getResult().get(0);
    }

}