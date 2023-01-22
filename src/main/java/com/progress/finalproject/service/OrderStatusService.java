package com.progress.finalproject.service;

import com.progress.finalproject.model.order.Order;
import com.progress.finalproject.model.order.OrderStatus;

import java.util.List;

public interface OrderStatusService {
    List<OrderStatus> getAllOrderStatuses();

}
