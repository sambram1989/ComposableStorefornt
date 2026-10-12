package com.wholescripts.core.search.provider.impl;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.CategorySource;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import org.apache.commons.collections.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collection;


public class WholescriptsCategoryUrlValueResolver extends AbstractValueResolver<ProductModel, Object, Object> {
    @Resource(name="wholescriptsCategorySource")
    private CategorySource categorySource;


    @Override
    protected void addFieldValues(InputDocument inputDocument, IndexerBatchContext indexerBatchContext, IndexedProperty indexedProperty, ProductModel productModel, ValueResolverContext<Object, Object> valueResolverContext) throws FieldValueProviderException {

        final Collection<CategoryModel> categories = categorySource.getCategoriesForConfigAndProperty(null,
                indexedProperty, productModel);

        if (CollectionUtils.isNotEmpty(categories)) {
            for (final CategoryModel category : categories) {
                    inputDocument.addField(indexedProperty, category.getCategoryUrl(), valueResolverContext.getFieldQualifier());
            }
        }
    }
}