/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.dao;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;

import com.wholescripts.core.model.CustomerMaterialInformationModel;
import com.wholescripts.core.model.ProtocolModel;
import com.wholescripts.core.model.RecommendationModel;

/**
 *
 */
public interface WholescriptsUserDao {

    public B2BCustomerModel getUserByUid(final String user);

    <T extends B2BCustomerModel> SearchPageData<T> getAllPatientsForPractitioner(SearchPageData<CustomerData> searchPageData,
                                                                                 String patientDirectHubId, String referralCode, String discount, String searchText);

    public SearchResult<B2BCustomerModel> getUserByCustomerProfile(String customerProfile);

    List<AddressModel> getAllAddressForCustomers(B2BUnitModel customer, String addressSearch);

    List<RecommendationModel> getRecommendationForPatient(B2BCustomerModel practitioner);

    List<ProtocolModel> getProtocolsForPatient(B2BCustomerModel patient, B2BCustomerModel practitioner);

    List<B2BUnitModel> getAllB2bUnits();

    List<CustomerMaterialInformationModel> getDistinctAccountNumberFromCustomerMaterial();
    
    List<B2BCustomerModel> getAllPatientsForPractitioner(String salesPersonId);
    
    List<ProtocolModel> getSharedAllProtocals();

    public B2BCustomerModel getPatientModelByID(String patientID, String salesPersonID);
}
