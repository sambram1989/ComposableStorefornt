/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;


import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.wholescripts.core.dao.WholescriptsItemTimeDao;
import com.wholescripts.core.model.DosingModel;
import com.wholescripts.core.model.ItemTimeModel;
import com.wholescripts.core.model.MedpaxEntryModel;
import com.wholescripts.core.service.WholescriptsCoreService;
import com.wholescripts.dto.DosingWsDTO;
import com.wholescripts.dto.MedpaxEntryWsDTO;


/**
 *
 */
public class WholescriptsMedpaxEntryReversePopulator implements Populator<MedpaxEntryWsDTO, MedpaxEntryModel>
{

	@Resource(name = "productService")
	ProductService productService;
	@Resource(name = "modelService")
	ModelService modelService;

	@Resource(name = "wholescriptsCoreService")
	WholescriptsCoreService wholescriptsCoreService;

	@Resource(name = "wholescriptsItemTimeDao")
	private WholescriptsItemTimeDao wholescriptsItemTimeDaoImpl;

	@Resource(name = "wsDosingCodeGenerator")
	private KeyGenerator wsDosingCodeGenerator;

	@Resource(name = "b2bCustomerService")
	private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;

	@Resource(name = "wsMedPaxEntryCodeGenerator")
	private KeyGenerator wsMedPaxEntryCodeGenerator;

	@Override
	public void populate(final MedpaxEntryWsDTO source, final MedpaxEntryModel target) throws ConversionException
	{
		target.setPrice(source.getPrice());
		target.setQuantity(source.getQuantity());
		target.setMsrpPrice(source.getMsrpPrice());
		if (StringUtils.isNotEmpty(source.getEntryNumber()))
		{
			target.setEntryNumber(source.getEntryNumber());
		}
		else
		{
			target.setEntryNumber(wsMedPaxEntryCodeGenerator.generate().toString());
		}
		if (source.getProduct() != null && StringUtils.isNotEmpty(source.getProduct().getCode()))
		{
			target.setProduct(productService.getProductForCode(source.getProduct().getCode()));
		}
		if (source.getDosing() != null)
		{
			target.setDosing(getDosingModelList(source.getDosing(),target));
		}

	}

	private List<DosingModel> getDosingModelList(final List<DosingWsDTO> dosingsWsDTO, MedpaxEntryModel medpaxEntryModel)
	{
		final List<DosingModel> dosingModels = new ArrayList<DosingModel>();
		for (final DosingWsDTO dosingWsDTO : dosingsWsDTO)
		{
			DosingModel dosingModel=new DosingModel();
			if(Objects.nonNull(medpaxEntryModel) && CollectionUtils.isNotEmpty(medpaxEntryModel.getDosing()))
			{
				final Optional<DosingModel> dosing = medpaxEntryModel.getDosing().stream().filter(dos -> dosingWsDTO.getId()!=null && dos.getId().equals(dosingWsDTO.getId())).findFirst();
				if (dosing.isPresent())
				{
					dosingModel = dosing.get();
					final List<ItemTimeModel> itemTimeList = new ArrayList<>();
					if (StringUtils.isNotEmpty(dosingWsDTO.getItemTimes().getCode()))
					{
						final ItemTimeModel itemtimeModel = wholescriptsItemTimeDaoImpl.getItemTimeByCode(dosingWsDTO.getItemTimes().getCode());
						itemTimeList.add(itemtimeModel);
					}
					dosingModel.setQuantity(dosingWsDTO.getQuantity());
					dosingModel.setItemTimes(itemTimeList);
					modelService.save(dosingModel);
					dosingModels.add(dosingModel);
				}
				else {
					createDosingForMedpaxEntry(dosingModels,dosingModel,dosingWsDTO);

				}

			}

			else
			{
				createDosingForMedpaxEntry(dosingModels,dosingModel,dosingWsDTO);

			}

		}
		return dosingModels;
	}

	private void createDosingForMedpaxEntry(List<DosingModel> dosingModels ,DosingModel dosingModel,DosingWsDTO dosingWsDTO) {
		dosingModel = modelService.create(DosingModel.class);
		final List<ItemTimeModel> itemTimeList = new ArrayList<>();
		if (StringUtils.isNotEmpty(dosingWsDTO.getItemTimes().getCode()))
		{
			final ItemTimeModel itemtimeModel = wholescriptsItemTimeDaoImpl
					.getItemTimeByCode(dosingWsDTO.getItemTimes().getCode());
			itemTimeList.add(itemtimeModel);
		}
		dosingModel.setId(wsDosingCodeGenerator.generate().toString());
		dosingModel.setQuantity(dosingWsDTO.getQuantity());
		dosingModel.setProduct(productService.getProductForCode(dosingWsDTO.getProduct().getCode()));
		dosingModel.setItemTimes(itemTimeList);
		dosingModel.setB2bCustomer(b2bCustomerService.getCurrentB2BCustomer());
		dosingModels.add(dosingModel);
	}
}
