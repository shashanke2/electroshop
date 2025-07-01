package com.electroshop.service;

import com.electroshop.model.OrderItem;

import java.util.List;

public interface OrderItemService {

    // Read-only method
    List<OrderItem> getItemsByOrderId(Long orderId);
}
