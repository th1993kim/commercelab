package com.commercelab.order.application.input

data class CreateOrderCommand(
    val ordererId: String,
    val ordererName: String,
    val ordererEmail: String?,
    val totalAmount: Long,
    val idempotencyKey: String,
    val items: List<CreateOrderItemCommand>
) {
    data class CreateOrderItemCommand(
        val productId: Long,
        val quantity: Int
    )
}
