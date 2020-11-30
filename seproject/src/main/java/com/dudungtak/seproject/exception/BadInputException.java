package com.dudungtak.seproject.exception;

public class BadInputException extends RuntimeException{
    public BadInputException() {
        super("requested with invalid input: (check menu id, dish id, ingredients id, or input types)");
    }
}
