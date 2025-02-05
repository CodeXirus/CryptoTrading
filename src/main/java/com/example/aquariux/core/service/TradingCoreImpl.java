package com.example.aquariux.core.service;

import com.example.aquariux.core.accountant.WalletAccountant;
import com.example.aquariux.core.models.entities.Order;
import com.example.aquariux.core.models.entities.Trade;
import com.example.aquariux.core.models.markets.MarketTick;
import com.example.aquariux.core.repositories.*;
import com.example.aquariux.core.scheduler.MarketTickScheduler;
import com.example.aquariux.order.models.OrderStatus;
import com.example.aquariux.order.models.responses.CreateOrderResponse;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TradingCoreImpl implements TradingCore {
    private Map<Long, MarketTick> marketTickMap;
    private OrderRepository orderRepository;
    private TradeRepository tradeRepository;
    private WalletAccountant walletAccountant;

    public TradingCoreImpl(MarketTickScheduler marketTickScheduler,
                           OrderRepository orderRepository,
                           TradeRepository tradeRepository,
                           WalletAccountant walletAccountant) {
        this.marketTickMap = marketTickScheduler.getAllMarketTicks();
        this.orderRepository = orderRepository;
        this.tradeRepository = tradeRepository;
        this.walletAccountant = walletAccountant;
    }

    public CreateOrderResponse executeOrder(Order order) {
        long marketId = order.getMarketId();
        MarketTick marketTick = marketTickMap.get(marketId);
        walletAccountant.checkWalletBalance(order, marketTick);
        Trade trade = new Trade();
        trade.setMarketId(order.getMarketId());
        trade.setQuantity(order.getQuantity());
        trade.setOrderSide(order.getOrderSide());
        order.setFilledQuantity(order.getQuantity());
        order.setRemainingQuantity(order.getQuantity() - order.getFilledQuantity());
        order.setOrderStatus(OrderStatus.EXECUTED);
        switch (order.getOrderSide()) {
            case BUY -> {
                trade.setFilledPrice(marketTick.getBidPrice());
                order.setAverageFilledPrice(marketTick.getBidPrice());
            }
            case SELL -> {
                order.setAverageFilledPrice(marketTick.getAskPrice());
                trade.setFilledPrice(marketTick.getAskPrice());
            }
        }
        Order successfulOrder = orderRepository.save(order);
        trade.setUserAccountId(order.getUserAccountId());
        trade.setOrder(order);
        Trade successfulTrade = tradeRepository.save(trade);
        walletAccountant.updateWalletBalance(successfulTrade, marketTick);
        return CreateOrderResponse.builder()
                .orderId(String.valueOf(successfulOrder.getOrderId()))
                .orderStatus(successfulOrder.getOrderStatus().name())
                .createdAtDatetime(successfulOrder.getCreatedAtDatetime().toString())
                .build();
    }
}
