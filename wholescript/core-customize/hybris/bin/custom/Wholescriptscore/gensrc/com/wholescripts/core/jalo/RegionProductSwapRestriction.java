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
 * Generated class for type RegionProductSwapRestriction.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class RegionProductSwapRestriction extends GenericItem
{
	/** Qualifier of the <code>RegionProductSwapRestriction.regionCode</code> attribute **/
	public static final String REGIONCODE = "regionCode";
	/** Qualifier of the <code>RegionProductSwapRestriction.productCode</code> attribute **/
	public static final String PRODUCTCODE = "productCode";
	/** Qualifier of the <code>RegionProductSwapRestriction.swapProductCode</code> attribute **/
	public static final String SWAPPRODUCTCODE = "swapProductCode";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(REGIONCODE, AttributeMode.INITIAL);
		tmp.put(PRODUCTCODE, AttributeMode.INITIAL);
		tmp.put(SWAPPRODUCTCODE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RegionProductSwapRestriction.productCode</code> attribute.
	 * @return the productCode
	 */
	public String getProductCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "productCode".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RegionProductSwapRestriction.productCode</code> attribute.
	 * @return the productCode
	 */
	public String getProductCode()
	{
		return getProductCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RegionProductSwapRestriction.productCode</code> attribute. 
	 * @param value the productCode
	 */
	public void setProductCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "productCode".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RegionProductSwapRestriction.productCode</code> attribute. 
	 * @param value the productCode
	 */
	public void setProductCode(final String value)
	{
		setProductCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RegionProductSwapRestriction.regionCode</code> attribute.
	 * @return the regionCode
	 */
	public String getRegionCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "regionCode".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RegionProductSwapRestriction.regionCode</code> attribute.
	 * @return the regionCode
	 */
	public String getRegionCode()
	{
		return getRegionCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RegionProductSwapRestriction.regionCode</code> attribute. 
	 * @param value the regionCode
	 */
	public void setRegionCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "regionCode".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RegionProductSwapRestriction.regionCode</code> attribute. 
	 * @param value the regionCode
	 */
	public void setRegionCode(final String value)
	{
		setRegionCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RegionProductSwapRestriction.swapProductCode</code> attribute.
	 * @return the swapProductCode
	 */
	public String getSwapProductCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "swapProductCode".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>RegionProductSwapRestriction.swapProductCode</code> attribute.
	 * @return the swapProductCode
	 */
	public String getSwapProductCode()
	{
		return getSwapProductCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RegionProductSwapRestriction.swapProductCode</code> attribute. 
	 * @param value the swapProductCode
	 */
	public void setSwapProductCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "swapProductCode".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>RegionProductSwapRestriction.swapProductCode</code> attribute. 
	 * @param value the swapProductCode
	 */
	public void setSwapProductCode(final String value)
	{
		setSwapProductCode( getSession().getSessionContext(), value );
	}
	
}
