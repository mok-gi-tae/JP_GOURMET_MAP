package com.gitae.jpgourmetmap.service;

import com.gitae.jpgourmetmap.api.dto.RestaurantDetailResponse;
import com.gitae.jpgourmetmap.api.dto.RestaurantSummaryResponse;
import com.gitae.jpgourmetmap.domain.Restaurant;
import com.gitae.jpgourmetmap.error.ResourceNotFoundException;
import com.gitae.jpgourmetmap.repository.RegionRepository;
import com.gitae.jpgourmetmap.repository.RestaurantRepository;
import com.gitae.jpgourmetmap.repository.ReviewRepository;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantQueryService {

    private final RegionRepository regionRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;

    public List<RestaurantSummaryResponse> findByRegion(Long regionId) {
        if (!regionRepository.existsById(regionId)) {
            throw new ResourceNotFoundException("Region", regionId);
        }

        return restaurantRepository.findAllByRegionIdOrderByNameAsc(regionId).stream()
                .map(RestaurantSummaryResponse::from)
                .toList();
    }

    public RestaurantDetailResponse findById(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant", restaurantId));
        Double averageRatingValue = reviewRepository.findAverageRatingByRestaurantId(restaurantId);
        BigDecimal averageRating = averageRatingValue == null
                ? null
                : BigDecimal.valueOf(averageRatingValue);
        long reviewCount = reviewRepository.countByRestaurantId(restaurantId);

        return RestaurantDetailResponse.of(restaurant, averageRating, reviewCount);
    }
}
