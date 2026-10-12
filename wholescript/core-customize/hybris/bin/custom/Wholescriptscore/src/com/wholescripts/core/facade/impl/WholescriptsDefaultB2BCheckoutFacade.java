package com.wholescripts.core.facade.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;
import static de.hybris.platform.util.localization.Localization.getLocalizedString;

import de.hybris.platform.b2b.enums.CheckoutPaymentType;
import de.hybris.platform.b2b.model.B2BCommentModel;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2bacceleratorfacades.checkout.data.PlaceOrderData;
import de.hybris.platform.b2bacceleratorfacades.exception.EntityValidationException;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BPaymentTypeData;
import de.hybris.platform.b2bacceleratorfacades.order.impl.DefaultB2BCheckoutFacade;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.commercefacades.order.data.ZoneDeliveryModeData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.strategies.GenerateMerchantTransactionCodeStrategy;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.payment.PaymentService;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.util.PriceValue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.wholescripts.core.facade.WholescriptsCheckoutFacade;
import com.wholescripts.core.facade.WholescriptsUserFacade;
import com.wholescripts.core.service.WholescriptsIntegrationsService;
import com.wholescripts.data.CustomerActivityData;
import com.wholescripts.data.OrderCreationRequest;
import com.wholescripts.data.RatedShipmentData;
import com.wholescripts.data.TotalChargesData;
import com.wholescripts.data.UPSShipingResponseData;
import com.wholescripts.data.USPSPackageData;
import com.wholescripts.data.USPSPostageData;
import com.wholescripts.data.USPSShippingResponseData;
import com.wholescripts.data.WholescriptsAuthResponseData;

public class WholescriptsDefaultB2BCheckoutFacade extends DefaultB2BCheckoutFacade implements WholescriptsCheckoutFacade{
    
	 private static final Logger LOG = Logger.getLogger(WholescriptsDefaultB2BCheckoutFacade.class);
	 private static final String CART_CHECKOUT_PAYMENTTYPE_INVALID = "cart.paymenttype.invalid";
    private static final String CART_CHECKOUT_DELIVERYADDRESS_INVALID = "cart.deliveryAddress.invalid";
    private static final String CART_CHECKOUT_DELIVERYMODE_INVALID = "cart.deliveryMode.invalid";
    private static final String CART_CHECKOUT_NOT_CALCULATED = "cart.not.calculated";
    private static final String CART_CHECKOUT_TRANSACTION_NOT_AUTHORIZED = "cart.transation.notAuthorized";
    @Resource(name = "wholescriptsIntegrationsService")
  	 private WholescriptsIntegrationsService wholescriptsIntegrationsService;
    

    @Resource(name="defaultGenerateMerchantTransactionCodeStrategy")
    private GenerateMerchantTransactionCodeStrategy generateMerchantTransactionCodeStrategy;

    @Resource(name="defaultPaymentService")
    private PaymentService paymentService;

    @Resource(name = "modelService")
    private ModelService modelService;
    
    @Resource(name = "wholescriptsUserFacade")
    private WholescriptsUserFacade wholescriptsUserFacade;

    @Resource(name = "userService")
    private UserService userService;
    @Resource(name = "commonI18NService")
    private CommonI18NService commonI18NService;
    @Resource(name = "configurationService")
    private ConfigurationService configurationService;
    @Resource(name = "sessionService")
    private SessionService sessionService;

    @Override
    protected boolean isValidCheckoutCart(PlaceOrderData placeOrderData) {
        final CartData cartData = getCheckoutCart();
        final boolean valid = true;

		  /*
		   * if (!cartData.isCalculated()) { throw new
		   * EntityValidationException(getLocalizedString(CART_CHECKOUT_NOT_CALCULATED)); }
		   */
        if (cartData.getDeliveryAddress() == null)
        {
            throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_DELIVERYADDRESS_INVALID));
        }

        if (cartData.getDeliveryMode() == null)
        {
            throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_DELIVERYMODE_INVALID));
        }

        if (getPaymentTypes().stream()
                .map(B2BPaymentTypeData::getCode)
                .noneMatch(code -> cartData.getPaymentType().getCode().equals(code)))
        {
            throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_PAYMENTTYPE_INVALID));
        }

        return valid;
    }

    @Override
    public <T extends AbstractOrderData> T placeOrder(PlaceOrderData placeOrderData) throws InvalidCartException {
        // for CARD type, transaction must be authorized before placing order
        final boolean isCardtPaymentType = CheckoutPaymentType.XCRD.equals(getCart().getPaymentType());
        if (isCardtPaymentType)
        {
            final List<PaymentTransactionModel> transactions = getCart().getPaymentTransactions();
            boolean authorized = false;
            for (final PaymentTransactionModel transaction : transactions)
            {
                for (final PaymentTransactionEntryModel entry : transaction.getEntries())
                {
                    if (entry.getType().equals(PaymentTransactionType.AUTHORIZATION)
                            && TransactionStatus.ACCEPTED.name().equals(entry.getTransactionStatus()))
                    {
                        authorized = true;
                        break;
                    }
                }
            }
            if (!authorized)
            {
                // FIXME - change error message
                throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_TRANSACTION_NOT_AUTHORIZED));
            }
        }
        CartModel cart = getCart();
        cart.setPurchaseOrderNumber(placeOrderData.getPoNumber());
        modelService.save(cart);
        if (isValidCheckoutCart(placeOrderData))
        {

            return (T) super.placeOrder();
        }

        return null;
    }
    @Override
    public CartData updateCheckoutCart(final CartData cartData)
    {
        final CartModel cartModel = getCart();
        if (cartModel == null)
        {
            return null;
        }
        // set payment type
        if (cartData.getPaymentType() != null)
        {
            final String newPaymentTypeCode = cartData.getPaymentType().getCode();
            if (CheckoutPaymentType.ZTEL.getCode().equals(newPaymentTypeCode)&& Objects.nonNull(cartModel.getPaymentInfo()))
            {
                cartModel.setPaymentInfo(null);
            }

             // clear delivery address, delivery mode and payment details when changing payment type
          /*  if (cartModel.getPaymentType() == null || !newPaymentTypeCode.equalsIgnoreCase(cartModel.getPaymentType().getCode()))
            {
                cartModel.setDeliveryAddress(null);
                cartModel.setDeliveryMode(null);
                cartModel.setPaymentInfo(null);
            }*/
            setPaymentTypeForCart(newPaymentTypeCode, cartModel);
            // cost center need to be be cleared if using card
            //we do not have cost center concept with XCRD hence updated it to CARD
            if (CheckoutPaymentType.CARD.getCode().equals(newPaymentTypeCode))
            {
                setCostCenterForCart(null, cartModel);
            }
        }

        // set cost center
        if (cartData.getCostCenter() != null)
        {
            setCostCenterForCart(cartData.getCostCenter().getCode(), cartModel);
        }

        // set purchase order number
        if (cartData.getPurchaseOrderNumber() != null)
        {
            cartModel.setPurchaseOrderNumber(cartData.getPurchaseOrderNumber());
        }

        // set delivery address
        if (cartData.getDeliveryAddress() != null)
        {
            setDeliveryAddress(cartData.getDeliveryAddress());
        }

        // set quote request description
        if (cartData.getB2BComment() != null)
        {
            final B2BCommentModel b2bComment = getModelService().create(B2BCommentModel.class);
            b2bComment.setComment(cartData.getB2BComment().getComment());
            getB2bCommentService().addComment(cartModel, b2bComment);
        }

        getModelService().save(cartModel);
        return getCheckoutCart();

    }

    @Override
  	public List<? extends DeliveryModeData> getSupportedDeliveryModes()
  	{
  		final List<DeliveryModeData> result = new ArrayList<DeliveryModeData>();
  		final CartModel cartModel = getCart();
  		if (cartModel != null)
  		{
  			UPSShipingResponseData upsShipingResponse = UPSIntegrationForShippingCost();
  			USPSShippingResponseData uspsShippingResponseData = USPSIntegrationForShippingCost();
  		   List<DeliveryModeModel> filteredDeliveryList = new ArrayList<>(getDeliveryService().getSupportedDeliveryModeListForOrder(cartModel));
			validateUPSAndUSPSDeliveryMode(upsShipingResponse, uspsShippingResponseData,filteredDeliveryList);
			for (final DeliveryModeModel deliveryModeModel : filteredDeliveryList)
  			{
  				result.add(convert(deliveryModeModel,upsShipingResponse,uspsShippingResponseData));
  			}
  		}
  		return result;
  	}
    
    private void validateUPSAndUSPSDeliveryMode( final UPSShipingResponseData upsShipingResponse,
   		 USPSShippingResponseData uspsShippingResponseData,List<DeliveryModeModel> filteredDeliveryList)
    {
   	 if(Objects.nonNull(uspsShippingResponseData)) {
   		 if(Objects.isNull(uspsShippingResponseData.getRateV4Response()) || CollectionUtils.isEmpty(uspsShippingResponseData.getRateV4Response().getPackageUSPS())){
   			 uspsShippingResponseData=null;
   		 }else {
   			 List<USPSPackageData> uspsPackageDataList=uspsShippingResponseData.getRateV4Response().getPackageUSPS();
   			Optional<USPSPackageData> packageData= uspsPackageDataList.stream().filter(p -> CollectionUtils.isNotEmpty(p.getPostage())).findAny();
   		   if(packageData.isEmpty()) {
   		   	uspsShippingResponseData=null;
   		   }
   		 }
   	 }

   	 List<DeliveryModeModel> deliveryList = new ArrayList<>(filteredDeliveryList);
   	 for(final DeliveryModeModel deliveryModeModel:deliveryList) {
   		 if (deliveryModeModel instanceof ZoneDeliveryModeModel)
   		 {
   			 final ZoneDeliveryModeModel zoneDeliveryModeModel = (ZoneDeliveryModeModel) deliveryModeModel;
   			 if (StringUtils.isNotEmpty(zoneDeliveryModeModel.getProvider()) && ("UPS".equals(zoneDeliveryModeModel.getProvider()) && Objects.isNull(upsShipingResponse)) ||("USPS".equals(zoneDeliveryModeModel.getProvider()) && Objects.isNull(uspsShippingResponseData)))
   			 {
   				 filteredDeliveryList.remove(deliveryModeModel);
   			 }
   		 }
   	 }
    }

	private USPSShippingResponseData USPSIntegrationForShippingCost()
	{
		USPSShippingResponseData uspsShippingResponseData = null;
		try
		{
			uspsShippingResponseData = wholescriptsIntegrationsService.getUSPSShipingCharges();
		}
		catch (Exception e)
		{
			LOG.info(e.getMessage());
		}
		return uspsShippingResponseData;
	}

	private UPSShipingResponseData UPSIntegrationForShippingCost()
	{
		UPSShipingResponseData upsShipingResponse = null;
		try
		{
			 upsShipingResponse = wholescriptsIntegrationsService.getUPSShipingCharges();
		}
		catch (Exception e)
		{
			LOG.info(e.getMessage());
		}
		return upsShipingResponse;
	}
     
	protected DeliveryModeData convert(final DeliveryModeModel deliveryModeModel,final UPSShipingResponseData upsShipingResponse, final USPSShippingResponseData uspsShippingResponseData)
	{
		if (deliveryModeModel instanceof ZoneDeliveryModeModel)
		{
			BaseStoreModel baseStore = deliveryModeModel.getStores().iterator().next();
			double totalCharges = 0.0d;
			final ZoneDeliveryModeModel zoneDeliveryModeModel = (ZoneDeliveryModeModel) deliveryModeModel;
			if(StringUtils.isNotEmpty(zoneDeliveryModeModel.getProvider()) && "UPS".equals(zoneDeliveryModeModel.getProvider()) && Objects.nonNull(upsShipingResponse)) {
				RatedShipmentData ratedShipmentData	= upsShipingResponse.getRateResponse().getRatedShipment().stream().filter(s -> StringUtils.isNotEmpty(zoneDeliveryModeModel.getApiCode()) && s.getService().getCode().equals(zoneDeliveryModeModel.getApiCode())).findFirst().orElse(null);
				if(Objects.nonNull(ratedShipmentData)) {
					List<TotalChargesData> totalChargesData =ratedShipmentData.getRatedPackage().stream().map(r -> r.getTotalCharges()).collect(Collectors.toList());
					for(TotalChargesData charges : totalChargesData) {
						totalCharges+= Double.valueOf(charges.getMonetaryValue());
					}	
				}
			}else if(StringUtils.isNotEmpty(zoneDeliveryModeModel.getProvider()) && "USPS".equals(zoneDeliveryModeModel.getProvider()) && Objects.nonNull(uspsShippingResponseData)) {
				List<USPSPackageData> uspsPackageDataList=uspsShippingResponseData.getRateV4Response().getPackageUSPS();
				for(USPSPackageData packageData : uspsPackageDataList) {
					if(Objects.nonNull(packageData.getPostage())) {
						USPSPostageData postageData=packageData.getPostage().stream().filter(p -> p.getClassId().equals("1")).findFirst().orElse(null);
						if(Objects.nonNull(postageData)) {
							totalCharges+=Double.valueOf(postageData.getRate());
						}
					}
				}
			}
			final CartModel cartModel = getCart();
			if (cartModel != null)
			{
				final ZoneDeliveryModeData zoneDeliveryModeData = getZoneDeliveryModeConverter().convert(zoneDeliveryModeModel);
				if(Objects.nonNull(zoneDeliveryModeData) && StringUtils.isNotEmpty(zoneDeliveryModeModel.getProvider()) && ("UPS".equals(zoneDeliveryModeModel.getProvider()) || "USPS".equals(zoneDeliveryModeModel.getProvider())) ) {
					zoneDeliveryModeData.setDeliveryCost(getPriceDataFactory().create(PriceDataType.BUY,
							BigDecimal.valueOf(totalCharges),baseStore.getDefaultCurrency().getIsocode()));
				}
				else {
					final PriceValue deliveryCost = getDeliveryService().getDeliveryCostForDeliveryModeAndAbstractOrder(deliveryModeModel,
							cartModel);
					if (zoneDeliveryModeData != null )
					{
						zoneDeliveryModeData.setDeliveryCost(getPriceDataFactory().create(PriceDataType.BUY,
								BigDecimal.valueOf(deliveryCost.getValue()), deliveryCost.getCurrencyIso()));
					}
				}	
				return zoneDeliveryModeData;
			}
			return null;
		}
		DeliveryModeData deliveryModeData = getDeliveryModeConverter().convert(deliveryModeModel);
		deliveryModeData.setDeliveryCost(getPriceDataFactory().create(PriceDataType.BUY,
								BigDecimal.valueOf(0.0), commonI18NService.getCurrentCurrency()));
		return deliveryModeData;
	}
	
	@Override
	public boolean setDeliveryMode(final String deliveryModeCode,final String deliveryCost)
	{
		validateParameterNotNullStandardMessage("deliveryModeCode", deliveryModeCode);

		final CartModel cartModel = getCart();
		if (cartModel != null && isSupportedDeliveryMode(deliveryModeCode, cartModel))
		{
			final DeliveryModeModel deliveryModeModel = getDeliveryService().getDeliveryModeForCode(deliveryModeCode);
			if (deliveryModeModel != null)
			{
				final CommerceCheckoutParameter parameter = createCommerceCheckoutParameter(cartModel, true);
				parameter.setDeliveryMode(deliveryModeModel);
				parameter.setDeliveryCost(deliveryCost);
				return getCommerceCheckoutService().setDeliveryMode(parameter);
			}
		}
		return false;
	}

    @Override
    public boolean authorizePayment(final OrderCreationRequest orderCreationRequest, final CustomerActivityData customerActivityData) {
   	 customerActivityData.setEvent("cc_aaprove");
   	 final CartModel cart = getCart();
        if (cart == null)
        {
            return false;
        }
        if (cart.getPaymentInfo() instanceof CreditCardPaymentInfoModel)
        {
            final CreditCardPaymentInfoModel creditCardPaymentInfoModel = cart == null ? null
                    : (CreditCardPaymentInfoModel) cart.getPaymentInfo();
            if (checkIfCurrentUserIsTheCartUser() && creditCardPaymentInfoModel != null)
            {
                try {
                    WholescriptsAuthResponseData authResponse = wholescriptsIntegrationsService.authorizationForTheCart(cart, orderCreationRequest);
                    
                    if(Objects.nonNull(authResponse) && authResponse.isAuthorized() && authResponse.getStatusCode().equals("100"))
                    {
                  	   if(StringUtils.isNotEmpty(authResponse.getNetworkTransactionId())) {
                  	   	sessionService.setAttribute("networkTransactionID", authResponse.getNetworkTransactionId());
                  	   }
                  	   customerActivityData.setMessage("Amount authorized " + authResponse.getAmount() + " with " + authResponse.getCardType()+ " Card and card number is " + authResponse.getCardNumber());
                        wholescriptsUserFacade.saveCustomerActivity(customerActivityData);
                        PaymentTransactionEntryModel entry = createPaymentTransactionForCart(authResponse,cart);
                        return entry != null
                                && (TransactionStatus.ACCEPTED.name().equals(entry.getTransactionStatus()));
                    }
                } catch (Exception e) {
               	 customerActivityData.setMessage("Failed amount authorization " + e.getMessage());
                   wholescriptsUserFacade.saveCustomerActivity(customerActivityData);
                   throw new RequestParameterException(String.format(e.getMessage()), RequestParameterException.MISSING);
                }
            }
        }
        return true;
    }

    private PaymentTransactionEntryModel createPaymentTransactionForCart(WholescriptsAuthResponseData authResponse, CartModel cartModel) {
        final PaymentTransactionModel transaction = modelService.create(PaymentTransactionModel.class);
        final BigDecimal amount = BigDecimal.valueOf(authResponse.getAmount() == null ? 0d : Double.parseDouble(authResponse.getAmount()));
        transaction.setCode(generateMerchantTransactionCodeStrategy.generateCode(cartModel));
        transaction.setPlannedAmount(amount);
        transaction.setOrder(cartModel);
        transaction.setInfo(cartModel.getPaymentInfo());
        final PaymentTransactionType paymentTransactionType = PaymentTransactionType.AUTHORIZATION;
        final String newEntryCode = paymentService.getNewPaymentTransactionEntryCode(transaction, paymentTransactionType);

        final PaymentTransactionEntryModel entry = modelService.create(PaymentTransactionEntryModel.class);
        entry.setAmount(amount);
        entry.setCurrency(cartModel.getCurrency());
        entry.setType(PaymentTransactionType.AUTHORIZATION);
        entry.setTime(new Date());
        entry.setPaymentTransaction(transaction);
        entry.setTransactionStatus(TransactionStatus.ACCEPTED.toString());
        entry.setRequestId("TransactionId : "+ authResponse.getTransactionId() + " : TransactionType : "+ authResponse.getTransactionType());
        entry.setTransactionStatusDetails("AuthorizationCode : "+ authResponse.getAuthorizationCode() + " : AuthRefCode : " + authResponse.getAuthRefCode() + " : MerchantId : "+ authResponse.getMerchantId());
        entry.setCode(newEntryCode);
        if (authResponse.isRejectedForFraud() || authResponse.isReviewForFraud()) {
      	  cartModel.setDeliveryBlock(true);;
        }
        modelService.saveAll(cartModel, transaction, entry);
        return entry;

    }

    @Override
    public CartData getCheckoutCart() {
        final CartData cartData = getCartFacade().getSessionCart();
        if (cartData != null)
        {
           // cartData.setDeliveryAddress(getDeliveryAddress());
            cartData.setDeliveryMode(getDeliveryMode());
            cartData.setPaymentInfo(getPaymentDetails());
        }
        return cartData;
    }
    protected CCPaymentInfoData getPaymentDetails()
    {
        final CartModel cart = getCart();
        if (cart != null)
        {
            final PaymentInfoModel paymentInfo = cart.getPaymentInfo();
            if (Objects.nonNull(paymentInfo) && paymentInfo instanceof CreditCardPaymentInfoModel)
            {
                return getCreditCardPaymentInfoConverter().convert((CreditCardPaymentInfoModel) paymentInfo);
            }
        }
        return null;
    }
    
    @Override
	 public OrderModel placeOrder(final CartModel cartModel, final SalesApplication source) throws InvalidCartException {
   	 
   	 final boolean isCardtPaymentType = CheckoutPaymentType.XCRD.equals(cartModel.getPaymentType());
   	 OrderModel orderModel = null;
   	 if (isCardtPaymentType) {
           final List<PaymentTransactionModel> transactions = cartModel.getPaymentTransactions();
           boolean authorized = false;
           for (final PaymentTransactionModel transaction : transactions) {
               for (final PaymentTransactionEntryModel entry : transaction.getEntries()) {
                   if (entry.getType().equals(PaymentTransactionType.AUTHORIZATION)
                           && TransactionStatus.ACCEPTED.name().equals(entry.getTransactionStatus())) {
                       authorized = true;
                       break;
                   }
               }
           }
           if (!authorized) {
               throw new EntityValidationException(getLocalizedString(CART_CHECKOUT_TRANSACTION_NOT_AUTHORIZED));
           }
       }
   	 
   	 LOG.info("Inside placeOrder of WholescriptsDefaultB2BCheckoutFacade, placing order for cart id " + cartModel.getCode());
   	 orderModel = super.placeOrder(cartModel);
   	 modelService.save(orderModel);
   	 modelService.remove(cartModel);
   	 return orderModel;
	 }
    
	 @Override
	 public boolean authorizeExternalOrderPayment(final CartModel  cart, final PaymentInfoModel  paymentInfo, final OrderCreationRequest orderCreationRequest) {
		 
		 if (paymentInfo instanceof CreditCardPaymentInfoModel) {
			 final CreditCardPaymentInfoModel creditCardPaymentInfoModel = (CreditCardPaymentInfoModel) paymentInfo;
			 if (creditCardPaymentInfoModel != null) {
				 try {
					 LOG.info("Inside authorizeExternalOrderPayment of WholescriptsDefaultB2BCheckoutFacade, performing authorization of cart id " + cart.getCode());
					 WholescriptsAuthResponseData authResponse = wholescriptsIntegrationsService.authorizationForTheCart(cart, orderCreationRequest);
					 if (Objects.nonNull(authResponse) && authResponse.isAuthorized() && authResponse.getStatusCode().equals("100")) {
						 PaymentTransactionEntryModel entry = createPaymentTransactionForCart(authResponse, cart);
						 return entry != null && (TransactionStatus.ACCEPTED.name().equals(entry.getTransactionStatus()));
					 }
				 }
				 catch (Exception e) {
					 throw new RequestParameterException(String.format(e.getMessage()), RequestParameterException.MISSING);
				 }
			 }
		 }
		 LOG.info("Inside authorizeExternalOrderPayment of WholescriptsDefaultB2BCheckoutFacade, completed authorization of cart id " + cart.getCode());
		 return true;
	 }
	 
	 @Override
	 public List<B2BPaymentTypeData> getPaymentTypes() {
		 
		 List<CheckoutPaymentType> checkoutPaymentTypes = getEnumerationService().getEnumerationValues(CheckoutPaymentType._TYPECODE);
		 B2BCustomerModel currentUser = (B2BCustomerModel) userService.getCurrentUser();
		 
		 if (Objects.nonNull(currentUser) && Objects.nonNull(currentUser.getDefaultB2BUnit())
				 && Objects.nonNull(currentUser.getDefaultB2BUnit().getSapCustomerGroup())) {
			
			 if(Objects.nonNull(currentUser.getDefaultB2BUnit().getSapPaymentTerms()) && currentUser.getDefaultB2BUnit().getSapPaymentTerms().getCode().startsWith("NT")) {
				 List<String> excludedPaymenyType = List.of(configurationService.getConfiguration().getString("wholescripts.exclude.payment.type").split(","));
				 checkoutPaymentTypes = checkoutPaymentTypes.stream().filter(p -> !excludedPaymenyType.contains(p.getCode())).collect(Collectors.toList());
			 }else {
				 checkoutPaymentTypes = checkoutPaymentTypes.stream().filter(p -> !p.getCode().startsWith("NT")).collect(Collectors.toList());
			 }
			
			 if ("02".equals(currentUser.getDefaultB2BUnit().getSapCustomerGroup().getCode())) {
				 
				 checkoutPaymentTypes = checkoutPaymentTypes.stream().filter(p -> p.getCode().equalsIgnoreCase("XCRD"))
						 .collect(Collectors.toList());
			 }
			 
			 
		 }
		 //	checkoutPaymentTypes.removeAll(getB2bCommerceUnitService().getRootUnit().getB2bExcludedPaymentTypes());

		 return Converters.convertAll(checkoutPaymentTypes, getB2bPaymentTypeDataConverter());
	 }
}
