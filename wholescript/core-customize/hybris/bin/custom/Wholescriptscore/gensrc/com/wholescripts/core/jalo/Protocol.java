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
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.order.CartEntry;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type Protocol.
 */
@SLDSafe
@SuppressWarnings({"unused","cast"})
public class Protocol extends Cart
{
	/** Qualifier of the <code>Protocol.protocolStatus</code> attribute **/
	public static final String PROTOCOLSTATUS = "protocolStatus";
	/** Qualifier of the <code>Protocol.protocolSharedStatus</code> attribute **/
	public static final String PROTOCOLSHAREDSTATUS = "protocolSharedStatus";
	/** Qualifier of the <code>Protocol.isWSProtocol</code> attribute **/
	public static final String ISWSPROTOCOL = "isWSProtocol";
	/** Qualifier of the <code>Protocol.termsAgreed</code> attribute **/
	public static final String TERMSAGREED = "termsAgreed";
	/** Qualifier of the <code>Protocol.protocolEntry</code> attribute **/
	public static final String PROTOCOLENTRY = "protocolEntry";
	/** Qualifier of the <code>Protocol.patients</code> attribute **/
	public static final String PATIENTS = "patients";
	/** Relation ordering override parameter constants for Protocol2Customer from ((Wholescriptscore))*/
	protected static String PROTOCOL2CUSTOMER_SRC_ORDERED = "relation.Protocol2Customer.source.ordered";
	protected static String PROTOCOL2CUSTOMER_TGT_ORDERED = "relation.Protocol2Customer.target.ordered";
	/** Relation disable markmodifed parameter constants for Protocol2Customer from ((Wholescriptscore))*/
	protected static String PROTOCOL2CUSTOMER_MARKMODIFIED = "relation.Protocol2Customer.markmodified";
	/**
	* {@link OneToManyHandler} for handling 1:n ENTRIES's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<AbstractOrderEntry> ENTRIESHANDLER = new OneToManyHandler<AbstractOrderEntry>(
	WholescriptscoreConstants.TC.PROTOCOLENTRY,
	true,
	"order",
	"entryNumber",
	false,
	true,
	CollectionType.LIST
	);
	/**
	* {@link OneToManyHandler} for handling 1:n PROTOCOLENTRY's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<CartEntry> PROTOCOLENTRYHANDLER = new OneToManyHandler<CartEntry>(
	WholescriptscoreConstants.TC.PROTOCOLENTRY,
	false,
	"protocol",
	null,
	false,
	true,
	CollectionType.LIST
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(Cart.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(PROTOCOLSTATUS, AttributeMode.INITIAL);
		tmp.put(PROTOCOLSHAREDSTATUS, AttributeMode.INITIAL);
		tmp.put(ISWSPROTOCOL, AttributeMode.INITIAL);
		tmp.put(TERMSAGREED, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Protocol.entries</code> attribute.
	 * @return the entries
	 */
	public List<AbstractOrderEntry> getEntries(final SessionContext ctx)
	{
		return (List<AbstractOrderEntry>)ENTRIESHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Protocol.entries</code> attribute.
	 * @return the entries
	 */
	public List<AbstractOrderEntry> getEntries()
	{
		return getEntries( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Protocol.entries</code> attribute. 
	 * @param value the entries
	 */
	public void setEntries(final SessionContext ctx, final List<AbstractOrderEntry> value)
	{
		ENTRIESHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Protocol.entries</code> attribute. 
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
	 * @deprecated since 2011, use {@link Utilities#getMarkModifiedOverride(de.hybris.platform.jalo.type.RelationType)
	 */
	@Override
	@Deprecated(since = "2105", forRemoval = true)
	public boolean isMarkModifiedDisabled(final Item referencedItem)
	{
		ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("B2BCustomer");
		if(relationSecondEnd0.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(PROTOCOL2CUSTOMER_MARKMODIFIED);
		}
		return true;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Protocol.isWSProtocol</code> attribute.
	 * @return the isWSProtocol - Flag whether it is a Wholescript Protocol
	 */
	public Boolean isIsWSProtocol(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, "isWSProtocol".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Protocol.isWSProtocol</code> attribute.
	 * @return the isWSProtocol - Flag whether it is a Wholescript Protocol
	 */
	public Boolean isIsWSProtocol()
	{
		return isIsWSProtocol( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Protocol.isWSProtocol</code> attribute. 
	 * @return the isWSProtocol - Flag whether it is a Wholescript Protocol
	 */
	public boolean isIsWSProtocolAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isIsWSProtocol( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Protocol.isWSProtocol</code> attribute. 
	 * @return the isWSProtocol - Flag whether it is a Wholescript Protocol
	 */
	public boolean isIsWSProtocolAsPrimitive()
	{
		return isIsWSProtocolAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Protocol.isWSProtocol</code> attribute. 
	 * @param value the isWSProtocol - Flag whether it is a Wholescript Protocol
	 */
	public void setIsWSProtocol(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, "isWSProtocol".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Protocol.isWSProtocol</code> attribute. 
	 * @param value the isWSProtocol - Flag whether it is a Wholescript Protocol
	 */
	public void setIsWSProtocol(final Boolean value)
	{
		setIsWSProtocol( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Protocol.isWSProtocol</code> attribute. 
	 * @param value the isWSProtocol - Flag whether it is a Wholescript Protocol
	 */
	public void setIsWSProtocol(final SessionContext ctx, final boolean value)
	{
		setIsWSProtocol( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Protocol.isWSProtocol</code> attribute. 
	 * @param value the isWSProtocol - Flag whether it is a Wholescript Protocol
	 */
	public void setIsWSProtocol(final boolean value)
	{
		setIsWSProtocol( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Protocol.patients</code> attribute.
	 * @return the patients
	 */
	public List<B2BCustomer> getPatients(final SessionContext ctx)
	{
		final List<B2BCustomer> items = getLinkedItems( 
			ctx,
			true,
			WholescriptscoreConstants.Relations.PROTOCOL2CUSTOMER,
			"B2BCustomer",
			null,
			Utilities.getRelationOrderingOverride(PROTOCOL2CUSTOMER_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(PROTOCOL2CUSTOMER_TGT_ORDERED, true)
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Protocol.patients</code> attribute.
	 * @return the patients
	 */
	public List<B2BCustomer> getPatients()
	{
		return getPatients( getSession().getSessionContext() );
	}
	
	public long getPatientsCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			true,
			WholescriptscoreConstants.Relations.PROTOCOL2CUSTOMER,
			"B2BCustomer",
			null
		);
	}
	
	public long getPatientsCount()
	{
		return getPatientsCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Protocol.patients</code> attribute. 
	 * @param value the patients
	 */
	public void setPatients(final SessionContext ctx, final List<B2BCustomer> value)
	{
		setLinkedItems( 
			ctx,
			true,
			WholescriptscoreConstants.Relations.PROTOCOL2CUSTOMER,
			null,
			value,
			Utilities.getRelationOrderingOverride(PROTOCOL2CUSTOMER_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(PROTOCOL2CUSTOMER_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(PROTOCOL2CUSTOMER_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Protocol.patients</code> attribute. 
	 * @param value the patients
	 */
	public void setPatients(final List<B2BCustomer> value)
	{
		setPatients( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to patients. 
	 * @param value the item to add to patients
	 */
	public void addToPatients(final SessionContext ctx, final B2BCustomer value)
	{
		addLinkedItems( 
			ctx,
			true,
			WholescriptscoreConstants.Relations.PROTOCOL2CUSTOMER,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(PROTOCOL2CUSTOMER_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(PROTOCOL2CUSTOMER_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(PROTOCOL2CUSTOMER_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to patients. 
	 * @param value the item to add to patients
	 */
	public void addToPatients(final B2BCustomer value)
	{
		addToPatients( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from patients. 
	 * @param value the item to remove from patients
	 */
	public void removeFromPatients(final SessionContext ctx, final B2BCustomer value)
	{
		removeLinkedItems( 
			ctx,
			true,
			WholescriptscoreConstants.Relations.PROTOCOL2CUSTOMER,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(PROTOCOL2CUSTOMER_SRC_ORDERED, true),
			Utilities.getRelationOrderingOverride(PROTOCOL2CUSTOMER_TGT_ORDERED, true),
			Utilities.getMarkModifiedOverride(PROTOCOL2CUSTOMER_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from patients. 
	 * @param value the item to remove from patients
	 */
	public void removeFromPatients(final B2BCustomer value)
	{
		removeFromPatients( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Protocol.protocolEntry</code> attribute.
	 * @return the protocolEntry
	 */
	public List<CartEntry> getProtocolEntry(final SessionContext ctx)
	{
		return (List<CartEntry>)PROTOCOLENTRYHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Protocol.protocolEntry</code> attribute.
	 * @return the protocolEntry
	 */
	public List<CartEntry> getProtocolEntry()
	{
		return getProtocolEntry( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Protocol.protocolEntry</code> attribute. 
	 * @param value the protocolEntry
	 */
	public void setProtocolEntry(final SessionContext ctx, final List<CartEntry> value)
	{
		PROTOCOLENTRYHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Protocol.protocolEntry</code> attribute. 
	 * @param value the protocolEntry
	 */
	public void setProtocolEntry(final List<CartEntry> value)
	{
		setProtocolEntry( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to protocolEntry. 
	 * @param value the item to add to protocolEntry
	 */
	public void addToProtocolEntry(final SessionContext ctx, final CartEntry value)
	{
		PROTOCOLENTRYHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to protocolEntry. 
	 * @param value the item to add to protocolEntry
	 */
	public void addToProtocolEntry(final CartEntry value)
	{
		addToProtocolEntry( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from protocolEntry. 
	 * @param value the item to remove from protocolEntry
	 */
	public void removeFromProtocolEntry(final SessionContext ctx, final CartEntry value)
	{
		PROTOCOLENTRYHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from protocolEntry. 
	 * @param value the item to remove from protocolEntry
	 */
	public void removeFromProtocolEntry(final CartEntry value)
	{
		removeFromProtocolEntry( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Protocol.protocolSharedStatus</code> attribute.
	 * @return the protocolSharedStatus - Shared Status of the Protocol
	 */
	public EnumerationValue getProtocolSharedStatus(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, "protocolSharedStatus".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Protocol.protocolSharedStatus</code> attribute.
	 * @return the protocolSharedStatus - Shared Status of the Protocol
	 */
	public EnumerationValue getProtocolSharedStatus()
	{
		return getProtocolSharedStatus( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Protocol.protocolSharedStatus</code> attribute. 
	 * @param value the protocolSharedStatus - Shared Status of the Protocol
	 */
	public void setProtocolSharedStatus(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, "protocolSharedStatus".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Protocol.protocolSharedStatus</code> attribute. 
	 * @param value the protocolSharedStatus - Shared Status of the Protocol
	 */
	public void setProtocolSharedStatus(final EnumerationValue value)
	{
		setProtocolSharedStatus( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Protocol.protocolStatus</code> attribute.
	 * @return the protocolStatus - Status of the Protocol
	 */
	public EnumerationValue getProtocolStatus(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, "protocolStatus".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Protocol.protocolStatus</code> attribute.
	 * @return the protocolStatus - Status of the Protocol
	 */
	public EnumerationValue getProtocolStatus()
	{
		return getProtocolStatus( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Protocol.protocolStatus</code> attribute. 
	 * @param value the protocolStatus - Status of the Protocol
	 */
	public void setProtocolStatus(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, "protocolStatus".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Protocol.protocolStatus</code> attribute. 
	 * @param value the protocolStatus - Status of the Protocol
	 */
	public void setProtocolStatus(final EnumerationValue value)
	{
		setProtocolStatus( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Protocol.termsAgreed</code> attribute.
	 * @return the termsAgreed - Flag for terms and condition
	 */
	public Boolean isTermsAgreed(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, "termsAgreed".intern());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Protocol.termsAgreed</code> attribute.
	 * @return the termsAgreed - Flag for terms and condition
	 */
	public Boolean isTermsAgreed()
	{
		return isTermsAgreed( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Protocol.termsAgreed</code> attribute. 
	 * @return the termsAgreed - Flag for terms and condition
	 */
	public boolean isTermsAgreedAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isTermsAgreed( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Protocol.termsAgreed</code> attribute. 
	 * @return the termsAgreed - Flag for terms and condition
	 */
	public boolean isTermsAgreedAsPrimitive()
	{
		return isTermsAgreedAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Protocol.termsAgreed</code> attribute. 
	 * @param value the termsAgreed - Flag for terms and condition
	 */
	public void setTermsAgreed(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, "termsAgreed".intern(),value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Protocol.termsAgreed</code> attribute. 
	 * @param value the termsAgreed - Flag for terms and condition
	 */
	public void setTermsAgreed(final Boolean value)
	{
		setTermsAgreed( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Protocol.termsAgreed</code> attribute. 
	 * @param value the termsAgreed - Flag for terms and condition
	 */
	public void setTermsAgreed(final SessionContext ctx, final boolean value)
	{
		setTermsAgreed( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Protocol.termsAgreed</code> attribute. 
	 * @param value the termsAgreed - Flag for terms and condition
	 */
	public void setTermsAgreed(final boolean value)
	{
		setTermsAgreed( getSession().getSessionContext(), value );
	}
	
}
