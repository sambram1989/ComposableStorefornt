/*
* * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.search.provider.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.impl.ModelAttributesValueResolver;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.wholescripts.core.model.ContentSearchModel;
import com.wholescripts.core.model.WholescriptsBrandModel;
import com.wholescripts.core.model.WholescriptsFaqModel;


public class WholescriptsSearchTermResolver extends ModelAttributesValueResolver<ContentSearchModel>
{
	private static final Logger LOG = Logger.getLogger(WholescriptsSearchTermResolver.class);
	private static final String CONTENT_TYPE_KEY = "searchTerm";
	private static final String CONTENT_TYPE_BRAND_VALUE = "code,name,description";
	private static final String CONTENT_TYPE_FAQ_VALUE = "uid,question,answer";
	private static final String CONTENT_TYPE_CATEGORY_VALUE = "code,name";
	private static final String CONTENT_TYPE_INTEGRATIONS_VALUE = "Integrations";

	@Override
	protected Object getAttributeValue(final IndexedProperty indexedProperty, final ContentSearchModel contentSearchModel,
			final String attributeName)
			throws FieldValueProviderException
	{
		return getModelAttributeValue(contentSearchModel);
	}

	protected Object getModelAttributeValue(final ContentSearchModel contentSearchModel)
	{
		final CategoryModel category = contentSearchModel.getCategory();
		final WholescriptsBrandModel brand = contentSearchModel.getBrand();
		final ContentSlotForPageModel slot = contentSearchModel.getIntegration();
		final WholescriptsFaqModel faq = contentSearchModel.getFaq();

		final StringBuilder valueBuilder = new StringBuilder();

		if (null != brand)
		{
			final List<String> attributes = Arrays.asList(CONTENT_TYPE_BRAND_VALUE.split(","));
			for (final String attributeName : attributes)
			{
				final String attributeValue = getModelService().getAttributeValue(brand, attributeName);
				if (attributeValue != null)
				{
					valueBuilder.append(attributeValue).append(" "); // Append value with a space separator (or any other desired delimiter)
				}
			}
			return valueBuilder.toString().trim(); // Return the accumulated string, trimmed of trailing spaces
		}

		if (null != faq)
		{
			final List<String> attributes = Arrays.asList(CONTENT_TYPE_FAQ_VALUE.split(","));
			for (final String attributeName : attributes)
			{
				final String attributeValue = getModelService().getAttributeValue(faq, attributeName);
				if (attributeValue != null)
				{
					valueBuilder.append(attributeValue).append(" "); // Append value with a space separator (or any other desired delimiter)
				}
			}
			return valueBuilder.toString().trim(); // Return the accumulated string, trimmed of trailing spaces
		}

		if (null != category)
		{
			final List<String> attributes = Arrays.asList(CONTENT_TYPE_CATEGORY_VALUE.split(","));
			for (final String attributeName : attributes)
			{
				final String attributeValue = getModelService().getAttributeValue(category, attributeName);
				if (attributeValue != null)
				{
					valueBuilder.append(attributeValue).append(" "); // Append value with a space separator (or any other desired delimiter)
				}
			}
			return valueBuilder.toString().trim(); // Return the accumulated string, trimmed of trailing spaces
		}

		return null;
	}
}
