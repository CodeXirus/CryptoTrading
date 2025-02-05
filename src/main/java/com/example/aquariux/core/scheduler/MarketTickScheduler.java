package com.example.aquariux.core.scheduler;

import com.example.aquariux.core.models.entities.Market;
import com.example.aquariux.core.models.entities.MarketTickHistory;
import com.example.aquariux.core.models.markets.MarketTick;
import com.example.aquariux.core.models.markets.SpotMarketTick;
import com.example.aquariux.core.models.scheduler.responses.binance.BinanceTicker;
import com.example.aquariux.core.models.scheduler.responses.huobi.HuobiTicker;
import com.example.aquariux.core.models.scheduler.responses.huobi.HuobiTickerResponse;
import com.example.aquariux.core.repositories.AssetRepository;
import com.example.aquariux.core.repositories.MarketRepository;
import com.example.aquariux.core.repositories.MarketTickHistoryRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Log4j2
@Service
public class MarketTickScheduler {
    private final RestTemplate restTemplate;
    private final AssetRepository assetRepository;
    private final MarketRepository marketRepository;
    private final MarketTickHistoryRepository marketTickHistoryRepository;
    private final String binanceEndpoint;
    private final String huobiEndpoint;
    private Map<Long, MarketTick> marketTickMap;

    public MarketTickScheduler(RestTemplate restTemplate,
                               AssetRepository assetRepository,
                               MarketRepository marketRepository,
                               MarketTickHistoryRepository marketTickHistoryRepository,
                               @Value("${scheduler.binance.url}") String binanceEndpoint,
                               @Value("${scheduler.huobi.url}") String huobiEndpoint) {
        this.restTemplate = restTemplate;
        this.assetRepository = assetRepository;
        this.marketRepository = marketRepository;
        this.marketTickHistoryRepository = marketTickHistoryRepository;
        this.binanceEndpoint = binanceEndpoint;
        this.huobiEndpoint = huobiEndpoint;
        this.marketTickMap = new HashMap<>();
    }

    /*
        Initial delay of 5seconds for Database to be populated on startup.
        Poll market ticker from Binance and Huobi on 10 seconds interval.
        Market Tickers will be persisted into Database for historical reads.
     */
    @Scheduled(fixedRate = 10000, initialDelay = 5000)
    public void pollMarketTicks() {
            List<Market> marketList = marketRepository.findAll();
            Set<String> symbolSet = new HashSet<>();
            for (Market market : marketList) {
                symbolSet.add(market.getSymbol());
            }
            Map<String, BinanceTicker> binanceTickersMap = new HashMap<>();
            Map<String, HuobiTicker> huobiTickersMap = new HashMap<>();
            try {
                binanceTickersMap = fetchBinanceTickers(symbolSet);
            } catch (Exception e) {
                log.warn("Failed to fetch market ticker from Binance.");
            }
            try {
                huobiTickersMap = fetchHuobiTickers(symbolSet);
            } catch (Exception e) {
                log.warn("Failed to fetch market ticker from Huobi.");
            }

            for (Market market : marketList) {
                HuobiTicker huobiTicker = huobiTickersMap.getOrDefault(market.getSymbol().toUpperCase(), new HuobiTicker());
                BinanceTicker binanceTicker = binanceTickersMap.getOrDefault(market.getSymbol().toUpperCase(), new BinanceTicker());
                MarketTick marketTick = SpotMarketTick.builder()
                        .marketId(market.getMarketId())
                        .baseAssetId(market.getBaseAssetId())
                        .quoteAssetId(market.getQuoteAssetId())
                        .symbol(market.getSymbol())
                        .marketType(market.getMarketType())
                        .bidPrice(Math.max(huobiTicker.getBid(), binanceTicker.getBidPrice()))
                        .askPrice(Math.min(huobiTicker.getAsk(), binanceTicker.getAskPrice()))
                        .bidSize(huobiTicker.getBid() > binanceTicker.getBidPrice() ? huobiTicker.getBidSize() : binanceTicker.getBidQty())
                        .askSize(huobiTicker.getAsk() < binanceTicker.getAskPrice() ? huobiTicker.getAskSize() : binanceTicker.getAskQty())
                        .build();
                marketTickMap.put(market.getMarketId(), marketTick);
                persistMarketTickHistory(marketTick);
                log.debug("MarketTicker: " + marketTickMap);
            }
    }

    public Map<Long, MarketTick> getAllMarketTicks() {
        return marketTickMap;
    }

    public MarketTick getMarketTickByMarketId(Long marketId) {
        return marketTickMap.get(marketId);
    }

    private Map<String, BinanceTicker> fetchBinanceTickers(Set<String> symbolSet) {
        ResponseEntity<BinanceTicker[]> binanceTickerResponse = restTemplate.getForEntity(binanceEndpoint, BinanceTicker[].class);
        BinanceTicker[] binanceTickerArray = binanceTickerResponse.getBody();
        return Arrays.asList(binanceTickerArray)
                .stream()
                .filter(marketTick -> symbolSet.contains(marketTick.getSymbol()))
                .collect(Collectors.toMap(BinanceTicker::getSymbol, Function.identity()));
    }

    private Map<String, HuobiTicker> fetchHuobiTickers(Set<String> symbolSet) {
        HuobiTickerResponse huobiTickerResponse = restTemplate.getForObject(huobiEndpoint, HuobiTickerResponse.class);
        return huobiTickerResponse.getData()
                .stream()
                .filter(marketTick -> symbolSet.contains(marketTick.getSymbol().toUpperCase()))
                .collect(Collectors.toMap(huobiTicker -> huobiTicker.getSymbol().toUpperCase(), Function.identity()));
    }

    private void persistMarketTickHistory(MarketTick marketTick) {
        MarketTickHistory marketTickHistory = new MarketTickHistory();
        marketTickHistory.setMarketId(marketTick.getMarketId());
        marketTickHistory.setBaseAssetId(marketTick.getBaseAssetId());
        marketTickHistory.setQuoteAssetId(marketTick.getQuoteAssetId());
        marketTickHistory.setSymbol(marketTick.getSymbol());
        marketTickHistory.setMarketType(marketTick.getMarketType());
        marketTickHistory.setBestBidPrice(marketTick.getBidPrice());
        marketTickHistory.setBidSize(marketTick.getBidSize());
        marketTickHistory.setBestAskPrice(marketTick.getAskPrice());
        marketTickHistory.setAskSize(marketTick.getAskSize());
        marketTickHistoryRepository.save(marketTickHistory);
    }
}
