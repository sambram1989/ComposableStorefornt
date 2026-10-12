/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.search.provider.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.classification.ClassificationClassesResolverStrategy;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.classification.features.UnlocalizedFeature;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.Qualifier;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import de.hybris.platform.solrfacetsearch.provider.impl.ValueProviderParameterUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.wholescripts.core.model.CustomerMaterialInformationModel;


public class WholescriptsPrivateLabelDeliveryFormValueResolver  extends AbstractValueResolver<CustomerMaterialInformationModel, FeatureList, Object> {

   public static final String OPTIONAL_PARAM = "optional";
   public static final boolean OPTIONAL_PARAM_DEFAULT_VALUE = true;
   @Resource(name = "classificationService")
   private ClassificationService classificationService;
   @Resource(name = "classificationClassesResolverStrategy")
   private ClassificationClassesResolverStrategy classificationClassesResolverStrategy;
	@Resource(name = "productService")
	ProductService productService;
	@Resource(name = "catalogVersionService")
	private CatalogVersionService catalogVersionService;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = "searchRestrictionService")
	private SearchRestrictionService searchRestrictionService;
	@Resource(name = "sessionService")
	private SessionService sessionService;
	
	 @Override
    protected FeatureList loadData(final IndexerBatchContext batchContext, final Collection<IndexedProperty> indexedProperties, final CustomerMaterialInformationModel custMatInfoModel) {
       
			if(StringUtils.isNotEmpty(custMatInfoModel.getBlueLabel())) {
				try {
					final ProductModel product = sessionService.executeInLocalView(new SessionExecutionBody()
					{
						@Override
						public ProductModel execute()
						{
							searchRestrictionService.disableSearchRestrictions();
							final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(
									configurationService.getConfiguration().getString("wholescripts.product.catalog"),
									configurationService.getConfiguration().getString("wholescripts.product.catalog.version"));
							return  productService.getProductForCode(catalogVersion,
									String.format("%018d",Integer.valueOf(custMatInfoModel.getBlueLabel())));
						}
					});
				if(Objects.nonNull(product)) {
					 return loadFeatures(indexedProperties, product);
				}
				}catch (Exception e) {
				}
			}
			return null;
    }
	 
	@Override
	protected void addFieldValues(InputDocument inputDocument, IndexerBatchContext indexerBatchContext, IndexedProperty indexedProperty,
			CustomerMaterialInformationModel custMatInfoModel, ValueResolverContext<FeatureList, Object> resolverContext) throws FieldValueProviderException
	{
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
                              final Object attributeValue = featureValue.getValue();
                              String attributeValueStr = ((ClassificationAttributeValueModel) attributeValue).getName();
                              if (StringUtils.isNotEmpty(attributeValueStr)) {
                                  hasValue |= filterAndAddFieldValues(inputDocument, indexerBatchContext, indexedProperty, attributeValueStr, resolverContext.getFieldQualifier());
                              }
                          }
                      }
                  }
              }
          }
      }
      if (!hasValue) {
          final boolean isOptional = ValueProviderParameterUtils.getBoolean(indexedProperty, OPTIONAL_PARAM, OPTIONAL_PARAM_DEFAULT_VALUE);
          if (!isOptional) {
              throw new FieldValueProviderException("No value resolved for indexed property " + indexedProperty.getName());
          }
      }	
	}

   protected FeatureList loadFeatures(final Collection<IndexedProperty> indexedProperties, final ProductModel productModel) {
       final List<ClassAttributeAssignmentModel> classAttributeAssignments = new ArrayList<>();
       for (final IndexedProperty indexedProperty : indexedProperties) {
           if (indexedProperty.getClassAttributeAssignment() != null) {
               classAttributeAssignments.add(indexedProperty.getClassAttributeAssignment());
           }
       }
       return classificationService.getFeatures(productModel, classAttributeAssignments);
   }

   protected List<Feature> getFeature(final IndexedProperty indexedProperty, final FeatureList featureList) {
       final List<Feature> features = new ArrayList<>();
       final ClassAttributeAssignmentModel classAttributeAssignment = indexedProperty.getClassAttributeAssignment();
       if (classAttributeAssignment != null) {
           final List<ClassAttributeAssignmentModel> matchingSubClassAttributeAssignments = classificationClassesResolverStrategy.getAllClassAttributeAssignments(classAttributeAssignment.getClassificationClass().getCategories().stream().filter(ClassificationClassModel.class::isInstance).map(ClassificationClassModel.class::cast).collect(Collectors.toSet())).stream().filter(subClassAttributeAssignment -> subClassAttributeAssignment.getClassificationAttribute().getCode().equals(classAttributeAssignment.getClassificationAttribute().getCode())).collect(Collectors.toList());
           matchingSubClassAttributeAssignments.add(classAttributeAssignment);
           matchingSubClassAttributeAssignments.forEach(matchingSubClassAttributeAssignment -> features.add(featureList.getFeatureByAssignment(matchingSubClassAttributeAssignment)));
       }
       return features;
   }

   protected List<FeatureValue> getFeatureValues(final Feature feature, final Qualifier qualifier) throws FieldValueProviderException {
       Locale locale = null;
       if (qualifier != null) {
           locale = qualifier.getValueForType(Locale.class);
       }
       // Use the locale if feature is localized
       if (feature instanceof UnlocalizedFeature) {
           return feature.getValues();
       } else {
           return feature.getValues();
       }
   }
}
