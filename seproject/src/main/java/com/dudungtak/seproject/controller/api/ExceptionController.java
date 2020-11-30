package com.dudungtak.seproject.controller.api;

import com.dudungtak.seproject.exception.BadInputException;
import com.dudungtak.seproject.exception.CannotStoreToDatabaseException;
import com.dudungtak.seproject.network.Header;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionController {
    @ExceptionHandler({BadInputException.class})
    public Header invalidInputException(BadInputException badInputException) {
        log.info("{}", "invalid input data");
        return Header.ERROR(badInputException.getMessage());
    }

    @ExceptionHandler({CannotStoreToDatabaseException.class})
    public Header cannotStoreToDatabaseException(CannotStoreToDatabaseException cannotStoreToDatabaseException) {
        log.info("{}", "cannot store to database");
        return Header.ERROR("internal server error");
    }

    @ExceptionHandler({RuntimeException.class})
    public Header runtimeException(RuntimeException runtimeException) {
        log.info("{}", runtimeException.getMessage());
        return Header.ERROR(runtimeException.getMessage());
    }
}
