package com.example.aquariux.core.models.scheduler.responses.binance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BinanceTickerResponse {
    List<BinanceTicker> data;
}
