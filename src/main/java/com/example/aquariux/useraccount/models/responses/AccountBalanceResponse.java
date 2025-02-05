package com.example.aquariux.useraccount.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AccountBalanceResponse {
    private String assetSymbol;
    private String assetName;
    private String balance;
}
