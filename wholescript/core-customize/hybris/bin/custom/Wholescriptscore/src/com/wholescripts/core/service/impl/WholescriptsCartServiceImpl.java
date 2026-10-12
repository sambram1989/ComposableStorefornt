/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.service.impl;

import com.wholescripts.core.model.*;
import com.wholescripts.core.service.WholescriptsCoreService;
import com.wholescripts.core.service.WholescriptsIntegrationsService;
import com.wholescripts.dto.ConsentUpdateBrandListWsDTO;
import com.wholescripts.dto.ConsentUpdateBrandWsDTO;
import de.hybris.platform.b2b.enums.CheckoutPaymentType;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import com.wholescripts.core.model.DosingModel;
import com.wholescripts.core.model.ItemTimeModel;
import com.wholescripts.core.model.MedPaxModel;
import com.wholescripts.core.model.MedpaxEntryModel;

import com.wholescripts.core.model.BrandAgreementModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationStatus;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.jalo.JaloObjectNoLongerValidException;
import de.hybris.platform.order.impl.DefaultCartService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.UUID;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wholescripts.core.dao.WholescriptsMedPaxDao;
import com.wholescripts.core.service.WholescriptsCartService;

/**
 *
 */
public class WholescriptsCartServiceImpl extends DefaultCartService implements WholescriptsCartService
{
	private static final Logger LOG = Logger.getLogger(WholescriptsCartServiceImpl.class);
	@Resource(name = "enumerationService")
	private EnumerationService enumerationService;
	@Resource(name = "modelService")
	private ModelService modelService;
	@Resource(name = "wholescriptsMedPaxDao")
	private WholescriptsMedPaxDao wholescriptsMedPaxDao;

	@Resource(name = "b2bCustomerService")
	private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;

	@Resource(name = "wholescriptsIntegrationsService")
	private WholescriptsIntegrationsService wholescriptsIntegrationsService;

	@Resource(name = "wholescriptsCoreService")
	private WholescriptsCoreService wholescriptsCoreService;

	@Override
	public CartModel getSessionCart()
	{
		try
		{
			CartModel cart = internalGetSessionCart();
			if(cart.getUser() instanceof B2BCustomerModel) {
				B2BCustomerModel customer= (B2BCustomerModel) cart.getUser();
				//By default CheckoutPaymentType is CARD from OOTB ,so updating it from B2BUnit SapPaymentTerms
				if(Objects.isNull(cart.getPaymentType()) || (Objects.nonNull(cart.getPaymentType()) &&  CheckoutPaymentType.CARD.equals(cart.getPaymentType())))
				{
					cart.setPaymentType(CheckoutPaymentType.XCRD);
					if(Objects.nonNull(customer.getDefaultB2BUnit()) && Objects.nonNull(customer.getDefaultB2BUnit().getSapPaymentTerms()) && (CheckoutPaymentType.ZTEL.equals(customer.getDefaultB2BUnit().getSapPaymentTerms())|| CheckoutPaymentType.XCRD.equals(customer.getDefaultB2BUnit().getSapPaymentTerms()))) {
						cart.setPaymentType(enumerationService.getEnumerationValue(CheckoutPaymentType.class, customer.getDefaultB2BUnit().getSapPaymentTerms().getCode()));
						getModelService().save(cart);
					}
				}

			}
			return cart;
		}
		catch (final JaloObjectNoLongerValidException ex)
		{
			if (LOG.isInfoEnabled())
			{
				LOG.info("Session Cart no longer valid. Removing from session. getSessionCart will create a new cart. "
						+ ex.getMessage());
			}
			getSessionService().removeAttribute(SESSION_CART_PARAMETER_NAME);
			return internalGetSessionCart();
		}
	}

	@Override
	public CommerceCartModification updateMedpaxCartEntry(String medpaxId,String entryNumber, String qty)
	{
		final CommerceCartModification modification = new CommerceCartModification();
		modification.setStatusCode(CommerceCartModificationStatus.UNAVAILABLE);
		modification.setQuantity(Long.valueOf(qty));
		modification.setQuantityAdded(Long.valueOf(qty));
		MedPaxModel medpax = wholescriptsMedPaxDao.getMedPaxByCode(medpaxId);
		if(Objects.nonNull(medpax)) {
			CartModel cart =getSessionCart();
			final List<AbstractOrderEntryModel> medpaxEntries = cart.getEntries().stream().filter(e -> StringUtils.isNotEmpty(e.getTemplateId()) && e.getTemplateId().split("-")[1].equals(medpax.getCode())).collect(Collectors.toList());
			for(AbstractOrderEntryModel entry: medpaxEntries) {
				entry.setAbstractOrderQty(Long.valueOf(qty));
				modelService.save(entry);
			}
			medpax.setAbstractOrderQty(Long.valueOf(qty));
			modelService.save(medpax);
			modification.setStatusCode(CommerceCartModificationStatus.SUCCESS);
		}
		return modification;
	}

	@Override
	public void deleteMedPaxEntryFromCart(String medpaxId, String entryNumber, String itemTimeCode)
	{
		MedPaxModel medpax = wholescriptsMedPaxDao.getMedPaxByCode(medpaxId);
		if(Objects.isNull(medpax) || CollectionUtils.isEmpty(medpax.getMedpaxEntry())) {
			throw new RequestParameterException("Model Not found or entries are empty for given ID:"+medpaxId);
		}
		List<MedpaxEntryModel> entryList = new ArrayList<>(medpax.getMedpaxEntry());
		Optional<MedpaxEntryModel>	 entry= medpax.getMedpaxEntry().stream().filter(e  -> e.getEntryNumber().equals(entryNumber)).findFirst();
		if(entry.isPresent()) {
			List<DosingModel> dosingList = new ArrayList<>(entry.get().getDosing());
			if(dosingList.size() > 1) {
				for(DosingModel dosing : dosingList) {
					Optional<ItemTimeModel> itemTime=dosing.getItemTimes().stream().filter(i -> i.getCode().equals(itemTimeCode)).findFirst();
					if(itemTime.isPresent()) {
						dosingList.remove(dosing);
						entry.get().setDosing(dosingList);
						modelService.save(entry.get());
						break;
					}	
				}
			}else {
				entryList.remove(entry.get());
				medpax.setMedpaxEntry(entryList);
			}
			modelService.save(medpax);
		}
	}

	@Override
	public void updateBrandConsentForCustomer(ConsentUpdateBrandListWsDTO request) throws Exception {
		B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
		if(Objects.nonNull(customer) && Objects.nonNull(customer.getDefaultB2BUnit()) && Objects.nonNull(request.getBrands()))
		{
			B2BUnitModel unit=customer.getDefaultB2BUnit();
			for(ConsentUpdateBrandWsDTO brand:request.getBrands())
			{
				try{
					BrandAgreementModel brandModel=	modelService.create(BrandAgreementModel.class);
					brandModel.setCode(UUID.randomUUID().toString());
					brandModel.setBrandName(brand.getBrandName());
					WholescriptsBrandModel productBrand=wholescriptsCoreService.getBrandByName(brand.getBrandName());
					if(Objects.nonNull(productBrand))
					{
						if(Objects.nonNull(productBrand.getBrandAgreementPdf()))
						{
							brandModel.setPdfPath(productBrand.getBrandAgreementPdf().getCode());

						}
						else {
							brandModel.setPdfPath(productBrand.getBrandAgreementPdfUrl());
						}
					}
					brandModel.setAgreementTimeStamp(String.valueOf(new Date()));
					if(CollectionUtils.isNotEmpty(unit.getBrandAgreements()))
					{
						List<BrandAgreementModel> existingAgreements = new ArrayList<>(unit.getBrandAgreements());
						if(!existingAgreements.contains(brandModel))
						{
							existingAgreements.add(brandModel);

						}
						unit.setBrandAgreements(existingAgreements);
					}
					else {
						unit.setBrandAgreements(Collections.singletonList(brandModel));
					}
					modelService.saveAll();
				}
				catch (Exception e) {

				}


			}

			try {

				wholescriptsIntegrationsService.updateBrandAgreementConsentInCDC(customer);

			} catch (Exception e) {

			}
		}
	}
}
