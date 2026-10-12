package com.wholescripts.core.strategies.impl;

import com.wholescripts.core.model.WatchTrackerModel;
import com.wholescripts.core.service.WholescriptsCoreService;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.model.SolrIndexModel;
import de.hybris.platform.solrfacetsearch.search.FacetSearchException;
import de.hybris.platform.solrfacetsearch.search.FacetValueField;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResult;
import de.hybris.platform.solrfacetsearch.search.context.FacetSearchContext;
import de.hybris.platform.solrfacetsearch.search.impl.DefaultFacetSearchStrategy;
import de.hybris.platform.solrfacetsearch.search.impl.SearchQueryConverterData;
import de.hybris.platform.solrfacetsearch.search.impl.SearchResultConverterData;
import de.hybris.platform.solrfacetsearch.solr.Index;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProvider;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.util.IOUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Objects;

public class WholescriptsWebinarFacetSearchStrategy extends DefaultFacetSearchStrategy {
    private static final Logger LOG = Logger.getLogger(WholescriptsWebinarFacetSearchStrategy.class);

    @Resource(name = "b2bCustomerService")
    private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;

    @Resource(name = "wholescriptsCoreService")
    private WholescriptsCoreService wholescriptsCoreService;

    public SearchResult search(SearchQuery searchQuery, Map<String, String> searchHints) throws FacetSearchException {

        checkQuery(searchQuery);
        SolrClient solrClient = null;

        SearchResult searchResult;
        try {
            FacetSearchConfig facetSearchConfig = searchQuery.getFacetSearchConfig();
            IndexedType indexedType = searchQuery.getIndexedType();
            FacetSearchContext facetSearchContext = getFacetSearchContextFactory().createContext(facetSearchConfig, indexedType, searchQuery);
            facetSearchContext.getSearchHints().putAll(searchHints);
            getFacetSearchContextFactory().initializeContext();
            this.checkContext(facetSearchContext);
            SolrIndexModel activeIndex;
            if (MapUtils.isNotEmpty(searchHints)) {
                boolean execute = Boolean.parseBoolean((String) searchHints.get("execute"));
                if (!execute) {
                    return null;
                }
            }

            SolrSearchProvider solrSearchProvider = getSolrSearchProviderFactory().getSearchProvider(facetSearchConfig, indexedType);
            activeIndex = getSolrIndexService().getActiveIndex(facetSearchConfig.getName(), indexedType.getIdentifier());
            Index index = solrSearchProvider.resolveIndex(facetSearchConfig, indexedType, activeIndex.getQualifier());
            solrClient = solrSearchProvider.getClient(index);
            SearchQueryConverterData searchQueryConverterData = new SearchQueryConverterData();
            searchQueryConverterData.setFacetSearchContext(facetSearchContext);
            searchQueryConverterData.setSearchQuery(searchQuery);
            SolrQuery solrQuery = (SolrQuery) getFacetSearchQueryConverter().convert(searchQueryConverterData);
            LOG.debug("SolrQuey inside WholescriptsWebinarFacetSearchStrategy: " + solrQuery);

            if (CollectionUtils.isNotEmpty(searchQuery.getFacetValues())) {
                boolean hasBothStatuses = searchQuery.getFacetValues().stream()
                        .filter(f -> "webinarStatus".equalsIgnoreCase(f.getField()))
                        .flatMap(f -> f.getValues().stream())
                        .collect(Collectors.toSet())
                        .containsAll(Arrays.asList("isWatched", "isNotWatched"));

                if (hasBothStatuses) {
                    // display all webinars
                }
                else
                {
                    for (FacetValueField filter : searchQuery.getFacetValues()) {
                        if (filter.getField().equalsIgnoreCase("webinarStatus")) {
                            B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
                            List<WatchTrackerModel> watchTrackerModels = wholescriptsCoreService.getWebinarsBasedOnStatus(customer.getUid(), true);
                            List<String> webinarCodes = new ArrayList<>();
                            if (CollectionUtils.isNotEmpty(watchTrackerModels)) {
                                webinarCodes = watchTrackerModels.stream()
                                        .map(WatchTrackerModel::getWebinarCode)
                                        .filter(Objects::nonNull)
                                        .distinct()
                                        .toList();

                            }
                            String webinarCodesList = null;
                            if (CollectionUtils.isNotEmpty(webinarCodes)) {
                                webinarCodesList = webinarCodes.stream()
                                        .map(code -> "\"" + code + "\"")
                                        .collect(Collectors.joining(" OR "));

                            }
                            if(filter.getValues().contains("isWatched") && webinarCodesList != null)
                            {
                                solrQuery.addFilterQuery("code_string:(" + webinarCodesList + ")");

                            }
                            else if(filter.getValues().contains("isNotWatched") && webinarCodesList != null){
                                solrQuery.addFilterQuery("!code_string:(" + webinarCodesList + ")");

                            }


                        }

                    }
                }

            }

            SolrRequest.METHOD method = this.resolveQueryMethod(facetSearchConfig);
            QueryResponse queryResponse = solrClient.query(index.getName(), solrQuery, method);
            SearchResultConverterData searchResultConverterData = new SearchResultConverterData();
            searchResultConverterData.setFacetSearchContext(facetSearchContext);
            searchResultConverterData.setQueryResponse(queryResponse);
            searchResult = (SearchResult) getFacetSearchResultConverter().convert(searchResultConverterData);
            LOG.debug("No of SearchResult inside WholescriptsWebinarFacetSearchStrategy: " + searchResult.getNumberOfResults());
            getFacetSearchContextFactory().getContext().setSearchResult(searchResult);
            getFacetSearchContextFactory().destroyContext();

        } catch (SolrServerException | IOException | RuntimeException | SolrServiceException ex) {
            getFacetSearchContextFactory().destroyContext(ex);
            throw new FacetSearchException(ex.getMessage(), ex);
        } finally {
            IOUtils.closeQuietly(solrClient);
        }

        return searchResult;
    }
}
