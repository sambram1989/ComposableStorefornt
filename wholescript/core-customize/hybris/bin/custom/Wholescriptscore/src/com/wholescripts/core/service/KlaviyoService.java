package com.wholescripts.core.service;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import org.springframework.http.ResponseEntity;

public interface KlaviyoService {

    ResponseEntity<String> sendOrderDetails(OrderModel order);

    ResponseEntity<String> sendOrderedProductDetails(OrderModel order, OrderEntryModel entry);
}
