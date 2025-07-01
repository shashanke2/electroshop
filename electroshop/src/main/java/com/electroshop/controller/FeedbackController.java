package com.electroshop.controller;

import com.electroshop.model.Feedback;
import com.electroshop.model.User;
import com.electroshop.service.FeedbackService;
import com.electroshop.service.UserService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private UserService userService;

    // Submit feedback
    @PostMapping
    public ResponseEntity<Feedback> submitFeedback(@Valid @RequestBody Feedback feedback) {
        if (feedback.getUser() != null && feedback.getUser().getId() != null) {
            User user = userService.getUserById(feedback.getUser().getId());
            feedback.setUser(user);
        }

        Feedback saved = feedbackService.submitFeedback(feedback);
        return ResponseEntity.ok(saved);
    }

    // Get feedback by ID
    @GetMapping("/{id}")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable Long id) {
        return ResponseEntity.ok(feedbackService.getFeedbackById(id));
    }

    // Get all feedback
    @GetMapping
    public ResponseEntity<List<Feedback>> getAllFeedback() {
        List<Feedback> list = feedbackService.getAllFeedback();
        return ResponseEntity.ok(list);
    }

    // Delete feedback
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.ok("Feedback deleted successfully.");
    }
}
