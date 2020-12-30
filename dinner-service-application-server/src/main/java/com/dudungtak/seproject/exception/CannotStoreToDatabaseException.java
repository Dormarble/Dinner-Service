package com.dudungtak.seproject.exception;

public class CannotStoreToDatabaseException extends RuntimeException {
    public CannotStoreToDatabaseException() {
        super("cannot store data to database. check database or server log");
    }
}
