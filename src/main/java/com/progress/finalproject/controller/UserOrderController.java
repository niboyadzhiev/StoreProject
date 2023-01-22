package com.progress.finalproject.controller;

import com.progress.finalproject.model.order.Order;
import com.progress.finalproject.model.order.OrderDetails;
import com.progress.finalproject.service.OrderService;
import com.progress.finalproject.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.*;

@Controller
public class UserOrderController {
    @Autowired
    UserService userService;
    @Autowired
    OrderService orderService;


    @RequestMapping(value = "/myOrders", method = RequestMethod.GET)
    public ModelAndView myOrders(Principal principal){
        ModelAndView modelAndView = new ModelAndView();
        Map<Order, List<OrderDetails>> customerOrdersWithDetails= new TreeMap<>();

        List<Order> orders = orderService.getUserOrders(userService.findByEmail(principal.getName()).get().getUserId());
        for (Order order : orders) {
            customerOrdersWithDetails.put(order, orderService.getOrderDetails(order.getOrderId()));
        }


        modelAndView.addObject("ordersMap", customerOrdersWithDetails);
        modelAndView.setViewName("/myOrders");


        return modelAndView;
    }
}
