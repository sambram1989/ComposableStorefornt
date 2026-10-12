/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateIfSingleResult;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;
import static java.lang.String.format;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.product.daos.ProductDao;
import de.hybris.platform.product.impl.DefaultProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wholescripts.core.dao.WholescriptsMaterialExclusionDao;
import com.wholescripts.core.dao.WholescriptsProductsDao;
import com.wholescripts.core.enums.SapCustomerGroupEnum;
import com.wholescripts.core.model.BrandCustomerClassRestrictionModel;
import com.wholescripts.core.model.CustomerClassProductRestrictionModel;
import com.wholescripts.core.model.CustomerMaterialInformationModel;
import com.wholescripts.core.model.CustomerProductGroupRestrictionModel;
import com.wholescripts.core.model.CustomerProductRestrictionModel;
import com.wholescripts.core.model.IngredientModel;
import com.wholescripts.core.model.IngredientReferenceModel;
import com.wholescripts.core.service.WholescriptsProductsService;
import com.wholescripts.core.strategies.WholescriptsMaterialExclusionStrategy;
import com.wholescripts.core.utils.WholescriptsCoreUtils;

public class WholescriptsProductServiceImpl extends DefaultProductService implements WholescriptsProductsService
{

	private static final Logger LOG = Logger.getLogger(WholescriptsProductServiceImpl.class);
	private static final String PRODUCT_FAMILY_FEATURE="wholescripts.pdp.product.family.classification.code";
	private static final String MASTER_VARIANT="wholescripts.pdp.mastervariant.classification.code";
	private static final String WHOLESCRIPTS_PRIVATE_LABEL_CODE = "wholescripts.private.label.code";
	private static final String SHOPIFY_PRIVATE_LABEL_CODE = "shopify.privatelabel.product.material.groups";
	private final static String COMMA = ",";
	private static final String MEDPAX_BLUE_LABEL_CODES = "wholescripts.medpax.blue.label.codes";
	private static final String WHOLESCRIPTS_PRIVATE_LABEL_MEDPAX_CODE="pl.medpax.product.medpax";
	private static final String  WHOLESCRIPTS_BLUE_LABEL_CODE="wholescripts.blue.label.code";
	private static final String  WHOLESCRIPTS_DEFAULT_CARTON_CODE ="medpax.product.carton";
	private static final String  WHOLESCRIPTS_CARTON_CODE="wholescripts.carton.cmir.group";

	@Resource(name = "productDao")
	private ProductDao productDao;
	@Resource(name = "wsMaterialExclusionStrategy")
	private WholescriptsMaterialExclusionStrategy wsMaterialExclusionStrategy;

	@Resource(name = "productService")
	private ProductService productService;

	@Resource(name = "wholescriptsProductsDao")
	private WholescriptsProductsDao wholescriptsProductsDao;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "sessionService")
	private SessionService sessionService;

	@Resource(name = "b2bCustomerService")
	private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;

	@Resource(name = "classificationService")
	private ClassificationService classificationService;

	@Resource(name = "catalogVersionService")
	private CatalogVersionService catalogVersionService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	
	@Resource(name = "wholescriptsMaterialExclusionDao")
	WholescriptsMaterialExclusionDao wholescriptsMaterialExclusionDao;


	@Override
	public ProductModel getProductForCode(final String code)
	{
		validateParameterNotNull(code, "Parameter code must not be null");
		final Collection<CatalogVersionModel> catalogVersion = catalogVersionService.getSessionCatalogVersionsForCatalog(
				configurationService.getConfiguration().getString("wholescripts.product.catalog"));
		List<ProductModel> products = productDao.findProductsByCode(catalogVersion.iterator().next(),String.format("%018d",Integer.valueOf(code)));
		if(CollectionUtils.isNotEmpty(products)) {
			boolean isExcluded=	wsMaterialExclusionStrategy.checkIfProductInExclusionTable(products.get(0));
			if(isExcluded) {
				products = new ArrayList<>();
			}
		}
		validateIfSingleResult(products, format("Product with code '%s' not found!", code),
				format("Product code '%s' is not unique, %d products found!", code,
						Integer.valueOf(products.size())));

		return products.get(0);
	}


	@Override
	public List<ProductModel> getAllProductFamiliesData(String productCode) {
		final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();

		ProductModel product = productService.getProductForCode(productCode);
		List<ProductFeatureModel> productFeatures = product.getFeatures();

		String featureQualifier = null;
		String featureQualifierValue = null;
		String catalogVersionPk = String.valueOf(product.getCatalogVersion().getPk());

		for (ProductFeatureModel feature : productFeatures) {
			if (Config.getParameter(PRODUCT_FAMILY_FEATURE).equalsIgnoreCase(feature.getQualifier())) {

				featureQualifier = feature.getQualifier();
				final Object attributeValue = feature.getValue();
				featureQualifierValue = String.valueOf(((ClassificationAttributeValueModel) attributeValue).getPk());
				break;
			}
		}
		List<ProductModel> productFamilies = new ArrayList<>();
		if (featureQualifier != null && featureQualifierValue != null) {
			productFamilies = new ArrayList<>(
					wholescriptsProductsDao.getAllProductFamiliesData(featureQualifier, featureQualifierValue, catalogVersionPk)
			);
			productFamilies.removeIf(familyProduct -> familyProduct.getCode().equals(productCode));

			if (!productFamilies.isEmpty()) {
				if (Objects.nonNull(customer)) {
					for (ProductModel productFamiliesProducts : productFamilies) {
						boolean isExcluded = wsMaterialExclusionStrategy.checkIfProductInExclusionTable(productFamiliesProducts);
						if (isExcluded) {
							return Collections.emptyList();
						}
					}
				}
				productFamilies.removeIf(discardProduct -> Boolean.TRUE.equals(discardProduct.getIsMedpax()));
			}
		}
		return productFamilies;
	}


	@Override
	public ProductModel getProductDetailsByCode(final String productId) {
		final ProductModel product = productService.getProductForCode(productId);
		boolean isMedPax = product.getIsMedpax();
		if (isMedPax) {
			
			final String sapMaterialGroups = configurationService.getConfiguration().getString(MEDPAX_BLUE_LABEL_CODES);
			List<ProductModel> blueLabelProducts = wholescriptsProductsDao.getProductsByMedpaxCriteria(sapMaterialGroups,
					product.getSapBasicMaterial(),
					product.getCatalogVersion()
			);

			if (blueLabelProducts != null && !blueLabelProducts.isEmpty()) {
				for (ProductModel blueLabelProduct : blueLabelProducts) {
					if (!blueLabelProduct.getCode().equals(product.getCode())) {
						FeatureList featureList = classificationService.getFeatures(blueLabelProduct);
						if (featureList != null) {
							Feature feature = featureList.getFeatureByCode(Config.getParameter(MASTER_VARIANT));
							if (feature != null && feature.getValue() != null) {
								String masterVariant = (String) feature.getValue().getValue();
								if (StringUtils.isNotBlank(masterVariant)) {
									return productService.getProductForCode(masterVariant);
								}
							}
						}
					}
				}
				// No Master Variant found, return the first Blue Label Product (excluding original Medpax)
				for (ProductModel blueLabelProduct : blueLabelProducts) {
					if (!blueLabelProduct.getCode().equals(product.getCode())) {
						return blueLabelProduct;
					}
				}
			}
		} else if (product.getSapMaterialGroup() != null && "1000014".equalsIgnoreCase(product.getSapMaterialGroup().getCode())) {
			final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
			final B2BUnitModel unit = customer.getDefaultB2BUnit();
			String accountNumber = WholescriptsCoreUtils.getFormattedDefaultB2bUnit(unit.getUid());
			CustomerMaterialInformationModel cmInfoModel = getCustomerMaterialInfo(product.getCode().replaceFirst("^0+", ""), accountNumber);
			if (cmInfoModel != null && cmInfoModel.getBlueLabel() != null) {
				return productService.getProductForCode(cmInfoModel.getBlueLabel());
			}
		}

		return product;
	}


	@Override
	public List<IngredientModel> getIngredientsByProductCode(String productCode) {

		// Validate the productCode to ensure it's not null
		validateParameterNotNull(productCode, "Parameter productCode must not be null");

		final ProductModel product = productService.getProductForCode(productCode);

		// Set the product in the session
		sessionService.setAttribute("ingredientCode", product);

		// If the product is found, return its ingredients; otherwise, return an empty list
		if (product != null) {
			final Collection<IngredientReferenceModel> allIngredients = product.getIngredientReferences();
			if (allIngredients != null && !allIngredients.isEmpty()) {
				final List<IngredientModel> ingredientsList = new ArrayList<>();

				for (final IngredientReferenceModel reference : allIngredients) {
					if (reference != null && reference.getIngredient() != null) {
						ingredientsList.add(reference.getIngredient());
					}
				}
				return ingredientsList;
			}
		}
		return Collections.emptyList();
	}

	@Override
	public boolean isPrivateLabel(ProductModel product) {
		String privateLabelCode = configurationService.getConfiguration().getString(WHOLESCRIPTS_PRIVATE_LABEL_CODE);
		if(StringUtils.isNotEmpty(privateLabelCode) && Objects.nonNull(product.getSapMaterialGroup()))
		{
			return privateLabelCode.equals(product.getSapMaterialGroup().getCode());

		}

        return false;
    }
	
	@Override
   public List<ProductModel> getProductsForShopify(final String accountNumber){
		
		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(
				configurationService.getConfiguration().getString("wholescripts.product.catalog"),
				configurationService.getConfiguration().getString("wholescripts.product.catalog.version"));
		
		final List<ProductModel> productModels = wholescriptsProductsDao.getProductsForShopify(catalogVersion);
		final List<ProductModel> validProductModels = new ArrayList<ProductModel>();
		
		if (CollectionUtils.isNotEmpty(productModels)) {
			LOG.debug("getProductsForShopify, original non pl product size " + productModels.size());
			for (ProductModel productModel : productModels) {
				boolean isExcluded = wsMaterialExclusionStrategy.checkIfProductInExclusionTable(productModel);
				if (!isExcluded) {
					validProductModels.add(productModel);
				}
			}
			LOG.debug("getProductsForShopify, modified non pl product size " + validProductModels.size());
		}
		
		final String plMaterialCodes= configurationService.getConfiguration().getString(SHOPIFY_PRIVATE_LABEL_CODE);
      final String[] plMaterialCodesArray = StringUtils.split(plMaterialCodes, COMMA);
      final List<String> plMaterialCodesList = Arrays.asList(plMaterialCodesArray);  
		final List<CustomerMaterialInformationModel> customerMaterialInformationModels = wholescriptsProductsDao
				.getCustomerMaterialInformationByCustomerId(accountNumber);
		
		if (CollectionUtils.isNotEmpty(customerMaterialInformationModels)) {
			LOG.debug("getProductsForShopify, customerMaterialInformation size " + customerMaterialInformationModels.size()
			+ " for account number " + accountNumber);
			
			for (CustomerMaterialInformationModel cmiModel : customerMaterialInformationModels) {
				try {
					final ProductModel plProduct = getProductForCode(cmiModel.getProductCode());
					if (plProduct.getSapMaterialGroup() != null && 
							plMaterialCodesList.contains(plProduct.getSapMaterialGroup().getCode())) {
						validProductModels.add(plProduct);
					}
				}
				catch (final UnknownIdentifierException e){
					LOG.error("product not found or either present in restriction for cmi code " + cmiModel.getProductCode());
				}
			}

		}
		
		LOG.debug("getProductsForShopify, final valid product size " + validProductModels.size());
		
		return validProductModels;
       
   }

	@Override
	public List<ProductModel> getProductsByMasterVariant(String featureQualifier, String featureQualifierValue, String catalogVersionPk) {
		// Fetch products from DAO layer
		List<ProductModel> variantProducts = wholescriptsProductsDao.getProductsByMasterVariant(featureQualifier, featureQualifierValue, catalogVersionPk);

		return variantProducts.isEmpty() ? Collections.emptyList() : new ArrayList<>(variantProducts);
	}

	@Override
	public CustomerMaterialInformationModel getCustomerMaterialInfo(String productCode,String account) {

		return wholescriptsProductsDao.getCustomerMaterialInfo(productCode,account);
	}
	
	@Override
	public List<CustomerProductRestrictionModel> getCustomerProductRestrictions(final String productCode) {
		return wholescriptsMaterialExclusionDao.getCustomerProductRestrictions(productCode);
	}
	
	@Override
	public List<CustomerProductGroupRestrictionModel> getCustomerProductGroupRestrictions(final String productGroup) {
		return wholescriptsMaterialExclusionDao.getCustomerProductGroupRestrictions(productGroup);
	}
	
	@Override
	public List<CustomerClassProductRestrictionModel> getCustomerClassProductRestrictions(final String productCode) {
		return wholescriptsMaterialExclusionDao.getCustomerClassProductRestrictions(productCode);
	}
	
	@Override
	public List<BrandCustomerClassRestrictionModel> getBrandCustomerClassRestriction(final String brandCode) {
		return wholescriptsMaterialExclusionDao.getBrandCustomerClassRestriction(brandCode);
	}

	@Override
	public boolean isPrivateLabelMedpax(ProductModel product) {
		String plMedpax = configurationService.getConfiguration().getString(WHOLESCRIPTS_PRIVATE_LABEL_MEDPAX_CODE);
		if(StringUtils.isNotEmpty(plMedpax))
		{
			return plMedpax.equals(product.getCode());

		}

		return false;
	}

	@Override
	public CustomerMaterialInformationModel getCustomerMaterialInfoByBlueLabel(String productCode, String account) {
		return wholescriptsProductsDao.getCustomerMaterialInfoByBlueLabel(productCode,account);
	}

	@Override
	public boolean isBlueLabel(ProductModel product) {
		String blueLabelCode = configurationService.getConfiguration().getString(WHOLESCRIPTS_BLUE_LABEL_CODE);
		if(StringUtils.isNotEmpty(blueLabelCode) && Objects.nonNull(product.getSapMaterialGroup()))
		{
			return blueLabelCode.equals(product.getSapMaterialGroup().getCode());

		}
		return false;
	}

	@Override
	public boolean isDefaultCarton(ProductModel productModel) {
		return configurationService.getConfiguration().getString(WHOLESCRIPTS_DEFAULT_CARTON_CODE).equals(productModel.getCode());
	}

	@Override
	public boolean isCarton(ProductModel product) {
		String cartonCode = configurationService.getConfiguration().getString(WHOLESCRIPTS_CARTON_CODE);
		if(StringUtils.isNotEmpty(cartonCode) && Objects.nonNull(product.getSapMaterialGroup()))
		{
			return cartonCode.equals(product.getSapMaterialGroup().getCode());

		}
		return false;
	}
	
	@Override
	public String getGenericPLNameFromCMI(final String productCode) {

		final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
		String genericPLName = null;
		if (Objects.nonNull(customer) && Objects.nonNull(customer.getDefaultB2BUnit())) {
			final B2BUnitModel unit = customer.getDefaultB2BUnit();
			String accountNumber = WholescriptsCoreUtils.getFormattedDefaultB2bUnit(unit.getUid());
			if(Objects.nonNull(unit.getSapCustomerGroup()) && SapCustomerGroupEnum.valueOf("02").equals(unit.getSapCustomerGroup())) {
				accountNumber = WholescriptsCoreUtils.getFormattedDefaultB2bUnit(unit.getSalesPerson());
			}
			final CustomerMaterialInformationModel cmInfoModel = getCustomerMaterialInfo(productCode.replaceFirst("^0+", ""),
					accountNumber);
			if (cmInfoModel != null && cmInfoModel.getProductDescription() != null) {
				genericPLName = cmInfoModel.getProductDescription();
			}

		}
		return genericPLName;
	}


}
