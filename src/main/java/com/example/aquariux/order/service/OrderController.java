package com.example.aquariux.order.service;

import com.example.aquariux.order.actions.GetOrderAction;
import com.example.aquariux.order.actions.ProcessOrderRequestAction;
import com.example.aquariux.order.models.requests.CreateOrderRequest;
import com.example.aquariux.order.models.responses.CreateOrderResponse;
import com.example.aquariux.order.models.responses.OrderResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/orders")
public class OrderController {
    private final ProcessOrderRequestAction processOrderRequestAction;
    private final GetOrderAction getOrderAction;

    public OrderController(ProcessOrderRequestAction processOrderRequestAction,
                           GetOrderAction getOrderAction) {
        this.processOrderRequestAction = processOrderRequestAction;
        this.getOrderAction = getOrderAction;
    }

    @PostMapping(produces = "application/json")
    public CreateOrderResponse postOrderRequest(@RequestBody CreateOrderRequest createOrderRequest, @RequestHeader("USER_ACCOUNT_ID") String userAccountId) {
        return processOrderRequestAction.processOrder(createOrderRequest, Long.parseLong(userAccountId));
    }

    @GetMapping(produces = "application/json")
    public List<OrderResponse> getOrders(@RequestHeader("USER_ACCOUNT_ID") String userAccountId) {
        return getOrderAction.getAllOrders(Long.parseLong(userAccountId));
    }

    @GetMapping(value = "/{orderId}", produces = "application/json")
    public OrderResponse getOrderById(@PathVariable String orderId, @RequestHeader("USER_ACCOUNT_ID") String userAccountId) {
        return getOrderAction.getOrderById(orderId, userAccountId);
    }
}
