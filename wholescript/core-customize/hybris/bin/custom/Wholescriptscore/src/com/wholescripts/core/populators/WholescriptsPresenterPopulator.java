package com.wholescripts.core.populators;

import com.wholescripts.core.dao.WholescriptsCoreDao;
import com.wholescripts.core.model.DosingModel;
import com.wholescripts.core.model.PresenterModel;
import com.wholescripts.core.model.WebinarModel;
import com.wholescripts.data.WholescriptsPresenterData;
import com.wholescripts.data.WholescriptsWebinarData;
import com.wholescripts.dto.DosingData;
import de.hybris.platform.cmsfacades.data.MediaData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class WholescriptsPresenterPopulator implements Populator<PresenterModel, WholescriptsPresenterData> {

    @Resource(name = "mediaModelConverter")
    private Converter<MediaModel, MediaData> mediaModelConverter;

    @Resource(name = "wholescriptsCoreDao")
    private WholescriptsCoreDao wholescriptsCoreDao;

    @Override
    public void populate(PresenterModel source, WholescriptsPresenterData target) throws ConversionException {
        target.setCode(source.getCode());
        target.setName(source.getName());
        target.setBiography(source.getBiography());
        target.setWebsiteUrl(source.getWebsiteUrl());
        if(source.getImage()!=null)
        {
            target.setImage(mediaModelConverter.convert(source.getImage()));

        }

        List<WebinarModel> relatedWebinars= wholescriptsCoreDao.getRelatedWebinarsByPresenter(source.getCode());
        List<WholescriptsWebinarData> webList = new ArrayList<>();
        if(Objects.nonNull(relatedWebinars))
        {
            relatedWebinars.stream().forEach(w -> {
                setRelatedWebinarsListData(w,webList);
            });
        }
        target.setRelatedWebinars(webList);


    }

    private void setRelatedWebinarsListData(WebinarModel wcModel, List<WholescriptsWebinarData> webList) {
        WholescriptsWebinarData webListData=new WholescriptsWebinarData();
        webListData.setCode(wcModel.getCode());
        webListData.setTitle(wcModel.getTitle());
        webListData.setShortDescription(wcModel.getShortDescription());
        if(wcModel.getVideoImage()!=null)
        {
            webListData.setVideoImage(wcModel.getVideoImage().getURL());

        }
        webListData.setVideoUrl(wcModel.getWebinarVideoUrl());
        if(StringUtils.isNotBlank(wcModel.getWebinarCaptionsUrl())){
            webListData.setVideoCaptionUrl(wcModel.getWebinarCaptionsUrl());
        }
        List<PresenterModel> presenter=wcModel.getPresenter();
        final List<String> presenterNames = new ArrayList<String>();
        if(!CollectionUtils.isEmpty(presenter))
        {
            webListData.setPresenter(Collections.singletonList(presenter.get(0).getName()));
        }
        webList.add(webListData);
    }
}
