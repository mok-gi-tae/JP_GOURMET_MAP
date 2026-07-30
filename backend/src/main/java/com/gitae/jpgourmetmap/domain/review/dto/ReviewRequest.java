package com.gitae.jpgourmetmap.domain.review.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ReviewRequest(
        @NotNull @DecimalMin("0.5") @DecimalMax("5.0") BigDecimal rating,
        @NotBlank @Size(max = 2000) String content
) {
}
