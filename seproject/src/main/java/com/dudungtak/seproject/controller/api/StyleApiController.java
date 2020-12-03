package com.dudungtak.seproject.controller.api;

import com.dudungtak.seproject.auth.util.Permission;
import com.dudungtak.seproject.model.enumpackage.AccessType;
import com.dudungtak.seproject.model.network.Header;
import com.dudungtak.seproject.model.network.request.StyleApiRequest;
import com.dudungtak.seproject.model.network.response.StyleApiResponse;
import com.dudungtak.seproject.domain.service.api.StyleApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/style")
public class StyleApiController {
    @Autowired
    StyleApiService styleApiService;

    @PostMapping("")
    public Header<StyleApiResponse> create(Authentication authentication, @RequestBody Header<StyleApiRequest> request) {
        Permission.isValidAccess(authentication, AccessType.MANAGER);

        return styleApiService.create(request);
    }

    @GetMapping("{id}")
    public Header<StyleApiResponse> read(Authentication authentication, @PathVariable Long id) {
        Permission.isValidAccess(authentication, AccessType.ALL);

        return styleApiService.read(id);
    }

    @GetMapping("")
    public Header<List<StyleApiResponse>> readAll(Authentication authentication) {
        Permission.isValidAccess(authentication, AccessType.ALL);

        return styleApiService.readAll();
    }

    @PutMapping("")
    public Header<StyleApiResponse> update(Authentication authentication, @RequestBody Header<StyleApiRequest> request) {
        Permission.isValidAccess(authentication, AccessType.MANAGER);

        return styleApiService.update(request);
    }

    @DeleteMapping("{id}")
    public Header delete(Authentication authentication, @PathVariable Long id) {
        Permission.isValidAccess(authentication, AccessType.MANAGER);

        return styleApiService.delete(id);
    }
}
