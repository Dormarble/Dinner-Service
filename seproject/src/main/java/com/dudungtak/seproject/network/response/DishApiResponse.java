package com.dudungtak.seproject.network.response;

import com.dudungtak.seproject.network.request.DishElementApiRequest;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.persistence.Lob;
import java.math.BigDecimal;
import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Data
@Builder
public class DishApiResponse {
    private Long id;

    private String name;

    private String status;

    private BigDecimal price;

    private String imgUrl;

    @Lob
    private Blob image;

    private LocalDate registeredAt;

    private LocalDate unregisteredAt;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime updatedAt;

    private String updatedBy;

    private List<DishElementApiResponse> dishElementList;
}
