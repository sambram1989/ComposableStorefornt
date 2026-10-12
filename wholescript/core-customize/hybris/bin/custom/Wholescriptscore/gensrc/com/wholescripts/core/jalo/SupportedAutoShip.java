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
 * Generated class for type SupportedAutoShip.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class SupportedAutoShip extends GenericItem
{
	/** Qualifier of the <code>SupportedAutoShip.code</code> attribute **/
	public static final String CODE = "code";
	/** Qualifier of the <code>SupportedAutoShip.name</code> attribute **/
	public static final String NAME = "name";
	/** Qualifier of the <code>SupportedAutoShip.priority</code> attribute **/
	public static final String PRIORITY = "priority";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CODE, AttributeMode.INITIAL);
		tmp.put(NAME, AttributeMode.INITIAL);
		tmp.put(PRIORITY, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SupportedAutoShip.code</code> attribute.
	 * @return the code - code
	 */
	public String getCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "code".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SupportedAutoShip.code</code> attribute.
	 * @return the code - code
	 */
	public String getCode()
	{
		return getCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SupportedAutoShip.code</code> attribute. 
	 * @param value the code - code
	 */
	public void setCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "code".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SupportedAutoShip.code</code> attribute. 
	 * @param value the code - code
	 */
	public void setCode(final String value)
	{
		setCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SupportedAutoShip.name</code> attribute.
	 * @return the name - name
	 */
	public String getName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "name".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SupportedAutoShip.name</code> attribute.
	 * @return the name - name
	 */
	public String getName()
	{
		return getName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SupportedAutoShip.name</code> attribute. 
	 * @param value the name - name
	 */
	public void setName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "name".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SupportedAutoShip.name</code> attribute. 
	 * @param value the name - name
	 */
	public void setName(final String value)
	{
		setName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SupportedAutoShip.priority</code> attribute.
	 * @return the priority - priority
	 */
	public String getPriority(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "priority".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SupportedAutoShip.priority</code> attribute.
	 * @return the priority - priority
	 */
	public String getPriority()
	{
		return getPriority( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SupportedAutoShip.priority</code> attribute. 
	 * @param value the priority - priority
	 */
	public void setPriority(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "priority".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SupportedAutoShip.priority</code> attribute. 
	 * @param value the priority - priority
	 */
	public void setPriority(final String value)
	{
		setPriority( getSession().getSessionContext(), value );
	}
	
}
