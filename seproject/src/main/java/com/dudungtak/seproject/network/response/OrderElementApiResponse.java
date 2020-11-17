package com.dudungtak.seproject.network.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderElementApiResponse {
    private Long id;

    private Long dishId;

    private String dishName;

    private Integer quantity;
}
