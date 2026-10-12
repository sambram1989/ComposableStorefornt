/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 27-May-2025, 11:44:08 pm                    ---
 * ----------------------------------------------------------------
 */
package com.wholescripts.core.jalo;

import com.wholescripts.core.constants.WholescriptscoreConstants;
import com.wholescripts.core.jalo.PatientDirectHub;
import de.hybris.platform.b2b.jalo.B2BUnit;
import de.hybris.platform.constants.CoreConstants;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type ReferralCodes.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class ReferralCodes extends GenericItem
{
	/** Qualifier of the <code>ReferralCodes.referralCode</code> attribute **/
	public static final String REFERRALCODE = "referralCode";
	/** Qualifier of the <code>ReferralCodes.lastName</code> attribute **/
	public static final String LASTNAME = "lastName";
	/** Qualifier of the <code>ReferralCodes.discount</code> attribute **/
	public static final String DISCOUNT = "discount";
	/** Qualifier of the <code>ReferralCodes.profileImage</code> attribute **/
	public static final String PROFILEIMAGE = "profileImage";
	/** Qualifier of the <code>ReferralCodes.referralLink</code> attribute **/
	public static final String REFERRALLINK = "referralLink";
	/** Qualifier of the <code>ReferralCodes.website</code> attribute **/
	public static final String WEBSITE = "website";
	/** Qualifier of the <code>ReferralCodes.description</code> attribute **/
	public static final String DESCRIPTION = "description";
	/** Qualifier of the <code>ReferralCodes.referralCodeType</code> attribute **/
	public static final String REFERRALCODETYPE = "referralCodeType";
	/** Qualifier of the <code>ReferralCodes.validFrom</code> attribute **/
	public static final String VALIDFROM = "validFrom";
	/** Qualifier of the <code>ReferralCodes.validTo</code> attribute **/
	public static final String VALIDTO = "validTo";
	/** Qualifier of the <code>ReferralCodes.patientDirectHub</code> attribute **/
	public static final String PATIENTDIRECTHUB = "patientDirectHub";
	/** Qualifier of the <code>ReferralCodes.b2bUnit</code> attribute **/
	public static final String B2BUNIT = "b2bUnit";
	/** Qualifier of the <code>ReferralCodes.address</code> attribute **/
	public static final String ADDRESS = "address";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n PATIENTDIRECTHUB's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<ReferralCodes> PATIENTDIRECTHUBHANDLER = new BidirectionalOneToManyHandler<ReferralCodes>(
	WholescriptscoreConstants.TC.REFERRALCODES,
	false,
	"patientDirectHub",
	null,
	false,
	true,
	CollectionType.SET
	);
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n B2BUNIT's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<ReferralCodes> B2BUNITHANDLER = new BidirectionalOneToManyHandler<ReferralCodes>(
	WholescriptscoreConstants.TC.REFERRALCODES,
	false,
	"b2bUnit",
	null,
	false,
	true,
	CollectionType.SET
	);
	/**
	* {@link OneToManyHandler} for handling 1:n ADDRESS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<Address> ADDRESSHANDLER = new OneToManyHandler<Address>(
	CoreConstants.TC.ADDRESS,
	false,
	"referralAddress",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(REFERRALCODE, AttributeMode.INITIAL);
		tmp.put(LASTNAME, AttributeMode.INITIAL);
		tmp.put(DISCOUNT, AttributeMode.INITIAL);
		tmp.put(PROFILEIMAGE, AttributeMode.INITIAL);
		tmp.put(REFERRALLINK, AttributeMode.INITIAL);
		tmp.put(WEBSITE, AttributeMode.INITIAL);
		tmp.put(DESCRIPTION, AttributeMode.INITIAL);
		tmp.put(REFERRALCODETYPE, AttributeMode.INITIAL);
		tmp.put(VALIDFROM, AttributeMode.INITIAL);
		tmp.put(VALIDTO, AttributeMode.INITIAL);
		tmp.put(PATIENTDIRECTHUB, AttributeMode.INITIAL);
		tmp.put(B2BUNIT, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReferralCodes.address</code> attribute.
	 * @return the address
	 */
	public Collection<Address> getAddress(final SessionContext ctx)
	{
		return ADDRESSHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReferralCodes.address</code> attribute.
	 * @return the address
	 */
	public Collection<Address> getAddress()
	{
		return getAddress( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReferralCodes.address</code> attribute. 
	 * @param value the address
	 */
	public void setAddress(final SessionContext ctx, final Collection<Address> value)
	{
		ADDRESSHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReferralCodes.address</code> attribute. 
	 * @param value the address
	 */
	public void setAddress(final Collection<Address> value)
	{
		setAddress( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to address. 
	 * @param value the item to add to address
	 */
	public void addToAddress(final SessionContext ctx, final Address value)
	{
		ADDRESSHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to address. 
	 * @param value the item to add to address
	 */
	public void addToAddress(final Address value)
	{
		addToAddress( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from address. 
	 * @param value the item to remove from address
	 */
	public void removeFromAddress(final SessionContext ctx, final Address value)
	{
		ADDRESSHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from address. 
	 * @param value the item to remove from address
	 */
	public void removeFromAddress(final Address value)
	{
		removeFromAddress( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReferralCodes.b2bUnit</code> attribute.
	 * @return the b2bUnit
	 */
	public B2BUnit getB2bUnit(final SessionContext ctx)
	{
		return (B2BUnit)getProperty( ctx, "b2bUnit".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReferralCodes.b2bUnit</code> attribute.
	 * @return the b2bUnit
	 */
	public B2BUnit getB2bUnit()
	{
		return getB2bUnit( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReferralCodes.b2bUnit</code> attribute. 
	 * @param value the b2bUnit
	 */
	public void setB2bUnit(final SessionContext ctx, final B2BUnit value)
	{
		B2BUNITHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReferralCodes.b2bUnit</code> attribute. 
	 * @param value the b2bUnit
	 */
	public void setB2bUnit(final B2BUnit value)
	{
		setB2bUnit( getSession().getSessionContext(), value );
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		PATIENTDIRECTHUBHANDLER.newInstance(ctx, allAttributes);
		B2BUNITHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReferralCodes.description</code> attribute.
	 * @return the description - Description for patientDirectHub
	 */
	public String getDescription(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "description".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReferralCodes.description</code> attribute.
	 * @return the description - Description for patientDirectHub
	 */
	public String getDescription()
	{
		return getDescription( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReferralCodes.description</code> attribute. 
	 * @param value the description - Description for patientDirectHub
	 */
	public void setDescription(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "description".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReferralCodes.description</code> attribute. 
	 * @param value the description - Description for patientDirectHub
	 */
	public void setDescription(final String value)
	{
		setDescription( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReferralCodes.discount</code> attribute.
	 * @return the discount
	 */
	public Integer getDiscount(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, "discount".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReferralCodes.discount</code> attribute.
	 * @return the discount
	 */
	public Integer getDiscount()
	{
		return getDiscount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReferralCodes.discount</code> attribute. 
	 * @return the discount
	 */
	public int getDiscountAsPrimitive(final SessionContext ctx)
	{
		Integer value = getDiscount( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReferralCodes.discount</code> attribute. 
	 * @return the discount
	 */
	public int getDiscountAsPrimitive()
	{
		return getDiscountAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReferralCodes.discount</code> attribute. 
	 * @param value the discount
	 */
	public void setDiscount(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, "discount".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReferralCodes.discount</code> attribute. 
	 * @param value the discount
	 */
	public void setDiscount(final Integer value)
	{
		setDiscount( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReferralCodes.discount</code> attribute. 
	 * @param value the discount
	 */
	public void setDiscount(final SessionContext ctx, final int value)
	{
		setDiscount( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReferralCodes.discount</code> attribute. 
	 * @param value the discount
	 */
	public void setDiscount(final int value)
	{
		setDiscount( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReferralCodes.lastName</code> attribute.
	 * @return the lastName
	 */
	public String getLastName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "lastName".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReferralCodes.lastName</code> attribute.
	 * @return the lastName
	 */
	public String getLastName()
	{
		return getLastName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReferralCodes.lastName</code> attribute. 
	 * @param value the lastName
	 */
	public void setLastName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "lastName".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReferralCodes.lastName</code> attribute. 
	 * @param value the lastName
	 */
	public void setLastName(final String value)
	{
		setLastName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReferralCodes.patientDirectHub</code> attribute.
	 * @return the patientDirectHub
	 */
	public PatientDirectHub getPatientDirectHub(final SessionContext ctx)
	{
		return (PatientDirectHub)getProperty( ctx, "patientDirectHub".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReferralCodes.patientDirectHub</code> attribute.
	 * @return the patientDirectHub
	 */
	public PatientDirectHub getPatientDirectHub()
	{
		return getPatientDirectHub( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReferralCodes.patientDirectHub</code> attribute. 
	 * @param value the patientDirectHub
	 */
	public void setPatientDirectHub(final SessionContext ctx, final PatientDirectHub value)
	{
		PATIENTDIRECTHUBHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReferralCodes.patientDirectHub</code> attribute. 
	 * @param value the patientDirectHub
	 */
	public void setPatientDirectHub(final PatientDirectHub value)
	{
		setPatientDirectHub( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReferralCodes.profileImage</code> attribute.
	 * @return the profileImage - Profile image associated with referral code
	 */
	public Media getProfileImage(final SessionContext ctx)
	{
		return (Media)getProperty( ctx, "profileImage".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReferralCodes.profileImage</code> attribute.
	 * @return the profileImage - Profile image associated with referral code
	 */
	public Media getProfileImage()
	{
		return getProfileImage( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReferralCodes.profileImage</code> attribute. 
	 * @param value the profileImage - Profile image associated with referral code
	 */
	public void setProfileImage(final SessionContext ctx, final Media value)
	{
		setProperty(ctx, "profileImage".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReferralCodes.profileImage</code> attribute. 
	 * @param value the profileImage - Profile image associated with referral code
	 */
	public void setProfileImage(final Media value)
	{
		setProfileImage( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReferralCodes.referralCode</code> attribute.
	 * @return the referralCode
	 */
	public String getReferralCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "referralCode".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReferralCodes.referralCode</code> attribute.
	 * @return the referralCode
	 */
	public String getReferralCode()
	{
		return getReferralCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReferralCodes.referralCode</code> attribute. 
	 * @param value the referralCode
	 */
	public void setReferralCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "referralCode".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReferralCodes.referralCode</code> attribute. 
	 * @param value the referralCode
	 */
	public void setReferralCode(final String value)
	{
		setReferralCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReferralCodes.referralCodeType</code> attribute.
	 * @return the referralCodeType
	 */
	public String getReferralCodeType(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "referralCodeType".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReferralCodes.referralCodeType</code> attribute.
	 * @return the referralCodeType
	 */
	public String getReferralCodeType()
	{
		return getReferralCodeType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReferralCodes.referralCodeType</code> attribute. 
	 * @param value the referralCodeType
	 */
	public void setReferralCodeType(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "referralCodeType".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReferralCodes.referralCodeType</code> attribute. 
	 * @param value the referralCodeType
	 */
	public void setReferralCodeType(final String value)
	{
		setReferralCodeType( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReferralCodes.referralLink</code> attribute.
	 * @return the referralLink - Referral link for sharing
	 */
	public String getReferralLink(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "referralLink".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReferralCodes.referralLink</code> attribute.
	 * @return the referralLink - Referral link for sharing
	 */
	public String getReferralLink()
	{
		return getReferralLink( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReferralCodes.referralLink</code> attribute. 
	 * @param value the referralLink - Referral link for sharing
	 */
	public void setReferralLink(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "referralLink".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReferralCodes.referralLink</code> attribute. 
	 * @param value the referralLink - Referral link for sharing
	 */
	public void setReferralLink(final String value)
	{
		setReferralLink( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReferralCodes.validFrom</code> attribute.
	 * @return the validFrom
	 */
	public String getValidFrom(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "validFrom".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReferralCodes.validFrom</code> attribute.
	 * @return the validFrom
	 */
	public String getValidFrom()
	{
		return getValidFrom( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReferralCodes.validFrom</code> attribute. 
	 * @param value the validFrom
	 */
	public void setValidFrom(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "validFrom".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReferralCodes.validFrom</code> attribute. 
	 * @param value the validFrom
	 */
	public void setValidFrom(final String value)
	{
		setValidFrom( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReferralCodes.validTo</code> attribute.
	 * @return the validTo
	 */
	public String getValidTo(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "validTo".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReferralCodes.validTo</code> attribute.
	 * @return the validTo
	 */
	public String getValidTo()
	{
		return getValidTo( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReferralCodes.validTo</code> attribute. 
	 * @param value the validTo
	 */
	public void setValidTo(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "validTo".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReferralCodes.validTo</code> attribute. 
	 * @param value the validTo
	 */
	public void setValidTo(final String value)
	{
		setValidTo( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReferralCodes.website</code> attribute.
	 * @return the website - Website associated with the referral code
	 */
	public String getWebsite(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "website".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReferralCodes.website</code> attribute.
	 * @return the website - Website associated with the referral code
	 */
	public String getWebsite()
	{
		return getWebsite( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReferralCodes.website</code> attribute. 
	 * @param value the website - Website associated with the referral code
	 */
	public void setWebsite(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "website".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReferralCodes.website</code> attribute. 
	 * @param value the website - Website associated with the referral code
	 */
	public void setWebsite(final String value)
	{
		setWebsite( getSession().getSessionContext(), value );
	}
	
}
