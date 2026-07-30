package com.gitae.jpgourmetmap.domain.user.dto;

public record LoginResponse(
        String accessToken,
        UserResponse user
) {
}
