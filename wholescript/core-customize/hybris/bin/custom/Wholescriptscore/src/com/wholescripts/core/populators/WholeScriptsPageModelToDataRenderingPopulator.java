package com.wholescripts.core.populators;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cmsfacades.data.AbstractPageData;
import de.hybris.platform.cmsfacades.rendering.populators.PageModelToDataRenderingPopulator;
import org.apache.commons.lang3.StringUtils;

public class WholeScriptsPageModelToDataRenderingPopulator extends PageModelToDataRenderingPopulator {
    @Override
    public void populate(AbstractPageModel pageModel, AbstractPageData targetData) {
        if (pageModel instanceof ContentPageModel && StringUtils.isNotEmpty(((ContentPageModel) pageModel).getKeywords())) {
            targetData.setKeywords(((ContentPageModel) pageModel).getKeywords());
            targetData.setProperties(((ContentPageModel) pageModel).getKeywords());
        }
    }
}
