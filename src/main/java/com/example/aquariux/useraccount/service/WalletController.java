package com.example.aquariux.useraccount.service;

import com.example.aquariux.exception.InvalidRequestException;
import com.example.aquariux.useraccount.actions.GetAccountBalanceAction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.aquariux.useraccount.models.responses.AccountBalanceResponse;

import java.util.List;

@RestController
@RequestMapping("api/wallet")
public class WalletController {
    private final GetAccountBalanceAction getAccountBalanceAction;

    public WalletController(GetAccountBalanceAction getAccountBalanceAction) {
        this.getAccountBalanceAction = getAccountBalanceAction;
    }

    /*
        Get all asset account balances belonging to user
        RequestHeader required: USER_ACCOUNT_ID:String
        Usually we will derive the user account id from the SessionData or JWT Token.
        However, we are assuming that the user is already authenticated, hence I am
        using this header (USER_ACCOUNT_ID) as a replacement.
     */
    @GetMapping(produces = "application/json")
    public List<AccountBalanceResponse> fetchAllAssetAccountFromWallet(@RequestHeader("USER_ACCOUNT_ID") String userAccountId) {
        return getAccountBalanceAction.getAllAccountBalanceInWallet(Long.parseLong(userAccountId));
    }

    /*
        Get asset account balance belonging to user by asset symbol
        Path Parameter: String symbol [USDT]
        RequestHeader required: USER_ACCOUNT_ID:String
     */
    @GetMapping(value = "/{symbol}", produces = "application/json")
    public ResponseEntity<?> getAccountBalanceForAsset(@PathVariable String symbol, @RequestHeader("USER_ACCOUNT_ID") String userAccountId) {
        try {
            return new ResponseEntity<>(getAccountBalanceAction.getAccountBalanceByAssetSymbolAndUserAccountId(symbol, Long.parseLong(userAccountId)), HttpStatus.OK);
        } catch (InvalidRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
