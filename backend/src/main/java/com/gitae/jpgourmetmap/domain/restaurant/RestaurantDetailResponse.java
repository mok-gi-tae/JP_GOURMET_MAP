package com.gitae.jpgourmetmap.domain.restaurant;

import java.math.BigDecimal;

public record RestaurantDetailResponse(
        Long id,
        String name,
        String category,
        String address,
        BigDecimal latitude,
        BigDecimal longitude,
        BigDecimal tabelogScore,
        String tabelogUrl,
        String youtubeUrl,
        BigDecimal averageUserRating,
        long reviewCount
) {
    public static RestaurantDetailResponse of(Restaurant restaurant, BigDecimal averageUserRating, long reviewCount) {
        return new RestaurantDetailResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getCategory(),
                restaurant.getAddress(),
                restaurant.getLatitude(),
                restaurant.getLongitude(),
                restaurant.getTabelogScore(),
                restaurant.getTabelogUrl(),
                restaurant.getYoutubeUrl(),
                averageUserRating,
                reviewCount
        );
    }
}
