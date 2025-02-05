package com.example.aquariux.core.repositories;

import com.example.aquariux.core.models.entities.Market;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarketRepository extends JpaRepository<Market, Long> {
    Optional<Market> findBySymbol(String symbol);
    List<Market> findByBaseAssetId(long baseAssetId);
    List<Market> findByQuoteAssetId(long quoteAssetId);
}
