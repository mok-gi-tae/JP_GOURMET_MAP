package com.gitae.jpgourmetmap.region;

import java.math.BigDecimal;

public record RegionResponse(
        Long id,
        String name,
        String city,
        BigDecimal latitude,
        BigDecimal longitude
) {
    public static RegionResponse from(Region region) {
        return new RegionResponse(
                region.getId(),
                region.getName(),
                region.getCity(),
                region.getLatitude(),
                region.getLongitude()
        );
    }
}
