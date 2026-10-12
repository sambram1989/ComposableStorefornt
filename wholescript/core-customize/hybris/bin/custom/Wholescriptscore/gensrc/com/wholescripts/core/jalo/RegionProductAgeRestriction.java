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
 * Generated class for type RegionProductAgeRestriction.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class RegionProductAgeRestriction extends GenericItem
{
	/** Qualifier of the <code>RegionProductAgeRestriction.regionCode</code> attribute **/
	public static final String REGIONCODE = "regionCode";
	/** Qualifier of the <code>RegionProductAgeRestriction.countryCode</code> attribute **/
	public static final String COUNTRYCODE = "countryCode";
	/** Qualifier of the <code>RegionProductAgeRestriction.age</code> attribute **/
	public static final String AGE = "age";
	/** Qualifier of the <code>RegionProductAgeRestriction.name</code> attribute **/
	public static final String NAME = "name";
	/** Qualifier of the <code>RegionProductAgeRestriction.description</code> attribute **/
	public static final String DESCRIPTION = "description";
	/** Qualifier of the <code>RegionProductAgeRestriction.productCode</code> attribute **/
	public static final String PRODUCTCODE = "productCode";
	/** Qualifier of the <code>RegionProductAgeRestriction.scExclusionRule</code> attribute **/
	public static final String SCEXCLUSIONRULE = "scExclusionRule";
	/** Qualifier of the <code>RegionProductAgeRestriction.inActive</code> attribute **/
	public static final String INACTIVE = "inActive";
	/** Qualifier of the <code>RegionProductAgeRestriction.validFrom</code> attribute **/
	public static final String VALIDFROM = "validFrom";
	/** Qualifier of the <code>RegionProductAgeRestriction.validTo</code> attribute **/
	public static final String VALIDTO = "validTo";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(REGIONCODE, AttributeMode.INITIAL);
		tmp.put(COUNTRYCODE, AttributeMode.INITIAL);
		tmp.put(AGE, AttributeMode.INITIAL);
		tmp.put(NAME, AttributeMode.INITIAL);
		tmp.put(DESCRIPTION, AttributeMode.INITIAL);
		tmp.put(PRODUCTCODE, AttributeMode.INITIAL);
		tmp.put(SCEXCLUSIONRULE, AttributeMode.INITIAL);
		tmp.put(INACTIVE, AttributeMode.INITIAL);
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
	 * <i>Generated method</i> - Getter of the <code>RegionProductAgeRestriction.age</code> attribute.
	 * @return the age
	 */
	public String getAge(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "age".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RegionProductAgeRestriction.age</code> attribute.
	 * @return the age
	 */
	public String getAge()
	{
		return getAge( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RegionProductAgeRestriction.age</code> attribute. 
	 * @param value the age
	 */
	public void setAge(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "age".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RegionProductAgeRestriction.age</code> attribute. 
	 * @param value the age
	 */
	public void setAge(final String value)
	{
		setAge( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RegionProductAgeRestriction.countryCode</code> attribute.
	 * @return the countryCode
	 */
	public String getCountryCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "countryCode".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RegionProductAgeRestriction.countryCode</code> attribute.
	 * @return the countryCode
	 */
	public String getCountryCode()
	{
		return getCountryCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RegionProductAgeRestriction.countryCode</code> attribute. 
	 * @param value the countryCode
	 */
	public void setCountryCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "countryCode".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RegionProductAgeRestriction.countryCode</code> attribute. 
	 * @param value the countryCode
	 */
	public void setCountryCode(final String value)
	{
		setCountryCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RegionProductAgeRestriction.description</code> attribute.
	 * @return the description
	 */
	public String getDescription(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "description".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RegionProductAgeRestriction.description</code> attribute.
	 * @return the description
	 */
	public String getDescription()
	{
		return getDescription( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RegionProductAgeRestriction.description</code> attribute. 
	 * @param value the description
	 */
	public void setDescription(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "description".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RegionProductAgeRestriction.description</code> attribute. 
	 * @param value the description
	 */
	public void setDescription(final String value)
	{
		setDescription( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RegionProductAgeRestriction.inActive</code> attribute.
	 * @return the inActive
	 */
	public String getInActive(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "inActive".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RegionProductAgeRestriction.inActive</code> attribute.
	 * @return the inActive
	 */
	public String getInActive()
	{
		return getInActive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RegionProductAgeRestriction.inActive</code> attribute. 
	 * @param value the inActive
	 */
	public void setInActive(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "inActive".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RegionProductAgeRestriction.inActive</code> attribute. 
	 * @param value the inActive
	 */
	public void setInActive(final String value)
	{
		setInActive( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RegionProductAgeRestriction.name</code> attribute.
	 * @return the name
	 */
	public String getName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "name".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RegionProductAgeRestriction.name</code> attribute.
	 * @return the name
	 */
	public String getName()
	{
		return getName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RegionProductAgeRestriction.name</code> attribute. 
	 * @param value the name
	 */
	public void setName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "name".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RegionProductAgeRestriction.name</code> attribute. 
	 * @param value the name
	 */
	public void setName(final String value)
	{
		setName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RegionProductAgeRestriction.productCode</code> attribute.
	 * @return the productCode
	 */
	public String getProductCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "productCode".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RegionProductAgeRestriction.productCode</code> attribute.
	 * @return the productCode
	 */
	public String getProductCode()
	{
		return getProductCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RegionProductAgeRestriction.productCode</code> attribute. 
	 * @param value the productCode
	 */
	public void setProductCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "productCode".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RegionProductAgeRestriction.productCode</code> attribute. 
	 * @param value the productCode
	 */
	public void setProductCode(final String value)
	{
		setProductCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RegionProductAgeRestriction.regionCode</code> attribute.
	 * @return the regionCode
	 */
	public String getRegionCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "regionCode".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RegionProductAgeRestriction.regionCode</code> attribute.
	 * @return the regionCode
	 */
	public String getRegionCode()
	{
		return getRegionCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RegionProductAgeRestriction.regionCode</code> attribute. 
	 * @param value the regionCode
	 */
	public void setRegionCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "regionCode".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RegionProductAgeRestriction.regionCode</code> attribute. 
	 * @param value the regionCode
	 */
	public void setRegionCode(final String value)
	{
		setRegionCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RegionProductAgeRestriction.scExclusionRule</code> attribute.
	 * @return the scExclusionRule
	 */
	public String getScExclusionRule(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "scExclusionRule".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RegionProductAgeRestriction.scExclusionRule</code> attribute.
	 * @return the scExclusionRule
	 */
	public String getScExclusionRule()
	{
		return getScExclusionRule( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RegionProductAgeRestriction.scExclusionRule</code> attribute. 
	 * @param value the scExclusionRule
	 */
	public void setScExclusionRule(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "scExclusionRule".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RegionProductAgeRestriction.scExclusionRule</code> attribute. 
	 * @param value the scExclusionRule
	 */
	public void setScExclusionRule(final String value)
	{
		setScExclusionRule( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RegionProductAgeRestriction.validFrom</code> attribute.
	 * @return the validFrom
	 */
	public Date getValidFrom(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, "validFrom".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RegionProductAgeRestriction.validFrom</code> attribute.
	 * @return the validFrom
	 */
	public Date getValidFrom()
	{
		return getValidFrom( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RegionProductAgeRestriction.validFrom</code> attribute. 
	 * @param value the validFrom
	 */
	public void setValidFrom(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, "validFrom".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RegionProductAgeRestriction.validFrom</code> attribute. 
	 * @param value the validFrom
	 */
	public void setValidFrom(final Date value)
	{
		setValidFrom( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RegionProductAgeRestriction.validTo</code> attribute.
	 * @return the validTo
	 */
	public Date getValidTo(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, "validTo".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RegionProductAgeRestriction.validTo</code> attribute.
	 * @return the validTo
	 */
	public Date getValidTo()
	{
		return getValidTo( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RegionProductAgeRestriction.validTo</code> attribute. 
	 * @param value the validTo
	 */
	public void setValidTo(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, "validTo".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RegionProductAgeRestriction.validTo</code> attribute. 
	 * @param value the validTo
	 */
	public void setValidTo(final Date value)
	{
		setValidTo( getSession().getSessionContext(), value );
	}
	
}
