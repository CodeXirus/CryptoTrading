package com.example.aquariux.trade.service;

import com.example.aquariux.trade.actions.GetTradeAction;
import com.example.aquariux.trade.models.TradeResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/trades")
public class TradeDataController {
    private final GetTradeAction getTradeAction;

    public TradeDataController(GetTradeAction getTradeAction) {
        this.getTradeAction = getTradeAction;
    }

    @GetMapping(produces = "application/json")
    public List<TradeResponse> getAllTradeHistory(@RequestHeader("USER_ACCOUNT_ID") String userAccountId) {
        return getTradeAction.getAllTradeHistories(Long.parseLong(userAccountId));
    }

    @GetMapping(value = "/{tradeId}", produces = "application/json")
    public TradeResponse getTradeHistoryById(@PathVariable String tradeId, @RequestHeader("USER_ACCOUNT_ID") String userAccountId) {
        return getTradeAction.getTradeHistoryById(Long.parseLong(tradeId), Long.parseLong(userAccountId));
    }
}
