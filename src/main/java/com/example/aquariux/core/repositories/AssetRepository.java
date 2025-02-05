package com.example.aquariux.core.repositories;

import com.example.aquariux.core.models.entities.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    Asset findByName(String name);
    Asset findBySymbol(String symbol);
}
