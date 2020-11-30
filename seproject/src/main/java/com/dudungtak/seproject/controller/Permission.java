package com.dudungtak.seproject.controller;

import com.dudungtak.seproject.enumpackage.AccessType;
import com.dudungtak.seproject.enumpackage.UserType;
import com.dudungtak.seproject.exception.PermissionDeniedException;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;

public class Permission {
    public static void isValidAccess(Authentication authentication, AccessType accessType) {
        if (accessType == AccessType.ALL)
            return;

        if (authentication == null)                      // unsigned user access API requiring permission
            throw new PermissionDeniedException();


        Claims claims = (Claims) authentication.getPrincipal();
        Integer userBitMap = AccessType.valueOf(claims.get("type", String.class)).getBitMap();
        Integer requiredBitMap = accessType.getBitMap();

        Integer bitMapResult = userBitMap & requiredBitMap;

        boolean canAccess = false;
        Integer checkBit = 1;
        for (int i = 0; i < 32; i++) {
            if ((bitMapResult & (checkBit << i)) != 0) {
                canAccess = true;
            }
        }

        if (!canAccess) throw new PermissionDeniedException();

        return;
    }
}
