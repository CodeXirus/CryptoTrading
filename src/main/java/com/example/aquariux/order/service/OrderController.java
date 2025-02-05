package com.example.aquariux.order.service;

import com.example.aquariux.exception.InvalidRequestException;
import com.example.aquariux.order.actions.GetOrderAction;
import com.example.aquariux.order.actions.ProcessOrderRequestAction;
import com.example.aquariux.order.models.requests.CreateOrderRequest;
import com.example.aquariux.order.models.responses.OrderResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> postOrderRequest(@RequestBody CreateOrderRequest createOrderRequest, @RequestHeader("USER_ACCOUNT_ID") String userAccountId) {
        try {
            return new ResponseEntity<>(processOrderRequestAction.processOrder(createOrderRequest, Long.parseLong(userAccountId)), HttpStatus.OK);
        } catch (InvalidRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
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
