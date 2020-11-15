package com.dudungtak.seproject.network.request;

import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Data
public class DishApiRequest {
    private Long id;

    private String name;

    private String status;

    private BigDecimal price;

    private LocalDate registeredAt;

    private LocalDate unregisteredAt;
}
