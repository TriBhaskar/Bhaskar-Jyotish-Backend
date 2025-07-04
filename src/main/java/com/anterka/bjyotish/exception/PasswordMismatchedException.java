package com.anterka.bjyotish.exception;

public class PasswordMismatchedException extends RuntimeException{
    public PasswordMismatchedException(String message){
        super(message);
    }
}
