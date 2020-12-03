package com.dudungtak.seproject.model.exception;

import com.dudungtak.seproject.model.enumpackage.AccessType;
import com.dudungtak.seproject.model.enumpackage.UserType;
import lombok.Getter;

@Getter
public class PermissionDeniedException extends RuntimeException {
    UserType userType;
    AccessType accessType;

    public PermissionDeniedException() {
        super("permission denied");
    }

    public PermissionDeniedException(String msg) {
        super(msg);
    }

    public PermissionDeniedException(AccessType accessType, AccessType requiredType) {
        super(String.format("permission denied (required: %s, accessed : %s)", accessType.getTitle(), requiredType.getTitle()));
    }
}
