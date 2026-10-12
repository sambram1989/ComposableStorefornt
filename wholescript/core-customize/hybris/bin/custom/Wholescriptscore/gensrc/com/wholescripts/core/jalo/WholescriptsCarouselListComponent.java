/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 27-May-2025, 11:44:08 pm                    ---
 * ----------------------------------------------------------------
 */
package com.wholescripts.core.jalo;

import com.wholescripts.core.constants.WholescriptscoreConstants;
import com.wholescripts.core.jalo.WholescriptsCarouselComponent;
import de.hybris.platform.cms2lib.components.BannerComponent;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
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
 * Generated class for type WholescriptsCarouselListComponent.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class WholescriptsCarouselListComponent extends BannerComponent
{
	/** Qualifier of the <code>WholescriptsCarouselListComponent.carouselList</code> attribute **/
	public static final String CAROUSELLIST = "carouselList";
	/** Relation ordering override parameter constants for WholescriptsCarouselList2WholescriptsCarousel from ((Wholescriptscore))*/
	protected static String WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL_SRC_ORDERED = "relation.WholescriptsCarouselList2WholescriptsCarousel.source.ordered";
	protected static String WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL_TGT_ORDERED = "relation.WholescriptsCarouselList2WholescriptsCarousel.target.ordered";
	/** Relation disable markmodifed parameter constants for WholescriptsCarouselList2WholescriptsCarousel from ((Wholescriptscore))*/
	protected static String WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL_MARKMODIFIED = "relation.WholescriptsCarouselList2WholescriptsCarousel.markmodified";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(BannerComponent.DEFAULT_INITIAL_ATTRIBUTES);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsCarouselListComponent.carouselList</code> attribute.
	 * @return the carouselList
	 */
	public List<WholescriptsCarouselComponent> getCarouselList(final SessionContext ctx)
	{
		final List<WholescriptsCarouselComponent> items = getLinkedItems( 
			ctx,
			true,
			WholescriptscoreConstants.Relations.WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL,
			"WholescriptsCarouselComponent",
			null,
			Utilities.getRelationOrderingOverride(WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL_TGT_ORDERED, true)
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsCarouselListComponent.carouselList</code> attribute.
	 * @return the carouselList
	 */
	public List<WholescriptsCarouselComponent> getCarouselList()
	{
		return getCarouselList( getSession().getSessionContext() );
	}
	
	public long getCarouselListCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			true,
			WholescriptscoreConstants.Relations.WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL,
			"WholescriptsCarouselComponent",
			null
		);
	}
	
	public long getCarouselListCount()
	{
		return getCarouselListCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsCarouselListComponent.carouselList</code> attribute. 
	 * @param value the carouselList
	 */
	public void setCarouselList(final SessionContext ctx, final List<WholescriptsCarouselComponent> value)
	{
		setLinkedItems( 
			ctx,
			true,
			WholescriptscoreConstants.Relations.WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL,
			null,
			value,
			Utilities.getRelationOrderingOverride(WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsCarouselListComponent.carouselList</code> attribute. 
	 * @param value the carouselList
	 */
	public void setCarouselList(final List<WholescriptsCarouselComponent> value)
	{
		setCarouselList( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to carouselList. 
	 * @param value the item to add to carouselList
	 */
	public void addToCarouselList(final SessionContext ctx, final WholescriptsCarouselComponent value)
	{
		addLinkedItems( 
			ctx,
			true,
			WholescriptscoreConstants.Relations.WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to carouselList. 
	 * @param value the item to add to carouselList
	 */
	public void addToCarouselList(final WholescriptsCarouselComponent value)
	{
		addToCarouselList( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from carouselList. 
	 * @param value the item to remove from carouselList
	 */
	public void removeFromCarouselList(final SessionContext ctx, final WholescriptsCarouselComponent value)
	{
		removeLinkedItems( 
			ctx,
			true,
			WholescriptscoreConstants.Relations.WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from carouselList. 
	 * @param value the item to remove from carouselList
	 */
	public void removeFromCarouselList(final WholescriptsCarouselComponent value)
	{
		removeFromCarouselList( getSession().getSessionContext(), value );
	}
	
	/**
	 * @deprecated since 2011, use {@link Utilities#getMarkModifiedOverride(de.hybris.platform.jalo.type.RelationType)
	 */
	@Override
	@Deprecated(since = "2105", forRemoval = true)
	public boolean isMarkModifiedDisabled(final Item referencedItem)
	{
		ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("WholescriptsCarouselComponent");
		if(relationSecondEnd0.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL_MARKMODIFIED);
		}
		return true;
	}
	
}
