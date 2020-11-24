package com.dudungtak.seproject.enumpackage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    RECEIVED(0, "received", "주문접수 상태"),
    PENDINGCONFIRM(1, "pendingConfirm", "주문 승인대기 상태"),
    CONFIRMING(2, "confirming", "승인 중"),
    STANDINGBYCOOKING(3, "standingByCooking", "조리대기 상태"),
    COOKING(4, "cooking", "조리 중"),
    STANDINGBYDELIVERY(5, "standingByDelivery", "배달대기 상태"),
    DELIVERY(6, "delivery", "배달 중"),
    DONE(7, "done", "주문완료 상태"),
    NOSTOCK(8, "noStock", "재고 없음으로 인한 취소 상태"),
    CANCELLED(9, "cancelled", "고객 취소 상태"),
    REJECTED(10, "rejected", "관리자 승인거부 상태");


    private Integer id;
    private String title;
    private String description;
}
