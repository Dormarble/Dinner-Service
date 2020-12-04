package com.dudungtak.seproject.controller.api;

import com.dudungtak.seproject.util.Permission;
import com.dudungtak.seproject.enumpackage.AccessType;
import com.dudungtak.seproject.network.Header;
import com.dudungtak.seproject.network.request.DishApiRequest;
import com.dudungtak.seproject.network.response.DishApiResponse;
import com.dudungtak.seproject.service.api.DishApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/dish")
public class DishApiController{
    @Autowired
    DishApiService dishApiService;

    @PostMapping("")
    public Header<DishApiResponse> create(Authentication authentication, @RequestBody Header<DishApiRequest> request) {
        Permission.isValidAccess(authentication, AccessType.MANAGER);

        return dishApiService.create(request);
    }

    @GetMapping("{id}")
    public Header<DishApiResponse> read(Authentication authentication, @PathVariable Long id) {
        Permission.isValidAccess(authentication, AccessType.ALL);

        return dishApiService.read(id);
    }

    @GetMapping("")
    public Header<List<DishApiResponse>> readAll(Authentication authentication, @PageableDefault(sort="name", size=20, direction = Sort.Direction.ASC)Pageable pageable) {
        Permission.isValidAccess(authentication, AccessType.ALL);

        return dishApiService.readAll(pageable);
    }

    @PutMapping("")
    public Header<DishApiResponse> update(Authentication authentication, @RequestBody Header<DishApiRequest> request) {
        Permission.isValidAccess(authentication, AccessType.MANAGER);

        return dishApiService.update(request);
    }

    @DeleteMapping("{id}")
    public Header delete(Authentication authentication, @PathVariable Long id) {
        Permission.isValidAccess(authentication, AccessType.MANAGER);

        return dishApiService.delete(id);
    }
}
