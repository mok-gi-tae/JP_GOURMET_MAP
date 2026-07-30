package com.gitae.jpgourmetmap.domain.review;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByRestaurantId(Long restaurantId);

    Optional<Review> findByUserIdAndRestaurantId(
            Long userId,
            Long restaurantId);

    boolean existsByUserIdAndRestaurantId(
            Long userId,
            Long restaurantId);

    long countByRestaurantId(Long restaurantId);

    @Query("select avg(r.rating) from Review r where r.restaurant.id = :restaurantId")
    Optional<BigDecimal> findAverageRatingByRestaurantId(@Param("restaurantId") Long restaurantId);
}
