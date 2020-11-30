package com.dudungtak.seproject.controller.api;

import com.dudungtak.seproject.controller.Permission;
import com.dudungtak.seproject.enumpackage.AccessType;
import com.dudungtak.seproject.network.Header;
import com.dudungtak.seproject.network.request.IngredientApiRequest;
import com.dudungtak.seproject.network.response.IngredientApiResponse;
import com.dudungtak.seproject.service.api.IngredientApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredient")
public class IngredientApiController {
    @Autowired
    IngredientApiService ingredientApiService;

    @PostMapping("")
    public Header<IngredientApiResponse> create(Authentication authentication, @RequestBody Header<IngredientApiRequest> request) {
        if(!Permission.isValidAccess(authentication, AccessType.MANAGER))
            return Header.ERROR("permission denied");

        return ingredientApiService.create(request);
    }

    @GetMapping("{id}")
    public Header<IngredientApiResponse> read(Authentication authentication, @PathVariable Long id) {
        if(!Permission.isValidAccess(authentication, AccessType.MANAGER))
            return Header.ERROR("permission denied");

        return ingredientApiService.read(id);
    }

    @GetMapping("")
    public Header<List<IngredientApiResponse>> readAll(Authentication authentication, Pageable pageable) {
        if(!Permission.isValidAccess(authentication, AccessType.MANAGER))
            return Header.ERROR("permission denied");


        return ingredientApiService.readAll(pageable);
    }

    @PutMapping("")
    public Header<IngredientApiResponse> update(Authentication authentication, @RequestBody Header<IngredientApiRequest> request) {
        if(!Permission.isValidAccess(authentication, AccessType.MANAGER))
            return Header.ERROR("permission denied");

        return ingredientApiService.update(request);
    }

    @DeleteMapping("{id}")
    public Header delete(Authentication authentication, @PathVariable Long id) {
        if(!Permission.isValidAccess(authentication, AccessType.MANAGER))
            return Header.ERROR("permission denied");

        return ingredientApiService.delete(id);
    }
}
