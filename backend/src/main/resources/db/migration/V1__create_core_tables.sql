CREATE TABLE users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(50) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email),
    CONSTRAINT uk_users_nickname UNIQUE (nickname)
);

CREATE TABLE regions (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    latitude DECIMAL(10, 7) NOT NULL,
    longitude DECIMAL(10, 7) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    CONSTRAINT pk_regions PRIMARY KEY (id),
    CONSTRAINT uk_regions_city_name UNIQUE (city, name)
);

CREATE TABLE restaurants (
    id BIGINT NOT NULL AUTO_INCREMENT,
    region_id BIGINT NOT NULL,
    name VARCHAR(150) NOT NULL,
    category VARCHAR(100) NOT NULL,
    address VARCHAR(500) NOT NULL,
    latitude DECIMAL(10, 7) NOT NULL,
    longitude DECIMAL(10, 7) NOT NULL,
    tabelog_score DECIMAL(3, 2),
    tabelog_url VARCHAR(1000),
    youtube_url VARCHAR(1000),
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    CONSTRAINT pk_restaurants PRIMARY KEY (id),
    CONSTRAINT fk_restaurants_region FOREIGN KEY (region_id) REFERENCES regions (id),
    CONSTRAINT ck_restaurants_tabelog_score CHECK (tabelog_score IS NULL OR (tabelog_score >= 0 AND tabelog_score <= 5))
);

CREATE INDEX idx_restaurants_region_id ON restaurants (region_id);

CREATE TABLE reviews (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    restaurant_id BIGINT NOT NULL,
    rating DECIMAL(2, 1) NOT NULL,
    content VARCHAR(2000) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    CONSTRAINT pk_reviews PRIMARY KEY (id),
    CONSTRAINT fk_reviews_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_reviews_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants (id),
    CONSTRAINT uk_reviews_user_restaurant UNIQUE (user_id, restaurant_id),
    CONSTRAINT ck_reviews_rating CHECK (rating >= 0.5 AND rating <= 5.0)
);

CREATE INDEX idx_reviews_restaurant_id ON reviews (restaurant_id);
