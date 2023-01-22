package com.progress.finalproject.service;


import com.progress.finalproject.exception.NotEnoughProductsInStockException;
import com.progress.finalproject.model.order.Order;
import com.progress.finalproject.model.order.OrderDetails;
import com.progress.finalproject.model.product.Product;
import com.progress.finalproject.model.user.User;

import java.beans.Customizer;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ShoppingCartService {

    void addProduct(Product product, int quantity);

    void removeProduct(Product product);

    Map<Product, Integer> getProductsInCart();

    void checkAvailability() throws NotEnoughProductsInStockException;


    void updateAvailability();

    BigDecimal getTotal();

    Order createOrder(User user);

    void generateOrderDetails(Order order);

    void placeOrder(Map<User, String> userPasswordMap);



}
