package com.dudungtak.seproject.controller.api;

import com.dudungtak.seproject.network.Header;
import com.dudungtak.seproject.network.response.UserApiResponse;
import com.dudungtak.seproject.service.api.UserApiSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserApiController {
    @Autowired
    UserApiSevice userApiSevice;

    @GetMapping("{id}")
    public Header<UserApiResponse> read(@PathVariable Long id) {
        return userApiSevice.read(id);
    }

    @GetMapping("")
    public Header<List<UserApiResponse>> readAll(Pageable pageable) {
        return userApiSevice.readAll(pageable);
    }
}
