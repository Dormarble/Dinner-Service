package com.dudungtak.seproject.network.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IngredientApiResponse {
    private Long id;

    private String name;

    private String type;

    private String status;

    private BigDecimal cost;

    private Integer stock;

    private LocalDate registeredAt;

    private LocalDate unregisteredAt;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime updatedAt;

    private String updatedBy;
}
