/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 27-May-2025, 11:44:08 pm                    ---
 * ----------------------------------------------------------------
 */
package com.wholescripts.core.jalo;

import com.wholescripts.core.constants.WholescriptscoreConstants;
import com.wholescripts.core.jalo.ReferralCodes;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Generated class for type PatientDirectHub.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class PatientDirectHub extends GenericItem
{
	/** Qualifier of the <code>PatientDirectHub.id</code> attribute **/
	public static final String ID = "id";
	/** Qualifier of the <code>PatientDirectHub.referralCodes</code> attribute **/
	public static final String REFERRALCODES = "referralCodes";
	/**
	* {@link OneToManyHandler} for handling 1:n REFERRALCODES's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<ReferralCodes> REFERRALCODESHANDLER = new OneToManyHandler<ReferralCodes>(
	WholescriptscoreConstants.TC.REFERRALCODES,
	false,
	"patientDirectHub",
	null,
	false,
	true,
	CollectionType.SET
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(ID, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PatientDirectHub.id</code> attribute.
	 * @return the id
	 */
	public String getId(final SessionContext ctx)
	{
		return (String)getProperty( ctx, "id".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PatientDirectHub.id</code> attribute.
	 * @return the id
	 */
	public String getId()
	{
		return getId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PatientDirectHub.id</code> attribute. 
	 * @param value the id
	 */
	public void setId(final SessionContext ctx, final String value)
	{
		setProperty(ctx, "id".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PatientDirectHub.id</code> attribute. 
	 * @param value the id
	 */
	public void setId(final String value)
	{
		setId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PatientDirectHub.referralCodes</code> attribute.
	 * @return the referralCodes
	 */
	public Set<ReferralCodes> getReferralCodes(final SessionContext ctx)
	{
		return (Set<ReferralCodes>)REFERRALCODESHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PatientDirectHub.referralCodes</code> attribute.
	 * @return the referralCodes
	 */
	public Set<ReferralCodes> getReferralCodes()
	{
		return getReferralCodes( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PatientDirectHub.referralCodes</code> attribute. 
	 * @param value the referralCodes
	 */
	public void setReferralCodes(final SessionContext ctx, final Set<ReferralCodes> value)
	{
		REFERRALCODESHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PatientDirectHub.referralCodes</code> attribute. 
	 * @param value the referralCodes
	 */
	public void setReferralCodes(final Set<ReferralCodes> value)
	{
		setReferralCodes( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to referralCodes. 
	 * @param value the item to add to referralCodes
	 */
	public void addToReferralCodes(final SessionContext ctx, final ReferralCodes value)
	{
		REFERRALCODESHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to referralCodes. 
	 * @param value the item to add to referralCodes
	 */
	public void addToReferralCodes(final ReferralCodes value)
	{
		addToReferralCodes( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from referralCodes. 
	 * @param value the item to remove from referralCodes
	 */
	public void removeFromReferralCodes(final SessionContext ctx, final ReferralCodes value)
	{
		REFERRALCODESHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from referralCodes. 
	 * @param value the item to remove from referralCodes
	 */
	public void removeFromReferralCodes(final ReferralCodes value)
	{
		removeFromReferralCodes( getSession().getSessionContext(), value );
	}
	
}
