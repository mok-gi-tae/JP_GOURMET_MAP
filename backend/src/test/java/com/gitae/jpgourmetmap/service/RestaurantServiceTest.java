package com.gitae.jpgourmetmap.service;

import com.gitae.jpgourmetmap.domain.region.Region;
import com.gitae.jpgourmetmap.domain.restaurant.Restaurant;
import com.gitae.jpgourmetmap.domain.restaurant.RestaurantDetailResponse;
import com.gitae.jpgourmetmap.domain.restaurant.RestaurantRepository;
import com.gitae.jpgourmetmap.domain.restaurant.RestaurantService;
import com.gitae.jpgourmetmap.domain.review.ReviewRepository;
import com.gitae.jpgourmetmap.exception.RestaurantNotFoundException;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private RestaurantService restaurantService;

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
        ReflectionTestUtils.setField(restaurant, "id", 1L);
        return restaurant;
    }

    @Test
    void 맛집_상세_조회_리뷰가_있으면_평균평점_반환() {
        Restaurant restaurant = restaurant();
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(reviewRepository.countByRestaurantId(1L)).thenReturn(3L);
        when(reviewRepository.findAverageRatingByRestaurantId(1L))
                .thenReturn(Optional.of(new BigDecimal("4.3333333")));

        RestaurantDetailResponse response = restaurantService.getRestaurantDetail(1L);

        assertThat(response.name()).isEqualTo("Ichiran Shinjuku");
        assertThat(response.reviewCount()).isEqualTo(3L);
        assertThat(response.averageUserRating()).isEqualByComparingTo("4.3");
    }

    @Test
    void 맛집_상세_조회_리뷰가_없으면_평균평점은_null() {
        Restaurant restaurant = restaurant();
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(reviewRepository.countByRestaurantId(1L)).thenReturn(0L);
        when(reviewRepository.findAverageRatingByRestaurantId(1L)).thenReturn(Optional.empty());

        RestaurantDetailResponse response = restaurantService.getRestaurantDetail(1L);

        assertThat(response.reviewCount()).isEqualTo(0L);
        assertThat(response.averageUserRating()).isNull();
    }

    @Test
    void 맛집_상세_조회_존재하지_않으면_예외() {
        when(restaurantRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> restaurantService.getRestaurantDetail(999L))
                .isInstanceOf(RestaurantNotFoundException.class);
    }
}
