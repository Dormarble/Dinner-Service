package com.dudungtak.seproject.network.request;

import com.dudungtak.seproject.enumpackage.StyleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import java.math.BigDecimal;
import java.sql.Blob;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StyleApiRequest {
    private Long id;

    private String name;

    private BigDecimal price;

    private StyleStatus status;

    private String content;

    private String imgUrl;

    @Lob
    private Blob image;

    private LocalDate registeredAt;

    private LocalDate unregisteredAt;
}
