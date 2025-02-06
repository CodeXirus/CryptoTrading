package com.example.aquariux.trade.service;

import com.example.aquariux.trade.actions.GetTradeAction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<?> getAllTradeHistory(@RequestHeader("USER_ACCOUNT_ID") String userAccountId) {
        try {
            return new ResponseEntity<>(getTradeAction.getAllTradeHistories(Long.parseLong(userAccountId)), HttpStatus.OK);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("userAccountId must be numeric.");
        }
    }

    /*
        Get trade by trade id
        Path Parameter: String tradeId ["123"]
        RequestHeader required: USER_ACCOUNT_ID:String
     */
    @GetMapping(value = "/{tradeId}", produces = "application/json")
    public ResponseEntity<?> getTradeHistoryById(@PathVariable String tradeId, @RequestHeader("USER_ACCOUNT_ID") String userAccountId) {
        try {
            return new ResponseEntity<>(getTradeAction.getTradeHistoryById(Long.parseLong(tradeId), Long.parseLong(userAccountId)), HttpStatus.OK);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("tradeId or userAccountId must be numeric.");
        }
    }

    /*
        Get trade by trade id
        Query Parameter: String symbol ["BTCUSDT"]
        RequestHeader required: USER_ACCOUNT_ID:String
     */
    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getTradeHistoryByMarketSymbol(@RequestParam String symbol, @RequestHeader("USER_ACCOUNT_ID") String userAccountId) {
        try {
            return new ResponseEntity<>(getTradeAction.getAllTradesByMarketSymbol(symbol, Long.parseLong(userAccountId)), HttpStatus.OK);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("userAccountId must be numeric.");
        }
    }
}
