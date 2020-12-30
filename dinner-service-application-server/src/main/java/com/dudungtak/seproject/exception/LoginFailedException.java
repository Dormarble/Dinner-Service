package com.dudungtak.seproject.exception;

public class LoginFailedException extends RuntimeException {
    public LoginFailedException() {
        super("fail to sign in");
    }
}
