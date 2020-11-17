package com.dudungtak.seproject.enumpackage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderPaymentType {
    CARD(0, "card", "카드 결제"),
    CASH(1, "cash", "현금 결제"),
    ACCOUNT(2, "account transfer", "현금 결제");

    private Integer id;
    private String title;
    private String description;
}
