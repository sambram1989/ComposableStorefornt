/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;

import com.wholescripts.core.dao.WholescriptsMedPaxDao;
import com.wholescripts.core.model.MedPaxModel;
import com.wholescripts.core.model.ReferralCodesModel;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;

import com.wholescripts.core.constants.WholescriptscoreConstants;
import com.wholescripts.core.model.RecommendationModel;
import com.wholescripts.dto.WsRecommendationData;
import com.wholescripts.dto.WsRecommendationEntryData;


/**
 *
 */
public class WholescriptsRecommendPopulator implements Populator<RecommendationModel, WsRecommendationData>
{

	@Resource(name = "wsRecommendEntryConverter")
	private Converter<AbstractOrderEntryModel, WsRecommendationEntryData> wsRecommendEntryConverter;

	@Resource(name = "commerceCommonI18NService")
	private CommerceCommonI18NService commerceCommonI18NService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "priceDataFactory")
	private PriceDataFactory priceDataFactory;

	@Resource(name = "wholescriptsMedPaxDao")
	private WholescriptsMedPaxDao wholescriptsMedPaxDao;

	@Resource(name = "enumerationService")
	protected EnumerationService enumerationService;



	@Override
	public void populate(final RecommendationModel source, final WsRecommendationData target)
			throws ConversionException
	{
		final SimpleDateFormat dateFormat = new SimpleDateFormat(getDateFormat());
		target.setId(source.getCode());
		if (source.getUser() != null)
		{
		target.setPractitioner(source.getUser().getName());
		}
		if (source.getPatient() != null)
		{
		target.setPatient(source.getPatient().getName());
		target.setPatientUID(source.getPatient().getUid());
		target.setPatientDefaultB2BUnit(source.getPatient().getDefaultB2BUnit().getUid());

		Set<ReferralCodesModel> b2bUnitReferralCodes = source.getPatient().getDefaultB2BUnit().getReferralCodes();
		if (b2bUnitReferralCodes != null && !b2bUnitReferralCodes.isEmpty()) {
			List<ReferralCodesModel> referralCodesList = new ArrayList<>(b2bUnitReferralCodes);
			String firstReferralCode = referralCodesList.get(0).getReferralCode();
			String lastName = referralCodesList.get(0).getLastName();
			target.setReferralCode(firstReferralCode);
			target.setReferralName(lastName);
		}
		}

		target.setTotalUnitCount(calcTotalUnitCount(source));
		//This is for setting the status code
		target.setStatus(source.getRecommendationStatus().getCode());

		//This is for setting the status name or vale
		if(Objects.nonNull(source.getRecommendationStatus())) {
			String recommendationStatusName = enumerationService.getEnumerationName(source.getRecommendationStatus());
			target.setStatusName(recommendationStatusName);
		}
		if (source.getEntries() != null)
		{
			target.setRecommendationEntries(wsRecommendEntryConverter.convertAll(source.getEntries()));
		}
		target.setCreationDate(dateFormat.format(source.getCreationtime()));
		if (CollectionUtils.isNotEmpty(source.getComments()))
		{
			final CommentModel comment = source.getComments().get(source.getComments().size() - 1);
			target.setNotes(comment.getText());
		}

		if(source.getMsrpSubTotalPrice() != null){
			final PriceData priceData = priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(source.getMsrpSubTotalPrice()), "USD");
			target.setMsrpSubTotalPrice(priceData);
		}
		target.setIsEmailNotificationEnabled(source.getIsEmailNotificationEnabled());
		target.setIsSmsNotificationEnabled(source.getIsSmsNotificationEnabled());
		target.setExternalOrder(source.getExternalOrder());
		target.setSourceId(source.getSourceId());
		target.setSourceName(source.getSourceName());
	}

	public String getDateFormat()
	{
		return configurationService.getConfiguration().getString(WholescriptscoreConstants.DATE_FORMAT_CONFIG
				+ (commerceCommonI18NService.getCurrentLocale() != null && commerceCommonI18NService.getCurrentCurrency() != null
						? commerceCommonI18NService.getCurrentLocale().getLanguage()
								+ commerceCommonI18NService.getCurrentCurrency().getIsocode()
						: WholescriptscoreConstants.DATE_FORMAT_CONFIG_DEFAULT));
	}


	protected Integer calcTotalUnitCount(final RecommendationModel source) {
		int totalUnitCount = 0;
		final MedPaxModel medpax = wholescriptsMedPaxDao.getMedPaxForOwner(source);
		for (final AbstractOrderEntryModel orderEntryModel : source.getEntries()) {
			totalUnitCount += orderEntryModel.getQuantity().intValue();
		}
		if (Objects.nonNull(medpax)) {
			totalUnitCount += 1;
		}
		return Integer.valueOf(totalUnitCount);
	}
}
