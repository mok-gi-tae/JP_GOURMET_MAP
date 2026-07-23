package com.gitae.jpgourmetmap.repository;


import com.gitae.jpgourmetmap.domain.Region;
import com.gitae.jpgourmetmap.domain.Restaurant;
import com.gitae.jpgourmetmap.domain.Review;
import com.gitae.jpgourmetmap.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;


import java.math.BigDecimal;
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
    void 유저id와_식당id으로_리뷰작성_조회() {
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
                        new BigDecimal(1.1),
                        new BigDecimal(2.2)
                )
        );
        Restaurant restaurant = restaurantRepository.save(
                new Restaurant(
                        region,
                        "gogoCurry",
                        "curry",
                        "abcdefg",
                        new BigDecimal(1.1),
                        new BigDecimal(2.2),
                        new BigDecimal(3.0),
                        "abc/def",
                        "ghi/jkl"
                )
        );

        Review review = new Review(
                user,
                restaurant,
                new BigDecimal(3.0),
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

    }

}
