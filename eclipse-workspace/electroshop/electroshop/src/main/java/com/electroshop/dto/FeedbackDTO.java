package com.electroshop.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDTO {

    private Long id;

    @NotBlank(message = "Feedback message cannot be blank")
    private String message;

    private Long userId; // Can be null (anonymous feedback)
}
