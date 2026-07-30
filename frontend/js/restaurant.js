import { apiFetch } from "./api.js";

export function fetchRestaurantsByRegion(regionId) {
    return apiFetch(`/api/restaurants?regionId=${encodeURIComponent(regionId)}`);
}

export function fetchRestaurantDetail(restaurantId) {
    return apiFetch(`/api/restaurants/${encodeURIComponent(restaurantId)}`);
}
