package com.dudungtak.seproject.network.response;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Lob;
import java.math.BigDecimal;
import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class MenuApiResponse {
    private Long id;

    private String name;

    private BigDecimal totalPrice;

    private String imgUrl;

    @Lob
    private Blob image;

    private LocalDate registeredAt;

    private LocalDate unregisteredAt;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime updatedAt;

    private String updatedBy;

    private List<MenuElementApiResponse> menuElementList;
}
