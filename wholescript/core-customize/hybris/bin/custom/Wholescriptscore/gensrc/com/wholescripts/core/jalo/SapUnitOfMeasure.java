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
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type sapUnitOfMeasure.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class SapUnitOfMeasure extends GenericItem
{
	/** Qualifier of the <code>sapUnitOfMeasure.code</code> attribute **/
	public static final String CODE = "code";
	/** Qualifier of the <code>sapUnitOfMeasure.conversion</code> attribute **/
	public static final String CONVERSION = "conversion";
	/** Qualifier of the <code>sapUnitOfMeasure.name</code> attribute **/
	public static final String NAME = "name";
	/** Qualifier of the <code>sapUnitOfMeasure.unitType</code> attribute **/
	public static final String UNITTYPE = "unitType";
	/** Qualifier of the <code>sapUnitOfMeasure.baseUnitQty</code> attribute **/
	public static final String BASEUNITQTY = "baseUnitQty";
	/** Qualifier of the <code>sapUnitOfMeasure.baseUnitOfMeasure</code> attribute **/
	public static final String BASEUNITOFMEASURE = "baseUnitOfMeasure";
	/** Qualifier of the <code>sapUnitOfMeasure.weight</code> attribute **/
	public static final String WEIGHT = "weight";
	/** Qualifier of the <code>sapUnitOfMeasure.width</code> attribute **/
	public static final String WIDTH = "width";
	/** Qualifier of the <code>sapUnitOfMeasure.length</code> attribute **/
	public static final String LENGTH = "length";
	/** Qualifier of the <code>sapUnitOfMeasure.height</code> attribute **/
	public static final String HEIGHT = "height";
	/** Qualifier of the <code>sapUnitOfMeasure.weightUnit</code> attribute **/
	public static final String WEIGHTUNIT = "weightUnit";
	/** Qualifier of the <code>sapUnitOfMeasure.dimensionUnit</code> attribute **/
	public static final String DIMENSIONUNIT = "dimensionUnit";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CODE, AttributeMode.INITIAL);
		tmp.put(CONVERSION, AttributeMode.INITIAL);
		tmp.put(NAME, AttributeMode.INITIAL);
		tmp.put(UNITTYPE, AttributeMode.INITIAL);
		tmp.put(BASEUNITQTY, AttributeMode.INITIAL);
		tmp.put(BASEUNITOFMEASURE, AttributeMode.INITIAL);
		tmp.put(WEIGHT, AttributeMode.INITIAL);
		tmp.put(WIDTH, AttributeMode.INITIAL);
		tmp.put(LENGTH, AttributeMode.INITIAL);
		tmp.put(HEIGHT, AttributeMode.INITIAL);
		tmp.put(WEIGHTUNIT, AttributeMode.INITIAL);
		tmp.put(DIMENSIONUNIT, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>sapUnitOfMeasure.baseUnitOfMeasure</code> attribute.
	 * @return the baseUnitOfMeasure - base UnitOfMeasure
	 */
	public String getBaseUnitOfMeasure(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "baseUnitOfMeasure".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>sapUnitOfMeasure.baseUnitOfMeasure</code> attribute.
	 * @return the baseUnitOfMeasure - base UnitOfMeasure
	 */
	public String getBaseUnitOfMeasure()
	{
		return getBaseUnitOfMeasure( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>sapUnitOfMeasure.baseUnitOfMeasure</code> attribute. 
	 * @param value the baseUnitOfMeasure - base UnitOfMeasure
	 */
	public void setBaseUnitOfMeasure(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "baseUnitOfMeasure".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>sapUnitOfMeasure.baseUnitOfMeasure</code> attribute. 
	 * @param value the baseUnitOfMeasure - base UnitOfMeasure
	 */
	public void setBaseUnitOfMeasure(final String value)
	{
		setBaseUnitOfMeasure( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>sapUnitOfMeasure.baseUnitQty</code> attribute.
	 * @return the baseUnitQty - base Unit Qty
	 */
	public String getBaseUnitQty(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "baseUnitQty".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>sapUnitOfMeasure.baseUnitQty</code> attribute.
	 * @return the baseUnitQty - base Unit Qty
	 */
	public String getBaseUnitQty()
	{
		return getBaseUnitQty( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>sapUnitOfMeasure.baseUnitQty</code> attribute. 
	 * @param value the baseUnitQty - base Unit Qty
	 */
	public void setBaseUnitQty(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "baseUnitQty".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>sapUnitOfMeasure.baseUnitQty</code> attribute. 
	 * @param value the baseUnitQty - base Unit Qty
	 */
	public void setBaseUnitQty(final String value)
	{
		setBaseUnitQty( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>sapUnitOfMeasure.code</code> attribute.
	 * @return the code
	 */
	public String getCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "code".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>sapUnitOfMeasure.code</code> attribute.
	 * @return the code
	 */
	public String getCode()
	{
		return getCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>sapUnitOfMeasure.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "code".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>sapUnitOfMeasure.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final String value)
	{
		setCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>sapUnitOfMeasure.conversion</code> attribute.
	 * @return the conversion
	 */
	public Double getConversion(final SessionContext ctx)
	{
		return (Double)getProperty( ctx, "conversion".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>sapUnitOfMeasure.conversion</code> attribute.
	 * @return the conversion
	 */
	public Double getConversion()
	{
		return getConversion( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>sapUnitOfMeasure.conversion</code> attribute. 
	 * @return the conversion
	 */
	public double getConversionAsPrimitive(final SessionContext ctx)
	{
		Double value = getConversion( ctx );
		return value != null ? value.doubleValue() : 0.0d;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>sapUnitOfMeasure.conversion</code> attribute. 
	 * @return the conversion
	 */
	public double getConversionAsPrimitive()
	{
		return getConversionAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>sapUnitOfMeasure.conversion</code> attribute. 
	 * @param value the conversion
	 */
	public void setConversion(final SessionContext ctx, final Double value)
	{
		setProperty(ctx, "conversion".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>sapUnitOfMeasure.conversion</code> attribute. 
	 * @param value the conversion
	 */
	public void setConversion(final Double value)
	{
		setConversion( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>sapUnitOfMeasure.conversion</code> attribute. 
	 * @param value the conversion
	 */
	public void setConversion(final SessionContext ctx, final double value)
	{
		setConversion( ctx,Double.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>sapUnitOfMeasure.conversion</code> attribute. 
	 * @param value the conversion
	 */
	public void setConversion(final double value)
	{
		setConversion( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>sapUnitOfMeasure.dimensionUnit</code> attribute.
	 * @return the dimensionUnit - Indicates the weight of product
	 */
	public EnumerationValue getDimensionUnit(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, "dimensionUnit".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>sapUnitOfMeasure.dimensionUnit</code> attribute.
	 * @return the dimensionUnit - Indicates the weight of product
	 */
	public EnumerationValue getDimensionUnit()
	{
		return getDimensionUnit( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>sapUnitOfMeasure.dimensionUnit</code> attribute. 
	 * @param value the dimensionUnit - Indicates the weight of product
	 */
	public void setDimensionUnit(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, "dimensionUnit".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>sapUnitOfMeasure.dimensionUnit</code> attribute. 
	 * @param value the dimensionUnit - Indicates the weight of product
	 */
	public void setDimensionUnit(final EnumerationValue value)
	{
		setDimensionUnit( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>sapUnitOfMeasure.height</code> attribute.
	 * @return the height - Indicates the weight of product
	 */
	public String getHeight(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "height".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>sapUnitOfMeasure.height</code> attribute.
	 * @return the height - Indicates the weight of product
	 */
	public String getHeight()
	{
		return getHeight( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>sapUnitOfMeasure.height</code> attribute. 
	 * @param value the height - Indicates the weight of product
	 */
	public void setHeight(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "height".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>sapUnitOfMeasure.height</code> attribute. 
	 * @param value the height - Indicates the weight of product
	 */
	public void setHeight(final String value)
	{
		setHeight( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>sapUnitOfMeasure.length</code> attribute.
	 * @return the length - Indicates the weight of product
	 */
	public String getLength(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "length".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>sapUnitOfMeasure.length</code> attribute.
	 * @return the length - Indicates the weight of product
	 */
	public String getLength()
	{
		return getLength( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>sapUnitOfMeasure.length</code> attribute. 
	 * @param value the length - Indicates the weight of product
	 */
	public void setLength(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "length".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>sapUnitOfMeasure.length</code> attribute. 
	 * @param value the length - Indicates the weight of product
	 */
	public void setLength(final String value)
	{
		setLength( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>sapUnitOfMeasure.name</code> attribute.
	 * @return the name
	 */
	public String getName(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("SapUnitOfMeasure.getName requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, "name".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>sapUnitOfMeasure.name</code> attribute.
	 * @return the name
	 */
	public String getName()
	{
		return getName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>sapUnitOfMeasure.name</code> attribute. 
	 * @return the localized name
	 */
	public Map<Language,String> getAllName(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,"name".intern(),C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>sapUnitOfMeasure.name</code> attribute. 
	 * @return the localized name
	 */
	public Map<Language,String> getAllName()
	{
		return getAllName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>sapUnitOfMeasure.name</code> attribute. 
	 * @param value the name
	 */
	public void setName(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("SapUnitOfMeasure.setName requires a session language", 0 );
		}
		setLocalizedProperty(ctx, "name".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>sapUnitOfMeasure.name</code> attribute. 
	 * @param value the name
	 */
	public void setName(final String value)
	{
		setName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>sapUnitOfMeasure.name</code> attribute. 
	 * @param value the name
	 */
	public void setAllName(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,"name".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>sapUnitOfMeasure.name</code> attribute. 
	 * @param value the name
	 */
	public void setAllName(final Map<Language,String> value)
	{
		setAllName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>sapUnitOfMeasure.unitType</code> attribute.
	 * @return the unitType
	 */
	public String getUnitType(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "unitType".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>sapUnitOfMeasure.unitType</code> attribute.
	 * @return the unitType
	 */
	public String getUnitType()
	{
		return getUnitType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>sapUnitOfMeasure.unitType</code> attribute. 
	 * @param value the unitType
	 */
	public void setUnitType(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "unitType".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>sapUnitOfMeasure.unitType</code> attribute. 
	 * @param value the unitType
	 */
	public void setUnitType(final String value)
	{
		setUnitType( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>sapUnitOfMeasure.weight</code> attribute.
	 * @return the weight - Indicates the weight of product
	 */
	public String getWeight(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "weight".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>sapUnitOfMeasure.weight</code> attribute.
	 * @return the weight - Indicates the weight of product
	 */
	public String getWeight()
	{
		return getWeight( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>sapUnitOfMeasure.weight</code> attribute. 
	 * @param value the weight - Indicates the weight of product
	 */
	public void setWeight(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "weight".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>sapUnitOfMeasure.weight</code> attribute. 
	 * @param value the weight - Indicates the weight of product
	 */
	public void setWeight(final String value)
	{
		setWeight( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>sapUnitOfMeasure.weightUnit</code> attribute.
	 * @return the weightUnit - Indicates the weight of product
	 */
	public EnumerationValue getWeightUnit(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, "weightUnit".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>sapUnitOfMeasure.weightUnit</code> attribute.
	 * @return the weightUnit - Indicates the weight of product
	 */
	public EnumerationValue getWeightUnit()
	{
		return getWeightUnit( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>sapUnitOfMeasure.weightUnit</code> attribute. 
	 * @param value the weightUnit - Indicates the weight of product
	 */
	public void setWeightUnit(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, "weightUnit".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>sapUnitOfMeasure.weightUnit</code> attribute. 
	 * @param value the weightUnit - Indicates the weight of product
	 */
	public void setWeightUnit(final EnumerationValue value)
	{
		setWeightUnit( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>sapUnitOfMeasure.width</code> attribute.
	 * @return the width - Indicates the weight of product
	 */
	public String getWidth(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "width".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>sapUnitOfMeasure.width</code> attribute.
	 * @return the width - Indicates the weight of product
	 */
	public String getWidth()
	{
		return getWidth( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>sapUnitOfMeasure.width</code> attribute. 
	 * @param value the width - Indicates the weight of product
	 */
	public void setWidth(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "width".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>sapUnitOfMeasure.width</code> attribute. 
	 * @param value the width - Indicates the weight of product
	 */
	public void setWidth(final String value)
	{
		setWidth( getSession().getSessionContext(), value );
	}
	
}
