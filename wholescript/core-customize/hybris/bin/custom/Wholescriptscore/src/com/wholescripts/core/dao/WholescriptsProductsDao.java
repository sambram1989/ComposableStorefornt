package com.wholescripts.core.dao;

import com.wholescripts.core.model.CustomerMaterialInformationModel;
import com.wholescripts.core.model.RegionProductAgeRestrictionModel;
import com.wholescripts.core.model.RegionProductSwapRestrictionModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Collection;
import java.util.List;

public interface WholescriptsProductsDao {

    List<ProductModel> getAllProductFamiliesData(String featureQualifier, String featureQualifierValue, String catalogVersionPk);

    ProductModel getPLProductBySKU(final String productCode, CatalogVersionModel catalogVersion);

    List<ProductModel> getNonPLProductsBySKUsList(final List<String> productSkusList, CatalogVersionModel catalogVersion);

    Collection<RegionProductSwapRestrictionModel> getProductSwapRestriction(final String productCode);

    List<RegionProductAgeRestrictionModel> getProductAgeRestriction(final String productCode);

    List<CustomerMaterialInformationModel> getCustomerMaterialInformationByCustomerId(final String customerId);

    List<ProductModel> getAllNonPLProducts(final CatalogVersionModel catalogVersion);

    List<ProductModel> getOrderedMedpaxProductsForUser(final String uId,final String productSearch);
    
    List<ProductModel> getProductsForShopify(final CatalogVersionModel catalogVersion);

    public List<ProductModel> getProductsByMasterVariant(String featureQualifier, String featureQualifierValue, String catalogVersionPk);

    CustomerMaterialInformationModel getCustomerMaterialInfo(String productCode,String account);

    List<ProductModel> getProductsByMedpaxCriteria(String sapMaterialGroups, String sapBasicMaterial, CatalogVersionModel catalogVersion);

    CustomerMaterialInformationModel getCustomerMaterialInfoByBlueLabel(String productCode, String account);
}
