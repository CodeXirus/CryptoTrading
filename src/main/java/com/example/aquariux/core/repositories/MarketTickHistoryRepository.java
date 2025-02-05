package com.example.aquariux.core.repositories;

import com.example.aquariux.core.models.entities.MarketTickHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketTickHistoryRepository extends JpaRepository<MarketTickHistory, Long> {
}
