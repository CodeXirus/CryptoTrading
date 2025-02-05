package com.example.aquariux.market.service;

import com.example.aquariux.core.models.markets.MarketTick;
import com.example.aquariux.market.action.GetMarketTickAction;
import com.example.aquariux.market.models.responses.CurrentBestPriceResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/markets/tick")
public class MarketDataController {
    private GetMarketTickAction getMarketTickAction;

    public MarketDataController(GetMarketTickAction getMarketTickAction) {
        this.getMarketTickAction = getMarketTickAction;
    }

    @GetMapping(value = "/{symbol}", produces = "application/json")
    public @ResponseBody MarketTick getMarketTickBySymbol(@PathVariable String symbol) {
        return getMarketTickAction.getMarketTickBySymbol(symbol);
    }

    @GetMapping(produces = "application/json")
    public @ResponseBody List<MarketTick> getAllMarketTick() {
        return getMarketTickAction.getAllMarketTicks();
    }

    @GetMapping(value = "/{symbol}/price", produces = "application/json")
    public @ResponseBody CurrentBestPriceResponse getCurrentBestPrice(@PathVariable String symbol) {
        return getMarketTickAction.getCurrentBestPriceBySymbol(symbol);
    }
}
