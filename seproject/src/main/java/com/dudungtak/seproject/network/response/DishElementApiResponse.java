package com.dudungtak.seproject.network.response;

import com.dudungtak.seproject.enumpackage.IngredientStatus;
import com.dudungtak.seproject.enumpackage.IngredientType;
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
