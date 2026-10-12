/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;

import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.converters.Populator;

import java.util.List;

import org.springframework.util.Assert;

import com.wholescripts.data.WholescriptsContentData;


/**
 */
public class WsSearchResultContentPopulator implements Populator<SearchResultValueData, WholescriptsContentData>
{
	@Override
	public void populate(final SearchResultValueData source, final WholescriptsContentData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		target.setCode(this.<String> getValue(source, "code"));
		target.setName(this.<String> getValue(source, "name"));
		target.setDescription(this.<String> getValue(source, "description"));
		target.setBrandImage(this.<String> getValue(source, "brandImage"));
		target.setBrandLogo(this.<String> getValue(source, "brandLogo"));
		target.setBrandURL(this.<String> getValue(source, "brandURL"));
		target.setBrandPriority(this.<String> getValue(source, "brandPriority"));
		target.setIsTopBrand(this.<Boolean> getValue(source, "isTopBrand"));
		target.setUid(this.<String> getValue(source, "uid"));
		target.setQuestion(this.<String> getValue(source, "question"));
		target.setAnswer(this.<String> getValue(source, "answer"));
		target.setThumbnail(this.<String> getValue(source, "thumbnail"));
		target.setCatgeoryPath(this.<String> getValue(source, "categoryPath"));
		target.setIntegrations(this.getList(source, "integrations"));
		target.setBrandCode(this.<String> getValue(source, "brandCode"));
		target.setBrandName(this.<String> getValue(source, "brandName"));
		target.setCategoryImage(this.<String> getValue(source, "picture"));
	}

	protected <T> T getValue(final SearchResultValueData source, final String propertyName)
	{
		if (source.getValues() == null)
		{
			return null;
		}
		return (T) source.getValues().get(propertyName);
	}

	protected List<String> getList(final SearchResultValueData source, final String propertyName)
	{
		if (source.getValues() == null)
		{
			return null;
		}
		return (List<String>) source.getValues().get(propertyName);
	}

	protected List<java.lang.String> getPresenterList(final SearchResultValueData source, final String propertyName)
	{
		if (source.getValues() == null)
		{
			return null;
		}
		return (List<java.lang.String>) source.getValues().get(propertyName);
	}

}
