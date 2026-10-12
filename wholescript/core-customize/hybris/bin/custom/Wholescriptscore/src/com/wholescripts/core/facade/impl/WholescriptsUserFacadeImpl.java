/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.facade.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;
import static de.hybris.platform.webservicescommons.util.YSanitizer.sanitize;
import static java.util.stream.Collectors.toList;

import com.wholescripts.data.*;
import de.hybris.platform.acceleratorocc.dto.payment.SopPaymentDetailsWsDTO;
import de.hybris.platform.acceleratorservices.payment.cybersource.enums.CardTypeEnum;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.enums.CreditCardType;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.order.CartService;
import de.hybris.platform.product.PriceService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import de.hybris.platform.wishlist2.Wishlist2Service;
import de.hybris.platform.wishlist2.enums.Wishlist2EntryPriority;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

import com.wholescripts.core.constants.WholescriptscoreConstants;
import com.wholescripts.core.dao.WholescriptsWishListDao;
import com.wholescripts.core.exception.AccertifyValidationException;
import com.wholescripts.core.exception.AgeRestrictionException;
import com.wholescripts.core.exception.POBoxAgeRestrictionException;
import com.wholescripts.core.facade.WholescriptsAccertifyFacade;
import com.wholescripts.core.facade.WholescriptsProductsFacade;
import com.wholescripts.core.facade.WholescriptsUserFacade;
import com.wholescripts.core.model.RecommendationModel;
import com.wholescripts.core.model.ReferralCodesModel;
import com.wholescripts.core.model.W9FormModel;
import com.wholescripts.core.populators.W9FormDataReversePopulator;
import com.wholescripts.core.service.WholescriptsIntegrationsService;
import com.wholescripts.core.service.WholescriptsRecommendService;
import com.wholescripts.core.service.WholescriptsUserService;
import com.wholescripts.core.service.WholescriptsWishlist2Service;
import com.wholescripts.core.utils.WholescriptsCoreUtils;
import com.wholescripts.dto.PractitionerAmendmentRequestWsDTO;
import com.wholescripts.dto.W9FormData;
import com.wholescripts.dto.referralCodes.ReferralCodeWsDTO;

public class WholescriptsUserFacadeImpl implements WholescriptsUserFacade {

    private static final Logger LOG = LoggerFactory.getLogger(WholescriptsUserFacadeImpl.class);
    private static final String DEFAULT_AVALARA_LINK = "avalara.stage.link.default.url";
    private static final String BLUE_LABEL_PLPWISHLIST_PREFIX = "PLP_WISHLIST_";
    private static final String MEDPAX_PLPWISHLIST_PREFIX = "MEDPAX_";
    private static final String PRIVATE_PLPWISHLIST_PREFIX = "PRIVATE_LABEL_";
    private static final String WHOLESCRIPTS_PRIVATE_LABEL_CODE = "wholescripts.private.label.code";
    private static final String B2BUNITSHIPPING = "RE";
    private static final String CONCAT_ADDRESS = ", ";
    private static final String ADDRESS_SHIPPING_KEY = "|KNA1|WE";

    @Resource(name = "wholescriptsUserService")
    WholescriptsUserService wholescriptsUserService;
    @Resource(name = "wholescriptsUserConverter")
    Converter wholescriptsUserConverter;
    @Resource(name = "wishlistService")
    Wishlist2Service wishlistService;
    @Resource(name = "wholescriptsWishlist2Service")
    WholescriptsWishlist2Service wholescriptsWishlist2Service;
    @Resource(name = "modelService")
    ModelService modelService;
    @Resource
    W9FormDataReversePopulator w9FormDataReversePopulator;
    @Resource(name = "productService")
    private ProductService productService;
    @Resource(name = "userService")
    private UserService userService;
    @Resource(name = "wholescriptsW9FormConverter")
    private Converter<W9FormModel, W9FormData> wholescriptsW9FormConverter;

    @Resource(name = "b2bCustomerService")
    private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;

    @Resource(name = "addressReversePopulator")
    private Populator<AddressData, AddressModel> addressReversePopulator;

    @Resource(name = "wholescriptsIntegrationsService")
    private WholescriptsIntegrationsService wholescriptsIntegrationsService;

    @Resource(name = "addressConverter")
    private Converter<AddressModel, AddressData> addressConverter;

    @Resource(name = "customerConverter")
    private Converter<UserModel, CustomerData> customerConverter;

    @Resource(name = "cartService")
    private CartService cartService;

    @Resource(name = "defaultCustomerAccountService")
    private CustomerAccountService customerAccountService;

    @Resource(name = "defaultCreditCardPaymentInfoConverter")
    private Converter<CreditCardPaymentInfoModel, CCPaymentInfoData> creditCardPaymentInfoConverter;

    @Resource(name = "commonI18NService")
    private CommonI18NService commonI18NService;

    @Resource(name = "searchRestrictionService")
    private SearchRestrictionService searchRestrictionService;

    @Resource(name = "b2bUnitService")
    private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;

    @Resource(name = "configurationService")
    private ConfigurationService configurationService;

    @Resource(name = "wholescriptsWishListDao")
    WholescriptsWishListDao wholescriptsWishListDao;

    @Resource(name = "wholescriptsRecommendService")
    WholescriptsRecommendService wholescriptsRecommendService;

    @Resource(name = "catalogVersionService")
    protected CatalogVersionService catalogVersionService;

    @Resource(name = "priceService")
    PriceService priceService;

    private UrlResolver<ProductModel> productModelUrlResolver;


    @Resource(name = "wholescriptsProductsFacade")
    protected WholescriptsProductsFacade wholescriptsProductsFacade;

    @Resource(name = "enumerationService")
    protected EnumerationService enumerationService;

    @Resource(name = "wsAccertifyCreateAddressPopulator")
    private Populator<AddressData, AccertifyVerificationCreateAccountRequestData> wsAccertifyCreateAddressPopulator;

    @Resource(name = "wholescriptsAccertifyFacade")
    private WholescriptsAccertifyFacade wholescriptsAccertifyFacade;
    
    @Resource(name = "wsAccertifyOldAddressPopulator")
    private Populator<AddressModel, AccertifyVerificationUpdateAccountRequestData> wsAccertifyOldAddressPopulator;
    
    @Resource(name = "wsAccertifyUpdateAddressPopulator")
    private Populator<AddressData, AccertifyVerificationUpdateAccountRequestData> wsAccertifyUpdateAddressPopulator;


    @Override
    public SearchPageData<CustomerData> getAllPatientsForPractitioner(final SearchPageData<CustomerData> searchPageData, final String referralCode, final String discount, final String searchText) {
        final SearchPageData<B2BCustomerModel> searchData = wholescriptsUserService.getAllPatientsForPractitioner(searchPageData, referralCode, discount, searchText);

        final SearchPageData<CustomerData> result = new SearchPageData();
        final List<CustomerData> patientList = searchData.getResults().stream().map(patientModel -> getPatientData(patientModel)).filter(Optional::isPresent).map(Optional::get).collect(toList());
        result.setResults(patientList);
        result.setPagination(searchData.getPagination());
        result.setSorts(searchData.getSorts());
        return result;
    }

    private Optional<CustomerData> getPatientData(final B2BCustomerModel patientModel) {
        return Optional.of((CustomerData) wholescriptsUserConverter.convert(patientModel));
    }

    @Override
    public AvalaraTaxLinkData getAvalaraTaxLinkForCurrentUser() {
        String avalaraLink = wholescriptsUserService.getAvalaraTaxLinkForCurrentUser();
        if (StringUtils.isEmpty(avalaraLink)) {
            avalaraLink = Config.getParameter(DEFAULT_AVALARA_LINK);
        }
        final AvalaraTaxLinkData avalaraTaxLinkData = new AvalaraTaxLinkData();
        avalaraTaxLinkData.setAvalaraTaxLink(avalaraLink);
        return avalaraTaxLinkData;
    }

    @Override
    public void updatePatientDirectnConsultFee(final boolean isPatientDirect, final boolean receiveConsultFee) {
        wholescriptsUserService.updatePatientDirectnConsultFee(isPatientDirect, receiveConsultFee);
    }

    public W9FormData saveW9FormData(final W9FormData w9FormData) {
        W9FormModel w9FormModel = modelService.create(W9FormModel.class);

        w9FormDataReversePopulator.populate(w9FormData, w9FormModel);

        w9FormModel = wholescriptsUserService.saveW9FormData(w9FormModel);
        return wholescriptsW9FormConverter.convert(w9FormModel);
    }


    @Override
    public WishlistData saveWishList(final String productCode) {
        if (StringUtils.isNotEmpty(productCode)) {
            final UserModel currentUser = userService.getCurrentUser();
            final WishlistData wishlistData = new WishlistData();
            final ProductModel product = productService.getProductForCode(productCode);
            if (Objects.nonNull(currentUser) && Objects.nonNull(((B2BCustomerModel) currentUser).getDefaultB2BUnit())) {
                final String accountNumber = WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(
                        ((B2BCustomerModel) currentUser).getDefaultB2BUnit().getUid()
                );

                if (Boolean.TRUE.equals(product.getIsMedpax())) {
                    return handleMedpaxProduct(currentUser, product, accountNumber, wishlistData);
                } else {
                    return handleBlueLabelProduct(currentUser, product, accountNumber, wishlistData);
                }
            }
        }
        return new WishlistData();
    }

    // Helper method to handle Medpax products
    private WishlistData handleMedpaxProduct(final UserModel currentUser, final ProductModel product, final String accountNumber, final WishlistData wishlistData) {
        final String medpaxWishlistName = MEDPAX_PLPWISHLIST_PREFIX.concat(accountNumber).toLowerCase();
        final Wishlist2Model medpaxWishlist = getOrCreateWishlist(currentUser, medpaxWishlistName);
        addProductToWishlist(medpaxWishlist, product, medpaxWishlistName);

        return this.populateWishList(medpaxWishlist, wishlistData);
    }


    // Helper method to handle Blue Label (normal) products
    private WishlistData handleBlueLabelProduct(final UserModel currentUser, final ProductModel product, final String accountNumber, final WishlistData wishlistData) {
        final String blueLabelWishlistName = BLUE_LABEL_PLPWISHLIST_PREFIX.concat(accountNumber).toLowerCase();
        final Wishlist2Model blueLabelWishlist = getOrCreateWishlist(currentUser, blueLabelWishlistName);
        addProductToWishlist(blueLabelWishlist, product, blueLabelWishlistName);
        return this.populateWishList(blueLabelWishlist, wishlistData);
    }

    // Creates or retrieves an existing wishlist
    private Wishlist2Model getOrCreateWishlist(final UserModel user, final String wishlistName) {
        Optional<Wishlist2Model> wishlistOpt = wholescriptsWishlist2Service.findWishlistsByUsernName(user, wishlistName);

        if (wishlistOpt.isEmpty()) {
            wishlistService.createWishlist(user, wishlistName, Boolean.FALSE.toString());
            wishlistOpt = wholescriptsWishlist2Service.findWishlistsByUsernName(user, wishlistName);
        }

        return wishlistOpt.orElse(null);
    }

    // Adds a product to a wishlist (if it isn't already present)
    private void addProductToWishlist(final Wishlist2Model wishlist, final ProductModel product, final String wishlistName) {
        if (wishlist != null && wishlist.getEntries().stream().noneMatch(e -> e.getProduct().getCode().equalsIgnoreCase(product.getCode()))) {
            wishlistService.addWishlistEntry(wishlist, product, 1, Wishlist2EntryPriority.HIGH, wishlistName);
        }
    }


    @Override
    public WishlistData removeWishListEntry(final String productCode) {
        if (StringUtils.isNotEmpty(productCode)) {
            final UserModel currentUser = userService.getCurrentUser();
            if (Objects.nonNull(currentUser) && Objects.nonNull(((B2BCustomerModel) currentUser).getDefaultB2BUnit())) {
                final ProductModel product = productService.getProductForCode(productCode);
                final String accountNumber = WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(
                        ((B2BCustomerModel) currentUser).getDefaultB2BUnit().getUid()
                );

                if (Boolean.TRUE.equals(product.getIsMedpax())) {
                    return handleMedpaxProductRemoval(currentUser, product, accountNumber);
                } else {
                    return handleBlueLabelProductRemoval(currentUser, product, accountNumber);
                }
            }
        }
        return new WishlistData();
    }

    // Helper method to remove Medpax product (only from Medpax Wishlist)
    private WishlistData handleMedpaxProductRemoval(final UserModel currentUser, final ProductModel product, final String accountNumber) {
        // Remove product only from Medpax Wishlist
        final String medpaxWishlistName = MEDPAX_PLPWISHLIST_PREFIX.concat(accountNumber).toLowerCase();
        return removeProductFromWishlist(currentUser, product, medpaxWishlistName);
    }

    // Helper method to remove Blue Label products
    private WishlistData handleBlueLabelProductRemoval(final UserModel currentUser, final ProductModel product, final String accountNumber) {
        final String blueLabelWishlistName = BLUE_LABEL_PLPWISHLIST_PREFIX.concat(accountNumber).toLowerCase();
        return removeProductFromWishlist(currentUser, product, blueLabelWishlistName);
    }

    // Removes a product from the specified wishlist
    private WishlistData removeProductFromWishlist(final UserModel user, final ProductModel product, final String wishlistName) {
        final Optional<Wishlist2Model> optionalWishlist = wholescriptsWishlist2Service.findWishlistsByUsernName(user, wishlistName);

        if (optionalWishlist.isPresent()) {
            final Wishlist2Model wishlist = optionalWishlist.get();
            wishlistService.removeWishlistEntryForProduct(product, wishlist);
            return populateWishList(wishlist, new WishlistData());
        }

        return new WishlistData();
    }


    @Override
    public List<WishlistData> getAllWishList() {
        final UserModel currentUser = userService.getCurrentUser();
        final List<WishlistData> wishListDataList = new ArrayList<>();
        final List<String> wishlistNames = new ArrayList<>();

        final List<Wishlist2Model> newWishList = wholescriptsWishlist2Service.findWishlistsByUser(currentUser);

        if (Objects.nonNull(newWishList) && !newWishList.isEmpty()) {
            for (final Wishlist2Model w : newWishList) {
                if (!wishlistNames.contains(w.getName())) {
                    final WishlistData wishlistData = new WishlistData();
                    wishListDataList.add(populateWishList(w, wishlistData));
                    wishlistNames.add(w.getName());
                }
            }
        }

        return wishListDataList;
    }


    private WishlistData populateWishList(final Wishlist2Model wishlist2Model, final WishlistData wishlistData) throws ConversionException {
        wishlistData.setName(wishlist2Model.getName());
        final List<WishlistEntryData> entries = new ArrayList<>();
        if (Objects.nonNull(wishlist2Model) && Objects.nonNull(wishlist2Model.getEntries())) {
            wishlist2Model.getEntries().stream().forEachOrdered(e -> {
                entries.add(this.populateWishListEntries(e));
            });
        }
        wishlistData.setWishListEntries(entries);
        return wishlistData;
    }

    private WishlistEntryData populateWishListEntries(final Wishlist2EntryModel wishlist2EntryModel) throws ConversionException {
        final WishlistEntryData varWishlistEntryData = new WishlistEntryData();
        if (Objects.nonNull(wishlist2EntryModel) && Objects.nonNull(wishlist2EntryModel.getProduct())) {
            varWishlistEntryData.setProductCode(wishlist2EntryModel.getProduct().getCode());
        }
        return varWishlistEntryData;
    }

    @Override
    public AddressData addAddress(final AddressData addressData) throws Exception {

        validateParameterNotNullStandardMessage("addressData", addressData);
        final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
        if (Objects.nonNull(customer)) {

            final AccertifyVerificationUpdateAccountRequestData accretifyUpdateAddressRequest = new AccertifyVerificationUpdateAccountRequestData();

            final AccertifyVerificationPreviousValuesData previousValues = new AccertifyVerificationPreviousValuesData();

            previousValues.setEmailAddress(customer.getUid());
            previousValues.setPhone(addressData.getPhone());
            previousValues.setFirstName(customer.getName());
            previousValues.setLastName("");
            previousValues.setHashedPassword(addressData.getAccertifyHashedPassword());
            previousValues.setUsername(customer.getUid());

            previousValues.setAddresses(Collections.emptyList());

            accretifyUpdateAddressRequest.setPreviousValues(previousValues);

            wsAccertifyUpdateAddressPopulator.populate(addressData, accretifyUpdateAddressRequest);
            if (accretifyUpdateAddressRequest != null
                    && accretifyUpdateAddressRequest.getUpdatedValues() != null
                    && accretifyUpdateAddressRequest.getUpdatedValues().getAddresses() != null
                    && !accretifyUpdateAddressRequest.getUpdatedValues().getAddresses().isEmpty()) {

                accretifyUpdateAddressRequest.getUpdatedValues().getAddresses().get(0).setPrimaryFlag(false);
            }

      	   //final AccertifyVerificationCreateAccountResponseData accertifyResponseData = wholescriptsAccertifyFacade.accertifyCreateAccountVerification(accretifyUpdateAddressRequest);
            final AccertifyVerificationUpdateAccountResponseData accertifyResponseData = wholescriptsAccertifyFacade.accertifyUpdateAccountVerification(accretifyUpdateAddressRequest);

      	   if (accertifyResponseData.getResults().getRiskScore().getRecommendationCode().equalsIgnoreCase("Approve")) {
      	      // Create the new address model
         	   final AddressModel newAddress = modelService.create(AddressModel.class);
               addressReversePopulator.populate(addressData, newAddress);
               newAddress.setRemarks(addressData.getRemarks());

               final SapAddressResponseData responseData= wholescriptsIntegrationsService.createAddressinSAP(addressData,customer);

               if(responseData!=null) {
                   if(responseData.getRETURN().equals("0")) {
                       // Store the address against the B2B Unit
                       wholescriptsUserService.saveAddressEntry(customer, newAddress,responseData);

                       // Update the address ID in the newly created address
                       addressData.setId(newAddress.getPk().toString());
                       return addressData;
                   }
               }
      	   } else {
      	   	throw new AccertifyValidationException("Accertify address validation failed");
      	   }
        }

        return new AddressData();
    }

    @Override
    public List<AddressData> getAllAddressForCustomers(final String addressSearch) throws Exception {
        final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
        LOG.info("Fetching addresses for customer: {}", customer != null ? customer.getUid() : "null");

        final WholescriptsGetCCResponseData creditCardInfo = wholescriptsIntegrationsService.getCreditCardInfoFromSap(customer);

        if (creditCardInfo == null || creditCardInfo.getCartData() == null) {
            LOG.warn("No credit card data found for customer: {}", customer != null ? customer.getUid() : "null");
        }

        // Mapping of payer B2BUnitModel to its associated card IDs
        final Map<B2BUnitModel, List<String>> payerUnitToCardIds = new HashMap<>();

        try {
            searchRestrictionService.disableSearchRestrictions();

            for (final WholescriptsGetCCCartItemResponseData item : creditCardInfo.getCartData().getItem()) {
                final String payerId = removeLeadingZeros(item.getPayer());
                final String salesAreaAccountNo = WholescriptsCoreUtils.getAccountNumberWithSalesArea(payerId);
                LOG.debug("Processing payer: {} (Sales Area Account No: {})", payerId, salesAreaAccountNo);

                final B2BUnitModel payerUnit = b2bUnitService.getUnitForUid(salesAreaAccountNo);
                if(Objects.nonNull(payerUnit))
                {
                    final String cardId = item.getCardId();
                    payerUnitToCardIds.computeIfAbsent(payerUnit, k -> new ArrayList<>());
                    if (!payerUnitToCardIds.get(payerUnit).contains(cardId)) { // Avoid duplicates
                        payerUnitToCardIds.get(payerUnit).add(cardId);
                        LOG.debug("Mapped payer unit {} to card ID {}", payerUnit.getUid(), cardId);
                    } else {
                        LOG.debug("Duplicate card ID {} for payer unit {} ignored.", cardId, payerUnit.getUid());
                    }
                }

            }
        } catch (final Exception e) {
            LOG.error("Error processing payer units", e);
        } finally {
            searchRestrictionService.enableSearchRestrictions();
        }

        if (Objects.nonNull(customer) && Objects.nonNull(customer.getDefaultB2BUnit())) {
            LOG.info("Fetching addresses for customer's default B2B unit: {}", customer.getDefaultB2BUnit().getUid());
            final List<AddressModel> searchDataList = wholescriptsUserService.getAllAddressForCustomers(customer.getDefaultB2BUnit(), addressSearch);

            return searchDataList.stream()
                    .map(address -> {
                        final Optional<AddressData> addressDataOpt = getAddressData(address);
                        if (addressDataOpt.isPresent()) {
                            final AddressData addressData = addressDataOpt.get();

                            // Get B2BUnitModel safely
                            final B2BUnitModel addressUnit = Optional.ofNullable(address.getOwner())
                                    .filter(B2BUnitModel.class::isInstance)
                                    .map(B2BUnitModel.class::cast)
                                    .orElse(null);

                            if (addressUnit != null && payerUnitToCardIds.containsKey(addressUnit)) {
                                addressData.setIsCardAvailable(true);
                                addressData.setCreditCardIds(payerUnitToCardIds.get(addressUnit));
                                LOG.debug("Address {} is linked to payer unit {} with available cards: {}",
                                        address.getPk(), addressUnit.getUid(), payerUnitToCardIds.get(addressUnit));
                            } else {
                                addressData.setIsCardAvailable(false);
                                LOG.debug("Address {} is not linked to any payer unit with credit cards", address.getPk());
                            }
                            return addressData;
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        LOG.warn("Customer or customer default B2B unit is null.");
        return Collections.emptyList();
    }





    private String removeLeadingZeros(final String accountNumber) {
        return accountNumber != null ? accountNumber.replaceFirst("^0+", "") : "";
    }


    @Override
    public CustomerData getPatientDetails(final String email) {

        B2BCustomerModel customer=wholescriptsUserService.getPatientModelByID(email);
        if(Objects.nonNull(customer))
        {
      	  CustomerData customerData = customerConverter.convert(customer);
           if(StringUtils.isEmpty(customerData.getReferralCode())) {
        		try
				{
					Thread.sleep(Integer.valueOf(configurationService.getConfiguration().getString("referral.code.sleep.time")));
					customer=wholescriptsUserService.getPatientModelByID(email);
				}
				catch (InterruptedException e)
				{
		        LOG.error(e.getMessage());
				}
        		if (Objects.nonNull(customer.getDefaultB2BUnit()) &&  CollectionUtils.isNotEmpty(customer.getDefaultB2BUnit().getReferralCodes())) {
   				List<ReferralCodesModel> referralCodesList = new ArrayList<>(customer.getDefaultB2BUnit().getReferralCodes());
   				String firstReferralCode = referralCodesList.get(0).getReferralCode();
   				String lastName = referralCodesList.get(0).getLastName();
   				customerData.setReferralCode(firstReferralCode.concat("|").concat(lastName));
   				customerData.setDiscount(customer.getDefaultB2BUnit().getDiscount());
   			}
           }
            return customerData;
        }

        return  new CustomerData();
    }


    public Optional<AddressData> getAddressData(final AddressModel address)
    {

        return Optional.ofNullable(  addressConverter.convert(address));
    }


    public WholescriptsGetAddressFromSapResponseData getAddressFromSap(final B2BCustomerModel b2bCustomer) throws Exception {
        final WholescriptsGetAddressFromSapResponseData addressResponseData = wholescriptsIntegrationsService.getAddressFromSap(b2bCustomer);
        final WholescriptsGetCCResponseData creditCardInfo = wholescriptsIntegrationsService.getCreditCardInfoFromSap(b2bCustomer);

        for (final WholescriptsGetAddressFromSapItemResponseData addressItem : addressResponseData.getAddresses().getItem()) {
            if (Objects.nonNull(creditCardInfo) && creditCardInfo.getCartData() != null && !creditCardInfo.getCartData().getItem().isEmpty()) {
                final List<String> cardPayer = creditCardInfo.getCartData().getItem().stream().map(i -> i.getPayer()).collect(toList());
                if (cardPayer.contains(addressItem.getAddressNumber())) {
                    addressItem.setIsCardAvailable(Boolean.TRUE);
                } else {
                    addressItem.setIsCardAvailable(Boolean.FALSE);
                }
            } else {
                addressItem.setIsCardAvailable(Boolean.FALSE);
            }
        }
        return addressResponseData;
    }



    @Override
    public AddressData addAddressWithoutSAP(final AddressData addressData,final String addressNumber) {

        validateParameterNotNullStandardMessage("addressData", addressData);
        final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
        if (Objects.nonNull(customer)) {
            // Create the new address model
            final AddressModel newAddress = modelService.create(AddressModel.class);
            addressReversePopulator.populate(addressData, newAddress);
            newAddress.setRemarks(addressData.getAddressDescription());
            wholescriptsUserService.saveAddressEntryForCart(customer, newAddress,addressNumber);
            addressData.setId(newAddress.getPk().toString());
            return addressData;
        }
        return new AddressData();
    }


    @Override
 	public AddressChangeResponseData updateAddress(final AddressWsDTO addressWsDTO, final AddressData addressData) throws Exception
 	{
 		final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
 		AddressChangeResponseData addressUpdateResponse = null;
 		if (Objects.nonNull(customer) && Objects.nonNull(customer.getDefaultB2BUnit()))
 		{
 			final B2BUnitModel unit = customer.getDefaultB2BUnit();
 			if ("US-NY".equals(addressData.getRegion().getIsocode()) && StringUtils.containsAnyIgnoreCase(addressData.getLine1(), configurationService.getConfiguration().getString("wholescripts.address.po.box"))) {
 				throw new POBoxAgeRestrictionException(configurationService.getConfiguration().getString("po.address.age.restrictions.error"));
       	 }
 			 if((StringUtils.isEmpty(unit.getAgeExpirationDate()) || unit.getAgeExpirationDate().equals("0000-00-00")) || (StringUtils.isNotEmpty(unit.getAgeBlockedDate()) && !unit.getAgeBlockedDate().equals("0000-00-00"))) {
 				if("US-NY".equals(addressData.getRegion().getIsocode())) {
 					throw new AgeRestrictionException(configurationService.getConfiguration().getString("address.age.restrictions.error"));
 				}
 			}
 			final String reqAddressNumber = addressWsDTO.getAddressNumber();
 			final String sapAddressNumber = String.format("%010d", Integer.valueOf(reqAddressNumber));
 			addressWsDTO.setAddressNumber(sapAddressNumber);

 			final List<AddressModel> allAddresses = wholescriptsUserService.getAllAddressForCustomers(unit, null);
 			final String accountNumberWithSalesArea = WholescriptsCoreUtils.getAccountNumberWithSalesArea(reqAddressNumber);
 			final String addessPublicKey = accountNumberWithSalesArea + "|" + reqAddressNumber + ADDRESS_SHIPPING_KEY;
 			final Optional<AddressModel> addressOption = allAddresses.stream()
 					.filter(address -> addessPublicKey.equalsIgnoreCase(address.getSapPublicKey())).findAny();

			final AccertifyVerificationUpdateAccountRequestData accretifyUpdateAddressRequest = new AccertifyVerificationUpdateAccountRequestData();

			if (addressOption.isPresent()) {
 				final AddressModel addressModel = addressOption.get();
 				wsAccertifyOldAddressPopulator.populate(addressModel, accretifyUpdateAddressRequest);
                if (accretifyUpdateAddressRequest != null
                        && accretifyUpdateAddressRequest.getPreviousValues() != null
                        && accretifyUpdateAddressRequest.getPreviousValues().getAddresses() != null
                        && !accretifyUpdateAddressRequest.getPreviousValues().getAddresses().isEmpty()) {

                    accretifyUpdateAddressRequest.getPreviousValues().getAddresses().get(0).setPrimaryFlag(false);
                }
				
 				wsAccertifyUpdateAddressPopulator.populate(addressData, accretifyUpdateAddressRequest);
                if (accretifyUpdateAddressRequest != null
                        && accretifyUpdateAddressRequest.getUpdatedValues() != null
                        && accretifyUpdateAddressRequest.getUpdatedValues().getAddresses() != null
                        && !accretifyUpdateAddressRequest.getUpdatedValues().getAddresses().isEmpty()) {

                    accretifyUpdateAddressRequest.getUpdatedValues().getAddresses().get(0).setPrimaryFlag(true);
                }

 				final AccertifyVerificationUpdateAccountResponseData accertifyResponseData = wholescriptsAccertifyFacade.accertifyUpdateAccountVerification(accretifyUpdateAddressRequest);
 				
 				if (accertifyResponseData.getResults().getRiskScore().getRecommendationCode().equalsIgnoreCase("Approve")) {
 					//updating the address in sap
 					addressUpdateResponse = wholescriptsIntegrationsService.updateAddressInSAP(addressWsDTO);
 					//updating the address commerce
 	 				addressReversePopulator.populate(addressData, addressModel);
 	 				addressModel.setRemarks(addressData.getRemarks());
 	 				modelService.save(addressModel);
 				}
 			} else {
   	   	throw new AccertifyValidationException("Accertify address validation failed");
   	   }
 		}
 		return addressUpdateResponse;
 	}



    @Override
    public void removeAddressInSAP(final B2BCustomerModel currentCustomer, final String addressId, AddressData addressData) throws Exception {
   	 final String sapAddressNumber = String.format("%010d",Integer.valueOf(addressId));
   	 wholescriptsIntegrationsService.removeAddress(currentCustomer, sapAddressNumber);
   	 final String accountNumberWithSalesArea = WholescriptsCoreUtils.getAccountNumberWithSalesArea(addressId);
     searchRestrictionService.disableSearchRestrictions();

     try {
         final B2BUnitModel shipToUnit = b2bUnitService.getUnitForUid(accountNumberWithSalesArea);

         if (shipToUnit == null) {
             return;
         }

         final String addressNumberWithSalesArea = WholescriptsCoreUtils.getAccountNumberWithSalesArea(addressId);

         final String publicKey = addressNumberWithSalesArea + "|" + addressId + ADDRESS_SHIPPING_KEY;
         String sapPublicKey = Arrays.stream(publicKey.split("_"))
                 .map(s -> s.equals(publicKey.split("_")[0]) ? String.format("%010d", Integer.parseInt(s)) : s)
                 .collect(Collectors.joining("_"));

         AddressModel address = shipToUnit.getAddresses().stream()
                 .filter(add -> add.getSapPublicKey().equals(publicKey))
                 .findFirst()
                 .orElse(null);

         if (address != null) {
             final AccertifyVerificationUpdateAccountRequestData accretifyUpdateAddressRequest = new AccertifyVerificationUpdateAccountRequestData();

             final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();

             accretifyUpdateAddressRequest.setUpdateTrigger("customerInitiated");
             accretifyUpdateAddressRequest.setAccountID(WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(customer.getDefaultB2BUnit().getUid()));

             wsAccertifyOldAddressPopulator.populate(address, accretifyUpdateAddressRequest);
             if (accretifyUpdateAddressRequest != null
                     && accretifyUpdateAddressRequest.getPreviousValues() != null
                     && accretifyUpdateAddressRequest.getPreviousValues().getAddresses() != null
                     && !accretifyUpdateAddressRequest.getPreviousValues().getAddresses().isEmpty()) {

                 accretifyUpdateAddressRequest.getPreviousValues().getAddresses().get(0).setPrimaryFlag(false);
             }

             final List<AccertifyVerificationAddressRequestData> updatedAddresses = new ArrayList<AccertifyVerificationAddressRequestData>();
             final AccertifyVerificationAddressRequestData updatedAddress = new AccertifyVerificationAddressRequestData();
             final AccertifyVerificationUpdatedValuesData updatedValues = new AccertifyVerificationUpdatedValuesData();
             updatedValues.setEmailAddress(customer.getUid());
             updatedValues.setPhone("");
             updatedValues.setFirstName(customer.getName());
             updatedValues.setLastName("");
             updatedValues.setHashedPassword("");
             updatedValues.setUsername(customer.getUid());
             updatedValues.setHashedPassword("");

             updatedAddresses.add(updatedAddress);
             updatedValues.setAddresses(Collections.emptyList());
             accretifyUpdateAddressRequest.setUpdatedValues(updatedValues);

             if(null != addressData){
                 accretifyUpdateAddressRequest.setEventSource(addressData.getAccertifyEventSource());
                 accretifyUpdateAddressRequest.setEmailAddress(customer.getUid());
                 accretifyUpdateAddressRequest.setHashedPassword(addressData.getAccertifyHashedPassword());
                 accretifyUpdateAddressRequest.setUsername(customer.getUid());
                 accretifyUpdateAddressRequest.setBrand("WholeScripts");
                 accretifyUpdateAddressRequest.setDeviceTransactionID(addressData.getAccertifyDeviceTransactionId());
                 accretifyUpdateAddressRequest.setUbaID(addressData.getAccertifyUbaID());
                 accretifyUpdateAddressRequest.setUbaEvents(addressData.getAccertifyUbaEvents());
                 accretifyUpdateAddressRequest.setSessionID(addressData.getAccertifySessionID());
                 accretifyUpdateAddressRequest.setIpAddress(addressData.getIpAddress());
                 accretifyUpdateAddressRequest.setUserAgent(addressData.getUserAgent());
                 accretifyUpdateAddressRequest.setPageID(addressData.getAccertifyPageId());
             }

             final AccertifyVerificationUpdateAccountResponseData accertifyResponseData = wholescriptsAccertifyFacade.accertifyUpdateAccountVerification(accretifyUpdateAddressRequest);
         }

         shipToUnit.setActive(false);
         modelService.save(shipToUnit);

     }finally {
         searchRestrictionService.enableSearchRestrictions();
     }
    }

    @Override
    public void updateCCPaymentInfo(final String paymentInfo) {

        validateParameterNotNullStandardMessage("paymentInfo", paymentInfo);
        final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
        final CreditCardPaymentInfoModel paymentInfoModel = customerAccountService
                .getCreditCardPaymentInfoForCode(customer, paymentInfo);
        modelService.save(paymentInfoModel);
        if (paymentInfoModel != null)
        {
            final CartModel cart=cartService.getSessionCart();
            if(Objects.nonNull(paymentInfoModel.getBillingAddress()))
            {
                cart.setPaymentAddress(paymentInfoModel.getBillingAddress());

            }
            cart.setPaymentInfo(paymentInfoModel);
            modelService.save(cart);
            modelService.refresh(cart);
        }
        else {
            throw new RequestParameterException("Payment details [" + sanitize(paymentInfo) + "] not found.",
                    RequestParameterException.UNKNOWN_IDENTIFIER, "paymentDetailsId");
        }

    }

    @Override
    public List<CCPaymentInfoData> getCCPaymentInfos(final boolean saved, final CCPaymentInfoData cartPaymentInfo) throws Exception {
        final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
        List<CreditCardPaymentInfoModel> creditCards = new ArrayList<>();
        creditCards= wholescriptsUserService.getCCPaymentInfos(customer,creditCards,cartPaymentInfo);
        final List<CCPaymentInfoData> ccPaymentInfos = new ArrayList<CCPaymentInfoData>();

        for (final CreditCardPaymentInfoModel ccPaymentInfoModel : creditCards)
        {
            final CCPaymentInfoData paymentInfoData = creditCardPaymentInfoConverter.convert(ccPaymentInfoModel);
            if(paymentInfoData == null)
            {
                continue;
            }
            ccPaymentInfos.add(paymentInfoData);

        }
        return ccPaymentInfos;
    }

    @Override
    public CCPaymentInfoData getCartPaymentInfo() {
        final CartModel cart=cartService.getSessionCart();
        final CCPaymentInfoData ccPaymentInfo = new CCPaymentInfoData();
        if(Objects.nonNull(cart.getPaymentInfo()))
        {
            final CreditCardPaymentInfoModel paymentInfo= (CreditCardPaymentInfoModel) cart.getPaymentInfo();
            if(Objects.nonNull(paymentInfo))
            {
                final CCPaymentInfoData paymentInfoData = creditCardPaymentInfoConverter.convert(paymentInfo);
                if(Objects.nonNull(paymentInfoData))
                {
                    return paymentInfoData;
                }

            }
        }
        return ccPaymentInfo;
    }

    @Override
    public void verifyAgeAndUpdateInSAP(final String inquiryId) throws Exception
    {
        wholescriptsUserService.verifyAgeAndUpdateInSAP(inquiryId);
    }

    @Override
    public ExternalSystemResponseData sendPractitionerAmendment(final PractitionerAmendmentRequestWsDTO request) throws Exception {
        final B2BCustomerModel customer = (B2BCustomerModel) userService.getCurrentUser();
        final PractitionerAmendmentRequestData requestData = prepareAmendmentRequestData(request, customer);
        final ExternalSystemResponseData response = wholescriptsIntegrationsService.sendPractitionerAmendment(requestData);
        customer.setIsAmendmentSigned(response.isSuccess());
        modelService.save(customer);
        return response;
    }

    private PractitionerAmendmentRequestData prepareAmendmentRequestData(final PractitionerAmendmentRequestWsDTO request, final B2BCustomerModel customer) {

        final PractitionerAmendmentRequestData amendmentRequestData = new  PractitionerAmendmentRequestData();

        final B2BUnitModel defaultb2bUnit = customer.getDefaultB2BUnit();

        final String accountNumber = WholescriptsCoreUtils
                .getAccountNumberForDefaultB2bUnit(customer.getDefaultB2BUnit().getUid());

        amendmentRequestData.setFullName(customer.getName());
        amendmentRequestData.setEmail(customer.getUid());
        amendmentRequestData.setSignedName(request.getSignedName());
        amendmentRequestData.setSignedDate(request.getSignedDate());
        amendmentRequestData.setSapUserId(accountNumber);
        amendmentRequestData.setCcLastFour(request.getCcLastFour());
        amendmentRequestData.setType(request.getType());
        amendmentRequestData.setBrowserString(request.getBrowserString());
        amendmentRequestData.setIp(request.getIp());

        final String accountNumberWithoutFormat = WholescriptsCoreUtils
                .getAccountNumberForDefaultB2bUnitWithoutFormat(defaultb2bUnit.getUid());
        final Optional<AddressModel> addressOptional = defaultb2bUnit.getAddresses().stream()
                .filter(a -> a.getSapPublicKey() != null && a.getSapPublicKey().contains(accountNumberWithoutFormat)
                        && a.getSapPublicKey().contains(B2BUNITSHIPPING))
                .findAny();
        if (addressOptional.isPresent()) {
            final AddressModel address = addressOptional.get();
            amendmentRequestData.setPhone(address.getPhone1());
            amendmentRequestData.setFax(address.getFax());
            final String practiceAddress = address.getStreetname().concat(CONCAT_ADDRESS).concat(address.getTown())
                    .concat(CONCAT_ADDRESS).concat(address.getPostalcode());
            amendmentRequestData.setPracticeAddress(practiceAddress);
            amendmentRequestData.setPracticeCountry(address.getCountry().getIsocode());

        }

        return amendmentRequestData;

    }

    @Override
    public CustomerExternalDetailsData getExternalDetails() throws Exception {
        final B2BCustomerModel customer = (B2BCustomerModel) userService.getCurrentUser();
        CustomerExternalDetailsData externalDetailsData = new CustomerExternalDetailsData();
        if (customer != null && customer.getDefaultB2BUnit() != null) {
            final String accountNumber = WholescriptsCoreUtils
                    .getAccountNumberForDefaultB2bUnit(customer.getDefaultB2BUnit().getUid());
            externalDetailsData = wholescriptsIntegrationsService.getExternalDetails(accountNumber);
        }
        return externalDetailsData;
    }


    @Override
    public void removeCreditCard(final String cartId, final String cardNumber, final B2BCustomerModel customer) {
        final List<CreditCardPaymentInfoModel> creditCards = customerAccountService.getCreditCardPaymentInfos(customer,
                false);
        if(CollectionUtils.isNotEmpty(creditCards))
        {
            final Optional<CreditCardPaymentInfoModel> cardToDelete=creditCards.stream().filter(p->p.getCardId()!=null && p.getNumber()!=null && p.getCardId().equals(cartId) && getMaskedCardNumber(p.getNumber()).equals(cardNumber)).findFirst();
            if(cardToDelete.isPresent())
            {
                final CreditCardPaymentInfoModel paymentInfo=cardToDelete.get();
                modelService.remove(paymentInfo);
                modelService.refresh(customer);

            }
            else
            {
                throw new IllegalArgumentException(
                        "Credit Card Payment Info "  + " does not belong to the customer " + customer + " and will not be removed.");
            }

        }

    }


    @Override
    public PractitionerAmendmentDetailData getPractitionerAmendment(final String type) throws Exception {
        final B2BCustomerModel customer = (B2BCustomerModel) userService.getCurrentUser();
        PractitionerAmendmentDetailData amendmentDetailsData = new PractitionerAmendmentDetailData();
        if (customer != null && customer.getDefaultB2BUnit() != null) {
            final String accountNumber = WholescriptsCoreUtils
                    .getAccountNumberForDefaultB2bUnit(customer.getDefaultB2BUnit().getUid());
            amendmentDetailsData = wholescriptsIntegrationsService.getPractitionerAmendment(type, accountNumber);
        }
        return amendmentDetailsData;
    }


    @Override
    public EstoreCredentialResponseData addEstoreCredentials(final EstoreCredentialRequestData requestBody) throws Exception {
        return wholescriptsIntegrationsService.addEstoreCredentials(requestBody);
    }

    @Override
    public EstoreCredentialResponseData deleteEstoreCredentials(final EstoreCredentialRequestData requestBody) throws Exception {
        return wholescriptsIntegrationsService.deleteEstoreCredentials(requestBody);
    }

    @Override
    public EstoreCredentialResponseData changeEstoreCredentials(final EstoreCredentialRequestData requestBody) throws Exception {
        return wholescriptsIntegrationsService.changeEstoreCredentials(requestBody);
    }

    @Override
    public EstoreOrderHistory getEstorePendingOrders() throws Exception {
        final B2BCustomerModel customer = (B2BCustomerModel) userService.getCurrentUser();
        EstoreOrderHistory orders = new EstoreOrderHistory();
        if (customer != null && customer.getDefaultB2BUnit() != null) {
            final String accountNumber = WholescriptsCoreUtils
                    .getAccountNumberForDefaultB2bUnit(customer.getDefaultB2BUnit().getUid());
            orders = wholescriptsIntegrationsService.getEstorePendingOrders(accountNumber);
        }
        return orders;
    }

    @Override
    public ShopifyStatusResponseData getShopifyStatus () throws Exception {
        final B2BCustomerModel customer = (B2BCustomerModel) userService.getCurrentUser();
        ShopifyStatusResponseData statusRespsonse = new ShopifyStatusResponseData();
        if (customer != null && customer.getDefaultB2BUnit() != null) {
            final String accountNumber = WholescriptsCoreUtils
                    .getAccountNumberForDefaultB2bUnit(customer.getDefaultB2BUnit().getUid());
            statusRespsonse = wholescriptsIntegrationsService.getShopifyStatus(accountNumber);
        }
        return statusRespsonse;
    }

    @Override
    public ShopifyProductDetailsData getShopifyProducts() throws Exception {
        final B2BCustomerModel customer = (B2BCustomerModel) userService.getCurrentUser();
        ShopifyProductDetailsData response = new ShopifyProductDetailsData();
        if (customer != null && customer.getDefaultB2BUnit() != null) {
            final String accountNumber = WholescriptsCoreUtils
                    .getAccountNumberForDefaultB2bUnit(customer.getDefaultB2BUnit().getUid());
            response = wholescriptsIntegrationsService.getShopifyProducts(accountNumber);
        }
        return response;
    }


    @Override
    public String getMaskedCardNumber(final String cardNumber) {
        return wholescriptsUserService.getMaskedCardNumber(cardNumber);
    }

    @Override
    public WholescriptsGetCCResponseData maskCardNumber(final WholescriptsGetCCResponseData wholescriptsGetCCResponseData) {

        final WholescriptsGetCCCartItemListResponseData listResponseData = wholescriptsGetCCResponseData.getCartData();
        if (Objects.nonNull(listResponseData)) {
            for (final WholescriptsGetCCCartItemResponseData itemData : listResponseData.getItem()) {

                itemData.setCardNumber(getMaskedCardNumber(itemData.getCardNumber()));
            }

        }
        return wholescriptsGetCCResponseData;
    }

    @Override
    public ExternalSystemResponseData addShopifyProducts(final ShopifyProductsRequestData shopifyProducts) throws Exception {
        final B2BCustomerModel customer = (B2BCustomerModel) userService.getCurrentUser();
        ExternalSystemResponseData response = new ExternalSystemResponseData();
        if (customer != null && customer.getDefaultB2BUnit() != null) {
            final String accountNumber = WholescriptsCoreUtils
                    .getAccountNumberForDefaultB2bUnit(customer.getDefaultB2BUnit().getUid());
            response = wholescriptsIntegrationsService.addShopifyProducts(accountNumber, shopifyProducts.getProducts());
        }
        return response;
    }

    @Override
    public ExternalSystemResponseData deleteShopifyProducts(final ShopifyProductsRequestData shopifyProducts) throws Exception {
        final B2BCustomerModel customer = (B2BCustomerModel) userService.getCurrentUser();
        ExternalSystemResponseData response = new ExternalSystemResponseData();
        if (customer != null && customer.getDefaultB2BUnit() != null) {
            final String accountNumber = WholescriptsCoreUtils
                    .getAccountNumberForDefaultB2bUnit(customer.getDefaultB2BUnit().getUid());
            response = wholescriptsIntegrationsService.deleteShopifyProducts(accountNumber, shopifyProducts.getProducts());
        }
        return response;
    }

    @Override
    public ExternalSystemResponseData estoreShippingConfirmation(final EstoreShippingConfirmationRequest requestBody)
            throws Exception {
        return wholescriptsIntegrationsService.estoreShippingConfirmation(requestBody);
    }

    @Override
    public ExternalSystemResponseData addEstoreCard(final SopPaymentDetailsWsDTO sopPaymentDetails, final WholescriptsGetCCResponseData wholescriptsGetCCResponseData) throws Exception {

   	 final B2BCustomerModel customerModel = (B2BCustomerModel) userService.getCurrentUser();
       final B2BUnitModel unit = customerModel.getDefaultB2BUnit();
   	 final String commerceExpiryMonth = sopPaymentDetails.getCard_expirationMonth();
       final String commerceExpiryYear = sopPaymentDetails.getCard_expirationYear();
       final String commerceLastFour = sopPaymentDetails.getCard_accountNumber().replace("*", "");
       final String commerceCardType = sopPaymentDetails.getCard_cardType();
       
       final WholescriptsGetCCCartItemListResponseData listResponseData = wholescriptsGetCCResponseData.getCartData();
       final ExternalSystemResponseData response = new ExternalSystemResponseData();
       response.setSuccess(false);
       
		 if (Objects.nonNull(listResponseData)) {
			 
			 for (final WholescriptsGetCCCartItemResponseData itemData : listResponseData.getItem()) {

				 final String sapCardTokenDetail = itemData.getCardNumber();
				 final String[] cCardNumber = sapCardTokenDetail.split("-");
				 final String[] cardValidity = itemData.getValidTo().split("-");
				 final String sapCardType = itemData.getCardType();
				 final String sapLastFour = cCardNumber[2];
				 final String sapValidMonth = cardValidity[1];
				 final String sapValidYear = cardValidity[0];

				 if (sapLastFour != null && sapLastFour.equalsIgnoreCase(commerceLastFour) && sapValidMonth != null
						 && sapValidMonth.equalsIgnoreCase(commerceExpiryMonth) && sapValidYear != null
						 && sapValidYear.equalsIgnoreCase(commerceExpiryYear) && sapCardType != null
						 && sapCardType.equalsIgnoreCase(commerceCardType)) {

					 unit.setEstoreCardToken(sapCardTokenDetail);
					 modelService.save(unit);
					 response.setSuccess(true);
					 break;
				 } 

			 }
		 }
       return response;
    }

    @Override
    public SopPaymentDetailsWsDTO getEstoreCard(final WholescriptsGetCCResponseData wholescriptsGetCCResponseData) throws Exception {


        final SopPaymentDetailsWsDTO sopPaymentDetails = new SopPaymentDetailsWsDTO();
        final B2BCustomerModel customerModel = (B2BCustomerModel) userService.getCurrentUser();
        final B2BUnitModel unit = customerModel.getDefaultB2BUnit();
        final String estoreCardToken = unit.getEstoreCardToken();
       
        final WholescriptsGetCCCartItemListResponseData listResponseData = wholescriptsGetCCResponseData.getCartData();
        
        if (Objects.nonNull(listResponseData)) {
 			 
 			 for (final WholescriptsGetCCCartItemResponseData itemData : listResponseData.getItem()) {

 				 final String sapCardTokenDetail = itemData.getCardNumber();
 				 final String[] cCardNumber = sapCardTokenDetail.split("-");
 				 final String[] cardValidity = itemData.getValidTo().split("-");
 				 final String sapCardType = itemData.getCardType();
 				 final String sapLastFour = cCardNumber[2];
 				 final String sapValidMonth = cardValidity[1];
 				 final String sapValidYear = cardValidity[0];

 				 if (estoreCardToken != null && estoreCardToken.equalsIgnoreCase(sapCardTokenDetail)) {
 					 
 					final AddressData billingAddress = itemData.getBillingAddress();
 					
 					if (billingAddress != null) {
 	                sopPaymentDetails.setBillTo_firstName(billingAddress.getFirstName());
 	                sopPaymentDetails.setBillTo_lastName(billingAddress.getLastName());
 	                sopPaymentDetails.setBillTo_street1(billingAddress.getLine1());
 	                sopPaymentDetails.setBillTo_street2(billingAddress.getLine2());
 	                sopPaymentDetails.setBillTo_city(billingAddress.getTown());
 	                sopPaymentDetails.setBillTo_postalCode(billingAddress.getPostalCode());
 	                sopPaymentDetails.setBillTo_phoneNumber(billingAddress.getPhone());
 	                sopPaymentDetails.setBillTo_country(billingAddress.getCountry().getIsocode());
 	                sopPaymentDetails.setBillTo_region(billingAddress.getRegion().getIsocodeShort());
 	            }
 					
 					sopPaymentDetails.setCard_accountNumber(WholescriptsCoreUtils.getMaskedCardNumber(itemData.getCardNumber()));
 	            sopPaymentDetails.setCard_issueNumber(itemData.getCardId());
 	            sopPaymentDetails.setCard_cardType(sapCardType);
 	            sopPaymentDetails.setCard_nameOnCard(itemData.getCreditCardName());
 	            sopPaymentDetails.setCard_expirationMonth(sapValidMonth);
 	            sopPaymentDetails.setCard_expirationYear(sapValidYear);
 	            break;
 				 } 

 			 }
 		 }
       return sopPaymentDetails;
    }

    @Override
    public EstoreOrderHistory estoreOrderHistory(final EstoreOrderHistoryRequestData requestBody) throws Exception {
        return wholescriptsIntegrationsService.estoreOrderHistory(requestBody);
    }

    @Override
    public CustomerData getNotificationSettingsforUser(final String email) {
        final B2BCustomerModel customer=wholescriptsUserService.getUserByUid(email);
        final CustomerData customerData=new CustomerData();
        if(Objects.nonNull(customer))
        {
            if(Objects.nonNull(customer.getIsEmailNotificationEnabled()))
            {
                customerData.setIsEmailNotificationEnabled(customer.getIsEmailNotificationEnabled());

            }
            if(Objects.nonNull(customer.getIsSmsNotificationEnabled()))
            {
                customerData.setIsSmsNotificationEnabled(customer.getIsSmsNotificationEnabled());

            }
        }
        return customerData;
    }
    @Override
    public String changePractitionerForPatient(final ReferralCodeWsDTO referralCodeRequest) throws Exception {
        final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
        return wholescriptsUserService.changePractitionerForPatient(customer,referralCodeRequest);

    }

    @Override
    public CustomerData getPractitionerDetails() {
        final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
        if (Objects.nonNull(customer) && Objects.nonNull(customer.getDefaultB2BUnit())) {
            final String salesPersonId = WholescriptsCoreUtils.getDefaultB2bUnit(customer.getDefaultB2BUnit().getSalesPerson());
            if (StringUtils.isNotBlank(salesPersonId)) {
                final String b2bUnitId = WholescriptsCoreUtils.getAccountNumberWithSalesArea(salesPersonId.replaceFirst("^0+", ""));
                searchRestrictionService.disableSearchRestrictions();
                final B2BUnitModel unitModel = b2bUnitService.getUnitForUid(b2bUnitId);
                if (Objects.nonNull(unitModel)) {
                    final Set<B2BCustomerModel> customers = b2bUnitService.getB2BCustomers(unitModel);
                    searchRestrictionService.enableSearchRestrictions();
                    final Optional<B2BCustomerModel> b2BCustomerModel = customers.stream().findFirst();
                    if (b2BCustomerModel.isPresent()) {
                        final B2BCustomerModel practitioner = b2BCustomerModel.get();

                        return customerConverter.convert(practitioner);
                    }
                }

            }
        }
        return new CustomerData();
    }

	 @Override
	 public boolean saveCustomerActivity(final CustomerActivityData customerActivityData) {

		 try {
			return wholescriptsUserService.saveCustomerActivity(customerActivityData);

		 } catch (final Exception ex) {
			 ex.printStackTrace();
			 return false;
		 }
	 }

    @Override
    public DashboardResponseData getDashBoardDetailsFromSAP(final B2BCustomerModel b2BCustomerModel) throws Exception {

        final DashboardResponseData dashboardResponseData = wholescriptsIntegrationsService.getDashBoardDetailsFromSAP(b2BCustomerModel);

        updateNewTopPatientsID(dashboardResponseData);
        createRecommendations(b2BCustomerModel, dashboardResponseData);
        updateSalesPersonDetails(b2BCustomerModel, dashboardResponseData);

        updatePatientLatestOrders(dashboardResponseData);
        updatePatientMostOrders(dashboardResponseData);

        updatePractionerMostOrders(dashboardResponseData);
        updatePractionerLatestOrders(dashboardResponseData);
        return dashboardResponseData;
    }

    @Override
    public void removeAddressInCommerce(final String addressId, final List<String> creditCardIds, AddressData addressData) throws Exception {
        final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();

        if (creditCardIds != null && !creditCardIds.isEmpty()) {
            for (final String creditCardId : creditCardIds) {
                if (StringUtils.isNotEmpty(creditCardId)) {
                    wholescriptsIntegrationsService.removeCreditCard(creditCardId, customer, addressId);
                }
            }
        }

        removeAddressInSAP(customer, addressId, addressData);
        LOG.info("Successfully removed address ID: {} for customer: {}", addressId, customer.getUid());
    }





    @Override
    @Cacheable(value = "shopifyCache", key = "T(de.hybris.platform.commercewebservicescommons.cache.CommerceCacheKeyGenerator).generateKey(false,false,'searchShopifyProducts',#fields)")
    public ShopifySearchResultData searchShopifyProducts(final String fields) throws Exception {
        final B2BCustomerModel customer = (B2BCustomerModel) userService.getCurrentUser();
        ShopifySearchResultData shopifySearchResult = new ShopifySearchResultData();
        if (customer != null && customer.getDefaultB2BUnit() != null) {
            final String accountNumber = WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(customer.getDefaultB2BUnit().getUid());
            shopifySearchResult = wholescriptsProductsFacade.searchShopifyProducts(accountNumber);
        }
        return shopifySearchResult;
    }

    private void updatePatientMostOrders(final DashboardResponseData dashboardResponseData) {
        final MostOrderedItemResponseData mostOrderedItemResponseData = dashboardResponseData.getTPaMostOrdered();
        if(null != mostOrderedItemResponseData){
            final List<MostOrderedItemData> mostOrderedItemDataList = mostOrderedItemResponseData.getItems();
            if(CollectionUtils.isNotEmpty(mostOrderedItemDataList)){
                for(final MostOrderedItemData mostOrderedItemData : mostOrderedItemDataList) {
                    try {

                        final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(configurationService.getConfiguration().getString("wholescripts.product.catalog"), configurationService.getConfiguration().getString("wholescripts.product.catalog.version"));
                        final ProductModel product = productService.getProductForCode(catalogVersion, mostOrderedItemData.getMatnr());
                        if (null != product) {
                            final String productUrl = getProductModelUrlResolver().resolve(product);
                            mostOrderedItemData.setProductURL(productUrl);
                        }
                    } catch (final Exception e) {
                        LOG.error("Product not found", e);
                    }
                }
            }
        }
    }

    private void updatePatientLatestOrders(final DashboardResponseData dashboardResponseData) {
        final LatestOrdersItemResponseData latestOrdersItemResponseData = dashboardResponseData.getTPaLatestOrders();
        if (null != latestOrdersItemResponseData) {
            final List<LatestOrdersItemData> latestOrdersItemDataList = latestOrdersItemResponseData.getItems();
            if (CollectionUtils.isNotEmpty(latestOrdersItemDataList)) {
                searchRestrictionService.disableSearchRestrictions();
                for (final LatestOrdersItemData latestOrdersItemData : latestOrdersItemDataList) {
                    final String b2bUnitId = WholescriptsCoreUtils.getAccountNumberWithSalesArea(latestOrdersItemData.getKunnr().replaceFirst("^0+", ""));
                    final B2BUnitModel unitModel = b2bUnitService.getUnitForUid(b2bUnitId);
                    final String customerId = getCustomerId(latestOrdersItemData.getKunnr());
                    latestOrdersItemData.setPatientId(customerId);
                    if (null != unitModel) {
                        latestOrdersItemData.setPatientName(unitModel.getName());
                    }
                }
                searchRestrictionService.enableSearchRestrictions();
            }
        }
    }

    private void updatePractionerMostOrders(final DashboardResponseData dashboardResponseData) {
        final MostOrderedItemResponseData mostOrderedItemResponseData = dashboardResponseData.getTPraMostOrdered();
        if(null != mostOrderedItemResponseData){
            final List<MostOrderedItemData> mostOrderedItemDataList = mostOrderedItemResponseData.getItems();
            if(CollectionUtils.isNotEmpty(mostOrderedItemDataList)){
                for(final MostOrderedItemData mostOrderedItemData : mostOrderedItemDataList){
                    try{
                        final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(
                                configurationService.getConfiguration().getString("wholescripts.product.catalog"),
                                configurationService.getConfiguration().getString("wholescripts.product.catalog.version"));
                        final ProductModel product = productService.getProductForCode(catalogVersion,mostOrderedItemData.getMatnr());
                        final String productUrl = getProductModelUrlResolver().resolve(product);
                        mostOrderedItemData.setProductURL(productUrl);

                        final List<PriceInformation> priceInformations = priceService.getPriceInformationsForProduct(product);
                        if(CollectionUtils.isNotEmpty(priceInformations))
                        {
                            final double price = priceInformations.get(0).getPriceValue().getValue();
                            final String priceInString = Double.toString(price);
                            mostOrderedItemData.setPrice(priceInString);
                        }

                    }catch(final Exception e){
                        LOG.error("Product not found",e);
                    }
                }
            }
        }
    }

    private void updatePractionerLatestOrders(final DashboardResponseData dashboardResponseData) {
        final LatestOrdersItemResponseData latestOrdersItemResponseData = dashboardResponseData.getTPraLatestOrders();
        if(null != latestOrdersItemResponseData){
            final List<LatestOrdersItemData> latestOrdersItemDataList = latestOrdersItemResponseData.getItems();
            if(CollectionUtils.isNotEmpty(latestOrdersItemDataList)){
                searchRestrictionService.disableSearchRestrictions();
                for(final LatestOrdersItemData latestOrdersItemData : latestOrdersItemDataList){
                    final String b2bUnitId = WholescriptsCoreUtils.getAccountNumberWithSalesArea(latestOrdersItemData.getKunnr().replaceFirst("^0+", ""));
                    final B2BUnitModel unitModel = b2bUnitService.getUnitForUid(b2bUnitId);
                    final String customerId = getCustomerId(latestOrdersItemData.getKunnr());
                    latestOrdersItemData.setPatientId(customerId);
                    if(null != unitModel){
                        latestOrdersItemData.setPatientName(unitModel.getName());
                    }
                }
                searchRestrictionService.enableSearchRestrictions();
            }
        }
    }

    private void updateSalesPersonDetails(final B2BCustomerModel b2BCustomerModel, final DashboardResponseData dashboardResponseData) {
        if (Objects.nonNull(b2BCustomerModel) && Objects.nonNull(b2BCustomerModel.getDefaultB2BUnit())) {
            searchRestrictionService.disableSearchRestrictions();
            if (StringUtils.isNotBlank((b2BCustomerModel.getDefaultB2BUnit().getSalesPerson()))) {
                final String salesPersonId = WholescriptsCoreUtils.getDefaultB2bUnit(b2BCustomerModel.getDefaultB2BUnit().getSalesPerson());
                final String b2bUnitId = WholescriptsCoreUtils.getAccountNumberWithSalesArea(salesPersonId.replaceFirst("^0+", ""));
                final B2BUnitModel unitModel = b2bUnitService.getUnitForUid(b2bUnitId);
                if (Objects.nonNull(unitModel)) {
                    final Set<B2BCustomerModel> customers = b2bUnitService.getB2BCustomers(unitModel);

                    final Optional<B2BCustomerModel> customer = customers.stream().findFirst();

                    if (customer.isPresent()) {
                        final B2BCustomerModel practitioner = customer.get();

                        final SalesPersonDetailsData salesPersonDetailsData = new SalesPersonDetailsData();
                        if(StringUtils.isNotBlank(unitModel.getName())){
                            salesPersonDetailsData.setName(unitModel.getName());
                        }

                        salesPersonDetailsData.setEmail(practitioner.getEmail());
                        dashboardResponseData.setSalesPersonDetails(salesPersonDetailsData);
                    }
                }
            }

            searchRestrictionService.enableSearchRestrictions();
        }
    }

    private void updateNewTopPatientsID(final DashboardResponseData dashboardResponseData) {

        if(dashboardResponseData.getTNewPatients() != null){
            final List<NewPatientItemData> newPatientItemDataList = dashboardResponseData.getTNewPatients().getItems();
            for(final NewPatientItemData newPatientItemData: newPatientItemDataList){
                final String customerId = getCustomerId(newPatientItemData.getKunnr());
                newPatientItemData.setPatientId(customerId);
            }
        }

        if(dashboardResponseData.getTTopPatients() != null){
            final List<TopPatientItemData> topPatientItemDataList = dashboardResponseData.getTTopPatients().getItems();
            for(final TopPatientItemData topPatientItemData: topPatientItemDataList){
                final String customerId = getCustomerId(topPatientItemData.getKunnr());
                if(StringUtils.isNotEmpty(customerId))
                {
                    final B2BCustomerModel customer=wholescriptsUserService.getUserByUid(customerId);
                    if(Objects.nonNull(customer) && StringUtils.isNotEmpty(customer.getName()))
                    {
                        topPatientItemData.setPatientName(customer.getName());
                    }
                }

                topPatientItemData.setPatientId(customerId);
            }
        }
    }

    private String getCustomerId(final String customerId) {

        final String salesAreaAcccountNo = WholescriptsCoreUtils.getAccountNumberWithSalesArea(customerId.replaceFirst("^0+", ""));
        searchRestrictionService.disableSearchRestrictions();
        final B2BUnitModel b2BUnitModel = b2bUnitService.getUnitForUid(salesAreaAcccountNo);

        if(null != b2BUnitModel){
            final Set<PrincipalModel> members = b2BUnitModel.getMembers();
            if(CollectionUtils.isNotEmpty(members)){
                for(final PrincipalModel member: members){
                    if(member instanceof CustomerModel){
                        final B2BCustomerModel customer = (B2BCustomerModel)member;
                        return customer.getUid();
                    }
                }
            }
        }
        searchRestrictionService.enableSearchRestrictions();
        return StringUtils.EMPTY;
    }

    private void createRecommendations(final B2BCustomerModel b2BCustomerModel, final DashboardResponseData dashboardResponseData) {
        final List<RecommendationModel> recommendationModelsList = wholescriptsRecommendService.getLatestRecommendationsByPractionerId(b2BCustomerModel.getUid());

        if(CollectionUtils.isNotEmpty(recommendationModelsList)){
            final List<LatestRecommendationsData> LatestRecommendationsDataList = new ArrayList<>();
            for(final RecommendationModel recommendationModel : recommendationModelsList){

                final LatestRecommendationsData latestRecommendationsData = new LatestRecommendationsData();
                if(null != recommendationModel.getPatient()){
                    latestRecommendationsData.setPatientName(recommendationModel.getPatient().getName());
                }
                latestRecommendationsData.setDate(recommendationModel.getModifiedtime().toString());
                if(Objects.nonNull(recommendationModel.getRecommendationStatus())){
                    final String recommendationStatus=enumerationService.getEnumerationName(recommendationModel.getRecommendationStatus());
                    latestRecommendationsData.setStatus(recommendationStatus);
                }
                latestRecommendationsData.setPatientId(recommendationModel.getPatient().getUid());
                latestRecommendationsData.setRecommendationCode(recommendationModel.getCode());

                LatestRecommendationsDataList.add(latestRecommendationsData);
            }
            dashboardResponseData.setRecommendations(LatestRecommendationsDataList);
        }
    }

    public UrlResolver<ProductModel> getProductModelUrlResolver() {
        return productModelUrlResolver;
    }

    public void setProductModelUrlResolver(final UrlResolver<ProductModel> productModelUrlResolver) {
        this.productModelUrlResolver = productModelUrlResolver;
    }


}