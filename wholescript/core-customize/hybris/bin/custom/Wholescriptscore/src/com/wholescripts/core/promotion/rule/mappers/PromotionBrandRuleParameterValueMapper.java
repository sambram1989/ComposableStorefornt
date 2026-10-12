/*
 * Copyright (c) 2025 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.promotion.rule.mappers;

import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapper;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapperException;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.Objects;

import javax.annotation.Resource;

import com.wholescripts.core.dao.WholescriptsCoreDao;
import com.wholescripts.core.model.WholescriptsBrandModel;

/**
 *
 */
public class PromotionBrandRuleParameterValueMapper implements RuleParameterValueMapper<WholescriptsBrandModel>
{
	 @Resource(name = "wholescriptsCoreDao")
    private WholescriptsCoreDao wholescriptsCoreDao;
	
	@Override
	public WholescriptsBrandModel fromString(final String value)
	{
		ServicesUtil.validateParameterNotNull(value, "String value cannot be null");
		String promotionBrandCode = value;

		WholescriptsBrandModel promotionBrand = wholescriptsCoreDao.getBrandForCode(promotionBrandCode);
		if (Objects.isNull(promotionBrand)) {
			throw new RuleParameterValueMapperException(promotionBrandCode);

		} else {
			return promotionBrand;
		}
	}

	@Override
	public String toString(final WholescriptsBrandModel promotionBrandModel)
	{
		  ServicesUtil.validateParameterNotNull(promotionBrandModel, "Object cannot be null");
		  return promotionBrandModel.getCode();
	}
}
