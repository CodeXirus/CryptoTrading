package com.example.aquariux.order.actions;

import com.example.aquariux.core.models.entities.Market;
import com.example.aquariux.core.models.entities.Order;
import com.example.aquariux.core.models.entities.Trade;
import com.example.aquariux.core.repositories.MarketRepository;
import com.example.aquariux.core.repositories.OrderRepository;
import com.example.aquariux.order.models.responses.OrderResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GetOrderActionImpl implements GetOrderAction {
    private final OrderRepository orderRepository;
    private final MarketRepository marketRepository;

    public GetOrderActionImpl(OrderRepository orderRepository, MarketRepository marketRepository) {
        this.orderRepository = orderRepository;
        this.marketRepository = marketRepository;
    }

    @Override
    public OrderResponse getOrderById(long orderId, long userAccountId) {
        Optional<Order> order = orderRepository.findByOrderIdAndUserAccountId(orderId, userAccountId);
        if(order.isEmpty()) {
            return null;
        }
        return orderTransformer(order.get());
    }

    @Override
    public List<OrderResponse> getAllOrders(long userAccountId) {
        List<Order> orderList = orderRepository.findAllByUserAccountId(userAccountId);
        return orderList.stream()
                .map(this::orderTransformer)
                .toList();
    }

    private OrderResponse orderTransformer(Order order) {
        Market market = marketRepository.findById(order.getMarketId()).orElseThrow();
        List<String> tradeIds = new ArrayList<>();
        for (Trade trade : order.getTrades()) {
            tradeIds.add(String.valueOf(trade.getTradeId()));
        }
        return OrderResponse.builder()
                .orderId(String.valueOf(order.getOrderId()))
                .marketSymbol(market.getSymbol())
                .orderQuantity(String.valueOf(order.getQuantity()))
                .filledQuantity(String.valueOf(order.getFilledQuantity()))
                .averageFilledPrice(String.valueOf(order.getAverageFilledPrice()))
                .remainingQuantity(String.valueOf(order.getRemainingQuantity()))
                .orderType(order.getOrderType().name())
                .orderStatus(order.getOrderStatus().name())
                .orderSide(order.getOrderSide().name())
                .createdAtDatetime(order.getCreatedAtDatetime().toString())
                .tradeIds(tradeIds)
                .build();
    }
}
