package com.example.cooks_corner.dto;

import com.example.cooks_corner.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String name;
    private String userPhotoUrl;
    private String description;
    private List<RecipeShortResponseDto> createdRecipes;
    private Integer recipesCount;
    private Integer followersCount;
    private Integer followingsCount;
}
