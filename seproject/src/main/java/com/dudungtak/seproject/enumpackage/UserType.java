package com.dudungtak.seproject.enumpackage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserType {
    CUSTOMER(0, "customer", "고객"),
    COOK(1, "cook", "요리사"),
    DELIVERYMAN(2, "delivery man", "배달원"),
    MANAGER(3, "manager", "관리자");

    private Integer id;
    private String title;
    private String description;
}
