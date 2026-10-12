package com.wholescripts.core.dao;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.util.List;
import java.util.Optional;

public interface WholescriptsWishListDao {

    Optional<Wishlist2Model> findWishlistsByUsernName(UserModel user, String name);

    List<Wishlist2Model> findWishlistsByUser(UserModel currentUser);
}
