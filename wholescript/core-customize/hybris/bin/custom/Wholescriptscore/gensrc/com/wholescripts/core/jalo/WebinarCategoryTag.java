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
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type WebinarCategoryTag.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class WebinarCategoryTag extends GenericItem
{
	/** Qualifier of the <code>WebinarCategoryTag.code</code> attribute **/
	public static final String CODE = "code";
	/** Qualifier of the <code>WebinarCategoryTag.name</code> attribute **/
	public static final String NAME = "name";
	/** Qualifier of the <code>WebinarCategoryTag.webinar</code> attribute **/
	public static final String WEBINAR = "webinar";
	/** Relation ordering override parameter constants for Webinar2WebinarCategoryTag from ((Wholescriptscore))*/
	protected static String WEBINAR2WEBINARCATEGORYTAG_SRC_ORDERED = "relation.Webinar2WebinarCategoryTag.source.ordered";
	protected static String WEBINAR2WEBINARCATEGORYTAG_TGT_ORDERED = "relation.Webinar2WebinarCategoryTag.target.ordered";
	/** Relation disable markmodifed parameter constants for Webinar2WebinarCategoryTag from ((Wholescriptscore))*/
	protected static String WEBINAR2WEBINARCATEGORYTAG_MARKMODIFIED = "relation.Webinar2WebinarCategoryTag.markmodified";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CODE, AttributeMode.INITIAL);
		tmp.put(NAME, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebinarCategoryTag.code</code> attribute.
	 * @return the code
	 */
	public String getCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "code".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebinarCategoryTag.code</code> attribute.
	 * @return the code
	 */
	public String getCode()
	{
		return getCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebinarCategoryTag.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "code".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebinarCategoryTag.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final String value)
	{
		setCode( getSession().getSessionContext(), value );
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
			return Utilities.getMarkModifiedOverride(WEBINAR2WEBINARCATEGORYTAG_MARKMODIFIED);
		}
		return true;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebinarCategoryTag.name</code> attribute.
	 * @return the name
	 */
	public String getName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "name".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebinarCategoryTag.name</code> attribute.
	 * @return the name
	 */
	public String getName()
	{
		return getName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebinarCategoryTag.name</code> attribute. 
	 * @param value the name
	 */
	public void setName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "name".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebinarCategoryTag.name</code> attribute. 
	 * @param value the name
	 */
	public void setName(final String value)
	{
		setName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebinarCategoryTag.webinar</code> attribute.
	 * @return the webinar
	 */
	public List<Webinar> getWebinar(final SessionContext ctx)
	{
		final List<Webinar> items = getLinkedItems( 
			ctx,
			false,
			WholescriptscoreConstants.Relations.WEBINAR2WEBINARCATEGORYTAG,
			"Webinar",
			null,
			Utilities.getRelationOrderingOverride(WEBINAR2WEBINARCATEGORYTAG_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(WEBINAR2WEBINARCATEGORYTAG_TGT_ORDERED, true)
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebinarCategoryTag.webinar</code> attribute.
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
			WholescriptscoreConstants.Relations.WEBINAR2WEBINARCATEGORYTAG,
			"Webinar",
			null
		);
	}
	
	public long getWebinarCount()
	{
		return getWebinarCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebinarCategoryTag.webinar</code> attribute. 
	 * @param value the webinar
	 */
	public void setWebinar(final SessionContext ctx, final List<Webinar> value)
	{
		setLinkedItems( 
			ctx,
			false,
			WholescriptscoreConstants.Relations.WEBINAR2WEBINARCATEGORYTAG,
			null,
			value,
			Utilities.getRelationOrderingOverride(WEBINAR2WEBINARCATEGORYTAG_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(WEBINAR2WEBINARCATEGORYTAG_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(WEBINAR2WEBINARCATEGORYTAG_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebinarCategoryTag.webinar</code> attribute. 
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
			WholescriptscoreConstants.Relations.WEBINAR2WEBINARCATEGORYTAG,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(WEBINAR2WEBINARCATEGORYTAG_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(WEBINAR2WEBINARCATEGORYTAG_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(WEBINAR2WEBINARCATEGORYTAG_MARKMODIFIED)
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
			WholescriptscoreConstants.Relations.WEBINAR2WEBINARCATEGORYTAG,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(WEBINAR2WEBINARCATEGORYTAG_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(WEBINAR2WEBINARCATEGORYTAG_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(WEBINAR2WEBINARCATEGORYTAG_MARKMODIFIED)
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
	
}
