package com.dudungtak.seproject.controller;

import com.dudungtak.seproject.enumpackage.AccessType;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;

public class AuthFilter {
    public static boolean isValidAccess(Authentication authentication, AccessType accessType) {
        if(accessType == AccessType.ALL)
            return true;

        if(authentication == null)
            return false;

        Claims claims = (Claims)authentication.getPrincipal();
        String job = claims.get("job", String.class);

        String access = accessType.getTitle();

        return job.equals(access);
    }
}
