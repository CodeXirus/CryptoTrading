package com.example.aquariux.market.action;

import com.example.aquariux.core.models.entities.Asset;
import com.example.aquariux.core.models.entities.Market;
import com.example.aquariux.core.models.markets.MarketTick;
import com.example.aquariux.core.repositories.AssetRepository;
import com.example.aquariux.core.repositories.MarketRepository;
import com.example.aquariux.core.scheduler.MarketTickScheduler;
import com.example.aquariux.exception.InvalidRequestException;
import com.example.aquariux.market.models.responses.CurrentBestPriceResponse;
import com.example.aquariux.market.models.responses.MarketTickResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GetMarketTickActionImpl implements GetMarketTickAction {
    private Map<Long, MarketTick> marketTickMap;
    private final MarketRepository marketRepository;
    private final AssetRepository assetRepository;

    public GetMarketTickActionImpl(MarketTickScheduler marketTickScheduler,
                                   MarketRepository marketRepository,
                                   AssetRepository assetRepository) {
        this.marketTickMap = marketTickScheduler.getAllMarketTicks();
        this.marketRepository = marketRepository;
        this.assetRepository = assetRepository;
    }
    @Override
    public MarketTickResponse getMarketTickBySymbol(String symbol) {
        Optional<Market> market = marketRepository.findBySymbol(symbol.toUpperCase());
        if (market.isEmpty()) {
            throw new InvalidRequestException("Invalid market symbol: " + symbol);
        }
        return transformToMarketTickResponse(marketTickMap.get(market.get().getMarketId()), market.get());
    }

    @Override
    public List<MarketTickResponse> getAllMarketTicks() {
        return marketTickMap.values()
                .stream()
                .map(this::transformToMarketTickResponse)
                .toList();
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

    private MarketTickResponse transformToMarketTickResponse(MarketTick marketTick) {
        Market market = marketRepository.findById(marketTick.getMarketId()).get();
        Asset baseAsset = assetRepository.findById(market.getBaseAssetId()).get();
        Asset quoteAsset = assetRepository.findById(market.getQuoteAssetId()).get();
        return MarketTickResponse.builder()
                .symbol(market.getSymbol())
                .baseAssetSymbol(baseAsset.getSymbol())
                .quoteAssetSymbol(quoteAsset.getSymbol())
                .marketType(market.getMarketType().name())
                .bidPrice(String.valueOf(marketTick.getBidPrice()))
                .bidSize(String.valueOf(marketTick.getBidSize()))
                .askPrice(String.valueOf(marketTick.getAskPrice()))
                .askSize(String.valueOf(marketTick.getAskSize()))
                .build();
    }

    private MarketTickResponse transformToMarketTickResponse(MarketTick marketTick, Market market) {
        Asset baseAsset = assetRepository.findById(market.getBaseAssetId()).get();
        Asset quoteAsset = assetRepository.findById(market.getQuoteAssetId()).get();
        return MarketTickResponse.builder()
                .symbol(market.getSymbol())
                .baseAssetSymbol(baseAsset.getSymbol())
                .quoteAssetSymbol(quoteAsset.getSymbol())
                .marketType(market.getMarketType().name())
                .bidPrice(String.valueOf(marketTick.getBidPrice()))
                .bidSize(String.valueOf(marketTick.getBidSize()))
                .askPrice(String.valueOf(marketTick.getAskPrice()))
                .askSize(String.valueOf(marketTick.getAskSize()))
                .build();
    }
}
