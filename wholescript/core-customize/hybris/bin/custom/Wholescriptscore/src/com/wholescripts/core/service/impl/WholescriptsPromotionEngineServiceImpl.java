/*
 * Copyright (c) 2025 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service.impl;

import de.hybris.platform.commerceservices.model.promotions.PromotionOrderRestrictionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.promotionengineservices.model.AbstractRuleBasedPromotionActionModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.promotionengineservices.promotionengine.impl.DefaultPromotionEngineService;
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.promotions.model.AbstractPromotionActionModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.promotions.result.PromotionOrderResults;
import de.hybris.platform.ruleengine.RuleEvaluationResult;
import de.hybris.platform.ruleengine.exception.RuleEngineRuntimeException;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.RuleEngineResultRAO;
import de.hybris.platform.util.DiscountValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.collect.Lists;


/**
 *
 */
public class WholescriptsPromotionEngineServiceImpl extends DefaultPromotionEngineService
{

	@Override
	protected PromotionOrderResults updatePromotionsNotThreadSafe(final Collection<PromotionGroupModel> promotionGroups,
			final AbstractOrderModel order, final Date date)
	{
		List<PromotionResult> actionApplicationResults;
		cleanupAbstractOrder(order);


		try
		{
			final RuleEvaluationResult ruleEvaluationResult = evaluate(order, promotionGroups, date);
			if (!ruleEvaluationResult.isEvaluationFailed())
			{
				LinkedHashSet<AbstractRuleActionRAO> actions  =	new LinkedHashSet<>();
				RuleEngineResultRAO result = ruleEvaluationResult.getResult();
				for(AbstractRuleActionRAO action : result.getActions()) {
					for(PromotionOrderRestrictionModel restriction : order.getPromotionOrderRestrictions()) {
						RuleBasedPromotionModel ruleModel = (RuleBasedPromotionModel) restriction.getPromotion();
						if(Objects.nonNull(ruleModel)) {
							DroolsRuleModel drools= (DroolsRuleModel) ruleModel.getRule();
							if(Objects.nonNull(drools) && ruleModel.getCode().equals(action.getFiredRuleCode()) && drools.getCurrentVersion()) {
								actions.add(action);
							}
						}
					}
				}
				result.setActions(actions);
				final List<ItemModel> applyAllActionModels = getRuleActionService().applyAllActions(result);
				final List<PromotionResultModel> applyAllActionsPromotionResultModel = applyAllActionModels.stream()
						.filter(item -> item instanceof PromotionResultModel).map(item -> (PromotionResultModel) item)
						.collect(Collectors.toList());
				actionApplicationResults = getModelService().getAllSources(applyAllActionsPromotionResultModel, new ArrayList());
			}
			else
			{
				actionApplicationResults = Lists.newArrayList();
			}
		}
		catch (final RuleEngineRuntimeException rere)
		{
			actionApplicationResults = new ArrayList<>();

		}
		getModelService().refresh(order);
		return new PromotionOrderResults(JaloSession.getCurrentSession().getSessionContext(),
				(AbstractOrder) getModelService().getSource(order), actionApplicationResults, 0.0D);
	}
	
	protected void cleanupAbstractOrder(AbstractOrderModel cart) {
		Set<PromotionResultModel> promotionResultModels = cart.getAllPromotionResults();
		if (CollectionUtils.isEmpty(promotionResultModels)) {
			return; 
		}
		for (PromotionResultModel promoResultModel : promotionResultModels) {
			for (AbstractPromotionActionModel action : promoResultModel.getActions()) {
				if (action instanceof AbstractRuleBasedPromotionActionModel) {
					undoPromotionAction(action); 
				}
			} 
			getModelService().removeAll(promoResultModel.getConsumedEntries());
			getModelService().removeAll(promoResultModel.getActions());
			getModelService().remove(promoResultModel);

		} 
		for (AbstractOrderEntryModel entry : cart.getEntries()) {
			List<DiscountValue> newDVs = (List<DiscountValue>)entry.getDiscountValues().stream().filter(dv -> !getActionUtils().isActionUUID(dv.getCode())).collect(Collectors.toList());
			entry.setDiscountValues(newDVs);
		} 
		getModelService().refresh(cart);
		//recalculateCart(cart);
	}
}
