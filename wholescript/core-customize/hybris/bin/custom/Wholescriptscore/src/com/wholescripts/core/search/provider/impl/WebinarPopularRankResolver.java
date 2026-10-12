package com.wholescripts.core.search.provider.impl;

import com.wholescripts.core.dao.WholescriptsCoreDao;
import com.wholescripts.core.model.WatchTrackerModel;
import com.wholescripts.core.model.WebinarModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import org.apache.commons.collections.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

public class WebinarPopularRankResolver extends AbstractValueResolver<WebinarModel, Object, Object> {
    @Resource(name = "wholescriptsCoreDao")
    private WholescriptsCoreDao wholescriptsCoreDao;
    @Override
    protected void addFieldValues(InputDocument inputDocument, IndexerBatchContext indexerBatchContext, IndexedProperty indexedProperty, WebinarModel webinarModel, ValueResolverContext<Object, Object> valueResolverContext) throws FieldValueProviderException {

        List<WatchTrackerModel> watchTrackerModels= wholescriptsCoreDao.getWatchTrackerDetails(webinarModel.getCode());
        if(CollectionUtils.isNotEmpty(watchTrackerModels))
        {
            long rank = watchTrackerModels.stream()
                    .mapToLong(WatchTrackerModel::getWeight)
                    .sum();
            inputDocument.addField(indexedProperty, rank, valueResolverContext.getFieldQualifier());


        }
        else {
            inputDocument.addField(indexedProperty, 0, valueResolverContext.getFieldQualifier());

        }

    }
}
