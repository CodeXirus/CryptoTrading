package com.example.aquariux.trade;

import com.example.aquariux.trade.actions.GetTradeAction;
import com.example.aquariux.trade.models.TradeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TradeDataControllerTest.class)
class TradeDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetTradeAction getTradeAction;

    private TradeResponse tradeResponse;

    @BeforeEach
    void setUp() {
        tradeResponse = TradeResponse.builder()
                .tradeId("1")
                .marketSymbol("BTCUSDT")
                .filledPrice("50000.00")
                .quantity("0.1")
                .orderId("1001")
                .build();

        when(getTradeAction.getAllTradeHistories(anyLong()))
                .thenReturn(Collections.singletonList(tradeResponse));

        when(getTradeAction.getTradeHistoryById(anyLong(), anyLong()))
                .thenReturn(tradeResponse);
    }

    @Test
    void test_get_all_trade_history_endpoint() throws Exception {
        mockMvc.perform(get("/api/trades")
                        .header("USER_ACCOUNT_ID", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].marketSymbol").value("BTCUSDT"));
    }

    @Test
    void test_get_trade_history_by_id_endpoint() throws Exception {
        mockMvc.perform(get("/api/trades/1")
                        .header("USER_ACCOUNT_ID", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.marketSymbol").value("BTCUSDT"));
    }
}
