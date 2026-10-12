/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.wholescripts.core.constants;

import java.util.Map;

/**
 * Global class for all Wholescriptscore constants. You can add global constants for your extension into this class.
 */
public final class WholescriptscoreConstants extends GeneratedWholescriptscoreConstants
{
	public static final String EXTENSIONNAME = "Wholescriptscore";
	public static final String DATE_FORMAT_CONFIG = "wholescript.dateFormat.";
	public static final String DATE_FORMAT_CONFIG_DEFAULT = "default";
	public static final String DATE_FORMAT_IN_REQUEST = "yyyyMMdd";

	public static final String TEXT_CATEGORY = "CATEGORY";
	public static final String TEXT_BASIC = "BASIC";

	public static final Map<String, String> CYBERSOURCE_SOP_CARD_TYPES = Map.of("VISA", "001",
			"MC", "002",
			"AMEX", "003",
			"DISC", "004");

	public static final String SHIPPING_ADDRESS_KEY = "WE";
	public static final String BILLING_ADDRESS_KEY = "RE";
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final String SALES_ORG_1710 = "_1710_10_10";

	private WholescriptscoreConstants()
	{
		//empty to avoid instantiating this constant class
	}

	// implement here constants used by this extension

	public static final String PLATFORM_LOGO_CODE = "WholescriptscorePlatformLogo";
	public static final String ACTING_USER_UID = "ACTING_USER_UID";
	public static final String ACTING_USER_ROLE = "ACTING_USER_ROLE";
	public static final String ASAGENT = "asagent";
	public static final String ASAGENT_GROUP = "ROLE_ASAGENTGROUP";
	public static final String CLASSIFICATION_ATTRIBUTE_PRODUCT_NAME = "wholescripts.material.productname.classification.code";
	public static final String ORDER_HEADER_BILL_STATUS_C = "C";
	public static final String ORDER_HEADER_CANCELLED_P = "P";
	public static final String ORDER_HEADER_CANCELLED_B = "B";
	public static final String ORDER_HEADER_CANCELLED_C = "C";
	public static final String ORDER_HEADER_CANCELLED_X = "X";
	public static final String ORDER_HEADER_DLV_STATUS_C = "C";
	public static final String ORDER_HEADER_DLV_STATUS_P = "P";
	public static final String ORDER_HEADER_BIL_DOC_O = "O";
	public static final String ORDER_STATUS_REFUNDED = "Refunded";
	public static final String ORDER_STATUS_PARTIALLY_REFUNDED = "Partially Refunded";
	public static final String ORDER_STATUS_COMPLETE = "Complete";
	public static final String ORDER_STATUS_PARTIALLY_CANCELLED = "Partially Cancelled";
	public static final String ORDER_STATUS_CANCELLED = "Cancelled";
	public static final String ORDER_STATUS_SHIPPED = "Shipped";
	public static final String ORDER_STATUS_PARTIALLY_SHIPPED = "Partially Shipped";
	public static final String ORDER_STATUS_PROCESSING = "Processing";
	public static final String COMMA = ",";
	public static final String BESTSELLER_SORT = "bestSellers";
	public static final String BESTSELLER_SORT_FIELD = "isBestSeller";
	public static final String UNDERSCORE = "_";
	public static final String BESTSELLER_SORT_FIELD_TYPE = "boolean";
	public static final String BESTSELLER_SESSION_FIELD = "bestSellersCategory";

}
