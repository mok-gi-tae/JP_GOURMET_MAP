package com.gitae.jpgourmetmap.repository;

import com.gitae.jpgourmetmap.domain.Review;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @EntityGraph(attributePaths = "user")
    List<Review> findAllByRestaurantIdOrderByCreatedAtDesc(Long restaurantId);

    long countByRestaurantId(Long restaurantId);

    @Query("select avg(r.rating) from Review r where r.restaurant.id = :restaurantId")
    Double findAverageRatingByRestaurantId(@Param("restaurantId") Long restaurantId);
}
