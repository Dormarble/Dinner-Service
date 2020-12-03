package com.dudungtak.seproject.model.network.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DishElementApiResponse {
    private Long id;

    private Long ingredientId;

    private String ingredientName;

    private Integer quantity;
}
