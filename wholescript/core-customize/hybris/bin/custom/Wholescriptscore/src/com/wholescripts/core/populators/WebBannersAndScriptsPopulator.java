package com.wholescripts.core.populators;

import com.wholescripts.core.dao.ReferralCodesDao;
import com.wholescripts.core.model.ReferralCodesModel;
import com.wholescripts.core.model.WebBannersAndScriptsEntryModel;
import com.wholescripts.core.model.WebBannersAndScriptsModel;
import com.wholescripts.dto.WebBannersAndScriptsData;
import com.wholescripts.dto.WebBannersAndScriptsEntryData;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.cmsfacades.data.MediaData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.session.SessionService;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Sadiq
 */
public class WebBannersAndScriptsPopulator implements Populator<WebBannersAndScriptsModel, WebBannersAndScriptsData> {

    @Resource(name = "mediaService")
    private MediaService mediaService;
    @Resource(name = "referralCodesDao")
    private ReferralCodesDao referralCodesDao;
    @Resource(name = "b2bCustomerService")
    private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;
    @Resource(name = "mediaModelConverter")
    private Converter<MediaModel, MediaData> mediaModelConverter;
    @Resource(name = "sessionService")
    private SessionService sessionService;
    @Override
    public void populate(WebBannersAndScriptsModel source, WebBannersAndScriptsData target) throws ConversionException {
        String referralCode = sessionService.getAttribute("referralCode");
        String lastName = sessionService.getAttribute("lastName");
        if(StringUtils.isNotEmpty(referralCode) && StringUtils.isNotEmpty(lastName)){
           Optional<ReferralCodesModel> refModel = referralCodesDao.getReferralCodeModelForID(referralCode,lastName);
            if(refModel.isPresent()){
                target.setReferralLink(refModel.get().getReferralLink());
            }
        }
        List<WebBannersAndScriptsEntryData> entryList = new ArrayList<>();
        source.getEntries().stream().forEach(w -> {
            setWebBannerAndScriptEntryData(w,entryList);
        });
        target.setEntries(entryList);
        target.setUid(source.getUid());
    }

    private void setWebBannerAndScriptEntryData(WebBannersAndScriptsEntryModel entryModel, List<WebBannersAndScriptsEntryData> entryList){
        WebBannersAndScriptsEntryData entryData = new WebBannersAndScriptsEntryData();

        entryData.setEntryNumber(entryModel.getEntryNumber());
        entryData.setOptionId(entryModel.getOptionId());
        entryData.setScript(entryModel.getScript());
        if (entryModel.getImage() != null && StringUtils.isNotEmpty(entryModel.getImage().getCode())) {
            entryData.setImage(mediaModelConverter.convert(entryModel.getImage()));
        }
        entryData.setOptionName(entryModel.getOptionName());
        entryData.setWebPageViewName(entryModel.getWebPageViewName());
        entryData.setWebPageViewUrl(entryModel.getWebPageViewUrl());

        entryList.add(entryData);
    }
}