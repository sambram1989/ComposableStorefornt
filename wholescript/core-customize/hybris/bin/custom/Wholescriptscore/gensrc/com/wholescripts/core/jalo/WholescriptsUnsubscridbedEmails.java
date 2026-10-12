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
 * Generated class for type WholescriptsUnsubscridbedEmails.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class WholescriptsUnsubscridbedEmails extends GenericItem
{
	/** Qualifier of the <code>WholescriptsUnsubscridbedEmails.emailAddress</code> attribute **/
	public static final String EMAILADDRESS = "emailAddress";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(EMAILADDRESS, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsUnsubscridbedEmails.emailAddress</code> attribute.
	 * @return the emailAddress - email address which has to be unsubscribed
	 */
	public String getEmailAddress(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "emailAddress".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsUnsubscridbedEmails.emailAddress</code> attribute.
	 * @return the emailAddress - email address which has to be unsubscribed
	 */
	public String getEmailAddress()
	{
		return getEmailAddress( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsUnsubscridbedEmails.emailAddress</code> attribute. 
	 * @param value the emailAddress - email address which has to be unsubscribed
	 */
	public void setEmailAddress(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "emailAddress".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsUnsubscridbedEmails.emailAddress</code> attribute. 
	 * @param value the emailAddress - email address which has to be unsubscribed
	 */
	public void setEmailAddress(final String value)
	{
		setEmailAddress( getSession().getSessionContext(), value );
	}
	
}
