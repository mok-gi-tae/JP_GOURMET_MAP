package com.gitae.jpgourmetmap.domain.restaurant;

import com.gitae.jpgourmetmap.domain.review.ReviewRepository;
import com.gitae.jpgourmetmap.exception.RestaurantNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;

    // RestaurantRepository에 있는 모든 행 조회 후 객체로 return
    public List<RestaurantResponse> getRestaurantsByRegion(Long regionId) {
        return restaurantRepository.findByRegionId(regionId)
                .stream()
                .map(RestaurantResponse::from)
                .toList();
    }

    // 맛집 상세 조회 (리뷰 평균 평점, 리뷰 수 포함)
    public RestaurantDetailResponse getRestaurantDetail(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("존재하지 않는 맛집입니다."));

        long reviewCount = reviewRepository.countByRestaurantId(restaurantId);
        BigDecimal averageUserRating = reviewRepository.findAverageRatingByRestaurantId(restaurantId)
                .map(average -> average.setScale(1, RoundingMode.HALF_UP))
                .orElse(null);

        return RestaurantDetailResponse.of(restaurant, averageUserRating, reviewCount);
    }

}
