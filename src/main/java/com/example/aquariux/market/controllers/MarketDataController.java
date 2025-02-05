package com.example.aquariux.market.controllers;

import com.example.aquariux.core.models.markets.MarketTick;
import com.example.aquariux.exception.InvalidRequestException;
import com.example.aquariux.market.action.GetMarketTickAction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public @ResponseBody ResponseEntity<?> getMarketTickBySymbol(@PathVariable String symbol) {
        try {
            return new ResponseEntity<>(getMarketTickAction.getMarketTickBySymbol(symbol), HttpStatus.OK);
        } catch (InvalidRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping(produces = "application/json")
    public @ResponseBody List<MarketTick> getAllMarketTick() {
        return getMarketTickAction.getAllMarketTicks();
    }

    @GetMapping(value = "/{symbol}/price", produces = "application/json")
    public @ResponseBody ResponseEntity<?> getCurrentBestPrice(@PathVariable String symbol) {
        try {
            return new ResponseEntity<>(getMarketTickAction.getCurrentBestPriceBySymbol(symbol), HttpStatus.OK);
        } catch (InvalidRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
