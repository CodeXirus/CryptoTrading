package com.example.aquariux.trade.actions;

import com.example.aquariux.trade.models.TradeResponse;

import java.util.List;

public interface GetTradeAction {
    List<TradeResponse> getAllTradeHistories(long userAccountId);
    TradeResponse getTradeHistoryById(long tradeId, long userAccountId);
    List<TradeResponse> getAllTradesByMarketSymbol(String symbol, long userAccountId);
}
