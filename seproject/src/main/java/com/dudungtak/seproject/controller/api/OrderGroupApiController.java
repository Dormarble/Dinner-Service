package com.dudungtak.seproject.controller.api;

import com.dudungtak.seproject.network.Header;
import com.dudungtak.seproject.network.request.OrderGroupApiRequest;
import com.dudungtak.seproject.network.response.OrderGroupApiResponse;
import com.dudungtak.seproject.service.api.OrderGroupApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class OrderGroupApiController {
    @Autowired
    OrderGroupApiService orderGroupApiService;

    @PostMapping("/{id}/order")
    public Header<OrderGroupApiResponse> create(@RequestBody Header<OrderGroupApiRequest> request) {
        return orderGroupApiService.create(request);
    }

    @GetMapping("/{id}/orders")
    public Header<List<OrderGroupApiResponse>> readAll(@PathVariable Long id, Pageable pageable) {
        return orderGroupApiService.readAll(id, pageable);
    }

    @GetMapping("/order/confirm")
    public Header<List<OrderGroupApiResponse>> nextConfirm() {
        return orderGroupApiService.nextConfirm();
    }

    @PostMapping("/order/confirm")
    public Header confirm(@RequestBody Header<List<OrderGroupApiRequest>> request) {
        return orderGroupApiService.confirm(request);
    }
}
