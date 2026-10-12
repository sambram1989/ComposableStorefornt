package com.wholescripts.core.service;

import de.hybris.platform.category.model.CategoryModel;

public interface WholescriptsCategoryService {
    /**
     * Retrieves the {@link CategoryModel} with the specific <url>url</url>.
     *
     * @param url the url of the to be found {@link CategoryModel}
     * @return found {@link CategoryModel}
     * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException   if no category with the specified url can be found.
     * @throws de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException if more than one category with the specified <url>url</url> and <url>catalogVersion</url> was
     *                                                                                 found.
     * @throws IllegalArgumentException                                                if parameter <url>url</url> is <url>null</url>.
     */
    CategoryModel getCategoryForUrl(String url);


}
