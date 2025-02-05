package com.example.aquariux.market.action;

import com.example.aquariux.market.models.responses.CurrentBestPriceResponse;
import com.example.aquariux.market.models.responses.MarketTickResponse;

import java.util.List;

public interface GetMarketTickAction {
    MarketTickResponse getMarketTickBySymbol(String symbol);
    List<MarketTickResponse> getAllMarketTicks();
    CurrentBestPriceResponse getCurrentBestPriceBySymbol(String symbol);
}
