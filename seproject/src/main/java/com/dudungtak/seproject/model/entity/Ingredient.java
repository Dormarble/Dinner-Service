package com.dudungtak.seproject.model.entity;

import com.dudungtak.seproject.model.enumpackage.IngredientStatus;
import com.dudungtak.seproject.model.enumpackage.IngredientType;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Accessors(chain = true)
@ToString(exclude = {"dishElementList"})
@EntityListeners(AuditingEntityListener.class)
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private IngredientType type;

    @Enumerated(EnumType.STRING)
    private IngredientStatus status;

    private BigDecimal cost;

    private Integer stock;

    private LocalDate registeredAt;

    private LocalDate unregisteredAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @LastModifiedBy
    private String updatedBy;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ingredient")
    private List<DishElement> dishElementList;
}
