/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 27-May-2025, 11:44:08 pm                    ---
 * ----------------------------------------------------------------
 */
package com.wholescripts.core.jalo;

import com.wholescripts.core.constants.WholescriptscoreConstants;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type PromotionCategory.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class PromotionCategory extends GenericItem
{
	/** Qualifier of the <code>PromotionCategory.code</code> attribute **/
	public static final String CODE = "code";
	/** Qualifier of the <code>PromotionCategory.products</code> attribute **/
	public static final String PRODUCTS = "products";
	/** Relation ordering override parameter constants for PromotionCategoryToProductRelation from ((Wholescriptscore))*/
	protected static String PROMOTIONCATEGORYTOPRODUCTRELATION_SRC_ORDERED = "relation.PromotionCategoryToProductRelation.source.ordered";
	protected static String PROMOTIONCATEGORYTOPRODUCTRELATION_TGT_ORDERED = "relation.PromotionCategoryToProductRelation.target.ordered";
	/** Relation disable markmodifed parameter constants for PromotionCategoryToProductRelation from ((Wholescriptscore))*/
	protected static String PROMOTIONCATEGORYTOPRODUCTRELATION_MARKMODIFIED = "relation.PromotionCategoryToProductRelation.markmodified";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CODE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionCategory.code</code> attribute.
	 * @return the code - promotion category code
	 */
	public String getCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "code".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionCategory.code</code> attribute.
	 * @return the code - promotion category code
	 */
	public String getCode()
	{
		return getCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PromotionCategory.code</code> attribute. 
	 * @param value the code - promotion category code
	 */
	public void setCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "code".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PromotionCategory.code</code> attribute. 
	 * @param value the code - promotion category code
	 */
	public void setCode(final String value)
	{
		setCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * @deprecated since 2011, use {@link Utilities#getMarkModifiedOverride(de.hybris.platform.jalo.type.RelationType)
	 */
	@Override
	@Deprecated(since = "2105", forRemoval = true)
	public boolean isMarkModifiedDisabled(final Item referencedItem)
	{
		ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("Product");
		if(relationSecondEnd0.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(PROMOTIONCATEGORYTOPRODUCTRELATION_MARKMODIFIED);
		}
		return true;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionCategory.products</code> attribute.
	 * @return the products
	 */
	public List<Product> getProducts(final SessionContext ctx)
	{
		final List<Product> items = getLinkedItems( 
			ctx,
			true,
			WholescriptscoreConstants.Relations.PROMOTIONCATEGORYTOPRODUCTRELATION,
			"Product",
			null,
			Utilities.getRelationOrderingOverride(PROMOTIONCATEGORYTOPRODUCTRELATION_SRC_ORDERED, true),
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PromotionCategory.products</code> attribute.
	 * @return the products
	 */
	public List<Product> getProducts()
	{
		return getProducts( getSession().getSessionContext() );
	}
	
	public long getProductsCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			true,
			WholescriptscoreConstants.Relations.PROMOTIONCATEGORYTOPRODUCTRELATION,
			"Product",
			null
		);
	}
	
	public long getProductsCount()
	{
		return getProductsCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PromotionCategory.products</code> attribute. 
	 * @param value the products
	 */
	public void setProducts(final SessionContext ctx, final List<Product> value)
	{
		setLinkedItems( 
			ctx,
			true,
			WholescriptscoreConstants.Relations.PROMOTIONCATEGORYTOPRODUCTRELATION,
			null,
			value,
			Utilities.getRelationOrderingOverride(PROMOTIONCATEGORYTOPRODUCTRELATION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(PROMOTIONCATEGORYTOPRODUCTRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PromotionCategory.products</code> attribute. 
	 * @param value the products
	 */
	public void setProducts(final List<Product> value)
	{
		setProducts( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to products. 
	 * @param value the item to add to products
	 */
	public void addToProducts(final SessionContext ctx, final Product value)
	{
		addLinkedItems( 
			ctx,
			true,
			WholescriptscoreConstants.Relations.PROMOTIONCATEGORYTOPRODUCTRELATION,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(PROMOTIONCATEGORYTOPRODUCTRELATION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(PROMOTIONCATEGORYTOPRODUCTRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to products. 
	 * @param value the item to add to products
	 */
	public void addToProducts(final Product value)
	{
		addToProducts( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from products. 
	 * @param value the item to remove from products
	 */
	public void removeFromProducts(final SessionContext ctx, final Product value)
	{
		removeLinkedItems( 
			ctx,
			true,
			WholescriptscoreConstants.Relations.PROMOTIONCATEGORYTOPRODUCTRELATION,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(PROMOTIONCATEGORYTOPRODUCTRELATION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(PROMOTIONCATEGORYTOPRODUCTRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from products. 
	 * @param value the item to remove from products
	 */
	public void removeFromProducts(final Product value)
	{
		removeFromProducts( getSession().getSessionContext(), value );
	}
	
}
