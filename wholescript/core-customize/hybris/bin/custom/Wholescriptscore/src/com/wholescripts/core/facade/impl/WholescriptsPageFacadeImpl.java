package com.wholescripts.core.facade.impl;

import com.wholescripts.core.facade.WholescriptsPageFacade;
import com.wholescripts.core.service.WholescriptsPageRenderingService;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.permissions.PermissionCachedCRUDService;
import de.hybris.platform.cms2.version.service.CMSVersionSessionContextProvider;
import de.hybris.platform.cmsfacades.data.AbstractPageData;
import de.hybris.platform.cmsfacades.pages.impl.DefaultPageFacade;
import de.hybris.platform.cmsfacades.rendering.PageRenderingService;

import javax.annotation.Resource;

public class WholescriptsPageFacadeImpl implements WholescriptsPageFacade  {

    @Resource(name = "wholescriptsPageRenderingService")
    private WholescriptsPageRenderingService wholescriptsPageRenderingService;
    @Resource(name = "cmsPermissionCachedCRUDService")
    private PermissionCachedCRUDService permissionCachedCRUDService;
    @Resource(name = "cmsVersionSessionContextProvider")
    private CMSVersionSessionContextProvider cmsVersionSessionContextProvider;
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
    @Override
    public AbstractPageData getPageforUrlData(String pageType, String pageLabelOrId, String url) throws CMSItemNotFoundException {
        permissionCachedCRUDService.initCache();
        cmsVersionSessionContextProvider.initCache();
        return wholescriptsPageRenderingService.getPageRenderingDataForUrl(pageType, pageLabelOrId, url);
    }

}
