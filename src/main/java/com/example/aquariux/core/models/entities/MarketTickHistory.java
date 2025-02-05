package com.example.aquariux.core.models.entities;

import com.example.aquariux.core.models.markets.MarketType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "market_tick_history")
@Getter
@Setter
@NoArgsConstructor
public class MarketTickHistory {
    @Id
    @SequenceGenerator(name = "tick_seq", sequenceName = "tick_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long tickSequence;
    private long marketId;
    private long baseAssetId;
    private long quoteAssetId;
    private String symbol;
    private double bestBidPrice;
    private double bidSize;
    private double bestAskPrice;
    private double askSize;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private MarketType marketType;

    @CreationTimestamp
    private Instant createdAtDatetime;
}
