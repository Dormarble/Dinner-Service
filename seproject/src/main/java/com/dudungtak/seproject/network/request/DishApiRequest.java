package com.dudungtak.seproject.network.request;

import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Data
public class DishApiRequest {
    private Long id;

    private String name;

    private String status;

    private BigDecimal price;

    private LocalDate registeredAt;

    private LocalDate unregisteredAt;

    private List<DishElementApiRequest> dishElementList;
}
