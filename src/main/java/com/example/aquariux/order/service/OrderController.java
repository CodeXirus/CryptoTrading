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

    /*
        Create order request.
        RequestHeader required: USER_ACCOUNT_ID:String
        Usually we will derive the user account id from the SessionData or JWT Token.
        However, we are assuming that the user is already authenticated, hence I am
        using this header (USER_ACCOUNT_ID) as a replacement.
     */
    @PostMapping(produces = "application/json")
    public ResponseEntity<?> createOrderRequest(@RequestBody CreateOrderRequest createOrderRequest, @RequestHeader("USER_ACCOUNT_ID") String userAccountId) {
        try {
            return new ResponseEntity<>(processOrderRequestAction.processOrder(createOrderRequest, Long.parseLong(userAccountId)), HttpStatus.OK);
        } catch (InvalidRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /*
        Get all orders belonging to user
        RequestHeader required: USER_ACCOUNT_ID:String
     */
    @GetMapping(produces = "application/json")
    public List<OrderResponse> getOrders(@RequestHeader("USER_ACCOUNT_ID") String userAccountId) {
        return getOrderAction.getAllOrders(Long.parseLong(userAccountId));
    }

    /*
        Get order by order id
        Path Parameter: String orderId ["123"]
        RequestHeader required: USER_ACCOUNT_ID:String
     */
    @GetMapping(value = "/{orderId}", produces = "application/json")
    public OrderResponse getOrderById(@PathVariable String orderId, @RequestHeader("USER_ACCOUNT_ID") String userAccountId) {
        return getOrderAction.getOrderById(orderId, userAccountId);
    }
}
