/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 27-May-2025, 11:44:08 pm                    ---
 * ----------------------------------------------------------------
 */
package com.wholescripts.core.jalo;

import com.wholescripts.core.constants.WholescriptscoreConstants;
import com.wholescripts.core.jalo.WholescriptsBannerComponent;
import com.wholescripts.core.jalo.WholescriptsCarouselListComponent;
import de.hybris.platform.cms2lib.components.BannerComponent;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type WholescriptsCarouselComponent.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class WholescriptsCarouselComponent extends BannerComponent
{
	/** Qualifier of the <code>WholescriptsCarouselComponent.backgroundImage</code> attribute **/
	public static final String BACKGROUNDIMAGE = "backgroundImage";
	/** Qualifier of the <code>WholescriptsCarouselComponent.banners</code> attribute **/
	public static final String BANNERS = "banners";
	/** Qualifier of the <code>WholescriptsCarouselComponent.carousel</code> attribute **/
	public static final String CAROUSEL = "carousel";
	/** Relation ordering override parameter constants for WholescriptsCarouselList2WholescriptsCarousel from ((Wholescriptscore))*/
	protected static String WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL_SRC_ORDERED = "relation.WholescriptsCarouselList2WholescriptsCarousel.source.ordered";
	protected static String WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL_TGT_ORDERED = "relation.WholescriptsCarouselList2WholescriptsCarousel.target.ordered";
	/** Relation disable markmodifed parameter constants for WholescriptsCarouselList2WholescriptsCarousel from ((Wholescriptscore))*/
	protected static String WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL_MARKMODIFIED = "relation.WholescriptsCarouselList2WholescriptsCarousel.markmodified";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(BannerComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(BACKGROUNDIMAGE, AttributeMode.INITIAL);
		tmp.put(BANNERS, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsCarouselComponent.backgroundImage</code> attribute.
	 * @return the backgroundImage
	 */
	public List<Media> getBackgroundImage(final SessionContext ctx)
	{
		List<Media> coll = (List<Media>)getProperty( ctx, "backgroundImage".intern());
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsCarouselComponent.backgroundImage</code> attribute.
	 * @return the backgroundImage
	 */
	public List<Media> getBackgroundImage()
	{
		return getBackgroundImage( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsCarouselComponent.backgroundImage</code> attribute. 
	 * @param value the backgroundImage
	 */
	public void setBackgroundImage(final SessionContext ctx, final List<Media> value)
	{
		setProperty(ctx, "backgroundImage".intern(),value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsCarouselComponent.backgroundImage</code> attribute. 
	 * @param value the backgroundImage
	 */
	public void setBackgroundImage(final List<Media> value)
	{
		setBackgroundImage( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsCarouselComponent.banners</code> attribute.
	 * @return the banners - banners .
	 */
	public List<WholescriptsBannerComponent> getBanners(final SessionContext ctx)
	{
		List<WholescriptsBannerComponent> coll = (List<WholescriptsBannerComponent>)getProperty( ctx, "banners".intern());
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsCarouselComponent.banners</code> attribute.
	 * @return the banners - banners .
	 */
	public List<WholescriptsBannerComponent> getBanners()
	{
		return getBanners( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsCarouselComponent.banners</code> attribute. 
	 * @param value the banners - banners .
	 */
	public void setBanners(final SessionContext ctx, final List<WholescriptsBannerComponent> value)
	{
		setProperty(ctx, "banners".intern(),value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsCarouselComponent.banners</code> attribute. 
	 * @param value the banners - banners .
	 */
	public void setBanners(final List<WholescriptsBannerComponent> value)
	{
		setBanners( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsCarouselComponent.carousel</code> attribute.
	 * @return the carousel
	 */
	public List<WholescriptsCarouselListComponent> getCarousel(final SessionContext ctx)
	{
		final List<WholescriptsCarouselListComponent> items = getLinkedItems( 
			ctx,
			false,
			WholescriptscoreConstants.Relations.WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL,
			"WholescriptsCarouselListComponent",
			null,
			Utilities.getRelationOrderingOverride(WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL_TGT_ORDERED, true)
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsCarouselComponent.carousel</code> attribute.
	 * @return the carousel
	 */
	public List<WholescriptsCarouselListComponent> getCarousel()
	{
		return getCarousel( getSession().getSessionContext() );
	}
	
	public long getCarouselCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			false,
			WholescriptscoreConstants.Relations.WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL,
			"WholescriptsCarouselListComponent",
			null
		);
	}
	
	public long getCarouselCount()
	{
		return getCarouselCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsCarouselComponent.carousel</code> attribute. 
	 * @param value the carousel
	 */
	public void setCarousel(final SessionContext ctx, final List<WholescriptsCarouselListComponent> value)
	{
		setLinkedItems( 
			ctx,
			false,
			WholescriptscoreConstants.Relations.WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL,
			null,
			value,
			Utilities.getRelationOrderingOverride(WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsCarouselComponent.carousel</code> attribute. 
	 * @param value the carousel
	 */
	public void setCarousel(final List<WholescriptsCarouselListComponent> value)
	{
		setCarousel( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to carousel. 
	 * @param value the item to add to carousel
	 */
	public void addToCarousel(final SessionContext ctx, final WholescriptsCarouselListComponent value)
	{
		addLinkedItems( 
			ctx,
			false,
			WholescriptscoreConstants.Relations.WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to carousel. 
	 * @param value the item to add to carousel
	 */
	public void addToCarousel(final WholescriptsCarouselListComponent value)
	{
		addToCarousel( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from carousel. 
	 * @param value the item to remove from carousel
	 */
	public void removeFromCarousel(final SessionContext ctx, final WholescriptsCarouselListComponent value)
	{
		removeLinkedItems( 
			ctx,
			false,
			WholescriptscoreConstants.Relations.WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from carousel. 
	 * @param value the item to remove from carousel
	 */
	public void removeFromCarousel(final WholescriptsCarouselListComponent value)
	{
		removeFromCarousel( getSession().getSessionContext(), value );
	}
	
	/**
	 * @deprecated since 2011, use {@link Utilities#getMarkModifiedOverride(de.hybris.platform.jalo.type.RelationType)
	 */
	@Override
	@Deprecated(since = "2105", forRemoval = true)
	public boolean isMarkModifiedDisabled(final Item referencedItem)
	{
		ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("WholescriptsCarouselListComponent");
		if(relationSecondEnd0.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(WHOLESCRIPTSCAROUSELLIST2WHOLESCRIPTSCAROUSEL_MARKMODIFIED);
		}
		return true;
	}
	
}
