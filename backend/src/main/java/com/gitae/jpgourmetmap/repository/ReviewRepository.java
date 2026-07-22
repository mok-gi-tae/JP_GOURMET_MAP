package com.gitae.jpgourmetmap.repository;

import com.gitae.jpgourmetmap.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByRestaurantId(Long restaurant_id);

    Optional<Review> findByUserIdAndRestaurantId(Long restaurant_id);

    boolean existsByUserIdAndRestaurantId(Long restaurant_id);
}
