/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 27-May-2025, 11:44:08 pm                    ---
 * ----------------------------------------------------------------
 */
package com.wholescripts.core.jalo;

import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.media.Media;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type WholescriptsBrand.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class WholescriptsBrand extends GenericItem
{
	/** Qualifier of the <code>WholescriptsBrand.code</code> attribute **/
	public static final String CODE = "code";
	/** Qualifier of the <code>WholescriptsBrand.name</code> attribute **/
	public static final String NAME = "name";
	/** Qualifier of the <code>WholescriptsBrand.description</code> attribute **/
	public static final String DESCRIPTION = "description";
	/** Qualifier of the <code>WholescriptsBrand.brandLogo</code> attribute **/
	public static final String BRANDLOGO = "brandLogo";
	/** Qualifier of the <code>WholescriptsBrand.brandImage</code> attribute **/
	public static final String BRANDIMAGE = "brandImage";
	/** Qualifier of the <code>WholescriptsBrand.brandUrl</code> attribute **/
	public static final String BRANDURL = "brandUrl";
	/** Qualifier of the <code>WholescriptsBrand.isTopBrand</code> attribute **/
	public static final String ISTOPBRAND = "isTopBrand";
	/** Qualifier of the <code>WholescriptsBrand.brandPriority</code> attribute **/
	public static final String BRANDPRIORITY = "brandPriority";
	/** Qualifier of the <code>WholescriptsBrand.brandAgreementPdfUrl</code> attribute **/
	public static final String BRANDAGREEMENTPDFURL = "brandAgreementPdfUrl";
	/** Qualifier of the <code>WholescriptsBrand.agreementRequired</code> attribute **/
	public static final String AGREEMENTREQUIRED = "agreementRequired";
	/** Qualifier of the <code>WholescriptsBrand.brandAgreementPdf</code> attribute **/
	public static final String BRANDAGREEMENTPDF = "brandAgreementPdf";
	/** Qualifier of the <code>WholescriptsBrand.shortDescription</code> attribute **/
	public static final String SHORTDESCRIPTION = "shortDescription";
	/** Qualifier of the <code>WholescriptsBrand.availableForContentSearch</code> attribute **/
	public static final String AVAILABLEFORCONTENTSEARCH = "availableForContentSearch";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CODE, AttributeMode.INITIAL);
		tmp.put(NAME, AttributeMode.INITIAL);
		tmp.put(DESCRIPTION, AttributeMode.INITIAL);
		tmp.put(BRANDLOGO, AttributeMode.INITIAL);
		tmp.put(BRANDIMAGE, AttributeMode.INITIAL);
		tmp.put(BRANDURL, AttributeMode.INITIAL);
		tmp.put(ISTOPBRAND, AttributeMode.INITIAL);
		tmp.put(BRANDPRIORITY, AttributeMode.INITIAL);
		tmp.put(BRANDAGREEMENTPDFURL, AttributeMode.INITIAL);
		tmp.put(AGREEMENTREQUIRED, AttributeMode.INITIAL);
		tmp.put(BRANDAGREEMENTPDF, AttributeMode.INITIAL);
		tmp.put(SHORTDESCRIPTION, AttributeMode.INITIAL);
		tmp.put(AVAILABLEFORCONTENTSEARCH, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.agreementRequired</code> attribute.
	 * @return the agreementRequired - Used to know whether  a  Brand a needs an  Agreement or not
	 */
	public Boolean isAgreementRequired(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, "agreementRequired".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.agreementRequired</code> attribute.
	 * @return the agreementRequired - Used to know whether  a  Brand a needs an  Agreement or not
	 */
	public Boolean isAgreementRequired()
	{
		return isAgreementRequired( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.agreementRequired</code> attribute. 
	 * @return the agreementRequired - Used to know whether  a  Brand a needs an  Agreement or not
	 */
	public boolean isAgreementRequiredAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isAgreementRequired( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.agreementRequired</code> attribute. 
	 * @return the agreementRequired - Used to know whether  a  Brand a needs an  Agreement or not
	 */
	public boolean isAgreementRequiredAsPrimitive()
	{
		return isAgreementRequiredAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.agreementRequired</code> attribute. 
	 * @param value the agreementRequired - Used to know whether  a  Brand a needs an  Agreement or not
	 */
	public void setAgreementRequired(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, "agreementRequired".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.agreementRequired</code> attribute. 
	 * @param value the agreementRequired - Used to know whether  a  Brand a needs an  Agreement or not
	 */
	public void setAgreementRequired(final Boolean value)
	{
		setAgreementRequired( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.agreementRequired</code> attribute. 
	 * @param value the agreementRequired - Used to know whether  a  Brand a needs an  Agreement or not
	 */
	public void setAgreementRequired(final SessionContext ctx, final boolean value)
	{
		setAgreementRequired( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.agreementRequired</code> attribute. 
	 * @param value the agreementRequired - Used to know whether  a  Brand a needs an  Agreement or not
	 */
	public void setAgreementRequired(final boolean value)
	{
		setAgreementRequired( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.availableForContentSearch</code> attribute.
	 * @return the availableForContentSearch
	 */
	public Boolean isAvailableForContentSearch(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, "availableForContentSearch".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.availableForContentSearch</code> attribute.
	 * @return the availableForContentSearch
	 */
	public Boolean isAvailableForContentSearch()
	{
		return isAvailableForContentSearch( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.availableForContentSearch</code> attribute. 
	 * @return the availableForContentSearch
	 */
	public boolean isAvailableForContentSearchAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isAvailableForContentSearch( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.availableForContentSearch</code> attribute. 
	 * @return the availableForContentSearch
	 */
	public boolean isAvailableForContentSearchAsPrimitive()
	{
		return isAvailableForContentSearchAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.availableForContentSearch</code> attribute. 
	 * @param value the availableForContentSearch
	 */
	public void setAvailableForContentSearch(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, "availableForContentSearch".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.availableForContentSearch</code> attribute. 
	 * @param value the availableForContentSearch
	 */
	public void setAvailableForContentSearch(final Boolean value)
	{
		setAvailableForContentSearch( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.availableForContentSearch</code> attribute. 
	 * @param value the availableForContentSearch
	 */
	public void setAvailableForContentSearch(final SessionContext ctx, final boolean value)
	{
		setAvailableForContentSearch( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.availableForContentSearch</code> attribute. 
	 * @param value the availableForContentSearch
	 */
	public void setAvailableForContentSearch(final boolean value)
	{
		setAvailableForContentSearch( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.brandAgreementPdf</code> attribute.
	 * @return the brandAgreementPdf
	 */
	public Media getBrandAgreementPdf(final SessionContext ctx)
	{
		return (Media)getProperty( ctx, "brandAgreementPdf".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.brandAgreementPdf</code> attribute.
	 * @return the brandAgreementPdf
	 */
	public Media getBrandAgreementPdf()
	{
		return getBrandAgreementPdf( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.brandAgreementPdf</code> attribute. 
	 * @param value the brandAgreementPdf
	 */
	public void setBrandAgreementPdf(final SessionContext ctx, final Media value)
	{
		setProperty(ctx, "brandAgreementPdf".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.brandAgreementPdf</code> attribute. 
	 * @param value the brandAgreementPdf
	 */
	public void setBrandAgreementPdf(final Media value)
	{
		setBrandAgreementPdf( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.brandAgreementPdfUrl</code> attribute.
	 * @return the brandAgreementPdfUrl
	 */
	public String getBrandAgreementPdfUrl(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "brandAgreementPdfUrl".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.brandAgreementPdfUrl</code> attribute.
	 * @return the brandAgreementPdfUrl
	 */
	public String getBrandAgreementPdfUrl()
	{
		return getBrandAgreementPdfUrl( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.brandAgreementPdfUrl</code> attribute. 
	 * @param value the brandAgreementPdfUrl
	 */
	public void setBrandAgreementPdfUrl(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "brandAgreementPdfUrl".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.brandAgreementPdfUrl</code> attribute. 
	 * @param value the brandAgreementPdfUrl
	 */
	public void setBrandAgreementPdfUrl(final String value)
	{
		setBrandAgreementPdfUrl( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.brandImage</code> attribute.
	 * @return the brandImage
	 */
	public Media getBrandImage(final SessionContext ctx)
	{
		return (Media)getProperty( ctx, "brandImage".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.brandImage</code> attribute.
	 * @return the brandImage
	 */
	public Media getBrandImage()
	{
		return getBrandImage( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.brandImage</code> attribute. 
	 * @param value the brandImage
	 */
	public void setBrandImage(final SessionContext ctx, final Media value)
	{
		setProperty(ctx, "brandImage".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.brandImage</code> attribute. 
	 * @param value the brandImage
	 */
	public void setBrandImage(final Media value)
	{
		setBrandImage( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.brandLogo</code> attribute.
	 * @return the brandLogo
	 */
	public Media getBrandLogo(final SessionContext ctx)
	{
		return (Media)getProperty( ctx, "brandLogo".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.brandLogo</code> attribute.
	 * @return the brandLogo
	 */
	public Media getBrandLogo()
	{
		return getBrandLogo( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.brandLogo</code> attribute. 
	 * @param value the brandLogo
	 */
	public void setBrandLogo(final SessionContext ctx, final Media value)
	{
		setProperty(ctx, "brandLogo".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.brandLogo</code> attribute. 
	 * @param value the brandLogo
	 */
	public void setBrandLogo(final Media value)
	{
		setBrandLogo( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.brandPriority</code> attribute.
	 * @return the brandPriority - Determines the order how we display in the page - sort
	 */
	public Integer getBrandPriority(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, "brandPriority".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.brandPriority</code> attribute.
	 * @return the brandPriority - Determines the order how we display in the page - sort
	 */
	public Integer getBrandPriority()
	{
		return getBrandPriority( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.brandPriority</code> attribute. 
	 * @return the brandPriority - Determines the order how we display in the page - sort
	 */
	public int getBrandPriorityAsPrimitive(final SessionContext ctx)
	{
		Integer value = getBrandPriority( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.brandPriority</code> attribute. 
	 * @return the brandPriority - Determines the order how we display in the page - sort
	 */
	public int getBrandPriorityAsPrimitive()
	{
		return getBrandPriorityAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.brandPriority</code> attribute. 
	 * @param value the brandPriority - Determines the order how we display in the page - sort
	 */
	public void setBrandPriority(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, "brandPriority".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.brandPriority</code> attribute. 
	 * @param value the brandPriority - Determines the order how we display in the page - sort
	 */
	public void setBrandPriority(final Integer value)
	{
		setBrandPriority( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.brandPriority</code> attribute. 
	 * @param value the brandPriority - Determines the order how we display in the page - sort
	 */
	public void setBrandPriority(final SessionContext ctx, final int value)
	{
		setBrandPriority( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.brandPriority</code> attribute. 
	 * @param value the brandPriority - Determines the order how we display in the page - sort
	 */
	public void setBrandPriority(final int value)
	{
		setBrandPriority( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.brandUrl</code> attribute.
	 * @return the brandUrl
	 */
	public String getBrandUrl(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "brandUrl".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.brandUrl</code> attribute.
	 * @return the brandUrl
	 */
	public String getBrandUrl()
	{
		return getBrandUrl( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.brandUrl</code> attribute. 
	 * @param value the brandUrl
	 */
	public void setBrandUrl(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "brandUrl".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.brandUrl</code> attribute. 
	 * @param value the brandUrl
	 */
	public void setBrandUrl(final String value)
	{
		setBrandUrl( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.code</code> attribute.
	 * @return the code
	 */
	public String getCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "code".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.code</code> attribute.
	 * @return the code
	 */
	public String getCode()
	{
		return getCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "code".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final String value)
	{
		setCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.description</code> attribute.
	 * @return the description
	 */
	public String getDescription(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("WholescriptsBrand.getDescription requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, "description".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.description</code> attribute.
	 * @return the description
	 */
	public String getDescription()
	{
		return getDescription( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.description</code> attribute. 
	 * @return the localized description
	 */
	public Map<Language,String> getAllDescription(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,"description".intern(),C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.description</code> attribute. 
	 * @return the localized description
	 */
	public Map<Language,String> getAllDescription()
	{
		return getAllDescription( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.description</code> attribute. 
	 * @param value the description
	 */
	public void setDescription(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("WholescriptsBrand.setDescription requires a session language", 0 );
		}
		setLocalizedProperty(ctx, "description".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.description</code> attribute. 
	 * @param value the description
	 */
	public void setDescription(final String value)
	{
		setDescription( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.description</code> attribute. 
	 * @param value the description
	 */
	public void setAllDescription(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,"description".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.description</code> attribute. 
	 * @param value the description
	 */
	public void setAllDescription(final Map<Language,String> value)
	{
		setAllDescription( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.isTopBrand</code> attribute.
	 * @return the isTopBrand - Determines if this brand needs to be sent for display
	 */
	public Boolean isIsTopBrand(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, "isTopBrand".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.isTopBrand</code> attribute.
	 * @return the isTopBrand - Determines if this brand needs to be sent for display
	 */
	public Boolean isIsTopBrand()
	{
		return isIsTopBrand( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.isTopBrand</code> attribute. 
	 * @return the isTopBrand - Determines if this brand needs to be sent for display
	 */
	public boolean isIsTopBrandAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isIsTopBrand( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.isTopBrand</code> attribute. 
	 * @return the isTopBrand - Determines if this brand needs to be sent for display
	 */
	public boolean isIsTopBrandAsPrimitive()
	{
		return isIsTopBrandAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.isTopBrand</code> attribute. 
	 * @param value the isTopBrand - Determines if this brand needs to be sent for display
	 */
	public void setIsTopBrand(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, "isTopBrand".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.isTopBrand</code> attribute. 
	 * @param value the isTopBrand - Determines if this brand needs to be sent for display
	 */
	public void setIsTopBrand(final Boolean value)
	{
		setIsTopBrand( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.isTopBrand</code> attribute. 
	 * @param value the isTopBrand - Determines if this brand needs to be sent for display
	 */
	public void setIsTopBrand(final SessionContext ctx, final boolean value)
	{
		setIsTopBrand( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.isTopBrand</code> attribute. 
	 * @param value the isTopBrand - Determines if this brand needs to be sent for display
	 */
	public void setIsTopBrand(final boolean value)
	{
		setIsTopBrand( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.name</code> attribute.
	 * @return the name
	 */
	public String getName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "name".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.name</code> attribute.
	 * @return the name
	 */
	public String getName()
	{
		return getName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.name</code> attribute. 
	 * @param value the name
	 */
	public void setName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "name".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.name</code> attribute. 
	 * @param value the name
	 */
	public void setName(final String value)
	{
		setName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.shortDescription</code> attribute.
	 * @return the shortDescription
	 */
	public String getShortDescription(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("WholescriptsBrand.getShortDescription requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, "shortDescription".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.shortDescription</code> attribute.
	 * @return the shortDescription
	 */
	public String getShortDescription()
	{
		return getShortDescription( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.shortDescription</code> attribute. 
	 * @return the localized shortDescription
	 */
	public Map<Language,String> getAllShortDescription(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,"shortDescription".intern(),C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>WholescriptsBrand.shortDescription</code> attribute. 
	 * @return the localized shortDescription
	 */
	public Map<Language,String> getAllShortDescription()
	{
		return getAllShortDescription( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.shortDescription</code> attribute. 
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
			throw new JaloInvalidParameterException("WholescriptsBrand.setShortDescription requires a session language", 0 );
		}
		setLocalizedProperty(ctx, "shortDescription".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.shortDescription</code> attribute. 
	 * @param value the shortDescription
	 */
	public void setShortDescription(final String value)
	{
		setShortDescription( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.shortDescription</code> attribute. 
	 * @param value the shortDescription
	 */
	public void setAllShortDescription(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,"shortDescription".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>WholescriptsBrand.shortDescription</code> attribute. 
	 * @param value the shortDescription
	 */
	public void setAllShortDescription(final Map<Language,String> value)
	{
		setAllShortDescription( getSession().getSessionContext(), value );
	}
	
}
