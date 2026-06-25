package com.commercelab.order.domain.model

class OrderItem(

    var id: Long? = null,
    var order: Order? = null,
    var productId: Long,
    var productName: String,
    var unitPrice: Long,
    var quantity: Int,
    var paymentAmount: Long
)
