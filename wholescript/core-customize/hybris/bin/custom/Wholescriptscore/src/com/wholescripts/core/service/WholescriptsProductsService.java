package com.wholescripts.core.service;

import com.wholescripts.core.model.BrandCustomerClassRestrictionModel;
import com.wholescripts.core.model.CustomerClassProductRestrictionModel;
import com.wholescripts.core.model.CustomerMaterialInformationModel;
import com.wholescripts.core.model.CustomerProductGroupRestrictionModel;
import com.wholescripts.core.model.CustomerProductRestrictionModel;
import com.wholescripts.core.model.IngredientModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.List;

public interface WholescriptsProductsService {

    List<ProductModel> getAllProductFamiliesData(String productCode);

    ProductModel getProductDetailsByCode(String productId);

    List<IngredientModel> getIngredientsByProductCode(String productId);

    boolean isPrivateLabel(ProductModel product);

    List<ProductModel> getProductsForShopify(final String accountNumber);

    List<ProductModel> getProductsByMasterVariant(String featureQualifier, String featureQualifierValue, String catalogVersionPk);

    CustomerMaterialInformationModel getCustomerMaterialInfo(String productCode,String account);
    
    List<CustomerProductRestrictionModel> getCustomerProductRestrictions(final String productCode);
    
    List<CustomerProductGroupRestrictionModel> getCustomerProductGroupRestrictions(final String productCode);
    
    List<CustomerClassProductRestrictionModel> getCustomerClassProductRestrictions(final String productCode);
    
    List<BrandCustomerClassRestrictionModel> getBrandCustomerClassRestriction(final String brandCode);

    boolean isPrivateLabelMedpax(ProductModel product);

    CustomerMaterialInformationModel getCustomerMaterialInfoByBlueLabel(String productCode,String account);


    boolean isBlueLabel(ProductModel product);

    boolean isDefaultCarton(ProductModel productModel);

    boolean isCarton(ProductModel product);
    
    String getGenericPLNameFromCMI(String productCode);
}
