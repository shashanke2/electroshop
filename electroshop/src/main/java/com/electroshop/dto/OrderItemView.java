package com.electroshop.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemView {
    private Long productId;
    private String productName;
    private int quantity;
    private double price; 
}
