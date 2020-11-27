package com.dudungtak.seproject.controller.api;

import com.dudungtak.seproject.controller.Permission;
import com.dudungtak.seproject.enumpackage.AccessType;
import com.dudungtak.seproject.network.Header;
import com.dudungtak.seproject.network.request.MenuApiRequest;
import com.dudungtak.seproject.network.response.MenuApiResponse;
import com.dudungtak.seproject.service.api.MenuApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/menu")
public class MenuApiController {
    @Autowired
    MenuApiService menuApiService;

    @PostMapping("")
    public Header<MenuApiResponse> create(Authentication authentication, @RequestBody Header<MenuApiRequest> request) {
        if(!Permission.isValidAccess(authentication, AccessType.MANAGER))
            return Header.ERROR("permission denied");

        return menuApiService.create(request);
    }

    @GetMapping("{id}")
    public Header<MenuApiResponse> read(Authentication authentication, @PathVariable Long id) {
        if(!Permission.isValidAccess(authentication, AccessType.ALL))
            return Header.ERROR("permission denied");

        return menuApiService.read(id);
    }

    @GetMapping("")
    public Header<List<MenuApiResponse>> readAll(Authentication authentication, Pageable pageable) {
        if(!Permission.isValidAccess(authentication, AccessType.ALL))
            return Header.ERROR("permission denied");

        return menuApiService.readAll(pageable);
    }

    @PutMapping("")
    public Header<MenuApiResponse> update(Authentication authentication, @RequestBody Header<MenuApiRequest> request) {
        if(!Permission.isValidAccess(authentication, AccessType.MANAGER))
            return Header.ERROR("permission denied");

        return menuApiService.update(request);
    }

    @DeleteMapping("{id}")
    public Header<MenuApiResponse> delete(Authentication authentication, @PathVariable Long id) {
        if(!Permission.isValidAccess(authentication, AccessType.MANAGER))
            return Header.ERROR("permission denied");

        return menuApiService.delete(id);
    }
}
