/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.search.provider.impl;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.media.MediaModel;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import com.wholescripts.core.model.PresenterModel;
import com.wholescripts.core.model.WebinarModel;


public class WebinarPresenterValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider
{
	private FieldNameProvider fieldNameProvider;
	private CommonI18NService commonI18NService;
	private static String PRESENT_PAGE_URL = "presentPageUrl";
	private static String PRESENT_PAGE_URL_SUFFIX = "/presenters/";

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

			final List<PresenterModel> presenters = findPresenter((WebinarModel) model);
			if (presenters != null && CollectionUtils.isNotEmpty(presenters))
			{
				final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();

				if (indexedProperty.isLocalized())
				{
					final Collection<LanguageModel> languages = indexConfig.getLanguages();
					for (final LanguageModel language : languages)
					{
						for (final PresenterModel presenter : presenters)
						{
							fieldValues.addAll(createFieldValue(presenter, language, indexedProperty));
						}
					}
				}
				else
				{
					for (final PresenterModel presenter : presenters)
					{
						fieldValues.addAll(createFieldValue(presenter, null, indexedProperty));
					}
				}
				return fieldValues;
			}
		}
		return Collections.emptyList();
	}

	protected List<PresenterModel> findPresenter(final WebinarModel webinar)
	{
		if (webinar != null)
		{
			final List<PresenterModel> presenters = webinar.getPresenter();
			if (presenters != null && CollectionUtils.isNotEmpty(presenters))
			{
				return presenters;
			}
		}
		return null;
	}

	protected List<FieldValue> createFieldValue(final PresenterModel presenter, final LanguageModel language,
			final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();

		if (language != null)
		{
			final Locale locale = i18nService.getCurrentLocale();
			Object values = null;
			try
			{
				i18nService.setCurrentLocale(getCommonI18NService().getLocaleForLanguage(language));
				values = getPropertyValue(presenter);
			}
			finally
			{
				i18nService.setCurrentLocale(locale);
			}

			final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty, language.getIsocode());
			for (final String fieldName : fieldNames)
			{
				fieldValues.add(new FieldValue(fieldName, values));
			}
		}
		else
		{
			final Object values = getPropertyValue(presenter);
			final Collection<String> fieldNames = getFieldNameProvider().getFieldNames(indexedProperty, null);
			for (final String fieldName : fieldNames)
			{
				fieldValues.add(new FieldValue(fieldName, values));
			}
		}

		return fieldValues;
	}

	protected Object getPropertyValue(final PresenterModel model)
	{
		final Map<String, Object> values = new HashMap<String, Object>();
		values.put(PresenterModel.CODE, getPropertyValue(model, PresenterModel.CODE));
		values.put(PresenterModel.NAME, getPropertyValue(model, PresenterModel.NAME));
		values.put(PRESENT_PAGE_URL, PRESENT_PAGE_URL_SUFFIX + getPropertyValue(model, PresenterModel.CODE));
		final MediaModel presenterImage = getMediaValue(model, PresenterModel.IMAGE);
		if(null != presenterImage) {
			values.put(PresenterModel.IMAGE, presenterImage.getUrl());
		}
		final String valueString = values.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue())
				.collect(Collectors.joining(" , "));

		return valueString;
	}

	protected Object getPropertyValue(final Object model, final String propertyName)
	{
		return modelService.getAttributeValue(model, propertyName);
	}

	protected MediaModel getMediaValue(final Object model, final String propertyName)
	{
		return (MediaModel) modelService.getAttributeValue(model, propertyName);
	}

}
