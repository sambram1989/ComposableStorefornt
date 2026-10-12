package com.wholescripts.core.populators;

import de.hybris.platform.commercefacades.search.data.SearchQueryData;
import de.hybris.platform.commercefacades.search.solrfacetsearch.converters.populator.SolrSearchQueryDecoderPopulator;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;
import de.hybris.platform.converters.Populator;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class WholescriptsSolrSearchQueryDecoderPopulator extends SolrSearchQueryDecoderPopulator implements Populator<SearchQueryData, SolrSearchQueryData> {
    private static final Logger LOG = Logger.getLogger(WholescriptsSolrSearchQueryDecoderPopulator.class);

    @Override
    public void populate(SearchQueryData source, SolrSearchQueryData target) {
        if (source == null)
        {
            return;
        }

        if (StringUtils.isNotEmpty(source.getValue()))
        {
            final List<SolrSearchQueryTermData> terms = new ArrayList<SolrSearchQueryTermData>();

            String query=source.getValue();
            boolean isQueryHasQuotes= query.startsWith("\"") && query.substring(0, query.length() - 1).endsWith("\"");
            final String[] split = source.getValue().split(":");
            if(isQueryHasQuotes)
            {
                if (split.length > 1)
                {
                    target.setFreeTextSearch(split[0]+ ":" + split[1]);
                }
                else if(split.length > 0)
                {
                    target.setFreeTextSearch(split[0]);
                }
            }
            else {
                if (split.length > 0)
                {
                    target.setFreeTextSearch(split[0]);
                }
                if (split.length > 1)
                {
                    target.setSort(split[1]);
                }

                for (int i = 2; (i + 1) < split.length; i += 2)
                {
                    final SolrSearchQueryTermData termData = new SolrSearchQueryTermData();
                    termData.setKey(split[i]);
                    try
                    {
                        termData.setValue(URLDecoder.decode(split[i + 1], "UTF-8"));
                    }
                    catch (final UnsupportedEncodingException e)
                    {
                        // UTF-8 is supported encoding, so it shouldn't come here
                        LOG.error("Solr search query URLdecoding failed.", e);
                    }
                    terms.add(termData);
                }
            }


            target.setFilterTerms(terms);
        }

        target.setFilterQueries(createSolrSearchFilterQueries(source));

        target.setSearchQueryContext(source.getSearchQueryContext());
    }
}
