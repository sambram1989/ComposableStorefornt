package com.wholescripts.core.populators;

import com.wholescripts.core.dao.WholescriptsCoreDao;
import com.wholescripts.core.model.WholescriptsBrandModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commercefacades.search.solrfacetsearch.converters.populator.SolrSearchStatePopulator;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

public class WholescriptsSolrSearchStatePopulator extends SolrSearchStatePopulator {

    @Resource(name = "wholescriptsCoreDao")
    private WholescriptsCoreDao wholescriptsCoreDao;
    @Override
    public void populate(SolrSearchQueryData source, SearchStateData target) {
        if (Objects.nonNull(source.getFilterTerms())) {
            final List<SolrSearchQueryTermData> terms = source.getFilterTerms();
            if (terms != null && !terms.isEmpty()) {
                for (final SolrSearchQueryTermData term : terms) {
                    if (StringUtils.isNotBlank(term.getKey()) && StringUtils.isNotBlank(term.getValue())) {

                       if(term.getKey().equalsIgnoreCase("categoryUrl"))
                       {
                          CategoryModel category= wholescriptsCoreDao.getCategoryByCategoryUrl(term.getValue());
                          if(Objects.nonNull(category))
                          {
                              target.setCategoryCode(category.getCode());

                          }
                       }
                       if(term.getKey().equalsIgnoreCase("brandUrl"))
                       {
                           WholescriptsBrandModel brand=wholescriptsCoreDao.getBrandCodeByBrandUrl(term.getValue());
                           if(Objects.nonNull(brand))
                           {
                               target.setBrandCode(brand.getCode());

                           }
                       }

                    }
                }
            }
        }

    }
}
