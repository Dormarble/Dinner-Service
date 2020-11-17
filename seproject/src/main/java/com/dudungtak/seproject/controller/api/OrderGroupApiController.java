package com.dudungtak.seproject.controller.api;

import com.dudungtak.seproject.network.Header;
import com.dudungtak.seproject.network.response.OrderGroupApiResponse;
import com.dudungtak.seproject.service.api.OrderGroupApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class OrderGroupApiController {
    @Autowired
    OrderGroupApiService orderGroupApiService;

    @GetMapping("/{id}/orders")
    public Header<List<OrderGroupApiResponse>> readAll(@PathVariable Long id, Pageable pageable) {
        return orderGroupApiService.readAll(id, pageable);
    }
}
