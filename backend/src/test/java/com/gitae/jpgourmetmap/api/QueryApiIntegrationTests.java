package com.gitae.jpgourmetmap.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gitae.jpgourmetmap.domain.Region;
import com.gitae.jpgourmetmap.domain.Restaurant;
import com.gitae.jpgourmetmap.domain.Review;
import com.gitae.jpgourmetmap.domain.User;
import com.gitae.jpgourmetmap.repository.RegionRepository;
import com.gitae.jpgourmetmap.repository.RestaurantRepository;
import com.gitae.jpgourmetmap.repository.ReviewRepository;
import com.gitae.jpgourmetmap.repository.UserRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class QueryApiIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    private Region region;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        region = regionRepository.save(new Region(
                "Shinjuku",
                "Tokyo",
                new BigDecimal("35.6938000"),
                new BigDecimal("139.7034000")
        ));
        restaurant = restaurantRepository.save(new Restaurant(
                region,
                "Ichiran Shinjuku",
                "Ramen",
                "Tokyo, Shinjuku",
                new BigDecimal("35.6900000"),
                new BigDecimal("139.7000000"),
                new BigDecimal("3.52"),
                "https://tabelog.com/example",
                "https://youtube.com/example"
        ));
    }

    @Test
    void getRegionsReturnsRegionList() throws Exception {
        mockMvc.perform(get("/api/regions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(region.getId()))
                .andExpect(jsonPath("$[0].name").value("Shinjuku"))
                .andExpect(jsonPath("$[0].city").value("Tokyo"));
    }

    @Test
    void getRestaurantsReturnsRestaurantsInRegion() throws Exception {
        mockMvc.perform(get("/api/restaurants").param("regionId", region.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(restaurant.getId()))
                .andExpect(jsonPath("$[0].name").value("Ichiran Shinjuku"))
                .andExpect(jsonPath("$[0].tabelogScore").value(3.52));
    }

    @Test
    void getRestaurantReturnsDetailWithReviewStatistics() throws Exception {
        User firstUser = userRepository.save(new User("first@example.com", "encoded", "first"));
        User secondUser = userRepository.save(new User("second@example.com", "encoded", "second"));
        reviewRepository.save(new Review(firstUser, restaurant, new BigDecimal("4.5"), "Great"));
        reviewRepository.save(new Review(secondUser, restaurant, new BigDecimal("3.5"), "Good"));

        mockMvc.perform(get("/api/restaurants/{restaurantId}", restaurant.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(restaurant.getId()))
                .andExpect(jsonPath("$.name").value("Ichiran Shinjuku"))
                .andExpect(jsonPath("$.averageUserRating").value(4.0))
                .andExpect(jsonPath("$.reviewCount").value(2));
    }

    @Test
    void getReviewsReturnsNicknameAndReviewContent() throws Exception {
        User user = userRepository.save(new User("reviewer@example.com", "encoded", "reviewer"));
        Review review = reviewRepository.save(
                new Review(user, restaurant, new BigDecimal("4.5"), "Delicious")
        );

        mockMvc.perform(get("/api/restaurants/{restaurantId}/reviews", restaurant.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(review.getId()))
                .andExpect(jsonPath("$[0].nickname").value("reviewer"))
                .andExpect(jsonPath("$[0].rating").value(4.5))
                .andExpect(jsonPath("$[0].content").value("Delicious"))
                .andExpect(jsonPath("$[0].createdAt").exists());
    }

    @Test
    void getRestaurantReturnsNotFoundForUnknownRestaurant() throws Exception {
        mockMvc.perform(get("/api/restaurants/{restaurantId}", Long.MAX_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Restaurant not found: " + Long.MAX_VALUE));
    }
}
