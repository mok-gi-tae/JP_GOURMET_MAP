package com.gitae.jpgourmetmap.domain.restaurant;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    // regionId 요청 받으면 그 regionId의 restaurants들 List로 반환
    @GetMapping
    public List<RestaurantResponse> getRestaurantsByRegion(
            @RequestParam Long regionId
    ) {
        return restaurantService.getRestaurantsByRegion(regionId);
    }

}
