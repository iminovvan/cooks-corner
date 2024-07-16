package com.example.cooks_corner.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginRequestDto {
    @Schema(example = "test.user@example.com")
    private String email;
    @Schema(example = "password123")
    private String password;
}
