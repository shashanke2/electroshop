package com.electroshop.repository;

import com.electroshop.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
