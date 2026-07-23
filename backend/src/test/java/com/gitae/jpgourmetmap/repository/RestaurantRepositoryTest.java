package com.gitae.jpgourmetmap.repository;

import com.gitae.jpgourmetmap.domain.Region;
import com.gitae.jpgourmetmap.domain.Restaurant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class RestaurantRepositoryTest {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    void 음식점_지역으로_조회하기() {
        Region region = regionRepository.save(
                new Region(
                        "Tokyo",
                        "Tokyo",
                        new BigDecimal(1.1),
                        new BigDecimal(2.2)
                )
        );

        Restaurant restaurant = new Restaurant(
                region,
                "gogoCurry",
                "curry",
                "abcdefg",
                new BigDecimal(1.1),
                new BigDecimal(2.2),
                new BigDecimal(3.0),
                "abc/def",
                "ghi/jkl"
        );
        restaurantRepository.save(restaurant);

        List<Restaurant> result = restaurantRepository.findByRegionId(
                region.getId()
        );

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getCategory()).isEqualTo("curry");

    }

}
