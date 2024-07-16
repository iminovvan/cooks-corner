package com.example.cooks_corner.dto;

import com.example.cooks_corner.entity.Image;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeShortResponseDto {
    @Schema(description = "ID of the recipe", example = "1")
    private Long id;

    @Schema(description = "Title of the recipe", example = "Pancakes")
    private String title;

    @Schema(description = "Username of the person who created the recipe", example = "john_doe")
    private String userName;

    @Schema(description = "URL of the recipe image", example = "https://res.cloudinary.com/demo/image/upload/v1620000000/sample.jpg")
    private String imageUrl;

    @Schema(description = "Number of likes the recipe has received", example = "150")
    private Integer likesCount;

    @Schema(description = "Number of times the recipe has been saved", example = "75")
    private Integer savesCount;
}
