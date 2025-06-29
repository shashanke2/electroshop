package com.electroshop.controller;

import com.electroshop.dto.FeedbackDTO;
import com.electroshop.model.Feedback;
import com.electroshop.model.User;
import com.electroshop.service.FeedbackService;
import com.electroshop.service.UserService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private UserService userService;

    // Submit feedback
    @PostMapping
    public ResponseEntity<FeedbackDTO> submitFeedback(@Valid @RequestBody FeedbackDTO dto) {
        Feedback feedback = new Feedback();
        feedback.setMessage(dto.getMessage());

        if (dto.getUserId() != null) {
            User user = userService.getUserById(dto.getUserId());
            feedback.setUser(user);
        }

        Feedback saved = feedbackService.submitFeedback(feedback);
        return ResponseEntity.ok(mapToDTO(saved));
    }

    // Get feedback by ID
    @GetMapping("/{id}")
    public ResponseEntity<FeedbackDTO> getFeedbackById(@PathVariable Long id) {
        return ResponseEntity.ok(mapToDTO(feedbackService.getFeedbackById(id)));
    }

    // Get all feedback
    @GetMapping
    public ResponseEntity<List<FeedbackDTO>> getAllFeedback() {
        List<FeedbackDTO> list = feedbackService.getAllFeedback()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    // Delete feedback
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.ok("Feedback deleted successfully.");
    }

    // DTO to Entity
    private FeedbackDTO mapToDTO(Feedback f) {
        return new FeedbackDTO(
                f.getId(),
                f.getMessage(),
                f.getUser() != null ? f.getUser().getId() : null
        );
    }
}