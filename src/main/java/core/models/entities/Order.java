package core.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import order.models.OrderStatus;
import order.models.OrderType;

import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Trade> trades;
}
