package com.progress.finalproject.service.impl;

import com.progress.finalproject.exception.NotEnoughProductsInStockException;
import com.progress.finalproject.model.order.Order;
import com.progress.finalproject.model.order.OrderDetails;
import com.progress.finalproject.model.product.Product;
import com.progress.finalproject.repository.OrderDetailsRepository;
import com.progress.finalproject.repository.OrderRepository;
import com.progress.finalproject.service.OrderService;
import com.progress.finalproject.service.ProductService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    ProductService productService;

    @Override
    public List<Order> getUserOrders(long id) {
        return orderRepository.findAllByCustomerId(id);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<OrderDetails> getOrderDetails(long orderId) {
        return orderDetailsRepository.findAllByOrderId(orderId);
    }

    @Override
    @Transactional
    public void updateOrderStatus  (long orderId, long statusId) throws NotEnoughProductsInStockException {
        Order order = orderRepository.findById(orderId).get();
        updateAvailability(orderId, order.getStatusId(), statusId);
        order.setStatusId(statusId);
        orderRepository.saveAndFlush(order);

    }

    @Override
    public void updateAvailability(long orderId, long fromStatus, long toStatus) throws NotEnoughProductsInStockException {
        if (toStatus == 5) {
            System.out.println("first if - toStaus7");
            for (OrderDetails od : orderDetailsRepository.findAllByOrderId(orderId)) {
                productService.findById(od.getProductId()).ifPresent(product -> {
                    product.setAvailableUnits((int) (product.getAvailableUnits() + od.getQuantity()));
                    productService.saveProduct(product);
                });
            }
        }
        if (fromStatus == 5) {
            System.out.println("second if - fromStaus7");
            for (OrderDetails od : orderDetailsRepository.findAllByOrderId(orderId)) {
                Optional<Product> product = productService.findById(od.getProductId());
                if (product.get().getAvailableUnits() < od.getQuantity()) {
                    throw new NotEnoughProductsInStockException(product.get());
                } else {
                    product.get().setAvailableUnits((int) (product.get().getAvailableUnits() - od.getQuantity()));
                    productService.saveProduct(product.get());
                }

            }

        }
    }

    @Override
    public Map<Order, List<OrderDetails>> getOrderDetailsMap() {
        Map<Order, List<OrderDetails>> allOrdersWithDetails = new TreeMap<>();
        for (Order order : getAllOrders()) {
            allOrdersWithDetails.put(order, getOrderDetails(order.getOrderId()));


        }
        return allOrdersWithDetails;
    }

    @Override
    public Optional<Order> findOrderById(long orderId) {
        return orderRepository.findById(orderId);
    }
}