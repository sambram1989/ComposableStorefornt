package com.wholescripts.util;

import com.wholescripts.model.SessionContextModel;
import com.wholescripts.services.AsmLaunchJwtService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.util.Config;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

public class AsmUtils {
    private static final Logger LOG = LogManager.getLogger(AsmUtils.class);
    public static final String ASM_DEEPLINK_SHOW_PARAM = "cscokpit.assistedservice.deeplink";
    public static final String ASM_DEEPLINK_PARAM = "assistedservicestorefront.deeplink.link";
    public static final String SAVED_CART = "saved";
    public static final String ACTIVE_CART = "active";
    public static final String INACTIVE_CART = "inactive";
    public static final String UNKNOWN_CART = "unknown";
    public static final String CART_TYPE_CODE = "cart";
    public static final String ORDER_TYPE_CODE = "order";

    private AsmUtils() {
    }

    public static boolean showAsmButton() {
        return Config.getBoolean("cscokpit.assistedservice.deeplink", false);
    }

    public static String getAsmDeepLink(BaseSiteModel currentSite, SessionContextModel sessionContext, AsmLaunchJwtService asmLaunchJwtService) {
        String siteURL = Config.getString("website." + currentSite.getUid() + ".https", "");
        String deepLinkPara = Config.getString("assistedservicestorefront.deeplink.link", "/assisted-service/emulate");
        return composeDeeplink(siteURL, deepLinkPara, sessionContext, asmLaunchJwtService);
    }

    protected static String composeDeeplink(String siteUrl, String deepLinkPara, SessionContextModel sessionContext, AsmLaunchJwtService asmLaunchJwtService) {
        String jwtToken = asmLaunchJwtService.generateJwtToken();
        StringBuilder deepLink = new StringBuilder();
        deepLink.append(siteUrl);

        if (null != sessionContext && null != sessionContext.getCurrentCustomer()) {
            UserModel user = sessionContext.getCurrentCustomer();
            String customerId = encodeURLString(getCheckedCustomerId(user));
            deepLink.append(deepLinkPara + "?customerId=" + customerId);
            AbstractOrderModel order = getOrderFromContext(sessionContext);
            addTicketInfo(deepLink, sessionContext.getCurrentTicket());
            if (order != null) {
                String typeCode = getTypeCode(order);
                deepLink.append("&").append(typeCode).append("Id=");
                if ("order".equals(typeCode)) {
                    deepLink.append(encodeURLString(order.getGuid()));
                } else {
                    deepLink.append(encodeURLString(order.getCode()));
                }

                appendCartType(deepLink, order, user);
            }
            LOG.info("Deep Link URL : "+ deepLink.toString());
        }

        if(!StringUtils.isEmpty(jwtToken)){
            char separator = deepLink.toString().contains("?") ? '&' : '?';
            deepLink.append(separator).append("token=").append(jwtToken);
        }

        return deepLink.toString();
    }

    public static void addTicketInfo(StringBuilder link, Object obj) {
        if (obj != null && link != null && obj instanceof CsTicketModel ticket) {
            link.append("&ticketId=");
            link.append(encodeURLString(ticket.getTicketID()));
        }

    }

    private static String getTypeCode(Object data) {
        if (data instanceof CartModel) {
            return "cart";
        } else {
            return data instanceof OrderModel ? "order" : null;
        }
    }

    public static void appendCartType(StringBuilder deepLink, AbstractOrderModel order, UserModel currentCustomer) {
        if (deepLink != null && order instanceof CartModel cart) {
            if (currentCustomer != null) {
                String cartType = getCartTypeWithCartAndCustomer(cart, currentCustomer);
                deepLink.append("&cartType=" + cartType);
            }
        }

    }

    private static CartModel getActiveCart(BaseSiteModel currentSite, UserModel user) {
        List<CartModel> activeCarts = user.getCarts().stream().filter((cart) -> isNotSavedOrEmptyCart(cart, currentSite)).sorted(Comparator.comparing(ItemModel::getModifiedtime).reversed()).toList();
        return !CollectionUtils.isEmpty(activeCarts) ? (CartModel)activeCarts.get(0) : null;
    }

    private static String getCartTypeWithCartAndCustomer(CartModel cart, UserModel currentCustomer) {
        if (cart.getSaveTime() != null) {
            return "saved";
        } else {
            AbstractOrderModel activeCart = getActiveCart(cart.getSite(), currentCustomer);
            UserModel cartOwner = cart.getUser();
            if (cart.equals(activeCart)) {
                return "active";
            } else {
                return cartOwner != null && cartOwner.equals(currentCustomer) ? "inactive" : "unknown";
            }
        }
    }

    private static AbstractOrderModel getOrderFromContext(SessionContextModel sessionContext) {
        AbstractOrderModel orderInContext = sessionContext.getCurrentOrder();
        if (orderInContext == null) {
            CsTicketModel ticket = sessionContext.getCurrentTicket();
            if (ticket != null) {
                orderInContext = ticket.getOrder();
            }
        }

        return orderInContext;
    }

    private static boolean isNotSavedOrEmptyCart(CartModel cart, BaseSiteModel currentSite) {
        if (cart.getSite() != null && !CollectionUtils.isEmpty(cart.getEntries())) {
            return cart.getSite().equals(currentSite) && cart.getSaveTime() == null;
        } else {
            return false;
        }
    }

    private static String encodeURLString(String str) {
        return StringUtils.hasLength(str) ? URLEncoder.encode(str, StandardCharsets.UTF_8) : "";
    }

    public static String getCheckedCustomerId(UserModel user) {
        if (user instanceof CustomerModel customer) {
            if (StringUtils.hasLength(customer.getCustomerID())) {
                return customer.getCustomerID();
            }
        }

        return "-";
    }

    public static boolean isLoginAllowed(BaseSiteModel currentSite, UserModel user) {
        if (currentSite != null) {
            if (currentSite.getChannel() != null && SiteChannel.B2B.getCode().equals(currentSite.getChannel().getCode()) && !isB2BUser(user)) {
                return false;
            }

            if (user instanceof CustomerModel) {
                CustomerModel customer = (CustomerModel)user;
                if (customer.getSite() != null && customer.getSite().getUid() != null) {
                    return customer.getSite().getUid().equals(currentSite.getUid());
                }
            }
        }

        return true;
    }

    private static boolean isB2BUser(UserModel user) {
        String className = "de.hybris.platform.b2b.model.B2BCustomerModel";

        try {
            Class<?> cls = Class.forName("de.hybris.platform.b2b.model.B2BCustomerModel");
            if (cls.isAssignableFrom(user.getClass())) {
                return true;
            }
        } catch (ClassNotFoundException var3) {
        }

        return false;
    }
}
