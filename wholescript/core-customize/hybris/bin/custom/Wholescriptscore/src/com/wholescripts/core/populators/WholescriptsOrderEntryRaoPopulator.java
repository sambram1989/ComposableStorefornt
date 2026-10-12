/*
 * Copyright (c) 2025 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;

import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ruleengineservices.converters.populator.OrderEntryRaoPopulator;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.util.DiscountValue;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wholescripts.core.model.PromotionCategoryModel;

/**
 *
 */
public class WholescriptsOrderEntryRaoPopulator extends OrderEntryRaoPopulator
{
	private static final Logger LOGGER = LoggerFactory.getLogger(WholescriptsOrderEntryRaoPopulator.class);
	@Override
	public void populate(AbstractOrderEntryModel source, OrderEntryRAO target) throws ConversionException
	{
		this.getOrderEntryRaoProductPopulator().populate(source.getProduct(), target);
		if(CollectionUtils.isNotEmpty(source.getProduct().getPromtionCategory())) {
			target.setPromoCategoryCodes(source.getProduct().getPromtionCategory().stream().map(PromotionCategoryModel::getCode).collect(Collectors.toSet()));
		}
		if(Objects.nonNull(source.getProduct().getWholescriptsBrand())) {
			target.setPromoBrandCodes(Set.of(source.getProduct().getWholescriptsBrand().getCode()));
		}
		if (Objects.nonNull(source.getQuantity())) {

			target.setQuantity(source.getQuantity().intValue());
		}
		Double basePrice = source.getBasePrice()/source.getQuantity();
		if (Objects.nonNull(basePrice)) {

			target.setBasePrice(BigDecimal.valueOf(basePrice));
			target.setPrice(target.getBasePrice());

			AbstractOrderModel order = source.getOrder();
			if (Objects.nonNull(order) && Objects.nonNull(order.getCurrency())) {
				target.setCurrencyIsoCode(order.getCurrency().getIsocode());
			} else {
				LOGGER.warn("Order is null or the order currency is not set correctly");
			}      
		}

		if (Objects.nonNull(source.getEntryNumber())) {

			target.setEntryNumber(source.getEntryNumber());
		}
		if (CollectionUtils.isNotEmpty(source.getDiscountValues())) {
			source.getDiscountValues().forEach(discountValue -> applyDiscount(target, discountValue));
			target.setDiscountValues(Converters.convertAll(source.getDiscountValues(), this.getDiscountValueConverter()));
		}
		target.setGiveAway(BooleanUtils.toBoolean(source.getGiveAway()));
		target.setBasePrice(target.getPrice());

	}

	protected void applyDiscount(OrderEntryRAO target, DiscountValue discountValue) {
		BigDecimal discountAmount = BigDecimal.valueOf(discountValue.apply(1.0D, target.getPrice().doubleValue(), this.getCurrencyUtils().getDigitsOfCurrencyOrDefault(target.getCurrencyIsoCode()), target.getCurrencyIsoCode()).getAppliedValue());
		discountAmount = BigDecimal.valueOf(discountAmount.intValue() /target.getQuantity());
		target.setPrice(target.getPrice().subtract(discountAmount));
	}
}
