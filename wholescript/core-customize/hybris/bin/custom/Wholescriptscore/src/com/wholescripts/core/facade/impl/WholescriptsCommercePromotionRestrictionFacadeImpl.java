/*
 * Copyright (c) 2025 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.facade.impl;

import de.hybris.platform.commercefacades.product.data.PromotionData;
import de.hybris.platform.commercefacades.promotion.PromotionOption;
import de.hybris.platform.commercefacades.promotion.impl.DefaultCommercePromotionRestrictionFacade;
import de.hybris.platform.commerceservices.model.promotions.PromotionOrderRestrictionModel;
import de.hybris.platform.commerceservices.promotion.CommercePromotionRestrictionException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.converters.ConfigurablePopulator;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collection;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.wholescripts.core.facade.WholescriptsCommercePromotionRestrictionFacade;
import com.wholescripts.core.service.WholescriptsCommerceCartService;
import com.wholescripts.core.service.WholescriptsCommercePromotionService;


/**
 *
 */
public class WholescriptsCommercePromotionRestrictionFacadeImpl extends DefaultCommercePromotionRestrictionFacade
		implements WholescriptsCommercePromotionRestrictionFacade
{
	private static final Logger LOG = Logger.getLogger(WholescriptsCommercePromotionRestrictionFacadeImpl.class);
	@Resource(name = "wholescriptsCommerceCartService")
	private WholescriptsCommerceCartService wholescriptsCommerceCartService;
	@Resource(name = "modelService")
	private ModelService modelService;
	@Resource(name = "wholescriptsCommercePromotionService")
	private WholescriptsCommercePromotionService wholescriptsCommercePromotionService;
	@Resource(name = "promotionsConverter")
	private Converter<AbstractPromotionModel, PromotionData> promotionsConverter;
	@Resource(name = "promotionConfiguredPopulator")
	private ConfigurablePopulator<AbstractPromotionModel, PromotionData, PromotionOption> promotionConfiguredPopulator;

	@Override
	public void enablePromotionForCart(final String promotionCode) throws Exception
	{
		final AbstractPromotionModel promotion = wholescriptsCommercePromotionService.getPromotionForCode(promotionCode);
		PromotionOrderRestrictionModel restriction;
		try
		{
			restriction = getCommercePromotionRestrictionService().getPromotionOrderRestriction(promotion);
		}
		catch (final UnknownIdentifierException e)
		{
			restriction = modelService.create(PromotionOrderRestrictionModel.class);
			restriction.setPromotion(promotion);
			modelService.save(restriction);
		}
		final CartModel sessionCart = getCartService().getSessionCart();
		getCommercePromotionRestrictionService().addOrderToRestriction(restriction, sessionCart);
		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(sessionCart);
		parameter.setPromotion(promotion);
		wholescriptsCommerceCartService.applyPromotionOnCart(parameter);
	}

	@Override
	public void disablePromotionForCart(final String promotionCode) throws Exception
	{
		final AbstractPromotionModel promotion = wholescriptsCommercePromotionService.getPromotionForCode(promotionCode);
		final PromotionOrderRestrictionModel restriction;

		try
		{
			restriction = getCommercePromotionRestrictionService().getPromotionOrderRestriction(promotion);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new CommercePromotionRestrictionException(e.getMessage());
		}

		final CartModel sessionCart = getCartService().getSessionCart();
		getCommercePromotionRestrictionService().removeOrderFromRestriction(restriction, sessionCart);

		final CommerceCartParameter parameter = new CommerceCartParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(sessionCart);
		try
		{
			wholescriptsCommerceCartService.applyPromotionOnCart(parameter);
		}
		catch (final Exception e)
		{
			LOG.error(e);
		}
	}

	@Override
	public PromotionData getPromotion(final String code, final Collection<PromotionOption> options)
	{
		if (code == null)
		{
			throw new IllegalArgumentException("Parameter code must not be null");
		}

		final AbstractPromotionModel promotionModel =wholescriptsCommercePromotionService.getPromotionModel(code);
		final PromotionData promotionData = promotionsConverter.convert(promotionModel);

		if (options != null)
		{
			promotionConfiguredPopulator.populate(promotionModel, promotionData, options);
		}

		return promotionData;
	}
}
