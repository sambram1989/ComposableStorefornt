import { selectedEnv } from '../ws-env/environment';
const CURRENT = `users/current`;
const ORG_CURRENT = `orgUsers/current`;
const envHostname = selectedEnv.occ;
const PROTOCOL = `${CURRENT}/protocols`;

const RECOMMENDATION = `${CURRENT}/recommendation`;
//Landing Page
export const USER_PROTOCOLS_ENDPOINT = ({
  currentPage = 0,
  pageSize = 10,
  sort = 'uid:asc',
  searchText = ''
}) =>
  `${PROTOCOL}?currentPage=${currentPage}&pageSize=${pageSize}&sort=${sort}&protocolName=${searchText}`;
export const CREATE_A_NEW_PROTOCOL = `${PROTOCOL}`;
export const EDIT_PROTOCOL_NAME = (protocolId = '') => `${PROTOCOL}/${protocolId}`;
export const DUPLICATE_A_PROTOCOL = (protocolId = '') => `${PROTOCOL}/${protocolId}/duplicate`;
export const DELETE_PROTOCOL = (protocolId = '') => `${PROTOCOL}/${protocolId}/delete`;
//EDIT PAGE ___-----
//getting left hand side product list
export const EDIT_PROTOCOL_LIST_API = `products/search?fields=FULL`;
//right hand side list of products in protocol
export const EDIT_PROTOCOL_PRODUCT_API = ({ protocolId = '' }) => `${PROTOCOL}/${protocolId}?fields=FULL`;
//review
export const FETCH_PROTOCOL_REVIEW_API = ({ protocolId = '' }) => `${PROTOCOL}/${protocolId}?fields=FULL`;
export const SEND_PROTOCOL_REVIEW_API = ({ protocolId = '' }) => `${PROTOCOL}/${protocolId}?protocolId=${protocolId}`;
//UPDATING DOSING ENTRY TOO without entry no
export const EDIT_ADD_PRODUCT_API = (protocolId = '') => `${PROTOCOL}/${protocolId}/entries`;
export const EDIT_DELETE_PRODUCT_API = ({ protocolId = '', entryNumber = '' }) => `${PROTOCOL}/${protocolId}/entries/${entryNumber}`;
export const GET_ICONS = `${CURRENT}/itemtimes`;
//save as custom
export const GET_CUSTOM_DOSING_ENTRIES = (productId = '') => `${CURRENT}/getCustomDosingEntries?productId=${productId}`;
export const POST_CUSTOM_DOSING_ENTRIES = ({ isSavedAsCustom = false, productId = '' }) => `${CURRENT}/saveCustomDosingEntries?productId=${productId}&isSavedAsCustom=${isSavedAsCustom}`;
//upadting edit dosing POPUP
export const UPDATING_A_PROTOCOL_WITH_ENTRY_NO = ({ protocolId = '', entryNo = '' }) => `${PROTOCOL}/${protocolId}/${entryNo}`;
export const UPDATING_A_PROTOCOL_WITHOUT_ENTRY_NO = ({ protocolId = '' }) => `${PROTOCOL}/${protocolId}/entries`;
//SHARING _____
export const SHARE_PROTOCOL_WITH_ALL = (protocolId = '') => `${PROTOCOL}/share/${protocolId}`;
export const STOP_SHARING_WITH_SPECIFIC_USER = (protocolId = '') => `${PROTOCOL}/${protocolId}/stop-sharing`;

//CLONE PROTOCOL
export const CLONE_PROTOCOL_TO_RECOMMENDATION =  `${CURRENT}/prReference/clone`;

//Find a Practioner
export const GET_REGIONS = (countryIsoCode = 'US') => `countries/${countryIsoCode}/regions`;
export const GET_PRACTITIONER_DETAILS =  `${CURRENT}/stores/getPractice`;
export const ADD_PRACTITIONER_DETAILS =  `${CURRENT}/stores/practice?fields=DEFAULT`;
export const UPDATE_PRACTITIONER_DETAILS = (name: string) => `${CURRENT}/stores/practice?fields=DEFAULT&posName=${name}`;
export const REMOVE_PRACTITIONER_FROM_DIRECTORY = (name: string) =>  `${CURRENT}/stores/pointOfService/optOutPractice?posName=${name}&optOutPractice=true`;
export const UPLOAD_PROFILE_IMAGE = (name: string) => `${CURRENT}/stores/uploadProfileImage?fields=DEFAULT&posName=${name}`;

// Find Practiotioner For Patient
export const GET_SEARCHED_PRACTITIONER_DETAILS = `stores/pointOfServiceList`;

// Patient Profile
export const GET_PATIENT_PROFILE_DETAILS = `${CURRENT}/getPatientDetails`;
export const GET_PATIENT_RECOMMENDATION_DETAILS = `${CURRENT}/getPatientRecommendations`;
export const GET_PATIENT_ORDER_HISTORY_DETAILS = `${CURRENT}/orderListById`;
export const GET_PATIENT_ACCOUNT_UPDATE_DETAILS = `${CURRENT}/accountUpdate`;

//WEbinars API ENDPOINTS

export const WEBINAR_SEARCH = `wsSearch/webinar?fields=FULL`;
export const WEBINAR_SEARCH_GET_FEATURED = `wsSearch/getFeatured?fields=FULL`;
export const WEBINAR_DETAILS = (webinarId = '') =>  `wsSearch/webinars/${encodeURI(webinarId)}?fields=FULL,relatedWebinars(FULL)`;
export const PRESENTOR_DETAILS = (presentorId = '') =>  `wsSearch/presenters/${encodeURI(presentorId)}?fields=FULL`;
export const PRESENTOR_LIST = `wsSearch/presenters`;
export const WEBINAR_WATCH_STATUS = `wsSearch/updateWatchStatus`;

//Patient direct HUB

export const CONSULTATION_FEES = `${CURRENT}/showW9Link`;
export const CONSULTATION_FEES_DOWNLOAD = `${CURRENT}/downloadW9Form`;

// search
export const CONTENT_SEARCH = `wsSearch/content?fields=FULL`;

// Checkout Delivery Address
export const GET_CHECKOUT_DELIVERY_ADDRESSES = (cartId='') => `${CURRENT}/carts/${cartId}/addresses`;
export const CHECKOUT_DELIVERY_ADDRESSES_VALIDATE = `${CURRENT}/addresses/verification?fields=DEFAULT`;
export const CHECKOUT_DELIVERY_ADDRESSES_ADD = (cartId='') => `${CURRENT}/carts/${cartId}/addresses/delivery?fields=FULL`;
export const CHECKOUT_DELIVERY_ADDRESSES_CREATE = (cartId='') => `${CURRENT}/carts/${cartId}/addresses/create-delivery-address?fields=DEFAULT`;
export const CHECKOUT_DELIVERY_ADDRESSES_VERIFICATION = (cartId='') => `${CURRENT}/carts/${cartId}/verification`;
export const CART_REMOVE_ITEMS = (cartId='') => `${CURRENT}/carts/${cartId}/entries`;
export const BRAND_CONSENT_UPDATE = `${CURRENT}/carts/current/brandConsentUpdate`;

//MedPax custom

export const  GET_MEDPAX_DETAILS = `${CURRENT}/getMyMedPax`;
export const CREATE_MEDPAX = `${CURRENT}/medPax/create`;
export const UPDATE_MEDPAX =  (medPaxID='') => `${CURRENT}/medPax/${medPaxID}/addEntry`;
export const UPDATE_MEDPAX_ENTRY = (medPaxEntryNumber='') => `${CURRENT}/medPaxEntry/${medPaxEntryNumber}`;
export const GET_MEDPAX_PACKETS =(medPaxID='') =>  `${CURRENT}/getMedPaxPackets/${medPaxID}`;
export const UPDATE_MEDPAX_PACKET = `${CURRENT}/update/medPaxPacketsInfo`;
export const UPDATE_PACKET_NAME = `${CURRENT}/updateMedPaxName`;
export const ADD_MEDPAX_TO_CART = (cartId='') => `${CURRENT}/carts/${cartId}/addtocart`;
export const GET_MEDPAX_FOR_OWNER = `${CURRENT}/getMedPaxForOwner`;
export const GET_MEDPAX_FOR_OWNER_WHOLESCRIPTS_PROTOCOL = `${CURRENT}/getMedPaxForOwner?fields=FULL&isWSProtocol=true`;
export const DELETE_MEDPAX = ({medPaxId='',medPaxEntryNumber=''}) => `${CURRENT}/medPaxEntry/${medPaxId}/${medPaxEntryNumber}`;
export const DELETE_ENTIRE_MEDPAX = `${CURRENT}/medPax/delete`;
export const GET_MEDPAX_BY_CODE = `${CURRENT}/getMedPaxByCode`;

export const CREATE_MEDPAX_NOTES=`${CURRENT}/updateMedPaxNotes`;

export const MEDPAX_BUY_AGAIN_API = () => `${CURRENT}/getOrderedMedpaxProductsForUser`;

export const UPDATING_A_RECOMMENDATION_WITH_ENTRY_NO = ({ protocolId = '', entryNo = '' }) => `${RECOMMENDATION}/${protocolId}/entries/${entryNo}`;
export const UPDATING_A_RECOMMENDATION_WITHOUT_ENTRY_NO = ({ protocolId = '' }) => `${RECOMMENDATION}/${protocolId}/entries`;
export const SHARE_RECOMMENDATION_FROM_REVIEW_PAGE = (recoId='') => `${RECOMMENDATION}/${recoId}/share`;

// Order details MedPax

export const MEDPAX_CARTON = '000000000200058254';
export const MEDPAX_HEADER = '000000000500000031';
export const MEDPAX_STANDALONE = 'STANDALONE';
export const MEDPAX_PRINTED_EXTERIOR = 'Medpax printed Exterior';
export const MEDPAX_CARTON_PRODUCT = '000000000500000032';
export const MEDPAX_HEADER_PRODUCT = '000000000200000633';

export const MEDPAX_CARTON_NEW = '000000000200000633,000000000200058254';
export const MEDPAX_HEADER_NEW = '000000000500000030,000000000500000031';
export const MEDPAX_DOSING_NEW = '000000000500000032';

//  Payment setting
export const GET_PAYMENTS = (isSaved: boolean) => `${CURRENT}/paymentdetails?fields=DEFAULT&saved=${isSaved}`;

// paymtrics
export const PAYMETRICS_RESPONSE_STATUS = 'knbr/customers/${customerId}/carts/${cartId}/payment/sop/response';
export const PAYMETRICS_GET_ACCESS_TOKEN = `${envHostname}/occ/v2/wholescripts/XiIntercept3/getAccessToken`;
//Payment Step Addresses

export const GET_ALL_ADDRESSES = `${CURRENT}/addresses/get-all-addresses?fields=FULL`;
export const ADD_CARD_IN_CHECKOUT_FLOW =  `${CURRENT}/payment/sop/response`;
export const REMOVE_CARD = (cardId='') => `${CURRENT}/remove/${cardId}`;
export const UPDATE_PAYMENT_DETAILS = (cartId='',paymentDetailsId='') => `${CURRENT}/carts/${cartId}/${paymentDetailsId}`;
export const GET_PAYMENT_DETAILS = (cartId='') => `${CURRENT}/carts/${cartId}/getPaymentInfoDetails?fields=DEFAULT`;

// Addresses

export const COMMERCE_ALL_ADDRESSES = `${CURRENT}/addresses`;


export const LOCAL_AUTH = 'spartacus⚿⚿auth';
export const LOCAL_CART = 'spartacus⚿wholescripts⚿cart';

// Autoships
export const GET_AUTOSHIPS = `${CURRENT}/get-auto-ship`;
export const GET_AUTOSHIPS_DETAILS = (orderNumber: number) => `${CURRENT}/get-auto-ship/${orderNumber}`;
export const UPDATE_AUTOSHIP_ORDER = `${CURRENT}/updateAutoShipOrder`;
export const AUTOSHIP_ADDRESSES_VERIFICATION = `${CURRENT}/autoship/verification`;

// FAQs
export const GET_FAQ_BY_ID = (id: string) => `getFaqQusAnsById/${id}`;

// Addresses
export const ADD_ADDRESS = `${CURRENT}/addresses`;
export const UPDATE_ADDRESS = `${CURRENT}/addresses/update-address`;
export const VALIDATE_ADDRESS = `${CURRENT}/addresses/verification?fields=DEFAULT`;
export const DELETE_ADDRESS = (addressNumber: number, creditCardIds: string) => `${CURRENT}/addresses/remove-address/${addressNumber}?creditCardIds=${creditCardIds}`;

// Private Label API ENDPOINTS
export const PRIVATE_LABEL = `wsSearch/search/private-label?fields=FULL`;

//Delete Entire cart
export const DELETE_ENTIRE_CART = (cartId='',userId='') => `users/${userId}/carts/${cartId}`;

//SEO
export const SEO_CONSTANT_KEY = 'seoMetaClassificationDataClass';

// PDP product family
export const PDP_PRODUCT_FAMILY = (productCode: string) => `products/product-family?fields=DEFAULT&productCode=${productCode}`;
export const PDP_ADD_TO_CART = (cartId='') => `${ORG_CURRENT}/carts/${cartId}/entries`;
export const CATEGORY_PREFIX = 'Z_PRODUCT_';
export const BRAND_PREFIX = 'Z_MM_';
// Shopify  
export const SHOPIFY_STATUS = `${CURRENT}/shopify/status`;
export const SHOPIFY_PRODUCTS_SEARCH = `users/current/shopify/products/search`;
export const ADD_SHOPIFY_PRODUCT = `${CURRENT}/shopify/products`
export const DELETE_SHOPIFY_PRODUCT = `${CURRENT}/shopify/products`

export const SHOPIFY_PRODUCTS = `${CURRENT}/shopify/products`;

// Promotions
export const GET_PROMOTIONS = `promotions`;
export const GET_PROMO_DETAILS =  (promoCode='') => `promotions/${promoCode}?fields=FULL`;

//cancel an order from order details

export const CANCEL_ORDER = (orderId='') => `${CURRENT}/cancel-order/${orderId}`;

//PRIVATE LABEL

export const GET_PRIVATE_LABEL_CARTONS = `${CURRENT}/getPrivateLabelCartons`;
export const POST_PRIVATE_LABEL_CARTON = `${CURRENT}/setPrivateLabelCarton`;
// ?cartonName=Test&medpaxId=10000&productCode=000000000300092928


//W9 Form API 
export const W9_FORM = `${CURRENT}/sendW9Info`;
export const REFERRAL_CODE_FORM = `${CURRENT}/referral-code`;
export const GET_ALL_REFERRAL_CODES = `${CURRENT}/all-referral-codes`;
export const GET_DONWLOAD_FORM=`${CURRENT}/downloadW9Form`;

//Porduct-details 
export const PRODUCT_DETAILS = (productId: string) => `products/${productId}/product-details?fields=FULL`;

//Suplement Facts
export const SUPPLEMENT_FACTS = (productId: string[]) => `products/supplement-facts?fields=DEFAULT&productIds=${productId}`;

// Referral Codes

export const REFERRAL_CODE_DETAILS = `${CURRENT}/referral-code-details`;
export const UPLOAD_PROFILE_IMAGE_FOR_REFERRAL = (referralCode: string) => `${CURRENT}/referral-code/${referralCode}/uploadProfileImage`;

//UPDATE PRACTITIONER
export const UPDATE_PRACTITIONER = `${CURRENT}/changePractitioner`;

// GET PRACTITIONER DETAILS
export const GET_PRACTITIONER = `${CURRENT}/getPractitionerDetails?fields=contactNumber,defaultAddress(FULL),email,name`;

// CREDIT CARD PAYMENT IMAGES
export const BASE_PATH = (card: string) => `/assets/icons/payment_method_icons/${card}-lg.svg`;

//SWAP Protocol 
export const SWAP_PROTOCOL_PATCH = (recommendationId:string,templateId:string) => `${CURRENT}/recommendation/${recommendationId}/swap-protocol?fields=DEFAULT&recommendationId=${recommendationId}&templateId=${templateId}`;

//update qty for medpax in cart
export const UPDATE_CART_QTY_MEDPAX = (cartId:string,medPaxEntryNumber:string) => `${CURRENT}/carts/${cartId}/medpax/${medPaxEntryNumber}`;

//delete medpax formula entries from cart
export const REMOVE_MEDPAX_FORMULA_FROM_CART = (cartId:string,entryNumber:string) => `${CURRENT}/carts/${cartId}/medpax/delete/${entryNumber}`;

// create promo code
export const CREATE_PROMO_CODE = (cartId:string) => `${CURRENT}/carts/${cartId}/promotions`;

// delete promo code
export const REMOVE_PROMO_CODE = (cartId:string, promoCode: string) => `${CURRENT}/carts/${cartId}/promotions/${promoCode}`;

// practitioner dashboard
export const GET_DASHBOARD_DETAILS = `/dashboard/current/getDashboardDetails?fields=FULL`;

// Best seller products
export const GET_BEST_SELLERS = `${CURRENT}/getBestSellerProducts?fields=FULL`;

// ACH Payment - Delego
export const DELEGO_TOKEN = `${CURRENT}/delego/token`;

// Order Simulate pages
export const ORDER_SIMULATE_CART_PATHS = ['/catalog', '/practitioner/dashboard', '/privatelabel', '/Full-Catalog/', '/my-favorites'];
  
``