package com.gitae.jpgourmetmap.dto.restaurant;

import com.gitae.jpgourmetmap.domain.Region;
import com.gitae.jpgourmetmap.domain.Restaurant;

import java.math.BigDecimal;

public record RestaurantResponse(
        Long id,
        String name,
        String category,
        BigDecimal latitude,
        BigDecimal longitude,
        BigDecimal tabelogScore
) {
    public static RestaurantResponse from(Restaurant restaurant) {
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getCategory(),
                restaurant.getLatitude(),
                restaurant.getLongitude(),
                restaurant.getTabelogScore()
        );
    }
}
