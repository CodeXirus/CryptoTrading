package com.example.aquariux.trade.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TradeResponse {
    private String tradeId;
    private String filledPrice;
    private String quantity;
    private String marketSymbol;
    private String orderId;
}
