package com.dudungtak.seproject.network.request;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class MenuApiRequest {
    private Long id;

    private String name;

    private BigDecimal totalPrice;

    private LocalDate registeredAt;

    private LocalDate unregisteredAt;

    private List<MenuElementApiRequest> menuElementList;
}
