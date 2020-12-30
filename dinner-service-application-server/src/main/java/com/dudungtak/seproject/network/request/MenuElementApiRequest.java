package com.dudungtak.seproject.network.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuElementApiRequest {
    private Long id;

    private Long dishId;

    private Integer quantity;
}
