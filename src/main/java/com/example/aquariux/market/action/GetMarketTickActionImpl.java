package com.example.aquariux.market.action;

import com.example.aquariux.core.models.entities.Market;
import com.example.aquariux.core.models.markets.MarketTick;
import com.example.aquariux.core.repositories.MarketRepository;
import com.example.aquariux.core.scheduler.MarketTickScheduler;
import com.example.aquariux.exception.InvalidRequestException;
import com.example.aquariux.market.models.responses.CurrentBestPriceResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GetMarketTickActionImpl implements GetMarketTickAction {
    private Map<Long, MarketTick> marketTickMap;
    private final MarketRepository marketRepository;

    public GetMarketTickActionImpl(MarketTickScheduler marketTickScheduler, MarketRepository marketRepository) {
        this.marketTickMap = marketTickScheduler.getAllMarketTicks();
        this.marketRepository = marketRepository;
    }
    @Override
    public MarketTick getMarketTickBySymbol(String symbol) {
        Optional<Market> market = marketRepository.findBySymbol(symbol.toUpperCase());
        if (market.isEmpty()) {
            throw new InvalidRequestException("Invalid market symbol: " + symbol);
        }
        return marketTickMap.get(market.get().getMarketId());
    }

    @Override
    public List<MarketTick> getAllMarketTicks() {
        return marketTickMap.values()
                .stream()
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public CurrentBestPriceResponse getCurrentBestPriceBySymbol(String symbol) {
        Optional<Market> market = marketRepository.findBySymbol(symbol.toUpperCase());
        if (market.isEmpty()) {
            throw new InvalidRequestException("Invalid market symbol: " + symbol.toUpperCase());
        }
        MarketTick marketTick = marketTickMap.get(market.get().getMarketId());
        return CurrentBestPriceResponse.builder()
                .symbol(market.get().getSymbol())
                .bestBidPrice(marketTick.getBidPrice())
                .bestAskPrice(marketTick.getAskPrice())
                .build();
    }
}
