/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.strategies.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.commerceservices.order.impl.DefaultCommerceCartCalculationStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.jalo.PromotionsManager.AutoApplyMode;
import de.hybris.platform.promotions.result.PromotionOrderResults;
import de.hybris.platform.tx.Transaction;

import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.wholescripts.core.model.ProtocolModel;
import com.wholescripts.core.model.RecommendationModel;
import com.wholescripts.core.strategies.WholescriptsCommerceCartCalculationStrategy;


public class WholescriptsCommerceCartCalculationStrategyImpl extends DefaultCommerceCartCalculationStrategy implements WholescriptsCommerceCartCalculationStrategy
{
	private boolean calculateExternalTaxes = false;
	@Resource(name = "defaultPromotionsService")
	private PromotionsService defaultPromotionsService;
	
	@Override
	public boolean calculateCart(final CommerceCartParameter parameter)
	{
		if(parameter.getCart() instanceof ProtocolModel || parameter.getCart() instanceof RecommendationModel ) {
		
		final CartModel cartModel = parameter.getCart();

		validateParameterNotNull(cartModel, "Cart model cannot be null");

		final CalculationService calcService = getCalculationService();
		boolean recalculated = false;
		if (calcService.requiresCalculation(cartModel))
		{
			final Transaction tx = Transaction.current();
			tx.begin();
			boolean rollbackNeeded = true;
			try
			{
				try
				{
					parameter.setRecalculate(false);
					beforeCalculate(parameter);
					setDeliveryCost(cartModel,parameter);
					calcService.calculate(cartModel);
					getPromotionsService().updatePromotions(getPromotionGroups(), cartModel, true, AutoApplyMode.APPLY_ALL,
							AutoApplyMode.APPLY_ALL, getTimeService().getCurrentTime());
					if(Objects.nonNull(cartModel.getDeliveryMode())  && cartModel.getDeliveryMode() instanceof ZoneDeliveryModeModel) {
						ZoneDeliveryModeModel zoneDeilvery =(ZoneDeliveryModeModel) cartModel.getDeliveryMode();
						if(!"UPS".equals(zoneDeilvery.getProvider()) && !"USPS".equals(zoneDeilvery.getProvider()) ) {
							resetDeliveryCostIfNecessary(cartModel);
						}
					}
					rollbackNeeded = false;
				}
				catch (final CalculationException calculationException)
				{
					//commented for to add 0 price row products.
//					throw new IllegalStateException(
//							"Cart model " + cartModel.getCode() + " was not calculated due to: " + calculationException.getMessage(),
//							calculationException);
				}
				finally
				{
					afterCalculate(parameter);
				}
				recalculated = true;
			}
			finally
			{
				if (rollbackNeeded || tx.isRollbackOnly())
				{
					tx.rollback();
				}
				else
				{
					tx.commit();
				}
			}
		}
		if (calculateExternalTaxes)
		{
			getExternalTaxesService().calculateExternalTaxes(cartModel);
		}
		return recalculated;
		
		}
		return true;
	}
	
	@Override
	public boolean recalculateCart(final CommerceCartParameter parameter)
	{
		if(parameter.getCart() instanceof ProtocolModel || parameter.getCart() instanceof RecommendationModel ) {
		final CartModel cartModel = parameter.getCart();

		final Transaction tx = Transaction.current();
		tx.begin();
		boolean rollbackNeeded = true;
		try
		{
			try
			{
				parameter.setRecalculate(true);
				beforeCalculate(parameter);
				setDeliveryCost(cartModel,parameter);
				getCalculationService().recalculate(cartModel);
				getPromotionsService().updatePromotions(getPromotionGroups(), cartModel, true, AutoApplyMode.APPLY_ALL,
						AutoApplyMode.APPLY_ALL, getTimeService().getCurrentTime());
				resetDeliveryCostIfNecessary(cartModel);
				rollbackNeeded = false;
			}
			catch (final CalculationException calculationException)
			{
				throw new IllegalStateException(String.format("Cart model %s was not calculated due to: %s ", cartModel.getCode(),
						calculationException.getMessage()), calculationException);
			}
			finally
			{
				afterCalculate(parameter);
			}
		}
		finally
		{
			if (rollbackNeeded || tx.isRollbackOnly())
			{
				tx.rollback();
			}
			else
			{
				tx.commit();
			}
		}

		return true;
		}
		return true;
	}

	protected void setDeliveryCost(final CartModel cartModel,final CommerceCartParameter parameter) throws CalculationException
	{
		if(StringUtils.isNotEmpty(parameter.getDeliveryCost())) {
			cartModel.setDeliveryCost(Double.valueOf(parameter.getDeliveryCost()));
			getCalculationService().calculateTotals(cartModel, true);
		}
	}

	@Override
	public PromotionOrderResults applyPromotionOnCart(CartModel cartModel)
	{
		return getPromotionsService().updatePromotions(getPromotionGroups(), cartModel, true, AutoApplyMode.APPLY_ALL,
				AutoApplyMode.APPLY_ALL, getTimeService().getCurrentTime());
		
	}
}
