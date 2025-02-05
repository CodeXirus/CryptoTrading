package com.example.aquariux.market.models.responses;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MarketTickResponse {
    private String symbol;
    private String baseAssetSymbol;
    private String quoteAssetSymbol;
    private String marketType;
    private String bidPrice;
    private String bidSize;
    private String askPrice;
    private String askSize;
}
