package com.dudungtak.seproject.controller;

import com.dudungtak.seproject.network.Header;
import org.springframework.web.bind.annotation.*;

@RestController
public abstract class CrudController<Req, Res> {
    @PostMapping("")
    public Header<Res> create(@RequestBody Header<Req> request) {
        return null;
    }

    @GetMapping("{id}")
    public Header<Res> read(@PathVariable Long id) {
        return null;
    }

    @PutMapping("")
    public Header<Res> update(@RequestBody Header<Req> request) {
        return null;
    }

    @DeleteMapping("{id}")
    public Header<Res> delete(@PathVariable Long id) {
        return null;
    }
}
