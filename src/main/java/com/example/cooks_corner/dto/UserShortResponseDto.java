package com.example.cooks_corner.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserShortResponseDto {
    @Schema(description = "Unique identifier of the user", example = "1")
    private Long id;

    @Schema(description = "Name of the user", example = "John Doe")
    private String name;

    @Schema(description = "URL of the user's photo", example = "https://res.cloudinary.com/demo/image/upload/v1620000000/sample.jpg")
    private String userPhotoUrl;
}
