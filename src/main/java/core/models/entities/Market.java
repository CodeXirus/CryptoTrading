package core.models.entities;

import core.models.markets.MarketType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "markets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Market {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long marketId;

    private long baseAssetId;
    private long quoteAssetId;
    private String symbol;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private MarketType marketType;
}
