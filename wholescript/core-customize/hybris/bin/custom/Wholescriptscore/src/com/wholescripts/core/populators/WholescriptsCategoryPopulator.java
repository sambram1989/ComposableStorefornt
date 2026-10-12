package com.wholescripts.core.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.converters.populator.CategoryPopulator;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.Objects;

public class WholescriptsCategoryPopulator  extends CategoryPopulator {


    @Resource(name="imageConverter")
    private Converter<MediaModel, ImageData> imageConverter;

    @Override
    public void populate(CategoryModel source, CategoryData target) {

        if (Objects.nonNull(source.getPicture()))
        {
            target.setCategoryImage(imageConverter.convert(source.getPicture()));
        }
        target.setDescription(source.getDescription());
        target.setCategoryUrl(source.getCategoryUrl());
        if(StringUtils.isNotEmpty(source.getRetailName())){
            target.setRetailName(source.getRetailName());
        }

    }

}
