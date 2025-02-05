package com.example.aquariux.market.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CurrentBestPriceResponse {
    private String symbol;
    private double bestBidPrice;
    private double bestAskPrice;
}
