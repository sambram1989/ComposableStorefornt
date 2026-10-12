/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 27-May-2025, 11:44:08 pm                    ---
 * ----------------------------------------------------------------
 */
package com.wholescripts.core.jalo;

import com.wholescripts.core.jalo.WholescriptsOutboundRequest;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type RegionProductAgeRestrictionOutboundRequest.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class RegionProductAgeRestrictionOutboundRequest extends WholescriptsOutboundRequest
{
	/** Qualifier of the <code>RegionProductAgeRestrictionOutboundRequest.createdDate</code> attribute **/
	public static final String CREATEDDATE = "createdDate";
	/** Qualifier of the <code>RegionProductAgeRestrictionOutboundRequest.inactiveReg</code> attribute **/
	public static final String INACTIVEREG = "inactiveReg";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(WholescriptsOutboundRequest.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(CREATEDDATE, AttributeMode.INITIAL);
		tmp.put(INACTIVEREG, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RegionProductAgeRestrictionOutboundRequest.createdDate</code> attribute.
	 * @return the createdDate
	 */
	public String getCreatedDate(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "createdDate".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RegionProductAgeRestrictionOutboundRequest.createdDate</code> attribute.
	 * @return the createdDate
	 */
	public String getCreatedDate()
	{
		return getCreatedDate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RegionProductAgeRestrictionOutboundRequest.createdDate</code> attribute. 
	 * @param value the createdDate
	 */
	public void setCreatedDate(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "createdDate".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RegionProductAgeRestrictionOutboundRequest.createdDate</code> attribute. 
	 * @param value the createdDate
	 */
	public void setCreatedDate(final String value)
	{
		setCreatedDate( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RegionProductAgeRestrictionOutboundRequest.inactiveReg</code> attribute.
	 * @return the inactiveReg
	 */
	public String getInactiveReg(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "inactiveReg".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RegionProductAgeRestrictionOutboundRequest.inactiveReg</code> attribute.
	 * @return the inactiveReg
	 */
	public String getInactiveReg()
	{
		return getInactiveReg( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RegionProductAgeRestrictionOutboundRequest.inactiveReg</code> attribute. 
	 * @param value the inactiveReg
	 */
	public void setInactiveReg(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "inactiveReg".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RegionProductAgeRestrictionOutboundRequest.inactiveReg</code> attribute. 
	 * @param value the inactiveReg
	 */
	public void setInactiveReg(final String value)
	{
		setInactiveReg( getSession().getSessionContext(), value );
	}
	
}
