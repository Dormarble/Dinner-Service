package com.dudungtak.seproject.enumpackage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    PENDINGCONFIRM(0, "pendingConfirm", "주문 승인대기 상태"),
    STANDINGBYCOOKING(1, "standingByCooking", "조리대기 상태"),
    COOKING(2, "cooking", "조리 중"),
    STANDINGBYDELIVERY(3, "standingByDelivery", "배달대기 상태"),
    DELIVERY(4, "delivery", "배달 중"),
    DONE(5, "done", "주문완료 상태"),
    NOSTOCK(6, "noStock", "재고 없음으로 인한 취소 상태"),
    CANCELLED(7, "cancelled", "고객 취소 상태"),
    REJECTED(8, "rejected", "관리자 승인거부 상태");


    private Integer id;
    private String title;
    private String description;
}
