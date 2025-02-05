package com.example.aquariux.order.actions;

import com.example.aquariux.core.models.entities.Market;
import com.example.aquariux.core.models.entities.Order;
import com.example.aquariux.core.repositories.MarketRepository;
import com.example.aquariux.core.service.TradingCore;
import com.example.aquariux.exception.InvalidRequestException;
import com.example.aquariux.order.models.requests.CreateOrderRequest;
import com.example.aquariux.order.models.OrderSide;
import com.example.aquariux.order.models.OrderType;
import com.example.aquariux.order.models.responses.CreateOrderResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProcessOrderRequestActionImpl implements ProcessOrderRequestAction {
    private TradingCore tradingCore;
    private MarketRepository marketRepository;

    public ProcessOrderRequestActionImpl(TradingCore tradingCore,
                                         MarketRepository marketRepository) {
        this.tradingCore = tradingCore;
        this.marketRepository = marketRepository;
    }

    public CreateOrderResponse processOrder(CreateOrderRequest createOrderRequest, long userAccountId) {
        validateOrder(createOrderRequest);
        validateUser(createOrderRequest, userAccountId);

        Optional<Market> optionalMarket = marketRepository.findBySymbol(createOrderRequest.getSymbol());
        if (optionalMarket.isEmpty()) {
            throw new InvalidRequestException("Invalid market symbol: " + createOrderRequest.getSymbol());
        }
        Market market = optionalMarket.get();
        Order order = new Order();
        order.setMarketId(market.getMarketId());
        order.setUserAccountId(userAccountId);
        order.setQuantity(Double.parseDouble(createOrderRequest.getQuantity()));
        order.setOrderType(OrderType.valueOf(createOrderRequest.getType().toUpperCase()));
        order.setOrderSide(OrderSide.valueOf(createOrderRequest.getSide().toUpperCase()));
        return tradingCore.executeOrder(order);
    }

    private void validateOrder(CreateOrderRequest createOrderRequest) {
        if (createOrderRequest.getSymbol().isBlank()) {
            throw new InvalidRequestException("Missing symbol from request body.");
        }
        if (createOrderRequest.getSide().isBlank()) {
            throw new InvalidRequestException("Missing order side from request body.");
        }
        if (createOrderRequest.getQuantity().isBlank()) {
            throw new InvalidRequestException("Missing quantity from request body.");
        }
    }

    private void validateUser(CreateOrderRequest createOrderRequest, long userAccountId) {
        /*
        Some authentication or compliance check here
        to determine if user is allowed to trade.
         */
    }
}
