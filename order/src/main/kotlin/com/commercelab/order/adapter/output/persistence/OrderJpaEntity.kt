package com.commercelab.order.adapter.output.persistence

import com.commercelab.order.domain.model.Order
import com.commercelab.order.domain.model.OrderStatus
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "orders",
    uniqueConstraints = [
        UniqueConstraint(name = "uk_orders_order_number", columnNames = ["order_number"]),
        UniqueConstraint(name = "uk_orders_idempotency_key", columnNames = ["order_id", "idempotency_key"]),

    ])
class OrderJpaEntity(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    var id: Long? = null,

    @Column(nullable = false, length = 64)
    var orderNumber: String,

    @Column(nullable = false)
    var ordererId: String,

    @Column(nullable = false)
    var ordererName: String,

    var ordererEmail: String? = null,

    @Column(nullable = false)
    var status: OrderStatus,

    @Column(nullable = false)
    var totalAmount: Long,

    @Column(nullable = false, length = 64)
    var idempotencyKey: String,

    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "order",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    var items: MutableList<OrderItemJpaEntity> = mutableListOf(),

    @CreatedDate
    var createdAt: LocalDateTime? = null,

    @LastModifiedDate
    var updatedAt: LocalDateTime? = null
) {

    fun addItem(item: OrderItemJpaEntity) {
        item.order = this;
        items.add(item);
    }

    companion object {
        fun from(order: Order) : OrderJpaEntity {
            val orderEntity = OrderJpaEntity(
                orderNumber = order.orderNumber,
                ordererId = order.ordererId,
                ordererName = order.ordererName,
                ordererEmail = order.ordererEmail,
                status = order.status,
                totalAmount = order.totalAmount,
                idempotencyKey = order.idempotencyKey,
            )

            order.items.forEach { item -> orderEntity.items.add(OrderItemJpaEntity.from(item)) }

            return orderEntity;
        }
    }

    fun toDomain() : Order {
        return Order(
            id = requireNotNull(id),
            orderNumber = orderNumber,
            ordererId = ordererId,
            ordererName = ordererName,
            ordererEmail = ordererEmail,
            status = status,
            totalAmount = totalAmount,
            idempotencyKey = idempotencyKey,
            items = items.map{ item -> item.toDomain()}.toMutableList()
        )

    }
}
