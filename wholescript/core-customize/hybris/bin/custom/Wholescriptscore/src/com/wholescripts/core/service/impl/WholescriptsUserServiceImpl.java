/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.comments.model.DomainModel;
import de.hybris.platform.comments.services.CommentService;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.order.CartService;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.interceptor.impl.InterceptorExecutionPolicy;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.Config;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.wholescripts.core.dao.WholescriptsUserDao;
import com.wholescripts.core.enums.CustomerProfileType;
import com.wholescripts.core.enums.ProtocolSharedStatus;
import com.wholescripts.core.enums.RecommendationStatus;
import com.wholescripts.core.enums.SapCustomerGroupEnum;
import com.wholescripts.core.helper.WholescriptsHelper;
import com.wholescripts.core.model.ProtocolModel;
import com.wholescripts.core.model.RecommendationModel;
import com.wholescripts.core.model.W9FormModel;
import com.wholescripts.core.model.WholescriptsCustomerActivityModel;
import com.wholescripts.core.service.WholescriptsIntegrationsService;
import com.wholescripts.core.service.WholescriptsUserService;
import com.wholescripts.core.service.WholescriptsWishlist2Service;
import com.wholescripts.core.utils.WholescriptsCoreUtils;
import com.wholescripts.data.AgeValidationRequestData;
import com.wholescripts.data.CustomerActivityData;
import com.wholescripts.data.PersonaDataResponseData;
import com.wholescripts.data.PersonaIncludedResponseData;
import com.wholescripts.data.PersonaInquiriesResponseData;
import com.wholescripts.data.SapAddressResponseData;
import com.wholescripts.data.WholescriptsAccountChangeResponseData;
import com.wholescripts.data.WholescriptsGetCCCartItemListResponseData;
import com.wholescripts.data.WholescriptsGetCCCartItemResponseData;
import com.wholescripts.data.WholescriptsGetCCResponseData;
import com.wholescripts.dto.referralCodes.ReferralCodeWsDTO;



/**
 *
 */
public class WholescriptsUserServiceImpl implements WholescriptsUserService {
	private static final Logger LOG = LogManager.getLogger(WholescriptsUserServiceImpl.class);
	
	private static final String ADDRESS_SHIPPING_KEY = "|KNA1|WE";

	@Resource(name = "paginatedFlexibleSearchService")
	private PaginatedFlexibleSearchService paginatedFlexibleSearchService;

	@Resource(name = "wholescriptsUserDao")
	private WholescriptsUserDao wholescriptsUserDao;

	@Resource(name = "b2bCustomerService")
	private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;

	@Resource(name = "modelService")
	private ModelService modelService;
	@Resource(name = "businessProcessService")
	private BusinessProcessService businessProcessService;
	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;
	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;
	@Resource(name = "wholescriptsWishlist2Service")
	WholescriptsWishlist2Service wholescriptsWishlist2Service;
	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "searchRestrictionService")
	private SearchRestrictionService searchRestrictionService;

	@Resource(name = "cartService")
	private CartService cartService;
	@Resource(name = "wholescriptsIntegrationsService")
	private WholescriptsIntegrationsService wholescriptsIntegrationsService;

	@Resource(name = "addressConverter")
	private Converter<AddressModel, AddressData> addressConverter;


	@Resource(name = "addressReversePopulator")
	private Populator<AddressData, AddressModel> addressReversePopulator;

	@Resource(name = "defaultCustomerAccountService")
	private CustomerAccountService customerAccountService;

	@Resource(name = "b2bUnitService")
	private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;
	
	@Resource(name = "enumerationService")
   protected EnumerationService enumerationService;

	@Resource(name = "wholescriptsHelper")
	private WholescriptsHelper wholescriptsHelper;
	
	@Resource(name = "commentService")
	private CommentService commentService;
	
	@Resource(name = "wsCommentCodeGenerator")
	private KeyGenerator wsCommentCodeGenerator;

	@Resource(name = "sessionService")
	private SessionService sessionService;

	@Override
	public B2BCustomerModel getUserByUid(final String user) {
		return wholescriptsUserDao.getUserByUid(user);
	}

	@Override
	public SearchPageData<B2BCustomerModel> getAllPatientsForPractitioner(final SearchPageData<CustomerData> searchPageData,
																		  final String referralCode, final String discount, final String searchText) {
		final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
		final String currentB2BUnitId = customer.getDefaultB2BUnit().getUid();
		final String salesPersonId = WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(currentB2BUnitId); // Use your method here for fetching salesPerson id from B2BUnit of Practitioner.

		return wholescriptsUserDao.getAllPatientsForPractitioner(searchPageData, salesPersonId, referralCode, discount, searchText);
	}


	@Override
	public String getAvalaraTaxLinkForCurrentUser() {
		final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
		if (Objects.nonNull(customer)) {
			return customer.getAvalaraTaxLink();
		}
		return StringUtils.EMPTY;
	}


	public SearchResult<B2BCustomerModel> getUserByCustomerProfile(final String customerProfile) {
		return wholescriptsUserDao.getUserByCustomerProfile(customerProfile);
	}

	@Override
	public void updatePatientDirectnConsultFee(final boolean isPatientDirect, final boolean receiveConsultFee) {
		final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
		if (Objects.nonNull(customer) && Objects.nonNull(customer.getDefaultB2BUnit())) {
			final B2BUnitModel unit = customer.getDefaultB2BUnit();
			unit.setIsPatientDirect(isPatientDirect);
			unit.setReceiveConsultationFee(receiveConsultFee);
			modelService.save(unit);
		}
	}

	@Override
	public W9FormModel saveW9FormData(final W9FormModel w9FormModel) {
		try {
			modelService.save(w9FormModel);
			B2BCustomerModel customer =	b2bCustomerService.getCurrentB2BCustomer();
			if(Objects.nonNull(customer) && Objects.nonNull(customer.getDefaultB2BUnit())) {
				B2BUnitModel unit=  customer.getDefaultB2BUnit();
				unit.setReceiveConsultationFee(Boolean.TRUE);
				modelService.save(unit);
			}
			sendW9FormToXymonet(w9FormModel);
			return w9FormModel;
		} catch (final Exception e) {
			LOG.error(String.format("Error while saving w9 form data due to %s", e.getMessage()));
		}
		return w9FormModel;
	}

	private void sendW9FormToXymonet(final W9FormModel w9FormModel) {
		final StoreFrontCustomerProcessModel customerProcessModel = (StoreFrontCustomerProcessModel) businessProcessService.createProcess(
				"sendW9FormProcess-" + WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(w9FormModel.getB2bCustomer().getDefaultB2BUnit().getUid()) + "-" + System.currentTimeMillis(), "sendW9FormProcess");
		customerProcessModel.setSite(baseSiteService.getCurrentBaseSite());
		customerProcessModel.setCustomer(w9FormModel.getB2bCustomer());
		customerProcessModel.setW9Form(w9FormModel);
		final BaseStoreModel currentBaseStore = baseStoreService.getCurrentBaseStore();
		if (currentBaseStore != null) {
			customerProcessModel.setStore(currentBaseStore);
		}

		modelService.save(customerProcessModel);
		businessProcessService.startProcess(customerProcessModel);
	}

	@Override
	public List<String> getFavouriteProductCodes() {
		final UserModel currentUser = userService.getCurrentUser();
		final List<Wishlist2Model> wishLists = wholescriptsWishlist2Service.findWishlistsByUser(currentUser);
		// Stream through wishlists, entries, and collect product codes
		if (null != wishLists) {
			return wishLists.stream().flatMap(wishlist -> wishlist.getEntries().stream()) // Flatten entries from each wishlist
					.map(Wishlist2EntryModel::getProduct) // Map each entry to its ProductModel
					.filter(product -> product != null) // Filter out null products
					.map(ProductModel::getCode) // Map each product to its code
					.collect(Collectors.toList()); // Collect to a List
		}
		return Collections.emptyList();
	}

	@Override
	public Boolean isWishlistAvailableForUser() {
		final UserModel currentUser = userService.getCurrentUser();
		final List<Wishlist2Model> wishLists = wholescriptsWishlist2Service.findWishlistsByUser(currentUser);
		return (null != wishLists && CollectionUtils.isNotEmpty(wishLists)) ? Boolean.TRUE : Boolean.FALSE;
	}


	@Override
	public void saveAddressEntry(B2BCustomerModel customerModel, AddressModel addressModel, SapAddressResponseData responseData) {
		validateParameterNotNull(customerModel, "CUSTOMER_MODEL_NOT_BE_NULL");
		validateParameterNotNull(addressModel, "Address model cannot be null");

		if (Objects.nonNull(addressModel) && Objects.nonNull(responseData)) {

			final B2BUnitModel unit = customerModel.getDefaultB2BUnit();
			final String sapAddressNumber = responseData.getAddressNumber().replaceFirst("^0+", "");
			final String addressNumberWithSalesArea = WholescriptsCoreUtils.getAccountNumberWithSalesArea(sapAddressNumber);

			final B2BUnitModel shipToUnit = modelService.create(B2BUnitModel.class);
			if (Objects.nonNull(shipToUnit)) {
				shipToUnit.setUid(addressNumberWithSalesArea);
				shipToUnit.setId(WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnitWithoutFormat(unit.getUid()));
				shipToUnit.setSapCustomerGroup(SapCustomerGroupEnum.valueOf("11"));

				addressModel.setOwner(shipToUnit);
				final String publicKey = addressNumberWithSalesArea + "|" + sapAddressNumber + ADDRESS_SHIPPING_KEY;
				addressModel.setSapPublicKey(publicKey);
				addressModel.setPublicKey(publicKey);
				addressModel.setSapCustomerID(sapAddressNumber);

				final Map<String, Object> params = ImmutableMap.of(
						InterceptorExecutionPolicy.DISABLED_INTERCEPTOR_BEANS,
						ImmutableSet.of("defaultGigyaB2BUnitModelValidateInterceptor")
				);

				sessionService.executeInLocalViewWithParams(params, new SessionExecutionBody() {
					@Override
					public void executeWithoutResult() {
						modelService.saveAll(shipToUnit, addressModel);
						modelService.refresh(addressModel);
					}
				});
			}
		}
	}





	@Override
	public List<AddressModel> getAllAddressForCustomers( B2BUnitModel unit, String addressSearch) {
		List<AddressModel>  searchAddressPageDataList =  wholescriptsUserDao.getAllAddressForCustomers(unit, addressSearch);
		List<AddressModel> filteredAddresList = searchAddressPageDataList
				.stream()
				.map(wholescriptsHelper::returnValidAddressFromAddress)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
		return filteredAddresList;
	}

	@Override
	public void saveAddressEntryForCart(B2BCustomerModel customerModel, AddressModel addressModel, String addressNumber) {
		validateParameterNotNull(customerModel, "CUSTOMER_MODEL_NOT_BE_NULL");
		validateParameterNotNull(addressModel, "Address model cannot be null");


		final CartModel cartModel = cartService.getSessionCart();

		addressModel.setOwner(cartModel);
		addressNumber=WholescriptsCoreUtils.getShipToOrPayerForDefaultB2bUnit(addressNumber);
		addressModel.setSapPublicKey(customerModel.getDefaultB2BUnit().getUid() + "|" + addressNumber + "|KNA1|RE");
		modelService.save(addressModel);
		modelService.refresh(addressModel);
		cartModel.setDeliveryAddress(addressModel);
		modelService.save(cartModel);
		modelService.refresh(cartModel);
	}

	@Override
	public void verifyAgeAndUpdateInSAP(String inquiryId) throws Exception {
		PersonaInquiriesResponseData inquiriesData = wholescriptsIntegrationsService.verifyAgeAndUpdateInSAP(inquiryId);
		if (Objects.nonNull(inquiriesData) && Objects.nonNull(inquiriesData.getData()) && Objects.nonNull(inquiriesData.getIncluded())) {
			PersonaIncludedResponseData govtIDData = inquiriesData.getIncluded().stream().filter(p -> p.getType().equals("verification/government-id")).findFirst().orElse(null);
			PersonaIncludedResponseData databaseData = inquiriesData.getIncluded().stream().filter(p -> p.getType().equals("verification/database")).findFirst().orElse(null);
			if (Objects.nonNull(govtIDData) && Objects.nonNull(govtIDData.getAttributes())) {
				ageVerifyThroughGovtIdAndUpdateInSAP(govtIDData, inquiriesData.getData());
			} else if (Objects.nonNull(databaseData) && Objects.nonNull(databaseData.getAttributes())) {
				ageVerifyThroughDatabaseAndUpdateInSAP(databaseData, inquiriesData.getData());
			} else {
				throw new RequestParameterException("Not Found");
			}
		}
	}


	private void ageVerifyThroughGovtIdAndUpdateInSAP(PersonaIncludedResponseData personaData, PersonaDataResponseData data) {
		if (StringUtils.isNotEmpty(personaData.getAttributes().getExpirationDate()) && StringUtils.isNotEmpty(personaData.getAttributes().getBirthdate())) {
			LocalDate currentDate = LocalDate.now();
			String[] birthDate = personaData.getAttributes().getBirthdate().split("-");
			int birtYear = Integer.valueOf(birthDate[0]);
			int birthMonth = Integer.valueOf(birthDate[1]);
			int birthDay = Integer.valueOf(birthDate[2]);
			LocalDate userBirthDate = LocalDate.of(birtYear, birthMonth, birthDay);
			Period period = Period.between(userBirthDate, currentDate);
			int userAge = Math.abs(period.getYears());
			String[] expiredDate = personaData.getAttributes().getExpirationDate().split("-");
			int expiredYear = Integer.valueOf(expiredDate[0]);
			int expiredMonth = Integer.valueOf(expiredDate[1]);
			int expiredDay = Integer.valueOf(expiredDate[2]);
			LocalDate expirationDate = LocalDate.of(expiredYear, expiredMonth, expiredDay);
			B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();

			if (Objects.nonNull(customer) && Objects.nonNull(customer.getDefaultB2BUnit())) {
				AgeValidationRequestData valdationData = new AgeValidationRequestData();
				String accountNumber = WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(customer.getDefaultB2BUnit().getUid());
				StringBuilder expired = new StringBuilder();
				StringBuilder blockdate = new StringBuilder();
				;
				if (userAge > 21 && expirationDate.isAfter(currentDate)) {
					Period expiredPeriod = Period.between(expirationDate, currentDate);
					if (Math.abs(expiredPeriod.getYears()) > 3) {
						expired.append(String.valueOf(currentDate.getYear() + 3))
								.append("-")
								.append(String.valueOf(currentDate.getMonthValue()))
								.append("-")
								.append(String.valueOf(currentDate.getDayOfMonth()));
					} else {
						expired.append(personaData.getAttributes().getExpirationDate());
					}
				} else if (userAge < 21) {
					LocalDate dateWhen21 = userBirthDate.plusYears(21);
					blockdate.append(String.valueOf(dateWhen21.getYear()))
							.append("-")
							.append(String.valueOf(dateWhen21.getMonthValue()))
							.append("-")
							.append(String.valueOf(dateWhen21.getDayOfMonth()));
				} else if (userAge > 21 && !personaData.getAttributes().getStatus().equalsIgnoreCase("passed")) {
					blockdate.append(String.valueOf(currentDate.getYear() + 1))
							.append("-")
							.append(String.valueOf(currentDate.getMonthValue()))
							.append("-")
							.append(String.valueOf(currentDate.getDayOfMonth()));
				} else {
					throw new RequestParameterException("Age verification is failed for Account:-" + accountNumber);
				}
				sendAgeVerificationDataToSCPI(valdationData, accountNumber, expired, blockdate, customer, data.getId(),personaData.getAttributes().getBirthdate());
			}
		}
	}

	private void ageVerifyThroughDatabaseAndUpdateInSAP(PersonaIncludedResponseData personaData, PersonaDataResponseData data) {
		if (StringUtils.isNotEmpty(personaData.getAttributes().getBirthdate())) {
			LocalDate currentDate = LocalDate.now();
			String[] birthDate = personaData.getAttributes().getBirthdate().split("-");
			int birtYear = Integer.valueOf(birthDate[0]);
			int birthMonth = Integer.valueOf(birthDate[1]);
			int birthDay = Integer.valueOf(birthDate[2]);
			LocalDate userBirthDate = LocalDate.of(birtYear, birthMonth, birthDay);
			Period period = Period.between(userBirthDate, currentDate);
			int userAge = period.getYears();
			B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
			if (Objects.nonNull(customer) && Objects.nonNull(customer.getDefaultB2BUnit())) {
				AgeValidationRequestData valdationData = new AgeValidationRequestData();
				String accountNumber = WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(customer.getDefaultB2BUnit().getUid());
				StringBuilder expired = new StringBuilder();
				StringBuilder blockdate = new StringBuilder();
				;
				if (userAge > 21 && personaData.getAttributes().getStatus().equalsIgnoreCase("passed")) {
					expired.append(String.valueOf(currentDate.getYear() + 3))
							.append("-")
							.append(String.valueOf(currentDate.getMonthValue()))
							.append("-")
							.append(String.valueOf(currentDate.getDayOfMonth()));
				} else if (userAge < 21) {
					LocalDate dateWhen21 = userBirthDate.plusYears(21);
					blockdate.append(String.valueOf(dateWhen21.getYear()))
							.append("-")
							.append(String.valueOf(dateWhen21.getMonthValue()))
							.append("-")
							.append(String.valueOf(dateWhen21.getDayOfMonth()));
				} else if (userAge > 21 && !personaData.getAttributes().getStatus().equalsIgnoreCase("passed")) {
					blockdate.append(String.valueOf(currentDate.getYear() + 1))
							.append("-")
							.append(String.valueOf(currentDate.getMonthValue()))
							.append("-")
							.append(String.valueOf(currentDate.getDayOfMonth()));
				} else {
					throw new RequestParameterException("Age verification is failed for Account:-" + accountNumber);
				}
				//update age validation in sap
				sendAgeVerificationDataToSCPI(valdationData, accountNumber, expired, blockdate, customer, data.getId(),personaData.getAttributes().getBirthdate());
			}
		}
	}

	private void sendAgeVerificationDataToSCPI(AgeValidationRequestData valdationData, String accountNumber, StringBuilder expired,
											   StringBuilder blockdate, final B2BCustomerModel customer, final String inquiryId,final String bithdate) {
		createAgeValidationRequest(valdationData, accountNumber, expired, blockdate,bithdate);
		try {
			boolean isUpdatedInSap = wholescriptsIntegrationsService.UpdateAgeVerificationInSAP(valdationData);
			if (isUpdatedInSap) {
				B2BUnitModel unit = customer.getDefaultB2BUnit();
				unit.setInquiryId(inquiryId);
				unit.setAgeExpirationDate(expired.toString());
				unit.setAgeBlockedDate(blockdate.toString());
				modelService.save(unit);
			}
		} catch (Exception e) {
			throw new RequestParameterException(e.getMessage());
		}
	}

	private void createAgeValidationRequest(AgeValidationRequestData valdationData, String accountNumber, StringBuilder expired,
											StringBuilder blockdate,String birthdate) {
		valdationData.setBlockedDate(blockdate.toString());
		valdationData.setBpAccount(accountNumber);
		valdationData.setCleanAll("");
		valdationData.setExpirationDate(expired.toString());
		valdationData.setIdDate(birthdate);
	}

	@Override
	public String getMaskedCardNumber(String cardNumber) {
		String[] cCardNumber = cardNumber.split("-");

		return "************" + cCardNumber[2];
	}

	@Override
	public WholescriptsGetCCResponseData getAddressByPayer(WholescriptsGetCCResponseData cCInfoFromSAP, B2BCustomerModel customer) {
		try {
			if (Objects.nonNull(cCInfoFromSAP) && Objects.nonNull(cCInfoFromSAP.getCartData())) {

					WholescriptsGetCCCartItemListResponseData listResponseData = cCInfoFromSAP.getCartData();
					if (Objects.nonNull(listResponseData)) {
						for (WholescriptsGetCCCartItemResponseData itemData : listResponseData.getItem()) {
							try {
								String payerId=itemData.getPayer();
								if(StringUtils.isNotEmpty(payerId))
								{
									String salesAreaAccountNo = WholescriptsCoreUtils.getAccountNumberWithSalesArea(payerId.replaceFirst("^0+", ""));

									searchRestrictionService.disableSearchRestrictions();
									B2BUnitModel payerUnitModel = b2bUnitService.getUnitForUid(salesAreaAccountNo);
									if (payerUnitModel == null ) {
										salesAreaAccountNo = WholescriptsCoreUtils.getAccountNumberWithSalesArea(payerId);
										payerUnitModel = b2bUnitService.getUnitForUid(salesAreaAccountNo);
									}
									searchRestrictionService.enableSearchRestrictions();
									if (Objects.nonNull(payerUnitModel) && CollectionUtils.isNotEmpty(payerUnitModel.getAddresses())) {

										Collection<AddressModel> addressModels = payerUnitModel.getAddresses().stream().filter(bA -> Boolean.TRUE.equals(bA.getBillingAddress())).collect(Collectors.toList());
										Optional<AddressModel> billingAddress = addressModels.stream().findFirst();
										// Set billing address if found
										billingAddress.ifPresent(addressModel -> itemData.setBillingAddress(getAddressData(addressModel)));
									}

								}

							} catch (Exception e) {
								LOG.error("Error processing billing address for item: {}", itemData, e);
							}
						}
					}
				}

		} catch (Exception e) {
			LOG.error("Error in getAddressByPayer method", e);
		}
		return cCInfoFromSAP;
	}


	@Override
	public List<CreditCardPaymentInfoModel> getCCPaymentInfos(B2BCustomerModel customer, List<CreditCardPaymentInfoModel> creditCards, CCPaymentInfoData cartPaymentInfo) throws Exception {
		WholescriptsGetCCResponseData cCInfoFromSAP = wholescriptsIntegrationsService.getCreditCardInfoFromSap(customer);
		cCInfoFromSAP=getAddressByPayer(cCInfoFromSAP,customer);
		if (Objects.nonNull(cCInfoFromSAP))
		{
			 List<CreditCardPaymentInfoModel> removeCreditCards =new ArrayList<CreditCardPaymentInfoModel>();
			List<CreditCardPaymentInfoModel> existingCreditCards = customerAccountService.getCreditCardPaymentInfos(customer,
					true);
			if(CollectionUtils.isNotEmpty(existingCreditCards))
			{
				if(cartPaymentInfo!=null && cartPaymentInfo.getId()!=null)
				{
					removeCreditCards=existingCreditCards.stream().filter(p->!p.getPk().toString().equals(cartPaymentInfo.getId())).toList();

				}
				else {
					removeCreditCards=existingCreditCards.stream().toList();
				}
				if(CollectionUtils.isNotEmpty(removeCreditCards))
				{
					modelService.removeAll(removeCreditCards);
					modelService.refresh(customer);
				}

				}
			WholescriptsGetCCCartItemListResponseData listResponseData = cCInfoFromSAP.getCartData();
			if (Objects.nonNull(listResponseData)) {
				for (WholescriptsGetCCCartItemResponseData itemData : listResponseData.getItem()) {

							if(Objects.nonNull(cartPaymentInfo) && Objects.nonNull(cartPaymentInfo.getCardNumber())&& Objects.nonNull(cartPaymentInfo.getCardid()) && cartPaymentInfo.getCardNumber().equals(getMaskedCardNumber(itemData.getCardNumber()))&&cartPaymentInfo.getCardid().equals(itemData.getCardId().toString()))
								{
									//already available for the user in cart ,hence not creating the same payment info

								}
							else {

								final CreditCardPaymentInfoModel cardPaymentInfoModel = modelService.create(CreditCardPaymentInfoModel.class);
								cardPaymentInfoModel.setCode(customer.getUid() + "_" + UUID.randomUUID());
								cardPaymentInfoModel.setUser(customer);

								cardPaymentInfoModel.setNumber(itemData.getCardNumber());
								cardPaymentInfoModel.setCardId(itemData.getCardId());

								cardPaymentInfoModel.setType(WholescriptsCoreUtils.getCardTypeForSAP(itemData.getCardType()));

								cardPaymentInfoModel.setCcOwner(itemData.getCreditCardName());

								String validTo = itemData.getValidTo();
								String[] date = validTo.split("-");
								cardPaymentInfoModel.setValidToYear(String.valueOf(date[0]));
								cardPaymentInfoModel.setValidToMonth(String.valueOf(date[1]));
								cardPaymentInfoModel.setSaved(true);
								AddressData addressData = itemData.getBillingAddress();
								if (Objects.nonNull(addressData)) {
									final AddressModel billingAddress = modelService.create(AddressModel.class);
									addressData.setBillingAddress(true);
									addressReversePopulator.populate(addressData, billingAddress);
									final B2BUnitModel unit = customer.getDefaultB2BUnit();

									if (Objects.nonNull(unit)) {
										billingAddress.setSapPublicKey(unit.getUid() + "|" + addressData.getAddressNumber() + "|KNA1|RE");
									}
									billingAddress.setOwner(cardPaymentInfoModel);
									cardPaymentInfoModel.setBillingAddress(billingAddress);
									itemData.setBillingAddress(addressData);
									modelService.save(billingAddress);

								}
								modelService.save(cardPaymentInfoModel);
								creditCards.add(cardPaymentInfoModel);
							}
				}

			}
		}
		return creditCards;
	}

	@Override
	public String changePractitionerForPatient(B2BCustomerModel customer, ReferralCodeWsDTO referralCodeRequest) throws Exception {
		if (Objects.nonNull(customer) && Objects.nonNull(customer.getDefaultB2BUnit())) {
			String salesPersonId = WholescriptsCoreUtils.getDefaultB2bUnit(customer.getDefaultB2BUnit().getSalesPerson());
			if (StringUtils.isNotBlank(salesPersonId)) {
				B2BUnitModel unitModel = null;
					WholescriptsAccountChangeResponseData response = wholescriptsIntegrationsService.accountChangeForPatient(customer, referralCodeRequest);
					if (Objects.nonNull(response) && "0".equals(response.getReturnCode())) {
						customer.setReferralCode(referralCodeRequest.getReferralCode());
						customer.setReferralName(referralCodeRequest.getLastName());
						modelService.save(customer);
						modelService.refresh(customer);
						String b2bUnitId = WholescriptsCoreUtils.getAccountNumberWithSalesArea(salesPersonId.replaceFirst("^0+", ""));
						searchRestrictionService.disableSearchRestrictions();
						unitModel = b2bUnitService.getUnitForUid(b2bUnitId);
						B2BCustomerModel practitioner;
						if (Objects.nonNull(unitModel)) {
							final Set<B2BCustomerModel> customers = b2bUnitService.getB2BCustomers(unitModel);
							searchRestrictionService.enableSearchRestrictions();
							Optional<B2BCustomerModel> b2BCustomerModel = customers.stream().findFirst();
							if (b2BCustomerModel.isPresent()) {
								practitioner = b2BCustomerModel.get();
								List<RecommendationModel> recommendations = wholescriptsUserDao.getRecommendationForPatient(practitioner);
								if(CollectionUtils.isNotEmpty(recommendations))
								{
									recommendations.forEach(r -> r.setRecommendationStatus(RecommendationStatus.PATIENT_DELETED));
									modelService.saveAll(recommendations);
								}
								List<ProtocolModel> protocols=wholescriptsUserDao.getProtocolsForPatient(customer,practitioner);
								if(CollectionUtils.isNotEmpty(protocols))
								{
									for(ProtocolModel protocol: protocols)
									{
										final Set<B2BCustomerModel> existingPatientList = new HashSet<>(protocol.getPatients());
										if(existingPatientList.contains(customer))
										{
											existingPatientList.remove(customer);
											protocol.setPatients(existingPatientList.stream().toList());
											modelService.save(protocol);
										}

								}
								}

								return "Practitioner  Changed Successfully";
							}

						}
					}

			}
			throw new RequestParameterException("Change your Practitioner is failed with :-" + salesPersonId);

		}
		throw new RequestParameterException("Change your Practitioner is failed");
    }

	public AddressData getAddressData(AddressModel addressModel) {
		return  addressConverter.convert(addressModel);
	}

	@Override
	public boolean saveCustomerActivity(CustomerActivityData customerActivityData) throws Exception {

		final WholescriptsCustomerActivityModel activityModel = modelService.create(WholescriptsCustomerActivityModel.class);
		final CustomerModel customer = (CustomerModel) userService.getCurrentUser();
		activityModel.setCustomer(customer);
		activityModel.setIpAddress(customerActivityData.getIpAddress());
		activityModel.setMessage(customerActivityData.getMessage());
		activityModel.setCreatedOn(new Date());
		activityModel.setEventName(customerActivityData.getEvent());
		modelService.save(activityModel);
		return true;
	}
	
	@Override
	public List<ProductModel> getFavouriteProducts() {
		final UserModel currentUser = userService.getCurrentUser();
		final List<Wishlist2Model> wishLists = wholescriptsWishlist2Service.findWishlistsByUser(currentUser);
		if (null != wishLists) {
			return wishLists.stream().flatMap(wishlist -> wishlist.getEntries().stream()) // Flatten entries from each wishlist
					.map(Wishlist2EntryModel::getProduct) // Map each entry to its ProductModel
					.filter(product -> product != null) // Filter out null products
					.collect(Collectors.toList()); // Collect to a List
		}
		return Collections.emptyList();
	}
	
	@Override
	public List<CommentModel> saveCustomerNotes(final String customerNote, final UserModel user) {
		
		   final DomainModel domainModel = commentService.getDomainForCode(Config.getParameter("comment.domain.code"));
			final ComponentModel componentModel = commentService.getComponentForCode(domainModel,
					Config.getParameter("comment.component.code"));
			final CommentTypeModel commentTypeModel = commentService.getCommentTypeForCode(componentModel,
					Config.getParameter("comment.component.type.code"));
			final CommentModel commentModel = modelService.create(CommentModel.class);
			commentModel.setCode(wsCommentCodeGenerator.generate().toString());
			commentModel.setText(customerNote);
			commentModel.setAuthor(user);
			commentModel.setComponent(componentModel);
			commentModel.setCommentType(commentTypeModel);
			final List<CommentModel> commentList = new ArrayList<>();
			commentList.add(commentModel);
			return commentList;
	}
	
	@Override
	public List<B2BCustomerModel> getAllPatientsForPractitioner(final String salesPersonId) {
		return wholescriptsUserDao.getAllPatientsForPractitioner(salesPersonId);
	}
	
	@Override
   public List<ProtocolModel> getSharedAllProtocals() {
		return wholescriptsUserDao.getSharedAllProtocals();
   }

	@Override
	public B2BCustomerModel getPatientModelByID(String patientID)
	{
	  B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
		if(Objects.nonNull(customer.getDefaultB2BUnit())) {
			final String salesPersonId = WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(customer.getDefaultB2BUnit().getUid());
			B2BCustomerModel customerModel= wholescriptsUserDao.getPatientModelByID(patientID, salesPersonId);
			if(Objects.isNull(customerModel)) {
		   	throw new ModelNotFoundException("Customer not found.");
		   }
			return customerModel;
		}
		return null;
	}


}