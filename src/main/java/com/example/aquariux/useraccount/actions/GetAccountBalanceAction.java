package com.example.aquariux.useraccount.actions;

import com.example.aquariux.useraccount.models.responses.AccountBalanceResponse;

import java.util.List;

public interface GetAccountBalanceAction {
    List<AccountBalanceResponse> getAllAccountBalanceInWallet(long userAccountId);
    AccountBalanceResponse getAccountBalanceByAssetSymbolAndUserAccountId(String symbol, long userAccountId);
}
