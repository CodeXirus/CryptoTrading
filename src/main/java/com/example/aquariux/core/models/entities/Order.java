package com.example.aquariux.core.models.entities;

import com.example.aquariux.order.models.OrderSide;
import com.example.aquariux.order.models.OrderStatus;
import com.example.aquariux.order.models.OrderType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private Long userAccountId;
    private double quantity;
    private double limitPrice;
    private long marketId;
    private double filledQuantity;
    private double averageFilledPrice;
    private double remainingQuantity;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private OrderType orderType;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private OrderStatus status;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private OrderSide side;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Trade> trades;
}
