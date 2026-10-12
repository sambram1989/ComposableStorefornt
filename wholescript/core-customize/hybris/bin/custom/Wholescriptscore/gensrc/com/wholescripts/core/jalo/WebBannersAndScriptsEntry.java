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
import de.hybris.platform.jalo.media.Media;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type WebBannersAndScriptsEntry.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class WebBannersAndScriptsEntry extends GenericItem
{
	/** Qualifier of the <code>WebBannersAndScriptsEntry.entryNumber</code> attribute **/
	public static final String ENTRYNUMBER = "entryNumber";
	/** Qualifier of the <code>WebBannersAndScriptsEntry.optionId</code> attribute **/
	public static final String OPTIONID = "optionId";
	/** Qualifier of the <code>WebBannersAndScriptsEntry.optionName</code> attribute **/
	public static final String OPTIONNAME = "optionName";
	/** Qualifier of the <code>WebBannersAndScriptsEntry.script</code> attribute **/
	public static final String SCRIPT = "script";
	/** Qualifier of the <code>WebBannersAndScriptsEntry.image</code> attribute **/
	public static final String IMAGE = "image";
	/** Qualifier of the <code>WebBannersAndScriptsEntry.webPageViewName</code> attribute **/
	public static final String WEBPAGEVIEWNAME = "webPageViewName";
	/** Qualifier of the <code>WebBannersAndScriptsEntry.webPageViewUrl</code> attribute **/
	public static final String WEBPAGEVIEWURL = "webPageViewUrl";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(ENTRYNUMBER, AttributeMode.INITIAL);
		tmp.put(OPTIONID, AttributeMode.INITIAL);
		tmp.put(OPTIONNAME, AttributeMode.INITIAL);
		tmp.put(SCRIPT, AttributeMode.INITIAL);
		tmp.put(IMAGE, AttributeMode.INITIAL);
		tmp.put(WEBPAGEVIEWNAME, AttributeMode.INITIAL);
		tmp.put(WEBPAGEVIEWURL, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebBannersAndScriptsEntry.entryNumber</code> attribute.
	 * @return the entryNumber
	 */
	public Integer getEntryNumber(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, "entryNumber".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebBannersAndScriptsEntry.entryNumber</code> attribute.
	 * @return the entryNumber
	 */
	public Integer getEntryNumber()
	{
		return getEntryNumber( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebBannersAndScriptsEntry.entryNumber</code> attribute. 
	 * @return the entryNumber
	 */
	public int getEntryNumberAsPrimitive(final SessionContext ctx)
	{
		Integer value = getEntryNumber( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebBannersAndScriptsEntry.entryNumber</code> attribute. 
	 * @return the entryNumber
	 */
	public int getEntryNumberAsPrimitive()
	{
		return getEntryNumberAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebBannersAndScriptsEntry.entryNumber</code> attribute. 
	 * @param value the entryNumber
	 */
	public void setEntryNumber(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, "entryNumber".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebBannersAndScriptsEntry.entryNumber</code> attribute. 
	 * @param value the entryNumber
	 */
	public void setEntryNumber(final Integer value)
	{
		setEntryNumber( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebBannersAndScriptsEntry.entryNumber</code> attribute. 
	 * @param value the entryNumber
	 */
	public void setEntryNumber(final SessionContext ctx, final int value)
	{
		setEntryNumber( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebBannersAndScriptsEntry.entryNumber</code> attribute. 
	 * @param value the entryNumber
	 */
	public void setEntryNumber(final int value)
	{
		setEntryNumber( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebBannersAndScriptsEntry.image</code> attribute.
	 * @return the image
	 */
	public Media getImage(final SessionContext ctx)
	{
		return (Media)getProperty( ctx, "image".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebBannersAndScriptsEntry.image</code> attribute.
	 * @return the image
	 */
	public Media getImage()
	{
		return getImage( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebBannersAndScriptsEntry.image</code> attribute. 
	 * @param value the image
	 */
	public void setImage(final SessionContext ctx, final Media value)
	{
		setProperty(ctx, "image".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebBannersAndScriptsEntry.image</code> attribute. 
	 * @param value the image
	 */
	public void setImage(final Media value)
	{
		setImage( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebBannersAndScriptsEntry.optionId</code> attribute.
	 * @return the optionId
	 */
	public String getOptionId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "optionId".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebBannersAndScriptsEntry.optionId</code> attribute.
	 * @return the optionId
	 */
	public String getOptionId()
	{
		return getOptionId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebBannersAndScriptsEntry.optionId</code> attribute. 
	 * @param value the optionId
	 */
	public void setOptionId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "optionId".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebBannersAndScriptsEntry.optionId</code> attribute. 
	 * @param value the optionId
	 */
	public void setOptionId(final String value)
	{
		setOptionId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebBannersAndScriptsEntry.optionName</code> attribute.
	 * @return the optionName
	 */
	public String getOptionName(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("WebBannersAndScriptsEntry.getOptionName requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, "optionName".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebBannersAndScriptsEntry.optionName</code> attribute.
	 * @return the optionName
	 */
	public String getOptionName()
	{
		return getOptionName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebBannersAndScriptsEntry.optionName</code> attribute. 
	 * @return the localized optionName
	 */
	public Map<Language,String> getAllOptionName(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,"optionName".intern(),C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebBannersAndScriptsEntry.optionName</code> attribute. 
	 * @return the localized optionName
	 */
	public Map<Language,String> getAllOptionName()
	{
		return getAllOptionName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebBannersAndScriptsEntry.optionName</code> attribute. 
	 * @param value the optionName
	 */
	public void setOptionName(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("WebBannersAndScriptsEntry.setOptionName requires a session language", 0 );
		}
		setLocalizedProperty(ctx, "optionName".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebBannersAndScriptsEntry.optionName</code> attribute. 
	 * @param value the optionName
	 */
	public void setOptionName(final String value)
	{
		setOptionName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebBannersAndScriptsEntry.optionName</code> attribute. 
	 * @param value the optionName
	 */
	public void setAllOptionName(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,"optionName".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebBannersAndScriptsEntry.optionName</code> attribute. 
	 * @param value the optionName
	 */
	public void setAllOptionName(final Map<Language,String> value)
	{
		setAllOptionName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebBannersAndScriptsEntry.script</code> attribute.
	 * @return the script
	 */
	public String getScript(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "script".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebBannersAndScriptsEntry.script</code> attribute.
	 * @return the script
	 */
	public String getScript()
	{
		return getScript( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebBannersAndScriptsEntry.script</code> attribute. 
	 * @param value the script
	 */
	public void setScript(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "script".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebBannersAndScriptsEntry.script</code> attribute. 
	 * @param value the script
	 */
	public void setScript(final String value)
	{
		setScript( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebBannersAndScriptsEntry.webPageViewName</code> attribute.
	 * @return the webPageViewName
	 */
	public String getWebPageViewName(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("WebBannersAndScriptsEntry.getWebPageViewName requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, "webPageViewName".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebBannersAndScriptsEntry.webPageViewName</code> attribute.
	 * @return the webPageViewName
	 */
	public String getWebPageViewName()
	{
		return getWebPageViewName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebBannersAndScriptsEntry.webPageViewName</code> attribute. 
	 * @return the localized webPageViewName
	 */
	public Map<Language,String> getAllWebPageViewName(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,"webPageViewName".intern(),C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebBannersAndScriptsEntry.webPageViewName</code> attribute. 
	 * @return the localized webPageViewName
	 */
	public Map<Language,String> getAllWebPageViewName()
	{
		return getAllWebPageViewName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebBannersAndScriptsEntry.webPageViewName</code> attribute. 
	 * @param value the webPageViewName
	 */
	public void setWebPageViewName(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("WebBannersAndScriptsEntry.setWebPageViewName requires a session language", 0 );
		}
		setLocalizedProperty(ctx, "webPageViewName".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebBannersAndScriptsEntry.webPageViewName</code> attribute. 
	 * @param value the webPageViewName
	 */
	public void setWebPageViewName(final String value)
	{
		setWebPageViewName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebBannersAndScriptsEntry.webPageViewName</code> attribute. 
	 * @param value the webPageViewName
	 */
	public void setAllWebPageViewName(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,"webPageViewName".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebBannersAndScriptsEntry.webPageViewName</code> attribute. 
	 * @param value the webPageViewName
	 */
	public void setAllWebPageViewName(final Map<Language,String> value)
	{
		setAllWebPageViewName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebBannersAndScriptsEntry.webPageViewUrl</code> attribute.
	 * @return the webPageViewUrl
	 */
	public String getWebPageViewUrl(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "webPageViewUrl".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebBannersAndScriptsEntry.webPageViewUrl</code> attribute.
	 * @return the webPageViewUrl
	 */
	public String getWebPageViewUrl()
	{
		return getWebPageViewUrl( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebBannersAndScriptsEntry.webPageViewUrl</code> attribute. 
	 * @param value the webPageViewUrl
	 */
	public void setWebPageViewUrl(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "webPageViewUrl".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebBannersAndScriptsEntry.webPageViewUrl</code> attribute. 
	 * @param value the webPageViewUrl
	 */
	public void setWebPageViewUrl(final String value)
	{
		setWebPageViewUrl( getSession().getSessionContext(), value );
	}
	
}
