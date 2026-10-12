/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service;

import com.wholescripts.data.CustomerActivityData;
import com.wholescripts.data.SapAddressResponseData;
import com.wholescripts.data.WholescriptsGetCCResponseData;
import com.wholescripts.dto.referralCodes.ReferralCodeWsDTO;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;

import com.wholescripts.core.model.ProtocolModel;
import com.wholescripts.core.model.W9FormModel;
/**
 *
 */
public interface WholescriptsUserService
{
	public B2BCustomerModel getUserByUid(String user);

	public SearchPageData<B2BCustomerModel> getAllPatientsForPractitioner(SearchPageData<CustomerData> searchPageData,
			String referralCode, String discount, String searchText);

	public String getAvalaraTaxLinkForCurrentUser();


	public W9FormModel saveW9FormData(W9FormModel w9FormModel);
	void updatePatientDirectnConsultFee(final boolean isPatientDirect,final boolean receiveConsultFee);
	/**
	 * Gets all the users by that customerProfile
	 * @param customerProfile
	 * @return
	 */
	public SearchResult<B2BCustomerModel> getUserByCustomerProfile(String customerProfile);

	public Boolean isWishlistAvailableForUser();

	public List<String> getFavouriteProductCodes();

	void saveAddressEntry(final B2BCustomerModel customerModel, final AddressModel addressModel, SapAddressResponseData responseData);

	List<AddressModel>  getAllAddressForCustomers(B2BUnitModel customer, String addressSearch);

	void saveAddressEntryForCart(final B2BCustomerModel customerModel, final AddressModel addressModel,String addressNumber);
	void verifyAgeAndUpdateInSAP(final String inquiryId) throws Exception;

	String getMaskedCardNumber(String cardNumber);

	WholescriptsGetCCResponseData getAddressByPayer(WholescriptsGetCCResponseData cCInfoFromSAP, B2BCustomerModel customer);

	List<CreditCardPaymentInfoModel> getCCPaymentInfos(B2BCustomerModel customer, List<CreditCardPaymentInfoModel> creditCards, CCPaymentInfoData cartPaymentInfo) throws Exception;

	String changePractitionerForPatient(B2BCustomerModel customer, ReferralCodeWsDTO referralCodeRequest) throws Exception;
	
	boolean saveCustomerActivity(final CustomerActivityData customerActivityData) throws Exception;
	
	List<ProductModel> getFavouriteProducts();
	
	List<CommentModel> saveCustomerNotes(final String customerNote, final UserModel user);
	
	List<B2BCustomerModel> getAllPatientsForPractitioner(final String salesPersonId);
	
	List<ProtocolModel> getSharedAllProtocals();
	public B2BCustomerModel getPatientModelByID(String patientID);
}
