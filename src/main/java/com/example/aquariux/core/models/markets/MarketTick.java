package com.example.aquariux.core.models.markets;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@ToString
public abstract class MarketTick {
    private long marketId;
    private long baseAssetId;
    private long quoteAssetId;
    private String symbol;
    private MarketType marketType;
    private double bidPrice;
    private double bidSize;
    private double askPrice;
    private double askSize;
}
