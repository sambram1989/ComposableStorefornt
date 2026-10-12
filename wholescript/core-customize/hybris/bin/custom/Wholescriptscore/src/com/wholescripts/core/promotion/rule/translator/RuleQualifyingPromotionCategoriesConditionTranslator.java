/*
 * Copyright (c) 2025 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.promotion.rule.translator;

import com.google.common.collect.Lists;
import de.hybris.platform.ruledefinitions.AmountOperator;
import de.hybris.platform.ruledefinitions.CollectionOperator;
import de.hybris.platform.ruledefinitions.conditions.AbstractRuleConditionTranslator;
import de.hybris.platform.ruledefinitions.conditions.builders.IrConditions;
import de.hybris.platform.ruledefinitions.conditions.builders.RuleIrAttributeConditionBuilder;
import de.hybris.platform.ruledefinitions.conditions.builders.RuleIrAttributeRelConditionBuilder;
import de.hybris.platform.ruledefinitions.conditions.builders.RuleIrGroupConditionBuilder;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrExistsCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrNotCondition;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;

/**
 *
 */
public class RuleQualifyingPromotionCategoriesConditionTranslator extends AbstractRuleConditionTranslator
{

	@Override
	public RuleIrCondition translate(RuleCompilerContext context, RuleConditionData condition, RuleConditionDefinitionData conditionDefinition)
	{
		Map<String, RuleParameterData> conditionParameters = condition.getParameters();
		RuleParameterData categoriesOperatorParameter = (RuleParameterData)conditionParameters.get("categories_operator");
		RuleParameterData categoriesParameter = (RuleParameterData)conditionParameters.get("promotionCategories");

		if (this.verifyAllPresent(new Object[]{categoriesOperatorParameter, categoriesParameter})) {
			CollectionOperator categoriesOperator = (CollectionOperator)categoriesOperatorParameter.getValue();
			List<String> categories = (List)categoriesParameter.getValue();
			if (this.verifyAllPresent(new Object[]{categoriesOperator, categories})) {
				RuleIrGroupCondition irQualifyingCategoriesCondition = RuleIrGroupConditionBuilder.newGroupConditionOf(RuleIrGroupOperator.AND).build();
				this.addQualifyingCategoriesConditions(context,categoriesOperator, categories, irQualifyingCategoriesCondition);
				return irQualifyingCategoriesCondition;
			}      
		}
		return IrConditions.empty();
	}

	protected void addQualifyingCategoriesConditions(RuleCompilerContext context, CollectionOperator categoriesOperator, List<String> categories, RuleIrGroupCondition irQualifyingCategoriesCondition) {
		String orderEntryRaoVariable = context.generateVariable(OrderEntryRAO.class);
		String cartRaoVariable = context.generateVariable(CartRAO.class);
		List<RuleIrCondition> irConditions = Lists.newArrayList();

		if (CollectionUtils.isNotEmpty(categories)) {
			RuleIrGroupCondition categoriesCondition = RuleIrGroupConditionBuilder.newGroupConditionOf(RuleIrGroupOperator.OR).build();
			Iterator var11 = categories.iterator();
			while(var11.hasNext()) {            String category = (String)var11.next();
			categoriesCondition.getChildren().add(RuleIrAttributeConditionBuilder.newAttributeConditionFor(orderEntryRaoVariable).withAttribute("promoCategoryCodes").withOperator(RuleIrAttributeOperator.CONTAINS).withValue(category).build());
			}
			irConditions.add(categoriesCondition);
		}
		irConditions.add(RuleIrAttributeRelConditionBuilder.newAttributeRelationConditionFor(cartRaoVariable).withAttribute("entries").withOperator(RuleIrAttributeOperator.CONTAINS).withTargetVariable(orderEntryRaoVariable).build());
		this.evaluateCategoriesOperator(context, categoriesOperator, categories, irQualifyingCategoriesCondition, irConditions);
	}

	protected void evaluateCategoriesOperator(RuleCompilerContext context, CollectionOperator categoriesOperator, List<String> categories, RuleIrGroupCondition irQualifyingCategoriesCondition, List<RuleIrCondition> irConditions) {
		if (CollectionOperator.NOT_CONTAINS.equals(categoriesOperator)) {

			RuleIrNotCondition irNotProductCondition = new RuleIrNotCondition();
			irNotProductCondition.setChildren(irConditions);
			irQualifyingCategoriesCondition.getChildren().add(irNotProductCondition);
		} else {
			String orderEntryRaoVariable = context.generateVariable(OrderEntryRAO.class);
			irConditions.addAll(this.getConsumptionSupport().newProductConsumedCondition(context, orderEntryRaoVariable));
			irQualifyingCategoriesCondition.getChildren().addAll(irConditions);
			if (CollectionOperator.CONTAINS_ALL.equals(categoriesOperator)) {
				this.addContainsAllCategoriesConditions(context, categories, irQualifyingCategoriesCondition);
			}      
		}
	}

	protected void addContainsAllCategoriesConditions(RuleCompilerContext context, List<String> categories, RuleIrGroupCondition irQualifyingCategoriesCondition) {
		if (CollectionUtils.isNotEmpty(categories)) {
			String orderEntryRaoVariable = context.generateVariable(OrderEntryRAO.class);
			RuleIrGroupCondition categoriesCondition = RuleIrGroupConditionBuilder.newGroupConditionOf(RuleIrGroupOperator.AND).build();
			Iterator var6 = categories.iterator();
			while(var6.hasNext()) {           
				String category = (String)var6.next();
				categoriesCondition.getChildren().add(RuleIrAttributeConditionBuilder.newAttributeConditionFor(orderEntryRaoVariable).withAttribute("promoCategoryCodes").withOperator(RuleIrAttributeOperator.CONTAINS).withValue(category).build());
			}
			RuleIrExistsCondition irExistsCategoryCondition = new RuleIrExistsCondition();
			irExistsCategoryCondition.setChildren(Arrays.asList(categoriesCondition));
			irQualifyingCategoriesCondition.getChildren().add(irExistsCategoryCondition);
		}
	}
}
