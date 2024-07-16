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
public class IngredientDto {
    @Schema(description = "Name of the ingredient", example = "Flour")
    private String name;
    @Schema(description = "Quantity of the ingredient", example = "2 cups")
    private String quantity;
}
