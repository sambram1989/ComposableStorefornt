/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.dataimport.batch.translator;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.impex.jalo.translators.AbstractValueTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.mediaconversion.MediaConversionService;
import de.hybris.platform.mediaconversion.enums.ConversionStatus;
import de.hybris.platform.servicelayer.media.MediaContainerService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.media.dao.MediaContainerDao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.common.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class WholescriptsMediaConversionTranslator extends AbstractValueTranslator {

	private static final Logger LOG = LoggerFactory.getLogger(WholescriptsMediaConversionTranslator.class);

	private static final String MEDIA_CONVERSION_GROUP = "ProductMediaConversionGroup";
	private static final String MEDIA_CATALOG = "Default";
	private static final String MEDIA_CATALOG_VERSION = "Staged";


	@Override
	public Object importValue(final String valueExpr, final Item toItem) throws JaloInvalidParameterException {

		if (StringUtils.isBlank(valueExpr)) {
			return null;
		}
		LOG.debug("valueExpr is {} and item is {} ", valueExpr, toItem);
		Collection<MediaModel> convertedMedia = new ArrayList();
		final MediaContainerModel mediaContainer = getMediaContainerForQualifier(valueExpr);
		if (mediaContainer != null) {
			final Collection<MediaModel> existingConvertedMedis = getMediaConversionService().getConvertedMedias(mediaContainer);
			if (CollectionUtil.isNotEmpty(existingConvertedMedis)) {
				getMediaConversionService().deleteConvertedMedias(mediaContainer);
			}
		}
		if (mediaContainer != null
				&& mediaContainer.getConversionGroup().getCode().equalsIgnoreCase(MEDIA_CONVERSION_GROUP)
				&& ConversionStatus.UNCONVERTED.equals(mediaContainer.getConversionStatus())) {
			LOG.debug("MediaContainer pk {} for value {} ", mediaContainer.getPk(), valueExpr);
			
			getMediaConversionService().convertMedias(mediaContainer);
			convertedMedia.addAll(getMediaConversionService().getConvertedMedias(mediaContainer)) ;
			LOG.debug("media converted successfully for value {} and size of converted media is {} ", valueExpr,
					convertedMedia.size());
			final MediaModel originalMedia = getMediaService().getMedia(valueExpr);
			if (originalMedia != null) {
				convertedMedia.add(originalMedia);
			}
		}
		return convertedMedia;
	}
	
	public MediaContainerModel getMediaContainerForQualifier(String qualifier) {
		MediaContainerModel mediaContainerModel = null;
		if (StringUtils.isNotBlank(qualifier)) {
			final CatalogVersionModel catalogVersion = getCatalogVersionService().getCatalogVersion(MEDIA_CATALOG, MEDIA_CATALOG_VERSION);
			List<MediaContainerModel> results = getMediaContainerDao().findMediaContainersByQualifier(qualifier);
			Optional<MediaContainerModel> mediaOptional = results.stream().filter(result -> result.getCatalogVersion().equals(catalogVersion)).findAny();
			if (mediaOptional.isPresent()) {
				mediaContainerModel = mediaOptional.get();
			}

		}
		return mediaContainerModel;
	}

	@Override
	public String exportValue(final Object var1) throws JaloInvalidParameterException {
		return null;
	}

	private MediaConversionService getMediaConversionService() {
		return (MediaConversionService) Registry.getApplicationContext().getBean("mediaConversionService");

	}

	private MediaContainerService getMediaContainerService() {
		return (MediaContainerService) Registry.getApplicationContext().getBean("mediaContainerService");
	}

	private MediaService getMediaService() {
		return (MediaService) Registry.getApplicationContext().getBean("mediaService");
	}
	
	private MediaContainerDao getMediaContainerDao() {
		return (MediaContainerDao) Registry.getApplicationContext().getBean("mediaContainerDao");
	}
	
	private CatalogVersionService getCatalogVersionService() {
		return (CatalogVersionService) Registry.getApplicationContext().getBean("catalogVersionService");
	}

}
