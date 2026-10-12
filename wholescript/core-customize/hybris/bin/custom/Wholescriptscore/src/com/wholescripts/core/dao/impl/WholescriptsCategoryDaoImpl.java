package com.wholescripts.core.dao.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wholescripts.core.dao.WholescriptsCategoryDao;

public class WholescriptsCategoryDaoImpl implements WholescriptsCategoryDao {
	
	 private static final Logger LOG = LoggerFactory.getLogger(WholescriptsCategoryDaoImpl.class);
    @Resource(name = "catalogVersionService")
    private CatalogVersionService catalogVersionService;
    @Resource(name = "configurationService")
    private ConfigurationService configurationService;
    @Resource(name = "flexibleSearchService")
 	 private FlexibleSearchService flexibleSearchService;
    /**
     * Finds all {@link CategoryModel}s with the specific <url>url</url>.
     *
     * @param url the category url
     * @return all found {@link CategoryModel}s, or an empty collection if no category can be found by the
     * <url>url</url> is not an active session catalog version.
     */
    @Override
    public Collection<CategoryModel> findCategoriesByUrl(String url) {
        final StringBuilder query = new StringBuilder("SELECT {c.PK} FROM {Category! as c} where {c.catalogVersion} = ?catalogVersion AND {c.categoryUrl} = ?url");
        final Map<String, Object> queryParameters = new HashMap();
        queryParameters.put("catalogVersion", catalogVersionService.getSessionCatalogVersionsForCatalog(configurationService.getConfiguration().getString("wholescripts.product.catalog")));
        queryParameters.put("url",url);
        final FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(query.toString(), queryParameters);
        LOG.info("WholescriptsCategoryDaoImpl, findCategoriesByUrl " + fsQuery);
        final SearchResult<CategoryModel> searchRes = flexibleSearchService.search(fsQuery);
        LOG.info("WholescriptsCategoryDaoImpl, findCategoriesByUrl search result " + searchRes + " and result " + searchRes.getResult());
		  if (Objects.isNull(searchRes) || searchRes.getTotalCount() == 0) {
			  throw new UnknownIdentifierException(" Category is not found with url " + url);
		  }
		  return searchRes.getResult();
    }
}
