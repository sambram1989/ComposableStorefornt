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
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type WSContactUsForm.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class WSContactUsForm extends GenericItem
{
	/** Qualifier of the <code>WSContactUsForm.contactFirstName</code> attribute **/
	public static final String CONTACTFIRSTNAME = "contactFirstName";
	/** Qualifier of the <code>WSContactUsForm.contactLastName</code> attribute **/
	public static final String CONTACTLASTNAME = "contactLastName";
	/** Qualifier of the <code>WSContactUsForm.contactPhone</code> attribute **/
	public static final String CONTACTPHONE = "contactPhone";
	/** Qualifier of the <code>WSContactUsForm.contactEmail</code> attribute **/
	public static final String CONTACTEMAIL = "contactEmail";
	/** Qualifier of the <code>WSContactUsForm.contactAccountNumber</code> attribute **/
	public static final String CONTACTACCOUNTNUMBER = "contactAccountNumber";
	/** Qualifier of the <code>WSContactUsForm.contactSelectCat</code> attribute **/
	public static final String CONTACTSELECTCAT = "contactSelectCat";
	/** Qualifier of the <code>WSContactUsForm.contactMesssage</code> attribute **/
	public static final String CONTACTMESSSAGE = "contactMesssage";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CONTACTFIRSTNAME, AttributeMode.INITIAL);
		tmp.put(CONTACTLASTNAME, AttributeMode.INITIAL);
		tmp.put(CONTACTPHONE, AttributeMode.INITIAL);
		tmp.put(CONTACTEMAIL, AttributeMode.INITIAL);
		tmp.put(CONTACTACCOUNTNUMBER, AttributeMode.INITIAL);
		tmp.put(CONTACTSELECTCAT, AttributeMode.INITIAL);
		tmp.put(CONTACTMESSSAGE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WSContactUsForm.contactAccountNumber</code> attribute.
	 * @return the contactAccountNumber
	 */
	public String getContactAccountNumber(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "contactAccountNumber".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WSContactUsForm.contactAccountNumber</code> attribute.
	 * @return the contactAccountNumber
	 */
	public String getContactAccountNumber()
	{
		return getContactAccountNumber( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WSContactUsForm.contactAccountNumber</code> attribute. 
	 * @param value the contactAccountNumber
	 */
	public void setContactAccountNumber(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "contactAccountNumber".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WSContactUsForm.contactAccountNumber</code> attribute. 
	 * @param value the contactAccountNumber
	 */
	public void setContactAccountNumber(final String value)
	{
		setContactAccountNumber( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WSContactUsForm.contactEmail</code> attribute.
	 * @return the contactEmail
	 */
	public String getContactEmail(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "contactEmail".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WSContactUsForm.contactEmail</code> attribute.
	 * @return the contactEmail
	 */
	public String getContactEmail()
	{
		return getContactEmail( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WSContactUsForm.contactEmail</code> attribute. 
	 * @param value the contactEmail
	 */
	public void setContactEmail(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "contactEmail".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WSContactUsForm.contactEmail</code> attribute. 
	 * @param value the contactEmail
	 */
	public void setContactEmail(final String value)
	{
		setContactEmail( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WSContactUsForm.contactFirstName</code> attribute.
	 * @return the contactFirstName
	 */
	public String getContactFirstName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "contactFirstName".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WSContactUsForm.contactFirstName</code> attribute.
	 * @return the contactFirstName
	 */
	public String getContactFirstName()
	{
		return getContactFirstName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WSContactUsForm.contactFirstName</code> attribute. 
	 * @param value the contactFirstName
	 */
	public void setContactFirstName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "contactFirstName".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WSContactUsForm.contactFirstName</code> attribute. 
	 * @param value the contactFirstName
	 */
	public void setContactFirstName(final String value)
	{
		setContactFirstName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WSContactUsForm.contactLastName</code> attribute.
	 * @return the contactLastName
	 */
	public String getContactLastName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "contactLastName".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WSContactUsForm.contactLastName</code> attribute.
	 * @return the contactLastName
	 */
	public String getContactLastName()
	{
		return getContactLastName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WSContactUsForm.contactLastName</code> attribute. 
	 * @param value the contactLastName
	 */
	public void setContactLastName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "contactLastName".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WSContactUsForm.contactLastName</code> attribute. 
	 * @param value the contactLastName
	 */
	public void setContactLastName(final String value)
	{
		setContactLastName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WSContactUsForm.contactMesssage</code> attribute.
	 * @return the contactMesssage
	 */
	public String getContactMesssage(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "contactMesssage".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WSContactUsForm.contactMesssage</code> attribute.
	 * @return the contactMesssage
	 */
	public String getContactMesssage()
	{
		return getContactMesssage( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WSContactUsForm.contactMesssage</code> attribute. 
	 * @param value the contactMesssage
	 */
	public void setContactMesssage(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "contactMesssage".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WSContactUsForm.contactMesssage</code> attribute. 
	 * @param value the contactMesssage
	 */
	public void setContactMesssage(final String value)
	{
		setContactMesssage( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WSContactUsForm.contactPhone</code> attribute.
	 * @return the contactPhone
	 */
	public String getContactPhone(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "contactPhone".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WSContactUsForm.contactPhone</code> attribute.
	 * @return the contactPhone
	 */
	public String getContactPhone()
	{
		return getContactPhone( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WSContactUsForm.contactPhone</code> attribute. 
	 * @param value the contactPhone
	 */
	public void setContactPhone(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "contactPhone".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WSContactUsForm.contactPhone</code> attribute. 
	 * @param value the contactPhone
	 */
	public void setContactPhone(final String value)
	{
		setContactPhone( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WSContactUsForm.contactSelectCat</code> attribute.
	 * @return the contactSelectCat
	 */
	public EnumerationValue getContactSelectCat(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, "contactSelectCat".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WSContactUsForm.contactSelectCat</code> attribute.
	 * @return the contactSelectCat
	 */
	public EnumerationValue getContactSelectCat()
	{
		return getContactSelectCat( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WSContactUsForm.contactSelectCat</code> attribute. 
	 * @param value the contactSelectCat
	 */
	public void setContactSelectCat(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, "contactSelectCat".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WSContactUsForm.contactSelectCat</code> attribute. 
	 * @param value the contactSelectCat
	 */
	public void setContactSelectCat(final EnumerationValue value)
	{
		setContactSelectCat( getSession().getSessionContext(), value );
	}
	
}
