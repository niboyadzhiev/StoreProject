package com.progress.finalproject.controller;

import com.progress.finalproject.exception.NotEnoughProductsInStockException;
import com.progress.finalproject.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class OrderManagementController {

    @Autowired
    OrderService orderService;
    @Autowired
    OrderStatusService orderStatusService;

    @RequestMapping(value = "/orderManagement", method = RequestMethod.GET)
    public ModelAndView listAllOrders(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allOrdersMap", orderService.getOrderDetailsMap());
        modelAndView.addObject("orderStatuses", orderStatusService.getAllOrderStatuses());
        modelAndView.setViewName("orderManagement");
        return modelAndView;
    }

    @PostMapping("/orderUpdate")
    public ModelAndView updateOrderStatus (@RequestParam("orderId") long orderId, @RequestParam("statusId") long statusId){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView
                .addObject("allOrdersMap", orderService.getOrderDetailsMap())
                .addObject("orderStatuses", orderStatusService.getAllOrderStatuses());

        try {
            orderService.updateOrderStatus(orderId,statusId);
        } catch (NotEnoughProductsInStockException e) {
            return listAllOrders().addObject("outOfStockMessage", e.getMessage());
        }

        modelAndView.setViewName("redirect:/orderManagement");
        return modelAndView;
    }
}
