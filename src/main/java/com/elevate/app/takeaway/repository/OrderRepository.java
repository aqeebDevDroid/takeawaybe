package com.elevate.app.takeaway.repository;

import com.elevate.app.takeaway.dto.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(value = "SELECT * FROM orders WHERE user_Id=?1", nativeQuery = true)
    Optional<List<Order>> getOrdersByUserId(long userId);
}
