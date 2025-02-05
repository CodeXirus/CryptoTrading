package com.example.aquariux.core.models.entities;

import com.example.aquariux.order.models.OrderSide;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "trades")
@Getter
@Setter
@NoArgsConstructor
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tradeId;

    private Long marketId;
    private double filledPrice;
    private double quantity;
    private OrderSide orderSide;
    private Long userAccountId;

    @CreationTimestamp
    private Instant createdAtDatetime;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}
