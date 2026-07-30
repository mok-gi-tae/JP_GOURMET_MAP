package com.gitae.jpgourmetmap.service;

import com.gitae.jpgourmetmap.domain.region.Region;
import com.gitae.jpgourmetmap.domain.restaurant.Restaurant;
import com.gitae.jpgourmetmap.domain.restaurant.RestaurantRepository;
import com.gitae.jpgourmetmap.domain.review.Review;
import com.gitae.jpgourmetmap.domain.review.ReviewRepository;
import com.gitae.jpgourmetmap.domain.review.ReviewService;
import com.gitae.jpgourmetmap.domain.review.dto.ReviewRequest;
import com.gitae.jpgourmetmap.domain.review.dto.ReviewResponse;
import com.gitae.jpgourmetmap.domain.user.User;
import com.gitae.jpgourmetmap.domain.user.UserRepository;
import com.gitae.jpgourmetmap.exception.DuplicateReviewException;
import com.gitae.jpgourmetmap.exception.RestaurantNotFoundException;
import com.gitae.jpgourmetmap.exception.ReviewAccessDeniedException;
import com.gitae.jpgourmetmap.exception.ReviewNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReviewService reviewService;

    private User userWithId(long id) {
        User user = new User("gitae@example.com", "encoded-password", "gita");
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }

    private Restaurant restaurant() {
        Region region = new Region(
                "Shinjuku", "Tokyo",
                new BigDecimal("35.6938000"), new BigDecimal("139.7034000")
        );
        Restaurant restaurant = new Restaurant(
                region,
                "Ichiran Shinjuku",
                "Ramen",
                "Tokyo, Shinjuku...",
                new BigDecimal("35.6900000"),
                new BigDecimal("139.7000000"),
                new BigDecimal("3.52"),
                "https://tabelog.com/...",
                "https://youtube.com/..."
        );
        ReflectionTestUtils.setField(restaurant, "id", 10L);
        return restaurant;
    }

    @Test
    void 리뷰_작성_성공() {
        Restaurant restaurant = restaurant();
        User user = userWithId(1L);
        ReviewRequest request = new ReviewRequest(new BigDecimal("4.5"), "맛있습니다.");

        when(restaurantRepository.findById(10L)).thenReturn(Optional.of(restaurant));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(reviewRepository.existsByUserIdAndRestaurantId(1L, 10L)).thenReturn(false);
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReviewResponse response = reviewService.createReview(10L, 1L, request);

        assertThat(response.nickname()).isEqualTo("gita");
        assertThat(response.rating()).isEqualByComparingTo("4.5");
        assertThat(response.content()).isEqualTo("맛있습니다.");
    }

    @Test
    void 리뷰_작성_맛집_없으면_예외() {
        ReviewRequest request = new ReviewRequest(new BigDecimal("4.5"), "맛있습니다.");
        when(restaurantRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.createReview(10L, 1L, request))
                .isInstanceOf(RestaurantNotFoundException.class);

        verify(reviewRepository, never()).save(any());
    }

    @Test
    void 리뷰_작성_이미_작성했으면_예외() {
        Restaurant restaurant = restaurant();
        User user = userWithId(1L);
        ReviewRequest request = new ReviewRequest(new BigDecimal("4.5"), "맛있습니다.");

        when(restaurantRepository.findById(10L)).thenReturn(Optional.of(restaurant));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(reviewRepository.existsByUserIdAndRestaurantId(1L, 10L)).thenReturn(true);

        assertThatThrownBy(() -> reviewService.createReview(10L, 1L, request))
                .isInstanceOf(DuplicateReviewException.class);

        verify(reviewRepository, never()).save(any());
    }

    @Test
    void 리뷰_목록_조회() {
        Restaurant restaurant = restaurant();
        User user = userWithId(1L);
        Review review = new Review(user, restaurant, new BigDecimal("4.0"), "괜찮아요.");

        when(reviewRepository.findByRestaurantId(10L)).thenReturn(List.of(review));

        List<ReviewResponse> responses = reviewService.getReviews(10L);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).nickname()).isEqualTo("gita");
    }

    @Test
    void 리뷰_수정_본인_리뷰면_성공() {
        Restaurant restaurant = restaurant();
        User owner = userWithId(1L);
        Review review = new Review(owner, restaurant, new BigDecimal("3.0"), "그냥 그래요.");
        ReflectionTestUtils.setField(review, "id", 100L);
        ReviewRequest request = new ReviewRequest(new BigDecimal("4.0"), "다시 가보니 괜찮았습니다.");

        when(reviewRepository.findById(100L)).thenReturn(Optional.of(review));

        ReviewResponse response = reviewService.updateReview(100L, 1L, request);

        assertThat(response.rating()).isEqualByComparingTo("4.0");
        assertThat(response.content()).isEqualTo("다시 가보니 괜찮았습니다.");
    }

    @Test
    void 리뷰_수정_존재하지_않으면_예외() {
        ReviewRequest request = new ReviewRequest(new BigDecimal("4.0"), "내용");
        when(reviewRepository.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.updateReview(100L, 1L, request))
                .isInstanceOf(ReviewNotFoundException.class);
    }

    @Test
    void 리뷰_수정_본인_리뷰가_아니면_예외() {
        Restaurant restaurant = restaurant();
        User owner = userWithId(1L);
        Review review = new Review(owner, restaurant, new BigDecimal("3.0"), "그냥 그래요.");
        ReflectionTestUtils.setField(review, "id", 100L);
        ReviewRequest request = new ReviewRequest(new BigDecimal("4.0"), "수정 시도");

        when(reviewRepository.findById(100L)).thenReturn(Optional.of(review));

        assertThatThrownBy(() -> reviewService.updateReview(100L, 2L, request))
                .isInstanceOf(ReviewAccessDeniedException.class);
    }

    @Test
    void 리뷰_삭제_본인_리뷰면_성공() {
        Restaurant restaurant = restaurant();
        User owner = userWithId(1L);
        Review review = new Review(owner, restaurant, new BigDecimal("3.0"), "그냥 그래요.");
        ReflectionTestUtils.setField(review, "id", 100L);

        when(reviewRepository.findById(100L)).thenReturn(Optional.of(review));

        reviewService.deleteReview(100L, 1L);

        verify(reviewRepository, times(1)).delete(review);
    }

    @Test
    void 리뷰_삭제_본인_리뷰가_아니면_예외() {
        Restaurant restaurant = restaurant();
        User owner = userWithId(1L);
        Review review = new Review(owner, restaurant, new BigDecimal("3.0"), "그냥 그래요.");
        ReflectionTestUtils.setField(review, "id", 100L);

        when(reviewRepository.findById(100L)).thenReturn(Optional.of(review));

        assertThatThrownBy(() -> reviewService.deleteReview(100L, 2L))
                .isInstanceOf(ReviewAccessDeniedException.class);

        verify(reviewRepository, never()).delete(any());
    }
}
