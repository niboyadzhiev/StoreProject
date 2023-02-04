package com.progress.finalproject.service.impl;


import com.progress.finalproject.exception.NotEnoughProductsInStockException;
import com.progress.finalproject.model.order.Order;
import com.progress.finalproject.model.order.OrderDetails;
import com.progress.finalproject.model.product.Product;
import com.progress.finalproject.model.user.User;
import com.progress.finalproject.repository.OrderDetailsRepository;
import com.progress.finalproject.repository.OrderRepository;
import com.progress.finalproject.repository.ProductRepository;
import com.progress.finalproject.service.EmailService;
import com.progress.finalproject.service.OrderService;
import com.progress.finalproject.service.ShoppingCartService;
import com.progress.finalproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Service
@SessionScope
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    UserService userService;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderDetailsRepository orderDetailsRepository;
    @Autowired
    EmailService emailService;



    private Map<Product, Integer> products = new HashMap<>();

    public ShoppingCartServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    public void addProduct(Product product, int quantity) {
        products.put(product, products.getOrDefault(product, 0) + quantity);
    }

    @Override
    public void removeProduct(Product product) {
        if (products.containsKey(product)) {
            if (products.get(product) > 1)
                products.replace(product, products.get(product) - 1);
            else if (products.get(product) == 1) {
                products.remove(product);
            }
        }
    }

    @Override
    public Map<Product, Integer> getProductsInCart() {
        return Collections.unmodifiableMap(products);
    }

    @Override
    public void checkAvailability() throws NotEnoughProductsInStockException {
        Optional<Product> product;
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            // Refresh quantity for every product before checking
            product = productRepository.findById(entry.getKey().getProductId());
            if (product.get().getAvailableUnits() < entry.getValue())
                throw new NotEnoughProductsInStockException(product.get());
            entry.getKey().setAvailableUnits(product.get().getAvailableUnits() - entry.getValue());
        }
    }
    @Override
    public void updateAvailability() {
        productRepository.saveAll(products.keySet());
        productRepository.flush();
        products.clear();

    }
    @Override
    public BigDecimal getTotal() {
        return products.entrySet().stream()
                .map(entry -> entry.getKey().getProductPrice().multiply(BigDecimal.valueOf(entry.getValue())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }
    @Override
    public Order createOrder(User user) {
        Order order = new Order();
        order.setCustomerId(user.getUserId());
        order.setStatusId(1);
        order.setOrderDate(Timestamp.from(Instant.now()));
        orderRepository.saveAndFlush(order);
        return order;
    }
    @Override
    public void generateOrderDetails(Order order) {
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setOrderId(order.getOrderId());
            orderDetails.setProductId(entry.getKey().getProductId());
            orderDetails.setQuantity(entry.getValue());
            orderDetails.setPrice(entry.getKey().getProductPrice());
            orderDetails.setVat(entry.getKey().getVat());
            orderDetailsRepository.saveAndFlush(orderDetails);
        }

    }

    @Override
    @Transactional
    public void placeOrder(Map<User, String> userPasswordMap) {
        userService.saveUser(userService.getUserFromMap(userPasswordMap));
        generateOrderDetails(createOrder(userService.getUserFromMap(userPasswordMap)));
        updateAvailability();
        if(userService.getPasswordFromMap(userPasswordMap) != null) {
            emailService.sendEmail(userService.getUserFromMap(userPasswordMap), userService.getPasswordFromMap(userPasswordMap));
        }

    }
}
