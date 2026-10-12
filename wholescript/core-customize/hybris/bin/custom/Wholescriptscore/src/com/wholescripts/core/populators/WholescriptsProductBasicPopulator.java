package com.wholescripts.core.populators;

import com.wholescripts.core.service.WholescriptsProductsService;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.commercefacades.product.converters.populator.ProductBasicPopulator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.util.Config;
import org.apache.commons.lang.StringUtils;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import javax.annotation.Resource;
import java.util.Objects;

import com.wholescripts.core.model.CustomerMaterialInformationModel;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import com.wholescripts.core.utils.WholescriptsCoreUtils;
import java.util.Arrays;
import java.util.List;

public class WholescriptsProductBasicPopulator<SOURCE extends ProductModel, TARGET extends ProductData>
        extends ProductBasicPopulator<SOURCE, TARGET> {

    private static final String PRODUCT_NAME = "wholescripts.pdp.productname.classification.code";
    private static final List<String> PRIVATE_LABEL_CODES = Arrays.asList("1000014", "1001024");

    @Resource(name = "classificationService")
    private ClassificationService classificationService;

    @Resource(name = "b2bCustomerService")
    private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;

    @Resource(name = "wholescriptsProductsService")
    WholescriptsProductsService wholescriptsProductsService;

    @Override
    public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException {
        super.populate(productModel, productData);

        if (productModel.getSapMaterialGroup() != null && PRIVATE_LABEL_CODES.contains(productModel.getSapMaterialGroup().getCode())) {
            final B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
            final B2BUnitModel unit = customer.getDefaultB2BUnit();
            String accountNumber = WholescriptsCoreUtils.getFormattedDefaultB2bUnit(unit.getUid());
            CustomerMaterialInformationModel cmInfoModel = wholescriptsProductsService.getCustomerMaterialInfo(
                    productModel.getCode().replaceFirst("^0+", ""), accountNumber);

            if (cmInfoModel != null && cmInfoModel.getProductDescription() != null) {
                productData.setName(cmInfoModel.getProductDescription());
                return;
            }
        }

        final String classificationKey = Config.getParameter(PRODUCT_NAME);
        final FeatureList featureList = classificationService.getFeatures(productModel);
        if (Objects.nonNull(featureList)) {
            final Feature productNameFeature = featureList.getFeatureByCode(classificationKey);
            if (Objects.nonNull(productNameFeature) && productNameFeature.getValue() != null) {
                final FeatureValue productNameValue = productNameFeature.getValue();
                if (Objects.nonNull(productNameValue) && Objects.nonNull(productNameValue.getValue())) {
                    final String productNameData = productNameValue.getValue().toString();
                    if (StringUtils.isNotBlank(productNameData)) {
                        productData.setName(productNameData);
                    }
                }
            }
        }
    }
}
