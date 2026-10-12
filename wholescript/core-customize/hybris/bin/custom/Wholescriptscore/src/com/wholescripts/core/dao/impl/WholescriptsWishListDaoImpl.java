package com.wholescripts.core.dao.impl;

import com.wholescripts.core.dao.WholescriptsWishListDao;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.wishlist2.model.Wishlist2Model;

import javax.annotation.Resource;
import java.util.*;

public class WholescriptsWishListDaoImpl extends AbstractItemDao implements WholescriptsWishListDao {

    @Resource(name = "flexibleSearchService")
    private FlexibleSearchService flexibleSearchService;

    @Override
    public Optional<Wishlist2Model> findWishlistsByUsernName(UserModel user, String name) {
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {Wishlist2} WHERE {user} = ?user AND {name} = ?name");
        fQuery.addQueryParameter("user", user);
        fQuery.addQueryParameter("name", name);
        SearchResult<Wishlist2Model> result = flexibleSearchService.search(fQuery);
        if (Objects.nonNull(result.getResult()) && result.getCount() > 0) {
            return Optional.of(result.getResult().get(0));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Wishlist2Model> findWishlistsByUser(UserModel currentUser) {
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {Wishlist2} WHERE {user} = ?user");
        fQuery.addQueryParameter("user", currentUser);
        SearchResult<Wishlist2Model> result = flexibleSearchService.search(fQuery);
        if (Objects.nonNull(result.getResult()) && result.getCount() > 0) {
            return result.getResult();
        } else {
            return null;
        }
    }

}
