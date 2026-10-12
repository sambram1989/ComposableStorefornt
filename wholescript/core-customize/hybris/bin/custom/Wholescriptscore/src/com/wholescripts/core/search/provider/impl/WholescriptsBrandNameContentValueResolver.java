package com.wholescripts.core.search.provider.impl;

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;

import java.util.Objects;

import com.wholescripts.core.model.ContentSearchModel;
import com.wholescripts.core.model.WholescriptsBrandModel;


public class WholescriptsBrandNameContentValueResolver extends AbstractValueResolver<ContentSearchModel, Object, Object> {

	@Override
	protected void addFieldValues(InputDocument inputDocument, IndexerBatchContext indexerBatchContext,
			IndexedProperty indexedProperty, final ContentSearchModel contentSearchModel, ValueResolverContext<Object, Object> valueResolverContext)
			throws FieldValueProviderException {
		
		WholescriptsBrandModel wholeScriptBrand = contentSearchModel.getBrand();
		if (Objects.nonNull(wholeScriptBrand)) {
			inputDocument.addField(indexedProperty, wholeScriptBrand.getName(), valueResolverContext.getFieldQualifier());
		}
	}
	
}