package com.dudungtak.seproject.network.request;

import lombok.*;

@Getter
@Data
public class DishElementApiRequest {
    private Long id;

    private Long ingredientId;

    private Integer quantity;
}
