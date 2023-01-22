package com.progress.finalproject.service;

import com.progress.finalproject.exception.NotEnoughProductsInStockException;
import com.progress.finalproject.model.order.Order;
import com.progress.finalproject.model.order.OrderDetails;
import com.progress.finalproject.model.product.Product;
import com.progress.finalproject.model.user.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OrderService {

    List<Order> getUserOrders(long customerId);

    List<Order> getAllOrders();
    List<OrderDetails> getOrderDetails(long orderId);


    void updateOrderStatus(long orderId, long statusId) throws NotEnoughProductsInStockException;

    void updateAvailability(long orderId, long fromStatus, long toStatus) throws NotEnoughProductsInStockException;

    Map<Order, List<OrderDetails>> getOrderDetailsMap ();

    Optional<Order> findOrderById(long orderId);

}
