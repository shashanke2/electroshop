package com.electroshop.service;

import com.electroshop.model.Feedback;

import java.util.List;

public interface FeedbackService {
    Feedback submitFeedback(Feedback feedback);
    Feedback getFeedbackById(Long id);
    List<Feedback> getAllFeedback();
    void deleteFeedback(Long id);
}
