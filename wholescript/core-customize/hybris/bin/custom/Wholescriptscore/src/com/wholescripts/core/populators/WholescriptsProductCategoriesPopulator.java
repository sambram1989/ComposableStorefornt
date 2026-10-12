package com.wholescripts.core.populators;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.converters.populator.ProductCategoriesPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import java.util.Collection;
public class WholescriptsProductCategoriesPopulator <SOURCE extends ProductModel, TARGET extends ProductData>
        extends ProductCategoriesPopulator<SOURCE, TARGET> {
    @Override
    public void populate(SOURCE productModel, TARGET productData) throws ConversionException {
        Collection<CategoryModel> categories = getCommerceProductService()
                .getSuperCategoriesExceptClassificationClassesForProduct(productModel)
                .stream()
                .filter(category -> !"catalog".equalsIgnoreCase(category.getCode()))
                .toList();
        productData.setCategories(getCategoryConverter().convertAll(categories));
    }
}