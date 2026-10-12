package com.wholescripts.core.service;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import java.util.List;
import java.util.Optional;

public interface WholescriptsWishlist2Service {
    Optional<Wishlist2Model> findWishlistsByUsernName(UserModel user, String name);

    List<Wishlist2Model> findWishlistsByUser(UserModel currentUser);
}
