package com.dudungtak.seproject.network.response;

import com.dudungtak.seproject.entity.OrderElement;
import com.dudungtak.seproject.enumpackage.OrderPaymentType;
import com.dudungtak.seproject.enumpackage.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderGroupApiResponse {
    private Long id;

    private BigDecimal totalPrice;

    private LocalDateTime orderAt;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime updatedAt;

    private String updatedBy;

    private Long userId;

    private OrderStatus status;

    private String revAddress;

    private OrderPaymentType paymentType;

    private String comment;

    private String revName;

    private StyleApiResponse style;

    private List<OrderElementApiResponse> orderElementList;
}
