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
import de.hybris.platform.servicelayer.internal.jalo.ServicelayerJob;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type WholescriptsOutboundSyncJob.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class WholescriptsOutboundSyncJob extends ServicelayerJob
{
	/** Qualifier of the <code>WholescriptsOutboundSyncJob.wholescriptsOutboundRequest</code> attribute **/
	public static final String WHOLESCRIPTSOUTBOUNDREQUEST = "wholescriptsOutboundRequest";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(ServicelayerJob.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(WHOLESCRIPTSOUTBOUNDREQUEST, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsOutboundSyncJob.wholescriptsOutboundRequest</code> attribute.
	 * @return the wholescriptsOutboundRequest
	 */
	public WholescriptsOutboundRequest getWholescriptsOutboundRequest(final SessionContext ctx)
	{
		return (WholescriptsOutboundRequest)getProperty( ctx, "wholescriptsOutboundRequest".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsOutboundSyncJob.wholescriptsOutboundRequest</code> attribute.
	 * @return the wholescriptsOutboundRequest
	 */
	public WholescriptsOutboundRequest getWholescriptsOutboundRequest()
	{
		return getWholescriptsOutboundRequest( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsOutboundSyncJob.wholescriptsOutboundRequest</code> attribute. 
	 * @param value the wholescriptsOutboundRequest
	 */
	public void setWholescriptsOutboundRequest(final SessionContext ctx, final WholescriptsOutboundRequest value)
	{
		setProperty(ctx, "wholescriptsOutboundRequest".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsOutboundSyncJob.wholescriptsOutboundRequest</code> attribute. 
	 * @param value the wholescriptsOutboundRequest
	 */
	public void setWholescriptsOutboundRequest(final WholescriptsOutboundRequest value)
	{
		setWholescriptsOutboundRequest( getSession().getSessionContext(), value );
	}
	
}
