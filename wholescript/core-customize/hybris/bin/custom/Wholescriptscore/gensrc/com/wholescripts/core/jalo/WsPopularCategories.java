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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type WsPopularCategories.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class WsPopularCategories extends GenericItem
{
	/** Qualifier of the <code>WsPopularCategories.categoryCode</code> attribute **/
	public static final String CATEGORYCODE = "categoryCode";
	/** Qualifier of the <code>WsPopularCategories.weight</code> attribute **/
	public static final String WEIGHT = "weight";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CATEGORYCODE, AttributeMode.INITIAL);
		tmp.put(WEIGHT, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WsPopularCategories.categoryCode</code> attribute.
	 * @return the categoryCode
	 */
	public String getCategoryCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "categoryCode".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WsPopularCategories.categoryCode</code> attribute.
	 * @return the categoryCode
	 */
	public String getCategoryCode()
	{
		return getCategoryCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WsPopularCategories.categoryCode</code> attribute. 
	 * @param value the categoryCode
	 */
	public void setCategoryCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "categoryCode".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WsPopularCategories.categoryCode</code> attribute. 
	 * @param value the categoryCode
	 */
	public void setCategoryCode(final String value)
	{
		setCategoryCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WsPopularCategories.weight</code> attribute.
	 * @return the weight
	 */
	public Integer getWeight(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, "weight".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WsPopularCategories.weight</code> attribute.
	 * @return the weight
	 */
	public Integer getWeight()
	{
		return getWeight( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WsPopularCategories.weight</code> attribute. 
	 * @return the weight
	 */
	public int getWeightAsPrimitive(final SessionContext ctx)
	{
		Integer value = getWeight( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WsPopularCategories.weight</code> attribute. 
	 * @return the weight
	 */
	public int getWeightAsPrimitive()
	{
		return getWeightAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WsPopularCategories.weight</code> attribute. 
	 * @param value the weight
	 */
	public void setWeight(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, "weight".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WsPopularCategories.weight</code> attribute. 
	 * @param value the weight
	 */
	public void setWeight(final Integer value)
	{
		setWeight( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WsPopularCategories.weight</code> attribute. 
	 * @param value the weight
	 */
	public void setWeight(final SessionContext ctx, final int value)
	{
		setWeight( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WsPopularCategories.weight</code> attribute. 
	 * @param value the weight
	 */
	public void setWeight(final int value)
	{
		setWeight( getSession().getSessionContext(), value );
	}
	
}
