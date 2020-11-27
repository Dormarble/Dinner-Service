package com.dudungtak.seproject.controller;

import com.dudungtak.seproject.enumpackage.AccessType;
import com.dudungtak.seproject.enumpackage.UserType;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;

public class Permission {
    public static boolean isValidAccess(Authentication authentication, AccessType accessType) {
        if(accessType == AccessType.ALL)
            return true;

        if(authentication == null)
            return false;

        if(accessType == AccessType.LOGINEDALL)
            return true;

        Claims claims = (Claims)authentication.getPrincipal();

        AccessType userType = AccessType.valueOf(claims.get("type", String.class));
        String userTypeTitle = userType.getTitle();
        String accessTitle = accessType.getTitle();

        return userTypeTitle.equals(accessTitle);
    }
}
