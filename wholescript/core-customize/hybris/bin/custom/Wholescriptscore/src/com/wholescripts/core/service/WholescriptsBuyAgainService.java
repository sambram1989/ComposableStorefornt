package com.wholescripts.core.service;

import de.hybris.platform.core.model.product.ProductModel;

import java.util.List;

public interface WholescriptsBuyAgainService {
    /**
     * @param uId
     * @return list of Ordered Medpax products
     */
    List<ProductModel> getOrderedMedpaxProductsForUser(final String uId,final String productSearch);
}

