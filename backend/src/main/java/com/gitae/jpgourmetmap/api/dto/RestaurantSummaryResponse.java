package com.gitae.jpgourmetmap.api.dto;

import com.gitae.jpgourmetmap.domain.Restaurant;
import java.math.BigDecimal;

public record RestaurantSummaryResponse(
        Long id,
        String name,
        String category,
        BigDecimal tabelogScore,
        BigDecimal latitude,
        BigDecimal longitude
) {
    public static RestaurantSummaryResponse from(Restaurant restaurant) {
        return new RestaurantSummaryResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getCategory(),
                restaurant.getTabelogScore(),
                restaurant.getLatitude(),
                restaurant.getLongitude()
        );
    }
}
