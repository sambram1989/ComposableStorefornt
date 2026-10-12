package com.wholescripts.core.strategies;

import com.wholescripts.core.facade.WholescriptsOrdersFacade;
import com.wholescripts.core.service.WholescriptsIntegrationsService;
import com.wholescripts.data.CreateOrderInSAPResponseData;
import com.wholescripts.data.OrderSimulateResponseData;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.strategies.impl.DefaultB2BCreateOrderFromCartStrategy;
import de.hybris.platform.commercewebservicescommons.errors.exceptions.RequestParameterException;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.strategies.CartValidator;
import de.hybris.platform.order.strategies.ordercloning.CloneAbstractOrderStrategy;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import javax.annotation.Resource;
import java.util.Objects;

import static de.hybris.platform.webservicescommons.util.YSanitizer.sanitize;

public class WholescriptsDefaultB2BCreateOrderFromCartStrategy extends DefaultB2BCreateOrderFromCartStrategy {

    private CartValidator cartValidator;
    private CloneAbstractOrderStrategy cloneAbstractOrderStrategy;

    @Resource(name = "wholescriptsIntegrationsService")
    private WholescriptsIntegrationsService wholescriptsIntegrationsService;

    @Resource(name = "wholescriptsOrdersFacade")
    private WholescriptsOrdersFacade wholescriptsOrdersFacade;

    @Resource(name = "modelService")
    private ModelService modelService;


    @Override
    public OrderModel createOrderFromCart(CartModel cart) throws InvalidCartException{

        if (cartValidator != null)
        {
            cartValidator.validateCart(cart);
        }
        try {
            OrderSimulateResponseData sapResponse=wholescriptsIntegrationsService.orderSimulateORCreate(cart, false);
            if(Objects.nonNull(sapResponse)&& sapResponse.getReturns().equals("0"))
            {
                final OrderModel orderModel = cloneAbstractOrderStrategy.clone(null, null, cart, sapResponse.getOrderNumber(), OrderModel.class,
                        OrderEntryModel.class);
                wholescriptsOrdersFacade.updateProductRankingFromOrders(orderModel);
                if(Objects.nonNull(orderModel.getPaymentInfo()))
                {
                    CreditCardPaymentInfoModel paymentInfo= (CreditCardPaymentInfoModel) orderModel.getPaymentInfo();
                    if(StringUtils.isNotEmpty(paymentInfo.getCvvNumber()))
                    {
                        paymentInfo.setCvvNumber(null);
                        modelService.save(paymentInfo);
                    }
                }
                return orderModel;
            }

        } catch (Exception e) {
            throw new RequestParameterException(String.format(e.getMessage(), sanitize(cart.getCode())),
                    RequestParameterException.INVALID, cart.getCode());
        }

        return new OrderModel();
    }

    @Required
    public void setCartValidator(final CartValidator cartValidator)
    {
        this.cartValidator = cartValidator;
    }

    @Required
    public void setCloneAbstractOrderStrategy(final CloneAbstractOrderStrategy cloneAbstractOrderStrategy)
    {
        this.cloneAbstractOrderStrategy = cloneAbstractOrderStrategy;
    }
}
