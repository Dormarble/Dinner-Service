package com.dudungtak.seproject.model.exception;

public class CannotStoreToDatabaseException extends RuntimeException {
    public CannotStoreToDatabaseException() {
        super("cannot store data to database. check database or server log");
    }
}
