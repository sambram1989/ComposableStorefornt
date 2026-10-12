/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.dao.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeValueModel;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

import com.wholescripts.core.dao.WholescriptsDeliveryModeDao;


/**
 * Default implementation of {@link de.hybris.platform.commerceservices.delivery.dao.CountryZoneDeliveryModeDao}.
 */
public class WholescriptsDeliveryModeDaoImpl extends AbstractItemDao implements WholescriptsDeliveryModeDao
{
	private static final String ZONE_COUNTRY_RELATION = "ZoneCountryRelation";
	private static final String STORE_TO_DELIVERY_MODE_RELATION = "BaseStore2DeliveryModeRel";



	@Override
	public Collection<DeliveryModeModel> findDeliveryModes(final AbstractOrderModel abstractOrder, final String sapCustomerCode)
	{
		final StringBuilder query = new StringBuilder("SELECT DISTINCT {zdm:").append(ItemModel.PK).append("}");
		query.append(" FROM { ").append(ZoneDeliveryModeValueModel._TYPECODE).append(" AS val");
		query.append(" JOIN ").append(ZoneDeliveryModeModel._TYPECODE).append(" AS zdm");
		query.append(" ON {val:").append(ZoneDeliveryModeValueModel.DELIVERYMODE).append("}={zdm:").append(ItemModel.PK)
				.append('}');
		query.append(" JOIN ").append(ZONE_COUNTRY_RELATION).append(" AS z2c");
		query.append(" ON {val:").append(ZoneDeliveryModeValueModel.ZONE).append("}={z2c:").append(Link.SOURCE).append('}');
		query.append(" JOIN ").append(STORE_TO_DELIVERY_MODE_RELATION).append(" AS s2d");
		query.append(" ON {val:").append(ZoneDeliveryModeValueModel.DELIVERYMODE).append("}={s2d:").append(Link.TARGET).append('}');
		query.append(" } WHERE {val:").append(ZoneDeliveryModeValueModel.CURRENCY).append("}=?currency");
		query.append(" AND {val:").append(ZoneDeliveryModeValueModel.SAPCUSTOMERGROUPCODE).append("}=?sapCustomerCode");
		query.append(" AND {val:").append(ZoneDeliveryModeValueModel.MINIMUM).append("}<=?minimum");
		query.append(" AND {val:").append(ZoneDeliveryModeValueModel.MAXIMUM).append("}>=?maximum");
		query.append(" AND {z2c:").append(Link.TARGET).append("}=?deliveryCountry");
		query.append(" AND {s2d:").append(Link.SOURCE).append("}=?store");
		query.append(" AND {zdm:").append(ZoneDeliveryModeModel.NET).append("}=?net");
		query.append(" AND {zdm:").append(ZoneDeliveryModeModel.ACTIVE).append("}=?active");

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("deliveryCountry", abstractOrder.getDeliveryAddress().getCountry());
		params.put("currency", abstractOrder.getCurrency());
		params.put("sapCustomerCode", sapCustomerCode);
		params.put("minimum", abstractOrder.getTotalPrice());
		params.put("maximum", abstractOrder.getTotalPrice());
		params.put("net", abstractOrder.getNet());
		params.put("active", Boolean.TRUE);
		params.put("store", abstractOrder.getStore());

		return doSearch(query.toString(), params, DeliveryModeModel.class);
	}


	protected <T> List<T> doSearch(final String query, final Map<String, Object> params, final Class<T> resultClass)
	{
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		if (params != null)
		{
			fQuery.addQueryParameters(params);
		}

		fQuery.setResultClassList(Collections.singletonList(resultClass));

		final SearchResult<T> searchResult = search(fQuery);
		return searchResult.getResult();
	}


	@Override
	public ZoneDeliveryModeValueModel findZoneDeliveryModesValue(final AbstractOrderModel abstractOrder,
			final String sapCustomerCode, final DeliveryModeModel deliveryMode)
	{
		final StringBuilder query = new StringBuilder("SELECT DISTINCT {val:").append(ItemModel.PK).append("}");
		query.append(" FROM { ").append(ZoneDeliveryModeValueModel._TYPECODE).append(" AS val");
		query.append(" JOIN ").append(ZoneDeliveryModeModel._TYPECODE).append(" AS zdm");
		query.append(" ON {val:").append(ZoneDeliveryModeValueModel.DELIVERYMODE).append("}={zdm:").append(ItemModel.PK)
				.append('}');
		query.append(" JOIN ").append(ZONE_COUNTRY_RELATION).append(" AS z2c");
		query.append(" ON {val:").append(ZoneDeliveryModeValueModel.ZONE).append("}={z2c:").append(Link.SOURCE).append('}');
		query.append(" JOIN ").append(STORE_TO_DELIVERY_MODE_RELATION).append(" AS s2d");
		query.append(" ON {val:").append(ZoneDeliveryModeValueModel.DELIVERYMODE).append("}={s2d:").append(Link.TARGET).append('}');
		query.append(" } WHERE {val:").append(ZoneDeliveryModeValueModel.CURRENCY).append("}=?currency");
		query.append(" AND {val:").append(ZoneDeliveryModeValueModel.SAPCUSTOMERGROUPCODE).append("}=?sapCustomerCode");
		query.append(" AND {val:").append(ZoneDeliveryModeValueModel.MINIMUM).append("}<=?minimum");
		query.append(" AND {val:").append(ZoneDeliveryModeValueModel.MAXIMUM).append("}>=?maximum");
		query.append(" AND {z2c:").append(Link.TARGET).append("}=?deliveryCountry");
		query.append(" AND {s2d:").append(Link.SOURCE).append("}=?store");
		query.append(" AND {zdm:").append(ZoneDeliveryModeModel.NET).append("}=?net");
		query.append(" AND {zdm:").append(ZoneDeliveryModeModel.ACTIVE).append("}=?active");
		query.append(" AND {val:").append(ZoneDeliveryModeValueModel.DELIVERYMODE).append("}=?deliveryMode");

		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("deliveryCountry", abstractOrder.getDeliveryAddress().getCountry());
		params.put("currency", abstractOrder.getCurrency());
		params.put("sapCustomerCode", sapCustomerCode);
		params.put("minimum", abstractOrder.getTotalPrice());
		params.put("maximum", abstractOrder.getTotalPrice());
		params.put("net", abstractOrder.getNet());
		params.put("active", Boolean.TRUE);
		params.put("store", abstractOrder.getStore());
		params.put(ZoneDeliveryModeValueModel.DELIVERYMODE, deliveryMode);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		if (params != null)
		{
			fQuery.addQueryParameters(params);
		}

		fQuery.setResultClassList(Collections.singletonList(ZoneDeliveryModeValueModel.class));

		final SearchResult<ZoneDeliveryModeValueModel> searchResult = search(fQuery);
		return CollectionUtils.isNotEmpty(searchResult.getResult()) ? searchResult.getResult().get(0) : null;
	}
}
