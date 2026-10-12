/*
 * Copyright (c) 2025 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateIfSingleResult;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;
import static java.lang.String.format;

import de.hybris.platform.commerceservices.promotion.impl.DefaultCommercePromotionService;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.OrderPromotionModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengineservices.enums.RuleStatus;
import de.hybris.platform.webservicescommons.util.YSanitizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;

import com.wholescripts.core.exception.PromotionInvalidException;
import com.wholescripts.core.service.WholescriptsCommercePromotionService;

/**
 *
 */
public class WholescriptsCommercePromotionServiceImpl extends DefaultCommercePromotionService implements WholescriptsCommercePromotionService
{
	private static final String PROMOTION_DOES_NOT_EXIST = "Promotion '%s' Not valid";
	@Override
	public AbstractPromotionModel getPromotionForCode(String code) throws Exception
	{
		validateParameterNotNull(code, "Parameter code must not be null");
		final List<AbstractPromotionModel> promotions = getCommercePromotionDao().findPromotionForCode(code);
		final List<AbstractPromotionModel> filteredPromotions = new ArrayList<>();
		for(AbstractPromotionModel promotion : promotions) {
			if(promotion instanceof RuleBasedPromotionModel) {
				RuleBasedPromotionModel ruleModel = (RuleBasedPromotionModel) promotion;
				AbstractRuleEngineRuleModel droolRule = ruleModel.getRule();
				if(Objects.nonNull(droolRule)){
					PromotionSourceRuleModel sourceRule =(PromotionSourceRuleModel) droolRule.getSourceRule();
					if(Objects.nonNull(sourceRule)) {
						if(sourceRule.getStatus().equals(RuleStatus.PUBLISHED)) {
							filteredPromotions.add(promotion);
						}
					}
				}
			}
		}
		if(CollectionUtils.isEmpty(filteredPromotions)) {
			throw new PromotionInvalidException(String.format(PROMOTION_DOES_NOT_EXIST, sanitize(code)));
		}
		return filteredPromotions.get(0);
	}

	@SuppressWarnings("removal")
	protected static String sanitize(final String input)
	{
		return YSanitizer.sanitize(input);
	}

	@Override
	public AbstractPromotionModel getPromotionModel(String code)
	{
		validateParameterNotNull(code, "Parameter code must not be null");
		final List<AbstractPromotionModel> promotions = getCommercePromotionDao().findPromotionForCode(code);
		final List<AbstractPromotionModel> filteredPromotions = new ArrayList<>();
		for(AbstractPromotionModel promotion :promotions) {
			if(promotion instanceof ProductPromotionModel || promotion instanceof OrderPromotionModel) {
				filteredPromotions.add(promotion);
			}
		}
		validateIfSingleResult(filteredPromotions, format("Promotion with code '%s' not found!", code),
				format("Promotion code '%s' is not unique, %d promotion found!", code, Integer.valueOf(filteredPromotions.size())));

		return filteredPromotions.get(0);
	}

}
