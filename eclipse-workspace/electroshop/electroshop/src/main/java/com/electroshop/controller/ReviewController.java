package com.electroshop.controller;

import com.electroshop.dto.ReviewDTO;
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
import java.util.stream.Collectors;

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
    public ResponseEntity<ReviewDTO> createReview(@Valid @RequestBody ReviewDTO dto) {
        Product product = productService.getProductById(dto.getProductId());
        User user = userService.getUserById(dto.getUserId());

        Review review = new Review();
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        review.setProduct(product);
        review.setUser(user);

        Review saved = reviewService.addReview(review);
        return ResponseEntity.ok(mapToDTO(saved));
    }

    // Get review by ID
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok(mapToDTO(reviewService.getReviewById(id)));
    }

    // Get all reviews for product
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByProductId(@PathVariable Long productId) {
        List<ReviewDTO> list = reviewService.getReviewsByProductId(productId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

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
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        List<ReviewDTO> list = reviewService.getAllReviews()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }
    
    // DTO to Entity
    private ReviewDTO mapToDTO(Review r) {
        return new ReviewDTO(
                r.getId(),
                r.getRating(),
                r.getComment(),
                r.getProduct().getId(),
                r.getUser().getId()
        );
    }
}