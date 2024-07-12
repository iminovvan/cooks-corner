package com.example.cooks_corner.dto;

import lombok.Data;

@Data
public class RegisterRequestDto{
    private String name;
    private String email;
    private String password;
}
