package com.dudungtak.seproject.exception;

public class ExistedUserException extends RuntimeException {
    public ExistedUserException() {
        super("user id already exists");
    }
}
