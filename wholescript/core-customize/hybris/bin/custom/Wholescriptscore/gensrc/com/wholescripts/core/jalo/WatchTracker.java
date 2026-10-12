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
 * Generated class for type WatchTracker.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class WatchTracker extends GenericItem
{
	/** Qualifier of the <code>WatchTracker.userId</code> attribute **/
	public static final String USERID = "userId";
	/** Qualifier of the <code>WatchTracker.webinarCode</code> attribute **/
	public static final String WEBINARCODE = "webinarCode";
	/** Qualifier of the <code>WatchTracker.isVideoWatched</code> attribute **/
	public static final String ISVIDEOWATCHED = "isVideoWatched";
	/** Qualifier of the <code>WatchTracker.weight</code> attribute **/
	public static final String WEIGHT = "weight";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(USERID, AttributeMode.INITIAL);
		tmp.put(WEBINARCODE, AttributeMode.INITIAL);
		tmp.put(ISVIDEOWATCHED, AttributeMode.INITIAL);
		tmp.put(WEIGHT, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WatchTracker.isVideoWatched</code> attribute.
	 * @return the isVideoWatched
	 */
	public Boolean isIsVideoWatched(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, "isVideoWatched".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WatchTracker.isVideoWatched</code> attribute.
	 * @return the isVideoWatched
	 */
	public Boolean isIsVideoWatched()
	{
		return isIsVideoWatched( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WatchTracker.isVideoWatched</code> attribute. 
	 * @return the isVideoWatched
	 */
	public boolean isIsVideoWatchedAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isIsVideoWatched( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WatchTracker.isVideoWatched</code> attribute. 
	 * @return the isVideoWatched
	 */
	public boolean isIsVideoWatchedAsPrimitive()
	{
		return isIsVideoWatchedAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WatchTracker.isVideoWatched</code> attribute. 
	 * @param value the isVideoWatched
	 */
	public void setIsVideoWatched(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, "isVideoWatched".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WatchTracker.isVideoWatched</code> attribute. 
	 * @param value the isVideoWatched
	 */
	public void setIsVideoWatched(final Boolean value)
	{
		setIsVideoWatched( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WatchTracker.isVideoWatched</code> attribute. 
	 * @param value the isVideoWatched
	 */
	public void setIsVideoWatched(final SessionContext ctx, final boolean value)
	{
		setIsVideoWatched( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WatchTracker.isVideoWatched</code> attribute. 
	 * @param value the isVideoWatched
	 */
	public void setIsVideoWatched(final boolean value)
	{
		setIsVideoWatched( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WatchTracker.userId</code> attribute.
	 * @return the userId
	 */
	public String getUserId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "userId".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WatchTracker.userId</code> attribute.
	 * @return the userId
	 */
	public String getUserId()
	{
		return getUserId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WatchTracker.userId</code> attribute. 
	 * @param value the userId
	 */
	public void setUserId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "userId".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WatchTracker.userId</code> attribute. 
	 * @param value the userId
	 */
	public void setUserId(final String value)
	{
		setUserId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WatchTracker.webinarCode</code> attribute.
	 * @return the webinarCode
	 */
	public String getWebinarCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "webinarCode".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WatchTracker.webinarCode</code> attribute.
	 * @return the webinarCode
	 */
	public String getWebinarCode()
	{
		return getWebinarCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WatchTracker.webinarCode</code> attribute. 
	 * @param value the webinarCode
	 */
	public void setWebinarCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "webinarCode".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WatchTracker.webinarCode</code> attribute. 
	 * @param value the webinarCode
	 */
	public void setWebinarCode(final String value)
	{
		setWebinarCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WatchTracker.weight</code> attribute.
	 * @return the weight
	 */
	public Integer getWeight(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, "weight".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WatchTracker.weight</code> attribute.
	 * @return the weight
	 */
	public Integer getWeight()
	{
		return getWeight( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WatchTracker.weight</code> attribute. 
	 * @return the weight
	 */
	public int getWeightAsPrimitive(final SessionContext ctx)
	{
		Integer value = getWeight( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WatchTracker.weight</code> attribute. 
	 * @return the weight
	 */
	public int getWeightAsPrimitive()
	{
		return getWeightAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WatchTracker.weight</code> attribute. 
	 * @param value the weight
	 */
	public void setWeight(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, "weight".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WatchTracker.weight</code> attribute. 
	 * @param value the weight
	 */
	public void setWeight(final Integer value)
	{
		setWeight( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WatchTracker.weight</code> attribute. 
	 * @param value the weight
	 */
	public void setWeight(final SessionContext ctx, final int value)
	{
		setWeight( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WatchTracker.weight</code> attribute. 
	 * @param value the weight
	 */
	public void setWeight(final int value)
	{
		setWeight( getSession().getSessionContext(), value );
	}
	
}
