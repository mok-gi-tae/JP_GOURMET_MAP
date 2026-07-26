package com.gitae.jpgourmetmap.domain.restaurant;

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
