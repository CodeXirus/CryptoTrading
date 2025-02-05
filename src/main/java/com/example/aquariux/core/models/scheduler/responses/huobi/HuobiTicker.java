package com.example.aquariux.core.models.scheduler.responses.huobi;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class HuobiTicker {
    private String symbol;
    private double bid;
    private double bidSize;
    private double ask;
    private double askSize;
}
