package com.electroshop.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Long id;

    @NotNull(message = "Order date is required")
    private LocalDate orderDate;

    private double totalAmount;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Order items are required")
    @Size(min = 1, message = "At least one item is required")
    private List<OrderItemDTO> orderItems;
}
