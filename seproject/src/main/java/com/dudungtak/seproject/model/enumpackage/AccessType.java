package com.dudungtak.seproject.model.enumpackage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AccessType {
    CUSTOMER(0, "customer", 1<<0, "고객"),
    COOK(1, "cook", 1<<1, "요리사"),
    DELIVERYMAN(2, "delivery man", 1<<2, "배달원"),
    MANAGER(3, "manager", 1<<3, "관리자"),
    LOGINEDALL(4, "logined all", (1<<0) + (1<<1) + (1<<2) + (1<<3), "로그인한 모든 사용자"),
    UNLOGINED(5, "unlogin user", 0, "로그인하지 않은 사용자"),
    ALL(6, "all", 0, "모든 사용자");

    private Integer id;
    private String title;
    private Integer bitMap;
    private String description;
}
