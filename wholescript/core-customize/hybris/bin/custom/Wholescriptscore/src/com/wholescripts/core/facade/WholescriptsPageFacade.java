package com.wholescripts.core.facade;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cmsfacades.data.AbstractPageData;

public interface WholescriptsPageFacade {
    /**
     * Returns {@link AbstractPageData} object based on pageLabelOrId or url. This should be used for rendering
     * purposes.
     *
     * @param pageType      the page type
     * @param pageLabelOrId the page label or id. This field is used only when the page type is ContentPage.
     * @param url           the url depends on the page type. If the page type is ProductPage then the url should be a product
     *                      url. If the page type is CategoryPage then the url should be a category url. If the page type is
     *                      CatalogPage then the url should be a catalog page.
     * @return the {@link AbstractPageData} object
     * @throws CMSItemNotFoundException when the page does not exists
     */
    AbstractPageData getPageforUrlData(String pageType, String pageLabelOrId, String url) throws CMSItemNotFoundException;


}
