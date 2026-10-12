package com.wholescripts.core.dao.impl;


import com.wholescripts.core.model.*;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCustomerService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchParameter;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.*;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wholescripts.core.dao.WholescriptsCoreDao;


public class WholescriptsCoreDaoImpl implements WholescriptsCoreDao {

	private final static String FIND_ITEMTIME_QUERY = "SELECT {" + ItemTimeModel.PK + "} FROM {" + ItemTimeModel._TYPECODE + "} WHERE {" + ItemTimeModel.CODE + "}=?code";
	/*private final static String FIND_DISCONTINUED_PRODUCT_QUERY = "SELECT {" + ProductModel.PK + "} FROM {"
			+ ProductModel._TYPECODE + "} WHERE {" + ProductModel.APPROVALSTATUS + "}=?approvalStatus  AND {"
			+ ProductModel.CATALOGVERSION + "}=?catalogVersion AND {" + ProductModel.DISCONTINUEDDATE + "} <= ?discontinuedDate";*/
	private final static String FIND_DISCONTINUED_PRODUCTS_QUERY =
			"SELECT {p." + ProductModel.PK + "} " +
					"FROM {" + ProductModel._TYPECODE + " AS p " +
					"JOIN WsDiscontinuedProducts AS dp ON {p." + ProductModel.CODE + "} = {dp.discontinuedProductsSku}} " +
					"WHERE {dp.discontinuedDate} <= ?discontinuedDate " +
					"AND {p." + ProductModel.CATALOGVERSION + "} = ?catalogVersion";

	private final static String FIND_AWSL_PRODUCTS_QUERY =
			"SELECT {p." + ProductModel.PK + "} " +
					"FROM {" + ProductModel._TYPECODE + " AS p " +
					"JOIN WsAvailableSuppliesLast AS awsl ON {p." + ProductModel.CODE + "} = {awsl.availableSuppliesLastSku}} " +
					"WHERE {p." + ProductModel.CATALOGVERSION + "} = ?catalogVersion";

	private final static String FIND_DosingByProductnUser_QUERY = "SELECT {" + DosingModel.PK + "} FROM {" + DosingModel._TYPECODE + "} WHERE {" + DosingModel.PRODUCT + "}=?productPK  AND {" + DosingModel.B2BCUSTOMER + "}=?b2bCustomerPk AND {" + DosingModel.ISSAVEDASCUSTOM + "}=?isSavedAsCustom";
	private final static String FIND_DosingBy_User_QUERY = "SELECT {" + DosingModel.PK + "} FROM {" + DosingModel._TYPECODE + "} WHERE {" + DosingModel.B2BCUSTOMER + "}=?b2bCustomerPk AND {" + DosingModel.ISSAVEDASCUSTOM + "}=?isSavedAsCustom";
	protected static final String GET_ALL_ACTIVE_ITEM_TIMES = "SELECT {" + ItemTimeModel.PK + "} FROM {" + ItemTimeModel._TYPECODE + "} WHERE {" + ItemTimeModel.ISDISPLAYABLE + "}=?isDisplayable";
	protected static final String GET_FAQ_QUESTION_LIST = "SELECT {" + WholescriptsFaqListModel.PK + "} FROM {" + WholescriptsFaqListModel._TYPECODE + "} WHERE {" + WholescriptsFaqListModel.ISCHILD + "}=?isChild";
	protected static final String GET_TOPIC_LIST = "SELECT {" + WholescriptsTopicModel.PK + "} FROM {"
			+ WholescriptsTopicModel._TYPECODE + "} WHERE {" + WholescriptsTopicModel.PAGE + "}=?page AND  {"
			+ WholescriptsTopicModel.CHILDTOPIC + "}=?childTopic ";

	private static final String WHOLESCRIPTS_BRAND_QUERY = "SELECT {b.PK} from {WholescriptsBrand AS b} WHERE {b.code}=?code";
	private static final String WHOLESCRIPTS_BRAND_QUERY_BY_NAME = "SELECT {b.PK} from {WholescriptsBrand AS b} WHERE {b.name}=?name";

	private static final String WHOLESCRIPTS_TOP_BRANDS_QUERY = "SELECT {b.PK} from {WholescriptsBrand AS b} ORDER BY {brandPriority} DESC";
	private static final String GET_FEATURED_WEBINAR = "SELECT {" + WebinarModel.PK + "} FROM {"
			+ WebinarModel._TYPECODE + "} WHERE {" + WebinarModel.ISFEATURED + "}=?isFeatured";

	private static final String GET_WEBINAR_DETAILS_BY_CODE ="SELECT {w.PK} FROM {Webinar as w} where {w.code}=?webinarCode";

	private static final String GET_WEBINAR_PRESENTER_DETAILS_BY_CODE="SELECT {p.PK} FROM  {Presenter as p} where {p.code}=?presenterCode";

	private static final String GET_RELATED_WEBINARS_QUERY="SELECT distinct {w.pk}  FROM {Webinar as w join Webinar2WebinarCategoryTag as rel ON {rel:source}={w. pk} join WebinarCategoryTag as wc ON {rel. target}={wc. pk}} where {w.code}!=?webinarCode and   {wc.pk}";

	private static final String GET_RELATED_WEBINARS_FOR_THE_PRESENTER="SELECT distinct {w.pk} from {Webinar as w join Webinar2Presenter as rel on {rel.source}={w.pk} join Presenter as p on {rel.target}={p.pk}} where {p.code}=?presenterCode";

   private static final String GET_ALL_THE_PRESENTERS= "SELECT {p.pk} from {Presenter as p} ORDER BY RAND()";

   private static final String GET_CATEGORY_BY_CATEGORY_URL="SELECT {c.pk} from {Category as c } where {c.categoryUrl}=?categoryUrl";

   private static final String GET_BRAND_BY_BRAND_URL="SELECT {wb.pk} from {WholescriptsBrand as wb } where {wb.brandUrl}=?brandUrl";

	protected static final String GET_FAQ_LIST_BY_UID = "SELECT {" + WholescriptsFaqListModel.PK + "} FROM {" + WholescriptsFaqListModel._TYPECODE + "} WHERE {" + WholescriptsFaqListModel.UID + "}=?uid";
	private static final String GET_CUSTOMERINFO_BY_ACCOUNT_BLUE_LABEL="SELECT {cm.pk} from {CustomerMaterialInformation as cm } where {cm.account}=?account and {cm.blueLabel}=?blueLabel";
	private static final String GET_AUTOSHIP_MODEL_BY_CODE="SELECT {a.pk} from {SupportedAutoShip as a } where {a.code}=?code";

	private static final String GET_BRIGHT_STOCK_MODEL_PCODE="SELECT {bs.pk} from {WholeScriptsBrightStockProducts as bs } where {bs.productCode}=?productCode";

	private static final String WHOLESCRIPTS_POPULAR_PRODUCTS = "SELECT {wpp.PK} FROM {WsPopularProducts AS wpp}, {Product AS p}, {CatalogVersion AS cv}, {sapmaterialgroupenum AS smg} WHERE {wpp.productcode} = {p.code} AND {p.catalogVersion} = {cv.pk} AND {p.sapMaterialGroup} = {smg.pk} AND {cv.pk} = ?catalogVersion AND {smg.code} IN (?sapMaterialGroups) ORDER BY {wpp.weight} DESC";

	private static final String WHOLESCRIPTS_PRODUCTS_FROM_PRACTITIONER_ORDERS = "SELECT DISTINCT {p.pk} FROM {Order AS o JOIN OrderEntry AS e ON {o.pk} = {e.order} JOIN Product AS p ON {e.product} = {p.pk}} WHERE {o.unit} = ?b2bUnit AND {p.sapMaterialGroup} IN ({{select {pk} from {sapmaterialgroupenum} where {code} in (?sapMaterialGroups)}})";

	private final static String PRODUCT_MATERIAL_CODES = "product.material.group.codes";

	private final static String COMMA = ",";

	private final static String PRODUCTS_BY_SKUS_FOR_MATERIAL_CODES = "SELECT {p.pk} FROM {Product AS p JOIN CatalogVersion AS cv ON {p.catalogVersion} = {cv.pk}} WHERE {cv.pk} = ?catalogVersion AND {p.code} in (?skus)   AND {p.sapMaterialGroup} IN ({{SELECT {pk} FROM {sapmaterialgroupenum} WHERE {code} IN (?materialCodes)}})";

	private final static String ALL_PRODUCTS_FOR_MATERIAL_CODES = "SELECT {p.pk} FROM {Product AS p JOIN CatalogVersion AS cv ON {p.catalogVersion} = {cv.pk}} WHERE {cv.pk} = ?catalogVersion  AND {p.sapMaterialGroup} IN ({{SELECT {pk} FROM {sapmaterialgroupenum} WHERE {code} IN (?materialCodes)}})";

	private final static String GET_B2BUNIT_BY_PHONENUMBER = "SELECT DISTINCT {bu.pk} FROM { Address AS add JOIN B2BUnit AS bu ON {add.owner} = {bu.PK} JOIN SapCustomerGroupEnum AS cg ON {cg.pk} = {bu.sapcustomergroup}} WHERE {add.Phone1} = ?phoneNumber and {cg.code} = ?sapCustomerGroup";

	private final static String KLAVIYO_ORDERS = "SELECT {pk} FROM {Order} WHERE {creationtime} >= ?tenMinutesAgo";

	private final static String GET_WATCHSTATUS_BY_WEBINARCODE = "SELECT {pk}  FROM  {WatchTracker} where {webinarCode}=?webinarCode";

	private final static String GET_WATCHT_RACKER_DETAILS = "SELECT DISTINCT{pk}  FROM  {WatchTracker} where {userId}=?userId and {webinarCode}=?webinarCode and {isVideoWatched}=?isVideoWatched" ;

	private final static String GET_WATCHTRACKER_BY_USERID="SELECT {pk}  FROM  {WatchTracker} where {userId}=?userId";

	private static final Logger LOG = LoggerFactory.getLogger(WholescriptsCoreDaoImpl.class);
	
	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;
	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "productService")
	private ProductService productService;
	@Resource(name = "paginatedFlexibleSearchService")
	private PaginatedFlexibleSearchService paginatedFlexibleSearchService;
	@Resource(name = "catalogVersionService")
	protected CatalogVersionService catalogVersionService;
	@Resource(name = "searchRestrictionService")
	private SearchRestrictionService searchRestrictionService;
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;
	@Resource(name = "b2bCustomerService")
	private B2BCustomerService<B2BCustomerModel, B2BUnitModel> b2bCustomerService;


	@Override
	public List<ItemTimeModel> getItemTimeByCode(final String itemTimeCode) {
		final StringBuilder queryBuilder = new StringBuilder();
		final Map<String, Object> queryParams = new HashMap<>();
		queryBuilder.append(FIND_ITEMTIME_QUERY);
		queryParams.put(ItemTimeModel.CODE, itemTimeCode);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryBuilder);
		query.addQueryParameters(queryParams);
		final SearchResult<ItemTimeModel> result = flexibleSearchService.search(query);
		if (Objects.nonNull(result) && CollectionUtils.isNotEmpty(result.getResult()))
		{
			return List.of(result.getResult().get(0));
		}
		return Collections.emptyList();
	}


	@Override
	public SearchPageData<ProductModel> getDiscontinuedProducts(final SearchPageData pageData)
	{
		final PaginatedFlexibleSearchParameter parameter = new PaginatedFlexibleSearchParameter();
		final StringBuilder queryBuilder = new StringBuilder();
		final Map<String, Object> queryParameters = new HashMap();
		queryBuilder.append(FIND_DISCONTINUED_PRODUCTS_QUERY);
		//queryParameters.put(ProductModel.APPROVALSTATUS, ArticleApprovalStatus.DISCONTINUED);
		queryParameters.put(ProductModel.CATALOGVERSION, getCatalogVersion());
		queryParameters.put("discontinuedDate", DateTime.now().minusMonths(3).toDate());
		final FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(queryBuilder.toString(), queryParameters);
		parameter.setFlexibleSearchQuery(fsQuery);
		parameter.setSearchPageData(pageData);
		final Map<String, String> sortCodeToQueryAlias = new HashMap();
		sortCodeToQueryAlias.put(ProductModel.NAME.toLowerCase(Locale.getDefault()), "p");
		parameter.setSortCodeToQueryAlias(sortCodeToQueryAlias);
		searchRestrictionService.disableSearchRestrictions();
		final SearchPageData<ProductModel> result = paginatedFlexibleSearchService.search(parameter);
		searchRestrictionService.enableSearchRestrictions();
		return result;
	}

	@Override
	public WsDiscontinuedProductsModel getDiscontinuedProductBySku(final String sku)
	{
		final String query = "SELECT {p.pk} FROM {WsDiscontinuedProducts AS p} WHERE {p.discontinuedProductsSku} = ?sku";
		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(query);
		flexibleSearchQuery.addQueryParameter("sku", sku);

		final List<WsDiscontinuedProductsModel> result = flexibleSearchService.<WsDiscontinuedProductsModel>search(flexibleSearchQuery).getResult();
		return result.isEmpty() ? null : result.get(0);
	}


	public SearchPageData<ProductModel> getAvailableWhileSuppliesLastProducts(final SearchPageData pageData) {
		final PaginatedFlexibleSearchParameter parameter = new PaginatedFlexibleSearchParameter();

		final Map<String, Object> queryParameters = new HashMap<>();
		queryParameters.put("catalogVersion", getCatalogVersion());

		final FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(FIND_AWSL_PRODUCTS_QUERY, queryParameters);
		parameter.setFlexibleSearchQuery(fsQuery);
		parameter.setSearchPageData(pageData);

		// Configure sorting
		final Map<String, String> sortCodeToQueryAlias = new HashMap<>();
		sortCodeToQueryAlias.put(ProductModel.NAME.toLowerCase(Locale.getDefault()), "p");
		parameter.setSortCodeToQueryAlias(sortCodeToQueryAlias);

		searchRestrictionService.disableSearchRestrictions();
		final SearchPageData<ProductModel> result = paginatedFlexibleSearchService.search(parameter);
		searchRestrictionService.enableSearchRestrictions();

		return result;
	}

	private CatalogVersionModel getCatalogVersion() {
		return catalogVersionService.getCatalogVersion(
				configurationService.getConfiguration().getString("wholescripts.product.catalog"),
				configurationService.getConfiguration().getString("wholescripts.product.catalog.version"));
	}


	@Override
	public List<ItemTimeModel> getAllActiveItemTimes() {
		try{
			final Map<String, Object> queryParams = new HashMap<>();
			queryParams.put(ItemTimeModel.ISDISPLAYABLE, 1);
			final FlexibleSearchQuery query= new FlexibleSearchQuery(GET_ALL_ACTIVE_ITEM_TIMES);
			query.addQueryParameters(queryParams);
			final SearchResult<ItemTimeModel> itemTimeResults = flexibleSearchService.search(query);
			return itemTimeResults.getResult();
		} catch (final Exception e){
			return Collections.emptyList();
		}
	}

	@Override
	public List<WholescriptsTopicModel> getAllTopicDetails(final String page, final String childTopic)
	{
		try
		{
			final Map<String, Object> queryParams = new HashMap<>();
			queryParams.put(WholescriptsTopicModel.PAGE, page);
			queryParams.put(WholescriptsTopicModel.CHILDTOPIC, childTopic);
			final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_TOPIC_LIST);
			query.addQueryParameters(queryParams);
			final SearchResult<WholescriptsTopicModel> topicResults = flexibleSearchService.search(query);
			return topicResults.getResult();
		}
		catch (final Exception e)
		{
			return Collections.emptyList();
		}
	}

	@Override
	public List<DosingModel> getAllDosingByProductnUser(final String productID, final String userID) {
		final StringBuilder queryBuilder = new StringBuilder();
		final Map<String, Object> queryParams = new HashMap<String, Object>();
		if(StringUtils.isNotEmpty(productID)) {
			queryBuilder.append(FIND_DosingByProductnUser_QUERY);
			queryParams.put("productPK", productService.getProductForCode(productID));
		}else {
			queryBuilder.append(FIND_DosingBy_User_QUERY);
		}
		queryParams.put("b2bCustomerPk", userService.getUserForUID(userID));
		queryParams.put(DosingModel.ISSAVEDASCUSTOM, 1);
		try {
			final SearchResult<DosingModel> result = flexibleSearchService.search(queryBuilder.toString(), queryParams);
			return result.getResult();
		} catch (final ModelNotFoundException e) {
			return Collections.emptyList();
		}
	}

	@Override
	public List<WholescriptsFaqListModel> getFaqQusAnsDetails()
	{
		final StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(GET_FAQ_QUESTION_LIST);
		final Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put(WholescriptsFaqListModel.ISCHILD, 0);
		try {
			final SearchResult<WholescriptsFaqListModel> result = flexibleSearchService.search(queryBuilder.toString(),queryParams);
			return result.getResult();
		} catch (final ModelNotFoundException e) {
			return Collections.emptyList();
		}
	}

	@Override
	public WholescriptsFaqListModel getFaqListByUid(final String uid) {
		final Map<String, Object> queryParams = new HashMap<>();
		queryParams.put(WholescriptsFaqListModel.UID, uid);

		try {
			final SearchResult<WholescriptsFaqListModel> result = flexibleSearchService.search(GET_FAQ_LIST_BY_UID, queryParams);
			return result.getResult().stream().findFirst().orElse(null);
		} catch (final ModelNotFoundException e) {
			return null;
		}
	}

	@Override
	public WholescriptsBrandModel getBrandForCode(final String brandCode)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(WHOLESCRIPTS_BRAND_QUERY);
		query.addQueryParameter("code", brandCode);
		final SearchResult<WholescriptsBrandModel> result = flexibleSearchService.search(query);
		return result.getResult().isEmpty() ? null : result.getResult().get(0);
	}

	@Override
	public List<WholescriptsBrandModel> getTopBrands()
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(WHOLESCRIPTS_TOP_BRANDS_QUERY);
		final SearchResult<WholescriptsBrandModel> result = flexibleSearchService.search(query);
		return result.getResult().isEmpty() ? Collections.emptyList() : result.getResult();
	}

	@Override
	public WebinarModel getfeaturedWebinar()
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_FEATURED_WEBINAR);
		query.addQueryParameter("isFeatured", Boolean.TRUE);
		final SearchResult<WebinarModel> result = flexibleSearchService.search(query);
		return result.getResult().isEmpty() ? null : result.getResult().get(0);
	}

	@Override
	public WebinarModel getWebinarDetails(String webinarCode) {
		final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_WEBINAR_DETAILS_BY_CODE);
		query.addQueryParameter("webinarCode", webinarCode);
		final SearchResult<WebinarModel> result = flexibleSearchService.search(query);
		return result.getResult().isEmpty() ? null : result.getResult().get(0);
	}

	@Override
	public PresenterModel getPresenterDetails(String presenterCode) {
		final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_WEBINAR_PRESENTER_DETAILS_BY_CODE);
		query.addQueryParameter("presenterCode", presenterCode);
		final SearchResult<PresenterModel> result = flexibleSearchService.search(query);
		return result.getResult().isEmpty() ? null : result.getResult().get(0);
	}

	@Override
	public List<WebinarModel> getRelatedWebinarsByWebCategory(List<WebinarCategoryTagModel> webCategorys, String webinarCode) {
		final StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(GET_RELATED_WEBINARS_QUERY);
		final Map<String, Object> queryParams = new HashMap<String, Object>();
		queryBuilder.append(" IN ( ?webCategorys ) ");
		final List<PK> webCategoryCodes = new ArrayList<PK>();
		for (final WebinarCategoryTagModel wcCode : webCategorys)
		{
			if(!webCategoryCodes.contains(wcCode.getPk()))
			{
				webCategoryCodes.add(wcCode.getPk());
			}

		}
		queryParams.put("webinarCode", webinarCode);
		queryParams.put("webCategorys", webCategoryCodes);
		try {
			final SearchResult<WebinarModel> result = flexibleSearchService.search(queryBuilder.toString(), queryParams);
			return result.getResult();
		} catch (final ModelNotFoundException e) {
			return Collections.emptyList();
		}
	}

	@Override
	public List<WebinarModel> getRelatedWebinarsByPresenter(String presenterCode) {

		final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_RELATED_WEBINARS_FOR_THE_PRESENTER);
		query.addQueryParameter("presenterCode", presenterCode);
		final SearchResult<WebinarModel> result = flexibleSearchService.search(query);
		return result.getResult();
	}

	@Override
	public List<PresenterModel> getPresenterDetails() {
		final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_ALL_THE_PRESENTERS);
		query.setCount(8);
		final SearchResult<PresenterModel> result = flexibleSearchService.search(query);
		return result.getResult();
	}

	@Override
	public CategoryModel getCategoryByCategoryUrl(String categoryUrl) {
		final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_CATEGORY_BY_CATEGORY_URL);
		query.addQueryParameter("categoryUrl", categoryUrl);
		final SearchResult<CategoryModel> result = flexibleSearchService.search(query);
		return result.getResult().isEmpty() ? null : result.getResult().get(0);
	}

	@Override
	public WholescriptsBrandModel getBrandCodeByBrandUrl(String brandUrl) {
		final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_BRAND_BY_BRAND_URL);
		query.addQueryParameter("brandUrl", brandUrl);
		LOG.info("WholescriptsCoreDaoImpl, getBrandCodeByBrandUrl " + query);
		final SearchResult<WholescriptsBrandModel> result = flexibleSearchService.search(query);
		LOG.info("WholescriptsCoreDaoImpl, getBrandCodeByBrandUrl result " + result);
		if (Objects.isNull(result) || result.getTotalCount() ==0) {
			throw new UnknownIdentifierException("Brand is not found with url " + brandUrl);
		}
		return result.getResult().isEmpty() ? null : result.getResult().get(0);
	}

	@Override
	public CustomerMaterialInformationModel getCustomerInfoModelForAccountAndBlueLabel(String productCode,final String account)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_CUSTOMERINFO_BY_ACCOUNT_BLUE_LABEL);
		query.addQueryParameter(CustomerMaterialInformationModel.BLUELABEL, productCode);
		query.addQueryParameter(CustomerMaterialInformationModel.ACCOUNT, account);
		final SearchResult<CustomerMaterialInformationModel> result = flexibleSearchService.search(query);
		return result.getResult().isEmpty() ? null : result.getResult().get(0);
	}

	@Override
	public SupportedAutoShipModel getAutoShipByCode(String code) {
		final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_AUTOSHIP_MODEL_BY_CODE);
		query.addQueryParameter("code", code);
		final SearchResult<SupportedAutoShipModel> result = flexibleSearchService.search(query);
		return result.getResult().isEmpty() ? null : result.getResult().get(0);
	}

	@Override
	public WholeScriptsBrightStockProductsModel getBrightStockForPLProduct(String productCode) {
		final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_BRIGHT_STOCK_MODEL_PCODE);
		query.addQueryParameter("productCode", productCode);
		final SearchResult<WholeScriptsBrightStockProductsModel> result = flexibleSearchService.search(query);
		return result.getResult().isEmpty() ? null : result.getResult().get(0);
	}
	@Override
	public WholescriptsBrandModel getBrandByName(final String name)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(WHOLESCRIPTS_BRAND_QUERY_BY_NAME);
		query.addQueryParameter("name", name);
		final SearchResult<WholescriptsBrandModel> result = flexibleSearchService.search(query);
		return result.getResult().isEmpty() ? null : result.getResult().get(0);
	}


	@Override
	public List<WsPopularProductsModel> getPopularProducts(Integer limit, CatalogVersionModel catalogVersion) {
		String materialCodes = configurationService.getConfiguration().getString(PRODUCT_MATERIAL_CODES);
		String[] materialCodesArray = StringUtils.split(materialCodes, COMMA);
		final FlexibleSearchQuery query = new FlexibleSearchQuery(WHOLESCRIPTS_POPULAR_PRODUCTS);
		query.addQueryParameter("catalogVersion", catalogVersion.getPk());
		query.addQueryParameter("sapMaterialGroups", Arrays.stream(materialCodesArray).toList());
		query.setCount(limit);
		final SearchResult<WsPopularProductsModel> result = flexibleSearchService.search(query);
		return result.getResult().isEmpty() ? null : result.getResult();
	}

	@Override
	public List<ProductModel> getProductsFromPractitionerOrders(Integer limit) {
		String materialCodes = configurationService.getConfiguration().getString(PRODUCT_MATERIAL_CODES);
		String[] materialCodesArray = StringUtils.split(materialCodes, COMMA);
		B2BCustomerModel customer = b2bCustomerService.getCurrentB2BCustomer();
		B2BUnitModel b2bunit = customer.getDefaultB2BUnit();
		final FlexibleSearchQuery query = new FlexibleSearchQuery(WHOLESCRIPTS_PRODUCTS_FROM_PRACTITIONER_ORDERS);
		query.addQueryParameter("b2bUnit", b2bunit);
		query.addQueryParameter("sapMaterialGroups", Arrays.stream(materialCodesArray).toList());
		query.setCount(limit);
		final SearchResult<ProductModel> result = flexibleSearchService.search(query);
		return result.getResult().isEmpty() ? null : result.getResult();
	}

	public List<ProductModel> getSelectedProductsDataForKlaviyo(final List<String> productSkusList, CatalogVersionModel catalogVersion) {
		String materialCodes = configurationService.getConfiguration().getString(PRODUCT_MATERIAL_CODES);
		String[] materialCodesArray = StringUtils.split(materialCodes, COMMA);
		FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(PRODUCTS_BY_SKUS_FOR_MATERIAL_CODES);
		searchQuery.addQueryParameter("catalogVersion", catalogVersion.getPk());
		searchQuery.addQueryParameter("skus", productSkusList);
		searchQuery.addQueryParameter("materialCodes", Arrays.stream(materialCodesArray).toList());

		SearchResult<ProductModel> searchResult = flexibleSearchService.search(searchQuery);
		return searchResult.getResult().isEmpty() ? Collections.emptyList() : searchResult.getResult();
	}

	@Override
	public List<ProductModel> getAllProductsForKlaviyo(final CatalogVersionModel catalogVersion) {
		String materialCodes = configurationService.getConfiguration().getString(PRODUCT_MATERIAL_CODES);
		String[] materialCodesArray = StringUtils.split(materialCodes, COMMA);
		FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(ALL_PRODUCTS_FOR_MATERIAL_CODES);
		searchQuery.addQueryParameter("catalogVersion", catalogVersion.getPk());
		searchQuery.addQueryParameter("materialCodes", Arrays.stream(materialCodesArray).toList());

		SearchResult<ProductModel> searchResult = flexibleSearchService.search(searchQuery);
		return searchResult.getResult().isEmpty() ? Collections.emptyList() : searchResult.getResult();
	}

	@Override
	public B2BUnitModel getB2BUnitByPhoneNumber(String phoneNumber) {
		final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_B2BUNIT_BY_PHONENUMBER);
		query.addQueryParameter("phoneNumber", phoneNumber);
		query.addQueryParameter("sapCustomerGroup", "02");
		final SearchResult<B2BUnitModel> result = flexibleSearchService.search(query);
		return result.getResult().isEmpty() ? null : result.getResult().get(0);
	}

	@Override
	public List<WatchTrackerModel> getWatchTrackerDetails(String webinarCode) {
		final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_WATCHSTATUS_BY_WEBINARCODE);
		query.addQueryParameter("webinarCode", webinarCode);
		final SearchResult<WatchTrackerModel> result = flexibleSearchService.search(query);
		return result.getResult().isEmpty() ?  Collections.emptyList() : result.getResult();
	}

	@Override
	public WatchTrackerModel getWatchTrackerModel(String userId, String webinarCode) {
		final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_WATCHT_RACKER_DETAILS);
		query.addQueryParameter("userId", userId);
		query.addQueryParameter("webinarCode", webinarCode);
		query.addQueryParameter("isVideoWatched", Boolean.TRUE);
		final SearchResult<WatchTrackerModel> result = flexibleSearchService.search(query);
		return result.getResult().isEmpty() ? null : result.getResult().get(0);
	}

	@Override
	public List<WatchTrackerModel> getWebinarsBasedOnStatus(String userId, boolean status) {
		final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_WATCHTRACKER_BY_USERID);
		query.addQueryParameter("userId", userId);
		query.addQueryParameter("isVideoWatched", status);
		final SearchResult<WatchTrackerModel> result = flexibleSearchService.search(query);
		return result.getResult().isEmpty() ?  Collections.emptyList() : result.getResult();
	}

	@Override
	public List<OrderModel> getOrdersForKlaviyo() {
		final FlexibleSearchQuery query = new FlexibleSearchQuery(KLAVIYO_ORDERS);

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, -10);
		Date tenMinutesAgo = calendar.getTime();
		query.addQueryParameter("tenMinutesAgo", tenMinutesAgo);

		final SearchResult<OrderModel> result = flexibleSearchService.search(query);
		return result.getResult().isEmpty() ? Collections.emptyList() : result.getResult();
	}
}
