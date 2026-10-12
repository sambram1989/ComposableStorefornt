/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 27-May-2025, 11:44:08 pm                    ---
 * ----------------------------------------------------------------
 */
package com.wholescripts.core.jalo;

import com.wholescripts.core.constants.WholescriptscoreConstants;
import com.wholescripts.core.jalo.WholescriptsFaq;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.security.PrincipalGroup;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Generated class for type WholescriptsFaqList.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class WholescriptsFaqList extends GenericItem
{
	/** Qualifier of the <code>WholescriptsFaqList.uid</code> attribute **/
	public static final String UID = "uid";
	/** Qualifier of the <code>WholescriptsFaqList.title</code> attribute **/
	public static final String TITLE = "title";
	/** Qualifier of the <code>WholescriptsFaqList.faqTopicList</code> attribute **/
	public static final String FAQTOPICLIST = "faqTopicList";
	/** Qualifier of the <code>WholescriptsFaqList.child</code> attribute **/
	public static final String CHILD = "child";
	/** Qualifier of the <code>WholescriptsFaqList.isChild</code> attribute **/
	public static final String ISCHILD = "isChild";
	/** Qualifier of the <code>WholescriptsFaqList.allGroups</code> attribute **/
	public static final String ALLGROUPS = "allGroups";
	/** Relation ordering override parameter constants for FaqListToPrincipalGroupRelation from ((Wholescriptscore))*/
	protected static String FAQLISTTOPRINCIPALGROUPRELATION_SRC_ORDERED = "relation.FaqListToPrincipalGroupRelation.source.ordered";
	protected static String FAQLISTTOPRINCIPALGROUPRELATION_TGT_ORDERED = "relation.FaqListToPrincipalGroupRelation.target.ordered";
	/** Relation disable markmodifed parameter constants for FaqListToPrincipalGroupRelation from ((Wholescriptscore))*/
	protected static String FAQLISTTOPRINCIPALGROUPRELATION_MARKMODIFIED = "relation.FaqListToPrincipalGroupRelation.markmodified";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(UID, AttributeMode.INITIAL);
		tmp.put(TITLE, AttributeMode.INITIAL);
		tmp.put(FAQTOPICLIST, AttributeMode.INITIAL);
		tmp.put(CHILD, AttributeMode.INITIAL);
		tmp.put(ISCHILD, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsFaqList.allGroups</code> attribute.
	 * @return the allGroups
	 */
	public Set<PrincipalGroup> getAllGroups(final SessionContext ctx)
	{
		final List<PrincipalGroup> items = getLinkedItems( 
			ctx,
			true,
			WholescriptscoreConstants.Relations.FAQLISTTOPRINCIPALGROUPRELATION,
			"PrincipalGroup",
			null,
			Utilities.getRelationOrderingOverride(FAQLISTTOPRINCIPALGROUPRELATION_SRC_ORDERED, true),
			false
		);
		return new LinkedHashSet<PrincipalGroup>(items);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsFaqList.allGroups</code> attribute.
	 * @return the allGroups
	 */
	public Set<PrincipalGroup> getAllGroups()
	{
		return getAllGroups( getSession().getSessionContext() );
	}
	
	public long getAllGroupsCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			true,
			WholescriptscoreConstants.Relations.FAQLISTTOPRINCIPALGROUPRELATION,
			"PrincipalGroup",
			null
		);
	}
	
	public long getAllGroupsCount()
	{
		return getAllGroupsCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsFaqList.allGroups</code> attribute. 
	 * @param value the allGroups
	 */
	public void setAllGroups(final SessionContext ctx, final Set<PrincipalGroup> value)
	{
		setLinkedItems( 
			ctx,
			true,
			WholescriptscoreConstants.Relations.FAQLISTTOPRINCIPALGROUPRELATION,
			null,
			value,
			Utilities.getRelationOrderingOverride(FAQLISTTOPRINCIPALGROUPRELATION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(FAQLISTTOPRINCIPALGROUPRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsFaqList.allGroups</code> attribute. 
	 * @param value the allGroups
	 */
	public void setAllGroups(final Set<PrincipalGroup> value)
	{
		setAllGroups( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to allGroups. 
	 * @param value the item to add to allGroups
	 */
	public void addToAllGroups(final SessionContext ctx, final PrincipalGroup value)
	{
		addLinkedItems( 
			ctx,
			true,
			WholescriptscoreConstants.Relations.FAQLISTTOPRINCIPALGROUPRELATION,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(FAQLISTTOPRINCIPALGROUPRELATION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(FAQLISTTOPRINCIPALGROUPRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to allGroups. 
	 * @param value the item to add to allGroups
	 */
	public void addToAllGroups(final PrincipalGroup value)
	{
		addToAllGroups( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from allGroups. 
	 * @param value the item to remove from allGroups
	 */
	public void removeFromAllGroups(final SessionContext ctx, final PrincipalGroup value)
	{
		removeLinkedItems( 
			ctx,
			true,
			WholescriptscoreConstants.Relations.FAQLISTTOPRINCIPALGROUPRELATION,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(FAQLISTTOPRINCIPALGROUPRELATION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(FAQLISTTOPRINCIPALGROUPRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from allGroups. 
	 * @param value the item to remove from allGroups
	 */
	public void removeFromAllGroups(final PrincipalGroup value)
	{
		removeFromAllGroups( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsFaqList.child</code> attribute.
	 * @return the child
	 */
	public List<WholescriptsFaqList> getChild(final SessionContext ctx)
	{
		List<WholescriptsFaqList> coll = (List<WholescriptsFaqList>)getProperty( ctx, "child".intern());
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsFaqList.child</code> attribute.
	 * @return the child
	 */
	public List<WholescriptsFaqList> getChild()
	{
		return getChild( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsFaqList.child</code> attribute. 
	 * @param value the child
	 */
	public void setChild(final SessionContext ctx, final List<WholescriptsFaqList> value)
	{
		setProperty(ctx, "child".intern(),value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsFaqList.child</code> attribute. 
	 * @param value the child
	 */
	public void setChild(final List<WholescriptsFaqList> value)
	{
		setChild( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsFaqList.faqTopicList</code> attribute.
	 * @return the faqTopicList
	 */
	public List<WholescriptsFaq> getFaqTopicList(final SessionContext ctx)
	{
		List<WholescriptsFaq> coll = (List<WholescriptsFaq>)getProperty( ctx, "faqTopicList".intern());
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsFaqList.faqTopicList</code> attribute.
	 * @return the faqTopicList
	 */
	public List<WholescriptsFaq> getFaqTopicList()
	{
		return getFaqTopicList( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsFaqList.faqTopicList</code> attribute. 
	 * @param value the faqTopicList
	 */
	public void setFaqTopicList(final SessionContext ctx, final List<WholescriptsFaq> value)
	{
		setProperty(ctx, "faqTopicList".intern(),value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsFaqList.faqTopicList</code> attribute. 
	 * @param value the faqTopicList
	 */
	public void setFaqTopicList(final List<WholescriptsFaq> value)
	{
		setFaqTopicList( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsFaqList.isChild</code> attribute.
	 * @return the isChild - is it child
	 */
	public Boolean isIsChild(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, "isChild".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsFaqList.isChild</code> attribute.
	 * @return the isChild - is it child
	 */
	public Boolean isIsChild()
	{
		return isIsChild( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsFaqList.isChild</code> attribute. 
	 * @return the isChild - is it child
	 */
	public boolean isIsChildAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isIsChild( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsFaqList.isChild</code> attribute. 
	 * @return the isChild - is it child
	 */
	public boolean isIsChildAsPrimitive()
	{
		return isIsChildAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsFaqList.isChild</code> attribute. 
	 * @param value the isChild - is it child
	 */
	public void setIsChild(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, "isChild".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsFaqList.isChild</code> attribute. 
	 * @param value the isChild - is it child
	 */
	public void setIsChild(final Boolean value)
	{
		setIsChild( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsFaqList.isChild</code> attribute. 
	 * @param value the isChild - is it child
	 */
	public void setIsChild(final SessionContext ctx, final boolean value)
	{
		setIsChild( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsFaqList.isChild</code> attribute. 
	 * @param value the isChild - is it child
	 */
	public void setIsChild(final boolean value)
	{
		setIsChild( getSession().getSessionContext(), value );
	}
	
	/**
	 * @deprecated since 2011, use {@link Utilities#getMarkModifiedOverride(de.hybris.platform.jalo.type.RelationType)
	 */
	@Override
	@Deprecated(since = "2105", forRemoval = true)
	public boolean isMarkModifiedDisabled(final Item referencedItem)
	{
		ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("PrincipalGroup");
		if(relationSecondEnd0.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(FAQLISTTOPRINCIPALGROUPRELATION_MARKMODIFIED);
		}
		return true;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsFaqList.title</code> attribute.
	 * @return the title - title
	 */
	public String getTitle(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("WholescriptsFaqList.getTitle requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, "title".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsFaqList.title</code> attribute.
	 * @return the title - title
	 */
	public String getTitle()
	{
		return getTitle( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsFaqList.title</code> attribute. 
	 * @return the localized title - title
	 */
	public Map<Language,String> getAllTitle(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,"title".intern(),C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsFaqList.title</code> attribute. 
	 * @return the localized title - title
	 */
	public Map<Language,String> getAllTitle()
	{
		return getAllTitle( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsFaqList.title</code> attribute. 
	 * @param value the title - title
	 */
	public void setTitle(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("WholescriptsFaqList.setTitle requires a session language", 0 );
		}
		setLocalizedProperty(ctx, "title".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsFaqList.title</code> attribute. 
	 * @param value the title - title
	 */
	public void setTitle(final String value)
	{
		setTitle( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsFaqList.title</code> attribute. 
	 * @param value the title - title
	 */
	public void setAllTitle(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,"title".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsFaqList.title</code> attribute. 
	 * @param value the title - title
	 */
	public void setAllTitle(final Map<Language,String> value)
	{
		setAllTitle( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsFaqList.uid</code> attribute.
	 * @return the uid - uid
	 */
	public String getUid(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "uid".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsFaqList.uid</code> attribute.
	 * @return the uid - uid
	 */
	public String getUid()
	{
		return getUid( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsFaqList.uid</code> attribute. 
	 * @param value the uid - uid
	 */
	public void setUid(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "uid".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsFaqList.uid</code> attribute. 
	 * @param value the uid - uid
	 */
	public void setUid(final String value)
	{
		setUid( getSession().getSessionContext(), value );
	}
	
}
