package com.example.aquariux.core.repositories;

import com.example.aquariux.core.models.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderIdAndUserAccountId(long orderId, long userAccountId);
    List<Order> findAllByUserAccountId(long userAccountId);
}
