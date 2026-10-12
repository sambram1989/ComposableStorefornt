/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 27-May-2025, 11:44:08 pm                    ---
 * ----------------------------------------------------------------
 */
package com.wholescripts.core.jalo;

import de.hybris.platform.cms2.jalo.contents.CMSItem;
import de.hybris.platform.cms2lib.components.BannerComponent;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type WholescriptsBannerComponent.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class WholescriptsBannerComponent extends BannerComponent
{
	/** Qualifier of the <code>WholescriptsBannerComponent.item</code> attribute **/
	public static final String ITEM = "item";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(BannerComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(ITEM, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBannerComponent.item</code> attribute.
	 * @return the item
	 */
	public CMSItem getItem(final SessionContext ctx)
	{
		return (CMSItem)getProperty( ctx, "item".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBannerComponent.item</code> attribute.
	 * @return the item
	 */
	public CMSItem getItem()
	{
		return getItem( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBannerComponent.item</code> attribute. 
	 * @param value the item
	 */
	public void setItem(final SessionContext ctx, final CMSItem value)
	{
		setProperty(ctx, "item".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBannerComponent.item</code> attribute. 
	 * @param value the item
	 */
	public void setItem(final CMSItem value)
	{
		setItem( getSession().getSessionContext(), value );
	}
	
}
