package com.example.cooks_corner.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String email;
    private String password;
}
