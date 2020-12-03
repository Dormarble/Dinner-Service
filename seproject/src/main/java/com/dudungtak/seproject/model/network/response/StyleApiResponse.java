package com.dudungtak.seproject.model.network.response;

import com.dudungtak.seproject.model.enumpackage.StyleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import java.math.BigDecimal;
import java.sql.Blob;
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

    private String imgUrl;

    @Lob
    private Blob image;

    private LocalDate registeredAt;

    private LocalDate unregisteredAt;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime updatedAt;

    private String updatedBy;
}
