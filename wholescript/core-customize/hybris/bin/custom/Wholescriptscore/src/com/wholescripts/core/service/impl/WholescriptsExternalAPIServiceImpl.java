package com.wholescripts.core.service.impl;

import com.wholescripts.core.constants.GeneratedWholescriptscoreConstants.Enumerations.CustomerProfileType;
import com.wholescripts.core.dao.WholescriptsExternalAPIDao;
import com.wholescripts.core.model.*;
import com.wholescripts.core.service.WholescriptsExternalAPIService;
import com.wholescripts.core.service.WholescriptsIntegrationsService;
import com.wholescripts.data.SapAddressResponseData;
import com.wholescripts.data.WholescriptsExternalAPIAddressData;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author Sadiq
 */
public class WholescriptsExternalAPIServiceImpl implements WholescriptsExternalAPIService {

    @Resource(name = "wholescriptsExternalAPIDao")
    private WholescriptsExternalAPIDao wholescriptsExternalAPIDao;
    @Resource(name = "modelService")
    private ModelService modelService;
    @Resource(name = "addressReverseConverter")
    private Converter<AddressData, AddressModel> addressReverseConverter;
    @Resource(name = "wholescriptsIntegrationsService")
    private WholescriptsIntegrationsService wholescriptsIntegrationsService;
    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "addressConverter")
    private Converter<AddressModel, AddressData> addressConverter;
    
    @Resource(name = "catalogVersionService")
    private CatalogVersionService catalogVersionService;
    
    @Resource(name = "configurationService")
    private ConfigurationService configurationService;

    @Override
    public List<String> getAllProductsBrandNames() {
        return wholescriptsExternalAPIDao.getAllProductsBrandNames();
    }

    @Override
    public List<String> getAllProductsCategories() {
   	 
   	 final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(
 				configurationService.getConfiguration().getString("wholescripts.product.catalog"),
 				configurationService.getConfiguration().getString("wholescripts.product.catalog.version"));

        return wholescriptsExternalAPIDao.getAllProductsCategories(catalogVersion);
    }

    @Override
    public AddressData createExternalAPIAddress(AddressData addressData, B2BCustomerModel customer) throws Exception {
        B2BUnitModel b2BUnit = customer.getDefaultB2BUnit();
        if (Objects.nonNull(b2BUnit)) {
            SapAddressResponseData responseData = wholescriptsIntegrationsService.createAddressinSAP(addressData, customer);
            if (responseData != null) {
                if (responseData.getRETURN().equals("0")) {
                    AddressModel addressModel = addressReverseConverter.convert(addressData);
                    if (Objects.nonNull(addressModel)) {
                        addressModel.setOwner(b2BUnit);
                        addressModel.setSapPublicKey(b2BUnit.getUid() + "|" + responseData.getAddressNumber() + "|KNA1|RE");
                        modelService.save(addressModel);
                        modelService.refresh(addressModel);
                        return addressConverter.convert(addressModel);
                    }
                } else {
                    throw new IllegalStateException("SAP returned an error: " + responseData.getRETURN());
                }
            } else {
                throw new IllegalStateException("Response from SAP is null");
            }
        }
        throw new IllegalStateException("Default B2BUnit for the customer not found");
    }

    @Override
    public List<RecommendationModel> getRecommendations(B2BCustomerModel user) {

        if (user.getCustomerProfile().getCode().equals(CustomerProfileType.PATIENT)) {
            return wholescriptsExternalAPIDao.findRecommendationsByPatient(user);
        } else if (user.getCustomerProfile().getCode().equals(CustomerProfileType.PRACTITIONER)) {
            return wholescriptsExternalAPIDao.findRecommendationsByPractitioner(user);
        } else {
            throw new IllegalArgumentException("Invalid user profile type for email: " + user);
        }
    }

    @Override
    public List<BrandCustomerClassRestrictionModel> getBrandCustomerClassRestrictions() {
        return wholescriptsExternalAPIDao.getBrandCustomerClassRestrictions();
    }

    @Override
    public List<CustomerClassProductRestrictionModel> getCustomerClassProductRestrictions() {
        return wholescriptsExternalAPIDao.getCustomerClassProductRestrictions();
    }

    @Override
    public List<CustomerProductRestrictionModel> getCustomerProductRestrictions() {
        return wholescriptsExternalAPIDao.getCustomerProductRestrictions();
    }

    @Override
    public List<CustomerProductGroupRestrictionModel> getCustomerProductGroupRestrictions() {
        return wholescriptsExternalAPIDao.getCustomerProductGroupRestrictions();
    }

    @Override
    public List<RegionProductRestrictionModel> getRegionProductRestrictions() {
        return wholescriptsExternalAPIDao.getRegionProductRestrictions();
    }

    @Override
    public List<RecommendationModel> getRecommendationsForExternal(final B2BCustomerModel practitioner, final B2BCustomerModel patient, 
   		 final String recommendationId) {
   	 return wholescriptsExternalAPIDao.getRecommendationsForExternal(practitioner, patient, recommendationId);
    }


    @Override
    public List<B2BUnitModel> getShippingAddressB2BUnits(final B2BUnitModel b2BUnitModel){
        return wholescriptsExternalAPIDao.getShippingAddressB2BUnits(b2BUnitModel);
    }

    @Override
    public List<B2BUnitModel> getPatients(final String customerId){
        return wholescriptsExternalAPIDao.getPatients(customerId);
    }

	 @Override
	 public SapAddressResponseData createExternalAPIAddress(WholescriptsExternalAPIAddressData wholescriptsExternalAPIAddressData)
			 throws Exception {
			 return wholescriptsIntegrationsService.createAddressinSAP(wholescriptsExternalAPIAddressData);
	 }
}
