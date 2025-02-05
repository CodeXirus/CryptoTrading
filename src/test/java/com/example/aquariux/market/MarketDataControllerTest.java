package com.example.aquariux.market.controllers;

import com.example.aquariux.core.models.markets.MarketTick;
import com.example.aquariux.core.models.markets.SpotMarketTick;
import com.example.aquariux.market.action.GetMarketTickAction;
import com.example.aquariux.market.models.responses.CurrentBestPriceResponse;
import com.example.aquariux.testconfig.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestConfig.class)
@WebMvcTest(MarketDataController.class)
class MarketDataControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GetMarketTickAction getMarketTickAction;
    private MarketTick sampleMarketTick;
    private CurrentBestPriceResponse sampleBestPrice;

    @BeforeEach
    void setUp() {
        sampleMarketTick = SpotMarketTick.builder()
                .symbol("BTCUSDT")
                .build();

        sampleBestPrice = CurrentBestPriceResponse.builder()
                .symbol("BTCUSDT")
                .bestBidPrice(45000.00)
                .bestAskPrice(45010.00)
                .build();
    }

    @Test
    void test_get_market_tick_by_symbol_endpoint() throws Exception {
        when(getMarketTickAction.getMarketTickBySymbol(anyString())).thenReturn(sampleMarketTick);

        mockMvc.perform(get("/api/markets/tick/BTCUSDT")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.symbol").value("BTCUSDT"));
    }

    @Test
    void test_get_all_market_tick_endpoint() throws Exception {
        List<MarketTick> marketTickList = Arrays.asList(sampleMarketTick);
        when(getMarketTickAction.getAllMarketTicks()).thenReturn(marketTickList);

        mockMvc.perform(get("/api/markets/tick")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].symbol").value("BTCUSDT"));
    }

    @Test
    void test_get_current_best_price_endpoint() throws Exception {
        when(getMarketTickAction.getCurrentBestPriceBySymbol(anyString())).thenReturn(sampleBestPrice);

        mockMvc.perform(get("/api/markets/tick/BTCUSDT/price")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.symbol").value("BTCUSDT"))
                .andExpect(jsonPath("$.bestBidPrice").value(45000.00))
                .andExpect(jsonPath("$.bestAskPrice").value(45010.00));
    }
}
