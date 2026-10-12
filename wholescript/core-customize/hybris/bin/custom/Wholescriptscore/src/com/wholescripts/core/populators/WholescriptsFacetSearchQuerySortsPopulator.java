package com.wholescripts.core.populators;

import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.solrfacetsearch.config.IndexedTypeSort;
import de.hybris.platform.solrfacetsearch.config.IndexedTypeSortField;
import de.hybris.platform.solrfacetsearch.search.OrderField;
import de.hybris.platform.solrfacetsearch.search.OrderField.SortOrder;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import de.hybris.platform.solrfacetsearch.search.impl.SearchQueryConverterData;
import de.hybris.platform.solrfacetsearch.search.impl.populators.FacetSearchQuerySortsPopulator;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;

import com.wholescripts.core.constants.WholescriptscoreConstants;


public class WholescriptsFacetSearchQuerySortsPopulator extends FacetSearchQuerySortsPopulator {

	@Resource(name = "sessionService")
   private SessionService sessionService;

	 @Override
    public void populate(final SearchQueryConverterData source, final SolrQuery target) {
        super.populate(source, target);

        final boolean scoreAlreadyAdded = target.getSorts().stream()
                .anyMatch(sortClause -> "score".equals(sortClause.getItem()));

        if (!scoreAlreadyAdded) {
            target.addSort("score", SolrQuery.ORDER.desc);
        }
    }

	 @Override
	 protected void buildSorts(final SearchQueryConverterData searchQueryConverterData, final List<PK> promotedItems, final List<OrderField> sorts) {

		 	final SearchQuery searchQuery = searchQueryConverterData.getSearchQuery();
	      final IndexedTypeSort currentSort = searchQueryConverterData.getFacetSearchContext().getNamedSort();
	      if (currentSort != null) {
	         if (currentSort.isApplyPromotedItems()) {
	            promotedItems.addAll(searchQuery.getPromotedItems());
	         }

	         final Iterator iterator = currentSort.getFields().iterator();

	         while(iterator.hasNext()) {
	            final IndexedTypeSortField indexedTypeSortField = (IndexedTypeSortField)iterator.next();

	            if (indexedTypeSortField.getFieldName().equalsIgnoreCase(WholescriptscoreConstants.BESTSELLER_SORT_FIELD)) {

	            	final String bestSellersCategory = sessionService.getAttribute(WholescriptscoreConstants.BESTSELLER_SESSION_FIELD);

	               if (StringUtils.isNotBlank(bestSellersCategory)) {
							final String sortFieldName = WholescriptscoreConstants.BESTSELLER_SORT_FIELD
									.concat(WholescriptscoreConstants.UNDERSCORE).concat(bestSellersCategory)
									.concat(WholescriptscoreConstants.UNDERSCORE)
									.concat(WholescriptscoreConstants.BESTSELLER_SORT_FIELD_TYPE);
	               	sorts.add(new OrderField(sortFieldName, indexedTypeSortField.isAscending() ? SortOrder.ASCENDING : SortOrder.DESCENDING));
	               } else {
		            	sorts.add(new OrderField(indexedTypeSortField.getFieldName(), indexedTypeSortField.isAscending() ? SortOrder.ASCENDING : SortOrder.DESCENDING));
		            }

	            } else {
	            	sorts.add(new OrderField(indexedTypeSortField.getFieldName(), indexedTypeSortField.isAscending() ? SortOrder.ASCENDING : SortOrder.DESCENDING));
	            }
	         }
	      } else {
	         promotedItems.addAll(searchQuery.getPromotedItems());
	         sorts.addAll(searchQuery.getSorts());
	      }

	   }
}
