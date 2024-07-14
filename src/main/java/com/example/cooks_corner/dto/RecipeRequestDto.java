package com.example.cooks_corner.dto;

import com.example.cooks_corner.entity.enums.Category;
import com.example.cooks_corner.entity.enums.Difficulty;
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
    private String title;
    private String description;
    private List<IngredientDto> ingredients;
    private Difficulty difficulty;
    private Category category;
    private String cookingTime;
}
