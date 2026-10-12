/*
 * Copyright (c) 2025 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.dao.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.enums.ProductPriceGroup;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.promotionengineservices.dao.impl.DefaultPromotionSourceRuleDao;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.ruleengineservices.enums.RuleStatus;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;

import com.wholescripts.core.jalo.WholescriptsBrand;
import com.wholescripts.core.model.WholescriptsBrandModel;

/**
 *
 */
public class WholescriptsPromotionSourceRuleDao extends DefaultPromotionSourceRuleDao
{
   @Resource(name = "productService")
	private ProductService productService;
	
	 public List<RuleBasedPromotionModel> findPromotions(Collection<PromotionGroupModel> promotionGroups, String productCode, Set<String> categoryCodes) {
	     return findPromotions(promotionGroups, productCode, categoryCodes, new Date());
		 }
	 
	 protected List<RuleBasedPromotionModel> findPromotions(Collection<PromotionGroupModel> promotionGroups, String productCode, Set<String> categoryCodes, Date currentDate) {
		 ProductModel product= productService.getProductForCode(productCode);
		 Map<String, Object> params = new HashMap<>();
		 params.put("productCode", productCode);
		 params.put("statusPublished", RuleStatus.PUBLISHED);
		 params.put("promotionGroups", promotionGroups);
		 params.put("publishedPromotionStatuses", new HashSet(Collections.singletonList(RuleStatus.PUBLISHED)));
		 params.put("nowDate", Date.from(currentDate.toInstant().truncatedTo(ChronoUnit.MINUTES)));
		 params.put("nextMinuteDate", 
				 Date.from(currentDate.toInstant().plus(1L, ChronoUnit.MINUTES).truncatedTo(ChronoUnit.MINUTES)));
		 params.put("excludeFromStorefrontDisplay", Boolean.TRUE);
		 StringBuilder stringBuilder = new StringBuilder(200);
		 stringBuilder.append("SELECT {rel.promotion} as pr FROM {PromotionSourceRule as r}, {ProductForPromotionSourceRule as rel} WHERE {rel.rule} = {r.pk} AND {r.status} =?statusPublished AND {r.excludeFromStorefrontDisplay} !=?excludeFromStorefrontDisplay AND {r.website} IN (?promotionGroups) AND {rel.productCode} = ?productCode AND ( {r.startDate} <= ?nowDate OR {r.startDate} IS NULL) AND ( {r.endDate} >= ?nextMinuteDate OR {r.endDate} IS NULL) AND {r.status} IN (?publishedPromotionStatuses) AND {rel.promotion} IS NOT NULL");
		 if(Objects.nonNull(product.getEurope1PriceFactory_PPG()) && product.getEurope1PriceFactory_PPG().equals(ProductPriceGroup.valueOf("02"))) {
			 params.put(PromotionSourceRuleModel.NONDISCOUNTABLE, 1);
			 stringBuilder.append(" AND {r.nonDiscountable}=?nonDiscountable");
		 }
		 
		Set<String> promoCodes= product.getPromtionCategory().stream().map(p -> p.getCode()).collect(Collectors.toSet());
		if(CollectionUtils.isNotEmpty(promoCodes)) {
			categoryCodes.addAll(promoCodes);
		}
	   WholescriptsBrandModel promoBrand=product.getWholescriptsBrand();
	   if(Objects.nonNull(promoBrand)) {
	   	categoryCodes.add(promoBrand.getCode());
	   }
		if (CollectionUtils.isNotEmpty(categoryCodes)) {
			 params.put("categoryCodes", categoryCodes);
			 stringBuilder.insert(0, "SELECT x.pr FROM ( {{");
			 stringBuilder.append("}} UNION {{");
			 stringBuilder.append("SELECT {rel.promotion} as pr FROM {PromotionSourceRule as r}, {CatForPromotionSourceRule as rel} WHERE {rel.rule} = {r.pk} AND {r.status} =?statusPublished AND {r.excludeFromStorefrontDisplay} !=?excludeFromStorefrontDisplay AND {r.website} IN (?promotionGroups) AND {rel.categoryCode} IN (?categoryCodes) AND ( {r.startDate} <= ?nowDate OR {r.startDate} IS NULL) AND ( {r.endDate} >= ?nextMinuteDate OR {r.endDate} IS NULL) AND NOT EXISTS ({{ SELECT 1 FROM {ExcludedProductForRule} WHERE {rule} = {rel.rule} AND {productCode} = ?productCode }}) AND NOT EXISTS ({{ SELECT 1 FROM {ExcludedCatForRule} WHERE {rule} = {rel.rule} AND {categoryCode} IN (?categoryCodes) }}) ");
			 if(Objects.nonNull(product.getEurope1PriceFactory_PPG()) && product.getEurope1PriceFactory_PPG().equals(ProductPriceGroup.valueOf("02"))) {
				 params.put(PromotionSourceRuleModel.NONDISCOUNTABLE, 1);
				 stringBuilder.append(" AND {r.nonDiscountable}=?nonDiscountable");
			 }
			 stringBuilder.append("}} UNION {{");
			 stringBuilder.append("SELECT promotion as pr FROM ({{ SELECT {cc1.rule} as r1, {cc1.promotion} as promotion, {cc1.conditionId} as c1, count({cc1.categoryCode}) as count_cat1 FROM {PromotionSourceRule as r}, {CombinedCatsForRule as cc1} WHERE {cc1.categoryCode} in (?categoryCodes) AND {r.pk} = {cc1.rule} AND {r.status} =?statusPublished AND {r.excludeFromStorefrontDisplay} !=?excludeFromStorefrontDisplay AND {r.website} IN (?promotionGroups) AND ( {r.startDate} <= ?nowDate OR {r.startDate} IS NULL) AND ( {r.endDate} >= ?nextMinuteDate OR {r.endDate} IS NULL) GROUP BY {cc1.rule}, {cc1.promotion}, {cc1.conditionId} }}) a WHERE EXISTS ({{ SELECT {cc2.rule}, {cc2.promotion}, {cc2.conditionId}, count({cc2.categoryCode}) FROM {PromotionSourceRule as r}, {CombinedCatsForRule as cc2} WHERE {cc2.rule} = r1 AND {cc2.conditionId} = c1 AND {r.pk} = {cc2.rule} AND {r.status} =?statusPublished AND {r.excludeFromStorefrontDisplay} !=?excludeFromStorefrontDisplay AND {r.website} IN (?promotionGroups) AND ( {r.startDate} <= ?nowDate OR {r.startDate} IS NULL) AND ( {r.endDate} >= ?nextMinuteDate OR {r.endDate} IS NULL) GROUP BY {cc2.rule}, {cc2.promotion}, {cc2.conditionId} HAVING count({cc2.categoryCode}) = count_cat1 }}) AND NOT EXISTS ({{ SELECT 1 FROM {ExcludedProductForRule} WHERE {rule} = r1 AND {productCode} = ?productCode }}) AND NOT EXISTS ({{ SELECT 1 FROM {ExcludedCatForRule} WHERE {rule} = r1 AND {categoryCode} IN (?categoryCodes) }}) ");
			 stringBuilder.append("}}) x WHERE x.pr IS NOT NULL ");
			
		 } 
		 SearchResult<RuleBasedPromotionModel> searchResult = getFlexibleSearchService().search(stringBuilder.toString(), params);
		 return searchResult.getResult();
	 }
}
