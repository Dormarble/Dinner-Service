package com.dudungtak.seproject.controller.api;

import com.dudungtak.seproject.network.Header;
import com.dudungtak.seproject.network.request.StyleApiRequest;
import com.dudungtak.seproject.network.response.StyleApiResponse;
import com.dudungtak.seproject.service.api.StyleApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/style")
public class StyleApiController {
    @Autowired
    StyleApiService styleApiService;

    @PostMapping("")
    public Header<StyleApiResponse> create(@RequestBody Header<StyleApiRequest> request) {
        return styleApiService.create(request);
    }

    @GetMapping("{id}")
    public Header<StyleApiResponse> read(@PathVariable Long id) {
        return styleApiService.read(id);
    }

    @PutMapping("")
    public Header<StyleApiResponse> update(@RequestBody Header<StyleApiRequest> request) {
        return styleApiService.update(request);
    }

    @DeleteMapping("{id}")
    public Header delete(@PathVariable Long id) {
        return styleApiService.delete(id);
    }
}
