package com.dudungtak.seproject.network.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderElementApiResponse {
    private Long id;

    private Long dishId;

    private String dishName;

    private Integer quantity;

    private BigDecimal totalPrice;
}
