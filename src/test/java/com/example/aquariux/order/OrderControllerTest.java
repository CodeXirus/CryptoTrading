package com.example.aquariux.order.service;

import com.example.aquariux.core.repositories.MarketRepository;
import com.example.aquariux.core.service.TradingCore;
import com.example.aquariux.order.actions.GetOrderAction;
import com.example.aquariux.order.actions.ProcessOrderRequestAction;
import com.example.aquariux.order.models.requests.CreateOrderRequest;
import com.example.aquariux.order.models.responses.CreateOrderResponse;
import com.example.aquariux.order.models.responses.OrderResponse;
import com.example.aquariux.testconfig.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestConfig.class)
@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProcessOrderRequestAction processOrderRequestAction;

    @MockBean
    private GetOrderAction getOrderAction;

    @MockBean
    private TradingCore tradingCore;

    @MockBean
    private MarketRepository marketRepository;

    private CreateOrderResponse createOrderResponse;
    private OrderResponse orderResponse;

    @BeforeEach
    void setUp() {
        createOrderResponse = new CreateOrderResponse();
        orderResponse = new OrderResponse();

        when(processOrderRequestAction.processOrder(any(CreateOrderRequest.class), anyLong()))
                .thenReturn(createOrderResponse);

        when(getOrderAction.getAllOrders(anyLong()))
                .thenReturn(Collections.singletonList(orderResponse));

        when(getOrderAction.getOrderById(anyString(), anyString()))
                .thenReturn(orderResponse);
    }

    @Test
    void test_post_order_request_endpoint() throws Exception {
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("USER_ACCOUNT_ID", "1")
                        .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    void test_get_orders_request_endpoint() throws Exception {
        mockMvc.perform(get("/api/orders")
                        .header("USER_ACCOUNT_ID", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void test_get_order_by_id_request_endpoint() throws Exception {
        mockMvc.perform(get("/api/orders/123")
                        .header("USER_ACCOUNT_ID", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
