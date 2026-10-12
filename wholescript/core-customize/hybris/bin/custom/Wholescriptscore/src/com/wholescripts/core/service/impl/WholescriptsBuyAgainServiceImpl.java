package com.wholescripts.core.service.impl;

import com.wholescripts.core.dao.WholescriptsProductsDao;
import com.wholescripts.core.service.WholescriptsBuyAgainService;
import de.hybris.platform.core.model.product.ProductModel;

import javax.annotation.Resource;
import java.util.List;

public class WholescriptsBuyAgainServiceImpl implements WholescriptsBuyAgainService {

    @Resource(name = "wholescriptsProductsDao")
    WholescriptsProductsDao wholescriptsProductsDao;

    @Override
    public List<ProductModel> getOrderedMedpaxProductsForUser(final String uid,final String productSearch) {
        return wholescriptsProductsDao.getOrderedMedpaxProductsForUser(uid,productSearch);
    }
}
