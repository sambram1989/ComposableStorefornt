/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 27-May-2025, 11:44:08 pm                    ---
 * ----------------------------------------------------------------
 */
package com.wholescripts.core.jalo;

import com.wholescripts.core.constants.WholescriptscoreConstants;
import com.wholescripts.core.jalo.Webinar;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type Presenter.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class Presenter extends GenericItem
{
	/** Qualifier of the <code>Presenter.code</code> attribute **/
	public static final String CODE = "code";
	/** Qualifier of the <code>Presenter.name</code> attribute **/
	public static final String NAME = "name";
	/** Qualifier of the <code>Presenter.biography</code> attribute **/
	public static final String BIOGRAPHY = "biography";
	/** Qualifier of the <code>Presenter.websiteUrl</code> attribute **/
	public static final String WEBSITEURL = "websiteUrl";
	/** Qualifier of the <code>Presenter.image</code> attribute **/
	public static final String IMAGE = "image";
	/** Qualifier of the <code>Presenter.webinar</code> attribute **/
	public static final String WEBINAR = "webinar";
	/** Relation ordering override parameter constants for Webinar2Presenter from ((Wholescriptscore))*/
	protected static String WEBINAR2PRESENTER_SRC_ORDERED = "relation.Webinar2Presenter.source.ordered";
	protected static String WEBINAR2PRESENTER_TGT_ORDERED = "relation.Webinar2Presenter.target.ordered";
	/** Relation disable markmodifed parameter constants for Webinar2Presenter from ((Wholescriptscore))*/
	protected static String WEBINAR2PRESENTER_MARKMODIFIED = "relation.Webinar2Presenter.markmodified";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CODE, AttributeMode.INITIAL);
		tmp.put(NAME, AttributeMode.INITIAL);
		tmp.put(BIOGRAPHY, AttributeMode.INITIAL);
		tmp.put(WEBSITEURL, AttributeMode.INITIAL);
		tmp.put(IMAGE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Presenter.biography</code> attribute.
	 * @return the biography - Biography
	 */
	public String getBiography(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("Presenter.getBiography requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, "biography".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Presenter.biography</code> attribute.
	 * @return the biography - Biography
	 */
	public String getBiography()
	{
		return getBiography( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Presenter.biography</code> attribute. 
	 * @return the localized biography - Biography
	 */
	public Map<Language,String> getAllBiography(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,"biography".intern(),C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Presenter.biography</code> attribute. 
	 * @return the localized biography - Biography
	 */
	public Map<Language,String> getAllBiography()
	{
		return getAllBiography( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Presenter.biography</code> attribute. 
	 * @param value the biography - Biography
	 */
	public void setBiography(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("Presenter.setBiography requires a session language", 0 );
		}
		setLocalizedProperty(ctx, "biography".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Presenter.biography</code> attribute. 
	 * @param value the biography - Biography
	 */
	public void setBiography(final String value)
	{
		setBiography( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Presenter.biography</code> attribute. 
	 * @param value the biography - Biography
	 */
	public void setAllBiography(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,"biography".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Presenter.biography</code> attribute. 
	 * @param value the biography - Biography
	 */
	public void setAllBiography(final Map<Language,String> value)
	{
		setAllBiography( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Presenter.code</code> attribute.
	 * @return the code
	 */
	public String getCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "code".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Presenter.code</code> attribute.
	 * @return the code
	 */
	public String getCode()
	{
		return getCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Presenter.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "code".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Presenter.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final String value)
	{
		setCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Presenter.image</code> attribute.
	 * @return the image
	 */
	public Media getImage(final SessionContext ctx)
	{
		return (Media)getProperty( ctx, "image".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Presenter.image</code> attribute.
	 * @return the image
	 */
	public Media getImage()
	{
		return getImage( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Presenter.image</code> attribute. 
	 * @param value the image
	 */
	public void setImage(final SessionContext ctx, final Media value)
	{
		setProperty(ctx, "image".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Presenter.image</code> attribute. 
	 * @param value the image
	 */
	public void setImage(final Media value)
	{
		setImage( getSession().getSessionContext(), value );
	}
	
	/**
	 * @deprecated since 2011, use {@link Utilities#getMarkModifiedOverride(de.hybris.platform.jalo.type.RelationType)
	 */
	@Override
	@Deprecated(since = "2105", forRemoval = true)
	public boolean isMarkModifiedDisabled(final Item referencedItem)
	{
		ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("Webinar");
		if(relationSecondEnd0.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(WEBINAR2PRESENTER_MARKMODIFIED);
		}
		return true;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Presenter.name</code> attribute.
	 * @return the name
	 */
	public String getName(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("Presenter.getName requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, "name".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Presenter.name</code> attribute.
	 * @return the name
	 */
	public String getName()
	{
		return getName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Presenter.name</code> attribute. 
	 * @return the localized name
	 */
	public Map<Language,String> getAllName(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,"name".intern(),C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Presenter.name</code> attribute. 
	 * @return the localized name
	 */
	public Map<Language,String> getAllName()
	{
		return getAllName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Presenter.name</code> attribute. 
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
			throw new JaloInvalidParameterException("Presenter.setName requires a session language", 0 );
		}
		setLocalizedProperty(ctx, "name".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Presenter.name</code> attribute. 
	 * @param value the name
	 */
	public void setName(final String value)
	{
		setName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Presenter.name</code> attribute. 
	 * @param value the name
	 */
	public void setAllName(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,"name".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Presenter.name</code> attribute. 
	 * @param value the name
	 */
	public void setAllName(final Map<Language,String> value)
	{
		setAllName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Presenter.webinar</code> attribute.
	 * @return the webinar
	 */
	public List<Webinar> getWebinar(final SessionContext ctx)
	{
		final List<Webinar> items = getLinkedItems( 
			ctx,
			false,
			WholescriptscoreConstants.Relations.WEBINAR2PRESENTER,
			"Webinar",
			null,
			Utilities.getRelationOrderingOverride(WEBINAR2PRESENTER_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(WEBINAR2PRESENTER_TGT_ORDERED, true)
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Presenter.webinar</code> attribute.
	 * @return the webinar
	 */
	public List<Webinar> getWebinar()
	{
		return getWebinar( getSession().getSessionContext() );
	}
	
	public long getWebinarCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			false,
			WholescriptscoreConstants.Relations.WEBINAR2PRESENTER,
			"Webinar",
			null
		);
	}
	
	public long getWebinarCount()
	{
		return getWebinarCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Presenter.webinar</code> attribute. 
	 * @param value the webinar
	 */
	public void setWebinar(final SessionContext ctx, final List<Webinar> value)
	{
		setLinkedItems( 
			ctx,
			false,
			WholescriptscoreConstants.Relations.WEBINAR2PRESENTER,
			null,
			value,
			Utilities.getRelationOrderingOverride(WEBINAR2PRESENTER_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(WEBINAR2PRESENTER_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(WEBINAR2PRESENTER_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Presenter.webinar</code> attribute. 
	 * @param value the webinar
	 */
	public void setWebinar(final List<Webinar> value)
	{
		setWebinar( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to webinar. 
	 * @param value the item to add to webinar
	 */
	public void addToWebinar(final SessionContext ctx, final Webinar value)
	{
		addLinkedItems( 
			ctx,
			false,
			WholescriptscoreConstants.Relations.WEBINAR2PRESENTER,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(WEBINAR2PRESENTER_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(WEBINAR2PRESENTER_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(WEBINAR2PRESENTER_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to webinar. 
	 * @param value the item to add to webinar
	 */
	public void addToWebinar(final Webinar value)
	{
		addToWebinar( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from webinar. 
	 * @param value the item to remove from webinar
	 */
	public void removeFromWebinar(final SessionContext ctx, final Webinar value)
	{
		removeLinkedItems( 
			ctx,
			false,
			WholescriptscoreConstants.Relations.WEBINAR2PRESENTER,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(WEBINAR2PRESENTER_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(WEBINAR2PRESENTER_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(WEBINAR2PRESENTER_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from webinar. 
	 * @param value the item to remove from webinar
	 */
	public void removeFromWebinar(final Webinar value)
	{
		removeFromWebinar( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Presenter.websiteUrl</code> attribute.
	 * @return the websiteUrl
	 */
	public String getWebsiteUrl(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "websiteUrl".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Presenter.websiteUrl</code> attribute.
	 * @return the websiteUrl
	 */
	public String getWebsiteUrl()
	{
		return getWebsiteUrl( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Presenter.websiteUrl</code> attribute. 
	 * @param value the websiteUrl
	 */
	public void setWebsiteUrl(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "websiteUrl".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Presenter.websiteUrl</code> attribute. 
	 * @param value the websiteUrl
	 */
	public void setWebsiteUrl(final String value)
	{
		setWebsiteUrl( getSession().getSessionContext(), value );
	}
	
}
