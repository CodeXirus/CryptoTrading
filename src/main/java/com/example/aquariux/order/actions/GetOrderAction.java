package com.example.aquariux.order.actions;

import com.example.aquariux.order.models.responses.OrderResponse;

import java.util.List;

public interface GetOrderAction {

    OrderResponse getOrderById(long orderId, long userAccountId);
    List<OrderResponse> getAllOrders(long userAccountId);
}
