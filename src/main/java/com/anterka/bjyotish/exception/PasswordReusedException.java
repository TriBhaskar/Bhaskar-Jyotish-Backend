package com.anterka.bjyotish.exception;

public class PasswordReusedException extends RuntimeException{
    public PasswordReusedException(String message){
        super(message);
    }
}
