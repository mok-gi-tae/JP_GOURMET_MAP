package com.gitae.jpgourmetmap.repository;

import com.gitae.jpgourmetmap.domain.Restaurant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findAllByRegionIdOrderByNameAsc(Long regionId);
}
