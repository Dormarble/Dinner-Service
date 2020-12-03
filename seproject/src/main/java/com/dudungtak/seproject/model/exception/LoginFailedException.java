package com.dudungtak.seproject.model.exception;

public class LoginFailedException extends RuntimeException {
    public LoginFailedException() {
        super("fail to sign in");
    }
}
