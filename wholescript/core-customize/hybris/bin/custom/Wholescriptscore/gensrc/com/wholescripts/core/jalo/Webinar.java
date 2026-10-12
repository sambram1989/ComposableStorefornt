/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 27-May-2025, 11:44:08 pm                    ---
 * ----------------------------------------------------------------
 */
package com.wholescripts.core.jalo;

import com.wholescripts.core.constants.WholescriptscoreConstants;
import com.wholescripts.core.jalo.Presenter;
import com.wholescripts.core.jalo.WebinarCategoryTag;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type Webinar.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class Webinar extends GenericItem
{
	/** Qualifier of the <code>Webinar.code</code> attribute **/
	public static final String CODE = "code";
	/** Qualifier of the <code>Webinar.title</code> attribute **/
	public static final String TITLE = "title";
	/** Qualifier of the <code>Webinar.shortDescription</code> attribute **/
	public static final String SHORTDESCRIPTION = "shortDescription";
	/** Qualifier of the <code>Webinar.description</code> attribute **/
	public static final String DESCRIPTION = "description";
	/** Qualifier of the <code>Webinar.duration</code> attribute **/
	public static final String DURATION = "duration";
	/** Qualifier of the <code>Webinar.isFeatured</code> attribute **/
	public static final String ISFEATURED = "isFeatured";
	/** Qualifier of the <code>Webinar.videoImage</code> attribute **/
	public static final String VIDEOIMAGE = "videoImage";
	/** Qualifier of the <code>Webinar.video</code> attribute **/
	public static final String VIDEO = "video";
	/** Qualifier of the <code>Webinar.videoStatus</code> attribute **/
	public static final String VIDEOSTATUS = "videoStatus";
	/** Qualifier of the <code>Webinar.sponsorImageUrl</code> attribute **/
	public static final String SPONSORIMAGEURL = "sponsorImageUrl";
	/** Qualifier of the <code>Webinar.sponsorImageAltText</code> attribute **/
	public static final String SPONSORIMAGEALTTEXT = "sponsorImageAltText";
	/** Qualifier of the <code>Webinar.sponsorName</code> attribute **/
	public static final String SPONSORNAME = "sponsorName";
	/** Qualifier of the <code>Webinar.webinarVideoUrl</code> attribute **/
	public static final String WEBINARVIDEOURL = "webinarVideoUrl";
	/** Qualifier of the <code>Webinar.publishedDate</code> attribute **/
	public static final String PUBLISHEDDATE = "publishedDate";
	/** Qualifier of the <code>Webinar.webinarCaptionsUrl</code> attribute **/
	public static final String WEBINARCAPTIONSURL = "webinarCaptionsUrl";
	/** Qualifier of the <code>Webinar.sponsorImage</code> attribute **/
	public static final String SPONSORIMAGE = "sponsorImage";
	/** Qualifier of the <code>Webinar.presenter</code> attribute **/
	public static final String PRESENTER = "presenter";
	/** Relation ordering override parameter constants for Webinar2Presenter from ((Wholescriptscore))*/
	protected static String WEBINAR2PRESENTER_SRC_ORDERED = "relation.Webinar2Presenter.source.ordered";
	protected static String WEBINAR2PRESENTER_TGT_ORDERED = "relation.Webinar2Presenter.target.ordered";
	/** Relation disable markmodifed parameter constants for Webinar2Presenter from ((Wholescriptscore))*/
	protected static String WEBINAR2PRESENTER_MARKMODIFIED = "relation.Webinar2Presenter.markmodified";
	/** Qualifier of the <code>Webinar.webinarCategoryTag</code> attribute **/
	public static final String WEBINARCATEGORYTAG = "webinarCategoryTag";
	/** Relation ordering override parameter constants for Webinar2WebinarCategoryTag from ((Wholescriptscore))*/
	protected static String WEBINAR2WEBINARCATEGORYTAG_SRC_ORDERED = "relation.Webinar2WebinarCategoryTag.source.ordered";
	protected static String WEBINAR2WEBINARCATEGORYTAG_TGT_ORDERED = "relation.Webinar2WebinarCategoryTag.target.ordered";
	/** Relation disable markmodifed parameter constants for Webinar2WebinarCategoryTag from ((Wholescriptscore))*/
	protected static String WEBINAR2WEBINARCATEGORYTAG_MARKMODIFIED = "relation.Webinar2WebinarCategoryTag.markmodified";
	/** Qualifier of the <code>Webinar.category</code> attribute **/
	public static final String CATEGORY = "category";
	/** Relation ordering override parameter constants for Webinar2Category from ((Wholescriptscore))*/
	protected static String WEBINAR2CATEGORY_SRC_ORDERED = "relation.Webinar2Category.source.ordered";
	protected static String WEBINAR2CATEGORY_TGT_ORDERED = "relation.Webinar2Category.target.ordered";
	/** Relation disable markmodifed parameter constants for Webinar2Category from ((Wholescriptscore))*/
	protected static String WEBINAR2CATEGORY_MARKMODIFIED = "relation.Webinar2Category.markmodified";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CODE, AttributeMode.INITIAL);
		tmp.put(TITLE, AttributeMode.INITIAL);
		tmp.put(SHORTDESCRIPTION, AttributeMode.INITIAL);
		tmp.put(DESCRIPTION, AttributeMode.INITIAL);
		tmp.put(DURATION, AttributeMode.INITIAL);
		tmp.put(ISFEATURED, AttributeMode.INITIAL);
		tmp.put(VIDEOIMAGE, AttributeMode.INITIAL);
		tmp.put(VIDEO, AttributeMode.INITIAL);
		tmp.put(VIDEOSTATUS, AttributeMode.INITIAL);
		tmp.put(SPONSORIMAGEURL, AttributeMode.INITIAL);
		tmp.put(SPONSORIMAGEALTTEXT, AttributeMode.INITIAL);
		tmp.put(SPONSORNAME, AttributeMode.INITIAL);
		tmp.put(WEBINARVIDEOURL, AttributeMode.INITIAL);
		tmp.put(PUBLISHEDDATE, AttributeMode.INITIAL);
		tmp.put(WEBINARCAPTIONSURL, AttributeMode.INITIAL);
		tmp.put(SPONSORIMAGE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.category</code> attribute.
	 * @return the category
	 */
	public List<Category> getCategory(final SessionContext ctx)
	{
		final List<Category> items = getLinkedItems( 
			ctx,
			true,
			WholescriptscoreConstants.Relations.WEBINAR2CATEGORY,
			"Category",
			null,
			Utilities.getRelationOrderingOverride(WEBINAR2CATEGORY_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(WEBINAR2CATEGORY_TGT_ORDERED, true)
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.category</code> attribute.
	 * @return the category
	 */
	public List<Category> getCategory()
	{
		return getCategory( getSession().getSessionContext() );
	}
	
	public long getCategoryCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			true,
			WholescriptscoreConstants.Relations.WEBINAR2CATEGORY,
			"Category",
			null
		);
	}
	
	public long getCategoryCount()
	{
		return getCategoryCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.category</code> attribute. 
	 * @param value the category
	 */
	public void setCategory(final SessionContext ctx, final List<Category> value)
	{
		setLinkedItems( 
			ctx,
			true,
			WholescriptscoreConstants.Relations.WEBINAR2CATEGORY,
			null,
			value,
			Utilities.getRelationOrderingOverride(WEBINAR2CATEGORY_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(WEBINAR2CATEGORY_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(WEBINAR2CATEGORY_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.category</code> attribute. 
	 * @param value the category
	 */
	public void setCategory(final List<Category> value)
	{
		setCategory( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to category. 
	 * @param value the item to add to category
	 */
	public void addToCategory(final SessionContext ctx, final Category value)
	{
		addLinkedItems( 
			ctx,
			true,
			WholescriptscoreConstants.Relations.WEBINAR2CATEGORY,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(WEBINAR2CATEGORY_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(WEBINAR2CATEGORY_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(WEBINAR2CATEGORY_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to category. 
	 * @param value the item to add to category
	 */
	public void addToCategory(final Category value)
	{
		addToCategory( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from category. 
	 * @param value the item to remove from category
	 */
	public void removeFromCategory(final SessionContext ctx, final Category value)
	{
		removeLinkedItems( 
			ctx,
			true,
			WholescriptscoreConstants.Relations.WEBINAR2CATEGORY,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(WEBINAR2CATEGORY_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(WEBINAR2CATEGORY_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(WEBINAR2CATEGORY_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from category. 
	 * @param value the item to remove from category
	 */
	public void removeFromCategory(final Category value)
	{
		removeFromCategory( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.code</code> attribute.
	 * @return the code
	 */
	public String getCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "code".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.code</code> attribute.
	 * @return the code
	 */
	public String getCode()
	{
		return getCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "code".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final String value)
	{
		setCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.description</code> attribute.
	 * @return the description - Webinar description
	 */
	public String getDescription(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("Webinar.getDescription requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, "description".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.description</code> attribute.
	 * @return the description - Webinar description
	 */
	public String getDescription()
	{
		return getDescription( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.description</code> attribute. 
	 * @return the localized description - Webinar description
	 */
	public Map<Language,String> getAllDescription(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,"description".intern(),C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.description</code> attribute. 
	 * @return the localized description - Webinar description
	 */
	public Map<Language,String> getAllDescription()
	{
		return getAllDescription( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.description</code> attribute. 
	 * @param value the description - Webinar description
	 */
	public void setDescription(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("Webinar.setDescription requires a session language", 0 );
		}
		setLocalizedProperty(ctx, "description".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.description</code> attribute. 
	 * @param value the description - Webinar description
	 */
	public void setDescription(final String value)
	{
		setDescription( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.description</code> attribute. 
	 * @param value the description - Webinar description
	 */
	public void setAllDescription(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,"description".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.description</code> attribute. 
	 * @param value the description - Webinar description
	 */
	public void setAllDescription(final Map<Language,String> value)
	{
		setAllDescription( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.duration</code> attribute.
	 * @return the duration
	 */
	public EnumerationValue getDuration(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, "duration".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.duration</code> attribute.
	 * @return the duration
	 */
	public EnumerationValue getDuration()
	{
		return getDuration( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.duration</code> attribute. 
	 * @param value the duration
	 */
	public void setDuration(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, "duration".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.duration</code> attribute. 
	 * @param value the duration
	 */
	public void setDuration(final EnumerationValue value)
	{
		setDuration( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.isFeatured</code> attribute.
	 * @return the isFeatured
	 */
	public Boolean isIsFeatured(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, "isFeatured".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.isFeatured</code> attribute.
	 * @return the isFeatured
	 */
	public Boolean isIsFeatured()
	{
		return isIsFeatured( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.isFeatured</code> attribute. 
	 * @return the isFeatured
	 */
	public boolean isIsFeaturedAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isIsFeatured( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.isFeatured</code> attribute. 
	 * @return the isFeatured
	 */
	public boolean isIsFeaturedAsPrimitive()
	{
		return isIsFeaturedAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.isFeatured</code> attribute. 
	 * @param value the isFeatured
	 */
	public void setIsFeatured(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, "isFeatured".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.isFeatured</code> attribute. 
	 * @param value the isFeatured
	 */
	public void setIsFeatured(final Boolean value)
	{
		setIsFeatured( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.isFeatured</code> attribute. 
	 * @param value the isFeatured
	 */
	public void setIsFeatured(final SessionContext ctx, final boolean value)
	{
		setIsFeatured( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.isFeatured</code> attribute. 
	 * @param value the isFeatured
	 */
	public void setIsFeatured(final boolean value)
	{
		setIsFeatured( getSession().getSessionContext(), value );
	}
	
	/**
	 * @deprecated since 2011, use {@link Utilities#getMarkModifiedOverride(de.hybris.platform.jalo.type.RelationType)
	 */
	@Override
	@Deprecated(since = "2105", forRemoval = true)
	public boolean isMarkModifiedDisabled(final Item referencedItem)
	{
		ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("Presenter");
		if(relationSecondEnd0.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(WEBINAR2PRESENTER_MARKMODIFIED);
		}
		ComposedType relationSecondEnd1 = TypeManager.getInstance().getComposedType("WebinarCategoryTag");
		if(relationSecondEnd1.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(WEBINAR2WEBINARCATEGORYTAG_MARKMODIFIED);
		}
		ComposedType relationSecondEnd2 = TypeManager.getInstance().getComposedType("Category");
		if(relationSecondEnd2.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(WEBINAR2CATEGORY_MARKMODIFIED);
		}
		return true;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.presenter</code> attribute.
	 * @return the presenter
	 */
	public List<Presenter> getPresenter(final SessionContext ctx)
	{
		final List<Presenter> items = getLinkedItems( 
			ctx,
			true,
			WholescriptscoreConstants.Relations.WEBINAR2PRESENTER,
			"Presenter",
			null,
			Utilities.getRelationOrderingOverride(WEBINAR2PRESENTER_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(WEBINAR2PRESENTER_TGT_ORDERED, true)
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.presenter</code> attribute.
	 * @return the presenter
	 */
	public List<Presenter> getPresenter()
	{
		return getPresenter( getSession().getSessionContext() );
	}
	
	public long getPresenterCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			true,
			WholescriptscoreConstants.Relations.WEBINAR2PRESENTER,
			"Presenter",
			null
		);
	}
	
	public long getPresenterCount()
	{
		return getPresenterCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.presenter</code> attribute. 
	 * @param value the presenter
	 */
	public void setPresenter(final SessionContext ctx, final List<Presenter> value)
	{
		setLinkedItems( 
			ctx,
			true,
			WholescriptscoreConstants.Relations.WEBINAR2PRESENTER,
			null,
			value,
			Utilities.getRelationOrderingOverride(WEBINAR2PRESENTER_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(WEBINAR2PRESENTER_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(WEBINAR2PRESENTER_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.presenter</code> attribute. 
	 * @param value the presenter
	 */
	public void setPresenter(final List<Presenter> value)
	{
		setPresenter( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to presenter. 
	 * @param value the item to add to presenter
	 */
	public void addToPresenter(final SessionContext ctx, final Presenter value)
	{
		addLinkedItems( 
			ctx,
			true,
			WholescriptscoreConstants.Relations.WEBINAR2PRESENTER,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(WEBINAR2PRESENTER_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(WEBINAR2PRESENTER_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(WEBINAR2PRESENTER_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to presenter. 
	 * @param value the item to add to presenter
	 */
	public void addToPresenter(final Presenter value)
	{
		addToPresenter( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from presenter. 
	 * @param value the item to remove from presenter
	 */
	public void removeFromPresenter(final SessionContext ctx, final Presenter value)
	{
		removeLinkedItems( 
			ctx,
			true,
			WholescriptscoreConstants.Relations.WEBINAR2PRESENTER,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(WEBINAR2PRESENTER_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(WEBINAR2PRESENTER_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(WEBINAR2PRESENTER_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from presenter. 
	 * @param value the item to remove from presenter
	 */
	public void removeFromPresenter(final Presenter value)
	{
		removeFromPresenter( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.publishedDate</code> attribute.
	 * @return the publishedDate
	 */
	public String getPublishedDate(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "publishedDate".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.publishedDate</code> attribute.
	 * @return the publishedDate
	 */
	public String getPublishedDate()
	{
		return getPublishedDate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.publishedDate</code> attribute. 
	 * @param value the publishedDate
	 */
	public void setPublishedDate(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "publishedDate".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.publishedDate</code> attribute. 
	 * @param value the publishedDate
	 */
	public void setPublishedDate(final String value)
	{
		setPublishedDate( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.shortDescription</code> attribute.
	 * @return the shortDescription
	 */
	public String getShortDescription(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("Webinar.getShortDescription requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, "shortDescription".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.shortDescription</code> attribute.
	 * @return the shortDescription
	 */
	public String getShortDescription()
	{
		return getShortDescription( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.shortDescription</code> attribute. 
	 * @return the localized shortDescription
	 */
	public Map<Language,String> getAllShortDescription(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,"shortDescription".intern(),C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.shortDescription</code> attribute. 
	 * @return the localized shortDescription
	 */
	public Map<Language,String> getAllShortDescription()
	{
		return getAllShortDescription( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.shortDescription</code> attribute. 
	 * @param value the shortDescription
	 */
	public void setShortDescription(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("Webinar.setShortDescription requires a session language", 0 );
		}
		setLocalizedProperty(ctx, "shortDescription".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.shortDescription</code> attribute. 
	 * @param value the shortDescription
	 */
	public void setShortDescription(final String value)
	{
		setShortDescription( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.shortDescription</code> attribute. 
	 * @param value the shortDescription
	 */
	public void setAllShortDescription(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,"shortDescription".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.shortDescription</code> attribute. 
	 * @param value the shortDescription
	 */
	public void setAllShortDescription(final Map<Language,String> value)
	{
		setAllShortDescription( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.sponsorImage</code> attribute.
	 * @return the sponsorImage
	 */
	public Media getSponsorImage(final SessionContext ctx)
	{
		return (Media)getProperty( ctx, "sponsorImage".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.sponsorImage</code> attribute.
	 * @return the sponsorImage
	 */
	public Media getSponsorImage()
	{
		return getSponsorImage( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.sponsorImage</code> attribute. 
	 * @param value the sponsorImage
	 */
	public void setSponsorImage(final SessionContext ctx, final Media value)
	{
		setProperty(ctx, "sponsorImage".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.sponsorImage</code> attribute. 
	 * @param value the sponsorImage
	 */
	public void setSponsorImage(final Media value)
	{
		setSponsorImage( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.sponsorImageAltText</code> attribute.
	 * @return the sponsorImageAltText
	 */
	public String getSponsorImageAltText(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "sponsorImageAltText".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.sponsorImageAltText</code> attribute.
	 * @return the sponsorImageAltText
	 */
	public String getSponsorImageAltText()
	{
		return getSponsorImageAltText( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.sponsorImageAltText</code> attribute. 
	 * @param value the sponsorImageAltText
	 */
	public void setSponsorImageAltText(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "sponsorImageAltText".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.sponsorImageAltText</code> attribute. 
	 * @param value the sponsorImageAltText
	 */
	public void setSponsorImageAltText(final String value)
	{
		setSponsorImageAltText( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.sponsorImageUrl</code> attribute.
	 * @return the sponsorImageUrl
	 */
	public String getSponsorImageUrl(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "sponsorImageUrl".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.sponsorImageUrl</code> attribute.
	 * @return the sponsorImageUrl
	 */
	public String getSponsorImageUrl()
	{
		return getSponsorImageUrl( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.sponsorImageUrl</code> attribute. 
	 * @param value the sponsorImageUrl
	 */
	public void setSponsorImageUrl(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "sponsorImageUrl".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.sponsorImageUrl</code> attribute. 
	 * @param value the sponsorImageUrl
	 */
	public void setSponsorImageUrl(final String value)
	{
		setSponsorImageUrl( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.sponsorName</code> attribute.
	 * @return the sponsorName
	 */
	public String getSponsorName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "sponsorName".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.sponsorName</code> attribute.
	 * @return the sponsorName
	 */
	public String getSponsorName()
	{
		return getSponsorName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.sponsorName</code> attribute. 
	 * @param value the sponsorName
	 */
	public void setSponsorName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "sponsorName".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.sponsorName</code> attribute. 
	 * @param value the sponsorName
	 */
	public void setSponsorName(final String value)
	{
		setSponsorName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.title</code> attribute.
	 * @return the title
	 */
	public String getTitle(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("Webinar.getTitle requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, "title".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.title</code> attribute.
	 * @return the title
	 */
	public String getTitle()
	{
		return getTitle( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.title</code> attribute. 
	 * @return the localized title
	 */
	public Map<Language,String> getAllTitle(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,"title".intern(),C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.title</code> attribute. 
	 * @return the localized title
	 */
	public Map<Language,String> getAllTitle()
	{
		return getAllTitle( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.title</code> attribute. 
	 * @param value the title
	 */
	public void setTitle(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("Webinar.setTitle requires a session language", 0 );
		}
		setLocalizedProperty(ctx, "title".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.title</code> attribute. 
	 * @param value the title
	 */
	public void setTitle(final String value)
	{
		setTitle( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.title</code> attribute. 
	 * @param value the title
	 */
	public void setAllTitle(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,"title".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.title</code> attribute. 
	 * @param value the title
	 */
	public void setAllTitle(final Map<Language,String> value)
	{
		setAllTitle( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.video</code> attribute.
	 * @return the video
	 */
	public Media getVideo(final SessionContext ctx)
	{
		return (Media)getProperty( ctx, "video".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.video</code> attribute.
	 * @return the video
	 */
	public Media getVideo()
	{
		return getVideo( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.video</code> attribute. 
	 * @param value the video
	 */
	public void setVideo(final SessionContext ctx, final Media value)
	{
		setProperty(ctx, "video".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.video</code> attribute. 
	 * @param value the video
	 */
	public void setVideo(final Media value)
	{
		setVideo( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.videoImage</code> attribute.
	 * @return the videoImage
	 */
	public Media getVideoImage(final SessionContext ctx)
	{
		return (Media)getProperty( ctx, "videoImage".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.videoImage</code> attribute.
	 * @return the videoImage
	 */
	public Media getVideoImage()
	{
		return getVideoImage( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.videoImage</code> attribute. 
	 * @param value the videoImage
	 */
	public void setVideoImage(final SessionContext ctx, final Media value)
	{
		setProperty(ctx, "videoImage".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.videoImage</code> attribute. 
	 * @param value the videoImage
	 */
	public void setVideoImage(final Media value)
	{
		setVideoImage( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.videoStatus</code> attribute.
	 * @return the videoStatus
	 */
	public EnumerationValue getVideoStatus(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, "videoStatus".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.videoStatus</code> attribute.
	 * @return the videoStatus
	 */
	public EnumerationValue getVideoStatus()
	{
		return getVideoStatus( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.videoStatus</code> attribute. 
	 * @param value the videoStatus
	 */
	public void setVideoStatus(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, "videoStatus".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.videoStatus</code> attribute. 
	 * @param value the videoStatus
	 */
	public void setVideoStatus(final EnumerationValue value)
	{
		setVideoStatus( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.webinarCaptionsUrl</code> attribute.
	 * @return the webinarCaptionsUrl
	 */
	public String getWebinarCaptionsUrl(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "webinarCaptionsUrl".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.webinarCaptionsUrl</code> attribute.
	 * @return the webinarCaptionsUrl
	 */
	public String getWebinarCaptionsUrl()
	{
		return getWebinarCaptionsUrl( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.webinarCaptionsUrl</code> attribute. 
	 * @param value the webinarCaptionsUrl
	 */
	public void setWebinarCaptionsUrl(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "webinarCaptionsUrl".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.webinarCaptionsUrl</code> attribute. 
	 * @param value the webinarCaptionsUrl
	 */
	public void setWebinarCaptionsUrl(final String value)
	{
		setWebinarCaptionsUrl( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.webinarCategoryTag</code> attribute.
	 * @return the webinarCategoryTag
	 */
	public List<WebinarCategoryTag> getWebinarCategoryTag(final SessionContext ctx)
	{
		final List<WebinarCategoryTag> items = getLinkedItems( 
			ctx,
			true,
			WholescriptscoreConstants.Relations.WEBINAR2WEBINARCATEGORYTAG,
			"WebinarCategoryTag",
			null,
			Utilities.getRelationOrderingOverride(WEBINAR2WEBINARCATEGORYTAG_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(WEBINAR2WEBINARCATEGORYTAG_TGT_ORDERED, true)
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.webinarCategoryTag</code> attribute.
	 * @return the webinarCategoryTag
	 */
	public List<WebinarCategoryTag> getWebinarCategoryTag()
	{
		return getWebinarCategoryTag( getSession().getSessionContext() );
	}
	
	public long getWebinarCategoryTagCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			true,
			WholescriptscoreConstants.Relations.WEBINAR2WEBINARCATEGORYTAG,
			"WebinarCategoryTag",
			null
		);
	}
	
	public long getWebinarCategoryTagCount()
	{
		return getWebinarCategoryTagCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.webinarCategoryTag</code> attribute. 
	 * @param value the webinarCategoryTag
	 */
	public void setWebinarCategoryTag(final SessionContext ctx, final List<WebinarCategoryTag> value)
	{
		setLinkedItems( 
			ctx,
			true,
			WholescriptscoreConstants.Relations.WEBINAR2WEBINARCATEGORYTAG,
			null,
			value,
			Utilities.getRelationOrderingOverride(WEBINAR2WEBINARCATEGORYTAG_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(WEBINAR2WEBINARCATEGORYTAG_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(WEBINAR2WEBINARCATEGORYTAG_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.webinarCategoryTag</code> attribute. 
	 * @param value the webinarCategoryTag
	 */
	public void setWebinarCategoryTag(final List<WebinarCategoryTag> value)
	{
		setWebinarCategoryTag( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to webinarCategoryTag. 
	 * @param value the item to add to webinarCategoryTag
	 */
	public void addToWebinarCategoryTag(final SessionContext ctx, final WebinarCategoryTag value)
	{
		addLinkedItems( 
			ctx,
			true,
			WholescriptscoreConstants.Relations.WEBINAR2WEBINARCATEGORYTAG,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(WEBINAR2WEBINARCATEGORYTAG_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(WEBINAR2WEBINARCATEGORYTAG_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(WEBINAR2WEBINARCATEGORYTAG_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to webinarCategoryTag. 
	 * @param value the item to add to webinarCategoryTag
	 */
	public void addToWebinarCategoryTag(final WebinarCategoryTag value)
	{
		addToWebinarCategoryTag( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from webinarCategoryTag. 
	 * @param value the item to remove from webinarCategoryTag
	 */
	public void removeFromWebinarCategoryTag(final SessionContext ctx, final WebinarCategoryTag value)
	{
		removeLinkedItems( 
			ctx,
			true,
			WholescriptscoreConstants.Relations.WEBINAR2WEBINARCATEGORYTAG,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(WEBINAR2WEBINARCATEGORYTAG_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(WEBINAR2WEBINARCATEGORYTAG_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(WEBINAR2WEBINARCATEGORYTAG_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from webinarCategoryTag. 
	 * @param value the item to remove from webinarCategoryTag
	 */
	public void removeFromWebinarCategoryTag(final WebinarCategoryTag value)
	{
		removeFromWebinarCategoryTag( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.webinarVideoUrl</code> attribute.
	 * @return the webinarVideoUrl
	 */
	public String getWebinarVideoUrl(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "webinarVideoUrl".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Webinar.webinarVideoUrl</code> attribute.
	 * @return the webinarVideoUrl
	 */
	public String getWebinarVideoUrl()
	{
		return getWebinarVideoUrl( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.webinarVideoUrl</code> attribute. 
	 * @param value the webinarVideoUrl
	 */
	public void setWebinarVideoUrl(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "webinarVideoUrl".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Webinar.webinarVideoUrl</code> attribute. 
	 * @param value the webinarVideoUrl
	 */
	public void setWebinarVideoUrl(final String value)
	{
		setWebinarVideoUrl( getSession().getSessionContext(), value );
	}
	
}
