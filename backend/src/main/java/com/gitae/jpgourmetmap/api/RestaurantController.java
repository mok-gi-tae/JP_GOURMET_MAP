package com.gitae.jpgourmetmap.api;

import com.gitae.jpgourmetmap.api.dto.RestaurantDetailResponse;
import com.gitae.jpgourmetmap.api.dto.RestaurantSummaryResponse;
import com.gitae.jpgourmetmap.api.dto.ReviewResponse;
import com.gitae.jpgourmetmap.service.RestaurantQueryService;
import com.gitae.jpgourmetmap.service.ReviewQueryService;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantQueryService restaurantQueryService;
    private final ReviewQueryService reviewQueryService;

    @GetMapping
    public List<RestaurantSummaryResponse> getRestaurants(
            @RequestParam @Positive Long regionId
    ) {
        return restaurantQueryService.findByRegion(regionId);
    }

    @GetMapping("/{restaurantId}")
    public RestaurantDetailResponse getRestaurant(
            @PathVariable @Positive Long restaurantId
    ) {
        return restaurantQueryService.findById(restaurantId);
    }

    @GetMapping("/{restaurantId}/reviews")
    public List<ReviewResponse> getReviews(
            @PathVariable @Positive Long restaurantId
    ) {
        return reviewQueryService.findByRestaurant(restaurantId);
    }
}
