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
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.media.Media;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type ItemTime.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class ItemTime extends GenericItem
{
	/** Qualifier of the <code>ItemTime.code</code> attribute **/
	public static final String CODE = "code";
	/** Qualifier of the <code>ItemTime.timeOfTheDay</code> attribute **/
	public static final String TIMEOFTHEDAY = "timeOfTheDay";
	/** Qualifier of the <code>ItemTime.iconImage</code> attribute **/
	public static final String ICONIMAGE = "iconImage";
	/** Qualifier of the <code>ItemTime.displayOrder</code> attribute **/
	public static final String DISPLAYORDER = "displayOrder";
	/** Qualifier of the <code>ItemTime.isActive</code> attribute **/
	public static final String ISACTIVE = "isActive";
	/** Qualifier of the <code>ItemTime.sapValue</code> attribute **/
	public static final String SAPVALUE = "sapValue";
	/** Qualifier of the <code>ItemTime.isDisplayable</code> attribute **/
	public static final String ISDISPLAYABLE = "isDisplayable";
	/** Qualifier of the <code>ItemTime.name</code> attribute **/
	public static final String NAME = "name";
	/** Qualifier of the <code>ItemTime.parentItemTimeCode</code> attribute **/
	public static final String PARENTITEMTIMECODE = "parentItemTimeCode";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CODE, AttributeMode.INITIAL);
		tmp.put(TIMEOFTHEDAY, AttributeMode.INITIAL);
		tmp.put(ICONIMAGE, AttributeMode.INITIAL);
		tmp.put(DISPLAYORDER, AttributeMode.INITIAL);
		tmp.put(ISACTIVE, AttributeMode.INITIAL);
		tmp.put(SAPVALUE, AttributeMode.INITIAL);
		tmp.put(ISDISPLAYABLE, AttributeMode.INITIAL);
		tmp.put(NAME, AttributeMode.INITIAL);
		tmp.put(PARENTITEMTIMECODE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemTime.code</code> attribute.
	 * @return the code - code
	 */
	public String getCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "code".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemTime.code</code> attribute.
	 * @return the code - code
	 */
	public String getCode()
	{
		return getCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ItemTime.code</code> attribute. 
	 * @param value the code - code
	 */
	public void setCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "code".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ItemTime.code</code> attribute. 
	 * @param value the code - code
	 */
	public void setCode(final String value)
	{
		setCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemTime.displayOrder</code> attribute.
	 * @return the displayOrder - Is it to be displayed on an Order
	 */
	public String getDisplayOrder(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "displayOrder".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemTime.displayOrder</code> attribute.
	 * @return the displayOrder - Is it to be displayed on an Order
	 */
	public String getDisplayOrder()
	{
		return getDisplayOrder( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ItemTime.displayOrder</code> attribute. 
	 * @param value the displayOrder - Is it to be displayed on an Order
	 */
	public void setDisplayOrder(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "displayOrder".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ItemTime.displayOrder</code> attribute. 
	 * @param value the displayOrder - Is it to be displayed on an Order
	 */
	public void setDisplayOrder(final String value)
	{
		setDisplayOrder( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemTime.iconImage</code> attribute.
	 * @return the iconImage - Image for Time of the day
	 */
	public Media getIconImage(final SessionContext ctx)
	{
		return (Media)getProperty( ctx, "iconImage".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemTime.iconImage</code> attribute.
	 * @return the iconImage - Image for Time of the day
	 */
	public Media getIconImage()
	{
		return getIconImage( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ItemTime.iconImage</code> attribute. 
	 * @param value the iconImage - Image for Time of the day
	 */
	public void setIconImage(final SessionContext ctx, final Media value)
	{
		setProperty(ctx, "iconImage".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ItemTime.iconImage</code> attribute. 
	 * @param value the iconImage - Image for Time of the day
	 */
	public void setIconImage(final Media value)
	{
		setIconImage( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemTime.isActive</code> attribute.
	 * @return the isActive - Is it Active
	 */
	public Boolean isIsActive(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, "isActive".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemTime.isActive</code> attribute.
	 * @return the isActive - Is it Active
	 */
	public Boolean isIsActive()
	{
		return isIsActive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemTime.isActive</code> attribute. 
	 * @return the isActive - Is it Active
	 */
	public boolean isIsActiveAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isIsActive( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemTime.isActive</code> attribute. 
	 * @return the isActive - Is it Active
	 */
	public boolean isIsActiveAsPrimitive()
	{
		return isIsActiveAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ItemTime.isActive</code> attribute. 
	 * @param value the isActive - Is it Active
	 */
	public void setIsActive(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, "isActive".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ItemTime.isActive</code> attribute. 
	 * @param value the isActive - Is it Active
	 */
	public void setIsActive(final Boolean value)
	{
		setIsActive( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ItemTime.isActive</code> attribute. 
	 * @param value the isActive - Is it Active
	 */
	public void setIsActive(final SessionContext ctx, final boolean value)
	{
		setIsActive( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ItemTime.isActive</code> attribute. 
	 * @param value the isActive - Is it Active
	 */
	public void setIsActive(final boolean value)
	{
		setIsActive( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemTime.isDisplayable</code> attribute.
	 * @return the isDisplayable - is it displayable
	 */
	public Boolean isIsDisplayable(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, "isDisplayable".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemTime.isDisplayable</code> attribute.
	 * @return the isDisplayable - is it displayable
	 */
	public Boolean isIsDisplayable()
	{
		return isIsDisplayable( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemTime.isDisplayable</code> attribute. 
	 * @return the isDisplayable - is it displayable
	 */
	public boolean isIsDisplayableAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isIsDisplayable( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemTime.isDisplayable</code> attribute. 
	 * @return the isDisplayable - is it displayable
	 */
	public boolean isIsDisplayableAsPrimitive()
	{
		return isIsDisplayableAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ItemTime.isDisplayable</code> attribute. 
	 * @param value the isDisplayable - is it displayable
	 */
	public void setIsDisplayable(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, "isDisplayable".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ItemTime.isDisplayable</code> attribute. 
	 * @param value the isDisplayable - is it displayable
	 */
	public void setIsDisplayable(final Boolean value)
	{
		setIsDisplayable( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ItemTime.isDisplayable</code> attribute. 
	 * @param value the isDisplayable - is it displayable
	 */
	public void setIsDisplayable(final SessionContext ctx, final boolean value)
	{
		setIsDisplayable( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ItemTime.isDisplayable</code> attribute. 
	 * @param value the isDisplayable - is it displayable
	 */
	public void setIsDisplayable(final boolean value)
	{
		setIsDisplayable( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemTime.name</code> attribute.
	 * @return the name - Name of the field
	 */
	public String getName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "name".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemTime.name</code> attribute.
	 * @return the name - Name of the field
	 */
	public String getName()
	{
		return getName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ItemTime.name</code> attribute. 
	 * @param value the name - Name of the field
	 */
	public void setName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "name".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ItemTime.name</code> attribute. 
	 * @param value the name - Name of the field
	 */
	public void setName(final String value)
	{
		setName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemTime.parentItemTimeCode</code> attribute.
	 * @return the parentItemTimeCode
	 */
	public String getParentItemTimeCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "parentItemTimeCode".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemTime.parentItemTimeCode</code> attribute.
	 * @return the parentItemTimeCode
	 */
	public String getParentItemTimeCode()
	{
		return getParentItemTimeCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ItemTime.parentItemTimeCode</code> attribute. 
	 * @param value the parentItemTimeCode
	 */
	public void setParentItemTimeCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "parentItemTimeCode".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ItemTime.parentItemTimeCode</code> attribute. 
	 * @param value the parentItemTimeCode
	 */
	public void setParentItemTimeCode(final String value)
	{
		setParentItemTimeCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemTime.sapValue</code> attribute.
	 * @return the sapValue - SAP Value
	 */
	public String getSapValue(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "sapValue".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemTime.sapValue</code> attribute.
	 * @return the sapValue - SAP Value
	 */
	public String getSapValue()
	{
		return getSapValue( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ItemTime.sapValue</code> attribute. 
	 * @param value the sapValue - SAP Value
	 */
	public void setSapValue(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "sapValue".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ItemTime.sapValue</code> attribute. 
	 * @param value the sapValue - SAP Value
	 */
	public void setSapValue(final String value)
	{
		setSapValue( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemTime.timeOfTheDay</code> attribute.
	 * @return the timeOfTheDay - Time Of The Day Enum
	 */
	public EnumerationValue getTimeOfTheDay(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, "timeOfTheDay".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ItemTime.timeOfTheDay</code> attribute.
	 * @return the timeOfTheDay - Time Of The Day Enum
	 */
	public EnumerationValue getTimeOfTheDay()
	{
		return getTimeOfTheDay( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ItemTime.timeOfTheDay</code> attribute. 
	 * @param value the timeOfTheDay - Time Of The Day Enum
	 */
	public void setTimeOfTheDay(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, "timeOfTheDay".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ItemTime.timeOfTheDay</code> attribute. 
	 * @param value the timeOfTheDay - Time Of The Day Enum
	 */
	public void setTimeOfTheDay(final EnumerationValue value)
	{
		setTimeOfTheDay( getSession().getSessionContext(), value );
	}
	
}
