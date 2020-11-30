package com.dudungtak.seproject.controller.api;

import com.dudungtak.seproject.entity.User;
import com.dudungtak.seproject.enumpackage.UserType;
import com.dudungtak.seproject.exception.AuthenticationException;
import com.dudungtak.seproject.exception.BadInputException;
import com.dudungtak.seproject.exception.CannotStoreToDatabaseException;
import com.dudungtak.seproject.network.Header;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionController {
    @ExceptionHandler({AuthenticationException.class})
    public Header authenticationException(AuthenticationException authenticationException) {
        User user = authenticationException.getUser();
        UserType requiredType = authenticationException.getRequiredType();

        if(user == null) log.info("permission denied : Unsigned user tried to access API requiring {} permission", requiredType);
        else log.info("permission denied : {}({}) tried to access API requiring {} permission", user.getType().getTitle(), user.getId(), requiredType);

        return Header.ERROR(authenticationException.getMessage());
    }

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

//    @ExceptionHandler({RuntimeException.class})
//    public Header runtimeException(RuntimeException runtimeException) {
//        log.info("{}", runtimeException.getMessage());
//        return Header.ERROR(runtimeException.getMessage());
//    }
}
