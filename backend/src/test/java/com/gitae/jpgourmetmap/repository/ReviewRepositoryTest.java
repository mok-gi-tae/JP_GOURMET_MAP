package com.gitae.jpgourmetmap.repository;


import com.gitae.jpgourmetmap.region.Region;
import com.gitae.jpgourmetmap.restaurant.Restaurant;
import com.gitae.jpgourmetmap.review.Review;
import com.gitae.jpgourmetmap.review.ReviewRepository;
import com.gitae.jpgourmetmap.user.User;
import com.gitae.jpgourmetmap.region.RegionRepository;
import com.gitae.jpgourmetmap.restaurant.RestaurantRepository;
import com.gitae.jpgourmetmap.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void 유저id와_식당id으로_리뷰조회() {
        User user = userRepository.save(
                new User(
                        "gitae@example.com",
                        "1234",
                        "gita"
                )
        );
        Region region = regionRepository.save(
                new Region(
                        "Tokyo",
                        "Tokyo",
                        new BigDecimal("1.1000000000"),
                        new BigDecimal("2.2000000000")
                )
        );
        Restaurant restaurant = restaurantRepository.save(
                new Restaurant(
                        region,
                        "gogoCurry",
                        "curry",
                        "abcdefg",
                        new BigDecimal("1.1000000000"),
                        new BigDecimal("2.2000000000"),
                        new BigDecimal("3.000"),
                        "abc/def",
                        "ghi/jkl"
                )
        );

        Review review = new Review(
                user,
                restaurant,
                new BigDecimal("3.000"),
                "美味い"
        );
        reviewRepository.save(review);

        Optional<Review> result = reviewRepository.findByUserIdAndRestaurantId(
                user.getId(),
                restaurant.getId()
        );
        // 잘 저장되서 존재하는가?
        assertThat(result).isPresent();

        assertThat(result.get().getRating()).isEqualByComparingTo("3.0");
        assertThat(result.get().getRating()).isNotEqualByComparingTo("2.999999999999");

    }

    @Test
    void 식당id로_리뷰조회() {
        User user = userRepository.save(
                new User(
                        "gitae@example.com",
                        "1234",
                        "gita"
                )
        );
        Region region = regionRepository.save(
                new Region(
                        "Tokyo",
                        "Tokyo",
                        new BigDecimal("1.1000000000"),
                        new BigDecimal("2.2000000000")
                )
        );
        Restaurant restaurant = restaurantRepository.save(
                new Restaurant(
                        region,
                        "gogoCurry",
                        "curry",
                        "abcdefg",
                        new BigDecimal("1.1000000000"),
                        new BigDecimal("2.2000000000"),
                        new BigDecimal("3.000"),
                        "abc/def",
                        "ghi/jkl"
                )
        );

        Review review = new Review(
                user,
                restaurant,
                new BigDecimal("3.000"),
                "美味い"
        );
        reviewRepository.save(review);

        List<Review> result = reviewRepository.findByRestaurantId(
                restaurant.getId()
        );

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);

        assertThat(result.get(0).getRating()).isEqualByComparingTo("3.0");
        assertThat(result.get(0).getRating()).isNotEqualByComparingTo("2.999999999999");

    }

    @Test
    void 식당id와_유저id로_리뷰존재_확인() {
        User user = userRepository.save(
                new User(
                        "gitae@example.com",
                        "1234",
                        "gita"
                )
        );
        Region region = regionRepository.save(
                new Region(
                        "Tokyo",
                        "Tokyo",
                        new BigDecimal("1.1000000000"),
                        new BigDecimal("2.2000000000")
                )
        );
        Restaurant restaurant = restaurantRepository.save(
                new Restaurant(
                        region,
                        "gogoCurry",
                        "curry",
                        "abcdefg",
                        new BigDecimal("1.1000000000"),
                        new BigDecimal("2.2000000000"),
                        new BigDecimal("3.000"),
                        "abc/def",
                        "ghi/jkl"
                )
        );

        Review review = new Review(
                user,
                restaurant,
                new BigDecimal("3.000"),
                "美味い"
        );
        reviewRepository.save(review);

        boolean result = reviewRepository.existsByUserIdAndRestaurantId(
                user.getId(),
                restaurant.getId()
        );

        assertThat(result).isTrue();

    }



}
