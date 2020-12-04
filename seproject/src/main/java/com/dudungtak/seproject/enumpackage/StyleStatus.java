package com.dudungtak.seproject.enumpackage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StyleStatus {
    REGISTERED(0, "registered", "등록 상태"),
    UNREGISTERED(1, "unregistered", "미등록 상태");

    private Integer id;
    private String title;
    private String description;
}
