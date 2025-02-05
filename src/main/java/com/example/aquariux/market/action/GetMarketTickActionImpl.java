package com.example.aquariux.market.action;

import com.example.aquariux.core.models.entities.Market;
import com.example.aquariux.core.models.markets.MarketTick;
import com.example.aquariux.core.repositories.MarketRepository;
import com.example.aquariux.core.scheduler.MarketTickScheduler;
import com.example.aquariux.market.models.responses.CurrentBestPriceResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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
        Market market = marketRepository.findBySymbol(symbol).orElseThrow();
        return marketTickMap.get(market.getMarketId());
    }

    @Override
    public List<MarketTick> getAllMarketTicks() {
        return marketTickMap.values()
                .stream()
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public CurrentBestPriceResponse getCurrentBestPriceBySymbol(String symbol) {
        Market market = marketRepository.findBySymbol(symbol).orElseThrow();
        MarketTick marketTick = marketTickMap.get(market.getMarketId());
        return CurrentBestPriceResponse.builder()
                .symbol(market.getSymbol())
                .bestBidPrice(marketTick.getBidPrice())
                .bestAskPrice(marketTick.getAskPrice())
                .build();
    }
}
