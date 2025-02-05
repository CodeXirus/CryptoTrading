package com.example.aquariux.core.repositories;

import com.example.aquariux.core.models.entities.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {
    List<Trade> findAllByUserAccountId(long userAccountId);
    Optional<Trade> findByTradeIdAndUserAccountId(long tradeId, long userAccountId);
}
