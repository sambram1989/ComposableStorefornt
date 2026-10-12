package com.wholescripts.core.facade;

import com.wholescripts.data.IngredientData;
import com.wholescripts.data.ShopifySearchResultData;
import com.wholescripts.data.WholescriptsProductsFamilyData;

import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.List;


public interface WholescriptsProductsFacade extends ProductFacade {

	List<WholescriptsProductsFamilyData> getAllProductFamilies(String productCode);

	ProductData getProductDetailsByCode(String productId);

	List<IngredientData> getIngredientsByProductCode(List<String>  productId);
	
	ShopifySearchResultData searchShopifyProducts(String accountNumber);

}
