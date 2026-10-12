/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commerceservices.delivery.DeliveryService;
import de.hybris.platform.commerceservices.delivery.dao.PickupDeliveryModeDao;
import de.hybris.platform.commerceservices.delivery.impl.DefaultDeliveryService;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeValueModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.PriceValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wholescripts.core.constants.WholescriptscoreConstants;
import com.wholescripts.core.dao.WholescriptsCartValidationDao;
import com.wholescripts.core.dao.WholescriptsDeliveryModeDao;


/**
 * Default implementation of the {@link DeliveryService}.
 */
public class WholescriptsDeliveryServiceImpl extends DefaultDeliveryService
{
	private static final Logger LOG = Logger.getLogger(WholescriptsDeliveryServiceImpl.class);

	@Resource(name = "wholescriptsDeliveryModeDao")
	private WholescriptsDeliveryModeDao wholescriptsDeliveryModeDao;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "pickupDeliveryModeDao")
	private PickupDeliveryModeDao pickupDeliveryModeDao;
	@Resource(name = "wholescriptsCartValidationDao")
	private WholescriptsCartValidationDao wholescriptsCartValidationDao;
	@Resource(name = "sessionService")
	private SessionService sessionService;



	@Override
	public List<DeliveryModeModel> getSupportedDeliveryModeListForOrder(final AbstractOrderModel abstractOrder)
	{
		validateParameterNotNull(abstractOrder, "abstractOrder model cannot be null");
		final List<DeliveryModeModel> deliveryModes = new ArrayList<DeliveryModeModel>();
		final AddressModel deliveryAddress = abstractOrder.getDeliveryAddress();
		final String postalCode = deliveryAddress.getPostalcode();
		final CurrencyModel currency = abstractOrder.getCurrency();
		final B2BCustomerModel customer = (B2BCustomerModel) abstractOrder.getUser();

		if (currency != null && deliveryAddress != null && deliveryAddress.getCountry() != null
				&& Objects.nonNull(deliveryAddress.getRegion()))
		{
			String regionCode = deliveryAddress.getRegion().getIsocode();
			if (regionCode.contains("-"))
			{
				regionCode = regionCode.split("-")[1];
			}
			final ArrayList<DeliveryModeModel> filteredDeliveryModes = new ArrayList<>(
					wholescriptsDeliveryModeDao.findDeliveryModes(abstractOrder, getSapCustomerCode(abstractOrder)));
			for (final AbstractOrderEntryModel entry : abstractOrder.getEntries())
			{
				final boolean isExcluded = wholescriptsCartValidationDao.validateIfProductInNYExclusion(entry.getProduct().getCode(),
						regionCode);
				if (isExcluded)
				{
					final Optional<DeliveryModeModel> mode = filteredDeliveryModes.stream().filter(d -> d.getCode().equals("03"))
							.findFirst();
					if (mode.isPresent())
					{
						//exclude USPS if age restriction
						filteredDeliveryModes.remove(mode.get());
					}
					break;
				}
			}
			if (StringUtils.containsAnyIgnoreCase(deliveryAddress.getLine1(),
					configurationService.getConfiguration().getString("wholescripts.address.po.box")))
			{
				//exclude UPS if PO box restriction
				if(Objects.nonNull(customer.getDefaultB2BUnit()) && Objects.nonNull(customer.getDefaultB2BUnit().getSapCustomerGroup()) && "02".equals(customer.getDefaultB2BUnit().getSapCustomerGroup().getCode())) {
					filteredDeliveryModes.removeAll(filteredDeliveryModes.stream()
							.filter(d -> d.getCode().equals("06") || d.getCode().equals("2D") || d.getCode().equals("3D"))
							.collect(Collectors.toList()));
				}else {
					filteredDeliveryModes.removeAll(filteredDeliveryModes.stream()
							.filter(d -> d.getCode().equals("06") || d.getCode().equals("2D") || d.getCode().equals("3D") || d.getCode().equals("01"))
							.collect(Collectors.toList()));
				}
			}

			if (Objects.nonNull(customer.getDefaultB2BUnit()) && Objects.nonNull(customer.getDefaultB2BUnit().getSapShippingCond()))
			{
				String shippingCond= customer.getDefaultB2BUnit().getSapShippingCond().getCode();
				if ("03".equals(shippingCond))
				{
					final Optional<DeliveryModeModel> uspsShipping = filteredDeliveryModes.stream()
							.filter(d -> d.getCode().equals("03")).findFirst();
					filteredDeliveryModes.removeAll(filteredDeliveryModes);
					if(uspsShipping.isPresent()) {
						filteredDeliveryModes.add(uspsShipping.get());
					}

				}else if("06".equals(shippingCond)) {
					final ArrayList<DeliveryModeModel> upsDeliveryModes = (ArrayList<DeliveryModeModel>) filteredDeliveryModes.stream()
							.filter(d -> d.getCode().equals("06") || d.getCode().equals("2D") || d.getCode().equals("3D")).collect(Collectors.toList());
					filteredDeliveryModes.removeAll(filteredDeliveryModes);
					filteredDeliveryModes.addAll(upsDeliveryModes);
				}
			}
			deliveryModes.addAll(filteredDeliveryModes);
		}
		if (StringUtils.isNotEmpty(postalCode) && isZipCodeAllowed(postalCode))
		{
			deliveryModes.addAll(pickupDeliveryModeDao.findPickupDeliveryModesForAbstractOrder(abstractOrder));
		}
		sortDeliveryModes(deliveryModes, abstractOrder);
		return deliveryModes;
	}

	private String getSapCustomerCode(final AbstractOrderModel abstractOrder)
	{
		final List userRole = new ArrayList<>(sessionService.getAttribute(WholescriptscoreConstants.ACTING_USER_ROLE));
		Optional<String> isAsagent=userRole.stream().filter(r -> WholescriptscoreConstants.ASAGENT_GROUP.equalsIgnoreCase(r.toString())).findAny();
		if (isAsagent.isPresent())
		{
			return "05";
		}
		final B2BCustomerModel customer = (B2BCustomerModel) abstractOrder.getUser();
		final B2BUnitModel b2BUnitModel = customer.getDefaultB2BUnit();
		if (b2BUnitModel != null && b2BUnitModel.getSapCustomerGroup() != null)
		{
			return b2BUnitModel.getSapCustomerGroup().getCode();
		}
		return "01";
	}

	public boolean isZipCodeAllowed(String postalCode)
	{
		final String postalCodes = configurationService.getConfiguration().getString("pickup.allowed.postalCodes");
		if (postalCode.contains("-"))
		{
			postalCode = postalCode.split("-")[0];
		}
		return Arrays.asList(postalCodes.split(",")).contains(postalCode);
	}


	@Override
	public PriceValue getDeliveryCostForDeliveryModeAndAbstractOrder(final DeliveryModeModel deliveryMode,
			final AbstractOrderModel abstractOrder)
	{
		validateParameterNotNull(deliveryMode, "deliveryMode model cannot be null");
		validateParameterNotNull(abstractOrder, "abstractOrder model cannot be null");
		final CurrencyModel currency = abstractOrder.getCurrency();
		final ZoneDeliveryModeValueModel valueModel = wholescriptsDeliveryModeDao.findZoneDeliveryModesValue(abstractOrder,
				getSapCustomerCode(abstractOrder),deliveryMode);
		if (Objects.nonNull(valueModel))
		{
			return new PriceValue(currency.getIsocode(), valueModel.getValue(), abstractOrder.getNet());
		}
		return new PriceValue(currency.getIsocode(), 0.0D, abstractOrder.getNet());

	}


}
