package com.wholescripts.core.service.impl;

import com.wholescripts.core.dao.WholescriptsWishListDao;
import com.wholescripts.core.service.WholescriptsWishlist2Service;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.wishlist2.Wishlist2Service;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

public class WholescriptsWishlist2ServiceImpl implements WholescriptsWishlist2Service {

    @Resource(name = "wholescriptsWishListDao")
    WholescriptsWishListDao wholescriptsWishListDao;
    @Resource(name = "wishlistService")
    Wishlist2Service wishlistService;

    @Override
    public Optional<Wishlist2Model> findWishlistsByUsernName(UserModel user, String name) {
        return wholescriptsWishListDao.findWishlistsByUsernName(user, name);
    }

    @Override
    public List<Wishlist2Model> findWishlistsByUser(UserModel currentUser) {
        return wholescriptsWishListDao.findWishlistsByUser(currentUser);
    }
}
