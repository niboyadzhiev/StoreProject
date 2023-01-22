package com.progress.finalproject.repository;

import com.progress.finalproject.model.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {
}
