package com.example.cooks_corner.dto;

import com.example.cooks_corner.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEditDto {
    private String name;
    private String description;
    private MultipartFile photo;
}
