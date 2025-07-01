package com.electroshop.controller;

import com.electroshop.dto.OrderDTO;
import com.electroshop.dto.OrderItemDTO;
import com.electroshop.model.Order;
import com.electroshop.model.OrderItem;
import com.electroshop.model.Product;
import com.electroshop.model.User;
import com.electroshop.service.OrderService;
import com.electroshop.service.ProductService;
import com.electroshop.service.UserService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<OrderDTO> placeOrder(@Valid @RequestBody OrderDTO orderDTO) {
        User user = userService.getUserById(orderDTO.getUserId());

        Order order = new Order();
        order.setOrderDate(orderDTO.getOrderDate());
        order.setUser(user);

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0;

        for (OrderItemDTO itemDTO : orderDTO.getOrderItems()) {
            Product product = productService.getProductById(itemDTO.getProductId());
            int requestedQty = itemDTO.getQuantity();

            productService.reduceStock(product.getId(), requestedQty);

            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(requestedQty);
            item.setOrder(order); // back-reference

            double itemPrice = product.getPrice();
            item.setPrice(itemPrice);

            totalAmount += itemPrice * requestedQty;
            orderItems.add(item);
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderService.placeOrder(order);
        return ResponseEntity.ok(mapToDTO(savedOrder));
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        org.springframework.security.core.Authentication authentication = 
            org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName(); // by default, email from login form

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        User user = userService.getUserByEmail(email); // use email here

        List<Order> orders;
        if (isAdmin) {
            orders = orderService.getAllOrders();
        } else {
            orders = orderService.getOrdersByUserId(user.getId());
        }

        List<OrderDTO> dtos = orders.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }


    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(mapToDTO(orderService.getOrderById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order deleted successfully.");
    }

    private OrderDTO mapToDTO(Order order) {
        List<OrderItemDTO> itemDTOs = order.getOrderItems().stream()
                .map(item -> new OrderItemDTO(
                        item.getId(),
                        item.getProduct().getId(),
                        item.getQuantity()
                ))
                .collect(Collectors.toList());

        return new OrderDTO(
                order.getId(),
                order.getOrderDate(),
                order.getTotalAmount(),
                order.getUser().getId(),
                itemDTOs
        );
    }
}
