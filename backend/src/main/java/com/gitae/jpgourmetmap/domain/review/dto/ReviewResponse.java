package com.gitae.jpgourmetmap.domain.review.dto;

import com.gitae.jpgourmetmap.domain.review.Review;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        String nickname,
        BigDecimal rating,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ReviewResponse from(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getUser().getNickname(),
                review.getRating(),
                review.getContent(),
                review.getCreatedAt(),
                review.getUpdatedAt()
        );
    }
}
