package com.wholescripts.core.search.provider.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class WholescriptsProductStatusValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider {

    private FieldNameProvider fieldNameProvider;

    protected FieldNameProvider getFieldNameProvider()
    {
        return fieldNameProvider;
    }

    @Required
    public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
    {
        this.fieldNameProvider = fieldNameProvider;
    }

    @Override
    public Collection<FieldValue> getFieldValues(IndexConfig indexConfig, IndexedProperty indexedProperty, final Object model) throws FieldValueProviderException {
        if (model instanceof ProductModel)
        {
            Boolean isFeaturedFlag=((ProductModel) model).getIsFeatured();
            Boolean isNewFlag=((ProductModel) model).getIsNew();

            return new ArrayList<FieldValue>(createFieldValue(isFeaturedFlag, isNewFlag, indexedProperty));
        }
        return Collections.emptyList();
    }
    protected List<FieldValue> createFieldValue(Boolean isFeaturedFlag, Boolean isNewFlag, final IndexedProperty indexedProperty)
    {
        final List<FieldValue> fieldValues = new ArrayList<FieldValue>();
        final Collection<String> fieldNames = fieldNameProvider.getFieldNames(indexedProperty, null);
        for (final String fieldName : fieldNames)
        {
            if(isFeaturedFlag)
            {
                fieldValues.add(new FieldValue(fieldName, "Featured"));

            }
            if(isNewFlag)
            {
                fieldValues.add(new FieldValue(fieldName, "New"));
            }
        }
        return fieldValues;
    }
}
