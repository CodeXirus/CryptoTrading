package core.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "asset_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssetAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private Long assetAccountId;

    private long assetId;
    private double quantity;
    private double quantityLocked;

    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;
}
