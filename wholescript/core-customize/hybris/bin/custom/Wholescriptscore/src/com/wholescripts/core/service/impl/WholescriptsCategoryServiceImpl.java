package com.wholescripts.core.service.impl;

import com.wholescripts.core.dao.WholescriptsCategoryDao;
import com.wholescripts.core.service.WholescriptsCategoryService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.impl.CatalogUtils;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import javax.annotation.Resource;
import java.util.Collection;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

public class WholescriptsCategoryServiceImpl implements WholescriptsCategoryService {

    @Resource(name = "wholescriptsCategoryDao")
    private WholescriptsCategoryDao wholescriptsCategoryDao;
    @Resource(name = "catalogVersionService")
    private CatalogVersionService catalogVersionService;

    @Override
    public CategoryModel getCategoryForUrl(final String url) {
        validateParameterNotNull(url, "Parameter 'url' was null.");
        final Collection<CategoryModel> categories = wholescriptsCategoryDao.findCategoriesByUrl(url);
        if (categories.isEmpty()) {
            throw new UnknownIdentifierException("Category with url '" + url + "' not found! (Active session catalogversions: "
                    + getCatalogVersionsString() + ")");
        } else if (categories.size() > 1) {
            throw new AmbiguousIdentifierException("Category with url '" + url + "' is not unique. " + categories.size()
                    + " categories found! (Active session catalogversions: " + getCatalogVersionsString() + ")");
        }
        return categories.iterator().next();
    }

    private String getCatalogVersionsString() {
        return CatalogUtils.getCatalogVersionsString(catalogVersionService.getSessionCatalogVersions());
    }


}
