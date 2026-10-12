/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 27-May-2025, 11:44:08 pm                    ---
 * ----------------------------------------------------------------
 */
package com.wholescripts.core.jalo;

import com.wholescripts.core.constants.WholescriptscoreConstants;
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
 * Generated class for type WholescriptsFaq.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class WholescriptsFaq extends GenericItem
{
	/** Qualifier of the <code>WholescriptsFaq.uid</code> attribute **/
	public static final String UID = "uid";
	/** Qualifier of the <code>WholescriptsFaq.question</code> attribute **/
	public static final String QUESTION = "question";
	/** Qualifier of the <code>WholescriptsFaq.answer</code> attribute **/
	public static final String ANSWER = "answer";
	/** Qualifier of the <code>WholescriptsFaq.globalSearchEnabled</code> attribute **/
	public static final String GLOBALSEARCHENABLED = "globalSearchEnabled";
	/** Qualifier of the <code>WholescriptsFaq.allGroups</code> attribute **/
	public static final String ALLGROUPS = "allGroups";
	/** Relation ordering override parameter constants for FaqToPrincipalGroupRelation from ((Wholescriptscore))*/
	protected static String FAQTOPRINCIPALGROUPRELATION_SRC_ORDERED = "relation.FaqToPrincipalGroupRelation.source.ordered";
	protected static String FAQTOPRINCIPALGROUPRELATION_TGT_ORDERED = "relation.FaqToPrincipalGroupRelation.target.ordered";
	/** Relation disable markmodifed parameter constants for FaqToPrincipalGroupRelation from ((Wholescriptscore))*/
	protected static String FAQTOPRINCIPALGROUPRELATION_MARKMODIFIED = "relation.FaqToPrincipalGroupRelation.markmodified";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(UID, AttributeMode.INITIAL);
		tmp.put(QUESTION, AttributeMode.INITIAL);
		tmp.put(ANSWER, AttributeMode.INITIAL);
		tmp.put(GLOBALSEARCHENABLED, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsFaq.allGroups</code> attribute.
	 * @return the allGroups
	 */
	public Set<PrincipalGroup> getAllGroups(final SessionContext ctx)
	{
		final List<PrincipalGroup> items = getLinkedItems( 
			ctx,
			true,
			WholescriptscoreConstants.Relations.FAQTOPRINCIPALGROUPRELATION,
			"PrincipalGroup",
			null,
			Utilities.getRelationOrderingOverride(FAQTOPRINCIPALGROUPRELATION_SRC_ORDERED, true),
			false
		);
		return new LinkedHashSet<PrincipalGroup>(items);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsFaq.allGroups</code> attribute.
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
			WholescriptscoreConstants.Relations.FAQTOPRINCIPALGROUPRELATION,
			"PrincipalGroup",
			null
		);
	}
	
	public long getAllGroupsCount()
	{
		return getAllGroupsCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsFaq.allGroups</code> attribute. 
	 * @param value the allGroups
	 */
	public void setAllGroups(final SessionContext ctx, final Set<PrincipalGroup> value)
	{
		setLinkedItems( 
			ctx,
			true,
			WholescriptscoreConstants.Relations.FAQTOPRINCIPALGROUPRELATION,
			null,
			value,
			Utilities.getRelationOrderingOverride(FAQTOPRINCIPALGROUPRELATION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(FAQTOPRINCIPALGROUPRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsFaq.allGroups</code> attribute. 
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
			WholescriptscoreConstants.Relations.FAQTOPRINCIPALGROUPRELATION,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(FAQTOPRINCIPALGROUPRELATION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(FAQTOPRINCIPALGROUPRELATION_MARKMODIFIED)
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
			WholescriptscoreConstants.Relations.FAQTOPRINCIPALGROUPRELATION,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(FAQTOPRINCIPALGROUPRELATION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(FAQTOPRINCIPALGROUPRELATION_MARKMODIFIED)
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
	 * <i>Generated method</i> - Getter of the <code>WholescriptsFaq.answer</code> attribute.
	 * @return the answer - code
	 */
	public String getAnswer(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("WholescriptsFaq.getAnswer requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, "answer".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsFaq.answer</code> attribute.
	 * @return the answer - code
	 */
	public String getAnswer()
	{
		return getAnswer( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsFaq.answer</code> attribute. 
	 * @return the localized answer - code
	 */
	public Map<Language,String> getAllAnswer(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,"answer".intern(),C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsFaq.answer</code> attribute. 
	 * @return the localized answer - code
	 */
	public Map<Language,String> getAllAnswer()
	{
		return getAllAnswer( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsFaq.answer</code> attribute. 
	 * @param value the answer - code
	 */
	public void setAnswer(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("WholescriptsFaq.setAnswer requires a session language", 0 );
		}
		setLocalizedProperty(ctx, "answer".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsFaq.answer</code> attribute. 
	 * @param value the answer - code
	 */
	public void setAnswer(final String value)
	{
		setAnswer( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsFaq.answer</code> attribute. 
	 * @param value the answer - code
	 */
	public void setAllAnswer(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,"answer".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsFaq.answer</code> attribute. 
	 * @param value the answer - code
	 */
	public void setAllAnswer(final Map<Language,String> value)
	{
		setAllAnswer( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsFaq.globalSearchEnabled</code> attribute.
	 * @return the globalSearchEnabled - Global Search
	 */
	public Boolean isGlobalSearchEnabled(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, "globalSearchEnabled".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsFaq.globalSearchEnabled</code> attribute.
	 * @return the globalSearchEnabled - Global Search
	 */
	public Boolean isGlobalSearchEnabled()
	{
		return isGlobalSearchEnabled( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsFaq.globalSearchEnabled</code> attribute. 
	 * @return the globalSearchEnabled - Global Search
	 */
	public boolean isGlobalSearchEnabledAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isGlobalSearchEnabled( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsFaq.globalSearchEnabled</code> attribute. 
	 * @return the globalSearchEnabled - Global Search
	 */
	public boolean isGlobalSearchEnabledAsPrimitive()
	{
		return isGlobalSearchEnabledAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsFaq.globalSearchEnabled</code> attribute. 
	 * @param value the globalSearchEnabled - Global Search
	 */
	public void setGlobalSearchEnabled(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, "globalSearchEnabled".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsFaq.globalSearchEnabled</code> attribute. 
	 * @param value the globalSearchEnabled - Global Search
	 */
	public void setGlobalSearchEnabled(final Boolean value)
	{
		setGlobalSearchEnabled( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsFaq.globalSearchEnabled</code> attribute. 
	 * @param value the globalSearchEnabled - Global Search
	 */
	public void setGlobalSearchEnabled(final SessionContext ctx, final boolean value)
	{
		setGlobalSearchEnabled( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsFaq.globalSearchEnabled</code> attribute. 
	 * @param value the globalSearchEnabled - Global Search
	 */
	public void setGlobalSearchEnabled(final boolean value)
	{
		setGlobalSearchEnabled( getSession().getSessionContext(), value );
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
			return Utilities.getMarkModifiedOverride(FAQTOPRINCIPALGROUPRELATION_MARKMODIFIED);
		}
		return true;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsFaq.question</code> attribute.
	 * @return the question - code
	 */
	public String getQuestion(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("WholescriptsFaq.getQuestion requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, "question".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsFaq.question</code> attribute.
	 * @return the question - code
	 */
	public String getQuestion()
	{
		return getQuestion( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsFaq.question</code> attribute. 
	 * @return the localized question - code
	 */
	public Map<Language,String> getAllQuestion(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,"question".intern(),C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsFaq.question</code> attribute. 
	 * @return the localized question - code
	 */
	public Map<Language,String> getAllQuestion()
	{
		return getAllQuestion( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsFaq.question</code> attribute. 
	 * @param value the question - code
	 */
	public void setQuestion(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("WholescriptsFaq.setQuestion requires a session language", 0 );
		}
		setLocalizedProperty(ctx, "question".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsFaq.question</code> attribute. 
	 * @param value the question - code
	 */
	public void setQuestion(final String value)
	{
		setQuestion( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsFaq.question</code> attribute. 
	 * @param value the question - code
	 */
	public void setAllQuestion(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,"question".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsFaq.question</code> attribute. 
	 * @param value the question - code
	 */
	public void setAllQuestion(final Map<Language,String> value)
	{
		setAllQuestion( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsFaq.uid</code> attribute.
	 * @return the uid - uid
	 */
	public String getUid(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "uid".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsFaq.uid</code> attribute.
	 * @return the uid - uid
	 */
	public String getUid()
	{
		return getUid( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsFaq.uid</code> attribute. 
	 * @param value the uid - uid
	 */
	public void setUid(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "uid".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsFaq.uid</code> attribute. 
	 * @param value the uid - uid
	 */
	public void setUid(final String value)
	{
		setUid( getSession().getSessionContext(), value );
	}
	
}
