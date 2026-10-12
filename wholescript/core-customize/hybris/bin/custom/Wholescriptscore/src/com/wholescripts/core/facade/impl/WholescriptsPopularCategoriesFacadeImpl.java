/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.facade.impl;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;

import com.wholescripts.core.constants.WholescriptscoreConstants;
import com.wholescripts.core.facade.WholescriptsPopularCategoriesFacade;
import com.wholescripts.core.model.WholescriptsBrandModel;
import com.wholescripts.core.model.WsPopularCategoriesModel;
import com.wholescripts.core.model.WsPopularProductsModel;
import com.wholescripts.core.populators.WholescriptsBrandPopulator;
import com.wholescripts.core.service.WholescriptsCoreService;
import com.wholescripts.core.service.WholescriptsPopularCategoriesService;
import com.wholescripts.dto.WholescriptsBrandData;
import com.wholescripts.dto.WsPopularCategoriesData;


public class WholescriptsPopularCategoriesFacadeImpl implements WholescriptsPopularCategoriesFacade
{
	private static final String WHOLESCRIPTS_POPULAR_BRANDS_COUNT = "wholescripts.popular.brands.count";

	private static final String WHOLESCRIPTS_POPULAR_CATEGORY_RESTRICTED_CODE = "wholescripts.popular.category.restrictedCode";
	private static final String WHOLESCRIPTS_POPULAR_PRODUCTS_MATERIAL_CODES = "wholescripts.popular.products.materialcodes";

	@Resource(name = "wholescriptsPopularCategoriesService")
	private WholescriptsPopularCategoriesService wholescriptsPopularCategoriesService;
	@Resource(name = "categoryService")
	private CategoryService categoryService;
	@Resource(name = "categoryConverter")
	private Converter<CategoryModel, CategoryData> categoryConverter;
	@Resource(name = "productConverter")
	private Converter<ProductModel, ProductData> productConverter;
	@Resource(name = "productService")
	private ProductService productService;
	@Resource(name = "brandPopulator")
	private WholescriptsBrandPopulator brandPopulator;
	@Resource(name = "wholescriptsCoreService")
	private WholescriptsCoreService wholescriptsCoreService;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Override
	public WsPopularCategoriesData getPopularCategoriesDetails(final String fields, final String type)
	{
		final WsPopularCategoriesData popularCategoriesData = new WsPopularCategoriesData();
		if (StringUtils.isNotEmpty(type) && StringUtils.equals(WholescriptscoreConstants.TEXT_CATEGORY, type))
		{
			final List<WsPopularCategoriesModel> popularCategories = wholescriptsPopularCategoriesService
					.getPopularCategoriesDetails();
			final List<CategoryData> topCategories = populateCategoryData(popularCategories);
			popularCategoriesData.setPopularCategories(topCategories);

			final List<CategoryData> popularByCategory = populatePopularByCategoryData(popularCategories);
			popularCategoriesData.setPopularByCategory(popularByCategory);
		}
		else
		{
			final List<WholescriptsBrandData> topBrands = populateBrandData();
			popularCategoriesData.setTopBrands(topBrands);
		}
		return popularCategoriesData;
	}

	private List<CategoryData> populatePopularByCategoryData(final List<WsPopularCategoriesModel> popularCategories)
	{
		final List<CategoryData> popularByCategory = new ArrayList<>();
		for (final WsPopularCategoriesModel category : popularCategories)
		{
			String excludedCategoryCode = configurationService.getConfiguration().getString(WHOLESCRIPTS_POPULAR_CATEGORY_RESTRICTED_CODE);
			if (excludedCategoryCode.equalsIgnoreCase(category.getCategoryCode())) {
				continue;
			}
			final CategoryModel categoryModel = categoryService.getCategoryForCode(category.getCategoryCode());
			final CategoryData categoryData = categoryConverter.convert(categoryModel);

			final List<WsPopularProductsModel> products = wholescriptsPopularCategoriesService
					.getPopularProductsDetails(category.getCategoryCode());
			final List<ProductData> productDatas = new ArrayList<>();
			for (final WsPopularProductsModel product : products) {
				final ProductModel productModel = productService.getProductForCode(product.getProductCode());
				// Filter by sapMaterialGroup
				String allowedSapMaterialGroups = configurationService.getConfiguration().getString(WHOLESCRIPTS_POPULAR_PRODUCTS_MATERIAL_CODES);
				String[] validMaterialGroups = allowedSapMaterialGroups.split(",");
				List<String> validMaterialGroupsList = Arrays.asList(validMaterialGroups);
				if (validMaterialGroupsList.contains(productModel.getSapMaterialGroup().getCode())) {
					final ProductData productData = productConverter.convert(productModel);
					productDatas.add(productData);
				}
			}
			categoryData.setProducts(productDatas);
			popularByCategory.add(categoryData);
		}
		return popularByCategory;
	}

	private List<CategoryData> populateCategoryData(final List<WsPopularCategoriesModel> categories)
	{
		final List<CategoryData> topCategories = new ArrayList<>();
		for (final WsPopularCategoriesModel category : categories)
		{
			// Exclude the category with code "catalog"
			String excludedCategoryCode = configurationService.getConfiguration().getString(WHOLESCRIPTS_POPULAR_CATEGORY_RESTRICTED_CODE);
			if (excludedCategoryCode.equalsIgnoreCase(category.getCategoryCode())) {
				continue;
			}
			final CategoryModel categoryModel = categoryService.getCategoryForCode(category.getCategoryCode());
			topCategories.add(categoryConverter.convert(categoryModel));
		}
		return topCategories;
	}

	private List<WholescriptsBrandData> populateBrandData()
	{
		final List<WholescriptsBrandData> topBrands = new ArrayList<>();
		final List<WholescriptsBrandModel> brands = wholescriptsCoreService.getTopBrands();
		final int topBrandsMaxCount = configurationService.getConfiguration().getInt(WHOLESCRIPTS_POPULAR_BRANDS_COUNT, 7);
		for (final WholescriptsBrandModel brandModel : brands)
		{
			if (Objects.nonNull(brandModel.getIsTopBrand()) && brandModel.getIsTopBrand())
			{
				final WholescriptsBrandData brandData = new WholescriptsBrandData();
				brandPopulator.populate(brandModel, brandData);
				topBrands.add(brandData);
			}
			if (topBrands.size() >= topBrandsMaxCount)
			{
				break;
			}
		}
		return topBrands;
	}

}
