package com.example.aquariux.market.action;

import com.example.aquariux.core.models.markets.MarketTick;
import com.example.aquariux.market.models.responses.CurrentBestPriceResponse;

import java.util.List;

public interface GetMarketTickAction {
    MarketTick getMarketTickBySymbol(String symbol);
    List<MarketTick> getAllMarketTicks();
    CurrentBestPriceResponse getCurrentBestPriceBySymbol(String symbol);
}
