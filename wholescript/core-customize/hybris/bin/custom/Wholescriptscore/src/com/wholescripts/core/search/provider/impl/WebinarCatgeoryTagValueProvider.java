/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.search.provider.impl;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
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

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.wholescripts.core.model.WebinarCategoryTagModel;
import com.wholescripts.core.model.WebinarModel;


public class WebinarCatgeoryTagValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider
{
	private FieldNameProvider fieldNameProvider;
	private CommonI18NService commonI18NService;

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
		if (model instanceof WebinarModel)
		{

			final List<WebinarCategoryTagModel> webinarCategoryTag = findWebinarTag((WebinarModel) model);
			if (webinarCategoryTag != null && CollectionUtils.isNotEmpty(webinarCategoryTag))
			{
				final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

				if (indexedProperty.isLocalized())
				{
					final Collection<LanguageModel> languages = indexConfig.getLanguages();
					for (final LanguageModel language : languages)
					{
						for (final WebinarCategoryTagModel webinarCategoryTagModel : webinarCategoryTag)
						{
							fieldValues.addAll(createFieldValue(webinarCategoryTagModel, language, indexedProperty));
						}
					}
				}
				else
				{
					for (final WebinarCategoryTagModel webinarCategoryTagModel : webinarCategoryTag)
					{
						fieldValues.addAll(createFieldValue(webinarCategoryTagModel, null, indexedProperty));
					}
				}
				return fieldValues;
			}
		}
		return Collections.emptyList();
	}

	protected List<WebinarCategoryTagModel> findWebinarTag(final WebinarModel webinar)
	{
		if (webinar != null)
		{
			final List<WebinarCategoryTagModel> webinarCategoryTag = webinar.getWebinarCategoryTag();
			if (webinarCategoryTag != null && CollectionUtils.isNotEmpty(webinarCategoryTag))
			{
				return webinarCategoryTag;
			}
		}
		return null;
	}

	protected List<FieldValue> createFieldValue(final WebinarCategoryTagModel category, final LanguageModel language,
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
		return getPropertyValue(model, "name");
	}

	protected Object getPropertyValue(final Object model, final String propertyName)
	{
		return modelService.getAttributeValue(model, propertyName);
	}
}
