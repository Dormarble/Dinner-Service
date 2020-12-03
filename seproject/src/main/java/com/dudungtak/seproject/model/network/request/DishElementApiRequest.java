package com.dudungtak.seproject.model.network.request;

import lombok.*;

@Getter
@Data
public class DishElementApiRequest {
    private Long id;

    private Long ingredientId;

    private Integer quantity;
}
