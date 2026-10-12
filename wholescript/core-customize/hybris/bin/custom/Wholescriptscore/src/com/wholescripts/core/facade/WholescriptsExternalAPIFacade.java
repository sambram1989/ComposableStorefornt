package com.wholescripts.core.facade;

import com.wholescripts.data.*;

import de.hybris.platform.b2b.model.B2BUnitModel;

import java.util.List;

import com.wholescripts.dto.ExternalAPIEMRRecommendationResponseWsDTO;
import com.wholescripts.dto.ExternalAPIEMRRecommendationWsDTO;
import com.wholescripts.dto.WholescriptsExternalRestrictedProductsData;


/**
 * @author Sadiq
 */
public interface WholescriptsExternalAPIFacade {
    List<String> getAllProductsBrandNames();
    List<String> getAllProductsCategories();

    ExternalAPIEMRRecommendationResponseWsDTO createRecommenDation(ExternalAPIEMRRecommendationWsDTO inputWsDTO);
    List<WholescriptsExternalAPIProductData> getAllProductsData();

    List<WholescriptsExternalAPIProductData> getSelectedNonPLProductsData(final List<String> productSkus);

    ExternalOrderResponseData createExternalOrder(final ExternalOrderRequestData externalOrderRequest, final OrderCreationRequest orderCreationRequest) throws Exception;

    List<WholescriptsExternalAPIProductData> getPLProductsData(final String customerId);

    WholescriptsExternalRestrictedProductsData getRestrictedProductsData();
    
    ExternalOrderResponseData cancelExternalOrder(final ExternalOrderDetailRequestData request) throws Exception;
    ExternalOrderDetailsResponseWSDTO getExternalOrderDetail(final ExternalOrderDetailRequestData request) throws Exception;
    
    WsExternalRecommendationResponse getRecommendationsForExternal(final ExternalRecommendationRequestData request);

    WholescriptsExternalAPICustomerData getCustomerData(final String customerId, final String email);

    WholescriptsExternalAPIAddressListData getAddressData(final String customerId);

    WholescriptsExternalAPIPatientListData getPatientsData(final String customerId);

    List<WholescriptsExternalAPIProductData> getAllNonPLProducts();
    
    SapAddressResponseData createExternalAPIAddress(WholescriptsExternalAPIAddressData addressData, B2BUnitModel b2bUnitModel)
			throws Exception;

}
