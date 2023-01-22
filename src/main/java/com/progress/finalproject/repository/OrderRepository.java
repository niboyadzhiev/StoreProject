package com.progress.finalproject.repository;

import com.progress.finalproject.model.order.Order;
import com.progress.finalproject.model.product.Product;
import com.progress.finalproject.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByCustomerId(long customerId);
}
