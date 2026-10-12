/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.dao.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.order.dao.impl.DefaultCommerceCartDao;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class WholescriptsCommerceCartDaoImpl extends DefaultCommerceCartDao
{
	protected final static String SELECTCLAUSE = "SELECT {pk} from {Cart!}";
	protected final static String FIND_CARTS_FOR_SITE_AND_USER = SELECTCLAUSE + "WHERE {" + CartModel.SITE + "} = ?site AND {"
			+ CartModel.USER + "} = ?user " + NOT_SAVED_CART_CLAUSE + NOT_QUOTE_CART_CLAUSE + ORDERBYCLAUSE;
	protected final static String FIND_OLD_CARTS_FOR_SITE = SELECTCLAUSE + "WHERE {" + CartModel.MODIFIEDTIME
			+ "} <= ?modifiedBefore AND {" + CartModel.SITE + "} = ?site " + NOT_SAVED_CART_CLAUSE + ORDERBYCLAUSE;
	protected final static String FIND_OLD_CARTS_FOR_SITE_AND_USER = SELECTCLAUSE + "WHERE {" + CartModel.USER + "} = ?user AND {"
			+ CartModel.MODIFIEDTIME + "} <= ?modifiedBefore AND {" + CartModel.SITE + "} = ?site " + NOT_SAVED_CART_CLAUSE
			+ ORDERBYCLAUSE;
	
	@Override
	public List<CartModel> getCartsForSiteAndUser(final BaseSiteModel site, final UserModel user)
	{
		final Map<String, Object> params = new HashMap<>();
		params.put("site", site);
		params.put("user", user);
		return doSearch(FIND_CARTS_FOR_SITE_AND_USER, params, CartModel.class);
	}
	
	@Override
	public List<CartModel> getCartsForRemovalForSiteAndUser(final Date modifiedBefore, final BaseSiteModel site,
			final UserModel user)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("site", site);
		params.put("modifiedBefore", modifiedBefore);
		if (user == null)
		{
			return doSearch(FIND_OLD_CARTS_FOR_SITE, params, CartModel.class);
		}
		else
		{
			params.put("user", user);
			return doSearch(FIND_OLD_CARTS_FOR_SITE_AND_USER, params, CartModel.class);
		}
	}

}
