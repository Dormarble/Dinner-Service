package com.dudungtak.seproject.model.network.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderElementApiRequest {
    private Long id;

    private Long dishId;

    private Integer quantity;

    private BigDecimal totalPrice;
}
