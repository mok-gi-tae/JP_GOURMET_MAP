package com.gitae.jpgourmetmap.domain.review;

import com.gitae.jpgourmetmap.domain.review.dto.MessageResponse;
import com.gitae.jpgourmetmap.domain.review.dto.ReviewRequest;
import com.gitae.jpgourmetmap.domain.review.dto.ReviewResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 작성
    @PostMapping("/api/restaurants/{restaurantId}/reviews")
    public ResponseEntity<ReviewResponse> createReview(
            @PathVariable Long restaurantId,
            @Valid @RequestBody ReviewRequest request,
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getPrincipal();
        ReviewResponse response = reviewService.createReview(restaurantId, userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 맛집별 리뷰 목록 조회
    @GetMapping("/api/restaurants/{restaurantId}/reviews")
    public List<ReviewResponse> getReviews(@PathVariable Long restaurantId) {
        return reviewService.getReviews(restaurantId);
    }

    // 리뷰 수정 (본인 작성 리뷰만)
    @PutMapping("/api/reviews/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequest request,
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getPrincipal();
        return ResponseEntity.ok(reviewService.updateReview(reviewId, userId, request));
    }

    // 리뷰 삭제 (본인 작성 리뷰만)
    @DeleteMapping("/api/reviews/{reviewId}")
    public ResponseEntity<MessageResponse> deleteReview(
            @PathVariable Long reviewId,
            Authentication authentication
    ) {
        Long userId = (Long) authentication.getPrincipal();
        reviewService.deleteReview(reviewId, userId);
        return ResponseEntity.ok(new MessageResponse("Review deleted successfully."));
    }
}
