/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 27-May-2025, 11:44:08 pm                    ---
 * ----------------------------------------------------------------
 */
package com.wholescripts.core.jalo;

import com.wholescripts.core.jalo.MedpaxEntry;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.product.Product;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type MedPax.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class MedPax extends GenericItem
{
	/** Qualifier of the <code>MedPax.code</code> attribute **/
	public static final String CODE = "code";
	/** Qualifier of the <code>MedPax.name</code> attribute **/
	public static final String NAME = "name";
	/** Qualifier of the <code>MedPax.description</code> attribute **/
	public static final String DESCRIPTION = "description";
	/** Qualifier of the <code>MedPax.cartonDescription</code> attribute **/
	public static final String CARTONDESCRIPTION = "cartonDescription";
	/** Qualifier of the <code>MedPax.carton</code> attribute **/
	public static final String CARTON = "carton";
	/** Qualifier of the <code>MedPax.medpaxEntry</code> attribute **/
	public static final String MEDPAXENTRY = "medpaxEntry";
	/** Qualifier of the <code>MedPax.medPaxOwner</code> attribute **/
	public static final String MEDPAXOWNER = "medPaxOwner";
	/** Qualifier of the <code>MedPax.boxes</code> attribute **/
	public static final String BOXES = "boxes";
	/** Qualifier of the <code>MedPax.packetsCount</code> attribute **/
	public static final String PACKETSCOUNT = "packetsCount";
	/** Qualifier of the <code>MedPax.formula</code> attribute **/
	public static final String FORMULA = "formula";
	/** Qualifier of the <code>MedPax.notes</code> attribute **/
	public static final String NOTES = "notes";
	/** Qualifier of the <code>MedPax.msrpTotalPrice</code> attribute **/
	public static final String MSRPTOTALPRICE = "msrpTotalPrice";
	/** Qualifier of the <code>MedPax.totalPrice</code> attribute **/
	public static final String TOTALPRICE = "totalPrice";
	/** Qualifier of the <code>MedPax.abstractOrderQty</code> attribute **/
	public static final String ABSTRACTORDERQTY = "abstractOrderQty";
	/** Qualifier of the <code>MedPax.itemDuration</code> attribute **/
	public static final String ITEMDURATION = "itemDuration";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CODE, AttributeMode.INITIAL);
		tmp.put(NAME, AttributeMode.INITIAL);
		tmp.put(DESCRIPTION, AttributeMode.INITIAL);
		tmp.put(CARTONDESCRIPTION, AttributeMode.INITIAL);
		tmp.put(CARTON, AttributeMode.INITIAL);
		tmp.put(MEDPAXENTRY, AttributeMode.INITIAL);
		tmp.put(MEDPAXOWNER, AttributeMode.INITIAL);
		tmp.put(BOXES, AttributeMode.INITIAL);
		tmp.put(PACKETSCOUNT, AttributeMode.INITIAL);
		tmp.put(FORMULA, AttributeMode.INITIAL);
		tmp.put(NOTES, AttributeMode.INITIAL);
		tmp.put(MSRPTOTALPRICE, AttributeMode.INITIAL);
		tmp.put(TOTALPRICE, AttributeMode.INITIAL);
		tmp.put(ABSTRACTORDERQTY, AttributeMode.INITIAL);
		tmp.put(ITEMDURATION, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.abstractOrderQty</code> attribute.
	 * @return the abstractOrderQty
	 */
	public Long getAbstractOrderQty(final SessionContext ctx)
	{
		return (Long)getProperty( ctx, "abstractOrderQty".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.abstractOrderQty</code> attribute.
	 * @return the abstractOrderQty
	 */
	public Long getAbstractOrderQty()
	{
		return getAbstractOrderQty( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.abstractOrderQty</code> attribute. 
	 * @return the abstractOrderQty
	 */
	public long getAbstractOrderQtyAsPrimitive(final SessionContext ctx)
	{
		Long value = getAbstractOrderQty( ctx );
		return value != null ? value.longValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.abstractOrderQty</code> attribute. 
	 * @return the abstractOrderQty
	 */
	public long getAbstractOrderQtyAsPrimitive()
	{
		return getAbstractOrderQtyAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.abstractOrderQty</code> attribute. 
	 * @param value the abstractOrderQty
	 */
	public void setAbstractOrderQty(final SessionContext ctx, final Long value)
	{
		setProperty(ctx, "abstractOrderQty".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.abstractOrderQty</code> attribute. 
	 * @param value the abstractOrderQty
	 */
	public void setAbstractOrderQty(final Long value)
	{
		setAbstractOrderQty( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.abstractOrderQty</code> attribute. 
	 * @param value the abstractOrderQty
	 */
	public void setAbstractOrderQty(final SessionContext ctx, final long value)
	{
		setAbstractOrderQty( ctx,Long.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.abstractOrderQty</code> attribute. 
	 * @param value the abstractOrderQty
	 */
	public void setAbstractOrderQty(final long value)
	{
		setAbstractOrderQty( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.boxes</code> attribute.
	 * @return the boxes
	 */
	public Integer getBoxes(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, "boxes".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.boxes</code> attribute.
	 * @return the boxes
	 */
	public Integer getBoxes()
	{
		return getBoxes( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.boxes</code> attribute. 
	 * @return the boxes
	 */
	public int getBoxesAsPrimitive(final SessionContext ctx)
	{
		Integer value = getBoxes( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.boxes</code> attribute. 
	 * @return the boxes
	 */
	public int getBoxesAsPrimitive()
	{
		return getBoxesAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.boxes</code> attribute. 
	 * @param value the boxes
	 */
	public void setBoxes(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, "boxes".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.boxes</code> attribute. 
	 * @param value the boxes
	 */
	public void setBoxes(final Integer value)
	{
		setBoxes( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.boxes</code> attribute. 
	 * @param value the boxes
	 */
	public void setBoxes(final SessionContext ctx, final int value)
	{
		setBoxes( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.boxes</code> attribute. 
	 * @param value the boxes
	 */
	public void setBoxes(final int value)
	{
		setBoxes( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.carton</code> attribute.
	 * @return the carton
	 */
	public Product getCarton(final SessionContext ctx)
	{
		return (Product)getProperty( ctx, "carton".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.carton</code> attribute.
	 * @return the carton
	 */
	public Product getCarton()
	{
		return getCarton( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.carton</code> attribute. 
	 * @param value the carton
	 */
	public void setCarton(final SessionContext ctx, final Product value)
	{
		setProperty(ctx, "carton".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.carton</code> attribute. 
	 * @param value the carton
	 */
	public void setCarton(final Product value)
	{
		setCarton( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.cartonDescription</code> attribute.
	 * @return the cartonDescription
	 */
	public String getCartonDescription(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "cartonDescription".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.cartonDescription</code> attribute.
	 * @return the cartonDescription
	 */
	public String getCartonDescription()
	{
		return getCartonDescription( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.cartonDescription</code> attribute. 
	 * @param value the cartonDescription
	 */
	public void setCartonDescription(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "cartonDescription".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.cartonDescription</code> attribute. 
	 * @param value the cartonDescription
	 */
	public void setCartonDescription(final String value)
	{
		setCartonDescription( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.code</code> attribute.
	 * @return the code
	 */
	public String getCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "code".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.code</code> attribute.
	 * @return the code
	 */
	public String getCode()
	{
		return getCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "code".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final String value)
	{
		setCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.description</code> attribute.
	 * @return the description
	 */
	public String getDescription(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "description".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.description</code> attribute.
	 * @return the description
	 */
	public String getDescription()
	{
		return getDescription( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.description</code> attribute. 
	 * @param value the description
	 */
	public void setDescription(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "description".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.description</code> attribute. 
	 * @param value the description
	 */
	public void setDescription(final String value)
	{
		setDescription( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.formula</code> attribute.
	 * @return the formula
	 */
	public Integer getFormula(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, "formula".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.formula</code> attribute.
	 * @return the formula
	 */
	public Integer getFormula()
	{
		return getFormula( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.formula</code> attribute. 
	 * @return the formula
	 */
	public int getFormulaAsPrimitive(final SessionContext ctx)
	{
		Integer value = getFormula( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.formula</code> attribute. 
	 * @return the formula
	 */
	public int getFormulaAsPrimitive()
	{
		return getFormulaAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.formula</code> attribute. 
	 * @param value the formula
	 */
	public void setFormula(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, "formula".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.formula</code> attribute. 
	 * @param value the formula
	 */
	public void setFormula(final Integer value)
	{
		setFormula( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.formula</code> attribute. 
	 * @param value the formula
	 */
	public void setFormula(final SessionContext ctx, final int value)
	{
		setFormula( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.formula</code> attribute. 
	 * @param value the formula
	 */
	public void setFormula(final int value)
	{
		setFormula( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.itemDuration</code> attribute.
	 * @return the itemDuration - item Duration Enum
	 */
	public EnumerationValue getItemDuration(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, "itemDuration".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.itemDuration</code> attribute.
	 * @return the itemDuration - item Duration Enum
	 */
	public EnumerationValue getItemDuration()
	{
		return getItemDuration( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.itemDuration</code> attribute. 
	 * @param value the itemDuration - item Duration Enum
	 */
	public void setItemDuration(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, "itemDuration".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.itemDuration</code> attribute. 
	 * @param value the itemDuration - item Duration Enum
	 */
	public void setItemDuration(final EnumerationValue value)
	{
		setItemDuration( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.medpaxEntry</code> attribute.
	 * @return the medpaxEntry
	 */
	public List<MedpaxEntry> getMedpaxEntry(final SessionContext ctx)
	{
		List<MedpaxEntry> coll = (List<MedpaxEntry>)getProperty( ctx, "medpaxEntry".intern());
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.medpaxEntry</code> attribute.
	 * @return the medpaxEntry
	 */
	public List<MedpaxEntry> getMedpaxEntry()
	{
		return getMedpaxEntry( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.medpaxEntry</code> attribute. 
	 * @param value the medpaxEntry
	 */
	public void setMedpaxEntry(final SessionContext ctx, final List<MedpaxEntry> value)
	{
		setProperty(ctx, "medpaxEntry".intern(),value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.medpaxEntry</code> attribute. 
	 * @param value the medpaxEntry
	 */
	public void setMedpaxEntry(final List<MedpaxEntry> value)
	{
		setMedpaxEntry( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.medPaxOwner</code> attribute.
	 * @return the medPaxOwner
	 */
	public Item getMedPaxOwner(final SessionContext ctx)
	{
		return (Item)getProperty( ctx, "medPaxOwner".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.medPaxOwner</code> attribute.
	 * @return the medPaxOwner
	 */
	public Item getMedPaxOwner()
	{
		return getMedPaxOwner( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.medPaxOwner</code> attribute. 
	 * @param value the medPaxOwner
	 */
	public void setMedPaxOwner(final SessionContext ctx, final Item value)
	{
		setProperty(ctx, "medPaxOwner".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.medPaxOwner</code> attribute. 
	 * @param value the medPaxOwner
	 */
	public void setMedPaxOwner(final Item value)
	{
		setMedPaxOwner( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.msrpTotalPrice</code> attribute.
	 * @return the msrpTotalPrice
	 */
	public Double getMsrpTotalPrice(final SessionContext ctx)
	{
		return (Double)getProperty( ctx, "msrpTotalPrice".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.msrpTotalPrice</code> attribute.
	 * @return the msrpTotalPrice
	 */
	public Double getMsrpTotalPrice()
	{
		return getMsrpTotalPrice( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.msrpTotalPrice</code> attribute. 
	 * @return the msrpTotalPrice
	 */
	public double getMsrpTotalPriceAsPrimitive(final SessionContext ctx)
	{
		Double value = getMsrpTotalPrice( ctx );
		return value != null ? value.doubleValue() : 0.0d;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.msrpTotalPrice</code> attribute. 
	 * @return the msrpTotalPrice
	 */
	public double getMsrpTotalPriceAsPrimitive()
	{
		return getMsrpTotalPriceAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.msrpTotalPrice</code> attribute. 
	 * @param value the msrpTotalPrice
	 */
	public void setMsrpTotalPrice(final SessionContext ctx, final Double value)
	{
		setProperty(ctx, "msrpTotalPrice".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.msrpTotalPrice</code> attribute. 
	 * @param value the msrpTotalPrice
	 */
	public void setMsrpTotalPrice(final Double value)
	{
		setMsrpTotalPrice( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.msrpTotalPrice</code> attribute. 
	 * @param value the msrpTotalPrice
	 */
	public void setMsrpTotalPrice(final SessionContext ctx, final double value)
	{
		setMsrpTotalPrice( ctx,Double.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.msrpTotalPrice</code> attribute. 
	 * @param value the msrpTotalPrice
	 */
	public void setMsrpTotalPrice(final double value)
	{
		setMsrpTotalPrice( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.name</code> attribute.
	 * @return the name
	 */
	public String getName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "name".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.name</code> attribute.
	 * @return the name
	 */
	public String getName()
	{
		return getName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.name</code> attribute. 
	 * @param value the name
	 */
	public void setName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "name".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.name</code> attribute. 
	 * @param value the name
	 */
	public void setName(final String value)
	{
		setName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.notes</code> attribute.
	 * @return the notes - code
	 */
	public String getNotes(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "notes".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.notes</code> attribute.
	 * @return the notes - code
	 */
	public String getNotes()
	{
		return getNotes( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.notes</code> attribute. 
	 * @param value the notes - code
	 */
	public void setNotes(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "notes".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.notes</code> attribute. 
	 * @param value the notes - code
	 */
	public void setNotes(final String value)
	{
		setNotes( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.packetsCount</code> attribute.
	 * @return the packetsCount
	 */
	public Integer getPacketsCount(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, "packetsCount".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.packetsCount</code> attribute.
	 * @return the packetsCount
	 */
	public Integer getPacketsCount()
	{
		return getPacketsCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.packetsCount</code> attribute. 
	 * @return the packetsCount
	 */
	public int getPacketsCountAsPrimitive(final SessionContext ctx)
	{
		Integer value = getPacketsCount( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.packetsCount</code> attribute. 
	 * @return the packetsCount
	 */
	public int getPacketsCountAsPrimitive()
	{
		return getPacketsCountAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.packetsCount</code> attribute. 
	 * @param value the packetsCount
	 */
	public void setPacketsCount(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, "packetsCount".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.packetsCount</code> attribute. 
	 * @param value the packetsCount
	 */
	public void setPacketsCount(final Integer value)
	{
		setPacketsCount( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.packetsCount</code> attribute. 
	 * @param value the packetsCount
	 */
	public void setPacketsCount(final SessionContext ctx, final int value)
	{
		setPacketsCount( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.packetsCount</code> attribute. 
	 * @param value the packetsCount
	 */
	public void setPacketsCount(final int value)
	{
		setPacketsCount( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.totalPrice</code> attribute.
	 * @return the totalPrice
	 */
	public Double getTotalPrice(final SessionContext ctx)
	{
		return (Double)getProperty( ctx, "totalPrice".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.totalPrice</code> attribute.
	 * @return the totalPrice
	 */
	public Double getTotalPrice()
	{
		return getTotalPrice( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.totalPrice</code> attribute. 
	 * @return the totalPrice
	 */
	public double getTotalPriceAsPrimitive(final SessionContext ctx)
	{
		Double value = getTotalPrice( ctx );
		return value != null ? value.doubleValue() : 0.0d;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>MedPax.totalPrice</code> attribute. 
	 * @return the totalPrice
	 */
	public double getTotalPriceAsPrimitive()
	{
		return getTotalPriceAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.totalPrice</code> attribute. 
	 * @param value the totalPrice
	 */
	public void setTotalPrice(final SessionContext ctx, final Double value)
	{
		setProperty(ctx, "totalPrice".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.totalPrice</code> attribute. 
	 * @param value the totalPrice
	 */
	public void setTotalPrice(final Double value)
	{
		setTotalPrice( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.totalPrice</code> attribute. 
	 * @param value the totalPrice
	 */
	public void setTotalPrice(final SessionContext ctx, final double value)
	{
		setTotalPrice( ctx,Double.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>MedPax.totalPrice</code> attribute. 
	 * @param value the totalPrice
	 */
	public void setTotalPrice(final double value)
	{
		setTotalPrice( getSession().getSessionContext(), value );
	}
	
}
