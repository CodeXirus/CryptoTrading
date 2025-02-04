package core.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "trades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private Long tradeId;

    private double filledPrice;
    private double quantity;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}
