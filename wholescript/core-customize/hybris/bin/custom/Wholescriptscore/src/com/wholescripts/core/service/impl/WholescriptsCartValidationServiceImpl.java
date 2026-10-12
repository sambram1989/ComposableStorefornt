/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service.impl;

import com.wholescripts.core.enums.CustomerProfileType;
import com.wholescripts.core.exception.BrandAgreementException;
import com.wholescripts.core.populators.WholescriptsBrandPopulator;
import com.wholescripts.dto.WholescriptsBrandData;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.wholescripts.core.dao.WholescriptsCartValidationDao;
import com.wholescripts.core.dao.WholescriptsMedPaxDao;
import com.wholescripts.core.exception.AgeRestrictionException;
import com.wholescripts.core.exception.POBoxAgeRestrictionException;
import com.wholescripts.core.exception.RegionExclusionException;
import com.wholescripts.core.service.WholescriptsCartValidationService;
import com.wholescripts.dto.WholescriptsValidateCartsWsDTO;
import com.wholescripts.core.model.WholescriptsBrandModel;
import com.wholescripts.core.model.RegionProductSwapRestrictionModel;
import com.wholescripts.core.model.BrandAgreementModel;
import com.wholescripts.core.model.MedPaxModel;

/**
 *
 */
public class WholescriptsCartValidationServiceImpl implements WholescriptsCartValidationService {
	@Resource(name = "wholescriptsCartValidationDao")
	private WholescriptsCartValidationDao wholescriptsCartValidationDao;
	@Resource(name = "cartService")
	private CartService cartService;
	@Resource(name = "b2bCustomerService")
	private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;
	@Resource(name = "orderEntryConverter")
	private Converter<AbstractOrderEntryModel, OrderEntryData> orderEntryConverter;
	@Resource(name = "wholescriptsMedPaxDao")
	private WholescriptsMedPaxDao wholescriptsMedPaxDao;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "brandPopulator")
	private WholescriptsBrandPopulator brandPopulator;

	@Resource(name = "wsBrandConverter")
	private Converter<WholescriptsBrandModel, WholescriptsBrandData> wsBrandConverter;

	@Override
	public void validateCart(WholescriptsValidateCartsWsDTO requestData) throws Exception {
		B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
		if(requestData.isBrandValidation())
		{
			validateBrandAgreement();
		}
		if(Objects.nonNull(requestData.getAddress()))
       {
		   AddressWsDTO address = requestData.getAddress();
		   CartModel cart = cartService.getSessionCart();
		   String regionCode = address.getRegion().getIsocode().contains("-") ? address.getRegion().getIsocode().split("-")[1] : address.getRegion().getIsocode();
		   if ("CA".equals(regionCode)) {
			   List<AbstractOrderEntryModel> caExlusionList = new ArrayList<>();
			   for (AbstractOrderEntryModel entry : cart.getEntries()) {
				   boolean isExcluded = wholescriptsCartValidationDao.validateIfProductInCAExclusion(entry.getProduct().getCode(), regionCode);
				   if (isExcluded) {
					   caExlusionList.add(entry);
				   }
			   }
			   if (CollectionUtils.isNotEmpty(caExlusionList)) {
				   List<OrderEntryData> caOrderEntryDataList = orderEntryConverter.convertAll(caExlusionList);
				   addMedpaxNameAndCode(cart, caOrderEntryDataList);
				   throw new RegionExclusionException(caOrderEntryDataList);
			   }
		   } else if ("NY".equals(regionCode)) {
			   List<AbstractOrderEntryModel> nyExlusionList = new ArrayList<>();
			   for (AbstractOrderEntryModel entry : cart.getEntries()) {
				   boolean isExcluded = wholescriptsCartValidationDao.validateIfProductInNYExclusion(entry.getProduct().getCode(), regionCode);
				   if (isExcluded) {
					   nyExlusionList.add(entry);
				   }
			   }
			   if (CollectionUtils.isNotEmpty(nyExlusionList)) {
				   List<OrderEntryData> ageOrderEntryDataList = orderEntryConverter.convertAll(nyExlusionList);
				   addMedpaxNameAndCode(cart, ageOrderEntryDataList);
				   if (StringUtils.containsAnyIgnoreCase(address.getLine1(), configurationService.getConfiguration().getString("wholescripts.address.po.box"))) {
					   throw new POBoxAgeRestrictionException(ageOrderEntryDataList);
				   } else if (Objects.nonNull(customer) && Objects.nonNull(customer.getDefaultB2BUnit()) && (StringUtils.isEmpty(customer.getDefaultB2BUnit().getAgeExpirationDate()) || customer.getDefaultB2BUnit().getAgeExpirationDate().equals("0000-00-00"))) {
					   throw new AgeRestrictionException(ageOrderEntryDataList);
				   }
			   }
		   }
	   }


	}

	@Override
	public RegionProductSwapRestrictionModel updateProductFromSwapRestriction(final AbstractOrderEntryModel entryModel) {
		if (Objects.nonNull(entryModel.getOrder()) && Objects.nonNull(entryModel.getOrder().getDeliveryAddress())) {
			AddressModel address = entryModel.getOrder().getDeliveryAddress();
			if (Objects.nonNull(address.getRegion())) {
				String regionCode = address.getRegion().getIsocode().contains("-") ? address.getRegion().getIsocode().split("-")[1] : address.getRegion().getIsocode();
				if (StringUtils.isNotBlank(regionCode) && "CA".equals(regionCode)) {
					{
						return wholescriptsCartValidationDao.updateProductFromSwapRestriction(entryModel.getProduct().getCode(), regionCode);

					}
				}
			}
		}
		return null;
	}

	private void addMedpaxNameAndCode(final CartModel cart, List<OrderEntryData> orderEntryData) {
		List<OrderEntryData> medpaxOrderEntryList = orderEntryData.stream()
				.filter(e -> CollectionUtils.isNotEmpty(e.getEntryGroupNumbers())).collect(Collectors.toList());
		for (OrderEntryData orderEntry : medpaxOrderEntryList) {
			AbstractOrderEntryModel entry = cart.getEntries().stream().filter(e -> e.getEntryNumber().equals(orderEntry.getEntryGroupNumbers().iterator().next())).findFirst().orElse(null);
			EntryGroup entryGroup = cart.getEntryGroups().stream().filter(e -> e.getGroupNumber().equals(entry.getEntryGroupNumbers().iterator().next())).findFirst().orElse(null);
			MedPaxModel medpax = wholescriptsMedPaxDao.getMedPaxByCode(entryGroup.getExternalReferenceId());
			if (Objects.nonNull(medpax)) {
				orderEntry.setMedpaxId(medpax.getCode());
				orderEntry.setMedpaxName(medpax.getName());
			}
		}
	}

	@Override
	public void validateBrandAgreement()  throws Exception {
		B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
		List<WholescriptsBrandModel> brandsList = new ArrayList<>();
		if (Objects.nonNull(customer) && Objects.nonNull(customer.getDefaultB2BUnit()) && Objects.nonNull(customer.getCustomerProfile()) && (customer.getCustomerProfile().equals(CustomerProfileType.PRACTITIONER) || customer.getCustomerProfile().equals(CustomerProfileType.DISTRIBUTOR)))
			{
				B2BUnitModel b2bUnit = customer.getDefaultB2BUnit();
				List<BrandAgreementModel> brandsAgreed = b2bUnit.getBrandAgreements();
				List<String> brandNamesList = new ArrayList<>();
				if (CollectionUtils.isNotEmpty(brandsAgreed)) {
					for (BrandAgreementModel brand : brandsAgreed) {
						if(!brandNamesList.contains(brand.getBrandName()))
						{
							brandNamesList.add(brand.getBrandName());

						}
					}
				}
				CartModel cart = cartService.getSessionCart();

				for (AbstractOrderEntryModel entry : cart.getEntries()) {
					WholescriptsBrandModel brandModel = entry.getProduct().getWholescriptsBrand();
					if (Objects.nonNull(brandModel) && Objects.nonNull(brandModel.getAgreementRequired()) &&  brandModel.getAgreementRequired() && !brandNamesList.contains(brandModel.getName())) {
						if(!brandsList.contains(brandModel))
						{
							brandsList.add(brandModel);
						}

					}

				}
			}
if(CollectionUtils.isNotEmpty(brandsList))
{
	final List<WholescriptsBrandData> brandsAgreementList=wsBrandConverter.convertAll(brandsList);
	throw new BrandAgreementException(brandsAgreementList);
}
	}
}