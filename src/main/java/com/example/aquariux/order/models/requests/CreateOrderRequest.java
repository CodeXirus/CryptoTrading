package com.example.aquariux.order.models.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateOrderRequest {
    private String symbol;
    private String side;
    private String type;
    private String quantity;
}
