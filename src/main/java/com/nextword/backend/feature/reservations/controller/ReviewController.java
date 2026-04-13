package com.nextword.backend.feature.reservations.controller;

import com.nextword.backend.feature.reservations.dto.ReviewRequest;
import com.nextword.backend.feature.reservations.services.ReviewServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewServices reviewService;

    public ReviewController(ReviewServices reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<String> leaveReview(@RequestBody ReviewRequest request) {
        String message = reviewService.createReview(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

}
