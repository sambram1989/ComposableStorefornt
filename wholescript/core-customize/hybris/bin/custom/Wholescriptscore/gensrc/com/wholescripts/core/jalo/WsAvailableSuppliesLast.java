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
 * Generated class for type WsAvailableSuppliesLast.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class WsAvailableSuppliesLast extends GenericItem
{
	/** Qualifier of the <code>WsAvailableSuppliesLast.availableSuppliesLastSku</code> attribute **/
	public static final String AVAILABLESUPPLIESLASTSKU = "availableSuppliesLastSku";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(AVAILABLESUPPLIESLASTSKU, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WsAvailableSuppliesLast.availableSuppliesLastSku</code> attribute.
	 * @return the availableSuppliesLastSku
	 */
	public String getAvailableSuppliesLastSku(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "availableSuppliesLastSku".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WsAvailableSuppliesLast.availableSuppliesLastSku</code> attribute.
	 * @return the availableSuppliesLastSku
	 */
	public String getAvailableSuppliesLastSku()
	{
		return getAvailableSuppliesLastSku( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WsAvailableSuppliesLast.availableSuppliesLastSku</code> attribute. 
	 * @param value the availableSuppliesLastSku
	 */
	public void setAvailableSuppliesLastSku(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "availableSuppliesLastSku".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WsAvailableSuppliesLast.availableSuppliesLastSku</code> attribute. 
	 * @param value the availableSuppliesLastSku
	 */
	public void setAvailableSuppliesLastSku(final String value)
	{
		setAvailableSuppliesLastSku( getSession().getSessionContext(), value );
	}
	
}
