/*
 * Copyright (c) 2025 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.strategies.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import com.wholescripts.core.model.RecommendationModel;
import com.wholescripts.core.service.KlaviyoService;

import com.wholescripts.core.service.WholescriptsRecommendService;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.commerceservices.constants.CommerceServicesConstants;
import de.hybris.platform.commerceservices.order.hook.CommercePlaceOrderMethodHook;
import de.hybris.platform.commerceservices.order.impl.DefaultCommercePlaceOrderStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.util.Config;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.wholescripts.core.enums.RecommendationStatus;

import javax.annotation.Resource;

/**
 *
 */
public class WholescriptsCommercePlaceOrderStrategy extends DefaultCommercePlaceOrderStrategy
{
	@Resource(name = "timeService")
	private TimeService timeService;

	@Resource(name = "klaviyoService")
	private KlaviyoService klaviyoService;
	private List<CommercePlaceOrderMethodHook> commercePlaceOrderMethodHooks;

	@Resource(name = "wholescriptsRecommendService")
	private WholescriptsRecommendService wholescriptsRecommendService;

	@Resource(name = "modelService")
	ModelService modelService;

	@Resource(name = "b2bCustomerService")
	private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;

	private static final String wholescriptsSAPCustomergroup = Config.getParameter("wholescripts.sapCustomerGroup.patient.code");


	private static final ExecutorService executor = Executors.newFixedThreadPool(10);

	@Override
	public CommerceOrderResult placeOrder(final CommerceCheckoutParameter parameter) throws InvalidCartException
	{
		final CartModel cartModel = parameter.getCart();
		validateParameterNotNull(cartModel, "Cart model cannot be null");
		final CommerceOrderResult result = new CommerceOrderResult();
		try
		{
			beforePlaceOrder(parameter);
			final CustomerModel customer = (CustomerModel) cartModel.getUser();
			validateParameterNotNull(customer, "Customer model cannot be null");

			final OrderModel orderModel = getOrderService().createOrderFromCart(cartModel);
			if (orderModel != null)
			{
				// Reset the Date attribute for use in determining when the order was placed
				orderModel.setDate(timeService.getCurrentTime());
				// Store the current site and store on the order
				orderModel.setSite(getBaseSiteService().getCurrentBaseSite());
				orderModel.setStore(getBaseStoreService().getCurrentBaseStore());
				orderModel.setLanguage(getCommonI18NService().getCurrentLanguage());

				if (parameter.getSalesApplication() != null)
				{
					orderModel.setSalesApplication(parameter.getSalesApplication());
				}

				// clear the promotionResults that where cloned from cart PromotionService.transferPromotionsToOrder will copy them over bellow.
				orderModel.setAllPromotionResults(Collections.<PromotionResultModel> emptySet());

				getModelService().saveAll(customer, orderModel);

				if (cartModel.getPaymentInfo() != null && cartModel.getPaymentInfo().getBillingAddress() != null)
				{
					final AddressModel billingAddress = cartModel.getPaymentInfo().getBillingAddress();
					orderModel.setPaymentAddress(billingAddress);
					orderModel.getPaymentInfo().setBillingAddress(getModelService().clone(billingAddress));
					getModelService().save(orderModel.getPaymentInfo());
				}
				if(Objects.nonNull(cartModel.getTotalDiscounts())) {
					orderModel.setTotalDiscounts(cartModel.getTotalDiscounts());
				}
				orderModel.setPurchaseOrderNumber(cartModel.getPurchaseOrderNumber());
				getModelService().save(orderModel);
				// Transfer promotions to the order
				getPromotionsService().transferPromotionsToOrder(cartModel, orderModel, false);
				getModelService().refresh(orderModel);
				getModelService().refresh(customer);

				result.setOrder(orderModel);

				this.beforeSubmitOrder(parameter, result);

				getOrderService().submitOrder(orderModel);

				orderModel.getEntries().stream()
						.filter(e -> StringUtils.isNotEmpty(e.getTemplateId()) && e.getTemplateId().startsWith("recommendation-"))
						.forEach(e -> {
							String templateId = e.getTemplateId();
							B2BCustomerModel customerModel = b2bCustomerService.getCurrentB2BCustomer();
							String customerGroupCode = customerModel.getDefaultB2BUnit().getSapCustomerGroup().getCode();

							if (wholescriptsSAPCustomergroup.equalsIgnoreCase(customerGroupCode)) {
								String recommendationCode = templateId.split("-")[1];
								Optional<RecommendationModel> recommendation = wholescriptsRecommendService.getRecommendationDetails(
										customerModel.getUid(), recommendationCode);

								recommendation.ifPresent(model -> {
									model.setRecommendationStatus(RecommendationStatus.PATIENT_ORDERED);
									modelService.save(model);
								});
							}
						});

				sendOrderDetailsToKlaviyo(orderModel);
			}
			else
			{
				throw new IllegalArgumentException(String.format("Order was not properly created from cart %s", cartModel.getCode()));
			}
		}
		finally
		{
			getExternalTaxesService().clearSessionTaxDocument();
		}

		this.afterPlaceOrder(parameter, result);
		return result;
	}

	private void sendOrderDetailsToKlaviyo(OrderModel order){
		klaviyoService.sendOrderDetails(order);
	}
	
	protected void afterPlaceOrder(final CommerceCheckoutParameter parameter, final CommerceOrderResult result)
			throws InvalidCartException
	{
		if (getCommercePlaceOrderMethodHooks() != null
				&& (parameter.isEnableHooks() && getConfigurationService().getConfiguration().getBoolean(
						CommerceServicesConstants.PLACEORDERHOOK_ENABLED, true)))
		{
			for (final CommercePlaceOrderMethodHook commercePlaceOrderMethodHook : getCommercePlaceOrderMethodHooks())
			{
				commercePlaceOrderMethodHook.afterPlaceOrder(parameter, result);
			}
		}
	}
	
	protected List<CommercePlaceOrderMethodHook> getCommercePlaceOrderMethodHooks()
	{
		return commercePlaceOrderMethodHooks;
	}

	public void setCommercePlaceOrderMethodHooks(final List<CommercePlaceOrderMethodHook> commercePlaceOrderMethodHooks)
	{
		this.commercePlaceOrderMethodHooks = commercePlaceOrderMethodHooks;
	}
}
