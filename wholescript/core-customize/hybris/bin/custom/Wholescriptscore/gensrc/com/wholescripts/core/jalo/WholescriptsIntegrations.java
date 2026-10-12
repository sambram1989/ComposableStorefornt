/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 27-May-2025, 11:44:08 pm                    ---
 * ----------------------------------------------------------------
 */
package com.wholescripts.core.jalo;

import com.wholescripts.core.constants.WholescriptscoreConstants;
import de.hybris.platform.b2b.jalo.B2BCustomer;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type WholescriptsIntegrations.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class WholescriptsIntegrations extends GenericItem
{
	/** Qualifier of the <code>WholescriptsIntegrations.userId</code> attribute **/
	public static final String USERID = "userId";
	/** Qualifier of the <code>WholescriptsIntegrations.request</code> attribute **/
	public static final String REQUEST = "request";
	/** Qualifier of the <code>WholescriptsIntegrations.response</code> attribute **/
	public static final String RESPONSE = "response";
	/** Qualifier of the <code>WholescriptsIntegrations.status</code> attribute **/
	public static final String STATUS = "status";
	/** Qualifier of the <code>WholescriptsIntegrations.destinationName</code> attribute **/
	public static final String DESTINATIONNAME = "destinationName";
	/** Qualifier of the <code>WholescriptsIntegrations.customer</code> attribute **/
	public static final String CUSTOMER = "customer";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n CUSTOMER's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<WholescriptsIntegrations> CUSTOMERHANDLER = new BidirectionalOneToManyHandler<WholescriptsIntegrations>(
	WholescriptscoreConstants.TC.WHOLESCRIPTSINTEGRATIONS,
	false,
	"customer",
	null,
	false,
	true,
	CollectionType.LIST
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(USERID, AttributeMode.INITIAL);
		tmp.put(REQUEST, AttributeMode.INITIAL);
		tmp.put(RESPONSE, AttributeMode.INITIAL);
		tmp.put(STATUS, AttributeMode.INITIAL);
		tmp.put(DESTINATIONNAME, AttributeMode.INITIAL);
		tmp.put(CUSTOMER, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		CUSTOMERHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsIntegrations.customer</code> attribute.
	 * @return the customer
	 */
	public B2BCustomer getCustomer(final SessionContext ctx)
	{
		return (B2BCustomer)getProperty( ctx, "customer".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsIntegrations.customer</code> attribute.
	 * @return the customer
	 */
	public B2BCustomer getCustomer()
	{
		return getCustomer( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsIntegrations.customer</code> attribute. 
	 * @param value the customer
	 */
	public void setCustomer(final SessionContext ctx, final B2BCustomer value)
	{
		CUSTOMERHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsIntegrations.customer</code> attribute. 
	 * @param value the customer
	 */
	public void setCustomer(final B2BCustomer value)
	{
		setCustomer( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsIntegrations.destinationName</code> attribute.
	 * @return the destinationName
	 */
	public String getDestinationName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "destinationName".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsIntegrations.destinationName</code> attribute.
	 * @return the destinationName
	 */
	public String getDestinationName()
	{
		return getDestinationName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsIntegrations.destinationName</code> attribute. 
	 * @param value the destinationName
	 */
	public void setDestinationName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "destinationName".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsIntegrations.destinationName</code> attribute. 
	 * @param value the destinationName
	 */
	public void setDestinationName(final String value)
	{
		setDestinationName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsIntegrations.request</code> attribute.
	 * @return the request - code
	 */
	public String getRequest(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "request".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsIntegrations.request</code> attribute.
	 * @return the request - code
	 */
	public String getRequest()
	{
		return getRequest( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsIntegrations.request</code> attribute. 
	 * @param value the request - code
	 */
	public void setRequest(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "request".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsIntegrations.request</code> attribute. 
	 * @param value the request - code
	 */
	public void setRequest(final String value)
	{
		setRequest( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsIntegrations.response</code> attribute.
	 * @return the response - code
	 */
	public String getResponse(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "response".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsIntegrations.response</code> attribute.
	 * @return the response - code
	 */
	public String getResponse()
	{
		return getResponse( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsIntegrations.response</code> attribute. 
	 * @param value the response - code
	 */
	public void setResponse(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "response".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsIntegrations.response</code> attribute. 
	 * @param value the response - code
	 */
	public void setResponse(final String value)
	{
		setResponse( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsIntegrations.status</code> attribute.
	 * @return the status
	 */
	public String getStatus(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "status".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsIntegrations.status</code> attribute.
	 * @return the status
	 */
	public String getStatus()
	{
		return getStatus( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsIntegrations.status</code> attribute. 
	 * @param value the status
	 */
	public void setStatus(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "status".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsIntegrations.status</code> attribute. 
	 * @param value the status
	 */
	public void setStatus(final String value)
	{
		setStatus( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsIntegrations.userId</code> attribute.
	 * @return the userId - code
	 */
	public User getUserId(final SessionContext ctx)
	{
		return (User)getProperty( ctx, "userId".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsIntegrations.userId</code> attribute.
	 * @return the userId - code
	 */
	public User getUserId()
	{
		return getUserId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsIntegrations.userId</code> attribute. 
	 * @param value the userId - code
	 */
	public void setUserId(final SessionContext ctx, final User value)
	{
		setProperty(ctx, "userId".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsIntegrations.userId</code> attribute. 
	 * @param value the userId - code
	 */
	public void setUserId(final User value)
	{
		setUserId( getSession().getSessionContext(), value );
	}
	
}
