package com.dudungtak.seproject.model.exception;

public class ExistedUserException extends RuntimeException {
    public ExistedUserException() {
        super("user id already exists");
    }
}
