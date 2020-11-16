package com.dudungtak.seproject.controller.api;

import com.dudungtak.seproject.network.Header;
import com.dudungtak.seproject.network.request.DishApiRequest;
import com.dudungtak.seproject.network.response.DishApiResponse;
import com.dudungtak.seproject.service.api.DishApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/api/dish")
public class DishApiController{
    @Autowired
    DishApiService dishApiService;

    @PostMapping("")
    public Header<DishApiResponse> create(@RequestBody Header<DishApiRequest> request) {
        return dishApiService.create(request);
    }

    @GetMapping("{id}")
    public Header<DishApiResponse> read(@PathVariable Long id) {
        return dishApiService.read(id);
    }

    @PutMapping("")
    public Header<DishApiResponse> update(@RequestBody Header<DishApiRequest> request) {
        return dishApiService.update(request);
    }

    @DeleteMapping("{id}")
    public Header delete(@PathVariable Long id) {
        return dishApiService.delete(id);
    }
}
