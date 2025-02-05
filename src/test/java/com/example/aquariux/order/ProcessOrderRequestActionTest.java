package com.example.aquariux.order;

import com.example.aquariux.core.models.entities.Market;
import com.example.aquariux.core.models.entities.Order;
import com.example.aquariux.core.repositories.MarketRepository;
import com.example.aquariux.core.service.TradingCore;
import com.example.aquariux.order.actions.ProcessOrderRequestActionImpl;
import com.example.aquariux.order.models.requests.CreateOrderRequest;
import com.example.aquariux.order.models.responses.CreateOrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProcessOrderRequestActionTest {

    @InjectMocks
    private ProcessOrderRequestActionImpl processOrderRequestAction;

    @Mock
    private TradingCore tradingCore;

    @Mock
    private MarketRepository marketRepository;

    private CreateOrderRequest createOrderRequest;
    private Market market;
    private Order order;
    private CreateOrderResponse createOrderResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        createOrderRequest = CreateOrderRequest.builder()
                .symbol("BTCUSDT")
                .type("MARKET")
                .side("BUY")
                .quantity("1.5")
                .build();

        market = new Market();
        market.setMarketId(1L);

        order = new Order();
        order.setMarketId(1L);
        order.setUserAccountId(1L);

        createOrderResponse = new CreateOrderResponse();

        when(marketRepository.findBySymbol(any())).thenReturn(Optional.of(market));
        when(tradingCore.executeOrder(any())).thenReturn(createOrderResponse);
    }

    @Test
    void test_able_to_process_order() {
        CreateOrderResponse response = processOrderRequestAction.processOrder(createOrderRequest, 1L);
        assertThat(response).isNotNull();
    }
}
