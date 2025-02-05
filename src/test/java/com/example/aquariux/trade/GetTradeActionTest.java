package com.example.aquariux.trade;

import com.example.aquariux.core.models.entities.Market;
import com.example.aquariux.core.models.entities.Order;
import com.example.aquariux.core.models.entities.Trade;
import com.example.aquariux.core.repositories.MarketRepository;
import com.example.aquariux.core.repositories.TradeRepository;
import com.example.aquariux.trade.actions.GetTradeActionImpl;
import com.example.aquariux.trade.models.TradeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class GetTradeActionTest {

    @InjectMocks
    private GetTradeActionImpl getTradeAction;

    @Mock
    private TradeRepository tradeRepository;

    @Mock
    private MarketRepository marketRepository;

    private Trade trade;
    private Market market;

    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        order = new Order();
        order.setOrderId(1L);

        trade = new Trade();
        trade.setTradeId(1L);
        trade.setFilledPrice(50000.00);
        trade.setQuantity(0.1);
        trade.setMarketId(1L);
        trade.setUserAccountId(1L);
        trade.setOrder(order);

        market = new Market();
        market.setMarketId(1L);
        market.setSymbol("BTCUSDT");

        when(tradeRepository.findAllByUserAccountId(anyLong())).thenReturn(Collections.singletonList(trade));
        when(tradeRepository.findByTradeIdAndUserAccountId(anyLong(), anyLong())).thenReturn(Optional.of(trade));
        when(marketRepository.findById(anyLong())).thenReturn(Optional.of(market));
    }

    @Test
    void test_get_all_trade_histories_return_trade_list() {
        List<TradeResponse> responses = getTradeAction.getAllTradeHistories(1L);
        assertThat(responses).isNotEmpty();
        assertThat(responses.get(0).getMarketSymbol()).isEqualTo("BTCUSDT");
    }

    @Test
    void test_get_trade_history_by_id() {
        TradeResponse response = getTradeAction.getTradeHistoryById(1L, 1L);
        assertThat(response).isNotNull();
        assertThat(response.getMarketSymbol()).isEqualTo("BTCUSDT");
    }

    @Test
    void test_other_user_cannot_view_your_trade_history_using_trade_id() {
        when(tradeRepository.findByTradeIdAndUserAccountId(anyLong(), anyLong())).thenReturn(Optional.empty());
        TradeResponse response = getTradeAction.getTradeHistoryById(1L, 2L);
        assertThat(response).isNull();
    }
}

