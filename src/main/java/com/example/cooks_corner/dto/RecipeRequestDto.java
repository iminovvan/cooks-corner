package com.example.cooks_corner.dto;

import com.example.cooks_corner.entity.enums.Category;
import com.example.cooks_corner.entity.enums.Difficulty;
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
public class RecipeRequestDto {
    @Schema(description = "Title of the recipe", example = "Pancakes")
    private String title;
    @Schema(description = "Description of the recipe", example = "Fluffy pancakes with syrup")
    private String description;
    @Schema(description = "List of ingredients for the recipe")
    private List<IngredientDto> ingredients;
    @Schema(description = "Difficulty level of the recipe", example = "EASY")
    private Difficulty difficulty;
    @Schema(description = "Category of the recipe", example = "BREAKFAST")
    private Category category;
    @Schema(description = "Estimated cooking time", example = "30 minutes")
    private String cookingTime;
}
