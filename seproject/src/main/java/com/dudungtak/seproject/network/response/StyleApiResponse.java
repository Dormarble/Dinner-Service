package com.dudungtak.seproject.network.response;

import com.dudungtak.seproject.enumpackage.StyleStatus;
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
public class StyleApiResponse {
    private Long id;

    private String name;

    private StyleStatus status;

    private BigDecimal price;

    private String content;

    private LocalDate registeredAt;

    private LocalDate unregisteredAt;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime updatedAt;

    private String updatedBy;
}
