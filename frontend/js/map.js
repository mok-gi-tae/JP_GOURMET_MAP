import { apiFetch } from "./api.js";

const JAPAN_CENTER = [36.2048, 138.2529];
const JAPAN_ZOOM = 5;
const REGION_ZOOM = 14;

let map;
let regionMarkers = [];
let restaurantMarkers = [];

export function initMap() {
    map = L.map("map").setView(JAPAN_CENTER, JAPAN_ZOOM);
    L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
        maxZoom: 19,
        attribution: "&copy; OpenStreetMap contributors",
    }).addTo(map);
    return map;
}

export function getMap() {
    return map;
}

export async function loadRegions(onRegionClick) {
    const regions = await apiFetch("/api/regions");

    regionMarkers.forEach((marker) => map.removeLayer(marker));
    regionMarkers = regions.map((region) => {
        const marker = L.marker([region.latitude, region.longitude]).addTo(map);
        marker.bindPopup(`<strong>${escapeHtml(region.name)}</strong><br>${escapeHtml(region.city)}`);
        marker.on("click", () => onRegionClick(region));
        return marker;
    });

    return regions;
}

export function flyToRegion(region) {
    map.flyTo([region.latitude, region.longitude], REGION_ZOOM);
}

const restaurantIcon = L.divIcon({
    className: "restaurant-marker-icon",
    html: "🍜",
    iconSize: [26, 26],
    iconAnchor: [13, 13],
});

export function renderRestaurantMarkers(restaurants, onRestaurantClick) {
    restaurantMarkers.forEach((marker) => map.removeLayer(marker));

    restaurantMarkers = restaurants.map((restaurant) => {
        const marker = L.marker([restaurant.latitude, restaurant.longitude], { icon: restaurantIcon }).addTo(map);
        marker.bindPopup(`<strong>${escapeHtml(restaurant.name)}</strong><br>${escapeHtml(restaurant.category)}`);
        marker.on("click", () => onRestaurantClick(restaurant.id));
        return marker;
    });
}

export function clearRestaurantMarkers() {
    restaurantMarkers.forEach((marker) => map.removeLayer(marker));
    restaurantMarkers = [];
}

function escapeHtml(value) {
    const div = document.createElement("div");
    div.textContent = value ?? "";
    return div.innerHTML;
}
