/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.storelocator.pos.PointOfServiceService;

import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.wholescripts.core.model.ProtocolModel;
import com.wholescripts.core.service.WholescriptsProtocolService;
import com.wholescripts.data.AddtoProtocolParam;
import com.wholescripts.data.CommerceProtocolParameter;

/**
 *
 */
public class WsCommerceProtocolParameterBasicPopulator implements Populator<AddtoProtocolParam, CommerceProtocolParameter>
{
		@Resource(name = "productService")
		private ProductService productService;
		@Resource(name = "pointOfServiceService")
		private PointOfServiceService pointOfServiceService;
		@Resource(name = "modelService")
		private ModelService modelService;
		@Resource(name = "wholescriptsProtocolService")
		private WholescriptsProtocolService wholescriptsProtocolService;
		
		@Override
		public void populate(final AddtoProtocolParam addToProtcolParams, final CommerceProtocolParameter parameter)
				throws ConversionException
		{
			parameter.setEnableHooks(true);
			final Optional<ProtocolModel> protocolModel = wholescriptsProtocolService.getProtocolModelForID(addToProtcolParams.getProtocolId(),false);
			if(protocolModel.isPresent()) {
				parameter.setCart(protocolModel.get());
			}
			if (StringUtils.isNotEmpty(addToProtcolParams.getStoreId()))
			{
				final PointOfServiceModel pointOfServiceModel = pointOfServiceService
						.getPointOfServiceForName(addToProtcolParams.getStoreId());
				parameter.setPointOfService(pointOfServiceModel);
			}
			if (addToProtcolParams.getProductCode() != null)
			{
				final ProductModel product = productService.getProductForCode(addToProtcolParams.getProductCode());
				parameter.setUnit(product.getUnit());
				parameter.setProduct(product);
			}
			parameter.setQuantity(addToProtcolParams.getQuantity());
			parameter.setCreateNewEntry(false);
			parameter.setEntryGroupNumbers(addToProtcolParams.getEntryGroupNumbers());	
			parameter.setItemDuration(addToProtcolParams.getItemDuration());
			parameter.setProductItemType(addToProtcolParams.getProductItemType());
			parameter.setDosingList(addToProtcolParams.getDosingList());
			parameter.setNotes(addToProtcolParams.getNotes());
		}
}
