package com.example.cooks_corner.dto;

import com.example.cooks_corner.entity.Image;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyUserResponseDto {
    @Schema(description = "Unique identifier of the user", example = "1")
    private Long id;

    @Schema(description = "Name of the user", example = "John Doe")
    private String name;

    @Schema(description = "URL of the user's photo", example = "https://res.cloudinary.com/demo/image/upload/v1620000000/sample.jpg")
    private String userPhotoUrl;

    @Schema(description = "Description or bio of the user", example = "A passionate cook and recipe creator.")
    private String description;

    @Schema(description = "List of short responses of the user's created recipes")
    private List<RecipeShortResponseDto> createdRecipes;

    @Schema(description = "List of short responses of the user's saved recipes")
    private List<RecipeShortResponseDto> savedRecipes;

    @Schema(description = "Count of recipes created by the user", example = "10")
    private Integer recipesCount;

    @Schema(description = "Count of followers the user has", example = "150")
    private Integer followersCount;

    @Schema(description = "Count of users the user is following", example = "75")
    private Integer followingsCount;

}
