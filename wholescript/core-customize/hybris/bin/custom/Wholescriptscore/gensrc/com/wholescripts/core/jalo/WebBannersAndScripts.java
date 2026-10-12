/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 27-May-2025, 11:44:08 pm                    ---
 * ----------------------------------------------------------------
 */
package com.wholescripts.core.jalo;

import com.wholescripts.core.jalo.WebBannersAndScriptsEntry;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type WebBannersAndScripts.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class WebBannersAndScripts extends GenericItem
{
	/** Qualifier of the <code>WebBannersAndScripts.uid</code> attribute **/
	public static final String UID = "uid";
	/** Qualifier of the <code>WebBannersAndScripts.entries</code> attribute **/
	public static final String ENTRIES = "entries";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(UID, AttributeMode.INITIAL);
		tmp.put(ENTRIES, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebBannersAndScripts.entries</code> attribute.
	 * @return the entries
	 */
	public List<WebBannersAndScriptsEntry> getEntries(final SessionContext ctx)
	{
		List<WebBannersAndScriptsEntry> coll = (List<WebBannersAndScriptsEntry>)getProperty( ctx, "entries".intern());
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebBannersAndScripts.entries</code> attribute.
	 * @return the entries
	 */
	public List<WebBannersAndScriptsEntry> getEntries()
	{
		return getEntries( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebBannersAndScripts.entries</code> attribute. 
	 * @param value the entries
	 */
	public void setEntries(final SessionContext ctx, final List<WebBannersAndScriptsEntry> value)
	{
		setProperty(ctx, "entries".intern(),value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebBannersAndScripts.entries</code> attribute. 
	 * @param value the entries
	 */
	public void setEntries(final List<WebBannersAndScriptsEntry> value)
	{
		setEntries( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebBannersAndScripts.uid</code> attribute.
	 * @return the uid
	 */
	public String getUid(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "uid".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WebBannersAndScripts.uid</code> attribute.
	 * @return the uid
	 */
	public String getUid()
	{
		return getUid( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebBannersAndScripts.uid</code> attribute. 
	 * @param value the uid
	 */
	public void setUid(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "uid".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WebBannersAndScripts.uid</code> attribute. 
	 * @param value the uid
	 */
	public void setUid(final String value)
	{
		setUid( getSession().getSessionContext(), value );
	}
	
}
