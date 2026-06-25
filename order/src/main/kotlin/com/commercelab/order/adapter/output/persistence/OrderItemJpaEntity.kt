package com.commercelab.order.adapter.output.persistence

import com.commercelab.order.domain.model.OrderItem
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "order_item")
class OrderItemJpaEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    var order: OrderJpaEntity? = null,

    @Column(nullable = false)
    var productId: Long,

    @Column(nullable = false, length = 200)
    var productName: String,

    @Column(nullable = false)
    var unitPrice: Long,

    @Column(nullable = false)
    var quantity: Int,

    @Column(nullable = false)
    var paymentAmount: Long
) {
    companion object {
        fun from(orderItem: OrderItem) = OrderItemJpaEntity(
                productId = orderItem.productId,
                productName = orderItem.productName,
                unitPrice = orderItem.unitPrice,
                quantity = orderItem.quantity,
                paymentAmount = orderItem.paymentAmount
        )
    }
}
