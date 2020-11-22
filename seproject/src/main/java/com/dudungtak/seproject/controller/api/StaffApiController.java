package com.dudungtak.seproject.controller.api;

import com.dudungtak.seproject.network.Header;
import com.dudungtak.seproject.network.request.StaffApiRequest;
import com.dudungtak.seproject.network.response.StaffApiResponse;
import com.dudungtak.seproject.service.api.StaffApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/staff")
public class StaffApiController {

    @Autowired
    StaffApiService staffApiService;

    @PostMapping("")
    public Header<StaffApiResponse> create(@RequestBody Header<StaffApiRequest> request) {
        return staffApiService.create(request);
    }

    @GetMapping("{id}")
    public Header<StaffApiResponse> read(@PathVariable Long id) {
        return staffApiService.read(id);
    }

    @GetMapping("")
    public Header<List<StaffApiResponse>> readAll(Pageable pageable) {
        return staffApiService.readAll(pageable);
    }

    @PutMapping("")
    public Header<StaffApiResponse> update(@RequestBody Header<StaffApiRequest> request) {
        return staffApiService.update(request);
    }

    @DeleteMapping("{id}")
    public Header<StaffApiResponse> delete(@PathVariable Long id) {
        return staffApiService.delete(id);
    }
}
