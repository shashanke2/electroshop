package com.electroshop.controller;

import com.electroshop.dto.OrderItemView;
import com.electroshop.model.OrderItem;
import com.electroshop.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    // Get all items by orderId
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItemView>> getItemsByOrderId(@PathVariable Long orderId) {
        List<OrderItemView> responseList = orderItemService.getItemsByOrderId(orderId)
                .stream()
                .map(this::mapToView)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    // Convert OrderItem → OrderItemView
    private OrderItemView mapToView(OrderItem item) {
        return new OrderItemView(
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getPrice()
        );
    }
}
