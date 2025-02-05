package com.example.aquariux.core.repositories;

import com.example.aquariux.core.models.entities.AssetAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssetAccountRepository extends JpaRepository<AssetAccount, Long> {
    Optional<AssetAccount> findByUserAccountIdAndAssetId(long userAccountId, long assetId);
}
