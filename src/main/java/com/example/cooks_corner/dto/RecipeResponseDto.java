package com.example.cooks_corner.dto;

import com.example.cooks_corner.entity.Image;
import com.example.cooks_corner.entity.enums.Category;
import com.example.cooks_corner.entity.enums.Difficulty;
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
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private List<IngredientDto> ingredients;
    private Difficulty difficulty;
    private Category category;
    private String cookingTime;
    private Long userId;
    private String userName;
    private Integer likesCount;
    private Integer savesCount;
}
