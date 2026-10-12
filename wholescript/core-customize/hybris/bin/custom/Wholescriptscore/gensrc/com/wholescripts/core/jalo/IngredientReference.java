/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 27-May-2025, 11:44:08 pm                    ---
 * ----------------------------------------------------------------
 */
package com.wholescripts.core.jalo;

import com.wholescripts.core.constants.WholescriptscoreConstants;
import com.wholescripts.core.jalo.Ingredient;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type IngredientReference.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class IngredientReference extends GenericItem
{
	/** Qualifier of the <code>IngredientReference.ingredient</code> attribute **/
	public static final String INGREDIENT = "ingredient";
	/** Qualifier of the <code>IngredientReference.units</code> attribute **/
	public static final String UNITS = "units";
	/** Qualifier of the <code>IngredientReference.productPOS</code> attribute **/
	public static final String PRODUCTPOS = "productPOS";
	/** Qualifier of the <code>IngredientReference.product</code> attribute **/
	public static final String PRODUCT = "product";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n PRODUCT's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<IngredientReference> PRODUCTHANDLER = new BidirectionalOneToManyHandler<IngredientReference>(
	WholescriptscoreConstants.TC.INGREDIENTREFERENCE,
	false,
	"product",
	"productPOS",
	true,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(INGREDIENT, AttributeMode.INITIAL);
		tmp.put(UNITS, AttributeMode.INITIAL);
		tmp.put(PRODUCTPOS, AttributeMode.INITIAL);
		tmp.put(PRODUCT, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		PRODUCTHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IngredientReference.ingredient</code> attribute.
	 * @return the ingredient
	 */
	public Ingredient getIngredient(final SessionContext ctx)
	{
		return (Ingredient)getProperty( ctx, "ingredient".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IngredientReference.ingredient</code> attribute.
	 * @return the ingredient
	 */
	public Ingredient getIngredient()
	{
		return getIngredient( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>IngredientReference.ingredient</code> attribute. 
	 * @param value the ingredient
	 */
	public void setIngredient(final SessionContext ctx, final Ingredient value)
	{
		setProperty(ctx, "ingredient".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>IngredientReference.ingredient</code> attribute. 
	 * @param value the ingredient
	 */
	public void setIngredient(final Ingredient value)
	{
		setIngredient( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IngredientReference.product</code> attribute.
	 * @return the product
	 */
	public Product getProduct(final SessionContext ctx)
	{
		return (Product)getProperty( ctx, "product".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IngredientReference.product</code> attribute.
	 * @return the product
	 */
	public Product getProduct()
	{
		return getProduct( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>IngredientReference.product</code> attribute. 
	 * @param value the product
	 */
	protected void setProduct(final SessionContext ctx, final Product value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		// initial-only attribute: make sure this attribute can be set during item creation only
		if ( ctx.getAttribute( "core.types.creation.initial") != Boolean.TRUE )
		{
			throw new JaloInvalidParameterException( "attribute '"+"product".intern()+"' is not changeable", 0 );
		}
		PRODUCTHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>IngredientReference.product</code> attribute. 
	 * @param value the product
	 */
	protected void setProduct(final Product value)
	{
		setProduct( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IngredientReference.productPOS</code> attribute.
	 * @return the productPOS
	 */
	 Integer getProductPOS(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, "productPOS".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IngredientReference.productPOS</code> attribute.
	 * @return the productPOS
	 */
	 Integer getProductPOS()
	{
		return getProductPOS( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IngredientReference.productPOS</code> attribute. 
	 * @return the productPOS
	 */
	 int getProductPOSAsPrimitive(final SessionContext ctx)
	{
		Integer value = getProductPOS( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IngredientReference.productPOS</code> attribute. 
	 * @return the productPOS
	 */
	 int getProductPOSAsPrimitive()
	{
		return getProductPOSAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>IngredientReference.productPOS</code> attribute. 
	 * @param value the productPOS
	 */
	 void setProductPOS(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, "productPOS".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>IngredientReference.productPOS</code> attribute. 
	 * @param value the productPOS
	 */
	 void setProductPOS(final Integer value)
	{
		setProductPOS( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>IngredientReference.productPOS</code> attribute. 
	 * @param value the productPOS
	 */
	 void setProductPOS(final SessionContext ctx, final int value)
	{
		setProductPOS( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>IngredientReference.productPOS</code> attribute. 
	 * @param value the productPOS
	 */
	 void setProductPOS(final int value)
	{
		setProductPOS( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IngredientReference.units</code> attribute.
	 * @return the units
	 */
	public Double getUnits(final SessionContext ctx)
	{
		return (Double)getProperty( ctx, "units".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IngredientReference.units</code> attribute.
	 * @return the units
	 */
	public Double getUnits()
	{
		return getUnits( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IngredientReference.units</code> attribute. 
	 * @return the units
	 */
	public double getUnitsAsPrimitive(final SessionContext ctx)
	{
		Double value = getUnits( ctx );
		return value != null ? value.doubleValue() : 0.0d;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>IngredientReference.units</code> attribute. 
	 * @return the units
	 */
	public double getUnitsAsPrimitive()
	{
		return getUnitsAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>IngredientReference.units</code> attribute. 
	 * @param value the units
	 */
	public void setUnits(final SessionContext ctx, final Double value)
	{
		setProperty(ctx, "units".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>IngredientReference.units</code> attribute. 
	 * @param value the units
	 */
	public void setUnits(final Double value)
	{
		setUnits( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>IngredientReference.units</code> attribute. 
	 * @param value the units
	 */
	public void setUnits(final SessionContext ctx, final double value)
	{
		setUnits( ctx,Double.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>IngredientReference.units</code> attribute. 
	 * @param value the units
	 */
	public void setUnits(final double value)
	{
		setUnits( getSession().getSessionContext(), value );
	}
	
}
