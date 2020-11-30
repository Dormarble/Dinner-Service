package com.dudungtak.seproject.exception;

import lombok.Getter;

@Getter
public class PermissionDeniedException extends RuntimeException {
    public PermissionDeniedException() {
        super("permission denied");
    }
}
