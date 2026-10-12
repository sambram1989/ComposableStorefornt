/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 27-May-2025, 11:44:08 pm                    ---
 * ----------------------------------------------------------------
 */
package com.wholescripts.core.jalo;

import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type Ingredient.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class Ingredient extends GenericItem
{
	/** Qualifier of the <code>Ingredient.code</code> attribute **/
	public static final String CODE = "code";
	/** Qualifier of the <code>Ingredient.ingredientName</code> attribute **/
	public static final String INGREDIENTNAME = "ingredientName";
	/** Qualifier of the <code>Ingredient.rda</code> attribute **/
	public static final String RDA = "rda";
	/** Qualifier of the <code>Ingredient.priority</code> attribute **/
	public static final String PRIORITY = "priority";
	/** Qualifier of the <code>Ingredient.unit</code> attribute **/
	public static final String UNIT = "unit";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CODE, AttributeMode.INITIAL);
		tmp.put(INGREDIENTNAME, AttributeMode.INITIAL);
		tmp.put(RDA, AttributeMode.INITIAL);
		tmp.put(PRIORITY, AttributeMode.INITIAL);
		tmp.put(UNIT, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Ingredient.code</code> attribute.
	 * @return the code
	 */
	public String getCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "code".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Ingredient.code</code> attribute.
	 * @return the code
	 */
	public String getCode()
	{
		return getCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Ingredient.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "code".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Ingredient.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final String value)
	{
		setCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Ingredient.ingredientName</code> attribute.
	 * @return the ingredientName
	 */
	public String getIngredientName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "ingredientName".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Ingredient.ingredientName</code> attribute.
	 * @return the ingredientName
	 */
	public String getIngredientName()
	{
		return getIngredientName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Ingredient.ingredientName</code> attribute. 
	 * @param value the ingredientName
	 */
	public void setIngredientName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "ingredientName".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Ingredient.ingredientName</code> attribute. 
	 * @param value the ingredientName
	 */
	public void setIngredientName(final String value)
	{
		setIngredientName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Ingredient.priority</code> attribute.
	 * @return the priority
	 */
	public Integer getPriority(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, "priority".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Ingredient.priority</code> attribute.
	 * @return the priority
	 */
	public Integer getPriority()
	{
		return getPriority( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Ingredient.priority</code> attribute. 
	 * @return the priority
	 */
	public int getPriorityAsPrimitive(final SessionContext ctx)
	{
		Integer value = getPriority( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Ingredient.priority</code> attribute. 
	 * @return the priority
	 */
	public int getPriorityAsPrimitive()
	{
		return getPriorityAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Ingredient.priority</code> attribute. 
	 * @param value the priority
	 */
	public void setPriority(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, "priority".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Ingredient.priority</code> attribute. 
	 * @param value the priority
	 */
	public void setPriority(final Integer value)
	{
		setPriority( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Ingredient.priority</code> attribute. 
	 * @param value the priority
	 */
	public void setPriority(final SessionContext ctx, final int value)
	{
		setPriority( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Ingredient.priority</code> attribute. 
	 * @param value the priority
	 */
	public void setPriority(final int value)
	{
		setPriority( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Ingredient.rda</code> attribute.
	 * @return the rda
	 */
	public Double getRda(final SessionContext ctx)
	{
		return (Double)getProperty( ctx, "rda".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Ingredient.rda</code> attribute.
	 * @return the rda
	 */
	public Double getRda()
	{
		return getRda( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Ingredient.rda</code> attribute. 
	 * @return the rda
	 */
	public double getRdaAsPrimitive(final SessionContext ctx)
	{
		Double value = getRda( ctx );
		return value != null ? value.doubleValue() : 0.0d;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Ingredient.rda</code> attribute. 
	 * @return the rda
	 */
	public double getRdaAsPrimitive()
	{
		return getRdaAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Ingredient.rda</code> attribute. 
	 * @param value the rda
	 */
	public void setRda(final SessionContext ctx, final Double value)
	{
		setProperty(ctx, "rda".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Ingredient.rda</code> attribute. 
	 * @param value the rda
	 */
	public void setRda(final Double value)
	{
		setRda( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Ingredient.rda</code> attribute. 
	 * @param value the rda
	 */
	public void setRda(final SessionContext ctx, final double value)
	{
		setRda( ctx,Double.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Ingredient.rda</code> attribute. 
	 * @param value the rda
	 */
	public void setRda(final double value)
	{
		setRda( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Ingredient.unit</code> attribute.
	 * @return the unit
	 */
	public EnumerationValue getUnit(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, "unit".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Ingredient.unit</code> attribute.
	 * @return the unit
	 */
	public EnumerationValue getUnit()
	{
		return getUnit( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Ingredient.unit</code> attribute. 
	 * @param value the unit
	 */
	public void setUnit(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, "unit".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Ingredient.unit</code> attribute. 
	 * @param value the unit
	 */
	public void setUnit(final EnumerationValue value)
	{
		setUnit( getSession().getSessionContext(), value );
	}
	
}
