package com.electroshop.service;

import com.electroshop.model.Review;

import java.util.List;

public interface ReviewService {
    Review addReview(Review review);
    Review getReviewById(Long id);
    List<Review> getReviewsByProductId(Long productId);
    void deleteReview(Long id);
    List<Review> getAllReviews();

}
