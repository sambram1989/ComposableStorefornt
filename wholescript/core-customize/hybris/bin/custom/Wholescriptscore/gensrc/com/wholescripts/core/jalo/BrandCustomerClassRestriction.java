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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type BrandCustomerClassRestriction.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class BrandCustomerClassRestriction extends GenericItem
{
	/** Qualifier of the <code>BrandCustomerClassRestriction.brandCode</code> attribute **/
	public static final String BRANDCODE = "brandCode";
	/** Qualifier of the <code>BrandCustomerClassRestriction.customerClass</code> attribute **/
	public static final String CUSTOMERCLASS = "customerClass";
	/** Qualifier of the <code>BrandCustomerClassRestriction.description</code> attribute **/
	public static final String DESCRIPTION = "description";
	/** Qualifier of the <code>BrandCustomerClassRestriction.validFrom</code> attribute **/
	public static final String VALIDFROM = "validFrom";
	/** Qualifier of the <code>BrandCustomerClassRestriction.validTo</code> attribute **/
	public static final String VALIDTO = "validTo";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(BRANDCODE, AttributeMode.INITIAL);
		tmp.put(CUSTOMERCLASS, AttributeMode.INITIAL);
		tmp.put(DESCRIPTION, AttributeMode.INITIAL);
		tmp.put(VALIDFROM, AttributeMode.INITIAL);
		tmp.put(VALIDTO, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BrandCustomerClassRestriction.brandCode</code> attribute.
	 * @return the brandCode
	 */
	public String getBrandCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "brandCode".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BrandCustomerClassRestriction.brandCode</code> attribute.
	 * @return the brandCode
	 */
	public String getBrandCode()
	{
		return getBrandCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BrandCustomerClassRestriction.brandCode</code> attribute. 
	 * @param value the brandCode
	 */
	public void setBrandCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "brandCode".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BrandCustomerClassRestriction.brandCode</code> attribute. 
	 * @param value the brandCode
	 */
	public void setBrandCode(final String value)
	{
		setBrandCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BrandCustomerClassRestriction.customerClass</code> attribute.
	 * @return the customerClass
	 */
	public String getCustomerClass(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "customerClass".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BrandCustomerClassRestriction.customerClass</code> attribute.
	 * @return the customerClass
	 */
	public String getCustomerClass()
	{
		return getCustomerClass( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BrandCustomerClassRestriction.customerClass</code> attribute. 
	 * @param value the customerClass
	 */
	public void setCustomerClass(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "customerClass".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BrandCustomerClassRestriction.customerClass</code> attribute. 
	 * @param value the customerClass
	 */
	public void setCustomerClass(final String value)
	{
		setCustomerClass( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BrandCustomerClassRestriction.description</code> attribute.
	 * @return the description
	 */
	public String getDescription(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "description".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BrandCustomerClassRestriction.description</code> attribute.
	 * @return the description
	 */
	public String getDescription()
	{
		return getDescription( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BrandCustomerClassRestriction.description</code> attribute. 
	 * @param value the description
	 */
	public void setDescription(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "description".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BrandCustomerClassRestriction.description</code> attribute. 
	 * @param value the description
	 */
	public void setDescription(final String value)
	{
		setDescription( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BrandCustomerClassRestriction.validFrom</code> attribute.
	 * @return the validFrom
	 */
	public Date getValidFrom(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, "validFrom".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BrandCustomerClassRestriction.validFrom</code> attribute.
	 * @return the validFrom
	 */
	public Date getValidFrom()
	{
		return getValidFrom( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BrandCustomerClassRestriction.validFrom</code> attribute. 
	 * @param value the validFrom
	 */
	public void setValidFrom(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, "validFrom".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BrandCustomerClassRestriction.validFrom</code> attribute. 
	 * @param value the validFrom
	 */
	public void setValidFrom(final Date value)
	{
		setValidFrom( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BrandCustomerClassRestriction.validTo</code> attribute.
	 * @return the validTo
	 */
	public Date getValidTo(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, "validTo".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BrandCustomerClassRestriction.validTo</code> attribute.
	 * @return the validTo
	 */
	public Date getValidTo()
	{
		return getValidTo( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BrandCustomerClassRestriction.validTo</code> attribute. 
	 * @param value the validTo
	 */
	public void setValidTo(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, "validTo".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BrandCustomerClassRestriction.validTo</code> attribute. 
	 * @param value the validTo
	 */
	public void setValidTo(final Date value)
	{
		setValidTo( getSession().getSessionContext(), value );
	}
	
}
