package com.gitae.jpgourmetmap.service;

import com.gitae.jpgourmetmap.api.dto.ReviewResponse;
import com.gitae.jpgourmetmap.error.ResourceNotFoundException;
import com.gitae.jpgourmetmap.repository.RestaurantRepository;
import com.gitae.jpgourmetmap.repository.ReviewRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewQueryService {

    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;

    public List<ReviewResponse> findByRestaurant(Long restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new ResourceNotFoundException("Restaurant", restaurantId);
        }

        return reviewRepository.findAllByRestaurantIdOrderByCreatedAtDesc(restaurantId).stream()
                .map(ReviewResponse::from)
                .toList();
    }
}
