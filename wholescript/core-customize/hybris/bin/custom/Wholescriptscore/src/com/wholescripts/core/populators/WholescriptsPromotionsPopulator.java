/*
 * Copyright (c) 2025 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.PromotionData;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.wholescripts.core.dao.WholescriptsCoreDao;
import com.wholescripts.core.model.WholescriptsBrandModel;
import com.wholescripts.core.service.WholescriptsCommercePromotionService;

/**
 *
 */
public class WholescriptsPromotionsPopulator implements Populator<AbstractPromotionModel, PromotionData>
{
	@Resource(name="imageConverter")
	private Converter<MediaModel, ImageData> imageConverter;

	@Resource(name = "productModelUrlResolver")
	private UrlResolver<ProductModel> productModelUrlResolver;
	@Resource(name = "wholescriptsCommercePromotionService")
	private WholescriptsCommercePromotionService wholescriptsCommercePromotionService;
	@Resource(name="productService")
	private ProductService productService;
	@Resource(name = "wholescriptsCoreDao")
   private WholescriptsCoreDao wholescriptsCoreDao;
	@Resource(name = "categoryService")
	private CategoryService categoryService;

	@Override
	public void populate(AbstractPromotionModel source, PromotionData target)
	{
		try
		{
			final AbstractPromotionModel promotion =wholescriptsCommercePromotionService.getPromotionForCode(source.getCode());
			target.setEnabled(true);
			if(Objects.isNull(promotion)) {
				target.setEnabled(false);
			}
			target.setTitle(source.getTitle());
			if(source instanceof ProductPromotionModel) {
				ProductPromotionModel productPromo = (ProductPromotionModel) source;
				if(Objects.nonNull(productPromo.getProductBanner())) {
					target.setProductBanner(imageConverter.convert(productPromo.getProductBanner()));
				}
			}
			if(Objects.nonNull(source.getPromotionBanner())) {
				target.setPromotionBanner(imageConverter.convert(source.getPromotionBanner()));
			}

			if(StringUtils.isNotEmpty(source.getColorBackground())) {
				target.setColorBackground(source.getColorBackground());
			}
			if(Objects.nonNull(source.getShowOnBanners())) {
            target.setShowOnBanners(source.getShowOnBanners());
			}
			if(Objects.nonNull(source.getShowOnPLP())) {
            target.setShowOnPLP(source.getShowOnPLP());
			}
			if(StringUtils.isNotEmpty(source.getColorText())) {
				target.setColorText(source.getColorText());
			}
			target.setRedirectUrl("/catalog");
			if(promotion instanceof RuleBasedPromotionModel) {

				RuleBasedPromotionModel ruleModel = (RuleBasedPromotionModel) promotion;
				AbstractRuleEngineRuleModel droolRule = ruleModel.getRule();
				PromotionSourceRuleModel sourceRule =(PromotionSourceRuleModel) droolRule.getSourceRule();
				target.setNonDiscountable(StringUtils.EMPTY);
				if(!sourceRule.getNonDiscountable()) {
					target.setNonDiscountable("query=Europe1PriceFactory_PPG:01");
				}
				JSONArray jsonArray = new JSONArray(sourceRule.getConditions());
				int i = 0;

				while (i < jsonArray.length()) {
					JSONObject rule = jsonArray.getJSONObject(i);
					JSONObject parameters = rule.getJSONObject("parameters");
					Iterator<String> keys = parameters.keys();

					while (keys.hasNext()) {
						String key = keys.next();

						switch (key) {
							case "promotionCategories":
								JSONArray categories = parameters.getJSONObject(key).getJSONArray("value");
								String promoCategoryUrl=  "/catalog/?query=promtionCategory:"+categories.getString(0);
								target.setRedirectUrl(promoCategoryUrl);
								break;

							case "promotionBrands":
								JSONArray value = parameters.getJSONObject(key).getJSONArray("value");
								String barndCode=  value.getString(0);
								WholescriptsBrandModel brand= wholescriptsCoreDao.getBrandForCode(barndCode);
								if(Objects.nonNull(brand)) {
									target.setRedirectUrl(StringUtils.isNotEmpty(brand.getBrandUrl()) ? brand.getBrandUrl() :"/catalog");	
								}
								break;

							case "categories":
								JSONArray category =  parameters.getJSONObject(key).getJSONArray("value");
								CategoryModel categoryModel=  categoryService.getCategoryForCode(category.getString(0).split(":")[0]);
								if(Objects.nonNull(categoryModel)) {
									target.setRedirectUrl(StringUtils.isNotEmpty(categoryModel.getCategoryUrl()) ? categoryModel.getCategoryUrl() :"/catalog");
								}
								break;  
							case "products":
								JSONArray product =  parameters.getJSONObject(key).getJSONArray("value");
								if(Objects.nonNull(product)) {
									String productUrl = productModelUrlResolver.resolve(productService.getProductForCode(product.getString(0).split(":")[0]));
									target.setRedirectUrl(productUrl);
								}
								break;	
						}
					}

					i++; // Move to the next rule
				}
			}
		}
		catch (Exception e)
		{
			target.setEnabled(false);
		}


	}
}
