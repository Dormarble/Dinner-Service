package com.dudungtak.seproject.service.api;

import com.dudungtak.seproject.entity.MenuElement;
import com.dudungtak.seproject.network.response.MenuElementApiResponse;

public class MenuElementApiService {

    public static MenuElementApiResponse response(MenuElement menuElement) {
        return MenuElementApiResponse.builder()
                .id(menuElement.getId())
                .dishId(menuElement.getDish().getId())
                .dishName(menuElement.getDish().getName())
                .quantity(menuElement.getQuantity())
                .build();
    }
}
