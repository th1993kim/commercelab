package com.commercelab.order.domain.model

enum class OrderStatus(
    val description: String
) {
    CREATED("주문 생성"),
    CONFIRMED("주문 완료"),
    CANCEL_REQUESTED("취소 요청"),
    CANCELED("취소"),
    FAILED("주문 실패")
}