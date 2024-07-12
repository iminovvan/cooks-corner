package com.example.cooks_corner.exception;


public class NotFoundException extends RuntimeException{
    public NotFoundException(String message){
        super(message);
    }
}
