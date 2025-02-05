package com.example.aquariux.core.models.entities;

import com.example.aquariux.core.models.markets.MarketType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "markets")
@Getter
@Setter
@NoArgsConstructor
public class Market {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long marketId;

    private long baseAssetId;
    private long quoteAssetId;
    private String symbol;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private MarketType marketType;
}
