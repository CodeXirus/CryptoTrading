package com.example.aquariux.core;

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
import com.example.aquariux.core.scheduler.MarketTickScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class MarketTickSchedulerTest {

    @InjectMocks
    private MarketTickScheduler marketTickScheduler;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private MarketRepository marketRepository;

    @Mock
    private MarketTickHistoryRepository marketTickHistoryRepository;

    private final String binanceEndpoint = "https://api.binance.com/api/v3/ticker/bookTicker";
    private final String huobiEndpoint = "https://api.huobi.pro/market/tickers";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        marketTickScheduler = new MarketTickScheduler(
                restTemplate, assetRepository, marketRepository, marketTickHistoryRepository,
                binanceEndpoint, huobiEndpoint);
    }

    @Test
    void test_poll_market_ticks() {
        Market market1 = new Market();
        market1.setMarketId(1L);
        market1.setSymbol("BTCUSDT");

        Market market2 = new Market();
        market2.setMarketId(2L);
        market2.setSymbol("ETHUSDT");

        List<Market> marketList = List.of(market1, market2);

        when(marketRepository.findAll()).thenReturn(marketList);

        // Setup Binance mock response
        BinanceTicker binanceTicker1 = new BinanceTicker();
        binanceTicker1.setSymbol("BTCUSDT");
        binanceTicker1.setBidPrice(45000.0);
        binanceTicker1.setAskPrice(45050.0);
        binanceTicker1.setBidQty(0.5);
        binanceTicker1.setAskQty(0.7);

        BinanceTicker binanceTicker2 = new BinanceTicker();
        binanceTicker2.setSymbol("ETHUSDT");
        binanceTicker2.setBidPrice(3000.0);
        binanceTicker2.setAskPrice(3020.0);
        binanceTicker2.setBidQty(2.0);
        binanceTicker2.setAskQty(1.5);

        BinanceTicker[] binanceTickers = {binanceTicker1, binanceTicker2};
        when(restTemplate.getForEntity(eq(binanceEndpoint), eq(BinanceTicker[].class)))
                .thenReturn(ResponseEntity.ok(binanceTickers));

        // Setup Huobi mock response
        HuobiTicker huobiTicker1 = new HuobiTicker();
        huobiTicker1.setSymbol("BTCUSDT");
        huobiTicker1.setBid(44900.0);
        huobiTicker1.setAsk(45100.0);
        huobiTicker1.setBidSize(0.6);
        huobiTicker1.setAskSize(0.8);

        HuobiTicker huobiTicker2 = new HuobiTicker();
        huobiTicker2.setSymbol("ETHUSDT");
        huobiTicker2.setBid(2990.0);
        huobiTicker2.setAsk(3030.0);
        huobiTicker2.setBidSize(2.5);
        huobiTicker2.setAskSize(1.2);

        HuobiTickerResponse huobiTickerResponse = new HuobiTickerResponse();
        huobiTickerResponse.setData(List.of(huobiTicker1, huobiTicker2));

        when(restTemplate.getForObject(eq(huobiEndpoint), eq(HuobiTickerResponse.class)))
                .thenReturn(huobiTickerResponse);

        marketTickScheduler.pollMarketTicks();

        Map<Long, MarketTick> marketTickMap = marketTickScheduler.getAllMarketTicks();

        assertNotNull(marketTickMap);
        assertEquals(2, marketTickMap.size());

        MarketTick btcTick = marketTickMap.get(1L);
        assertNotNull(btcTick);
        assertEquals("BTCUSDT", btcTick.getSymbol());
        assertEquals(45000.0, btcTick.getBidPrice());
        assertEquals(45050.0, btcTick.getAskPrice());
        assertEquals(0.5, btcTick.getBidSize());
        assertEquals(0.7, btcTick.getAskSize());

        MarketTick ethTick = marketTickMap.get(2L);
        assertNotNull(ethTick);
        assertEquals("ETHUSDT", ethTick.getSymbol());
        assertEquals(3000.0, ethTick.getBidPrice());
        assertEquals(3020.0, ethTick.getAskPrice());
        assertEquals(2.0, ethTick.getBidSize());
        assertEquals(1.5, ethTick.getAskSize());

        verify(marketRepository, times(1)).findAll();
        verify(restTemplate, times(1)).getForEntity(eq(binanceEndpoint), eq(BinanceTicker[].class));
        verify(restTemplate, times(1)).getForObject(eq(huobiEndpoint), eq(HuobiTickerResponse.class));
        verify(marketTickHistoryRepository, times(2)).save(any(MarketTickHistory.class));
    }

    @Test
    void test_get_market_tick_by_market_id() {
        MarketTick marketTick = SpotMarketTick.builder()
                .marketId(1L)
                .symbol("BTCUSDT")
                .bidPrice(45000.0)
                .askPrice(45050.0)
                .bidSize(0.5)
                .askSize(0.7)
                .build();

        marketTickScheduler.getAllMarketTicks().put(1L, marketTick);

        MarketTick result = marketTickScheduler.getMarketTickByMarketId(1L);

        assertNotNull(result);
        assertEquals("BTCUSDT", result.getSymbol());
        assertEquals(45000.0, result.getBidPrice());
        assertEquals(45050.0, result.getAskPrice());
        assertEquals(0.5, result.getBidSize());
        assertEquals(0.7, result.getAskSize());
    }
}
