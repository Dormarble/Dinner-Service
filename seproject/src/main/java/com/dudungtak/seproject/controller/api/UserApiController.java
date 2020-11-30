package com.dudungtak.seproject.controller.api;

import com.dudungtak.seproject.controller.Permission;
import com.dudungtak.seproject.enumpackage.AccessType;
import com.dudungtak.seproject.network.Header;
import com.dudungtak.seproject.network.request.UserApiRequest;
import com.dudungtak.seproject.network.response.UserApiResponse;
import com.dudungtak.seproject.service.api.UserApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class UserApiController {
    @Autowired
    UserApiService userApiService;

    @PostMapping("user/login")
    public Header signIn(Authentication authentication, @RequestBody Header<UserApiRequest> request) {
        Permission.isValidAccess(authentication, AccessType.ALL);

        return userApiService.signIn(request);
    }

    @PostMapping("user")
    public Header create(Authentication authentication, @RequestBody Header<UserApiRequest> request) {
        Permission.isValidAccess(authentication, AccessType.ALL);

        return userApiService.signUp(request);
    }

    @GetMapping("user")
    public Header<UserApiResponse> read(Authentication authentication) {
        Permission.isValidAccess(authentication, AccessType.LOGINEDALL);

        return userApiService.read(authentication);
    }

    @GetMapping("users")
    public Header<List<UserApiResponse>> readAll(Authentication authentication, Pageable pageable) {
        Permission.isValidAccess(authentication, AccessType.MANAGER);

        return userApiService.readAllCustomer(pageable);
    }

    @PutMapping("user")
    public Header<UserApiResponse> update(Authentication authentication, @RequestBody Header<UserApiRequest> request) {
        Permission.isValidAccess(authentication, AccessType.LOGINEDALL);

        return userApiService.update(authentication, request);
    }
}
