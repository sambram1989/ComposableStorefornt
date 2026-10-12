package com.wholescripts.core.dao;

import de.hybris.platform.category.model.CategoryModel;

import java.util.Collection;

public interface WholescriptsCategoryDao {

    /**
     * Finds all {@link CategoryModel}s with the specific <url>url</url>.
     *
     * @param url the category url
     * @return all found {@link CategoryModel}s, or an empty collection if no category can be found by the
     * <url>url</url> is not an active session catalog version.
     */
    Collection<CategoryModel> findCategoriesByUrl(String url);

}
