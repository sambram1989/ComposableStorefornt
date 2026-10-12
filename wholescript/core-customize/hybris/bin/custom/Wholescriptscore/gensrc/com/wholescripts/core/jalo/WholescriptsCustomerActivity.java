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
import de.hybris.platform.jalo.user.Customer;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type WholescriptsCustomerActivity.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class WholescriptsCustomerActivity extends GenericItem
{
	/** Qualifier of the <code>WholescriptsCustomerActivity.eventName</code> attribute **/
	public static final String EVENTNAME = "eventName";
	/** Qualifier of the <code>WholescriptsCustomerActivity.customer</code> attribute **/
	public static final String CUSTOMER = "customer";
	/** Qualifier of the <code>WholescriptsCustomerActivity.ipAddress</code> attribute **/
	public static final String IPADDRESS = "ipAddress";
	/** Qualifier of the <code>WholescriptsCustomerActivity.message</code> attribute **/
	public static final String MESSAGE = "message";
	/** Qualifier of the <code>WholescriptsCustomerActivity.createdOn</code> attribute **/
	public static final String CREATEDON = "createdOn";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(EVENTNAME, AttributeMode.INITIAL);
		tmp.put(CUSTOMER, AttributeMode.INITIAL);
		tmp.put(IPADDRESS, AttributeMode.INITIAL);
		tmp.put(MESSAGE, AttributeMode.INITIAL);
		tmp.put(CREATEDON, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsCustomerActivity.createdOn</code> attribute.
	 * @return the createdOn
	 */
	public Date getCreatedOn(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, "createdOn".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsCustomerActivity.createdOn</code> attribute.
	 * @return the createdOn
	 */
	public Date getCreatedOn()
	{
		return getCreatedOn( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsCustomerActivity.createdOn</code> attribute. 
	 * @param value the createdOn
	 */
	public void setCreatedOn(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, "createdOn".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsCustomerActivity.createdOn</code> attribute. 
	 * @param value the createdOn
	 */
	public void setCreatedOn(final Date value)
	{
		setCreatedOn( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsCustomerActivity.customer</code> attribute.
	 * @return the customer
	 */
	public Customer getCustomer(final SessionContext ctx)
	{
		return (Customer)getProperty( ctx, "customer".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsCustomerActivity.customer</code> attribute.
	 * @return the customer
	 */
	public Customer getCustomer()
	{
		return getCustomer( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsCustomerActivity.customer</code> attribute. 
	 * @param value the customer
	 */
	public void setCustomer(final SessionContext ctx, final Customer value)
	{
		setProperty(ctx, "customer".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsCustomerActivity.customer</code> attribute. 
	 * @param value the customer
	 */
	public void setCustomer(final Customer value)
	{
		setCustomer( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsCustomerActivity.eventName</code> attribute.
	 * @return the eventName
	 */
	public String getEventName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "eventName".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsCustomerActivity.eventName</code> attribute.
	 * @return the eventName
	 */
	public String getEventName()
	{
		return getEventName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsCustomerActivity.eventName</code> attribute. 
	 * @param value the eventName
	 */
	public void setEventName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "eventName".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsCustomerActivity.eventName</code> attribute. 
	 * @param value the eventName
	 */
	public void setEventName(final String value)
	{
		setEventName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsCustomerActivity.ipAddress</code> attribute.
	 * @return the ipAddress
	 */
	public String getIpAddress(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "ipAddress".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsCustomerActivity.ipAddress</code> attribute.
	 * @return the ipAddress
	 */
	public String getIpAddress()
	{
		return getIpAddress( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsCustomerActivity.ipAddress</code> attribute. 
	 * @param value the ipAddress
	 */
	public void setIpAddress(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "ipAddress".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsCustomerActivity.ipAddress</code> attribute. 
	 * @param value the ipAddress
	 */
	public void setIpAddress(final String value)
	{
		setIpAddress( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsCustomerActivity.message</code> attribute.
	 * @return the message
	 */
	public String getMessage(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "message".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsCustomerActivity.message</code> attribute.
	 * @return the message
	 */
	public String getMessage()
	{
		return getMessage( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsCustomerActivity.message</code> attribute. 
	 * @param value the message
	 */
	public void setMessage(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "message".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsCustomerActivity.message</code> attribute. 
	 * @param value the message
	 */
	public void setMessage(final String value)
	{
		setMessage( getSession().getSessionContext(), value );
	}
	
}
