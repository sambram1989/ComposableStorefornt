package com.wholescripts.core.populators;


import de.hybris.platform.commercefacades.product.converters.populator.ProductUrlPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.product.ProductModel;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;



public class WholescriptsProductUrlPopulator extends ProductUrlPopulator {

    @Override
    public void populate(final ProductModel source, final ProductData target) {

        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");

        target.setCode(source.getCode());
        target.setUrl(getProductModelUrlResolver().resolve(source));

    }

}
