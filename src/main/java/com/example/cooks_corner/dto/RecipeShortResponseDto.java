package com.example.cooks_corner.dto;

import com.example.cooks_corner.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeShortResponseDto {
    private Long id;
    private String title;
    private String userName;
    private String imageUrl;
    private Integer likesCount;
    private Integer savesCount;
}
