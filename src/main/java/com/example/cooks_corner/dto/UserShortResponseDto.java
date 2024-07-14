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
public class UserShortResponseDto {
    private Long id;
    private String name;
    private String userPhotoUrl;
}
