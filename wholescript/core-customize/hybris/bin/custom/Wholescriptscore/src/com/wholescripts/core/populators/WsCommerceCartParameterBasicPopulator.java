/*
 * Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.populators;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.comments.model.DomainModel;
import de.hybris.platform.comments.services.CommentService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.order.CartService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.storelocator.pos.PointOfServiceService;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.wholescripts.core.constants.GeneratedWholescriptscoreConstants.Enumerations.ItemDuration;
import com.wholescripts.core.dao.WholescriptsItemTimeDao;
import com.wholescripts.core.dao.WholescriptsRecommendationDao;
import com.wholescripts.core.model.DosingModel;
import com.wholescripts.core.model.ItemTimeModel;
import com.wholescripts.core.model.RecommendationModel;
import com.wholescripts.dto.DosingWsDTO;
import com.wholescripts.dto.ItemTimeWsDTO;
import com.wholescripts.dto.WsAddtoCartParam;
import com.wholescripts.dto.WsCommerceCartParameter;


/**
 *
 */
public class WsCommerceCartParameterBasicPopulator implements Populator<WsAddtoCartParam, WsCommerceCartParameter>
{
	@Resource(name = "productService")
	private ProductService productService;
	@Resource(name = "cartService")
	private CartService cartService;
	@Resource(name = "pointOfServiceService")
	private PointOfServiceService pointOfServiceService;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "commentService")
	private CommentService commentService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "enumerationService")
	protected EnumerationService enumerationService;

	@Resource(name = "wsCodeGenerator")
	private KeyGenerator keyGenerator;

	@Resource(name = "wsDosingCodeGenerator")
	private KeyGenerator wsDosingCodeGenerator;

	@Resource(name = "wsCommentCodeGenerator")
	private KeyGenerator wsCommentCodeGenerator;


	@Resource(name = "wholescriptsRecommendationDao")
	private WholescriptsRecommendationDao wholescriptsRecommendationDao;

	@Resource(name = "wholescriptsItemTimeDao")
	private WholescriptsItemTimeDao WholescriptsItemTimeDaoImpl;
	


	@Override
	public void populate(final WsAddtoCartParam addToCartParams, final WsCommerceCartParameter parameter)
			throws ConversionException
	{
		if(addToCartParams.isIsRequestForCart()) {
			parameter.setEnableHooks(true);
			final CartModel cartModel = cartService.getSessionCart();
			parameter.setCart(cartModel);
			if (StringUtils.isNotEmpty(addToCartParams.getStoreId()))
			{
				final PointOfServiceModel pointOfServiceModel = pointOfServiceService.getPointOfServiceForName(
						addToCartParams.getStoreId());
				parameter.setPointOfService(pointOfServiceModel);
			}
			// Product code may be optional, e.g. when it's addressed by order entry.
			if (addToCartParams.getProductCode() != null)
			{
				final ProductModel product = productService.getProductForCode(addToCartParams.getProductCode());
				parameter.setProduct(product);
				parameter.setUnit(product.getUnit());
			}
			parameter.setQuantity(addToCartParams.getQuantity());
			parameter.setCreateNewEntry(false);
			parameter.setEntryGroupNumbers(addToCartParams.getEntryGroupNumbers());
			parameter.setMedpaxId(addToCartParams.getMedpaxId());
			parameter.setMedpaxName(addToCartParams.getMedpaxName());
			parameter.setCartonName(addToCartParams.getCartonName());
			parameter.setPacketName(addToCartParams.getPacketName());
			parameter.setTemplateId(addToCartParams.getTemplateId());
			parameter.setSapTemplateId(addToCartParams.getSapTemplateId());			
			parameter.setMedpaxEntryNumber(addToCartParams.getMedpaxEntryNumber());
			parameter.setHidden(addToCartParams.isHidden());
			parameter.setAbstractOrderQty(addToCartParams.getAbstractOrderQty());
			parameter.setDoseNote(addToCartParams.getDoseNote());
			parameter.setIsPrivateLabelCarton(addToCartParams.isIsPrivateLabelCarton());
		}else {
			ProductModel product = modelService.create(ProductModel.class);
			final List<DosingModel> doseModelList = new ArrayList<>();
			if (addToCartParams.getQuantity() == 0)
			{
				final RecommendationModel recommendModel = wholescriptsRecommendationDao
						.getRecommendationByCode(addToCartParams.getRecommendationId());
				parameter.setCart(recommendModel);
				if (StringUtils.isNotEmpty(addToCartParams.getStoreId()))
				{
					final PointOfServiceModel pointOfServiceModel = pointOfServiceService
							.getPointOfServiceForName(addToCartParams.getStoreId());
					parameter.setPointOfService(pointOfServiceModel);
				}
				// Product code may be optional, e.g. when it's addressed by order entry.
				if (addToCartParams.getProductCode() != null)
				{
					product = productService.getProductForCode(addToCartParams.getProductCode());
					parameter.setUnit(product.getUnit());
				}
				parameter.setQuantity(addToCartParams.getQuantity());
				parameter.setCreateNewEntry(false);
				parameter.setEntryGroupNumbers(addToCartParams.getEntryGroupNumbers());
				parameter.setProduct(product);
			}

			else
			{
				if (addToCartParams.getProductCode() != null)
				{
					product = productService.getProductForCode(addToCartParams.getProductCode());
				}
				if (addToCartParams.getEntryInput().getProductItemType() != null)
				{
					parameter.setProductItemType(addToCartParams.getEntryInput().getProductItemType());
				}
				parameter.setEnableHooks(true);
				if (addToCartParams.getEntryInput().getItemDuration() != null)
				{
					parameter.setItemDuration(addToCartParams.getEntryInput().getItemDuration());
				}
				else
				{
					parameter.setItemDuration(ItemDuration.ON_GOING);
				}
				if (addToCartParams.getEntryInput().getNotes() != null)
				{

					final DomainModel domainModel = commentService.getDomainForCode(Config.getParameter("comment.domain.code"));
					final ComponentModel componentModel = commentService.getComponentForCode(domainModel,
							Config.getParameter("comment.component.code"));
					final CommentTypeModel commentTypeModel = commentService.getCommentTypeForCode(componentModel,
							Config.getParameter("comment.component.type.code"));
					final CommentModel commentModel = modelService.create(CommentModel.class);
					commentModel.setCode(wsCommentCodeGenerator.generate().toString());
					commentModel.setText(addToCartParams.getEntryInput().getNotes());
					commentModel.setAuthor(userService.getCurrentUser());
					commentModel.setComponent(componentModel);
					commentModel.setCommentType(commentTypeModel);
					final List<CommentModel> commentList = new ArrayList<>();
					commentList.add(commentModel);
					parameter.setComment(commentList);

				}
				if (CollectionUtils.isNotEmpty(addToCartParams.getEntryInput().getDosing()))
				{
					for (final DosingWsDTO dosingWsDTO : addToCartParams.getEntryInput().getDosing())
					{
						final DosingModel doseModel = modelService.create(DosingModel.class);
						if(StringUtils.isNotEmpty(dosingWsDTO.getId())) {
							doseModel.setId(dosingWsDTO.getId());
						}else {
							doseModel.setId(wsDosingCodeGenerator.generate().toString());
						}
						final ItemTimeWsDTO timeWsDTO = dosingWsDTO.getItemTimes();
						final String time = timeWsDTO.getCode();
						final ItemTimeModel itemtimeModel = WholescriptsItemTimeDaoImpl.getItemTimeByCode(time);
						doseModel.setItemTimes(Arrays.asList(itemtimeModel));
						doseModel.setB2bCustomer((B2BCustomerModel) userService.getCurrentUser());
						if (addToCartParams.getProductCode() != null)
						{
							product = productService.getProductForCode(addToCartParams.getProductCode());
						}
						doseModel.setProduct(product);
						doseModel.setQuantity(Integer.valueOf(dosingWsDTO.getQuantity()));
						doseModelList.add(doseModel);
					}
					parameter.setDosing(doseModelList);
				}

				final RecommendationModel recommendModel = wholescriptsRecommendationDao
						.getRecommendationByCode(addToCartParams.getRecommendationId());
				parameter.setCart(recommendModel);
				if (StringUtils.isNotEmpty(addToCartParams.getStoreId()))
				{
					final PointOfServiceModel pointOfServiceModel = pointOfServiceService
							.getPointOfServiceForName(addToCartParams.getStoreId());
					parameter.setPointOfService(pointOfServiceModel);
				}
				// Product code may be optional, e.g. when it's addressed by order entry.
				if (addToCartParams.getProductCode() != null)
				{
					product = productService.getProductForCode(addToCartParams.getProductCode());
					parameter.setUnit(product.getUnit());
				}
				parameter.setQuantity(addToCartParams.getQuantity());
				parameter.setCreateNewEntry(false);
				parameter.setEntryGroupNumbers(addToCartParams.getEntryGroupNumbers());
				parameter.setProduct(product);
			}
		}
	}
}
