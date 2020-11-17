package com.dudungtak.seproject.network.response;

import com.dudungtak.seproject.enumpackage.IngredientStatus;
import com.dudungtak.seproject.enumpackage.IngredientType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class IngredientApiResponse {
    private Long id;

    private String name;

    private IngredientType type;

    private IngredientStatus status;

    private BigDecimal cost;

    private Integer stock;

    private LocalDate registeredAt;

    private LocalDate unregisteredAt;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime updatedAt;

    private String updatedBy;
}
