/*
 * Copyright (c) 2025 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service.impl;

import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.promotions.result.PromotionOrderResults;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wholescripts.core.exception.PromotionErrorMessageException;
import com.wholescripts.core.service.WholescriptsCommerceCartService;
import com.wholescripts.core.strategies.WholescriptsCommerceCartCalculationStrategy;


/**
 *
 */
public class WholescriptsCommerceCartServiceImpl implements WholescriptsCommerceCartService
{
	@Resource(name = "wholescriptsCommerceCartCalculationStrategy")
	WholescriptsCommerceCartCalculationStrategy wholescriptsCommerceCartCalculationStrategy;

	@Override
	public void applyPromotionOnCart(final CommerceCartParameter parameter) throws Exception
	{
		final PromotionOrderResults results = wholescriptsCommerceCartCalculationStrategy.applyPromotionOnCart(parameter.getCart());
		final ObjectMapper objectMapper = new ObjectMapper();
		if (Objects.nonNull(parameter.getPromotion()))
		{
			final RuleBasedPromotionModel promotion = (RuleBasedPromotionModel) parameter.getPromotion();
			if (CollectionUtils.isNotEmpty(results.getAllResults()))
			{
				List<String> codes=	results.getAllResults().stream().map(r -> r.getPromotion().getCode()).collect(Collectors.toList());
				if (!codes.contains(promotion.getCode()))
				{
					setErrorMessage(objectMapper, promotion);
				}
				return;
			}

			setErrorMessage(objectMapper, promotion);
		}
	}

	private void setErrorMessage(final ObjectMapper objectMapper, final RuleBasedPromotionModel promotion)
			throws JsonProcessingException, JsonMappingException, PromotionErrorMessageException
			{
		throw new PromotionErrorMessageException("Promotion is available but Condition does'nt match");
	}


}
