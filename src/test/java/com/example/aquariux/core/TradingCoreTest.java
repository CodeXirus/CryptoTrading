package com.example.aquariux.core;

import com.example.aquariux.core.accountant.WalletAccountant;
import com.example.aquariux.core.models.entities.Order;
import com.example.aquariux.core.models.entities.Trade;
import com.example.aquariux.core.models.markets.MarketTick;
import com.example.aquariux.core.models.markets.SpotMarketTick;
import com.example.aquariux.core.repositories.OrderRepository;
import com.example.aquariux.core.repositories.TradeRepository;
import com.example.aquariux.core.scheduler.MarketTickScheduler;
import com.example.aquariux.core.service.TradingCoreImpl;
import com.example.aquariux.order.models.OrderSide;
import com.example.aquariux.order.models.OrderStatus;
import com.example.aquariux.order.models.responses.CreateOrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TradingCoreTest {

    @Mock
    private MarketTickScheduler marketTickScheduler;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private TradeRepository tradeRepository;

    @Mock
    private WalletAccountant walletAccountant;

    @InjectMocks
    private TradingCoreImpl tradingCore;

    private Order order;
    private MarketTick marketTick;

    @BeforeEach
    void setUp() {
        Map<Long, MarketTick> marketTickMap = new HashMap<>();
        marketTick = SpotMarketTick.builder()
                .marketId(1L)
                .bidPrice(100.0)
                .askPrice(105.0)
                .build();
        marketTickMap.put(1L, marketTick);

        when(marketTickScheduler.getAllMarketTicks()).thenReturn(marketTickMap);
        tradingCore = new TradingCoreImpl(marketTickScheduler, orderRepository, tradeRepository, walletAccountant);

        order = new Order();
        order.setMarketId(1L);
        order.setQuantity(10);
        order.setUserAccountId(123L);
        order.setOrderSide(OrderSide.BUY);
        order.setCreatedAtDatetime(Instant.now());
    }

    @Test
    void test_execute_order() {
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(tradeRepository.save(any(Trade.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CreateOrderResponse response = tradingCore.executeOrder(order);

        assertNotNull(response);
        assertEquals(OrderStatus.EXECUTED.name(), response.getOrderStatus());
        verify(walletAccountant, times(1)).checkWalletBalance(order, marketTick);
        verify(walletAccountant, times(1)).updateWalletBalance(any(Trade.class), eq(marketTick));
        verify(orderRepository, times(1)).save(order);
        verify(tradeRepository, times(1)).save(any(Trade.class));
    }

    @Test
    void test_execute_order_insufficient_balance_throws_exception() {
        doThrow(new RuntimeException("Insufficient funds"))
                .when(walletAccountant).checkWalletBalance(any(Order.class), any(MarketTick.class));

        Exception exception = assertThrows(RuntimeException.class, () -> tradingCore.executeOrder(order));
        assertEquals("Insufficient funds", exception.getMessage());
        verify(walletAccountant, times(1)).checkWalletBalance(order, marketTick);
        verify(orderRepository, never()).save(any(Order.class));
        verify(tradeRepository, never()).save(any(Trade.class));
    }
}
