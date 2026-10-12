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
 * Generated class for type MaterialRestrictionRequest.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class MaterialRestrictionRequest extends WholescriptsOutboundRequest
{
	/** Qualifier of the <code>MaterialRestrictionRequest.exclusion</code> attribute **/
	public static final String EXCLUSION = "exclusion";
	/** Qualifier of the <code>MaterialRestrictionRequest.inclusion</code> attribute **/
	public static final String INCLUSION = "inclusion";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(WholescriptsOutboundRequest.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(EXCLUSION, AttributeMode.INITIAL);
		tmp.put(INCLUSION, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MaterialRestrictionRequest.exclusion</code> attribute.
	 * @return the exclusion
	 */
	public String getExclusion(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "exclusion".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MaterialRestrictionRequest.exclusion</code> attribute.
	 * @return the exclusion
	 */
	public String getExclusion()
	{
		return getExclusion( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MaterialRestrictionRequest.exclusion</code> attribute. 
	 * @param value the exclusion
	 */
	public void setExclusion(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "exclusion".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MaterialRestrictionRequest.exclusion</code> attribute. 
	 * @param value the exclusion
	 */
	public void setExclusion(final String value)
	{
		setExclusion( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MaterialRestrictionRequest.inclusion</code> attribute.
	 * @return the inclusion
	 */
	public String getInclusion(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "inclusion".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MaterialRestrictionRequest.inclusion</code> attribute.
	 * @return the inclusion
	 */
	public String getInclusion()
	{
		return getInclusion( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MaterialRestrictionRequest.inclusion</code> attribute. 
	 * @param value the inclusion
	 */
	public void setInclusion(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "inclusion".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MaterialRestrictionRequest.inclusion</code> attribute. 
	 * @param value the inclusion
	 */
	public void setInclusion(final String value)
	{
		setInclusion( getSession().getSessionContext(), value );
	}
	
}
