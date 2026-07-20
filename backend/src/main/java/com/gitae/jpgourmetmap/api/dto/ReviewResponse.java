package com.gitae.jpgourmetmap.api.dto;

import com.gitae.jpgourmetmap.domain.Review;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        String nickname,
        BigDecimal rating,
        String content,
        LocalDateTime createdAt
) {
    public static ReviewResponse from(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getUser().getNickname(),
                review.getRating(),
                review.getContent(),
                review.getCreatedAt()
        );
    }
}
