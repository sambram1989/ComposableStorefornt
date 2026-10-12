package com.wholescripts.core.service;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cmsfacades.data.AbstractPageData;

public interface WholescriptsPageRenderingService {

    /**
     * Returns {@link AbstractPageData} object based on pageLabelOrId or url.
     *
     * @param pageTypeCode
     *           the page type
     * @param pageLabelOrId
     *           the page label or id. This field is used only when the page type is ContentPage.
     * @param url
     *           the url depends on the page type. If the page type is ProductPage then the url should be a product
     *           url. If the page type is CategoryPage then the url should be a category url. If the page type is
     *           CatalogPage then the url should be a catalog page.
     * @return the {@link AbstractPageData} object.
     * @throws CMSItemNotFoundException
     *            if the page does not exists.
     */
    AbstractPageData getPageRenderingDataForUrl(final String pageTypeCode, final String pageLabelOrId, final String url)
            throws CMSItemNotFoundException;

}
