/*
 * Copyright (c) 2025 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.strategies.impl;

import de.hybris.platform.promotionengineservices.compiler.strategies.ConditionResolutionStrategy;
import de.hybris.platform.promotionengineservices.dao.PromotionSourceRuleDao;
import de.hybris.platform.promotionengineservices.model.CatForPromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.CombinedCatsForRuleModel;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.ruledefinitions.CollectionOperator;
import de.hybris.platform.ruleengine.dao.RulesModuleDao;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengine.strategies.DroolsKIEBaseFinderStrategy;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;


public class PromoCatConditionResolutionStrategyImpl  implements ConditionResolutionStrategy {

	private ModelService modelService;
	private PromotionSourceRuleDao promotionSourceRuleDao;
	private RulesModuleDao rulesModuleDao;
	private DroolsKIEBaseFinderStrategy droolsKIEBaseFinderStrategy;

	public void getAndStoreParameterValues(RuleConditionData condition, PromotionSourceRuleModel rule, RuleBasedPromotionModel ruleBasedPromotion) {
		if (BooleanUtils.toBoolean(rule.getExcludeFromStorefrontDisplay())) {
			return; 
		}
		RuleParameterData categoriesOperatorParameter = (RuleParameterData)condition.getParameters().get("categories_operator");
		RuleParameterData categoriesParameter = (RuleParameterData)condition.getParameters().get("promotionCategories");
		if (categoriesOperatorParameter == null || categoriesParameter == null) {
			return;  
		}

		List<String> categoryCodes = (List<String>)categoriesParameter.getValue();
		CollectionOperator categoriesOperator = (CollectionOperator)categoriesOperatorParameter.getValue();

		if (CollectionUtils.isNotEmpty(categoryCodes)) {

			processCategoriesOperatorParameter(rule, ruleBasedPromotion, categoryCodes, categoriesOperator);

		} 
	}

	protected void processCategoriesOperatorParameter(PromotionSourceRuleModel rule, RuleBasedPromotionModel ruleBasedPromotion, List<String> categoryCodes, CollectionOperator categoriesOperator) {
		if (CollectionOperator.CONTAINS_ALL.equals(categoriesOperator)) {
			Integer conditionId = getNextConditionId(rule);
			for (String categoryCode : categoryCodes) {
				CombinedCatsForRuleModel combinedCatCodeForRule = (CombinedCatsForRuleModel)getModelService().create(CombinedCatsForRuleModel.class);
				combinedCatCodeForRule.setCategoryCode(categoryCode);
				combinedCatCodeForRule.setRule(rule);
				combinedCatCodeForRule.setConditionId(conditionId);
				combinedCatCodeForRule.setPromotion(ruleBasedPromotion);
				getModelService().save(combinedCatCodeForRule);
			} 
		} else {
			for (String categoryCode : categoryCodes) {
				CatForPromotionSourceRuleModel catCodeForRule = (CatForPromotionSourceRuleModel)getModelService().create(CatForPromotionSourceRuleModel.class);
				catCodeForRule.setCategoryCode(categoryCode);
				catCodeForRule.setRule(rule);
				catCodeForRule.setPromotion(ruleBasedPromotion);
				getModelService().save(catCodeForRule);
			} 
		} 
	}

	public void cleanStoredParameterValues(RuleCompilerContext context) {
		PromotionSourceRuleModel rule = (PromotionSourceRuleModel)context.getRule();
		String kieBaseName = getKIEBaseName(context.getModuleName());
		List<CatForPromotionSourceRuleModel> catForPromotionModels = getPromotionSourceRuleDao().findAllCatForPromotionSourceRule(rule, kieBaseName);
		List<CatForPromotionSourceRuleModel> cats = (List<CatForPromotionSourceRuleModel>)catForPromotionModels.stream().filter(cat -> isOfSameModule(cat, context.getModuleName())).collect(Collectors.toList());
		getModelService().removeAll(cats);
		List<CombinedCatsForRuleModel> combinedCatsForRule = getPromotionSourceRuleDao().findAllCombinedCatsForRule(rule, kieBaseName);
		getModelService().removeAll(combinedCatsForRule);
	}

	protected boolean isOfSameModule(CatForPromotionSourceRuleModel cat, String moduleName) {
		if (cat != null && cat.getPromotion() != null && cat.getPromotion().getRule() instanceof DroolsRuleModel) {
			DroolsRuleModel dr = (DroolsRuleModel)cat.getPromotion().getRule();
			return (dr.getKieBase() != null && dr.getKieBase().getKieModule() != null && moduleName
					.equals(dr.getKieBase().getKieModule().getName()));
		} 
		return false;
	}

	protected Integer getNextConditionId(PromotionSourceRuleModel rule) {
		Integer lastConditionId = getPromotionSourceRuleDao().findLastConditionIdForRule(rule);
		if (lastConditionId == null)
			return Integer.valueOf(1); 
		return Integer.valueOf(lastConditionId.intValue() + 1);
	}

	protected String getKIEBaseName(String moduleName) {
		DroolsKIEModuleModel droolsKIEModule = (DroolsKIEModuleModel)getRulesModuleDao().findByName(moduleName);
		DroolsKIEBaseModel kieBaseForKIEModule = getDroolsKIEBaseFinderStrategy().getKIEBaseForKIEModule(droolsKIEModule);
		return kieBaseForKIEModule.getName();
	}

	protected ModelService getModelService() {
		return this.modelService;
	}

	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}

	protected PromotionSourceRuleDao getPromotionSourceRuleDao() {
		return this.promotionSourceRuleDao;
	}

	public void setPromotionSourceRuleDao(PromotionSourceRuleDao promotionSourceRuleDao) {
		this.promotionSourceRuleDao = promotionSourceRuleDao;
	}

	protected RulesModuleDao getRulesModuleDao() {
		return this.rulesModuleDao;
	}

	public void setRulesModuleDao(RulesModuleDao rulesModuleDao) {
		this.rulesModuleDao = rulesModuleDao;
	}

	protected DroolsKIEBaseFinderStrategy getDroolsKIEBaseFinderStrategy() {
		return this.droolsKIEBaseFinderStrategy;
	}

	public void setDroolsKIEBaseFinderStrategy(DroolsKIEBaseFinderStrategy droolsKIEBaseFinderStrategy) {
		this.droolsKIEBaseFinderStrategy = droolsKIEBaseFinderStrategy;
	}
}
