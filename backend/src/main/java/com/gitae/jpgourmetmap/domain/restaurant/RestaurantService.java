package com.gitae.jpgourmetmap.domain.restaurant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    // RestaurantRepository에 있는 모든 행 조회 후 객체로 return
    public List<RestaurantResponse> getRestaurantsByRegion(Long regionId) {
        return restaurantRepository.findByRegionId(regionId)
                .stream()
                .map(RestaurantResponse::from)
                .toList();
    }

}
