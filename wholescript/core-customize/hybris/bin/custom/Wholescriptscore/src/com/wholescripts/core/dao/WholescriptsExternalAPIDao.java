package com.wholescripts.core.dao;

import com.wholescripts.core.model.*;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.user.AddressModel;

import java.util.List;

/**
 * @author Sadiq
 */
public interface WholescriptsExternalAPIDao {
    List<String> getAllProductsBrandNames();
    List<String> getAllProductsCategories(final CatalogVersionModel catalogVersion);
    List<RecommendationModel> findRecommendationsByPatient(B2BCustomerModel patient);
    List<RecommendationModel> findRecommendationsByPractitioner(B2BCustomerModel practitioner);

    List<BrandCustomerClassRestrictionModel> getBrandCustomerClassRestrictions();
    List<CustomerClassProductRestrictionModel> getCustomerClassProductRestrictions();
    List<CustomerProductRestrictionModel> getCustomerProductRestrictions();
    List<CustomerProductGroupRestrictionModel> getCustomerProductGroupRestrictions();
    List<RegionProductRestrictionModel> getRegionProductRestrictions();

    List<RecommendationModel> getRecommendationsForExternal(final B2BCustomerModel practitioner, final B2BCustomerModel patient,
			final String recommendationId);

    List<B2BUnitModel> getShippingAddressB2BUnits(final B2BUnitModel b2BUnitModel);
    List<B2BUnitModel> getPatients(final String customerId);
}
