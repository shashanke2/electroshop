package com.electroshop.service;

import com.electroshop.model.Order;

import java.util.List;

public interface OrderService {

    Order placeOrder(Order order);

    Order getOrderById(Long id);
        
    List<Order> getOrdersByUserId(Long userId);

    List<Order> getAllOrders();

    void deleteOrder(Long id);
}
