/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 27-May-2025, 11:44:08 pm                    ---
 * ----------------------------------------------------------------
 */
package com.wholescripts.core.jalo;

import de.hybris.platform.b2b.jalo.B2BCustomer;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.user.Address;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type W9Form.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class W9Form extends GenericItem
{
	/** Qualifier of the <code>W9Form.b2bCustomer</code> attribute **/
	public static final String B2BCUSTOMER = "b2bCustomer";
	/** Qualifier of the <code>W9Form.name</code> attribute **/
	public static final String NAME = "name";
	/** Qualifier of the <code>W9Form.businessName</code> attribute **/
	public static final String BUSINESSNAME = "businessName";
	/** Qualifier of the <code>W9Form.fedTaxClass</code> attribute **/
	public static final String FEDTAXCLASS = "fedTaxClass";
	/** Qualifier of the <code>W9Form.taxClassLimitedText</code> attribute **/
	public static final String TAXCLASSLIMITEDTEXT = "taxClassLimitedText";
	/** Qualifier of the <code>W9Form.taxClassOtherText</code> attribute **/
	public static final String TAXCLASSOTHERTEXT = "taxClassOtherText";
	/** Qualifier of the <code>W9Form.exemptPayee</code> attribute **/
	public static final String EXEMPTPAYEE = "exemptPayee";
	/** Qualifier of the <code>W9Form.exemptFatca</code> attribute **/
	public static final String EXEMPTFATCA = "exemptFatca";
	/** Qualifier of the <code>W9Form.address</code> attribute **/
	public static final String ADDRESS = "address";
	/** Qualifier of the <code>W9Form.accountNumbers</code> attribute **/
	public static final String ACCOUNTNUMBERS = "accountNumbers";
	/** Qualifier of the <code>W9Form.requestersInfo</code> attribute **/
	public static final String REQUESTERSINFO = "requestersInfo";
	/** Qualifier of the <code>W9Form.ssn</code> attribute **/
	public static final String SSN = "ssn";
	/** Qualifier of the <code>W9Form.ein</code> attribute **/
	public static final String EIN = "ein";
	/** Qualifier of the <code>W9Form.signature</code> attribute **/
	public static final String SIGNATURE = "signature";
	/** Qualifier of the <code>W9Form.signatureDate</code> attribute **/
	public static final String SIGNATUREDATE = "signatureDate";
	/** Qualifier of the <code>W9Form.browserString</code> attribute **/
	public static final String BROWSERSTRING = "browserString";
	/** Qualifier of the <code>W9Form.ip</code> attribute **/
	public static final String IP = "ip";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(B2BCUSTOMER, AttributeMode.INITIAL);
		tmp.put(NAME, AttributeMode.INITIAL);
		tmp.put(BUSINESSNAME, AttributeMode.INITIAL);
		tmp.put(FEDTAXCLASS, AttributeMode.INITIAL);
		tmp.put(TAXCLASSLIMITEDTEXT, AttributeMode.INITIAL);
		tmp.put(TAXCLASSOTHERTEXT, AttributeMode.INITIAL);
		tmp.put(EXEMPTPAYEE, AttributeMode.INITIAL);
		tmp.put(EXEMPTFATCA, AttributeMode.INITIAL);
		tmp.put(ADDRESS, AttributeMode.INITIAL);
		tmp.put(ACCOUNTNUMBERS, AttributeMode.INITIAL);
		tmp.put(REQUESTERSINFO, AttributeMode.INITIAL);
		tmp.put(SSN, AttributeMode.INITIAL);
		tmp.put(EIN, AttributeMode.INITIAL);
		tmp.put(SIGNATURE, AttributeMode.INITIAL);
		tmp.put(SIGNATUREDATE, AttributeMode.INITIAL);
		tmp.put(BROWSERSTRING, AttributeMode.INITIAL);
		tmp.put(IP, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>W9Form.accountNumbers</code> attribute.
	 * @return the accountNumbers
	 */
	public String getAccountNumbers(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "accountNumbers".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>W9Form.accountNumbers</code> attribute.
	 * @return the accountNumbers
	 */
	public String getAccountNumbers()
	{
		return getAccountNumbers( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>W9Form.accountNumbers</code> attribute. 
	 * @param value the accountNumbers
	 */
	public void setAccountNumbers(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "accountNumbers".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>W9Form.accountNumbers</code> attribute. 
	 * @param value the accountNumbers
	 */
	public void setAccountNumbers(final String value)
	{
		setAccountNumbers( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>W9Form.address</code> attribute.
	 * @return the address
	 */
	public Address getAddress(final SessionContext ctx)
	{
		return (Address)getProperty( ctx, "address".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>W9Form.address</code> attribute.
	 * @return the address
	 */
	public Address getAddress()
	{
		return getAddress( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>W9Form.address</code> attribute. 
	 * @param value the address
	 */
	public void setAddress(final SessionContext ctx, final Address value)
	{
		setProperty(ctx, "address".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>W9Form.address</code> attribute. 
	 * @param value the address
	 */
	public void setAddress(final Address value)
	{
		setAddress( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>W9Form.b2bCustomer</code> attribute.
	 * @return the b2bCustomer
	 */
	public B2BCustomer getB2bCustomer(final SessionContext ctx)
	{
		return (B2BCustomer)getProperty( ctx, "b2bCustomer".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>W9Form.b2bCustomer</code> attribute.
	 * @return the b2bCustomer
	 */
	public B2BCustomer getB2bCustomer()
	{
		return getB2bCustomer( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>W9Form.b2bCustomer</code> attribute. 
	 * @param value the b2bCustomer
	 */
	public void setB2bCustomer(final SessionContext ctx, final B2BCustomer value)
	{
		setProperty(ctx, "b2bCustomer".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>W9Form.b2bCustomer</code> attribute. 
	 * @param value the b2bCustomer
	 */
	public void setB2bCustomer(final B2BCustomer value)
	{
		setB2bCustomer( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>W9Form.browserString</code> attribute.
	 * @return the browserString
	 */
	public String getBrowserString(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "browserString".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>W9Form.browserString</code> attribute.
	 * @return the browserString
	 */
	public String getBrowserString()
	{
		return getBrowserString( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>W9Form.browserString</code> attribute. 
	 * @param value the browserString
	 */
	public void setBrowserString(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "browserString".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>W9Form.browserString</code> attribute. 
	 * @param value the browserString
	 */
	public void setBrowserString(final String value)
	{
		setBrowserString( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>W9Form.businessName</code> attribute.
	 * @return the businessName
	 */
	public String getBusinessName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "businessName".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>W9Form.businessName</code> attribute.
	 * @return the businessName
	 */
	public String getBusinessName()
	{
		return getBusinessName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>W9Form.businessName</code> attribute. 
	 * @param value the businessName
	 */
	public void setBusinessName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "businessName".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>W9Form.businessName</code> attribute. 
	 * @param value the businessName
	 */
	public void setBusinessName(final String value)
	{
		setBusinessName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>W9Form.ein</code> attribute.
	 * @return the ein
	 */
	public String getEin(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "ein".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>W9Form.ein</code> attribute.
	 * @return the ein
	 */
	public String getEin()
	{
		return getEin( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>W9Form.ein</code> attribute. 
	 * @param value the ein
	 */
	public void setEin(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "ein".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>W9Form.ein</code> attribute. 
	 * @param value the ein
	 */
	public void setEin(final String value)
	{
		setEin( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>W9Form.exemptFatca</code> attribute.
	 * @return the exemptFatca
	 */
	public String getExemptFatca(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "exemptFatca".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>W9Form.exemptFatca</code> attribute.
	 * @return the exemptFatca
	 */
	public String getExemptFatca()
	{
		return getExemptFatca( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>W9Form.exemptFatca</code> attribute. 
	 * @param value the exemptFatca
	 */
	public void setExemptFatca(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "exemptFatca".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>W9Form.exemptFatca</code> attribute. 
	 * @param value the exemptFatca
	 */
	public void setExemptFatca(final String value)
	{
		setExemptFatca( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>W9Form.exemptPayee</code> attribute.
	 * @return the exemptPayee
	 */
	public String getExemptPayee(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "exemptPayee".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>W9Form.exemptPayee</code> attribute.
	 * @return the exemptPayee
	 */
	public String getExemptPayee()
	{
		return getExemptPayee( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>W9Form.exemptPayee</code> attribute. 
	 * @param value the exemptPayee
	 */
	public void setExemptPayee(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "exemptPayee".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>W9Form.exemptPayee</code> attribute. 
	 * @param value the exemptPayee
	 */
	public void setExemptPayee(final String value)
	{
		setExemptPayee( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>W9Form.fedTaxClass</code> attribute.
	 * @return the fedTaxClass
	 */
	public EnumerationValue getFedTaxClass(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, "fedTaxClass".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>W9Form.fedTaxClass</code> attribute.
	 * @return the fedTaxClass
	 */
	public EnumerationValue getFedTaxClass()
	{
		return getFedTaxClass( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>W9Form.fedTaxClass</code> attribute. 
	 * @param value the fedTaxClass
	 */
	public void setFedTaxClass(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, "fedTaxClass".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>W9Form.fedTaxClass</code> attribute. 
	 * @param value the fedTaxClass
	 */
	public void setFedTaxClass(final EnumerationValue value)
	{
		setFedTaxClass( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>W9Form.ip</code> attribute.
	 * @return the ip
	 */
	public String getIp(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "ip".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>W9Form.ip</code> attribute.
	 * @return the ip
	 */
	public String getIp()
	{
		return getIp( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>W9Form.ip</code> attribute. 
	 * @param value the ip
	 */
	public void setIp(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "ip".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>W9Form.ip</code> attribute. 
	 * @param value the ip
	 */
	public void setIp(final String value)
	{
		setIp( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>W9Form.name</code> attribute.
	 * @return the name
	 */
	public String getName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "name".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>W9Form.name</code> attribute.
	 * @return the name
	 */
	public String getName()
	{
		return getName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>W9Form.name</code> attribute. 
	 * @param value the name
	 */
	public void setName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "name".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>W9Form.name</code> attribute. 
	 * @param value the name
	 */
	public void setName(final String value)
	{
		setName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>W9Form.requestersInfo</code> attribute.
	 * @return the requestersInfo
	 */
	public String getRequestersInfo(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "requestersInfo".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>W9Form.requestersInfo</code> attribute.
	 * @return the requestersInfo
	 */
	public String getRequestersInfo()
	{
		return getRequestersInfo( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>W9Form.requestersInfo</code> attribute. 
	 * @param value the requestersInfo
	 */
	public void setRequestersInfo(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "requestersInfo".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>W9Form.requestersInfo</code> attribute. 
	 * @param value the requestersInfo
	 */
	public void setRequestersInfo(final String value)
	{
		setRequestersInfo( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>W9Form.signature</code> attribute.
	 * @return the signature
	 */
	public String getSignature(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "signature".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>W9Form.signature</code> attribute.
	 * @return the signature
	 */
	public String getSignature()
	{
		return getSignature( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>W9Form.signature</code> attribute. 
	 * @param value the signature
	 */
	public void setSignature(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "signature".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>W9Form.signature</code> attribute. 
	 * @param value the signature
	 */
	public void setSignature(final String value)
	{
		setSignature( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>W9Form.signatureDate</code> attribute.
	 * @return the signatureDate
	 */
	public Date getSignatureDate(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, "signatureDate".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>W9Form.signatureDate</code> attribute.
	 * @return the signatureDate
	 */
	public Date getSignatureDate()
	{
		return getSignatureDate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>W9Form.signatureDate</code> attribute. 
	 * @param value the signatureDate
	 */
	public void setSignatureDate(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, "signatureDate".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>W9Form.signatureDate</code> attribute. 
	 * @param value the signatureDate
	 */
	public void setSignatureDate(final Date value)
	{
		setSignatureDate( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>W9Form.ssn</code> attribute.
	 * @return the ssn
	 */
	public String getSsn(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "ssn".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>W9Form.ssn</code> attribute.
	 * @return the ssn
	 */
	public String getSsn()
	{
		return getSsn( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>W9Form.ssn</code> attribute. 
	 * @param value the ssn
	 */
	public void setSsn(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "ssn".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>W9Form.ssn</code> attribute. 
	 * @param value the ssn
	 */
	public void setSsn(final String value)
	{
		setSsn( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>W9Form.taxClassLimitedText</code> attribute.
	 * @return the taxClassLimitedText
	 */
	public String getTaxClassLimitedText(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "taxClassLimitedText".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>W9Form.taxClassLimitedText</code> attribute.
	 * @return the taxClassLimitedText
	 */
	public String getTaxClassLimitedText()
	{
		return getTaxClassLimitedText( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>W9Form.taxClassLimitedText</code> attribute. 
	 * @param value the taxClassLimitedText
	 */
	public void setTaxClassLimitedText(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "taxClassLimitedText".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>W9Form.taxClassLimitedText</code> attribute. 
	 * @param value the taxClassLimitedText
	 */
	public void setTaxClassLimitedText(final String value)
	{
		setTaxClassLimitedText( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>W9Form.taxClassOtherText</code> attribute.
	 * @return the taxClassOtherText
	 */
	public String getTaxClassOtherText(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "taxClassOtherText".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>W9Form.taxClassOtherText</code> attribute.
	 * @return the taxClassOtherText
	 */
	public String getTaxClassOtherText()
	{
		return getTaxClassOtherText( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>W9Form.taxClassOtherText</code> attribute. 
	 * @param value the taxClassOtherText
	 */
	public void setTaxClassOtherText(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "taxClassOtherText".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>W9Form.taxClassOtherText</code> attribute. 
	 * @param value the taxClassOtherText
	 */
	public void setTaxClassOtherText(final String value)
	{
		setTaxClassOtherText( getSession().getSessionContext(), value );
	}
	
}
