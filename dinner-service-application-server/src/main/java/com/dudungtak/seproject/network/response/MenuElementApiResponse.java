package com.dudungtak.seproject.network.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuElementApiResponse {
    private Long id;

    private Long dishId;

    private String dishName;

    private Integer quantity;
}
