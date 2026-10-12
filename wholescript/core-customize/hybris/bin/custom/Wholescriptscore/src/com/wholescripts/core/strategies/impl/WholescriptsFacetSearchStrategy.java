package com.wholescripts.core.strategies.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchFilterQueryData;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.model.SolrIndexModel;
import de.hybris.platform.solrfacetsearch.search.FacetSearchException;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.SearchResult;
import de.hybris.platform.solrfacetsearch.search.context.FacetSearchContext;
import de.hybris.platform.solrfacetsearch.search.impl.DefaultFacetSearchStrategy;
import de.hybris.platform.solrfacetsearch.search.impl.SearchQueryConverterData;
import de.hybris.platform.solrfacetsearch.search.impl.SearchResultConverterData;
import de.hybris.platform.solrfacetsearch.solr.Index;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProvider;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.util.IOUtils;

import com.wholescripts.core.utils.WholescriptsCoreUtils;

public class WholescriptsFacetSearchStrategy extends DefaultFacetSearchStrategy {
	
	private static final Logger LOG = Logger.getLogger(WholescriptsFacetSearchStrategy.class);
	
	@Resource(name = "b2bCustomerService")
   private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;
	 
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
            boolean execute = Boolean.parseBoolean((String)searchHints.get("execute"));
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
         SolrQuery solrQuery = (SolrQuery)getFacetSearchQueryConverter().convert(searchQueryConverterData);
         LOG.debug("SolrQuey inside WholescriptsFacetSearchStrategy: " + solrQuery);
         
         if(StringUtils.isNotBlank(getCurrentCustomerSapId())) {
         	 solrQuery.addFilterQuery("!customerProdRestriction_string_mv:" + getCurrentCustomerSapId());
         	 solrQuery.addFilterQuery("!customerProductGroupRestriction_string_mv:" + getCurrentCustomerSapId());
         	
         }
         
			if (StringUtils.isNotBlank(getCurrentCustomerClass())) {
				solrQuery.addFilterQuery("!customerClassProductRestriction_string_mv:" + getCurrentCustomerClass());
				solrQuery.addFilterQuery("!brandCustomerClassRestriction_string_mv:" + getCurrentCustomerClass());

			}

         SolrRequest.METHOD method = this.resolveQueryMethod(facetSearchConfig);
         QueryResponse queryResponse = solrClient.query(index.getName(), solrQuery, method);
         SearchResultConverterData searchResultConverterData = new SearchResultConverterData();         
         searchResultConverterData.setFacetSearchContext(facetSearchContext);
         searchResultConverterData.setQueryResponse(queryResponse);
         searchResult = (SearchResult)getFacetSearchResultConverter().convert(searchResultConverterData);
         LOG.debug("No of SearchResult inside WholescriptsFacetSearchStrategy: " + searchResult.getNumberOfResults());
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
	
	private String getCurrentCustomerSapId() {
		
		final B2BCustomerModel customerModel = b2bCustomerService.getCurrentB2BCustomer();
		String accountNumber = null;

		if (customerModel != null && customerModel.getDefaultB2BUnit()!= null) {
			
			accountNumber = WholescriptsCoreUtils.getAccountNumberForDefaultB2bUnitWithoutFormat(customerModel.getDefaultB2BUnit().getUid())
					.replaceFirst("^0+", "");
		}
		return accountNumber;
		
	}
	
	private String getCurrentCustomerClass() {
		
		final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
		String customerClass = null;
		
		if (customer != null && customer.getDefaultB2BUnit() != null && customer.getDefaultB2BUnit().getSapCustomerClassification() != null ) {
			
			customerClass = customer.getDefaultB2BUnit().getSapCustomerClassification().getCode();
		}
		return customerClass;
		
	}

}
