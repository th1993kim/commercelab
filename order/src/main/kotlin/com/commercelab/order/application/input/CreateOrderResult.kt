package com.commercelab.order.application.input

data class CreateOrderResult(
    val id: Long,
    val orderNumber: String,
    val totalAmount: Long,
    val items: List<CreateOrderItemResult>
) {
    data class CreateOrderItemResult(
        val productId: Long,
        val productName: String,
        val quantity: Int,
        val unitPrice: Long,
        val paymentAmount: Long
    )
}
