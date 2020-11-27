package com.dudungtak.seproject.controller.api;

import com.dudungtak.seproject.controller.AuthFilter;
import com.dudungtak.seproject.enumpackage.AccessType;
import com.dudungtak.seproject.network.Header;
import com.dudungtak.seproject.network.request.StyleApiRequest;
import com.dudungtak.seproject.network.response.StyleApiResponse;
import com.dudungtak.seproject.service.api.StyleApiService;
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
        if(!AuthFilter.isValidAccess(authentication, AccessType.MANAGER))
            return Header.ERROR("permission denied");

        return styleApiService.create(request);
    }

    @GetMapping("{id}")
    public Header<StyleApiResponse> read(Authentication authentication, @PathVariable Long id) {
        if(!AuthFilter.isValidAccess(authentication, AccessType.ALL))
            return Header.ERROR("permission denied");

        return styleApiService.read(id);
    }

    @GetMapping("")
    public Header<List<StyleApiResponse>> readAll(Authentication authentication) {
        if(!AuthFilter.isValidAccess(authentication, AccessType.ALL))
            return Header.ERROR("permission denied");

        return styleApiService.readAll();
    }

    @PutMapping("")
    public Header<StyleApiResponse> update(Authentication authentication, @RequestBody Header<StyleApiRequest> request) {
        if(!AuthFilter.isValidAccess(authentication, AccessType.MANAGER))
            return Header.ERROR("permission denied");

        return styleApiService.update(request);
    }

    @DeleteMapping("{id}")
    public Header delete(Authentication authentication, @PathVariable Long id) {
        if(!AuthFilter.isValidAccess(authentication, AccessType.MANAGER))
            return Header.ERROR("permission denied");

        return styleApiService.delete(id);
    }
}
