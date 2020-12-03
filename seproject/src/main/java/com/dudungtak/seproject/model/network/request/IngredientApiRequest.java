package com.dudungtak.seproject.model.network.request;

import com.dudungtak.seproject.model.enumpackage.IngredientStatus;
import com.dudungtak.seproject.model.enumpackage.IngredientType;
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
    protected Long id;

    protected String name;

    protected IngredientType type;

    protected IngredientStatus status;

    protected BigDecimal cost;

    protected Integer stock;

    protected LocalDate registeredAt;
}
