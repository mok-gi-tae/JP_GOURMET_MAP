package com.gitae.jpgourmetmap.repository;

import com.gitae.jpgourmetmap.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByRestaurantId(Long restaurantId);

    Optional<Review> findByUserIdAndRestaurantId(
            Long userId,
            Long restaurantId);

    boolean existsByUserIdAndRestaurantId(
            Long userId,
            Long restaurantId);
}
