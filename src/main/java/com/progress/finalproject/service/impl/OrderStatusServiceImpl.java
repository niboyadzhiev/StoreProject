package com.progress.finalproject.service.impl;

import com.progress.finalproject.model.order.OrderStatus;
import com.progress.finalproject.repository.OrderStatusRepository;
import com.progress.finalproject.service.OrderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OrderStatusServiceImpl implements OrderStatusService {
    @Autowired
    OrderStatusRepository orderStatusRepository;


    @Override
    public List<OrderStatus> getAllOrderStatuses() {
        return orderStatusRepository.findAll();
    }

}
