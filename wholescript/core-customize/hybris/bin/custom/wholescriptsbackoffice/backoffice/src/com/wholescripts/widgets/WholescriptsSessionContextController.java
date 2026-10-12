package com.wholescripts.widgets;

import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent.Level;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.components.Stopwatch;
import com.hybris.cockpitng.util.CockpitSessionService;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.notifications.DefaultNotificationService;
import com.wholescripts.model.SessionContextModel;
import com.wholescripts.services.AsmLaunchJwtService;
import com.wholescripts.util.AsmUtils;
import com.wholescripts.util.SessionContextUtil;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.util.AbstractComparator;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.ticket.model.CsTicketModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

public class WholescriptsSessionContextController extends DefaultWidgetController {
    protected static final String USER_ANCHOR_COMPONENT_ID = "userAnchor";
    protected static final String TICKET_ANCHOR_COMPONENT_ID = "ticketAnchor";
    protected static final String ORDER_ANCHOR_COMPONENT_ID = "orderAnchor";
    protected static final String END_SESSION_BUTTON_COMPONENT_ID = "endSessionBtn";
    protected static final String SESSION_CALL_BUTTON_COMPONENT_ID = "callContextBtn";
    protected static final String ASM_LAUNCH_BUTTON_COMPONENT_ID = "asmBtn";
    protected static final String START_CALL_LABEL_KEY = "sessionContext.call.button.start";
    protected static final String END_CALL_LABEL_KEY = "sessionContext.call.button.end";
    protected static final String CURRENT_SESSION_CALL_MODE = "enabled.call.mode";
    protected static final String IN_CALL_MODE = "in.call";
    protected static final String FREE_CALL_MODE = "free";
    protected static final String START_CALL_CSS_CLASS = "y-start-call-btn";
    protected static final String END_CALL_CSS_CLASS = "y-end-call-btn";
    protected static final String ANCHOR_CSS_CLASS = "y-session-context-label-value";
    protected static final String ANCHOR_CSS_COLOR_CLASS = "y-session-context-label-value-color";
    protected final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("mm 'min' ':' ss 'sec'");
    protected A userAnchor;
    protected A ticketAnchor;
    protected A orderAnchor;
    protected Label cartAnchor;
    protected Button endSessionBtn;
    protected Button asmBtn;
    protected Button callContextBtn;
    protected Label customerPlaceholder;
    protected Label ticketPlaceholder;
    protected Label orderPlaceholder;
    protected Label cartPlaceholder;
    protected Image userImage;
    protected Image ticketImage;
    protected Image orderImage;
    protected Combobox availableSites;
    protected Stopwatch stopWatch;
    protected Div sessionTimerDiv;
    @Resource
    protected transient BaseSiteService baseSiteService;
    @Resource
    protected transient CockpitSessionService cockpitSessionService;
    @Resource
    protected transient DefaultNotificationService notificationService;
    @Resource
    public AsmLaunchJwtService asmLaunchJwtService;

    public WholescriptsSessionContextController() {
    }

    public void initialize(Component comp) {
        super.initialize(comp);
        ListModelList<BaseSiteModel> model = this.getAvailableSites();
        if (null != model && model.getSize() > 0) {
            model.addToSelection((BaseSiteModel)model.get(0));
        } else {
            this.asmBtn.setVisible(false);
        }

        this.availableSites.setModel(model);
    }

    @ViewEvent(
            componentID = "userAnchor",
            eventName = "onClick"
    )
    public void getUserDetails() {
        UserModel user = SessionContextUtil.createOrReturnSessionContext(this.getWidgetInstanceManager().getModel()).getCurrentCustomer();
        if (null != user) {
            this.sendOutput("itemToShow", user);
        }

    }

    @ViewEvent(
            componentID = "ticketAnchor",
            eventName = "onClick"
    )
    public void getTicketDetails() {
        CsTicketModel ticket = SessionContextUtil.createOrReturnSessionContext(this.getWidgetInstanceManager().getModel()).getCurrentTicket();
        if (null != ticket) {
            this.sendOutput("itemToShow", ticket);
        }

    }

    @ViewEvent(
            componentID = "orderAnchor",
            eventName = "onClick"
    )
    public void getOrderDetails() {
        AbstractOrderModel order = SessionContextUtil.createOrReturnSessionContext(this.getWidgetInstanceManager().getModel()).getCurrentOrder();
        if (null != order) {
            this.sendOutput("itemToShow", order);
        }

    }

    @SocketEvent(
            socketId = "selectedItem"
    )
    public void itemSelected(Object msg) {
        if (msg instanceof CustomerModel || msg instanceof OrderModel || msg instanceof ReturnRequestModel || msg instanceof CsTicketModel) {
            this.resetToDefault();
            String currentSessionCustomerId = "";
            SessionContextModel currentContextSession = SessionContextUtil.getCurrentSessionContext(this.getWidgetInstanceManager().getModel());
            if (msg instanceof CustomerModel) {
                CustomerModel customer = (CustomerModel)msg;
                currentSessionCustomerId = this.customerSelected(customer, currentContextSession);
            } else if (msg instanceof CsTicketModel) {
                CsTicketModel currentTicket = (CsTicketModel)msg;
                currentSessionCustomerId = this.currentTicketSelected(currentTicket, currentContextSession);
            } else if (msg instanceof OrderModel || msg instanceof ReturnRequestModel) {
                currentSessionCustomerId = this.currentOrderSelected(msg, currentContextSession);
            }

            this.callContextBtn.setAttribute("enabled.call.mode", "free");
            this.handleUIComponents();
            this.handleSessionCallMode();
            this.cockpitSessionService.setAttribute("sessionContextUID", currentSessionCustomerId);
            this.sessionTimerDiv.setVisible(true);
        }
    }

    protected String customerSelected(CustomerModel customer, SessionContextModel currentContextSession) {
        this.applyAnchorValueSelected(this.userAnchor, customer.getName());
        if (null != currentContextSession && customer.equals(currentContextSession.getCurrentCustomer())) {
            AbstractOrderModel order = currentContextSession.getCurrentOrder();
            CsTicketModel ticket = currentContextSession.getCurrentTicket();
            if (order != null) {
                this.applyAnchorValueSelected(this.orderAnchor, order.getCode());
            } else if (ticket != null) {
                this.currentTicketSelected(ticket, currentContextSession);
            }
        } else {
            SessionContextUtil.populateCustomer(this.getWidgetInstanceManager().getModel(), customer);
            this.restartTimer();
        }

        return customer.getUid();
    }

    protected String currentTicketSelected(CsTicketModel currentTicket, SessionContextModel currentContextSession) {
        String currentSessionCustomerId = "";
        UserModel previousCustomer = null;
        if (null != currentContextSession) {
            previousCustomer = currentContextSession.getCurrentCustomer();
        }

        SessionContextUtil.populateTicket(this.getWidgetInstanceManager().getModel(), currentTicket);
        if (null != currentTicket.getOrder()) {
            if (currentTicket.getOrder() instanceof OrderModel) {
                this.applyAnchorValueSelected(this.orderAnchor, currentTicket.getOrder().getCode());
            } else if (currentTicket.getOrder() instanceof CartModel) {
                this.cartAnchor.setValue(currentTicket.getOrder().getCode());
            }
        }

        if (null != currentTicket.getCustomer()) {
            this.applyAnchorValueSelected(this.userAnchor, currentTicket.getCustomer().getName());
            currentSessionCustomerId = currentTicket.getCustomer().getUid();
            if (null == previousCustomer || !previousCustomer.getUid().equals(currentSessionCustomerId)) {
                this.restartTimer();
            }
        }

        this.applyAnchorValueSelected(this.ticketAnchor, currentTicket.getTicketID());
        this.handleSiteUpdate(currentTicket.getBaseSite());
        return currentSessionCustomerId;
    }

    protected String currentOrderSelected(Object msg, SessionContextModel currentContextSession) {
        String currentSessionCustomerId = "";
        UserModel previousCustomer = null;
        OrderModel currentOrder = null;
        if (msg instanceof ReturnRequestModel returnModel) {
            currentOrder = returnModel.getOrder();
        } else {
            currentOrder = (OrderModel)msg;
        }

        if (null != currentContextSession) {
            previousCustomer = currentContextSession.getCurrentCustomer();
        }

        SessionContextUtil.populateOrder(this.getWidgetInstanceManager().getModel(), currentOrder);
        this.applyAnchorValueSelected(this.orderAnchor, currentOrder.getCode());
        this.applyAnchorValueSelected(this.userAnchor, currentOrder.getUser().getName());
        currentSessionCustomerId = currentOrder.getUser().getUid();
        if (null == previousCustomer || !previousCustomer.getUid().equals(currentSessionCustomerId)) {
            this.restartTimer();
        }

        this.handleSiteUpdate(currentOrder.getSite());
        return currentSessionCustomerId;
    }

    @ViewEvent(
            componentID = "endSessionBtn",
            eventName = "onClick"
    )
    public void endCurrentSession() {
        SessionContextUtil.clearSessionContext(this.getWidgetInstanceManager().getModel());
        this.resetToDefault();
        this.handleUIComponents();
        this.sendOutput("itemToShow", (Object)null);
        this.sendOutput("viewData", (Object)null);
        this.stopWatch.stop();
        this.sessionTimerDiv.setVisible(false);
        this.cockpitSessionService.removeAttribute("sessionContextUID");
    }

    @SocketEvent(
            socketId = "itemCreated"
    )
    public void itemCreated(Object msg) {
        if (msg instanceof CsTicketModel || msg instanceof CustomerModel) {
            this.itemSelected(msg);
            this.sendOutput("itemToShow", msg);
        }

    }

    protected void applyAnchorValueSelected(A anchor, String labelValue) {
        anchor.setLabel(labelValue);
        anchor.addSclass("y-session-context-label-value-color");
    }

    protected void applyAnchorValueUnSelected(A anchor) {
        anchor.setLabel((String)null);
        anchor.removeSclass("y-session-context-label-value-color");
    }

    protected void restartTimer() {
        this.stopWatch.reset();
        this.stopWatch.start();
    }

    protected void resetToDefault() {
        this.callContextBtn.removeAttribute("enabled.call.mode");
        this.applyAnchorValueUnSelected(this.userAnchor);
        this.applyAnchorValueUnSelected(this.ticketAnchor);
        this.applyAnchorValueUnSelected(this.orderAnchor);
        this.cartAnchor.setValue((String)null);
        this.availableSites.setDisabled(false);
        if (this.availableSites.getModel().getSize() > 0) {
            this.availableSites.setSelectedIndex(0);
        }

    }

    protected void handleSiteUpdate(BaseSiteModel baseSiteModel) {
        if (baseSiteModel == null) {
            this.availableSites.setDisabled(true);
        } else {
            ListModel<BaseSiteModel> sites = this.availableSites.getModel();

            for(int i = 0; i < sites.getSize(); ++i) {
                if (((BaseSiteModel)sites.getElementAt(i)).getUid().equals(baseSiteModel.getUid())) {
                    this.availableSites.setSelectedIndex(i);
                    this.availableSites.setDisabled(true);
                    break;
                }
            }

        }
    }

    @ViewEvent(
            componentID = "callContextBtn",
            eventName = "onClick"
    )
    public void handleSessionCall() {
        this.handleSessionCallMode();
    }

    @ViewEvent(
            componentID = "asmBtn",
            eventName = "onClick"
    )
    public void launchASM() {
        SessionContextModel currentSessionContext = SessionContextUtil.getCurrentSessionContext(this.getWidgetInstanceManager().getModel());
        BaseSiteModel site = (BaseSiteModel)this.availableSites.getModel().getElementAt(this.availableSites.getSelectedIndex());
        if (currentSessionContext != null && currentSessionContext.getCurrentCustomer() != null) {
            UserModel user = currentSessionContext.getCurrentCustomer();
            if (!AsmUtils.isLoginAllowed(site, user)) {
                this.notificationService.notifyUser("", "JustMessage", Level.FAILURE, new Object[]{Labels.getLabel("customersupportbackoffice.customer.login.not.allowed.message")});
                return;
            }
        }

        Executions.getCurrent().sendRedirect(AsmUtils.getAsmDeepLink((BaseSiteModel)this.availableSites.getModel().getElementAt(this.availableSites.getSelectedIndex()), currentSessionContext, asmLaunchJwtService), "_blank");
    }

    public boolean showASMButton() {
        return AsmUtils.showAsmButton();
    }

    protected void handleUIComponents() {
        SessionContextModel currentSessionContext = SessionContextUtil.getCurrentSessionContext(this.getWidgetInstanceManager().getModel());
        this.endSessionBtn.setVisible(null != currentSessionContext);
        this.callContextBtn.setVisible(null != currentSessionContext);
        this.customerPlaceholder.setVisible(null == currentSessionContext || null == currentSessionContext.getCurrentCustomer());
        this.ticketPlaceholder.setVisible(null == currentSessionContext || null == currentSessionContext.getCurrentTicket());
        this.orderPlaceholder.setVisible(null == currentSessionContext || null == currentSessionContext.getCurrentOrder());
        this.cartPlaceholder.setVisible(null == currentSessionContext || !this.hasCartInSessionContext(currentSessionContext));
    }

    protected boolean hasCartInSessionContext(SessionContextModel sessionContext) {
        if (sessionContext != null && sessionContext.getCurrentTicket() != null) {
            Object associatedObj = sessionContext.getCurrentTicket().getOrder();
            return associatedObj instanceof CartModel;
        } else {
            return false;
        }
    }

    protected void handleSessionCallMode() {
        if (null != this.callContextBtn.getAttribute("enabled.call.mode") && this.callContextBtn.getAttribute("enabled.call.mode").equals("in.call")) {
            this.callContextBtn.setLabel(this.getWidgetInstanceManager().getLabel("sessionContext.call.button.end"));
            this.callContextBtn.setAttribute("enabled.call.mode", "free");
            this.callContextBtn.setSclass("y-end-call-btn");
        } else {
            this.callContextBtn.setLabel(this.getWidgetInstanceManager().getLabel("sessionContext.call.button.start"));
            this.callContextBtn.setAttribute("enabled.call.mode", "in.call");
            this.callContextBtn.setSclass("y-start-call-btn");
        }

    }

    protected ListModelList<BaseSiteModel> getAvailableSites() {
        ListModelList model = new ListModelList();
        Collection<BaseSiteModel> allBaseSites = this.baseSiteService.getAllBaseSites();
        if (CollectionUtils.isNotEmpty(allBaseSites)) {
            List<BaseSiteModel> availableBaseSites = new ArrayList(allBaseSites.size());

            for(BaseSiteModel baseSite : allBaseSites) {
                availableBaseSites.add(baseSite);
            }

            Collections.sort(availableBaseSites, new AbstractComparator<BaseSiteModel>() {
                protected int compareInstances(BaseSiteModel instance1, BaseSiteModel instance2) {
                    return (new AbstractComparator<String>() {
                        protected int compareInstances(String instance1, String instance2) {
                            int result = instance1.compareToIgnoreCase(instance2);
                            return result == 0 ? instance1.compareTo(instance2) : result;
                        }
                    }).compare(instance1.getName(), instance2.getName());
                }
            });
            model.addAll(availableBaseSites);
        }

        return model;
    }
}
