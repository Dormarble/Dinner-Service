package com.dudungtak.seproject.exception;

public class BadInputException extends RuntimeException{
    public BadInputException() {
        super("requested with invalid input: (check entities id, or input types)");
    }
}
