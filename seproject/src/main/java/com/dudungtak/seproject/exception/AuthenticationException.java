package com.dudungtak.seproject.exception;

import lombok.Getter;

@Getter
public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {
        super("permission denied");
    }
}
