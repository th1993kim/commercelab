package com.commercelab.order.domain.model

class Order(

    var id: Long? = null,
    var orderNumber: String,
    var ordererId: String,
    var ordererName: String,
    var ordererEmail: String? = null,
    var status: OrderStatus,
    var totalAmount: Long,
    var idempotencyKey: String,
    var items: MutableList<OrderItem>
) {

}
