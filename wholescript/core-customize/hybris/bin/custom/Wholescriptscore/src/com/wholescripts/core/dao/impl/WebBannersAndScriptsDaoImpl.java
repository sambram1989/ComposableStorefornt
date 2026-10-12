package com.wholescripts.core.dao.impl;

import com.wholescripts.core.dao.WebBannersAndScriptsDao;
import com.wholescripts.core.model.WebBannersAndScriptsModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.util.Config;

import javax.annotation.Resource;

/**
 * @author Sadiq
 */

public class WebBannersAndScriptsDaoImpl implements WebBannersAndScriptsDao {
    @Resource(name = "flexibleSearchService")
    private FlexibleSearchService flexibleSearchService;
    @Override
    public WebBannersAndScriptsModel getAllWebBannersAndScripts() {
        final String query = "select {w.pk} from {WebBannersAndScripts as w} where {w.uid} = ?uid";
        final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query);
        searchQuery.addQueryParameter("uid", Config.getParameter("uid.WebBannersAndScripts"));
        WebBannersAndScriptsModel result = flexibleSearchService.searchUnique(searchQuery);
        return result;
    }

}
