package com.dudungtak.seproject.service.api;

import com.dudungtak.seproject.entity.OrderElement;
import com.dudungtak.seproject.network.response.OrderElementApiResponse;

public class OrderElementApiService {
    public static OrderElementApiResponse response(OrderElement orderElement) {
        return OrderElementApiResponse.builder()
                .id(orderElement.getId())
                .dishId(orderElement.getDish().getId())
                .dishName(orderElement.getDish().getName())
                .quantity(orderElement.getQuantity())
                .build();
    }
}
