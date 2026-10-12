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
 * Generated class for type CustomerMaterialInformation.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class CustomerMaterialInformation extends GenericItem
{
	/** Qualifier of the <code>CustomerMaterialInformation.account</code> attribute **/
	public static final String ACCOUNT = "account";
	/** Qualifier of the <code>CustomerMaterialInformation.productCode</code> attribute **/
	public static final String PRODUCTCODE = "productCode";
	/** Qualifier of the <code>CustomerMaterialInformation.productDescription</code> attribute **/
	public static final String PRODUCTDESCRIPTION = "productDescription";
	/** Qualifier of the <code>CustomerMaterialInformation.blueLabel</code> attribute **/
	public static final String BLUELABEL = "blueLabel";
	/** Qualifier of the <code>CustomerMaterialInformation.salesUnit</code> attribute **/
	public static final String SALESUNIT = "salesUnit";
	/** Qualifier of the <code>CustomerMaterialInformation.brightStock</code> attribute **/
	public static final String BRIGHTSTOCK = "brightStock";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(ACCOUNT, AttributeMode.INITIAL);
		tmp.put(PRODUCTCODE, AttributeMode.INITIAL);
		tmp.put(PRODUCTDESCRIPTION, AttributeMode.INITIAL);
		tmp.put(BLUELABEL, AttributeMode.INITIAL);
		tmp.put(SALESUNIT, AttributeMode.INITIAL);
		tmp.put(BRIGHTSTOCK, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerMaterialInformation.account</code> attribute.
	 * @return the account
	 */
	public String getAccount(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "account".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerMaterialInformation.account</code> attribute.
	 * @return the account
	 */
	public String getAccount()
	{
		return getAccount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerMaterialInformation.account</code> attribute. 
	 * @param value the account
	 */
	public void setAccount(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "account".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerMaterialInformation.account</code> attribute. 
	 * @param value the account
	 */
	public void setAccount(final String value)
	{
		setAccount( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerMaterialInformation.blueLabel</code> attribute.
	 * @return the blueLabel
	 */
	public String getBlueLabel(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "blueLabel".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerMaterialInformation.blueLabel</code> attribute.
	 * @return the blueLabel
	 */
	public String getBlueLabel()
	{
		return getBlueLabel( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerMaterialInformation.blueLabel</code> attribute. 
	 * @param value the blueLabel
	 */
	public void setBlueLabel(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "blueLabel".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerMaterialInformation.blueLabel</code> attribute. 
	 * @param value the blueLabel
	 */
	public void setBlueLabel(final String value)
	{
		setBlueLabel( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerMaterialInformation.brightStock</code> attribute.
	 * @return the brightStock
	 */
	public String getBrightStock(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "brightStock".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerMaterialInformation.brightStock</code> attribute.
	 * @return the brightStock
	 */
	public String getBrightStock()
	{
		return getBrightStock( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerMaterialInformation.brightStock</code> attribute. 
	 * @param value the brightStock
	 */
	public void setBrightStock(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "brightStock".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerMaterialInformation.brightStock</code> attribute. 
	 * @param value the brightStock
	 */
	public void setBrightStock(final String value)
	{
		setBrightStock( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerMaterialInformation.productCode</code> attribute.
	 * @return the productCode
	 */
	public String getProductCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "productCode".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerMaterialInformation.productCode</code> attribute.
	 * @return the productCode
	 */
	public String getProductCode()
	{
		return getProductCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerMaterialInformation.productCode</code> attribute. 
	 * @param value the productCode
	 */
	public void setProductCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "productCode".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerMaterialInformation.productCode</code> attribute. 
	 * @param value the productCode
	 */
	public void setProductCode(final String value)
	{
		setProductCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerMaterialInformation.productDescription</code> attribute.
	 * @return the productDescription
	 */
	public String getProductDescription(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "productDescription".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerMaterialInformation.productDescription</code> attribute.
	 * @return the productDescription
	 */
	public String getProductDescription()
	{
		return getProductDescription( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerMaterialInformation.productDescription</code> attribute. 
	 * @param value the productDescription
	 */
	public void setProductDescription(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "productDescription".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerMaterialInformation.productDescription</code> attribute. 
	 * @param value the productDescription
	 */
	public void setProductDescription(final String value)
	{
		setProductDescription( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerMaterialInformation.salesUnit</code> attribute.
	 * @return the salesUnit
	 */
	public String getSalesUnit(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "salesUnit".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerMaterialInformation.salesUnit</code> attribute.
	 * @return the salesUnit
	 */
	public String getSalesUnit()
	{
		return getSalesUnit( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerMaterialInformation.salesUnit</code> attribute. 
	 * @param value the salesUnit
	 */
	public void setSalesUnit(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "salesUnit".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerMaterialInformation.salesUnit</code> attribute. 
	 * @param value the salesUnit
	 */
	public void setSalesUnit(final String value)
	{
		setSalesUnit( getSession().getSessionContext(), value );
	}
	
}
