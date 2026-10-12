package com.wholescripts.core.search.provider.impl;

import com.wholescripts.core.dao.WholescriptsProductsDao;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import de.hybris.platform.util.Config;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

public class WholescriptsMaterialProductFormResolver extends AbstractValueResolver<ProductModel, FeatureList, Object> {

    private static final String MATERIAL_PRODUCT_FORM = "wholescripts.pdp.productform.classification.code";

    private static final String BLUE_LABEL_CODE = "wholescripts.blue.label.code";
    private static final String MASTER_VARIANT="wholescripts.pdp.mastervariant.classification.code";


    @Resource(name = "classificationService")
    private ClassificationService classificationService;

    @Resource(name = "wholescriptsProductsDao")
    private WholescriptsProductsDao wholescriptsProductsDao;

    @Resource(name = "configurationService")
    private ConfigurationService configurationService;

    @Resource(name = "productService")
    private ProductService productService;

    @Override
    protected FeatureList loadData(IndexerBatchContext batchContext, Collection<IndexedProperty> indexedProperties, ProductModel product) {
        return classificationService.getFeatures(product);
    }

    @Override
    protected void addFieldValues(InputDocument document, IndexerBatchContext batchContext, IndexedProperty indexedProperty,
                                  ProductModel product, ValueResolverContext<FeatureList, Object> resolverContext) throws FieldValueProviderException {

        boolean isMedpax = Boolean.TRUE.equals(product.getIsMedpax());

        if (isMedpax) {
            // checking for blue label product associated with Medpax
            List<ProductModel> blueLabelProducts = wholescriptsProductsDao.getProductsByMedpaxCriteria(configurationService.getConfiguration().getString(BLUE_LABEL_CODE),
                    product.getSapBasicMaterial(),
                    product.getCatalogVersion());

            if (CollectionUtils.isNotEmpty(blueLabelProducts)) {
                for (ProductModel blueLabelProduct : blueLabelProducts) {
                    if(!blueLabelProduct.equals(product))
                    {
                        FeatureList featureList = classificationService.getFeatures(blueLabelProduct);
                        if (featureList != null) {
                            Feature feature = featureList.getFeatureByCode(Config.getParameter(MASTER_VARIANT));
                            if (feature != null && feature.getValue() != null) {
                                String masterVariant = (String) feature.getValue().getValue();
                                if (StringUtils.isNotBlank(masterVariant)) {
                                    blueLabelProduct= productService.getProductForCode(masterVariant);
                                }
                            }
                        }
                        String materialProductForm = extractMaterialProductForm(blueLabelProduct);
                        if (StringUtils.isNotBlank(materialProductForm)) {
                            document.addField(indexedProperty, materialProductForm);
                            return; // Stop after first valid form
                        }
                    }

                }
            }
        }

        // For normal products, extract only their own material product form (ignoring blue label products)
        String materialProductForm = extractMaterialProductForm(product);
        if (StringUtils.isNotBlank(materialProductForm)) {
            document.addField(indexedProperty, materialProductForm);
        }
    }

    //Extracts the material product form from the product's classification features.
    private String extractMaterialProductForm(ProductModel product) {
        FeatureList features = classificationService.getFeatures(product);
        return extractFeatureValue(features, configurationService.getConfiguration().getString(MATERIAL_PRODUCT_FORM));
    }


    // Extracts a feature value based on the feature code.
    private String extractFeatureValue(FeatureList featureList, String featureCode) {
        if (featureList == null || StringUtils.isBlank(featureCode)) {
            return null;
        }
        Feature feature = featureList.getFeatureByCode(featureCode);
        if (feature != null && CollectionUtils.isNotEmpty(feature.getValues())) {
            FeatureValue value = feature.getValues().get(0); // Get the first value
            if (value.getValue() instanceof ClassificationAttributeValueModel) {
                return ((ClassificationAttributeValueModel) value.getValue()).getName();
            }
        }
        return null;
    }
}
