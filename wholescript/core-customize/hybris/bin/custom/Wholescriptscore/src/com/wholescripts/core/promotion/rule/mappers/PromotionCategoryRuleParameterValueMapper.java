/*
 * Copyright (c) 2025 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.promotion.rule.mappers;

import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapper;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapperException;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.Objects;

import javax.annotation.Resource;

import com.wholescripts.core.dao.WholescriptsPromotionCategoryDao;
import com.wholescripts.core.model.PromotionCategoryModel;


/**
 *
 */
public class PromotionCategoryRuleParameterValueMapper implements RuleParameterValueMapper<PromotionCategoryModel>
{
	@Resource(name = "wholescriptsPromotionCategoryDao")
	private WholescriptsPromotionCategoryDao wholescriptsPromotionCategoryDao;
	
	@Override
	public PromotionCategoryModel fromString(final String value)
	{
		ServicesUtil.validateParameterNotNull(value, "String value cannot be null");
		String promoCategoryCode = value;

		PromotionCategoryModel promotionCategory = wholescriptsPromotionCategoryDao.getPromotionCategoryByCode(promoCategoryCode);

		if (Objects.isNull(promotionCategory)) {
			throw new RuleParameterValueMapperException(promoCategoryCode);

		} else {
			return promotionCategory;
		}
	}

	@Override
	public String toString(final PromotionCategoryModel promotionCategoryModel)
	{
		  ServicesUtil.validateParameterNotNull(promotionCategoryModel, "Object cannot be null");
		  return promotionCategoryModel.getCode();
	}

}
