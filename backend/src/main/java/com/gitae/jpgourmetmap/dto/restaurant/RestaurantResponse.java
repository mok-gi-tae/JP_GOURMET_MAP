package com.gitae.jpgourmetmap.dto.restaurant;

import com.gitae.jpgourmetmap.domain.Region;
import com.gitae.jpgourmetmap.domain.Restaurant;
import jakarta.persistence.*;

import java.math.BigDecimal;

public record RestaurantResponse(
        Long id,
        Region region,
        String name,
        String category,
        String address,
        BigDecimal latitude,
        BigDecimal longitude,
        BigDecimal tabelogScore,
        String tabelogUrl,
        String youtubeUrl
) {
    public static RestaurantResponse from(Restaurant restaurant) {
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getRegion(),
                restaurant.getName(),
                restaurant.getCategory(),
                restaurant.getAddress(),
                restaurant.getLatitude(),
                restaurant.getLongitude(),
                restaurant.getTabelogScore(),
                restaurant.getTabelogUrl(),
                restaurant.getYoutubeUrl()
        );
    }
}
