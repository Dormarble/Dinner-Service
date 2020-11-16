package com.dudungtak.seproject.network.request;

import com.dudungtak.seproject.enumpackage.IngredientStatus;
import com.dudungtak.seproject.enumpackage.IngredientType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IngredientApiRequest {
    private Long id;

    private String name;

    private IngredientType type;

    private IngredientStatus status;

    private BigDecimal cost;

    private Integer stock;

    private LocalDate registeredAt;

    private LocalDate unregisteredAt;
}
