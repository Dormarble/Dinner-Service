package com.dudungtak.seproject.controller.api;

import com.dudungtak.seproject.network.Header;
import com.dudungtak.seproject.network.request.DishApiRequest;
import com.dudungtak.seproject.network.request.IngredientApiRequest;
import com.dudungtak.seproject.network.response.DishApiResponse;
import com.dudungtak.seproject.network.response.IngredientApiResponse;
import com.dudungtak.seproject.service.api.IngredientApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ingredient")
public class IngredientApiController {
    @Autowired
    IngredientApiService ingredientApiService;

    @PostMapping("")
    public Header<IngredientApiResponse> create(@RequestBody Header<IngredientApiRequest> request) {
        return ingredientApiService.create(request);
    }

    @GetMapping("{id}")
    public Header<IngredientApiResponse> read(@PathVariable Long id) {
        return ingredientApiService.read(id);
    }


    @PutMapping("")
    public Header<IngredientApiResponse> update(@RequestBody Header<IngredientApiRequest> request) {
        return ingredientApiService.update(request);
    }

    @DeleteMapping("{id}")
    public Header delete(@PathVariable Long id) {
        return ingredientApiService.delete(id);
    }
}
