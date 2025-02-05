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

    @GetMapping(produces = "application/json")
    public List<AccountBalanceResponse> fetchAllAssetAccountFromWallet(@RequestHeader("USER_ACCOUNT_ID") String userAccountId) {
        return getAccountBalanceAction.getAllAccountBalanceInWallet(Long.parseLong(userAccountId));
    }

    @GetMapping(value = "/{symbol}", produces = "application/json")
    public ResponseEntity<?> getAccountBalanceForAsset(@PathVariable String symbol, @RequestHeader("USER_ACCOUNT_ID") String userAccountId) {
        try {
            return new ResponseEntity<>(getAccountBalanceAction.getAccountBalanceByAssetSymbolAndUserAccountId(symbol, Long.parseLong(userAccountId)), HttpStatus.OK);
        } catch (InvalidRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
