package com.dudungtak.seproject.network.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderElementApiRequest {
    private Long id;

    private Long dishId;

    private Integer quantity;
}
