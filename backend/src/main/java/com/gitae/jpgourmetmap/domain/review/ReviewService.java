package com.gitae.jpgourmetmap.domain.review;

import com.gitae.jpgourmetmap.domain.restaurant.Restaurant;
import com.gitae.jpgourmetmap.domain.restaurant.RestaurantRepository;
import com.gitae.jpgourmetmap.domain.review.dto.ReviewRequest;
import com.gitae.jpgourmetmap.domain.review.dto.ReviewResponse;
import com.gitae.jpgourmetmap.domain.user.User;
import com.gitae.jpgourmetmap.domain.user.UserRepository;
import com.gitae.jpgourmetmap.exception.DuplicateReviewException;
import com.gitae.jpgourmetmap.exception.InvalidCredentialsException;
import com.gitae.jpgourmetmap.exception.RestaurantNotFoundException;
import com.gitae.jpgourmetmap.exception.ReviewAccessDeniedException;
import com.gitae.jpgourmetmap.exception.ReviewNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    // 리뷰 작성 (맛집당 1인 1리뷰)
    @Transactional
    public ReviewResponse createReview(Long restaurantId, Long userId, ReviewRequest request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("존재하지 않는 맛집입니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidCredentialsException("존재하지 않는 사용자입니다."));

        if (reviewRepository.existsByUserIdAndRestaurantId(userId, restaurantId)) {
            throw new DuplicateReviewException("이미 이 맛집에 리뷰를 작성했습니다.");
        }

        Review review = new Review(user, restaurant, request.rating(), request.content());
        return ReviewResponse.from(reviewRepository.save(review));
    }

    // 맛집별 리뷰 목록 조회
    public List<ReviewResponse> getReviews(Long restaurantId) {
        return reviewRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(ReviewResponse::from)
                .toList();
    }

    // 리뷰 수정 (본인 작성 리뷰만)
    @Transactional
    public ReviewResponse updateReview(Long reviewId, Long userId, ReviewRequest request) {
        Review review = getOwnedReview(reviewId, userId);
        review.update(request.rating(), request.content());
        return ReviewResponse.from(review);
    }

    // 리뷰 삭제 (본인 작성 리뷰만)
    @Transactional
    public void deleteReview(Long reviewId, Long userId) {
        Review review = getOwnedReview(reviewId, userId);
        reviewRepository.delete(review);
    }

    private Review getOwnedReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("존재하지 않는 리뷰입니다."));
        if (!review.getUser().getId().equals(userId)) {
            throw new ReviewAccessDeniedException("본인이 작성한 리뷰만 수정 또는 삭제할 수 있습니다.");
        }
        return review;
    }
}
