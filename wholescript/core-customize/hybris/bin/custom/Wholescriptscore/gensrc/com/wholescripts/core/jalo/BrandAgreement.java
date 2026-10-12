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
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type BrandAgreement.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class BrandAgreement extends GenericItem
{
	/** Qualifier of the <code>BrandAgreement.code</code> attribute **/
	public static final String CODE = "code";
	/** Qualifier of the <code>BrandAgreement.brandName</code> attribute **/
	public static final String BRANDNAME = "brandName";
	/** Qualifier of the <code>BrandAgreement.pdfPath</code> attribute **/
	public static final String PDFPATH = "pdfPath";
	/** Qualifier of the <code>BrandAgreement.agreementTimeStamp</code> attribute **/
	public static final String AGREEMENTTIMESTAMP = "agreementTimeStamp";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(CODE, AttributeMode.INITIAL);
		tmp.put(BRANDNAME, AttributeMode.INITIAL);
		tmp.put(PDFPATH, AttributeMode.INITIAL);
		tmp.put(AGREEMENTTIMESTAMP, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BrandAgreement.agreementTimeStamp</code> attribute.
	 * @return the agreementTimeStamp
	 */
	public String getAgreementTimeStamp(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "agreementTimeStamp".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BrandAgreement.agreementTimeStamp</code> attribute.
	 * @return the agreementTimeStamp
	 */
	public String getAgreementTimeStamp()
	{
		return getAgreementTimeStamp( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BrandAgreement.agreementTimeStamp</code> attribute. 
	 * @param value the agreementTimeStamp
	 */
	public void setAgreementTimeStamp(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "agreementTimeStamp".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BrandAgreement.agreementTimeStamp</code> attribute. 
	 * @param value the agreementTimeStamp
	 */
	public void setAgreementTimeStamp(final String value)
	{
		setAgreementTimeStamp( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BrandAgreement.brandName</code> attribute.
	 * @return the brandName
	 */
	public String getBrandName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "brandName".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BrandAgreement.brandName</code> attribute.
	 * @return the brandName
	 */
	public String getBrandName()
	{
		return getBrandName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BrandAgreement.brandName</code> attribute. 
	 * @param value the brandName
	 */
	public void setBrandName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "brandName".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BrandAgreement.brandName</code> attribute. 
	 * @param value the brandName
	 */
	public void setBrandName(final String value)
	{
		setBrandName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BrandAgreement.code</code> attribute.
	 * @return the code - Brand Agreement  Unique code
	 */
	public String getCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "code".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BrandAgreement.code</code> attribute.
	 * @return the code - Brand Agreement  Unique code
	 */
	public String getCode()
	{
		return getCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BrandAgreement.code</code> attribute. 
	 * @param value the code - Brand Agreement  Unique code
	 */
	public void setCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "code".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BrandAgreement.code</code> attribute. 
	 * @param value the code - Brand Agreement  Unique code
	 */
	public void setCode(final String value)
	{
		setCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BrandAgreement.pdfPath</code> attribute.
	 * @return the pdfPath
	 */
	public String getPdfPath(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "pdfPath".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BrandAgreement.pdfPath</code> attribute.
	 * @return the pdfPath
	 */
	public String getPdfPath()
	{
		return getPdfPath( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BrandAgreement.pdfPath</code> attribute. 
	 * @param value the pdfPath
	 */
	public void setPdfPath(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "pdfPath".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BrandAgreement.pdfPath</code> attribute. 
	 * @param value the pdfPath
	 */
	public void setPdfPath(final String value)
	{
		setPdfPath( getSession().getSessionContext(), value );
	}
	
}
