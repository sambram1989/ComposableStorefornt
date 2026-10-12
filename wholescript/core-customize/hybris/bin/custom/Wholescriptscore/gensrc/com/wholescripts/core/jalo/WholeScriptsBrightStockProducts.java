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
 * Generated class for type WholeScriptsBrightStockProducts.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class WholeScriptsBrightStockProducts extends GenericItem
{
	/** Qualifier of the <code>WholeScriptsBrightStockProducts.productCode</code> attribute **/
	public static final String PRODUCTCODE = "productCode";
	/** Qualifier of the <code>WholeScriptsBrightStockProducts.outOfStock</code> attribute **/
	public static final String OUTOFSTOCK = "outOfStock";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(PRODUCTCODE, AttributeMode.INITIAL);
		tmp.put(OUTOFSTOCK, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholeScriptsBrightStockProducts.outOfStock</code> attribute.
	 * @return the outOfStock
	 */
	public Boolean isOutOfStock(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, "outOfStock".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholeScriptsBrightStockProducts.outOfStock</code> attribute.
	 * @return the outOfStock
	 */
	public Boolean isOutOfStock()
	{
		return isOutOfStock( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholeScriptsBrightStockProducts.outOfStock</code> attribute. 
	 * @return the outOfStock
	 */
	public boolean isOutOfStockAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isOutOfStock( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholeScriptsBrightStockProducts.outOfStock</code> attribute. 
	 * @return the outOfStock
	 */
	public boolean isOutOfStockAsPrimitive()
	{
		return isOutOfStockAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholeScriptsBrightStockProducts.outOfStock</code> attribute. 
	 * @param value the outOfStock
	 */
	public void setOutOfStock(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, "outOfStock".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholeScriptsBrightStockProducts.outOfStock</code> attribute. 
	 * @param value the outOfStock
	 */
	public void setOutOfStock(final Boolean value)
	{
		setOutOfStock( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholeScriptsBrightStockProducts.outOfStock</code> attribute. 
	 * @param value the outOfStock
	 */
	public void setOutOfStock(final SessionContext ctx, final boolean value)
	{
		setOutOfStock( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholeScriptsBrightStockProducts.outOfStock</code> attribute. 
	 * @param value the outOfStock
	 */
	public void setOutOfStock(final boolean value)
	{
		setOutOfStock( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholeScriptsBrightStockProducts.productCode</code> attribute.
	 * @return the productCode
	 */
	public String getProductCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "productCode".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholeScriptsBrightStockProducts.productCode</code> attribute.
	 * @return the productCode
	 */
	public String getProductCode()
	{
		return getProductCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholeScriptsBrightStockProducts.productCode</code> attribute. 
	 * @param value the productCode
	 */
	public void setProductCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "productCode".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholeScriptsBrightStockProducts.productCode</code> attribute. 
	 * @param value the productCode
	 */
	public void setProductCode(final String value)
	{
		setProductCode( getSession().getSessionContext(), value );
	}
	
}
