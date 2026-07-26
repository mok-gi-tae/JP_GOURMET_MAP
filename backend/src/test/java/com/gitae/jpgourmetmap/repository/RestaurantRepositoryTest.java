package com.gitae.jpgourmetmap.repository;

import com.gitae.jpgourmetmap.region.Region;
import com.gitae.jpgourmetmap.restaurant.Restaurant;
import com.gitae.jpgourmetmap.region.RegionRepository;
import com.gitae.jpgourmetmap.restaurant.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

@DataJpaTest
public class RestaurantRepositoryTest {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    void 음식점_지역으로_조회하기() {
        Region tokyo = regionRepository.save(
                new Region(
                        "Tokyo",
                        "Tokyo",
                        new BigDecimal("1.1000000000"),
                        new BigDecimal("2.2000000000")
                )
        );

        Region osaka = regionRepository.save(
                new Region(
                        "Osaka",
                        "Osaka",
                        new BigDecimal("2.2000000000"),
                        new BigDecimal("1.1000000000")
                )
        );

        Restaurant tokyoRestaurant = new Restaurant(
                tokyo,
                "gogoCurry",
                "curry",
                "abcdefg",
                new BigDecimal("1.1000000000"),
                new BigDecimal("2.2000000000"),
                new BigDecimal("3.000"),
                "abc/def",
                "ghi/jkl"
        );

        Restaurant osakaRestaurant = new Restaurant(
                osaka,
                "gogoCurry",
                "curry",
                "abcdefg",
                new BigDecimal("2.2000000000"),
                new BigDecimal("1.1000000000"),
                new BigDecimal("4.000"),
                "abc/def",
                "ghi/jkl"
        );
        restaurantRepository.save(tokyoRestaurant);
        restaurantRepository.save(osakaRestaurant);

        List<Restaurant> result = restaurantRepository.findByRegionId(
                tokyo.getId()
        );

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRegion().getId()).isEqualTo(tokyo.getId());
        assertThat(result.get(0).getId()).isEqualTo(tokyoRestaurant.getId());
    }

}
