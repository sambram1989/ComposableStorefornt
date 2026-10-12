/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 27-May-2025, 11:44:08 pm                    ---
 * ----------------------------------------------------------------
 */
package com.wholescripts.core.jalo;

import com.wholescripts.core.constants.WholescriptscoreConstants;
import de.hybris.platform.b2b.jalo.B2BCustomer;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.order.CartEntry;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type Recommendation.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class Recommendation extends Cart
{
	/** Qualifier of the <code>Recommendation.recommendationStatus</code> attribute **/
	public static final String RECOMMENDATIONSTATUS = "recommendationStatus";
	/** Qualifier of the <code>Recommendation.patient</code> attribute **/
	public static final String PATIENT = "patient";
	/** Qualifier of the <code>Recommendation.termsAgreed</code> attribute **/
	public static final String TERMSAGREED = "termsAgreed";
	/** Qualifier of the <code>Recommendation.sendEmail</code> attribute **/
	public static final String SENDEMAIL = "sendEmail";
	/** Qualifier of the <code>Recommendation.isSmsNotificationEnabled</code> attribute **/
	public static final String ISSMSNOTIFICATIONENABLED = "isSmsNotificationEnabled";
	/** Qualifier of the <code>Recommendation.isEmailNotificationEnabled</code> attribute **/
	public static final String ISEMAILNOTIFICATIONENABLED = "isEmailNotificationEnabled";
	/** Qualifier of the <code>Recommendation.referralCode</code> attribute **/
	public static final String REFERRALCODE = "referralCode";
	/** Qualifier of the <code>Recommendation.referralName</code> attribute **/
	public static final String REFERRALNAME = "referralName";
	/** Qualifier of the <code>Recommendation.sourceId</code> attribute **/
	public static final String SOURCEID = "sourceId";
	/** Qualifier of the <code>Recommendation.sourceName</code> attribute **/
	public static final String SOURCENAME = "sourceName";
	/**
	* {@link OneToManyHandler} for handling 1:n ENTRIES's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<AbstractOrderEntry> ENTRIESHANDLER = new OneToManyHandler<AbstractOrderEntry>(
	WholescriptscoreConstants.TC.RECOMMENDATIONENTRY,
	true,
	"order",
	"entryNumber",
	false,
	true,
	CollectionType.LIST
	);
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n PATIENT's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<Recommendation> PATIENTHANDLER = new BidirectionalOneToManyHandler<Recommendation>(
	WholescriptscoreConstants.TC.RECOMMENDATION,
	false,
	"patient",
	null,
	false,
	true,
	CollectionType.LIST
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(Cart.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(RECOMMENDATIONSTATUS, AttributeMode.INITIAL);
		tmp.put(PATIENT, AttributeMode.INITIAL);
		tmp.put(TERMSAGREED, AttributeMode.INITIAL);
		tmp.put(SENDEMAIL, AttributeMode.INITIAL);
		tmp.put(ISSMSNOTIFICATIONENABLED, AttributeMode.INITIAL);
		tmp.put(ISEMAILNOTIFICATIONENABLED, AttributeMode.INITIAL);
		tmp.put(REFERRALCODE, AttributeMode.INITIAL);
		tmp.put(REFERRALNAME, AttributeMode.INITIAL);
		tmp.put(SOURCEID, AttributeMode.INITIAL);
		tmp.put(SOURCENAME, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		PATIENTHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Recommendation.entries</code> attribute.
	 * @return the entries
	 */
	public List<AbstractOrderEntry> getEntries(final SessionContext ctx)
	{
		return (List<AbstractOrderEntry>)ENTRIESHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Recommendation.entries</code> attribute.
	 * @return the entries
	 */
	public List<AbstractOrderEntry> getEntries()
	{
		return getEntries( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Recommendation.entries</code> attribute. 
	 * @param value the entries
	 */
	public void setEntries(final SessionContext ctx, final List<AbstractOrderEntry> value)
	{
		ENTRIESHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Recommendation.entries</code> attribute. 
	 * @param value the entries
	 */
	public void setEntries(final List<AbstractOrderEntry> value)
	{
		setEntries( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to entries. 
	 * @param value the item to add to entries
	 */
	public void addToEntries(final SessionContext ctx, final CartEntry value)
	{
		ENTRIESHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to entries. 
	 * @param value the item to add to entries
	 */
	public void addToEntries(final CartEntry value)
	{
		addToEntries( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from entries. 
	 * @param value the item to remove from entries
	 */
	public void removeFromEntries(final SessionContext ctx, final CartEntry value)
	{
		ENTRIESHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from entries. 
	 * @param value the item to remove from entries
	 */
	public void removeFromEntries(final CartEntry value)
	{
		removeFromEntries( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Recommendation.isEmailNotificationEnabled</code> attribute.
	 * @return the isEmailNotificationEnabled
	 */
	public Boolean isIsEmailNotificationEnabled(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, "isEmailNotificationEnabled".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Recommendation.isEmailNotificationEnabled</code> attribute.
	 * @return the isEmailNotificationEnabled
	 */
	public Boolean isIsEmailNotificationEnabled()
	{
		return isIsEmailNotificationEnabled( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Recommendation.isEmailNotificationEnabled</code> attribute. 
	 * @return the isEmailNotificationEnabled
	 */
	public boolean isIsEmailNotificationEnabledAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isIsEmailNotificationEnabled( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Recommendation.isEmailNotificationEnabled</code> attribute. 
	 * @return the isEmailNotificationEnabled
	 */
	public boolean isIsEmailNotificationEnabledAsPrimitive()
	{
		return isIsEmailNotificationEnabledAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Recommendation.isEmailNotificationEnabled</code> attribute. 
	 * @param value the isEmailNotificationEnabled
	 */
	public void setIsEmailNotificationEnabled(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, "isEmailNotificationEnabled".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Recommendation.isEmailNotificationEnabled</code> attribute. 
	 * @param value the isEmailNotificationEnabled
	 */
	public void setIsEmailNotificationEnabled(final Boolean value)
	{
		setIsEmailNotificationEnabled( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Recommendation.isEmailNotificationEnabled</code> attribute. 
	 * @param value the isEmailNotificationEnabled
	 */
	public void setIsEmailNotificationEnabled(final SessionContext ctx, final boolean value)
	{
		setIsEmailNotificationEnabled( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Recommendation.isEmailNotificationEnabled</code> attribute. 
	 * @param value the isEmailNotificationEnabled
	 */
	public void setIsEmailNotificationEnabled(final boolean value)
	{
		setIsEmailNotificationEnabled( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Recommendation.isSmsNotificationEnabled</code> attribute.
	 * @return the isSmsNotificationEnabled
	 */
	public Boolean isIsSmsNotificationEnabled(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, "isSmsNotificationEnabled".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Recommendation.isSmsNotificationEnabled</code> attribute.
	 * @return the isSmsNotificationEnabled
	 */
	public Boolean isIsSmsNotificationEnabled()
	{
		return isIsSmsNotificationEnabled( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Recommendation.isSmsNotificationEnabled</code> attribute. 
	 * @return the isSmsNotificationEnabled
	 */
	public boolean isIsSmsNotificationEnabledAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isIsSmsNotificationEnabled( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Recommendation.isSmsNotificationEnabled</code> attribute. 
	 * @return the isSmsNotificationEnabled
	 */
	public boolean isIsSmsNotificationEnabledAsPrimitive()
	{
		return isIsSmsNotificationEnabledAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Recommendation.isSmsNotificationEnabled</code> attribute. 
	 * @param value the isSmsNotificationEnabled
	 */
	public void setIsSmsNotificationEnabled(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, "isSmsNotificationEnabled".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Recommendation.isSmsNotificationEnabled</code> attribute. 
	 * @param value the isSmsNotificationEnabled
	 */
	public void setIsSmsNotificationEnabled(final Boolean value)
	{
		setIsSmsNotificationEnabled( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Recommendation.isSmsNotificationEnabled</code> attribute. 
	 * @param value the isSmsNotificationEnabled
	 */
	public void setIsSmsNotificationEnabled(final SessionContext ctx, final boolean value)
	{
		setIsSmsNotificationEnabled( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Recommendation.isSmsNotificationEnabled</code> attribute. 
	 * @param value the isSmsNotificationEnabled
	 */
	public void setIsSmsNotificationEnabled(final boolean value)
	{
		setIsSmsNotificationEnabled( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Recommendation.patient</code> attribute.
	 * @return the patient - Patient or Practitioner
	 */
	public B2BCustomer getPatient(final SessionContext ctx)
	{
		return (B2BCustomer)getProperty( ctx, "patient".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Recommendation.patient</code> attribute.
	 * @return the patient - Patient or Practitioner
	 */
	public B2BCustomer getPatient()
	{
		return getPatient( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Recommendation.patient</code> attribute. 
	 * @param value the patient - Patient or Practitioner
	 */
	public void setPatient(final SessionContext ctx, final B2BCustomer value)
	{
		PATIENTHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Recommendation.patient</code> attribute. 
	 * @param value the patient - Patient or Practitioner
	 */
	public void setPatient(final B2BCustomer value)
	{
		setPatient( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Recommendation.recommendationStatus</code> attribute.
	 * @return the recommendationStatus
	 */
	public EnumerationValue getRecommendationStatus(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, "recommendationStatus".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Recommendation.recommendationStatus</code> attribute.
	 * @return the recommendationStatus
	 */
	public EnumerationValue getRecommendationStatus()
	{
		return getRecommendationStatus( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Recommendation.recommendationStatus</code> attribute. 
	 * @param value the recommendationStatus
	 */
	public void setRecommendationStatus(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, "recommendationStatus".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Recommendation.recommendationStatus</code> attribute. 
	 * @param value the recommendationStatus
	 */
	public void setRecommendationStatus(final EnumerationValue value)
	{
		setRecommendationStatus( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Recommendation.referralCode</code> attribute.
	 * @return the referralCode
	 */
	public String getReferralCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "referralCode".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Recommendation.referralCode</code> attribute.
	 * @return the referralCode
	 */
	public String getReferralCode()
	{
		return getReferralCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Recommendation.referralCode</code> attribute. 
	 * @param value the referralCode
	 */
	public void setReferralCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "referralCode".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Recommendation.referralCode</code> attribute. 
	 * @param value the referralCode
	 */
	public void setReferralCode(final String value)
	{
		setReferralCode( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Recommendation.referralName</code> attribute.
	 * @return the referralName
	 */
	public String getReferralName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "referralName".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Recommendation.referralName</code> attribute.
	 * @return the referralName
	 */
	public String getReferralName()
	{
		return getReferralName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Recommendation.referralName</code> attribute. 
	 * @param value the referralName
	 */
	public void setReferralName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "referralName".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Recommendation.referralName</code> attribute. 
	 * @param value the referralName
	 */
	public void setReferralName(final String value)
	{
		setReferralName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Recommendation.sendEmail</code> attribute.
	 * @return the sendEmail - Flag to check whether email is triggered
	 */
	public Boolean isSendEmail(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, "sendEmail".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Recommendation.sendEmail</code> attribute.
	 * @return the sendEmail - Flag to check whether email is triggered
	 */
	public Boolean isSendEmail()
	{
		return isSendEmail( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Recommendation.sendEmail</code> attribute. 
	 * @return the sendEmail - Flag to check whether email is triggered
	 */
	public boolean isSendEmailAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isSendEmail( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Recommendation.sendEmail</code> attribute. 
	 * @return the sendEmail - Flag to check whether email is triggered
	 */
	public boolean isSendEmailAsPrimitive()
	{
		return isSendEmailAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Recommendation.sendEmail</code> attribute. 
	 * @param value the sendEmail - Flag to check whether email is triggered
	 */
	public void setSendEmail(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, "sendEmail".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Recommendation.sendEmail</code> attribute. 
	 * @param value the sendEmail - Flag to check whether email is triggered
	 */
	public void setSendEmail(final Boolean value)
	{
		setSendEmail( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Recommendation.sendEmail</code> attribute. 
	 * @param value the sendEmail - Flag to check whether email is triggered
	 */
	public void setSendEmail(final SessionContext ctx, final boolean value)
	{
		setSendEmail( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Recommendation.sendEmail</code> attribute. 
	 * @param value the sendEmail - Flag to check whether email is triggered
	 */
	public void setSendEmail(final boolean value)
	{
		setSendEmail( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Recommendation.sourceId</code> attribute.
	 * @return the sourceId
	 */
	public String getSourceId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "sourceId".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Recommendation.sourceId</code> attribute.
	 * @return the sourceId
	 */
	public String getSourceId()
	{
		return getSourceId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Recommendation.sourceId</code> attribute. 
	 * @param value the sourceId
	 */
	public void setSourceId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "sourceId".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Recommendation.sourceId</code> attribute. 
	 * @param value the sourceId
	 */
	public void setSourceId(final String value)
	{
		setSourceId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Recommendation.sourceName</code> attribute.
	 * @return the sourceName
	 */
	public String getSourceName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "sourceName".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Recommendation.sourceName</code> attribute.
	 * @return the sourceName
	 */
	public String getSourceName()
	{
		return getSourceName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Recommendation.sourceName</code> attribute. 
	 * @param value the sourceName
	 */
	public void setSourceName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "sourceName".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Recommendation.sourceName</code> attribute. 
	 * @param value the sourceName
	 */
	public void setSourceName(final String value)
	{
		setSourceName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Recommendation.termsAgreed</code> attribute.
	 * @return the termsAgreed - Flag for terms and condition
	 */
	public Boolean isTermsAgreed(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, "termsAgreed".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Recommendation.termsAgreed</code> attribute.
	 * @return the termsAgreed - Flag for terms and condition
	 */
	public Boolean isTermsAgreed()
	{
		return isTermsAgreed( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Recommendation.termsAgreed</code> attribute. 
	 * @return the termsAgreed - Flag for terms and condition
	 */
	public boolean isTermsAgreedAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isTermsAgreed( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Recommendation.termsAgreed</code> attribute. 
	 * @return the termsAgreed - Flag for terms and condition
	 */
	public boolean isTermsAgreedAsPrimitive()
	{
		return isTermsAgreedAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Recommendation.termsAgreed</code> attribute. 
	 * @param value the termsAgreed - Flag for terms and condition
	 */
	public void setTermsAgreed(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, "termsAgreed".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Recommendation.termsAgreed</code> attribute. 
	 * @param value the termsAgreed - Flag for terms and condition
	 */
	public void setTermsAgreed(final Boolean value)
	{
		setTermsAgreed( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Recommendation.termsAgreed</code> attribute. 
	 * @param value the termsAgreed - Flag for terms and condition
	 */
	public void setTermsAgreed(final SessionContext ctx, final boolean value)
	{
		setTermsAgreed( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Recommendation.termsAgreed</code> attribute. 
	 * @param value the termsAgreed - Flag for terms and condition
	 */
	public void setTermsAgreed(final boolean value)
	{
		setTermsAgreed( getSession().getSessionContext(), value );
	}
	
}
