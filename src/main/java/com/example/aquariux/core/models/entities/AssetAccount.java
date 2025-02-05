package com.example.aquariux.core.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "asset_accounts")
@Getter
@Setter
@NoArgsConstructor
public class AssetAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assetAccountId;

    private long assetId;
    private double quantity;

    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;
}
