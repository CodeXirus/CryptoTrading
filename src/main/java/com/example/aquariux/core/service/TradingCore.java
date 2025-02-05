package com.example.aquariux.core.service;

import com.example.aquariux.core.models.entities.Order;
import com.example.aquariux.order.models.responses.CreateOrderResponse;

public interface TradingCore {
    CreateOrderResponse executeOrder(Order order);
}
