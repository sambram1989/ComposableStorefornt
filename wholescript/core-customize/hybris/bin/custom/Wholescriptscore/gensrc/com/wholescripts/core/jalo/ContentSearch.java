/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 27-May-2025, 11:44:08 pm                    ---
 * ----------------------------------------------------------------
 */
package com.wholescripts.core.jalo;

import com.wholescripts.core.jalo.WholescriptsBrand;
import com.wholescripts.core.jalo.WholescriptsFaq;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.cms2.jalo.relations.ContentSlotForPage;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type ContentSearch.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class ContentSearch extends GenericItem
{
	/** Qualifier of the <code>ContentSearch.integration</code> attribute **/
	public static final String INTEGRATION = "integration";
	/** Qualifier of the <code>ContentSearch.brand</code> attribute **/
	public static final String BRAND = "brand";
	/** Qualifier of the <code>ContentSearch.category</code> attribute **/
	public static final String CATEGORY = "category";
	/** Qualifier of the <code>ContentSearch.faq</code> attribute **/
	public static final String FAQ = "faq";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(INTEGRATION, AttributeMode.INITIAL);
		tmp.put(BRAND, AttributeMode.INITIAL);
		tmp.put(CATEGORY, AttributeMode.INITIAL);
		tmp.put(FAQ, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContentSearch.brand</code> attribute.
	 * @return the brand
	 */
	public WholescriptsBrand getBrand(final SessionContext ctx)
	{
		return (WholescriptsBrand)getProperty( ctx, "brand".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContentSearch.brand</code> attribute.
	 * @return the brand
	 */
	public WholescriptsBrand getBrand()
	{
		return getBrand( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContentSearch.brand</code> attribute. 
	 * @param value the brand
	 */
	public void setBrand(final SessionContext ctx, final WholescriptsBrand value)
	{
		setProperty(ctx, "brand".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContentSearch.brand</code> attribute. 
	 * @param value the brand
	 */
	public void setBrand(final WholescriptsBrand value)
	{
		setBrand( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContentSearch.category</code> attribute.
	 * @return the category
	 */
	public Category getCategory(final SessionContext ctx)
	{
		return (Category)getProperty( ctx, "category".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContentSearch.category</code> attribute.
	 * @return the category
	 */
	public Category getCategory()
	{
		return getCategory( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContentSearch.category</code> attribute. 
	 * @param value the category
	 */
	public void setCategory(final SessionContext ctx, final Category value)
	{
		setProperty(ctx, "category".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContentSearch.category</code> attribute. 
	 * @param value the category
	 */
	public void setCategory(final Category value)
	{
		setCategory( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContentSearch.faq</code> attribute.
	 * @return the faq
	 */
	public WholescriptsFaq getFaq(final SessionContext ctx)
	{
		return (WholescriptsFaq)getProperty( ctx, "faq".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContentSearch.faq</code> attribute.
	 * @return the faq
	 */
	public WholescriptsFaq getFaq()
	{
		return getFaq( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContentSearch.faq</code> attribute. 
	 * @param value the faq
	 */
	public void setFaq(final SessionContext ctx, final WholescriptsFaq value)
	{
		setProperty(ctx, "faq".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContentSearch.faq</code> attribute. 
	 * @param value the faq
	 */
	public void setFaq(final WholescriptsFaq value)
	{
		setFaq( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContentSearch.integration</code> attribute.
	 * @return the integration
	 */
	public ContentSlotForPage getIntegration(final SessionContext ctx)
	{
		return (ContentSlotForPage)getProperty( ctx, "integration".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContentSearch.integration</code> attribute.
	 * @return the integration
	 */
	public ContentSlotForPage getIntegration()
	{
		return getIntegration( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContentSearch.integration</code> attribute. 
	 * @param value the integration
	 */
	public void setIntegration(final SessionContext ctx, final ContentSlotForPage value)
	{
		setProperty(ctx, "integration".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContentSearch.integration</code> attribute. 
	 * @param value the integration
	 */
	public void setIntegration(final ContentSlotForPage value)
	{
		setIntegration( getSession().getSessionContext(), value );
	}
	
}
