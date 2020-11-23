package com.dudungtak.seproject.network.request;

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
public class OrderGroupApiRequest {
    private Long id;

    private BigDecimal totalPrice;

    private LocalDateTime orderAt;

    private Long userId;

    private OrderStatus status;

    private String revAddress;

    private OrderPaymentType paymentType;

    private String comment;

    private String revName;

    private StyleApiRequest style;

    private List<OrderElementApiRequest> orderElementList;
}
