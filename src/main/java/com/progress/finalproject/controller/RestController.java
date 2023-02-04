package com.progress.finalproject.controller;

import com.progress.finalproject.model.order.Order;
import com.progress.finalproject.model.product.Product;
import com.progress.finalproject.service.OrderService;
import com.progress.finalproject.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.web.bind.annotation.RestController
public class RestController {
    @Autowired
    ProductService productService;
    @Autowired
    OrderService orderService;

    @RequestMapping(value = "/getProduct", method = RequestMethod.GET)
    public Product product (@RequestParam("id") long productId) {
        return productService.findById(productId).orElse(new Product());
    }
    @RequestMapping(value = "/getOrder", method = RequestMethod.GET)
    public Order order (@RequestParam("id") long orderId) {
        return orderService.findOrderById(orderId).orElse(new Order());
    }

}
