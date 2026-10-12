/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.solr.common.util.CollectionUtil;

import com.wholescripts.core.model.WholescriptsFaqListModel;
import com.wholescripts.core.model.WholescriptsFaqModel;
import com.wholescripts.data.FaqData;
import com.wholescripts.data.FaqListData;
import com.wholescripts.dto.protocol.ProtocolWsDTO;

/**
 *
 */
public class WholescriptsFaqPopulators implements Populator<WholescriptsFaqListModel, FaqListData>
{
   @Resource(name = "wsFaqConverter")
	Converter<WholescriptsFaqListModel, FaqListData> wsFaqConverter;
	@Override
	public void populate(WholescriptsFaqListModel source, FaqListData target) throws ConversionException
	{
		target.setTitle(source.getTitle());
		target.setUid(source.getUid());
		setFaqDataList(source, target);
		if(CollectionUtil.isNotEmpty(source.getChild())) {
			target.setChild(wsFaqConverter.convertAll(source.getChild()));
		}		
	}

	private void setFaqDataList(WholescriptsFaqListModel source, FaqListData target)
	{
		List<FaqData> faqList= new ArrayList<>();	
		source.getFaqTopicList().forEach(faq-> {
			setFaqData(faqList,faq);});
		target.setFaqTopicList(faqList);
	}
	
	private void setFaqData(final List<FaqData> faqList, WholescriptsFaqModel faqModel) { 
		FaqData data = new FaqData();
		data.setUid(faqModel.getUid());
		data.setQuestion(faqModel.getQuestion());
		data.setAnswer(faqModel.getAnswer());
		faqList.add(data);
	}
}
