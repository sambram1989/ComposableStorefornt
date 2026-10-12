/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 27-May-2025, 11:44:08 pm                    ---
 * ----------------------------------------------------------------
 */
package com.wholescripts.core.jalo;

import com.wholescripts.core.jalo.Dosing;
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
 * Generated class for type MedpaxEntry.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class MedpaxEntry extends GenericItem
{
	/** Qualifier of the <code>MedpaxEntry.entryNumber</code> attribute **/
	public static final String ENTRYNUMBER = "entryNumber";
	/** Qualifier of the <code>MedpaxEntry.product</code> attribute **/
	public static final String PRODUCT = "product";
	/** Qualifier of the <code>MedpaxEntry.dosing</code> attribute **/
	public static final String DOSING = "dosing";
	/** Qualifier of the <code>MedpaxEntry.price</code> attribute **/
	public static final String PRICE = "price";
	/** Qualifier of the <code>MedpaxEntry.quantity</code> attribute **/
	public static final String QUANTITY = "quantity";
	/** Qualifier of the <code>MedpaxEntry.msrpPrice</code> attribute **/
	public static final String MSRPPRICE = "msrpPrice";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(ENTRYNUMBER, AttributeMode.INITIAL);
		tmp.put(PRODUCT, AttributeMode.INITIAL);
		tmp.put(DOSING, AttributeMode.INITIAL);
		tmp.put(PRICE, AttributeMode.INITIAL);
		tmp.put(QUANTITY, AttributeMode.INITIAL);
		tmp.put(MSRPPRICE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedpaxEntry.dosing</code> attribute.
	 * @return the dosing
	 */
	public List<Dosing> getDosing(final SessionContext ctx)
	{
		List<Dosing> coll = (List<Dosing>)getProperty( ctx, "dosing".intern());
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedpaxEntry.dosing</code> attribute.
	 * @return the dosing
	 */
	public List<Dosing> getDosing()
	{
		return getDosing( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedpaxEntry.dosing</code> attribute. 
	 * @param value the dosing
	 */
	public void setDosing(final SessionContext ctx, final List<Dosing> value)
	{
		setProperty(ctx, "dosing".intern(),value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedpaxEntry.dosing</code> attribute. 
	 * @param value the dosing
	 */
	public void setDosing(final List<Dosing> value)
	{
		setDosing( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedpaxEntry.entryNumber</code> attribute.
	 * @return the entryNumber
	 */
	public String getEntryNumber(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "entryNumber".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedpaxEntry.entryNumber</code> attribute.
	 * @return the entryNumber
	 */
	public String getEntryNumber()
	{
		return getEntryNumber( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedpaxEntry.entryNumber</code> attribute. 
	 * @param value the entryNumber
	 */
	public void setEntryNumber(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "entryNumber".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedpaxEntry.entryNumber</code> attribute. 
	 * @param value the entryNumber
	 */
	public void setEntryNumber(final String value)
	{
		setEntryNumber( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedpaxEntry.msrpPrice</code> attribute.
	 * @return the msrpPrice
	 */
	public Double getMsrpPrice(final SessionContext ctx)
	{
		return (Double)getProperty( ctx, "msrpPrice".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedpaxEntry.msrpPrice</code> attribute.
	 * @return the msrpPrice
	 */
	public Double getMsrpPrice()
	{
		return getMsrpPrice( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedpaxEntry.msrpPrice</code> attribute. 
	 * @return the msrpPrice
	 */
	public double getMsrpPriceAsPrimitive(final SessionContext ctx)
	{
		Double value = getMsrpPrice( ctx );
		return value != null ? value.doubleValue() : 0.0d;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedpaxEntry.msrpPrice</code> attribute. 
	 * @return the msrpPrice
	 */
	public double getMsrpPriceAsPrimitive()
	{
		return getMsrpPriceAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedpaxEntry.msrpPrice</code> attribute. 
	 * @param value the msrpPrice
	 */
	public void setMsrpPrice(final SessionContext ctx, final Double value)
	{
		setProperty(ctx, "msrpPrice".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedpaxEntry.msrpPrice</code> attribute. 
	 * @param value the msrpPrice
	 */
	public void setMsrpPrice(final Double value)
	{
		setMsrpPrice( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedpaxEntry.msrpPrice</code> attribute. 
	 * @param value the msrpPrice
	 */
	public void setMsrpPrice(final SessionContext ctx, final double value)
	{
		setMsrpPrice( ctx,Double.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedpaxEntry.msrpPrice</code> attribute. 
	 * @param value the msrpPrice
	 */
	public void setMsrpPrice(final double value)
	{
		setMsrpPrice( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedpaxEntry.price</code> attribute.
	 * @return the price
	 */
	public Double getPrice(final SessionContext ctx)
	{
		return (Double)getProperty( ctx, "price".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedpaxEntry.price</code> attribute.
	 * @return the price
	 */
	public Double getPrice()
	{
		return getPrice( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedpaxEntry.price</code> attribute. 
	 * @return the price
	 */
	public double getPriceAsPrimitive(final SessionContext ctx)
	{
		Double value = getPrice( ctx );
		return value != null ? value.doubleValue() : 0.0d;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedpaxEntry.price</code> attribute. 
	 * @return the price
	 */
	public double getPriceAsPrimitive()
	{
		return getPriceAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedpaxEntry.price</code> attribute. 
	 * @param value the price
	 */
	public void setPrice(final SessionContext ctx, final Double value)
	{
		setProperty(ctx, "price".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedpaxEntry.price</code> attribute. 
	 * @param value the price
	 */
	public void setPrice(final Double value)
	{
		setPrice( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedpaxEntry.price</code> attribute. 
	 * @param value the price
	 */
	public void setPrice(final SessionContext ctx, final double value)
	{
		setPrice( ctx,Double.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedpaxEntry.price</code> attribute. 
	 * @param value the price
	 */
	public void setPrice(final double value)
	{
		setPrice( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedpaxEntry.product</code> attribute.
	 * @return the product
	 */
	public Product getProduct(final SessionContext ctx)
	{
		return (Product)getProperty( ctx, "product".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedpaxEntry.product</code> attribute.
	 * @return the product
	 */
	public Product getProduct()
	{
		return getProduct( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedpaxEntry.product</code> attribute. 
	 * @param value the product
	 */
	public void setProduct(final SessionContext ctx, final Product value)
	{
		setProperty(ctx, "product".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedpaxEntry.product</code> attribute. 
	 * @param value the product
	 */
	public void setProduct(final Product value)
	{
		setProduct( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedpaxEntry.quantity</code> attribute.
	 * @return the quantity
	 */
	public Integer getQuantity(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, "quantity".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedpaxEntry.quantity</code> attribute.
	 * @return the quantity
	 */
	public Integer getQuantity()
	{
		return getQuantity( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedpaxEntry.quantity</code> attribute. 
	 * @return the quantity
	 */
	public int getQuantityAsPrimitive(final SessionContext ctx)
	{
		Integer value = getQuantity( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedpaxEntry.quantity</code> attribute. 
	 * @return the quantity
	 */
	public int getQuantityAsPrimitive()
	{
		return getQuantityAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedpaxEntry.quantity</code> attribute. 
	 * @param value the quantity
	 */
	public void setQuantity(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, "quantity".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedpaxEntry.quantity</code> attribute. 
	 * @param value the quantity
	 */
	public void setQuantity(final Integer value)
	{
		setQuantity( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedpaxEntry.quantity</code> attribute. 
	 * @param value the quantity
	 */
	public void setQuantity(final SessionContext ctx, final int value)
	{
		setQuantity( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedpaxEntry.quantity</code> attribute. 
	 * @param value the quantity
	 */
	public void setQuantity(final int value)
	{
		setQuantity( getSession().getSessionContext(), value );
	}
	
}
