package com.example.cooks_corner.dto;

import com.example.cooks_corner.entity.Image;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Name of the user", example = "Jane Doe")
    private String name;

    @Schema(description = "Description or bio of the user", example = "A creative chef and food blogger.")
    private String description;

    @Schema(description = "Photo of the user", type = "string", format = "binary")
    private MultipartFile photo;
}
