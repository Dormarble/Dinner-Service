package com.dudungtak.seproject.domain.service.api;

import com.dudungtak.seproject.model.entity.OrderElement;
import com.dudungtak.seproject.model.network.response.OrderElementApiResponse;

public class OrderElementApiService {
    public static OrderElementApiResponse response(OrderElement orderElement) {
        return OrderElementApiResponse.builder()
                .id(orderElement.getId())
                .dishId(orderElement.getDish().getId())
                .dishName(orderElement.getDish().getName())
                .quantity(orderElement.getQuantity())
                .totalPrice(orderElement.getTotalPrice())
                .build();
    }
}
