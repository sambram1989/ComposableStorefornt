/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.search.provider.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.CategorySource;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import com.wholescripts.core.model.CustomerMaterialInformationModel;

/**
 *
 */
public class WholescriptsPrivateLabelCategoryCodeValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider
{
	private CategorySource categorySource;
	private FieldNameProvider fieldNameProvider;
	private CommonI18NService commonI18NService;
	@Resource(name = "productService")
	ProductService productService;
	@Resource(name = "catalogVersionService")
	private CatalogVersionService catalogVersionService;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = "searchRestrictionService")
	private SearchRestrictionService searchRestrictionService;
	@Resource(name = "sessionService")
	private SessionService sessionService;

	protected CategorySource getCategorySource()
	{
		return categorySource;
	}

	@Required
	public void setCategorySource(final CategorySource categorySource)
	{
		this.categorySource = categorySource;
	}

	protected FieldNameProvider getFieldNameProvider()
	{
		return fieldNameProvider;
	}

	@Required
	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		CustomerMaterialInformationModel custMatInfoModel= (CustomerMaterialInformationModel) model;
		if(StringUtils.isNotEmpty(custMatInfoModel.getBlueLabel())) {
			try {
				final ProductModel product = sessionService.executeInLocalView(new SessionExecutionBody()
				{
					@Override
					public ProductModel execute()
					{
						searchRestrictionService.disableSearchRestrictions();
						final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(
								configurationService.getConfiguration().getString("wholescripts.product.catalog"),
								configurationService.getConfiguration().getString("wholescripts.product.catalog.version"));
						return  productService.getProductForCode(catalogVersion,
								String.format("%018d",Integer.valueOf(custMatInfoModel.getBlueLabel())));
					}
				});
				if(Objects.nonNull(product)) {
					final Collection<CategoryModel> categories  = sessionService.executeInLocalView(new SessionExecutionBody()
					{
						@Override
						public Collection<CategoryModel>  execute()
						{
							searchRestrictionService.disableSearchRestrictions();
							return  getCategorySource().getCategoriesForConfigAndProperty(indexConfig,
									indexedProperty, product);
						}
					});
					 
					if (categories != null && !categories.isEmpty())
					{
						final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

						if (indexedProperty.isLocalized())
						{
							final Collection<LanguageModel> languages = indexConfig.getLanguages();
							for (final LanguageModel language : languages)
							{
								for (final CategoryModel category : categories)
								{
									fieldValues.addAll(createFieldValue(category, language, indexedProperty));
								}
							}
						}
						else
						{
							for (final CategoryModel category : categories)
							{
								fieldValues.addAll(createFieldValue(category, null, indexedProperty));
							}
						}
						return fieldValues;
					}
				}
			}catch (Exception e) {
				// XXX: handle exception
			}
		}
		return Collections.emptyList();
	}

	protected List<FieldValue> createFieldValue(final CategoryModel category, final LanguageModel language,
			final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();

		if (language != null)
		{
			final Locale locale = i18nService.getCurrentLocale();
			Object value = null;
			try
			{
				i18nService.setCurrentLocale(getCommonI18NService().getLocaleForLanguage(language));
				value = getPropertyValue(category);
			}
			finally
			{
				i18nService.setCurrentLocale(locale);
			}

			final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty, language.getIsocode());
			for (final String fieldName : fieldNames)
			{
				fieldValues.add(new FieldValue(fieldName, value));
			}
		}
		else
		{
			final Object value = getPropertyValue(category);
			final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty, null);
			for (final String fieldName : fieldNames)
			{
				fieldValues.add(new FieldValue(fieldName, value));
			}
		}

		return fieldValues;
	}

	protected Object getPropertyValue(final Object model)
	{
		return getPropertyValue(model, "code");
	}

	protected Object getPropertyValue(final Object model, final String propertyName)
	{
		return modelService.getAttributeValue(model, propertyName);
	}
}

