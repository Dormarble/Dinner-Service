package com.dudungtak.seproject.controller.api;

import com.dudungtak.seproject.network.Header;
import com.dudungtak.seproject.network.request.MenuApiRequest;
import com.dudungtak.seproject.network.response.MenuApiResponse;
import com.dudungtak.seproject.service.api.MenuApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/menu")
public class MenuApiController {
    @Autowired
    MenuApiService menuApiService;

    @PostMapping("")
    public Header<MenuApiResponse> create(@RequestBody Header<MenuApiRequest> request) {
        return menuApiService.create(request);
    }

    @GetMapping("{id}")
    public Header<MenuApiResponse> read(@PathVariable Long id) {
        return menuApiService.read(id);
    }

    @GetMapping("")
    public Header<List<MenuApiResponse>> readAll(Pageable pageable) {
        return menuApiService.readAll(pageable);
    }

    @PutMapping("")
    public Header<MenuApiResponse> update(@RequestBody Header<MenuApiRequest> request) {
        return menuApiService.update(request);
    }

    @DeleteMapping("{id}")
    public Header<MenuApiResponse> delete(@PathVariable Long id) {
        return menuApiService.delete(id);
    }
}
