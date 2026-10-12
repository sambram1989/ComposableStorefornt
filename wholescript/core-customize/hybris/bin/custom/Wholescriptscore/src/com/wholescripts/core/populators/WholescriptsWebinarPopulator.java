package com.wholescripts.core.populators;

import com.wholescripts.core.dao.WholescriptsCoreDao;
import com.wholescripts.core.model.PresenterModel;
import com.wholescripts.core.model.WebinarCategoryTagModel;
import com.wholescripts.core.model.WebinarModel;
import com.wholescripts.data.WholescriptsWebinarData;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cmsfacades.data.MediaData;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;
import com.wholescripts.data.WholescriptsPresenterData;
import com.wholescripts.data.WholescriptsWebinarCategoryTagData;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class WholescriptsWebinarPopulator implements Populator<WebinarModel, WholescriptsWebinarData> {
    private static final Logger LOG = Logger.getLogger(WholescriptsWebinarPopulator.class.getName());

    @Resource(name = "wholescriptsPresenterConverter")
    private Converter<PresenterModel, WholescriptsPresenterData> wholescriptsPresenterConverter;

    @Resource(name = "wholescriptsCoreDao")
    private WholescriptsCoreDao wholescriptsCoreDao;

    @Resource(name = "categoryService")
    private CategoryService categoryService;
    @Resource(name = "categoryConverter")
    private Converter<CategoryModel, CategoryData> categoryConverter;

    @Resource(name = "mediaModelConverter")
    private Converter<MediaModel, MediaData> mediaModelConverter;

    @Override
    public void populate(WebinarModel source, WholescriptsWebinarData target) throws ConversionException {

        target.setCode(source.getCode());
        target.setTitle(source.getTitle());
        target.setDescription(source.getDescription());
        target.setIsFeatured(source.getIsFeatured());
        target.setShortDescription(source.getShortDescription());
        target.setSponsorImageUrl(source.getSponsorImageUrl());
        target.setSponsorImageAltText(source.getSponsorImageAltText());
        target.setSponsorName(source.getSponsorName());
        if(source.getSponsorImage()!=null){
            target.setSponsorImage(mediaModelConverter.convert(source.getSponsorImage()));
        }

        if(source.getVideoImage()!=null)
        {
            target.setVideoImage(source.getVideoImage().getURL());

        }
        if (null != source.getVideo())
        {
            target.setVideo(source.getVideo().getURL());
        }
        target.setVideoUrl(source.getWebinarVideoUrl());
        if(StringUtils.isNotBlank(source.getWebinarCaptionsUrl()))
        {
            target.setVideoCaptionUrl(source.getWebinarCaptionsUrl());
        }
        List<WholescriptsWebinarCategoryTagData> webCategoryList = new ArrayList<>();
        if(Objects.nonNull(source.getWebinarCategoryTag()))
        {
            source.getWebinarCategoryTag().stream().forEach(wc -> {
                setWebCategoryTagData(wc,webCategoryList);
            });
        }

        target.setWebinarCategories(webCategoryList);
        List<PresenterModel> presenter=source.getPresenter();
        if(!CollectionUtils.isEmpty(presenter))
        {
            target.setPresenter(Collections.singletonList(presenter.get(0).getName()));
        }
        if(!CollectionUtils.isEmpty(source.getPresenter()))
        {
            target.setPresenters(wholescriptsPresenterConverter.convertAll(source.getPresenter()));

        }
        List<CategoryData> categoryList = new ArrayList<>();
        if(Objects.nonNull( source.getCategory()))
        {
            source.getCategory().stream().forEach(c -> {
                setCategoryData(c,categoryList);
            });
        }
        target.setCatalogCategories(categoryList);
        if(!CollectionUtils.isEmpty(source.getWebinarCategoryTag()))
        {
            List<WholescriptsWebinarData> webCatList = new ArrayList<>();
            List<WebinarModel> relatedWebinarsList=wholescriptsCoreDao.getRelatedWebinarsByWebCategory(source.getWebinarCategoryTag().stream().collect(Collectors.toList()),source.getCode());

            if(Objects.nonNull(relatedWebinarsList))
            {
                relatedWebinarsList.stream().forEach(w -> {
                    setRelatedWebinarsListData(w,webCatList);
                });
            }

            target.setRelatedWebinars(webCatList);

        }

    }

    private void setRelatedWebinarsListData(WebinarModel wCmodel, List<WholescriptsWebinarData> webCatList) {
        WholescriptsWebinarData webCatData=new WholescriptsWebinarData();
        webCatData.setCode(wCmodel.getCode());
        webCatData.setTitle(wCmodel.getTitle());
        webCatData.setSponsorName(wCmodel.getSponsorName());
        webCatData.setShortDescription(wCmodel.getShortDescription());
        if(wCmodel.getVideoImage()!=null)
        {
            webCatData.setVideoImage(wCmodel.getVideoImage().getURL());

        }
        webCatData.setVideoUrl(wCmodel.getWebinarVideoUrl());
        if(StringUtils.isNotBlank(wCmodel.getWebinarCaptionsUrl())){
            webCatData.setVideoCaptionUrl(wCmodel.getWebinarCaptionsUrl());
        }
        List<PresenterModel> presenter=wCmodel.getPresenter();
        final List<String> presenterNames = new ArrayList<String>();
        if(!CollectionUtils.isEmpty(presenter))
        {
            webCatData.setPresenter(Collections.singletonList(presenter.get(0).getName()));
        }
        webCatList.add(webCatData);
    }

    private void setCategoryData(CategoryModel source, List<CategoryData> categoryList) {
        try{
            final CategoryData categoryData=categoryConverter.convert(source);
            categoryList.add(categoryData);
        } catch (Exception e) {
            LOG.error(e);

        }


    }

    private void setWebCategoryTagData(WebinarCategoryTagModel wCmodel, List<WholescriptsWebinarCategoryTagData> webCategoryList) {
        WholescriptsWebinarCategoryTagData wcData=new WholescriptsWebinarCategoryTagData();
        wcData.setCode(wCmodel.getCode());
        wcData.setName(wCmodel.getName());
        webCategoryList.add(wcData);


    }
}
