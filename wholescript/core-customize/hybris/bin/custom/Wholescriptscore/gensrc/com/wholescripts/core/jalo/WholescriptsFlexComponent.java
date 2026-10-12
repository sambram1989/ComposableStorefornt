/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 27-May-2025, 11:44:08 pm                    ---
 * ----------------------------------------------------------------
 */
package com.wholescripts.core.jalo;

import com.wholescripts.core.jalo.WholescriptsBannerComponent;
import de.hybris.platform.cms2.jalo.contents.components.CMSFlexComponent;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type WholescriptsFlexComponent.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class WholescriptsFlexComponent extends CMSFlexComponent
{
	/** Qualifier of the <code>WholescriptsFlexComponent.banners</code> attribute **/
	public static final String BANNERS = "banners";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(CMSFlexComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(BANNERS, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsFlexComponent.banners</code> attribute.
	 * @return the banners - banners .
	 */
	public List<WholescriptsBannerComponent> getBanners(final SessionContext ctx)
	{
		List<WholescriptsBannerComponent> coll = (List<WholescriptsBannerComponent>)getProperty( ctx, "banners".intern());
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsFlexComponent.banners</code> attribute.
	 * @return the banners - banners .
	 */
	public List<WholescriptsBannerComponent> getBanners()
	{
		return getBanners( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsFlexComponent.banners</code> attribute. 
	 * @param value the banners - banners .
	 */
	public void setBanners(final SessionContext ctx, final List<WholescriptsBannerComponent> value)
	{
		setProperty(ctx, "banners".intern(),value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsFlexComponent.banners</code> attribute. 
	 * @param value the banners - banners .
	 */
	public void setBanners(final List<WholescriptsBannerComponent> value)
	{
		setBanners( getSession().getSessionContext(), value );
	}
	
}
