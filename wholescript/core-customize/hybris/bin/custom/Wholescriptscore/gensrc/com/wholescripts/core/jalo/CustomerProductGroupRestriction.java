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
 * Generated class for type CustomerProductGroupRestriction.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class CustomerProductGroupRestriction extends GenericItem
{
	/** Qualifier of the <code>CustomerProductGroupRestriction.customerSAPId</code> attribute **/
	public static final String CUSTOMERSAPID = "customerSAPId";
	/** Qualifier of the <code>CustomerProductGroupRestriction.productGroup</code> attribute **/
	public static final String PRODUCTGROUP = "productGroup";
	/** Qualifier of the <code>CustomerProductGroupRestriction.validFrom</code> attribute **/
	public static final String VALIDFROM = "validFrom";
	/** Qualifier of the <code>CustomerProductGroupRestriction.validTo</code> attribute **/
	public static final String VALIDTO = "validTo";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CUSTOMERSAPID, AttributeMode.INITIAL);
		tmp.put(PRODUCTGROUP, AttributeMode.INITIAL);
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
	 * <i>Generated method</i> - Getter of the <code>CustomerProductGroupRestriction.customerSAPId</code> attribute.
	 * @return the customerSAPId
	 */
	public String getCustomerSAPId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "customerSAPId".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerProductGroupRestriction.customerSAPId</code> attribute.
	 * @return the customerSAPId
	 */
	public String getCustomerSAPId()
	{
		return getCustomerSAPId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerProductGroupRestriction.customerSAPId</code> attribute. 
	 * @param value the customerSAPId
	 */
	public void setCustomerSAPId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "customerSAPId".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerProductGroupRestriction.customerSAPId</code> attribute. 
	 * @param value the customerSAPId
	 */
	public void setCustomerSAPId(final String value)
	{
		setCustomerSAPId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerProductGroupRestriction.productGroup</code> attribute.
	 * @return the productGroup
	 */
	public String getProductGroup(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "productGroup".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerProductGroupRestriction.productGroup</code> attribute.
	 * @return the productGroup
	 */
	public String getProductGroup()
	{
		return getProductGroup( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerProductGroupRestriction.productGroup</code> attribute. 
	 * @param value the productGroup
	 */
	public void setProductGroup(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "productGroup".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerProductGroupRestriction.productGroup</code> attribute. 
	 * @param value the productGroup
	 */
	public void setProductGroup(final String value)
	{
		setProductGroup( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerProductGroupRestriction.validFrom</code> attribute.
	 * @return the validFrom
	 */
	public Date getValidFrom(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, "validFrom".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerProductGroupRestriction.validFrom</code> attribute.
	 * @return the validFrom
	 */
	public Date getValidFrom()
	{
		return getValidFrom( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerProductGroupRestriction.validFrom</code> attribute. 
	 * @param value the validFrom
	 */
	public void setValidFrom(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, "validFrom".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerProductGroupRestriction.validFrom</code> attribute. 
	 * @param value the validFrom
	 */
	public void setValidFrom(final Date value)
	{
		setValidFrom( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerProductGroupRestriction.validTo</code> attribute.
	 * @return the validTo
	 */
	public Date getValidTo(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, "validTo".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerProductGroupRestriction.validTo</code> attribute.
	 * @return the validTo
	 */
	public Date getValidTo()
	{
		return getValidTo( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerProductGroupRestriction.validTo</code> attribute. 
	 * @param value the validTo
	 */
	public void setValidTo(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, "validTo".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerProductGroupRestriction.validTo</code> attribute. 
	 * @param value the validTo
	 */
	public void setValidTo(final Date value)
	{
		setValidTo( getSession().getSessionContext(), value );
	}
	
}
