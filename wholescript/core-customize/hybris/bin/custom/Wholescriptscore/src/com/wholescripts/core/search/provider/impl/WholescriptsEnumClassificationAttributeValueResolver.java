package com.wholescripts.core.search.provider.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.classification.ClassificationClassesResolverStrategy;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.classification.features.LocalizedFeature;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import de.hybris.platform.solrfacetsearch.provider.impl.ValueProviderParameterUtils;
import org.apache.log4j.Logger;
import de.hybris.platform.solrfacetsearch.provider.Qualifier;


public class WholescriptsEnumClassificationAttributeValueResolver extends AbstractValueResolver<ProductModel, FeatureList, Object> {

    @Resource(name = "classificationService")
    private ClassificationService classificationService;

    @Resource(name = "classificationClassesResolverStrategy")
    private ClassificationClassesResolverStrategy classificationClassesResolverStrategy;

    public static final String OPTIONAL_PARAM = "optional";
    public static final boolean OPTIONAL_PARAM_DEFAULT_VALUE = true;


    @Override
    protected FeatureList loadData(final IndexerBatchContext batchContext, final Collection<IndexedProperty> indexedProperties,
                                   final ProductModel product) {
        return loadFeatures(indexedProperties, product);
    }

    @Override
    protected void addFieldValues(final InputDocument document, final IndexerBatchContext batchContext,
                                  final IndexedProperty indexedProperty, final ProductModel model,
                                  final ValueResolverContext<FeatureList, Object> resolverContext) throws FieldValueProviderException {
        boolean hasValue = false;

        final FeatureList featureList = resolverContext.getData();
        if (featureList != null) {
            final List<Feature> features = getFeature(indexedProperty, featureList);
            if (features != null) {
                for (final Feature feature : features) {
                    if (feature != null) {
                        final List<FeatureValue> featureValues = getFeatureValues(feature, resolverContext.getQualifier());
                        if (featureValues != null && !featureValues.isEmpty()) {
                            for (final FeatureValue featureValue : featureValues) {
                                Object attributeValue = featureValue.getValue();

                                if (attributeValue instanceof ClassificationAttributeValueModel) {
                                    attributeValue = ((ClassificationAttributeValueModel) attributeValue)
                                            .getCode();
                                }

                                hasValue |= filterAndAddFieldValues(document, batchContext, indexedProperty, attributeValue,
                                        resolverContext.getFieldQualifier());
                            }
                        }
                    }
                }
            }
        }

        if (!hasValue) {
            final boolean isOptional = ValueProviderParameterUtils.getBoolean(indexedProperty, OPTIONAL_PARAM,
                    OPTIONAL_PARAM_DEFAULT_VALUE);
            if (!isOptional) {
                throw new FieldValueProviderException("No value resolved for indexed property " + indexedProperty.getName());
            }
        }
    }

//    protected FeatureList loadFeatures(final Collection<IndexedProperty> indexedProperties, final ProductModel productModel) {
//        final List<ClassAttributeAssignmentModel> classAttributeAssignments = new ArrayList<>();
//
//        for (final IndexedProperty indexedProperty : indexedProperties) {
//            if (indexedProperty.getClassAttributeAssignment()!=null) {
//                indexedProperty.getClassAttributeAssignments().forEach(classAttributeAssignment -> {
//                    final List<ClassAttributeAssignmentModel> matchingSubClassAttributeAssignments = classificationClassesResolverStrategy
//                            .getAllClassAttributeAssignments(classAttributeAssignment.getClassificationClass().getCategories()
//                                    .parallelStream().filter(ClassificationClassModel.class::isInstance)
//                                    .map(ClassificationClassModel.class::cast).collect(Collectors.toSet()))
//                            .stream().filter(subClassAttributeAssignment -> subClassAttributeAssignment.getClassificationAttribute()
//                                    .getCode().equals(classAttributeAssignment.getClassificationAttribute().getCode()))
//                            .collect(Collectors.toList());
//                    matchingSubClassAttributeAssignments.add(classAttributeAssignment);
//                    classAttributeAssignments.addAll(matchingSubClassAttributeAssignments);
//                });
//            }
//        }
//        return classificationService.getFeatures(productModel, classAttributeAssignments);
//    }
protected FeatureList loadFeatures(final Collection<IndexedProperty> indexedProperties, final ProductModel productModel) {
    final List<ClassAttributeAssignmentModel> classAttributeAssignments = new ArrayList<>();

    for (final IndexedProperty indexedProperty : indexedProperties) {
        // Check if the classAttributeAssignment is not null
        if (indexedProperty.getClassAttributeAssignment() != null) {
            // Directly add the classAttributeAssignment to the list
            classAttributeAssignments.add(indexedProperty.getClassAttributeAssignment());
        }
    }

    // Use the accumulated classAttributeAssignments to get features for the product model
    return classificationService.getFeatures(productModel, classAttributeAssignments);
}


    protected List<Feature> getFeature(final IndexedProperty indexedProperty, final FeatureList featureList) {
        final List<Feature> features = new ArrayList<>();

        // Retrieve the single ClassAttributeAssignmentModel directly
        final ClassAttributeAssignmentModel classAttributeAssignment = indexedProperty.getClassAttributeAssignment();
        if (classAttributeAssignment != null) {
            final List<ClassAttributeAssignmentModel> matchingSubClassAttributeAssignments = classificationClassesResolverStrategy
                    .getAllClassAttributeAssignments(
                            classAttributeAssignment.getClassificationClass().getCategories().stream()
                                    .filter(ClassificationClassModel.class::isInstance)
                                    .map(ClassificationClassModel.class::cast)
                                    .collect(Collectors.toSet())
                    )
                    .stream()
                    .filter(subClassAttributeAssignment -> subClassAttributeAssignment.getClassificationAttribute().getCode()
                            .equals(classAttributeAssignment.getClassificationAttribute().getCode()))
                    .collect(Collectors.toList());

            // Add the main class attribute assignment to the list
            matchingSubClassAttributeAssignments.add(classAttributeAssignment);

            // Add features based on the matching class attribute assignments
            matchingSubClassAttributeAssignments.forEach(matchingSubClassAttributeAssignment ->
                    features.add(featureList.getFeatureByAssignment(matchingSubClassAttributeAssignment))
            );
        }

        return features;
    }


    protected List<FeatureValue> getFeatureValues(final Feature feature, final Qualifier qualifier)
            throws FieldValueProviderException {
        Locale locale = null;

        if (qualifier != null) {
            locale = qualifier.getValueForType(Locale.class);
        }

        if (feature instanceof LocalizedFeature && locale != null) {
            return ((LocalizedFeature) feature).getValues(locale);
        } else {
            return feature.getValues();
        }
    }
}
