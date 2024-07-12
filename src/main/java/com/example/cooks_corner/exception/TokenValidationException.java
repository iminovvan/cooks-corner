package com.example.cooks_corner.exception;

public class TokenValidationException extends RuntimeException {
    public TokenValidationException(String message){
        super(message);
    }
}
