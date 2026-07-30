import { apiFetch } from "./api.js";

export function fetchReviews(restaurantId) {
    return apiFetch(`/api/restaurants/${encodeURIComponent(restaurantId)}/reviews`);
}

export function createReview(restaurantId, { rating, content }) {
    return apiFetch(`/api/restaurants/${encodeURIComponent(restaurantId)}/reviews`, {
        method: "POST",
        body: JSON.stringify({ rating, content }),
    });
}

export function updateReview(reviewId, { rating, content }) {
    return apiFetch(`/api/reviews/${encodeURIComponent(reviewId)}`, {
        method: "PUT",
        body: JSON.stringify({ rating, content }),
    });
}

export function deleteReview(reviewId) {
    return apiFetch(`/api/reviews/${encodeURIComponent(reviewId)}`, {
        method: "DELETE",
    });
}
