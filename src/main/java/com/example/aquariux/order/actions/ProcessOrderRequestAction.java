package com.example.aquariux.order.actions;

import com.example.aquariux.order.models.requests.CreateOrderRequest;
import com.example.aquariux.order.models.responses.CreateOrderResponse;

public interface ProcessOrderRequestAction {
    CreateOrderResponse processOrder(CreateOrderRequest createOrderRequest, long userAccountId);
}
