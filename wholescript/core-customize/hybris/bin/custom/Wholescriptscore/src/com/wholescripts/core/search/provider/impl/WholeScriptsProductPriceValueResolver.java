package com.wholescripts.core.search.provider.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.product.PriceCriteria;
import de.hybris.platform.product.PriceService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.Qualifier;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import de.hybris.platform.solrfacetsearch.provider.impl.ValueProviderParameterUtils;
import de.hybris.platform.util.Config;
import org.apache.commons.collections.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class WholeScriptsProductPriceValueResolver extends AbstractValueResolver<ProductModel, Object, List<PriceInformation>> {
    public static final String OPTIONAL_PARAM = "optional";
    public static final boolean OPTIONAL_PARAM_DEFAULT_VALUE = true;
    private static final String RETAIL_GROUP = "wholescripts.retail.group";
    private static final String WHOLESALE_GROUP = "wholescripts.wholesale.group";
    private static final String GUEST_GROUP = "wholescripts.guest.group";

    @Resource(name="priceService")
    private PriceService priceService;

    @Override
    protected List<PriceInformation> loadQualifierData(final IndexerBatchContext batchContext,
                                                       final Collection<IndexedProperty> indexedProperties, final ProductModel product, final Qualifier qualifier)
    {
        return loadPriceInformations(indexedProperties, product);
    }
    @Override
    protected void addFieldValues(InputDocument inputDocument, IndexerBatchContext batchContext, IndexedProperty indexedProperty, ProductModel productModel, ValueResolverContext<Object, List<PriceInformation>> valueResolverContext) throws FieldValueProviderException
    {
        boolean hasPrice = false;

        Collection<PriceRowModel> priceRows = productModel.getEurope1Prices();
        String customerPriceGroup = Config.getParameter(RETAIL_GROUP);

        if (CollectionUtils.isNotEmpty(priceRows))
        {
            if(indexedProperty.getValueProviderParameter().equals(customerPriceGroup))
            {
                final Double retailsPrice= getPriceValue(indexedProperty, priceRows,customerPriceGroup);
                if (retailsPrice != null)
                {
                    hasPrice = true;
                    inputDocument.addField(indexedProperty, retailsPrice, valueResolverContext.getFieldQualifier());
                }

            }
            String wholeSalePriceGroup = Config.getParameter(WHOLESALE_GROUP);
            if(indexedProperty.getValueProviderParameter().equals(wholeSalePriceGroup))
            {
                final Double wholeSalePrice= getPriceValue(indexedProperty, priceRows,wholeSalePriceGroup);
                if (wholeSalePrice != null)
                {
                    hasPrice = true;
                    inputDocument.addField(indexedProperty, wholeSalePrice, valueResolverContext.getFieldQualifier());
                }
            }
            String mapPriceGroup = Config.getParameter(GUEST_GROUP);
            if(indexedProperty.getValueProviderParameter().equals(mapPriceGroup))
            {
                final Double mapPrice = getPriceValue(indexedProperty, priceRows,mapPriceGroup);
                if (mapPrice != null)
                {
                    hasPrice = true;
                    inputDocument.addField(indexedProperty, mapPrice, valueResolverContext.getFieldQualifier());
                }
            }

        }

        if (!hasPrice)
        {
            final boolean isOptional = ValueProviderParameterUtils.getBoolean(indexedProperty, OPTIONAL_PARAM,
                    OPTIONAL_PARAM_DEFAULT_VALUE);
            if (!isOptional)
            {
                throw new FieldValueProviderException("No value resolved for indexed property " + indexedProperty.getName());
            }
        }
    }
    protected Double getPriceValue(final IndexedProperty indexedProperty, final Collection<PriceRowModel> priceRows,final String customerPriceGroup)
            throws FieldValueProviderException
    {
        Double value = 0.0;

        PriceRowModel priceRow = priceRows.stream().filter(pr -> pr.getUg()!=null && customerPriceGroup.equalsIgnoreCase(pr.getUg().getCode())).findAny().orElse(null);
         if(priceRow!=null && priceRow.getPrice()!=null){
            return priceRow.getPrice();
        }

        return value;
    }



    protected List<PriceInformation> loadPriceInformations(final Collection<IndexedProperty> indexedProperties,
                                                           final ProductModel product)
    {
        final PriceCriteria priceCriteria = PriceCriteria.DefaultPriceCriteria.forProduct(product).forDate(new Date());
        return priceService.getPriceInformations(priceCriteria);
    }
}
