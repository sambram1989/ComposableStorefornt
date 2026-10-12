/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 27-May-2025, 11:44:08 pm                    ---
 * ----------------------------------------------------------------
 */
package com.wholescripts.core.jalo;

import com.wholescripts.core.jalo.ItemTime;
import de.hybris.platform.b2b.jalo.B2BCustomer;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type Dosing.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class Dosing extends GenericItem
{
	/** Qualifier of the <code>Dosing.id</code> attribute **/
	public static final String ID = "id";
	/** Qualifier of the <code>Dosing.itemTimes</code> attribute **/
	public static final String ITEMTIMES = "itemTimes";
	/** Qualifier of the <code>Dosing.product</code> attribute **/
	public static final String PRODUCT = "product";
	/** Qualifier of the <code>Dosing.b2bCustomer</code> attribute **/
	public static final String B2BCUSTOMER = "b2bCustomer";
	/** Qualifier of the <code>Dosing.quantity</code> attribute **/
	public static final String QUANTITY = "quantity";
	/** Qualifier of the <code>Dosing.isSavedAsCustom</code> attribute **/
	public static final String ISSAVEDASCUSTOM = "isSavedAsCustom";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(ID, AttributeMode.INITIAL);
		tmp.put(ITEMTIMES, AttributeMode.INITIAL);
		tmp.put(PRODUCT, AttributeMode.INITIAL);
		tmp.put(B2BCUSTOMER, AttributeMode.INITIAL);
		tmp.put(QUANTITY, AttributeMode.INITIAL);
		tmp.put(ISSAVEDASCUSTOM, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Dosing.b2bCustomer</code> attribute.
	 * @return the b2bCustomer - Dosing for Customer
	 */
	public B2BCustomer getB2bCustomer(final SessionContext ctx)
	{
		return (B2BCustomer)getProperty( ctx, "b2bCustomer".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Dosing.b2bCustomer</code> attribute.
	 * @return the b2bCustomer - Dosing for Customer
	 */
	public B2BCustomer getB2bCustomer()
	{
		return getB2bCustomer( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Dosing.b2bCustomer</code> attribute. 
	 * @param value the b2bCustomer - Dosing for Customer
	 */
	public void setB2bCustomer(final SessionContext ctx, final B2BCustomer value)
	{
		setProperty(ctx, "b2bCustomer".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Dosing.b2bCustomer</code> attribute. 
	 * @param value the b2bCustomer - Dosing for Customer
	 */
	public void setB2bCustomer(final B2BCustomer value)
	{
		setB2bCustomer( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Dosing.id</code> attribute.
	 * @return the id - Dosing ID
	 */
	public String getId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "id".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Dosing.id</code> attribute.
	 * @return the id - Dosing ID
	 */
	public String getId()
	{
		return getId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Dosing.id</code> attribute. 
	 * @param value the id - Dosing ID
	 */
	public void setId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "id".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Dosing.id</code> attribute. 
	 * @param value the id - Dosing ID
	 */
	public void setId(final String value)
	{
		setId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Dosing.isSavedAsCustom</code> attribute.
	 * @return the isSavedAsCustom - Flag for if Dosing is saved as custom
	 */
	public Boolean isIsSavedAsCustom(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, "isSavedAsCustom".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Dosing.isSavedAsCustom</code> attribute.
	 * @return the isSavedAsCustom - Flag for if Dosing is saved as custom
	 */
	public Boolean isIsSavedAsCustom()
	{
		return isIsSavedAsCustom( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Dosing.isSavedAsCustom</code> attribute. 
	 * @return the isSavedAsCustom - Flag for if Dosing is saved as custom
	 */
	public boolean isIsSavedAsCustomAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isIsSavedAsCustom( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Dosing.isSavedAsCustom</code> attribute. 
	 * @return the isSavedAsCustom - Flag for if Dosing is saved as custom
	 */
	public boolean isIsSavedAsCustomAsPrimitive()
	{
		return isIsSavedAsCustomAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Dosing.isSavedAsCustom</code> attribute. 
	 * @param value the isSavedAsCustom - Flag for if Dosing is saved as custom
	 */
	public void setIsSavedAsCustom(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, "isSavedAsCustom".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Dosing.isSavedAsCustom</code> attribute. 
	 * @param value the isSavedAsCustom - Flag for if Dosing is saved as custom
	 */
	public void setIsSavedAsCustom(final Boolean value)
	{
		setIsSavedAsCustom( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Dosing.isSavedAsCustom</code> attribute. 
	 * @param value the isSavedAsCustom - Flag for if Dosing is saved as custom
	 */
	public void setIsSavedAsCustom(final SessionContext ctx, final boolean value)
	{
		setIsSavedAsCustom( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Dosing.isSavedAsCustom</code> attribute. 
	 * @param value the isSavedAsCustom - Flag for if Dosing is saved as custom
	 */
	public void setIsSavedAsCustom(final boolean value)
	{
		setIsSavedAsCustom( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Dosing.itemTimes</code> attribute.
	 * @return the itemTimes - Item times for Dosing
	 */
	public List<ItemTime> getItemTimes(final SessionContext ctx)
	{
		List<ItemTime> coll = (List<ItemTime>)getProperty( ctx, "itemTimes".intern());
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Dosing.itemTimes</code> attribute.
	 * @return the itemTimes - Item times for Dosing
	 */
	public List<ItemTime> getItemTimes()
	{
		return getItemTimes( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Dosing.itemTimes</code> attribute. 
	 * @param value the itemTimes - Item times for Dosing
	 */
	public void setItemTimes(final SessionContext ctx, final List<ItemTime> value)
	{
		setProperty(ctx, "itemTimes".intern(),value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Dosing.itemTimes</code> attribute. 
	 * @param value the itemTimes - Item times for Dosing
	 */
	public void setItemTimes(final List<ItemTime> value)
	{
		setItemTimes( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Dosing.product</code> attribute.
	 * @return the product - Product For Dosing
	 */
	public Product getProduct(final SessionContext ctx)
	{
		return (Product)getProperty( ctx, "product".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Dosing.product</code> attribute.
	 * @return the product - Product For Dosing
	 */
	public Product getProduct()
	{
		return getProduct( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Dosing.product</code> attribute. 
	 * @param value the product - Product For Dosing
	 */
	public void setProduct(final SessionContext ctx, final Product value)
	{
		setProperty(ctx, "product".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Dosing.product</code> attribute. 
	 * @param value the product - Product For Dosing
	 */
	public void setProduct(final Product value)
	{
		setProduct( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Dosing.quantity</code> attribute.
	 * @return the quantity - Dosing Quantity
	 */
	public Integer getQuantity(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, "quantity".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Dosing.quantity</code> attribute.
	 * @return the quantity - Dosing Quantity
	 */
	public Integer getQuantity()
	{
		return getQuantity( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Dosing.quantity</code> attribute. 
	 * @return the quantity - Dosing Quantity
	 */
	public int getQuantityAsPrimitive(final SessionContext ctx)
	{
		Integer value = getQuantity( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Dosing.quantity</code> attribute. 
	 * @return the quantity - Dosing Quantity
	 */
	public int getQuantityAsPrimitive()
	{
		return getQuantityAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Dosing.quantity</code> attribute. 
	 * @param value the quantity - Dosing Quantity
	 */
	public void setQuantity(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, "quantity".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Dosing.quantity</code> attribute. 
	 * @param value the quantity - Dosing Quantity
	 */
	public void setQuantity(final Integer value)
	{
		setQuantity( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Dosing.quantity</code> attribute. 
	 * @param value the quantity - Dosing Quantity
	 */
	public void setQuantity(final SessionContext ctx, final int value)
	{
		setQuantity( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Dosing.quantity</code> attribute. 
	 * @param value the quantity - Dosing Quantity
	 */
	public void setQuantity(final int value)
	{
		setQuantity( getSession().getSessionContext(), value );
	}
	
}
