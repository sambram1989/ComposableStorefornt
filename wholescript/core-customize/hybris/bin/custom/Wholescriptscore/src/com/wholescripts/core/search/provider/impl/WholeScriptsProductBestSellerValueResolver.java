package com.wholescripts.core.search.provider.impl;
import com.wholescripts.core.model.WsPopularProductsModel;
import com.wholescripts.core.service.WholescriptsPopularCategoriesService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.product.CommerceProductService;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import de.hybris.platform.util.Config;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
public class WholeScriptsProductBestSellerValueResolver  extends AbstractValueResolver<ProductModel, Object, Object> {
    @Resource(name = "wholescriptsPopularCategoriesService")
    private WholescriptsPopularCategoriesService wholescriptsPopularCategoriesService;
    @Resource(name="commerceProductService")
    private CommerceProductService commerceProductService;
    private static final String BEST_SELLAR_COUNT = "wholescripts.bestSeller.products.count";
    @Override
    protected void addFieldValues(InputDocument inputDocument, IndexerBatchContext indexerBatchContext, IndexedProperty indexedProperty, ProductModel productModel, ValueResolverContext<Object, Object> valueResolverContext) throws FieldValueProviderException {
        Collection<CategoryModel> categories=commerceProductService.getSuperCategoriesExceptClassificationClassesForProduct(productModel);
        String count = Config.getParameter(BEST_SELLAR_COUNT);
        long productCount = 0;
        if(StringUtils.isNotBlank(count))
        {
            productCount = Long.parseLong(count);
        }
        if(CollectionUtils.isNotEmpty(categories))
        {
            for (final CategoryModel category : categories)
            {
                final List<WsPopularProductsModel> bestSellerProducts = new ArrayList<WsPopularProductsModel>();

                if(category != null)
                {
                    indexedProperty.setExportId(indexedProperty.getName()+"_" + category.getCode());
                    List<WsPopularProductsModel> popularProducts= wholescriptsPopularCategoriesService.getPopularProductsDetails(category.getCode());
                    bestSellerProducts.addAll(popularProducts.stream()
                            .sorted(Comparator.comparing(WsPopularProductsModel::getWeight).reversed())
                            .limit(productCount).collect(Collectors.toSet()));
                    if(CollectionUtils.isNotEmpty(bestSellerProducts))
                    {
                        inputDocument.addField(indexedProperty, getBestSellerValue(bestSellerProducts,productModel), valueResolverContext.getFieldQualifier());

                    }
                }
            }
        }
    }
    boolean  getBestSellerValue(List<WsPopularProductsModel> bestSellerProducts,ProductModel productModel)
    {
        boolean isBestSellarFound=false;
        for(final WsPopularProductsModel product : bestSellerProducts)
        {
            if(product!=null && product.getProductCode().equals(productModel.getCode()))
            {
                isBestSellarFound=true;
                break;
            }

        }
       return isBestSellarFound;
    }
}