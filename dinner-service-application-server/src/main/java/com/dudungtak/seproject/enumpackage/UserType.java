package com.dudungtak.seproject.enumpackage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserType {
    CUSTOMER(0, "customer", 1<<0, "고객"),
    COOK(1, "cook", 1<<1, "요리사"),
    DELIVERYMAN(2, "delivery man", 1<<2, "배달원"),
    MANAGER(3, "manager", 1<<3, "관리자");

    private Integer id;
    private String title;
    private Integer bitMap;
    private String description;
}
