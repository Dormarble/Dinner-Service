package com.dudungtak.seproject.service.api;

import com.dudungtak.seproject.entity.DishElement;
import com.dudungtak.seproject.network.response.DishElementApiResponse;

public class DishElementApiService {
    public static DishElementApiResponse response(DishElement dishElement) {
        return DishElementApiResponse.builder()
                .id(dishElement.getId())
                .ingredientId(dishElement.getIngredient().getId())
                .ingredientName(dishElement.getIngredient().getName())
                .quantity(dishElement.getQuantity())
                .build();
    }
}
