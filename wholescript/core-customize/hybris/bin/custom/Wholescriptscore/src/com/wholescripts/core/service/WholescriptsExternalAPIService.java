package com.wholescripts.core.service;

import com.wholescripts.core.model.*;
import com.wholescripts.data.SapAddressResponseData;
import com.wholescripts.data.WholescriptsExternalAPIAddressData;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.user.AddressModel;

import java.util.List;

/**
 * @author Sadiq
 */
public interface WholescriptsExternalAPIService {
    List<String> getAllProductsBrandNames();
    List<String> getAllProductsCategories();
    AddressData createExternalAPIAddress(AddressData addressData, B2BCustomerModel b2BCustomerModel) throws Exception;
    List<RecommendationModel> getRecommendations(B2BCustomerModel b2BCustomerModel);

    List<BrandCustomerClassRestrictionModel> getBrandCustomerClassRestrictions();
    List<CustomerClassProductRestrictionModel> getCustomerClassProductRestrictions();
    List<CustomerProductRestrictionModel> getCustomerProductRestrictions();
    List<CustomerProductGroupRestrictionModel> getCustomerProductGroupRestrictions();
    List<RegionProductRestrictionModel> getRegionProductRestrictions();
    
    List<RecommendationModel> getRecommendationsForExternal(final B2BCustomerModel practitioner, final B2BCustomerModel patient,
			final String recommendationId);

    List<B2BUnitModel> getShippingAddressB2BUnits(final B2BUnitModel b2BUnitModel);
    List<B2BUnitModel> getPatients(final String customerId);
    SapAddressResponseData createExternalAPIAddress(WholescriptsExternalAPIAddressData wholescriptsExternalAPIAddressData) throws Exception;
}
