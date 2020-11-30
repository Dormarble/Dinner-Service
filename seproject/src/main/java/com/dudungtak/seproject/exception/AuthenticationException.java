package com.dudungtak.seproject.exception;

import com.dudungtak.seproject.entity.User;
import com.dudungtak.seproject.enumpackage.UserType;
import lombok.Getter;

@Getter
public class AuthenticationException extends RuntimeException {
    private User user;
    private UserType requiredType;

    public AuthenticationException(UserType requiredType) {
        this(null, requiredType);
    }

    public AuthenticationException(User user, UserType requiredType) {
        super("permission denied");
        this.user = user;
        this.requiredType = requiredType;
    }
}
