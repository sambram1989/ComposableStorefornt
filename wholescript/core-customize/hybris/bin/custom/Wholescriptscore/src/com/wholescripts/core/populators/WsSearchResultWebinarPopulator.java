/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;

import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.converters.Populator;

import java.util.List;

import org.springframework.util.Assert;

import com.wholescripts.data.WholescriptsWebinarData;


/**
 */
public class WsSearchResultWebinarPopulator implements Populator<SearchResultValueData, WholescriptsWebinarData>
{
	@Override
	public void populate(final SearchResultValueData source, final WholescriptsWebinarData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		target.setCode(this.<String> getValue(source, "code"));
		target.setDescription(this.<String> getValue(source, "name"));
		target.setShortDescription(this.<String> getValue(source, "shortDescription"));
		target.setTitle(this.<String> getValue(source, "title"));
		target.setVideoStatus(this.<String> getValue(source, "videoStatus"));
		target.setDuration(this.<String> getValue(source, "duration"));
		target.setPresenterCode(this.getList(source, "presenterCode"));
		target.setPresenter(this.getPresenterList(source, "presenter"));
		target.setWebinarTag(this.getList(source, "webinarTag"));
		target.setVideoImage(this.<String> getValue(source, "videoImageUrl"));
		target.setVideo(this.<String> getValue(source, "video"));
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
