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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type WsDiscontinuedProducts.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class WsDiscontinuedProducts extends GenericItem
{
	/** Qualifier of the <code>WsDiscontinuedProducts.discontinuedProductsSku</code> attribute **/
	public static final String DISCONTINUEDPRODUCTSSKU = "discontinuedProductsSku";
	/** Qualifier of the <code>WsDiscontinuedProducts.discontinuedDate</code> attribute **/
	public static final String DISCONTINUEDDATE = "discontinuedDate";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(DISCONTINUEDPRODUCTSSKU, AttributeMode.INITIAL);
		tmp.put(DISCONTINUEDDATE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WsDiscontinuedProducts.discontinuedDate</code> attribute.
	 * @return the discontinuedDate
	 */
	public Date getDiscontinuedDate(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, "discontinuedDate".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WsDiscontinuedProducts.discontinuedDate</code> attribute.
	 * @return the discontinuedDate
	 */
	public Date getDiscontinuedDate()
	{
		return getDiscontinuedDate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WsDiscontinuedProducts.discontinuedDate</code> attribute. 
	 * @param value the discontinuedDate
	 */
	public void setDiscontinuedDate(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, "discontinuedDate".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WsDiscontinuedProducts.discontinuedDate</code> attribute. 
	 * @param value the discontinuedDate
	 */
	public void setDiscontinuedDate(final Date value)
	{
		setDiscontinuedDate( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WsDiscontinuedProducts.discontinuedProductsSku</code> attribute.
	 * @return the discontinuedProductsSku
	 */
	public String getDiscontinuedProductsSku(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "discontinuedProductsSku".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WsDiscontinuedProducts.discontinuedProductsSku</code> attribute.
	 * @return the discontinuedProductsSku
	 */
	public String getDiscontinuedProductsSku()
	{
		return getDiscontinuedProductsSku( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WsDiscontinuedProducts.discontinuedProductsSku</code> attribute. 
	 * @param value the discontinuedProductsSku
	 */
	public void setDiscontinuedProductsSku(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "discontinuedProductsSku".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WsDiscontinuedProducts.discontinuedProductsSku</code> attribute. 
	 * @param value the discontinuedProductsSku
	 */
	public void setDiscontinuedProductsSku(final String value)
	{
		setDiscontinuedProductsSku( getSession().getSessionContext(), value );
	}
	
}
