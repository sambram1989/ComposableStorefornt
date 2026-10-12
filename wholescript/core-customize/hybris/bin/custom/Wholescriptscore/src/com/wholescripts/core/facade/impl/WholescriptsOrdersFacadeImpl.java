package com.wholescripts.core.facade.impl;

import static de.hybris.platform.util.localization.Localization.getLocalizedString;

import com.wholescripts.core.dao.WholescriptsCoreDao;
import com.wholescripts.core.enums.CustomerProfileType;
import com.wholescripts.core.model.CustomerMaterialInformationModel;
import com.wholescripts.core.model.WholeScriptsBrightStockProductsModel;
import com.wholescripts.core.service.WholescriptsProductsService;
import de.hybris.platform.acceleratorfacades.payment.data.PaymentSubscriptionResultData;
import de.hybris.platform.acceleratorocc.dto.payment.SopPaymentDetailsWsDTO;
import de.hybris.platform.acceleratorservices.payment.data.PaymentSubscriptionResultItem;
import de.hybris.platform.b2b.enums.CheckoutPaymentType;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.b2bacceleratorfacades.api.cart.CartFacade;
import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.CartModificationDataList;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.StockData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.dao.CommerceCartDao;
import de.hybris.platform.commerceservices.strategies.GenerateMerchantTransactionCodeStrategy;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.PaymentAuthorizationException;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.ACHCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.payment.PaymentService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.stock.impl.StockLevelDao;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import com.wholescripts.core.constants.WholescriptscoreConstants;
import com.wholescripts.core.dao.WholescriptsCartValidationDao;
import com.wholescripts.core.exception.AgeRestrictionException;
import com.wholescripts.core.exception.POBoxAgeRestrictionException;
import com.wholescripts.core.exception.RegionExclusionException;
import com.wholescripts.core.exception.WholescriptsPageNotFoundException;
import com.wholescripts.core.facade.WholescriptsOrdersFacade;
import com.wholescripts.core.model.RegionProductSwapRestrictionModel;
import com.wholescripts.core.model.SupportedAutoShipModel;
import com.wholescripts.core.page.validators.WholeScriptsB2BPlaceOrderCartValidator;
import com.wholescripts.core.service.WholescriptsIntegrationsService;
import com.wholescripts.core.service.WholescriptsPopularCategoriesService;
import com.wholescripts.core.service.WholescriptsTokenizationService;
import com.wholescripts.core.utils.WholescriptsCoreUtils;
import com.wholescripts.data.AutoShipItemResponseData;
import com.wholescripts.data.AutoShipOrderResponseData;
import com.wholescripts.data.AutoShipResponseData;
import com.wholescripts.data.AutoShipsResponseHdr;
import com.wholescripts.data.AutoShipsResponseHdrItem;
import com.wholescripts.data.BillingDocumentsResponseData;
import com.wholescripts.data.CustomerActivityData;
import com.wholescripts.data.OrderAddressResponseData;
import com.wholescripts.data.OrderCreationRequest;
import com.wholescripts.data.OrderDetailsResponseData;
import com.wholescripts.data.OrderHDRBillingResponseData;
import com.wholescripts.data.OrderHDRDLVItemData;
import com.wholescripts.data.OrderHeaderResponseData;
import com.wholescripts.data.OrderHistoryListResponseData;
import com.wholescripts.data.OrderHistoryPractionerResponseoneData;
import com.wholescripts.data.OrderHistoryResponseData;
import com.wholescripts.data.OrderItemsReponseData;
import com.wholescripts.data.OrderListResponseoneData;
import com.wholescripts.data.OrderUpdateAutoShipResponseData;
import com.wholescripts.data.SupportedAutoShipData;
import com.wholescripts.data.WholescriptsGetCCCartItemListResponseData;
import com.wholescripts.data.WholescriptsGetCCCartItemResponseData;
import com.wholescripts.data.WholescriptsGetCCResponseData;
import com.wholescripts.dto.OrderChangeAutoShipRequestWsDTO;
import com.wholescripts.dto.WholescriptsValidateAutoshipWsDTO;

public class WholescriptsOrdersFacadeImpl implements WholescriptsOrdersFacade {
    private static final Logger LOG = Logger.getLogger(WholescriptsOrdersFacadeImpl.class);

    private static final List<String> PRIVATE_LABEL_CODES = Arrays.asList("1000014", "1001024");

    @Resource(name = "wholescriptsIntegrationsService")
    private WholescriptsIntegrationsService wholescriptsIntegrationsService;

    @Resource(name = "userFacade")
    protected UserFacade userFacade;

    @Resource(name = "b2bCartFacade")
    private CartFacade cartFacade;

    @Resource(name = "wholeScriptsB2BPlaceOrderCartValidator")
    private WholeScriptsB2BPlaceOrderCartValidator wholeScriptsB2BPlaceOrderCartValidator;

    @Resource(name = "b2bCheckoutFacade")
    private WholescriptsDefaultB2BCheckoutFacade  b2bCheckoutFacade;

    @Resource(name = "cartService")
    private CartService cartService;

    @Resource(name = "modelService")
    private ModelService modelService;

    @Resource(name = "addressConverter")
    private Converter<AddressModel, AddressData> addressConverter;

    @Resource(name="defaultGenerateMerchantTransactionCodeStrategy")
    private GenerateMerchantTransactionCodeStrategy generateMerchantTransactionCodeStrategy;

    @Resource(name="defaultPaymentService")
    private PaymentService paymentService;

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "b2bCustomerService")
    private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;

    @Resource(name = "wholescriptsTokenizationService")
    WholescriptsTokenizationService tokenizationService;

    @Resource(name = "defaultCommerceCartDao")
    CommerceCartDao cartDao;

    @Resource(name = "paymentSubscriptionResultDataConverter")
    private Converter<PaymentSubscriptionResultItem, PaymentSubscriptionResultData> paymentSubscriptionResultDataConverter;
    @Resource(name="stockConverter")
    private Converter<ProductModel, StockData> stockConverter;
    @Resource(name="imageConverter")
    private Converter<MediaModel, ImageData> imageConverter;
    @Resource(name = "productService")
    private ProductService productService;
    @Resource(name="productModelUrlResolver")
    private UrlResolver<ProductModel> productModelUrlResolver;
    @Resource(name="wsAutoShipFrequnceyConverter")
    private Converter<SupportedAutoShipModel, SupportedAutoShipData> frequencyConveter;

    @Resource(name = "wholescriptsPopularCategoriesService")
    private WholescriptsPopularCategoriesService wholescriptsPopularCategoriesService;

    @Resource(name = "wholescriptsCartValidationDao")
    private WholescriptsCartValidationDao wholescriptsCartValidationDao;

    @Resource(name = "b2bUnitService")
    private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;
    @Resource(name = "searchRestrictionService")
    private SearchRestrictionService searchRestrictionService;
    @Resource(name = "configurationService")
 	private ConfigurationService configurationService;

    @Resource(name = "stockService")
    private StockService stockService;

    @Resource(name="stockLevelDao")
    private StockLevelDao stockLevelDao;

    @Resource(name = "wholescriptsProductsService")
    WholescriptsProductsService wholescriptsProductsService;

    @Resource(name = "wholescriptsCoreDao")
    private WholescriptsCoreDao wholescriptsCoreDao;

    @Resource(name = "baseStoreService")
    private BaseStoreService baseStoreService;

    public OrderHistoryListResponseData getOrderHistoryListForPatient(final B2BCustomerModel b2bCustomer, final String startDate, final String endDate) throws Exception {
   	 OrderHistoryListResponseData response = wholescriptsIntegrationsService.getOrderHistoryListForPatient(b2bCustomer, startDate, endDate);
   	 populatePatientOrderHistoryStatus(response);
   	 return response;
    }

    public OrderDetailsResponseData getOrderDetails(B2BCustomerModel b2bCustomer, final String orderNumber, final BaseStoreModel baseStore) throws Exception {
        OrderDetailsResponseData responseData = wholescriptsIntegrationsService.getOrderDetails(b2bCustomer,orderNumber, baseStore);
        enrichOrderDetailsResponse(responseData,b2bCustomer);
        populateOrderStatus(responseData);
        return responseData;
    }

    private void enrichOrderDetailsResponse(OrderDetailsResponseData responseData,B2BCustomerModel b2bCustomer) throws WholescriptsPageNotFoundException {
        if (Objects.nonNull(responseData) && Objects.nonNull(responseData.getOrderItem()) &&
                CollectionUtils.isNotEmpty(responseData.getOrderItem().getItems())) {
      	  
      	  String soldTo = responseData.getOrderHeader().getItems().getSoldTo();
          String salesPerson = responseData.getOrderHeader().getItems().getSalesPerson();

          if(StringUtils.isNotEmpty(soldTo)) {
         	 StringBuilder accountNumber = new StringBuilder();
         	 accountNumber.append(soldTo.replaceFirst("^0+", ""))
         	 .append("_")
         	 .append("1710")
         	 .append("_")
         	 .append("10")
         	 .append("_")
         	 .append("10");         	
         	
         	 if(!accountNumber.toString().equals(b2bCustomer.getDefaultB2BUnit().getUid())) {
         		 searchRestrictionService.disableSearchRestrictions();
         		 B2BUnitModel soldtoUnit = b2bUnitService.getUnitForUid(accountNumber.toString());
         		 if(Objects.nonNull(soldtoUnit) && !WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(b2bCustomer.getDefaultB2BUnit().getUid()).equals(soldtoUnit.getSalesPerson())) {
         			 throw new WholescriptsPageNotFoundException("order not found");
         		 } 
         	 }
         	 else {
         		 if( !(Objects.equals(soldTo, WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(b2bCustomer.getDefaultB2BUnit().getUid())) ||
         						 Objects.equals(salesPerson, b2bCustomer.getDefaultB2BUnit().getSalesPerson()))) {
         			 throw new WholescriptsPageNotFoundException("order not found");
         		 }
         	 }

          }
         
      	  // removed promo line items from list 
      	  List<OrderItemsReponseData> filteredPromoItems=  responseData.getOrderItem().getItems().stream().filter(i -> "000999".equals(i.getSalesItem())).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(filteredPromoItems)) {
            	responseData.getOrderItem().getItems().removeAll(filteredPromoItems);
            }
      	  String shipTo = responseData.getOrderHeader().getItems().getShipTo();
            List<OrderAddressResponseData> addressResponseData = responseData.getOrderAddress().getItems();
            // Find the region by matching shipTo with the partner attribute
            String regionCode = extractRegionFromAddressResponseData(shipTo, addressResponseData);
            for (OrderItemsReponseData item : responseData.getOrderItem().getItems()) {
                if (Objects.nonNull(item) && Objects.nonNull(item.getMaterial())) {
                    try {
                        ProductModel product = null;
                        try{
                            product = productService.getProductForCode(item.getMaterial());
                        } catch (Exception e) {
                            LOG.error("Failed to fetch product for material: " + item.getMaterial(), e);
                        }

                        if (Objects.nonNull(product)) {
                            RegionProductSwapRestrictionModel swapRestriction = checkProductInSwapTable(product,regionCode);
                            if (Objects.nonNull(swapRestriction) && Objects.nonNull(swapRestriction.getSwapProductCode())) {
                                ProductModel swappedProduct = null;
                                try{
                                    swappedProduct = productService.getProductForCode(swapRestriction.getSwapProductCode());
                                } catch (Exception e) {
                                    LOG.error("Failed to fetch swapped product for code: " + swapRestriction.getSwapProductCode(), e);
                                }
                                if (Objects.nonNull(swappedProduct)) {
                                    product = swappedProduct;
                                } else {
                                    LOG.warn("Swapped product missing for code: " + swapRestriction.getSwapProductCode());
                                }
                            }
                            item.setStock(stockStatusForEntries(product));
                            if(product.getIsMedpax())
                            {
                                item.setIsMedPax(true);
                            }
                           else if(wholescriptsProductsService.isPrivateLabel(product))
                            {
                                item.setIsPrivateLabel(true);
                            }
                           else if(wholescriptsProductsService.isBlueLabel(product)){
                                item.setIsBlueLabel(true);
                            }
                           else if(wholescriptsProductsService.isCarton(product)) {
                                item.setIsCarton(true);
                            }

                            if (Objects.nonNull(product.getPicture())) {
                                item.setImage(imageConverter.convert(product.getPicture()));
                            }
                            if (product.getSapMaterialGroup() == null || !PRIVATE_LABEL_CODES.contains(product.getSapMaterialGroup().getCode())) {
                                item.setProductUrl(productModelUrlResolver.resolve(product));
                            }
                            if (Objects.nonNull(product.getName())) {
                                item.setProductName(product.getName());
                            }
                        } else {
                            LOG.warn("No product found for material in Catalog: " + item.getMaterial());
                            StockData stock=new StockData();
                            stock.setStockLevelStatus(StockLevelStatus.OUTOFSTOCK);
                            stock.setForceOutOfStock(true);
                            item.setStock(stock);
                            item.setIsProductUnavailable(true);
                     /*       final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
                            if (Objects.nonNull(customer) && Objects.nonNull(customer.getDefaultB2BUnit())) {
                                final B2BUnitModel unit = customer.getDefaultB2BUnit();
                                String accountNumber =WholescriptsCoreUtils.getFormattedDefaultB2bUnit(unit.getUid());
                                boolean defaultCarton= configurationService.getConfiguration().getString("medpax.product.carton").equals(item.getMaterial());

                                if (StringUtils.isNotEmpty(accountNumber))
                                {
                                    CustomerMaterialInformationModel cmInfoModel=wholescriptsProductsService.getCustomerMaterialInfoByBlueLabel( item.getMaterial(),accountNumber);
                                    if(Objects.nonNull(cmInfoModel))
                                    {
                                        final BaseStoreModel baseStore = baseStoreService.getCurrentBaseStore();
                                        StockData stockData=new StockData();
                                        if (baseStore != null &&  baseStore.getWarehouses() != null && !baseStore.getWarehouses().isEmpty()){
                                            StockLevelModel stockLevel =stockLevelDao.findStockLevel(item.getMaterial(),baseStore.getWarehouses().get(0));
                                            StockLevelStatus stockLevelStatus= WholescriptsCoreUtils.checkStatus(stockLevel);
                                            stockData.setStockLevelStatus(stockLevelStatus);
                                            if(Objects.nonNull(stockLevel) && Objects.nonNull(stockLevel.getInStockStatus()))
                                            {
                                                boolean isForceOutOfStockLevel =  InStockStatus.FORCEOUTOFSTOCK.equals(stockLevel.getInStockStatus());

                                                stockData.setForceOutOfStock(isForceOutOfStockLevel);
                                            }
                                            item.setStock(stockData);
                                        }
                                        if(!defaultCarton)
                                        {
                                            item.setIsPrivateLabelCarton(true);

                                        }

                                    }
                                    else {
                                        if(!defaultCarton)
                                        {
                                            StockData stockData=new StockData();
                                            stockData.setStockLevelStatus(StockLevelStatus.OUTOFSTOCK);
                                            item.setStock(stockData);
                                            item.setIsPrivateLabelCarton(true);
                                        }


                                    }
                                }

                            }*/
                        }
                    } catch (Exception e) {
                        LOG.error("Failed to enrich order details for material: " + item.getMaterial(), e);
                    }
                } else {
                    LOG.warn("Skipping item due to missing material code.");
                }
            }
        } else {
            LOG.warn("Response data or order items are missing.");
        }
    }

    private StockData stockStatusForEntries(ProductModel productModel) {
        final BaseStoreModel baseStore = baseStoreService.getCurrentBaseStore();
        StockData stockData=new StockData();
        if (baseStore != null &&  baseStore.getWarehouses() != null && !baseStore.getWarehouses().isEmpty()){
            StockLevelModel stockLevel =stockService.getStockLevel(productModel, baseStore.getWarehouses().get(0));
            boolean isPrivateLabel=wholescriptsProductsService.isPrivateLabel(productModel);
            boolean isPrivateLabelMedpax=wholescriptsProductsService.isPrivateLabelMedpax(productModel);
            boolean isDefaultCarton=wholescriptsProductsService.isDefaultCarton(productModel);
            if(isPrivateLabel)
            {
                final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
                if (Objects.nonNull(customer) && Objects.nonNull(customer.getDefaultB2BUnit())) {
                    final B2BUnitModel unit = customer.getDefaultB2BUnit();
                    String accountNumber =WholescriptsCoreUtils.getFormattedDefaultB2bUnit(unit.getUid());
                    if (StringUtils.isNotEmpty(accountNumber))
                    {
                        CustomerMaterialInformationModel cmInfoModel=wholescriptsProductsService.getCustomerMaterialInfo( productModel.getCode().replaceFirst("^0+", ""),accountNumber);
                        if(Objects.nonNull(cmInfoModel))
                        {
                            StockLevelStatus stockLevelStatus= checkPrivateLabelStockStatus(stockLevel,cmInfoModel);
                            stockData.setStockLevelStatus(stockLevelStatus);
                        }
                    }

                }

            }
            else if(isPrivateLabelMedpax || isDefaultCarton)
            {
                stockData.setStockLevelStatus(StockLevelStatus.INSTOCK);;
            }
            else
            {
                StockLevelStatus status= WholescriptsCoreUtils.checkStatus(stockLevel);
                stockData.setStockLevelStatus(status);
            }

            if(Objects.nonNull(stockLevel) && Objects.nonNull(stockLevel.getInStockStatus()))
            {
                boolean isForceOutOfStockLevel =  InStockStatus.FORCEOUTOFSTOCK.equals(stockLevel.getInStockStatus());

                stockData.setForceOutOfStock(isForceOutOfStockLevel);
            }

        }

        return stockData;
    }
    protected  StockLevelStatus checkPrivateLabelStockStatus(StockLevelModel stockLevel,CustomerMaterialInformationModel customerMaterial) {
        StockLevelStatus resultStatus = StockLevelStatus.OUTOFSTOCK;
        if(Objects.isNull(stockLevel)) {
            if(Objects.nonNull(customerMaterial.getBrightStock())) {
                String brightStockCode = String.format("%018d", Integer.valueOf(customerMaterial.getBrightStock()));
                WholeScriptsBrightStockProductsModel brightStockModel = wholescriptsCoreDao.getBrightStockForPLProduct(brightStockCode);
                if (Objects.nonNull(brightStockModel)) {

                    if(brightStockModel.getOutOfStock())
                    {
                        //already out of stock by default
                    }
                    else {
                        resultStatus = StockLevelStatus.INSTOCK;
                    }
                }
            }
            return resultStatus;
        } else {
            if(Objects.nonNull(stockLevel.getInStockStatus()) && !InStockStatus.NOTSPECIFIED.equals(stockLevel.getInStockStatus()))
            {
                boolean isInStockStatusLevel =  InStockStatus.FORCEINSTOCK.equals(stockLevel.getInStockStatus());
                if (isInStockStatusLevel) {
                    resultStatus = StockLevelStatus.INSTOCK;
                }
            }

            else {
                if(stockLevel.getAvailable() < stockLevel.getReserved())
                {
                    if(Objects.nonNull(customerMaterial.getBrightStock()))
                    {
                        String brightStockCode=String.format("%018d",Integer.valueOf(customerMaterial.getBrightStock()));
                        WholeScriptsBrightStockProductsModel brightStockModel=wholescriptsCoreDao.getBrightStockForPLProduct(brightStockCode);
                        if(Objects.nonNull(brightStockModel))
                        {
                            if(brightStockModel.getOutOfStock())
                            {
                                //already out of stock by default
                            }
                            else {
                                resultStatus = StockLevelStatus.INSTOCK;
                            }

                        }

                    }

                }
                else {
                    resultStatus = StockLevelStatus.INSTOCK;
                }

            }

            return resultStatus;
        }

    }

    private RegionProductSwapRestrictionModel checkProductInSwapTable(ProductModel product,String regionCode) {
        if (Objects.nonNull(product) && Objects.nonNull(regionCode)) {
            try {
                return wholescriptsCartValidationDao.updateProductFromSwapRestriction(product.getCode(), regionCode);
            } catch (Exception e) {
                LOG.error("Error occurred while checking swap restriction for product code: " + product, e);
            }
        }
        return null;
    }

    private String extractRegionFromAddressResponseData(String shipTo, List<OrderAddressResponseData> addressResponseData) {
        if (StringUtils.isNotBlank(shipTo) && CollectionUtils.isNotEmpty(addressResponseData)) {
            for (OrderAddressResponseData address : addressResponseData) {
                if (Objects.nonNull(address.getPartner()) && shipTo.equals(address.getPartner())) {
                    return address.getRegion();
                }
            }
        }
        return null; // Return null if no matching address is found
    }


    public OrderHistoryPractionerResponseoneData getOrderHistoryListForPractitioner(final B2BCustomerModel b2bCustomer, final String startDate, final String endDate) throws Exception{
   	 OrderHistoryPractionerResponseoneData response =  wholescriptsIntegrationsService.getOrderHistoryListForPractitioner(b2bCustomer, startDate, endDate);
        hideOldOrdersFromResponse(response);
   	 populatePractitionerOrderHistoryStatus(response);
   	 return response;
    }

    private void hideOldOrdersFromResponse(OrderHistoryPractionerResponseoneData response) {
        if (Objects.nonNull(response.getOrder_list()))
        {
            if (CollectionUtils.isNotEmpty(response.getOrder_list().getItems())) {
                    response.getOrder_list().getItems().removeIf(order ->
                            order.getSalesOrder() != null && order.getSalesOrder().startsWith("H")
                    );

            }

        }

    }

    @Override
    public OrderHistoryPractionerResponseoneData getOrderHistoryListByPatientID(B2BCustomerModel b2bCustomer, String startDate, String endDate) throws Exception {
   	  final B2BCustomerModel currentCustomer = b2bCustomerService.getCurrentB2BCustomer();
   	  if (CustomerProfileType.PRACTITIONER.getCode().equalsIgnoreCase(currentCustomer.getCustomerProfile().getCode()) 
   			  && CustomerProfileType.PATIENT.getCode().equalsIgnoreCase(b2bCustomer.getCustomerProfile().getCode())) {
   		  
   		  final String currentB2BUnitId = currentCustomer.getDefaultB2BUnit().getUid();
   		  final String salesPersonId = WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnit(currentB2BUnitId);
   		  
			  if (salesPersonId.equalsIgnoreCase(b2bCustomer.getDefaultB2BUnit().getSalesPerson())) {
				  OrderHistoryPractionerResponseoneData response = wholescriptsIntegrationsService
						  .getOrderHistoryListByPatientID(b2bCustomer, startDate, endDate);
				  hideOldOrdersFromResponse(response);
				  populatePractitionerOrderHistoryStatus(response);
				  return response;
			  }
			  else {
				  throw new WholescriptsPageNotFoundException("Patient id is not valid");
			  }
   		  
   	  } else {
   		  throw new WholescriptsPageNotFoundException("order not found");
   	  }
    }


    public void updateTermsInCart(final boolean termsChecked)
    {
        if (!termsChecked)
        {
            throw new RequestParameterException(getLocalizedString("cart.term.unchecked"));
        }
        final CartModel cart = cartService.getSessionCart();
        if(Objects.nonNull(cart))
        {
            cart.setSalesPolicyTermsAgreed(true);
            modelService.save(cart);
        }
    }

    public void validateUser()
    {
        if (userFacade.isAnonymousUser())
        {
            throw new AccessDeniedException("Access is denied");
        }
    }

    @Override
    public void validateCart(CartData cartData) throws InvalidCartException {
        final Errors errors = new BeanPropertyBindingResult(cartData, "sessionCart");
        wholeScriptsB2BPlaceOrderCartValidator.validate(cartData, errors);
        if (errors.hasErrors())
        {
            throw new WebserviceValidationException(errors);
        }

        try
        {
            final List<CartModificationData> modificationList = cartFacade.validateCurrentCartData();
            if(CollectionUtils.isNotEmpty(modificationList))
            {
                final CartModificationDataList cartModificationDataList = new CartModificationDataList();
                cartModificationDataList.setCartModificationList(modificationList);
                throw new WebserviceValidationException(cartModificationDataList);
            }
        }
        catch (final CommerceCartModificationException e)
        {
            throw new InvalidCartException(e);
        }
    }

    @Override
    public void validateAndAuthorizePayment(final CartData cartData, final OrderCreationRequest orderCreationRequest, final CustomerActivityData customerActivityData) throws PaymentAuthorizationException {
        if (CheckoutPaymentType.XCRD.getCode().equals(cartData.getPaymentType().getCode()) && !b2bCheckoutFacade.authorizePayment(orderCreationRequest, customerActivityData))
        {
            throw new PaymentAuthorizationException();
        }
    }

    @Override
    public void authorizeExternalOrderPayment(final CartModel cart, final PaymentInfoModel paymentInfo, final OrderCreationRequest orderCreationRequest) throws PaymentAuthorizationException {
        if (CheckoutPaymentType.XCRD.getCode().equals(cart.getPaymentType().getCode()) 
      		  && !b2bCheckoutFacade.authorizeExternalOrderPayment(cart, paymentInfo, orderCreationRequest)) {
            throw new PaymentAuthorizationException();
        }
    }

    @Override
    public WholescriptsGetCCResponseData getAddressFromSapByPayer(WholescriptsGetCCResponseData wholescriptsGetCCResponseData, B2BCustomerModel currentB2BCustomer) {
        try {
            if (Objects.isNull(wholescriptsGetCCResponseData) || Objects.isNull(wholescriptsGetCCResponseData.getCartData())) {
                return wholescriptsGetCCResponseData;
            }
            WholescriptsGetCCCartItemListResponseData listResponseData = wholescriptsGetCCResponseData.getCartData();
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
                            if (Objects.nonNull(payerUnitModel) && CollectionUtils.isNotEmpty(payerUnitModel.getAddresses()))
                            {

                                Collection<AddressModel> addressModels = payerUnitModel.getAddresses().stream().filter(bA -> Boolean.TRUE.equals(bA.getBillingAddress())).collect(Collectors.toList());
                                Optional<AddressModel> billingAddress = addressModels.stream().findFirst();
                                // Set billing address if found
                                billingAddress.ifPresent(addressModel -> itemData.setBillingAddress(getAddressData(addressModel)));
                               
                            }
                        }


                    } catch (Exception e) {
                        LOG.error("Error processing billing address for item: {}", e);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Error in getAddressFromSapByPayer method", e);
        }
        return wholescriptsGetCCResponseData;
    }



    private AddressData getAddressData(AddressModel addressModel) {
        return  addressConverter.convert(addressModel);    }

    @Override
    public AutoShipOrderResponseData getAutoShipOrders(String orderNumber) throws Exception {
        AutoShipOrderResponseData responseData = wholescriptsIntegrationsService.getAutoShipOrders(orderNumber);
        AutoShipResponseData autoShipsResponse = responseData.getAutoShips();
        if (Objects.nonNull(autoShipsResponse) && CollectionUtils.isNotEmpty(autoShipsResponse.getItems())) {
            for (AutoShipItemResponseData item : autoShipsResponse.getItems()) {
                try {
                    String shipTo = item.getShipTo();
                    String regionCode = null;
                    String salesAreaAccountNo = WholescriptsCoreUtils.getAccountNumberWithSalesArea(shipTo);
                    // Fetch the B2BUnit by shipTo ID
                    final B2BUnitModel b2bUnit = b2bUnitService.getUnitForUid(salesAreaAccountNo);
                    if (Objects.nonNull(b2bUnit)) {
                        // Fetch the shipping address for the B2BUnit
                        AddressModel shippingAddress = b2bUnit.getShippingAddress();
                        if (Objects.nonNull(shippingAddress) && Objects.nonNull(shippingAddress.getRegion())) {
                            // Extract the region code for the shipTo address
                            regionCode = shippingAddress.getRegion().getIsocodeShort();
                        }
                    }

                    ProductModel product = productService.getProductForCode(item.getMaterial());
                    if (Objects.nonNull(product)) {
                        // Check if the product has a swap restriction for the region
                        RegionProductSwapRestrictionModel swapRestriction = checkProductInSwapTable(product, regionCode);
                        if (Objects.nonNull(swapRestriction)) {
                            // Fetch the swapped product
                            ProductModel swappedProduct = productService.getProductForCode(swapRestriction.getSwapProductCode());
                            if (Objects.nonNull(swappedProduct)) {
                                product = swappedProduct; // Use the swapped product
                            }
                        }
                        item.setStock(stockConverter.convert(product));
                        if (product.getPicture() != null) {
                            item.setImage(imageConverter.convert(product.getPicture()));
                        }
                        if (CollectionUtils.isNotEmpty(product.getSupportedAutoShip())) {
                            item.setAutoShipFrequency(frequencyConveter.convertAll(product.getSupportedAutoShip()));
                        }
                        if (Objects.nonNull(product.getName())) {
                            item.setProductName(product.getName());
                        }
                        item.setIsCarton(wholescriptsProductsService.isCarton(product));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        if (Objects.nonNull(responseData.getAutoShipsHdr())) {
            updateShippingAddressForAutoShipOrders(responseData.getAutoShipsHdr());

        }
        return responseData;
    }

    private void updateShippingAddressForAutoShipOrders(AutoShipsResponseHdr headerResponse) {
            for (AutoShipsResponseHdrItem item : headerResponse.getItem()) {
                B2BUnitModel unitModel = null;
                String shipTo = item.getShipTo();
                String salesAreaAccountNo = WholescriptsCoreUtils.getAccountNumberWithSalesArea(shipTo.replaceFirst("^0+", ""));
                searchRestrictionService.disableSearchRestrictions();
                unitModel = b2bUnitService.getUnitForUid(salesAreaAccountNo);
                if (unitModel == null ) {
                    salesAreaAccountNo = WholescriptsCoreUtils.getAccountNumberWithSalesArea(shipTo);
                    unitModel = b2bUnitService.getUnitForUid(salesAreaAccountNo);
                    searchRestrictionService.enableSearchRestrictions();
                }
                if (Objects.nonNull(unitModel))
                {
                    if(unitModel.getActive())
                    {
                        if(CollectionUtils.isNotEmpty(unitModel.getShippingAddresses()))
                        {
                            Optional<AddressModel> shippingAddress = unitModel.getShippingAddresses().stream().findFirst();
                            AddressModel addressModel;
                            if (shippingAddress.isPresent()) {
                                addressModel = shippingAddress.get();
                                item.setShippingAddress(getAddressData(addressModel));

                            }
                        }
                    }
                    else {
                        item.setHideShipTo(true);
                    }

                }

            }

    }

    @Override
    public PaymentSubscriptionResultData completeSopCreateSubscription(Map<String, String> parameterMap, SopPaymentDetailsWsDTO sopPaymentDetails, String cartId) throws Exception {
        final B2BCustomerModel customerModel = b2bCustomerService.getCurrentB2BCustomer();

        final PaymentSubscriptionResultItem paymentSubscriptionResultItem = tokenizationService
                .completeSopCreatePaymentSubscription(customerModel, sopPaymentDetails, parameterMap);

        if (paymentSubscriptionResultItem != null)
        {
            if(StringUtils.isNotBlank(cartId))
            {
                updateCartForPayment(paymentSubscriptionResultItem,customerModel,cartId);
            }

            return paymentSubscriptionResultDataConverter.convert(paymentSubscriptionResultItem);
        }

        return null;
    }

    private void updateCartForPayment(PaymentSubscriptionResultItem paymentSubscriptionResultItem, B2BCustomerModel customerModel, String cartId) {
        final CartModel cart = cartDao.getCartForCodeAndUser(cartId,customerModel);

        cart.setPaymentType(CheckoutPaymentType.XCRD);
        if(Objects.nonNull(paymentSubscriptionResultItem.getStoredCard()))
        {
            cart.setPaymentInfo(paymentSubscriptionResultItem.getStoredCard());
            if(Objects.nonNull(paymentSubscriptionResultItem.getStoredCard().getBillingAddress()))
            {
                AddressModel billingAddress=paymentSubscriptionResultItem.getStoredCard().getBillingAddress();
                cart.setPaymentAddress(billingAddress);
            }

        }

        modelService.save(cart);
    }

    @Override
    public OrderUpdateAutoShipResponseData updateAutoShipOrder(OrderChangeAutoShipRequestWsDTO orderChangeAutoShipRequestWsDTO) throws Exception {
        OrderUpdateAutoShipResponseData responseData= wholescriptsIntegrationsService.updateAutoShipOrder(orderChangeAutoShipRequestWsDTO);
        if(Objects.nonNull(responseData) && Objects.nonNull(responseData.getAutoShipsHdr()))
        {
            updateShippingAddressForAutoShipOrders(responseData.getAutoShipsHdr());

        }
        return responseData;
    }

    @Override
    public void cancelOrderInSAP(String orderId, String orderEntryNumber) throws Exception {
        wholescriptsIntegrationsService.cancelOrderInSAP(orderId, orderEntryNumber);
    }
    @Override
    public void updateProductRankingFromOrders(OrderModel order) {

        wholescriptsPopularCategoriesService.updateProductRankingFromOrders(order);

    }
    @Override
    public OrderHistoryResponseData getOrderHistoryWebErrors() throws Exception{
        return wholescriptsIntegrationsService.getOrderHistoryWebErrors();
    }

    @Override
    public boolean hasAutoShip() throws Exception {
        AutoShipOrderResponseData responseData = wholescriptsIntegrationsService.getAutoShipOrdersForCurrentUser();
        return Objects.nonNull(responseData.getAutoShips()) &&
                CollectionUtils.isNotEmpty(responseData.getAutoShips().getItems());
    }

    private void populateOrderStatus(final OrderDetailsResponseData responseData) {
   	 
		 if (Objects.nonNull(responseData.getOrderHeader()) && Objects.nonNull(responseData.getOrderHeader().getItems())) {
			 
			 final OrderHeaderResponseData  orderHeader = responseData.getOrderHeader().getItems();
			 Double totalCreditedAmount = 0.0;
			 
			 if (Objects.nonNull(responseData.getOrderHdrBil())) {
				 final List<OrderHDRBillingResponseData>  orderHdrBillItems = responseData.getOrderHdrBil().getItems();
				 if(CollectionUtils.isNotEmpty(orderHdrBillItems)) {
					 for (OrderHDRBillingResponseData orderHdrBillItem : orderHdrBillItems) {
						 if(WholescriptscoreConstants.ORDER_HEADER_BIL_DOC_O.equalsIgnoreCase(orderHdrBillItem.getDoctype())) {
							 final String hdrBillItemTotal = orderHdrBillItem.getTotal();
							 if (StringUtils.isNotBlank(hdrBillItemTotal)) {
								 totalCreditedAmount = totalCreditedAmount + Double.valueOf(hdrBillItemTotal);
							 }
						 }
					 }
					 
				 }
				 
			 }

			 final OrderHDRDLVItemData orderHeaderDelivery =  responseData.getOrderHdrDlv();
			 
			  boolean partialshipped = false;
			  boolean shipped = false;

			  if (Objects.nonNull(responseData.getOrderItem()) 
					  && Objects.nonNull(responseData.getOrderItem().getItems())) {

				  List<OrderItemsReponseData> orderItems = responseData.getOrderItem().getItems();

				  boolean partialOrderShipped = orderItems.stream().anyMatch(
						  orderItem -> !WholescriptscoreConstants.ORDER_HEADER_DLV_STATUS_C.equalsIgnoreCase(orderItem.getDlvStatus()));
				  
				  boolean completeOrderShipped = orderItems.stream().allMatch(
						  orderItem -> WholescriptscoreConstants.ORDER_HEADER_DLV_STATUS_C.equalsIgnoreCase(orderItem.getDlvStatus()));

				  if (partialOrderShipped) {
					  partialshipped = true;
				  }
				  
				  if (completeOrderShipped) {
					  shipped = true;
				  }

			  }
			 
			 
			 
			 if (StringUtils.isNotBlank(orderHeader.getTotal()) && totalCreditedAmount > 0.0) {
				 final Double headerTotal = Double.valueOf(orderHeader.getTotal());
				 if (Double.compare(headerTotal, totalCreditedAmount) == 0) {
					 orderHeader.setOrderStatus((WholescriptscoreConstants.ORDER_STATUS_REFUNDED));
				 } else {
					 orderHeader.setOrderStatus(WholescriptscoreConstants.ORDER_STATUS_PARTIALLY_REFUNDED);
				 }
				 
			 } else if (WholescriptscoreConstants.ORDER_HEADER_BILL_STATUS_C.equalsIgnoreCase(orderHeader.getBilStatus())) {
				 orderHeader.setOrderStatus(WholescriptscoreConstants.ORDER_STATUS_COMPLETE);
			 } else if (WholescriptscoreConstants.ORDER_HEADER_CANCELLED_B.equalsIgnoreCase(orderHeader.getCanceled())) {
				 orderHeader.setOrderStatus(WholescriptscoreConstants.ORDER_STATUS_PARTIALLY_CANCELLED);
			 } else if (WholescriptscoreConstants.ORDER_HEADER_CANCELLED_C.equalsIgnoreCase(orderHeader.getCanceled())
					 || WholescriptscoreConstants.ORDER_HEADER_CANCELLED_X.equalsIgnoreCase(orderHeader.getCanceled())) {
				 orderHeader.setOrderStatus(WholescriptscoreConstants.ORDER_STATUS_CANCELLED);
			 } else if (shipped && orderHeaderDelivery != null) {
				 orderHeader.setOrderStatus(WholescriptscoreConstants.ORDER_STATUS_SHIPPED);
			 } else if (partialshipped && orderHeaderDelivery != null) {
				 orderHeader.setOrderStatus(WholescriptscoreConstants.ORDER_STATUS_PARTIALLY_SHIPPED);
			 } else {
				 orderHeader.setOrderStatus(WholescriptscoreConstants.ORDER_STATUS_PROCESSING);
			 }

		 }
   	 
    }

    private void populatePractitionerOrderHistoryStatus(final OrderHistoryPractionerResponseoneData response) {
        if (response != null && response.getOrder_list() != null && CollectionUtils.isNotEmpty(response.getOrder_list().getItems())) {
            final List<OrderListResponseoneData> orderList = response.getOrder_list().getItems();
            for (OrderListResponseoneData order : orderList) {
                Double totalCreditedAmount = 0.0;
                if (Objects.nonNull(order.getBillDocs()) && CollectionUtils.isNotEmpty(order.getBillDocs().getItems())) {
                    List<BillingDocumentsResponseData> orderBillDocuments = order.getBillDocs().getItems();
                    for (BillingDocumentsResponseData orderBillDocs : orderBillDocuments) {
                        if (WholescriptscoreConstants.ORDER_HEADER_BIL_DOC_O.equalsIgnoreCase(orderBillDocs.getDoctype())) {
                            final Double orderDocValue = orderBillDocs.getDocval();
                            if (orderDocValue != null) {
                                totalCreditedAmount += orderDocValue;
                            }
                        }
                    }
                }

                if (totalCreditedAmount > 0.0) {
                    final Double headerTotal = Double.valueOf(order.getTotal());
                    if (Double.compare(headerTotal, totalCreditedAmount) == 0) {
                        order.setOrderStatus(WholescriptscoreConstants.ORDER_STATUS_REFUNDED);
                    } else {
                        order.setOrderStatus(WholescriptscoreConstants.ORDER_STATUS_PARTIALLY_REFUNDED);
                    }

                } else if (WholescriptscoreConstants.ORDER_HEADER_BILL_STATUS_C.equalsIgnoreCase(order.getBilStatus())) {
                    order.setOrderStatus(WholescriptscoreConstants.ORDER_STATUS_COMPLETE);
                } else if (WholescriptscoreConstants.ORDER_HEADER_CANCELLED_P.equalsIgnoreCase(order.getCanceled())) {
                    order.setOrderStatus(WholescriptscoreConstants.ORDER_STATUS_PARTIALLY_CANCELLED);
                } else if (WholescriptscoreConstants.ORDER_HEADER_CANCELLED_C.equalsIgnoreCase(order.getCanceled())
                        || WholescriptscoreConstants.ORDER_HEADER_CANCELLED_X.equalsIgnoreCase(order.getCanceled())) {
                    order.setOrderStatus(WholescriptscoreConstants.ORDER_STATUS_CANCELLED);
                } else if (WholescriptscoreConstants.ORDER_HEADER_DLV_STATUS_C.equalsIgnoreCase(order.getDlvStatus())) {
                    order.setOrderStatus(WholescriptscoreConstants.ORDER_STATUS_SHIPPED);
                } else if (WholescriptscoreConstants.ORDER_HEADER_DLV_STATUS_P.equalsIgnoreCase(order.getDlvStatus())) {
                    order.setOrderStatus(WholescriptscoreConstants.ORDER_STATUS_PARTIALLY_SHIPPED);
                } else {
                    order.setOrderStatus(WholescriptscoreConstants.ORDER_STATUS_PROCESSING);
                }
            }
        } else {
            LOG.warn("Order list or items are null/empty in practitioner order history response");
        }
    }



    private void populatePatientOrderHistoryStatus(final OrderHistoryListResponseData response) {

		 final List<OrderHeaderResponseData> orderList = response.getOrderHeader().getItems();

		 final List<OrderHDRBillingResponseData> orderHdrBillList = response.getOrderHdrBil().getItems();

		 if (CollectionUtils.isNotEmpty(orderList)) {
			 
			 for (OrderHeaderResponseData order : orderList) {

				 Double totalCreditedAmount = 0.0;

				 final Optional<OrderHDRBillingResponseData> orderHdrBillOptional = orderHdrBillList.stream()
						 .filter(orderHdrBill -> orderHdrBill.getDocNumber().equalsIgnoreCase(order.getSalesOrder())).findAny();
				 
				 if (orderHdrBillOptional.isPresent()) {

					 OrderHDRBillingResponseData orderHdrBill = orderHdrBillOptional.get();

					 if (WholescriptscoreConstants.ORDER_HEADER_BIL_DOC_O.equalsIgnoreCase(orderHdrBill.getDoctype()))
					 {
						 final String orderHdrTotal = orderHdrBill.getTotal();
						 if (StringUtils.isNotEmpty(orderHdrTotal))
						 {
							 totalCreditedAmount = totalCreditedAmount + Double.valueOf(orderHdrTotal);
						 }
					 }

				 }

				 if (totalCreditedAmount > 0.0) {
					 final Double headerTotal = Double.valueOf(order.getTotal());
					 
					 if (Double.compare(headerTotal, totalCreditedAmount) == 0) {
						 order.setOrderStatus((WholescriptscoreConstants.ORDER_STATUS_REFUNDED));
					 }
					 else {
						 order.setOrderStatus(WholescriptscoreConstants.ORDER_STATUS_PARTIALLY_REFUNDED);
					 }

				 }
				 else if (WholescriptscoreConstants.ORDER_HEADER_BILL_STATUS_C.equalsIgnoreCase(order.getBilStatus())) {
					 order.setOrderStatus(WholescriptscoreConstants.ORDER_STATUS_COMPLETE);
				 }
				 else if (WholescriptscoreConstants.ORDER_HEADER_CANCELLED_P.equalsIgnoreCase(order.getCanceled())) {
					 order.setOrderStatus(WholescriptscoreConstants.ORDER_STATUS_PARTIALLY_CANCELLED);
				 }
				 else if (WholescriptscoreConstants.ORDER_HEADER_CANCELLED_C.equalsIgnoreCase(order.getCanceled())
						 || WholescriptscoreConstants.ORDER_HEADER_CANCELLED_X.equalsIgnoreCase(order.getCanceled())) {
					 order.setOrderStatus(WholescriptscoreConstants.ORDER_STATUS_CANCELLED);
				 }
				 else if (WholescriptscoreConstants.ORDER_HEADER_DLV_STATUS_C.equalsIgnoreCase(order.getDlvStatus())) {
					 order.setOrderStatus(WholescriptscoreConstants.ORDER_STATUS_SHIPPED);
				 }
				 else if (WholescriptscoreConstants.ORDER_HEADER_DLV_STATUS_P.equalsIgnoreCase(order.getDlvStatus())) {
					 order.setOrderStatus(WholescriptscoreConstants.ORDER_STATUS_PARTIALLY_SHIPPED);
				 }
				 else {
					 order.setOrderStatus(WholescriptscoreConstants.ORDER_STATUS_PROCESSING);
				 }

			 }

		 }

	 }
	 
	 @Override
    public void createACHPaymentInfo(OrderCreationRequest orderCreationRequest) {
		 
		 final CartModel cart = cartService.getSessionCart();
       if(Objects.nonNull(cart) && "Success".equalsIgnoreCase(orderCreationRequest.getAchCCAuthResponse())) {
      	 final ACHCardPaymentInfoModel achpaymentInfo = modelService.create(ACHCardPaymentInfoModel.class);
      	 achpaymentInfo.setCcType(orderCreationRequest.getAchCCType());
      	 achpaymentInfo.setCcNumber(orderCreationRequest.getAchCCNumber());
      	 achpaymentInfo.setCcValidity("2199-01-31");
      	 achpaymentInfo.setCcName(orderCreationRequest.getAchCCName());
      	 achpaymentInfo.setAuthAmount(orderCreationRequest.getAchAuthAmount());
      	 achpaymentInfo.setCurrency(orderCreationRequest.getAchCurrency());
      	 achpaymentInfo.setAuthDate(orderCreationRequest.getAchAuthDate());
      	 achpaymentInfo.setAuthTime(orderCreationRequest.getAchAuthTime());
      	 achpaymentInfo.setAuthCCNumber(orderCreationRequest.getAchAuthCCNumber());
      	 achpaymentInfo.setCcReAuthAmount(orderCreationRequest.getAchCCReAuthAmount());
      	 achpaymentInfo.setCcAuthResponse(orderCreationRequest.getAchCCAuthResponse());
      	 final UserModel customerModel = cart.getUser();
      	 achpaymentInfo.setCode(customerModel.getUid() + "_" + UUID.randomUUID());
      	 achpaymentInfo.setUser(customerModel);
      	 
      	 cart.setAchCardPaymentInfo(achpaymentInfo);
      	 modelService.saveAll(achpaymentInfo, cart);
       }
        
    }

	 @Override
	 public void verifyAutoshipAddressForRestriction(WholescriptsValidateAutoshipWsDTO requestData) throws Exception
	 {
		 if(Objects.nonNull(requestData.getAddress()))
		 {
			 B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
			 AddressWsDTO address = requestData.getAddress();
			 String regionCode = address.getRegion().getIsocode().contains("-") ? address.getRegion().getIsocode().split("-")[1] : address.getRegion().getIsocode();
			 if ("CA".equals(regionCode)) {
				 List<AbstractOrderEntryModel> caExlusionList = new ArrayList<>();
				 for (String productCode : requestData.getProductCodes()) {
					 boolean isExcluded = wholescriptsCartValidationDao.validateIfProductInCAExclusion(productCode, regionCode);
					 if (isExcluded) {
						 throw new RegionExclusionException("This product "+productCode+" cannot be shipped to California");
					 }
				 }
			 } else if ("NY".equals(regionCode)) {
				 for (String productCode : requestData.getProductCodes()) {
					 boolean isExcluded = wholescriptsCartValidationDao.validateIfProductInNYExclusion(productCode, regionCode);
					 if (isExcluded) {
						 if (StringUtils.containsAnyIgnoreCase(address.getLine1(), configurationService.getConfiguration().getString("wholescripts.address.po.box"))) {
							 throw new POBoxAgeRestrictionException("This product "+productCode+" cannot be shipped to New York");
						 } else if (Objects.nonNull(customer) && Objects.nonNull(customer.getDefaultB2BUnit()) && (StringUtils.isEmpty(customer.getDefaultB2BUnit().getAgeExpirationDate()) || customer.getDefaultB2BUnit().getAgeExpirationDate().equals("0000-00-00"))) {
							 throw new AgeRestrictionException("This "+productCode+" cannot be shipped to New York");
						 }
					 }
				 }
			 }
		 }
	 }


}
