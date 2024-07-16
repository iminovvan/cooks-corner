package com.example.cooks_corner.dto;

import com.example.cooks_corner.entity.Image;
import com.example.cooks_corner.entity.enums.Category;
import com.example.cooks_corner.entity.enums.Difficulty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeResponseDto {
    @Schema(description = "ID of the recipe", example = "1")
    private Long id;
    @Schema(description = "Title of the recipe", example = "Pancakes")
    private String title;
    @Schema(description = "Description of the recipe", example = "Fluffy pancakes with syrup")
    private String description;
    @Schema(description = "URL of the recipe image", example = "https://res.cloudinary.com/demo/image/upload/v1620000000/sample.jpg")
    private String imageUrl;
    @Schema(description = "List of ingredients for the recipe")
    private List<IngredientDto> ingredients;
    @Schema(description = "Difficulty level of the recipe", example = "EASY")
    private Difficulty difficulty;
    @Schema(description = "Category of the recipe", example = "BREAKFAST")
    private Category category;
    @Schema(description = "Estimated cooking time", example = "30 minutes")
    private String cookingTime;
    @Schema(description = "ID of the user who created the recipe", example = "10")
    private Long userId;
    @Schema(description = "Username of the user who created the recipe", example = "John Doe")
    private String userName;
    @Schema(description = "Number of likes the recipe has received", example = "150")
    private Integer likesCount;
    @Schema(description = "Number of times the recipe has been saved", example = "75")
    private Integer savesCount;
}
