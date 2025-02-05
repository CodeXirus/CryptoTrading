package com.example.aquariux.core.models.scheduler.responses.huobi;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class HuobiTickerResponse {
    private String status;
    private long ts;
    private List<HuobiTicker> data;
}
