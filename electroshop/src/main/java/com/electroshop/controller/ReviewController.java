package com.electroshop.controller;

import com.electroshop.model.Product;
import com.electroshop.model.Review;
import com.electroshop.model.User;
import com.electroshop.service.ProductService;
import com.electroshop.service.ReviewService;
import com.electroshop.service.UserService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    // Add Review
    @PostMapping
    public ResponseEntity<Review> createReview(@Valid @RequestBody Review review) {
        Product product = productService.getProductById(review.getProduct().getId());
        User user = userService.getUserById(review.getUser().getId());

        review.setProduct(product);
        review.setUser(user);

        Review saved = reviewService.addReview(review);
        return ResponseEntity.ok(saved);
    }

    // Get review by ID
    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    // Get all reviews for product
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getReviewsByProductId(@PathVariable Long productId) {
        List<Review> list = reviewService.getReviewsByProductId(productId);
        return ResponseEntity.ok(list);
    }

    // Delete Review
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok("Review deleted successfully.");
    }

    // Get all reviews
    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> list = reviewService.getAllReviews();
        return ResponseEntity.ok(list);
    }
}
