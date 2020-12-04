package com.dudungtak.seproject.controller;

import com.dudungtak.seproject.exception.PermissionDeniedException;
import com.dudungtak.seproject.exception.BadInputException;
import com.dudungtak.seproject.exception.ExistedUserException;
import com.dudungtak.seproject.exception.LoginFailedException;
import com.dudungtak.seproject.network.Header;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
@Slf4j
public class ExceptionController {
    @ExceptionHandler({PermissionDeniedException.class})
    public Header permissionDeniedException(PermissionDeniedException permissionDeniedException) {
        log.info("{}", permissionDeniedException.getMessage());
        return Header.ERROR(permissionDeniedException.getMessage());
    }

    @ExceptionHandler({BadInputException.class})
    public Header invalidInputException(BadInputException badInputException) {
        log.info("{}", "invalid input data");
        return Header.ERROR(badInputException.getMessage());
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public Header entityNotFoundException(EntityNotFoundException entityNotFoundException) {
        log.info("{}", "entity not found");
        return Header.ERROR("entity not found");
    }

    @ExceptionHandler({ExistedUserException.class})
    public Header existedUserException(ExistedUserException existedUserException) {
        log.info("{}", existedUserException.getMessage());
        return Header.ERROR(existedUserException.getMessage());
    }

    @ExceptionHandler({LoginFailedException.class})
    public Header loginFailedException(LoginFailedException loginFailedException) {
        log.info("{}", loginFailedException.getMessage());
        return Header.ERROR(loginFailedException.getMessage());
    }

//    @ExceptionHandler({RuntimeException.class})
//    public Header runtimeException(RuntimeException runtimeException) {
//        log.info("{}", runtimeException.getMessage());
//        return Header.ERROR(runtimeException.getMessage());
//    }
}
