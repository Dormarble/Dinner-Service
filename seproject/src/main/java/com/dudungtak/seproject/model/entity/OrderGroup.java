package com.dudungtak.seproject.model.entity;

import com.dudungtak.seproject.model.enumpackage.OrderPaymentType;
import com.dudungtak.seproject.model.enumpackage.OrderStatus;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Accessors(chain = true)
@ToString(exclude = {"user", "style", "orderElementList"})
@EntityListeners(AuditingEntityListener.class)
public class OrderGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime orderAt;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String revAddress;

    private String revName;

    @Enumerated(EnumType.STRING)
    private OrderPaymentType paymentType;

    private BigDecimal totalPrice;

    private String comment;

    @CreatedDate
    private LocalDateTime createdAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @LastModifiedBy
    private String updatedBy;

    @ManyToOne
    private User user;

    @ManyToOne
    private Style style;

    @OneToOne
    private User cook;

    @OneToOne
    private User deliveryMan;

    private LocalDateTime cookAt;

    private LocalDateTime deliveryAt;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "orderGroup")
    private List<OrderElement> orderElementList;
}
