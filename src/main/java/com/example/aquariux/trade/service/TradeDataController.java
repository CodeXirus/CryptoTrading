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

    /*
        Get all trades belonging to user
        RequestHeader required: USER_ACCOUNT_ID:String
        Usually we will derive the user account id from the SessionData or JWT Token.
        However, we are assuming that the user is already authenticated, hence I am
        using this header (USER_ACCOUNT_ID) as a replacement.
     */
    @GetMapping(produces = "application/json")
    public List<TradeResponse> getAllTradeHistory(@RequestHeader("USER_ACCOUNT_ID") String userAccountId) {
        return getTradeAction.getAllTradeHistories(Long.parseLong(userAccountId));
    }

    /*
        Get trade by trade id
        Path Parameter: String tradeId ["123"]
        RequestHeader required: USER_ACCOUNT_ID:String
     */
    @GetMapping(value = "/{tradeId}", produces = "application/json")
    public TradeResponse getTradeHistoryById(@PathVariable String tradeId, @RequestHeader("USER_ACCOUNT_ID") String userAccountId) {
        return getTradeAction.getTradeHistoryById(Long.parseLong(tradeId), Long.parseLong(userAccountId));
    }
}
