package com.dudungtak.seproject.enumpackage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StaffJob {
    MANAGER(0, "manager", "관리자"),
    COOK(1, "cook", "주방"),
    DELIVERYMAN(2, "delivery man", "배달");

    private Integer id;
    private String title;
    private String description;
}
