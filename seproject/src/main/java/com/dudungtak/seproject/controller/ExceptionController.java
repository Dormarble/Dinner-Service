package com.dudungtak.seproject.controller;

import com.dudungtak.seproject.exception.AuthenticationException;
import com.dudungtak.seproject.exception.BadInputException;
import com.dudungtak.seproject.exception.ExistedUserException;
import com.dudungtak.seproject.network.Header;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
@Slf4j
public class ExceptionController {
    @ExceptionHandler({AuthenticationException.class})
    public Header authenticationException(AuthenticationException authenticationException) {
        log.info("{}", authenticationException.getMessage());
        return Header.ERROR(authenticationException.getMessage());
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

//    @ExceptionHandler({RuntimeException.class})
//    public Header runtimeException(RuntimeException runtimeException) {
//        log.info("{}", runtimeException.getMessage());
//        return Header.ERROR(runtimeException.getMessage());
//    }
}
