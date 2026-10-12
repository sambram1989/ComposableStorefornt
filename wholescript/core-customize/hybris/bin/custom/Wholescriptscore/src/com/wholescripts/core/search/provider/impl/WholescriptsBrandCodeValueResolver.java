package com.wholescripts.core.search.provider.impl;
import com.wholescripts.core.model.WholescriptsBrandModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import java.util.Objects;
public class WholescriptsBrandCodeValueResolver extends AbstractValueResolver<ProductModel, Object, Object> {
    @Override
    protected void addFieldValues(InputDocument inputDocument, IndexerBatchContext indexerBatchContext, IndexedProperty indexedProperty, ProductModel productModel, ValueResolverContext<Object, Object> valueResolverContext) throws FieldValueProviderException {
        WholescriptsBrandModel wholeScriptBrand= productModel.getWholescriptsBrand();
        if(Objects.nonNull(wholeScriptBrand))
        {
            inputDocument.addField(indexedProperty, wholeScriptBrand.getCode(), valueResolverContext.getFieldQualifier());
        }
    }
}