package com.example.aquariux.trade.actions;

import com.example.aquariux.core.models.entities.Market;
import com.example.aquariux.core.models.entities.Trade;
import com.example.aquariux.core.repositories.MarketRepository;
import com.example.aquariux.core.repositories.TradeRepository;
import com.example.aquariux.trade.models.TradeResponse;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class GetTradeActionImpl implements GetTradeAction {
    private final TradeRepository tradeRepository;
    private final MarketRepository marketRepository;

    public GetTradeActionImpl(TradeRepository tradeRepository, MarketRepository marketRepository) {
        this.tradeRepository = tradeRepository;
        this.marketRepository = marketRepository;
    }

    @Override
    public List<TradeResponse> getAllTradeHistories(long userAccountId) {
        List<Trade> tradeList = tradeRepository.findAllByUserAccountId(userAccountId);
        return tradeList.stream()
                .map(this::transformTrade)
                .toList();
    }

    @Override
    public TradeResponse getTradeHistoryById(long tradeId, long userAccountId) {
        Optional<Trade> trade = tradeRepository.findByTradeIdAndUserAccountId(tradeId, userAccountId);
        if (trade.isEmpty()) {
            return null;
        }
        return transformTrade(trade.get());
    }

    @Override
    public List<TradeResponse> getAllTradesByMarketSymbol(String symbol, long userAccountId) {
        Optional<Market> market = marketRepository.findBySymbol(symbol.toUpperCase());
        if (market.isPresent()) {
            List<Trade> tradeList = tradeRepository.findAllByMarketIdAndUserAccountId(market.get().getMarketId(), userAccountId);
            return tradeList.stream()
                    .map(this::transformTrade)
                    .toList();
        } else {
            return Collections.emptyList();
        }
    }

    private TradeResponse transformTrade(Trade trade) {
        Market market = marketRepository.findById(trade.getMarketId()).get();
        return TradeResponse.builder()
                .tradeId(String.valueOf(trade.getTradeId()))
                .filledPrice(String.valueOf(trade.getFilledPrice()))
                .quantity(String.valueOf(trade.getQuantity()))
                .marketSymbol(market.getSymbol())
                .orderId(String.valueOf(trade.getOrder().getOrderId()))
                .build();
    }
}
